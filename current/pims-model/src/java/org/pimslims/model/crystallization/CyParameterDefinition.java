package org.pimslims.model.crystallization;

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
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "CRYZ_PARAMETERDEFINITION")
@org.pimslims.annotation.MetaClass(helpText = "The list of possible parameters associated with a protocol where the values are associated with an Experiment.", keyNames = {}, subclasses = {})
public class CyParameterDefinition extends org.pimslims.model.core.LabBookEntry implements
    java.lang.Comparable, java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_LABEL = "label";

    public static final String PROP_PARAMTYPE = "paramType";

    public static final String PROP_UNIT = "unit";

    public static final String PROP_DISPLAYUNIT = "displayUnit";

    public static final String PROP_DEFAULTVALUE = "defaultValue";

    public static final String PROP_MINVALUE = "minValue";

    public static final String PROP_MAXVALUE = "maxValue";

    public static final String PROP_POSSIBLEVALUES = "possibleValues";

    public static final String PROP_PARAMETERS = "cyParameters";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = true, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The name of the parameter.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "LABEL", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Label of the parameter.")
    private String label;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PARAMTYPE", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "parameter type of parameter (as in Float, String, ...)", constraints = { "value_must_be_one_of('Float', 'Double', 'Int', 'Long', 'String', 'Boolean')" })
    private String paramType;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "UNIT", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Unit of parameter (L, Kg, g/L, s-1, etc.", constraints = {
        "contains_no_linebreak", "no_white_space" })
    private String unit;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "DISPLAYUNIT", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Display unit of the parameter.", constraints = {
        "contains_no_linebreak", "no_white_space" })
    private String displayUnit;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "DEFAULTVALUE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The default value of the parameter.", constraints = { "contains_no_linebreak" })
    private String defaultValue;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "MINVALUE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The minimum value of the parameter.", constraints = { "contains_no_linebreak" })
    private String minValue;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "MAXVALUE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The maximum value of the parameter.", constraints = { "contains_no_linebreak" })
    private String maxValue;

    /* ------------------------------------------------------------ */
    @org.hibernate.annotations.CollectionOfElements
    @JoinTable(name = "cryz_cypade_possva", joinColumns = @JoinColumn(name = "PARAMETERDEFINITIONID"))
    @org.hibernate.annotations.IndexColumn(name = "ORDER_", base = 0)
    @Column(name = "POSSIBLEVALUE")
    @org.pimslims.annotation.Attribute(helpText = "The list of possible values for the parameter.")
    private final List<String> possibleValues = new ArrayList<String>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cyParameterDefinition")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "cyParameterDefinition")
    private final Set<CyParameter> cyParameters = new HashSet<CyParameter>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected CyParameterDefinition() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected CyParameterDefinition(WritableVersion wVersion)
        throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public CyParameterDefinition(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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
     * Property: label
     */
    public String getLabel() {
        return (String) get_prop(PROP_LABEL);
    }

    public void setLabel(String label) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_LABEL, label);
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
     * Property: unit
     */
    public String getUnit() {
        return (String) get_prop(PROP_UNIT);
    }

    public void setUnit(String unit) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_UNIT, unit);
    }

    /**
     * Property: displayUnit
     */
    public String getDisplayUnit() {
        return (String) get_prop(PROP_DISPLAYUNIT);
    }

    public void setDisplayUnit(String displayUnit) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DISPLAYUNIT, displayUnit);
    }

    /**
     * Property: defaultValue
     */
    public String getDefaultValue() {
        return (String) get_prop(PROP_DEFAULTVALUE);
    }

    public void setDefaultValue(String defaultValue) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DEFAULTVALUE, defaultValue);
    }

    /**
     * Property: minValue
     */
    public String getMinValue() {
        return (String) get_prop(PROP_MINVALUE);
    }

    public void setMinValue(String minValue) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MINVALUE, minValue);
    }

    /**
     * Property: maxValue
     */
    public String getMaxValue() {
        return (String) get_prop(PROP_MAXVALUE);
    }

    public void setMaxValue(String maxValue) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MAXVALUE, maxValue);
    }

    /**
     * Property: possibleValues
     */
    public List<String> getPossibleValues() {
        return (List<String>) get_prop(PROP_POSSIBLEVALUES);
    }

    public void setPossibleValues(List<String> possibleValues)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_POSSIBLEVALUES, possibleValues);
    }

    /**
     * Property: parameters
     */
    public Set<CyParameter> getCyParameters() {
        return (Set<CyParameter>) get_prop(PROP_PARAMETERS);
    }

    public void setCyParameters(java.util.Collection<CyParameter> parameters)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PARAMETERS, parameters);
    }

    public void addParameter(CyParameter parameter) throws org.pimslims.exception.ConstraintException {
        add(PROP_PARAMETERS, parameter);
    }

    public void removeParameter(CyParameter parameter) throws org.pimslims.exception.ConstraintException {
        remove(PROP_PARAMETERS, parameter);
    }

}
