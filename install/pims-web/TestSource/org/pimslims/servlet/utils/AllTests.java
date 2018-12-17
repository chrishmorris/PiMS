package org.pimslims.servlet.utils;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.presentation.AttributeToHTMLTest;

public class AllTests {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for org.pimslims.servlet.utils");
        // $JUnit-BEGIN$
        suite.addTestSuite(AttributeToHTMLTest.class);
        // TODO suite.addTestSuite(ViewAmountTest.class);
        suite.addTestSuite(RecordsFilterTester.class);
        suite.addTestSuite(ViewAmountTest.class);
        // $JUnit-END$
        return suite;
    }

}
