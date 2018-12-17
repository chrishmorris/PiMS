package org.pimslims.model.target;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.pimslims.dao.WritableVersion;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.TargetStatus;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "TARG_MILESTONE")
@org.pimslims.annotation.MetaClass(helpText = "The milestone status of a target.", keyNames = {}, subclasses = {}, parent = org.pimslims.model.target.Target.class, parentRoleName = "target")
public class Milestone extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_DATE = "date";

    public static final String PROP_STATUS = "status";

    public static final String PROP_EXPERIMENT = "experiment";

    public static final String PROP_TARGET = "target";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "DATE_", columnDefinition = "TIMESTAMPTZ", unique = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @org.pimslims.annotation.Attribute(helpText = "The date when the status is assigned.")
    private Calendar date;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "STATUSID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "targ_milestone_status_inx")
    @org.pimslims.annotation.Role(helpText = "The code of the status.", low = 1, high = 1, isChangeable = false, reverseRoleName = "targetStatuss")
    private TargetStatus status;

    /* ------------------------------------------------------------ */
    // we would like to make this required, but there's no way to upgrade old databases
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "EXPERIMENTID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "targ_milestone_experiment_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = 1, isChangeable = true, reverseRoleName = "milestones")
    private Experiment experiment;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "TARGETID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "targ_milestone_target_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = 1, isChangeable = true, reverseRoleName = "milestones")
    private Target target;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Milestone() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Milestone(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Milestone(WritableVersion wVersion, java.util.Calendar date,
        org.pimslims.model.reference.TargetStatus status, org.pimslims.model.experiment.Experiment experiment)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_DATE, date);
        attributes.put(PROP_STATUS, status);
        attributes.put(PROP_EXPERIMENT, experiment);
        assert null != experiment;
        init(attributes);
    }

    /**
     * Constructor for required arguments
     */
    @Deprecated
    public Milestone(WritableVersion wVersion, java.util.Calendar date,
        org.pimslims.model.reference.TargetStatus status, org.pimslims.model.target.Target target)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_DATE, date);
        attributes.put(PROP_STATUS, status);
        attributes.put(PROP_TARGET, target);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Milestone(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: date
     */
    public Calendar getDate() {
        return (Calendar) get_prop(PROP_DATE);
    }

    public void setDate(Calendar date) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DATE, date);
    }

    /**
     * Property: status
     */
    public TargetStatus getStatus() {
        return (TargetStatus) get_prop(PROP_STATUS);
    }

    public void setStatus(TargetStatus status) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_STATUS, status);
    }

    /**
     * Property: experiment
     */
    public Experiment getExperiment() {
        return (Experiment) get_prop(PROP_EXPERIMENT);
    }

    public void setExperiment(Experiment experiment) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_EXPERIMENT, experiment);
    }

    /**
     * Property: target
     */
    @Deprecated
    // use getProject
    public Target getTarget() {
        return (Target) get_prop(PROP_TARGET);
    }

    @Deprecated
    // use setExperiment
    public void setTarget(Target target) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_TARGET, target);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */
    /**
     * Derived method: getResearchObjective()
     */
    // use getProject
    public ResearchObjective getResearchObjective() {
        if (null != this.getExperiment()) {
            return this.getExperiment().getResearchObjective();
        }
        return null;

    }

    public org.pimslims.model.experiment.Project getProject() {
        if (null != this.getExperiment()) {
            return this.getExperiment().getProject();
        }
        return null;

    }

    /**
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {
        if (null != this.target) {
            return this.target.getName() + "-" + this.status.getName();
        }
        return (null == this.experiment ? "" : this.experiment.getName()) + "-"
            + (null == this.status ? "" : this.status.getName());
    }

}
