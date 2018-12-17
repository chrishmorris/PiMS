/**
 * org.pimslims.model.attribute DerivedAttributeTest.java
 * 
 * @date 3 Dec 2007 09:19:31
 * 
 * @author Bill Lin
 * 
 * Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 2.0
 * 
 * Copyright (c) 2007
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 * 
 * A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.model.attribute;

import java.util.Collections;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.test.AbstractTestCase;


/**
 * DerivedAttributeTest
 * 
 */
public class MultipleAttributeTest extends AbstractTestCase {

    public void testSetMultipleAttribute() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            Protocol protocol = create(Protocol.class);
            protocol.setRemarks(Collections.singletonList("testRemark"));
            Protocol protocol2 = create(Protocol.class);
            protocol2.setRemarks((protocol.getRemarks()));
            wv.flush();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }

    }

}
