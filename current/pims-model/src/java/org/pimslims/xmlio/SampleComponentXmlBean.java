/**
 * pims-web org.pimslims.xmlio SampleComponentXmlBean.java
 * 
 * @author pajanne
 * @date Jul 15, 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.xmlio;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.pimslims.model.molecule.Construct;
import org.pimslims.model.molecule.MoleculeFeature;
import org.pimslims.model.reference.ComponentCategory;

/**
 * SampleComponentXmlBean
 * 
 */
public class SampleComponentXmlBean {

    private String name;

    private String componentCategory;

    private String details;

    private String molType;

    private String sequenceType;

    private String constructStatus;

    private String function;

    private List<MoleculeFeatureXmlBean> resistanceDetails = new ArrayList<MoleculeFeatureXmlBean>(0);

    private List<MoleculeFeatureXmlBean> promoterDetails = new ArrayList<MoleculeFeatureXmlBean>(0);

    private List<MoleculeFeatureXmlBean> markerDetails = new ArrayList<MoleculeFeatureXmlBean>(0);

    /**
     * 
     * Constructor for SampleComponentXmlBean
     * 
     * @param construct
     */
    public SampleComponentXmlBean(final Construct construct) {
        this.name = construct.getName();
        this.componentCategory = "";
        if (construct.getCategories().size() != 0) {
            for (final ComponentCategory category : construct.getCategories()) {
                this.componentCategory = this.componentCategory + category.getName();
            }
        }
        this.details = construct.getDetails();
        this.molType = construct.getMolType();
        this.sequenceType = construct.getSequenceType();
        this.constructStatus = construct.getConstructStatus();
        this.function = construct.getFunction();
        if (construct.getMoleculeFeatures().size() != 0) {
            for (final MoleculeFeature feature : construct.getMoleculeFeatures()) {
                if ("resistance".equals(feature.getFeatureType())) {
                    this.resistanceDetails.add(new MoleculeFeatureXmlBean(feature));
                } else if ("promoter".equals(feature.getFeatureType())) {
                    this.promoterDetails.add(new MoleculeFeatureXmlBean(feature));
                } else {
                    this.markerDetails.add(new MoleculeFeatureXmlBean(feature));
                }
            }
        }
    }

    // Required by jaxb
    private SampleComponentXmlBean() {
        // Required by jaxb
    }

    /**
     * @return Returns the name.
     */
    @XmlAttribute
    public String getName() {
        return this.name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return Returns the componentCategory.
     */
    @XmlAttribute
    public String getComponentCategory() {
        return this.componentCategory;
    }

    /**
     * @param componentCategory The componentCategory to set.
     */
    public void setComponentCategory(final String componentCategory) {
        this.componentCategory = componentCategory;
    }

    /**
     * @return Returns the details.
     */
    @XmlAttribute
    public String getDetails() {
        return this.details;
    }

    /**
     * @param details The details to set.
     */
    public void setDetails(final String details) {
        this.details = details;
    }

    /**
     * @return Returns the molType.
     */
    @XmlAttribute
    public String getMolType() {
        return this.molType;
    }

    /**
     * @param molType The molType to set.
     */
    public void setMolType(final String molType) {
        this.molType = molType;
    }

    /**
     * @return Returns the sequenceType.
     */
    @XmlAttribute
    public String getSequenceType() {
        return this.sequenceType;
    }

    /**
     * @param sequenceType The sequenceType to set.
     */
    public void setSequenceType(final String sequenceType) {
        this.sequenceType = sequenceType;
    }

    /**
     * @return Returns the constructStatus.
     */
    @XmlAttribute
    public String getConstructStatus() {
        if (null == this.constructStatus) {
            return "empty";
        }
        return this.constructStatus;
    }

    /**
     * @param constructStatus The constructStatus to set.
     */
    public void setConstructStatus(final String constructStatus) {
        this.constructStatus = constructStatus;
    }

    /**
     * @return Returns the function.
     */
    @XmlElement
    public String getFunction() {
        return this.function;
    }

    /**
     * @param function The function to set.
     */
    public void setFunction(final String function) {
        this.function = function;
    }

    /**
     * @return Returns the resistanceFeatures.
     */
    @XmlElement
    public List<MoleculeFeatureXmlBean> getResistanceDetails() {
        return this.resistanceDetails;
    }

    /**
     * @param resistanceFeatures The resistanceFeatures to set.
     */
    public void setResistanceDetails(final List<MoleculeFeatureXmlBean> resistanceDetails) {
        this.resistanceDetails = resistanceDetails;
    }

    /**
     * @return Returns the promoterDetails.
     */
    @XmlElement
    public List<MoleculeFeatureXmlBean> getPromoterDetails() {
        return this.promoterDetails;
    }

    /**
     * @param promoterDetails The promoterDetails to set.
     */
    public void setPromoterDetails(final List<MoleculeFeatureXmlBean> promoterDetails) {
        this.promoterDetails = promoterDetails;
    }

    /**
     * @return Returns the markerDetails.
     */
    public List<MoleculeFeatureXmlBean> getMarkerDetails() {
        return this.markerDetails;
    }

    /**
     * @param markerDetails The markerDetails to set.
     */
    public void setMarkerDetails(final List<MoleculeFeatureXmlBean> markerDetails) {
        this.markerDetails = markerDetails;
    }

}
