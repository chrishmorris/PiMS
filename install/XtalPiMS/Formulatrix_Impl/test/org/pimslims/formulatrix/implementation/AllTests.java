/**
 *  AllTests.java
 * 
 * @author cm65
 * @date 13 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.formulatrix.implementation;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.formulatrix.command.ImporterTest;

/**
 * AllTests
 * 
 */
public class AllTests {
    
    //MAYBE get all these working

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for connection to manufacturer's database");
        //$JUnit-BEGIN$
        suite.addTestSuite(PlateInspectionServiceImplTest.class);
        suite.addTestSuite(TrialServiceImplTest.class);
        suite.addTestSuite(ImageSaveAndFindImplTest.class);
        
        suite.addTestSuite(LocationServiceImplTest.class);
        suite.addTestSuite(ImporterTest.class);
        
        //MAYBE suite.addTestSuite(ScreenViewImplTest.class);
        //MAYBE suite.addTestSuite(ScreenServiceImplTest.class);
        // MAYBE suite.addTestSuite(DataStorageImplTest.class);

        //$JUnit-END$
        return suite;
    }

}
