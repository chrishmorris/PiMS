/**
 * DM org.pimslims.hibernate PermissionTest.java
 * 
 * @author bl67
 * @date 11 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 bl67 This library is free software; you can redistribute it and/or modify it
 *           under the terms of the GNU Lesser General Public License as published by the Free Software
 *           Foundation; either version 2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */
package org.pimslims.model.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.hibernate.HibernateException;
import org.pimslims.access.PIMSAccess;
import org.pimslims.dao.AbstractModel;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.core.Note;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;
import org.pimslims.persistence.HibernateUtilTester;
import org.pimslims.search.Searcher;
import org.pimslims.test.AbstractTestCase;

/**
 * PermissionTest
 * 
 */
public class PermissionTester extends AbstractTestCase {
    Collection<Long> accessIDs = new HashSet<Long>();

    String userName1 = null;

    String userName2 = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        wv = getWV();
        try {
            //create 2 new accessobject
            LabNotebook access_g1 = create(LabNotebook.class);
            LabNotebook access_g2 = create(LabNotebook.class);
            accessIDs.add(access_g1.getDbId());
            accessIDs.add(access_g2.getDbId());
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

            //create 2 userGroup linked with each permission
            UserGroup usergroup1 = create(UserGroup.class, UserGroup.PROP_PERMISSIONS, permission1_read);
            usergroup1.addPermission(permission1_create);
            usergroup1.addPermission(permission1_UPDATE);
            UserGroup usergroup2 = create(UserGroup.class, UserGroup.PROP_PERMISSIONS, permission2_read);
            usergroup2.addPermission(permission2_create);
            usergroup2.addPermission(permission2_UPDATE);
            //create 2 users belong to each group
            User user1 = create(User.class, User.PROP_USERGROUPS, usergroup1);
            User user2 = create(User.class, User.PROP_USERGROUPS, usergroup2);
            userName1 = user1.getName();
            userName2 = user2.getName();
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }

    public void testRead() throws AccessException, ConstraintException, AbortedException, HibernateException {

        //user1 create a target
        String TargetName1 = createTargetAndNoteByUser(userName1);
        //user2 create a target
        String TargetName2 = createTargetAndNoteByUser(userName2);

        //admin can see both target&Note
        assertTrue(getTargetByUser(AbstractModel.SUPERUSER, TargetName1));
        assertTrue(getTargetByUser(AbstractModel.SUPERUSER, TargetName2));
        assertTrue(getNoteByUser(AbstractModel.SUPERUSER, TargetName1));
        assertTrue(getNoteByUser(AbstractModel.SUPERUSER, TargetName2));

        //user1 can see target1 not target2
        assertTrue(getTargetByUser(userName1, TargetName1));
        assertTrue(getNoteByUser(userName1, TargetName1));
        assertFalse(getTargetByUser(userName1, TargetName2));
        assertFalse(getNoteByUser(userName1, TargetName2));

        //user2 can see target2 not target1
        assertTrue(getTargetByUser(userName2, TargetName2));
        assertTrue(getNoteByUser(userName2, TargetName2));
        assertFalse(getTargetByUser(userName2, TargetName1));
        assertFalse(getNoteByUser(userName2, TargetName1));

        //user2 create a target would not affect user1's count of target
        int countofTarget = getCountOfTargetByUser(userName1);
        createTargetAndNoteByUser(userName2);
        assertEquals(countofTarget, getCountOfTargetByUser(userName1));

    }

