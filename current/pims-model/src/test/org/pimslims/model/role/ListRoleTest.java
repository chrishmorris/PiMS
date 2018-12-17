/**
 * org.pimslims.model.role DerivedRoleTest.java
 * 
 * @date 3 Dec 2007 08:37:45
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
package org.pimslims.model.role;

import java.util.ArrayList;

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.test.AbstractTestCase;

/**
 * ListRoleTest
 * 
 */
public class ListRoleTest extends AbstractTestCase {
    public void testOrderedParameterDefinition() throws ConstraintException, AccessException,
        AbortedException {
        wv = getWV();
        String hook = null;
        try {
            Protocol protocol = create(Protocol.class);
            hook = protocol.get_Hook();
            ParameterDefinition pd2 = create(ParameterDefinition.class);
            pd2.setLabel("pd2");
            ParameterDefinition pd3 = create(ParameterDefinition.class);
            pd3.setLabel("pd3");
            ParameterDefinition pd1 = create(ParameterDefinition.class);
            pd1.setLabel("pd1");
            ArrayList<ParameterDefinition> paramList = new ArrayList<ParameterDefinition>();
            paramList.add(pd1);
            paramList.add(pd2);
            paramList.add(pd3);
            protocol.setParameterDefinitions(paramList);
            verifyProtocolPD(protocol);
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
        rv = getRV();
        try {
            Protocol protocol = rv.get(hook);
            verifyProtocolPD(protocol);
        } finally {
            if (!rv.isCompleted())
                rv.abort();
        }
    }

    /**
     * @param protocol
     */
    private void verifyProtocolPD(Protocol protocol) {
        Integer i = 1;
        for (ParameterDefinition pd : protocol.getParameterDefinitions()) {
            assertEquals("pd" + i, pd.getLabel());
            i += 1;
        }

    }

    public void testNoNullPD() {
        rv = getRV();
        try {
            for (Protocol protocol : rv.getAll(Protocol.class, 0, 100)) {
                for (ParameterDefinition pd : protocol.getParameterDefinitions())
                    assertNotNull(pd);
            }
        } finally {
            rv.abort();
        }
    }

}
