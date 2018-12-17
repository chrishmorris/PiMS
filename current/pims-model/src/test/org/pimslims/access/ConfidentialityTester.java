/**
 * pims-model org.pimslims.access ConfidentialityTester.java
 * 
 * @author pajanne
 * @date Oct 22, 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 pajanne This library is free software; you can redistribute it and/or modify
 *           it under the terms of the GNU Lesser General Public License as published by the Free Software
 *           Foundation; either version 2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */
package org.pimslims.access;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.stat.Statistics;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.AbstractTestAccess;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.people.Organisation;
import org.pimslims.persistence.HibernateUtilTester;
import org.pimslims.search.Paging;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * ConfidentialityTester
 * 
 */
public class ConfidentialityTester extends AbstractTestCase {

    private static String USERNAME_A;

    private static String USERNAME_B;

    private String userAHook = null;

    private String userBHook = null;

    String ownerA;

    String ownerB;

    AbstractModel model;

    @Override
    protected void setUp() {
        this.model = ModelImpl.getModel();
        AccessImpl access = (AccessImpl) this.model.getAccess();
        MetaClass metaclassUser = access.getUser();
        assertNotNull("metaclass for user", metaclassUser);
        wv = getWV();
        try {
            //create 2 new accessobject
            LabNotebook access_g1 = create(LabNotebook.class);
            LabNotebook access_g2 = create(LabNotebook.class);
            ownerA = access_g1.getName();
            ownerB = access_g2.getName();
            //Create 2 read permissions on each accessobject
            Permission permission1_read = create(Permission.class, Permission.PROP_OPTYPE, PIMSAccess.READ);
            permission1_read.setAccessObject(access_g1);

            Permission permission2_read = create(Permission.class, Permission.PROP_OPTYPE, PIMSAccess.READ);
            permission2_read.setAccessObject(access_g2);

            //Create 2 create permissions on each accessobject
            Permission permission1_create =
                create(Permission.class, Permission.PROP_OPTYPE, PIMSAccess.CREATE);
            permission1_create.setAccessObject(access_g1);
            Permission permission2_create =
                create(Permission.class, Permission.PROP_OPTYPE, PIMSAccess.CREATE);
            permission2_create.setAccessObject(access_g2);

            //Create 2 update permissions on each accessobject
            Permission permission1_UPDATE =
                create(Permission.class, Permission.PROP_OPTYPE, PIMSAccess.UPDATE);
            permission1_UPDATE.setAccessObject(access_g1);

            Permission permission2_UPDATE =
                create(Permission.class, Permission.PROP_OPTYPE, PIMSAccess.UPDATE);
            permission2_UPDATE.setAccessObject(access_g2);

            //Create 2 delete permissions on each accessobject
            Permission permission1_del = create(Permission.class, Permission.PROP_OPTYPE, PIMSAccess.DELETE);
            permission1_del.setAccessObject(access_g1);

            Permission permission2_del = create(Permission.class, Permission.PROP_OPTYPE, PIMSAccess.DELETE);
            permission2_del.setAccessObject(access_g2);

            //create 2 userGroup linked with each permission
            UserGroup usergroup1 = create(UserGroup.class, UserGroup.PROP_PERMISSIONS, permission1_read);
            usergroup1.addPermission(permission1_create);
            usergroup1.addPermission(permission1_UPDATE);
            usergroup1.addPermission(permission1_del);
            UserGroup usergroup2 = create(UserGroup.class, UserGroup.PROP_PERMISSIONS, permission2_read);
            usergroup2.addPermission(permission2_create);
            usergroup2.addPermission(permission2_UPDATE);
            usergroup2.addPermission(permission2_del);
            //create 2 users belong to each group
            User userA = create(User.class, User.PROP_USERGROUPS, usergroup1);
            User userB = create(User.class, User.PROP_USERGROUPS, usergroup2);
            USERNAME_A = userA.getName();
            USERNAME_B = userB.getName();
            // ensure two different user names
            assertTrue("Two different user names", !USERNAME_A.equals(USERNAME_B));

            this.userAHook = userA.get_Hook();
            this.userBHook = userB.get_Hook();
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    @Override
    protected void tearDown() throws Exception {
        WritableVersion wv = this.model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            // delete new users
            User userA = wv.get(this.userAHook);
            wv.delete(userA);
            User userB = wv.get(this.userBHook);
            wv.delete(userB);
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } catch (final RuntimeException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        super.tearDown();
        System.gc();
        System.gc();
        Statistics stat = HibernateUtilTester.getStatistics(model);
        if (stat.getSessionCloseCount() - stat.getSessionOpenCount() < 0) {
            System.err.println((stat.getSessionOpenCount() - stat.getSessionCloseCount())
                + " session is not closed");
        }
        // make sure every session is closed
        assert stat.getSessionCloseCount() == stat.getSessionOpenCount();
    }

    public void testTwoDifferentUsers() {
        WritableVersion wv = this.model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            // test two different users
            User userA = wv.get(this.userAHook);
            User userB = wv.get(this.userBHook);
            assertTrue("Two different users", !userA.equals(userB));
        } catch (final RuntimeException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testConfidentiality() {
        WritableVersion wv = null;
        WritableVersion wvA = null;
        WritableVersion wvB = null;
        try {
            wv = this.model.getWritableVersion("unknown_user");
            fail("Writable version with invalid user");
        } catch (IllegalArgumentException e) {
            // that's fine
        } finally {
            if (null != wv) {
                wv.abort();
            }
        }

        try {
            // writable version for userA
            wvA = this.model.getWritableVersion(USERNAME_A);
            assertEquals("transaction user", USERNAME_A, wvA.getUsername());

            // writable version for userB
            wvB = this.model.getWritableVersion(USERNAME_B);
            assertEquals("transaction user", USERNAME_B, wvB.getUsername());

            // create organisation with userA
            Organisation organisationA = new Organisation(wvA, POJOFactory.getAttrOrganisation());
            assertEquals("owner for: " + USERNAME_A, ownerA, organisationA.get_Owner());

            // create organisation with userB
            Organisation organisationB = new Organisation(wvB, POJOFactory.getAttrOrganisation());
            assertEquals("owner for: " + USERNAME_B, ownerB, organisationB.get_Owner());

            // test confidentiality for userA
            java.util.Collection<Organisation> organisationListA = wvA.getAll(Organisation.class, 0, 100);
            assertTrue("Organisation visible for userA", organisationListA.contains(organisationA));
            assertFalse("confidentiality for userA", organisationListA.contains(organisationB));

            organisationListA = wvA.findAll(Organisation.class, Collections.EMPTY_MAP, new Paging(0, 100));
            assertTrue("Organisation visible for userA", organisationListA.contains(organisationA));
            assertFalse("confidentiality for userA", organisationListA.contains(organisationB));

            Map criteria = new HashMap();
            criteria.put(Organisation.PROP_NAME, organisationA.getName());
            organisationListA = wvA.findAll(Organisation.class, criteria, new Paging(0, 100));
            assertTrue("Organisation visible for userA", organisationListA.contains(organisationA));
            assertFalse("confidentiality for userA", organisationListA.contains(organisationB));

            // test confidentiality for userB
            java.util.Collection<Organisation> organisationListB = wvB.getAll(Organisation.class, 0, 100);
            assertTrue("Organisation visible for userB", organisationListB.contains(organisationB));
            assertFalse("confidentiality for userB", organisationListB.contains(organisationA));

            // delete organisationA & organisationB
            wvA.delete(organisationA);
            wvB.delete(organisationB);
        } catch (ModelException ex) {
            Throwable cause = ex;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            cause.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (null != wvA) {
                wvA.abort();
            }
            if (null != wvB) {
                wvB.abort();
            }
        }
        AbstractTestAccess.suiteTearDown(this.model);
    }

}
