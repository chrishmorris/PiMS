package org.pimslims.model.target;

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
import org.pimslims.model.experiment.Project;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.sample.SampleComponent;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "TRAG_RESEARCHOBJECTIVEELEMENT")
@org.pimslims.annotation.MetaClass(helpText = "The elements of the research objective e.g. list of targets, part of targets or small molecules.", keyNames = {}, subclasses = {}, parent = org.pimslims.model.target.ResearchObjective.class, parentRoleName = "researchObjective")
public class ResearchObjectiveElement extends org.pimslims.model.core.LabBookEntry implements
    java.lang.Comparable, java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_COMPONENTTYPE = "componentType";

    public static final String PROP_STATUS = "status";

    public static final String PROP_WHYCHOSEN = "whyChosen";

    public static final String PROP_ALWAYSINCLUDED = "alwaysIncluded";

    public static final String PROP_DOMAIN = "domain";

    public static final String PROP_APPROXBEGINSEQID = "approxBeginSeqId";

    public static final String PROP_APPROXENDSEQID = "approxEndSeqId";

    @Deprecated
    // use PROP_EXPRESSIONOBJECTIVE
    public static final String PROP_RESEARCHOBJECTIVE = "researchObjective";

    public static final String PROP_EXPRESSIONOBJECTIVE = "researchObjective";

    public static final String PROP_MOLECULE = "molecule";

    public static final String PROP_TARGET = "target";

    public static final String PROP_SAMPLECOMPONENTS = "sampleComponents";

    public static final String PROP_TRIALMOLECULES = "trialMolecules";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "COMPONENTTYPE", length = 32, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "Type of the blueprint component. e.g. 'PolyBlueprintComp' or 'NonPolyBlueprintComp'", constraints = {
        "contains_no_linebreak", "no_white_space" })
    private String componentType;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "STATUS", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The status of the blueprint component.", constraints = { "contains_no_linebreak" })
    private String status;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "WHYCHOSEN", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Details of the basis for the selection of this BlueprintComponent.")
    private String whyChosen;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ALWAYSINCLUDED", columnDefinition = "BOOLEAN", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Is this NonPolyBlueprintComp included all times in the experiments?")
    private Boolean alwaysIncluded;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "DOMAIN", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The name of the target domain used to define the BlueprintComponent.", constraints = { "contains_no_linebreak" })
    private String domain;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "APPROXBEGINSEQID", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The approxBeginSeqId is the approximative residue number that gives the residue begining position of the domain in the target sequence.")
    private Integer approxBeginSeqId;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "APPROXENDSEQID", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The approxEndSeqId is the approximative residue number that gives the residue ending position of the domain in the target sequence.")
    private Integer approxEndSeqId;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "RESEARCHOBJECTIVEID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "targ_roe_researchobjective_inx")
    @org.pimslims.annotation.Role(helpText = "The trial to which this element belongs.", low = 1, high = 1, isChangeable = true, reverseRoleName = "researchObjectiveElements")
    private ResearchObjective researchObjective;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "MOLECULEID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "targ_roe_molecule_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = 1, isChangeable = true, reverseRoleName = "Unresolved")
    private Molecule molecule;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "TARGETID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "targ_roe_target_inx")
    @org.pimslims.annotation.Role(helpText = "The target associated to the research objective element.", low = 0, high = 1, isChangeable = true, reverseRoleName = "researchObjectiveElements")
    private Target target;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "researchObjectiveElement")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "researchObjectiveElement")
    private final Set<SampleComponent> sampleComponents = new HashSet<SampleComponent>(0);

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "MOLE_MOLECULE2RELAREOBEL", inverseJoinColumns = { @JoinColumn(name = "TRIALMOLECULEID", nullable = false, updatable = false) }, joinColumns = { @JoinColumn(name = "RELATEDROELEMENTID", nullable = false, updatable = false) })
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "Unresolved")
    private final Set<Molecule> trialMolecules = new HashSet<Molecule>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected ResearchObjectiveElement() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected ResearchObjectiveElement(final WritableVersion wVersion)
        throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public ResearchObjectiveElement(final WritableVersion wVersion, final java.lang.String componentType,
        final java.lang.String whyChosen, final Project researchObjective)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_COMPONENTTYPE, componentType);
        attributes.put(PROP_WHYCHOSEN, whyChosen);
        attributes.put(PROP_RESEARCHOBJECTIVE, researchObjective);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public ResearchObjectiveElement(final WritableVersion wVersion,
        final java.util.Map<String, Object> attributes) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: componentType
     */
    public String getComponentType() {
        return (String) get_prop(PROP_COMPONENTTYPE);
    }

    public void setComponentType(final String componentType)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_COMPONENTTYPE, componentType);
    }

    /**
     * Property: status
     */
    public String getStatus() {
        return (String) get_prop(PROP_STATUS);
    }

    public void setStatus(final String status) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_STATUS, status);
    }

    /**
     * Property: whyChosen
     */
    public String getWhyChosen() {
        return (String) get_prop(PROP_WHYCHOSEN);
    }

    public void setWhyChosen(final String whyChosen) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_WHYCHOSEN, whyChosen);
    }

    /**
     * Property: alwaysIncluded
     */
    public Boolean getAlwaysIncluded() {
        return (Boolean) get_prop(PROP_ALWAYSINCLUDED);
    }

    public void setAlwaysIncluded(final Boolean alwaysIncluded)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ALWAYSINCLUDED, alwaysIncluded);
    }

    /**
     * Property: domain
     */
    public String getDomain() {
        return (String) get_prop(PROP_DOMAIN);
    }

    public void setDomain(final String domain) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DOMAIN, domain);
    }

    /**
     * Property: approxBeginSeqId
     */
    public Integer getApproxBeginSeqId() {
        return (Integer) get_prop(PROP_APPROXBEGINSEQID);
    }

    public void setApproxBeginSeqId(final Integer approxBeginSeqId)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_APPROXBEGINSEQID, approxBeginSeqId);
    }

    /**
     * Property: approxEndSeqId
     */
    public Integer getApproxEndSeqId() {
        return (Integer) get_prop(PROP_APPROXENDSEQID);
    }

    public void setApproxEndSeqId(final Integer approxEndSeqId)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_APPROXENDSEQID, approxEndSeqId);
    }

    /**
     * Property: researchObjective
     * 
     * 
     */
    public ResearchObjective getResearchObjective() {
        return (ResearchObjective) get_prop(PROP_RESEARCHOBJECTIVE);
    }

    /**
     * Property: researchObjective
     */
    public ExpressionObjective getExpressionObjective() {
        return (ExpressionObjective) get_prop(PROP_EXPRESSIONOBJECTIVE);
    }

    /**
     * Property: molecule
     */
    public Molecule getMolecule() {
        return (Molecule) get_prop(PROP_MOLECULE);
    }

    public void setMolecule(final Molecule molecule) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MOLECULE, molecule);
    }

    /**
     * Property: target
     */
    public Target getTarget() {
        return (Target) get_prop(PROP_TARGET);
    }

    public void setTarget(final Target target) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_TARGET, target);
    }

    /**
     * Property: sampleComponents
     */
    public Set<SampleComponent> getSampleComponents() {
        return (Set<SampleComponent>) get_prop(PROP_SAMPLECOMPONENTS);
    }

    public void setSampleComponents(final java.util.Collection<SampleComponent> sampleComponents)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SAMPLECOMPONENTS, sampleComponents);
    }

    public void addSampleComponent(final SampleComponent sampleComponent)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_SAMPLECOMPONENTS, sampleComponent);
    }

    public void removeSampleComponent(final SampleComponent sampleComponent)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_SAMPLECOMPONENTS, sampleComponent);
    }

    /**
     * Property: trialMolComponents (replaced by trialMolecules)
     */
    public Set<Molecule> getTrialMolComponents() {
        return (Set<Molecule>) get_prop(PROP_TRIALMOLECULES);
    }

    public void setTrialMolComponents(final java.util.Collection<Molecule> trialMolComponents)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_TRIALMOLECULES, trialMolComponents);
    }

    public void addTrialMolComponent(final org.pimslims.model.molecule.Molecule trialMolComponent)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_TRIALMOLECULES, trialMolComponent);
    }

    public void removeTrialMolComponent(final org.pimslims.model.molecule.Molecule trialMolComponent)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_TRIALMOLECULES, trialMolComponent);
    }

    /**
     * Property: trialMolecules
     */
    public Set<Molecule> getTrialMolecules() {
        return (Set<Molecule>) get_prop(PROP_TRIALMOLECULES);
    }

    public void setTrialMolecules(final java.util.Collection<Molecule> trialMolecules)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_TRIALMOLECULES, trialMolecules);
    }

    public void addTrialMolecule(final org.pimslims.model.molecule.Molecule trialMolecule)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_TRIALMOLECULES, trialMolecule);
    }

    public void removeTrialMolecule(final org.pimslims.model.molecule.Molecule trialMolecule)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_TRIALMOLECULES, trialMolecule);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */
    /**
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {
        if (null != target) {
            return target.getName();
        }
        return this.get_Hook();
    }

}
