/**
 * ccp.api.pojo ExperimentTester.java
 * 
 * @date 02-Nov-2006 08:58:07
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 *         Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 *           Copyright (c) 2006
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
package org.pimslims.model.api;

import java.util.Collections;
import java.util.Map;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;

/**
 * ExperimentTester
 * 
 */
public class SampleTester extends org.pimslims.test.AbstractTestCase {
    public void testSetSampleCategory() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            SampleCategory sc = create(SampleCategory.class);
            SampleCategory sc2 = create(SampleCategory.class);
            Sample sample =
                create(Sample.class, AbstractSample.PROP_SAMPLECATEGORIES, Collections.singletonList(sc));
            assertEquals(sc, sample.getSampleCategories().iterator().next());
            Map<String, Object> sampleAttrMap = new java.util.HashMap<String, Object>();
            sampleAttrMap.put(AbstractSample.PROP_NAME, "sample name" + System.currentTimeMillis());
            sampleAttrMap.put(AbstractSample.PROP_SAMPLECATEGORIES, Collections.singleton(sc));
            Sample sample2 = new Sample(wv, sampleAttrMap);
            assertEquals(sc, sample2.getSampleCategories().iterator().next());
            sample2.setSampleCategories(Collections.singleton(sc2));
            assertEquals(sc2, sample2.getSampleCategories().iterator().next());
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }

    public void testSetRow() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            Sample sample = create(Sample.class);
            sample.setRowPosition(8);
            assertEquals(new Integer(8), sample.getRowPosition());
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }

    public void testSetRow1536() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            Sample sample = create(Sample.class);
            HolderType type = new HolderType(wv, UNIQUE);
            type.setMaxRow(32);
            Holder holder = new Holder(wv, UNIQUE, type);
            sample.setHolder(holder);
            sample.setRowPosition("AF");
            assertEquals(new Integer(31), sample.getRowPosition());
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }

    public void testSetRowTooBig() throws AccessException {
        wv = getWV();
        try {
            Sample sample;
            try {
                sample = create(Sample.class);
                HolderType type = new HolderType(wv, UNIQUE);
                type.setMaxRow(8);
                Holder holder = new Holder(wv, UNIQUE, type);
                sample.setHolder(holder);
            } catch (ConstraintException e) {
                throw new RuntimeException(e);
            }
            sample.setRowPosition(9);
            fail("row too big");
        } catch (ConstraintException e) {
            // that's right
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }

}
