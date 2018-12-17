/**
 * 
 */
package org.pimslims.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ContainerUtility;
import org.pimslims.lab.Util;
import org.pimslims.lab.Utils;
import org.pimslims.model.location.Location;
import org.pimslims.model.sample.Sample;

/**
 * @author cm65
 * 
 */
public class LocationUtility {

    @Deprecated
    // Location is obsolete
    public static Location duplicate(final Location location, final WritableVersion rw)
        throws ConstraintException, AccessException {

        Location dupl = null;

        // This name will be reset later
        dupl = new Location(rw, "aa" + System.currentTimeMillis());
        assert dupl != null;
        final Map<String, Object> duplParam = Utils.deleteNullValues(location.get_Values());

        duplParam.put(Location.PROP_NAME, Util.makeName(rw, location.get_Name(), dupl.getClass()));
        dupl.set_Values(duplParam);

        dupl.setLocation(location.getLocation());
        dupl.setContents(Collections.EMPTY_SET);
        dupl.setHolderLocations(Collections.EMPTY_SET);

        return dupl;
    }

    /**
     * @param samples the samples to filter
     * @return the samples with no specified location
     */
    public static List<Sample> filterUnlocatedSamples(final Collection<Sample> samples) {
        final List<Sample> ret = new ArrayList(samples.size());
        for (final Iterator iterator = samples.iterator(); iterator.hasNext();) {
            final Sample sample = (Sample) iterator.next();
            if (null == ContainerUtility.getCurrentLocation(sample)) {
                ret.add(sample);
            }
        }
        return ret;
    }

    /**
     * @param samples
     * @param locations
     * @return those samples that are in one of the locations
     */
    public static List<Sample> filterSamplesInLocations(final Collection<Sample> samples,
        final Collection<Location> locations) {
        final List<Sample> ret = new ArrayList(samples.size());
        for (final Iterator iterator = samples.iterator(); iterator.hasNext();) {
            final Sample sample = (Sample) iterator.next();
            if (locations.contains(ContainerUtility.getCurrentLocation(sample))) {
                ret.add(sample);
            }
        }
        return ret;
    }

    public static Collection<ModelObjectShortBean> getContentLocations(final Collection<Location> contents) {
        final Collection<ModelObjectShortBean> beans = new HashSet<ModelObjectShortBean>();
        for (final Location content : contents) {
            beans.add(new ModelObjectShortBean(content));
        }
        return beans;
    }
}
