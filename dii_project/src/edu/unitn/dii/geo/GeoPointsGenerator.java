package edu.unitn.dii.geo;

import java.awt.List;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class GeoPointsGenerator {

	/**
	 * @param args
	 */
	public void main(String[] args) {
		// System.out.println ("GeoPointsGenerator");
		// findRandomPoints();
	}

	public Coordinate[] generatePoints(int targetNumber , float[] xpoints , float[] ypoints) {
		// xPoints: array of lattitudes
		// yPoints: array of longituteds
		

		if (xpoints.length != ypoints.length) {
			System.out.println(xpoints.length + "  " + ypoints.length
					+ " Unmatched latitude and longitude values");
		}

		int npoints = xpoints.length;

		float maxlong = findMax(ypoints);
		float maxlat = findMax(xpoints);

		float minlong = findMin(ypoints);
		float minlat = findMin(xpoints);

		Polygon2D polygon = new Polygon2D(xpoints, ypoints, npoints);

		int numOfPoints = 0;
		Random random;
		float pointY;
		float pointX;

		Coordinate cood;

		Coordinate[] pointsInsidePlace = new Coordinate[targetNumber];

		while (numOfPoints < targetNumber) {
			random = new Random();
			pointY = (float) random.nextFloat() % (maxlong - minlong)
					+ minlong;
			pointX = (float) random.nextFloat() % (maxlat - minlat) + minlat;

			if (polygon.contains(pointX, pointY)) {
				cood = new Coordinate(pointX, pointY);
			
				//System.out.println ("### " + pointX);
				pointsInsidePlace[numOfPoints] = cood;
				numOfPoints = numOfPoints + 1;

			}

		}
		return pointsInsidePlace;

	}
	
	

	private float findMax(float[] arr) {

		float max = arr[0];
		for (int i = 0; i < arr.length; i++) {
			if (max < arr[i]) {
				max = arr[i];
			}
		}

		return max;
	}

	// find better algorithm
	private float findMin(float[] arr) {
		// TODO Auto-generated method stub
		float min = arr[0];
		for (int i = 0; i < arr.length; i++) {
			if (min > arr[i]) {
				min = arr[i];
			}

		}
		return min;
	}

}
