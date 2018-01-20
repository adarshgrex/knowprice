package syncher;

import junit.framework.Test;
import junit.framework.TestSuite;

public class SyncherTestSuite extends TestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite("Syncher Test Suite");
//        suite.addTestSuite(EventSyncherTest.class);
        return suite;
    }
}