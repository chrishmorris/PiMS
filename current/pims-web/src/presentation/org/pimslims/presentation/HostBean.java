/**
 * current-pims-web org.pimslims.presentation HostBean.java
 * 
 * @author susy
 * @date 23-Apr-2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 susy The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation;

import java.io.Serializable;

/**
 * HostBean for details of Host cells
 * 
 */
public class HostBean extends ModelObjectShortBean implements Serializable {

    @Override
    public int compareTo(final Object obj) {

        if (!(obj instanceof HostBean)) {
            throw new ClassCastException("obj1 is not an HostBean! ");
        }
        return 0;
    }

    // Hook to the corresponding PIMS object    
    private String hostHook;

    private String hostName;

    private String hostOrganism;

    private String organismHook;

    private String strain;

    private String genotype;

    private String harbouredPlasmids;

    private String antibioticRes;

    private String selectableMarkers;

    private String suppliers;

    private String hostUse;

    private String comments;

    /**
     * Constructor for HostBean
     */
    public HostBean() {
        super();
        // 
    }

    /**
     * @return Returns the antibioticRes.
     */
    public String getAntibioticRes() {
        return this.antibioticRes;
    }

    /**
     * @param antibioticRes The antibioticRes to set.
     */
    public void setAntibioticRes(final String antibioticRes) {
        this.antibioticRes = antibioticRes;
    }

    /**
     * @return Returns the comments.
     */
    public String getComments() {
        return this.comments;
    }

    /**
     * @param comments The comments to set.
     */
    public void setComments(final String comments) {
        this.comments = comments;
    }

    /**
     * @return Returns the genotype.
     */
    public String getGenotype() {
        return this.genotype;
    }

    /**
     * @param genotype The genotype to set.
     */
    public void setGenotype(final String genotype) {
        this.genotype = genotype;
    }

    /**
     * @return Returns the harbouredPlasmids.
     */
    public String getHarbouredPlasmids() {
        return this.harbouredPlasmids;
    }

    /**
     * @param harbouredPlasmids The harbouredPlasmids to set.
     */
    public void setHarbouredPlasmids(final String harbouredPlasmids) {
        this.harbouredPlasmids = harbouredPlasmids;
    }

    /**
     * @return Returns the hostName.
     */
    public String getHostName() {
        return this.hostName;
    }

    /**
     * @param hostName The hostName to set.
     */
    public void setHostName(final String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return Returns the hostOrganism.
     */
    public String getHostOrganism() {
        return this.hostOrganism;
    }

    /**
     * @param hostOrganism The hostOrganism to set.
     */
    public void setHostOrganism(final String hostOrganism) {
        this.hostOrganism = hostOrganism;
    }

    /**
     * @return Returns the hostUse.
     */
    public String getHostUse() {
        return this.hostUse;
    }

    /**
     * @param hostUse The hostUse to set.
     */
    public void setHostUse(final String hostUse) {
        this.hostUse = hostUse;
    }

    /**
     * @return Returns the organismHook.
     */
    public String getOrganismHook() {
        return this.organismHook;
    }

    /**
     * @param organismHook The organismHook to set.
     */
    public void setOrganismHook(final String organismHook) {
        this.organismHook = organismHook;
    }

    /**
     * @return Returns the selectableMarkers.
     */
    public String getSelectableMarkers() {
        return this.selectableMarkers;
    }

    /**
     * @param selectableMarkers The selectableMarkers to set.
     */
    public void setSelectableMarkers(final String selectableMarkers) {
        this.selectableMarkers = selectableMarkers;
    }

    /**
     * @return Returns the suppliers.
     */
    public String getSuppliers() {
        return this.suppliers;
    }

    /**
     * @param suppliers The suppliers to set.
     */
    public void setSuppliers(final String suppliers) {
        this.suppliers = suppliers;
    }

    /**
     * @return Returns the strain.
     */
    public String getStrain() {
        return this.strain;
    }

    /**
     * @param strain The strain to set.
     */
    public void setStrain(final String strain) {
        this.strain = strain;
    }

    /**
     * @return Returns the hostHook.
     */
    public String getHostHook() {
        return this.hostHook;
    }

    /**
     * @param hostHook The hostHook to set.
     */
    public void setHostHook(final String hostHook) {
        this.hostHook = hostHook;
    }

}
