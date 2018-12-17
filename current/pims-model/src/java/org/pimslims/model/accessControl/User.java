package org.pimslims.model.accessControl;

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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.people.Person;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.target.ResearchObjective;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "SYSTEMCLASSID")
@org.pimslims.annotation.EquivalentClasses({
    @org.pimslims.annotation.EquivalentClass("http://www.w3.org/ns/prov#Person"),
    @org.pimslims.annotation.EquivalentClass("http://purl.org/dc/terms/Agent") })
@Table(name = "ACCO_USER", uniqueConstraints = {})
@org.pimslims.annotation.MetaClass(orderBy = "name", helpText = "N/A", keyNames = { "NAME" }, subclasses = {}, parentRoleName = "project")
public class User extends org.pimslims.model.core.SystemClass implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_PERSON = "person";

    public static final String PROP_SUPERVISOR = "supervisor";

    public static final String PROP_SUPERVISEES = "supervisees";

    public static final String PROP_USERGROUPS = "userGroups";

    public static final String PROP_DIGESTED_PASSWORD = "digestedPassword";

    public static final String PROP_ISACTIVE = "isActive";

    public static final String PROP_SAMPLE_CATEGORIES = "sampleCategories";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 2L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label")
    @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "userid for logging in", constraints = { "contains_no_linebreak" })
    private String name;

    @Basic(optional = true)
    @Column(name = "DIGESTEDPASSWORD", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "password")
    private String digestedPassword;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "PERSONID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "acco_user_person_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = 1, isChangeable = true, reverseRoleName = "users")
    private Person person;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "SUPERVISORID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "acco_user_supervisor_inx")
    @org.pimslims.annotation.Role(helpText = "Is supervised by", low = 0, high = 1, isChangeable = true, reverseRoleName = "supervisees")
    private User supervisor;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "supervisor")
    @org.pimslims.annotation.Role(helpText = "List of students supervised by this user.", low = 0, high = -1, isChangeable = true, reverseRoleName = "supervisor")
    private Set<User> supervisees = new HashSet<User>(0);

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "memberUsers")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "memberUsers")
    private final Set<UserGroup> userGroups = new HashSet<UserGroup>(0);

    // It would be nicer to make this required, but then it could not be added by upgrade.
    @Basic(optional = true)
    @Column(name = "isActive", columnDefinition = "BOOLEAN", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Can log in")
    private Boolean isActive = true;

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "sam_sampca2user", joinColumns = { @JoinColumn(name = "USERID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "SAMPLECATEGORYID", nullable = false, updatable = false) })
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @org.pimslims.annotation.Role(helpText = "Sample categories of interest to a user.", low = 0, high = -1, isChangeable = true)
    private Set<SampleCategory> sampleCategories = new HashSet<SampleCategory>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected User() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected User(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public User(final WritableVersion wVersion, final java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public User(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
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

    public void setName(final String name) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_NAME, name);
    }

    /**
     * Property: person
     */
    public Person getPerson() {
        return (Person) get_prop(PROP_PERSON);
    }

    public void setPerson(final Person person) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PERSON, person);
    }

    /**
     * User.getSupervisor
     * 
     * @return
     */
    public User getSupervisor() {
        return (User) get_prop(PROP_SUPERVISOR);
    }

    public void setSupervisor(final User supervisor) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SUPERVISOR, supervisor);
    }

    /**
     * Property: userGroups
     */
    public Set<UserGroup> getUserGroups() {
        return (Set<UserGroup>) get_prop(PROP_USERGROUPS);
    }

    public void setUserGroups(final java.util.Collection<UserGroup> userGroups)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_USERGROUPS, userGroups);
    }

    public void addUserGroup(final org.pimslims.model.accessControl.UserGroup userGroup)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_USERGROUPS, userGroup);
    }

    public void removeUserGroup(final org.pimslims.model.accessControl.UserGroup userGroup)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_USERGROUPS, userGroup);
    }

    /**
     * 
     * Person.getResearchObjectives
     * 
     * @return
     */
    @Deprecated
    public Collection<ResearchObjective> getResearchObjectives() {
        return this.get_Version().findAll(ResearchObjective.class, ResearchObjective.PROP_OWNER, this);
    }

    /**
     * User.getProjects
     * 
     * @return the projects this user owns
     */
    public Collection<org.pimslims.model.experiment.Project> getProjects() {
        Collection<ResearchObjective> ret =
            this.get_Version().findAll(ResearchObjective.class, ResearchObjective.PROP_OWNER, this);
        return new ArrayList<Project>(ret);
    }

    /**
     * User.getSupervisees
     * 
     * @return
     */
    public Collection<User> getSupervisees() {
        return (Set<User>) get_prop(PROP_SUPERVISEES);
    }

    public void addSupervisee(User student) throws org.pimslims.exception.ConstraintException {
        add(PROP_SUPERVISEES, student);
    }

    public void removeSupervisee(User student) throws org.pimslims.exception.ConstraintException {
        remove(PROP_SUPERVISEES, student);
    }

    /**
     * User.getDigestedPassword
     * 
     * @return
     */
    public String getDigestedPassword() {
        return this.digestedPassword;
    }

    /**
     * User.setDigestedPassword
     * 
     * @param password the password to save, in plaintext
     * @throws ConstraintException
     * @throws AccessException
     */
    public void setDigestedPassword(String password) throws ConstraintException, AccessException {
        String digested = digest(password);
        set_prop(PROP_DIGESTED_PASSWORD, digested);

    }

    private String digest(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(password.getBytes());
        byte[] digest = md.digest();
        StringBuilder hex = new StringBuilder(digest.length * 2);
        for (byte b : digest) {
            hex.append(String.format("%02x", b));
        }
        String digested = hex.toString();
        return digested;
    }

    @Override
    public void set_Value(String attributeName, Object value) throws AccessException, ConstraintException {
        if (PROP_DIGESTED_PASSWORD.equals(attributeName)) {
            this.setDigestedPassword((String) value);
        } else {
            super.set_Value(attributeName, value);
        }
    }

    /**
     * User.isPassword
     * 
     * @param string
     * @return
     */
    public boolean isPassword(String string) {
        if (string == null) {
            return null == this.digestedPassword;
        }
        return this.digest(string).equals(this.digestedPassword);
    }

    /**
     * User.getIsActive Note that legacy users have null in this column. They must be considered as active.
     * 
     * @return
     */
    public Boolean getIsActive() {
        return null == this.isActive || this.isActive.booleanValue();
    }

    /**
     * User.setIsActive
     * 
     * @param b
     */
    public void setIsActive(boolean b) {
        this.isActive = b;
    }

    public String getFamilyName() {
        if (null == this.person) {
            return null;
        }
        return this.person.getFamilyName();
    }

    public void setFamilyName(String familyName) throws ConstraintException {
        this.person.setFamilyName(familyName);
    }

    public String getGivenName() {
        if (null == this.person) {
            return null;
        }
        return this.person.getGivenName();
    }

    public void setGivenName(String givenName) throws ConstraintException {
        this.person.setGivenName(givenName);
    }

    public String getFamilyTitle() {
        if (null == this.person) {
            return null;
        }
        return this.person.getFamilyTitle();
    }

    public void setFamilyTitle(String familyTitle) throws ConstraintException {
        this.person.setFamilyTitle(familyTitle);
    }

    public void setTitle(String title) throws ConstraintException {
        this.person.setTitle(title);
    }

    public List<String> getMiddleInitials() {
        if (null == this.person) {
            return null;
        }
        return this.person.getMiddleInitials();
    }

    public String getEmail() {
        if (null == this.person) {
            return null;
        }
        return this.person.getEmail();
    }

    public void setEmail(String email) {
        this.person.setEmail(email);
    }

    /* User.getPreferences Done by search,because performance of User is needed for access control
     * 
     * @return
     */
    public Collection<Preference> getPreferences() {
        return this.get_Version().findAll(Preference.class, Preference.PROP_USER, this);
    }

    /**
     * Property: sampleCategories Preferred sample categories
     */
    public Set<SampleCategory> getSampleCategories() {
        return (Set<SampleCategory>) get_prop(PROP_SAMPLE_CATEGORIES);
    }

    public void addSampleCategory(org.pimslims.model.reference.SampleCategory sampleCategory)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_SAMPLE_CATEGORIES, sampleCategory);
    }

    /**
     * User.isPreferred
     * 
     * @param category
     * @return
     */
    public boolean isPreferred(SampleCategory category) {
        if (this.sampleCategories.isEmpty()) {
            return true;
        }
        return this.sampleCategories.contains(category);
    }

    /**
     * User.setSampleCategories
     * 
     * @param singleton
     */
    public void setSampleCategories(Collection<SampleCategory> categories) {
        this.sampleCategories = new HashSet(categories);
    }

    /**
     * User.getHolders
     * 
     * @return
     */
    public Collection<Holder> getHolders() {
        return this.get_Version().findAll(Holder.class, Holder.PROP_SUBSCRIBERS, this);
    }

    /**
     * User.addHolder
     * 
     * @param holder
     * @throws ConstraintException
     */
    public void addHolder(Holder holder) throws ConstraintException {
        holder.addSubscriber(this);
    }

    /**
     * User.clearHolders
     * 
     * @throws ConstraintException
     */
    public void clearHolders() throws ConstraintException {
        Collection<Holder> holders = this.getHolders();
        for (Iterator iterator = holders.iterator(); iterator.hasNext();) {
            Holder holder = (Holder) iterator.next();
            holder.removeSubscriber(this);
        }

    }

}
