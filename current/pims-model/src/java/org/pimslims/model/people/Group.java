package org.pimslims.model.people;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "PEOP_GROUP")
@org.pimslims.annotation.MetaClass(orderBy = "name", helpText = "A group of persons within an organisation.", keyNames = {}, subclasses = {}, parent = org.pimslims.model.people.Organisation.class, parentRoleName = "organisation")
public class Group extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_URL = "url";

    public static final String PROP_ORGANISATION = "organisation";

    public static final String PROP_PERSONINGROUPS = "personInGroups";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The name of the group.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "URL", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The url of the group. e.g. 'http://www.pims-lims.org'.")
    private String url;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ORGANISATIONID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "peop_group_organisation_inx")
    @org.pimslims.annotation.Role(helpText = "The organisation to which the group belongs. e.g. 'University of Cambridge'.", low = 1, high = 1, isChangeable = true, reverseRoleName = "groups")
    private Organisation organisation;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    @org.pimslims.annotation.Role(helpText = "Persons in group, with address information etc.", low = 0, high = -1, isChangeable = true, reverseRoleName = "group")
    private final Set<PersonInGroup> personInGroups = new HashSet<PersonInGroup>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Group() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Group(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Group(WritableVersion wVersion, org.pimslims.model.people.Organisation organisation)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_ORGANISATION, organisation);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Group(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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
        if (this.name == null)
            return "";
        return this.name;
    }

    public void setName(String name) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_NAME, name);
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
     * Property: organisation
     */
    public Organisation getOrganisation() {
        return (Organisation) get_prop(PROP_ORGANISATION);
    }

    public void setOrganisation(Organisation organisation) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ORGANISATION, organisation);
    }

    /**
     * Property: personInGroups
     */
    public Set<PersonInGroup> getPersonInGroups() {
        return (Set<PersonInGroup>) get_prop(PROP_PERSONINGROUPS);
    }

    public void setPersonInGroups(java.util.Collection<PersonInGroup> personInGroups)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PERSONINGROUPS, personInGroups);
    }

    public void addPersonInGroup(PersonInGroup personInGroup)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_PERSONINGROUPS, personInGroup);
    }

    public void removePersonInGroup(PersonInGroup personInGroup)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_PERSONINGROUPS, personInGroup);
    }

}
