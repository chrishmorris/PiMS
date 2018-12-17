/**
 * 
 */
package org.pimslims.crystallization.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.holder.HolderLocation;
import org.pimslims.model.location.Location;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.sample.Sample;

/**
 * @author bl67
 * 
 */
public class DAOUtils {

    /**
     * <p>
     * Standard string to use when null or no information is provided for an attribute with a NOT NULL
     * constraint.
     * </p>
     */
    public static final String UNKNOWN = "Unknown";

    /**
     * <p>
     * Find the HolderCategory for xtalPiMS:DeepWellPlate.
     * </p>
     * 
     * @param rv - the ReadableVersion to search
     * @return The HolderCategory, or null if not found
     */
    protected static HolderCategory findHolderCategory(final ReadableVersion rv, final String name) {

        final Map<String, Object> attr = new HashMap<String, Object>();
        attr.put(HolderCategory.PROP_NAME, name);
        return rv.findFirst(HolderCategory.class, attr);

    }

    public static Sample getSampleByPosition(final Holder holder, final WellPosition well) {
        for (final Sample sample : holder.getSamples()) {
            final WellPosition sampleWell =
                new WellPosition(sample.getRowPosition(), sample.getColPosition(), sample.getSubPosition());
            if (sampleWell.equals(well)) {
                return sample;
            }

        }
        return null;

    }

    public static Location getCurrentLocation(final Holder holder) {
        Location ret = null;
        final Collection<HolderLocation> locations = holder.getHolderLocations();
        for (final Object element : locations) {
            final HolderLocation hl = (HolderLocation) element;
            if (null == hl.getEndDate()) {
                // this could be it
                if (null != ret) {
                    throw new IllegalStateException("This holder is in two places");
                }
                ret = hl.getLocation();
            }
        }
        return ret;
    }
}
