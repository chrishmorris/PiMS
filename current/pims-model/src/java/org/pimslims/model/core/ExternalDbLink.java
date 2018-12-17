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
import org.pimslims.model.reference.Database;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "ATTACHMENTID")
@Table(name = "CORE_EXTERNALDBLINK")
@org.pimslims.annotation.MetaClass(helpText = "The information on a database reference. This is a reference to an element in an outside database.", keyNames = {}, subclasses = {})
public class ExternalDbLink extends org.pimslims.model.core.Attachment implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_RELEASE = "release";

    public static final String PROP_ACCESSION_NUMBER = "code";

    @Deprecated
    // renamed to Accession Number 
    public static final String PROP_CODE = "code";

    public static final String PROP_URL = "url";

    public static final String PROP_DATABASE = "dbName";

    @Deprecated
    // use PROP_DATABASE_NAME
    public static final String PROP_DBNAME = "dbName";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "RELEASE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The database release.", constraints = { "contains_no_linebreak" })
    private String release;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CODE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The database code that identifies the reference. e.g. 'Q12178'. In case of PDB reference, the chain has to be specified.", constraints = {
        "contains_no_linebreak", "no_white_space" })
    private String code;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "URL", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The url of the database reference entry.")
    private String url;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DBNAMEID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "core_externaldblink_dbname_inx")
    @org.pimslims.annotation.Role(helpText = "The name of the database to which the reference belongs.", low = 1, high = 1, isChangeable = true, reverseRoleName = "dbRefs")
    private Database dbName;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected ExternalDbLink() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected ExternalDbLink(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public ExternalDbLink(WritableVersion wVersion, org.pimslims.model.reference.Database dbName,
        org.pimslims.model.core.LabBookEntry parent) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_DATABASE, dbName);
        attributes.put(PROP_PARENTENTRY, parent);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public ExternalDbLink(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: release
     */
    public String getRelease() {
        return (String) get_prop(PROP_RELEASE);
    }

    public void setRelease(String release) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_RELEASE, release);
    }

    /**
     * Property: code
     */
    public String getAccessionNumber() {
        return (String) get_prop(PROP_ACCESSION_NUMBER);
    }

    public void setAccessionNumber(String code) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ACCESSION_NUMBER, code);
    }

    @Deprecated
    // renamed to Accession Number
    public String getCode() {
        return (String) get_prop(PROP_ACCESSION_NUMBER);
    }

    @Deprecated
    // renamed to Accession Number
    public void setCode(String code) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ACCESSION_NUMBER, code);
    }

    /**
     * Property: url
     */
    public String getUrl() {
        return (String) get_prop(PROP_URL);
    }

    public void setUrl(String url) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_URL, url);
    }

    /**
     * Property: dbName
     */
    public Database getDatabaseName() {
        return (Database) get_prop(PROP_DATABASE);
    }

    public void setDatabaseName(Database dbName) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DATABASE, dbName);
    }

    @Deprecated
    // renamed
    public Database getDbName() {
        return (Database) get_prop(PROP_DATABASE);
    }

    @Deprecated
    // renamed
    public void setDbName(Database dbName) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DATABASE, dbName);
    }

    /**
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {
        return this.dbName.getName();
    }

}
