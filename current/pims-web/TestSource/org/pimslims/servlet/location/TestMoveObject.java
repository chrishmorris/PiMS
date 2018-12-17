/**
 * pims-web org.pimslims.servlet.location TestMoveObject.java
 * 
 * @author Marc Savitsky
 * @date 18 Jul 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky *
 * 
 *           .
 */
package org.pimslims.servlet.location;

import junit.framework.Assert;

import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ContainerUtility;
import org.pimslims.model.holder.HolderLocation;
import org.pimslims.model.location.Location;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.sample.LocationBeanTest;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * TestMoveObject
 * 
 */
@Deprecated
// I think this is obsolete --Chris
public class TestMoveObject extends AbstractTestCase {

    Sample sample;

    Location locationA;

    Location locationB;

    /**
     * @param name
     */
    public TestMoveObject(final String name) {
        super(name);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.wv = this.getWV();

        this.locationA = LocationBeanTest.createLocation(this.wv);
        this.locationA.setName("locationA");
        this.locationB = LocationBeanTest.createLocation(this.wv);
        this.locationB.setName("locationB");

        this.sample = POJOFactory.createSample(this.wv);
        ContainerUtility.move(this.sample, this.locationA);
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

    public void testMoveObject() {
        try {
            MoveObject.move(this.sample, this.locationB);
            Location myLocation = null;
            for (final HolderLocation holderlocation : this.sample.getHolder().getHolderLocations()) {
                if (null == holderlocation.getEndDate()) {
                    myLocation = holderlocation.getLocation();
                }
            }
            Assert.assertEquals(this.locationB, myLocation);
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            this.wv.abort();
        }
    }

}