    public void testAddRole() throws AbortedException, ConstraintException, AccessException {

        //create a SampleCategory by admin
        wv = getWV(AbstractModel.SUPERUSER);
        String scHook = null;
        try {
            SampleCategory sc = create(SampleCategory.class);
            scHook = sc.get_Hook();
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
        //user1 add a sample to this SampleCategory
        wv = getWV(userName1);
        try {
            SampleCategory sc = wv.get(scHook);
            Sample sample = create(Sample.class, AbstractSample.PROP_SAMPLECATEGORIES, sc);
            //user1 should  see sample1 from this SampleCategory
            assertTrue(sc.getAbstractSamples().contains(sample));
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }

        //user2 add a sample to this SampleCategory
        wv = getWV(userName2);
        try {
            SampleCategory sc = wv.get(scHook);
            Sample sample = create(Sample.class, AbstractSample.PROP_SAMPLECATEGORIES, sc);
            assertTrue(sc.getAbstractSamples().contains(sample));
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
        //admin should see both sample from SampleCategory
        rv = getRV(AbstractModel.SUPERUSER);
        try {
            SampleCategory sc = rv.get(scHook);
            assertEquals(2, sc.getAbstractSamples().size());
        } finally {
            if (!rv.isCompleted())
                rv.abort();
        }
    }

    public void testGetRole() throws AbortedException, ConstraintException, AccessException,
        HibernateException {
        //testJoinRole()
        wv = getWV(userName1);
        String rsHook = null;
        try {
            RefSample rs = create(RefSample.class);
            rsHook = rs.get_Hook();
            Sample bc = create(Sample.class);
            bc.setRefSample(rs);
            wv.flush();
            wv.commit();
            HibernateUtilTester.clear2ndLevelCache(model);
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
        rv = getRV(userName1);
        try {
            RefSample rs = rv.get(rsHook);
            assertEquals(1, rs.getConformings().size());
        } finally {
            if (!rv.isCompleted())
                rv.abort();
        }
    }

    public void testFindAll() throws AccessException, ConstraintException, AbortedException {
        //user1 create a target
        String TargetName1 = createTargetAndNoteByUser(userName1);
        //user2 create a target
        String TargetName2 = createTargetAndNoteByUser(userName2);

        //user1 searchAll using searcher
        rv = getRV(userName1);
        try {
            Searcher searcher = new Searcher(rv);
            MetaClass metaClass = model.getMetaClass(Target.class.getName());
            //search for user1's target
            ArrayList<ModelObject> results = searcher.searchAll(metaClass, TargetName1);
            assertEquals(1, results.size());
            //search for user2's target
            assertEquals(TargetName1, results.iterator().next().get_Name());
            results = searcher.searchAll(metaClass, TargetName2);
            assertEquals(0, results.size());
        } finally {
            if (!rv.isCompleted())
                rv.abort();
        }
    }

    public void testFindRole() throws AccessException, ConstraintException, AbortedException {
        //user1 create a target
        String TargetName1 = createTargetAndNoteByUser(userName1);

        wv = getWV(userName1);
        try {
            Target t = wv.findFirst(Target.class, Target.PROP_NAME, TargetName1);
            Organism org = create(Organism.class);
            t.setSpecies(org);
            Organism org_result = t.findFirst(Target.PROP_SPECIES, Organism.PROP_NAME, org.getName());

            assertEquals(org, org_result);
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }

    /**
     * @param userName
     * @return
     */
    private int getCountOfTargetByUser(String userName) {
        rv = getRV(userName);
        try {
            return rv.getCountOfAll(model.getMetaClass(Target.class.getName()));
        } finally {
            if (!rv.isCompleted())
                rv.abort();
        }

    }

    /**
     * @param targetName
     * @param superuser
     * @return
     */
    private boolean getTargetByUser(String userName, String targetName) {
        rv = getRV(userName);
        Target t = null;
        try {
            t = rv.findFirst(Target.class, Target.PROP_NAME, targetName);
        } finally {
            if (!rv.isCompleted())
                rv.abort();
        }
        return t != null;
    }

    private boolean getNoteByUser(String userName, String targetName) {
        rv = getRV(userName);
        Note note = null;
        try {
            note = rv.findFirst(Note.class, Note.PROP_NAME, targetName);
        } finally {
            if (!rv.isCompleted())
                rv.abort();
        }
        return note != null;
    }

    private String createTargetAndNoteByUser(String userName) throws AccessException, ConstraintException,
        AbortedException {
        String targetName;
        wv = getWV(userName);
        try {
            Target t = create(Target.class);
            Note note = create(Note.class, Attachment.PROP_PARENTENTRY, t);
            targetName = t.getName();
            note.setName(targetName);
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
        return targetName;
    }

}
