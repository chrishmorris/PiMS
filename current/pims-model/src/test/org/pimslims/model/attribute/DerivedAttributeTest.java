/**
 * org.pimslims.model.attribute DerivedAttributeTest.java
 * 
 * @date 3 Dec 2007 09:19:31
 * 
 * @author Bill Lin
 * 
 *         Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 2.0
 * 
 *           Copyright (c) 2007
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
package org.pimslims.model.attribute;

import java.util.Collection;
import java.util.HashSet;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.Note;
import org.pimslims.model.target.Target;
import org.pimslims.test.AbstractTestCase;

/**
 * DerivedAttributeTest
 * 
 */
public class DerivedAttributeTest extends AbstractTestCase {
    protected final AbstractModel model = ModelImpl.getModel();

    public void testGetDerivedAttribute() {
        final MetaClass targetMC = model.getMetaClass(Target.class.getName());
        assertNotNull(targetMC.getAttribute("seqString"));
        assertTrue(targetMC.getAttribute("seqString").isDerived());
    }

    public void testDefaultCreateDate() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            final Target t = create(Target.class);
            assertTrue(t.getCreationDate() != null);

            final Attachment att = create(Note.class);
            assertTrue(att.getDate() != null);

        } finally {
            wv.abort();
        }
    }

    public void testDefaultCreator() throws AccessException, ConstraintException, AbortedException {
        String username;
        wv = getWV();
        try {
            final User user = create(User.class);
            username = user.getName();
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        wv = getWV(username);
        try {
            final Target t = create(Target.class);
            assertTrue(t.getCreator() != null);

            final Attachment att = create(Note.class);
            assertTrue(att.getAuthor() != null);

        } finally {
            wv.abort();
        }
    }

    public void testSetAliasNames() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            final Target t = create(Target.class);
            final Collection<String> aliasNames = new HashSet<String>();
            aliasNames.add("" + System.currentTimeMillis());
            t.setAliasNames(aliasNames);
            wv.flush();
            aliasNames.clear();
            aliasNames.add("1" + System.currentTimeMillis());
            t.setAliasNames(aliasNames);
            assertEquals(1, t.getAliases().size());
            wv.flush();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }
}
