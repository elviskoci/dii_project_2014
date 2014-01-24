package edu.unitn.dii.load;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import edu.unitn.dii.yelp.Business;
import edu.unitn.dii.yelp.FullAddress;
import edu.unitn.dii.yelp.Review;
import edu.unitn.dii.yelp.Yelp;
import edu.unitn.dii.foursquare.TrentinoTips;
import fi.foyt.foursquare.api.entities.Category;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.Location;

public class LoadData {
	 
	private TreeMap <Business, Integer> data = new TreeMap <Business, Integer>();
	
	public void combineDataFourSquareYelp(){	
		TrentinoTips tt=null;
		Yelp yelp = null;
		try {
			tt= TrentinoTips.deserialiseTips("./storage/foursquare_output.ser");
			yelp= Yelp.deserialiseYelpData("./storage/yelp_output_tn.ser");
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		TreeMap<CompactVenue,Integer> foursquare_venues_rep=tt.getUniqueVenues();
		TreeMap<Business,Integer> yelp_businesses_rep = yelp.getYelp_results();
		
		ArrayList<Business> foursquareBusinesses= new ArrayList<Business>();
		Iterator<CompactVenue> vit = foursquare_venues_rep.keySet().iterator();
		System.out.println("Number of initial number of foursquare venues: "+foursquare_venues_rep.size());
		
		while(vit.hasNext()){	
					
			CompactVenue venue = new CompactVenue();
			venue = vit.next();
//			if (venue.getName().indexOf("Grotta")>0){
//				System.out.println("NAME " +venue.getName());
//				System.out.println("PHONE "+venue.getContact().getPhone());
//				System.out.println("URL "+venue.getUrl());
//				System.out.println("CITY "+venue.getLocation().getCity());
//				System.out.println("POSTAL CODE "+venue.getLocation().getPostalCode());
//				System.out.println("CITY "+venue.getLocation().getAddress());
//				System.out.println("");
//			}
//			
			Business business = new Business();
			business.setBid(venue.getId());
			business.setName(venue.getName());
			business.setUrl(venue.getUrl());
			
			if(venue.getContact()!=null)
			business.setPhone(venue.getContact().getPhone());
			
			Category[] categories= venue.getCategories();
			ArrayList<String> catgrs_list = new ArrayList<String>();
			if(categories!=null){
				for(int i=0;i<categories.length;i++){
					catgrs_list.add(categories[0].getName());
				}
			}
			
			business.setCategories(catgrs_list);
			
			if(venue.getStats()!=null){
				business.setCheckins_count(venue.getStats().getCheckinsCount());
				business.setUsers_count(venue.getStats().getUsersCount());
			}
		
			if(venue.getTips()!=null){
				business.setTips_count(venue.getTips().getCount());
			}	
			
			Location location = venue.getLocation();
			FullAddress loc = new FullAddress();
			
			if(location!=null){
				loc=new FullAddress(location.getAddress(),location.getCity()
						,location.getState(), location.getCountry(),
						location.getPostalCode(),location.getLat(),
						location.getLng());
			}
			
			business.setFullAddress(loc);
			foursquareBusinesses.add(business);
			
//			ArrayList<Review> review = new ArrayList<Review>();
		}
	
		System.out.println("Number of copied venues: "+foursquareBusinesses.size());
		System.out.println("Number of businesses: "+yelp_businesses_rep.size());
		System.out.println("Total number of businesses before combination: "
								+(yelp_businesses_rep.size()+foursquareBusinesses.size()));
		
		ArrayList<Business> yelpBusinesses = new ArrayList<Business>(yelp_businesses_rep.keySet());
		Collections.sort(foursquareBusinesses,new SortByBusinessName());
		Collections.sort(yelpBusinesses,new SortByBusinessName());
		
		System.out.println("Size of yelpBusinesses: "+yelpBusinesses.size());
		System.out.println("Size of venueToBusiness: "+foursquareBusinesses.size());
		
		Iterator<Business> vbsitr  =  foursquareBusinesses.iterator();
		TreeMap<Business,Integer> matching_foursquare = new TreeMap<Business,Integer>();
		while(vbsitr.hasNext()){
			matching_foursquare.put(vbsitr.next(),0);
		}
		
		System.out.println("\n");
		System.out.println("Number of elements in data before initial merge:"+ this.data.size());
		
		int matches_counter=0;
		Iterator<Business> bit = yelpBusinesses.iterator();
		while(bit.hasNext()){	
			Business a = bit.next();
			
//			if (a.getName().indexOf("Alla Grotta")>0){
//				System.out.println("NAME " +a.getName());
//				System.out.println("PHONE "+a.getPhone());
//				System.out.println("URL "+a.getUrl());
//				System.out.println("CITY "+a.getFullAddress().getCity());
//				System.out.println("POSTAL CODE "+a.getFullAddress().getPostal_code());
//				System.out.println("CITY "+a.getFullAddress().getAddress());
//				System.out.println("");
//			}
			
			Iterator<Business> vbit  =  foursquareBusinesses.iterator();
			while(vbit.hasNext()){				
				Business b =vbit.next();
				int r = -1;
				r= compareBusinesses(a, b);
				if(r==0){	
					matches_counter++;
					
					if(this.data.containsKey(a)){
						int val1= this.data.get(a);
						
						if(b.getUrl()!=null && b.getUrl().compareTo("null")!=0 &&
								b.getUrl().compareTo("")!=0 ){
							a.setUrl(b.getUrl());
						}
						
						a.setTips_count(b.getTips_count());
						a.setUsers_count(b.getUsers_count());
						a.setCheckins_count(b.getCheckins_count());
						
						if(b.getCategories()!=null && b.getCategories().size()>0)
						a.getCategories().addAll(b.getCategories());
						
						this.data.put(a, val1+1);
					}else{
						this.data.put(a, 1);
					}
					
					int val2 = matching_foursquare.get(b);
					matching_foursquare.put(b, val2+1);
					
					System.out.println("PHONE "+a.getPhone()+"\t\t"+b.getPhone());
					System.out.println("URL "+a.getUrl()+"\t\t"+b.getUrl());
					System.out.println("CITY "+a.getFullAddress().getCity()+"\t\t"+b.getFullAddress().getCity());
					System.out.println("POSTAL CODE "+a.getFullAddress().getPostal_code()+"\t\t"+b.getFullAddress().getPostal_code());
					System.out.println("CITY "+a.getFullAddress().getAddress()+"\t\t"+b.getFullAddress().getAddress());
					System.out.println("");
				}			
		    }	
			this.data.put(a, 0);
//			System.out.println("NAME "+a.getName());
//			System.out.println("PHONE "+a.getPhone());
//			System.out.println("URL "+a.getUrl());
//			System.out.println("CITY "+a.getFullAddress().getCity());
//			System.out.println("POSTAL CODE "+a.getFullAddress().getPostal_code());
//			System.out.println("CITY "+a.getFullAddress().getAddress());
//			System.out.println("");
		}
		System.out.println("\n");
		
		FoursquareMatchingBusinessesComparator vmc = new FoursquareMatchingBusinessesComparator(matching_foursquare);
		TreeMap<Business, Integer> sorted_venue_matches = new TreeMap<Business, Integer>(vmc);
		sorted_venue_matches.putAll(matching_foursquare);
		System.out.println("Total Number of matches: "+matches_counter);
		System.out.println("Number of elements in sorted_venue_matches :"+ sorted_venue_matches.size());
		System.out.println("Number of elements in data before total merge:"+ this.data.size());

		while(!sorted_venue_matches.isEmpty()){
			
			Entry<Business,Integer> mv=sorted_venue_matches.pollLastEntry();
			if(mv.getValue()>0){
				break;
			}
			this.data.put(mv.getKey(),0);
//			System.out.println("NAME "+mv.getKey().getName());
//			System.out.println("PHONE "+mv.getKey().getPhone());
//			System.out.println("URL "+mv.getKey().getUrl());
//			System.out.println("CITY "+mv.getKey().getFullAddress().getCity());
//			System.out.println("POSTAL CODE "+mv.getKey().getFullAddress().getPostal_code());
//			System.out.println("CITY "+mv.getKey().getFullAddress().getAddress());
//			System.out.println("");
			
		}
		
		System.out.println("Total Number of combined businesses : "+this.data.size());
				
		ArrayList<Business> sorted_merged_businesses = new ArrayList<>(this.data.keySet());
		Collections.sort(sorted_merged_businesses, new SortByBusinessName());
		Iterator<Business> itr = sorted_merged_businesses.iterator();
		System.out.println("\n\nAll combined businesses sorted in asc order: ");
		while(itr.hasNext()){	
			System.out.println(""+itr.next().getName());
		}
	}
	
		
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
    			
    			String[] specialWords={"bar","ristorante","pizzeria","caffé","caffe","caffe'",
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
    		System.out.println("MATCH FIRST: "+a.getName()+"\t|\t"+b.getName());
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
    		System.out.println("MATCH SECOND: "+a.getName()+"\t|\t"+b.getName());
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
			
//    		}else if(a.getPhone()!=null && b.getPhone()!=null &&
//	    			a.getPhone().compareTo("")!=0 && b.getPhone().compareTo("")!=0 &&
//	    			a.getPhone().compareToIgnoreCase("null")!=0 && 
//	    			b.getPhone().compareToIgnoreCase("null")!=0){
//	    			
//    				if(a.getPhone().compareToIgnoreCase(b.getPhone())==0){
//    					System.out.println("MATCH PHONE "+a.getPhone()+"\t|\t"+b.getPhone());
//    				}
//	    			return a.getPhone().compareToIgnoreCase(b.getPhone());
//	    			
//	    	}else if(a.getUrl()!=null && b.getUrl()!=null &&
//	    			a.getUrl().compareTo("")!=0 && b.getUrl().compareTo("")!=0 &&
//	    			a.getUrl().compareToIgnoreCase("null")!=0 && 
//	    			b.getUrl().compareToIgnoreCase("null")!=0){ 
//	    			 
//	    			if(a.getUrl().compareToIgnoreCase(b.getUrl())==0){
//	    				System.out.println("MATCH URL "+a.getUrl()+"\t|\t"+b.getUrl());
//	    			}
//	    			return a.getUrl().compareToIgnoreCase(b.getUrl());
    	}	 
    }	
	
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
	
	class SortByBusinessName implements Comparator<Business> , java.io.Serializable{
		private static final long serialVersionUID = 1L;

		public int compare(Business a, Business b) {
			
			return a.getName().compareTo(b.getName());
		}
	}
	
	public static void main(String args[]){	
		LoadData ld = new LoadData();
		ld.combineDataFourSquareYelp();
		
	}
}
