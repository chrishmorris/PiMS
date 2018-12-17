/*
 * Created on 01-Jun-2005 @author: Chris Morris
 */
package org.pimslims.servlet.remote;


import junit.textui.TestRunner;

/**
 * description
 * 
 * Inner type of
 * 
 * @version 0.1
 */
public class RemoteTests extends junit.framework.TestSuite {

    /**
     * 
     */
    public RemoteTests() {
        super("Testing the PIMS servlets, through HTTP connections");
    }

    /**
     * @return test suite
     */
    public static junit.framework.Test suite() {

        junit.framework.TestSuite suite = new RemoteTests();
        // TODO suite.addTest(TestErrorRecovery.suite());

        suite.addTestSuite(TestLogIn.class);
        suite.addTest(TestOrganisation.suite());
        suite.addTest(TestHelpPages.suite());
        return suite;
    }

    /**
     * Runs these tests from the command line
     * 
     * @param args ignored
     */
    public static void main(final String[] args) {
        TestRunner.run(suite());
    }
}
