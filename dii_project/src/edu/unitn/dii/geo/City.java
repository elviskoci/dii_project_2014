package edu.unitn.dii.geo;

import java.util.ArrayList;
import java.util.List;

public class City {

	/**
	 * @param args
	 */
	
	private String cityName ;
	private ArrayList<ArrayList<Float>> boundary;
	
	public   City(String nameOfCity, ArrayList<ArrayList<Float>> listofBoundaryPoints) {
		
		setCityName(nameOfCity);
		setBoundary(listofBoundaryPoints);

	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public ArrayList<ArrayList<Float>> getBoundary() {
		return boundary;
	}

	public void setBoundary(ArrayList<ArrayList<Float>> boundary) {
		this.boundary = boundary;
	}
	
	public Coordinate[] getPointsInCity (int numOfCities){
		
		GeoPointsGenerator geoPointGenerator = new GeoPointsGenerator();
		int numberOfPoints = numOfCities;//number of points to be generated inside the boundary
		
		int boundaryPoints = boundary.get(1).size();
		
		float[] xPoints = new float[boundaryPoints];
		float[] yPoints = new float[boundaryPoints];
		
		//Rework: there is better way of chaning arraylist to array, but it is not working..try later
		for ( int i = 0 ;  i < boundaryPoints ; i++){

			//System.out.println("%%% " + boundary.get(0).get(i));
			xPoints[i] = boundary.get(0).get(i);
			yPoints[i] = boundary.get(1).get(i);							
		}
		
		//retrieve list of random points inside the given city
		Coordinate [] pointsInsidePlace = new Coordinate[numberOfPoints]; 
		pointsInsidePlace = geoPointGenerator.generatePoints(numberOfPoints ,  xPoints ,yPoints);
		
		/*
		String str = "";
		for (int i = 0 ; i < numberOfPoints ; i++){
			str = str + "add(\"" + pointsInsidePlace[i].getLatitude() + " , " + pointsInsidePlace[i].getLongitude() + "\");" + "\n";
			//str = str +   pointsInsidePlace[i].getLatitude() + " , " + pointsInsidePlace[i].getLongitude()  + "\n";
			
		}
		System.out.println(str);
		*/

		
		return pointsInsidePlace;
		
	}

}
