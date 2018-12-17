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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.protocol.ParameterDefinition;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "EXPE_PARAMETER")
@org.pimslims.annotation.MetaClass(orderBy = "name", helpText = "A run-time parameter (or result) of the parent Experiment.", keyNames = {}, subclasses = {}, parent = org.pimslims.model.experiment.Experiment.class, parentRoleName = "experiment")
public class Parameter extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_VALUE = "value";

    public static final String PROP_UNIT = "unit";

    public static final String PROP_NAME = "name";

    public static final String PROP_PARAMTYPE = "paramType";

    public static final String PROP_EXPERIMENT = "experiment";

    public static final String PROP_PARAMETERDEFINITION = "parameterDefinition";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "VALUE", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The value of this Parameter.")
    private String value;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "UNIT", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The unit for this Parameter.", constraints = {
        "contains_no_linebreak", "no_white_space" })
    private String unit;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The name of this Parameter.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PARAMTYPE", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The type of this Parameter.", constraints = { "value_must_be_one_of('Int', 'String', 'Boolean', 'DateTime','Interval','Float' )" })
    private String paramType;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EXPERIMENTID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "expe_parameter_experiment_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "parameters")
    private Experiment experiment;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "PARAMETERDEFINITIONID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "expe_parameter_pd_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = 1, isChangeable = true, reverseRoleName = "parameters")
    private ParameterDefinition parameterDefinition;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Parameter() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Parameter(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Parameter(WritableVersion wVersion, org.pimslims.model.experiment.Experiment experiment)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_EXPERIMENT, experiment);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Parameter(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: value
     */
    public String getValue() {
        return (String) get_prop(PROP_VALUE);
    }

    public void setValue(String value) throws org.pimslims.exception.ConstraintException {
        checkValue(value, this.paramType);
        set_prop(PROP_VALUE, value);
    }

    /**
     * Parameter.checkValue
     * 
     * @param value2
     * @param paramType2
     * @throws ModelException
     */
    void checkValue(String value, String paramType) throws ConstraintException {
        if (value == null || value.length() == 0) {
            return;
        }
        if (paramType == null || paramType.length() == 0) {
            return;
        }
        if (paramType.equals("Int")) {
            try {
                new Integer(value);
            } catch (java.lang.NumberFormatException e) {
                throw new ConstraintException(this + " can not accept Integer value:" + value, e);
            }
        } else if (paramType.equals("Float")) {
            try {
                new Float(value);
            } catch (java.lang.NumberFormatException e) {
                throw new ConstraintException(this + " can not accept Float value:" + value, e);
            }
        } else if (paramType.equals("Boolean")) {
            if (value.equalsIgnoreCase("true")) {
                return;
            } else if (value.equalsIgnoreCase("false")) {
                return;
            }
            throw new ConstraintException(this + " can not accept Boolean value:" + value);
        }

    }

    /**
     * Property: unit
     */
    public String getUnit() {
        return (String) get_prop(PROP_UNIT);
    }

    public void setUnit(String unit) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_UNIT, unit);
    }

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
     * Property: paramType
     */
    public String getParamType() {
        return (String) get_prop(PROP_PARAMTYPE);
    }

    public void setParamType(String paramType) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PARAMTYPE, paramType);
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
     * Property: parameterDefinition
     */
    public ParameterDefinition getParameterDefinition() {
        return (ParameterDefinition) get_prop(PROP_PARAMETERDEFINITION);
    }

    public void setParameterDefinition(ParameterDefinition parameterDefinition)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PARAMETERDEFINITION, parameterDefinition);
        if (parameterDefinition != null) {
            this.name = parameterDefinition.getName();
            this.paramType = parameterDefinition.getParamType();

        }
    }

}
