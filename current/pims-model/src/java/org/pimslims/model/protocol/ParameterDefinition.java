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
import java.util.Iterator;
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
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Parameter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "PROT_PARAMETERDEFINITION", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME",
    "PROTOCOLID" }) })
@org.pimslims.annotation.MetaClass(orderBy = "name", helpText = "The list of possible parameters associated with a protocol where the values are associated with an Experiment.", keyNames = {
    "NAME", "PROTOCOL" }, subclasses = {}, parent = org.pimslims.model.protocol.Protocol.class, parentRoleName = "protocol")
public class ParameterDefinition extends org.pimslims.model.core.LabBookEntry implements
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

    public static final String PROP_ISMANDATORY = "isMandatory";

    public static final String PROP_ISGROUPLEVEL = "isGroupLevel";

    public static final String PROP_ISRESULT = "isResult";

    public static final String PROP_POSSIBLEVALUES = "possibleValues";

    public static final String PROP_PROTOCOL = "protocol";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The name of the parameter.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "LABEL", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Label of the parameter.")
    private String label;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "PARAMTYPE", length = 32, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "parameter type of parameter (as in Float, String, ...)", constraints = { "value_must_be_one_of('Float', 'Int', 'String', 'Boolean', 'DateTime','Interval')" })
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
    @Basic(optional = false)
    @Column(name = "ISMANDATORY", columnDefinition = "BOOLEAN", unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "Flag to indicate whether it is mandatory for a value to be assigned to any Parameters derived from this ParameterDefinition before the Parameter's parent Experiment can be considered completed.")
    private final Boolean isMandatory = true;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "ISGROUPLEVEL", columnDefinition = "BOOLEAN", unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "Is this parameter definition for individual experiment or for group of experiment like the ones done in plate?")
    private final Boolean isGroupLevel = false;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "ISRESULT", columnDefinition = "BOOLEAN", unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "Is this parameter definition for result parameters or setup parameters? (false is the default)")
    private final Boolean isResult = false;

    /* ------------------------------------------------------------ */
    @org.hibernate.annotations.CollectionOfElements
    // TODO @javax.persistence.ElementCollection
    @JoinTable(name = "PROT_PARADE_POSSVA", joinColumns = @JoinColumn(name = "PARAMETERDEFINITIONID"))
    @org.hibernate.annotations.IndexColumn(name = "ORDER_", base = 0)
    @Column(name = "POSSIBLEVALUE")
    @org.pimslims.annotation.Attribute(helpText = "The list of possible values for the parameter.")
    private final List<String> possibleValues = new ArrayList<String>(0);

    /* ------------------------------------------------------------ */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PROTOCOLID", unique = false, nullable = false, updatable = false, insertable = false)
    @org.hibernate.annotations.Index(name = "prot_pd_protocol_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "parameterDefinitions")
    private Protocol protocol;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected ParameterDefinition() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected ParameterDefinition(final WritableVersion wVersion)
        throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public ParameterDefinition(final WritableVersion wVersion, final java.lang.String name,
        final java.lang.String paramType, final org.pimslims.model.protocol.Protocol protocol)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);
        attributes.put(PROP_PARAMTYPE, paramType);
        attributes.put(PROP_PROTOCOL, protocol);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public ParameterDefinition(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
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
     * Property: label
     */
    public String getLabel() {
        return (String) get_prop(PROP_LABEL);
    }

    public void setLabel(final String label) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_LABEL, label);
    }

    /**
     * Property: paramType
     */
    public String getParamType() {
        return (String) get_prop(PROP_PARAMTYPE);
    }

    public void setParamType(final String paramType) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PARAMTYPE, paramType);
    }

    /**
     * Property: unit
     */
    public String getUnit() {
        return (String) get_prop(PROP_UNIT);
    }

    public void setUnit(final String unit) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_UNIT, unit);
    }

    /**
     * Property: displayUnit
     */
    public String getDisplayUnit() {
        return (String) get_prop(PROP_DISPLAYUNIT);
    }

    public void setDisplayUnit(final String displayUnit) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DISPLAYUNIT, displayUnit);
    }

    /**
     * Property: defaultValue
     */
    public String getDefaultValue() {
        return (String) get_prop(PROP_DEFAULTVALUE);
    }

    public void setDefaultValue(final String defaultValue) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DEFAULTVALUE, defaultValue);
    }

    /**
     * Property: minValue
     */
    public String getMinValue() {
        return (String) get_prop(PROP_MINVALUE);
    }

    public void setMinValue(final String minValue) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MINVALUE, minValue);
    }

    /**
     * Property: maxValue
     */
    public String getMaxValue() {
        return (String) get_prop(PROP_MAXVALUE);
    }

    public void setMaxValue(final String maxValue) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MAXVALUE, maxValue);
    }

    /**
     * Property: isMandatory
     */
    public Boolean getIsMandatory() {
        return (Boolean) get_prop(PROP_ISMANDATORY);
    }

    public void setIsMandatory(final Boolean isMandatory) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ISMANDATORY, isMandatory);
    }

    /**
     * Property: isGroupLevel
     */
    public Boolean getIsGroupLevel() {
        return (Boolean) get_prop(PROP_ISGROUPLEVEL);
    }

    public void setIsGroupLevel(final Boolean isGroupLevel) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ISGROUPLEVEL, isGroupLevel);
    }

    /**
     * Property: isResult
     */
    public Boolean getIsResult() {
        return (Boolean) get_prop(PROP_ISRESULT);
    }

    public void setIsResult(final Boolean isResult) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ISRESULT, isResult);
    }

    /**
     * Property: possibleValues
     */
    public List<String> getPossibleValues() {
        return (List<String>) get_prop(PROP_POSSIBLEVALUES);
    }

    public void setPossibleValues(final List<String> possibleValues)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_POSSIBLEVALUES, possibleValues);
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

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */

    public Collection<Parameter> getParameters() {
        return this.get_Version().findAll(Parameter.class, Parameter.PROP_PARAMETERDEFINITION, this);
    }

    /**
     * Derived method: getResultParameters()
     */
    public Set<Parameter> getResultParameters() {
        final Set<Parameter> results = new HashSet<Parameter>(0);
        for (final Parameter element : this.getParameters()) {
            if (element.getParameterDefinition().getIsResult()) {
                results.add(element);
            }
        }
        return results;
    }

    /**
     * Derived method: getSetupParameters()
     */
    public Set<Parameter> getSetupParameters() {
        final Set<Parameter> results = new HashSet<Parameter>(0);
        for (final Parameter element : this.getParameters()) {
            if (!element.getParameterDefinition().getIsResult()) {
                results.add(element);
            }
        }
        return results;
    }

    /**
     * ParameterDefinition.moveDown
     * 
     * @throws ConstraintException TODO make this part of a "Child" interface Moves the current parameter
     *             definition to the next position in the list
     */
    public void moveDown() throws ConstraintException {
        List<ParameterDefinition> rearranged = new ArrayList();
        List<ParameterDefinition> pds = this.protocol.getParameterDefinitions();
        for (Iterator iterator = pds.iterator(); iterator.hasNext();) {
            ParameterDefinition pd = (ParameterDefinition) iterator.next();
            if (pd == this) {
                if (iterator.hasNext()) {
                    ParameterDefinition next = (ParameterDefinition) iterator.next();
                    rearranged.add(next);
                }
                rearranged.add(this);
            } else {
                rearranged.add(pd);
            }
        }
        this.protocol.setParameterDefinitions(rearranged);
    }

}
