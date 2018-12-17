/**
 * pims-model org.pimslims.model.role OutputSampleTest.java
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
package org.pimslims.model.role;

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.test.AbstractTestCase;

/**
 * OutputSampleTest
 * 
 */
public class OutputSampleTest extends AbstractTestCase {
    public void testSampleToOutPutSample() throws AbortedException, ConstraintException, AccessException {
        String samplehook = null;
        wv = getWV();
        try {
            Sample sample = create(Sample.class);
            samplehook = sample.get_Hook();
            OutputSample os = create(OutputSample.class);
            os.setSample(sample);
            assertEquals(os, sample.getOutputSample());
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }

        rv = getRV();
        try {
            Sample sample = rv.get(samplehook);
            assertNotNull(sample.getOutputSample());
        } finally {
            rv.abort();
        }

    }
}
