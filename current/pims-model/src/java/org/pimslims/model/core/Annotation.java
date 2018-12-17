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

import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "ATTACHMENTID")
@Table(name = "CORE_ANNOTATION")
@org.pimslims.annotation.MetaClass(helpText = "Annotation to store file and a description associate to it.", keyNames = {}, subclasses = {})
public class Annotation extends org.pimslims.model.core.Attachment implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_FILENAME = "filename";

    public static final String PROP_MIMETYPE = "mimeType";

    public static final String PROP_TITLE = "title";

    public static final String PROP_LEGEND = "legend";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The name of the annotation.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "FILENAME", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The file name of the annotation.", constraints = {
        "contains_no_linebreak", "contains_no_backslash" })
    private String filename;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "MIMETYPE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The mime type of the annotation.", constraints = { "contains_no_linebreak" })
    private String mimeType;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "TITLE", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The title of the annotation.", constraints = { "contains_no_linebreak" })
    private String title;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "LEGEND", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The legend associated to the annotation.")
    private String legend;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Annotation() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Annotation(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Annotation(WritableVersion wVersion, org.pimslims.model.core.LabBookEntry parent)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put(PROP_PARENTENTRY, parent);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Annotation(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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
     * Property: filename
     */
    public String getFilename() {
        return (String) get_prop(PROP_FILENAME);
    }

    public void setFilename(String filename) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_FILENAME, filename);
    }

    /**
     * Property: mimeType
     */
    public String getMimeType() {
        return (String) get_prop(PROP_MIMETYPE);
    }

    public void setMimeType(String mimeType) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MIMETYPE, mimeType);
    }

    /**
     * Property: title
     */
    public String getTitle() {
        return (String) get_prop(PROP_TITLE);
    }

    public void setTitle(String title) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_TITLE, title);
    }

    /**
     * Property: legend
     */
    public String getLegend() {
        return (String) get_prop(PROP_LEGEND);
    }

    public void setLegend(String legend) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_LEGEND, legend);
    }

}
