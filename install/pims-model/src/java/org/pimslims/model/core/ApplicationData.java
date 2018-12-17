package org.pimslims.model.core;

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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.dao.WritableVersion;

@Deprecated
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "ATTACHMENTID")
@Table(name = "CORE_APPLICATIONDATA")
@org.pimslims.annotation.MetaClass(helpText = "Stores program-specific data. Intended for application-specific extensions to the data model. ApplicationData objects should be used only by the application that wrote them originally.  ApplicationData objects all have an attribute 'value' holding the data value. The data type of the value differs between the different subclasses of ApplicationData", keyNames = {}, subclasses = {})
public abstract class ApplicationData extends org.pimslims.model.core.Attachment implements
    java.lang.Comparable, java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_APPLICATION = "application";
    public static final String PROP_KEYWORD = "keyword";
    public static final String PROP_VALUE = "value";
    public static final String PROP_TYPE = "type";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "APPLICATION", length = 80, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "Name of application owning the data.", constraints = { "contains_no_linebreak" })
    private String application;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "KEYWORD", length = 80, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "keyword or name of variable", constraints = { "contains_no_linebreak" })
    private String keyword;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "VALUE", columnDefinition = "TEXT", unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "Data value")
    private String value;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "TYPE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The type of the application data value.", constraints = { "contains_no_linebreak" })
    private String type;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected ApplicationData() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected ApplicationData(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments without memops project assigned to default in init()
     */
    public ApplicationData(WritableVersion wVersion, java.lang.String application, java.lang.String keyword,
        java.lang.String value, org.pimslims.model.core.LabBookEntry parent)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_APPLICATION, application);
        attributes.put(PROP_KEYWORD, keyword);
        attributes.put(PROP_VALUE, value);
        attributes.put(PROP_PARENTENTRY, parent);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public ApplicationData(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: application
     */
    public String getApplication() {
        return (String) get_prop(PROP_APPLICATION);
    }

    public void setApplication(String application) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_APPLICATION, application);
    }

    /**
     * Property: keyword
     */
    public String getKeyword() {
        return (String) get_prop(PROP_KEYWORD);
    }

    public void setKeyword(String keyword) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_KEYWORD, keyword);
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
     * Property: type
     */
    public String getType() {
        return (String) get_prop(PROP_TYPE);
    }

    public void setType(String type) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_TYPE, type);
    }

}
