/** 
 * pims-tools org.pimslims.data AllTests.java
 * @author cm65
 * @date 8 Nov 2010
 *
 * Protein Information Management System
 * @version: 3.2
 *
 * Copyright (c) 2010 cm65 
 * The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.data;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * AllTests
 *
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.pimslims.data");
        //$JUnit-BEGIN$
        suite.addTestSuite(XlsReaderTest.class);
        suite.addTestSuite(ExperimentTypeUtilityTester.class);
        suite.addTestSuite(ProtocolUtilityTest.class);
        suite.addTestSuite(DBNameUtilityTester.class);
        suite.addTestSuite(TargetCsvLoaderTest.class);
        suite.addTestSuite(RefDataLoaderTest.class);
        suite.addTestSuite(NaturalSourceUtilityTester.class);
        suite.addTestSuite(ExtensionsUtilityTester.class);
        //$JUnit-END$
        return suite;
    }

}
