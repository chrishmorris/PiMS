/**
 * 31-pims-web org.pimslims.utils.sequenator RoboticsTemplateHelper.java
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
import org.pimslims.utils.experiment.Utils;

/**
 * RoboticsTemplateHelper
 * 
 */

public class RoboticsTemplateHelper implements DefaultsHelper {

    private DefaultsHelper.ExperimentType etype = null;

    /**
     * DefaultsHelper.getPremixVol
     * 
     * @see org.pimslims.utils.sequenator.DefaultsHelper#getPremixVol()
     */
    public int getPremixVol() {
        switch (this.etype) {
            case IControl:
                return 0;
            case RControl:
                //leaking deliberately
            case Sample:
                //leaking deliberately
            default:
                return 8;
        }
    }

    /**
     * DefaultsHelper.getPrimerVol
     * 
     * @see org.pimslims.utils.sequenator.DefaultsHelper#getPrimerVol()
     */
    public int getPrimerVol() {
        switch (this.etype) {
            case IControl:
                return 0;
            case RControl:
                return 2;
            case Sample:
                //leaking deliberately
            default:
                return 4;
        }
    }

    /**
     * DefaultsHelper.getTemplateVol
     * 
     * @see org.pimslims.utils.sequenator.DefaultsHelper#getTemplateVol()
     */
    public int getTemplateVol() {
        switch (this.etype) {
            case IControl:
                return 0;
            case RControl:
                return 3;
            case Sample:
                //leaking deliberately
            default:
                return 4;
        }
    }

    /**
     * DefaultsHelper.getTotalVol
     * 
     * @see org.pimslims.utils.sequenator.DefaultsHelper#getTotalVol()
     */
    public int getTotalVol() {
        switch (this.etype) {
            case IControl:
                return 0;
            case RControl:
                //leaking deliberately
            case Sample:
                //leaking deliberately
            default:
                return 20;
        }
    }

    /**
     * DefaultsHelper.setExperiment
     * 
     * @see org.pimslims.utils.sequenator.DefaultsHelper#setExperiment(org.pimslims.model.experiment.Experiment)
     */
    public void setExperiment(final Experiment experiment) {

        if (Utils.isReactionControl(experiment)) {
            this.etype = DefaultsHelper.ExperimentType.RControl;
        } else if (Utils.isInstrumentControl(experiment)) {
            this.etype = DefaultsHelper.ExperimentType.IControl;
        } else {
            this.etype = DefaultsHelper.ExperimentType.Sample;
        }

    }

}