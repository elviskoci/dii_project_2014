package edu.unitn.dii.foursquare;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.unitn.dii.yelp.Business;
import edu.unitn.dii.yelp.FullAddress;
import edu.unitn.dii.yelp.Review;
import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.Category;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.CompleteTip;
import fi.foyt.foursquare.api.entities.Location;

public class TrentinoTips implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private transient ArrayList<CompleteTip[]> results = new ArrayList<CompleteTip[]>();
	private TreeSet<Business> foursquare_businesses = new TreeSet<Business>();
	private TreeMap<CompleteTip, Integer> uniqueTips = new TreeMap<CompleteTip,Integer>(new CompleteTipComparator());
	private TreeMap<CompactVenue, Integer> uniqueVenues= new TreeMap<CompactVenue, Integer>(new CompactVenueComparator());
	private int totalQueries=0;
	private int succQueries=0;
	private transient static int trials=1;
	private int totalNrTips = 0;
	
	public void executeBatchTipQueries(ArrayList<String> geopoints, String serialization_file){
		  	  
		  this.results = new ArrayList<CompleteTip[]>();
		  Iterator<String> itr= geopoints.iterator();
		  FoursquareApi current = foursquareApi1; 
		  
		  //Call the foursquare api for each geographical point of interest
		  while(itr.hasNext()){
			
			if(((this.totalQueries/500)%2)>=1){
			  current=foursquareApi1;
			}else{
			  current = foursquareApi2;	
			}
  
			try{
				Thread.sleep(1000);
				this.searchTips(itr.next(), current);
		  	} catch (FoursquareApiException e) {
		  		e.printStackTrace();
		    } catch (InterruptedException e) {
				e.printStackTrace();
			}
		  }  
		  
		  foursquareDataToBusinessData();
		  try {
				serialiseTips(serialization_file);
		  } catch (IOException e) {
				System.out.println("SERIALIZATION FAILED!!");
				System.out.println(e.getMessage());
		  }
	  }
	  
	  //Find the unique the tips, venues and locations from the repeated data.
	  //Print statistics about this objects.
	  private void printSearchStats(){
		
		  //Check for repeated results. Print statics : nr of unique tips, nr of repetitions per tip...
		  Iterator<CompleteTip[]> resList = this.results.iterator();	  
		  //int totalRep=0;
//		  int trep=0;
//		  while(resList.hasNext()){
//			  CompleteTip[] tipsArray = resList.next();
//			  for (CompleteTip tip : tipsArray) {			  
//				
//				  if(!this.uniqueTips.isEmpty()){
//					 
//					  if(this.uniqueTips.containsKey(tip)){
//						 		 trep=this.uniqueTips.get(tip);
//								 this.uniqueTips.put(tip, trep+1);	
//								 //totalRep++;
//					  }else{
//						 this.uniqueTips.put(tip, 1);
//					  }
//				  }
//				  else{				 
//					  this.uniqueTips.put(tip, 1); 
//				  }
//				  
//			  }  
//		  } 
		  
		  TipRepetitionComparator trc = new TipRepetitionComparator(this.uniqueTips);
	      TreeMap<CompleteTip,Integer> sorted_tips = new TreeMap<CompleteTip,Integer>(trc);
	      sorted_tips.putAll(this.uniqueTips);
	      System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	      System.out.println("Tips sorted by number of repetitions: \n");
	      Set<CompleteTip> tip_set = sorted_tips.descendingKeySet();
	      Iterator<CompleteTip> titr = tip_set.iterator();
	      while(titr.hasNext()){
	    	  CompleteTip t = new CompleteTip();
	    	  t= titr.next();
	    	  System.out.println(""+t.getId()+": "+this.uniqueTips.get(t));
	      }
	      
//	      //Find statistics about the venues
//		  Iterator<CompleteTip[]> resList2 = this.results.iterator();	  
//		  int nullVenues=0, vrep=0;
//		  while(resList2.hasNext()){
//			  CompleteTip[] tipsArray = resList2.next();
//			  for (CompleteTip tip : tipsArray) {			  
//				 if( tip.getVenue()!=null){
//					  CompactVenue venue = new CompactVenue();
//					  venue =tip.getVenue(); 
//					  if(!this.uniqueTips.isEmpty()){
//						 
//						  if(this.uniqueVenues.containsKey(venue)){
//							  		 vrep=this.uniqueVenues.get(venue);
//									 this.uniqueVenues.put(venue, vrep+1);					 
//						  }else{
//							 this.uniqueVenues.put(venue, 1);
//						  }
//					  }
//					  else{				 
//						  this.uniqueVenues.put(venue, 1); 
//					  }
//					  
//				 }else{
//					 nullVenues++;
//				 }
//			  }
//		  }  
		  
	      VenueRepetitionComparator vrc = new VenueRepetitionComparator(this.uniqueVenues);
	      TreeMap<CompactVenue,Integer> sorted_venues = new TreeMap<CompactVenue,Integer>(vrc);
	      sorted_venues.putAll(this.uniqueVenues);
	      System.out.println("Venues sorted by number of repetitions: \n");
	      
	      Set<CompactVenue> venues_set = sorted_venues.descendingKeySet();
	      Iterator<CompactVenue> vitr = venues_set.iterator();
	      while(vitr.hasNext()){
	    	  CompactVenue v = new CompactVenue();
	    	  v= vitr.next();
	    	  System.out.println(""+v.getName()+": "+this.uniqueVenues.get(v));
	      }
	    		  
	      HashMap<String,Integer> locations = new  HashMap<String, Integer>();   
	      Iterator<CompactVenue> vlitr= venues_set.iterator();
	      String address="";
	      int locRep=0, nullLocation=0;
	      while(vlitr.hasNext()){ 	  
	    	  CompactVenue venue= new CompactVenue(); 
	    	  venue=vlitr.next();
	    	  if(venue.getLocation()!=null){
	    		  address=venue.getLocation().getCity();
	    		  if(locations.containsKey(address)){
	    			  locRep=locations.get(address);
	    			  locations.put(address, locRep+1);
	    		  }else{
	    			  locations.put(address, 1);
	    		  }
	    	  }else{
	    		  nullLocation++;
	    	  }
	      }
	      
	      ValueComparator lc =  new ValueComparator(locations);
	      TreeMap<String,Integer> sorted_locations = new TreeMap<String,Integer>(lc);
	      sorted_locations.putAll(locations);
	      System.out.println("\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	      System.out.println("Total number of unique locations: "+locations.size());
	      System.out.println("Number of null locations objects returned: "+nullLocation);
	      System.out.println("Locations sorted by number of repetitions: \n"+sorted_locations);
	  }
	  
	  //Print the data of retrieved tips
	  private void printTips(){
		  
		  Iterator<CompleteTip> itr = this.uniqueTips.keySet().iterator();
		  System.out.println("\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
		  		+ "\nPRINT THE LIST OF UNIQUE TIPS:\n");
		  int i=1;
		  while(itr.hasNext()){
			  CompleteTip tip = itr.next();          
	    	   try{   
		      	System.out.println("====================================================================");
		      	System.out.println("Tip nr: "+i);
		      	System.out.println("Tip Id: "+tip.getId());
		      	System.out.println("Tip Text: "+tip.getText());
		  		System.out.println("Tip Created Date: "+tip.getCreatedAt());
		  		System.out.println("Tip URL: "+tip.getUrl());	
	  			if(tip.getUser()!=null){
	  				System.out.println("Tip UserID: "+tip.getUser().getId());
	  				System.out.println("Tip User HomeCity: "+tip.getUser().getHomeCity());
	  			}
	  			else{
	  				System.out.println("Tip User: NULL");
	  			}
	  		
	  			if(tip.getVenue()!=null){
	  				System.out.println("Venue ID: "+tip.getVenue().getId());
	  				System.out.println("Venue Name: "+tip.getVenue().getName());
	  				
	  				boolean start=true;
	  				if(tip.getVenue().getCategories()!=null){
	  					System.out.print("Venue Categories: ");
	  					
	  					for(Category cat : tip.getVenue().getCategories()){
	  						if(!start){
	  							System.out.print(", "+cat.getName());
	  						}
	  						else{
	  							System.out.print(""+cat.getName());
	  							start=false;
	  						}
	  					}
	  					System.out.println();
	  				}else{
		  				System.out.println("Venue Categories: NULL");	
	  				}
	  				
	  				if(tip.getVenue().getStats()!=null){
	  					System.out.println("Venue Checkins: "+tip.getVenue().getStats().getCheckinsCount());
	  					System.out.println("Venue Users: "+tip.getVenue().getStats().getUsersCount());
	  				}else{
	  					System.out.println("Venue Stats: NULL");
	  				}
	  				
	  				if(tip.getVenue().getTips()!=null){
	  					System.out.println("Venue Tip_Count: "+tip.getVenue().getTips().getCount());
	  				}else{
	  					System.out.println("Venue Tip_Count: NULL");
	  				}
	  				
	  				if(tip.getVenue().getLocation()!=null){
		  				System.out.println("Venue Address: "+tip.getVenue().getLocation().getAddress());
		  				System.out.println("Venue City: "+tip.getVenue().getLocation().getCity());
		  				System.out.println("Venue Country: "+tip.getVenue().getLocation().getCountry());
		  				System.out.println("Venue Latitude: "+tip.getVenue().getLocation().getLat());
		  				System.out.println("Venue Longitude: "+tip.getVenue().getLocation().getLng());
		  			}
		  			else{
			  			System.out.println("Venue Address: NULL");
			  		}
		  		    
		  		}
		  		else{
		  			System.out.println("Venue: NULL");
		  		}
	  			System.out.println();
		  	  
	    	  }catch(NullPointerException ex){
	    			ex.printStackTrace();
		      }
          	  i++;
		  }
	  }
	  
	  private void foursquareDataToBusinessData(){
		  
		  Iterator<CompleteTip[]> resList = this.results.iterator();	  
		  int trep=0;
		  int vrep=0;
		  int nullVenues=0; 
		  while(resList.hasNext()){
			  CompleteTip[] tipsArray = resList.next();
			  for (CompleteTip tip : tipsArray) {			  
				  
				  if( tip.getVenue()!=null){
					  CompactVenue venue = new CompactVenue();
					  venue =tip.getVenue(); 
					  if(!this.uniqueVenues.isEmpty()){
						 
						  if(this.uniqueVenues.containsKey(venue)){ 		
							  		 vrep=this.uniqueVenues.get(venue);
									 this.uniqueVenues.put(venue, vrep+1);
									 if(this.uniqueTips.containsKey(tip)){
								 		 trep=this.uniqueTips.get(tip);
										 this.uniqueTips.put(tip, trep+1);	
									 }else{
										  Business business = venueToBusiness(venue);
										  Review review =tipToReview(tip);
										  business.getReviews().add(review);
										  foursquare_businesses.add(business);
										  this.uniqueTips.put(tip, 1);  
									 }
						  }else{
							
								Business business=venueToBusiness(venue);	 
								Review review =tipToReview(tip);
								business.getReviews().add(review);
								foursquare_businesses.add(business);
								this.uniqueTips.put(tip, 1);
							   	this.uniqueVenues.put(venue, 1);
						  }
					  }
					  else{				 
						  Business business =venueToBusiness(venue);
						  Review review =tipToReview(tip);
						  business.getReviews().add(review);
						  foursquare_businesses.add(business);
						  this.uniqueTips.put(tip, 1);
						  this.uniqueVenues.put(venue, 1); 
					  }
					  
				 }else{
					 nullVenues++;
				 }
				 this.totalNrTips++;  
			  }  
		  } 
		  
		  System.out.println("Total number of queries performed :"+this.totalQueries);
		  System.out.println("Total number of successful queries performed :"+this.succQueries);
		  System.out.println("Total number of retrived tips: "+this.totalNrTips);		  
		  System.out.println("Total number of unique tips: "+this.uniqueTips.size());
		  System.out.println("Total number of unique venues: "+this.uniqueVenues.size());
		  System.out.println("Total size of the result list is : "+this.results.size());
	      System.out.println("Number of null venue objects returned: "+nullVenues);
		  
	  }
	  
	  private String handleNullVal(String val){
		  
		  if(val==null){
			  return "null"; 
		  }
		  
		  return val;
	  }
	  
	  private Business venueToBusiness(CompactVenue venue){
		  	
		  	Business business = new Business();
			business.setBid(venue.getId());
			business.setName(handleNullVal(venue.getName()));
			business.setUrl(handleNullVal(venue.getUrl()));
			
			if(venue.getContact()!=null)
			business.setPhone(handleNullVal(venue.getContact().getPhone()));
			
			Category[] categories= venue.getCategories();
			ArrayList<String> catgrs_list = new ArrayList<String>();
			if(categories!=null){
				for(int i=0;i<categories.length;i++){
					catgrs_list.add(categories[0].getName());
				}
			}else{
				catgrs_list.add("null");
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
				
				String city = location.getCity();
				if(city==null){
					city="unknown";
				}
				
				loc=new FullAddress(handleNullVal(location.getAddress()),city
						,handleNullVal(location.getState()), handleNullVal(location.getCountry()),
						handleNullVal(location.getPostalCode()),location.getLat(),
						location.getLng());
			}
			
			business.setFullAddress(loc);
		  
			return business;
	  }
	  
	  private Review tipToReview(CompleteTip tip){
		  
		  Review review = new Review();
		  review.setCreated_at((Long)tip.getCreatedAt().longValue());
		  review.setText(tip.getText());
		  review.setReview_id(tip.getId());
		  //I could not find likes. 
		  //The Api does not offer function
		  //to get the likes for the tip.
		  review.setLikes(-1);
		  if(tip.getUser()!=null){
			  review.setUser_id(tip.getUser().getId());
		  }else{
			  review.setUser_id("unknown");
		  }
		  
		  return review;
	  }
	  
	  //Request tips from foursquare for the given point
	  private void searchTips(String ll, FoursquareApi foursquareApi ) throws FoursquareApiException  {
		 
		   this.totalQueries++;
		   System.out.println("Query "+this.totalQueries+" with cordinates "+ll+" ");
		   
		   Result<CompleteTip[]> result = foursquareApi.tipsSearch(ll,500,null,null, null);
	      
	       if (result.getMeta().getCode() == 200) {	
		       results.add(result.getResult());
		       this.succQueries++;
       	   }
	       else {
	        // TODO: Proper error handling
        	System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	        System.out.println("Error occured quering "+ll);
	        System.out.println("  code: " + result.getMeta().getCode());
	        System.out.println("  type: " + result.getMeta().getErrorType());
	        System.out.println("  detail: " + result.getMeta().getErrorDetail());
	        System.out.println(""+result.getMeta());
	        	
	        	//In case of an error we try again the call max 3 times
	        	if(this.trials < 3){
	        		this.trials++;
	        		searchTips(ll,foursquareApi);
	        	}else{
	        		trials=1;
	        	}
	        }
	  }
	  
	  //Comparator to order locations based on the repetitions
	  class ValueComparator implements Comparator<String> , java.io.Serializable{
		private static final long serialVersionUID = 1L;
			Map<String, Integer> base;
		    public ValueComparator(Map<String, Integer> base) {
		        this.base = base;
		    }

		    // Note: this comparator imposes orderings that are inconsistent with equals.    
		    public int compare(String a, String b) {
		        if (base.get(a) >= base.get(b)) {
		            return -1;
		        } else {
		            return 1;
		        } // returning 0 would merge keys
		    }
	  }
	  
	  //Comparator to order venues based on the repetitions
	  class VenueRepetitionComparator implements Comparator<CompactVenue> , java.io.Serializable {
		private static final long serialVersionUID = 1L;
			Map< CompactVenue, Integer> base;
		    public VenueRepetitionComparator(Map<CompactVenue,Integer> base) {
		        this.base = base;
		    }

		    // Note: this comparator imposes orderings that are inconsistent with equals.    
		    public int compare(CompactVenue a, CompactVenue b) {
		        if (base.get(a) >= base.get(b)) {
		            return -1;
		        } else {
		            return 1;
		        }
		    }
	  }
	  
	  // Comparator to order tips based on the repetitions
	  class TipRepetitionComparator implements Comparator<CompleteTip> , java.io.Serializable {
		private static final long serialVersionUID = 1L;
			Map< CompleteTip, Integer> base;
		    public TipRepetitionComparator(Map<CompleteTip,Integer> base) {
		        this.base = base;
		    }

		    // Note: this comparator imposes orderings that are inconsistent with equals.    
		    public int compare(CompleteTip a, CompleteTip b) {
		        if (base.get(a) >= base.get(b)) {
		            return -1;
		        } else {
		            return 1;
		        }
		    }
	  }
	  
	  //Custom Comparator for CompactVenue objects. It compares venue objects based on the venue_id
	  class CompactVenueComparator implements Comparator<CompactVenue> , java.io.Serializable {		
		private static final long serialVersionUID = 1L;

			public int compare(CompactVenue a, CompactVenue b) {
	            	return a.getId().compareTo(b.getId());
			}
	  }
	  
	  //Custom Comparator for CompleteTip objects. It compares tip objects based on the tip_id
	  class CompleteTipComparator implements Comparator<CompleteTip> , java.io.Serializable {		
		private static final long serialVersionUID = 1L;

			public int compare(CompleteTip a, CompleteTip b) {
	            	return a.getId().compareTo(b.getId());
			}
	  }
	
	  // Serialise the TrentinoTips object.
	  public void serialiseTips(String file) throws IOException{
			
		  	/*String formatString="dd_MMM_yyyy";
			DateFormat df= new SimpleDateFormat(formatString, Locale.ENGLISH);
			df.setTimeZone(TimeZone.getTimeZone("GMT"));
			String formatedDate= df.format(new Date());*/
			
	        FileOutputStream fileOut =
	        new FileOutputStream(file);
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(this);
	        out.close();
	        fileOut.close();
	        System.out.println("Serialized data is saved in:"+file);
	  }
		
	  // Deserialise the TrentinoTips object.
	  public static TrentinoTips deserialiseTips(String file) throws IOException, ClassNotFoundException{
		
		    File serFile=  new File(file);
		    if(!serFile.exists()){
		    	System.out.println("Serialization file does not exist. Create new TrentinoTips object");
		    	return new TrentinoTips();
		    }
		    
		    TrentinoTips tips = null;
		    FileInputStream fileIn = new FileInputStream(file);//./storage/foursquare_output-19-Jan.ser
		    ObjectInputStream in = new ObjectInputStream(fileIn);
		    tips = (TrentinoTips) in.readObject();
		    in.close();
		    fileIn.close();
		    System.out.println("The file was deserialized: "+file);
		    return tips;
	  }
	  
	public TreeSet<Business> getFoursquare_businesses() {
		return foursquare_businesses;
	}

	public static void main(String[] args) {
			
		boolean deserialize = false;
		TrentinoTips tt= new TrentinoTips() ;
		
		if(deserialize){
			try {
				tt=deserialiseTips("./storage/foursquare_output.ser");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    
		tt.executeBatchTipQueries(PointsOfInterest.GEO_POINTS,"./storage/foursquare_output.ser");
		//tt.printSearchStats();
		//tt.printTips();
	 }		
}
