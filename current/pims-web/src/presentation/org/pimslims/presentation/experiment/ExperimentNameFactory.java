/**
 * pims-web org.pimslims.presentation.experiment ExperimentNameFactory.java
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

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.protocol.Protocol;

/**
 * ExperimentNameFactory
 * 
 */
public interface ExperimentNameFactory {

    String suggestExperimentName(ReadableVersion version, Experiment experiment, String inputSampleName);

    String suggestExperimentName(ReadableVersion version, Protocol protocol);

    String suggestExperimentName(ReadableVersion version, Protocol protocol, Project project);
}
