package utils;

import java.text.DecimalFormat;

public class Calculator {

	public double CalculationByDistance(double initialLat, double initialLong,
			double finalLat, double finalLong) {
		int R = 6371; // km (Earth radius)
		double dLat = toRadians(finalLat - initialLat);
		double dLon = toRadians(finalLong - initialLong);
		initialLat = toRadians(initialLat);
		finalLat = toRadians(finalLat);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2)
				* Math.sin(dLon / 2) * Math.cos(initialLat)
				* Math.cos(finalLat);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return R * c;
	}
	public double getPrice(double distance,double millage,double fuelPrice){
		double price = 0.0;
		price = (distance/millage) * fuelPrice;
		return price;
	}
	public double CalculationByDistance1(double initialLat, double initialLong,
										double finalLat, double finalLong) {
		int Radius = 6371;// radius of earth in Km
		double lat1 = initialLat;
		double lat2 = finalLat;
		double lon1 = initialLong;
		double lon2 = finalLong;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		double c = 2 * Math.asin(Math.sqrt(a));
		double valueResult = Radius * c;
		double km = valueResult / 1;
		DecimalFormat newFormat = new DecimalFormat("####");
		int kmInDec = Integer.valueOf(newFormat.format(km));
		double meter = valueResult % 1000;
		int meterInDec = Integer.valueOf(newFormat.format(meter));
		return Radius * c;
	}

	public double toRadians(double deg) {
		return deg * (Math.PI / 180);
	}
}
