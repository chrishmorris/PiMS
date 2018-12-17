package org.pimslims.crystallization.tools;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.pimslims.crystallization.tools");
        //$JUnit-BEGIN$
        //TODO suite.addTestSuite(OptimisationScreenGeneratorTest.class);
        suite.addTestSuite(OrderXmlLoaderTest.class);
        suite.addTest(ExperimentXmlLoaderTest.suite());
        suite.addTestSuite(ImageXmlLoaderTest.class);
        //$JUnit-END$
        return suite;
    }

}
