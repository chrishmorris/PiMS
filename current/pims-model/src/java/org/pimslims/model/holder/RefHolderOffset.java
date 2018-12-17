package org.pimslims.model.holder;

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
@Table(name = "HOLD_REFHOLDEROFFSET", uniqueConstraints = { @UniqueConstraint(columnNames = { "ROWOFFSET",
    "COLOFFSET", "SUBOFFSET", "HOLDERID" }) })
@org.pimslims.annotation.MetaClass(helpText = "A holder will confirm to a refholder in certain position through this class.", keyNames = {
    "ROWOFFSET", "COLOFFSET", "SUBOFFSET", "HOLDER" }, subclasses = {}, parent = org.pimslims.model.holder.Holder.class, parentRoleName = "holder")
public class RefHolderOffset extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_ROWOFFSET = "rowOffset";
    public static final String PROP_COLOFFSET = "colOffset";
    public static final String PROP_SUBOFFSET = "subOffset";
    public static final String PROP_REFHOLDER = "refHolder";
    public static final String PROP_HOLDER = "holder";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ROWOffset", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The row offset of the refholder in the holder. The first position should be 1.")
    private Integer rowOffset;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "COLOffset", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The column offset of the refholder in the holder. The first position should be 1.")
    private Integer colOffset;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "SUBOffset", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The sub-position offset of the refholder in the holder. The first position should be 1.")
    private Integer subOffset;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REFHOLDERID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name="hold_rho_refholder_inx")
    @org.pimslims.annotation.Role(helpText = "The refholder which this offset linked.", low = 1, high = 1, isChangeable = true, reverseRoleName = "refHolderOffsets")
    private RefHolder refHolder;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "HOLDERID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name="hold_rho_holder_inx")
    @org.pimslims.annotation.Role(helpText = "The holder which this offset linked.", low = 1, high = 1, isChangeable = true, reverseRoleName = "refHolderOffsets")
    private Holder holder;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected RefHolderOffset() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected RefHolderOffset(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public RefHolderOffset(WritableVersion wVersion, org.pimslims.model.holder.RefHolder refHolder,
        org.pimslims.model.holder.Holder holder) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_REFHOLDER, refHolder);
        attributes.put(PROP_HOLDER, holder);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public RefHolderOffset(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: rowOffset
     */
    public Integer getRowOffset() {
        return (Integer) get_prop(PROP_ROWOFFSET);
    }

    public void setRowOffset(Integer rowOffset) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ROWOFFSET, rowOffset);
    }

    /**
     * Property: colOffset
     */
    public Integer getColOffset() {
        return (Integer) get_prop(PROP_COLOFFSET);
    }

    public void setColOffset(Integer colOffset) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_COLOFFSET, colOffset);
    }

    /**
     * Property: subOffset
     */
    public Integer getSubOffset() {
        return (Integer) get_prop(PROP_SUBOFFSET);
    }

    public void setSubOffset(Integer subOffset) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SUBOFFSET, subOffset);
    }

    /**
     * Property: refHolder
     */
    public RefHolder getRefHolder() {
        return (RefHolder) get_prop(PROP_REFHOLDER);
    }

    public void setRefHolder(RefHolder refHolder) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_REFHOLDER, refHolder);
    }

    /**
     * Property: holder
     */
    public Holder getHolder() {
        return (Holder) get_prop(PROP_HOLDER);
    }

    public void setHolder(Holder holder) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_HOLDER, holder);
    }

    /**
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {

        return this.getClass().getSimpleName() + ":" + refHolder.getName() + " - " + holder.getName();
    }

}
