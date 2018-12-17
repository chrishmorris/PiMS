/**
 * pims-model org.pimslims.upgrader LocationRetirer.java
 * 
 * @author cm65
 * @date 30 Jan 2012
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.upgrader;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.holder.HolderLocation;
import org.pimslims.model.location.Location;
import org.pimslims.model.reference.HolderType;

/**
 * LocationRetirer
 * 
 * Moves all locations to Holder
 * 
 */
public class LocationRetirer extends ClassRetirer {

    private HolderType holderType;

    /**
     * Constructor for LocationRetirer
     * 
     * @param version
     * @param metaClass
     * @throws ConstraintException
     */
    public LocationRetirer(WritableVersion version) throws ConstraintException {
        super(version, version.getMetaClass(Location.class));
        this.holderType = new HolderType(version, "Location");
    }

    @SuppressWarnings("deprecation")
    @Override
    public Holder retire(ModelObject old) throws ConstraintException, AccessException {
        Location location = (Location) old;
        if (null != location.getLocation()) {
            return null; // retire this when we visit the  containing location
        }
        Holder ret = doRetire(location);
        //TODO  now retire any contained locations
        Collection<Location> contents = location.getContents();
        location.setContents(java.util.Collections.EMPTY_SET); // break any cycle
        for (Iterator iterator = contents.iterator(); iterator.hasNext();) {
            Location contained = (Location) iterator.next();
            Holder holder = this.retire(contained);
            holder.setParentHolder(ret);
        }
        return ret;
    }

    private Holder doRetire(Location location) throws ConstraintException, AccessException {

        String name = location.getName();
        if (null != this.version.findFirst(Holder.class, AbstractHolder.PROP_NAME, name)) {
            name = this.version.getUniqueName(Holder.class, name);
        }
        Holder holder = new Holder(version, name, this.holderType);

        copyPageAttributes(location, holder);

        if (!"".equals(location.getTempWithDisplayUnit())) {
            holder.setDetails(holder.getDetails() + " Temperature: " + location.getTempWithDisplayUnit());
        }
        if (!"".equals(location.getPressureWithDisplayUnit())) {
            holder.setDetails(holder.getDetails() + " Pressure: " + location.getPressureWithDisplayUnit());
        }
        this.appendAttribute(location, Location.PROP_LOCATIONTYPE, holder, LabBookEntry.PROP_DETAILS);

        Set<HolderLocation> holderLocations = location.getHolderLocations();
        for (Iterator iterator = holderLocations.iterator(); iterator.hasNext();) {
            HolderLocation hl = (HolderLocation) iterator.next();
            if (null == hl.getEndDate()) {
                // there's a holder in this location
                Holder contained = hl.getHolder();
                if (null == contained.getParentHolder()) {
                    contained.setParentHolder(holder);
                }
            }
        }
        return holder;
    }

}
