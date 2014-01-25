//package edu.unitn.dii.test;
//
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.google.gson.Gson;
//
//import au.com.bytecode.opencsv.CSVReader;
//
//import edu.unitn.dii.foursquare.Query;
//import edu.unitn.dii.geo.City;
//import edu.unitn.dii.geo.GeoMultiPolygon;
//import edu.unitn.dii.geo.GeoPolygon;
//import edu.unitn.dii.geo.GeoShape;
//
//public class GeoTester {
//	public static List<City> topCities = new ArrayList<City>();
//
//	public static void main(String args[]) throws IOException {
//		GeoTester t = new GeoTester();
//		t.readTopCities();
//		
//		Query q = new Query();
//		q.queryFoursquareTips(topCities);
//	}
//
//	public void readTopCities() {
//
//		CSVReader reader;
//		try {
//			reader = new CSVReader(new FileReader("resources/top_cities.csv"));
//
//			String[] nextLine;
//
//			try {
//				while ((nextLine = reader.readNext()) != null) {
//					String nameOfCity = nextLine[0];
//
//					if (!nameOfCity.equalsIgnoreCase("COMUNE")
//							&& !nameOfCity.equalsIgnoreCase("")) {
//						// retrieves the boundary from json data
//						ArrayList<ArrayList<Float>> boudaryOfCity = getCityBoundaryList(nextLine[9]);
//
//						City city = new City(nextLine[0], boudaryOfCity);
//						topCities.add(city);
//						
//
//					}
//				}
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private ArrayList<ArrayList<Float>> getCityBoundaryList(String json) {
//
//		Gson gson = new Gson();
//
//		GeoShape geoShape = gson.fromJson(json, GeoShape.class);
//
//		if (geoShape.type.equalsIgnoreCase("MultiPolygon")) {
//			GeoMultiPolygon geoMultiPolygon = gson.fromJson(json,
//					GeoMultiPolygon.class);
//			return (geoMultiPolygon.getPointList());
//
//		} else {
//			GeoPolygon geoPolygon = gson.fromJson(json, GeoPolygon.class);
//			return (geoPolygon.getPointList());
//		}
//	}
//
//}
