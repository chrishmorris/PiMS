package org.pimslims.crystallization;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.pimslims.crystallization.servlet.ViewByNameTest;

public class AllTests extends TestCase {

    public AllTests() {
        super("All performance tests ");
    }

    /**
     * @return test suite
     */
    public static junit.framework.Test suite() {
        final junit.framework.TestSuite suite = new TestSuite();

        suite.addTestSuite(ViewByNameTest.class);
        return suite;
    }
}
