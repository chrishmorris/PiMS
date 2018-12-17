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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.people.Group;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.presentation.mru.MRUController;

public class EditRoleTest extends TestCase {

    private static final String UNIQUE = "er" + System.currentTimeMillis();

    /**
     * USER_NAME String
     */
    private static final String USER_NAME = EditRoleTest.UNIQUE + "user";

    private final AbstractModel model;

    /**
     * @param name
     */
    public EditRoleTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    //
    public final void testDoGetNoHook() throws ServletException, IOException {
        final EditRole servlet = new EditRole();
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
            final Organisation organisation = new Organisation(version, EditRoleTest.UNIQUE + "g");
            hook = organisation.get_Hook();
            new Group(version, organisation);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        //test
        final EditRole servlet = new EditRole();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map parms = new HashMap();
        parms.put("isInModelWindow", new String[] { "true" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setPathInfo("/" + hook + "/" + Organisation.PROP_GROUPS);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        final Collection found = (Collection) request.getAttribute("addResults");
        //test and tidy up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Organisation organisation = version.get(hook);
            final Group group = organisation.getGroups().iterator().next();
            group.delete();
            organisation.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        Assert.assertTrue(0 < found.size());
    }

    public final void testDoGetSystemClass() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        // set up
        String hook = null;
        final String username = EditRoleTest.UNIQUE;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {

            final UserGroup group = new UserGroup(version, EditRoleTest.UNIQUE);
            group.addMemberUser(new User(version, username));
            final LabNotebook book = new LabNotebook(version, EditRoleTest.UNIQUE);
            new Permission(version, "read", book, group);
            final Sample sample = new Sample(version, EditRoleTest.UNIQUE + "g");
            hook = sample.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        //test
        final EditRole servlet = new EditRole();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(username);
        final Map parms = new HashMap();
        parms.put("isInModelWindow", new String[] { "true" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setPathInfo("/" + hook + "/" + AbstractSample.PROP_SAMPLECATEGORIES);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        final Collection found = (Collection) request.getAttribute("addResults");
        //test and tidy up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Sample organisation = version.get(hook);
            organisation.delete();
            final User user = version.findFirst(User.class, User.PROP_NAME, username);
            final Set<UserGroup> userGroups = user.getUserGroups();
            for (final Iterator iterator = userGroups.iterator(); iterator.hasNext();) {
                final UserGroup userGroup = (UserGroup) iterator.next();
                final Set<Permission> permissions = userGroup.getPermissions();
                for (final Iterator iterator2 = permissions.iterator(); iterator2.hasNext();) {
                    final Permission permission = (Permission) iterator2.next();
                    permission.getLabNotebook().delete();
                }
                userGroup.delete();
            }
            user.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        Assert.assertTrue(0 < found.size());
    }

    public final void testDoGetAdd() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        // set up
        String targetHook = null;
        String dnaHook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Molecule protein = new Molecule(version, "protein", EditRoleTest.UNIQUE + "pAdd");
            final Target target = new Target(version, EditRoleTest.UNIQUE + "p", protein);
            targetHook = target.get_Hook();
            final Molecule dna = new Molecule(version, "DNA", EditRoleTest.UNIQUE + "dna");
            dnaHook = dna.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        MRUController.clearMRUList(Access.ADMINISTRATOR);

        //test
        final EditRole servlet = new EditRole();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map parms = new HashMap();
        parms.put("search_all", new String[] { EditRoleTest.UNIQUE + "dna" });
        final MockHttpServletRequest request = new MockHttpServletRequest("post", session, parms);
        Assert.assertNotNull(request.getQueryString());
        request.setPathInfo("/" + targetHook + "/" + Target.PROP_NUCLEICACIDS);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        final Map<String, String> control = (Map<String, String>) request.getAttribute("addControl");
        Assert.assertNotNull(control);
        Assert.assertTrue(control.containsKey(dnaHook));
        Assert.assertEquals("<span title=\"Add?\"><input type=\"checkbox\" name=\"add\" value=\"" + dnaHook
            + "\" /></span>", control.get(dnaHook));
        final Collection<ModelObjectBean> found = (Collection) request.getAttribute("addResults");
        Assert.assertEquals(1, found.size());
        Assert.assertEquals(dnaHook, found.iterator().next().getHook());

        //tidy up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Target target = version.get(targetHook);
            final Molecule protein = target.getProtein();
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

    public final void testDoGetRemove() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        // set up
        String targetHook = null;
        String dnaHook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Molecule protein = new Molecule(version, "protein", EditRoleTest.UNIQUE + "pAdd");
            final Target target = new Target(version, EditRoleTest.UNIQUE + "p", protein);
            targetHook = target.get_Hook();
            final Molecule dna = new Molecule(version, "DNA", EditRoleTest.UNIQUE + "dna");
            dnaHook = dna.get_Hook();
            target.addNucleicAcid(dna);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        // was MRUController.clearMRUList(Access.ADMINISTRATOR);

        //test
        final EditRole servlet = new EditRole();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map parms = new HashMap();
        parms.put("search_all", new String[] { EditRoleTest.UNIQUE + "dna" });
        final MockHttpServletRequest request = new MockHttpServletRequest("post", session, parms);
        Assert.assertNotNull(request.getQueryString());
        request.setPathInfo("/" + targetHook + "/" + Target.PROP_NUCLEICACIDS);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        final Map<String, String> control = (Map<String, String>) request.getAttribute("addControl");
        Assert.assertNotNull(control);
        Assert.assertTrue(control.containsKey(dnaHook));
        Assert.assertTrue(control.get(dnaHook).contains("Already added"));
        final Collection<ModelObjectBean> found = (Collection) request.getAttribute("addResults");
        Assert.assertEquals(1, found.size());
        Assert.assertEquals(dnaHook, found.iterator().next().getHook());

        //tidy up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Target target = version.get(targetHook);
            final Molecule protein = target.getProtein();
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

    public final void testPaging() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        // set up
        String targetHook = null;
        String dnaHook = null;
        String dna2Hook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Molecule protein = new Molecule(version, "protein", EditRoleTest.UNIQUE + "pAdd1");
            final Target target = new Target(version, EditRoleTest.UNIQUE + "p1", protein);
            targetHook = target.get_Hook();
            final Molecule dna = new Molecule(version, "DNA", EditRoleTest.UNIQUE + "dnap");
            dnaHook = dna.get_Hook();
            final Molecule dna2 = new Molecule(version, "DNA", EditRoleTest.UNIQUE + "dna2");
            dna2Hook = dna2.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        MRUController.clearMRUList(Access.ADMINISTRATOR);

        //test
        final EditRole servlet = new EditRole();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map parms = new HashMap();
        parms.put("search_all", new String[] { EditRoleTest.UNIQUE + "dna" });
        parms.put("pagesize", new String[] { "1" });
        final MockHttpServletRequest request = new MockHttpServletRequest("post", session, parms);
        Assert.assertNotNull(request.getQueryString());
        request.setPathInfo("/" + targetHook + "/" + Target.PROP_NUCLEICACIDS);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        final Collection<ModelObjectBean> found = (Collection) request.getAttribute("addResults");
        Assert.assertEquals(1, found.size());
        Assert.assertEquals(new Integer(2), request.getAttribute("resultSize"));

        //tidy up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Target target = version.get(targetHook);
            final Molecule protein = target.getProtein();
            target.delete();
            ModelObject dna = version.get(dnaHook);
            dna.delete();
            dna = version.get(dna2Hook);
            dna.delete();
            protein.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public final void TODOtestDoGetCant() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        // set up
        String hook = null;
        String userHook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            userHook = new User(version, EditRoleTest.USER_NAME).get_Hook();
            hook = new Organisation(version, EditRoleTest.UNIQUE + "cant").get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        //test
        final EditRole servlet = new EditRole();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(EditRoleTest.USER_NAME);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        request.setPathInfo("/" + hook + "/" + Organisation.PROP_GROUPS);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

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

    public final void testDoPostNothing() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        // set up
        String hook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            hook = new Organisation(version, EditRoleTest.UNIQUE + "p").get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        //test
        final EditRole servlet = new EditRole();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("post", session, Collections.EMPTY_MAP);
        request.setPathInfo("/" + hook + "/" + Organisation.PROP_GROUPS);
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_SEE_OTHER, response.getStatus());

        //tidy up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
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

    public final void testDoPostDelete() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        // set up
        String hook = null;
        String groupHook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Organisation organisation = new Organisation(version, EditRoleTest.UNIQUE + "p");
            hook = organisation.get_Hook();
            final Group group = new Group(version, organisation);
            groupHook = group.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        //test
        final EditRole servlet = new EditRole();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map parms = new HashMap();
        parms.put("delete", new String[] { groupHook });
        final MockHttpServletRequest request = new MockHttpServletRequest("post", session, parms);
        request.setPathInfo("/" + hook + "/" + Organisation.PROP_GROUPS);
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_SEE_OTHER, response.getStatus());

        //tidy up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Assert.assertNull(version.get(groupHook));
            final Organisation organisation = version.get(hook);
            Assert.assertEquals(0, organisation.getGroups().size());
            organisation.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public final void testDoPostRemove() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        // set up
        String targetHook = null;
        String dnaHook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Molecule protein = new Molecule(version, "protein", EditRoleTest.UNIQUE + "p");
            final Target target = new Target(version, EditRoleTest.UNIQUE + "pr", protein);
            targetHook = target.get_Hook();
            final Molecule dna = new Molecule(version, "DNA", EditRoleTest.UNIQUE + "dnar");
            dnaHook = dna.get_Hook();
            target.addNucleicAcid(dna);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        MRUController.clearMRUList(Access.ADMINISTRATOR);

        //test
        final EditRole servlet = new EditRole();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map parms = new HashMap();
        parms.put("remove", new String[] { dnaHook });
        final MockHttpServletRequest request = new MockHttpServletRequest("post", session, parms);
        request.setPathInfo("/" + targetHook + "/" + Target.PROP_NUCLEICACIDS);
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_SEE_OTHER, response.getStatus());
        Assert.assertEquals(1, MRUController.getMRUList(Access.ADMINISTRATOR).getMRUs().size());

        //tidy up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
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

    public final void testDoPostAdd() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        // set up
        String targetHook = null;
        String dnaHook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Molecule protein = new Molecule(version, "protein", EditRoleTest.UNIQUE + "pAddx");
            final Target target = new Target(version, EditRoleTest.UNIQUE + "pa", protein);
            targetHook = target.get_Hook();
            final Molecule dna = new Molecule(version, "DNA", EditRoleTest.UNIQUE + "dnapa");
            dnaHook = dna.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        MRUController.clearMRUList(Access.ADMINISTRATOR);

        //test
        final EditRole servlet = new EditRole();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map parms = new HashMap();
        parms.put("add", new String[] { dnaHook });
        final MockHttpServletRequest request = new MockHttpServletRequest("post", session, parms);
        request.setPathInfo("/" + targetHook + "/" + Target.PROP_NUCLEICACIDS);
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_SEE_OTHER, response.getStatus());

        Assert.assertEquals(1, MRUController.getMRUList(Access.ADMINISTRATOR).getMRUs().size());

        //tidy up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Target target = version.get(targetHook);
            final Molecule protein = target.getProtein();
            Assert.assertEquals(1, target.getNucleicAcids().size());
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
