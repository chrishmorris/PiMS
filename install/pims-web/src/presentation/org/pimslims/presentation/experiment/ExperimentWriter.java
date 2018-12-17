package org.pimslims.presentation.experiment;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;

/**
 * business delegate for experiments - connects a gui classes with datamodel classes; It is a good place for
 * business logic and for mapping between gui action forms beans and datamodel objects.
 * 
 * @author Ekaterina Pilicheva
 * @since 31 May 2006
 * 
 *        Instead make these methods not static, and use this.rv or this.wv The above was DONE by Peter
 *        Troshin in August 2006
 * 
 */

public final class ExperimentWriter {

    // provides static methods only
    private ExperimentWriter() throws AccessException {
        super();
    }

    public static void createOutputSamplesForExperiment(final WritableVersion wv,
        final Experiment experiment, final Protocol protocol) throws AccessException, ConstraintException {

        // find all the existing ones
        final Collection<OutputSample> outputSamples = experiment.getOutputSamples();
        final Collection<String> names = new HashSet(outputSamples.size());
        final Collection<RefOutputSample> refOutputSamples =
            new HashSet<RefOutputSample>(outputSamples.size());
        for (final Iterator iter = outputSamples.iterator(); iter.hasNext();) {
            final OutputSample os = (OutputSample) iter.next();
            names.add(os.getName());
            refOutputSamples.add(os.getRefOutputSample());
        }
        final Collection<RefOutputSample> ross = protocol.getRefOutputSamples();

        for (final RefOutputSample ros : ross) {
            String name = experiment.getName();
            if (1 < ross.size()) {
                name = name + "." + ros.getName();
            }
            if (null == experiment.getName() || "".equals(experiment.getName())) {
                name =
                    experiment.getExperimentType().getName() + " " + experiment.getDbId() + " "
                        + ros.getName();
            }
            final Float amount = ros.getAmount();
            final String unit = ros.getUnit();

            if (names.contains(name) || refOutputSamples.contains(ros)) {
                continue;
            } // don't make duplicate

            final Map<String, Object> attrMap = new HashMap<String, Object>();
            attrMap.put(OutputSample.PROP_EXPERIMENT, experiment);
            attrMap.put(OutputSample.PROP_REFOUTPUTSAMPLE, ros);
            //attrMap.put(OutputSample.PROP_NAME, name != null ? name.toString() : ExperimentReader.NONE);
            attrMap.put(OutputSample.PROP_NAME, ros.get_Name());
            attrMap.put(OutputSample.PROP_AMOUNT, amount);
            attrMap.put(OutputSample.PROP_AMOUNTUNIT, unit);
            final OutputSample os = (OutputSample) wv.create(OutputSample.class, attrMap);

            // make a sample if possible
            final SampleCategory ref = ros.getSampleCategory();
            /*
             * if (null==ref) { //LATER a data model change will make this test unnecessary continue; // can't
             * make a sample without sample categories }
             */
            final Sample sample = new Sample(wv, name);
            sample.setOutputSample(os);
            sample.setIsActive(Boolean.TRUE);

            wv.flush();
            final Set<SampleCategory> categories = Collections.singleton(ref);
            sample.setSampleCategories(categories);
            wv.flush();

            // copy values from the RefSample and OutputSample
            sample.setAmountDisplayUnit(ros.getDisplayUnit());
            sample.setAmountUnit(ros.getUnit());
            sample.setInitialAmount(ros.getAmount());
            sample.setCurrentAmount(ros.getAmount());
            sample.setIsActive(true);
            // LATER sample.setSampleComponents();
        }

    }// EndOf createOutputSamplesForExperiment

}
