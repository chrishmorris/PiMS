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

import java.util.ArrayList;
import java.util.Calendar;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.crystallization.DropAnnotation;
import org.pimslims.model.location.Location;
import org.pimslims.model.reference.AbstractHolderType;
import org.pimslims.model.reference.ContainerType;
import org.pimslims.model.reference.CrystalType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.schedule.ScheduledTask;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "ABSTRACTHOLDERID")
@Table(name = "HOLD_HOLDER", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(helpText = "The information on the holder. It is a plastic labware that cannot be separated like plates, tubes,... that contain one or many sample(s) and that holds the bar code information for example. It can be also used to group holder together.", keyNames = {}, subclasses = {})
public class Holder extends org.pimslims.model.holder.AbstractHolder implements java.lang.Comparable,
    java.io.Serializable, Container {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_STARTDATE = "startDate";

    public static final String PROP_ENDDATE = "endDate";

    public static final String PROP_HOLDERLOCATIONS = "holderLocations";

    public static final String PROP_SAMPLES = "samples";

    public static final String PROP_REFHOLDEROFFSETS = "refHolderOffsets";

    public static final String PROP_SCHEDULEDTASKS = "scheduledTasks";

    public static final String PROP_CRYSTALNUMBER = "crystalNumber";

    public static final String PROP_FIRSTSAMPLE = "firstSample";

    public static final String PROP_LASTTASK = "lastTask";

    public static final String PROP_SUBSCRIBERS = "subscribers";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 2L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "STARTDATE", columnDefinition = "TIMESTAMPTZ", unique = false, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @org.hibernate.annotations.Index(name = "hold_holder_startdate_inx")
    @org.pimslims.annotation.Attribute(helpText = "N/A")
    private Calendar startDate;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ENDDATE", columnDefinition = "TIMESTAMPTZ", unique = false, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @org.pimslims.annotation.Attribute(helpText = "N/A")
    private Calendar endDate;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CRYSTALNUMBER", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The number of samples marked as crystals.")
    private Integer crystalNumber = 0;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "FIRSTSAMPLEID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "hold_holder_firstsample_inx")
    @org.pimslims.annotation.Role(helpText = "The first sample in this holder.", low = 0, high = 1, isChangeable = true)
    private Sample firstSample;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "LASTTASKID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "hold_holder_lasttask_inx")
    @org.pimslims.annotation.Role(helpText = "The latest task completed on this holder.", low = 0, high = 1, isChangeable = true)
    private ScheduledTask lastTask;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "holder")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "holder")
    private final Set<HolderLocation> holderLocations = new HashSet<HolderLocation>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "holder")
    @org.pimslims.annotation.Role(helpText = "List of sample contained in an holder.", low = 0, high = -1, isChangeable = true, reverseRoleName = "holder")
    private final Set<Sample> samples = new HashSet<Sample>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "holder")
    @org.pimslims.annotation.Role(helpText = "List of ref-holder offsets which links to ref-holder this holder confirmed to. ", low = 0, high = -1, isChangeable = true, reverseRoleName = "holder")
    private final Set<RefHolderOffset> refHolderOffsets = new HashSet<RefHolderOffset>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "holder")
    @org.pimslims.annotation.Role(helpText = "scheduled Tasks of this holder ", low = 0, high = -1, isChangeable = true, reverseRoleName = "holder")
    private final Set<ScheduledTask> scheduledTasks = new HashSet<ScheduledTask>(0);

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "hol_holder2subsc", joinColumns = { @JoinColumn(name = "ABSTRACTHOLDERID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "USERID", nullable = false, updatable = false) })
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @org.pimslims.annotation.Role(helpText = "List of users subscribed to news about the holder.", low = 0, high = -1, isChangeable = true)
    private final Set<User> subscribers = new HashSet<User>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Holder() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Holder(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    @Deprecated
    // use Holder(final WritableVersion wVersion, final java.lang.String name, final HolderType type)
    public Holder(final WritableVersion wVersion, final java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);

        init(attributes);
    }

    /**
     * Constructor for required and recommended arguments
     */
    public Holder(final WritableVersion wVersion, final java.lang.String name, final HolderType type)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);
        attributes.put(AbstractHolder.PROP_HOLDERTYPE, type);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Holder(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: startDate
     */
    public Calendar getStartDate() {
        return (Calendar) get_prop(PROP_STARTDATE);
    }

    public void setStartDate(final Calendar startDate) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_STARTDATE, startDate);
    }

    /**
     * Property: endDate
     */
    public Calendar getEndDate() {
        return (Calendar) get_prop(PROP_ENDDATE);
    }

    public void setEndDate(final Calendar endDate) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ENDDATE, endDate);
    }

    /**
     * Property: crystalNumber
     */
    public Integer getCrystalNumber() {
        return (Integer) get_prop(PROP_CRYSTALNUMBER);
    }

    public void setCrystalNumber(final Integer crystalNumber)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CRYSTALNUMBER, crystalNumber);
    }

    /**
     * Property: first sample
     */
    public Sample getFirstSample() {
        return (Sample) get_prop(PROP_FIRSTSAMPLE);
    }

    public void setLastTask(final Sample firstSample) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_FIRSTSAMPLE, firstSample);
    }

    /**
     * Property: LastTask
     */
    public ScheduledTask getLastTask() {
        return (ScheduledTask) get_prop(PROP_LASTTASK);
    }

    public void setLastTask(final ScheduledTask lastTask) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_LASTTASK, lastTask);
    }

    /**
     * Property: holderLocations
     */
    @Deprecated
    public Set<HolderLocation> getHolderLocations() {
        return (Set<HolderLocation>) get_prop(PROP_HOLDERLOCATIONS);
    }

    @Deprecated
    public void setHolderLocations(final java.util.Collection<HolderLocation> holderLocations)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_HOLDERLOCATIONS, holderLocations);
    }

    @Deprecated
    public void addHolderLocation(final HolderLocation holderLocation)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_HOLDERLOCATIONS, holderLocation);
    }

    @Deprecated
    public void removeHolderLocation(final HolderLocation holderLocation)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_HOLDERLOCATIONS, holderLocation);
    }

    /**
     * Property: samples
     */
    public Set<Sample> getSamples() {
        return (Set<Sample>) get_prop(PROP_SAMPLES);
    }

    public void setSamples(final java.util.Collection<Sample> samples)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SAMPLES, samples);
    }

    public void addSample(final Sample sample) throws org.pimslims.exception.ConstraintException {
        add(PROP_SAMPLES, sample);
    }

    public void removeSample(final Sample sample) throws org.pimslims.exception.ConstraintException {
        remove(PROP_SAMPLES, sample);
    }

    /**
     * Property: refHolderOffsets
     */
    public Set<RefHolderOffset> getRefHolderOffsets() {
        return (Set<RefHolderOffset>) get_prop(PROP_REFHOLDEROFFSETS);
    }

    public void setRefHolderOffsets(final java.util.Collection<RefHolderOffset> refHolderOffsets)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_REFHOLDEROFFSETS, refHolderOffsets);
    }

    public void addRefHolderOffset(final RefHolderOffset refHolderOffset)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_REFHOLDEROFFSETS, refHolderOffset);
    }

    public void removeRefHolderOffset(final RefHolderOffset refHolderOffset)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_REFHOLDEROFFSETS, refHolderOffset);
    }

    /**
     * Property: scheduledTasks
     */
    public Set<ScheduledTask> getScheduledTasks() {
        return (Set<ScheduledTask>) get_prop(PROP_SCHEDULEDTASKS);
    }

    public void setScheduledTasks(final java.util.Collection<ScheduledTask> scheduledTasks)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SCHEDULEDTASKS, scheduledTasks);
    }

    public void addScheduledTask(final ScheduledTask scheduledTask)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_SCHEDULEDTASKS, scheduledTask);
    }

    public void removeScheduledTask(final ScheduledTask scheduledTask)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_SCHEDULEDTASKS, scheduledTask);
    }

    public void updateDerivedData() {
        //calculate the crystalNumber
        crystalNumber = 0;
        for (final Sample sample : this.samples) {
            for (final DropAnnotation dropannotation : sample.getDropAnnotations()) {
                if (dropannotation.getScore() != null
                    && dropannotation.getScore().getName().equalsIgnoreCase("Crystals")) {
                    crystalNumber++;
                    break;
                }
            }
        }
        //calculate the lastTask
        this.lastTask = null;
        for (final ScheduledTask task : this.scheduledTasks) {
            if (task.getCompletionTime() != null) {
                if (this.lastTask == null
                    || task.getCompletionTime().after(this.lastTask.getCompletionTime())) {
                    this.lastTask = task;
                }
            }
        }
        //set the first sample
        // FIXME For xtalPiMS this needs to be more clever - it should be the first non-reservoir drop!
        // Note also that the samples collection is not ordered!
        // So, could check for holder.holderType instanceof CrystalType and special-case
        // Either way, this should really be finding the lowest row/col/drop (or col/row/drop?)
        if (this.samples == null || this.samples.size() == 0) {
            this.firstSample = null;
        }

        // JMD new
        else if (1 == this.samples.size()) {
            this.firstSample = this.samples.iterator().next();
        }

        else {
            // JMD Original
            //this.firstSample = this.samples.iterator().next();

            // JMD new
            AbstractHolderType aht = this.getHolderType();
            Integer res = null;
            if ((null != aht) && (aht instanceof CrystalType)) {
                res = ((CrystalType) aht).getResSubPosition();
            }

            // If samples is ordered we could just do
            Iterator<Sample> iter = this.samples.iterator();
            if (null == res) {
                this.firstSample = iter.next();
            } else {

                // Loop till the first non-res sample
                boolean hit = false;
                while (iter.hasNext()) {
                    Sample s = iter.next();
                    if (!res.equals(s.getSubPosition())) {
                        this.firstSample = s;
                        hit = true;
                        break;
                    }
                }

                // Fallback
                if (!hit) {
                    this.firstSample = this.samples.iterator().next();
                }
            }

        }
    }

    /**
     * Container.setContainerType
     * 
     * @throws ConstraintException
     * 
     * @see org.pimslims.model.holder.Container#setContainerType(org.pimslims.model.reference.ContainerType)
     */
    @Override
    public void setContainerType(ContainerType type) throws ConstraintException {
        this.setHolderType((AbstractHolderType) type);
    }

    /**
     * Container.getContainerType
     * 
     * @see org.pimslims.model.holder.Container#getContainerType()
     */
    @Override
    public ContainerType getContainerType() {
        return this.getHolderType();
    }

    /**
     * Containable.getContainer
     * 
     * @see org.pimslims.model.holder.Containable#getContainer()
     */
    @Override
    public Container getContainer() {
        if (null != this.getParentHolder()) {
            return (Container) this.getParentHolder();
        }
        return this.getCurrentLocation();
    }

    /**
     * Containable.setContainer
     * 
     * @throws ConstraintException
     * @see org.pimslims.model.holder.Containable#setContainer(org.pimslims.model.holder.Container)
     */
    @Override
    public void setContainer(Container container) throws ConstraintException {
        if (container instanceof Holder || null == container) {
            this.setParentHolder((AbstractHolder) container);
            move(null);
            return;
        }
        this.setParentHolder(null);
        move((Location) container);

    }

    @Deprecated
    // use setContainer(Container)
    public void move(final Location location) throws ConstraintException {
        final Calendar now = java.util.Calendar.getInstance();

        // move out of current location
        final Collection<HolderLocation> locations = this.getHolderLocations();
        for (final Iterator iter = locations.iterator(); iter.hasNext();) {
            final HolderLocation hl = (HolderLocation) iter.next();

            if (null == hl.getEndDate()) {
                hl.setEndDate(now);
            }
        }
        if (null != location) {
            // now put it here
            new HolderLocation((WritableVersion) this.get_Version(), now, location, this);
        }
    }

    @Deprecated
    public Location getCurrentLocation() {
        Location ret = null;
        final Collection<HolderLocation> locations = this.getHolderLocations();
        for (final Iterator iter = locations.iterator(); iter.hasNext();) {
            final HolderLocation hl = (HolderLocation) iter.next();
            if (null == hl.getEndDate()) {
                // this could be it
                if (null != ret) {
                    throw new IllegalStateException("This holder is in two places");
                }
                ret = hl.getLocation();
            }
        }
        return ret;
    }

    /**
     * Container.getContained
     * 
     * @see org.pimslims.model.holder.Container#getContained()
     */
    public Collection<Containable> getContained() {
        Collection ret = new ArrayList();
        ret.addAll(this.getSamples());
        ret.addAll(this.getSubHolders());
        return ret;
    }

    /**
     * Container.getDimension
     * 
     * @see org.pimslims.model.holder.Container#getDimension()
     */
    public int getDimension() {
        if (null == this.getContainerType()) {
            return 1;
        }
        return this.getContainerType().getDimension();
    }

    /**
     * Property: samples
     */
    public Set<User> getSubscribers() {
        return (Set<User>) get_prop(PROP_SUBSCRIBERS);
    }

    public void setSubscribers(final java.util.Collection<User> subscribers)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SUBSCRIBERS, subscribers);
    }

    public void addSubscriber(final User subscriber) throws org.pimslims.exception.ConstraintException {
        add(PROP_SUBSCRIBERS, subscriber);
    }

    public void removeSubscriber(final User subscriber) throws org.pimslims.exception.ConstraintException {
        remove(PROP_SUBSCRIBERS, subscriber);
    }

}
