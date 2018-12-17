/**
 * current-pims-web org.pimslims.servlet.remote TestToFirstError.java
 * 
 * @author cm65
 * @date 26 Feb 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.servlet.remote;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.meterware.httpunit.WebRequest;

/**
 * TestToFirstError
 * 
 */
public class TestToFirstError extends RandomGet {

    /**
     * HtmlParseException
     * 
     */
    public static class HtmlParseException extends RuntimeException {

        /**
         * @param message
         */
        public HtmlParseException(final String message) {
            super(message);
        }

    }

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        try {
            suite.addTest(new TestToFirstError("test"));
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return suite;
    }

    /**
     * @param methodName
     * @param seed
     * @throws MalformedURLException
     */
    public TestToFirstError(final String methodName, final long seed) throws MalformedURLException {
        super(methodName, seed, new java.net.URL("http://localhost:8080/git/"));
    }

    /**
     * @param methodName
     * @throws MalformedURLException
     */
    public TestToFirstError(final String methodName) throws MalformedURLException {
        super(methodName);
    }

    /**
     * @see org.pimslims.servlet.remote.RemoteTest#onHtmlError(java.net.URL, java.lang.String, int, int)
     */
    @Override
    protected void onHtmlError(final URL url, final String msg, final int line, final int column) {
        final String message =
            "HTML error for : " + url + "\n    line: " + line + " column: " + column + "\n   " + msg;
        System.err.println(message);
        //throw new HtmlParseException(message);
    }

    /**
     * Get the first page, and check all pages linked from it
     * 
     * @throws MalformedURLException
     */
    @Override
    public void test() {
        System.out.println(this.getClass().getName());
        com.meterware.httpunit.WebResponse response = null;
        int pages = 1;
        try {
            response = this.get(RandomGet.START_PAGE, Collections.EMPTY_MAP);
            response = this.login(response);

            this.random = new Random(this.seed);
            response = this.get(RandomGet.START_PAGE, Collections.EMPTY_MAP);
            while (pages++ < RandomGet.maxNumberOfPage) {
                try {
                    if (null != response) {
                        this.addHistory(response.getURL().toString());
                    }
                    final WebRequest request = this.chooseNextRequest(response);
                    if (null == request) {
                        // all links from this page have been checked already
                        this.restart(0);
                    }
                    response = this.click(request);
                    if (this.stopOnBadRequest() && null != response
                        && HttpServletResponse.SC_BAD_REQUEST == response.getResponseCode()) {
                        break;
                    }
                } catch (final HtmlParseException e) {
                    if (this.stopOnHtmlError(response, e)) {
                        break;
                    }

                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } //CHECKSTYLE:OFF rate case in which this is appropriate 
        /* catch (final Exception e) {
            e.printStackTrace();
            //CHECKSTYLE:ON
        } */finally {
            this.printHistory(); // show context of error
            System.err.println("error found after : " + pages + " pages, seed was: " + this.seed);
        }

    }

    protected boolean stopOnHtmlError(final com.meterware.httpunit.WebResponse response,
        final HtmlParseException e) {
        return false;
        /* try {
            final String text = response.getText();
            System.out.println(text);
        } catch (final IOException e1) {
            e1.printStackTrace();
        } 
        return true; */
    }

    /**
     * @param pages
     * @param i
     * @param e
     */
    protected boolean onError(final int pages, final int i, final Exception e) {
        System.err.println("error-" + i + " found after : " + pages + " pages, seed was: " + (this.seed + i));
        this.printHistory();
        if (null != e) {
            e.printStackTrace();
        }
        return true;
    }

}
