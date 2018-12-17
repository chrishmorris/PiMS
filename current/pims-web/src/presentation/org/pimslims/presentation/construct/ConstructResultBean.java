// ----------------------------------------------------------------------------------------------
// SPOTTarget.java SPOT target bean
// Created by Johan van Niekerk SSPF-Dundee
// Apated from Jon Diprose construct_milestone code
// 23 March 2006
// ----------------------------------------------------------------------------------------------

package org.pimslims.presentation.construct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.experiment.ExperimentBean;

/**
 * Bean to represent a SPOT Construct Milestone
 * 
 * @author Jon Diprose
 */
public class ConstructResultBean implements Comparable, Serializable {

    /**
     * To satisfy Serializable Interface
     */
    private static final long serialVersionUID = 8178143313488237133L;

    /**
     * the current transaction
     */
    //private final ReadableVersion version;
    /**
     * Properties
     */
    private Long constructMilestoneId;

    private String name;

    private String constructHook;

    private String targetName;

    private String milestoneName;

    private Calendar dateOfExperiment;

    private Date dateOfEntry;

    private String person_login_name;

    private String protocol;

    private String result;

    private String comments;

    private String procedure;

    private Boolean markedForDelete;

    private Long protocolId;

    private Boolean customProtocol;

    private Boolean customProtocolUsed;

    private Boolean lastResult;

    // PIMS properties
    private Milestone blueprintStatus;

    private Experiment experiment;

    private String constructDescription;

    private Collection<Attachment> files;

    private ExperimentBean expBean;

    private ModelObjectShortBean conBean;

    private boolean syntheticGeneMilestone;

    private String sgeneHook;

    /**
     * Zero-argument Constructor
     */
    public ConstructResultBean() {
        this.constructDescription = null;
    }

    /**
     * A model of a milestone, for the SPOT view. Note however that there is no milestone for defining a
     * construct. If it has not yet been cloned, the BlueprintStatus will be null.
     * 
     * @param version the current transaction
     * @param construct the construct this milestone is for
     * @param milestone the latest status, or null if only defined.
     */
    public ConstructResultBean(final ReadableVersion version, final Milestone milestone) {

        // Store arguments in class variables
        this.useMilestoneDetails(milestone);

        final ResearchObjective construct = milestone.getResearchObjective();
        // Get some details from the construct
        if (null != construct) {
            this.useBlueprintDetails(version, construct, ConstructBeanReader.readConstruct(construct));
        }
        if (null != milestone.getTarget()) {
            this.targetName = milestone.getTarget().getName();
        }

        // Get the Experiment
        final Experiment e = this.blueprintStatus.getExperiment();
        if (e != null) {
            this.useExperimentDetails(e);
        }
    }

    /**
     * @param milestone
     */
    private void useMilestoneDetails(final Milestone milestone) {
        this.milestoneName = milestone.getStatus().getName();
        this.blueprintStatus = milestone;
        this.dateOfExperiment = this.blueprintStatus.getDate();
    }

    public ConstructResultBean(final ReadableVersion version, final Experiment experiment,
        final ConstructBean constructBean) {

        this.blueprintStatus = null;
        this.dateOfExperiment = experiment.getEndDate();

        final Project construct = experiment.getProject();
        // Get some details from the construct
        if (null != construct) {
            this.useBlueprintDetails(version, (ResearchObjective) construct, constructBean);
        }
        this.useExperimentDetails(experiment);

        final Collection<Milestone> milestones = experiment.getMilestones();
        if (1 == milestones.size()) {
            final Milestone milestone = milestones.iterator().next();
            this.useMilestoneDetails(milestone);
        }

    }

    /**
     * @param version
     * @param construct
     */
    private void useBlueprintDetails(final ReadableVersion version, final ResearchObjective construct,
        final ConstructBean cb) {
        this.name = construct.getCommonName();
        this.setConstructHook(construct.get_Hook());
        this.name = construct.getCommonName();
        this.constructDescription = cb.getDescription();

        //Susy for SyntheticGene
        this.syntheticGeneMilestone = false;
        if (construct.getSimilarityDetails().contains("Synthetic gene ::")) {
            this.syntheticGeneMilestone = true;
            //String[] sgBits = new String[3];
            //sgBits = construct.getSimilarityDetails().split("::");
            //final String sgSampleHook = sgBits[2].trim();
            //this.sgeneHook = sgSampleHook;

        }
        //Susy 27-01-10 to use for pimsWidget:link tag in list of Constructs
        this.conBean = cb;

        // Get all the ResearchObjectiveElemente
        final Collection c = construct.getResearchObjectiveElements();
        final Iterator i = c.iterator();
        while (i.hasNext()) {
            final ResearchObjectiveElement bpc = (ResearchObjectiveElement) i.next();
            final Target t = bpc.getTarget();
            if (t != null) {
                // Get the Target's commonName
                this.targetName = t.getName();
            }
        }
    }

