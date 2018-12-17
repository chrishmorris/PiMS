package org.pimslims.presentation.experiment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.pimslims.lab.Utils;
import org.pimslims.lab.file.CaliperFile;
import org.pimslims.lab.sequence.ProteinSequence;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.construct.ConstructBean;

public class ExperimentBean extends ModelObjectBean implements Comparable<Object>, Serializable {

    //private final Long dbId;

    // private final String name;

    private String status;

    private final ModelObjectShortBean experimentType;

    private final ModelObjectShortBean researchObjective;

    private Integer pcrProductSize;

    private Float proteinMW;

    private final Calendar experimentDate;

    @Deprecated
    // close transaction before dispatching JSP
    private final Experiment experiment;

    protected Collection<InputSampleBean> inputBeans;

    // Each bean in the presentation layer contains a hook to the corresponding
    // PIMS object
    private final String pimsExperimentHook;

    private final ModelObjectShortBean milestone;

    private final ModelObjectShortBean protocol;

    @Override
    public int compareTo(final Object obj) {

        if (!(obj instanceof ExperimentBean)) {
            if (obj instanceof ModelObjectBean) {
                return super.compareTo(obj);
            }
            throw new ClassCastException("obj1 is not an ExperimentSummary! ");
        }

        final Calendar argCmDate = ((ExperimentBean) obj).experimentDate;
        final Calendar thisCmDate = this.experimentDate;

        if (thisCmDate.after(argCmDate)) {
            return 0;
        } else if (thisCmDate.before(argCmDate)) {
            return 1;
        } else {
            return this.getDbId().compareTo(((ExperimentBean) obj).getDbId());
        }

    }

    @Override
    public boolean equals(final Object obj) {

        if (!(obj instanceof ExperimentBean)) {
            throw new ClassCastException("obj1 is not an ExperimentSummary! ");
        }

        final String column0 = ((ExperimentBean) obj).getExperimentHook();
        final String column1 = this.getExperimentHook();

        return column1.equals(column0);
    }

    @Override
    public int hashCode() {
        return this.getExperimentHook().hashCode();
    }

    public ExperimentBean(final Experiment experiment) {

        super(experiment);
        this.experiment = experiment;

        final Set<Milestone> milestones = experiment.getMilestones();
        if (1 == milestones.size()) {
            this.milestone = new ModelObjectShortBean(milestones.iterator().next());
        } else {
            this.milestone = null;
        }

        this.name = experiment.getName();
        this.status = experiment.getStatus();

        if (null == experiment.getProject()) {
            this.researchObjective = null;
            this.pcrProductSize = null;
            this.proteinMW = null;
        } else {
            this.researchObjective = new ModelObjectShortBean((ModelObject) experiment.getProject());
            for (final ResearchObjectiveElement element : ((ResearchObjective) experiment.getProject())
                .getResearchObjectiveElements()) {
                for (final Molecule bc : element.getTrialMolComponents()) {
                    for (final ComponentCategory category : bc.getCategories()) {
                        if (category.getName().equals(CaliperFile.PCR_PRODUCT)) {
                            this.pcrProductSize = bc.getSequence().length();
                        }
                        if (category.getName().equals(ConstructBean.EXPRESSED_PROTEIN)) {
                            ProteinSequence protein;
                            try {
                                protein = new ProteinSequence(bc.getSequence());

                            } catch (final IllegalArgumentException e) {
                                throw new RuntimeException(e);
                            }
                            this.proteinMW = protein.getMass();
                        }
                    }
                }
            }
        }

        this.experimentType = new ModelObjectShortBean(experiment.getExperimentType());
        if (experiment.getProtocol() == null) {
            this.protocol = null;
        } else {
            this.protocol = new ModelObjectShortBean(experiment.getProtocol());
        }
        this.experimentDate = experiment.getStartDate();
        this.pimsExperimentHook = experiment.get_Hook();
        //this.dbId = experiment.getDbId();

        this.inputBeans = new HashSet<InputSampleBean>();
        for (final InputSample inputSample : experiment.getInputSamples()) {
            this.inputBeans.add(new InputSampleBean(inputSample));
        }

    }

    //public Long getDbId() {
    //    return this.dbId;
    //}

    @Deprecated
    public String getExperimentName() {
        return this.getName();
    }

    public String getExperimentHook() {
        return this.pimsExperimentHook;
    }

    public String getExperimentStatus() {
        return this.status;
    }

    public void setExperimentStatus(final String name) {
        this.status = name;
    }

    public Calendar getExperimentDate() {
        return this.experimentDate;
    }

    public ModelObjectShortBean getExperimentType() {
        return this.experimentType;
    }

    public String getExperimentTypeName() {
        return this.experimentType.getName();
    }

    public String getExperimentTypeHook() {
        return this.experimentType.getHook();
    }

    public String getExperimentProtocolName() {
        if (null == this.protocol) {
            return "";
        }
        return this.protocol.getName();
    }

    public String getExperimentProtocolHook() {
        if (null == this.protocol) {
            return "";
        }
        return this.protocol.getHook();
    }

    @Deprecated
    public String getStartDateOfExperimentString() {
        return Utils.getDateFormat().format(this.experiment.getStartDate().getTime());
    }

    @Deprecated
    public String getEndDateOfExperimentString() {
        return Utils.getDateFormat().format(this.experiment.getEndDate().getTime());
    }

    public String getStatus() {
        return (String) super.getValues().get(Experiment.PROP_STATUS);
    }

    public String getDetails() {
        return (String) super.getValues().get(LabBookEntry.PROP_DETAILS);
    }

    public String getBluePrint() {
        return this.getBluePrintName();
    }

    public String getBluePrintName() {
        if (null != this.researchObjective) {
            return this.researchObjective.getName();
        }
        return "";
    }

    public String getBluePrintHook() {
        if (null != this.researchObjective) {
            return this.researchObjective.getHook();
        }
        return "";
    }

    public Object getPcrProductSize() {
        return this.pcrProductSize;
    }

    public String getProteinMW() {
        if (null == this.proteinMW) {
            return "Not set";
        }
        final int i = this.proteinMW.intValue();
        final DecimalFormat myFormatter = new DecimalFormat("#####");
        final String result = myFormatter.format(i / 1000);
        return result + " kDa";
    }

    @Deprecated
    public ModelObject getModelObject() {
        return this.experiment;
    }

    public Collection<InputSampleBean> getInputSampleBeans() {
        return this.inputBeans;
    }

    /**
     * ExperimentBean.getMilestone
     * 
     * @return
     */
    public ModelObjectShortBean getMilestone() {
        return this.milestone;
    }

    public boolean getHasMilestone() {
        if (null == this.milestone) {
            return false;
        }
        return true;
    }

    /**
     * ExperimentBean.getProtocol
     * 
     * @return
     */
    public ModelObjectShortBean getProtocol() {
        return this.protocol;
    }

}
