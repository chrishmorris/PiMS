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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "ATTACHMENTID")
@Table(name = "CORE_NOTE")
@org.pimslims.annotation.MetaClass(helpText = "Note.", keyNames = {}, subclasses = {})
public class Note extends org.pimslims.model.core.Attachment implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_RELATEDENTRIES = "relatedEntries";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The name of the annotation.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "CORE_NOTE2RELATEDENTRYS", joinColumns = { @JoinColumn(name = "NOTEID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "LABBOOKENTRYID", nullable = false, updatable = false) })
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @org.pimslims.annotation.Role(helpText = "List of LabBookEntry related to this Note.", low = 0, high = -1, isChangeable = true)
    private final Set<LabBookEntry> relatedEntries = new HashSet<LabBookEntry>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Note() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Note(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Note(WritableVersion wVersion, org.pimslims.model.core.LabBookEntry parent)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put(PROP_PARENTENTRY, parent);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Note(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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
     * Property: relatedEntries
     */
    public Set<LabBookEntry> getRelatedEntries() {
        return (Set<LabBookEntry>) get_prop(PROP_RELATEDENTRIES);
    }

    public void setRelatedEntries(java.util.Collection<LabBookEntry> relatedEntries)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_RELATEDENTRIES, relatedEntries);
    }

    public void addRelatedEntry(LabBookEntry relatedEntry) throws org.pimslims.exception.ConstraintException {
        add(PROP_RELATEDENTRIES, relatedEntry);
    }

    public void removeRelatedEntry(LabBookEntry relatedEntry)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_RELATEDENTRIES, relatedEntry);
    }

}
