package org.pimslims.model.protocol;

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
import java.util.Collection;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.dao.WritableVersion;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.InstrumentType;

@Entity
@org.pimslims.annotation.EquivalentClasses({
    @org.pimslims.annotation.EquivalentClass("http://purl.uniprot.org/core/Method"),
    @org.pimslims.annotation.EquivalentClass("http://www.w3.org/ns/prov#Plan") })
@org.pimslims.annotation.SubClassOf("http://purl.obolibrary.org/obo/OBI_0000260")
// plan 
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "PROT_PROTOCOL", uniqueConstraints = {})
@org.pimslims.annotation.MetaClass(orderBy = "name", helpText = "The information on the protocol. A protocol is a series of procedures that contain steps.", keyNames = { "NAME" }, subclasses = {})
public class Protocol extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_OBJECTIVE = "objective";

    public static final String PROP_METHODDESCRIPTION = "methodDescription";

    public static final String PROP_ISFORUSE = "isForUse";

    public static final String PROP_REMARKS = "remarks";

    public static final String PROP_EXPERIMENTTYPE = "experimentType";

    public static final String PROP_PARAMETERDEFINITIONS = "parameterDefinitions";

    public static final String PROP_REFINPUTSAMPLES = "refInputSamples";

    public static final String PROP_REFOUTPUTSAMPLES = "refOutputSamples";

    public static final String PROP_INSTRUMENTTYPE = "instrumentType";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label")
    @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The name of the protocol, its unique identifier.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "OBJECTIVE", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The objective of the protocol.")
    private String objective;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "METHODDESCRIPTION", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Detailed description of the protocol method as text field.")
    private String methodDescription;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "ISFORUSE", columnDefinition = "BOOLEAN", unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "To prevent incorrect protocols from being used. If experiments have been done using them, then these protocols can't be deleted. It's a way of marking them not to be used.")
    private Boolean isForUse = true;

    /* ------------------------------------------------------------ */
    @org.hibernate.annotations.CollectionOfElements
    @JoinTable(name = "PROT_PROTOCOL_REMARKS", joinColumns = @JoinColumn(name = "PROTOCOLID"))
    @org.hibernate.annotations.IndexColumn(name = "ORDER_", base = 0)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#comment")
    @Column(name = "REMARK")
    @org.pimslims.annotation.Attribute(helpText = "Some remarks on the reason why the protocol has been updated.")
    private final List<String> remarks = new ArrayList<String>(0);

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EXPERIMENTTYPEID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "prot_protocol_et_inx")
    @org.pimslims.annotation.Role(helpText = "The experiment type associated to a protocol.", low = 1, high = 1, isChangeable = true, reverseRoleName = "protocols")
    private ExperimentType experimentType;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "INSTRUMENTTYPEID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "prot_protocol_it_inx")
    @org.pimslims.annotation.Role(helpText = "The type of instrument needed for this protocol.", low = 0, high = 1, isChangeable = true, reverseRoleName = "protocols")
    private InstrumentType instrumentType;

    /* ------------------------------------------------------------ */
    @OneToMany
    @JoinColumn(name = "protocolid", nullable = false, insertable = true, updatable = true)
    @org.hibernate.annotations.IndexColumn(name = "order_")
    @org.pimslims.annotation.Role(helpText = "The list of parameterDefinitions associated to a given protocol.", low = 0, high = -1, isChangeable = true, reverseRoleName = "protocol")
    private final List<ParameterDefinition> parameterDefinitions = new ArrayList<ParameterDefinition>(0);

    /* ------------------------------------------------------------ */
    @OneToMany
    @JoinColumn(name = "PROTOCOLID", nullable = false, insertable = true, updatable = true)
    @org.hibernate.annotations.IndexColumn(name = "order_")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "protocol")
    private final List<RefInputSample> refInputSamples = new ArrayList<RefInputSample>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "protocol")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "protocol")
    private final Set<RefOutputSample> refOutputSamples = new HashSet<RefOutputSample>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Protocol() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Protocol(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Protocol(final WritableVersion wVersion, final java.lang.String name,
        final org.pimslims.model.reference.ExperimentType experimentType)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);
        attributes.put(PROP_EXPERIMENTTYPE, experimentType);
        attributes.put(PROP_PARAMETERDEFINITIONS, parameterDefinitions);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Protocol(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
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
     * Property: objective
     */
    public String getObjective() {
        return (String) get_prop(PROP_OBJECTIVE);
    }

    public void setObjective(final String objective) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_OBJECTIVE, objective);
    }

    /**
     * Property: methodDescription
     */
    public String getMethodDescription() {
        return (String) get_prop(PROP_METHODDESCRIPTION);
    }

    public void setMethodDescription(final String methodDescription)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_METHODDESCRIPTION, methodDescription);
    }

    /**
     * Property: isForUse
     */
    public Boolean getIsForUse() {
        return (Boolean) get_prop(PROP_ISFORUSE);
    }

    public void setIsForUse(final Boolean isForUse) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ISFORUSE, isForUse);
    }

    /**
     * Property: remarks
     */
    public List<String> getRemarks() {
        return (List<String>) get_prop(PROP_REMARKS);
    }

    public void setRemarks(final List<String> remarks) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_REMARKS, remarks);
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
     * Property: parameterDefinitions
     */
    public List<ParameterDefinition> getParameterDefinitions() {
        return (List<ParameterDefinition>) get_prop(PROP_PARAMETERDEFINITIONS);
    }

    public void setParameterDefinitions(final List<ParameterDefinition> parameterDefinitions)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PARAMETERDEFINITIONS, parameterDefinitions);
    }

    /**
     * Property: refInputSamples
     */
    public List<RefInputSample> getRefInputSamples() {
        return (List<RefInputSample>) get_prop(PROP_REFINPUTSAMPLES);
    }

    public void setRefInputSamples(final java.util.List<RefInputSample> refInputSamples)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_REFINPUTSAMPLES, refInputSamples);
    }

    public void addRefInputSample(final RefInputSample refInputSample)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_REFINPUTSAMPLES, refInputSample);
    }

    public void removeRefInputSample(final RefInputSample refInputSample)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_REFINPUTSAMPLES, refInputSample);
    }

    /**
     * Property: refOutputSamples
     */
    public Set<RefOutputSample> getRefOutputSamples() {
        return (Set<RefOutputSample>) get_prop(PROP_REFOUTPUTSAMPLES);
    }

    public void setRefOutputSamples(final java.util.Collection<RefOutputSample> refOutputSamples)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_REFOUTPUTSAMPLES, refOutputSamples);
    }

    public void addRefOutputSample(final RefOutputSample refOutputSample)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_REFOUTPUTSAMPLES, refOutputSample);
    }

    public void removeRefOutputSample(final RefOutputSample refOutputSample)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_REFOUTPUTSAMPLES, refOutputSample);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */
    /**
     * Derived method: getResultParamDefinitions()
     */
    public Set<ParameterDefinition> getResultParamDefinitions() {
        final Set<ParameterDefinition> results = new HashSet<ParameterDefinition>(0);
        for (final ParameterDefinition element : this.getParameterDefinitions()) {
            if (element.getIsResult()) {
                results.add(element);
            }
        }
        return results;
    }

    /**
     * Derived method: getSetupParamDefinitions()
     */
    public Set<ParameterDefinition> getSetupParamDefinitions() {
        final Set<ParameterDefinition> results = new HashSet<ParameterDefinition>(0);
        for (final ParameterDefinition element : this.getParameterDefinitions()) {
            if (!element.getIsResult()) {
                results.add(element);
            }
        }
        return results;
    }

    /**
     * 
     * Protocol.getExperiments
     * 
     * @return
     */
    public Collection<Experiment> getExperiments() {
        return this.get_Version().findAll(Experiment.class, Experiment.PROP_PROTOCOL, this);

    }

    /**
     * Property: instrumentType
     */
    public InstrumentType getInstrumentType() {
        return (InstrumentType) get_prop(PROP_INSTRUMENTTYPE);
    }

    public void setInstrumentType(final InstrumentType instrumentType)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_INSTRUMENTTYPE, instrumentType);
    }

}
