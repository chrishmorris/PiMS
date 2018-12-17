package org.pimslims.model.schedule;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.dao.WritableVersion;
import org.pimslims.model.reference.HolderType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "SCHE_SCHEDULEPLAN", uniqueConstraints = {})
@org.pimslims.annotation.MetaClass(helpText = "A pre-defined plan for scheduling consisting of a name, description and list of offsets from time zero.", keyNames = { "NAME" }, subclasses = {})
public class SchedulePlan extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_HOLDERTYPES = "holderTypes";

    public static final String PROP_SCHEDULEPLANOFFSETS = "schedulePlanOffsets";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The name of the plan.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "defaultSchedulePlan")
    @org.pimslims.annotation.Role(helpText = "the HolderTypes that have this plan as their default plan", low = 0, high = -1, isChangeable = true, reverseRoleName = "defaultSchedulePlan")
    private final Set<HolderType> holderTypes = new HashSet<HolderType>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheduleplanid", nullable = false, insertable = true, updatable = true)
    @org.hibernate.annotations.IndexColumn(name = "order_")
    @org.pimslims.annotation.Role(helpText = "the offsets for this plan", low = 0, high = -1, isChangeable = true, reverseRoleName = "schedulePlan")
    private final List<SchedulePlanOffset> schedulePlanOffsets = new ArrayList<SchedulePlanOffset>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected SchedulePlan() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected SchedulePlan(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public SchedulePlan(WritableVersion wVersion, java.lang.String name,
        java.util.List<org.pimslims.model.schedule.SchedulePlanOffset> schedulePlanOffsets)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);
        attributes.put(PROP_SCHEDULEPLANOFFSETS, schedulePlanOffsets);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public SchedulePlan(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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
     * Property: holderTypes
     */
    public Set<HolderType> getHolderTypes() {
        return (Set<HolderType>) get_prop(PROP_HOLDERTYPES);
    }

    public void setHolderTypes(java.util.Collection<HolderType> holderTypes)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_HOLDERTYPES, holderTypes);
    }

    public void addHolderType(HolderType holderType) throws org.pimslims.exception.ConstraintException {
        add(PROP_HOLDERTYPES, holderType);
    }

    public void removeHolderType(HolderType holderType) throws org.pimslims.exception.ConstraintException {
        remove(PROP_HOLDERTYPES, holderType);
    }

    /**
     * Property: schedulePlanOffsets
     */
    public List<SchedulePlanOffset> getSchedulePlanOffsets() {
        return (List<SchedulePlanOffset>) get_prop(PROP_SCHEDULEPLANOFFSETS);
    }

    public void setSchedulePlanOffsets(List<SchedulePlanOffset> schedulePlanOffsets)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SCHEDULEPLANOFFSETS, schedulePlanOffsets);
    }

}
