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
@PrimaryKeyJoinColumn(name = "ABSTRACTHOLDERTYPEID")
@Table(name = "REF_PINTYPE", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(orderBy = "name", helpText = "The type of pin for a crystal container.", keyNames = {}, subclasses = {})
public class PinType extends org.pimslims.model.reference.AbstractHolderType implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_LENGTH = "length";

    public static final String PROP_LOOPTYPE = "loopType";

    public static final String PROP_LOOPLENGTH = "loopLength";

    public static final String PROP_WIREWIDTH = "wireWidth";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "LENGTH", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The length of pin + hat, in millimeter.")
    private Float length;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "LOOPTYPE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The type of the loop.", constraints = { "contains_no_linebreak" })
    private String loopType;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "LOOPLENGTH", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The length of the loop containing the Crystal, in millimeter.")
    private Float loopLength;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "WIREWIDTH", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The width of the loop wire, in micrometer.")
    private Float wireWidth;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected PinType() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected PinType(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public PinType(WritableVersion wVersion, java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put(PROP_NAME, name);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public PinType(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: length
     */
    public Float getLength() {
        return (Float) get_prop(PROP_LENGTH);
    }

    public void setLength(Float length) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_LENGTH, length);
    }

    /**
     * Property: loopType
     */
    public String getLoopType() {
        return (String) get_prop(PROP_LOOPTYPE);
    }

    public void setLoopType(String loopType) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_LOOPTYPE, loopType);
    }

    /**
     * Property: loopLength
     */
    public Float getLoopLength() {
        return (Float) get_prop(PROP_LOOPLENGTH);
    }

    public void setLoopLength(Float loopLength) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_LOOPLENGTH, loopLength);
    }

    /**
     * Property: wireWidth
     */
    public Float getWireWidth() {
        return (Float) get_prop(PROP_WIREWIDTH);
    }

    public void setWireWidth(Float wireWidth) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_WIREWIDTH, wireWidth);
    }

    /**
     * ContainerType.getDimension
     * 
     * @see org.pimslims.model.reference.ContainerType#getDimension()
     */
    public int getDimension() {
        return 0;
    }

}
