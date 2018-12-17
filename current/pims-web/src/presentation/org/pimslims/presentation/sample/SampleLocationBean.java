/**
 * current-pims-web org.pimslims.presentation.sample SampleLocationBean.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pimslims.model.holder.Containable;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.holder.HolderLocation;
import org.pimslims.model.location.Location;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;

/**
 * SampleLocationBean
 * 
 */
public class SampleLocationBean extends ModelObjectBean {

    private static final Map<String, Object> NO_END_DATE = new HashMap<String, Object>();
    static {
        SampleLocationBean.NO_END_DATE.put(HolderLocation.PROP_ENDDATE, null);
    }

    /**
     * @param modelObject
     */
    @Deprecated
    // location is obsolete
    public SampleLocationBean(final Location location) {
        super(location);
    }

    @Deprecated
    // location is obsolete
    public static SampleLocationBean getCurrentLocation(final Containable object) {

        if (object instanceof org.pimslims.model.sample.Sample) {
            final Sample sample = (Sample) object;
            final Holder holder = sample.getHolder();
            if (null == holder) {
                return null;
            }
            final HolderLocation hl =
                holder.findFirst(Holder.PROP_HOLDERLOCATIONS, SampleLocationBean.NO_END_DATE);
            if (null != hl) {
                final Location location = hl.getLocation();
                if (null == location) {
                    return null;
                }
                return new SampleLocationBean(location);
            }
        }

        if (object instanceof org.pimslims.model.holder.Holder) {
            final Holder holder = (Holder) object;
            final HolderLocation hl =
                holder.findFirst(Holder.PROP_HOLDERLOCATIONS, SampleLocationBean.NO_END_DATE);
            if (null != hl) {
                return new SampleLocationBean(hl.getLocation());
            }
        }

        if (object instanceof org.pimslims.model.location.Location) {
            final Location location = (Location) object;
            if (null != location.getLocation()) {
                return new SampleLocationBean(location.getLocation());
            }
        }

        return null;
    }

    // this part is not obsolete!
    public static List<ModelObjectShortBean> getCurrentLocationTrail(final Containable object) {

        if (object instanceof Location) {
            final List<ModelObjectShortBean> ret =
                SampleLocationBean.getCurrentLocationTrail((Location) object);
            Collections.reverse(ret);
            return ret;
        }

        final List<ModelObjectShortBean> ret = new ArrayList<ModelObjectShortBean>();

        Holder holder = null;
        Location location = null;
        if (object instanceof Sample) {
            holder = ((Sample) object).getHolder();
            if (null != holder) {
                ret.add(new ModelObjectShortBean(holder));
                holder = SampleLocationBean.addHolders(ret, holder);
                location = holder.getCurrentLocation();
            }
        } else {
            holder = (Holder) object;
            holder = SampleLocationBean.addHolders(ret, holder);
            location = holder.getCurrentLocation();
        }

        // process the holders
        if (null != location) {
            ret.add(new ModelObjectShortBean(location));
            ret.addAll(SampleLocationBean.getCurrentLocationTrail(location));
        }

        Collections.reverse(ret);
        return ret;
    }

    private static Holder addHolders(final List<ModelObjectShortBean> ret, Holder holder) {
        while (null != holder.getParentHolder()) {
            holder = (Holder) holder.getParentHolder();
            ret.add(new ModelObjectShortBean(holder));
        }
        return holder;
    }

    private static List<ModelObjectShortBean> getCurrentLocationTrail(Location location) {
        final List<ModelObjectShortBean> ret = new ArrayList<ModelObjectShortBean>();
        while (null != location.getLocation()) {
            location = location.getLocation();
            ret.add(new ModelObjectShortBean(location));
        }
        return ret;

    }
}
