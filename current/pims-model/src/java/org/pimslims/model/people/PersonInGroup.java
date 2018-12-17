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
import java.util.Calendar;
import java.util.List;

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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.pimslims.dao.WritableVersion;

@Deprecated
// User is sufficient
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "PEOP_PERSONINGROUP")
@org.pimslims.annotation.MetaClass(helpText = "The person as member of a group includes position and contact information.", keyNames = {}, subclasses = {}, parent = org.pimslims.model.people.Person.class, parentRoleName = "person")
public class PersonInGroup extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_POSITION = "position";

    public static final String PROP_MAILINGADDRESS = "mailingAddress";

    public static final String PROP_DELIVERYADDRESS = "deliveryAddress";

    public static final String PROP_EMAILADDRESS = "emailAddress";

    public static final String PROP_FAXNUMBER = "faxNumber";

    public static final String PROP_STARTDATE = "startDate";

    public static final String PROP_ENDDATE = "endDate";

    public static final String PROP_PHONENUMBERS = "phoneNumbers";

    public static final String PROP_GROUP = "group";

    public static final String PROP_PERSON = "person";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "POSITION_", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Job position.", constraints = { "contains_no_linebreak" })
    private String position;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "MAILINGADDRESS", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Mailing address of the person within a certain group.")
    private String mailingAddress;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "DELIVERYADDRESS", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Delivery address of the person within a certain group.")
    private String deliveryAddress;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "EMAILADDRESS", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Email address of the person within a certain group.", constraints = { "contains_no_linebreak" })
    private String emailAddress;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "FAXNUMBER", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Fax number of the person within a certain group.", constraints = { "contains_no_linebreak" })
    private String faxNumber;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "STARTDATE", columnDefinition = "TIMESTAMPTZ", unique = false, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @org.pimslims.annotation.Attribute(helpText = "N/A")
    private Calendar startDate;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ENDDATE", columnDefinition = "TIMESTAMPTZ", unique = false, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @org.pimslims.annotation.Attribute(helpText = "The date from which the contact data is not valid.")
    private Calendar endDate;

    /* ------------------------------------------------------------ */
    @org.hibernate.annotations.CollectionOfElements
    @JoinTable(name = "PEOP_PERSINGR_PHONNU", joinColumns = @JoinColumn(name = "PERSONINGROUPID"))
    @org.hibernate.annotations.IndexColumn(name = "order_", base = 0)
    @Column(name = "PHONENUMBER")
    @org.pimslims.annotation.Attribute(helpText = "List of telephone numbers of the person within a certain group.")
    private final List<String> phoneNumbers = new ArrayList<String>(0);

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "GROUPID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "peop_personingroup_group_inx")
    @org.pimslims.annotation.Role(helpText = "Group that the PersonInGroup belongs to.", low = 1, high = 1, isChangeable = true, reverseRoleName = "personInGroups")
    private Group group;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PERSONID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "peop_personingroup_person_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "personInGroups")
    private Person person;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected PersonInGroup() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected PersonInGroup(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public PersonInGroup(WritableVersion wVersion, org.pimslims.model.people.Group group,
        org.pimslims.model.people.Person person) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_GROUP, group);
        attributes.put(PROP_PERSON, person);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public PersonInGroup(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: position
     */
    public String getPosition() {
        return (String) get_prop(PROP_POSITION);
    }

    public void setPosition(String position) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_POSITION, position);
    }

    /**
     * Property: mailingAddress
     */
    public String getMailingAddress() {
        return (String) get_prop(PROP_MAILINGADDRESS);
    }

    public void setMailingAddress(String mailingAddress) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MAILINGADDRESS, mailingAddress);
    }

    /**
     * Property: deliveryAddress
     */
    public String getDeliveryAddress() {
        return (String) get_prop(PROP_DELIVERYADDRESS);
    }

    public void setDeliveryAddress(String deliveryAddress) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DELIVERYADDRESS, deliveryAddress);
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
     * Property: faxNumber
     */
    public String getFaxNumber() {
        return (String) get_prop(PROP_FAXNUMBER);
    }

    public void setFaxNumber(String faxNumber) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_FAXNUMBER, faxNumber);
    }

    /**
     * Property: startDate
     */
    public Calendar getStartDate() {
        return (Calendar) get_prop(PROP_STARTDATE);
    }

    public void setStartDate(Calendar startDate) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_STARTDATE, startDate);
    }

    /**
     * Property: endDate
     */
    public Calendar getEndDate() {
        return (Calendar) get_prop(PROP_ENDDATE);
    }

    public void setEndDate(Calendar endDate) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ENDDATE, endDate);
    }

    /**
     * Property: phoneNumbers
     */
    public List<String> getPhoneNumbers() {
        return (List<String>) get_prop(PROP_PHONENUMBERS);
    }

    public void setPhoneNumbers(List<String> phoneNumbers) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PHONENUMBERS, phoneNumbers);
    }

    /**
     * Property: group
     */
    public Group getGroup() {
        return (Group) get_prop(PROP_GROUP);
    }

    public void setGroup(Group group) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_GROUP, group);
    }

    /**
     * Property: person
     */
    public Person getPerson() {
        return (Person) get_prop(PROP_PERSON);
    }

    public void setPerson(Person person) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PERSON, person);
    }

    /**
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {
        return this.getClass().getSimpleName() + ":" + this.person.getName() + " - " + this.group.getName();
    }

}
