/**
 * org.pimslims.hibernate AbstractModelObjectTest.java
 * 
 * @date 19-Sep-2006 06:29:54
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 *         Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 *           Copyright (c) 2006
 * 
 *           This library is free software; you can redistribute it and/or modify it under the terms of the
 *           GNU Lesser General Public License as published by the Free Software Foundation; either version
 *           2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.api.PersonTester;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.BookCitation;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.core.Note;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.people.Person;
import org.pimslims.test.AbstractTestCase;

/**
 * AbstractModelObjectTest
 * 
 */
public class AbstractModelObjectTest extends AbstractTestCase {

    /**
     * The model in use
     */
    protected final AbstractModel model = ModelImpl.getModel();

    String hook;

    public void testFile() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            LabBookEntry t = new Organisation(wv, UNIQUE);
            t.addAttachment((Note) create(Note.class));
            assertTrue(t.getNotes().size() == 1);
            assertTrue(t.get_Files().size() == 0);
        } finally {
            wv.abort();
        }
    }

    public void testGenericFindAll() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        try {
            org.pimslims.model.people.Person person1 = (Person) wv.get(this.hook);
            BookCitation citation1 = person1.findFirst(LabBookEntry.PROP_ATTACHMENTS);
            assertNotNull(citation1);
            Collection<BookCitation> bcs =
                person1.findAll(LabBookEntry.PROP_ATTACHMENTS, Collections.emptyMap());
            assertNotNull(bcs);
            assertTrue(bcs.size() > 0);
            Collection<BookCitation> bcs2 = wv.getAll(BookCitation.class, 0, 100);
            assertNotNull(bcs2);
            // wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    // test with Molecule, there may be an issue in Oracle with the CLOB for sequence
    public void testMoleculeFindAll() throws AccessException, ConstraintException {
        wv = getWV();

        try {
            Molecule molecule = create(Molecule.class);
            BookCitation citation = create(BookCitation.class);
            molecule.addAttachment(citation);
            Collection<Attachment> bcs =
                molecule.findAll(LabBookEntry.PROP_ATTACHMENTS, Collections.emptyMap());
            assertTrue(bcs.contains(citation));

            Collection<ModelObject> pages =
                citation.findAll(Attachment.PROP_PARENTENTRY, Collections.emptyMap());
            assertTrue(pages.contains(molecule));

        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testSetRole() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        try {

            org.pimslims.model.people.Person person2 =
                (org.pimslims.model.people.Person) wv.create(org.pimslims.model.people.Person.class,
                    PersonTester.ATTRIBUTES);

            // test set 0-1 role using a collection
            LabNotebook ao = new LabNotebook(wv, "a name");
            person2.set_Role(LabBookEntry.PROP_ACCESS, Collections.singleton((ModelObject) ao));
            assertTrue(person2.getAccess() == ao);

            // test set 0-1 role using a modelObject
            LabNotebook ao2 = new LabNotebook(wv, "another name");
            person2.set_Role(LabBookEntry.PROP_ACCESS, ao2);
            assertTrue(person2.getAccess() == ao2);
            // wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testToString() {

        ReadableVersion rv1 = model.getReadableVersion(AbstractModel.SUPERUSER);
        assertNotNull(rv1);
        try {
            org.pimslims.model.people.Person person1 = (Person) rv1.get(this.hook);

            BookCitation citation1 = (BookCitation) person1.getAttachments().iterator().next();
            //as person's get_name is not hook, the toString should be combination of name+hook
            assertTrue(person1.toString().contains(person1.get_Hook()));
            assertTrue(person1.toString().contains(person1.get_Name()));
            //as citation's get_name is part of hook, the toString should be the hook
            System.out.println(citation1.get_Name());
            citation1.setTitle("");
            assertEquals(citation1.get_Hook(), citation1.toString());
            rv1.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!rv1.isCompleted()) {
                rv1.abort();

            }
        }
    }

    public void testGetVersion() {
        ReadableVersion rv1 = model.getReadableVersion(AbstractModel.SUPERUSER);
        ReadableVersion rv2 = model.getReadableVersion(AbstractModel.SUPERUSER);
        assertNotNull(rv1);
        assertNotNull(rv2);
        try {
            org.pimslims.model.people.Person person1 = (Person) rv1.get(this.hook);
            org.pimslims.model.people.Person person2 = (Person) rv2.get(this.hook);
            assertTrue(person1.get_Version() == rv1);
            assertTrue(person2.get_Version() == rv2);

            BookCitation citation1 = (BookCitation) person1.getAttachments().iterator().next();
            BookCitation citation2 = (BookCitation) person2.getAttachments().iterator().next();
            assertTrue(rv1.getSession().contains(citation1));
            assertFalse(rv2.getSession().contains(citation1));
            assertTrue(rv2.getSession().contains(citation2));
            assertFalse(rv1.getSession().contains(citation2));
            assertTrue(citation1.get_Version() == rv1);
            assertTrue(citation2.get_Version() == rv2);

            rv1.commit();
            rv2.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!rv1.isCompleted()) {
                rv1.abort();
            }
            if (!rv2.isCompleted()) {
                rv2.abort();
            }
        }
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        wv = getWV();
        try {
            org.pimslims.model.people.Person person =
                (org.pimslims.model.people.Person) wv.create(org.pimslims.model.people.Person.class,
                    PersonTester.ATTRIBUTES);
            Map<String, Object> props = new HashMap();
            props.put(BookCitation.PROP_AUTHOR, new User(wv, UNIQUE));
            props.put(BookCitation.PROP_YEAR, 2001);
            props.put(BookCitation.PROP_PARENTENTRY, person);
            BookCitation citation = new BookCitation(wv, props);
            assertNotNull(person);
            person.addAttachment(citation);
            this.hook = person.get_Hook();
            wv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            org.pimslims.model.people.Person person = (Person) wv.get(this.hook);
            Collection<ModelObject> citations = new HashSet<ModelObject>(person.getAttachments());
            for (Iterator iterator = citations.iterator(); iterator.hasNext();) {
                BookCitation citation = (BookCitation) iterator.next();
                citation.delete();
                citation.getAuthor().delete();
            }
            wv.delete(person);
            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }
}
