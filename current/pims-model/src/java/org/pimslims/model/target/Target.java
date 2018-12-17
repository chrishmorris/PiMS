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

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
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
import org.pimslims.annotation.Attribute;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.Organism;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "TARG_TARGET", uniqueConstraints = {})
@org.pimslims.annotation.MetaClass(helpText = "The information on the target.", keyNames = { "NAME" }, subclasses = {}, parentRoleName = "rootProject")
public class Target extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_ORF = "orf";

    public static final String PROP_GENENAME = "geneName";

    public static final String PROP_BIOLOGICALPROCESS = "biologicalProcess";

    public static final String PROP_BIOCHEMICALFUNCTION = "biochemicalFunction";

    public static final String PROP_FUNCTIONDESCRIPTION = "functionDescription";

    public static final String PROP_TOPOLOGY = "topology";

    public static final String PROP_CELLLOCATION = "cellLocation";

    public static final String PROP_CATALYTICACTIVITY = "catalyticActivity";

    public static final String PROP_PATHWAY = "pathway";

    public static final String PROP_SIMILARITYDETAILS = "similarityDetails";

    public static final String PROP_WHYCHOSEN = "whyChosen";

    public static final String PROP_ALIASES = "aliases";

    public static final String PROP_PROTEIN = "protein";

    public static final String PROP_SPECIES = "species";

    public static final String PROP_RESEARCHOBJECTIVEELEMENTS = "researchObjectiveElements";

    public static final String PROP_MILESTONES = "milestones";

    public static final String PROP_NUCLEICACIDS = "nucleicAcids";

    @Deprecated
    // no longer used
    public static final String PROP_PROJECTS = "projects";

    public static final String PROP_TARGETGROUPS = "targetGroups";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The name of the target used to uniquely identify the target. It is its uniprot entry name, e.g. CAIC_ECOLI.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ORF", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Open Reading Frame name.", constraints = { "contains_no_linebreak" })
    private String orf;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "GENENAME", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The official name of the gene for this target. e.g. 'fcy1'.", constraints = { "contains_no_linebreak" })
    private String geneName;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "BIOLOGICALPROCESS", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The biological process where the target is involved. e.g. 'Unknown'.")
    private String biologicalProcess;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "BIOCHEMICALFUNCTION", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The biochemical function within the biological pathway of the target. e.g. 'Cytosine deaminase'.")
    private String biochemicalFunction;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "FUNCTIONDESCRIPTION", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The function description is an human-readable description of the biochemical function. e.g. 'Converts cytosine to uracil or 5-methylcytosine to thymine by deaminating carbon number 4'.")
    private String functionDescription;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "TOPOLOGY", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The topology of the target is a general description of its topology or if it is a membrane protein, the topology could refer to the membrane one predicted by the TMHMM program or obtained by experimental observation.")
    private String topology;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CELLLOCATION", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The typical location of the target within a cell. e.g. 'cytoplasmic'.", constraints = { "contains_no_linebreak" })
    private String cellLocation;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CATALYTICACTIVITY", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The official statement of catalytic activity. e.g. 'CYTOSINE + H(2)O = URACIL + NH(3)'.")
    private String catalyticActivity;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PATHWAY", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The description of the biological processes this target is related to. e.g. 'Pyrimidine salvage pathway'.")
    private String pathway;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "SIMILARITYDETAILS", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Information relating this target to other organisms/genes/proteins. e.g. 'Belongs to the cytidine and deoxycytidylate deaminases family.'")
    private String similarityDetails;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "WHYCHOSEN", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Details of the basis for the selection of this target.")
    private String whyChosen;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PROTEINID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "targ_target_protein_inx")
    @org.pimslims.annotation.Role(helpText = "The MolComponent used to describe the Target protein", low = 1, high = 1, isChangeable = true, reverseRoleName = "protTargets")
    private Molecule protein;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "SPECIESID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "targ_target_species_inx")
    @org.pimslims.annotation.Role(helpText = "Specie of target.", low = 0, high = 1, isChangeable = true, reverseRoleName = "targets")
    private Organism species;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "target")
    @org.pimslims.annotation.Role(helpText = "Research objective elements linked to a single Target, representing the fact that different fragments of the same target may be studied in various contexts.", low = 0, high = -1, isChangeable = true, reverseRoleName = "target")
    private final Set<ResearchObjectiveElement> researchObjectiveElements =
        new HashSet<ResearchObjectiveElement>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "target")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "target")
    private final Set<Milestone> milestones = new HashSet<Milestone>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "target")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "target")
    private final Set<Alias> aliases = new HashSet<Alias>(0);

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "TARG_TARGET2NUCLAC", joinColumns = { @JoinColumn(name = "NUCTARGETID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "NUCLEICACIDID", nullable = false, updatable = false) })
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @org.pimslims.annotation.Role(helpText = "The nucleic acid MolComponent used to describe the Target", low = 0, high = -1, isChangeable = true, reverseRoleName = "nucTargets")
    private final Set<Molecule> nucleicAcids = new HashSet<Molecule>(0);

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "TARG_TARGET2PROJECTS", joinColumns = { @JoinColumn(name = "TARGETID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "PROJECTID", nullable = false, updatable = false) })
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @org.pimslims.annotation.Role(helpText = "The list of project to wich this target belongs.", low = 0, high = -1, isChangeable = true, reverseRoleName = "targets")
    @Deprecated
    private final Set<Project> projects = new HashSet<Project>(0);

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "targets")
    @org.pimslims.annotation.Role(helpText = "The list of target groups to wich this target belongs.", low = 0, high = -1, isChangeable = true, reverseRoleName = "targets")
    private final Set<TargetGroup> targetGroups = new HashSet<TargetGroup>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Target() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Target(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Target(final WritableVersion wVersion, final java.lang.String name,
        final org.pimslims.model.molecule.Molecule protein) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);
        attributes.put(PROP_PROTEIN, protein);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Target(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
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
     * Property: orf
     */
    public String getOrf() {
        return (String) get_prop(PROP_ORF);
    }

    public void setOrf(final String orf) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ORF, orf);
    }

    /**
     * Property: geneName
     */
    public String getGeneName() {
        return (String) get_prop(PROP_GENENAME);
    }

    public void setGeneName(final String geneName) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_GENENAME, geneName);
    }

    /**
     * Property: biologicalProcess
     */
    public String getBiologicalProcess() {
        return (String) get_prop(PROP_BIOLOGICALPROCESS);
    }

    public void setBiologicalProcess(final String biologicalProcess)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_BIOLOGICALPROCESS, biologicalProcess);
    }

    /**
     * Property: biochemicalFunction
     */
    public String getBiochemicalFunction() {
        return (String) get_prop(PROP_BIOCHEMICALFUNCTION);
    }

    public void setBiochemicalFunction(final String biochemicalFunction)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_BIOCHEMICALFUNCTION, biochemicalFunction);
    }

    /**
     * Property: functionDescription
     */
    public String getFunctionDescription() {
        return (String) get_prop(PROP_FUNCTIONDESCRIPTION);
    }

    public void setFunctionDescription(final String functionDescription)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_FUNCTIONDESCRIPTION, functionDescription);
    }

    /**
     * Property: topology
     */
    public String getTopology() {
        return (String) get_prop(PROP_TOPOLOGY);
    }

    public void setTopology(final String topology) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_TOPOLOGY, topology);
    }

    /**
     * Property: cellLocation
     */
    public String getCellLocation() {
        return (String) get_prop(PROP_CELLLOCATION);
    }

    public void setCellLocation(final String cellLocation) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CELLLOCATION, cellLocation);
    }

    /**
     * Property: catalyticActivity
     */
    public String getCatalyticActivity() {
        return (String) get_prop(PROP_CATALYTICACTIVITY);
    }

    public void setCatalyticActivity(final String catalyticActivity)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CATALYTICACTIVITY, catalyticActivity);
    }

    /**
     * Property: pathway
     */
    public String getPathway() {
        return (String) get_prop(PROP_PATHWAY);
    }

    public void setPathway(final String pathway) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PATHWAY, pathway);
    }

    /**
     * Property: similarityDetails
     */
    public String getSimilarityDetails() {
        return (String) get_prop(PROP_SIMILARITYDETAILS);
    }

    public void setSimilarityDetails(final String similarityDetails)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SIMILARITYDETAILS, similarityDetails);
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
     * Property: aliases
     */
    public Set<Alias> getAliases() {
        return (Set<Alias>) get_prop(PROP_ALIASES);
    }

    public void setAliases(final Collection<Alias> aliases) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ALIASES, aliases);
    }

    /**
     * Property: protein
     */
    public Molecule getProtein() {
        return (Molecule) get_prop(PROP_PROTEIN);
    }

    public void setProtein(final Molecule protein) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PROTEIN, protein);
    }

    /**
     * Property: species
     */
    public Organism getSpecies() {
        return (Organism) get_prop(PROP_SPECIES);
    }

    public void setSpecies(final Organism species) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SPECIES, species);
    }

    /**
     * Property: researchObjectiveElements
     */
    public Set<ResearchObjectiveElement> getResearchObjectiveElements() {
        return (Set<ResearchObjectiveElement>) get_prop(PROP_RESEARCHOBJECTIVEELEMENTS);
    }

    public void setResearchObjectiveElements(
        final java.util.Collection<ResearchObjectiveElement> researchObjectiveElements)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_RESEARCHOBJECTIVEELEMENTS, researchObjectiveElements);
    }

    public void addResearchObjectiveElement(final ResearchObjectiveElement researchObjectiveElement)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_RESEARCHOBJECTIVEELEMENTS, researchObjectiveElement);
    }

    public void removeResearchObjectiveElement(final ResearchObjectiveElement researchObjectiveElement)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_RESEARCHOBJECTIVEELEMENTS, researchObjectiveElement);
    }

    /**
     * Property: milestones
     */
    public Set<Milestone> getMilestones() {
        return (Set<Milestone>) get_prop(PROP_MILESTONES);
    }

    public void setMilestones(final java.util.Collection<Milestone> milestones)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MILESTONES, milestones);
    }

    public void addMilestone(final Milestone milestone) throws org.pimslims.exception.ConstraintException {
        add(PROP_MILESTONES, milestone);
    }

    public void removeMilestone(final Milestone milestone) throws org.pimslims.exception.ConstraintException {
        remove(PROP_MILESTONES, milestone);
    }

    /**
     * Property: nucleicAcids
     */
    public Set<Molecule> getNucleicAcids() {
        return (Set<Molecule>) get_prop(PROP_NUCLEICACIDS);
    }

    public void setNucleicAcids(final java.util.Collection<Molecule> nucleicAcids)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_NUCLEICACIDS, nucleicAcids);
    }

    public void addNucleicAcid(final org.pimslims.model.molecule.Molecule nucleicAcid)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_NUCLEICACIDS, nucleicAcid);
    }

    public void removeNucleicAcid(final org.pimslims.model.molecule.Molecule nucleicAcid)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_NUCLEICACIDS, nucleicAcid);
    }

    /**
     * Property: projects
     */
    @Deprecated
    public Set<Project> getProjects() {
        return java.util.Collections.EMPTY_SET;
    }

    @Deprecated
    public void setProjects(final java.util.Collection<Project> projects)
        throws org.pimslims.exception.ConstraintException {
        throw new UnsupportedOperationException("Target.Project is obsolete");
    }

    @Deprecated
    public void addProject(final org.pimslims.model.target.Project project)
        throws org.pimslims.exception.ConstraintException {
        throw new UnsupportedOperationException("Target.Project is obsolete");
    }

    @Deprecated
    public void removeProject(final org.pimslims.model.target.Project project)
        throws org.pimslims.exception.ConstraintException {
        throw new UnsupportedOperationException("Target.Project is obsolete");
    }

    /**
     * Property: targetGroups
     */
    public Set<TargetGroup> getTargetGroups() {
        return (Set<TargetGroup>) get_prop(PROP_TARGETGROUPS);
    }

    public void setTargetGroups(final java.util.Collection<TargetGroup> targetGroups)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_TARGETGROUPS, targetGroups);
    }

    public void addTargetGroup(final org.pimslims.model.target.TargetGroup targetGroup)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_TARGETGROUPS, targetGroup);
    }

    public void removeTargetGroup(final org.pimslims.model.target.TargetGroup targetGroup)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_TARGETGROUPS, targetGroup);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */
    /**
     * Derived method: getSeqString()
     */
    @Attribute(helpText = "This is usually the amino acid sequence derived from Molecule.Polymer.")
    public java.lang.String getSeqString() {
        final org.pimslims.model.molecule.Molecule protein = this.getProtein();
        if (protein == null) {
            return "";
        }
        return protein.getSequence();
    }

    /**
     * Derived method: getProteinName()
     */
    @Attribute(helpText = "This is the name of the associated protein derived from protein.getName()")
    public String getProteinName() {
        final Molecule protein = this.getProtein();
        if (null == protein) {
            return "";
        }
        return protein.getName();
    }

    /**
     * 
     * Target.setAliasNames
     * 
     * @param aliasNames
     * @throws org.pimslims.exception.ConstraintException
     * @throws AccessException
     */
    public void setAliasNames(final Collection<String> aliasNames)
        throws org.pimslims.exception.ConstraintException {
        //drop current alias first
        if (this.aliases != null) {
            for (final Alias alias : this.aliases) {
                this.get_Version().getSession().delete(alias);//by pass access control
            }
        }
        final Collection<Alias> aliases = new LinkedList<Alias>();
        for (final String aliasName : aliasNames) {
            final Alias alias = new Alias((WritableVersion) this.get_Version(), aliasName, this);
            aliases.add(alias);
        }
        set_prop(PROP_ALIASES, aliases);
    }
}
