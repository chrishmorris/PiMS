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

import org.pimslims.annotation.Attribute;
import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "MOLE_MOLECULEFEATURE")
@org.pimslims.annotation.MetaClass(helpText = "The MolCompFeature is used to assign information to each part of the MolComponent sequence, as defined by a series of MolResidues with consecutive serials.", keyNames = {}, subclasses = {}, parent = org.pimslims.model.molecule.Molecule.class, parentRoleName = "molecule")
public class MoleculeFeature extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_FEATURETYPE = "featureType";

    public static final String PROP_STATUS = "status";

    public static final String PROP_STARTSEQID = "startSeqId";

    public static final String PROP_ENDSEQID = "endSeqId";

    public static final String PROP_ORDERING = "ordering";

    public static final String PROP_MOLECULE = "molecule";

    public static final String PROP_REFMOLECULE = "refMolecule";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The name of the feature.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "FEATURETYPE", length = 80, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The type of the feature e.g. 'tag', 'cleavage site', 'promoter', 'resistance', 'marker', 'product',...", constraints = { "contains_no_linebreak" })
    private String featureType;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "STATUS", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The status of the feature e.g. cut, uncut,...", constraints = { "contains_no_linebreak" })
    private String status;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "STARTSEQID", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Residue number (MolResidue.serial) of the start sequence in the MolComponent.")
    private Integer startSeqId;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ENDSEQID", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Residue number (MolResidue.serial) of the end sequence in the MolComponent.")
    private Integer endSeqId;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ORDERING", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Integer defining the ordering of MolCompFeatures. For when startSeqId and endSeqId are not known.")
    private Integer ordering;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MOLECULEID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "mole_mf_molecule_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "moleculeFeatures")
    private Molecule molecule;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "REFMOLECULEID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "mole_mf_refmolecule_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = 1, isChangeable = true, reverseRoleName = "refMoleculeFeatures")
    private Molecule refMolecule;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected MoleculeFeature() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected MoleculeFeature(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public MoleculeFeature(WritableVersion wVersion, java.lang.String name, java.lang.String featureType,
        org.pimslims.model.molecule.Molecule molComponent) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);
        attributes.put(PROP_FEATURETYPE, featureType);
        attributes.put(PROP_MOLECULE, molComponent);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public MoleculeFeature(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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
     * Property: featureType
     */
    public String getFeatureType() {
        return (String) get_prop(PROP_FEATURETYPE);
    }

    public void setFeatureType(String featureType) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_FEATURETYPE, featureType);
    }

    /**
     * Property: status
     */
    public String getStatus() {
        return (String) get_prop(PROP_STATUS);
    }

    public void setStatus(String status) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_STATUS, status);
    }

    /**
     * Property: startSeqId
     */
    public Integer getStartSeqId() {
        return (Integer) get_prop(PROP_STARTSEQID);
    }

    public void setStartSeqId(Integer startSeqId) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_STARTSEQID, startSeqId);
    }

    /**
     * Property: endSeqId
     */
    public Integer getEndSeqId() {
        return (Integer) get_prop(PROP_ENDSEQID);
    }

    public void setEndSeqId(Integer endSeqId) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ENDSEQID, endSeqId);
    }

    /**
     * Property: ordering
     */
    public Integer getOrdering() {
        return (Integer) get_prop(PROP_ORDERING);
    }

    public void setOrdering(Integer ordering) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ORDERING, ordering);
    }

    /**
     * Property: molecule
     */
    public Molecule getMolecule() {
        return (Molecule) get_prop(PROP_MOLECULE);
    }

    public void setMolecule(Molecule molecule) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MOLECULE, molecule);
    }

    /**
     * Property: refMolecule
     */
    public Molecule getRefMolecule() {
        return (Molecule) get_prop(PROP_REFMOLECULE);
    }

    public void setRefMolecule(Molecule refMolecule) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_REFMOLECULE, refMolecule);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */
    /**
     * Derived method: getLength()
     */
    @Attribute(helpText = "The sequence length of the feature.")
    public java.lang.Integer getLength() {
        if ((getStartSeqId() == null) || (getEndSeqId() == null)) {
            return new java.lang.Integer(0);
        }
        return new java.lang.Integer(getEndSeqId().intValue() - getStartSeqId().intValue() + 1);
    }

}
