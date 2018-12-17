/**
 * 
 */
package org.pimslims.presentation.worklist;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.pimslims.dao.ModelImpl;
import org.pimslims.lab.Utils;
import org.pimslims.leeds.TargetUtility;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.presentation.servlet.utils.ValueFormatter;

/**
 * @author jon
 * 
 */
public class ConstructProgressBean extends ModelObjectShortBean {

    //TODO no, can't reuse these, they store local state
    private static final SimpleDateFormat DATE_FORMAT = Utils.getDateFormat();

    private String targetId;

    private String targetName;

    private String constructId;

    private String constructHook;

    private String organism = "";

    @Deprecated
    // target.project is no longer used
    private String project = "";

    private String milestone;

    private String dateOfExperiment;

    private Calendar dateOfExperimentDate;

    private String result;

    private String personId;

    /**
     * Zero-arg Constructor, used by old reports
     * 
     */
    public ConstructProgressBean() {
        super();
    }

    public ConstructProgressBean(final Milestone milestone2) {
        super();
        this.milestone = milestone2.getStatus().getName();
        this.dateOfExperimentDate = milestone2.getDate();
        this.setDateOfExperiment(ValueFormatter.formatDate(this.dateOfExperimentDate));
        final Target target = milestone2.getTarget();
        this.targetName = target.getProtein().getName();
        if (null != target.getSpecies()) {
            this.organism = target.getSpecies().getName();
        }
        /* projects no longer used
        final Collection<Project> projects = target.getProjects();
        if (1 == projects.size()) {
            this.project = projects.iterator().next().getShortName();
        } */
    }

    public ConstructProgressBean(final Experiment experiment) {
        super();
        this.milestone = experiment.getExperimentType().getName();
        this.dateOfExperimentDate = experiment.getEndDate();
        this.setDateOfExperiment(ValueFormatter.formatDate(this.dateOfExperimentDate));
        final Target target = TargetUtility.getFirstTarget(experiment);
        this.targetName = target.getProtein().getName();
        if (null != target.getSpecies()) {
            this.organism = target.getSpecies().getName();
        }
        /*
        final Collection<Project> projects = target.getProjects();
        if (1 == projects.size()) {
            this.project = projects.iterator().next().getShortName();
        } */

    }

    /**
     * @return Returns the constructId.
     */
    public String getConstructId() {
        return this.constructId;
    }

    /**
     * @param constructId The constructId to set.
     */
    public void setConstructId(final String constructId) {
        this.constructId = constructId;
    }

    /**
     * @return Returns the dateOfExperimentDate.
     */
    public Calendar getDateOfExperimentDate() {
        return this.dateOfExperimentDate;
    }

    /**
     * @param calendar TODO this looks wrong
     */
    public void setDateOfExperiment(final Calendar calendar) {
        if (this.dateOfExperimentDate == null) {
            this.setDateOfExperiment("");
        } else {
            this.setDateOfExperiment(ValueFormatter.formatDate(this.dateOfExperimentDate));
        }

    }

    /**
     * @return Returns the dateOfExperiment.
     */
    public String getDateOfExperiment() {
        return this.dateOfExperiment;
    }

    /**
     * @param dateOfExperiment The dateOfExperiment to set.
     */
    private void setDateOfExperiment(final String dateOfExperiment) {
        this.dateOfExperiment = dateOfExperiment;
    }

    /**
     * @return Returns the milestone.
     */
    public String getMilestone() {
        return this.milestone;
    }

    /**
     * @param milestone The milestone to set.
     */
    public void setMilestone(final String description) {
        this.milestone = description;
    }

    /**
     * @return Returns the organism.
     */
    public String getOrganism() {
        return this.organism;
    }

    /**
     * @param organism The organism to set.
     */
    public void setOrganism(final String organism) {
        this.organism = organism;
    }

    /**
     * @return Returns the project.
     */
    @Deprecated
    // target.project is no longer used
    public String getProject() {
        return this.project;
    }

    /**
     * @param project The project to set.
     */
    public void setProject(final String project) {
        this.project = project;
    }

    /**
     * @return
     */
    @Deprecated
    public String getProteinName() {
        return this.targetName;
    }

    /**
     * @return
     */
    public String getTargetName() {
        return this.targetName;
    }

    /**
     * @param proteinName The proteinName to set.
     */
    public void setTargetName(final String proteinName) {
        this.targetName = proteinName;
    }

    /**
     * @return Returns the result.
     */
    public String getResult() {
        return this.result;
    }

    /**
     * @param result The result to set.
     */
    public void setResult(final String result) {
        this.result = result;
    }

    /**
     * @param targetId The targetId to set.
     */
    public void setTargetId(final String targetId) {
        this.targetId = targetId;
    }

    /**
     * @return Returns the targetId.
     */
    public String getTargetId() {
        return this.targetId;
    }

    /**
     * @return Returns the personId.
     */
    public String getPersonId() {
        return this.personId;
    }

    /**
     * @param personId The personId to set.
     */
    public void setPersonId(final String personId) {
        this.personId = personId;
    }

    public int getDaysAgo() {
        final long msAgo = System.currentTimeMillis() - this.dateOfExperimentDate.getTimeInMillis();
        final int daysAgo = (int) (msAgo / (1000 * 60 * 60 * 24));
        return daysAgo;
    }

    /**
     * @return the constructHook
     */
    public String getConstructHook() {
        return this.constructHook;
    }

    /**
     * @param constructHook the constructHook to set
     */
    public void setConstructHook(final String constructHook) {
        this.constructHook = constructHook;
    }

    @Override
    public String getClassName() {
        return ResearchObjective.class.getName();
    }

    @Override
    public String getHook() {
        return this.constructHook;
    }

    @Override
    public String getName() {
        return this.constructId;
    }

    /**
     * @return the classDisplayName
     */
    @Override
    public String getClassDisplayName() {
        return ServletUtil.getDisplayName(ModelImpl.getModel()
            .getMetaClass(ResearchObjective.class.getName()));
    }

    /**
     * ConstructProgressBean.getMetaClass
     * 
     * @see org.pimslims.presentation.ModelObjectShortBean#getMetaClass()
     */
    @Override
    public MetaClass getMetaClass() {
        return ModelImpl.getModel().getMetaClass(ResearchObjective.class.getName());
    }

}
