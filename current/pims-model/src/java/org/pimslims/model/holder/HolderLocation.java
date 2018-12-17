package org.pimslims.model.holder;

/**
 * Generated 05-Aug-2008 09:22:37 by Hibernate Tools 3.2.0.b9
 * 
 * Protein Information Management System - PiMS project
 * 
 * @see http://www.pims-lims.org
 * @version: 2.0
 * 
 *           Copyright (c) 2007
 * 
 *           This library is free software; you can redistribute it and/or modify it under the terms of the
 *           GNU Lesser General Public License as published by the Free Software Foundation; either version
 *           2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */

import java.util.Calendar;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.pimslims.dao.WritableVersion;
import org.pimslims.model.location.Location;

@SuppressWarnings("deprecation")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "HOLD_HOLDERLOCATION")
@org.pimslims.annotation.MetaClass(helpText = "The location of the holder.", keyNames = {}, subclasses = {}, parent = org.pimslims.model.holder.Holder.class, parentRoleName = "holder")
@Deprecated
// no reason to reference these outside DM project
public class HolderLocation extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_STARTDATE = "startDate";

    public static final String PROP_ENDDATE = "endDate";

    public static final String PROP_HOLDER = "holder";

    public static final String PROP_LOCATION = "location";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "STARTDATE", columnDefinition = "TIMESTAMPTZ", unique = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @org.pimslims.annotation.Attribute(helpText = "The start date is the date when the holder has been put in this particular location.")
    private Calendar startDate;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ENDDATE", columnDefinition = "TIMESTAMPTZ", unique = false, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @org.pimslims.annotation.Attribute(helpText = "The end date is the date when the holder has been removed from this particular location.")
    private Calendar endDate;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "HOLDERID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "hold_holderlocation_holder_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "holderLocations")
    private Holder holder;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "LOCATIONID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "hold_holderlocation_l_inx")
    @org.pimslims.annotation.Role(helpText = "The location associated.", low = 1, high = 1, isChangeable = true, reverseRoleName = "holderLocations")
    private Location location;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected HolderLocation() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected HolderLocation(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public HolderLocation(WritableVersion wVersion, java.util.Calendar startDate,
        org.pimslims.model.location.Location location, org.pimslims.model.holder.Holder holder)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_STARTDATE, startDate);
        attributes.put(PROP_HOLDER, holder);
        attributes.put(PROP_LOCATION, location);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public HolderLocation(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: startDate
     */
    public Calendar getStartDate() {
        return (Calendar) get_prop(PROP_STARTDATE);
    }

    public void setStartDate(Calendar startDate) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_STARTDATE, startDate);
    }

    /**
     * Property: endDate
     */
    public Calendar getEndDate() {
        return (Calendar) get_prop(PROP_ENDDATE);
    }

    public void setEndDate(Calendar endDate) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ENDDATE, endDate);
    }

    /**
     * Property: holder
     */
    public Holder getHolder() {
        return (Holder) get_prop(PROP_HOLDER);
    }

    public void setHolder(Holder holder) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_HOLDER, holder);
    }

    /**
     * Property: location
     */
    public Location getLocation() {
        return (Location) get_prop(PROP_LOCATION);
    }

    public void setLocation(Location location) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_LOCATION, location);
    }

    /**
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {

        return this.getClass().getSimpleName() + ":" + holder.getName() + " - " + location.getName();
    }

}
