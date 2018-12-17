/**
 * V4_3-web org.pimslims.lab ContainerUtility.java
 * 
 * @author cm65
 * @date 4 Oct 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.lab;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Containable;
import org.pimslims.model.holder.Container;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.holder.HolderLocation;
import org.pimslims.model.location.Location;
import org.pimslims.model.sample.Sample;

/**
 * ContainerUtility
 * 
 */

@SuppressWarnings("deprecation")
// necessarily uses deprecated Location
public class ContainerUtility {

    public static Location getCurrentLocation(final Containable object) {
        if (object instanceof Holder) {
            return ((Holder) object).getCurrentLocation();
        } else if (object instanceof Sample) {
            return ContainerUtility.getCurrentLocation((Sample) object);
        } else {
            return ((Location) object).getLocation();
        }
    }

    /**
     * @param sample
     * @return
     */
    @Deprecated
    // in data model, just do sample.getCurrentLocation()
    private static Location getCurrentLocation(final Sample sample) {
        final Holder holder = sample.getHolder();
        if (null == holder) {
            return null;
        }
        return ContainerUtility.getCurrentLocation(holder);
    }

    /**
     * TODO test this method
     * 
     * @param sample the sample
     * @param location
     * @throws ConstraintException
     * @throws ConstraintException
     */
    @Deprecated
    // Location is obsolete
    private static void move(final Sample sample, final Location location) throws ConstraintException {

        final WritableVersion version = (WritableVersion) sample.get_Version();
        if (null == sample.getHolder()) {
            sample.setHolder(new Holder(version, sample.getName()));
        }
        final Holder holder = sample.getHolder();

        try {
            final Calendar now = java.util.Calendar.getInstance();
            final List<HolderLocation> locations = new ArrayList<HolderLocation>(holder.getHolderLocations());
            for (final Iterator<HolderLocation> iter = locations.iterator(); iter.hasNext();) {
                final HolderLocation hl = iter.next();
                if (null == hl.getEndDate()) {
                    hl.setEndDate(now);
                }
            }
            new HolderLocation(version, now, location, holder).setAccess(holder.getAccess());
        } catch (final ConstraintException e) {
            // have set an end date before a start date
            // some existing record must refer to the future
            // this should not happen, so transform to RuntimeException
            throw new RuntimeException(e);
        }

    }

    private static void move(final Containable object, final Location location) throws ConstraintException {

        if (object instanceof org.pimslims.model.sample.Sample) {
            ContainerUtility.move((Sample) object, location);
        } else if (object instanceof org.pimslims.model.holder.Holder) {
            ((Holder) object).move(location);
        } else {
            final Location thisLocation = (Location) object;
            thisLocation.setContainer(location);
        }
    }

    /**
     * ContainerUtility.move
     * 
     * @param sample
     * @param holder
     */
    private static void move(final Sample sample, final Holder holder) throws ConstraintException {
        sample.setHolder(holder);
    }

    /**
     * ContainerUtility.move
     * 
     * @param shelf
     * @param fridge
     */
    private static void move(final Holder shelf, final Holder fridge) throws ConstraintException {
        shelf.setParentHolder(fridge);
    }

    private static void move(final Containable contents, final Holder holder) throws ConstraintException {
        if (contents instanceof Sample) {
            ContainerUtility.move((Sample) contents, holder);
        } else {
            ContainerUtility.move((Holder) contents, holder);

        }
    }

    public static void move(final Containable contents, final Container container) throws ConstraintException {
        if (container instanceof Holder) {
            ContainerUtility.move(contents, (Holder) container);
        } else {
            ContainerUtility.move(contents, (Location) container);

        }
    }

    public static void move(final Containable object, final Container destination, final Integer rowPosition,
        final Integer colPosition, final Integer subPosition) throws ConstraintException {

        //TODO instanceof and cast will become unnecessary with new DM 

        if (object instanceof org.pimslims.model.sample.Sample) {
            final Sample sample = (Sample) object;
            sample.setContainer(destination);
            sample.setColPosition(colPosition);
            sample.setRowPosition(rowPosition);
            sample.setSubPosition(subPosition);
        } else if (object instanceof org.pimslims.model.holder.Holder) {
            final Holder holder = (Holder) object;
            if (destination instanceof Holder) {
                holder.setParentHolder((AbstractHolder) destination);
            } else {
                holder.setContainer(destination);
            }
            holder.setColPosition(colPosition);
            holder.setRowPosition(rowPosition);
            holder.setSubPosition(subPosition);
        } else {
            ((Location) object).setContainer(destination);
        }

    }
}
