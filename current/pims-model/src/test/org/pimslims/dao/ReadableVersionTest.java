/**
 * current-datamodel org.pimslims.hibernate ReadablerVersionTest.java
 * 
 * @author bl67
 * @date 11 Jul 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 bl67 This library is free software; you can redistribute it and/or modify it
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
package org.pimslims.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.people.Person;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.test.AbstractTestCase;

/**
 * ReadablerVersionTest
 * 
 */
public class ReadableVersionTest extends AbstractTestCase {

    public void testFindNull() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            Sample sample = this.create(Sample.class);
            wv.flush();
            final HashMap<String, Object> attr = new HashMap<String, Object>();
            attr.put(AbstractSample.PROP_NAME, sample.getName());
            assertEquals(sample, this.wv.findFirst(Sample.class, attr));

            attr.put(Sample.PROP_ASSIGNTO, null);
            assertEquals(sample, this.wv.findFirst(Sample.class, attr));

            User user = this.create(User.class);
            sample.setAssignTo(user);
            assertNull(this.wv.findFirst(Sample.class, attr));

        } finally {
            wv.abort();
        }
    }

    public void testFindString() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            Person p = this.create(Person.class);
            wv.flush();
            p.setTitle(null);
            final HashMap<String, Object> attr = new HashMap<String, Object>();
            attr.put(Person.PROP_FAMILYNAME, p.getFamilyName());
            assertEquals(p, this.wv.findFirst(Person.class, attr));

            p.setTitle(null);
            attr.put(Person.PROP_TITLE, null);
            assertEquals(p, this.wv.findFirst(Person.class, attr));

            p.setTitle("");
            attr.put(Person.PROP_TITLE, "");

            p.setTitle("Dr");
            assertNull(this.wv.findFirst(Person.class, attr));
        } finally {
            wv.abort();
        }
    }

    public void testFindEmptyString() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            Person p = this.create(Person.class);
            wv.flush();
            p.setTitle(null);
            final HashMap<String, Object> attr = new HashMap<String, Object>();
            attr.put(Person.PROP_FAMILYNAME, p.getFamilyName());
            p.setTitle("");
            attr.put(Person.PROP_TITLE, "");
            assertEquals(p, this.wv.findFirst(Person.class, attr));

        } finally {
            wv.abort();
        }
    }

    // 
    // hqlQuery.setParameter(this.alias + this.serialNumber, null);
    //org.hibernate.Query hqlQuery = rv.getSession().createQuery(hql).setCacheable(isQueryCacheable);

    public void testHql() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            Person p = this.create(Person.class);
            p.setTitle(null);
            wv.flush();
            assertEquals("", p.getTitle()); // changed in getter

            String hql =
                "select distinct A from org.pimslims.model.people.Person A  where  A.familyName = :familyName1  Order by A.dbId DESC";
            org.hibernate.Query hqlQuery = wv.getSession().createQuery(hql);
            // could .setCacheable(isQueryCacheable)
            hqlQuery.setParameter("familyName1", p.getFamilyName());
            List list = hqlQuery.list();
            assertEquals(1, list.size());

            hql =
                "select distinct A from org.pimslims.model.people.Person A  where  A.familyName = :familyName1  AND  A.title IS NULL  Order by A.dbId DESC";
            hqlQuery = wv.getSession().createQuery(hql);
            // could .setCacheable(isQueryCacheable)
            hqlQuery.setParameter("familyName1", p.getFamilyName());
            list = hqlQuery.list();
            assertEquals(1, list.size());

            // if you use setParameter("title2", null) Hibernate fails to make the correct SQL
            // This could be fixed by using the Criteria API, but that is a big change

        } finally {
            wv.abort();
        }
    }

    public void testSortedByName() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            Collection<Sample> samples = new HashSet<Sample>();
            samples.add((Sample) create(Sample.class, AbstractSample.PROP_NAME,
                "3-" + System.currentTimeMillis()));
            samples.add((Sample) create(Sample.class, AbstractSample.PROP_NAME,
                "1-" + System.currentTimeMillis()));
            samples.add((Sample) create(Sample.class, AbstractSample.PROP_NAME,
                "2-" + System.currentTimeMillis()));
            samples = wv.sortByName(samples);

            //verity
            Iterator<Sample> it = samples.iterator();
            for (int i = 1; i <= 3; i++) {
                Sample sample = it.next();
                assertTrue(sample.get_Name().startsWith(i + ""));
            }
        } finally {
            wv.abort();
        }
    }

    @SuppressWarnings("deprecation")
    public void testGetSearchableFields() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            Person p = this.create(Person.class);
            wv.flush();
            p.setTitle("Mr");
            Map<String, MetaAttribute> map =
                wv.getSearchableFields(AbstractTestCase.model.getMetaClass(Person.class.getName()));
            assertTrue(map.containsKey(Person.PROP_TITLE));
            assertFalse(map.containsKey(Person.PROP_FAMILYTITLE));
        } finally {
            wv.abort();
        }
    }

    @SuppressWarnings("deprecation")
    public void testGetSearchableFieldsUser() throws AccessException, ConstraintException, AbortedException {
        wv = getWV();
        try {
            UserGroup group = new UserGroup(wv, UNIQUE);
            new Permission(wv, "read", new LabNotebook(wv, UNIQUE), group);
            new User(wv, UNIQUE).addUserGroup(group);
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }

        //test
        wv = AbstractTestCase.model.getWritableVersion(UNIQUE);
        try {
            Map<String, MetaAttribute> map =
                wv.getSearchableFields(AbstractTestCase.model.getMetaClass(Person.class.getName()));
            assertFalse(map.containsKey(Person.PROP_FAMILYTITLE));
        } finally {
            wv.abort();
        }

        // clean up        
        wv = getWV();
        try {
            wv.findFirst(LabNotebook.class, LabNotebook.PROP_NAME, UNIQUE).delete();
            User user = wv.findFirst(User.class, User.PROP_NAME, UNIQUE);
            wv.delete(user.getUserGroups());
            user.delete();
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }

    public void testGetSearchableFieldsSystemClass() throws AccessException, ConstraintException,
        AbortedException {
        wv = getWV();
        try {
            UserGroup group = new UserGroup(wv, UNIQUE);
            new Permission(wv, "read", new LabNotebook(wv, UNIQUE), group);
            new User(wv, UNIQUE).addUserGroup(group);
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }

        //test
        wv = AbstractTestCase.model.getWritableVersion(UNIQUE);
        try {
            Map<String, MetaAttribute> map =
                wv.getSearchableFields(AbstractTestCase.model.getMetaClass(SampleCategory.class.getName()));
        } finally {
            wv.abort();
        }

        // clean up        
        wv = getWV();
        try {
            wv.findFirst(LabNotebook.class, LabNotebook.PROP_NAME, UNIQUE).delete();
            User user = wv.findFirst(User.class, User.PROP_NAME, UNIQUE);
            wv.delete(user.getUserGroups());
            user.delete();
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }
}
