package org.pimslims.model.location;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.annotation.Attribute;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.holder.Containable;
import org.pimslims.model.holder.Container;
import org.pimslims.model.holder.HolderLocation;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.reference.ContainerType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "LOCA_LOCATION")
@org.pimslims.annotation.MetaClass(orderBy = "name", helpText = "The information on a laboratory location used for storage.", keyNames = {}, subclasses = {})
@Deprecated
// use Holder
public class Location extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable, Container {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_LOCATIONTYPE = "locationType";

    public static final String PROP_TEMPERATURE = "temperature";

    public static final String PROP_TEMPDISPLAYUNIT = "tempDisplayUnit";

    public static final String PROP_PRESSURE = "pressure";

    public static final String PROP_PRESSUREDISPLAYUNIT = "pressureDisplayUnit";

    public static final String PROP_LOCATION = "location";

    public static final String PROP_ORGANISATION = "organisation";

    public static final String PROP_CONTENTS = "contents";

    public static final String PROP_HOLDERLOCATIONS = "holderLocations";

    public static final String PROP_INSTRUMENTS = "instruments";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    private static final ContainerType CONTAINER_TYPE = new ContainerType() {
        public String getName() {
            return "Location";
        }

        public int getDimension() {
            return 0; // no "position in location"
        }

    };

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The name of the location.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "LOCATIONTYPE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The type of the location. e.g. 'Building', 'Room', 'Fridge', 'Cupboard', 'Shelves',...", constraints = { "contains_no_linebreak" })
    private String locationType;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "TEMPERATURE", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The temperature of the location in kelvin (K).")
    private Float temperature;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "TEMPDISPLAYUNIT", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "This is the unit entered by user and used for display.", constraints = {
        "contains_no_linebreak", "no_white_space" })
    private String tempDisplayUnit;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PRESSURE", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The pressure of the location in pascal (Pa).")
    private Float pressure;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PRESSUREDISPLAYUNIT", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "This is the unit entered by user and used for display.", constraints = {
        "contains_no_linebreak", "no_white_space" })
    private String pressureDisplayUnit;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "LOCATIONID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "loca_location_location_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = 1, isChangeable = true, reverseRoleName = "contents")
    private Location location;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "ORGANISATIONID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "loca_location_organisation_inx")
    @org.pimslims.annotation.Role(helpText = "The organisation to which the location belongs.", low = 0, high = 1, isChangeable = true, reverseRoleName = "Unresolved")
    private Organisation organisation;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
    @org.pimslims.annotation.Role(helpText = "List of sub location contained in another location. This is the contents of a Location.", low = 0, high = -1, isChangeable = true, reverseRoleName = "location")
    private final Set<Location> contents = new HashSet<Location>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
    @org.pimslims.annotation.Role(helpText = "List over time of occupancy for a given location.", low = 0, high = -1, isChangeable = true, reverseRoleName = "location")
    private final Set<HolderLocation> holderLocations = new HashSet<HolderLocation>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
    @org.pimslims.annotation.Role(helpText = "instruments in this location.", low = 0, high = -1, isChangeable = true, reverseRoleName = "location")
    private final Set<Instrument> instruments = new HashSet<Instrument>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Location() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Location(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Location(final WritableVersion wVersion, final java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Location(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: name
     */
    @Override
    public String getName() {
        return this.name;
    }

    public void setName(final String name) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_NAME, name);
    }

    /**
     * Property: locationType
     */
    public String getLocationType() {
        return (String) get_prop(PROP_LOCATIONTYPE);
    }

    public void setLocationType(final String locationType) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_LOCATIONTYPE, locationType);
    }

    /**
     * Property: temperature
     */
    public Float getTemperature() {
        return (Float) get_prop(PROP_TEMPERATURE);
    }

    public void setTemperature(final Float temperature) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_TEMPERATURE, temperature);
    }

    /**
     * Property: tempDisplayUnit
     */
    public String getTempDisplayUnit() {
        return (String) get_prop(PROP_TEMPDISPLAYUNIT);
    }

    public void setTempDisplayUnit(final String tempDisplayUnit)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_TEMPDISPLAYUNIT, tempDisplayUnit);
    }

    /**
     * Property: pressure
     */
    public Float getPressure() {
        return (Float) get_prop(PROP_PRESSURE);
    }

    public void setPressure(final Float pressure) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PRESSURE, pressure);
    }

    /**
     * Property: pressureDisplayUnit
     */
    public String getPressureDisplayUnit() {
        return (String) get_prop(PROP_PRESSUREDISPLAYUNIT);
    }

    public void setPressureDisplayUnit(final String pressureDisplayUnit)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PRESSUREDISPLAYUNIT, pressureDisplayUnit);
    }

    /**
     * Property: location
     */
    public Location getLocation() {
        return (Location) get_prop(PROP_LOCATION);
    }

    @Deprecated
    // use setContainer(Container)
    public void setLocation(final Location location) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_LOCATION, location);
    }

    /**
     * Property: organisation
     */
    public Organisation getOrganisation() {
        return (Organisation) get_prop(PROP_ORGANISATION);
    }

    public void setOrganisation(final Organisation organisation)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ORGANISATION, organisation);
    }

    /**
     * Property: contents
     */
    @Deprecated
    // use getContained()
    public Set<Location> getContents() {
        return (Set<Location>) get_prop(PROP_CONTENTS);
    }

    public void setContents(final java.util.Collection<Location> contents)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CONTENTS, contents);
    }

    public void addContent(final Location content) throws org.pimslims.exception.ConstraintException {
        add(PROP_CONTENTS, content);
    }

    public void removeContent(final Location content) throws org.pimslims.exception.ConstraintException {
        remove(PROP_CONTENTS, content);
    }

    /**
     * Property: holderLocations
     */
    public Set<HolderLocation> getHolderLocations() {
        return (Set<HolderLocation>) get_prop(PROP_HOLDERLOCATIONS);
    }

    public void setHolderLocations(final java.util.Collection<HolderLocation> holderLocations)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_HOLDERLOCATIONS, holderLocations);
    }

    public void addHolderLocation(final HolderLocation holderLocation)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_HOLDERLOCATIONS, holderLocation);
    }

    public void removeHolderLocation(final HolderLocation holderLocation)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_HOLDERLOCATIONS, holderLocation);
    }

    /**
     * Property: Instruments
     */
    public Set<Instrument> getInstrument() {
        return (Set<Instrument>) get_prop(PROP_INSTRUMENTS);
    }

    public void setInstruments(final java.util.Collection<Instrument> instruments)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_INSTRUMENTS, instruments);
    }

    public void addInstrument(final Instrument instrument) throws org.pimslims.exception.ConstraintException {
        add(PROP_INSTRUMENTS, instrument);
    }

    public void removeInstrument(final Instrument instrument)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_INSTRUMENTS, instrument);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */
    /**
     * Derived method: getLength()
     */
    @Attribute(helpText = "The temperature with DisplayUnit.")
    public String getTempWithDisplayUnit() {
        if (this.temperature == null) {
            return "";
        }
        if (this.tempDisplayUnit == null) {
            return this.temperature.toString();
        }
        return this.temperature + " " + this.tempDisplayUnit;

    }

    @Attribute(helpText = "The Pressure with DisplayUnit.")
    public String getPressureWithDisplayUnit() {
        if (this.pressure == null) {
            return "";
        }
        if (this.pressureDisplayUnit == null) {
            return this.pressure.toString();
        }
        return this.pressure + " " + this.pressureDisplayUnit;

    }

    /**
     * Container.setContainerType
     * 
     * @see org.pimslims.model.holder.Container#setContainerType(org.pimslims.model.reference.ContainerType)
     */
    public void setContainerType(ContainerType type) {
        assert ("Location".equals(type.getName()));
    }

    /**
     * Container.getContainerType
     * 
     * @see org.pimslims.model.holder.Container#getContainerType()
     */
    public ContainerType getContainerType() {
        return CONTAINER_TYPE;
    }

    /**
     * Containable.getContainer
     * 
     * @see org.pimslims.model.holder.Containable#getContainer()
     */
    public Container getContainer() {
        return this.getLocation();
    }

    /**
     * Containable.setContainer
     * 
     * @see org.pimslims.model.holder.Containable#setContainer(org.pimslims.model.holder.Container)
     */
    public void setContainer(Container container) throws ConstraintException {
        this.setLocation((Location) container);
    }

    /**
     * Container.getContained
     * 
     * @see org.pimslims.model.holder.Container#getContained()
     */
    public Collection<Containable> getContained() {
        Collection ret = new ArrayList();
        ret.addAll(this.getContents());
        Set<HolderLocation> hls = this.getHolderLocations();
        for (Iterator iterator = hls.iterator(); iterator.hasNext();) {
            HolderLocation hl = (HolderLocation) iterator.next();
            ret.add(hl.getHolder());
        }
        return ret;
    }

    /**
     * ContainerType.getDimension
     * 
     * @see org.pimslims.model.reference.ContainerType#getDimension()
     */
    public int getDimension() {
        return 0; // no "position in location"
    }

    /**
     * Containable.setColPosition
     * 
     * @see org.pimslims.model.holder.Containable#setColPosition(java.lang.Integer)
     */
    public void setColPosition(Integer column) {
        // not implemented
    }

    /**
     * Containable.setRowPosition
     * 
     * @see org.pimslims.model.holder.Containable#setRowPosition(java.lang.Integer)
     */
    public void setRowPosition(Integer row) {
        // not implemented
    }

    /**
     * Containable.setSubPosition
     * 
     * @see org.pimslims.model.holder.Containable#setSubPosition(java.lang.Integer)
     */
    public void setSubPosition(Integer subposition) {
        // not implemented
    }

    /**
     * Containable.getColPosition
     * 
     * @see org.pimslims.model.holder.Containable#getColPosition()
     */
    public Integer getColPosition() {
        return null;
    }

    /**
     * Containable.getRowPosition
     * 
     * @see org.pimslims.model.holder.Containable#getRowPosition()
     */
    public Integer getRowPosition() {
        return null;
    }

    /**
     * Containable.getSubPosition
     * 
     * @see org.pimslims.model.holder.Containable#getSubPosition()
     */
    public Integer getSubPosition() {
        return null;
    }

}
