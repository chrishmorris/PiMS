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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.reference.ImageType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.schedule.ScheduledTask;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "CRYZ_IMAGE")
@org.pimslims.annotation.MetaClass(helpText = "N/A", keyNames = {}, subclasses = {})
public class Image extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_FILEPATH = "filePath";

    public static final String PROP_FILENAME = "fileName";

    public static final String PROP_MIMETYPE = "mimeType";

    public static final String PROP_INSTRUMENT = "instrument";

    public static final String PROP_SAMPLE = "sample";

    public static final String PROP_DROPANNOTATIONS = "dropAnnotations";

    public static final String PROP_PARAMETERS = "cyParameters";

    public static final String PROP_IMAGETYPE = "imageType";

    @Deprecated
    // use ImageType
    public static final String PROP_WELLIMAGETYPE = "wellImageType";

    public static final String PROP_SCHEDULEDTASK = "scheduledTask";

    public static final String PROP_OFFSETX = "offsetX";

    public static final String PROP_OFFSETY = "offsetY";

    public static final String PROP_XLENGTHPERPIXEL = "xLengthPerPixel";

    public static final String PROP_YLENGTHPERPIXEL = "yLengthPerPixel";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "FILEPATH", length = 254, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "N/A", constraints = { "contains_no_linebreak",
        "contains_no_backslash" })
    private String filePath;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "FILENAME", length = 254, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "N/A", constraints = { "contains_no_linebreak",
        "contains_no_backslash" })
    private String fileName;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "MIMETYPE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "N/A", constraints = { "contains_no_linebreak" })
    private String mimeType;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "INSTRUMENTID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "cryz_image_instrument_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = 1, isChangeable = true, reverseRoleName = "images")
    private Instrument instrument;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "SAMPLEID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "cryz_image_sample_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = 1, isChangeable = true, reverseRoleName = "images")
    private Sample sample;

/* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "IMAGETYPEID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "cryz_image_imagetype_inx")
    @org.pimslims.annotation.Role(helpText = "the type of this image", low = 0, high = 1, isChangeable = true, reverseRoleName = "images")
    private ImageType imageType;

/* ------------------------------------------------------------ */
    @Deprecated
    // use ImageType
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "WELLIMAGETYPEID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "cryz_image_wellimagetype_inx")
    @org.pimslims.annotation.Role(helpText = "the type of this image", low = 0, high = 1, isChangeable = true, reverseRoleName = "images")
    private WellImageType wellImageType;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "SCHEDULEDTASKID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "cryz_image_scheduletask_inx")
    @org.pimslims.annotation.Role(helpText = "the task of this image belongs to", low = 0, high = 1, isChangeable = true, reverseRoleName = "images")
    private ScheduledTask scheduledTask;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "image")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "image")
    private final Set<DropAnnotation> dropAnnotations = new HashSet<DropAnnotation>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "image")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "image")
    private final Set<CyParameter> cyParameters = new HashSet<CyParameter>(0);

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "OFFSETX", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "offset of image centre from well centre, in column direction")
    private Float offsetX = new Float(0);

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "OFFSETY", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "offset of image centre from well centre, in row direction")
    private Float offsetY = new Float(0);

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "XLENGTHPERPIXEL", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The width of a pixel in microns")
    private Float xLengthPerPixel;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "YLENGTHPERPIXEL", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The height of a pixel in microns")
    private Float yLengthPerPixel; /* ************************************************************ */

    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Image() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Image(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Image(final WritableVersion wVersion, final java.lang.String filePath,
        final java.lang.String fileName) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_FILEPATH, filePath);
        attributes.put(PROP_FILENAME, fileName);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Image(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: filePath
     */
    public String getFilePath() {
        return (String) get_prop(PROP_FILEPATH);
    }

    public void setFilePath(final String filePath) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_FILEPATH, filePath);
    }

    /**
     * Property: fileName
     */
    public String getFileName() {
        return (String) get_prop(PROP_FILENAME);
    }

    public void setFileName(final String fileName) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_FILENAME, fileName);
    }

    /**
     * Property: mimeType
     */
    public String getMimeType() {
        return (String) get_prop(PROP_MIMETYPE);
    }

    public void setMimeType(final String mimeType) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MIMETYPE, mimeType);
    }

    /**
     * Property: instrument
     */
    public Instrument getInstrument() {
        return (Instrument) get_prop(PROP_INSTRUMENT);
    }

    public void setInstrument(final Instrument instrument) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_INSTRUMENT, instrument);
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
     * Property: imageType
     */
    public ImageType getImageType() {
        return (ImageType) get_prop(PROP_IMAGETYPE);
    }

    public void setImageType(final ImageType imageType) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_IMAGETYPE, imageType);
    }

    /**
     * Property: wellImageType
     */
    @Deprecated
    // use ImageType
    public WellImageType getWellImageType() {
        return (WellImageType) get_prop(PROP_WELLIMAGETYPE);
    }

    @Deprecated
    // use ImageType
    public void setWellImageType(final WellImageType imageType)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_WELLIMAGETYPE, imageType);
    }

    /**
     * Property: scheduledTask
     */
    public ScheduledTask getScheduledTask() {
        return (ScheduledTask) get_prop(PROP_SCHEDULEDTASK);
    }

    public void setScheduledTask(final ScheduledTask scheduledTask)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SCHEDULEDTASK, scheduledTask);
    }

    /**
     * Property: dropAnnotations
     */
    public Set<DropAnnotation> getDropAnnotations() {
        return (Set<DropAnnotation>) get_prop(PROP_DROPANNOTATIONS);
    }

    public void setDropAnnotations(final java.util.Collection<DropAnnotation> dropAnnotations)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DROPANNOTATIONS, dropAnnotations);
    }

    public void addDropAnnotation(final DropAnnotation dropAnnotation)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_DROPANNOTATIONS, dropAnnotation);
    }

    public void removeDropAnnotation(final DropAnnotation dropAnnotation)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_DROPANNOTATIONS, dropAnnotation);
    }

    /**
     * Property: parameters
     */
    public Set<CyParameter> getCyParameters() {
        return (Set<CyParameter>) get_prop(PROP_PARAMETERS);
    }

    public void setCyParameters(final java.util.Collection<CyParameter> parameters)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PARAMETERS, parameters);
    }

    public void addParameter(final CyParameter parameter) throws org.pimslims.exception.ConstraintException {
        add(PROP_PARAMETERS, parameter);
    }

    public void removeParameter(final CyParameter parameter)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_PARAMETERS, parameter);
    }

    /**
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {

        return this.fileName;
    }

    /**
     * Image.getOffsetX
     * 
     * @return
     */
    public Float getOffsetX() {
        return this.offsetX;
    }

    /**
     * Image.getOffsetX
     * 
     * @return
     */
    public Float getOffsetY() {
        return this.offsetY;
    }

    /**
     * Image.setOffsetX
     * 
     * @param f
     * @throws ConstraintException
     */
    public void setOffsetX(float f) throws ConstraintException {
        set_prop(PROP_OFFSETX, f);
    }

    /**
     * Image.setOffsetX
     * 
     * @param f
     * @throws ConstraintException
     */
    public void setOffsetY(float f) throws ConstraintException {
        set_prop(PROP_OFFSETY, f);
    }

    public void setXLengthPerPixel(float f) throws ConstraintException {
        set_prop(PROP_XLENGTHPERPIXEL, f);
    }

    public void setYLengthPerPixel(float f) throws ConstraintException {
        set_prop(PROP_YLENGTHPERPIXEL, f);
    }

    /**
     * .Image.getXlengthPerPixel
     * 
     * @return
     * @see org.pimslims.model.crystallization.WellImageType#getXlengthPerPixel()
     */
    public Float getXLengthPerPixel() {
        if (null != this.xLengthPerPixel) {
            return this.xLengthPerPixel;
        }
        if (null == this.wellImageType) {
            return null;
        }
        return this.wellImageType.getXlengthPerPixel();
    }

    /**
     * .Image.getYlengthPerPixel
     * 
     * @return
     * @see org.pimslims.model.crystallization.WellImageType#getYlengthPerPixel()
     */
    public Float getYLengthPerPixel() {
        if (null != this.yLengthPerPixel) {
            return this.yLengthPerPixel;
        }
        if (null == this.wellImageType) {
            return null;
        }
        return this.wellImageType.getYlengthPerPixel();
    }

}
