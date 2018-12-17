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

import java.util.Collection;
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
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;

@Entity
@org.pimslims.annotation.EquivalentClass("http://purl.uniprot.org/core/Molecule")
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "SUBSTANCEID")
@Table(name = "MOLE_MOLECULE", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(helpText = "One type of component. A molecule represents a pure component of known melecular structure. This includes most chemicals, proteins, DNA, RNA, DNA/RNA and also enzymes, constructs and primers. Restriction enzymes, primers and constructs are subtypes of Molecule with additional features recorded. NaCl is a Molecule, so is lysozyme.", keyNames = {}, subclasses = {
    org.pimslims.model.molecule.Construct.class, org.pimslims.model.molecule.Primer.class,
    org.pimslims.model.molecule.Extension.class })
public class Molecule extends org.pimslims.model.molecule.Substance implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_MOLTYPE = "molType";

    public static final String PROP_CAS_NUMBER = "casNum";

    @Deprecated
    // renamed to Cas Number 
    public static final String PROP_CASNUM = "casNum";

    public static final String PROP_EMPIRICALFORMULA = "empiricalFormula";

    public static final String PROP_ISVOLATILE = "isVolatile";

    public static final String PROP_MOLECULARMASS = "molecularMass";

    public static final String PROP_SEQUENCE_DETAILS = "seqDetails";

    @Deprecated
    // renamed to Sequence Details     
    public static final String PROP_SEQDETAILS = "seqDetails";

    public static final String PROP_SEQUENCE = "seqString";

    @Deprecated
    // renamed as Sequence 
    public static final String PROP_SEQSTRING = "seqString";

    public static final String PROP_MOLECULEFEATURES = "moleculeFeatures";

    public static final String PROP_PROTEIN_FOR_TARGETS = "protTargets";

    @Deprecated
    // use PROP_PROTEIN_FOR_TARGETS
    public static final String PROP_PROTTARGETS = "protTargets";

    public static final String PROP_REFMOLECULEFEATURES = "refMoleculeFeatures";

    public static final String PROP_DNA_FOR_TARGETS = "nucTargets";

    @Deprecated
    // use PROP_DNA_FOR_TARGETS
    public static final String PROP_NUCTARGETS = "nucTargets";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "MOLTYPE", length = 80, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "Type of component, on the general lines of biopolymer/polymer/nonpolymer. See MolComponentType DataType for details", constraints = { "value_must_be_one_of('protein', 'DNA', 'RNA', 'DNA/RNA', 'carbohydrate', 'other')" })
    private String molType;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CASNUM", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "CAS registry number (http://www.cas.org/EO/regsys.html).", constraints = {
        "contains_no_linebreak", "no_white_space" })
    private String casNum;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "EMPIRICALFORMULA", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The molecular formula of the molComponent.", constraints = { "contains_no_linebreak" })
    private String empiricalFormula;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ISVOLATILE", columnDefinition = "BOOLEAN", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "True if the component is volatile.")
    private Boolean isVolatile;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "MOLECULARMASS", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The experimental molecular mass in dalton (Da).")
    private Float molecularMass;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "SEQDETAILS", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "N/A")
    private String seqDetails;

    /* ------------------------------------------------------------ */
    @Basic(optional = true, fetch = FetchType.LAZY)
    @Column(name = "SEQSTRING", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Sequence of the Molecule.")
    private String seqString;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "molecule")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "molecule")
    private Set<MoleculeFeature> moleculeFeatures = new HashSet<MoleculeFeature>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "protein")
    @org.pimslims.annotation.Role(helpText = "The Target associated to the Molecule that defines it", low = 0, high = -1, isChangeable = true, reverseRoleName = "protein")
    private Set<Target> protTargets = new HashSet<Target>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "refMolecule")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "refMolecule")
    private Set<MoleculeFeature> refMoleculeFeatures = new HashSet<MoleculeFeature>(0);

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "nucleicAcids")
    @org.pimslims.annotation.Role(helpText = "The Target associated other MolComponent that define it", low = 0, high = -1, isChangeable = true, reverseRoleName = "nucleicAcids")
    private Set<Target> nucTargets = new HashSet<Target>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Molecule() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Molecule(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Molecule(WritableVersion wVersion, java.lang.String molType, java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_MOLTYPE, molType);
        attributes.put(PROP_NAME, name);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Molecule(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: molType
     */
    public String getMolType() {
        return (String) get_prop(PROP_MOLTYPE);
    }

    public static final String PROTEIN = "protein";

    public static final String DNA = "DNA";

    public void setMolType(String molType) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MOLTYPE, molType);
    }

    /**
     * Property: casNum
     */
    public String getCasNumber() {
        return (String) get_prop(PROP_CAS_NUMBER);
    }

    public void setCasNumber(String casNum) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CAS_NUMBER, casNum);
    }

    @Deprecated
    // renamed to Cas Number
    public String getCasNum() {
        return (String) get_prop(PROP_CAS_NUMBER);
    }

    @Deprecated
    // renamed to Cas Number
    public void setCasNum(String casNum) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CAS_NUMBER, casNum);
    }

    /**
     * Property: empiricalFormula
     */
    public String getEmpiricalFormula() {
        return (String) get_prop(PROP_EMPIRICALFORMULA);
    }

    public void setEmpiricalFormula(String empiricalFormula)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_EMPIRICALFORMULA, empiricalFormula);
    }

    /**
     * Property: isVolatile
     */
    public Boolean getIsVolatile() {
        return (Boolean) get_prop(PROP_ISVOLATILE);
    }

    public void setIsVolatile(Boolean isVolatile) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ISVOLATILE, isVolatile);
    }

    /**
     * Property: molecularMass
     */
    public Float getMolecularMass() {
        return (Float) get_prop(PROP_MOLECULARMASS);
    }

    public void setMolecularMass(Float molecularMass) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MOLECULARMASS, molecularMass);
    }

    /**
     * Property: seqDetails
     */
    public String getSequenceDetails() {
        return (String) get_prop(PROP_SEQUENCE_DETAILS);
    }

    public void setSequenceDetails(String seqDetails) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SEQUENCE_DETAILS, seqDetails);
    }

    @Deprecated
    // renamed to Sequence Details
    public String getSeqDetails() {
        return (String) get_prop(PROP_SEQUENCE_DETAILS);
    }

    @Deprecated
    // renamed to Sequence Details
    public void setSeqDetails(String seqDetails) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SEQUENCE_DETAILS, seqDetails);
    }

    /**
     * Property: seqString
     */
    public String getSequence() {
        return (String) get_prop(PROP_SEQUENCE);
    }

    public void setSequence(String seqString) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SEQUENCE, seqString);
    }

    @Deprecated
    // renamed as Sequence
    public String getSeqString() {
        return (String) get_prop(PROP_SEQUENCE);
    }

    @Deprecated
    // renamed as Sequence
    public void setSeqString(String seqString) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SEQUENCE, seqString);
    }

    /**
     * Property: moleculeFeatures
     */
    public Set<MoleculeFeature> getMoleculeFeatures() {
        return (Set<MoleculeFeature>) get_prop(PROP_MOLECULEFEATURES);
    }

    public void setMoleculeFeatures(java.util.Collection<MoleculeFeature> moleculeFeatures)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MOLECULEFEATURES, moleculeFeatures);
    }

    public void addMoleculeFeature(MoleculeFeature moleculeFeature)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_MOLECULEFEATURES, moleculeFeature);
    }

    public void removeMoleculeFeature(MoleculeFeature moleculeFeature)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_MOLECULEFEATURES, moleculeFeature);
    }

    /**
     * Property: protTargets
     */
    public Set<Target> getProteinForTargets() {
        return (Set<Target>) get_prop(PROP_PROTEIN_FOR_TARGETS);
    }

    public void setProteinForTargets(java.util.Collection<Target> protTargets)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PROTEIN_FOR_TARGETS, protTargets);
    }

    @Deprecated
    // renamed
    public Set<Target> getProtTargets() {
        return (Set<Target>) get_prop(PROP_PROTEIN_FOR_TARGETS);
    }

    @Deprecated
    // renamed
    public void setProtTargets(java.util.Collection<Target> protTargets)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PROTEIN_FOR_TARGETS, protTargets);
    }

    @Deprecated
    // renamed
    public void addProtTarget(Target protTarget) throws org.pimslims.exception.ConstraintException {
        add(PROP_PROTEIN_FOR_TARGETS, protTarget);
    }

    @Deprecated
    // renamed
    public void removeProtTarget(Target protTarget) throws org.pimslims.exception.ConstraintException {
        remove(PROP_PROTEIN_FOR_TARGETS, protTarget);
    }

    /**
     * Property: refMoleculeFeatures
     */
    public Set<MoleculeFeature> getRefMoleculeFeatures() {
        return (Set<MoleculeFeature>) get_prop(PROP_REFMOLECULEFEATURES);
    }

    public void setRefMoleculeFeatures(java.util.Collection<MoleculeFeature> refMoleculeFeatures)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_REFMOLECULEFEATURES, refMoleculeFeatures);
    }

    public void addRefMoleculeFeature(MoleculeFeature refMoleculeFeature)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_REFMOLECULEFEATURES, refMoleculeFeature);
    }

    public void removeRefMoleculeFeature(MoleculeFeature refMoleculeFeature)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_REFMOLECULEFEATURES, refMoleculeFeature);
    }

    /**
     * Property: nucTargets
     */
    public Set<Target> getDnaForTargets() {
        return (Set<Target>) get_prop(PROP_DNA_FOR_TARGETS);
    }

    public void setDnaForTargets(java.util.Collection<Target> nucTargets)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DNA_FOR_TARGETS, nucTargets);
    }

    @Deprecated
    // renamed
    public Set<Target> getNucTargets() {
        return (Set<Target>) get_prop(PROP_DNA_FOR_TARGETS);
    }

    @Deprecated
    // renamed
    public void setNucTargets(java.util.Collection<Target> nucTargets)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DNA_FOR_TARGETS, nucTargets);
    }

    @Deprecated
    // renamed
    public void addNucTarget(org.pimslims.model.target.Target nucTarget)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_DNA_FOR_TARGETS, nucTarget);
    }

    @Deprecated
    // renamed
    public void removeNucTarget(org.pimslims.model.target.Target nucTarget)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_DNA_FOR_TARGETS, nucTarget);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */
    /**
     * Property: researchObjectiveElements
     */
    public Collection<ResearchObjectiveElement> getResearchObjectiveElements() {
        return this.get_Version().findAll(ResearchObjectiveElement.class,
            ResearchObjectiveElement.PROP_MOLECULE, this);
    }

    /**
     * Property: relatedResearchObjectiveElements
     */
    public Collection<ResearchObjectiveElement> getRelatedResearchObjectiveElements() {
        return this.get_Version().findAll(ResearchObjectiveElement.class,
            ResearchObjectiveElement.PROP_TRIALMOLECULES, this);
    }

    public void addRelatedResearchObjectiveElement(
        org.pimslims.model.target.ResearchObjectiveElement relatedResearchObjectiveElement)
        throws org.pimslims.exception.ConstraintException {
        relatedResearchObjectiveElement.addTrialMolecule(this);
    }

}
