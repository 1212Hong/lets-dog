package com.ssafy.dog.domain.gps.util;

public class DistanceCalculator {

	public static final double EARTH_RADIUS = 6371; //km 단위

	// Haversine formula
	public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		lat1 = Math.toRadians(lat1);
		lon1 = Math.toRadians(lon1);
		lat2 = Math.toRadians(lat2);
		lon2 = Math.toRadians(lon2);

		return EARTH_RADIUS * Math.acos(
			Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
	}
}
