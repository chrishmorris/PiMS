/**
 * pims-web org.pimslims.servlet FindJspTest.java
 * 
 * @author cm65
 * @date 27 Aug 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockServletConfig;

/**
 * FindJspTest
 * 
 */
public class FindJspTest extends TestCase {

    private final AbstractModel model;

    /**
     * @param methodName name to test method to run
     */
    public FindJspTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    public void testDispatchCustomJsp() throws ServletException, IOException {
        final FindJsp servlet = new FindJsp();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap();
        parms.put(FindJsp.JSP_TO_FIND, new String[] { "/view/org.pimslims.model.target.Milestone.jsp" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        final HttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(request, response);
        Assert.assertEquals("/JSP/view/org.pimslims.model.target.Milestone.jsp", request.getDispatchedPath());
    }

    public void testDispatchDefaultJsp() throws ServletException, IOException {
        final FindJsp servlet = new FindJsp();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap();
        parms.put(FindJsp.JSP_TO_FIND, new String[] { "/search/org.pimslims.model.target.Milestone.jsp" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        final HttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(request, response);
        Assert.assertEquals("/JSP/search/Default.jsp", request.getDispatchedPath());
    }
}
