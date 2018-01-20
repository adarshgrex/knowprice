/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package syncher;

import java.security.Principal;

import utils.Calculator;
import utils.StringUtils;
import junit.framework.TestCase;

public class CalculatorTest extends TestCase {
	//http://www.movable-type.co.uk/scripts/latlong.html
	public void testCalculateDistanceFromKavaliToNellore() throws Exception {
		double lat1 = 14.913181 ;
		double long1 = 79.992980;
		double lat2 = 14.442599 ;
		double long2 = 79.986456;
		double distance = myCalculator.CalculationByDistance1(lat1, long1, lat2, long2);
		assertEquals(52.33, StringUtils.getRoundDoubleValue(distance));		
	}
	public void testCalculateDistanceFromKavaliToOngole() throws Exception {
		double lat1 = 14.913181 ;
		double long1 = 79.992980;
		double lat2 = 15.505723 ;
		double long2 = 80.049922;
		double distance = myCalculator.CalculationByDistance(lat1, long1, lat2, long2);
		assertEquals(66.17, StringUtils.getRoundDoubleValue(distance));		
	}
	public void testCalculateDistanceFromNelloreToKavali() throws Exception {
		double lat1 = 14.442599 ;
		double long1 = 79.986456;
		double lat2 = 14.913181 ;
		double long2 = 79.992980;
		double distance = myCalculator.CalculationByDistance(lat1, long1, lat2, long2);
		assertEquals(52.33, StringUtils.getRoundDoubleValue(distance));		
	}
	public void testCalculateDistanceFromOngoleToKavali() throws Exception {
		double lat1 = 15.505723 ;
		double long1 = 80.049922;
		
		double lat2 = 14.913181 ;
		double long2 = 79.992980;
		double distance = myCalculator.CalculationByDistance(lat1, long1, lat2, long2);
		assertEquals(66.17, StringUtils.getRoundDoubleValue(distance));		
	}
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	Calculator myCalculator;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		myCalculator = new Calculator();
	}
}