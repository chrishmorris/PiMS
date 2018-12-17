package org.pimslims;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.utils.SVGTest;
import org.pimslims.utils.orders.operon.TestPrimerOrderFormImpl;

public class AllWebTests {
    static public String TestingDataPath = "./data";

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(AllWebTests.suite());
    }

    public static Test suite() {
        //when run all test, do not show logs of ref-data loader
        //AbstractLoader.silent = true;

        if (System.getProperty("TestingDataPath") != null) {
            AllWebTests.TestingDataPath = System.getProperty("TestingDataPath");
        }
        final TestSuite suite = new TestSuite("Test for org.pimslims");
        suite.addTest(uk.ac.sspf.spot.AllTests.suite());

        suite.addTest(org.pimslims.bioinf.AllBioinfTests.suite());

        suite.addTest(org.pimslims.lab.LabTestSuite.suite());

        suite.addTest(uk.ac.sspf.spot.beans.AllTests.suite());
        suite.addTest(org.pimslims.graph.AllTests.suite());

        suite.addTestSuite(TestPrimerOrderFormImpl.class);
        suite.addTestSuite(SVGTest.class);

        suite.addTest(org.pimslims.servlet.LocalTests.suite());
        suite.addTest(org.pimslims.servlet.utils.AllTests.suite());

        suite.addTest(org.pimslims.presentation.AllTests.suite());

        // TODO fix - it has an absolute path in it
        //suite.addTest(org.pimslims.utils.primer3.AllTests.suite());
        //suite.addTestSuite(TestPlasmid.class);
        return suite;
    }

}
