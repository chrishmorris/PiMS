/**
 * pims-web org.pimslims.presentation.plateExperiment DefaultPlateName.java
 * 
 * @author Marc Savitsky
 * @date 5 Feb 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation.plateExperiment;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.presentation.experiment.DefaultExperimentName;

/**
 * DefaultPlateName
 * 
 */
public class DefaultPlateName implements PlateNameFactory {

    public String suggestPlateName(final ReadableVersion version, final Holder holder, final Protocol protocol) {
        //return DefaultPlateName.makeName(version, protocol.getName());
        return version.getUniqueName(ExperimentGroup.class, protocol.getExperimentType().get_Name() + "-"
            + DefaultExperimentName.makeAcronym(protocol.getExperimentType().get_Name()));
    }
}
