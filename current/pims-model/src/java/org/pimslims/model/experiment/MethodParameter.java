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

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "EXPE_METHODPARAMETER")
@org.pimslims.annotation.MetaClass(helpText = "Parameters used for the Method.", keyNames = {}, subclasses = {}, parent = org.pimslims.model.experiment.Method.class, parentRoleName = "method")
public class MethodParameter extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_VALUE = "value";

    public static final String PROP_PARAMTYPE = "paramType";

    public static final String PROP_UNIT = "unit";

    public static final String PROP_METHOD = "method";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The name of the parameter - if several Parameter objects have the same name this is interpreted as the members of a list corresponding to the name.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "VALUE", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "value of the parameter, in String representation")
    private String value;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PARAMTYPE", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "parameter type of parameter (as in Float, String, ...)", constraints = { "value_must_be_one_of('Float', 'Int', 'Long','String','Boolean')" })
    private String paramType;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "UNIT", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Unit of parameter (L, Kg, g/L, s-1, etc.)", constraints = {
        "contains_no_linebreak", "no_white_space" })
    private String unit;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "METHODID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "expe_methpa_method_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "methodParameters")
    private Method method;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected MethodParameter() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected MethodParameter(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public MethodParameter(WritableVersion wVersion, java.lang.String name,
        org.pimslims.model.experiment.Method method) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);
        attributes.put(PROP_METHOD, method);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public MethodParameter(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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
     * Property: value
     */
    public String getValue() {
        return (String) get_prop(PROP_VALUE);
    }

    public void setValue(String value) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_VALUE, value);
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
     * Property: method
     */
    public Method getMethod() {
        return (Method) get_prop(PROP_METHOD);
    }

    public void setMethod(Method method) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_METHOD, method);
    }

}
