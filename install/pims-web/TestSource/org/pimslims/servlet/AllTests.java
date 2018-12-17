package org.pimslims.servlet;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.servlet.remote.RemoteTests;

public class AllTests {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for org.pimslims.servlet");
        // $JUnit-BEGIN$
        suite.addTest(LocalTests.suite());
        suite.addTest(RemoteTests.suite());
        // $JUnit-END$
        return suite;
    }

}
