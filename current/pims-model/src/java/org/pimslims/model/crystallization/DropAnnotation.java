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

import java.util.Calendar;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;
import org.pimslims.model.experiment.Software;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.sample.Sample;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "CRYZ_DROPANNOTATION", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(helpText = "N/A", keyNames = {}, subclasses = {})
public class DropAnnotation extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_CMDLINEPARAM = "cmdLineParam";

    public static final String PROP_SCOREDATE = "scoreDate";

    public static final String PROP_IMAGE = "image";

    public static final String PROP_SAMPLE = "sample";

    public static final String PROP_SCORE = "score";

    public static final String PROP_SOFTWARE = "software";

    public static final String PROP_HOLDER = "holder";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CMDLINEPARAM", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "N/A")
    private String cmdLineParam;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "SCOREDATE", columnDefinition = "TIMESTAMPTZ", unique = false, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @org.pimslims.annotation.Attribute(helpText = "N/A")
    private Calendar scoreDate;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "IMAGEID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "cryz_dropannotation_image_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = 1, isChangeable = true, reverseRoleName = "dropAnnotations")
    private Image image;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "SAMPLEID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "cryz_dropannotation_sample_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = 1, isChangeable = true, reverseRoleName = "dropAnnotations")
    private Sample sample;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SCOREID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "cryz_dropannotation_score_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "dropAnnotations")
    private Score score;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "SOFTWAREID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "cryz_dropannotation_s_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = 1, isChangeable = true, reverseRoleName = "dropAnnotations")
    private Software software;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "HOLDERID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "cryz_dropannotation_holder_inx")
    @org.pimslims.annotation.Role(helpText = "The holder of this dropAnnotation belongs to.", low = 0, high = 1, isChangeable = true, reverseRoleName = "dropAnnotations")
    private Holder holder;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected DropAnnotation() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected DropAnnotation(final WritableVersion wVersion)
        throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public DropAnnotation(final WritableVersion wVersion, final org.pimslims.model.crystallization.Score score)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_SCORE, score);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public DropAnnotation(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: cmdLineParam
     */
    public String getCmdLineParam() {
        return (String) get_prop(PROP_CMDLINEPARAM);
    }

    public void setCmdLineParam(final String cmdLineParam) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CMDLINEPARAM, cmdLineParam);
    }

    /**
     * Property: scoreDate
     */
    public Calendar getScoreDate() {
        return (Calendar) get_prop(PROP_SCOREDATE);
    }

    public void setScoreDate(final Calendar scoreDate) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SCOREDATE, scoreDate);
    }

    /**
     * Property: image
     */
    public Image getImage() {
        return (Image) get_prop(PROP_IMAGE);
    }

    public void setImage(final Image image) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_IMAGE, image);
    }

    /**
     * Property: sample
     */
    public Sample getSample() {
        return (Sample) get_prop(PROP_SAMPLE);
    }

    public void setSample(final Sample sample) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SAMPLE, sample);
    }

    /**
     * Property: score
     */
    public Score getScore() {
        return (Score) get_prop(PROP_SCORE);
    }

    public void setScore(final Score score) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SCORE, score);
    }

    /**
     * Property: software
     */
    public Software getSoftware() {
        return (Software) get_prop(PROP_SOFTWARE);
    }

    public void setSoftware(final Software software) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SOFTWARE, software);
    }

    /**
     * Property: holder
     */
    public Holder getHolder() {
        return (Holder) get_prop(PROP_HOLDER);
    }

    public void setHolder(final Holder holder) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_HOLDER, holder);
    }

    /**
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {
        return this.get_Hook();
    }

}
