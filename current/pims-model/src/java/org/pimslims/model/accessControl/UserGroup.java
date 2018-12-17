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
 * 
 *       TODO can we merge this with TOMCATROLE?
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "SYSTEMCLASSID")
@Table(name = "ACCO_USERGROUP", uniqueConstraints = {})
@org.pimslims.annotation.MetaClass(orderBy = "name", helpText = "N/A", keyNames = { "NAME" }, subclasses = {}, parentRoleName = "project")
public class UserGroup extends org.pimslims.model.core.SystemClass implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_PERMISSIONS = "permissions";

    public static final String PROP_HEADER = "header";

    public static final String PROP_MEMBERUSERS = "memberUsers";

    public static final String PROP_MAXSIZE = "maxSize";

    public static final String PROP_IS_PUBLIC = "isPublic";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "N/A", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "maxSize", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Permitted number of users", constraints = {})
    private Integer maxSize;

    /* ------------------------------------------------------------ */
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "userGroup")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "userGroup")
    private final Set<Permission> permissions = new HashSet<Permission>(0);

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "HEADERID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "acco_usergroup_header_inx")
    @org.pimslims.annotation.Role(helpText = "The header user of this group", low = 0, high = 1, isChangeable = true)
    private User header;

    /* ------------------------------------------------------------ */
    // TODO renaming UserGroup.memberUsers to members
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "ACCO_USERGROUP2USER", joinColumns = { @JoinColumn(name = "USERGROUPID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "MEMBERUSERID", nullable = false, updatable = false) })
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "userGroups")
    private final Set<User> memberUsers = new HashSet<User>(0);

    // It would be nicer to make this required, but then it could not be added by upgrade.
    @Basic(optional = true)
    @Column(name = "isPublic", columnDefinition = "BOOLEAN", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Group can be seen by all, not just members")
    private Boolean isPublic = false;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected UserGroup() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected UserGroup(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public UserGroup(final WritableVersion wVersion, final java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public UserGroup(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
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
     * Property: permissions
     */
    public Set<Permission> getPermissions() {
        return (Set<Permission>) get_prop(PROP_PERMISSIONS);
    }

    public void setPermissions(final java.util.Collection<Permission> permissions)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PERMISSIONS, permissions);
    }

    public void addPermission(final Permission permission) throws org.pimslims.exception.ConstraintException {
        add(PROP_PERMISSIONS, permission);
    }

    public void removePermission(final Permission permission)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_PERMISSIONS, permission);
    }

    /**
     * Property: header
     */
    public User getHeader() {
        return (User) get_prop(PROP_HEADER);
    }

    public void setHeader(final User header) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_HEADER, header);
    }

    /**
     * Property: memberUsers
     */
    public Set<User> getMemberUsers() {
        return (Set<User>) get_prop(PROP_MEMBERUSERS);
    }

    public void setMemberUsers(final java.util.Collection<User> memberUsers)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MEMBERUSERS, memberUsers);
    }

    public void addMemberUser(final org.pimslims.model.accessControl.User memberUser)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_MEMBERUSERS, memberUser);
    }

    public void removeMemberUser(final org.pimslims.model.accessControl.User memberUser)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_MEMBERUSERS, memberUser);
    }

    /**
     * UserGroup.getMaxSize
     * 
     * @return
     */
    public Integer getMaxSize() {
        return this.maxSize;
    }

    /**
     * UserGroup.setMaxSize
     * 
     * @param i
     */
    public void setMaxSize(Integer i) {
        this.maxSize = i;
    }

    /**
     * @return Returns the isPublic.
     */
    public Boolean getIsPublic() {
        return null != this.isPublic && this.isPublic;
    }

    /**
     * @param isPublic The isPublic to set.
     */
    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

}
