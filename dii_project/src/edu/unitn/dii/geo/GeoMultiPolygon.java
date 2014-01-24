package edu.unitn.dii.geo;

import java.util.ArrayList;
import java.util.Iterator;

public class GeoMultiPolygon {
	public String type = "";
	public ArrayList<ArrayList<ArrayList<ArrayList<Float>>>> coordinates = new ArrayList<ArrayList<ArrayList<ArrayList<Float>>>>();

	public GeoMultiPolygon() {

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
				ArrayList b = (ArrayList) a.get(i);

				// list of points
				for (int j = 0; j < b.size(); j++) {
					ArrayList<Float> d = (ArrayList<Float>) b.get(j);
					latitude.add((float) d.get(1));
					longitude.add((float) d.get(0));
				}

			}
		}
		
		ArrayList<ArrayList<Float>> retArrayList = new ArrayList<ArrayList<Float>>();
		retArrayList.add(latitude);
		retArrayList.add(longitude);
		
		return retArrayList;
	}
	
	
	
}
