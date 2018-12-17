package org.pimslims.embl;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.pimslims.embl");
        //$JUnit-BEGIN$
        suite.addTestSuite(OwnerChangerTest.class);
        suite.addTestSuite(HamburgResearchObjectiveBeanTest.class);
        suite.addTestSuite(HamburgParserTest.class);
        //$JUnit-END$
        return suite;
    }

}
