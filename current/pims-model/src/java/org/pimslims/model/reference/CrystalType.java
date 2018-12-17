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
import org.pimslims.exception.ConstraintException;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "HOLDERTYPEID")
@Table(name = "REF_CRYSTALTYPE", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(orderBy = "name", helpText = "The type of a crystal container which has one reservor sub-position.", keyNames = {}, subclasses = {})
public class CrystalType extends org.pimslims.model.reference.HolderType implements java.lang.Comparable,
    java.io.Serializable {

    /**
     * NO_RESERVOIR int
     */
    public static final int NO_RESERVOIR = 100;

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_RESSUBPOSITION = "resSubPosition";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "ResSubPos", columnDefinition = "INTEGER", unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The sub-position of reservor in the holder. The first position should be 1.", constraints = { "int_value_gt_0" })
    private Integer resSubPosition;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected CrystalType() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected CrystalType(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public CrystalType(WritableVersion wVersion, java.lang.Integer resSubPosition, java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put(PROP_RESSUBPOSITION, resSubPosition);
        attributes.put(PROP_NAME, name);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public CrystalType(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: resSubPosition The subposition number that represents the reservoir. If none does, then
     * NO_RESERVOIR or null.
     */
    public Integer getResSubPosition() {
        return (Integer) get_prop(PROP_RESSUBPOSITION);
    }

    public void setResSubPosition(Integer resSubPosition) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_RESSUBPOSITION, resSubPosition);
    }

    public boolean hasReservoir() {
        return this.getResSubPosition() == this.getMaxSubPosition();
    }

    public void setHasReservoir(boolean reservoir) throws ConstraintException {
        if (reservoir) {
            this.setResSubPosition(this.getMaxSubPosition());
        } else {
            this.setResSubPosition(NO_RESERVOIR);
        }
    }

    /**
     * CrystalType.getSubpositions
     * 
     * @return
     */
    public int[] getSubpositions() {
        int count = this.getMaxSubPosition() - (this.hasReservoir() ? 1 : 0);
        int[] ret = new int[count];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = i;
        }
        return ret;
    }

}
