package org.pimslims;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllToolsTests {
    static public String TestingDataPath = "data";

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(AllToolsTests.suite());
    }

    public static Test suite() {
        //when run all test, do not show logs of ref-data loader
        //AbstractLoader.silent = true;

        if (System.getProperty("TestingDataPath") != null) {
            AllToolsTests.TestingDataPath = System.getProperty("TestingDataPath");
        }
        final TestSuite suite = new TestSuite("Test for org.pimslims");

        suite.addTest(org.pimslims.command.AllTests.suite());
        suite.addTest(org.pimslims.data.AllTests.suite());

        //        suite.addTestSuite(ProtocolXmlConvertorTest.class);

        return suite;
    }

}
