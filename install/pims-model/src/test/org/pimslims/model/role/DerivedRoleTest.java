/**
 * org.pimslims.model.role DerivedRoleTest.java
 * 
 * @date 3 Dec 2007 08:37:45
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
package org.pimslims.model.role;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.sample.Sample;

import junit.framework.TestCase;

/**
 * DerivedRoleTest
 * 
 */
public class DerivedRoleTest extends TestCase {
    protected final AbstractModel model = ModelImpl.getModel();

    public void testGetDerivedRole() {
        MetaClass sampleMC = model.getMetaClass(Sample.class.getName());
        assertNotNull(sampleMC.getMetaRole("localRiskPhrases"));
    }
}
