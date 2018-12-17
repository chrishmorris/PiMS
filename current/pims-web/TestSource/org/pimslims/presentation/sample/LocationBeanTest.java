/**
 * pims-web org.pimslims.presentation.sample LocationBeanTest.java
 * 
 * @author Marc Savitsky
 * @date 22 Jul 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation.sample;

import java.util.Collection;

import junit.framework.Assert;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.location.Location;
import org.pimslims.test.AbstractTestCase;

/**
 * LocationBeanTest
 * 
 */
@Deprecated
// Location is obsolete
public class LocationBeanTest extends AbstractTestCase {

    private Location locationA;

    private int count;

    /**
     * @param arg0
     */
    public LocationBeanTest(final String methodName) {
        super(methodName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.wv = this.getWV();
        final Collection<LocationBean> beans = LocationBean.getAllLocationsIn(this.wv, null);
        this.count = beans.size();

        this.locationA = LocationBeanTest.createLocation(this.wv);
        this.locationA.setName("location A");
        final Location locationB = LocationBeanTest.createLocation(this.wv);
        locationB.setName("location B");

        final Location locationAA = LocationBeanTest.createLocation(this.wv);
        locationAA.setName("location AA");
        locationAA.setLocation(this.locationA);
        final Location locationAB = LocationBeanTest.createLocation(this.wv);
        locationAB.setName("location AB");
        locationAB.setLocation(this.locationA);
        final Location locationAC = LocationBeanTest.createLocation(this.wv);
        locationAC.setName("location AC");
        locationAC.setLocation(this.locationA);

        final Location locationBA = LocationBeanTest.createLocation(this.wv);
        locationBA.setName("location BA");
        locationBA.setLocation(locationB);
        final Location locationBB = LocationBeanTest.createLocation(this.wv);
        locationBB.setName("location BB");
        locationBB.setLocation(locationB);

        final Location locationAAA = LocationBeanTest.createLocation(this.wv);
        locationAAA.setName("location AAA");
        locationAAA.setLocation(locationAA);
        final Location locationAAB = LocationBeanTest.createLocation(this.wv);
        locationAAB.setName("location AAB");
        locationAAB.setLocation(locationAA);

        final Location locationABA = LocationBeanTest.createLocation(this.wv);
        locationABA.setName("location ABA");
        locationABA.setLocation(locationAB);
        final Location locationABB = LocationBeanTest.createLocation(this.wv);
        locationABB.setName("location ABB");
        locationABB.setLocation(locationAB);

        final Location locationBAA = LocationBeanTest.createLocation(this.wv);
        locationBAA.setName("location BAA");
        locationBAA.setLocation(locationBA);
        final Location locationBAB = LocationBeanTest.createLocation(this.wv);
        locationBAB.setName("location BAB");
        locationBAB.setLocation(locationBA);

        final Location locationBBA = LocationBeanTest.createLocation(this.wv);
        locationBBA.setName("location BBA");
        locationBBA.setLocation(locationBB);
        final Location locationBBB = LocationBeanTest.createLocation(this.wv);
        locationBBB.setName("location BBB");
        locationBBB.setLocation(locationBB);
    }

    /**
     * LocationBeanTest.createLocation
     * 
     * @param wv
     * @return
     */
    public static Location createLocation(final WritableVersion wv) throws ConstraintException {
        return new Location(wv, AbstractTestCase.UNIQUE);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        if (this.wv.isCompleted()) {
            // can't tidy up
            return;
        }
        if (null != this.wv) {
            this.wv.abort(); // not testing persistence
        }
    }

    public final void testgetAllLocationsInNull() throws ConstraintException {

        try {
            final Collection<LocationBean> beans = LocationBean.getAllLocationsIn(this.wv, null);
            Assert.assertEquals("", 2, beans.size() - this.count);

        } finally {
            this.wv.abort();
        }
    }

    public final void testgetAllLocationsInLoationA() throws ConstraintException {

        try {
            final LocationBean beanA = new LocationBean(this.locationA);
            final Collection<LocationBean> beans = LocationBean.getAllLocationsIn(this.wv, beanA);
            Assert.assertEquals("", 3, beans.size());

        } finally {
            this.wv.abort();
        }
    }

}
