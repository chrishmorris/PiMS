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

import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "CRYZ_PARAMETER")
@org.pimslims.annotation.MetaClass(helpText = "The value of a parameter for a given experiment.", keyNames = {}, subclasses = {}, parent = org.pimslims.model.crystallization.Image.class, parentRoleName = "image")
public class CyParameter extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_VALUE = "value";

    public static final String PROP_IMAGE = "image";

    public static final String PROP_PARAMETERDEFINITION = "cyParameterDefinition";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "VALUE", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The value of a parameter.")
    private String value;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "IMAGEID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "cryz_parameter_image_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "cyParameters")
    private Image image;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PARAMETERDEFINITIONID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "cryz_parameter_pd_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "cyParameters")
    private CyParameterDefinition cyParameterDefinition;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected CyParameter() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected CyParameter(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public CyParameter(WritableVersion wVersion,
        org.pimslims.model.crystallization.CyParameterDefinition parameterDefinition,
        org.pimslims.model.crystallization.Image image) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_IMAGE, image);
        attributes.put(PROP_PARAMETERDEFINITION, parameterDefinition);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public CyParameter(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: value
     */
    public String getValue() {
        return (String) get_prop(PROP_VALUE);
    }

    public void setValue(String value) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_VALUE, value);
    }

    /**
     * Property: image
     */
    public Image getImage() {
        return (Image) get_prop(PROP_IMAGE);
    }

    public void setImage(Image image) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_IMAGE, image);
    }

    /**
     * Property: parameterDefinition
     */
    public CyParameterDefinition getCyParameterDefinition() {
        return (CyParameterDefinition) get_prop(PROP_PARAMETERDEFINITION);
    }

    public void setCyParameterDefinition(CyParameterDefinition parameterDefinition)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PARAMETERDEFINITION, parameterDefinition);
    }

    /**
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {

        return this.cyParameterDefinition.getName();
    }

}
