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
 * 
 *       TODO reconsider the mappings. The existence of this table doesn't seem to help anything. How does it
 *       differ from PublicEntry?
 */

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;
import org.pimslims.metamodel.PublicAccess;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "CORE_SYSTEMCLASS", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(helpText = "Base class for data model classes. Contains elements common to all classes.", keyNames = {}, subclasses = {
    org.pimslims.model.accessControl.UserGroup.class, org.pimslims.model.accessControl.User.class,
    org.pimslims.model.accessControl.Preference.class, org.pimslims.model.accessControl.Permission.class,
    org.pimslims.model.core.LabNotebook.class })
public abstract class SystemClass extends org.pimslims.metamodel.AbstractModelObject implements
    java.lang.Comparable, java.io.Serializable, PublicAccess {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_DBID = "dbId";

    public static final String PROP_DETAILS = "details";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Id
    @Basic(optional = false)
    @Column(name = "DBID", unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "")
    private Long dbId;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#comment")
    @Column(name = "DETAILS", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Details field for comments.")
    private String details;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected SystemClass() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected SystemClass(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public SystemClass(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: dbId
     */
    @Override
    public Long getDbId() {
        return this.dbId;
    }

    @Override
    protected void setDbId(final Long dbId) {
        this.dbId = dbId;
    }

    /**
     * Property: details
     */
    public String getDetails() {
        return (String) get_prop(PROP_DETAILS);
    }

    public void setDetails(final String details) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DETAILS, details);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */
    @Override
    public LabNotebook getAccess() {
        return null;
    }

}
