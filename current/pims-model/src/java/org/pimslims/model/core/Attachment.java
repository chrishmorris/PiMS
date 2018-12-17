/**
 * Protein Information Management System - PiMS project
 * 
 * @see http://www.pims-lims.org
 * @version: 2.3
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
package org.pimslims.model.core;

import java.util.Calendar;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;
import org.pimslims.metamodel.ControlledAccess;
import org.pimslims.model.accessControl.User;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "CORE_ATTACHMENT", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(helpText = "Base class for all kind of attachments.", keyNames = {}, subclasses = {
    org.pimslims.model.core.ExternalDbLink.class, org.pimslims.model.core.Annotation.class,
    org.pimslims.model.core.Citation.class, org.pimslims.model.core.Note.class,
    org.pimslims.model.core.ApplicationData.class }, parent = org.pimslims.model.core.LabBookEntry.class, parentRoleName = "parentEntry")
public abstract class Attachment extends org.pimslims.metamodel.AbstractModelObject implements
    java.lang.Comparable, java.io.Serializable, ControlledAccess {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_DBID = "dbId";

    public static final String PROP_DETAILS = "details";

    public static final String PROP_DATE = "date";

    public static final String PROP_AUTHOR = "author";

    public static final String PROP_PARENTENTRY = "parentEntry";

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

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "DATE_", columnDefinition = "TIMESTAMPTZ", unique = false, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @org.pimslims.annotation.Attribute(helpText = "Date attachment was created.")
    private final Calendar date = Calendar.getInstance();

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "AUTHORID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "core_attachment_author_inx")
    @org.pimslims.annotation.Role(helpText = "The author who created this attachment.", low = 0, high = 1, isChangeable = true)
    private User author;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PARENTENTRYID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "core_attachment_pe_inx")
    @org.pimslims.annotation.Role(helpText = "The entry to which this attachment belongs.", low = 1, high = 1, isChangeable = true, reverseRoleName = "attachments")
    private LabBookEntry parentEntry;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Attachment() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Attachment(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
        this.author = wVersion.getCurrentUser();
    }

    /**
     * Constructor for required arguments
     */
    public Attachment(final WritableVersion wVersion, final org.pimslims.model.core.LabBookEntry parentEntry)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put(PROP_PARENTENTRY, parentEntry);
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

    /**
     * Property: date
     */
    public Calendar getDate() {
        return (Calendar) get_prop(PROP_DATE);
    }

    public void setDate(final Calendar date) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DATE, date);
    }

    /**
     * Property: author
     */
    public User getAuthor() {
        return (User) get_prop(PROP_AUTHOR);
    }

    public void setCreator(final User author) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_AUTHOR, author);
    }

    /**
     * Property: parentEntry
     */
    public LabBookEntry getParentEntry() {
        return (LabBookEntry) get_prop(PROP_PARENTENTRY);
    }

    public void setParentEntry(final LabBookEntry parentEntry)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PARENTENTRY, parentEntry);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */
    @Override
    public LabNotebook getAccess() {
        return this.parentEntry.getAccess();

    }
}
