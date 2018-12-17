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

import java.util.Calendar;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "CITATIONID")
@Table(name = "CORE_CONFERENCECITATION", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(helpText = "Citation (litterature reference) to Conference presentation", keyNames = {}, subclasses = {})
public class ConferenceCitation extends org.pimslims.model.core.Citation implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_CONFERENCETITLE = "conferenceTitle";

    public static final String PROP_CONFERENCESITE = "conferenceSite";

    public static final String PROP_CITY = "city";

    public static final String PROP_STATEPROVINCE = "stateProvince";

    public static final String PROP_COUNTRY = "country";

    public static final String PROP_STARTDATE = "startDate";

    public static final String PROP_ENDDATE = "endDate";

    public static final String PROP_ABSTRACTNUMBER = "abstractNumber";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CONFERENCETITLE", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Title of cnference")
    private String conferenceTitle;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CONFERENCESITE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Location of conference", constraints = { "contains_no_linebreak" })
    private String conferenceSite;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CITY", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "City where conference is held", constraints = { "contains_no_linebreak" })
    private String city;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "STATEPROVINCE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "State or provinde where conference is held", constraints = { "contains_no_linebreak" })
    private String stateProvince;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "COUNTRY", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Country where conference is held", constraints = { "contains_no_linebreak" })
    private String country;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "STARTDATE", columnDefinition = "TIMESTAMPTZ", unique = false, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @org.pimslims.annotation.Attribute(helpText = "Starting date of conference")
    private Calendar startDate;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ENDDATE", columnDefinition = "TIMESTAMPTZ", unique = false, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @org.pimslims.annotation.Attribute(helpText = "Finishing date of conference")
    private Calendar endDate;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ABSTRACTNUMBER", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Abstract 'number' (not an integer) of presentation.", constraints = { "contains_no_linebreak" })
    private String abstractNumber;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected ConferenceCitation() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected ConferenceCitation(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public ConferenceCitation(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: conferenceTitle
     */
    public String getConferenceTitle() {
        return (String) get_prop(PROP_CONFERENCETITLE);
    }

    public void setConferenceTitle(String conferenceTitle) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CONFERENCETITLE, conferenceTitle);
    }

    /**
     * Property: conferenceSite
     */
    public String getConferenceSite() {
        return (String) get_prop(PROP_CONFERENCESITE);
    }

    public void setConferenceSite(String conferenceSite) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CONFERENCESITE, conferenceSite);
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
     * Property: stateProvince
     */
    public String getStateProvince() {
        return (String) get_prop(PROP_STATEPROVINCE);
    }

    public void setStateProvince(String stateProvince) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_STATEPROVINCE, stateProvince);
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
     * Property: abstractNumber
     */
    public String getAbstractNumber() {
        return (String) get_prop(PROP_ABSTRACTNUMBER);
    }

    public void setAbstractNumber(String abstractNumber) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ABSTRACTNUMBER, abstractNumber);
    }

}
