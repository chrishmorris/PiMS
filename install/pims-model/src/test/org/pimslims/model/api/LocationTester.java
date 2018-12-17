/**
 * pims-model org.pimslims.model.api LocationTest.java
 * 
 * @author bl67
 * @date 24 Oct 2008
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

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.location.Location;
import org.pimslims.test.AbstractTestCase;

/**
 * LocationTest
 * 
 */
public class LocationTester extends AbstractTestCase {

    public void testDerivedAttribute() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            Location location = create(Location.class);
            location.setTemperature(1.0f);
            location.setTempDisplayUnit("c");
            assertEquals("1.0 c", location.getTempWithDisplayUnit());
            location.setPressure(1.0f);
            location.setPressureDisplayUnit("p");
            assertEquals("1.0 p", location.getPressureWithDisplayUnit());

        } finally {
            wv.abort();
        }
    }
}
