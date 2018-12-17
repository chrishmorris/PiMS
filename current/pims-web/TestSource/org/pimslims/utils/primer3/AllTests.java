package org.pimslims.utils.primer3;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.pimslims.utils.primer3");
        // $JUnit-BEGIN$
        suite.addTestSuite(Primer3Tester.class);
        // $JUnit-END$
        return suite;
    }

}
