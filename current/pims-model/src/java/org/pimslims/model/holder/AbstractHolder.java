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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.reference.AbstractHolderType;
import org.pimslims.model.reference.HolderCategory;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "HOLD_ABSTRACTHOLDER", uniqueConstraints = {})
@org.pimslims.annotation.MetaClass(helpText = "Abstract Holder which could be a Holder or Ref-Holder.", keyNames = { "NAME" }, subclasses = {
    org.pimslims.model.holder.Holder.class, org.pimslims.model.holder.RefHolder.class })
public abstract class AbstractHolder extends org.pimslims.model.core.LabBookEntry implements
    java.lang.Comparable, java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_ROWPOSITION = "rowPosition";

    public static final String PROP_COLPOSITION = "colPosition";

    public static final String PROP_SUBPOSITION = "subPosition";

    public static final String PROP_HOLDERTYPE = "holderType";

    public static final String PROP_HOLDERCATEGORIES = "holderCategories";

    public static final String PROP_SUPHOLDER = "parentHolder";

    public static final String PROP_SUBHOLDERS = "subHolders";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The name of the holder or the code to identify it. It is the unique identifier.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ROWPOSITION", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The row position of the current holder in the parent holder. The first position should be 1.", constraints = { "int_value_gt_0" })
    private Integer rowPosition;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "COLPOSITION", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The column position of the current holder in the parent holder. The first position should be 1.", constraints = { "int_value_gt_0" })
    private Integer colPosition;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "SUBPOSITION", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The sub-position of the current holder in the parent holder. The first position should be 1.", constraints = { "int_value_gt_0" })
    private Integer subPosition;

    /* ------------------------------------------------------------ */
    //
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "HOLDERTYPEID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "hold_abstractholder_ht_inx")
    @org.pimslims.annotation.Role(helpText = "The type of holder associated to a holder.", low = 0, high = 1, isChangeable = true, reverseRoleName = "Unresolved")
    private AbstractHolderType holderType;

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "hold_holdca2abstholders", joinColumns = { @JoinColumn(name = "ABSTHOLDERID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "HOLDERCATEGORYID", nullable = false, updatable = false) })
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "abstractHolders")
    private final Set<HolderCategory> holderCategories = new HashSet<HolderCategory>(0);

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "ABSTRACTHOLDERID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "hold_abstractholder_p_inx")
    @org.pimslims.annotation.Role(helpText = "The holder to which the sub holders belong.", low = 0, high = 1, isChangeable = true, reverseRoleName = "subHolders")
    private AbstractHolder parentHolder;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentHolder")
    @org.pimslims.annotation.Role(helpText = "List of sub holder contained in an holder. This is the contents of an holder.", low = 0, high = -1, isChangeable = true, reverseRoleName = "parentHolder")
    private final Set<AbstractHolder> subHolders = new HashSet<AbstractHolder>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected AbstractHolder() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected AbstractHolder(final WritableVersion wVersion)
        throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public AbstractHolder(final WritableVersion wVersion, final java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public AbstractHolder(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
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
     * Property: rowPosition
     */
    public Integer getRowPosition() {
        return (Integer) get_prop(PROP_ROWPOSITION);
    }

    public void setRowPosition(final Integer rowPosition) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ROWPOSITION, rowPosition);
    }

    /**
     * Property: colPosition
     */
    public Integer getColPosition() {
        return (Integer) get_prop(PROP_COLPOSITION);
    }

    public void setColPosition(final Integer colPosition) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_COLPOSITION, colPosition);
    }

    /**
     * Property: subPosition
     */
    public Integer getSubPosition() {
        return (Integer) get_prop(PROP_SUBPOSITION);
    }

    public void setSubPosition(final Integer subPosition) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SUBPOSITION, subPosition);
    }

    /**
     * Property: holderType
     */
    public AbstractHolderType getHolderType() {
        return (AbstractHolderType) get_prop(PROP_HOLDERTYPE);
    }

    public void setHolderType(final AbstractHolderType holderType)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_HOLDERTYPE, holderType);
    }

    /**
     * Property: holderCategories
     */
    public Set<HolderCategory> getHolderCategories() {
        return (Set<HolderCategory>) get_prop(PROP_HOLDERCATEGORIES);
    }

    public void setHolderCategories(final java.util.Collection<HolderCategory> holderCategories)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_HOLDERCATEGORIES, holderCategories);
    }

    public void addHolderCategory(final org.pimslims.model.reference.HolderCategory holderCategory)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_HOLDERCATEGORIES, holderCategory);
    }

    public void removeHolderCategory(final org.pimslims.model.reference.HolderCategory holderCategory)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_HOLDERCATEGORIES, holderCategory);
    }

    /**
     * Property: supHolder
     */
    public AbstractHolder getParentHolder() {
        return (AbstractHolder) get_prop(PROP_SUPHOLDER);
    }

    public void setParentHolder(final AbstractHolder supHolder)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SUPHOLDER, supHolder);
    }

    /**
     * Property: subHolders
     */
    public Set<AbstractHolder> getSubHolders() {
        return (Set<AbstractHolder>) get_prop(PROP_SUBHOLDERS);
    }

    public void setSubHolders(final java.util.Collection<AbstractHolder> subHolders)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SUBHOLDERS, subHolders);
    }

    public void addSubHolder(final AbstractHolder subHolder)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_SUBHOLDERS, subHolder);
    }

    public void removeSubHolder(final AbstractHolder subHolder)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_SUBHOLDERS, subHolder);
    }

}
