package org.pimslims.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.AccessRightsUtility;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;

public class PermissionWriterTest extends TestCase {

    /**
     * UNIQUE String
     */
    private static final String UNIQUE = "pw" + System.currentTimeMillis();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(PermissionWriterTest.class);
    }

    private final AbstractModel model;

    public PermissionWriterTest(final String methodName) {
        super(methodName);
        this.model = org.pimslims.dao.ModelImpl.getModel();
    }

    /*
     * Test method for 'org.pimslims.presentation.PermissionWriter.PermissionWriter(WritableVersion)'
     */
    public void testPermissionWriter() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            //final PermissionWriter writer =
            new PermissionWriter(version, PermissionReader.getAllOwners(version), Collections.EMPTY_SET);

            //final List<ModelObjectShortBean> owners = writer.getOwners();
            // was Assert.assertEquals("reference", owners.get(0));

            //final List<UserGroupBean> groups = writer.getGroups();
            // was Assert.assertEquals(org.pimslims.access.Access.ADMINISTRATOR, groups.get(0).getName());
        } finally {
            version.abort();
        }
    }

    /*
     * Test method for 'org.pimslims.presentation.PermissionWriter.save(UserGroupBean)'

    public void testSaveUserGroupBean2() {
    	WritableVersion version = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
    	try {
    		UserGroup group = new UserGroup(version, "testGroup");
    		AccessObject owner  = new AccessObject(version, "testOwner");
    		PermissionWriter writer = new PermissionWriter(version);

    		new Permission(version,"PIMS", "read", "", Boolean.TRUE, owner, group);
    		assertFalse(writer.isPermitted(group, "create", owner));
    		assertTrue(writer.isPermitted(group, "read", owner));

    		// could now test resetting an existing permission

    	} catch (ConstraintException e) {
    		fail(e.getMessage());
    	} finally {
    		version.abort();
    	}

    } */

    public void testFindPermissions() throws ConstraintException {

        final WritableVersion version = this.model.getTestVersion();
        try {
            final UserGroup group = new UserGroup(version, PermissionWriterTest.UNIQUE);
            final LabNotebook owner = new LabNotebook(version, "testOwner" + System.currentTimeMillis());
            //final PermissionWriter writer =
            //    new PermissionWriter(version, PermissionReader.getAllOwners(version));
            Assert.assertEquals(0, AccessRightsUtility.findPermissions(group, "read", owner).size());
            new Permission(version, "PIMS" + System.currentTimeMillis(), "read", owner, group);
            Assert.assertEquals(1, AccessRightsUtility.findPermissions(group, "read", owner).size());

        } finally {
            version.abort();
        }

    }

    public void testSetCan() throws ConstraintException, AccessException {

        final WritableVersion version = this.model.getTestVersion();
        try {
            final UserGroup group = new UserGroup(version, PermissionWriterTest.UNIQUE);
            final LabNotebook owner = new LabNotebook(version, "testOwner" + System.currentTimeMillis());
            //final PermissionWriter writer =
            //    new PermissionWriter(version, PermissionReader.getAllOwners(version));
            Assert.assertFalse(AccessRightsUtility.isPermitted(group, "delete", owner));
            AccessRightsUtility.setCan(version, group, "delete", owner);
            Assert.assertTrue(AccessRightsUtility.isPermitted(group, "delete", owner));
            AccessRightsUtility.setCant(version, group, "delete", owner);
            Assert.assertFalse(AccessRightsUtility.isPermitted(group, "delete", owner));

        } finally {
            version.abort();
        }

    }

    public void testSaveParameters() throws ConstraintException, AccessException {

        final WritableVersion version = this.model.getTestVersion();
        try {
            final UserGroup group = new UserGroup(version, PermissionWriterTest.UNIQUE);
            final LabNotebook owner = new LabNotebook(version, "testOwner" + System.currentTimeMillis());
            //final PermissionWriter writer =
            //    new PermissionWriter(version, PermissionReader.getAllOwners(version));
            AccessRightsUtility.setCan(version, group, "delete", owner);
            final PermissionWriter writer =
                new PermissionWriter(version, Collections.singleton(owner), Collections.singleton(group));

            final Collection<String> parameters = new ArrayList();
            // e.g. owner=org.pimslims.model.core.AccessObject%3A486007&a%3Aread%3Ab=on
            parameters.add("owner");
            parameters.add(group.getName() + ":read:" + owner.getName());
            writer.save(parameters);

            Assert.assertTrue(AccessRightsUtility.isPermitted(group, "read", owner));
            Assert.assertFalse(AccessRightsUtility.isPermitted(group, "delete", owner));

        } finally {
            version.abort();
        }

    }

    /*
     * Test method for 'org.pimslims.presentation.PermissionWriter.save(UserGroupBean)'
     */
    public void testSaveUserGroupBean() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final String groupName = PermissionWriterTest.UNIQUE;
            final String ownerName = "testOwner" + System.currentTimeMillis();
            final UserGroup group = new UserGroup(version, groupName);
            final LabNotebook owner = new LabNotebook(version, ownerName);
            AccessRightsUtility.setCan(version, group, "delete", owner);
            final PermissionWriter writer =
                new PermissionWriter(version, Collections.singleton(owner), Collections.EMPTY_SET);
            final UserGroupBean bean =
                new UserGroupBean(group, Collections.singleton(groupName + ":unlock:" + ownerName));
            final UserGroupBean.Permission p = bean.getPermission(ownerName);

            // test parsing
            Assert.assertFalse(p.getCreate());
            Assert.assertTrue(p.getUnlock());

            // test save
            writer.save(bean);
            Assert.assertFalse(AccessRightsUtility.isPermitted(group, "create", owner));
            Assert.assertTrue(AccessRightsUtility.isPermitted(group, "unlock", owner));
            Assert.assertFalse(AccessRightsUtility.isPermitted(group, "delete", owner));
        } finally {
            version.abort();
        }
    }

    /*
     * Test method for 'org.pimslims.presentation.PermissionWriter.save(UserGroupBean)'
     */
    public void testDontClearWhenSaveUserGroupBean() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final String groupName = PermissionWriterTest.UNIQUE;
            final String ownerName = "testOwner" + System.currentTimeMillis();
            final UserGroup group = new UserGroup(version, groupName);
            final LabNotebook owner = new LabNotebook(version, ownerName);
            AccessRightsUtility.setCan(version, group, "read", owner);
            final PermissionWriter writer =
                new PermissionWriter(version, Collections.EMPTY_SET, Collections.EMPTY_SET);
            final UserGroupBean bean = new UserGroupBean(group, Collections.singleton("owner"));

            // test save
            writer.save(bean);
            Assert.assertTrue(AccessRightsUtility.isPermitted(group, "read", owner));

        } finally {
            version.abort();
        }
    }

    public void testDontClear() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final UserGroup groupA = new UserGroup(version, PermissionWriterTest.UNIQUE + "a");
            final UserGroup groupB = new UserGroup(version, PermissionWriterTest.UNIQUE + "b");
            final LabNotebook ownerA = new LabNotebook(version, PermissionWriterTest.UNIQUE + "a");
            final LabNotebook ownerB = new LabNotebook(version, PermissionWriterTest.UNIQUE + "b");
            AccessRightsUtility.setCan(version, groupA, "read", ownerA);
            AccessRightsUtility.setCan(version, groupA, "read", ownerB);
            AccessRightsUtility.setCan(version, groupB, "read", ownerA);
            AccessRightsUtility.setCan(version, groupB, "read", ownerB);
            final PermissionWriter writer =
                new PermissionWriter(version, Collections.singleton(ownerB), Collections.singleton(groupA));

            final Collection<String> parameters = new ArrayList();
            // e.g. owner=org.pimslims.model.core.AccessObject%3A486007&a%3Aread%3Ab=on
            parameters.add("owner");
            parameters.add(groupA.getName() + ":read:" + ownerB.getName());
            writer.save(parameters);
            Assert.assertTrue(AccessRightsUtility.isPermitted(groupB, "read", ownerB));

        } finally {
            version.abort();
        }
    }

}
