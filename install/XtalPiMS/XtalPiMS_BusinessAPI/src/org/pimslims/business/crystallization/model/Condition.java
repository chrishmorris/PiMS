/*
 * Condition.java Created on 17 April 2007, 13:55 To change this template, choose Tools | Template Manager and
 * open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

import java.util.ArrayList;
import java.util.List;

import org.pimslims.business.XtalObject;
import org.pimslims.business.core.model.SafetyInformation;

/**
 * <p>
 * Class for a condition in a screen - made up from a list of components
 * </p>
 * TODO implement equals and hashCode
 * @author IMB
 */
public class Condition extends XtalObject {
    private String localName = "";

    private int localNumber = 0;

    private String manufacturerName = "";

    private String manufacturerScreenName = "";

    private String manufacturerCode = "";

    private String manufacturerCatalogueCode = "";

    private float finalpH;

    private boolean volatileCondition = false;

    private Boolean saltCondition = false;

    private SafetyInformation safetyInformation = null;

    private List<ComponentQuantity> components = new ArrayList<ComponentQuantity>();

    /**
     * Creates a new instance of a Condition
     */
    public Condition() {

    }

    /**
     * 
     * @return
     */
    public String getLocalName() {
        return localName;
    }

    /**
     * 
     * @param localName
     */
    public void setLocalName(final String localName) {
        this.localName = localName;
    }

    /**
     * 
     * @return
     */
    public int getLocalNumber() {
        return localNumber;
    }

    /**
     * 
     * @param localNumber
     */
    public void setLocalNumber(final int localNumber) {
        this.localNumber = localNumber;
    }

    /**
     * 
     * @return
     */
    public String getManufacturerName() {
        return manufacturerName;
    }

    /**
     * 
     * @param manufacturerName
     */
    public void setManufacturerName(final String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    /**
     * 
     * @return
     */
    public String getManufacturerScreenName() {
        return manufacturerScreenName;
    }

    /**
     * 
     * @param manufacturerScreenName
     */
    public void setManufacturerScreenName(final String manufacturerScreenName) {
        this.manufacturerScreenName = manufacturerScreenName;
    }

    /**
     * 
     * @return
     */
    public String getManufacturerCode() {
        return manufacturerCode;
    }

    /**
     * 
     * @param manufacturerCode
     */
    public void setManufacturerCode(final String manufacturerCode) {
        this.manufacturerCode = manufacturerCode;
    }

    /**
     * 
     * @return
     */
    public List<ComponentQuantity> getComponents() {
        return components;
    }

    /**
     * 
     * @param components
     */
    public void setComponents(final List<ComponentQuantity> components) {
        this.components = components;
    }

    /**
     * 
     * @param component
     */
    public void addComponent(final ComponentQuantity component) {
        if (this.getComponents() == null) {
            this.setComponents(new ArrayList<ComponentQuantity>());
        }
        this.getComponents().add(component);
    }

    /**
     * 
     * @return
     */
    public SafetyInformation getSafetyInformation() {
        return safetyInformation;
    }

    /**
     * 
     * @param safetyInformation
     */
    public void setSafetyInformation(final SafetyInformation safetyInformation) {
        this.safetyInformation = safetyInformation;
    }

    public String getManufacturerCatalogueCode() {
        return manufacturerCatalogueCode;
    }

    public void setManufacturerCatalogueCode(final String manufacturerCatalogueCode) {
        this.manufacturerCatalogueCode = manufacturerCatalogueCode;
    }

    public float getFinalpH() {
        return finalpH;
    }

    public void setFinalpH(final float finalpH) {
        this.finalpH = finalpH;
    }

    public boolean isVolatileCondition() {
        return volatileCondition;
    }

    public void setVolatileCondition(final boolean volatileCondition) {
        this.volatileCondition = volatileCondition;
    }

    public void setSaltCondition(final Boolean saltCondition) {
        this.saltCondition = saltCondition;
    }

    public Boolean getSaltCondition() {
        return saltCondition;
    }

    @Override
    public String toString() {
        return "localName:" + localName + ", localNumber:" + localNumber + ", manufacturerName:"
            + manufacturerName + ", manufacturerScreenName:" + manufacturerScreenName;
    }

}
