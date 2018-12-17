/**
 * V4_0-web org.pimslims.servlet ChooseForCreateTest.java
 * 
 * @author cm65
 * @date 25 Jan 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.util.Collection;
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
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;

/**
 * ChooseForCreateTest
 * 
 */
public class ChooseForCreateTest extends TestCase {

    private static final String UNIQUE = "cc" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for ChooseForCreateTest
     * 
     * @param name
     */
    public ChooseForCreateTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public final void testDoGetNoClass() throws IOException {
        final ChooseForCreate servlet = new ChooseForCreate();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        try {
            servlet.doGet(request, response);
        } catch (final ServletException e) {
            // that's fine;
        }
    }

    public final void testDoGetNoRole() throws ServletException, IOException {
        final ChooseForCreate servlet = new ChooseForCreate();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        request.setPathInfo("/" + Sample.class.getName());
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }

    public final void testDoGet() throws ServletException, IOException {
        final ChooseForCreate servlet = new ChooseForCreate();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        request.setPathInfo("/" + Sample.class.getName() + "/" + Sample.PROP_REFSAMPLE);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals("/JSP/ChooseForCreate.jsp", request.getDispatchedPath());
        final Collection<ModelObjectBean> beans = (Collection<ModelObjectBean>) request.getAttribute("beans");
        Assert.assertNotNull(beans);
        Assert.assertFalse(1 == beans.size());
        final ModelObjectBean bean = beans.iterator().next();
        Assert.assertEquals(RefSample.class.getName(), bean.getClassName());
        final Map<String, String> actions = (Map<String, String>) request.getAttribute("actions");
        Assert.assertNotNull(actions);
        Assert.assertTrue(actions.containsKey(bean.getHook()));
        Assert.assertEquals(request.getAttribute("totalRecords"), request.getAttribute("resultSize"));
    }

    public final void testDoGetRO() throws ServletException, IOException {
        final ChooseForCreate servlet = new ChooseForCreate();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        request.setPathInfo("/" + Experiment.class.getName() + "/" + Experiment.PROP_PROJECT);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals("/JSP/ChooseForCreate.jsp", request.getDispatchedPath());
        final Collection<ModelObjectBean> beans = (Collection<ModelObjectBean>) request.getAttribute("beans");
        Assert.assertNotNull(beans);
        Assert.assertFalse(1 == beans.size());
        final ModelObjectBean bean = beans.iterator().next();
        Assert.assertEquals(ResearchObjective.class.getName(), bean.getClassName());
        final Map<String, String> actions = (Map<String, String>) request.getAttribute("actions");
        Assert.assertNotNull(actions);
        Assert.assertTrue(actions.containsKey(bean.getHook()));
        Assert.assertEquals(request.getAttribute("totalRecords"), request.getAttribute("resultSize"));
    }

    public final void testDoGetOne() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        final String hook1, hook2;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            hook1 = new RefSample(version, ChooseForCreateTest.UNIQUE + "one").get_Hook();
            hook2 = new RefSample(version, ChooseForCreateTest.UNIQUE + "two").get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        final ChooseForCreate servlet = new ChooseForCreate();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put("search_all", new String[] { ChooseForCreateTest.UNIQUE });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setPathInfo("/" + Sample.class.getName() + "/" + Sample.PROP_REFSAMPLE);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals("/JSP/ChooseForCreate.jsp", request.getDispatchedPath());
        final Collection<ModelObjectBean> beans = (Collection<ModelObjectBean>) request.getAttribute("beans");
        Assert.assertNotNull(beans);
        Assert.assertTrue(2 == beans.size());
        Assert.assertEquals(new Integer(2), request.getAttribute("resultSize"));

        // clean up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ModelObject mo = version.get(hook1);
            mo.delete();
            final ModelObject mo2 = version.get(hook2);
            mo2.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
