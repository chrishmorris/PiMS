/**
 * pims-model org.pimslims.access AccessTestSuite.java
 * 
 * @author pajanne
 * @date Jun 22, 2009
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.access;

import java.io.FileNotFoundException;

import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.pimslims.dao.ModelImpl;

/**
 * AccessTestSuite
 * 
 */
public class AccessTestSuite extends TestSuite {

    public AccessTestSuite() {
        super("Testing the implementation of org.pimslims.access");
    }

    /**
     * @return test suite
     */
    public static junit.framework.Test suite() {
        final junit.framework.TestSuite suite = new AccessTestSuite();

        // AccessImplTest should be run separaterly
        // suite.addTestSuite(AccessImplTester.class); 
        suite.addTestSuite(AccessAllTester.class);
        suite.addTestSuite(ChildAccessTest.class);
        suite.addTestSuite(ConfidentialityTester.class);
        suite.addTestSuite(RoleAccessTest.class);
        return suite;
    }

    public static void oneTimeTearDown() {
        /* could HibernateUtilTester.showStatistics(model);
         final Statistics stat = HibernateUtilTester.getStatistics();
         // make sure every session is closed
         assert stat.getSessionCloseCount() == stat.getSessionOpenCount(); */

    }

    /**
     * Runs these tests from the command line
     * 
     * @param args ignored
     */
    public static void main(final String[] args) {

        try {
            ModelImpl.getModel(new java.io.File("conf/Properties"));
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        TestRunner.run(suite());
    }

}
