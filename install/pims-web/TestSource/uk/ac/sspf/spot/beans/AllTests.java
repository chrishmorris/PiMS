package uk.ac.sspf.spot.beans;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for uk.ac.sspf.spot.beans");
        // $JUnit-BEGIN$
        suite.addTestSuite(ConstructProgressBeanTest.class);
        // $JUnit-END$
        return suite;
    }

}
