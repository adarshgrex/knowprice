package syncher;

import java.util.List;

import dataobjects.LocationState;
import junit.framework.TestCase;

public class StatesSyncherTest extends TestCase {
	public void testGetStatesListHappyFlow() throws Exception {
		List<LocationState> statesList = sut.getStatesList();
		assertEquals(29, statesList.size());
	}
	
	StatesSyncher sut;
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		sut = null;
	}
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		sut = new StatesSyncher();
	}
}
