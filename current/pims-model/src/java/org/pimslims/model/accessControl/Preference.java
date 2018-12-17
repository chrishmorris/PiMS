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

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "SYSTEMCLASSID")
@Table(name = "ACCO_PREFERENCE", uniqueConstraints = {})
@org.pimslims.annotation.MetaClass(orderBy = "name", helpText = "A user preference setting", keyNames = {
    "USER", "NAME" }, subclasses = {}, parent = User.class, parentRoleName = "user")
public class Preference extends org.pimslims.model.core.SystemClass implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_USER = "user";

    public static final String PROP_VALUE = "value";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 2L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "keyword, the type of preference", constraints = { "contains_no_linebreak" })
    private String name;

    @Basic(optional = true)
    @Column(name = "VALUE", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "the value of the preference")
    private String value;

    /* ------------------------------------------------------------ */
    // Could have , reverseRoleName = "preferences"
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "USERID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "acco_pref_user_inx")
    @org.pimslims.annotation.Role(helpText = "The user whose preference this is", low = 1, high = 1, isChangeable = false)
    private User user;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Preference() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Preference(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     * 
     * @param string
     * @param user2
     */
    public Preference(final WritableVersion wVersion, User user, final String name, final String value)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);
        attributes.put(PROP_VALUE, value);
        attributes.put(PROP_USER, user);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Preference(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
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
    public User getUser() {
        return this.user;
    }

    /**
     * User.getDigestedPassword
     * 
     * @return
     */
    public String getValue() {
        return this.value;
    }

    /**
     * User.setDigestedPassword
     * 
     * @param password the password to save, in plaintext
     * @throws ConstraintException
     * @throws AccessException
     */
    public void setValue(String value) throws ConstraintException, AccessException {
        set_prop(PROP_VALUE, value);

    }

}
