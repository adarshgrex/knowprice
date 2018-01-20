package syncher;

import dataobjects.Travel;
import dataobjects.TravelLocation;
import junit.framework.TestCase;
import junit.framework.TestResult;

public class LocationSyncherTest extends TestCase{
	LocationSyncher location;

	public void testgetLocationDetailsByNameHappyFlow() throws Exception {
		String searchAddress = "Kavali, Andhra Pradesh, India";
		TravelLocation locationDetailsByName = location.getLocationDetailsByName(searchAddress);
		assertEquals("Andhra Pradesh", locationDetailsByName.getState());
		assertEquals(14.9131806, locationDetailsByName.getLatitude());
		assertEquals(79.9929798, locationDetailsByName.getLongitude());
	}

	public void testTravelInformation() throws Exception {
		String sourceLocation = "Kavali";
		String destinationLocation = "Hyderabad";
		Travel travelInfo = location.getTravelInformation(sourceLocation,destinationLocation);
		assertEquals(travelInfo.getDistance(), "397 km");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		location = null;
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		location = new LocationSyncher();
	}

}
