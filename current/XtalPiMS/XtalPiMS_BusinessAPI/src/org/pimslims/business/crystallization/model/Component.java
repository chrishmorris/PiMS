/*
 * Component.java
 *
 * Created on 01 May 2007, 10:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

import org.pimslims.business.Link;
import org.pimslims.business.XtalObject;
import org.pimslims.business.core.model.SafetyInformation;

/**
 * <p>
 * A class to describe a chemical component used in a Condition that makes up a Crystallization Screen
 * </p>
 * <p>
 * Although this will not be strictly necessary for every condition, they are likely to be stored in the
 * database as reference data, so are unlikely to be edited very often.
 * </p>
 * <p>
 * A component could be included in multiple Screen Conditions
 * </p>
 * <p>
 * A component will have its own safety information, which may or may not affect the safety information of the
 * Condition
 * </p>
 * 
 * @author IMB
 */
public class Component extends XtalObject {

    private String chemicalName = "";

    private SafetyInformation safetyInformation = null;

    private Link chemicalImage = null;

    private float pH = 7.0f;

    private boolean volatileComponent = false;

    /**
     * <p>
     * CAS registry numbers are unique numerical identifiers for chemical compounds, polymers, biological
     * sequences, mixtures and alloys. They are also referred to as CAS numbers, CAS RNs or CAS #s.
     * </p>
     * 
     * <p>
     * Chemical Abstracts Service (CAS), a division of the American Chemical Society, assigns these
     * identifiers to every chemical that has been described in the literature. The intention is to make
     * database searches more convenient, as chemicals often have many names. Almost all molecule databases
     * today allow searching by CAS number.
     * </p>
     */
    private String casNumber = "";

    /**
     * Creates a new instance of a Component
     */
    public Component() {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Component other = (Component) obj;
        if (this.chemicalName != other.chemicalName
            && (this.chemicalName == null || !this.chemicalName.equals(other.chemicalName))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 53 * hash + (this.chemicalName != null ? this.chemicalName.hashCode() : 0);
        return hash;
    }

    /**
     * 
     * @return
     */
    public String getChemicalName() {
        return chemicalName;
    }

    /**
     * 
     * @param chemicalName
     */
    public void setChemicalName(String chemicalName) {
        this.chemicalName = chemicalName;
    }

    /**
     * Getter for the SafetyInformation
     * 
     * @return The Safety Information for this chemical
     */
    public SafetyInformation getSafetyInformation() {
        return safetyInformation;
    }

    /**
     * Setter for the Safety Information for this chemical
     * 
     * @param safetyInformation The Safety Information to use
     */
    public void setSafetyInformation(SafetyInformation safetyInformation) {
        this.safetyInformation = safetyInformation;
    }

    /**
     * Getter for the CAS Number
     * 
     * @return The CAS Number associated with this chemical
     */
    public String getCasNumber() {
        return casNumber;
    }

    /**
     * Setter for the CAS Number
     * 
     * @param casNumber Sets the CAS Number for this chemical.
     */
    public void setCasNumber(String casNumber) {
        this.casNumber = casNumber;
    }

    public Link getChemicalImage() {
        return chemicalImage;
    }

    public void setChemicalImage(Link chemicalImage) {
        this.chemicalImage = chemicalImage;
    }

    public float getPH() {
        return pH;
    }

    public void setPH(float pH) {
        this.pH = pH;
    }

    public boolean isVolatileComponent() {
        return volatileComponent;
    }

    public void setVolatileComponent(boolean volatileComponent) {
        this.volatileComponent = volatileComponent;
    }

}
