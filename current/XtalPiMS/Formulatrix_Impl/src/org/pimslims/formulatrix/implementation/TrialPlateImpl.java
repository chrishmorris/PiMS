/**
 *  TrialPlateImpl.java
 * 
 * @author cm65
 * @date 21 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.formulatrix.implementation;

import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.TrialPlate;

/**
 * TrialPlateImpl
 * 
 */
public class TrialPlateImpl extends TrialPlate {

    private long mfrExperimentId;

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
    public long getMfrExperimentId() {
        return this.mfrExperimentId;
    }

    /**
     * @param rhombixExperimentId The rhombixExperimentId to set.
     */
    public void setMfrExperimentId(long rhombixExperimentId) {
        this.mfrExperimentId = rhombixExperimentId;
    }

}
