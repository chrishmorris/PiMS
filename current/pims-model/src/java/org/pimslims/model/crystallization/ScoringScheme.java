package org.pimslims.model.crystallization;

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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "CRYZ_SCORINGSCHEME", uniqueConstraints = {})
@org.pimslims.annotation.MetaClass(helpText = "N/A", keyNames = { "NAME" }, subclasses = {}, parentRoleName = "project")
public class ScoringScheme extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_VERSION = "version";

    public static final String PROP_SCORES = "scores";

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
    @Column(name = "VERSION", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "N/A", constraints = { "contains_no_linebreak" })
    private String version;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "scoringScheme")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "scoringScheme")
    private Set<Score> scores = new HashSet<Score>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected ScoringScheme() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected ScoringScheme(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public ScoringScheme(WritableVersion wVersion, java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public ScoringScheme(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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
     * Property: version
     */
    public String getVersion() {
        return (String) get_prop(PROP_VERSION);
    }

    public void setVersion(String version) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_VERSION, version);
    }

    /**
     * Property: scores
     */
    public Set<Score> getScores() {
        return (Set<Score>) get_prop(PROP_SCORES);
    }

    public void setScores(java.util.Collection<Score> scores)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SCORES, scores);
    }

    public void addScore(Score score) throws org.pimslims.exception.ConstraintException {
        add(PROP_SCORES, score);
    }

    public void removeScore(Score score) throws org.pimslims.exception.ConstraintException {
        remove(PROP_SCORES, score);
    }

}
