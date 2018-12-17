/**
 * org.pimslims.upgrader RefDataToolTest.java
 * 
 * @date 13-Dec-2006 14:08:27
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
package org.pimslims.upgrader;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;

/**
 * RefDataToolTest
 * 
 */
public class RefDataToolTest extends TestCase {

    private static final String UNIQUE = "rdt" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for RefDataToolTest
     * 
     * @param name
     */
    public RefDataToolTest(String name) {
        super(name);

        this.model = ModelImpl.getModel();
    }

    // this test will fail in empty DB
    public void _testCurrentRefDataIsValid() {
        RefDataTool rdt = new RefDataTool();
        assertTrue("Current DB's ref data has problem", rdt.isRefDatasValid());
    }

    public void testNoNeedToMergeSampleCategories() {
        WritableVersion version = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            RefDataTool.renameSampleCategory("old" + UNIQUE, "new" + UNIQUE, version);
            assertNull(version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "old" + UNIQUE));
            assertNotNull(version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "new" + UNIQUE));
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public void testMergeSampleCategories() throws ConstraintException {
        WritableVersion version = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            new SampleCategory(version, "new" + UNIQUE);
            SampleCategory old = new SampleCategory(version, "old" + UNIQUE);
            Sample sample = new Sample(version, UNIQUE);
            sample.addSampleCategory(old);
            version.flush();
            RefDataTool.renameSampleCategory("old" + UNIQUE, "new" + UNIQUE, version);
            version.flush();
            assertNull(version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "old" + UNIQUE));
            assertNotNull(version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "new" + UNIQUE));
            assertEquals(1, sample.getSampleCategories().size());
            assertEquals("new" + UNIQUE, sample.getSampleCategories().iterator().next().getName());
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public void testRenameSampleCategory() throws ConstraintException {
        WritableVersion version = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            SampleCategory old = new SampleCategory(version, "old" + UNIQUE);
            Sample sample = new Sample(version, UNIQUE);
            sample.addSampleCategory(old);
            version.flush();
            RefDataTool.renameSampleCategory("old" + UNIQUE, "new" + UNIQUE, version);
            assertNull(version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "old" + UNIQUE));
            assertNotNull(version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "new" + UNIQUE));
            assertEquals(1, sample.getSampleCategories().size());
            assertEquals("new" + UNIQUE, sample.getSampleCategories().iterator().next().getName());
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }
}
