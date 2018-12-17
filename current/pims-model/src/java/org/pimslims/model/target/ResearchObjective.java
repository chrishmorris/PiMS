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
import java.util.Collections;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.protocol.Workflow;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "TARG_RESEARCHOBJECTIVE")
@org.pimslims.annotation.MetaClass(helpText = "What you want to express - one or more open reading frames, perhaps truncated, and ligands", keyNames = {}, subclasses = {})
public class ResearchObjective extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable, org.pimslims.model.experiment.Project, ExpressionObjective {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_LOCALNAME = "localName";

    public static final String PROP_SYSTEMATICNAME = "systematicName";

    public static final String PROP_COMMONNAME = "commonName";

    public static final String PROP_BIOLOGICALPROCESS = "biologicalProcess";

    public static final String PROP_BIOCHEMICALFUNCTION = "biochemicalFunction";

    public static final String PROP_FUNCTIONDESCRIPTION = "functionDescription";

    public static final String PROP_CLIENTCOSTCODE = "clientCostCode";

    public static final String PROP_CELLLOCATION = "cellLocation";

    public static final String PROP_CATALYTICACTIVITY = "catalyticActivity";

    public static final String PROP_PATHWAY = "pathway";

    public static final String PROP_SIMILARITYDETAILS = "similarityDetails";

    public static final String PROP_WHYCHOSEN = "whyChosen";

    public static final String PROP_OWNER = "owner";

    public static final String PROP_RESEARCHOBJECTIVEELEMENTS = "researchObjectiveElements";

    public static final String PROP_EXPERIMENTS = "experiments";

    public static final String PROP_WORKFLOWS = "workflows";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    // what use is this?
    @Basic(optional = true)
    @Column(name = "LOCALNAME", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "", constraints = { "contains_no_linebreak" })
    private String localName;

