package edu.unitn.dii.foursquare;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.CompleteTip;
import fi.foyt.foursquare.api.entities.TipGroup;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;
import fi.foyt.foursquare.api.io.IOHandler;
import fi.foyt.foursquare.api.io.Method;
import fi.foyt.foursquare.api.io.MultipartParameter;
import fi.foyt.foursquare.api.io.Response;

public class Foursquare {

  public static void main(String[] args) {
    String ll = args.length > 0 ? args[0] : "46.05739, 11.614582";
    try {
      //(new Foursquare()).searchVenues(ll);
      (new Foursquare()).searchTips(ll);
    } catch (FoursquareApiException e) {
      // TODO: Error handling
    }
    
  }
  
  public void searchTips(String ll) throws FoursquareApiException  {
	 
  	   FoursquareApi foursquareApi = new FoursquareApi("5OUFCQD5N3ENAFDZIG1SSQXNWJX3HVB4VO4WMQYWKMBJVWC1", "Y3QQXZOE3FX5EWYOH2KCMVJI4QP2GSPIZ41IZRC5UFJEFFHJ", "https://foursquare.com/user/53771962, https://www.foursquare.com");
     
       Result<CompleteTip[]> result = foursquareApi.tipsSearch(ll,500,null,null, null);
      
       if (result.getMeta().getCode() == 200) {
      
       int i=1;
       
       System.out.println("\n\nList of TIPS:");
       for (CompleteTip tip : result.getResult()) {
    	   
    	try{   
	      	System.out.println("====================================================================");
	      	System.out.println("Tip nr: "+i);
	      	System.out.println("Id:"+tip.getId());
	      	System.out.println("Text: "+tip.getText());
	  		System.out.println("Created Date: "+tip.getCreatedAt());
	        System.out.println("Venue: "+tip.getVenue().getName()+"\n");
    		}
    		catch(NullPointerException ex){
    			//System.out.println("Null Value");
    			ex.printStackTrace();
	        }
          i++;
        }
      } else {
        // TODO: Proper error handling
        System.out.println("Error occured: ");
        System.out.println("  code: " + result.getMeta().getCode());
        System.out.println("  type: " + result.getMeta().getErrorType());
        System.out.println("  detail: " + result.getMeta().getErrorDetail());
        System.out.println(""+result.getMeta());
      }
  }

    public void searchVenues(String ll) throws FoursquareApiException {
  
	// First we need a initialize FoursquareApi.    	
    FoursquareApi foursquareApi = new FoursquareApi("5OUFCQD5N3ENAFDZIG1SSQXNWJX3HVB4VO4WMQYWKMBJVWC1", "Y3QQXZOE3FX5EWYOH2KCMVJI4QP2GSPIZ41IZRC5UFJEFFHJ", "https://foursquare.com/user/53771962, https://www.foursquare.com");
   
    // After client has been initialized we can make queries.
    Result<VenuesSearchResult> result = foursquareApi.venuesSearch(ll, null, null, null, null,50, "checkin", null, null, null, null);
   
    // if query was ok we can finally we do something with the data
    if (result.getMeta().getCode() == 200) {
    	
    	  int i=1;
      	  System.out.println("\n\nList of VENUES:");
          for (CompactVenue venue: result.getResult().getVenues()){
        	  System.out.println("\n====================================================================");
        	  System.out.println("Id: "+venue.getId());
        	  System.out.println("Venue: "+venue.getName());
        	  System.out.println("Location: "+venue.getLocation().getCity());
        	  if(venue.getTips()!=null){
		    	  System.out.println("Tips:\n \n");
		    	  for (TipGroup tipGroup : venue.getTips().getGroups()){
		    		  for (CompleteTip tip : tipGroup.getItems()) { 
		    			// TODO: Do something we the data
		    	        	System.out.println("Tip nr: "+i);
		    	        	System.out.println("Id:"+tip.getId());
		    	        	System.out.println("Text: "+tip.getText());
		    	    		System.out.println("Created Date: "+tip.getCreatedAt());
		    	            System.out.println("Venue: "+tip.getVenue().getName()+"\n");
		    	            i++;
		    		  }
		    	  }  
        	  }
         } 
     }else {
      System.out.println("Error occured: ");
      System.out.println("  code: " + result.getMeta().getCode());
      System.out.println("  type: " + result.getMeta().getErrorType());
      System.out.println("  detail: " + result.getMeta().getErrorDetail());
      System.out.println(""+result.getMeta());
    }
  }
}

