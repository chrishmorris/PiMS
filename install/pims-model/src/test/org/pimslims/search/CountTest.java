/**
 * DM org.pimslims.metamodel PagingTest.java
 * 
 * @author bl67
 * @date 22 Feb 2008
 * 
 *       Protein Information Management System
 * @version: 2.1
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
package org.pimslims.search;

import java.util.HashMap;
import java.util.Map;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.test.AbstractTestCase;

/**
 * test rv Count
 * 
 */
public class CountTest extends AbstractTestCase {
    public void testBasic() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            Class testClass = Molecule.class;
            int oldCount = wv.getCountOfAll(model.getMetaClass(testClass.getName()));
            create(testClass);
            int newCount = wv.getCountOfAll(model.getMetaClass(testClass.getName()));
            assertEquals(oldCount + 1, newCount);
        } finally {
            wv.abort();
        }

    }

    public void testCritial() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            Class testClass = Molecule.class;
            String details = "details" + System.currentTimeMillis();
            Map<String, Object> critials = new HashMap<String, Object>();
            critials.put(Molecule.PROP_SEQUENCE_DETAILS, details);
            int oldCount = wv.count(testClass, critials);
            assertEquals(0, oldCount);
            create(testClass, Molecule.PROP_SEQUENCE_DETAILS, details);
            int newCount = wv.count(testClass, critials);
            assertEquals(1, newCount);
        } finally {
            wv.abort();
        }

    }

}
