package edu.unitn.dii.geo;

import java.util.ArrayList;
import java.util.Iterator;

public class GeoPolygon {

	public String type="";
	public ArrayList<ArrayList<ArrayList<Float>>> coordinates = new  ArrayList<ArrayList<ArrayList<Float>>>();
	
	
	
	public GeoPolygon(){
		
	}
	
	public ArrayList<ArrayList<Float>> getPointList() {
		Iterator polygonIt = coordinates.iterator();

		ArrayList<Float> latitude = new ArrayList<Float>();
		ArrayList<Float> longitude = new ArrayList<Float>();

		// iterate through various levels of array to get the points
		while (polygonIt.hasNext()) {

			ArrayList a = new ArrayList();
			a = (ArrayList) polygonIt.next();

			// list of points inside polygons
			for (int i = 0; i < a.size(); i++) {
				
				ArrayList<Float> b = (ArrayList<Float>) a.get(i);
				latitude.add((float) b.get(1));
				longitude.add((float) b.get(0));				

			}
		}
		
		ArrayList<ArrayList<Float>> retArrayList = new ArrayList<ArrayList<Float>>();
		retArrayList.add(latitude);
		retArrayList.add(longitude);
		
		return retArrayList;
	}

}
