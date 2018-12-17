/**
 * pims-model org.pimslims.model.role removeTest.java
 * 
 * @author bl67
 * @date 17 Nov 2008
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
package org.pimslims.model.role;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.RefSample;
import org.pimslims.test.AbstractTestCase;

/**
 * removeTest
 * 
 */
public class removeTest extends AbstractTestCase {

    public void testRemoveFromRefSample() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            SampleCategory sc = create(SampleCategory.class);
            RefSample refSample = create(RefSample.class, AbstractSample.PROP_SAMPLECATEGORIES, sc);
            refSample.remove(AbstractSample.PROP_SAMPLECATEGORIES, sc);
            assertEquals(0, refSample.getSampleCategories().size());
        } finally {
            wv.abort();
        }
    }

}
