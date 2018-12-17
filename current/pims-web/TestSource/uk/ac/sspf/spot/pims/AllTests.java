package uk.ac.sspf.spot.pims;

import junit.framework.Test;
import junit.framework.TestSuite;

@Deprecated
public class AllTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for uk.ac.sspf.spot.pims");
        // $JUnit-BEGIN$
        suite.addTestSuite(ConstructFilterTest.class);
        suite.addTestSuite(ConstructProgressTest.class);
        suite.addTestSuite(ConstructFastaTest.class);
        // $JUnit-END$
        return suite;
    }

}
