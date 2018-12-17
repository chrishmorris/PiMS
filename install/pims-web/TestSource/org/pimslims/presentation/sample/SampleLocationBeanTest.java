/**
 * current-pims-web org.pimslims.presentation.sample SampleLocationBeanTest.java
 * 
 * @author cm65
 * @date 22 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
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
import org.pimslims.lab.ContainerUtility;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.location.Location;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ModelObjectShortBean;

/**
 * SampleLocationBeanTest
 * 
 */

@SuppressWarnings("deprecation")
// Location is obsolete
public class SampleLocationBeanTest extends TestCase {

    private static final String UNIQUE = "test" + System.currentTimeMillis();

    //private static final Calendar NOW = java.util.Calendar.getInstance();

    private final AbstractModel model;

    /**
     * @param arg0
     */
    public SampleLocationBeanTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.sample.SampleLocationBean#getCurrentLocation(org.pimslims.model.sample.Sample)}
     * .
     * 
     * @throws ConstraintException
     */
    public final void testNoHolder() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Sample sample = new Sample(version, SampleLocationBeanTest.UNIQUE);
            final SampleLocationBean bean = SampleLocationBean.getCurrentLocation(sample);
            Assert.assertNull(bean);
        } finally {
            version.abort();
        }
    }

    public final void testNoCurrentLocation() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Sample sample = new Sample(version, SampleLocationBeanTest.UNIQUE);
            final Holder holder = new Holder(version, SampleLocationBeanTest.UNIQUE, null);
            sample.setHolder(holder);
            final SampleLocationBean bean = SampleLocationBean.getCurrentLocation(sample);
            Assert.assertNull(bean);
        } finally {
            version.abort();
        }
    }

    public final void testCurrentLocation() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Sample sample = new Sample(version, SampleLocationBeanTest.UNIQUE);
            final Holder holder = new Holder(version, SampleLocationBeanTest.UNIQUE, null);
            sample.setHolder(holder);
            final Location location = new Location(version, "loc" + SampleLocationBeanTest.UNIQUE);
            ContainerUtility.move(sample, location);
            final SampleLocationBean bean = SampleLocationBean.getCurrentLocation(sample);
            Assert.assertNotNull(bean);
            Assert.assertEquals(location.getName(), bean.getName());
            Assert.assertEquals(location.get_Hook(), bean.getHook());
        } finally {
            version.abort();
        }
    }

    public final void testSetLocationNoHolder() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Sample sample = new Sample(version, SampleLocationBeanTest.UNIQUE);
            final Location location = new Location(version, "loc" + SampleLocationBeanTest.UNIQUE);
            ContainerUtility.move(sample, location);
            final SampleLocationBean bean = SampleLocationBean.getCurrentLocation(sample);
            Assert.assertNotNull(bean);
            Assert.assertEquals(location.getName(), bean.getName());
        } finally {
            version.abort();
        }
    }

    public void testGetCurrentLocationTrailLocation() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Location shelf = new Location(version, SampleLocationBeanTest.UNIQUE);
            final Location fridge = new Location(version, "loc" + SampleLocationBeanTest.UNIQUE);
            ContainerUtility.move(shelf, fridge);
            final List<ModelObjectShortBean> breadcrumbs = SampleLocationBean.getCurrentLocationTrail(shelf);
            Assert.assertEquals(1, breadcrumbs.size());
            final ModelObjectShortBean bean = breadcrumbs.get(0);
            Assert.assertEquals(fridge.getName(), bean.getName());
        } finally {
            version.abort();
        }
    }

    public void testGetCurrentLocationTrailHH() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Holder shelf = new Holder(version, SampleLocationBeanTest.UNIQUE, null);
            final Holder fridge = new Holder(version, "loc" + SampleLocationBeanTest.UNIQUE, null);
            ContainerUtility.move(shelf, fridge);
            final List<ModelObjectShortBean> breadcrumbs = SampleLocationBean.getCurrentLocationTrail(shelf);
            Assert.assertEquals(1, breadcrumbs.size());
            final ModelObjectShortBean bean = breadcrumbs.get(0);
            Assert.assertEquals(fridge.getName(), bean.getName());
        } finally {
            version.abort();
        }
    }

    public void testGetCurrentLocationTrailHolder() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Holder holder = new Holder(version, SampleLocationBeanTest.UNIQUE, null);
            final Location location = new Location(version, "loc" + SampleLocationBeanTest.UNIQUE);
            ContainerUtility.move(holder, location);
            final List<ModelObjectShortBean> breadcrumbs = SampleLocationBean.getCurrentLocationTrail(holder);
            Assert.assertEquals(1, breadcrumbs.size());
            final ModelObjectShortBean bean = breadcrumbs.get(0);
            Assert.assertEquals(location.getName(), bean.getName());
        } finally {
            version.abort();
        }
    }

    public void testRemoveHL() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Holder holder = new Holder(version, SampleLocationBeanTest.UNIQUE, null);
            final Location location = new Location(version, "loc" + SampleLocationBeanTest.UNIQUE);
            ContainerUtility.move(holder, location);
            ContainerUtility.move(holder, null);
            Assert.assertEquals(null, holder.getContainer());
        } finally {
            version.abort();
        }
    }

    public void testGetCurrentLocationTrailSHL() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Sample sample = new Sample(version, SampleLocationBeanTest.UNIQUE);
            final Holder holder = new Holder(version, SampleLocationBeanTest.UNIQUE + "H", null);
            ContainerUtility.move(sample, holder);
            final Location location = new Location(version, "loc" + SampleLocationBeanTest.UNIQUE);
            ContainerUtility.move(holder, location);
            final List<ModelObjectShortBean> breadcrumbs = SampleLocationBean.getCurrentLocationTrail(sample);
            Assert.assertEquals(2, breadcrumbs.size());
            Assert.assertEquals(holder.getName(), breadcrumbs.get(1).getName());
            Assert.assertEquals(location.getName(), breadcrumbs.get(0).getName());
        } finally {
            version.abort();
        }
    }

    public void testGetCurrentLocationTrailS() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Sample sample = new Sample(version, SampleLocationBeanTest.UNIQUE);
            final List<ModelObjectShortBean> breadcrumbs = SampleLocationBean.getCurrentLocationTrail(sample);
            Assert.assertEquals(0, breadcrumbs.size());
        } finally {
            version.abort();
        }
    }
}
