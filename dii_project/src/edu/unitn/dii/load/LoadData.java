package edu.unitn.dii.load;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.hadoop.hbase.client.HTable;

import edu.unitn.dii.yelp.Business;
import edu.unitn.dii.yelp.FullAddress;
import edu.unitn.dii.yelp.Review;
import edu.unitn.dii.yelp.Yelp;
import edu.unitn.dii.foursquare.TrentinoTips;
import edu.unitn.dii.hbase.DBManager;
import fi.foyt.foursquare.api.entities.Category;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.Location;

/**
 * Combine the data retrieved from Foursquare and Yelp.
 * Load the data into HBASE
 * 
 * @author user
 *
 */
public class LoadData {
	 
	private TreeMap <Business, Integer> data = new TreeMap <Business, Integer>();
	
	
	/**
	 * Combine the data retrieved from Foursquare and Yelp before loading to HBASE.
	 * When a business from Foursquare matches with a business from yelp
	 * than the data is merged and represented by single object.  
	 * 
	 * @param yelp_serialization_file
	 * @param fsqr_serialization_file
	 */
	public void combineDataFourSquareYelp(String yelp_serialization_file, String fsqr_serialization_file){	
		
		TrentinoTips tt=null;
		Yelp yelp = null;
		try {
			tt= TrentinoTips.deserialiseTips(yelp_serialization_file);
			yelp= Yelp.deserialiseYelpData(fsqr_serialization_file);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Get the list of businesses retrieved from Yelp and Foursquare and convert them to arraylist 
		//this for simplicity of manipulation 
		TreeMap<Business,Integer> yelp_businesses_rep = yelp.getYelp_results();	
		ArrayList<Business> yelpBusinesses = new ArrayList<Business>(yelp_businesses_rep.keySet());
		ArrayList<Business> foursquareBusinesses= new ArrayList<Business>(tt.getFoursquare_businesses());	
		
		//Sort them to alphabetical order. Supposedly this will decrease
		//the complexity when trying to find the matches.
		Collections.sort(foursquareBusinesses,new SortByBusinessName());
		Collections.sort(yelpBusinesses,new SortByBusinessName());
		
		System.out.println("Number of copied foursquare venues: "+foursquareBusinesses.size());
		System.out.println("Number of yelp businesses: "+yelpBusinesses.size());
		System.out.println("Total number of businesses before combination: "
								+(yelp_businesses_rep.size()+foursquareBusinesses.size()));
		
		//create a TreeMap with the data from Foursquare.
		//We will use this TreeMap to mark the business 
		//that have a match with a yelp business. 
		Iterator<Business> vbsitr  =  foursquareBusinesses.iterator();
		TreeMap<Business,Integer> matching_foursquare = new TreeMap<Business,Integer>();
		while(vbsitr.hasNext()){
			matching_foursquare.put(vbsitr.next(),0);
		}
		
		//Two while loops.The first one iterating on the yelp list of businesses,
		//and the second on the foursquare list of businesses;
		//The function compareBusinesses will do the comparison and return 0,1,-1.
		//If the result is zero there is a match. 
		int accepted_matches=0, total_matches=0;
		boolean match=false;
		Iterator<Business> bit = yelpBusinesses.iterator();
		while(bit.hasNext()){	
			Business a = bit.next();			
			int r = -1;
			match=false;
			Iterator<Business> vbit  =  foursquareBusinesses.iterator();
			while(vbit.hasNext()){				
				Business b =vbit.next();
				r= compareBusinesses(a, b);
				if(r==0){	
				
				int val= matching_foursquare.get(b);
				if(val<1){
					
					matching_foursquare.put(b, val+1);					
					
					//Merge the data from business b and business a.
					
					if(b.getUrl()!=null && b.getUrl().compareTo("null")!=0 &&
							b.getUrl().compareTo("")!=0 ){
						a.setUrl(b.getUrl());
					}
					
					a.setTips_count(b.getTips_count());
					a.setUsers_count(b.getUsers_count());
					a.setCheckins_count(b.getCheckins_count());
					
					if(b.getCategories()!=null && b.getCategories().size()>0)
					a.getCategories().addAll(b.getCategories());
					
					if(b.getReviews()!=null && b.getReviews().size()>0)
					a.getReviews().addAll(b.getReviews());
					this.data.put(a, 1);
					
					accepted_matches++;
//					matching_foursquare.put(b, 1);
//					System.out.println("\n==============================================================");
//					System.out.println("PHONE "+a.getPhone()+"\t\t"+b.getPhone());
//					System.out.println("URL "+a.getUrl()+"\t\t"+b.getUrl());
//					System.out.println("CITY "+a.getFullAddress().getCity()+"\t\t"+b.getFullAddress().getCity());
//					System.out.println("POSTAL CODE "+a.getFullAddress().getPostal_code()+"\t\t"+b.getFullAddress().getPostal_code());
//					System.out.println("CITY "+a.getFullAddress().getAddress()+"\t\t"+b.getFullAddress().getAddress());
//					Iterator<Review> rit = a.getReviews().iterator();
//					int l=1;
//					while(rit.hasNext()){
//						System.out.println(l+". "+rit.next().getText());
//						l++;
//					}
//					System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
				  }
				  total_matches++;
				  break;
			   }			
		    }
			
			if(match){
				match=false;
			}
			if(!match){
				this.data.put(a, 0);
			}
//			System.out.println("NAME "+a.getName());
//			System.out.println("PHONE "+a.getPhone());
//			System.out.println("URL "+a.getUrl());
//			System.out.println("CITY "+a.getFullAddress().getCity());
//			System.out.println("POSTAL CODE "+a.getFullAddress().getPostal_code());
//			System.out.println("CITY "+a.getFullAddress().getAddress());
//			System.out.println("");
		}
		System.out.println("\n");
		
		//Sort the list according to the number of matches for each foursquare business
		//Consider only those that do not have a match (number of matches 0)
		//Add these businesses to the final combined data.
		FoursquareMatchingBusinessesComparator vmc = new FoursquareMatchingBusinessesComparator(matching_foursquare);
		TreeMap<Business, Integer> sorted_venue_matches = new TreeMap<Business, Integer>(vmc);
		sorted_venue_matches.putAll(matching_foursquare);
		System.out.println("Number of accepted matches: "+accepted_matches);
		System.out.println("Total number of matches: "+total_matches);
		System.out.println("Number of elements in sorted_venue_matches :"+ sorted_venue_matches.size());
		System.out.println("Number of elements in data before total merge:"+ this.data.size());

		while(!sorted_venue_matches.isEmpty()){
			
			Entry<Business,Integer> mv=sorted_venue_matches.pollLastEntry();
			if(mv.getValue()>0){
				break;
			}
			
			Business b = new  Business();
			b=mv.getKey();
//			String new_id="";
//			if(b.getName()!=null && b.getName()!="null" && b.getName()!=""){
//				new_id= b.getName().toLowerCase().replaceAll("\\s","-");
//				b.setBid(new_id);
//			}else{
//				b.setName("Unknown");
//			}
////			System.out.println(b.getBid());
			
			this.data.put(b,0);
//			System.out.println("NAME "+mv.getKey().getName());
//			System.out.println("PHONE "+mv.getKey().getPhone());
//			System.out.println("URL "+mv.getKey().getUrl());
//			System.out.println("CITY "+mv.getKey().getFullAddress().getCity());
//			System.out.println("POSTAL CODE "+mv.getKey().getFullAddress().getPostal_code());
//			System.out.println("CITY "+mv.getKey().getFullAddress().getAddress());
//			System.out.println("");
			
		}
//		System.out.println("Size of foursquare matches after postprocessing: "+sorted_venue_matches.size());	
		System.out.println("Total Number of combined businesses : "+this.data.size());	
//		ArrayList<Business> sorted_merged_businesses = new ArrayList<Business>(this.data.keySet());
//		Collections.sort(sorted_merged_businesses, new SortByBusinessName());
//		Iterator<Business> itr = sorted_merged_businesses.iterator();
//		System.out.println("\n\nAll combined businesses sorted in asc order: ");
//		while(itr.hasNext()){	
//			Business business = new Business();
//			business=itr.next();
//			
////			System.out.println("\n=====================================================================");
//			System.out.println(""+business.getName());
////			System.out.println("PHONE "+business.getPhone());
////			System.out.println("URL "+business.getUrl());
////			System.out.println("CITY "+business.getFullAddress().getCity());
////			System.out.println("POSTAL CODE "+business.getFullAddress().getPostal_code());
////			System.out.println("CITY "+business.getFullAddress().getAddress());
////			Iterator<Review> rit = business.getReviews().iterator();
////			int l=1;
////			while(rit.hasNext()){
////				System.out.println(l+". "+rit.next().getText());
////				l++;
////			}
////			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//		}
	}
	
		
	/**
	 * Compare businesses based on their name, phone, address, city and postal code.
	 * Evaluate the result to determine if the businesses match.
	 * Depending on the case a score 3 or higher gives a positive result (a match) 
	 * Return 0 for match and other number for non match.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public int compareBusinesses(Business a, Business b) {
        
    	int eval=0; 
    	String str1="", str2="";
    	boolean matchNames =false;
    	 	  	
    	if(		a.getName()!=null && b.getName()!=null &&
    			b.getName().compareToIgnoreCase("null")!=0 && 
    		    a.getName().compareToIgnoreCase("null")!=0 &&
    		    a.getName().compareTo("")!=0 && b.getName().compareTo("")!=0 ){
    			str1 = a.getName().toLowerCase().replaceAll("\\s","");
    			str2 = b.getName().toLowerCase().replaceAll("\\s","");
    			
    			String[] specialWords={"bar","ristorante","pizzeria","caff�","caffe","caffe'",
    					"pasticceria","studio","supermercato","associazione","trattoria",
    					"antica","antico"};
    			
    			int i=0;
    			
    			while(i<specialWords.length){
    				str1.replaceAll(specialWords[i], "");
    				str1.replaceAll(specialWords[i], "");
    				i++;
    			}
    			
    			if(str1.compareTo("")!=0 && str2.compareTo("")!=0){
	    			
    				if((str1.compareTo(str2)==0)){
	    						eval=eval+2;
	    						matchNames=true;
	    			}else if(str1.indexOf(str2)>=0 || str2.indexOf(str1)>=0){	
	    						eval=eval+1;
	    			}
    			}
    	}
    	
    	if(		a.getPhone()!=null && b.getPhone()!=null &&
    			b.getPhone().compareToIgnoreCase("null")!=0 && 
    		    a.getPhone().compareToIgnoreCase("null")!=0 &&
    		    a.getPhone().compareTo("")!=0 && b.getPhone().compareTo("")!=0){
    			str1 =a.getPhone();
    			str2 = b.getPhone(); 
	    		str1 = str1.replaceAll("\\s","");
	    		str1 = str1.replaceAll("\\+?", "");
	    		str1 = str1.replaceAll("-", "");
	    		str2 = str2.replaceAll("\\s","");
	    		str2 = str2.replaceAll("\\+?", "");
	    		str2 = str2.replaceAll("-", "");
	    		
				if(str1.compareTo(str2)==0){
					eval=eval+3;
				}
    	}
    	
    	if (eval>3){
//    		System.out.println("\nMATCH FIRST: "+a.getName()+"\t|\t"+b.getName());
    		return 0;  		
    	}
    	
    	if(eval==0){	    		

    		if(a.getName()!=null && b.getName()!=null &&
	    	   b.getName().compareToIgnoreCase("null")!=0 && 
	    	   a.getName().compareToIgnoreCase("null")!=0 &&
	    	   a.getName().compareTo("")!=0 && b.getName().compareTo("")!=0 ){ 
	    			
    				if(a.getName().compareToIgnoreCase(b.getName())==0){
//    					System.out.println("NO GENERAL MATCH BUT");
//    					System.out.println("NAME "+a.getName()+"\t\t"+b.getName());
//    					System.out.println("PHONE "+a.getPhone()+"\t\t"+b.getPhone());
//    					System.out.println("URL "+a.getUrl()+"\t\t"+b.getUrl());
//    					System.out.println("CITY "+a.getFullAddress().getCity()+"\t\t"+b.getFullAddress().getCity());
//    					System.out.println("POSTAL CODE "+a.getFullAddress().getPostal_code()+"\t\t"+b.getFullAddress().getPostal_code());
//    					System.out.println("CITY "+a.getFullAddress().getAddress()+"\t\t"+b.getFullAddress().getAddress());
//    					System.out.println("");
    					return a.getBid().compareToIgnoreCase(b.getBid());
    				}
    				return a.getName().compareToIgnoreCase(b.getName());

	    	}else{
		    		int rez =a.getBid().compareTo(b.getBid());
	    			if(rez==0)
	    			System.out.println("Comparison between the bid is zero");
					return rez;
		    }
    	}
    		
    	FullAddress al = a.getFullAddress();
    	FullAddress bl = b.getFullAddress();
    	
    	if(al!=null && bl!=null){
    		
	    	if(		al.getCity()!=null && bl.getCity()!=null && 
	    			bl.getCity().compareToIgnoreCase("unknown")!=0 && 
	    		    al.getCity().compareToIgnoreCase("unknown")!=0 &&
	    			bl.getCity().compareToIgnoreCase("null")!=0 && 
	    		    al.getCity().compareToIgnoreCase("null")!=0 &&
	    		    al.getCity().compareTo("")!=0 && bl.getCity().compareTo("")!=0){
		    		
	    			str1 = al.getCity().toLowerCase().replaceAll("\\s","");
	    			str2 = bl.getCity().toLowerCase().replaceAll("\\s","");
	    			
	    			
	    			if(str1.compareTo(str2)==0 || LevenshteinDistance.computeDistance(str1, str2)<=2){
	    				eval=eval+1;
	    			}
	    			else if( al.getState_code()!=null && al.getState_code().compareTo("")!=0 && 
	    					 al.getState_code().compareTo("null")!=0){
	    				String sc= al.getState_code().toLowerCase().replaceAll("\\s","");
	    				if(sc.compareTo(str2)==0){
	    					eval=eval+1;
	    				}
	    			}
	    	}		    	
	    	
	    	if(		al.getPostal_code()!=null && bl.getPostal_code()!=null &&
	    			bl.getPostal_code().compareToIgnoreCase("null")!=0 && 
	    		    al.getPostal_code().compareToIgnoreCase("null")!=0 &&
	    		    al.getPostal_code().compareTo("")!=0 && bl.getPostal_code().compareTo("")!=0 ){
	    			str1 = al.getPostal_code().trim();
					str2 = bl.getPostal_code().trim();
			
	    			if(str1.compareTo(str2)==0){
	    				eval=eval+1;
	    			}
	    	}
	    	
	    	if(		al.getAddress()!=null && bl.getAddress()!=null &&
	    			bl.getAddress().compareToIgnoreCase("null")!=0 && 
	    		    al.getAddress().compareToIgnoreCase("null")!=0 &&
	    		    al.getAddress().compareTo("")!=0 && bl.getAddress().compareTo("")!=0 ){
	    		    	
	    			str1 = al.getAddress().toLowerCase().replaceAll("\\s","");
	    			str2 = bl.getAddress().toLowerCase().replaceAll("\\s","");
	    			
	    			str1 = str1.replaceAll("via","");
	    			str2 = str2.replaceAll("via","");
	    			
	    			if(str1.compareTo(str2)==0 || str1.indexOf(str2)>=0 || str2.indexOf(str1)>=0){
	    				eval=eval+1;
	    			}
	    	}
    	}
    
    	if((eval >=3 && matchNames) || eval>=4 ){
//    		System.out.println("\nMATCH SECOND: "+a.getName()+"\t|\t"+b.getName());
    		return 0;
    	}else{  
    		
			if(a.getName()!=null && b.getName()!=null &&
	    	   b.getName().compareToIgnoreCase("null")!=0 && 
	    	   a.getName().compareToIgnoreCase("null")!=0 &&
	    	   a.getName().compareTo("")!=0 && b.getName().compareTo("")!=0){ 
	    			
    				if(a.getName().compareToIgnoreCase(b.getName())==0){
//    					System.out.println("NO GENERAL MATCHING BUT");
//    					System.out.println("NAME "+a.getName()+"\t\t"+b.getName());
//    					System.out.println("PHONE "+a.getPhone()+"\t\t"+b.getPhone());
//    					System.out.println("URL "+a.getUrl()+"\t\t"+b.getUrl());
//    					System.out.println("CITY "+a.getFullAddress().getCity()+"\t\t"+b.getFullAddress().getCity());
//    					System.out.println("POSTAL CODE "+a.getFullAddress().getPostal_code()+"\t\t"+b.getFullAddress().getPostal_code());
//    					System.out.println("CITY "+a.getFullAddress().getAddress()+"\t\t"+b.getFullAddress().getAddress());
//    					System.out.println("");
    					return a.getBid().compareToIgnoreCase(b.getBid());
    				}
    				return a.getName().compareToIgnoreCase(b.getName());

	    	}else{
		    		int rez =a.getBid().compareTo(b.getBid());
	    			if(rez==0)
	    			System.out.println("COMPARISON WITH BID IS ZERO BUT NO MATCH");
					return rez;
		    }
    	}	 
    }	
	
	
	/**
	 * @author user
	 * 
	 * This class is used to compare the businesses based on the number of matches,
	 * which id represented by the value in the Map.
	 * 
	 */
	class FoursquareMatchingBusinessesComparator implements Comparator<Business> , java.io.Serializable{
		private static final long serialVersionUID = 1L;
		private Map<Business,Integer> base;
		
		public FoursquareMatchingBusinessesComparator(Map<Business, Integer> map){
			this.base =map;
		}
		
		public int compare(Business a, Business b) {
			
			if (base.get(a) >= base.get(b)) {
	            return -1;
	        } else {
	            return  1;
	        }
		}
	}
	
	/**
	 * Insert the combined data into HBASE
	 * 
	 * @param tableName
	 */
	public void insertData(String tableName){
		
		DBManager dbmgr= DBManager.getInstance();
		
		HTable table = dbmgr.createOrGetSchema(tableName);
		
		ArrayList<Business> business = new ArrayList<Business>(this.data.keySet());
		try {
			dbmgr.insertDataToHbase(table, business);
		} catch (IOException e) {
			
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Compare the businesses based on their name. 
	 * This comparator class will be used to sort
	 * the businesses lexicographically.
	 * 
	 * @author user
	 *
	 */
	class SortByBusinessName implements Comparator<Business> , java.io.Serializable{
		private static final long serialVersionUID = 1L;
		public int compare(Business a, Business b) {
			
			return a.getName().compareTo(b.getName());
		}
	}
	
	public static void main(String args[]){	
		LoadData ld = new LoadData();
		ld.combineDataFourSquareYelp("./storage/foursquare_output.ser","./storage/yelp_output.ser");	
		ld.insertData("BusinessReview");
	}
}
