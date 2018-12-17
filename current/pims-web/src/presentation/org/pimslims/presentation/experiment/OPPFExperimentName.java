/**
 * pims-web org.pimslims.presentation.experiment OPPFExperimentName.java
 * 
 * @author Marc Savitsky
 * @date 9 May 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky * * either *
 * 
 * 
 */
package org.pimslims.presentation.experiment;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.protocol.Protocol;

/**
 * OPPFExperimentName
 * 
 */
public class OPPFExperimentName implements ExperimentNameFactory {

    public String suggestExperimentName(final ReadableVersion version, final Experiment experiment,
        final String inputSampleName) {
        // OPPF request - experiment name defaults to constructId + experimentType.name + n
        String string = null;
        if (null != experiment.getResearchObjective()) {
            string =
                new String(experiment.getResearchObjective().getLocalName() + " "
                    + experiment.getExperimentType().get_Name());
        } else {
            string = new String(experiment.getExperimentType().get_Name());
        }

        return version.getUniqueName(Experiment.class, string);
    }

    public String suggestExperimentName(final ReadableVersion version, final Protocol protocol) {
        final String experimentName = new String(protocol.getExperimentType().get_Name());
        return version.getUniqueName(Experiment.class, experimentName);
    }

    /**
     * ExperimentNameFactory.suggestExperimentName
     * 
     * @see org.pimslims.presentation.experiment.ExperimentNameFactory#suggestExperimentName(org.pimslims.dao.ReadableVersion,
     *      org.pimslims.model.protocol.Protocol, org.pimslims.model.experiment.Project)
     */
    public String suggestExperimentName(final ReadableVersion version, final Protocol protocol,
        final Project project) {
        // TODO could use project (contruct) name
        return this.suggestExperimentName(version, protocol);
    }

}
