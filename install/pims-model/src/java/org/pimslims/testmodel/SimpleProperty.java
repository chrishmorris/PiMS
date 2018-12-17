/** 
 * Protein Information Management System - PiMS project
 * 
 * @see http://www.pims-lims.org
 * @version: 2.3
 * 
 * Copyright (c) 2007
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 * 
 * A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.testmodel;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.pimslims.constraint.ConstraintFactory;
import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name="MEMOPSBASECLASSID")
@Table(name=TableName.TEST_SIMPLEPROPERTY, uniqueConstraints={ @UniqueConstraint(columnNames={ "NAME" }) })
@org.pimslims.annotation.MetaClass(
    helpText="A class that have all possible type of simple properties.", 
    keyNames={ "NAME" }, 
    subclasses={}, 
    parent=org.pimslims.model.implementation.Project.class, 
    parentRoleName="project")
public class SimpleProperty extends org.pimslims.model.implementation.MemopsBaseClass implements java.lang.Comparable, java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME="name"; // varchar(80)
    public static final String PROP_LONGNAME="longName"; // varchar(254)
    public static final String PROP_DETAILS="details"; // text
    public static final String PROP_AMOUNT="amount"; // flaot
    public static final String PROP_POSITION="position"; // integer
    public static final String PROP_ISMANDATORY="isMandatory"; // boolean
    public static final String PROP_REMARKS = "remarks"; // collection

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID=1L;

    /* ------------------------------------------------------------ */
    @Basic(optional=false)
    @Column(name=PROP_NAME, length=80, unique=false, nullable=false)
    @org.pimslims.annotation.Attribute(helpText="Name", constraints={ ConstraintType.CONTAINS_NO_LINEBREAK })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional=true)
    @Column(name=PROP_LONGNAME, length=254, unique=false, nullable=true)
    @org.pimslims.annotation.Attribute(helpText="Long name.")
    private String longName;

    /* ------------------------------------------------------------ */
    @Basic(optional=true)
    @Column(name=PROP_DETAILS, columnDefinition=ColumnType.TEXT, unique=false, nullable=true)
    @org.pimslims.annotation.Attribute(helpText="Detail field for comments.")
    private String details;
    
    /* ------------------------------------------------------------ */
    @Basic(optional=true)
    @Column(name=PROP_AMOUNT, columnDefinition=ColumnType.FLOAT, unique=false, nullable=true) 
    @org.pimslims.annotation.Attribute(helpText="amount")
    private Float amount;

    /* ------------------------------------------------------------ */
    @Basic(optional=true)
    @Column(name=PROP_POSITION, columnDefinition=ColumnType.INTEGER, unique=false, nullable=true) 
    @org.pimslims.annotation.Attribute(helpText="position")
    private Integer position;
    
    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name=PROP_ISMANDATORY, columnDefinition=ColumnType.BOOLEAN, unique=false, nullable=false)
    @org.pimslims.annotation.Attribute(helpText = "Flag to indicate whether it is mandatory.")
    private final Boolean isMandatory = true;

    /* ------------------------------------------------------------ */
    @org.hibernate.annotations.CollectionOfElements
    @JoinTable(name = TableName.TEST_SIMPLEPROPERTY_REMARKS, joinColumns = @JoinColumn(name = "protocolid"))
    @org.hibernate.annotations.IndexColumn(name = "order_", base = 1)
    @Column(name = "remark")
    @org.pimslims.annotation.Attribute(helpText = "Some remarks.")
    private final List<String> remarks = new ArrayList<String>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected SimpleProperty() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected SimpleProperty(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments without memops project assigned to default in init()
     */
    public SimpleProperty(WritableVersion wVersion, java.lang.String name) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public SimpleProperty(WritableVersion wVersion, java.util.Map<String, Object> attributes) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }


    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: name
     */
    public String getName() {
        return (String) get_prop(PROP_NAME);
    }

    public void setName(String name) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_NAME, name);
    }

    /**
     * Property: longName
     */
    public String getLongName() {
        return (String) get_prop(PROP_LONGNAME);
    }

    public void setLongName(String longName) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_LONGNAME, longName);
    }

    /**
     * Property: details
     */
    public String getDetails() {
        return (String) get_prop(PROP_DETAILS);
    }

    public void setDetails(String details) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DETAILS, details);
    }
    
    /**
     * Property: AMOUNT
     */
    public Float getAmount() {
        return (Float) get_prop(PROP_AMOUNT);
    }

    public void setAmount(Float amount) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_AMOUNT, amount);
    }

    /** 
     * Property: position
     */
    public Integer getPosition() {
        return (Integer)get_prop(PROP_POSITION);
    }

    public void setPosition(Integer position) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_POSITION, position);
    }
    
    /**
     * Property: isMandatory
     */
    public Boolean getIsMandatory() {
        return (Boolean) get_prop(PROP_ISMANDATORY);
    }

    public void setIsMandatory(Boolean isMandatory) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ISMANDATORY, isMandatory);
    }

    /**
     * Property: remarks
     */
    public List<String> getRemarks() {
        return (List<String>) get_prop(PROP_REMARKS);
    }

    public void setRemarks(List<String> remarks) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_REMARKS, remarks);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */
    /**
     * Derived method: getExtraMethod()
     */

}
