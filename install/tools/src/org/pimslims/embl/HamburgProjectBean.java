package org.pimslims.embl;

import java.util.Collection;

/**
 * Used to load construct data from Hamburg's legacy xml files.
 * 
 * These elements are not supported: clond, exprc, xtalc, funding project, URL,
 * contact
 * 
 * @author cm65
 * 
 */
public class HamburgProjectBean {

    private final String id;

    /**
     * Milestone reached
     */
    private final String task;

    /**
     * location in lab
     */
    private final String labID;

    /**
     * usually "EMBL Hamburg"
     */
    private final String lab;

    public static final String HAMBURG = "EMBL Hamburg";

    /**
     * principal investigator
     */
    private final String pi;

    /**
     * usually Public
     */
    private final String access;

    public static final String PUBLIC = "Public";

    /**
     * e.g. (41-296)
     */
    private final String description;

    private final String contact;

    private final String remarks;

    private final Collection<DbLinkBean> dbLinks;

    private final HamburgExperimentBean experiment;

    public HamburgProjectBean(String id, String task, String labID, String lab,
            String pi, String access, String description, String contact,
            String remarks, HamburgExperimentBean experiment,
            Collection<DbLinkBean> dblinks) {

        this.task = task;
        this.labID = labID;
        this.lab = lab;
        this.pi = pi;
        this.access = access;
        this.description = description;
        this.id = id;
        this.contact = contact;
        this.remarks = remarks;
        this.dbLinks = dblinks;
        this.experiment = experiment;
    }

    public String getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public String getLabID() {
        return labID;
    }

    public String getLab() {
        return lab;
    }

    public String getPi() {
        return pi;
    }

    public String getAccess() {
        return access;
    }

    public String getDescription() {
        return description;
    }

    public String getContact() {
        return this.contact;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public Collection<DbLinkBean> getDbLinks() {
        return dbLinks;
    }

    public String getConstructDescription() {
        return experiment.getConstructDescription();
    }

    public Float getExpressionLevel() {
        return experiment.getExpressionLevel();
    }

    public String getExpressionQuality() {
        return experiment.getExpressionQuality();
    }

    public String getStrain() {
        return experiment.getStrain();
    }

    public String getVector() {
        return experiment.getVector();
    }

    public String getCrystalForm() {
        return experiment.getCrystalForm();
    }

    public String getCrystalSize() {
        return experiment.getCrystalSize();
    }

    public String getLigand() {
        return experiment.getLigand();
    }

    public String getResolution() {
        return experiment.getResolution();
    }

    public String getCondition() {
        return experiment.getCondition();
    }

    public boolean hasCrystallography() {
        return this.experiment.isChrystallography();
    }

}