    /* ------------------------------------------------------------ */
    // what use is this?
    @Basic(optional = true)
    @Column(name = "SYSTEMATICNAME", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "", constraints = { "contains_no_linebreak" })
    private String systematicName;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "COMMONNAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The name of the ResearchObjective", constraints = { "contains_no_linebreak" })
    private String commonName;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    // what use is this?
    @Column(name = "BIOLOGICALPROCESS", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "")
    private String biologicalProcess;

    /* ------------------------------------------------------------ */
    // what use is this?
    @Basic(optional = true)
    @Column(name = "BIOCHEMICALFUNCTION", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "")
    private String biochemicalFunction;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "FUNCTIONDESCRIPTION", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The function description is an human-readable description of the biochemical function.")
    private String functionDescription;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    // what use is this?
    @Column(name = "CELLLOCATION", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "", constraints = { "contains_no_linebreak" })
    private String cellLocation;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CATALYTICACTIVITY", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The official statement of catalytic activity.")
    private String catalyticActivity;

    /* ------------------------------------------------------------ */
    // what use is this?
    @Basic(optional = true)
    @Column(name = "PATHWAY", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The description of the biological processes this is related to.")
    private String pathway;

    /* ------------------------------------------------------------ */
    // what use is this?
    @Basic(optional = true)
    @Column(name = "SIMILARITYDETAILS", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The information relating this to other organisms/genes/proteins.")
    private String similarityDetails;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "WHYCHOSEN", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Details of the basis for the selection of this research objective.")
    private String whyChosen;

    @Basic(optional = true)
    @Column(name = "CLIENTCOSTCODE", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Purchase order id; a reference number for the customers' accounting system.")
    private String clientCostCode;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "OWNERID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "targ_ro_owner_inx")
    @org.pimslims.annotation.Role(helpText = "User who owns this Project or Construct.", low = 0, high = 1, isChangeable = true, reverseRoleName = "researchObjectives")
    private User owner;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "researchObjective")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "researchObjective")
    private final Set<ResearchObjectiveElement> researchObjectiveElements =
        new HashSet<ResearchObjectiveElement>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "researchObjective")
    @org.pimslims.annotation.Role(helpText = "Experiments performed within a Project / for this Construct.", low = 0, high = -1, isChangeable = true, reverseRoleName = "researchObjective")
    private final Set<Experiment> experiments = new HashSet<Experiment>(0);

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "projects")
    @org.pimslims.annotation.Role(helpText = "Workflows for this Project/Construct", low = 0, high = -1, isChangeable = true, reverseRoleName = "projects")
    private final Set<Workflow> workflows = new HashSet<Workflow>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected ResearchObjective() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected ResearchObjective(final WritableVersion wVersion)
        throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public ResearchObjective(final WritableVersion wVersion, final java.lang.String commonName,
        final java.lang.String whyChosen) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_COMMONNAME, commonName);
        attributes.put(PROP_WHYCHOSEN, whyChosen);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public ResearchObjective(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: localName
     */
    public String getLocalName() {
        return (String) get_prop(PROP_LOCALNAME);
    }

    public void setLocalName(final String localName) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_LOCALNAME, localName);
    }

    /**
     * Property: systematicName
     */
    public String getSystematicName() {
        return (String) get_prop(PROP_SYSTEMATICNAME);
    }

    public void setSystematicName(final String systematicName)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SYSTEMATICNAME, systematicName);
    }

    /**
     * Property: commonName
     */
    public String getCommonName() {
        return (String) get_prop(PROP_COMMONNAME);
    }

    public void setCommonName(final String commonName) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_COMMONNAME, commonName);
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
     * Property: owner
     */
    public User getOwner() {
        return (User) get_prop(PROP_OWNER);
    }

    public void setOwner(final User owner) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_OWNER, owner);
    }

    /**
     * Property: expressionObjectiveElements
     */
    @Deprecated
    // use getExpressionObjectiveElements
    public Set<ResearchObjectiveElement> getBlueprintComponents() {
        return (Set<ResearchObjectiveElement>) get_prop(PROP_RESEARCHOBJECTIVEELEMENTS);
    }

    @Deprecated
    // use setExpressionObjectiveElements
    public void setBlueprintComponents(
        final java.util.Collection<ResearchObjectiveElement> expressionObjectiveElements)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_RESEARCHOBJECTIVEELEMENTS, expressionObjectiveElements);
    }

    /**
     * Property: expressionObjectiveElements
     */
    public Set<ResearchObjectiveElement> getExpressionObjectiveElements() {
        return (Set<ResearchObjectiveElement>) get_prop(PROP_RESEARCHOBJECTIVEELEMENTS);
    }

    public void setExpressionObjectiveElements(
        final java.util.Collection<ResearchObjectiveElement> expressionObjectiveElements)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_RESEARCHOBJECTIVEELEMENTS, expressionObjectiveElements);
    }

    @Deprecated
    // use addExpressionObjectiveElement
    public void addBlueprintComponent(final ResearchObjectiveElement expressionObjectiveElement)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_RESEARCHOBJECTIVEELEMENTS, expressionObjectiveElement);
    }

    @Deprecated
    // use removeExpressionObjectiveElement
    public void removeBlueprintComponent(final ResearchObjectiveElement expressionObjectiveElement)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_RESEARCHOBJECTIVEELEMENTS, expressionObjectiveElement);
    }

    public void addExpressionObjectiveElement(final ResearchObjectiveElement expressionObjectiveElement)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_RESEARCHOBJECTIVEELEMENTS, expressionObjectiveElement);
    }

    public void removeExpressionObjectiveElement(final ResearchObjectiveElement expressionObjectiveElement)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_RESEARCHOBJECTIVEELEMENTS, expressionObjectiveElement);
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
     * Property: experiments
     */
    public Set<Experiment> getExperiments() {
        return (Set<Experiment>) get_prop(PROP_EXPERIMENTS);
    }

    public void setExperiments(final java.util.Collection<Experiment> experiments)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_EXPERIMENTS, experiments);
    }

    public void addExperiment(final Experiment experiment) throws org.pimslims.exception.ConstraintException {
        add(PROP_EXPERIMENTS, experiment);
    }

    public void removeExperiment(final Experiment experiment)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_EXPERIMENTS, experiment);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */
    /**
     * helper method: getMilestones()
     */
    public Set<Milestone> getMilestones() {
        final Set<Milestone> results = new HashSet<Milestone>(0);
        for (final Experiment element : this.getExperiments()) {
            results.addAll(element.getMilestones());
        }
        return results;
    }

    /**
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {
        return this.commonName;
    }

    /**
     * ResearchObjective.getClientCostCode
     * 
     * @return
     */
    public String getClientCostCode() {
        return (String) get_prop(PROP_CLIENTCOSTCODE);
    }

    /**
     * ResearchObjective.setClientCostCode
     * 
     * @param unique
     * @throws ConstraintException
     */
    public void setClientCostCode(String purchaseOrderId) throws ConstraintException {
        set_prop(PROP_CLIENTCOSTCODE, purchaseOrderId);
    }

    /**
     * ResearchObjective.getExpressionObjectives
     * 
     * @see org.pimslims.model.experiment.Project#getExpressionObjectives()
     */
    public Collection<ExpressionObjective> getExpressionObjectives() {
        return Collections.singletonList((ExpressionObjective) this);
    }

    /**
     * ResearchObjective.getProject
     * 
     * @see org.pimslims.model.target.ExpressionObjective#getProject()
     */
    public Project getProject() {
        return this;
    }

    /**
     * Property: workflow
     */
    @Override
    public Set<Workflow> getWorkflows() {
        return (Set<Workflow>) get_prop(PROP_WORKFLOWS);
    }

    @Override
    public void setWorkflows(final java.util.Collection<Workflow> workflows)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_WORKFLOWS, workflows);
    }

    @Override
    public void addWorkflow(final Workflow workflow) throws org.pimslims.exception.ConstraintException {
        add(PROP_WORKFLOWS, workflow);
    }

    @Override
    public void removeWorkflow(final Workflow workflow) throws org.pimslims.exception.ConstraintException {
        remove(PROP_WORKFLOWS, workflow);
    }

}
