/**
 * V3_3-web org.pimslims.servlet.spot SpotConstructMilestoneTest.java
 * 
 * @author cm65
 * @date 1 Mar 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.spot;

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
 * SpotConstructMilestoneTest
 * 
 */
public class SpotConstructMilestoneTest extends TestCase {

    private final AbstractModel model;

    /**
     * @param name
     */
    public SpotConstructMilestoneTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.spot.SpotConstructMilestone#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * .
     */
    public void testDoGetBadRequest() throws ServletException, IOException {
        final SpotConstructMilestone servlet = new SpotConstructMilestone();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.spot.SpotConstructMilestone#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * .
     */
    public void testDoGetNotFound() throws ServletException, IOException {
        final SpotConstructMilestone servlet = new SpotConstructMilestone();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        parms.put("commonName", new String[] { "nonesuch" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.spot.SpotConstructMilestone#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * .
     */
    public void testDoGetSpecific() throws ServletException, IOException {
        final SpotConstructMilestone servlet = new SpotConstructMilestone();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        parms.put("commonName", new String[] { "*** your construct name here ***" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        final MockHttpServletResponse response = new MockHttpServletResponse();

        final long start = System.currentTimeMillis();
        servlet.doGet(request, response);
        final long time = System.currentTimeMillis() - start;
        System.out.println(servlet.getClass().getName() + " using " + time + "ms ");
        Assert.assertTrue("Too long: " + time, time < 3000);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

}
