package edu.unitn.dii.foursquare;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.unitn.dii.geo.City;
import edu.unitn.dii.geo.Coordinate;

public class Query {

	public Query() {

	}

	public void queryFoursquareTips(List<City> cities) {
		int numberOfPoints = 300;
		
		//need to divide points into 500 points chunk each due to query limit
		
		List<ArrayList<String>> pointsArray = new ArrayList<ArrayList<String>>();
		int count = 0;
		
		for(Iterator<City> i = cities.iterator(); i.hasNext(); ) {
			City city = i.next();	
			//Review: May you we should change the output of getPointsInCity to Arraylist
			//That way we do not need  j  loop
			
			switch(city.getCityName().toLowerCase()){
				case "bolzano":  numberOfPoints = 400;break;
				case "trento":  numberOfPoints = 300;break;
				default: numberOfPoints = 50;
			}
			/*
			if (city.getCityName().equalsIgnoreCase("Bolzano") || city.getCityName().equalsIgnoreCase("Trento") || city.getCityName().equalsIgnoreCase("Merano") ){
				numberOfPoints = 300;
				continue;
			}else{ numberOfPoints = 50;}
			*/
			
			Coordinate[] pointsInCity = city.getPointsInCity(numberOfPoints);
			
			for ( int j = 0 ; j < pointsInCity.length ;j++){
				if(count % 400 == 0){					
					pointsArray.add (new ArrayList<String>());					
				}
				
				pointsArray.get(pointsArray.size() - 1).add (((Coordinate) pointsInCity[j]).getLatitude() +"," + ((Coordinate) pointsInCity[j]).getLongitude() );
				//System.out.println ("\"" + ((Coordinate) pointsInCity[j]).getLatitude() +" , " + ((Coordinate) pointsInCity[j]).getLongitude() +"\"");
				count++;
			}	
			
			
			
					
		}
		
		//now 3 blocks			
		//System.out.println(pointsArray.size() + "%%%");

	TrentinoTips tt = new TrentinoTips();
		//tt.executeBatchTipQueries( pointsArray.get(0),"./storage/foursquare_output.ser");

		///*
		  try { 
		  	tt =   TrentinoTips.deserialiseTips("./storage/foursquare_output.ser"); 
		  	//tt.executeBatchTipQueries( pointsArray.get(2),"./storage/foursquare_output.ser");
		  }
		  catch (ClassNotFoundException | IOException e) { // TODO		  Auto-generated catch block 
		  e.printStackTrace(); }//*/
		

	}
}
