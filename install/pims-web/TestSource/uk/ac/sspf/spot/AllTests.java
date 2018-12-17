package uk.ac.sspf.spot;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for uk.ac.sspf.spot");
        // $JUnit-BEGIN$
        //suite.addTest(uk.ac.sspf.spot.pims.AllTests.suite());
        suite.addTest(uk.ac.sspf.spot.beans.AllTests.suite());
        // $JUnit-END$
        return suite;
    }

}
