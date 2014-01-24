package edu.unitn.dii.geo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReverseGeocoder {

	/**
	 * @param args
	 */
	public static  void main(String[] args) {
		reverseGeocode();
		// TODO Auto-generated method stub
		//https://maps.googleapis.com/maps/api/geocode/output?parameters
		//http://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&sensor=true_or_false
		//http://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&sensor=true_or_false
	}
	
	private static void reverseGeocode(){
		 //http://maps.google.com/maps/geo?q=40.714224,-73.961452&output=json&oe=utf8&sensor=true_or_false&key=your_api_key
		//String localityName = "";
	    HttpURLConnection connection = null;
	    URL serverAddress = null;

	    try 
	    {
	        // build the URL using the latitude & longitude you want to lookup
	    	serverAddress = new URL("http://maps.googleapis.com/maps/api/geocode/json?latlng=46.065,11.0995&sensor=false");
	       // serverAddress = new URL("http://maps.google.com/maps/geo?q=" + Double.toString(loc.getLatitude()) + "," + Double.toString(loc.getLongitude()) +
		     //   		"&output=xml&oe=utf8&sensor=true&key=" + R.string.GOOGLE_MAPS_API_KEY);
	        //set up out communications stuff
	        connection = null;
		      
	        //Set up the initial connection
			connection = (HttpURLConnection)serverAddress.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);
		                  
			connection.connect();
		    
			
			
			
			 
			    // new JSONObject(responseStrBuilder.toString());
			
			
			
			try
			{
				
				BufferedReader streamReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")); 
			    StringBuilder responseStrBuilder = new StringBuilder();

			    String inputStr;
			    while ((inputStr = streamReader.readLine()) != null)
			    {
			    	System.out.println("EEEEEE  " + inputStr); 
			    	responseStrBuilder.append(inputStr);
			    }
			    
			   
				
				/*
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				InputSource source = new InputSource(isr);
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser parser = factory.newSAXParser();
				XMLReader xr = parser.getXMLReader();
				GoogleReverseGeocodeXmlHandler handler = new GoogleReverseGeocodeXmlHandler();
				
				xr.setContentHandler(handler);
				xr.parse(source);
				
				localityName = handler.getLocalityName();*/
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			
	    }
	    catch (Exception ex)
	    {
	        ex.printStackTrace();
	    }
	    
	   // return localityName;

		
	}

}

/*

package example.geocoding;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.location.Location;

public class Geocoder 
{
	public static String reverseGeocode(Location loc)
	{
	    //http://maps.google.com/maps/geo?q=40.714224,-73.961452&output=json&oe=utf8&sensor=true_or_false&key=your_api_key
		String localityName = "";
	    HttpURLConnection connection = null;
	    URL serverAddress = null;

	    try 
	    {
	        // build the URL using the latitude & longitude you want to lookup
	        // NOTE: I chose XML return format here but you can choose something else
	        serverAddress = new URL("http://maps.google.com/maps/geo?q=" + Double.toString(loc.getLatitude()) + "," + Double.toString(loc.getLongitude()) +
		        		"&output=xml&oe=utf8&sensor=true&key=" + R.string.GOOGLE_MAPS_API_KEY);
	        //set up out communications stuff
	        connection = null;
		      
	        //Set up the initial connection
			connection = (HttpURLConnection)serverAddress.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);
		                  
			connection.connect();
		    
			try
			{
				InputStreamReader isr = new InputStreamReader(connection.getInputStream());
				InputSource source = new InputSource(isr);
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser parser = factory.newSAXParser();
				XMLReader xr = parser.getXMLReader();
				GoogleReverseGeocodeXmlHandler handler = new GoogleReverseGeocodeXmlHandler();
				
				xr.setContentHandler(handler);
				xr.parse(source);
				
				localityName = handler.getLocalityName();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			
	    }
	    catch (Exception ex)
	    {
	        ex.printStackTrace();
	    }
	    
	    return localityName;
	}
}

*/





