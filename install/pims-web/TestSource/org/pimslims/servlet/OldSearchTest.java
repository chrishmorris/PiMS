/**
 * current-pims-web org.pimslims.servlet SearchTest.java
 * 
 * @author cm65
 * @date 27 Mar 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.servlet.construct.SearchConstruct;
import org.pimslims.servlet.target.SearchTarget;

/**
 * SearchTest
 * 
 */
public class OldSearchTest extends TestCase {

    private static final String UNIQUE = "Search" + System.currentTimeMillis();

    private static final String NAME = "n" + OldSearchTest.UNIQUE;

    private final AbstractModel model;

    /**
     * @param name
     */
    public OldSearchTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public final void testSearchConstructNoParms() throws ServletException, IOException {
        final SearchConstruct servlet = new SearchConstruct();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        request.setPathInfo("/" + ResearchObjective.class.getName());
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertNull(request.getAttribute("noSearch"));
        Assert.assertTrue(0 <= (Integer) request.getAttribute("resultSize"));
    }

    public final void testSearchConstruct() throws ServletException, IOException {
        final SearchConstruct servlet = new SearchConstruct();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        request.setPathInfo("/" + ResearchObjective.class.getName());
        request.setQueryString("SUBMIT=Search");
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertNull(request.getAttribute("noSearch"));
        Assert.assertTrue(0 <= (Integer) request.getAttribute("resultSize"));
    }

    public final void testDoGetMetaClassInParm() throws ServletException, IOException {
        final SearchTarget servlet = new SearchTarget();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put("SUBMIT", new String[] { "Search" });
        parms.put("_metaClass", new String[] { ResearchObjective.class.getName() });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertNull(request.getAttribute("noSearch"));
        Assert.assertTrue(0 <= (Integer) request.getAttribute("resultSize"));
    }

}
