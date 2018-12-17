/**
 * current-pims-web org.pimslims.servlet DeleteTest.java
 * 
 * 
 * Copyright (c) 2008 cm65
 * 
 * 
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
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.Database;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.presentation.CsrfDefence;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;

public class DeleteTest extends TestCase {

    private static final String UNIQUE = "d" + System.currentTimeMillis();

    /**
     * USER_NAME String
     */
    private static final String USER_NAME = DeleteTest.UNIQUE + "user";

    /**
     * PARENT_NAME String
     */
    private static final String PARENT_NAME = DeleteTest.UNIQUE + "parent";

    //private static final String NAME = "n" + DeleteTest.UNIQUE;

    private final AbstractModel model;

    /**
     * @param name
     */
    public DeleteTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public final void testDoGetNoHook() throws ServletException, IOException {
        final Delete servlet = new Delete();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }

    public final void testDoGet() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        // set up
        String hook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            hook = new Organisation(version, DeleteTest.UNIQUE).get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        //test
        final Delete servlet = new Delete();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        request.setPathInfo("/" + hook);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals(1, ((Collection) request.getAttribute("modelObjects")).size());
        Assert.assertEquals(0, ((Collection) request.getAttribute("cantDelete")).size());

        final String output = response.getOutput();
        System.out.println(output);

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

    public final void testDoGetCant() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        // set up
        String hook = null;
        String userHook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            userHook = new User(version, DeleteTest.USER_NAME).get_Hook();
            hook = new Organisation(version, DeleteTest.UNIQUE).get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        //test
        final Delete servlet = new Delete();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(DeleteTest.USER_NAME);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        request.setPathInfo("/" + hook);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals(0, ((Collection) request.getAttribute("modelObjects")).size());
        Assert.assertEquals(1, ((Collection) request.getAttribute("cantDelete")).size());

        //tidy up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ModelObject object = version.get(hook);
            object.delete();
            final ModelObject user = version.get(userHook);
            user.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public final void testDoGet2() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        // set up
        String hook1 = null;
        String hook2 = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            hook1 = new Organisation(version, DeleteTest.UNIQUE + "g1").get_Hook();
            hook2 = new Organisation(version, DeleteTest.UNIQUE + "g2").get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        //test
        final Delete servlet = new Delete();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap(1);
        parms.put("hook", new String[] { hook1, hook2 });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals(2, ((Collection) request.getAttribute("modelObjects")).size());

        //tidy up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            ModelObject object = version.get(hook1);
            object.delete();
            object = version.get(hook2);
            object.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public final void testDoPostNoHook() throws ServletException, IOException {
        final Delete servlet = new Delete();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        final MockHttpServletRequest request = new MockHttpServletRequest("post", session, parms);
        final String token = CsrfDefence.getReusableToken(request, "/Delete");
        parms.put(CsrfDefence.PARAMETER, new String[] { token });
        request.setServletPath("/Delete");
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
    }

    public final void testDoPost() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        // set up
        String hook = null;
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            hook = new Organisation(version, DeleteTest.UNIQUE + "p").get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        //test
        final Delete servlet = new Delete();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        final MockHttpServletRequest request = new MockHttpServletRequest("post", session, parms);
        request.setQueryString(hook);
        final String token = CsrfDefence.getReusableToken(request, "/Delete");
        parms.put(CsrfDefence.PARAMETER, new String[] { token });
        request.setServletPath("/Delete");
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals("text/html;charset=utf-8", response.getContentType());
        final String output = response.getOutput();
        Assert.assertTrue(output.contains("has been deleted from PIMS"));
        Assert.assertTrue(output, output.contains(DeleteTest.UNIQUE + "p"));

        //tidy up
        final ReadableVersion rv = this.model.getReadableVersion(Access.ADMINISTRATOR);
        try {
            Assert.assertNull(rv.get(hook));
            rv.commit();
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    public final void testDbRef() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        // set up
        String hook = null;
        String hook2 = null;
        String hook3 = null;
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Organisation organisation = new Organisation(version, DeleteTest.UNIQUE + "r");
            final Database db = new Database(version, DeleteTest.UNIQUE);
            final ExternalDbLink link = new ExternalDbLink(version, db, organisation);
            hook = link.get_Hook();
            hook2 = organisation.get_Hook();
            hook3 = db.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        //test
        final Delete servlet = new Delete();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        final MockHttpServletRequest request = new MockHttpServletRequest("post", session, parms);
        request.setQueryString(hook);
        final String token = CsrfDefence.getReusableToken(request, "/Delete");
        parms.put(CsrfDefence.PARAMETER, new String[] { token });
        request.setServletPath("/Delete");
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals("text/html;charset=utf-8", response.getContentType());
        final String output = response.getOutput();
        Assert.assertTrue(output.contains("has been deleted from PIMS"));

        // tidy up
        final WritableVersion rv = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Assert.assertNull(rv.get(hook));
            final ModelObject object2 = rv.get(hook2);
            object2.delete();
            final ModelObject object3 = rv.get(hook3);
            object3.delete();
            rv.commit();
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    public final void testParent() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        // set up
        String hook = null;
        String protocolHook = null;
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ExperimentType type = new ExperimentType(version, DeleteTest.UNIQUE);
            final Protocol protocol = new Protocol(version, DeleteTest.PARENT_NAME, type);
            hook = new ParameterDefinition(version, DeleteTest.UNIQUE, "String", protocol).get_Hook();
            protocolHook = protocol.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        //test
        final Delete servlet = new Delete();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        final MockHttpServletRequest request = new MockHttpServletRequest("post", session, parms);
        final String token = CsrfDefence.getReusableToken(request, "/Delete");
        parms.put(CsrfDefence.PARAMETER, new String[] { token });
        request.setServletPath("/Delete");
        request.setQueryString(hook);
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals("text/html;charset=utf-8", response.getContentType());
        final String output = response.getOutput();
        Assert.assertTrue(output, output.contains(DeleteTest.PARENT_NAME));

        //tidy up
        final WritableVersion rv = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Assert.assertNull(rv.get(hook));
            final Protocol protocol = rv.get(protocolHook);
            final ExperimentType type = protocol.getExperimentType();
            protocol.delete();
            type.delete();
            rv.commit();
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    public final void testAjax() throws ServletException, IOException, ConstraintException, AbortedException,
        AccessException {
        // set up
        String hook1 = null;
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            hook1 = new Organisation(version, DeleteTest.UNIQUE + "a").get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        //test
        final Delete servlet = new Delete();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap(2);
        parms.put("hook", new String[] { hook1 });
        parms.put("isAJAX", new String[] { "true" });
        final MockHttpServletRequest request = new MockHttpServletRequest("post", session, parms);
        final String token = CsrfDefence.getReusableToken(request, "/Delete");
        parms.put(CsrfDefence.PARAMETER, new String[] { token });
        request.setServletPath("/Delete");
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals("text/xml", response.getContentType());
        final String output = response.getOutput();
        Assert.assertTrue(output.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<deleted>"));
        output.contains("<object hook=\"" + hook1 + "\" />");
        Assert.assertTrue(output, output.endsWith("</deleted>\r\n"));

        final ReadableVersion rv = this.model.getReadableVersion(Access.ADMINISTRATOR);
        try {
            Assert.assertNull(rv.get(hook1));
            rv.commit();
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    public final void testDoPost2() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        // set up
        String hook1 = null;
        String hook2 = null;
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            hook1 = new Organisation(version, DeleteTest.UNIQUE + "p1").get_Hook();
            hook2 = new Organisation(version, DeleteTest.UNIQUE + "p2").get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        //test
        final Delete servlet = new Delete();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap(1);
        parms.put("hook", new String[] { hook1, hook2 });
        final MockHttpServletRequest request = new MockHttpServletRequest("post", session, parms);
        final String token = CsrfDefence.getReusableToken(request, "/Delete");
        parms.put(CsrfDefence.PARAMETER, new String[] { token });
        request.setServletPath("/Delete");
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        final ReadableVersion rv = this.model.getReadableVersion(Access.ADMINISTRATOR);
        try {
            Assert.assertNull(rv.get(hook1));
            Assert.assertNull(rv.get(hook2));
            rv.commit();
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

}
