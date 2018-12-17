/*
 * Created on 27-May-2005 @author: Chris Morris
 */
package org.pimslims.servlet.remote;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;

import junit.framework.Test;
import junit.textui.TestRunner;

/**
 * description
 * 
 * TODO accept path parameter
 * 
 * @version 0.1
 */
public class TestHelpPages extends RemoteTest {

    /**
     * @param methodName name of test method to call
     * @throws MalformedURLException
     */
    public TestHelpPages(String methodName) throws MalformedURLException {
        super(methodName, new java.net.URL("http://localhost:8080/V2_0/"));
    }

    /**
     * Get the index page, and check all pages linked from it
     * 
     * @throws IOException
     */
    public void test() throws IOException {
        com.meterware.httpunit.WebResponse index = get("", Collections.EMPTY_MAP);
        try {
            checkPage(index, 15);
            System.out.println(totoalCheckedPage + " pages are checked!");
        } catch (IOException e) {
            fail(e.getMessage());
        } // TODO more depth
    }

    /**
     * @return a suite including all tests for this class
     */
    public static Test suite() {
        return new junit.framework.TestSuite(TestHelpPages.class);
    }

    /**
     * @param args ignored
     */
    public static void main(final String[] args) {
        TestRunner.run(suite());
    }
}
