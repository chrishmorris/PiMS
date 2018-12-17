/**
 * pims-model org.pimslims.model.attribute GetValueTest.java
 * 
 * @author bl67
 * @date 12 Aug 2008
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2008 bl67 This library is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 * 
 * A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */
package org.pimslims.model.attribute;

import java.util.LinkedList;
import java.util.List;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.test.AbstractTestCase;

/**
 * GetValueTest
 * 
 */
public class GetValueTest extends AbstractTestCase {

    public void testGet_Value() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            List<String> remarks = new LinkedList<String>();
            remarks.add("test remarks");
            Protocol protocol = create(Protocol.class);
            Object value = protocol.get_Value(Protocol.PROP_REMARKS);
            assertEquals(protocol.getRemarks(), value);

            Protocol p2 = wv.findFirst(Protocol.class, Protocol.PROP_NAME, "PiMS LIC Plate culture");
            if (p2 != null) {
                value = p2.get_Value(Protocol.PROP_REMARKS);
                assertEquals(p2.getRemarks(), value);
            }
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }
}
