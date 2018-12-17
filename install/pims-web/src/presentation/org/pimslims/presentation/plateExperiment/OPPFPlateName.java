/**
 * pims-web org.pimslims.presentation.plateExperiment OPPFPlateName.java
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

/**
 * OPPFPlateName
 * 
 */
public class OPPFPlateName implements PlateNameFactory {

    public String suggestPlateName(final ReadableVersion version, final Holder holder, final Protocol protocol) {

        System.out.println("OPPFPlateName.suggestPlateName");
        // OPPF request - plate experiment name defaults to PCRnnn + protocol name + n
        //String plateNumber = null;
        //String protocolName = null;
        String experimentTypeName = null;

        //if (null != holder) {
        //    plateNumber = holder.getName().substring(0, 6);
        // }
        if (null != protocol) {
            //final int i = protocol.getName().lastIndexOf("-");
            //if (i > 0) {
            //    protocolName = protocol.getName().substring(i + 2);
            //} else {
            //    protocolName = protocol.getName().substring(i + 1);
            //}
            experimentTypeName = protocol.getExperimentType().get_Name();
        }

        return version.getUniqueName(ExperimentGroup.class, "PCRnnn " + experimentTypeName);
    }
}
