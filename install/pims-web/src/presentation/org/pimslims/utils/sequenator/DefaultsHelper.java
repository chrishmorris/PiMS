/**
 * 31-pims-web org.pimslims.utils.sequenator DefaultsHelper.java
 * 
 * @author pvt43
 * @date 29 May 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils.sequenator;

import org.pimslims.model.experiment.Experiment;

/**
 * DefaultsHelper
 * 
 */
public interface DefaultsHelper {

    static enum ExperimentType {
        RControl, IControl, Sample
    }

    int getPremixVol();

    int getTemplateVol();

    int getPrimerVol();

    int getTotalVol();

    void setExperiment(Experiment experiment);

}
