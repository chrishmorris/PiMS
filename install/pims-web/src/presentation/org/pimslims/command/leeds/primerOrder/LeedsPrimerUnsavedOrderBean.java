package org.pimslims.command.leeds.primerOrder;

import java.util.Collection;
import java.util.Set;

import org.pimslims.presentation.order.UnsavedOrderBean;
import org.pimslims.util.File;

public class LeedsPrimerUnsavedOrderBean extends UnsavedOrderBean implements ILeedsPrimerOrderBean {
    // plate wide value
    String purificationType;

    String startingSynthesisScale;

    String bufferType;

    String normalization;

    // well wide value
    Float MW;

    String restrictionSites;

    Integer lenghtOnGene;

    Float tmSeller;

    Float tmFull;

    Float tmGene;

    Float concertrationInUM;

    Float volumnInNL;

    Float OD;

    Float amountInUg;

    Set<File> files;

    // duplication info
    LeedsPrimerUnsavedOrderBean duplicatedWithinOrder = null; // duplicated

    // within order
    // plate
    String duplicatedSampleHook = null; // duplicated within DB

    Collection<String> duplicatedSampleExpTypes = null;// the exp types of

    // duplicted Sample
    String duplicationInfo;

    // does it need to rename before save order
    public boolean isNeedRename() {
        if (this.isDuplicatedInPlateOrder()) {
            return true;
        }
        if (this.getDuplicatedSampleHook() != null && !this.needMerge()) {
            return true;
        }
        return false;
    }

    /* is it an acceptable order
    public boolean isRejected() {
        if (this.getDuplicatedSampleExpTypes() == null) {
            return false;
        }
        if (this.getDuplicatedSampleExpTypes().contains(FormFieldsNames.leedscloningDesign)
            || this.getDuplicatedSampleExpTypes().contains(FormFieldsNames.primersDesign)) {
            return true;
        }

        return false;
    } */

    public boolean needMerge() {
        /* if (this.getDuplicatedSampleHook() != null && !this.isRejected()) {
            return true;
        } */
        return false;
    }

    /**
     * @return the annotationHooks
     */
    public Set<org.pimslims.util.File> getFiles() {
        return this.files;
    }

    /**
     * @param files the annotationHooks to set
     */
    public void setFiles(final Set<File> files) {
        this.files = files;
    }

    /**
     * @return the amountInUg
     */
    public Float getAmountInUg() {
        return this.amountInUg;
    }

    /**
     * @param amountInUg the amountInUg to set
     */
    public void setAmountInUg(final Float amountInUg) {
        this.amountInUg = amountInUg;
    }

    /**
     * @return the bufferType
     */
    public String getBufferType() {
        return this.bufferType;
    }

    /**
     * @param bufferType the bufferType to set
     */
    public void setBufferType(final String bufferType) {
        this.bufferType = bufferType;
    }

    /**
     * @return the concertrationInUM
     */
    public Float getConcertrationInUM() {
        return this.concertrationInUM;
    }

    /**
     * @param concertrationInUM the concertrationInUM to set
     */
    public void setConcertrationInUM(final Float concertrationInUM) {
        this.concertrationInUM = concertrationInUM;
    }

    /**
     * @return the mW
     */
    public Float getMw() {
        return this.MW;
    }

    /**
     * @param mw the mW to set
     */
    public void setMw(final Float mw) {
        this.MW = mw;
    }

    /**
     * @return the normalization
     */
    public String getNormalization() {
        return this.normalization;
    }

    /**
     * @param normalization the normalization to set
     */
    public void setNormalization(final String normalization) {
        this.normalization = normalization;
    }

    /**
     * @return the oD
     */
    public Float getOd() {
        return this.OD;
    }

    /**
     * @param od the oD to set
     */
    public void setOd(final Float od) {
        this.OD = od;
    }

    /**
     * @return the purificationType
     */
    public String getPurificationType() {
        return this.purificationType;
    }

    /**
     * @param purificationType the purificationType to set
     */
    public void setPurificationType(final String purificationType) {
        this.purificationType = purificationType;
    }

    /**
     * @return the restrictionSites
     */
    public String getRestrictionSites() {
        return this.restrictionSites;
    }

    /**
     * @param restrictionSites the restrictionSites to set
     */
    public void setRestrictionSites(final String restrictionSites) {
        this.restrictionSites = restrictionSites;
    }

    /**
     * @return the startingSynthesisScale
     */
    public String getStartingSynthesisScale() {
        return this.startingSynthesisScale;
    }

    /**
     * @param startingSynthesisScale the startingSynthesisScale to set
     */
    public void setStartingSynthesisScale(final String startingSynthesisScale) {
        this.startingSynthesisScale = startingSynthesisScale;
    }

    /**
     * @return the tmFull
     */
    public Float getTmFull() {
        return this.tmFull;
    }

    /**
     * @param tmFull the tmFull to set
     */
    public void setTmFull(final Float tmFull) {
        this.tmFull = tmFull;
    }

    /**
     * @return the tmGene
     */
    public Float getTmGene() {
        return this.tmGene;
    }

    /**
     * @param tmGene the tmGene to set
     */
    public void setTmGene(final Float tmGene) {
        this.tmGene = tmGene;
    }

    /**
     * @return the tmSeller
     */
    public Float getTmSeller() {
        return this.tmSeller;
    }

    /**
     * @param tmSeller the tmSeller to set
     */
    public void setTmSeller(final Float tmSeller) {
        this.tmSeller = tmSeller;
    }

    /**
     * @return the volumnInNL
     */
    public Float getVolumnInNL() {
        return this.volumnInNL;
    }

    /**
     * @param volumnInNL the volumnInNL to set
     */
    public void setVolumnInNL(final Float volumnInNL) {
        this.volumnInNL = volumnInNL;
    }

    /**
     * @return the duplicatedInPlateOrder
     */
    public boolean isDuplicatedInPlateOrder() {
        return this.duplicatedWithinOrder != null;
    }

    /**
     * @return the duplicatedWithOrder
     */
    public LeedsPrimerUnsavedOrderBean getDuplicatedWithOrder() {
        return this.duplicatedWithinOrder;
    }

    /**
     * @param duplicatedWithOrder the duplicatedWithOrder to set
     */
    public void setDuplicatedWithOrder(final LeedsPrimerUnsavedOrderBean duplicatedWithOrder) {
        this.duplicatedWithinOrder = duplicatedWithOrder;
    }

    /**
     * @return the duplicatedSampleExpTypes
     */
    public Collection<String> getDuplicatedSampleExpTypes() {
        return this.duplicatedSampleExpTypes;
    }

    /**
     * @param duplicatedSampleExpTypes the duplicatedSampleExpTypes to set
     */
    public void setDuplicatedSampleExpTypes(final Collection<String> duplicatedSampleExpTypes) {
        this.duplicatedSampleExpTypes = duplicatedSampleExpTypes;
    }

    /**
     * @return the duplicatedSampleHook
     */
    public String getDuplicatedSampleHook() {
        return this.duplicatedSampleHook;
    }

    /**
     * @param duplicatedSampleHook the duplicatedSampleHook to set
     */
    public void setDuplicatedSampleHook(final String duplicatedSampleHook) {
        this.duplicatedSampleHook = duplicatedSampleHook;
    }

    /**
     * @return the lenghtOnGene
     */
    public Integer getLenghtOnGene() {
        return this.lenghtOnGene;
    }

    /**
     * @param lenghtOnGene the lenghtOnGene to set
     */
    public void setLenghtOnGene(final Integer lenghtOnGene) {
        this.lenghtOnGene = lenghtOnGene;
    }

}
