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
import org.pimslims.model.sample.RefSample;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "HOLD_REFSAMPLEPOSITION", uniqueConstraints = { @UniqueConstraint(columnNames = {
    "ROWPOSITION", "COLPOSITION", "SUBPOSITION", "REFHOLDERID" }) })
@org.pimslims.annotation.MetaClass(helpText = "N/A", keyNames = { "ROWPOSITION", "COLPOSITION",
    "SUBPOSITION", "REFHOLDER" }, subclasses = {}, parent = org.pimslims.model.holder.RefHolder.class, parentRoleName = "refHolder")
public class RefSamplePosition extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_ROWPOSITION = "rowPosition";
    public static final String PROP_COLPOSITION = "colPosition";
    public static final String PROP_SUBPOSITION = "subPosition";
    public static final String PROP_REFHOLDER = "refHolder";
    public static final String PROP_REFSAMPLE = "refSample";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ROWPOSITION", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The row position of the refSample in the holder. The first position should be 1.", constraints = { "int_value_gt_0" })
    private Integer rowPosition;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "COLPOSITION", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The column position of the refSample in the holder. The first position should be 1.", constraints = { "int_value_gt_0" })
    private Integer colPosition;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "SUBPOSITION", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The sub-position of the refSample in the holder. The first position should be 1.", constraints = { "int_value_gt_0" })
    private Integer subPosition;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "RefHOLDERID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name="hold_rsp_refholder_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "refSamplePositions")
    private RefHolder refHolder;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "REFSAMPLEID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name="hold_rsp_refsample_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = 1, isChangeable = true, reverseRoleName = "refSamplePositions")
    private RefSample refSample;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected RefSamplePosition() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected RefSamplePosition(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public RefSamplePosition(WritableVersion wVersion, org.pimslims.model.holder.RefHolder refHolder)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_REFHOLDER, refHolder);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public RefSamplePosition(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: rowPosition
     */
    public Integer getRowPosition() {
        return (Integer) get_prop(PROP_ROWPOSITION);
    }

    public void setRowPosition(Integer rowPosition) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ROWPOSITION, rowPosition);
    }

    /**
     * Property: colPosition
     */
    public Integer getColPosition() {
        return (Integer) get_prop(PROP_COLPOSITION);
    }

    public void setColPosition(Integer colPosition) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_COLPOSITION, colPosition);
    }

    /**
     * Property: subPosition
     */
    public Integer getSubPosition() {
        return (Integer) get_prop(PROP_SUBPOSITION);
    }

    public void setSubPosition(Integer subPosition) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SUBPOSITION, subPosition);
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
     * Property: refSample
     */
    public RefSample getRefSample() {
        return (RefSample) get_prop(PROP_REFSAMPLE);
    }

    public void setRefSample(RefSample refSample) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_REFSAMPLE, refSample);
    }

    /**
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {

        return this.get_Hook();
    }

}