    /**
     * @param e
     */
    private void useExperimentDetails(final Experiment e) {
        // Store it
        this.experiment = e;
        this.comments = e.getDetails();
        this.dateOfExperiment = e.getEndDate();
        this.result = e.getStatus();

        // Get the Experiments creator
        final User p = e.getCreator();

        // If there is a creator
        if (p != null) {

            // Get the creators familyName
            this.person_login_name = p.getName();

        }
        //Susy 21-01-10 to get files from Experiment
        final Collection<Attachment> files = new ArrayList<Attachment>();
        for (final Attachment file : e.getAttachments()) {
            files.add(file);
        }
        this.files = files;

        //Susy 22-01-10 also, need ExperimentBeans to make pimsWidget:link in the list
        final ExperimentBean expb = new ExperimentBean(e);
        this.expBean = expb;

        /* this data is no longer written
         final Collection<ApplicationData> appDatas = new HashSet<ApplicationData>();
         for (final ApplicationData appData : e.getApplicationData()) {
             if (appData.getApplication().equalsIgnoreCase("SPOT")) {
                 appDatas.add(appData);
             }
         }

         for (final ApplicationData a : appDatas) {

             if (StringUtils.equals("protocol", a.getKeyword())) {
                 this.protocol = ((AppDataString) a).getValue();
             }
             if (StringUtils.equals("comments", a.getKeyword())) {
                 this.comments = ((AppDataString) a).getValue();
             }
         }
         */
    }

    public Milestone getBlueprintStatus() {
        return this.blueprintStatus;
    }

    public int compareTo(final Object obj) {
        if (!(obj instanceof ConstructResultBean)) {
            throw new ClassCastException(obj + " is not a SPOTConstructMilestone! ");
        }
        final ConstructResultBean arg = (ConstructResultBean) obj;
        final Calendar argCmDate = (arg).getDateOfExperiment();
        final Calendar thisCmDate = this.getDateOfExperiment();
        if (thisCmDate.equals(argCmDate)) {
            return this.experiment.getDbId().compareTo(arg.experiment.getDbId());
        }
        return thisCmDate.compareTo(argCmDate);
    }

    /**
     * @return the hook for the blueprint status
     */
    public String getHook() {
        return this.getBlueprintStatus().get_Hook();
    }

    /**
     * @return Returns the comments.
     */
    public String getComments() {
        return this.comments;
    }

    /**
     * @param comments The comments to set.
     */
    public void setComments(final String comments) {
        this.comments = comments;
    }

    /**
     * @return Returns the constructId.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param constructId The constructId to set.
     */
    public void setConstructId(final String constructId) {
        this.name = constructId;
    }

    /**
     * @return Returns the constructHook.
     */
    public String getConstructHook() {
        return this.constructHook;
    }

    /**
     * @param constructHook The constructHook to set.
     */
    public void setConstructHook(final String constructHook) {
        this.constructHook = constructHook;
    }

    /**
     * @return Returns the constructMilestoneId.
     */
    public Long getConstructMilestoneId() {
        return this.constructMilestoneId;
    }

    /**
     * @param constructMilestoneId The constructMilestoneId to set.
     */
    public void setConstructMilestoneId(final Long constructMilestoneId) {
        this.constructMilestoneId = constructMilestoneId;
    }

    /**
     * @return Returns the customProtocol.
     */
    public Boolean getCustomProtocol() {
        return this.customProtocol;
    }

    /**
     * @param customProtocol The customProtocol to set.
     */
    public void setCustomProtocol(final Boolean customProtocol) {
        this.customProtocol = customProtocol;
    }

    /**
     * @return Returns the customProtocolUsed.
     */
    public Boolean getCustomProtocolUsed() {
        return this.customProtocolUsed;
    }

    /**
     * @param customProtocolUsed The customProtocolUsed to set.
     */
    public void setCustomProtocolUsed(final Boolean customProtocolUsed) {
        this.customProtocolUsed = customProtocolUsed;
    }

    /**
     * @return Returns the dateOfEntry.
     */
    public Date getDateOfEntry() {
        return this.dateOfEntry;
    }

