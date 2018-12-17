/*
 * SafetyInformation.java
 *
 * Created on 01 May 2007, 16:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.core.model;

import org.pimslims.business.XtalObject;

/**
 * <p>
 * All safety information about a sample
 * </p>
 * <p>
 * LATER: Need to add in all the details that might be required by all labs, synchrotrons, etc.
 * </p>
 * 
 * @author IMB
 */
public class SafetyInformation extends XtalObject {

    private String sourceOrganism = "";

    private boolean radioactive = false;

    private boolean contaminant = false;

    private boolean toxic = false;

    private boolean corrosive = false;

    private boolean oxidizing = false;

    private boolean explosive = false;

    private boolean flammable = false;

    private boolean biologicalHazard = false;

    private String dangerDetails = "None";

    private double sampleTemperature = 20.0;

    /**
     * Creates a new instance of Safety Information
     */
    public SafetyInformation() {

    }

    /**
     * 
     * @return
     */
    public String getSourceOrganism() {
        return sourceOrganism;
    }

    /**
     * 
     * @param sourceOrganism
     */
    public void setSourceOrganism(String sourceOrganism) {
        this.sourceOrganism = sourceOrganism;
    }

    /**
     * 
     * @return
     */
    public boolean isRadioactive() {
        return radioactive;
    }

    /**
     * 
     * @param radioactive
     */
    public void setRadioactive(boolean radioactive) {
        this.radioactive = radioactive;
    }

    /**
     * 
     * @return
     */
    public boolean isContaminant() {
        return contaminant;
    }

    /**
     * 
     * @param contaminant
     */
    public void setContaminant(boolean contaminant) {
        this.contaminant = contaminant;
    }

    /**
     * 
     * @return
     */
    public boolean isToxic() {
        return toxic;
    }

    /**
     * 
     * @param toxic
     */
    public void setToxic(boolean toxic) {
        this.toxic = toxic;
    }

    /**
     * 
     * @return
     */
    public boolean isCorrosive() {
        return corrosive;
    }

    /**
     * 
     * @param corrosive
     */
    public void setCorrosive(boolean corrosive) {
        this.corrosive = corrosive;
    }

    /**
     * 
     * @return
     */
    public boolean isOxidizing() {
        return oxidizing;
    }

    /**
     * 
     * @param oxidizing
     */
    public void setOxidizing(boolean oxidizing) {
        this.oxidizing = oxidizing;
    }

    /**
     * 
     * @return
     */
    public boolean isExplosive() {
        return explosive;
    }

    /**
     * 
     * @param explosive
     */
    public void setExplosive(boolean explosive) {
        this.explosive = explosive;
    }

    /**
     * 
     * @return
     */
    public boolean isFlammable() {
        return flammable;
    }

    /**
     * 
     * @param flammable
     */
    public void setFlammable(boolean flammable) {
        this.flammable = flammable;
    }

    /**
     * 
     * @return
     */
    public boolean isBiologicalHazard() {
        return biologicalHazard;
    }

    /**
     * 
     * @param biologicalHazard
     */
    public void setBiologicalHazard(boolean biologicalHazard) {
        this.biologicalHazard = biologicalHazard;
    }

    /**
     * 
     * @return
     */
    public String getDangerDetails() {
        return dangerDetails;
    }

    /**
     * 
     * @param dangerDetails
     */
    public void setDangerDetails(String dangerDetails) {
        this.dangerDetails = dangerDetails;
    }

    /**
     * 
     * @return
     */
    public double getSampleTemperature() {
        return sampleTemperature;
    }

    /**
     * 
     * @param sampleTemperature
     */
    public void setSampleTemperature(double sampleTemperature) {
        this.sampleTemperature = sampleTemperature;
    }

}
