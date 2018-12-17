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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.dao.WritableVersion;

@Entity
@org.pimslims.annotation.EquivalentClasses({
    @org.pimslims.annotation.EquivalentClass("http://omv.ontoware.org/2005/05/ontology#Organisation"),
    @org.pimslims.annotation.EquivalentClass("http://www.w3.org/ns/prov#Organisation") })
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "PEOP_ORGANISATION", uniqueConstraints = {})
@org.pimslims.annotation.MetaClass(orderBy = "name", helpText = "Organisation, public institute or private company.", keyNames = { "NAME" }, subclasses = {})
public class Organisation extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_ORGANISATIONTYPE = "organisationType";

    public static final String PROP_CITY = "city";

    public static final String PROP_POSTALCODE = "postalCode";

    public static final String PROP_COUNTRY = "country";

    public static final String PROP_PHONENUMBER = "phoneNumber";

    public static final String PROP_FAXNUMBER = "faxNumber";

    public static final String PROP_EMAILADDRESS = "emailAddress";

    public static final String PROP_URL = "url";

    public static final String PROP_ADDRESSES = "addresses";

    public static final String PROP_GROUPS = "groups";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label")
    @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The name of organisation, its unique identifier.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ORGANISATIONTYPE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The type of organisation, can be 'home laboratory', 'other laboratory', 'reagent supplier', 'instrument supplier',...", constraints = { "contains_no_linebreak" })
    private String organisationType;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CITY", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The city of the organisation. e.g. 'Cambridge'.", constraints = { "contains_no_linebreak" })
    private String city;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "POSTALCODE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The postal code  e.g. 'CB2 1GA'.", constraints = { "contains_no_linebreak" })
    private String postalCode;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "COUNTRY", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The country of the organisation. e.g. 'UK'.", constraints = { "contains_no_linebreak" })
    private String country;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PHONENUMBER", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The telephone number.", constraints = { "contains_no_linebreak" })
    private String phoneNumber;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "FAXNUMBER", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The fax number.", constraints = { "contains_no_linebreak" })
    private String faxNumber;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "EMAILADDRESS", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Email address of the organisation.", constraints = { "contains_no_linebreak" })
    private String emailAddress;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "URL", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The url of the organisation web site.")
    private String url;

    /* ------------------------------------------------------------ */
    @org.hibernate.annotations.CollectionOfElements
    @JoinTable(name = "PEOP_ORGA_ADDRESSES", joinColumns = @JoinColumn(name = "ORGANISATIONID"))
    @org.hibernate.annotations.IndexColumn(name = "ORDER_", base = 0)
    @Column(name = "ADDRESS")
    @org.pimslims.annotation.Attribute(helpText = "The address of the organisation. e.g. '80 Tennis Court Road'. It can be stored as a list for multiple line address.")
    private final List<String> addresses = new ArrayList<String>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "organisation")
    private final Set<Group> groups = new HashSet<Group>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Organisation() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Organisation(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Organisation(WritableVersion wVersion, java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Organisation(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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
     * Property: organisationType
     */
    public String getOrganisationType() {
        return (String) get_prop(PROP_ORGANISATIONTYPE);
    }

    public void setOrganisationType(String organisationType)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ORGANISATIONTYPE, organisationType);
    }

    /**
     * Property: city
     */
    public String getCity() {
        return (String) get_prop(PROP_CITY);
    }

    public void setCity(String city) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CITY, city);
    }

    /**
     * Property: postalCode
     */
    public String getPostalCode() {
        return (String) get_prop(PROP_POSTALCODE);
    }

    public void setPostalCode(String postalCode) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_POSTALCODE, postalCode);
    }

    /**
     * Property: country
     */
    public String getCountry() {
        return (String) get_prop(PROP_COUNTRY);
    }

    public void setCountry(String country) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_COUNTRY, country);
    }

    /**
     * Property: phoneNumber
     */
    public String getPhoneNumber() {
        return (String) get_prop(PROP_PHONENUMBER);
    }

    public void setPhoneNumber(String phoneNumber) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PHONENUMBER, phoneNumber);
    }

    /**
     * Property: faxNumber
     */
    public String getFaxNumber() {
        return (String) get_prop(PROP_FAXNUMBER);
    }

    public void setFaxNumber(String faxNumber) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_FAXNUMBER, faxNumber);
    }

    /**
     * Property: emailAddress
     */
    public String getEmailAddress() {
        return (String) get_prop(PROP_EMAILADDRESS);
    }

    public void setEmailAddress(String emailAddress) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_EMAILADDRESS, emailAddress);
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
     * Property: addresses
     */
    public List<String> getAddresses() {
        return (List<String>) get_prop(PROP_ADDRESSES);
    }

    public void setAddresses(List<String> addresses) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ADDRESSES, addresses);
    }

    /**
     * Property: groups
     */
    public Set<Group> getGroups() {
        return (Set<Group>) get_prop(PROP_GROUPS);
    }

    public void setGroups(java.util.Collection<Group> groups)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_GROUPS, groups);
    }

    public void addGroup(Group group) throws org.pimslims.exception.ConstraintException {
        add(PROP_GROUPS, group);
    }

    public void removeGroup(Group group) throws org.pimslims.exception.ConstraintException {
        remove(PROP_GROUPS, group);
    }

}
