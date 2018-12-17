/**
 * V5_0-web org.pimslims.lab NMR.java
 * 
 * @author cm65
 * @date 20 Dec 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.lab;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.sample.Sample;

/**
 * NMR
 * 
 */
public class NMR {

    public static boolean isNmr(final Experiment experiment) {
        return NMR.EXPERIMENT_TYPE.equals(experiment.getExperimentType().getName());
    }

    public static boolean isNmr(final Protocol protocol) {
        return NMR.EXPERIMENT_TYPE.equals(protocol.getExperimentType().getName());
    }

    public static String getIsotopicLabelling(final Protocol protocol) {
        final ParameterDefinition def =
            protocol.findFirst(Protocol.PROP_PARAMETERDEFINITIONS,
                org.pimslims.model.protocol.ParameterDefinition.PROP_NAME, NMR.PARAMETER_DEFINITION);
        if (null == def) {
            return null;
        }
        return def.getDefaultValue();
    }

    // e.g. 2H13C
    public static Pattern LABELLING = Pattern.compile(".*?((\\d+[A-Z])+).*");

    public static String getIsotopicLabelling(final Sample sample) {
        final Matcher m = NMR.LABELLING.matcher(sample.getName());
        if (!m.matches()) {
            return null;
        }
        return m.group(1);
    }

    public static boolean isSuitable(final Sample sample, final Protocol protocol) {
        final String label = NMR.getIsotopicLabelling(sample);
        if (null == label) {
            return null == NMR.getIsotopicLabelling(protocol);
        } else {
            return label.equals(NMR.getIsotopicLabelling(protocol));
        }
    }

    /**
     * PARAMETER_DEFINITION String
     */
    public static final String PARAMETER_DEFINITION = "Isotopic Labelling";

    /**
     * EXPERIMENT_TYPE String
     */
    public static final String EXPERIMENT_TYPE = "NMR";
}