    /**
     * @param dateOfEntry The dateOfEntry to set.
     */
    public void setDateOfEntry(final Date dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    /**
     * @return Returns the dateOfExperiment.
     */
    public Calendar getDateOfExperiment() {
        return this.dateOfExperiment;

    }

    /**
     * @param dateOfExperiment The dateOfExperiment to set.
     */
    public void setDateOfExperiment(final Calendar dateOfExperiment) {
        this.dateOfExperiment = dateOfExperiment;
    }

    /**
     * @return Returns the markedForDelete.
     */
    public Boolean getMarkedForDelete() {
        return this.markedForDelete;
    }

    /**
     * @param markedForDelete The markedForDelete to set.
     */
    public void setMarkedForDelete(final Boolean markedForDelete) {
        this.markedForDelete = markedForDelete;
    }

    /**
     * @return Returns the milestoneTypeId.
     */
    public String getMilestoneName() {
        return this.milestoneName;
    }

    /**
     * @param milestoneTypeId The milestoneTypeId to set.
     */
    public void setMilestoneName(final String milestoneName) {
        this.milestoneName = milestoneName;
    }

    /**
     * @return Returns the personId.
     */
    public String getPerson_login_name() {
        return this.person_login_name;
    }

    /**
     * @return Returns the procedure.
     */
    public String getProcedure() {
        return this.procedure;
    }

    /**
     * @param procedure The procedure to set.
     */
    public void setProcedure(final String procedure) {
        this.procedure = procedure;
    }

    /**
     * @return Returns the protocol.
     */
    public String getProtocol() {
        return this.protocol;
    }

    /**
     * @param protocol The protocol to set.
     */
    public void setProtocol(final String protocol) {
        this.protocol = protocol;
    }

    /**
     * @return Returns the protocolId.
     */
    public Long getProtocolId() {
        return this.protocolId;
    }

    /**
     * @param protocolId The protocolId to set.
     */
    public void setProtocolId(final Long protocolId) {
        this.protocolId = protocolId;
    }

    /**
     * @return Returns the result.
     */

    public String getResult() {
        return this.result;
    }

    /**
     * @return Returns the targetId.
     */
    public String getProteinName() {
        return this.targetName;
    }

    /**
     * @param targetId The targetId to set.
     */
    public void setProteinName(final String proteinName) {
        this.targetName = proteinName;
    }

    /**
     * @see org.pimslims.model.expBlueprint.BlueprintStatus#getCode()
     */
    public TargetStatus getCode() {
        return this.blueprintStatus.getStatus();
    }

    /**
     * @see org.pimslims.model.expBlueprint.BlueprintStatus#getExpBlueprint()
     */
    public ResearchObjective getExpBlueprint() {
        if (null == this.blueprintStatus) {
            return null;
        }
        return this.blueprintStatus.getResearchObjective();
    }

    /**
     * @see org.pimslims.model.expBlueprint.BlueprintStatus#getExperiment()
     */
    public ModelObject getExperiment() {
        // return version.getModelObject(blueprintStatus.getExperiment());
        return this.experiment;
    }

    public String getConstructDescription() {
        return this.constructDescription;
    }

    public void setConstructDescription(final String constructDescription) {
        this.constructDescription = constructDescription;
    }

    public Collection<Attachment> getFiles() {
        return this.files;
    }

    public void setFiles(final List files) {
        this.files = files;
    }

    public ExperimentBean getExpBean() {
        return this.expBean;
    }

    public void setExpBean(final ExperimentBean expBean) {
        this.expBean = expBean;
    }

    public ModelObjectShortBean getConBean() {
        return this.conBean;
    }

/* unused
    public void setConBean(final ConstructBean conBean) {
        this.conBean = conBean;
    } */

    public Boolean getSyntheticGeneMilestone() {
        return this.syntheticGeneMilestone;
    }

    public void setSyntheticGeneMilestone(final Boolean syntheticGeneMilestone) {
        this.syntheticGeneMilestone = syntheticGeneMilestone;
    }

    public String getSgeneHook() {
        return this.sgeneHook;
    }

    public void setSgeneHook(final String sgeneHook) {
        this.sgeneHook = sgeneHook.trim();
    }

    public String getExperimentHook() {
        if (null == this.experiment) {
            return null;
        }
        return this.experiment.get_Hook();
    }

    public String getExperimentName() {
        if (null == this.experiment) {
            return null;
        }
        return this.experiment.getName();
    }

    /**
     * @return the lastResult
     */
    public Boolean getLastResult() {
        return this.lastResult;
    }

    /**
     * @param lastResult the lastResult to set
     */
    public void setLastResult(final Boolean lastResult) {
        this.lastResult = lastResult;
    }

}
