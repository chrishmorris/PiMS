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
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;
import org.pimslims.model.core.LabNotebook;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "SYSTEMCLASSID")
@Table(name = "ACCO_PERMISSION", uniqueConstraints = { @UniqueConstraint(columnNames = { "OPTYPE",
    "ACCESSOBJECTID", "USERGROUPID" }) })
@org.pimslims.annotation.MetaClass(helpText = "N/A", keyNames = { "OPTYPE", "ACCESSOBJECT", "USERGROUP" }, subclasses = {}, parent = org.pimslims.model.accessControl.UserGroup.class, parentRoleName = "userGroup")
public class Permission extends org.pimslims.model.core.SystemClass implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */

    @Deprecated
    // obsolete
    public static final String PROP_PERMISSIONCLASS = "permissionClass";

    public static final String PROP_OPTYPE = "opType";

    @Deprecated
    // obsolete
    public static final String PROP_ROLENAME = "roleName";

    @Deprecated
    //obsolete
    public static final String PROP_PERMISSION = "permission";

    @Deprecated
    // PROP_LABNOTEBOOK
    public static final String PROP_ACCESSOBJECT = "accessObject";

    public static final String PROP_LABNOTEBOOK = "accessObject";

    public static final String PROP_USERGROUP = "userGroup";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ----- TODO remove this in 5.0
     * 4.4 includes the change to make it nullable.
     *  ------------------------------------------------------- */
    @Basic(optional = false)
    @Column(name = "PERMISSIONCLASS", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "N/A", constraints = { "contains_no_linebreak",
        "no_white_space" })
    @Deprecated
    private final String permissionClass = "any";

    /* ------TODO remove this in 5.0
     * 4.4 includes the change to make it nullable.
     * ------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "OPTYPE", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "N/A", constraints = { "value_must_be_one_of('create', 'read', 'update','delete', 'unlock', 'admin')" })
    private final String opType = "any";

    /* -----TODO remove this in 5.0
     * 4.4 includes the change to make it nullable.
     * ------------------------------------------------------- */
    @Basic(optional = false)
    @Column(name = "ROLENAME", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "N/A", constraints = { "contains_no_linebreak",
        "no_white_space" })
    @Deprecated
    // not used
    private String roleName = "any";

    /* ----TODO remove this in 5.0
     * 4.4 includes the change to make it nullable.
     * -------------------------------------------------------- */
    @Basic(optional = false)
    @Column(name = "PERMISSION", columnDefinition = "BOOLEAN", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "N/A")
    @Deprecated
    // always true
    private Boolean permission = true;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ACCESSOBJECTID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "acco_permission_ao_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "permissions")
    private LabNotebook accessObject;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USERGROUPID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "acco_permission_usergroup_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "permissions")
    private UserGroup userGroup;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Permission() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Permission(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    @Deprecated
    public Permission(final WritableVersion wVersion, final java.lang.String permissionClass,
        final java.lang.String opType, final org.pimslims.model.core.LabNotebook accessObject,
        final org.pimslims.model.accessControl.UserGroup userGroup)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        //was attributes.put(PROP_PERMISSIONCLASS, permissionClass);
        attributes.put(PROP_OPTYPE, opType);
        attributes.put(PROP_LABNOTEBOOK, accessObject);
        attributes.put(PROP_USERGROUP, userGroup);

        init(attributes);
    }

    public Permission(final WritableVersion wVersion, final java.lang.String opType,
        final org.pimslims.model.core.LabNotebook accessObject,
        final org.pimslims.model.accessControl.UserGroup userGroup)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put(PROP_OPTYPE, opType);
        attributes.put(PROP_LABNOTEBOOK, accessObject);
        attributes.put(PROP_USERGROUP, userGroup);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Permission(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: permissionClass
     */
    @Deprecated
    // obsolete
    public String getPermissionClass() {
        return "any";
    }

    @Deprecated
    // obsolete
    public void setPermissionClass(final String permissionClass)
        throws org.pimslims.exception.ConstraintException {
        if (!"any".equals(permissionClass)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Property: opType
     */
    public String getOpType() {
        return (String) get_prop(PROP_OPTYPE);
    }

    public void setOpType(final String opType) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_OPTYPE, opType);
    }

    /**
     * Property: roleName
     */
    @Deprecated
    // obsolete
    public String getRoleName() {
        return "any";
    }

    @Deprecated
    // obsolete
    public void setRoleName(final String roleName) {
        if (!"any".equals(roleName)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Property: permission
     */
    @Deprecated
    // obsolete
    public Boolean getPermission() {
        return true;
    }

    @Deprecated
    // obsolete
    public void setPermission(final Boolean permission) {
        if (!permission) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Property: accessObject
     */
    @Deprecated
    // use getLabNotebook
    public LabNotebook getAccessObject() {
        return (LabNotebook) get_prop(PROP_LABNOTEBOOK);
    }

    /**
     * Permission.getLabNotebook
     * 
     * @return
     */
    public LabNotebook getLabNotebook() {
        return (LabNotebook) get_prop(PROP_LABNOTEBOOK);
    }

    public void setAccessObject(final LabNotebook accessObject)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_LABNOTEBOOK, accessObject);
    }

    /**
     * Property: userGroup
     */
    public UserGroup getUserGroup() {
        return (UserGroup) get_prop(PROP_USERGROUP);
    }

    public void setUserGroup(final UserGroup userGroup) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_USERGROUP, userGroup);
    }

    /**
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {

        return (null == this.userGroup ? "" : this.userGroup.getName()) + " " + this.opType + " "
            + (null == this.accessObject ? "" : this.accessObject.getName());

    }

}
