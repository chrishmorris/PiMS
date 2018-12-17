/**
 * V3_3-web org.pimslims.presentation.sample SampleBeanTest.java
 * 
 * @author cm65
 * @date 6 Oct 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.sample;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.sample.Sample;

/**
 * SampleBeanTest
 * 
 */
public class SampleBeanTest extends TestCase {

    private static final String UNIQUE = "sb" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for SampleBeanTest
     * 
     * @param name
     */
    public SampleBeanTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.sample.SampleBean#SampleBean(org.pimslims.model.sample.Sample)}.
     */
    public void testSampleBeanSample() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Sample sample = new Sample(version, SampleBeanTest.UNIQUE);
            sample.setIsActive(true);
            //TODO sample.setSequence("TT");
            final SampleBean bean = new SampleBean(sample);
            Assert.assertEquals(sample.getName(), bean.getName());
            Assert.assertTrue(bean.getIsActive());
            Assert.assertEquals("TT", bean.getSequence());
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.sample.SampleBean#SampleBean(org.pimslims.model.sample.Sample)}.
     * obsolete public void testHolder() throws ConstraintException { final WritableVersion version =
     * this.model.getTestVersion(); try { final Sample sample = new Sample(version, SampleBeanTest.UNIQUE);
     * final Holder holder = new Holder(version, "plate" + SampleBeanTest.UNIQUE, null);
     * sample.setHolder(holder); sample.setColPosition(1); sample.setRowPosition(8); final Location location =
     * new Location(version, "loc" + SampleBeanTest.UNIQUE); ContainerUtility.move(sample, location); final
     * SampleBean bean = new SampleBean(sample);
     * 
     * Assert.assertEquals(holder.getName(), bean.getHolder().getName());
     * Assert.assertEquals(holder.get_Hook(), bean.getHolder().getHook()); Assert.assertEquals("H01",
     * bean.getPositionInPlate());
     * 
     * Assert.assertEquals(sample.getName(), bean.getName()); Assert.assertNull(bean.getIsActive());
     * Assert.assertEquals(location.getName(), bean.getLocation().getName()); } finally { version.abort(); } }
     */

}
