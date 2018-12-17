/**
 * current-pims-web org.pimslims.presentation ExperimentUpdateBean.java
 * 
 * @author cm65
 * @date 4 Mar 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.pimslims.exception.AccessException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.experiment.ExperimentReader;
import org.pimslims.presentation.experiment.InputSampleBean;
import org.pimslims.presentation.experiment.OutputSampleBean;
import org.pimslims.presentation.experiment.ParameterBean;

/**
 * ExperimentUpdateBean
 * 
 * Describes the changes to an experiment in a transaction. Used to make a JSON response.
 * 
 */
public class ExperimentUpdateBean extends WellExperimentBean implements UpdateBean {

    /**
     * attribute name => new value
     */
    private final Map<String, Object> changed = new HashMap<String, Object>();

    /**
     * Beans for all the input samples which have changed
     */
    private final Set updatedInputSamples = new HashSet<InputSampleBean>();

    /**
     * Beans for all the output samples which have changed (At the time of writing the experiment must have
     * exactly one output sample, but this may change.)
     */
    private final Set updatedOutputSamples = new HashSet<OutputSampleBean>();

    /**
     * Beans for all the parameters samples which have changed
     */
    private final Set updatedParameters = new HashSet<ParameterBean>();

    /**
     * @param experiment
     * @param holderName
     * @param holderHook
     * @param row
     * @param column
     * @param targetName
     * @param targetHook
     * @param changed e.g. Experiment.PROP_NAME => "Chris' experiment"
     * @throws AccessException
     */
    private ExperimentUpdateBean(final Experiment experiment, final Map<String, Object> changed)
        throws AccessException {
        super(experiment, null, null, null, null, -1, -1, Collections.EMPTY_MAP); //TODO samplesFOrCategory
        // note we set cache to null - probably not many experiments changed
        // could improve performance by making caller provide one
        this.changed.putAll(changed);
    }

    /**
     * @param experiment
     * @throws AccessException
     */
    private ExperimentUpdateBean(final Experiment experiment) throws AccessException {
        this(experiment, Collections.EMPTY_MAP);
    }

    /**
     * @see org.pimslims.presentation.UpdateBean#getChanged()
     * @return the update attributes and roles
     */
    public Map<String, Object> getChanged() {
        return this.changed;
    }

    /**
     * @param inputSampleBean
     */
    private void addUpdatedInputSample(final InputSampleBean inputSampleBean) {
        this.updatedInputSamples.add(inputSampleBean);
    }

    /**
     * @param outputSampleBean
     */
    private void addUpdatedOutputSample(final OutputSampleBean outputSampleBean) {
        this.updatedOutputSamples.add(outputSampleBean);
    }

    /**
     * @param parameterBean
     */
    private void addUpdatedParameter(final ParameterBean parameterBean) {
        this.updatedParameters.add(parameterBean);
    }

    /**
     * @param value
     */
    private void addUpdates(final Map<String, Object> updates) {
        this.changed.putAll(updates);
    }

    /**
     * @return
     */
    public Set<ExperimentUpdateBean> getUpdatedOutputSamples() {
        return new HashSet(this.updatedOutputSamples);
    }

    /**
     * @return the updated input samples
     */
    public Set<InputSampleBean> getUpdatedInputSamples() {
        return new HashSet(this.updatedInputSamples);
    }

    /**
     * @return
     */
    public Set<ParameterBean> getUpdatedParameters() {
        return new HashSet(this.updatedParameters);
    }

    /**
     * Return details of an update to an experiment group
     * 
     * @param changed a map of model object => attribute => new value
     * @param cache place to save possible samples
     * @return beans representing the changed experiments
     * @throws AccessException
     */
    public static Set<ExperimentUpdateBean> getBeans(final String userName,
        final Map<ModelObject, Map<String, Object>> changed) throws AccessException {
        final Map<Experiment, ExperimentUpdateBean> ret = new HashMap(changed.size());
        //final Map<SampleCategory, List<SampleBean>> cache = new HashMap();

        for (final Iterator iterator = changed.entrySet().iterator(); iterator.hasNext();) {
            final Map.Entry<ModelObject, Map<String, Object>> entry =
                (Map.Entry<ModelObject, Map<String, Object>>) iterator.next();
            if (entry.getKey() instanceof Experiment) {
                final Experiment experiment = (Experiment) entry.getKey();
                final ExperimentUpdateBean bean = ExperimentUpdateBean.getBean(ret, experiment);
                bean.addUpdates(entry.getValue());
            } else if (entry.getKey() instanceof Parameter) {
                final Parameter parm = (Parameter) entry.getKey();
                final Experiment experiment = parm.getExperiment();
                final ExperimentUpdateBean bean = ExperimentUpdateBean.getBean(ret, experiment);
                bean.addUpdatedParameter(new ParameterBean(parm));
            } else if (entry.getKey() instanceof InputSample) {
                final InputSample is = (InputSample) entry.getKey();
                final Experiment experiment = is.getExperiment();
                final ExperimentUpdateBean bean = ExperimentUpdateBean.getBean(ret, experiment);
                Collection<Sample> samples = new ArrayList();
                if (null != is.getRefInputSample()) {
                    // TODO cache this result
                    final Collection<Sample> except =
                        ExperimentReader.getOutputsFromGroup(is.getExperiment());
                    samples =
                    //ExperimentReader.getPossible(is.getRefInputSample().getSampleCategory(), except);
                        ExperimentReader.getPossible(is.getRefInputSample(), is.getSample(), except);
                }
                final InputSampleBean inputSampleBean = new InputSampleBean(is, samples);
                bean.addUpdatedInputSample(inputSampleBean);
            } else if (entry.getKey() instanceof OutputSample) {
                final OutputSample os = (OutputSample) entry.getKey();
                final Experiment experiment = os.getExperiment();
                final ExperimentUpdateBean bean = ExperimentUpdateBean.getBean(ret, experiment);
                bean.addUpdatedOutputSample(new OutputSampleBean(os));
            } else {
                throw new RuntimeException("Unexpected type: " + entry.getKey());
            }
        }
        return new HashSet(ret.values());
    }

    /**
     * @param ret
     * @param experiment
     * @return
     * @throws AccessException
     */
    private static ExperimentUpdateBean getBean(final Map<Experiment, ExperimentUpdateBean> ret,
        final Experiment experiment) throws AccessException {
        if (!ret.containsKey(experiment)) {
            ret.put(experiment, new ExperimentUpdateBean(experiment));
        }
        final ExperimentUpdateBean bean = ret.get(experiment);
        return bean;
    }

}
