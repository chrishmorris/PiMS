/**
 * V4_0-web org.pimslims.servlet.access CreateUserTest.java
 * 
 * @author cm65
 * @date 16 Mar 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.access;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.people.Person;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.sec.License;
import org.pimslims.servlet.PasswordChange;

/**
 * CreateUserTest
 * 
 */
public class CreateUserTest extends TestCase {

    private static final String UNIQUE = "cu" + System.currentTimeMillis();

    private final AbstractModel model;

    private final String userName = CreateUserTest.UNIQUE;

    /**
     * Constructor for CreateUserTest
     * 
     * @param name
     */
    public CreateUserTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * CreateUserTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final UserGroup group = new UserGroup(version, CreateUserTest.UNIQUE + "leaders");
            group.addMemberUser(new User(version, this.userName));
            final LabNotebook book = new LabNotebook(version, CreateUserTest.UNIQUE);
            new org.pimslims.model.accessControl.Permission(version, "create", book, group);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * CreateUserTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final User user = version.findFirst(User.class, User.PROP_NAME, this.userName);
            if (null != user) {
                user.delete();
                final Set<UserGroup> groups = user.getUserGroups();
                for (final Iterator iterator = groups.iterator(); iterator.hasNext();) {
                    final UserGroup group = (UserGroup) iterator.next();
                    final Set<Permission> permissions = group.getPermissions();
                    for (final Iterator iterator2 = permissions.iterator(); iterator2.hasNext();) {
                        final Permission permission = (Permission) iterator2.next();
                        permission.getLabNotebook().delete();
                    }
                    group.delete();
                }
            }
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        super.tearDown();
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.access.CreateUser#create(org.pimslims.metamodel.MetaClass, java.util.Map, org.pimslims.dao.WritableVersion)}
     * .
     */
    public void testCreate() throws AccessException, ConstraintException, ServletException {
        final MetaClass personMetaClass = this.model.getMetaClass(Person.class.getName());
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map map = new HashMap();
            map.put(Person.class.getName() + ":" + Person.PROP_FAMILYNAME,
                new String[] { CreateUserTest.UNIQUE });
            CreateUser.create(personMetaClass, map, version);
        } finally {
            version.abort();
        }
    }

    public void testOrdinaryUserCantCreate() throws AccessException, ConstraintException, ServletException {
        final MetaClass personMetaClass = this.model.getMetaClass(User.class.getName());
        final WritableVersion version = this.model.getWritableVersion(this.userName);
        try {
            final Map map = new HashMap();
            map.put(Person.class.getName() + ":" + Person.PROP_FAMILYNAME,
                new String[] { CreateUserTest.UNIQUE });
            CreateUser.create(personMetaClass, map, version);
            Assert.fail("Security error, this user not permitted to make new userids");
        } catch (final AssertionError e) {
            // that's great
        } finally {
            version.abort();
        }
    }

    public void TODOtestLeaderCanCreate() throws AccessException, ConstraintException, ServletException,
        AbortedException {
        String groupHook = null;
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final User user = version.findFirst(User.class, User.PROP_NAME, this.userName);

            final Set<UserGroup> groups = user.getUserGroups();
            for (final Iterator iterator = groups.iterator(); iterator.hasNext();) {
                final UserGroup group = (UserGroup) iterator.next();
                group.setHeader(user);
                groupHook = group.get_Hook();
            }

            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final MetaClass personMetaClass = this.model.getMetaClass(User.class.getName());
        final WritableVersion version1 = this.model.getWritableVersion(this.userName);
        try {
            final Map map = new HashMap();
            map.put(User.class.getName() + ":" + User.PROP_NAME, new String[] { CreateUserTest.UNIQUE
                + "member" });
            map.put(User.class.getName() + ":" + User.PROP_USERGROUPS, new String[] { groupHook });
            final ModelObject user = CreateUser.create(personMetaClass, map, version1);
            final UserGroup group = version1.get(groupHook);
            Assert.assertTrue(group.getMemberUsers().contains(user));
            Assert.assertTrue(PasswordChange.isLeader(user.get_Name(), this.userName, version1));
        } finally {
            version1.abort();
        }
    }

    public void testLeaderCantCreateTooMany() throws AccessException, ConstraintException, ServletException,
        AbortedException {
        String groupHook = null;
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final User user = version.findFirst(User.class, User.PROP_NAME, this.userName);

            final Set<UserGroup> groups = user.getUserGroups();
            for (final Iterator iterator = groups.iterator(); iterator.hasNext();) {
                final UserGroup group = (UserGroup) iterator.next();
                group.setHeader(user);
                groupHook = group.get_Hook();
                group.setMaxSize(0);
            }

            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final MetaClass personMetaClass = this.model.getMetaClass(Person.class.getName());
        final WritableVersion version1 = this.model.getWritableVersion(this.userName);
        try {
            final Map map = new HashMap();
            map.put(Person.class.getName() + ":" + Person.PROP_FAMILYNAME,
                new String[] { CreateUserTest.UNIQUE });
            map.put(User.PROP_USERGROUPS, new String[] { groupHook });
            CreateUser.create(personMetaClass, map, version1);
            Assert.fail("too many new userids");
        } catch (final AssertionError e) {
            // that's great
        } finally {
            version1.abort();
        }
    }

    public void testServlet() throws AccessException, ConstraintException, IOException, ServletException {

        final CreateUser servlet = new CreateUser();
        final MockServletConfig config = new MockServletConfig(this.model);
        final License testLicense = new License("maxUsers=0".getBytes("utf8"));
        config.setAttribute(CreateUser.LICENSE, testLicense);
        servlet.init(config);
        final Map<String, String[]> parms = new HashMap();
        parms.put(User.class.getName() + ":" + User.PROP_NAME, new String[] { CreateUserTest.UNIQUE });
        parms.put(Person.class.getName() + ":" + Person.PROP_FAMILYNAME,
            new String[] { CreateUserTest.UNIQUE });
        final MockHttpServletRequest request =
            new MockHttpServletRequest("post", new MockHttpSession(Access.ADMINISTRATOR), parms);
        final MockHttpServletResponse response = new MockHttpServletResponse(true);
        try {
            servlet.doPost(request, response);
            Assert.fail("created too many");
        } catch (final ServletException e) {
            Assert.assertTrue(e.getMessage().startsWith("Maximum"));
        }

    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.access.CreateUser#create(org.pimslims.metamodel.MetaClass, java.util.Map, org.pimslims.dao.WritableVersion)}
     * .
     */
    public void testCreatewithGroup() throws AccessException, ConstraintException, ServletException {
        final MetaClass metaClass = this.model.getMetaClass(User.class.getName());
        final WritableVersion version = this.model.getTestVersion();
        try {
            final UserGroup group = new UserGroup(version, CreateUserTest.UNIQUE);
            final Map map = new HashMap();
            map.put(User.class.getName() + ":" + User.PROP_NAME,
                new String[] { CreateUserTest.UNIQUE + "two" });
            map.put(User.class.getName() + ":" + User.PROP_USERGROUPS, new String[] { group.get_Hook() });
            final ModelObject object = CreateUser.create(metaClass, map, version);
            Assert.assertTrue(((User) object).getUserGroups().contains(group));

        } finally {
            version.abort();
        }
    }

}
