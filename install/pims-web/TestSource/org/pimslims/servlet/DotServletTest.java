/**
 * V5_0-web org.pimslims.servlet DotServletTest.java
 * 
 * @author cm65
 * @date 13 Jun 2013
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;

/**
 * DotServletTest
 * 
 */
public class DotServletTest extends TestCase {

    private static final String UNIQUE = "dot" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for DotServletTest
     * 
     * @param name
     */
    public DotServletTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testDoGet() throws ConstraintException, AbortedException, ServletException, IOException,
        AccessException {
        // set up
        String hook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            hook = new Sample(version, DotServletTest.UNIQUE).get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        //test
        final DotServlet servlet = new DotServlet();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put("format", new String[] { "cmapx" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setHeader("Accept", "");
        request.setPathInfo("/" + hook);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        final String output = response.getOutput();
        Assert.assertFalse(output.contains("\\\\"));
        System.out.println(output); //TODO remove

        //tidy up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ModelObject object = version.get(hook);
            object.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
