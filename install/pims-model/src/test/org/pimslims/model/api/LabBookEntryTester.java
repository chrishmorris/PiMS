/**
 * pims-model org.pimslims.model.api LabBookEntryTest.java
 * 
 * @author bl67
 * @date 28 Oct 2008
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
package org.pimslims.model.api;

import java.util.Collection;
import java.util.Iterator;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.Note;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;
import org.pimslims.test.AbstractTestCase;

/**
 * LabBookEntryTest
 * 
 */
public class LabBookEntryTester extends AbstractTestCase {

    public void testGetOrderedNote() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            Target t = create(Target.class);
            Note n1 = create(Note.class, Attachment.PROP_PARENTENTRY, t);
            Note n2 = create(Note.class, Attachment.PROP_PARENTENTRY, t);
            Note n3 = create(Note.class, Attachment.PROP_PARENTENTRY, t);
            Note n4 = create(Note.class, Attachment.PROP_PARENTENTRY, t);
            Note n5 = create(Note.class, Attachment.PROP_PARENTENTRY, t);
            Note n6 = create(Note.class, Attachment.PROP_PARENTENTRY, t);
            Collection<Note> notes = t.getNotes();
            Iterator<Note> it = notes.iterator();
            assertEquals(n1, it.next());
            assertEquals(n2, it.next());
            assertEquals(n3, it.next());
            assertEquals(n4, it.next());
            assertEquals(n5, it.next());
            assertEquals(n6, it.next());

        } finally {
            wv.abort();
        }
    }

    public void testNotChangeable() {
        MetaClass metaClass = model.getMetaClass(Target.class.getName());
        MetaAttribute lastEditDate = metaClass.getAttribute(LabBookEntry.PROP_CREATIONDATE);
        assertFalse(lastEditDate.isChangeable());
        MetaAttribute creationDate = metaClass.getAttribute(LabBookEntry.PROP_CREATIONDATE);
        assertFalse(creationDate.isChangeable());
    }

    public void testLastEditDate() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            //Sample t = create(Sample.class);
            Sample t = new Sample(wv, UNIQUE);
            assertNull(t.getLastEditedDate());
            assertNotNull(t.getCreationDate());

            t.setDetails("new details");
            assertNotNull(t.getLastEditedDate());
        } finally {
            wv.abort();
        }
    }

}
