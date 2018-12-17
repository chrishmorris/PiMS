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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "CITATIONID")
@Table(name = "CORE_THESISCITATION", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(helpText = "Citation (litterature reference) to  a Thesis.", keyNames = {}, subclasses = {})
public class ThesisCitation extends org.pimslims.model.core.Citation implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_INSTITUTION = "institution";
    public static final String PROP_CITY = "city";
    public static final String PROP_STATEPROVINCE = "stateProvince";
    public static final String PROP_COUNTRY = "country";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "INSTITUTION", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Institution where thesis is submitted")
    private String institution;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CITY", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "City where institution is located", constraints = { "contains_no_linebreak" })
    private String city;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "STATEPROVINCE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "State or province where institution is located", constraints = { "contains_no_linebreak" })
    private String stateProvince;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "COUNTRY", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Country where institution is located", constraints = { "contains_no_linebreak" })
    private String country;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected ThesisCitation() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected ThesisCitation(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public ThesisCitation(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: institution
     */
    public String getInstitution() {
        return (String) get_prop(PROP_INSTITUTION);
    }

    public void setInstitution(String institution) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_INSTITUTION, institution);
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

}
