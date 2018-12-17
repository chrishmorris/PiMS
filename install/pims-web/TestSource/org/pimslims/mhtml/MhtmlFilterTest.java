/**
 * V5_0-web org.pimslims.mhtml MhtmlFilterTest.java
 * 
 * @author cm65
 * @date 16 Jul 2013
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.mhtml;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.regex.Matcher;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletContext;

/**
 * MhtmlFilterTest
 * 
 */
public class MhtmlFilterTest extends TestCase {

    /**
     * Constructor for MhtmlFilterTest
     * 
     * @param name
     */
    public MhtmlFilterTest(final String name) {
        super(name);
    }

    public void testExtract() {
        final Matcher m =
            MhtmlFilter.WRAPPED_URI
                .matcher("/beta/Save/beta/Search/org.pimslims.model.target.Target/PiMS_Report.doc");
        Assert.assertTrue(m.matches());
        Assert.assertEquals("/Search/org.pimslims.model.target.Target", m.group(1));
    }

    public void test() throws ServletException, IOException {
        final javax.servlet.Filter filter = new MhtmlFilter();
        filter.init(this.getFilterConfig());
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        request.setRequestURI("/pims/Save/Search/org.pimslims.model.experiment.Experiment/Report.doc");
        final MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, this.getFilterChain());
        // could use dummy request dispatcher
        Assert
            .assertEquals("attachment; filename=PiMS_Report.doc", response.getHeader("Content-Disposition"));
        // or maybe xls for search results
    }

    public void testInline() throws ServletException, IOException {
        final javax.servlet.Filter filter = new MhtmlFilter();
        filter.init(this.getFilterConfig());
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        request.setRequestURI("/pims/Save/Search/org.pimslims.model.experiment.Experiment/Report.doc");
        final MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, this.getFilterChain());
        // could use dummy request dispatcher
        final String output = response.getOutput();
        Assert.assertTrue(output.contains("Content-Location: /pims/skins/commercial/header_logo.png"));
        Assert.assertTrue(output.contains("Content-Type: text/css"));
    }

    /**
     * MhtmlFilterTest.getFilterChain
     * 
     * @return
     */
    private FilterChain getFilterChain() {
        final FilterChain ret = new FilterChain() {

            @Override
            public void doFilter(final ServletRequest request, final ServletResponse response)
                throws IOException, ServletException {
                Assert.fail("not forwarded");
            }
        };
        return ret;
    }

    /**
     * MhtmlFilterTest.getFilterConfig
     * 
     * @return
     */
    private FilterConfig getFilterConfig() {
        final FilterConfig ret = new FilterConfig() {

            @Override
            public String getFilterName() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getInitParameter(final String arg0) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Enumeration getInitParameterNames() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public ServletContext getServletContext() {
                return new MockServletContext();
            }
        };
        return ret;
    }

}
