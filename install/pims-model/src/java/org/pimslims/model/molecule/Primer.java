package org.pimslims.model.molecule;

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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "MOLECULEID")
@Table(name = "MOLE_PRIMER", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(helpText = "This is a subtype of MolComponent. A specific oligonucleotide or primer such as a PCR-primer used to amplify nucleic acid fragments.", keyNames = {}, subclasses = {})
public class Primer extends org.pimslims.model.molecule.Molecule implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_DIRECTION = "direction";
    public static final String PROP_ISUNIVERSAL = "isUniversal";
    public static final String PROP_MELTINGTEMPERATURE = "meltingTemperature";
    public static final String PROP_MELTINGTEMPERATUREGENE = "meltingTemperatureGene";
    public static final String PROP_MELTINGTEMPERATURESELLER = "meltingTemperatureSeller";
    public static final String PROP_GCGENE = "gcGene";
    public static final String PROP_LENGTHONGENE = "lengthOnGene";
    public static final String PROP_OPTICALDENSITY = "opticalDensity";
    public static final String PROP_PARTICULARITY = "particularity";
    public static final String PROP_RESTRICTIONSITE = "restrictionSite";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "DIRECTION", length = 32, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "Which direction? 5' or 3'.", constraints = {
        "contains_no_linebreak", "no_white_space" })
    private String direction;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "ISUNIVERSAL", columnDefinition = "BOOLEAN", unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "Is universal for all polymer molecules?")
    private Boolean isUniversal;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "MELTINGTEMPERATURE", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The melting temperature at which this primer is used.")
    private Float meltingTemperature;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "MELTINGTEMPERATUREGENE", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The melting temperature for part length of the primer common with the gene")
    private Float meltingTemperatureGene;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "MELTINGTEMPERATURESELLER", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Primers melting temperature provided by manufacturer")
    private Float meltingTemperatureSeller;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "GCGENE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "GC contents of the part of the primer which intersects with the gene.")
    private String gcGene;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "LENGTHONGENE", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The length of the primers part which is common with the gene.")
    private Integer lengthOnGene;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "OPTICALDENSITY", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Optical density of the solution.")
    private String opticalDensity;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PARTICULARITY", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "This is usually purity or purification method.")
    private String particularity;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "RESTRICTIONSITE", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The restriction site on the primer")
    private String restrictionSite;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Primer() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Primer(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Primer(WritableVersion wVersion, java.lang.String direction, java.lang.Boolean isUniversal,
        java.lang.String molType, java.lang.String name) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_DIRECTION, direction);
        attributes.put(PROP_ISUNIVERSAL, isUniversal);
        attributes.put(PROP_MOLTYPE, molType);
        attributes.put(PROP_NAME, name);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Primer(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: direction
     */
    public String getDirection() {
        return (String) get_prop(PROP_DIRECTION);
    }

    public void setDirection(String direction) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DIRECTION, direction);
    }

    /**
     * Property: isUniversal
     */
    public Boolean getIsUniversal() {
        return (Boolean) get_prop(PROP_ISUNIVERSAL);
    }

    public void setIsUniversal(Boolean isUniversal) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ISUNIVERSAL, isUniversal);
    }

    /**
     * Property: gcGene
     */
    public String getGcGene() {
        return (String) get_prop(PROP_GCGENE);
    }

    public void setGcGene(String gcGene) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_GCGENE, gcGene);
    }

    /**
     * Property: lengthOnGene
     */
    public Integer getLengthOnGene() {
        return (Integer) get_prop(PROP_LENGTHONGENE);
    }

    public void setLengthOnGene(Integer lengthOnGene) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_LENGTHONGENE, lengthOnGene);
    }

    /**
     * Property: meltingTemperature
     */
    public Float getMeltingTemperature() {
        return (Float) get_prop(PROP_MELTINGTEMPERATURE);
    }

    public void setMeltingTemperature(Float meltingTemperature)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MELTINGTEMPERATURE, meltingTemperature);
    }

    /**
     * Property: meltingTemperatureGene
     */
    public Float getMeltingTemperatureGene() {
        return (Float) get_prop(PROP_MELTINGTEMPERATUREGENE);
    }

    public void setMeltingTemperatureGene(Float meltingTemperatureGene)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MELTINGTEMPERATUREGENE, meltingTemperatureGene);
    }

    /**
     * Property: meltingTemperatureSeller
     */
    public Float getMeltingTemperatureSeller() {
        return (Float) get_prop(PROP_MELTINGTEMPERATURESELLER);
    }

    public void setMeltingTemperatureSeller(Float meltingTemperatureSeller)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MELTINGTEMPERATURESELLER, meltingTemperatureSeller);
    }

    /**
     * Property: opticalDensity
     */
    public String getOpticalDensity() {
        return (String) get_prop(PROP_OPTICALDENSITY);
    }

    public void setOpticalDensity(String opticalDensity) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_OPTICALDENSITY, opticalDensity);
    }

    /**
     * Property: particularity
     */
    public String getParticularity() {
        return (String) get_prop(PROP_PARTICULARITY);
    }

    public void setParticularity(String particularity) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PARTICULARITY, particularity);
    }

    /**
     * Property: restrictionSite
     */
    public String getRestrictionSite() {
        return (String) get_prop(PROP_RESTRICTIONSITE);
    }

    public void setRestrictionSite(String restrictionSite) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_RESTRICTIONSITE, restrictionSite);
    }
}
