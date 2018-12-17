package org.pimslims.model.reference;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;
import org.pimslims.model.holder.HolderTypePosition;
import org.pimslims.model.schedule.SchedulePlan;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "ABSTRACTHOLDERTYPEID")
@Table(name = "REF_HOLDERTYPE", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(orderBy = "name", helpText = "The information to describe a type of holder. A tube uses maxVolume, plate with one sample/well uses HolderTypePosition with no position set rather than A1.", keyNames = {}, subclasses = { org.pimslims.model.reference.CrystalType.class })
public class HolderType extends org.pimslims.model.reference.AbstractHolderType implements
    java.lang.Comparable, java.io.Serializable, ContainerType {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_MAXROW = "maxRow";

    public static final String PROP_MAXCOLUMN = "maxColumn";

    public static final String PROP_MAXSUBPOSITION = "maxSubPosition";

    public static final String PROP_MAXVOLUME = "maxVolume";

    public static final String PROP_MAXVOLUMEDISPLAYUNIT = "maxVolumeDisplayUnit";

    public static final String PROP_HOLDHOLDERFLAG = "holdHolderFlag";

    public static final String PROP_HOLDERTYPEPOSITIONS = "holderTypePositions";

    public static final String PROP_DEFAULTSCHEDULEPLAN = "defaultSchedulePlan";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "MAXROW", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The maximum number of rows.")
    private Integer maxRow;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "MAXCOLUMN", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The maximum number of columns.")
    private Integer maxColumn;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "MAXSUBPOSITION", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The maximum number of sub-positions.")
    private Integer maxSubPosition;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "MAXVOLUME", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The maximum volume.")
    private Float maxVolume;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "MAXVOLUMEDISPLAYUNIT", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The display unit of the max volume.", constraints = {
        "contains_no_linebreak", "no_white_space" })
    private String maxVolumeDisplayUnit;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "HOLDHOLDERFLAG", columnDefinition = "BOOLEAN", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Is this holder can hold another holder?")
    private Boolean holdHolderFlag;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "holderType")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "holderType")
    private Set<HolderTypePosition> holderTypePositions = new HashSet<HolderTypePosition>(0);

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "DefaultSchedulePlanID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "ref_holdertype_dsp_inx")
    @org.pimslims.annotation.Role(helpText = "the default SchedulePlan of this holderType", low = 0, high = 1, isChangeable = true, reverseRoleName = "holderTypes")
    private SchedulePlan defaultSchedulePlan;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected HolderType() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected HolderType(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public HolderType(WritableVersion wVersion, java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put(PROP_NAME, name);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public HolderType(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: maxRow
     */
    public Integer getMaxRow() {
        return (Integer) get_prop(PROP_MAXROW);
    }

    public void setMaxRow(Integer maxRow) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MAXROW, maxRow);
    }

    /**
     * Property: maxColumn
     */
    public Integer getMaxColumn() {
        return (Integer) get_prop(PROP_MAXCOLUMN);
    }

    public void setMaxColumn(Integer maxColumn) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MAXCOLUMN, maxColumn);
    }

    /**
     * Property: maxSubPosition
     */
    public Integer getMaxSubPosition() {
        return (Integer) get_prop(PROP_MAXSUBPOSITION);
    }

    public void setMaxSubPosition(Integer maxSubPosition) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MAXSUBPOSITION, maxSubPosition);
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
     * Property: maxVolumeDisplayUnit
     */
    public String getMaxVolumeDisplayUnit() {
        return (String) get_prop(PROP_MAXVOLUMEDISPLAYUNIT);
    }

    public void setMaxVolumeDisplayUnit(String maxVolumeDisplayUnit)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MAXVOLUMEDISPLAYUNIT, maxVolumeDisplayUnit);
    }

    /**
     * Property: holdHolderFlag
     */
    public Boolean getHoldHolderFlag() {
        return (Boolean) get_prop(PROP_HOLDHOLDERFLAG);
    }

    public void setHoldHolderFlag(Boolean holdHolderFlag) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_HOLDHOLDERFLAG, holdHolderFlag);
    }

    /**
     * Property: holderTypePositions
     */
    public Set<HolderTypePosition> getHolderTypePositions() {
        return (Set<HolderTypePosition>) get_prop(PROP_HOLDERTYPEPOSITIONS);
    }

    public void setHolderTypePositions(java.util.Collection<HolderTypePosition> holderTypePositions)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_HOLDERTYPEPOSITIONS, holderTypePositions);
    }

    public void addHolderTypePosition(HolderTypePosition holderTypePosition)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_HOLDERTYPEPOSITIONS, holderTypePosition);
    }

    public void removeHolderTypePosition(HolderTypePosition holderTypePosition)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_HOLDERTYPEPOSITIONS, holderTypePosition);
    }

    /**
     * Property: defaultSchedulePlan
     */
    public SchedulePlan getDefaultSchedulePlan() {
        return (SchedulePlan) get_prop(PROP_DEFAULTSCHEDULEPLAN);
    }

    public void setDefaultSchedulePlan(SchedulePlan defaultSchedulePlan)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DEFAULTSCHEDULEPLAN, defaultSchedulePlan);
    }

    /**
     * ContainerType.getDimension
     * 
     * @see org.pimslims.model.reference.ContainerType#getDimension()
     */
    public int getDimension() {
        if (null != this.getMaxSubPosition() && this.getMaxSubPosition() > 1) {
            return 3;
        }
        if (null != this.getMaxColumn() && this.getMaxColumn() > 1) {
            return 2;
        }
        if (null != this.getMaxRow() && this.getMaxRow() > 1) {
            return 1;
        }
        return 0; // e.g. a loop
    }

}
