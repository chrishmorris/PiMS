/*
 * Sample.java
 *
 * Created on 17 April 2007, 13:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pimslims.business.core.model;

import java.util.Calendar;

import org.pimslims.business.Link;
import org.pimslims.business.XtalObject;

/**
 * <p>
 * All details about a sample of soluble protein
 * </p>
 * <p>
 * LATER: Alot of this information is perhaps not relevant to display in the crystallization interface, so
 * could simply be repleced by link information to get this information by linking through to the Core PIMS
 * site
 * </p>
 * 
 * @author IMB
 */
public class Sample extends XtalObject {
    public static String PROP_SAFETY_INFORMATION  = "safetyInformation";
    public static String PROP_NAME  = "NAME";
    public static String PROP_DESCRIPTION  = "description";
    public static String PROP_PH  = "PH";
    public static String PROP_MOLECULAR_WEIGHT  = "molecularWeight";
    public static String PROP_NUM_SUB_UNITS  = "numSubUnits";
    public static String PROP_BATCH_REFERENCE  = "batchReference";
    public static String PROP_ORIGIN  = "origin";
    public static String PROP_TYPE  = "type";
    public static String PROP_CELLULAR_LOCATION  = "cellularLocation";
    public static String PROP_CONCENTRATION  = "concentration";
    public static String PROP_CREATE_DATE  = "createDate";
    public static String PROP_GI_NUMBER  = "giNumber";
    public static String PROP_CONSTRUCT  = "construct";
    public static String PROP_OWNER  = "sample";
        
    private SafetyInformation safetyInformation = new SafetyInformation();

    private String name = "";

    private String description = "";

    private double PH = 7.0;

    private double molecularWeight = 0.0;

    private int numSubUnits = 0;

    private String batchReference = "";

    private String origin = "";

    private String type = "";

    private String cellularLocation = "";

    private double concentration = 0.0;

    private Calendar createDate = null;

    private Person owner = null;
    /**
     * A gi number (genInfo identifier) is a unique integer which identifies a particular sequence; the gi
     * number will change every time the sequence changes.
     */
    private long giNumber = 0;

    /**
     * The construct link provides the link back to the LIMS system that contains the details of the construct
     * used to create this sample. It is up to the implementation to fill this link in such that it is
     * appropriate for that implementation and lims interface. It is not the responsibility of xtalPIMS to
     * manage constructs
     */
    private Link constructLink = new Link();

    private Construct construct = null;

    /**
     * The sample link provides the link back to the LIMS system that this xtalpims is being developed against
     * (e.g. PIMS or OPPF PlateDB/Nautilus/ OPINE. It is up to the implementation to fill this information in.
     */
    private Link sampleLink = new Link();

    /**
     * The target link provides the link back to the LIMS system web page that describes this target.
     */
    private Link targetLink = new Link();

    /**
     * Creates a new instance of a Sample
     */
    public Sample() {
    }

    public Sample(String name) {
        this.name = name;
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
    public void setSafetyInformation(SafetyInformation safetyInformation) {
        this.safetyInformation = safetyInformation;
    }

    /**
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     */
    public double getPH() {
        return PH;
    }

    /**
     * 
     * @param PH
     */
    public void setPH(double PH) {
        this.PH = PH;
    }

    /**
     * 
     * @return
     */
    public double getMolecularWeight() {
        return molecularWeight;
    }

    /**
     * 
     * @param molecularWeight
     */
    public void setMolecularWeight(double molecularWeight) {
        this.molecularWeight = molecularWeight;
    }

    /**
     * 
     * @return
     */
    public int getNumSubUnits() {
        return numSubUnits;
    }

    /**
     * 
     * @param numSubUnits
     */
    public void setNumSubUnits(int numSubUnits) {
        this.numSubUnits = numSubUnits;
    }

    /**
     * 
     * @return
     */
    public String getBatchReference() {
        return batchReference;
    }

    /**
     * 
     * @param batchReference
     */
    public void setBatchReference(String batchReference) {
        this.batchReference = batchReference;
    }

    /**
     * 
     * @return
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * 
     * @param origin
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * 
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     */
    public String getCellularLocation() {
        return cellularLocation;
    }

    /**
     * 
     * @param cellularLocation
     */
    public void setCellularLocation(String cellularLocation) {
        this.cellularLocation = cellularLocation;
    }

    /**
     * 
     * @return
     */
    public double getConcentration() {
        return concentration;
    }

    /**
     * 
     * @param concentration
     */
    public void setConcentration(double concentration) {
        this.concentration = concentration;
    }

    public long getGiNumber() {
        return giNumber;
    }

    public void setGiNumber(long giNumber) {
        this.giNumber = giNumber;
    }

    public Link getConstructLink() {
        return constructLink;
    }

    public void setConstructLink(Link constructLink) {
        this.constructLink = constructLink;
    }

    public Link getSampleLink() {
        return sampleLink;
    }

    public void setSampleLink(Link sampleLink) {
        this.sampleLink = sampleLink;
    }

    public Link getTargetLink() {
        return targetLink;
    }

    public void setTargetLink(Link targetLink) {
        this.targetLink = targetLink;
    }

    public Calendar getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Calendar createDate) {
        this.createDate = createDate;
    }

    public Construct getConstruct() {
        return construct;
    }

    public void setConstruct(Construct construct) {
        this.construct = construct;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }
}
