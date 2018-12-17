package org.pimslims.lab;

import java.util.Collections;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.location.Location;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.LocationUtility;

@SuppressWarnings("deprecation")
// Location is obsolete
public class LocationUtilityTest extends TestCase {

    private final AbstractModel model;

    private static final String UNIQUE = "lu" + System.currentTimeMillis();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(LocationUtilityTest.class);
    }

    public LocationUtilityTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    public void testGetCurrentLocationNowhere() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Holder holder = new Holder(version, "lu" + System.currentTimeMillis(), null);
            Assert.assertNull(ContainerUtility.getCurrentLocation(holder));
        } finally {
            version.abort();
        }

    }

    public void testMoveHolderFirstTime() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Holder holder = new Holder(version, "lu" + System.currentTimeMillis(), null);
            final Location location = new Location(version, "lu");
            ContainerUtility.move(holder, location);
            Assert.assertEquals(location, ContainerUtility.getCurrentLocation(holder));
        } finally {
            version.abort();
        }
    }

    public void testMoveHolderSecondTime() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Holder holder = new Holder(version, "test2nd" + System.currentTimeMillis(), null);
            final Location here = new Location(version, "lu");
            ContainerUtility.move(holder, here);
            final Location there = new Location(version, "testthere");
            ContainerUtility.move(holder, there);
            Assert.assertEquals(there, ContainerUtility.getCurrentLocation(holder));
        } finally {
            version.abort();
        }
    }

    public void testNoSamples() {
        final List<Sample> unlocated = LocationUtility.filterUnlocatedSamples(Collections.EMPTY_SET);
        Assert.assertEquals(0, unlocated.size());
        final List<Sample> located =
            LocationUtility.filterSamplesInLocations(Collections.EMPTY_SET, Collections.EMPTY_SET);
        Assert.assertEquals(0, located.size());
    }

    public void testFilterUnlocatedSample() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Sample sample = new Sample(version, LocationUtilityTest.UNIQUE);
            final List<Sample> unlocated =
                LocationUtility.filterUnlocatedSamples(Collections.singleton(sample));
            Assert.assertEquals(1, unlocated.size());
            final Location here = new Location(version, LocationUtilityTest.UNIQUE);
            final List<Sample> located =
                LocationUtility.filterSamplesInLocations(Collections.singleton(sample),
                    Collections.singleton(here));
            Assert.assertEquals(0, located.size());
        } finally {
            version.abort();
        }
    }

    public void testFilterLocatedSample() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Sample sample = new Sample(version, LocationUtilityTest.UNIQUE);
            final Holder holder = new Holder(version, LocationUtilityTest.UNIQUE, null);
            final Location here = new Location(version, LocationUtilityTest.UNIQUE);
            ContainerUtility.move(holder, here);
            sample.setHolder(holder);
            final List<Sample> unlocated =
                LocationUtility.filterUnlocatedSamples(Collections.singleton(sample));
            Assert.assertEquals(0, unlocated.size());

            final List<Sample> located =
                LocationUtility.filterSamplesInLocations(Collections.singleton(sample),
                    Collections.singleton(here));
            Assert.assertEquals(1, located.size());
        } finally {
            version.abort();
        }
    }
}
