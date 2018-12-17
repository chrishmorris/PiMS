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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
import org.pimslims.model.accessControl.User;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.people.Group;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;

public class ListRoleTest extends TestCase {

    private static final String UNIQUE = "er" + System.currentTimeMillis();

    /**
     * USER_NAME String
     */
    private static final String USER_NAME = ListRoleTest.UNIQUE + "user";

    private final AbstractModel model;

    /**
     * @param name
     */
    public ListRoleTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    //
    public final void testDoGetNoHook() throws ServletException, IOException {
        final ListRole servlet = new ListRole();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }

    public final void testDoGet1Child() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        // set up
        String hook = null;
        WritableVersion version = this.model.getTestVersion();
        try {
            final Organisation organisation = new Organisation(version, ListRoleTest.UNIQUE);
            new Group(version, organisation);
            hook = organisation.get_Hook();
            version.commit();

        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        //test
        final ListRole servlet = new ListRole();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        request.setPathInfo("/" + hook + "/" + Organisation.PROP_GROUPS);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals("Delete?", request.getAttribute("controlHeader"));
        Assert.assertNull(request.getAttribute("control"));
        final List<ModelObjectBean> beans = (List<ModelObjectBean>) request.getAttribute("beans");
        Assert.assertNotNull(beans);
        Assert.assertEquals(1, beans.size());
        Assert.assertNotNull(request.getAttribute("attributes"));
        final Map<String, String> actions = (Map<String, String>) request.getAttribute("actions");
        Assert.assertNotNull(actions);
        Assert.assertTrue(actions.containsKey(beans.iterator().next().getHook()));

        //tidy up
        version = this.model.getTestVersion();
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
        WritableVersion version = this.model.getTestVersion();
        try {
            userHook = new User(version, ListRoleTest.USER_NAME).get_Hook();
            hook = new Organisation(version, ListRoleTest.UNIQUE + "cant").get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        //test
        final ListRole servlet = new ListRole();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(ListRoleTest.USER_NAME);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        request.setPathInfo("/" + hook + "/" + Organisation.PROP_GROUPS);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals("", request.getAttribute("controlHeader"));
        // should we Assert.assertNull(request.getAttribute("control"));
        Assert.assertNotNull(request.getAttribute("beans"));

        //tidy up
        version = this.model.getTestVersion();
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

    public final void notYetTestDoPostNothing() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        // set up
        String hook = null;
        WritableVersion version = this.model.getTestVersion();
        try {
            hook = new Organisation(version, ListRoleTest.UNIQUE + "p").get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        //test
        final ListRole servlet = new ListRole();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("post", session, Collections.EMPTY_MAP);
        request.setPathInfo("/" + hook + "/" + Organisation.PROP_GROUPS);
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_SEE_OTHER, response.getStatus());

        //tidy up
        version = this.model.getTestVersion();
        try {
            final ModelObject modelObject = version.get(hook);
            modelObject.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public final void notYetTestDoPostRemove() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        // set up
        String targetHook = null;
        String dnaHook = null;
        WritableVersion version = this.model.getTestVersion();
        try {
            final Molecule protein = new Molecule(version, "protein", ListRoleTest.UNIQUE + "p");
            final Target target = new Target(version, ListRoleTest.UNIQUE + "p", protein);
            targetHook = target.get_Hook();
            final Molecule dna = new Molecule(version, "DNA", ListRoleTest.UNIQUE + "dna");
            dnaHook = dna.get_Hook();
            target.addNucleicAcid(dna);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        //test
        final ListRole servlet = new ListRole();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map parms = new HashMap();
        parms.put("remove", new String[] { dnaHook });
        final MockHttpServletRequest request = new MockHttpServletRequest("post", session, parms);
        request.setPathInfo("/" + targetHook + "/" + Target.PROP_NUCLEICACIDS);
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_SEE_OTHER, response.getStatus());

        //tidy up
        version = this.model.getTestVersion();
        try {
            final Target target = version.get(targetHook);
            final Molecule protein = target.getProtein();
            Assert.assertEquals(0, target.getNucleicAcids().size());
            target.delete();
            final ModelObject dna = version.get(dnaHook);
            dna.delete();
            protein.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }
}
