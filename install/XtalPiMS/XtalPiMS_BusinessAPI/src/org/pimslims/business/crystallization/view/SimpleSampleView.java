/** 
 * xtalPiMSApi org.pimslims.business.crystallization.view SimpleSampleView.java
 * @author Jon
 * @date 16 Sep 2010
 *
 * Protein Information Management System
 * @version: 4.1
 *
 * Copyright (c) 2010 Jon 
 * The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.business.crystallization.view;

/**
 * SimpleSampleView - this class gives a simple view of a sample, including
 * id, name and details for the sample and its parent construct and project.
 * 
 * @author Jon Diprose
 */
public class SimpleSampleView {

    private Long sampleId;

    private Long constructId;

    private Long projectId;

    private String sampleName;

    private String constructName;

    private String projectName;

    private String sampleDetails;

    private String constructDetails;

    private String projectDetails;

    private String projectLeader;

    /**
     * @return Returns the sampleId.
     */
    public Long getSampleId() {
        return sampleId;
    }

    /**
     * @param sampleId The sampleId to set.
     */
    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    /**
     * @return Returns the constructId.
     */
    public Long getConstructId() {
        return constructId;
    }

    /**
     * @param constructId The constructId to set.
     */
    public void setConstructId(Long constructId) {
        this.constructId = constructId;
    }

    /**
     * @return Returns the projectId.
     */
    public Long getProjectId() {
        return projectId;
    }

    /**
     * @param projectId The projectId to set.
     */
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    /**
     * @return Returns the sampleName.
     */
    public String getSampleName() {
        return sampleName;
    }

    /**
     * @param sampleName The sampleName to set.
     */
    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    /**
     * @return Returns the constructName.
     */
    public String getConstructName() {
        return constructName;
    }

    /**
     * @param constructName The constructName to set.
     */
    public void setConstructName(String constructName) {
        this.constructName = constructName;
    }

    /**
     * @return Returns the projectName.
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName The projectName to set.
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * @return Returns the sampleDetails.
     */
    public String getSampleDetails() {
        return sampleDetails;
    }

    /**
     * @param sampleDetails The sampleDetails to set.
     */
    public void setSampleDetails(String sampleDetails) {
        this.sampleDetails = sampleDetails;
    }

    /**
     * @return Returns the constructDetails.
     */
    public String getConstructDetails() {
        return constructDetails;
    }

    /**
     * @param constructDetails The constructDetails to set.
     */
    public void setConstructDetails(String constructDetails) {
        this.constructDetails = constructDetails;
    }

    /**
     * @return Returns the projectDetails.
     */
    public String getProjectDetails() {
        return projectDetails;
    }

    /**
     * @param projectDetails The projectDetails to set.
     */
    public void setProjectDetails(String projectDetails) {
        this.projectDetails = projectDetails;
    }

    /**
     * @return Returns the projectLeader.
     */
    public String getProjectLeader() {
        return projectLeader;
    }

    /**
     * @param projectLeader The projectLeader to set.
     */
    public void setProjectLeader(String projectLeader) {
        this.projectLeader = projectLeader;
    }

}
