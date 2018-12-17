package org.pimslims.servlet.experiment;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTestForExperiments {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTestForExperiments.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.pimslims.servlet.experiment");
        // $JUnit-BEGIN$
        suite.addTestSuite(UpdateInputSamplesTest.class);
        suite.addTestSuite(ViewExperimentTest.class);
        // TODO suite.addTestSuite(ViewExperimentTest.class);
        // $JUnit-END$
        return suite;
    }

}
