package org.pimslims.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.AccessRightsUtility;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;

public class PermissionReaderTest extends TestCase {

    private static final String UNIQUE = "pr" + System.currentTimeMillis();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(PermissionReaderTest.class);
    }

    private final AbstractModel model;

    public PermissionReaderTest(final String methodName) {
        super(methodName);
        this.model = org.pimslims.dao.ModelImpl.getModel();
    }

    public void testUserGroupBeanEquals() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final UserGroup group = new UserGroup(version, PermissionReaderTest.UNIQUE);
            final User boss = new User(version, PermissionReaderTest.UNIQUE + "boss");
            group.setHeader(boss);
            final UserGroupBean bean1 = new UserGroupBean(group);
            final UserGroupBean bean2 = new UserGroupBean(group);
            Assert.assertEquals(bean1, bean2);
            Assert.assertEquals(boss.getName(), bean1.getHead().getName());
        } finally {
            version.abort();
        }
    }

    public void testGetOwners() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            new LabNotebook(version, PermissionReaderTest.UNIQUE);
            final PermissionReader reader = new PermissionReader(version);
            final List<ModelObjectShortBean> owners = reader.getOwners();
            Assert.assertTrue(0 < owners.size());
            //TODO when ModelObjectShortBean has a good .equals method Assert.assertTrue(owners.contains(new ModelObjectShortBean(owner)));
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testGetGroups() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final UserGroup group = new UserGroup(version, PermissionReaderTest.UNIQUE);
            final PermissionReader reader = new PermissionReader(version);
            final List<UserGroupBean> groups = reader.getGroups();
            // was Assert.assertEquals(org.pimslims.access.Access.ADMINISTRATOR, groups.get(0).getName());
            Assert.assertTrue(groups.contains(new UserGroupBean(group)));
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void xtestAdminCanDoAnything() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            new LabNotebook(version, "testOwner");
            final PermissionReader reader = new PermissionReader(version);
            final List<UserGroupBean> groups = reader.getGroups();
            // TODO this will fail in an empty DB
            Assert.assertTrue("one group", 1 < groups.size());
            final UserGroupBean adminGroup = groups.get(0);
            Assert.assertEquals(org.pimslims.access.Access.ADMINISTRATOR, adminGroup.getName());
            final UserGroupBean.Permission p = adminGroup.getPermission("testOwner");
            Assert.assertNotNull(p);
            Assert.assertTrue("admin can create", p.getCreate());
            Assert.assertTrue("admin can read", p.getRead());
            Assert.assertTrue("admin can update", p.getUpdate());
            Assert.assertTrue("admin can delete", p.getDelete());
            final UserGroupBean.Permission p2 = adminGroup.getPermissions().get("testOwner");
            Assert.assertEquals("get from map", p, p2);
            final UserGroupBean.Permission p3 = adminGroup.getPermissions().get("reference");
            Assert.assertEquals("get for reference", p, p3);
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void xtestAll() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            new UserGroup(version, "testGroup");
            final PermissionReader reader = new PermissionReader(version);
            final List<UserGroupBean> groups = reader.getGroups();
            UserGroupBean groupBean = null;
            for (final Iterator iter = groups.iterator(); iter.hasNext();) {
                final UserGroupBean element = (UserGroupBean) iter.next();
                if ("testGroup".equals(element.getName())) {
                    groupBean = element;
                    break;
                }
            }
            Assert.assertNotNull(groupBean);
            final UserGroupBean.Permission p = groupBean.getPermission("all");
            Assert.assertNotNull(p);
            Assert.assertTrue(p.getCreate());
            Assert.assertTrue(p.getRead());
            Assert.assertTrue(p.getUpdate());
            Assert.assertTrue(p.getDelete());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testActualPermission() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final String permissionName = "testOwner" + System.currentTimeMillis();
            final String groupName = "testGroup" + System.currentTimeMillis();
            final UserGroup group = new UserGroup(version, groupName);
            final LabNotebook owner = new LabNotebook(version, permissionName);
            new Permission(version, "unlock", owner, group);
            final PermissionReader reader = new PermissionReader(version);
            final List<UserGroupBean> groups = reader.getGroups();
            UserGroupBean groupBean = null;
            for (final Iterator iter = groups.iterator(); iter.hasNext();) {
                final UserGroupBean element = (UserGroupBean) iter.next();
                if (groupName.equals(element.getName())) {
                    groupBean = element;
                    break;
                }
            }
            Assert.assertNotNull(groupBean);
            final UserGroupBean.Permission p = groupBean.getPermission(permissionName);
            Assert.assertNotNull(p);
            Assert.assertFalse(p.getCreate());
            Assert.assertTrue(p.getUnlock());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testUserGroupBeanFromParameters() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final UserGroup testGroup = new UserGroup(version, PermissionReaderTest.UNIQUE);
            final Collection<String> parms = new ArrayList<String>();
            parms.add(testGroup.getName() + ":create:testOwner");
            parms.add("otherGroup:delete:testOwner");
            parms.add(testGroup.getName() + ":unlock:testOwner");
            parms.add("owner");
            final UserGroupBean bean = new UserGroupBean(testGroup, parms);
            final UserGroupBean.Permission p = bean.getPermission("testOwner");
            Assert.assertNotNull(p);
            Assert.assertFalse(p.getDelete());
            Assert.assertTrue(p.getCreate());
            Assert.assertTrue(p.getUnlock());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testIsPermitted() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final UserGroup group = new UserGroup(version, "testGroup" + System.currentTimeMillis());
            final LabNotebook owner = new LabNotebook(version, "testOwner" + System.currentTimeMillis());
            new Permission(version, "read", owner, group);
            //final PermissionReader reader = new PermissionReader(version);
            Assert.assertTrue(AccessRightsUtility.isPermitted(group, "read", owner));
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testGetGroupsForOwners() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final String permissionName = "testOwner" + System.currentTimeMillis();
            final String groupName = "testGroup" + System.currentTimeMillis();
            final UserGroup group = new UserGroup(version, groupName);
            final LabNotebook owner = new LabNotebook(version, permissionName);
            new Permission(version, "read", owner, group);
            final PermissionReader reader = new PermissionReader(version);

            final String[] groupHooks = reader.getGroupsForOwners(new String[] { owner.get_Hook() });
            Assert.assertEquals(1, groupHooks.length);
            Assert.assertEquals(group.get_Hook(), groupHooks[0]);

        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testGetOwnersForGroups() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final String permissionName = "testOwner" + System.currentTimeMillis();
            final String groupName = "testGroup" + System.currentTimeMillis();
            final UserGroup group = new UserGroup(version, groupName);
            final LabNotebook owner = new LabNotebook(version, permissionName);
            new Permission(version, "read", owner, group);
            final PermissionReader reader = new PermissionReader(version);

            final String[] hooks = reader.getOwnersForGroups(new String[] { group.get_Hook() });
            Assert.assertEquals(1, hooks.length);
            Assert.assertEquals(owner.get_Hook(), hooks[0]);

        } finally {
            version.abort(); // not testing persistence
        }
    }

}
