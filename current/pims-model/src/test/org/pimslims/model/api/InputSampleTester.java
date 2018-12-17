/**
 * ccp.api.pojo InputSampleTest.java
 * 
 * @date 10 Sep 2007 14:54:03
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 * Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
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
package org.pimslims.model.api;

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.test.AbstractTestCase;


/**
 * InputSampleTest
 * 
 */
public class InputSampleTester extends AbstractTestCase {

    public void testSample() throws AbortedException, ConstraintException, AccessException {
        String sampleHook = null;
        String inputSampleHook = null;

        wv = getWV();
        try {
            InputSample is = create(InputSample.class);
            Sample sample = create(Sample.class);
            is.setSample(sample);
            sampleHook = sample.get_Hook();
            inputSampleHook = is.get_Hook();
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }

        rv = getRV();
        try {
            Sample sample = rv.get(sampleHook);
            assertNotNull(sample);
            assertTrue(sample.getInputSamples().size() == 1);
            assertEquals(inputSampleHook, sample.getInputSamples().iterator().next().get_Hook());
        } finally {
            if (!rv.isCompleted())
                rv.abort();
        }
    }
}
