package org.pimslims.servlet.remote;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Random;

import junit.framework.AssertionFailedError;
import junit.framework.Test;

import com.meterware.httpunit.WebRequest;

public class StaticLinkTest extends RandomGet {

    public StaticLinkTest(final String methodName) throws MalformedURLException {
        super(methodName);
    }

    /**
     * Get the first page, and check all pages linked from it
     */
    @Override
    public void test() {

        com.meterware.httpunit.WebResponse response;
        try {
            response = this.get(RandomGet.START_PAGE, Collections.EMPTY_MAP);
        } catch (final IOException e1) {
            throw new RuntimeException(e1);
        }
        response = this.login(response);
        int pages = 1;
        for (int i = 0; i < RandomGet.maxNumberOfError; i++) {
            this.random = new Random(this.seed + i);
            try {
                response = this.get(RandomGet.START_PAGE, Collections.EMPTY_MAP);
                while (true) {
                    while (true) {
                        final WebRequest request = this.chooseNextRequest(response);
                        if (null == request) {
                            break;
                        }
                        response = this.click(request);
                        pages++;
                        if (pages > RandomGet.maxNumberOfPage) {
                            return;
                        }
                    }
                    // start again
                    this.restart(-1);
                }
            } catch (final AssertionFailedError e) {
                this.printHistory();
                System.err.println(e.getLocalizedMessage());
                e.printStackTrace();
                System.err.println("error-" + i + " found after : " + pages + " pages, seed was: "
                    + (this.seed + i));

                // CHECKSTYLE:OFF catch Exception here to chow history
            } catch (final Exception e) {
                this.printHistory();
                System.err.println(e.getLocalizedMessage());
                e.printStackTrace();
                System.err.println("error-" + i + " found after : " + pages + " pages, seed was: "
                    + (this.seed + i));
            }
            // CHECKSTYLE:ON
        }
    }

    @Override
    public boolean isStaticTest() {
        return true;
    }

    /**
     * @return a suite including all tests for this class
     */
    public static Test suite() {
        return new junit.framework.TestSuite(StaticLinkTest.class);
    }

}
