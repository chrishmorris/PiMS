/**
 * pims-web org.pimslims.presentation.experiment DefaultExperimentName.java
 * 
 * @author Marc Savitsky
 * @date 9 May 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky * * either *
 * 
 */
package org.pimslims.presentation.experiment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.protocol.Protocol;

/**
 * DefaultExperimentName
 * 
 */

public class DefaultExperimentName implements ExperimentNameFactory {

    private static final Pattern CONSTRUCT_NAME = Pattern.compile("([^-]*)(-.*)?");

    public String suggestExperimentName(final ReadableVersion version, final Experiment experiment,
        final String inputSampleName) {

        final String typeName = experiment.getExperimentType().get_Name();
        String constructName = typeName;
        if (null != experiment.getProject()) {
            constructName = experiment.getProject().getName();
        }
        if (null != inputSampleName) {
            constructName = DefaultExperimentName.makeConstructName(inputSampleName);
        }
        final String experimentName = constructName + "-" + DefaultExperimentName.makeAcronym(typeName);
        return version.getUniqueName(Experiment.class, experimentName);
    }

    public String suggestExperimentName(final ReadableVersion version, final Protocol protocol) {
        final String experimentName = new String(protocol.getExperimentType().get_Name());
        return version.getUniqueName(Experiment.class, experimentName);
    }

    public static String makeConstructName(final String inputSampleName) {
        String constructName = inputSampleName;
        final Matcher m = DefaultExperimentName.CONSTRUCT_NAME.matcher(inputSampleName);
        if (m.matches()) {
            constructName = m.group(1);
        }

        final Matcher ml = DefaultExperimentName.LABELLING.matcher(inputSampleName);
        if (ml.matches() && !DefaultExperimentName.LABELLING.matcher(constructName).matches()) {
            constructName = constructName + "-" + ml.group(1);
        }
        return constructName;
    }

    public static Pattern LABELLING = Pattern.compile(".*?((\\d+[A-Z])+).*"); //TODO NMR.Labelling

    private static final Pattern INITIAL_LETTER = Pattern.compile("\\s*(\\S)\\S*");

    /**
     * DefaultExperimentName.makeAcronym
     * 
     * @param name
     * @return a three letter abbreviation for the name
     */
    public static String makeAcronym(final String name) {
        final java.util.regex.Matcher m = DefaultExperimentName.INITIAL_LETTER.matcher(name);
        String ret = "";
        if (m.find()) {
            ret += m.group(1);
            if (m.find()) {
                ret += m.group(1);
                if (m.find()) {
                    ret += m.group(1);
                }
            } else {
                // just one word
                final int length = Math.min(3, name.length());
                return name.substring(0, length);
            }
        }
        return ret;
    }

    /**
     * ExperimentNameFactory.suggestExperimentName
     * 
     * @see org.pimslims.presentation.experiment.ExperimentNameFactory#suggestExperimentName(org.pimslims.dao.ReadableVersion,
     *      org.pimslims.model.protocol.Protocol, org.pimslims.model.experiment.Project)
     */
    public String suggestExperimentName(final ReadableVersion version, final Protocol protocol,
        final Project construct) {
        final String experimentName = construct.getName() + " " + protocol.getExperimentType().getName();
        return version.getUniqueName(Experiment.class, experimentName);
    }

}
