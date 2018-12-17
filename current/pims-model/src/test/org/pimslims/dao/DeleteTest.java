/**
 * pims-model org.pimslims.dao DeleteTest.java
 * 
 * @author bl67
 * @date 18 Dec 2008
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2008 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.dao;

import java.util.Collections;

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.DeleteConstraintException;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.test.AbstractTestCase;

/**
 * DeleteTest
 * 
 */
public class DeleteTest extends AbstractTestCase {

    String sampleCategoryHook;

    String sampleCategoryHook2;

    String sampleHook;

    String sampleHook2;

    String refisHook;

    @Override
    public void setUp() throws AccessException, ConstraintException, AbortedException {
        wv = getWV();
        try {
            SampleCategory sc = create(SampleCategory.class);
            sampleCategoryHook = sc.get_Hook();
            SampleCategory sc2 = create(SampleCategory.class);
            sampleCategoryHook2 = sc2.get_Hook();

            Sample sample = create(Sample.class, AbstractSample.PROP_SAMPLECATEGORIES, sc);
            sampleHook = sample.get_Hook();

            Sample sample2 = create(Sample.class, AbstractSample.PROP_SAMPLECATEGORIES, sc2);
            sampleHook2 = sample2.get_Hook();

            RefInputSample refis = create(RefInputSample.class, RefInputSample.PROP_SAMPLECATEGORY, sc);
            create(RefInputSample.class, RefInputSample.PROP_SAMPLECATEGORY, sc);
            create(RefOutputSample.class, RefOutputSample.PROP_SAMPLECATEGORY, sc);
            refisHook = refis.get_Hook();
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }

    public void testNotDeletable() throws AccessException, AbortedException, ConstraintException {
        wv = getWV();
        RefInputSample refis = wv.get(refisHook);
        Sample sample = wv.get(sampleHook);
        try {
            SampleCategory sc = wv.get(sampleCategoryHook);
            sc.delete();
            wv.commit();
            fail();
        } catch (DeleteConstraintException e) {
            // e.printStackTrace();

            assertTrue("error message should contain sampleCategory is used by which object", e
                .getRequiredByObjects().contains(refis));
            assertFalse(e.getRequiredByObjects().contains(sample));

        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }

    public void testDelete() throws AccessException, AbortedException, ConstraintException {
        wv = getWV();
        Sample sample2 = wv.get(sampleHook2);
        try {
            SampleCategory sc = wv.get(sampleCategoryHook);
            SampleCategory sc2 = wv.get(sampleCategoryHook2);
            sample2.setSampleCategories(Collections.singleton(sc));
            sc2.delete();
            System.out.println("sample's category: " + sample2.getSampleCategories());
            wv.flush();
            wv.commit();

        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }

        wv = getWV();
        try {
            Sample sample2_reloaded = wv.get(sampleHook2);
            //make sure the sample does not containt the sc which is deleted
            assertEquals(1, sample2_reloaded.getSampleCategories().size());
            assertEquals(sampleCategoryHook, sample2_reloaded.getSampleCategories().iterator().next()
                .get_Hook());
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }
}
