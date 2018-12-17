/**
 * V2_2-pims-web org.pimslims.servlet.standard ViewRefSampleTest.java
 * 
 * @author cm65
 * @date 25 Apr 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.servlet.standard;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;

/**
 * ViewRefSampleTest
 * 
 */
public class ViewRefSampleTest extends TestCase {

    private static final String UNIQUE = "vrf" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * @param name name of test method to run
     */
    public ViewRefSampleTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.standard.ViewRefSample#getMayUpdate(org.pimslims.model.sample.RefSample)}.
     * 
     * @throws ConstraintException
     */
    public final void testMayUpdate() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final RefSample recipe = new RefSample(version, ViewRefSampleTest.UNIQUE);
            Assert.assertTrue(ViewRefSample.getMayUpdate(recipe));
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.standard.ViewRefSample#getMayUpdate(org.pimslims.model.sample.RefSample)}.
     * 
     * @throws ConstraintException
     */
    public final void testInUse() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final RefSample recipe = new RefSample(version, "recipe" + ViewRefSampleTest.UNIQUE);
            final Sample stock = new Sample(version, "stock" + ViewRefSampleTest.UNIQUE);
            stock.setIsActive(true);
            stock.setRefSample(recipe);
            Assert.assertFalse(ViewRefSample.getMayUpdate(recipe));
        } finally {
            version.abort();
        }
    }

}
