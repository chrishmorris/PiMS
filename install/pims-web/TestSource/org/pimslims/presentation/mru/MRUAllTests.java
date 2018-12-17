package org.pimslims.presentation.mru;

import junit.framework.Test;
import junit.framework.TestSuite;

public class MRUAllTests {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(MRUAllTests.suite());
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for org.pimslims.presentation.mru");
        // $JUnit-BEGIN$
        suite.addTestSuite(MRUTest.class);
        suite.addTestSuite(MRUControllerTest.class);
        // $JUnit-END$
        return suite;
    }

}
