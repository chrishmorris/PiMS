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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.PostLoad;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.pimslims.dao.WritableVersion;
import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.Note;
import org.pimslims.model.people.Group;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "EXPE_EXPERIMENT", uniqueConstraints = {})
@org.pimslims.annotation.EquivalentClass("http://www.w3.org/ns/prov#Activity")
@org.pimslims.annotation.SubClassOf("http://purl.obolibrary.org/obo/OBI_0000011")
// planned_process
@org.pimslims.annotation.MetaClass(helpText = "The information on the experiment that has been done.", keyNames = { "NAME" }, subclasses = {})
public class Experiment extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_STARTDATE = "startDate";

    public static final String PROP_ENDDATE = "endDate";

    public static final String PROP_STATUS = "status";

    public static final String PROP_INVOICENUMBER = "invoiceNumber";

    public static final String PROP_ISLOCKED = "isLocked";

    public static final String PROP_OPERATOR = "operator";

    // or use PROP_PROJECT
    public static final String PROP_RESEARCHOBJECTIVE = "researchObjective";

    public static final String PROP_PROJECT = "researchObjective";

    public static final String PROP_EXPERIMENTGROUP = "experimentGroup";

    public static final String PROP_EXPERIMENTTYPE = "experimentType";

    public static final String PROP_GROUP = "group";

    public static final String PROP_INSTRUMENT = "instrument";

    public static final String PROP_METHOD = "method";

    public static final String PROP_PROTOCOL = "protocol";

    public static final String PROP_MILESTONES = "milestones";

    public static final String PROP_OUTPUTSAMPLES = "outputSamples";

    public static final String PROP_INPUTSAMPLES = "inputSamples";

    public static final String PROP_PARAMETERS = "parameters";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label")
    @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The name of the Experiment, its unique identifier.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "STARTDATE", columnDefinition = "TIMESTAMPTZ", unique = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/ns/prov#startedAtTime")
    @org.pimslims.annotation.Attribute(helpText = "The start date of the experiment step (e.g. '2003-12-25', '2003-12-25:09:00').")
    private Calendar startDate;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "ENDDATE", columnDefinition = "TIMESTAMPTZ", unique = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @org.pimslims.annotation.Attribute(helpText = "The end date of the experiment step.")
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/ns/prov#endedAtTime")
    private Calendar endDate;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "STATUS", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The status of the experiment. e.g. 'Unknown', 'OK', 'Failed', 'In_process' or 'To_be_run'.", constraints = { "value_must_be_one_of('Unknown', 'OK', 'Failed', 'In_process','To_be_run')" })
    private String status;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "INVOICENUMBER", length = 1024, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The id of the invoice in your accounting system, if you have billed a client for this experiment.", constraints = { "contains_no_linebreak" })
    private String invoiceNumber;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ISLOCKED", columnDefinition = "BOOLEAN", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Is this experiment locked for read-only access after group leader cheked it?")
    private Boolean isLocked = false;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "OPERATORID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "expe_experiment_operator_inx")
    @org.pimslims.annotation.Role(helpText = "The user who run this experiment.", low = 0, high = 1, isChangeable = true)
    private User operator;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "RESEARCHOBJECTIVEID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "expe_experiment_ro_inx")
    @org.pimslims.annotation.Role(helpText = "The research objective on which this experiment is done. It can therefore also be seen as way of logically grouping experiments.", low = 0, high = 1, isChangeable = true, reverseRoleName = "experiments")
    private ResearchObjective researchObjective;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "EXPERIMENTGROUPID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "expe_experiment_eg_inx")
    @org.pimslims.annotation.Role(helpText = "The experiment group is used to associate Experiments of same group type.", low = 0, high = 1, isChangeable = true, reverseRoleName = "experiments")
    private ExperimentGroup experimentGroup;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EXPERIMENTTYPEID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "expe_experiment_et_inx")
    @org.pimslims.annotation.Role(helpText = "The experiment type: e.g. 'Cloning', 'Protein Expression', 'Protein Purification', 'Crystallogenesis', 'NMR', ...", low = 1, high = 1, isChangeable = true, reverseRoleName = "experiments")
    private ExperimentType experimentType;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "GROUPID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "expe_experiment_group_inx")
    @org.pimslims.annotation.Role(helpText = "Group where experiment was carried out.", low = 0, high = 1, isChangeable = true, reverseRoleName = "experiments")
    private Group group;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "INSTRUMENTID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "expe_experiment_instrument_inx")
    @org.pimslims.annotation.Role(helpText = "Instrument (e.g. robot, spectrometer) used for Experiment.", low = 0, high = 1, isChangeable = true, reverseRoleName = "experiments")
    private Instrument instrument;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "METHODID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "expe_experiment_method_inx")
    @org.pimslims.annotation.Role(helpText = "Method used for an experiment.", low = 0, high = 1, isChangeable = true, reverseRoleName = "experiments")
    private Method method;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @org.pimslims.annotation.EquivalentProperty("http://purl.uniprot.org/core/method")
    @org.pimslims.annotation.SubPropertyOf("http://www.w3.org/ns/prov#wasInfluencedBy")
    @JoinColumn(name = "PROTOCOLID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "expe_experiment_protocol_inx")
    @org.pimslims.annotation.Role(helpText = "The protocol used by the experiment.", low = 0, high = 1, isChangeable = true, reverseRoleName = "experiments")
    private Protocol protocol;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "experiment")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "experiment")
    private final Set<Milestone> milestones = new HashSet<Milestone>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "experiment")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "experiment")
    private final Set<OutputSample> outputSamples = new HashSet<OutputSample>(0);

    /* ------------------------------------------------------------ */
    //TODO consider FetchType=EAGER
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "experiment")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "experiment")
    private final Set<Parameter> parameters = new HashSet<Parameter>(0);

    /* ------------------------------------------------------------ */
    @OneToMany
    @JoinColumn(name = "experimentId", nullable = false, insertable = true, updatable = true)
    @org.hibernate.annotations.IndexColumn(name = "order_")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "experiment")
    private final List<InputSample> inputSamples = new ArrayList<InputSample>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Experiment() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Experiment(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Experiment(final WritableVersion wVersion, final java.lang.String name,
        final java.util.Calendar startDate, final java.util.Calendar endDate,
        final org.pimslims.model.reference.ExperimentType experimentType)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);
        attributes.put(PROP_STARTDATE, startDate);
        attributes.put(PROP_ENDDATE, endDate);
        attributes.put(PROP_EXPERIMENTTYPE, experimentType);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Experiment(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
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
     * Property: status
     */
    public String getStatus() {
        return (String) get_prop(PROP_STATUS);
    }

    public void setStatus(final String status) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_STATUS, status);
    }

    /**
     * Property: isLocked
     */
    public Boolean getIsLocked() {
        return (Boolean) get_prop(PROP_ISLOCKED);
    }

    @Transient
    private Boolean wasLocked;

    @PostLoad
    private void saveLocked() {
        this.wasLocked = this.isLocked;
    }

    public void setIsLocked(final Boolean isLocked) throws org.pimslims.exception.ConstraintException {
        if (Boolean.TRUE.equals(isLocked)) {
            // any updater can lock
            super.set_prop(PROP_ISLOCKED, isLocked); // note updates Last Updated Date first
        } else {
            this.set_prop(PROP_ISLOCKED, isLocked); // will check mayUnlock
        }
    }

    /**
     * Experiment.set_prop
     * 
     * @see org.pimslims.metamodel.AbstractModelObject#set_prop(java.lang.String, java.lang.Object) Could
     *      raise this to LabBookEntry
     */
    @Override
    protected void set_prop(String prop_name, Object value) throws ConstraintException {
        if (PROP_ISLOCKED.equals(prop_name) && !Boolean.TRUE.equals(value)) {
            if (this.isLocked == value) {
                return;
            }
            // trying to unlock
            if (!((WritableVersionImpl) get_Version()).getAccessController().mayUnlock(this)) {
                throw new ConstraintException("You do not have permission to unlock this page");
            }
            this.wasLocked = false;
            super.set_prop(PROP_ISLOCKED, false);
            new Note((WritableVersion) this.get_Version(), this).setDetails("Unlocked");
            return;
        }

        if (Boolean.TRUE.equals(this.wasLocked)) {
            // trying to update a locked page
            throw new ConstraintException(this, prop_name, value, "Page is locked");
        }

        if (Experiment.PROP_STATUS.equals(prop_name) && ("OK".equals(value) || "Failed".equals(value))) {
            this.setIsLocked(true);
        }
        super.set_prop(prop_name, value);
    }

    /**
     * Property: operator
     */
    public User getOperator() {
        return (User) get_prop(PROP_OPERATOR);
    }

    public void setOperator(final User operator) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_OPERATOR, operator);
    }

    /**
     * Property: researchObjective now use getProject
     */
    public ResearchObjective getResearchObjective() {
        return (ResearchObjective) get_prop(PROP_RESEARCHOBJECTIVE);
    }

    /**
     * Experiment.getProject
     * 
     * @return the project this experiment is part of
     */
    public org.pimslims.model.experiment.Project getProject() {
        return (Project) get_prop(PROP_PROJECT);
    }

    /**
     * Experiment.setResearchObjective
     * 
     * @param researchObjective
     * @throws org.pimslims.exception.ConstraintException
     */
    public void setResearchObjective(final Project researchObjective)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_RESEARCHOBJECTIVE, researchObjective);
    }

    /**
     * Experiment.setProject
     * 
     * @param researchObjective the project this experiment is part of
     * @throws org.pimslims.exception.ConstraintException
     */
    public void setProject(final org.pimslims.model.experiment.Project researchObjective)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PROJECT, researchObjective);
    }

    /**
     * Property: experimentGroup
     */
    public ExperimentGroup getExperimentGroup() {
        return (ExperimentGroup) get_prop(PROP_EXPERIMENTGROUP);
    }

    public void setExperimentGroup(final ExperimentGroup experimentGroup)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_EXPERIMENTGROUP, experimentGroup);
    }

    /**
     * Property: experimentType
     */
    public ExperimentType getExperimentType() {
        return (ExperimentType) get_prop(PROP_EXPERIMENTTYPE);
    }

    public void setExperimentType(final ExperimentType experimentType)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_EXPERIMENTTYPE, experimentType);
    }

    /**
     * Property: group
     */
    public Group getGroup() {
        return (Group) get_prop(PROP_GROUP);
    }

    public void setGroup(final Group group) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_GROUP, group);
    }

    /**
     * Property: instrument
     */
    public Instrument getInstrument() {
        return (Instrument) get_prop(PROP_INSTRUMENT);
    }

    public void setInstrument(final Instrument instrument) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_INSTRUMENT, instrument);
    }

    /**
     * Property: method
     */
    public Method getMethod() {
        return (Method) get_prop(PROP_METHOD);
    }

    public void setMethod(final Method method) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_METHOD, method);
    }

    /**
     * Property: protocol
     */
    public Protocol getProtocol() {
        return (Protocol) get_prop(PROP_PROTOCOL);
    }

    public void setProtocol(final Protocol protocol) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PROTOCOL, protocol);
    }

    /**
     * Property: milestones
     */
    public Set<Milestone> getMilestones() {
        return (Set<Milestone>) get_prop(PROP_MILESTONES);
    }

    public void setMilestones(final java.util.Collection<Milestone> milestones)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MILESTONES, milestones);
    }

    public void addMilestone(final Milestone milestone) throws org.pimslims.exception.ConstraintException {
        add(PROP_MILESTONES, milestone);
    }

    public void removeMilestone(final Milestone milestone) throws org.pimslims.exception.ConstraintException {
        remove(PROP_MILESTONES, milestone);
    }

    /**
     * Property: outputSamples
     */
    public Set<OutputSample> getOutputSamples() {
        return (Set<OutputSample>) get_prop(PROP_OUTPUTSAMPLES);
    }

    public void setOutputSamples(final java.util.Collection<OutputSample> outputSamples)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_OUTPUTSAMPLES, outputSamples);
    }

    public void addOutputSample(final OutputSample outputSample)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_OUTPUTSAMPLES, outputSample);
    }

    public void removeOutputSample(final OutputSample outputSample)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_OUTPUTSAMPLES, outputSample);
    }

    /**
     * Property: parameters
     */
    public Set<Parameter> getParameters() {
        return (Set<Parameter>) get_prop(PROP_PARAMETERS);
    }

    public void setParameters(final java.util.Collection<Parameter> parameters)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PARAMETERS, parameters);
    }

    public void addParameter(final Parameter parameter) throws org.pimslims.exception.ConstraintException {
        add(PROP_PARAMETERS, parameter);
    }

    public void removeParameter(final Parameter parameter) throws org.pimslims.exception.ConstraintException {
        remove(PROP_PARAMETERS, parameter);
    }

    /**
     * Property: inputSamples
     */
    public List<InputSample> getInputSamples() {
        return (List<InputSample>) get_prop(PROP_INPUTSAMPLES);
    }

    public void setInputSamples(final java.util.List<InputSample> inputSamples)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_INPUTSAMPLES, inputSamples);
    }

    public void addInputSample(final InputSample inputSample)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_INPUTSAMPLES, inputSample);
    }

    public void removeInputSample(final InputSample inputSample)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_INPUTSAMPLES, inputSample);
    }

    /**
     * Experiment.setInvoiceNumber
     * 
     * @param unique
     * @throws ConstraintException
     */
    public void setInvoiceNumber(String invoiceId) throws ConstraintException {
        set_prop(PROP_INVOICENUMBER, invoiceId);
    }

    /**
     * Experiment.getInvoiceNumber
     * 
     * @return
     */
    public String getInvoiceNumber() {
        return (String) get_prop(PROP_INVOICENUMBER);
    }

}
