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

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.crystallization.Image;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "PublicEntryID")
@Table(name = "REF_IMAGETYPE", uniqueConstraints = {})
@org.pimslims.annotation.MetaClass(orderBy = "name", helpText = "The type of Image", keyNames = { "NAME" }, subclasses = {})
public class ImageType extends org.pimslims.model.reference.PublicEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_XLENGTHPERPIXEL = "xlengthPerPixel";

    public static final String PROP_YLENGTHPERPIXEL = "ylengthPerPixel";

    public static final String PROP_SIZEX = "sizeX";

    public static final String PROP_SIZEY = "sizeY";

    public static final String PROP_COLOURDEPTH = "colourDepth";

    public static final String PROP_URL = "url";

    public static final String PROP_CATORGORY = "catorgory";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label")
    @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The unique name.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "XLENGTHPERPIXEL", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The length per pixel of image's X .")
    private Float xlengthPerPixel;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "YLENGTHPERPIXEL", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The length per pixel of image's Y .")
    private Float ylengthPerPixel;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "SIZEX", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The the width of image.")
    private Integer sizeX;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "SIZEY", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The the height of image.")
    private Integer sizeY;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "COLOURDEPTH", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The the colour depth of image.")
    private Integer colourDepth;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "URL", length = 180, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "the default url for its image. eg: http://www.oppf.ox.ac.uk/vault/images/lowres/.", constraints = { "contains_no_linebreak" })
    private String url;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CATORGORY", length = 180, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "type of image, eg:COMPOSITE, SLICE, ZOOMED", constraints = { "contains_no_linebreak" })
    private String catorgory;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected ImageType() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected ImageType(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public ImageType(final WritableVersion wVersion, final java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put(PROP_NAME, name);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public ImageType(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
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

    public void setName(final String name) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_NAME, name);
    }

    /**
     * @return Returns the xlengthPerPixel.
     */
    public Float getXlengthPerPixel() {
        return (Float) get_prop(PROP_XLENGTHPERPIXEL);
    }

    /**
     * @param xlengthPerPixel The xlengthPerPixel to set.
     * @throws ConstraintException
     */
    public void setXlengthPerPixel(final Float xlengthPerPixel) throws ConstraintException {
        set_prop(PROP_XLENGTHPERPIXEL, xlengthPerPixel);
    }

    /**
     * @return Returns the ylengthPerPixel.
     */
    public Float getYlengthPerPixel() {
        return (Float) get_prop(PROP_YLENGTHPERPIXEL);
    }

    /**
     * @param ylengthPerPixel The ylengthPerPixel to set.
     * @throws ConstraintException
     */
    public void setYlengthPerPixel(final Float ylengthPerPixel) throws ConstraintException {
        set_prop(PROP_YLENGTHPERPIXEL, ylengthPerPixel);
    }

    /**
     * @return Returns the sizeX.
     */
    public Integer getSizeX() {
        return (Integer) get_prop(PROP_SIZEX);
    }

    /**
     * @param sizeX The sizeX to set.
     * @throws ConstraintException
     */
    public void setSizeX(final Integer sizeX) throws ConstraintException {
        set_prop(PROP_SIZEX, sizeX);
    }

    /**
     * @return Returns the sizeY.
     */
    public Integer getSizeY() {
        return (Integer) get_prop(PROP_SIZEY);
    }

    /**
     * @param sizeY The sizeY to set.
     * @throws ConstraintException
     */
    public void setSizeY(final Integer sizeY) throws ConstraintException {
        set_prop(PROP_SIZEY, sizeY);
    }

    /**
     * @return Returns the colourDepth.
     */
    public Integer getColourDepth() {
        return (Integer) get_prop(PROP_COLOURDEPTH);
    }

    /**
     * @param colourDepth The colourDepth to set.
     * @throws ConstraintException
     */
    public void setColourDepth(final Integer colourDepth) throws ConstraintException {
        set_prop(PROP_COLOURDEPTH, colourDepth);
    }

    /**
     * @return Returns the url.
     * 
     */
    public String getUrl() {
        return (String) get_prop(PROP_URL);
    }

    /**
     * @param url The url to set.
     * @throws ConstraintException
     */
    public void setUrl(final String url) throws ConstraintException {
        set_prop(PROP_URL, url);
    }

    /**
     * @return Returns the catorgory.
     */
    public String getCatorgory() {
        return (String) get_prop(PROP_CATORGORY);
    }

    /**
     * @param catorgory The catorgory to set.
     * @throws ConstraintException
     */
    public void setCatorgory(final String catorgory) throws ConstraintException {
        set_prop(PROP_CATORGORY, catorgory);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */
    /**
     * Property: experiments
     */
    public Collection<Image> getImages() {
        return this.get_Version().findAll(Image.class, Image.PROP_IMAGETYPE, this);
    }

}
