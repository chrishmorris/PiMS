package org.pimslims.presentation.experiment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.lab.experiment.ExperimentTypeUtil;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.mru.MRUController;

/**
 * @author cm65
 * 
 */
public class ExperimentReader {

    public static final String NONE = MRUController.NONE;

    public static final int MAX_SAMPLESFORLIST = 20;

    /**
     * The current transaction
     */
    protected final ReadableVersion rv;

    /**
     * The experiment the user is editing
     */
    protected final Experiment experiment;

    /**
     * @param rv the current transaction
     * @throws AccessException
     */
    public ExperimentReader(final ReadableVersion rv, final Experiment experiment) throws AccessException {
        this.rv = rv;
        this.experiment = experiment;
    }

    /**
     * Note that this method is inefficient if used for an experiment group, since it will search for each
     * well.
     * 
     * @param cache place to save result of possible samples calculation
     * @return beans representing the input samples
     * @throws AccessException
     */
    public static List<InputSampleBean> getInputSamples(final Experiment experiment, final String userName,
        final Collection<Sample> except) throws AccessException {

        final List<InputSampleBean> ret = new ArrayList<InputSampleBean>();

        for (final Iterator i = experiment.getInputSamples().iterator(); i.hasNext();) {
            final InputSample inputSample = (InputSample) i.next();

            Collection<Sample> samples = new ArrayList();
            if (null != inputSample.getRefInputSample()) {
                samples =
                //ExperimentReader.getPossible(inputSample.getRefInputSample().getSampleCategory(), except);
                    ExperimentReader.getPossible(inputSample.getRefInputSample(), inputSample.getSample(),
                        except);

            }
            final InputSampleBean inputSampleBean = new InputSampleBean(inputSample, samples);
            ret.add(inputSampleBean);
        }

        return ret;

    }// EndOf getInputSamples

    /**
     * @param category
     * @param except
     * @return
     */
    /*
    public static Collection<Sample> getPossible(final SampleCategory category,
        final Collection<Sample> except) {
        // find samples that could be used

        final Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put(AbstractSample.PROP_ISACTIVE, true);
        criteria.put(AbstractSample.PROP_SAMPLECATEGORIES, category);

        final Collection<Sample> samples = category.get_Version().findAll(Sample.class, criteria);
        samples.removeAll(except);
        return samples;
    }
    */
    public static Collection<Sample> getPossible(final RefInputSample refInputSample, final Sample sample,
        final Collection<Sample> except) {

        // find samples that could be used
        Collection<Sample> samples = new HashSet<Sample>();

        final Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put(AbstractSample.PROP_ISACTIVE, true);
        criteria.put(AbstractSample.PROP_SAMPLECATEGORIES, refInputSample.getSampleCategory());

        String hook = null;
        if (null != sample) {
            hook = sample.get_Hook();
        }

        final int count = refInputSample.get_Version().count(Sample.class, criteria);

        if (count < ExperimentReader.MAX_SAMPLESFORLIST) {
            samples = refInputSample.get_Version().findAll(Sample.class, criteria);

        } else {

            final Map<String, String> mrus =
                MRUController.getPossibleMRUItems(refInputSample.get_Version(), Sample.class.getName(), hook,
                    refInputSample.getSampleCategory(), true);

            final Collection<String> keys = mrus.keySet();
            for (final String key : keys) {
                final Sample mySample = refInputSample.get_Version().get(key);
                samples.add(mySample);
            }
        }
        samples.removeAll(except);
        final Protocol protocol = refInputSample.getProtocol();
        if (!org.pimslims.lab.NMR.isNmr(protocol)) {
            return samples;
        } else {
            final Collection<Sample> ret = new ArrayList(samples.size());
            for (final Iterator iterator = samples.iterator(); iterator.hasNext();) {
                final Sample s = (Sample) iterator.next();
                if (org.pimslims.lab.NMR.isSuitable(s, protocol)) {
                    ret.add(s);
                }
            }
            return ret;
        }
    }

    /**
     * @param experiment2
     * @return the outputs from this experiment and the others in the same group if any
     */
    public static Set<Sample> getOutputsFromGroup(final Experiment experiment2) {
        Set<Experiment> experiments = Collections.singleton(experiment2);
        if (null != experiment2.getExperimentGroup()) {
            experiments = experiment2.getExperimentGroup().getExperiments();
        }
        final Set<OutputSample> outputs = new HashSet(experiments.size());
        for (final Iterator iterator = experiments.iterator(); iterator.hasNext();) {
            final Experiment exp = (Experiment) iterator.next();
            outputs.addAll(exp.getOutputSamples());
        }
        final Set<Sample> ret = new HashSet(outputs.size());
        for (final Iterator iterator = outputs.iterator(); iterator.hasNext();) {
            final OutputSample outputSample = (OutputSample) iterator.next();
            if (null != outputSample.getSample()) {
                ret.add(outputSample.getSample());
            }
        }
        return ret;
    }

    public List<OutputSampleBean> getOutputSamples() throws AccessException {
        final List<OutputSampleBean> ret = new ArrayList<OutputSampleBean>();

        final Collection<OutputSample> outputs = this.experiment.getOutputSamples();
        for (final Iterator i = outputs.iterator(); i.hasNext();) {
            final OutputSample outputSample = (OutputSample) i.next();
            final OutputSampleBean outputSampleBean = new OutputSampleBean(outputSample);
            ret.add(outputSampleBean);
        }
        return ret;
    }// EndOf getOutputSamples

    public Boolean get_MayUpdate() {
        if (null == this.experiment) {
            return false;
        }
        return this.experiment.get_MayUpdate();
    }

    public Experiment getExperiment() {
        return this.experiment;
    }

    public Holder getPlate() {
        final ExperimentGroup group = this.experiment.getExperimentGroup();
        if (null == group) {
            return null;
        }
        return HolderFactory.getPlate(group);
    }

    public Object getOwner() {
        if (null == this.experiment) {
            return "";
        }
        return this.experiment.get_Owner();
    }

    /**
     * @param experiment
     * @return the parameters, in alphabetical order
     */
    public static List<Parameter> getParameters(final Experiment experiment) {
        final List<Parameter> parameters = new ArrayList(experiment.getParameters());
        Collections.sort(parameters, ExperimentTypeUtil.ALPHABETICAL_ORDER_OF_NAME);
        return parameters;
    }
}
