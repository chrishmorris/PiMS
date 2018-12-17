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

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "CRYZ_SCORE", uniqueConstraints = { @UniqueConstraint(columnNames = { "VALUE",
    "SCORINGSCHEMEID" }) })
@org.pimslims.annotation.MetaClass(helpText = "N/A", keyNames = { "VALUE", "SCORINGSCHEME" }, subclasses = {}, parent = org.pimslims.model.crystallization.ScoringScheme.class, parentRoleName = "scoringScheme")
public class Score extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_COLOR = "color";

    public static final String PROP_NAME = "name";

    public static final String PROP_VALUE = "value";

    public static final String PROP_SCORINGSCHEME = "scoringScheme";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "color", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The color to display.")
    private String color;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "N/A", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "VALUE", columnDefinition = "INTEGER", unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "N/A")
    private Integer value;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SCORINGSCHEMEID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "cryz_score_scoringscheme_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "scores")
    private ScoringScheme scoringScheme;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Score() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Score(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Score(final WritableVersion wVersion, final java.lang.Integer value,
        final org.pimslims.model.crystallization.ScoringScheme scoringScheme)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_VALUE, value);
        attributes.put(PROP_SCORINGSCHEME, scoringScheme);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Score(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: color
     */
    public String getColor() {
        return (String) get_prop(PROP_COLOR);
    }

    public void setColor(final String color) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_COLOR, color);
    }

    /**
     * Property: name
     */
    @Override
    public String getName() {
        if (this.name == null)
            return "";
        return this.name;
    }

    public void setName(final String name) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_NAME, name);
    }

    /**
     * Property: value
     */
    public Integer getValue() {
        return (Integer) get_prop(PROP_VALUE);
    }

    public void setValue(final Integer value) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_VALUE, value);
    }

    /**
     * Property: scoringScheme
     */
    public ScoringScheme getScoringScheme() {
        return (ScoringScheme) get_prop(PROP_SCORINGSCHEME);
    }

    public void setScoringScheme(final ScoringScheme scoringScheme)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SCORINGSCHEME, scoringScheme);
    }

}
