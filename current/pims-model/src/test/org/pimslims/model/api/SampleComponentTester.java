/**
 * current-datamodel org.pimslims.hibernate.POJO SampleComponentTest.java
 * 
 * @author bl67
 * @date 10 Jul 2008
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
package org.pimslims.model.api;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.test.AbstractTestCase;


/**
 * SampleComponentTest
 * 
 */
public class SampleComponentTester extends AbstractTestCase {
    public void testDerivedName() throws AccessException, ConstraintException {
        wv = getWV();
        try {

            SampleComponent sc = create(SampleComponent.class);
            Substance component = sc.getRefComponent();
            AbstractSample sample = sc.getAbstractSample();
            assertTrue(sc.get_Name().contains(component.get_Name()));
            assertTrue(sc.get_Name().contains(sample.get_Name()));
        } finally {
            wv.abort();
        }
    }
}
