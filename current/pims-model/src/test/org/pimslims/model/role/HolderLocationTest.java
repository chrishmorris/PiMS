/**
 * pims-model org.pimslims.model.role OutputSampleTest.java
 * 
 * @author bl67
 * @date 12 Aug 2008
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
import org.pimslims.model.holder.HolderLocation;
import org.pimslims.test.AbstractTestCase;

/**
 * OutputSampleTest
 * 
 */
public class HolderLocationTest extends AbstractTestCase {

    public void testDeleteHolder() throws ConstraintException, AccessException {
        wv = getWV();
        try {
            HolderLocation hl = create(HolderLocation.class);
            hl.getHolder().delete();
            assertNull(wv.get(hl.get_Hook()));
        } finally {
            wv.abort();
        }

    }

    /* this used to fail because of a bad constraint
    public void testDeleteLocation() throws ConstraintException, AccessException {

        MetaRole role =
            model.getMetaClass(HolderLocation.class.getName()).getMetaRole(HolderLocation.PROP_LOCATION);
        ManyToOne annotation = ((MetaRoleImpl) role).getAnnotation(ManyToOne.class);
        assertNotNull(annotation);
        List<javax.persistence.CascadeType> cascadeTypes = Arrays.asList(annotation.cascade());
        for (Iterator iterator = cascadeTypes.iterator(); iterator.hasNext();) {
            javax.persistence.CascadeType cascadeType = (javax.persistence.CascadeType) iterator.next();
            System.out.println(cascadeType);
        }
        assertTrue(Arrays.deepToString(annotation.cascade()),
            cascadeTypes.contains(javax.persistence.CascadeType.REMOVE));

        wv = getWV();
        try {
            HolderLocation hl = create(HolderLocation.class);
            hl.getLocation().delete();
            wv.flush();
        } finally {
            wv.abort();
        }
    } */
}
