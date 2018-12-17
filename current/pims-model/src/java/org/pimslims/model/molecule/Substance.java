package org.pimslims.model.molecule;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.reference.Organism;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "MOLE_ABSTRACTCOMPONENT", uniqueConstraints = {})
@org.pimslims.annotation.MetaClass(helpText = "The abstract component is used to define the details which characterise all components, these are name, any synonyms and a CAS number. There are 4 subclasses of abstract component: Molecular component (MolComponent), Cell, Substance and Composite.", keyNames = { "NAME" }, subclasses = {
    org.pimslims.model.molecule.Molecule.class, org.pimslims.model.molecule.Host.class })
public abstract class Substance extends org.pimslims.model.core.LabBookEntry implements
    java.lang.Comparable, java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_SYNONYMS = "synonyms";

    public static final String PROP_KEYWORDS = "keywords";

    public static final String PROP_NATURALSOURCE = "naturalSource";

    public static final String PROP_CATEGORIES = "categories";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The name of the component. Unique identifier.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @org.hibernate.annotations.CollectionOfElements
    @JoinTable(name = "MOLE_ABSTCO_SYNONYMS", joinColumns = @JoinColumn(name = "ABSTRACTCOMPONENTID"))
    @org.hibernate.annotations.IndexColumn(name = "ORDER_", base = 0)
    @Column(name = "SYNONYM_")
    @org.pimslims.annotation.Attribute(helpText = "The synonyms of the component.")
    private final List<String> synonyms = new ArrayList<String>(0);

    /* ------------------------------------------------------------ */
    @org.hibernate.annotations.CollectionOfElements
    @JoinTable(name = "MOLE_ABSTCO_KEYWORDS", joinColumns = @JoinColumn(name = "ABSTRACTCOMPONENTID"))
    @org.hibernate.annotations.IndexColumn(name = "ORDER_", base = 0)
    @Column(name = "KEYWORD")
    @org.pimslims.annotation.Attribute(helpText = "N/A")
    private final List<String> keywords = new ArrayList<String>(0);

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "NATURALSOURCEID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "mole_abstractcomponent_ns_inx")
    @org.pimslims.annotation.Role(helpText = "The natural source of the component.", low = 0, high = 1, isChangeable = true, reverseRoleName = "Unresolved")
    private Organism naturalSource;

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "MOLE_COMPCA2COMPONENTS", inverseJoinColumns = { @JoinColumn(name = "CATEGORYID", nullable = false, updatable = false) }, joinColumns = { @JoinColumn(name = "COMPONENTID", nullable = false, updatable = false) })
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @org.pimslims.annotation.Role(helpText = "List of category associated to a component.", low = 0, high = -1, isChangeable = true, reverseRoleName = "Unresolved")
    private final Set<ComponentCategory> categories = new HashSet<ComponentCategory>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Substance() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Substance(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Substance(WritableVersion wVersion, java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Substance(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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
     * Property: synonyms
     */
    public List<String> getSynonyms() {
        return (List<String>) get_prop(PROP_SYNONYMS);
    }

    public void setSynonyms(List<String> synonyms) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SYNONYMS, synonyms);
    }

    /**
     * Property: keywords
     */
    public List<String> getKeywords() {
        return (List<String>) get_prop(PROP_KEYWORDS);
    }

    public void setKeywords(List<String> keywords) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_KEYWORDS, keywords);
    }

    /**
     * Property: naturalSource
     */
    public Organism getNaturalSource() {
        return (Organism) get_prop(PROP_NATURALSOURCE);
    }

    public void setNaturalSource(Organism naturalSource) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_NATURALSOURCE, naturalSource);
    }

    /**
     * Property: categories
     */
    public Set<ComponentCategory> getCategories() {
        return (Set<ComponentCategory>) get_prop(PROP_CATEGORIES);
    }

    public void setCategories(java.util.Collection<ComponentCategory> categories)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CATEGORIES, categories);
    }

    public void addCategory(org.pimslims.model.reference.ComponentCategory category)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_CATEGORIES, category);
    }

    public void removeCategory(org.pimslims.model.reference.ComponentCategory category)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_CATEGORIES, category);
    }

}
