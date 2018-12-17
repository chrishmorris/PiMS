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

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.crystallization.Image;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.holder.Holder;
import org.pimslims.search.Paging;
import org.pimslims.search.Paging.Order;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "SCHE_SCHEDULEDTASK", uniqueConstraints = { @UniqueConstraint(columnNames = { "SCHEDULEDTIME",
    "HOLDERID" }) })
@org.pimslims.annotation.MetaClass(helpText = "Scheduled Task,eg: scheduled plate inspection experiment.", keyNames = {
    "SCHEDULEDTIME", "HOLDER" }, subclasses = {}, parent = org.pimslims.model.holder.Holder.class, parentRoleName = "holder")
public class ScheduledTask extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_SCHEDULEDTIME = "scheduledTime";

    public static final String PROP_COMPLETIONTIME = "completionTime";

    public static final String PROP_STATE = "state";

    public static final String PROP_PRIORITY = "priority";

    public static final String PROP_HOLDER = "holder";

    public static final String PROP_INSTRUMENT = "instrument";

    public static final String PROP_SCHEDULEPLANOFFSET = "schedulePlanOffset";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The unique name.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    // note must be "TIMESTAMP" without time zone for Oracle, since it is part of the key
    @Column(name = "SCHEDULEDTIME", columnDefinition = "TIMESTAMP", unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "the point in time that the scheduled task should be enacted.")
    private Calendar scheduledTime;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "COMPLETIONTIME", columnDefinition = "TIMESTAMP WITH TIME ZONE", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "the point in time that the scheduled task is completed.")
    private Calendar completionTime;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "STATE", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "available for use as a state flag.")
    private Integer state;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "Priority", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "available for use to indicate the priority of this scheduled task relative to others.")
    private Integer priority;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "HOLDERID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "sche_scheduledtask_h_inx")
    @org.pimslims.annotation.Role(helpText = "the holder that is being scheduled.", low = 1, high = 1, isChangeable = true, reverseRoleName = "scheduledTasks")
    private Holder holder;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "INSTRUMENTID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "sche_scheduledtask_i_inx")
    @org.pimslims.annotation.Role(helpText = "the Instrument that should be used to enact this scheduled task.", low = 0, high = 1, isChangeable = true, reverseRoleName = "scheduledTasks")
    private Instrument instrument;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "SchedulePlanOffsetID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "sche_scheduledtask_spo_inx")
    @org.pimslims.annotation.Role(helpText = "the point on a SchedulePlan to which this Schedule relates", low = 0, high = 1, isChangeable = true, reverseRoleName = "scheduledTasks")
    private SchedulePlanOffset schedulePlanOffset;

    /* ------------------------------------------------------------  
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "scheduledTask")
    @org.pimslims.annotation.Role(helpText = "Images generated by this task", low = 0, high = -1, isChangeable = true, reverseRoleName = "scheduledTask")
    @Deprecated // performance problem, this collection can be very large
    private final Set<Image> images = new HashSet<Image>(0); */

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected ScheduledTask() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected ScheduledTask(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public ScheduledTask(final WritableVersion wVersion, final java.lang.String name,
        final java.util.Calendar scheduledTime, final org.pimslims.model.holder.Holder holder)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);
        attributes.put(PROP_SCHEDULEDTIME, scheduledTime);
        attributes.put(PROP_HOLDER, holder);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public ScheduledTask(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
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
     * Property: scheduledTime
     */
    public Calendar getScheduledTime() {
        return (Calendar) get_prop(PROP_SCHEDULEDTIME);
    }

    public void setScheduledTime(final Calendar scheduledTime)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SCHEDULEDTIME, scheduledTime);
    }

    /**
     * Property: completionTime
     */
    public Calendar getCompletionTime() {
        return (Calendar) get_prop(PROP_COMPLETIONTIME);
    }

    public void setCompletionTime(final Calendar completionTime)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_COMPLETIONTIME, completionTime);
    }

    /**
     * Property: state
     */
    public Integer getState() {
        return (Integer) get_prop(PROP_STATE);
    }

    public void setState(final Integer state) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_STATE, state);
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
     * Property: holder
     */
    public Holder getHolder() {
        return (Holder) get_prop(PROP_HOLDER);
    }

    public void setHolder(final Holder holder) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_HOLDER, holder);
    }

    /**
     * Property: Instrument
     */
    public Instrument getInstrument() {
        return (Instrument) get_prop(PROP_INSTRUMENT);
    }

    public void setInstrument(final Instrument instrument) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_INSTRUMENT, instrument);
    }

    /**
     * Property: schedulePlanOffset
     */
    public SchedulePlanOffset getSchedulePlanOffset() {
        return (SchedulePlanOffset) get_prop(PROP_SCHEDULEPLANOFFSET);
    }

    public void setSchedulePlanOffset(final SchedulePlanOffset schedulePlanOffset)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SCHEDULEPLANOFFSET, schedulePlanOffset);
    }

    /**
     * Property: images
     */
    @Deprecated
    // performance problem, better to use getImages(int count)
    public Collection<Image> getImages() {
        return this.get_Version().findAll(Image.class, Image.PROP_SCHEDULEDTASK, this);
    }

    /**
     * ScheduledTask.getImages
     * 
     * @param paging the desired start, limit, and order
     * @return
     */
    public Collection<Image> getImages(Paging paging) {
        Map criteria = new HashMap(1);
        criteria.put(Image.PROP_SCHEDULEDTASK, this);
        return this.get_Version().findAll(Image.class, criteria, paging);
    }

    /**
     * ScheduledTask.getImages
     * 
     * @param count number of images wanted
     * @return recent images
     */
    public Collection<Image> getImages(int count) {
        Paging paging = new Paging(0, count);
        paging.addOrderBy(LabBookEntry.PROP_CREATIONDATE, Order.DESC);
        return this.getImages(paging);
    }

    /*
    public void setImages(final java.util.Collection<Image> images)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_IMAGES, images);
    } */

}
