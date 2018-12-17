/**
 * V4_0-web org.pimslims.servlet.plateExperiment SearchPlateTest.java
 * 
 * @author cm65
 * @date 23 Mar 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.plateExperiment;

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
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.servlet.QuickSearch;

/**
 * SearchPlateTest
 * 
 */
public class SearchPlateTest extends TestCase {

    private final AbstractModel model;

    /**
     * @param name
     */
    public SearchPlateTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public final void testDoGet() throws ServletException, IOException {
        final SearchPlate servlet = new SearchPlate();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        request.setQueryString("SUBMIT=Search");

        final long start = System.currentTimeMillis();
        servlet.doGet(request, response);
        final long time = System.currentTimeMillis() - start;
        Assert.assertTrue("Too long: " + time + "ms", time < 10000);
        System.out.println(this.getClass().getName() + ": " + time + "ms");

        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertNull(request.getAttribute("noSearch"));
        Assert.assertTrue(0 <= (Integer) request.getAttribute("resultSize"));
        //Assert.assertEquals(new Integer(1), request.getAttribute("pagenumber"));
        Assert.assertEquals(new Integer(20), request.getAttribute("pagesize"));
    }

    public final void testDoGetNone() throws ServletException, IOException {
        final SearchPlate servlet = new SearchPlate();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map parms = new HashMap();
        parms.put("name", new String[] { "nonesuch" });
        parms.put("SUBMIT", new String[] { "search" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);

        servlet.doGet(request, response);

        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertNull(request.getAttribute("noSearch"));
        Assert.assertEquals(new Integer(0), request.getAttribute("resultSize"));
        //Assert.assertEquals(new Integer(1), request.getAttribute("pagenumber"));
        Assert.assertEquals(new Integer(20), request.getAttribute("pagesize"));
    }

    public final void testSearchAllNone() throws ServletException, IOException {
        final SearchPlate servlet = new SearchPlate();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map parms = new HashMap();
        parms.put(QuickSearch.SEARCH_ALL, new String[] { "nonesuch" });
        parms.put("SUBMIT", new String[] { "Quick Search" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);

        servlet.doGet(request, response);

        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertNull(request.getAttribute("noSearch"));
        Assert.assertEquals(new Integer(0), request.getAttribute("resultSize"));
        //Assert.assertEquals(new Integer(1), request.getAttribute("pagenumber"));
        Assert.assertEquals(new Integer(20), request.getAttribute("pagesize"));
    }

    public final void testDoGetNoGroups() throws ServletException, IOException {
        final SearchPlate servlet = new SearchPlate();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map parms = new HashMap();
        parms.put("name", new String[] { "nonesuch" });
        parms.put("_only_experiment_groups", new String[] { "true" });
        parms.put("SUBMIT", new String[] { "search" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);

        servlet.doGet(request, response);

        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertNull(request.getAttribute("noSearch"));
        Assert.assertEquals(new Integer(0), request.getAttribute("resultSize"));
        //Assert.assertEquals(new Integer(1), request.getAttribute("pagenumber"));
        Assert.assertEquals(new Integer(20), request.getAttribute("pagesize"));
    }

}
