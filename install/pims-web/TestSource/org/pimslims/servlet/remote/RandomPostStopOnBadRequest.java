package org.pimslims.servlet.remote;

import java.net.MalformedURLException;

import junit.framework.Test;
import junit.framework.TestSuite;

public class RandomPostStopOnBadRequest extends TestToFirstError {

    /**
     * We need a suite method here, because JUnit's introspection gets confused by subclasses.
     * 
     * @return a suite containing the test to run
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        try {
            suite.addTest(new RandomPostStopOnBadRequest("test"));
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return suite;
    }

    public RandomPostStopOnBadRequest(final String methodName) throws MalformedURLException {
        super(methodName, 1197556772324L); //TODO remove seed
    }

    @Override
    protected boolean stopOnBadRequest() {
        return true;
    }

}
