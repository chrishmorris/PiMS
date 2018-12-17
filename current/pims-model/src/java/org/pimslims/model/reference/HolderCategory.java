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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "PublicEntryID")
@Table(name = "REF_HOLDERCATEGORY", uniqueConstraints = {})
@org.pimslims.annotation.MetaClass(orderBy = "name", helpText = "The category of the holder.", keyNames = { "NAME" }, subclasses = {}, parentRoleName = "project")
public class HolderCategory extends org.pimslims.model.reference.PublicEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_ABSTRACTHOLDERTYPES = "abstractHolderTypes";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The unique name.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "hold_holdca2absthoty", joinColumns = { @JoinColumn(name = "HOLDERCATEGORYID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ABSTRACTHOLDERTYPEID", nullable = false, updatable = false) })
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "holderCategories")
    private final Set<AbstractHolderType> abstractHolderTypes = new HashSet<AbstractHolderType>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected HolderCategory() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected HolderCategory(final WritableVersion wVersion)
        throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public HolderCategory(final WritableVersion wVersion, final java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put(PROP_NAME, name);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public HolderCategory(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
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
     * Property: abstractHolderTypes
     */
    public Set<AbstractHolderType> getAbstractHolderTypes() {
        return (Set<AbstractHolderType>) get_prop(PROP_ABSTRACTHOLDERTYPES);
    }

    public void setAbstractHolderTypes(final java.util.Collection<AbstractHolderType> abstractHolderTypes)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ABSTRACTHOLDERTYPES, abstractHolderTypes);
    }

    public void addAbstractHolderType(final org.pimslims.model.reference.AbstractHolderType abstractHolderType)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_ABSTRACTHOLDERTYPES, abstractHolderType);
    }

    public void removeAbstractHolderType(
        final org.pimslims.model.reference.AbstractHolderType abstractHolderType)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_ABSTRACTHOLDERTYPES, abstractHolderType);
    }

}
