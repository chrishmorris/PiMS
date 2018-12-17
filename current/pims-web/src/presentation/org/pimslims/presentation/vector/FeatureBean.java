/**
 * pims-web org.pimslims.presentation.vector MoleculeFeatureBean.java
 * 
 * @author pajanne
 * @date May 14, 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation.vector;

import java.util.HashMap;
import java.util.Map;

import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.molecule.MoleculeFeature;
import org.pimslims.presentation.ModelObjectBean;

/**
 * MoleculeFeatureBean
 * 
 */
public class FeatureBean extends ModelObjectBean {
    private String name;

    private String featureType;

    private String status;

    private Integer startSeq;

    private Integer endSeq;

    private String details;

    /**
     * 
     * Constructor for MoleculeFeatureBean
     * 
     * @param feature
     */
    public FeatureBean(final MoleculeFeature feature) {
        super(feature);
        this.name = feature.getName();
        this.featureType = feature.getFeatureType();
        this.status = feature.getStatus();
        this.startSeq = feature.getStartSeqId();
        this.endSeq = feature.getEndSeqId();
        this.details = feature.getDetails();
    }

    public static Map<String, Object> getAttributes(final FeatureBean bean) {
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(MoleculeFeature.PROP_NAME, bean.getName());
        attributes.put(MoleculeFeature.PROP_FEATURETYPE, bean.getFeatureType());
        attributes.put(MoleculeFeature.PROP_STATUS, bean.getStatus());
        attributes.put(MoleculeFeature.PROP_STARTSEQID, bean.getStartSeq());
        attributes.put(MoleculeFeature.PROP_ENDSEQID, bean.getEndSeq());
        attributes.put(LabBookEntry.PROP_DETAILS, bean.getDetails());
        return attributes;
    }

    /**
     * @return Returns the name.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @param name The name to set.
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return Returns the featureType.
     */
    public String getFeatureType() {
        return this.featureType;
    }

    /**
     * @param featureType The featureType to set.
     */
    public void setFeatureType(final String featureType) {
        this.featureType = featureType;
    }

    /**
     * @return Returns the status.
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * @param status The status to set.
     */
    public void setStatus(final String status) {
        this.status = status;
    }

    /**
     * @return Returns the startSeq.
     */
    public Integer getStartSeq() {
        return this.startSeq;
    }

    /**
     * @param startSeq The startSeq to set.
     */
    public void setStartSeq(final Integer startSeq) {
        this.startSeq = startSeq;
    }

    /**
     * @return Returns the endSeq.
     */
    public Integer getEndSeq() {
        return this.endSeq;
    }

    /**
     * @param endSeq The endSeq to set.
     */
    public void setEndSeq(final Integer endSeq) {
        this.endSeq = endSeq;
    }

    /**
     * @return Returns the details.
     */
    public String getDetails() {
        return this.details;
    }

    /**
     * @param details The details to set.
     */
    public void setDetails(final String details) {
        this.details = details;
    }

}
