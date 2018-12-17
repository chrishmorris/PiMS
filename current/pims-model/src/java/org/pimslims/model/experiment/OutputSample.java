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

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.dao.WritableVersion;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.sample.Sample;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "EXPE_OUTPUTSAMPLE")
@org.pimslims.annotation.MetaClass(helpText = "OutputSamples attach a Sample to an Experiment as the product of that Experiment.", keyNames = {}, subclasses = {}, parent = org.pimslims.model.experiment.Experiment.class, parentRoleName = "experiment")
public class OutputSample extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_AMOUNT = "amount";

    public static final String PROP_AMOUNTUNIT = "amountUnit";

    public static final String PROP_AMOUNTDISPLAYUNIT = "amountDisplayUnit";

    public static final String PROP_ROLE = "role";

    public static final String PROP_NAME = "name";

    public static final String PROP_EXPERIMENT = "experiment";

    public static final String PROP_REFOUTPUTSAMPLE = "refOutputSample";

    public static final String PROP_SAMPLE = "sample";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "AMOUNT", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The amount of the sample used by the experiment.")
    private Float amount;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "AMOUNTUNIT", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "One of the three possible units: kg, m3 or number.", constraints = { "value_must_be_one_of('kg', 'L', 'number')" })
    private String amountUnit;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "AMOUNTDISPLAYUNIT", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "This is the unit entered by user and used for display.", constraints = {
        "contains_no_linebreak", "no_white_space" })
    private String amountDisplayUnit;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ROLE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The role of the sample that it takes during the experiment or simply its classification number.", constraints = { "contains_no_linebreak" })
    private String role;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The name to display for this InputSample.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EXPERIMENTID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "expe_outputsample_e_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "outputSamples")
    private Experiment experiment;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "REFOUTPUTSAMPLEID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "expe_outputsample_ros_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = 1, isChangeable = true, reverseRoleName = "outputSamples")
    private RefOutputSample refOutputSample;

    /* ------------------------------------------------------------ */
    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "SAMPLEID", unique = true, nullable = true)
    @org.pimslims.annotation.Role(helpText = "Sample involved as an output sample of an experiment.", low = 0, high = 1, isChangeable = true, reverseRoleName = "outputSample")
    private Sample sample;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected OutputSample() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected OutputSample(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public OutputSample(WritableVersion wVersion, org.pimslims.model.experiment.Experiment experiment)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_EXPERIMENT, experiment);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public OutputSample(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: amount
     */
    public Float getAmount() {
        return (Float) get_prop(PROP_AMOUNT);
    }

    public void setAmount(Float amount) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_AMOUNT, amount);
    }

    /**
     * Property: amountUnit
     */
    public String getAmountUnit() {
        return (String) get_prop(PROP_AMOUNTUNIT);
    }

    public void setAmountUnit(String amountUnit) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_AMOUNTUNIT, amountUnit);
    }

    /**
     * Property: amountDisplayUnit
     */
    public String getAmountDisplayUnit() {
        return (String) get_prop(PROP_AMOUNTDISPLAYUNIT);
    }

    public void setAmountDisplayUnit(String amountDisplayUnit)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_AMOUNTDISPLAYUNIT, amountDisplayUnit);
    }

    /**
     * Property: role
     */
    public String getRole() {
        return (String) get_prop(PROP_ROLE);
    }

    public void setRole(String role) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ROLE, role);
    }

    /**
     * Property: name
     */
    @Override
    public String getName() {
        if (this.name == null)
            return "";
        return this.name;
    }

    public void setName(String name) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_NAME, name);
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
     * Property: refOutputSample
     */
    public RefOutputSample getRefOutputSample() {
        return (RefOutputSample) get_prop(PROP_REFOUTPUTSAMPLE);
    }

    public void setRefOutputSample(RefOutputSample refOutputSample)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_REFOUTPUTSAMPLE, refOutputSample);
    }

    /**
     * Property: sample
     */
    public Sample getSample() {
        return (Sample) get_prop(PROP_SAMPLE);
    }

    public void setSample(Sample sample) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SAMPLE, sample);
    }

}
