package org.pimslims.model.protocol;

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
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.RefSample;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "PROT_REFINPUTSAMPLE", uniqueConstraints = { @UniqueConstraint(columnNames = { "PROTOCOLID",
    "NAME" }) })
@org.pimslims.annotation.MetaClass(helpText = "An InputSampleDef is used to specify that an Experiment created from the parent Protocol should have an InputSample that accepts Samples belonging to the specified SampleCategory (any if InputSampleDef.sampleCategory is null).", keyNames = {}, subclasses = {}, parent = org.pimslims.model.protocol.Protocol.class, parentRoleName = "protocol")
public class RefInputSample extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_AMOUNT = "amount";

    public static final String PROP_UNIT = "unit";

    public static final String PROP_DISPLAYUNIT = "displayUnit";

    public static final String PROP_PROTOCOL = "protocol";

    public static final String PROP_SAMPLECATEGORY = "sampleCategory";

    public static final String PROP_RECIPE = "recipe";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The name of the InputSampleDef. It is envisioned that this will be something useful to the user during the viewing and editing of Protocols. This should also be copied down to InputSample.name when an InputSample is created from an InputSampleDef.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "AMOUNT", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The default amount of Sample to use for an InputSample created from this InputSampleDef.")
    private Float amount;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "UNIT", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Unit (L, Kg, g/L, s-1, etc.) for InputSampleDef.amount", constraints = { "value_must_be_one_of('kg', 'L', 'number')" })
    private String unit;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "DISPLAYUNIT", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The unit to display for InputSampleDef.amount.", constraints = {
        "contains_no_linebreak", "no_white_space" })
    private String displayUnit;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PROTOCOLID", unique = false, nullable = false, updatable = false, insertable = false)
    @org.hibernate.annotations.Index(name = "prot_ris_protocol_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "refInputSamples")
    private Protocol protocol;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SAMPLECATEGORYID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "prot_ris_samplecategory_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "refInputSamples")
    private SampleCategory sampleCategory;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "RECIPEID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "prot_ris_recipe_inx")
    @org.pimslims.annotation.Role(helpText = "Reagent specification", low = 0, high = 1, isChangeable = true, reverseRoleName = "refInputSamples")
    private RefSample recipe;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected RefInputSample() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected RefInputSample(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public RefInputSample(WritableVersion wVersion,
        org.pimslims.model.reference.SampleCategory sampleCategory,
        org.pimslims.model.protocol.Protocol protocol) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_PROTOCOL, protocol);
        attributes.put(PROP_SAMPLECATEGORY, sampleCategory);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public RefInputSample(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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
        if (this.name == null)
            return "";
        return this.name;
    }

    public void setName(String name) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_NAME, name);
    }

    /**
     * Property: amount
     */
    public Float getAmount() {
        return (Float) get_prop(PROP_AMOUNT);
    }

    public void setAmount(Float amount) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_AMOUNT, amount);
    }

    /**
     * Property: unit
     */
    public String getUnit() {
        return (String) get_prop(PROP_UNIT);
    }

    public void setUnit(String unit) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_UNIT, unit);
    }

    /**
     * Property: displayUnit
     */
    public String getDisplayUnit() {
        return (String) get_prop(PROP_DISPLAYUNIT);
    }

    public void setDisplayUnit(String displayUnit) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DISPLAYUNIT, displayUnit);
    }

    /**
     * Property: protocol
     */
    public Protocol getProtocol() {
        return (Protocol) get_prop(PROP_PROTOCOL);
    }

    public void setProtocol(Protocol protocol) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PROTOCOL, protocol);
    }

    /**
     * Property: sampleCategory
     */
    public SampleCategory getSampleCategory() {
        return (SampleCategory) get_prop(PROP_SAMPLECATEGORY);
    }

    public void setSampleCategory(SampleCategory sampleCategory)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SAMPLECATEGORY, sampleCategory);
    }

    /**
     * Property: sampleCategory
     */
    public RefSample getRecipe() {
        return (RefSample) get_prop(PROP_RECIPE);
    }

    public void setRecipe(RefSample recipe) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_RECIPE, recipe);
    }

}
