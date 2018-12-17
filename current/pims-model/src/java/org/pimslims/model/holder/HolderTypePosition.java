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

import org.pimslims.dao.WritableVersion;
import org.pimslims.model.reference.HolderType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "HOLD_HOLDERTYPEPOSITION")
@org.pimslims.annotation.MetaClass(helpText = "Description of the position in a certain holder type.", keyNames = {}, subclasses = {}, parent = org.pimslims.model.reference.HolderType.class, parentRoleName = "holderType")
public class HolderTypePosition extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_ROWPOSITION = "rowPosition";

    public static final String PROP_COLPOSITION = "colPosition";

    public static final String PROP_SUBPOSITION = "subPosition";

    public static final String PROP_MAXVOLUME = "maxVolume";

    public static final String PROP_MAXVOLUMEDIPLAYUNIT = "maxVolumeDiplayUnit";

    public static final String PROP_HOLDERTYPE = "holderType";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The name of the position in holder could be reservoir, well,...", constraints = {
        "contains_no_linebreak", "no_white_space" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ROWPOSITION", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The row position in the holder type. The first position should be 1.", constraints = { "int_value_gt_0" })
    private Integer rowPosition;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "COLPOSITION", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The column position in the holder type. The first position should be 1.", constraints = { "int_value_gt_0" })
    private Integer colPosition;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "SUBPOSITION", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The sub-position in the holder type. The first position should be 1.", constraints = { "int_value_gt_0" })
    private Integer subPosition;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "MAXVOLUME", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The maximum volume.")
    private Float maxVolume;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "MAXVOLUMEDIPLAYUNIT", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The display unit of the max volume.", constraints = {
        "contains_no_linebreak", "no_white_space" })
    private String maxVolumeDiplayUnit;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "HOLDERTYPEID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "hold_htp_holdertype_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "holderTypePositions")
    private HolderType holderType;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected HolderTypePosition() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected HolderTypePosition(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public HolderTypePosition(WritableVersion wVersion, org.pimslims.model.reference.HolderType holderType)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_HOLDERTYPE, holderType);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public HolderTypePosition(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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

    public void setName(String name) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_NAME, name);
    }

    /**
     * Property: rowPosition
     */
    public Integer getRowPosition() {
        return (Integer) get_prop(PROP_ROWPOSITION);
    }

    public void setRowPosition(Integer rowPosition) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ROWPOSITION, rowPosition);
    }

    /**
     * Property: colPosition
     */
    public Integer getColPosition() {
        return (Integer) get_prop(PROP_COLPOSITION);
    }

    public void setColPosition(Integer colPosition) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_COLPOSITION, colPosition);
    }

    /**
     * Property: subPosition
     */
    public Integer getSubPosition() {
        return (Integer) get_prop(PROP_SUBPOSITION);
    }

    public void setSubPosition(Integer subPosition) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SUBPOSITION, subPosition);
    }

    /**
     * Property: maxVolume
     */
    public Float getMaxVolume() {
        return (Float) get_prop(PROP_MAXVOLUME);
    }

    public void setMaxVolume(Float maxVolume) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MAXVOLUME, maxVolume);
    }

    /**
     * Property: maxVolumeDiplayUnit
     */
    public String getMaxVolumeDiplayUnit() {
        return (String) get_prop(PROP_MAXVOLUMEDIPLAYUNIT);
    }

    public void setMaxVolumeDiplayUnit(String maxVolumeDiplayUnit)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MAXVOLUMEDIPLAYUNIT, maxVolumeDiplayUnit);
    }

    /**
     * Property: holderType
     */
    public HolderType getHolderType() {
        return (HolderType) get_prop(PROP_HOLDERTYPE);
    }

    public void setHolderType(HolderType holderType) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_HOLDERTYPE, holderType);
    }

}
