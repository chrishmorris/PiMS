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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "PublicEntryID")
@Table(name = "REF_ABSTRACTHOLDERTYPE", uniqueConstraints = {})
@org.pimslims.annotation.MetaClass(orderBy = "name", helpText = "The reference information for an holder.", keyNames = { "NAME" }, subclasses = {
    org.pimslims.model.reference.PinType.class, org.pimslims.model.reference.HolderType.class })
public abstract class AbstractHolderType extends org.pimslims.model.reference.PublicEntry implements
    java.lang.Comparable, java.io.Serializable, ContainerType {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_DEFAULTHOLDERCATEGORIES = "defaultHolderCategories";

    public static final String PROP_HOLDERTYPESOURCES = "holderTypeSources";

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
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "abstractHolderTypes")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "abstractHolderTypes")
    private final Set<HolderCategory> defaultHolderCategories = new HashSet<HolderCategory>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "holderType")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "holderType")
    private Set<HolderTypeSource> holderTypeSources = new HashSet<HolderTypeSource>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected AbstractHolderType() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected AbstractHolderType(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public AbstractHolderType(WritableVersion wVersion, java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put(PROP_NAME, name);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public AbstractHolderType(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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
     * Property: defaultHolderCategories
     */
    public Set<HolderCategory> getDefaultHolderCategories() {
        return (Set<HolderCategory>) get_prop(PROP_DEFAULTHOLDERCATEGORIES);
    }

    public void setDefaultHolderCategories(java.util.Collection<HolderCategory> defaultHolderCategories)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DEFAULTHOLDERCATEGORIES, defaultHolderCategories);
    }

    public void addDefaultHolderCategory(org.pimslims.model.reference.HolderCategory defaultHolderCategorie)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_DEFAULTHOLDERCATEGORIES, defaultHolderCategorie);
    }

    public void removeDefaultHolderCategory(org.pimslims.model.reference.HolderCategory defaultHolderCategorie)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_DEFAULTHOLDERCATEGORIES, defaultHolderCategorie);
    }

    /**
     * Property: holderTypeSources
     */
    public Set<HolderTypeSource> getHolderTypeSources() {
        return (Set<HolderTypeSource>) get_prop(PROP_HOLDERTYPESOURCES);
    }

    public void setHolderTypeSources(java.util.Collection<HolderTypeSource> holderTypeSources)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_HOLDERTYPESOURCES, holderTypeSources);
    }

    public void addRefSampleSource(HolderTypeSource holderTypeSource)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_HOLDERTYPESOURCES, holderTypeSource);
    }

    public void removeRefSampleSource(HolderTypeSource holderTypeSource)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_HOLDERTYPESOURCES, holderTypeSource);
    }

}
