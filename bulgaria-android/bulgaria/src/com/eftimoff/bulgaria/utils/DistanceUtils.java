package com.eftimoff.bulgaria.utils;

import android.location.Location;

public class DistanceUtils {

	private DistanceUtils() {
	}

	public static double distance(double startLatitude, double startLongitude, double endLatitude,
			double endLongitude) {
		return distance(startLatitude, startLongitude, endLatitude, endLongitude, 0, 0);
	}

	/*
	 * Calculate distance between two points in latitude and longitude taking
	 * into account height difference. If you are not interested in height
	 * difference pass 0.0. Uses Haversine method as its base.
	 * 
	 * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
	 * el2 End altitude in meters
	 */
	public static double distance(double startLatitude, double startLongitude, double endLatitude,
			double endLongitude, double el1, double el2) {
		float[] results = new float[3];
		Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results);
		return results[0] / 1000;
	}
}
