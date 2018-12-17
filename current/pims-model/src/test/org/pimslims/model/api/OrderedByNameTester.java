/**
 * pims-model org.pimslims.model.api OrderedByNameTest.java
 * 
 * @author bl67
 * @date 14 Oct 2008
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

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.PublicEntry;
import org.pimslims.test.AbstractTestCase;

/**
 * OrderedByNameTest
 * 
 */
public class OrderedByNameTester extends AbstractTestCase {
    public void testHolderTypeOrderByName() throws AccessException, ConstraintException {
        wv = getWV();

        try {
            String details = Calendar.getInstance().toString();

            HolderType htC = create(HolderType.class, PublicEntry.PROP_DETAILS, details);
            htC.setName("C" + System.currentTimeMillis());

            HolderType htA = create(HolderType.class, PublicEntry.PROP_DETAILS, details);
            htA.setName("A" + System.currentTimeMillis());

            HolderType htB = create(HolderType.class, PublicEntry.PROP_DETAILS, details);
            htB.setName("B" + System.currentTimeMillis());

            HolderType htE = create(HolderType.class, PublicEntry.PROP_DETAILS, details);
            htE.setName("E" + System.currentTimeMillis());

            HolderType htD = create(HolderType.class, PublicEntry.PROP_DETAILS, details);
            htD.setName("D" + System.currentTimeMillis());

            Collection<HolderType> results = wv.findAll(HolderType.class, PublicEntry.PROP_DETAILS, details);

            Iterator<HolderType> iter = results.iterator();
            assertEquals(htA, iter.next());
            assertEquals(htB, iter.next());
            assertEquals(htC, iter.next());
            assertEquals(htD, iter.next());
            assertEquals(htE, iter.next());

        } finally {
            wv.abort();
        }
    }
}
