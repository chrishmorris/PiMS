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

@org.pimslims.annotation.EquivalentClass("http://purl.uniprot.org/core/Journal_Citation")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "CITATIONID")
@Table(name = "CORE_JOURNALCITATION", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(helpText = "Citation (litterature reference) to Journal article", keyNames = {}, subclasses = {})
public class JournalCitation extends org.pimslims.model.core.Citation implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_JOURNALABBREVIATION = "journalAbbreviation";

    public static final String PROP_JOURNALFULLNAME = "journalFullName";

    public static final String PROP_VOLUME = "volume";

    public static final String PROP_ISSUE = "issue";

    public static final String PROP_ASTM = "astm";

    public static final String PROP_ISSN = "issn";

    public static final String PROP_CSD = "csd";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "JOURNALABBREVIATION", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Journal abbreviation according to ACS standard abbreviations", constraints = { "contains_no_linebreak" })
    private String journalAbbreviation;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "JOURNALFULLNAME", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Full name of Journal")
    private String journalFullName;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "VOLUME", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Journal volume 'number' (not an integer)", constraints = { "contains_no_linebreak" })
    private String volume;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ISSUE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Journal issue 'number' (not an integer)", constraints = { "contains_no_linebreak" })
    private String issue;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ASTM", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "ASTM code .", constraints = { "contains_no_linebreak" })
    private String astm;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ISSN", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "ISSN code", constraints = { "contains_no_linebreak" })
    private String issn;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CSD", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "CSD code", constraints = { "contains_no_linebreak" })
    private String csd;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected JournalCitation() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected JournalCitation(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public JournalCitation(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: journalAbbreviation
     */
    public String getJournalAbbreviation() {
        return (String) get_prop(PROP_JOURNALABBREVIATION);
    }

    public void setJournalAbbreviation(String journalAbbreviation)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_JOURNALABBREVIATION, journalAbbreviation);
    }

    /**
     * Property: journalFullName
     */
    public String getJournalFullName() {
        return (String) get_prop(PROP_JOURNALFULLNAME);
    }

    public void setJournalFullName(String journalFullName) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_JOURNALFULLNAME, journalFullName);
    }

    /**
     * Property: volume
     */
    public String getVolume() {
        return (String) get_prop(PROP_VOLUME);
    }

    public void setVolume(String volume) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_VOLUME, volume);
    }

    /**
     * Property: issue
     */
    public String getIssue() {
        return (String) get_prop(PROP_ISSUE);
    }

    public void setIssue(String issue) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ISSUE, issue);
    }

    /**
     * Property: astm
     */
    public String getAstm() {
        return (String) get_prop(PROP_ASTM);
    }

    public void setAstm(String astm) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ASTM, astm);
    }

    /**
     * Property: issn
     */
    public String getIssn() {
        return (String) get_prop(PROP_ISSN);
    }

    public void setIssn(String issn) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ISSN, issn);
    }

    /**
     * Property: csd
     */
    public String getCsd() {
        return (String) get_prop(PROP_CSD);
    }

    public void setCsd(String csd) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CSD, csd);
    }

}
