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

import javax.persistence.Basic;
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

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "SCHE_SCHEDULEPLANOFFSET", uniqueConstraints = { @UniqueConstraint(columnNames = {
    "OFFSETTIME", "SCHEDULEPLANID" }) })
@org.pimslims.annotation.MetaClass(helpText = "An offset from time zero for a particular SchedulePlan. Typically we create all the Schedule records for a SchedulePlan in one go. Time zero is typically the creation date of the Holder that is being scheduled.", keyNames = {
    "OFFSETTIME", "SCHEDULEPLAN" }, subclasses = {}, parent = org.pimslims.model.schedule.SchedulePlan.class, parentRoleName = "schedulePlan")
public class SchedulePlanOffset extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_OFFSETTIME = "offsetTime";
    public static final String PROP_PRIORITY = "priority";
    public static final String PROP_SCHEDULEPLAN = "schedulePlan";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "OffsetTime", columnDefinition = "int8", unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "the offset in milliseconds from time zero, unique to the SchedulePlan.")
    private Long offsetTime;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "Priority", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "the default priority that should be assigned to Schedules created for this offset.")
    private Integer priority;

    /* ------------------------------------------------------------ */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SchedulePlanID", unique = false, nullable = false, updatable = false, insertable = false)
    @org.hibernate.annotations.Index(name="sche_spo_scheduleplan_inx")
    @org.pimslims.annotation.Role(helpText = "the SchedulePlan to which this offset belongs", low = 1, high = 1, isChangeable = true, reverseRoleName = "schedulePlanOffsets")
    private SchedulePlan schedulePlan;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected SchedulePlanOffset() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected SchedulePlanOffset(final WritableVersion wVersion)
        throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public SchedulePlanOffset(final WritableVersion wVersion, final java.lang.Long offsetTime,
        final org.pimslims.model.schedule.SchedulePlan schedulePlan)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_OFFSETTIME, offsetTime);
        attributes.put(PROP_SCHEDULEPLAN, schedulePlan);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public SchedulePlanOffset(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: offsetTime
     */
    public Long getOffsetTime() {
        return (Long) get_prop(PROP_OFFSETTIME);
    }

    public void setOffsetTime(final Long offsetTime) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_OFFSETTIME, offsetTime);
    }

    /**
     * Property: priority
     */
    public Integer getPriority() {
        return (Integer) get_prop(PROP_PRIORITY);
    }

    public void setPriority(final Integer priority) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PRIORITY, priority);
    }

    /**
     * Property: schedulePlan
     */
    public SchedulePlan getSchedulePlan() {
        return (SchedulePlan) get_prop(PROP_SCHEDULEPLAN);
    }

    public void setSchedulePlan(final SchedulePlan schedulePlan)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SCHEDULEPLAN, schedulePlan);
    }

    /**
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {
        return this.get_Hook();
    }

}
