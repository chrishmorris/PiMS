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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "MEMOPSBASECLASSID")
@Table(name = TableName.TEST_CLASSTEMPLATE, uniqueConstraints = {  })
@org.pimslims.annotation.MetaClass(
    helpText = "A model class template.", 
    keyNames = { "NAME" }, 
    subclasses = {}, 
    parent = org.pimslims.model.implementation.Project.class, 
    parentRoleName = "project")
public class ClassTemplate extends org.pimslims.model.implementation.MemopsBaseClass implements java.lang.Comparable, java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = PROP_NAME, length=80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(
        helpText = "The name of the template class.", 
        constraints = { ConstraintType.CONTAINS_NO_LINEBREAK})
    private String name;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected ClassTemplate() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected ClassTemplate(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments without memops project assigned to default in init()
     */
    public ClassTemplate(WritableVersion wVersion, java.lang.String name) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public ClassTemplate(WritableVersion wVersion, java.util.Map<String, Object> attributes) throws org.pimslims.exception.ConstraintException {
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
    
    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */
    /**
     * Derived method: getExtraMethod()
     */

}
