package org.pimslims.presentation.plateExperiment;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.experiment.ExperimentBean;
import org.pimslims.presentation.experiment.InputSampleBean;
import org.pimslims.presentation.experiment.OutputSampleBean;

public class PlateExperimentBean extends ExperimentBean implements Comparable<Object>, Serializable {

    private final Collection<OutputSampleBean> outputBeans;

    private final Collection<PlateExperimentParameterBean> parameterBeans;

    private final int row;

    private final int column;

    public PlateExperimentBean(final Experiment experiment) {

        super(experiment);

        this.outputBeans = new HashSet<OutputSampleBean>();
        for (final OutputSample outputSample : experiment.getOutputSamples()) {
            this.outputBeans.add(new OutputSampleBean(outputSample));
        }

        this.parameterBeans = new HashSet<PlateExperimentParameterBean>();
        for (final Parameter parameter : experiment.getParameters()) {
            this.parameterBeans.add(new PlateExperimentParameterBean(parameter));
        }

        this.row = HolderFactory.getRow(experiment);
        this.column = HolderFactory.getColumn(experiment);
    }

    public void clearInputSampleBeans() {
        this.inputBeans.clear();
    }

    public void addInputSampleBean(final InputSample inputSample) {
        this.inputBeans.add(new InputSampleBean(inputSample));
    }

    public Collection<OutputSampleBean> getOutputSampleBeans() {
        return this.outputBeans;
    }

    public void clearOutputSampleBeans() {
        this.outputBeans.clear();
    }

    public Collection<PlateExperimentParameterBean> getParameterBeans() {
        return this.parameterBeans;
    }

    public void clearParameterBeans() {
        this.parameterBeans.clear();
    }

    public void addParameterBean(final Parameter parameter) {
        this.parameterBeans.add(new PlateExperimentParameterBean(parameter));
    }

    /**
     * ExperimentBean.getHolder
     * 
     * @return
     */
    public ModelObjectShortBean getHolder() {
        if (this.outputBeans.iterator().hasNext()) {
            final OutputSampleBean bean = this.outputBeans.iterator().next();
            return bean.getHolder();
        }
        return null;
    }

    /**
     * ExperimentBean.getRow
     * 
     * @return
     */
    public int getRow() {
        return this.row;
    }

    /**
     * ExperimentBean.getColumn
     * 
     * @return
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * 
     * PlateExperimentBean.compareTo
     * 
     * @see org.pimslims.presentation.experiment.ExperimentBean#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final Object o1) {
        final PlateExperimentBean bean = (PlateExperimentBean) o1;
        return this.getDbId().compareTo(bean.getDbId());
    }
}
