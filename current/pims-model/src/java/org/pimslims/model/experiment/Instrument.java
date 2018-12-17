package org.pimslims.model.experiment;

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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.crystallization.WellImageType;
import org.pimslims.model.location.Location;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.reference.ImageType;
import org.pimslims.model.reference.InstrumentType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "EXPE_INSTRUMENT")
@org.pimslims.annotation.MetaClass(helpText = "Instrument used.", keyNames = {}, subclasses = {})
public class Instrument extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_MODEL = "model";

    public static final String PROP_SERIALNUMBER = "serialNumber";

    public static final String PROP_MANUFACTURER = "manufacturer";

    public static final String PROP_DEFAULTIMAGETYPE = "defaultImageType";

    public static final String PROP_DEFAULTWELLIMAGETYPE = "defaultWellImageType";

    public static final String PROP_METHODS = "methods";

    public static final String PROP_INSTRUMENTTYPES = "instrumentTypes";

    public static final String PROP_LOCATION = "location";

    public static final String PROP_TEMPERATURE = "temperature";

    public static final String PROP_TEMPDISPLAYUNIT = "tempDisplayUnit";

    public static final String PROP_PRESSURE = "pressure";

    public static final String PROP_PRESSUREDISPLAYUNIT = "pressureDisplayUnit";

    private static final String PROP_FILE_PATH = "filePath";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label")
    @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The name of the instrument.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "MODEL", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The model of the instrument.", constraints = { "contains_no_linebreak" })
    private String model;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "SERIALNUMBER", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Instrument 'serial number'. A string that uniquely identifies a given instrument.", constraints = { "contains_no_linebreak" })
    private String serialNumber;

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
    @Basic(optional = true)
    @Column(name = "FILEPATH", length = 1024, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "URL of folder that the output files appear in.", constraints = { "contains_no_linebreak" })
    private String filePath;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "MANUFACTURERID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "expe_instrument_m_inx")
    @org.pimslims.annotation.Role(helpText = "The name of the manufacturer of the instrument.", low = 0, high = 1, isChangeable = true, reverseRoleName = "Unresolved")
    private Organisation manufacturer;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "LOCATIONID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "expe_instrument_location_inx")
    @org.pimslims.annotation.Role(helpText = "The location of this instrument.", low = 0, high = 1, isChangeable = true, reverseRoleName = "Unresolved")
    private Location location;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "DEFAULTIMAGETYPEID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "expe_instrument_dit_inx")
    @org.pimslims.annotation.Role(helpText = "The default image type of the images generated by this instrument.", low = 0, high = 1, isChangeable = true, reverseRoleName = "Unresolved")
    private ImageType defaultImageType;

    /* ------------------------------------------------------------ */
    @Deprecated
    // use ImageType
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "DEFAULTWELLIMAGETYPEID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "expe_instrument_dwit_inx")
    @org.pimslims.annotation.Role(helpText = "The default image type of the images generated by this instrument.", low = 0, high = 1, isChangeable = true, reverseRoleName = "Unresolved")
    private WellImageType defaultWellImageType;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "instrument")
    @org.pimslims.annotation.Role(helpText = "Methods implemented on a given instrument.", low = 0, high = -1, isChangeable = true, reverseRoleName = "instrument")
    private final Set<Method> methods = new HashSet<Method>(0);

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "expe_instrument2insttype", inverseJoinColumns = { @JoinColumn(name = "INSTRUMENTTYPEID", nullable = false, updatable = false) }, joinColumns = { @JoinColumn(name = "INSTRUMENTID", nullable = false, updatable = false) })
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @org.pimslims.annotation.Role(helpText = "List of instrument type associated to an instrument.", low = 0, high = -1, isChangeable = true, reverseRoleName = "Unresolved")
    @org.pimslims.annotation.SubPropertyOf("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
    private final Set<InstrumentType> instrumentTypes = new HashSet<InstrumentType>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Instrument() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Instrument(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Instrument(final WritableVersion wVersion, final java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Instrument(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
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
     * Property: model
     */
    public String getModel() {
        return (String) get_prop(PROP_MODEL);
    }

    public void setModel(final String model) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MODEL, model);
    }

    /**
     * Property: serialNumber
     */
    public String getSerialNumber() {
        return (String) get_prop(PROP_SERIALNUMBER);
    }

    public void setSerialNumber(final String serialNumber) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SERIALNUMBER, serialNumber);
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
     * Instrument.getFilePath
     * 
     * @return
     */
    public String getFilePath() {
        return (String) get_prop(PROP_FILE_PATH);
    }

    /**
     * Instrument.setFilePath
     * 
     * @param url of folder the output files are kept in
     */
    public void setFilePath(String url) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_FILE_PATH, url);
    }

    /**
     * Property: manufacturer
     */
    public Organisation getManufacturer() {
        return (Organisation) get_prop(PROP_MANUFACTURER);
    }

    public void setManufacturer(final Organisation manufacturer)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MANUFACTURER, manufacturer);
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
     * Property: default image type
     */
    public ImageType getDefaultImageType() {
        return (ImageType) get_prop(PROP_DEFAULTIMAGETYPE);
    }

    /**
     * Property: default image type
     */
    @Deprecated
    // use ImageType
    public WellImageType getDefaultWellImageType() {
        return (WellImageType) get_prop(PROP_DEFAULTWELLIMAGETYPE);
    }

    public void setDefaultImageType(final ImageType imageType)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DEFAULTIMAGETYPE, imageType);
    }

    @Deprecated
    // use ImageType
    public void setDefaultWellImageType(final WellImageType imageType)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DEFAULTWELLIMAGETYPE, imageType);
    }

    /**
     * Property: methods
     */
    public Set<Method> getMethods() {
        return (Set<Method>) get_prop(PROP_METHODS);
    }

    public void setMethods(final java.util.Collection<Method> methods)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_METHODS, methods);
    }

    public void addMethod(final Method method) throws org.pimslims.exception.ConstraintException {
        add(PROP_METHODS, method);
    }

    public void removeMethod(final Method method) throws org.pimslims.exception.ConstraintException {
        remove(PROP_METHODS, method);
    }

    /**
     * Property: instrumentTypes
     */
    public Set<InstrumentType> getInstrumentTypes() {
        return (Set<InstrumentType>) get_prop(PROP_INSTRUMENTTYPES);
    }

    public void setInstrumentTypes(final java.util.Collection<InstrumentType> instrumentTypes)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_INSTRUMENTTYPES, instrumentTypes);
    }

    public void addInstrumentType(final org.pimslims.model.reference.InstrumentType instrumentType)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_INSTRUMENTTYPES, instrumentType);
    }

    public void removeInstrumentType(final org.pimslims.model.reference.InstrumentType instrumentType)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_INSTRUMENTTYPES, instrumentType);
    }

}
