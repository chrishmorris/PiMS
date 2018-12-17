package org.pimslims.model.reference;

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
@PrimaryKeyJoinColumn(name = "PublicEntryID")
@Table(name = "REF_HAZARDPHRASE", uniqueConstraints = { @UniqueConstraint(columnNames = { "CLASSIFICATION",
    "CODE" }) })
@org.pimslims.annotation.MetaClass(orderBy = "code", helpText = "Phrases required for describing the hazard properties of a sample.", keyNames = {
    "CLASSIFICATION", "CODE" }, subclasses = {}, parentRoleName = "project")
public class HazardPhrase extends org.pimslims.model.reference.PublicEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_CLASSIFICATION = "classification";
    public static final String PROP_CODE = "code";
    public static final String PROP_PHRASE = "phrase";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "CLASSIFICATION", length = 32, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The classification to which the phrase belongs, e.g. MSDS or local", constraints = { "value_must_be_one_of('MSDS', 'local')" })
    private String classification;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "CODE", length = 80, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The code associated to the phrase, e.g. 'S3/7'", constraints = { "contains_no_linebreak" })
    private String code;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PHRASE", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The phrase, e.g. 'Keep container tightly closed in a cool place.'")
    private String phrase;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected HazardPhrase() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected HazardPhrase(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public HazardPhrase(WritableVersion wVersion, java.lang.String classification, java.lang.String code)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put(PROP_CLASSIFICATION, classification);
        attributes.put(PROP_CODE, code);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public HazardPhrase(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: classification
     */
    public String getClassification() {
        return (String) get_prop(PROP_CLASSIFICATION);
    }

    public void setClassification(String classification) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CLASSIFICATION, classification);
    }

    /**
     * Property: code
     */
    public String getCode() {
        return (String) get_prop(PROP_CODE);
    }

    public void setCode(String code) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CODE, code);
    }

    /**
     * Property: phrase
     */
    public String getPhrase() {
        return (String) get_prop(PROP_PHRASE);
    }

    public void setPhrase(String phrase) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PHRASE, phrase);
    }

    /**
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {
        return code;
    }

}
