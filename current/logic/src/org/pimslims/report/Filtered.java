/**
 * V5_0-web org.pimslims.presentation Filtered.java
 * 
 * @author cm65
 * @date 10 Sep 2013
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.report;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.reference.SampleCategory;

/**
 * Filtered
 * 
 */
public abstract class Filtered {

    public abstract boolean getFilteredOut();

    public static final Filtered OUT = new Filtered() {
        @Override
        public boolean getFilteredOut() {
            return true;
        }

        /**
         * .toString
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "Rejected";
        }

    };

    public static final Filtered IN = new Filtered() {
        @Override
        public boolean getFilteredOut() {
            return false;
        }

        /**
         * .toString
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "Accepted";
        }
    };

    /**
     * T2CReport.getFilterMap
     * 
     * @param beans
     * @return
     */
    public static Map<String, Filtered> getExperimentFilterMap(final List<Experiment> experiments,
        final Set<String> excludedHooks, final Set<String> products) {
        final Map<String, Filtered> ret = new HashMap(experiments.size());
        experiments: for (final Iterator iterator = experiments.iterator(); iterator.hasNext();) {
            final Experiment experiment = (Experiment) iterator.next();

            if (excludedHooks.contains(experiment.get_Hook())) {
                // this particular experiment has been hidden
                ret.put(experiment.get_Hook(), OUT);
                continue;
            }
            if (null == products) {
                ret.put(experiment.get_Hook(), IN);
                continue;
            }
            // also filtering by outputs
            final Collection<OutputSample> oss = experiment.getOutputSamples();
            if (oss.isEmpty()) {
                // experiment is characterisation TODO add check box
                ret.put(experiment.get_Hook(), IN);
                continue;
            }
            for (final Iterator iterator2 = oss.iterator(); iterator2.hasNext();) {
                final OutputSample os = (OutputSample) iterator2.next();
                SampleCategory cat = null;
                if (null != os.getRefOutputSample()) {
                    cat = os.getRefOutputSample().getSampleCategory();
                } else if (null != os.getSample() && 1 == os.getSample().getSampleCategories().size()) {
                    cat = os.getSample().getSampleCategories().iterator().next();
                }
                if (null == cat || products.contains(cat.getName())) {
                    ret.put(experiment.get_Hook(), IN);
                    continue experiments;
                }
            }
            // experiment has no outputs of wanted categories
            ret.put(experiment.get_Hook(), OUT);
        }
        return ret;
    }

}
