package org.pimslims.model.molecule;

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
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.people.Organisation;

/**
 * @author Anne Pajon
 * @date Jun 11, 2009
 * 
 *       Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 3.2
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

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "SUBSTANCEID")
@Table(name = "MOLE_HOST", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(helpText = "One type of component. Hosts are used in Culture experiments, and the intended host(s) is an important factor in choice of vector.", keyNames = {}, subclasses = {})
public class Host extends Substance implements java.lang.Comparable, java.io.Serializable {
    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_STRAIN = "strain";

    public static final String PROP_GENOTYPE = "genotype";

    public static final String PROP_HARBOUREDPLASMIDS = "harbouredPlasmids";

    public static final String PROP_ANTIBIOTICRESISTANCES = "antibioticResistances";

    public static final String PROP_SELECTABLEMARKERS = "selectableMarkers";

    public static final String PROP_USE = "use";

    public static final String PROP_COMMENTS = "comments";

    public static final String PROP_SUPPLIERS = "suppliers";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "STRAIN", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The host starin.", constraints = { "contains_no_linebreak" })
    private String strain;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "GENOTYPE", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The genotype.")
    private String genotype;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "HARBOUREDPLASMIDS", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The names of the harboured plasmids.")
    private String harbouredPlasmids;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ANTIBIOTICRESISTANCES", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The names of the antibiotic resistances.")
    private String antibioticResistances;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "SELECTABLEMARKERS", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The names of the selectable markers.")
    private String selectableMarkers;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "USE", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The use of this host.")
    private String use;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#comment")
    @Column(name = "COMMENTS", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Some space for comments.")
    private String comments;

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "MOLE_HOST2ORGANISATION", joinColumns = { @JoinColumn(name = "HOSTID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ORGANISATIONID", nullable = false, updatable = false) })
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @org.pimslims.annotation.Role(helpText = "The list of suppliers.", low = 0, high = -1, isChangeable = true, reverseRoleName = "hosts")
    private final Set<Organisation> suppliers = new HashSet<Organisation>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Host() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Host(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Host(final WritableVersion wVersion, final java.lang.String molType, final java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Host(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */

    /**
     * @return Returns the strain.
     */
    public String getStrain() {
        return (String) get_prop(PROP_STRAIN);
    }

    /**
     * @param strain The strain to set.
     * @throws ConstraintException
     */
    public void setStrain(final String strain) throws ConstraintException {
        set_prop(PROP_STRAIN, strain);
    }

    /**
     * @return Returns the genotype.
     */
    public String getGenotype() {
        return (String) get_prop(PROP_GENOTYPE);
    }

    /**
     * @param genotype The genotype to set.
     * @throws ConstraintException
     */
    public void setGenotype(final String genotype) throws ConstraintException {
        set_prop(PROP_GENOTYPE, genotype);
    }

    /**
     * @return Returns the harbouredPlasmids.
     */
    public String getHarbouredPlasmids() {
        return (String) get_prop(PROP_HARBOUREDPLASMIDS);
    }

    /**
     * @param harbouredPlasmids The harbouredPlasmids to set.
     * @throws ConstraintException
     */
    public void setHarbouredPlasmids(final String harbouredPlasmids) throws ConstraintException {
        set_prop(PROP_HARBOUREDPLASMIDS, harbouredPlasmids);
    }

    /**
     * @return Returns the antibioticResistances.
     */
    public String getAntibioticResistances() {
        return (String) get_prop(PROP_ANTIBIOTICRESISTANCES);
    }

    /**
     * @param antibioticResistances The antibioticResistances to set.
     * @throws ConstraintException
     */
    public void setAntibioticResistances(final String antibioticResistances) throws ConstraintException {
        set_prop(PROP_ANTIBIOTICRESISTANCES, antibioticResistances);
    }

    /**
     * @return Returns the selectableMarkers.
     */
    public String getSelectableMarkers() {
        return (String) get_prop(PROP_SELECTABLEMARKERS);
    }

    /**
     * @param selectableMarkers The selectableMarkers to set.
     * @throws ConstraintException
     */
    public void setSelectableMarkers(final String selectableMarkers) throws ConstraintException {
        set_prop(PROP_SELECTABLEMARKERS, selectableMarkers);
    }

    /**
     * @return Returns the use.
     */
    public String getUse() {
        return (String) get_prop(PROP_USE);
    }

    /**
     * @param use The use to set.
     * @throws ConstraintException
     */
    public void setUse(final String use) throws ConstraintException {
        set_prop(PROP_USE, use);
    }

    /**
     * @return Returns the comments.
     */
    public String getComments() {
        return (String) get_prop(PROP_COMMENTS);
    }

    /**
     * @param comments The comments to set.
     * @throws ConstraintException
     */
    public void setComments(final String comments) throws ConstraintException {
        set_prop(PROP_COMMENTS, comments);
    }

    /**
     * Property: suppliers
     */
    public Set<Organisation> getSuppliers() {
        return (Set<Organisation>) get_prop(PROP_SUPPLIERS);
    }

    public void setSuppliers(final java.util.Collection<Organisation> suppliers)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SUPPLIERS, suppliers);
    }

    public void addSupplier(final Organisation supplier) throws org.pimslims.exception.ConstraintException {
        add(PROP_SUPPLIERS, supplier);
    }

    public void removeSupplier(final Organisation supplier) throws org.pimslims.exception.ConstraintException {
        remove(PROP_SUPPLIERS, supplier);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */

}
