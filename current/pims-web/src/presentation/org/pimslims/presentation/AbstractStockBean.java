/**
 * pims-web org.pimslims.presentation AbstractStockBean.java Superclass for plasmid and cell stocks
 * 
 * @author susy
 * @date 7 Dec 2009
 * 
 * Protein Information Management System
 * @version: 3.2
 * 
 * Copyright (c) 2009 susy The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation;

import java.util.Calendar;

import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.presentation.servlet.utils.ValueFormatter;

/**
 * AbstractStockBean
 * 
 */
public class AbstractStockBean extends ModelObjectBean {

    private String rack;

    private String rackPosition;

    private Calendar experimentDate;

    private String dateAsString;

    private String description;

    private String vector;

    private String initials;

    // Each bean in the presentation layer contains a hook to the corresponding
    // PIMS object
    private String pimsVectorHook;

    private String pimsSampleHook;

    private String pimsExperimentHook;

    private String pimsHolderHook;

    private String antibioticRes;

    private Float volume;

    private String stockName;

    protected AbstractStockBean(final LabBookEntry page) {
        super(page);

    }

    public String getRack() {
        return this.rack;
    }

    public void setRack(final String rack) {
        this.rack = rack;
    }

    public String getRackPosition() {
        return this.rackPosition;
    }

    public void setRackPosition(final String rackPosition) {
        this.rackPosition = rackPosition;
    }

    public Calendar getExperimentDate() {
        return this.experimentDate;
    }

    public void setExperimentDate(final Calendar date) {
        this.experimentDate = date;
    }

    public String getDateAsString() {
        return this.dateAsString;
    }

    public void setDateAsString(final String dateAsString) {
        this.dateAsString = dateAsString;
    }

    public String getDescription() {
        return this.description;
    }

    protected void setDescription(final String description) {
        this.description = description;
    }

    public String getVector() {
        return this.vector;
    }

    public void setVector(final String vector) {
        this.vector = vector;
    }

    public String getInitials() {
        return this.initials;
    }

    public void setInitials(final String initials) {
        this.initials = initials;
    }

    public String getPimsVectorHook() {
        return this.pimsVectorHook;
    }

    public void setPimsVectorHook(final String pimsVectorHook) {
        this.pimsVectorHook = pimsVectorHook;
    }

    public String getPimsSampleHook() {
        return this.pimsSampleHook;
    }

    public void setPimsSampleHook(final String pimsSampleHook) {
        this.pimsSampleHook = pimsSampleHook;
    }

    public String getPimsExperimentHook() {
        return this.pimsExperimentHook;
    }

    public void setPimsExperimentHook(final String pimsExperimentHook) {
        this.pimsExperimentHook = pimsExperimentHook;
    }

    public String getPimsHolderHook() {
        return this.pimsHolderHook;
    }

    public void setPimsHolderHook(final String location) {
        this.pimsHolderHook = location;
    }

    public String getAntibioticRes() {
        return this.antibioticRes;
    }

    protected void setAntibioticRes(final String antibioticRes) {
        this.antibioticRes = antibioticRes;
    }

    public Float getVolume() {
        return this.volume;
    }

    public void setVolume(final Float volume) {
        this.volume = volume;
    }

    public String getStockName() {
        return this.stockName;
    }

    public void setStockName(final String stockName) {
        this.stockName = stockName;
    }

    /**
     * PlasmidStockBean.experimentDetails
     * 
     * @param experiment
     * @param stockBean
     */
    static void experimentDetails(final Experiment experiment, final AbstractStockBean stockBean) {
        final String expHook = experiment.get_Hook();
        stockBean.setPimsExperimentHook(expHook);
        final Calendar expdate = experiment.getStartDate();
        stockBean.setExperimentDate(expdate);
        final String stringDate = ValueFormatter.formatDate(expdate);
        stockBean.setDateAsString(stringDate);
    }

}
