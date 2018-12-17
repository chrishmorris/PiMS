package org.pimslims.model.experiment;

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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "EXPE_EXPERIMENTGROUP")
@org.pimslims.annotation.MetaClass(helpText = "Group of experiments serving a given purpose.", keyNames = {}, subclasses = {})
public class ExperimentGroup extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_STARTDATE = "startDate";

    public static final String PROP_ENDDATE = "endDate";

    public static final String PROP_NAME = "name";

    public static final String PROP_PURPOSE = "purpose";

    public static final String PROP_EXPERIMENTS = "experiments";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "STARTDATE", columnDefinition = "TIMESTAMPTZ", unique = false, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @org.pimslims.annotation.Attribute(helpText = "The start date of the experiment group step (e.g. '2003-12-25', '2003-12-25:09:00').")
    private Calendar startDate;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ENDDATE", columnDefinition = "TIMESTAMPTZ", unique = false, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @org.pimslims.annotation.Attribute(helpText = "The end date of the experiment step.")
    private Calendar endDate;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The name of the group of experiment. e.g. 'cloning', 'expression',...", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "PURPOSE", length = 80, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The purpose of the experiment group.", constraints = { "contains_no_linebreak" })
    private String purpose;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "experimentGroup")
    @org.pimslims.annotation.Role(helpText = "Experiments grouped under the same ExperimentGroup.", low = 0, high = -1, isChangeable = true, reverseRoleName = "experimentGroup")
    private final Set<Experiment> experiments = new HashSet<Experiment>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected ExperimentGroup() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected ExperimentGroup(final WritableVersion wVersion)
        throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public ExperimentGroup(final WritableVersion wVersion, final java.lang.String name,
        final java.lang.String purpose) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);
        attributes.put(PROP_PURPOSE, purpose);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public ExperimentGroup(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
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
     * Property: purpose
     */
    public String getPurpose() {
        return (String) get_prop(PROP_PURPOSE);
    }

    public void setPurpose(final String purpose) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PURPOSE, purpose);
    }

    /**
     * Property: experiments
     */
    public Set<Experiment> getExperiments() {
        return (Set<Experiment>) get_prop(PROP_EXPERIMENTS);
    }

    public void setExperiments(final java.util.Collection<Experiment> experiments)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_EXPERIMENTS, experiments);
    }

    public void addExperiment(final Experiment experiment) throws org.pimslims.exception.ConstraintException {
        add(PROP_EXPERIMENTS, experiment);
    }

    public void removeExperiment(final Experiment experiment)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_EXPERIMENTS, experiment);
    }

}
