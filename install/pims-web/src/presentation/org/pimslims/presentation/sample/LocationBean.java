/**
 * current-pims-web org.pimslims.presentation.sample LocationBean.java
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.holder.HolderLocation;
import org.pimslims.model.location.Location;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;

/**
 * LocationBean
 * 
 */
@Deprecated
// Obsolete
public class LocationBean extends ModelObjectBean implements Comparable<Object>, Serializable {

    private String type;

    private Float temperature;

    private String temperatureDisplayUnit;

    private Float pressure;

    private String pressureDisplayUnit;

    private Location parentLocation;

    private Collection<HolderLocation> holderLocations;

    private final Vector<Location> locationStack = new Vector<Location>();

    private static final int MAX_LOCATIONS = 100;

    public LocationBean() {
        // caller must set attributes
    }

    public LocationBean(final Location location) {
        super(location);
        this.type = location.getLocationType();
        this.temperature = location.getTemperature();
        this.temperatureDisplayUnit = location.getTempDisplayUnit();
        this.pressure = location.getPressure();
        this.pressureDisplayUnit = location.getPressureDisplayUnit();
        this.parentLocation = location.getLocation();
        this.holderLocations = location.getHolderLocations();

        Location bean = location;
        do {
            this.locationStack.add(bean);
            bean = bean.getLocation();

        } while (null != bean);

        Collections.reverse(this.locationStack);
    }

    /*
    public int compareTo(final Object obj) {
        if (!(obj instanceof LocationBean)) {
            throw new ClassCastException("obj1 is not a LocationBean! ");
        }

        String argName = null;
        String thisName = null;

        argName = ((LocationBean) obj).getName();
        thisName = this.getName();

        return thisName.compareTo(argName);
    }
    */

    @Override
    public int compareTo(final Object obj) {
        if (!(obj instanceof LocationBean)) {
            throw new ClassCastException("obj1 is not a LocationBean! ");
        }

        final Vector v2 = ((LocationBean) obj).locationStack;
        final Vector v1 = this.locationStack;

        int i = 0;

        while (i < v1.size() && i < v2.size()) {

            final Location l1 = (Location) v1.get(i);
            final Location l2 = (Location) v2.get(i);

            final String name1 = l1.getName();
            final String name2 = l2.getName();

            if (!name1.equals(name2)) {
                return l1.getName().compareTo(l2.getName());
            }

            i++;
        }

        return new Integer(v1.size()).compareTo(new Integer(v2.size()));
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof LocationBean)) {
            throw new ClassCastException("object is not a LocationBean! ");
        }
        final LocationBean bean = (LocationBean) o;
        return bean.getHook().equals(this.getHook());
    }

    @Override
    public int hashCode() {
        return this.getHook().hashCode();
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setTemperature(final String temperature) {
        if (LocationBean.validString(temperature)) {
            this.temperature = Float.parseFloat(temperature);
        }
    }

    public void setTemperature(final Float temperature) {
        this.temperature = temperature;
    }

    public Float getTemperature() {
        return this.temperature;
    }

    public void setTemperatureDisplayUnit(final String temperatureDisplayUnit) {
        this.temperatureDisplayUnit = temperatureDisplayUnit;
    }

    public String getTemperatureDisplayUnit() {
        return this.temperatureDisplayUnit;
    }

    public void setPressure(final String pressure) {
        if (LocationBean.validString(pressure)) {
            this.pressure = Float.parseFloat(pressure);
        }
    }

    public void setPressure(final Float pressure) {
        this.pressure = pressure;
    }

    public Float getPressure() {
        return this.pressure;
    }

    public void setPressureDisplayUnit(final String pressureDisplayUnit) {
        this.pressureDisplayUnit = pressureDisplayUnit;
    }

    public String getPressureDisplayUnit() {
        return this.pressureDisplayUnit;
    }

    public void setParentLocation(final Location parentLocation) {
        this.parentLocation = parentLocation;
    }

    public Location getParentLocation() {
        return this.parentLocation;
    }

    public Collection<HolderLocation> getHolderLocations() {
        return this.holderLocations;
    }

    public String getLocationStack() {
        final StringBuffer sb = new StringBuffer();
        for (final Location l : this.locationStack) {
            sb.append(l.getName());
            sb.append(">");
        }

        return sb.substring(0, sb.length() - 1);
    }

    public Collection<LocationBean> getLocationBeanStack() {
        final Collection<LocationBean> beans = new HashSet<LocationBean>();
        for (final Location location : this.locationStack) {
            beans.add(new LocationBean(location));
        }
        return beans;
    }

    //LATER maybe this should list only "head" locations, those whose PROP_LOCATION is null
    public static Collection<LocationBean> getAllLocations(final ReadableVersion version) {
        final Collection<Location> locations = version.getAll(Location.class, 0, LocationBean.MAX_LOCATIONS);
        if (LocationBean.MAX_LOCATIONS == locations.size()) {
            throw new IllegalStateException("You have too many locations");
        }
        final List<LocationBean> ret = new ArrayList<LocationBean>(locations.size());
        for (final Iterator iter = locations.iterator(); iter.hasNext();) {
            final Location location = (Location) iter.next();
            ret.add(new LocationBean(location));
        }
        Collections.sort(ret);
        return ret;
    }

    /**
     * 
     * @param version
     * @return
     */
    public static Collection<LocationBean> getAllLocationsIn(final ReadableVersion version,
        final LocationBean bean) {

        //final Collection<Location> locations = version.getAll(Location.class, 0, LocationBean.MAX_LOCATIONS);
        final Map<String, Object> attributes = new HashMap<String, Object>();
        Location loc = null;
        if (null != bean) {
            loc = version.get(bean.getHook());
        }
        attributes.put(Location.PROP_LOCATION, loc);
        final Collection<Location> locations = version.findAll(Location.class, attributes);
        if (LocationBean.MAX_LOCATIONS == locations.size()) {
            throw new IllegalStateException("You have too many locations");
        }
        final List<LocationBean> ret = new ArrayList<LocationBean>(locations.size());
        for (final Iterator iter = locations.iterator(); iter.hasNext();) {
            final Location location = (Location) iter.next();
            ret.add(new LocationBean(location));
        }
        Collections.sort(ret);
        return ret;
    }

    /*
    public static Collection<HolderLocation> getActiveHolderLocations(final Location location) {
        final Collection<HolderLocation> holderLocations = new HashSet<HolderLocation>();
        final Collection<HolderLocation> allHolderLocations = location.getHolderLocations();
        for (final HolderLocation holderLocation : allHolderLocations) {
            if (null == holderLocation.getEndDate()) {
                holderLocations.add(holderLocation);
            }
        }
        return holderLocations;
    }
    */
    public static Collection<ModelObjectShortBean> getContents(final Location location) {

        final Collection<ModelObject> ret = new HashSet<ModelObject>();
        final Collection<HolderLocation> allHolderLocations = location.getHolderLocations();
        for (final HolderLocation holderLocation : allHolderLocations) {
            if (null == holderLocation.getEndDate()) {
                ret.add(holderLocation.getHolder());

            }
        }
        ret.addAll(location.getContents());
        return ModelObjectShortBean.getBeans(ret);
    }

    private static boolean validString(final String s) {
        if (null == s || s.length() == 0) {
            return false;
        }
        return true;
    }

}
