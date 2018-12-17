/**
 * pims-web org.pimslims.presentation.sample HolderContentsTest.java
 * 
 * @author cm65
 * @date 28 Jun 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.sample;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ModelObjectBean;

/**
 * HolderContentsTest
 * 
 */
public class HolderContentsTest extends TestCase {

    private static final String UNIQUE = "hc" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for SampleBeanTest
     * 
     * @param name
     */
    public HolderContentsTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testNoType() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Holder holder = new Holder(version, HolderContentsTest.UNIQUE, null);
            HolderContents.getHolderContents(holder);
            Assert.fail("no type");
        } catch (final IllegalStateException ex) {
            // that's fine
        } finally {
            version.abort();
        }
    }

    public void test2x3x5empty() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final HolderType type = new HolderType(version, HolderContentsTest.UNIQUE);
            type.setMaxRow(2);
            type.setMaxColumn(3);
            type.setMaxSubPosition(5);
            final Holder holder = new Holder(version, HolderContentsTest.UNIQUE, type);
            final List<List<List<ModelObjectBean>>> contents = HolderContents.getHolderContents(holder);
            Assert.assertEquals(2, contents.size());
            final List<List<ModelObjectBean>> row1 = contents.iterator().next();
            Assert.assertEquals(3, row1.size());
            final List subs = row1.iterator().next();
            Assert.assertEquals(5, subs.size());
        } finally {
            version.abort();
        }
    }

    public void testOneSample() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final HolderType type = new HolderType(version, HolderContentsTest.UNIQUE);
            type.setMaxRow(2);
            type.setMaxColumn(1);
            type.setMaxSubPosition(1);
            final Holder holder = new Holder(version, HolderContentsTest.UNIQUE, type);
            final Sample sample = new Sample(version, HolderContentsTest.UNIQUE);
            sample.setHolder(holder);
            sample.setRowPosition(1);
            sample.setColPosition(1);
            sample.setSubPosition(1);

            final List<List<List<ModelObjectBean>>> contents = HolderContents.getHolderContents(holder);
            Assert.assertEquals(2, contents.size());
            final List<List<ModelObjectBean>> row1 = contents.iterator().next();
            Assert.assertEquals(1, row1.size());
            final List subs = row1.iterator().next();
            Assert.assertEquals(1, subs.size());
            final SampleBean bean = (SampleBean) subs.iterator().next();
            Assert.assertNotNull(bean);
            Assert.assertEquals(sample.get_Hook(), bean.getHook());
        } finally {
            version.abort();
        }
    }

    public void testOneHolder() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final HolderType type = new HolderType(version, HolderContentsTest.UNIQUE);
            type.setMaxRow(2);
            type.setMaxColumn(1);
            type.setMaxSubPosition(1);
            final Holder holder = new Holder(version, HolderContentsTest.UNIQUE, type);
            final Holder contained =
                new Holder(version, HolderContentsTest.UNIQUE + "inner", new HolderType(version,
                    HolderContentsTest.UNIQUE + "inner"));
            contained.setParentHolder(holder);
            contained.setRowPosition(1);
            contained.setColPosition(1);
            contained.setSubPosition(1);

            final List<List<List<ModelObjectBean>>> contents = HolderContents.getHolderContents(holder);
            Assert.assertEquals(2, contents.size());
            final List<List<ModelObjectBean>> row1 = contents.iterator().next();
            Assert.assertEquals(1, row1.size());
            final List subs = row1.iterator().next();
            Assert.assertEquals(1, subs.size());
            final ModelObjectBean bean = (ModelObjectBean) subs.iterator().next();
            Assert.assertNotNull(bean);
            Assert.assertEquals(contained.get_Hook(), bean.getHook());
        } finally {
            version.abort();
        }
    }

    public void test1D() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final HolderType type = new HolderType(version, HolderContentsTest.UNIQUE);
            type.setMaxRow(2);
            type.setMaxColumn(1);
            type.setMaxSubPosition(1);
            final Holder holder = new Holder(version, HolderContentsTest.UNIQUE, type);
            Assert.assertEquals(1, HolderContents.getDimensions(holder));
        } finally {
            version.abort();
        }
    }

    public void test3D() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final HolderType type = new HolderType(version, HolderContentsTest.UNIQUE);
            type.setMaxRow(4);
            type.setMaxColumn(7);
            type.setMaxSubPosition(4);
            final Holder holder = new Holder(version, HolderContentsTest.UNIQUE, type);
            Assert.assertEquals(3, HolderContents.getDimensions(holder));
        } finally {
            version.abort();
        }
    }
}
