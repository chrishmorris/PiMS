package uk.ac.ox.oppf.www.wsplate.impl;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for uk.ac.ox.oppf.www.wsplate.impl");
        //$JUnit-BEGIN$
        suite.addTestSuite(TestPlateInfoProviderImpl.class);
        suite.addTestSuite(TestImagingTaskProviderImpl.class);
        suite.addTestSuite(TestWSPlateImpl.class);
        //$JUnit-END$
        return suite;
    }

}
