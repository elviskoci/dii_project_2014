package edu.unitn.dii.load;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import edu.unitn.dii.yelp.Business;
import edu.unitn.dii.yelp.FullAddress;
import edu.unitn.dii.yelp.Yelp;
import edu.unitn.dii.foursquare.TrentinoTips;
import fi.foyt.foursquare.api.entities.Category;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.CompleteTip;
import fi.foyt.foursquare.api.entities.Location;

public class LoadData {
	
	private ArrayList <Business> data = new ArrayList <Business>();
	
	public void combineDataFourSquareYelp(){	
		TrentinoTips tt=null;
		Yelp yelp = null;
		try {
			tt= TrentinoTips.deserialiseTips("./storage/foursquare_output.ser");
			yelp= Yelp.deserialiseYelpData("./storage/yelp_output.ser");
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		TreeMap<CompactVenue,Integer> venues_rep=tt.getUniqueVenues();
		TreeMap<Business,Integer> businesses_rep = yelp.getYelp_results();
		//TreeMap<Business,Integer> venuesToBusiness= new TreeMap<Business, Integer>();
		ArrayList<Business> venuesToBusiness= new ArrayList<Business>();
		
		System.out.println("Number of initial venues: "+venues_rep.size());
		
		Set<CompactVenue> venues =venues_rep.keySet();
		Iterator<CompactVenue> vit = venues.iterator();
		
		while(vit.hasNext()){	
					
			CompactVenue venue = new CompactVenue();
			venue = vit.next();
			
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
			venuesToBusiness.add(business);
		}
	
		System.out.println("Number of copied venues: "+venuesToBusiness.size());
		System.out.println("Number of businesses: "+businesses_rep.size());
		System.out.println("Total number of businesses before combination: "
								+(businesses_rep.size()+venuesToBusiness.size()));
			
		Set<Business> business_set = businesses_rep.keySet();
		Iterator<Business> bit  = business_set.iterator();
						
		Iterator<Business> vbsitr =venuesToBusiness.iterator();
		HashMap<Business,Integer> matching_venues = new HashMap<Business,Integer>();
		
		while(vbsitr.hasNext()){
			matching_venues.put(vbsitr.next(),0);
		}
		
		System.out.println("Number of elements in matching_veneues hash map: "+matching_venues.size());
		//System.out.println("matching_veneues: "+matching_venues);
		
		while(bit.hasNext()){	
			Business a = bit.next();
			Iterator<Business> vbit  =  venuesToBusiness.iterator();
			while(vbit.hasNext()){				
				Business b =vbit.next();
				int r =compareBusinesses(a, b);
				
				if(r==0){	
					//when the businesses match needs to be implemented
					//from where do we get the data
					if(!this.data.contains(a)){
					
						this.data.add(a);					
					}
					int val = matching_venues.get(b);
					matching_venues.put(b, val+1);
					
				}			
		    }	
			this.data.add(a);
		}
		
		System.out.println("Number of combined businesses before including non matching foursquare : "+this.data.size());
		
		VenueMatchingsComparator vmc = new VenueMatchingsComparator(matching_venues);
		TreeMap<Business, Integer> sorted_venue_matches = new TreeMap<Business, Integer>(vmc);
		sorted_venue_matches.putAll(matching_venues);
		System.out.println("Number of elements in  sorted_matches :"+ sorted_venue_matches.size());
		System.out.println(""+sorted_venue_matches);

		while(!sorted_venue_matches.isEmpty()){
			
			Entry<Business,Integer> mv=sorted_venue_matches.pollLastEntry();
			
			if(mv.getValue()>0){
				break;
			}
			this.data.add(mv.getKey());
		}
		
		System.out.println("Number of combined businesses : "+this.data.size());
		
		Collections.sort(this.data, new SortData());
		
		Iterator<Business> itr = this.data.iterator();
		System.out.println("\n\nAll combined businesses sorted in asc order: ");
		while(itr.hasNext()){
			
			System.out.println(""+itr.next().getName());
		}		
	}
	
		
	public int compareBusinesses(Business a, Business b) {
        
    	int eval=0; 
    	String str1="", str2="";
    	 	  	
    	if(		a.getName()!=null && b.getName()!=null &&
    			a.getName().compareTo("")!=0 && b.getName().compareTo("")!=0 ){ 
    			
    			str1 = a.getName().toLowerCase().replaceAll("\\s","");
    			str2 = b.getName().toLowerCase().replaceAll("\\s","");
    			
    			boolean isSpecialWord =false;
    			
    			String[] specialWords={"bar","ristorante","pizzeria","caffé","disco","pasticceria"};
    			
    			int i=0;
    			
    			while(i<specialWords.length){
    				if(str1.compareTo(specialWords[i])==0){
    					isSpecialWord=true;
    					break;
    				}
    				
    				if(str2.compareTo(specialWords[i])==0){
    					isSpecialWord=true;
    					break;
    				}
    				
    				i++;
    			}
    			
    			if(isSpecialWord==false && (str1.compareTo(str2)==0 || str1.indexOf(str2)>=0 || str2.indexOf(str1)>=0)){
    				
    				eval=eval+2;
    			}	
    	}
    	
    	if(		a.getPhone()!=null && b.getPhone()!=null &&
    			a.getPhone().compareTo("")!=0 && b.getPhone().compareTo("")!=0){
    			
	    		str1 = a.getPhone().replaceAll("\\s","");
	    		str1 = a.getPhone().replaceAll("+", "");
	    		str1 = a.getPhone().replaceAll("-", "");
				str2 = b.getPhone().replaceAll("\\s","");
				str2 = a.getPhone().replaceAll("+", "");
	    		str2 = a.getPhone().replaceAll("-", "");
	    		
				if(str1.compareTo(str2)==0){
					eval=eval+3;
				}
    	}
    	
    	if(		a.getUrl()!=null && b.getUrl()!=null &&
    			a.getUrl().compareTo("")!=0 && b.getUrl().compareTo("")!=0){
	    		
    			str1 = a.getUrl().trim();
    			str2 = b.getUrl().trim();
    			
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
		    	   a.getName().compareTo("")!=0 && b.getName().compareTo("")!=0 ){ 
		    			
	    				return a.getName().compareToIgnoreCase(b.getName());
	    				
		    	}else if(a.getPhone()!=null && b.getPhone()!=null &&
		    			a.getPhone().compareTo("")!=0 && b.getPhone().compareTo("")!=0 ){
		    			
		    			return a.getPhone().compareToIgnoreCase(b.getPhone());
		    			
		    	}else if(a.getUrl()!=null && b.getUrl()!=null &&
		    			a.getUrl().compareTo("")!=0 && b.getUrl().compareTo("")!=0){ 
		    			 
		    			return a.getUrl().compareToIgnoreCase(b.getUrl());
		    			
		    	}else{
	    				return a.getBid().compareTo(b.getBid());
 		    }
    	}
    		
    	FullAddress al = a.getFullAddress();
    	FullAddress bl = b.getFullAddress();
    	
    	if(al!=null && bl!=null){
    		
	    	if(		al.getCity()!=null && bl.getCity()!=null &&
	    			al.getCity().compareTo("")!=0 && bl.getCity().compareTo("")!=0 ){
	    		
		    		str1 = al.getCity().toLowerCase().replaceAll("\\s","");
	    			str2 = bl.getCity().toLowerCase().replaceAll("\\s","");
	    			
	    			if(str1.compareTo(str2)==0 || LevenshteinDistance.computeDistance(str1, str2)<=2){
	    				eval=eval+1;
	    			}		
	    	}		    	
	    	
	    	if(		al.getPostal_code()!=null && bl.getPostal_code()!=null &&
	    			al.getPostal_code().compareTo("")!=0 && bl.getPostal_code().compareTo("")!=0 ){
	    			
	    			str1 = al.getPostal_code().trim();
					str2 = bl.getPostal_code().trim();
			
	    			if(str1.compareTo(str2)==0){
	    				eval=eval+1;
	    			}
	    	}
	    	
	    	if(		al.getAddress()!=null && bl.getAddress()!=null &&
	    			al.getAddress().compareTo("")!=0 && bl.getAddress().compareTo("")!=0){
	    			
	    			str1 = al.getAddress().toLowerCase().replaceAll("\\s","");
	    			str2 = bl.getAddress().toLowerCase().replaceAll("\\s","");
	    			
	    			if(str1.compareTo(str2)==0 || str1.indexOf(str2)>=0 || str2.indexOf(str1)>=0 
	    					|| LevenshteinDistance.computeDistance(str1, str2)<=3 ){
	    				eval=eval+1;
	    			}
	    	}
    	}
    
    	// If match the name and one of the address is that ok ?
    	if(eval >=4){
    		System.out.println("MATCH SECOND: "+a.getName()+"\t|\t"+b.getName());
    		return 0;
    	}else{  
    		//System.out.println("Partial: "+a.getName()+" , "+b.getName());
    		if(a.getName()!=null && b.getName()!=null &&
	    	   a.getName().compareTo("")!=0 && b.getName().compareTo("")!=0 ){ 
	    			
    				return a.getName().compareToIgnoreCase(b.getName());
	    	
    		}else if(a.getPhone()!=null && b.getPhone()!=null &&
	    			a.getPhone().compareTo("")!=0 && b.getPhone().compareTo("")!=0 ){
	    			
	    			return a.getPhone().compareToIgnoreCase(b.getPhone());
	    			
	    	}else if(a.getUrl()!=null && b.getUrl()!=null &&
	    			a.getUrl().compareTo("")!=0 && b.getUrl().compareTo("")!=0){ 
	    			 
	    			return a.getUrl().compareToIgnoreCase(b.getUrl());
	    	
	    	}else{
    				return a.getBid().compareTo(b.getBid());
		    }
    	}	 
    }	
	
	
	class VenueMatchingsComparator implements Comparator<Business> , java.io.Serializable{
		private static final long serialVersionUID = 1L;
		private Map<Business,Integer> base;
		
		public VenueMatchingsComparator(Map<Business, Integer> map){
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
	
	class SortData implements Comparator<Business> , java.io.Serializable{
		
		public int compare(Business a, Business b) {
			
			return a.getName().compareTo(b.getName());
		}
	}
	
	public static void main(String args[]){	
		LoadData ld = new LoadData();
		ld.combineDataFourSquareYelp();
		
	}
}
