package org.pimslims.servlet.plateExperiment;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.servlet.ajax.UpdatePlateExperimentTest;

public class AllTestForPlateExperiments {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(AllTestForPlateExperiments.suite());
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for org.pimslims.servlet.plateExperiment");
        // $JUnit-BEGIN$
        suite.addTestSuite(CreateExperimentGroupTest.class);
        suite.addTestSuite(ExperimentGroupDataTest.class);
        suite.addTestSuite(UpdatePlateExperimentTest.class);
        suite.addTestSuite(PlateExperimentPerformanceTest.class);
        suite.addTestSuite(OrderPlateTest.class);
        suite.addTestSuite(SearchPlateTest.class);
        suite.addTestSuite(PlateExperimentDAOTest.class);
        // $JUnit-END$
        return suite;
    }

}
