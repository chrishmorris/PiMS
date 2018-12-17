package org.pimslims.presentation.experiment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.pimslims.lab.Utils;
import org.pimslims.lab.experiment.ParameterComparator;
import org.pimslims.lab.file.CaliperFile;
import org.pimslims.lab.sequence.ProteinSequence;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.protocol.ProtocolBean;

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

    private final ProtocolBean protocol;

    private List<OutputSampleBean> outputBeans;

    private final List<ParameterBean> parameters;

    private final String method;

    @Override
    public int compareTo(final Object obj) {

        if (!(obj instanceof ExperimentBean)) {
            if (obj instanceof ModelObjectBean) {
                return super.compareTo(obj);
            }
            throw new ClassCastException();
        }

        final Calendar argCmDate = ((ExperimentBean) obj).experimentDate;
        final Calendar thisCmDate = this.experimentDate;

        if (thisCmDate.after(argCmDate)) {
            return -1;
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
        this.processOutputSamples();
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
            this.method = null;
        } else {
            this.protocol = new ProtocolBean(experiment.getProtocol());
            this.method = experiment.getProtocol().getMethodDescription();
        }
        this.experimentDate = experiment.getStartDate();
        this.pimsExperimentHook = experiment.get_Hook();
        //this.dbId = experiment.getDbId();

        this.inputBeans = new HashSet<InputSampleBean>();
        for (final InputSample inputSample : experiment.getInputSamples()) {
            this.inputBeans.add(new InputSampleBean(inputSample));
        }

        this.parameters = new ArrayList(experiment.getParameters().size());
        final List<Parameter> parameters2 = new ArrayList(experiment.getParameters());
        Collections.sort(parameters2, new ParameterComparator());
        for (final Iterator iterator = parameters2.iterator(); iterator.hasNext();) {
            final Parameter parameter = (Parameter) iterator.next();
            this.parameters.add(new ParameterBean(parameter));
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

    public Collection<OutputSampleBean> getOutputSampleBeans() {
        return this.outputBeans;
    }

    private void processOutputSamples() {
        final List<OutputSampleBean> ret = new ArrayList<OutputSampleBean>();

        final Collection<OutputSample> outputs = this.experiment.getOutputSamples();
        for (final Iterator i = outputs.iterator(); i.hasNext();) {
            final OutputSample outputSample = (OutputSample) i.next();
            final OutputSampleBean outputSampleBean = new OutputSampleBean(outputSample);
            ret.add(outputSampleBean);
        }
        this.outputBeans = ret;
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
    public ModelObjectBean getProtocol() {
        return this.protocol;
    }

    /**
     * ExperimentBean.getMenu
     * 
     * @see org.pimslims.presentation.ModelObjectShortBean#getMenu()
     */
    @Override
    public String getMenu() {
        // TODO Auto-generated method stub
        return super.getMenu();
    }

    /**
     * ExperimentBean.getParameters
     */
    public List<ParameterBean> getParameters() {
        return this.parameters;
    }

    /**
     * ExperimentBean.getMethod
     */
    public String getMethod() {
        return this.method;
    }

}
