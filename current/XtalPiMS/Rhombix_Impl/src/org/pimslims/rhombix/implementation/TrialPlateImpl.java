/**
 * Rhombix_Impl org.pimslims.rhombix TrialPlateImpl.java
 * 
 * @author cm65
 * @date 21 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.implementation;

import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.TrialPlate;

/**
 * TrialPlateImpl
 * 
 */
public class TrialPlateImpl extends TrialPlate {

    private long rhombixExperimentId;

    /**
     * Constructor for TrialPlateImpl
     * 
     * @param plateType
     */
    TrialPlateImpl(PlateType plateType) {
        super(plateType);
    }

    /**
     * TrialPlateImpl.getRhombixExperimentId
     * 
     * @return
     */
    public long getRhombixExperimentId() {
        return this.rhombixExperimentId;
    }

    /**
     * @param rhombixExperimentId The rhombixExperimentId to set.
     */
    public void setRhombixExperimentId(long rhombixExperimentId) {
        this.rhombixExperimentId = rhombixExperimentId;
    }

}
