/**
 * Rhombix_Impl org.pimslims.rhombix AllTests.java
 * 
 * @author cm65
 * @date 13 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.implementation;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.rhombix.command.ImporterTest;

/**
 * AllTests
 * 
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.pimslims.rhombix");
        //$JUnit-BEGIN$
        suite.addTestSuite(ScreenViewImplTest.class);
        suite.addTestSuite(ScreenServiceImplTest.class);
        suite.addTestSuite(ConstructServiceImplTest.class);
        suite.addTestSuite(ImagerServiceImplTest.class);
        suite.addTestSuite(PlateInspectionServiceImplTest.class);
        // needs tunnel to Rhombix installation suite.addTestSuite(RhombixDataStorageImplTest.class);
        suite.addTestSuite(TrialServiceImplTest.class);
        suite.addTestSuite(SampleServiceImplTest.class);
        suite.addTestSuite(ImageSaveAndFindImplTest.class);
        suite.addTestSuite(ImporterTest.class);

        //$JUnit-END$
        return suite;
    }

}
