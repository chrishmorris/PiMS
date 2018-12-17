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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.dao.WritableVersion;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.sample.Sample;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "PEOP_PERSON")
@org.pimslims.annotation.MetaClass(helpText = "An individual person.", keyNames = {}, subclasses = {})
public class Person extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_FAMILYNAME = "familyName";

    public static final String PROP_GIVENNAME = "givenName";

    public static final String PROP_FAMILYTITLE = "familyTitle";

    public static final String PROP_TITLE = "title";

    public static final String PROP_MIDDLEINITIALS = "middleInitials";

    public static final String PROP_CURRENTGROUP = "currentGroup";

    public static final String PROP_PERSONINGROUPS = "personInGroups";

    public static final String PROP_USERS = "users";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    public static final String PROP_EMAIL = null;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "FAMILYNAME", length = 80, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "Family name ('last name' for western names).", constraints = { "contains_no_linebreak" })
    private String familyName;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "GIVENNAME", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Given name ('first name' for western names).", constraints = { "contains_no_linebreak" })
    private String givenName;

    @Basic(optional = true)
    @Column(name = "EMAIL", length = 80, unique = false, nullable = true)
    // it's harder than you might think to write a correct constraint for email address
    @org.pimslims.annotation.Attribute(helpText = "Email address", constraints = {})
    private String email;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "FAMILYTITLE", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Family title (e.g. Jr.)", constraints = {
        "contains_no_linebreak", "no_white_space" })
    /* TODO ISPyB has
                <xs:restriction base="xs:string">
                    <xs:enumeration value="Jr."/>
                    <xs:enumeration value="Sr."/>
                    <xs:enumeration value="III"/>
                    <xs:enumeration value="IV"/>
                    <xs:enumeration value="V"/>
                </xs:restriction>*/
    private String familyTitle;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "TITLE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Title, 'Mr.', 'Mrs.', 'Ms.', 'Dr.', 'Professor',...", constraints = { "contains_no_linebreak" })
    private String title;

    /* ------------------------------------------------------------ */
    @org.hibernate.annotations.CollectionOfElements
    @JoinTable(name = "PEOP_PERSON_MIDDIN", joinColumns = @JoinColumn(name = "PERSONID"))
    @org.hibernate.annotations.IndexColumn(name = "ORDER_", base = 0)
    @Column(name = "MIDDLEINITIAL")
    @org.pimslims.annotation.Attribute(helpText = "Middle initials (including first one).")
    private final List<String> middleInitials = new ArrayList<String>(0);

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "CURRENTGROUPID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "peop_person_currentgroup_inx")
    @org.pimslims.annotation.Role(helpText = "The current group associated to this person.", low = 0, high = 1, isChangeable = true, reverseRoleName = "Unresolved")
    private Group currentGroup;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "person")
    private final Set<PersonInGroup> personInGroups = new HashSet<PersonInGroup>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "person")
    private final Set<User> users = new HashSet<User>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Person() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Person(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Person(final WritableVersion wVersion, final java.lang.String familyName)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_FAMILYNAME, familyName);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Person(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: familyName
     */
    public String getFamilyName() {
        return (String) get_prop(PROP_FAMILYNAME);
    }

    public void setFamilyName(final String familyName) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_FAMILYNAME, familyName);
    }

    /**
     * Property: givenName
     */
    public String getGivenName() {
        return (String) get_prop(PROP_GIVENNAME);
    }

    public void setGivenName(final String givenName) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_GIVENNAME, givenName);
    }

    /**
     * Property: familyTitle
     */
    public String getFamilyTitle() {
        return (String) get_prop(PROP_FAMILYTITLE);
    }

    public void setFamilyTitle(final String familyTitle) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_FAMILYTITLE, familyTitle);
    }

    /**
     * Property: title
     */
    public String getTitle() {
        return (String) get_prop(PROP_TITLE);
    }

    public void setTitle(final String title) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_TITLE, title);
    }

    /**
     * Property: middleInitials
     */
    public List<String> getMiddleInitials() {
        return (List<String>) get_prop(PROP_MIDDLEINITIALS);
    }

    public void setMiddleInitials(final List<String> middleInitials)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MIDDLEINITIALS, middleInitials);
    }

    /**
     * Property: currentGroup
     */
    public Group getCurrentGroup() {
        return (Group) get_prop(PROP_CURRENTGROUP);
    }

    public void setCurrentGroup(final Group currentGroup) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CURRENTGROUP, currentGroup);
    }

    /**
     * Property: personInGroups
     */
    public Set<PersonInGroup> getPersonInGroups() {
        return (Set<PersonInGroup>) get_prop(PROP_PERSONINGROUPS);
    }

    public void setPersonInGroups(final java.util.Collection<PersonInGroup> personInGroups)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PERSONINGROUPS, personInGroups);
    }

    public void addPersonInGroup(final PersonInGroup personInGroup)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_PERSONINGROUPS, personInGroup);
    }

    public void removePersonInGroup(final PersonInGroup personInGroup)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_PERSONINGROUPS, personInGroup);
    }

    /**
     * Property: users
     */
    public Set<User> getUsers() {
        return (Set<User>) get_prop(PROP_USERS);
    }

    public void setUsers(final java.util.Collection<User> users)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_USERS, users);
    }

    public void addUser(final User user) throws org.pimslims.exception.ConstraintException {
        add(PROP_USERS, user);
    }

    public void removeUser(final User user) throws org.pimslims.exception.ConstraintException {
        remove(PROP_USERS, user);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */
    /**
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {
        String ret = this.familyName;
        if (null != this.givenName && this.givenName.length() > 0) {
            ret = this.givenName + " " + ret;
        }
        return ret;
    }

    /**
     * 
     * Person.getSamples
     * 
     * @return
     */
    public Collection<Sample> getSamples() {
        return this.get_Version().findAll(Sample.class, Sample.PROP_ASSIGNTO, this);
    }

    /**
     * @return Returns the email.
     * 
     *         Please note that applications that use this method may need to declare a policy for personal
     *         data.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * @param email The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

}
