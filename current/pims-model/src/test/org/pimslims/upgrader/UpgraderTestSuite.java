/*
 * Created on 25-Oct-2004 @author: Chris Morris
 */
package org.pimslims.upgrader;

import java.io.FileNotFoundException;

import junit.textui.TestRunner;

import org.pimslims.dao.ModelImpl;

// import org.pimslims.servlet.TestListener;

/**
 * Test suite for the implementation of the PIMS back end. This implementation is based on the hibernate tools
 * code generator.
 */
public class UpgraderTestSuite extends junit.framework.TestSuite {

    /**
     * 
     */
    public UpgraderTestSuite() {
        super("Testing the implementation of Upgrader");
    }

    /**
     * @return test suite
     */
    public static junit.framework.Test suite() {
        junit.framework.TestSuite suite = new UpgraderTestSuite();

        //suite.addTestSuite(TestUpgrader.class);
        // oldsuite.addTestSuite(DataFixerTest.class);
        //suite.addTestSuite(DBDataTest.class);
        suite.addTestSuite(RefDataToolTest.class);
        //suite.addTestSuite(StrangeCharTest.class);
        // needs special database access suite.addTestSuite(TestNormalUpgrader.class);
        suite.addTestSuite(Version34Test.class);
        //suite.addTestSuite(Version38Test.class);
        suite.addTestSuite(Version42Test.class);

        return suite;
    }

    public static void oneTimeTearDown() {
        /* could HibernateUtilTester.showStatistics(this.model);
         Statistics stat = HibernateUtilTester.getStatistics();
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
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        TestRunner.run(suite());
    }
}
