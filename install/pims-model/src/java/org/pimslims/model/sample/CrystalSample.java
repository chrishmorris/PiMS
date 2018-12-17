package org.pimslims.model.sample;
/**
 * Generated 05-Aug-2008 09:22:37 by Hibernate Tools 3.2.0.b9
 *
 * Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 2.0
 *
 * Copyright (c) 2007
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * A copy of the license is in dist/docs/LGPL.txt.
 * It is also available here:
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
@PrimaryKeyJoinColumn(name="SAMPLEID")
@Table(name="SAM_CRYSTALSAMPLE", uniqueConstraints={@UniqueConstraint(columnNames={})})
@org.pimslims.annotation.MetaClass(helpText = "The CrystalSample contains information about an individual Crystal and its  physical properties : dimensions, color,...", keyNames = {}, subclasses = {})

@Deprecated
public class CrystalSample extends org.pimslims.model.sample.Sample implements java.lang.Comparable,java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_SPACEGROUP = "spaceGroup";
    public static final String PROP_MORPHOLOGY = "morphology";
    public static final String PROP_CRYSTALTYPE = "crystalType";
    public static final String PROP_COLOUR = "colour";
    public static final String PROP_X = "x";
    public static final String PROP_Y = "y";
    public static final String PROP_Z = "z";
    public static final String PROP_A = "a";
    public static final String PROP_B = "b";
    public static final String PROP_C = "c";
    public static final String PROP_ALPHA = "alpha";
    public static final String PROP_BETA = "beta";
    public static final String PROP_GAMMA = "gamma";


    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;
    /* ------------------------------------------------------------ */
    @Basic(optional=true)
    @Column(name="SPACEGROUP", length=32, unique=false, nullable=true) 
    @org.pimslims.annotation.Attribute(helpText = "The spacegroup name.", constraints = { "contains_no_linebreak", "no_white_space" })
    private String spaceGroup;

    /* ------------------------------------------------------------ */
    @Basic(optional=true)
    @Column(name="MORPHOLOGY", length=254, unique=false, nullable=true) 
    @org.pimslims.annotation.Attribute(helpText = "Description of the shape of the crystal.")
    private String morphology;

    /* ------------------------------------------------------------ */
    @Basic(optional=true)
    @Column(name="CRYSTALTYPE", length=254, unique=false, nullable=true) 
    @org.pimslims.annotation.Attribute(helpText = "The type of the crystal.")
    private String crystalType;

    /* ------------------------------------------------------------ */
    @Basic(optional=true)
    @Column(name="COLOUR", length=254, unique=false, nullable=true) 
    @org.pimslims.annotation.Attribute(helpText = "Description of the colour of the crystal.")
    private String colour;

    /* ------------------------------------------------------------ */
    @Basic(optional=true)
    @Column(name="X", columnDefinition="FLOAT8", unique=false, nullable=true) 
    @org.pimslims.annotation.Attribute(helpText = "Length of x dimension of crystal in millimeter (mm).")
    private Float x;

    /* ------------------------------------------------------------ */
    @Basic(optional=true)
    @Column(name="Y", columnDefinition="FLOAT8", unique=false, nullable=true) 
    @org.pimslims.annotation.Attribute(helpText = "Length of y dimension of crystal in millimeter (mm).")
    private Float y;

    /* ------------------------------------------------------------ */
    @Basic(optional=true)
    @Column(name="Z", columnDefinition="FLOAT8", unique=false, nullable=true) 
    @org.pimslims.annotation.Attribute(helpText = "Length of z dimension of crystal in millimeter (mm).")
    private Float z;

    /* ------------------------------------------------------------ */
    @Basic(optional=true)
    @Column(name="A", columnDefinition="FLOAT8", unique=false, nullable=true) 
    @org.pimslims.annotation.Attribute(helpText = "Length of Cell axis a in angstrom.")
    private Float a;

    /* ------------------------------------------------------------ */
    @Basic(optional=true)
    @Column(name="B", columnDefinition="FLOAT8", unique=false, nullable=true) 
    @org.pimslims.annotation.Attribute(helpText = "Length of Cell axis b in angstrom.")
    private Float b;

    /* ------------------------------------------------------------ */
    @Basic(optional=true)
    @Column(name="C", columnDefinition="FLOAT8", unique=false, nullable=true) 
    @org.pimslims.annotation.Attribute(helpText = "Length of Cell axis c in angstrom.")
    private Float c;

    /* ------------------------------------------------------------ */
    @Basic(optional=true)
    @Column(name="ALPHA", columnDefinition="FLOAT8", unique=false, nullable=true) 
    @org.pimslims.annotation.Attribute(helpText = "Angle between Cell axis b and c in degrees.")
    private Float alpha;

    /* ------------------------------------------------------------ */
    @Basic(optional=true)
    @Column(name="BETA", columnDefinition="FLOAT8", unique=false, nullable=true) 
    @org.pimslims.annotation.Attribute(helpText = "Angle between Cell axis a and c in degrees.")
    private Float beta;

    /* ------------------------------------------------------------ */
    @Basic(optional=true)
    @Column(name="GAMMA", columnDefinition="FLOAT8", unique=false, nullable=true) 
    @org.pimslims.annotation.Attribute(helpText = "Angle between Cell axis a and b in degrees.")
    private Float gamma;


    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected CrystalSample() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected CrystalSample(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments 

     */
    public CrystalSample(WritableVersion wVersion, java.lang.String name) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);
        
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public CrystalSample(WritableVersion wVersion, java.util.Map<String, Object> attributes) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /** 
     * Property: spaceGroup
     */
    public String getSpaceGroup() {
        return (String)get_prop(PROP_SPACEGROUP);
    }

    public void setSpaceGroup(String spaceGroup) throws org.pimslims.exception.ConstraintException {
	    set_prop(PROP_SPACEGROUP, spaceGroup);
    }
    
    /** 
     * Property: morphology
     */
    public String getMorphology() {
        return (String)get_prop(PROP_MORPHOLOGY);
    }

    public void setMorphology(String morphology) throws org.pimslims.exception.ConstraintException {
	    set_prop(PROP_MORPHOLOGY, morphology);
    }
    
    /** 
     * Property: crystalType
     */
    public String getCrystalType() {
        return (String)get_prop(PROP_CRYSTALTYPE);
    }

    public void setCrystalType(String crystalType) throws org.pimslims.exception.ConstraintException {
	    set_prop(PROP_CRYSTALTYPE, crystalType);
    }
    
    /** 
     * Property: colour
     */
    public String getColour() {
        return (String)get_prop(PROP_COLOUR);
    }

    public void setColour(String colour) throws org.pimslims.exception.ConstraintException {
	    set_prop(PROP_COLOUR, colour);
    }
    
    /** 
     * Property: x
     */
    public Float getX() {
        return (Float)get_prop(PROP_X);
    }

    public void setX(Float x) throws org.pimslims.exception.ConstraintException {
	    set_prop(PROP_X, x);
    }
    
    /** 
     * Property: y
     */
    public Float getY() {
        return (Float)get_prop(PROP_Y);
    }

    public void setY(Float y) throws org.pimslims.exception.ConstraintException {
	    set_prop(PROP_Y, y);
    }
    
    /** 
     * Property: z
     */
    public Float getZ() {
        return (Float)get_prop(PROP_Z);
    }

    public void setZ(Float z) throws org.pimslims.exception.ConstraintException {
	    set_prop(PROP_Z, z);
    }
    
    /** 
     * Property: a
     */
    public Float getA() {
        return (Float)get_prop(PROP_A);
    }

    public void setA(Float a) throws org.pimslims.exception.ConstraintException {
	    set_prop(PROP_A, a);
    }
    
    /** 
     * Property: b
     */
    public Float getB() {
        return (Float)get_prop(PROP_B);
    }

    public void setB(Float b) throws org.pimslims.exception.ConstraintException {
	    set_prop(PROP_B, b);
    }
    
    /** 
     * Property: c
     */
    public Float getC() {
        return (Float)get_prop(PROP_C);
    }

    public void setC(Float c) throws org.pimslims.exception.ConstraintException {
	    set_prop(PROP_C, c);
    }
    
    /** 
     * Property: alpha
     */
    public Float getAlpha() {
        return (Float)get_prop(PROP_ALPHA);
    }

    public void setAlpha(Float alpha) throws org.pimslims.exception.ConstraintException {
	    set_prop(PROP_ALPHA, alpha);
    }
    
    /** 
     * Property: beta
     */
    public Float getBeta() {
        return (Float)get_prop(PROP_BETA);
    }

    public void setBeta(Float beta) throws org.pimslims.exception.ConstraintException {
	    set_prop(PROP_BETA, beta);
    }
    
    /** 
     * Property: gamma
     */
    public Float getGamma() {
        return (Float)get_prop(PROP_GAMMA);
    }

    public void setGamma(Float gamma) throws org.pimslims.exception.ConstraintException {
	    set_prop(PROP_GAMMA, gamma);
    }
    

}


