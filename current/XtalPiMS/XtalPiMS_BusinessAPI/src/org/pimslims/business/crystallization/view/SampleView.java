/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.business.crystallization.view;

import java.util.Calendar;

import net.sf.json.JSONObject;

/**
 *
 * @author ian
 */
public class SampleView {
    public static final String PROP_ID = "sampleId";
    public static final String PROP_NAME = "sampleName";
    public static final String PROP_DESCRIPTION = "description";
    public static final String PROP_PH = "PH";
    public static final String PROP_MOLECULAR_WEIGHT = "molecularWeight";
    public static final String PROP_NUM_SUB_UNITS = "numSubUnits";
    public static final String PROP_BATCH_REFERENCE = "batchReference";
    public static final String PROP_ORIGIN = "origin";
    public static final String PROP_TYPE = "type";
    public static final String PROP_CELLULAR_LOCATION = "cellularLocation";
    public static final String PROP_CONCENTRATION = "concentration";
    public static final String PROP_CREATE_DATE = "createDate";
    public static final String PROP_GI_NUMBER = "giNumber";
    public static final String PROP_TARGET_NAME = "targetName";
    public static final String PROP_TARGET_ID = "targetId";
    public static final String PROP_CONSTRUCT_NAME = "constructName";
    public static final String PROP_CONSTRUCT_ID = "constructId";
    public static final String PROP_OWNER = "owner";
    public static final String PROP_GROUP = "group";
    
    private String sampleName;
    private String description;
    private Long sampleId = -1L;
    private double PH = 7.0;
    private double molecularWeight = 0.0;
    private int numSubUnits = 0;
    private String batchReference = "";
    private String origin = "";
    private String type = "";
    private String cellularLocation = "";
    private double concentration = 0.0;
    private Calendar createDate = null;
    private long giNumber = 0;
    /**
     * The construct link provides the link back to the LIMS system that 
     * contains the details of the construct used to create this sample.
     * It is up to the implementation to fill this link in such that it is
     * appropriate for that implementation and LIMS interface.  
     * It is not the responsibility of xtalPIMS to manage constructs
     */
    private String constructName = "";
    private Long constructId = -1L;

    /**
     * The target link provides the link back to the LIMS system web page that 
     * describes this target.
     */
    private String targetName = "";
    private Long targetId = -1L;
    private String owner = "";
    private String group = "";
    
    
    public SampleView() {
        
    }
    
    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String name) {
        this.sampleName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long id) {
        this.sampleId = id;
    }

    public double getPH() {
        return PH;
    }

    public void setPH(double PH) {
        this.PH = PH;
    }

    public double getMolecularWeight() {
        return molecularWeight;
    }

    public void setMolecularWeight(double molecularWeight) {
        this.molecularWeight = molecularWeight;
    }

    public int getNumSubUnits() {
        return numSubUnits;
    }

    public void setNumSubUnits(int numSubUnits) {
        this.numSubUnits = numSubUnits;
    }

    public String getBatchReference() {
        return batchReference;
    }

    public void setBatchReference(String batchReference) {
        this.batchReference = batchReference;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCellularLocation() {
        return cellularLocation;
    }

    public void setCellularLocation(String cellularLocation) {
        this.cellularLocation = cellularLocation;
    }

    public double getConcentration() {
        return concentration;
    }

    public void setConcentration(double concentration) {
        this.concentration = concentration;
    }

    public Calendar getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Calendar createDate) {
        this.createDate = createDate;
    }

    public long getGiNumber() {
        return giNumber;
    }

    public void setGiNumber(long giNumber) {
        this.giNumber = giNumber;
    }

    public String getConstructName() {
        return constructName;
    }

    public void setConstructName(String constructName) {
        this.constructName = constructName;
    }

    public Long getConstructId() {
        return constructId;
    }

    public void setConstructId(Long constructId) {
        this.constructId = constructId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
    
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(PH);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result
                + ((batchReference == null) ? 0 : batchReference.hashCode());
        result = prime
                * result
                + ((cellularLocation == null) ? 0 : cellularLocation.hashCode());
        temp = Double.doubleToLongBits(concentration);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result
                + ((constructId == null) ? 0 : constructId.hashCode());
        result = prime * result
                + ((constructName == null) ? 0 : constructName.hashCode());
        result = prime * result
                + ((createDate == null) ? 0 : createDate.hashCode());
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + (int) (giNumber ^ (giNumber >>> 32));
        result = prime * result + ((sampleId == null) ? 0 : sampleId.hashCode());
        temp = Double.doubleToLongBits(molecularWeight);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((sampleName == null) ? 0 : sampleName.hashCode());
        result = prime * result + numSubUnits;
        result = prime * result + ((origin == null) ? 0 : origin.hashCode());
        result = prime * result
                + ((targetId == null) ? 0 : targetId.hashCode());
        result = prime * result
                + ((targetName == null) ? 0 : targetName.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SampleView other = (SampleView) obj;
        if (Double.doubleToLongBits(PH) != Double.doubleToLongBits(other.PH))
            return false;
        if (batchReference == null) {
            if (other.batchReference != null)
                return false;
        } else if (!batchReference.equals(other.batchReference))
            return false;
        if (cellularLocation == null) {
            if (other.cellularLocation != null)
                return false;
        } else if (!cellularLocation.equals(other.cellularLocation))
            return false;
        if (Double.doubleToLongBits(concentration) != Double
                .doubleToLongBits(other.concentration))
            return false;
        if (constructId == null) {
            if (other.constructId != null)
                return false;
        } else if (!constructId.equals(other.constructId))
            return false;
        if (constructName == null) {
            if (other.constructName != null)
                return false;
        } else if (!constructName.equals(other.constructName))
            return false;
        if (createDate == null) {
            if (other.createDate != null)
                return false;
        } else if (!createDate.equals(other.createDate))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (giNumber != other.giNumber)
            return false;
        if (sampleId == null) {
            if (other.sampleId != null)
                return false;
        } else if (!sampleId.equals(other.sampleId))
            return false;
        if (Double.doubleToLongBits(molecularWeight) != Double
                .doubleToLongBits(other.molecularWeight))
            return false;
        if (sampleName == null) {
            if (other.sampleName != null)
                return false;
        } else if (!sampleName.equals(other.sampleName))
            return false;
        if (numSubUnits != other.numSubUnits)
            return false;
        if (origin == null) {
            if (other.origin != null)
                return false;
        } else if (!origin.equals(other.origin))
            return false;
        if (targetId == null) {
            if (other.targetId != null)
                return false;
        } else if (!targetId.equals(other.targetId))
            return false;
        if (targetName == null) {
            if (other.targetName != null)
                return false;
        } else if (!targetName.equals(other.targetName))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

    public JSONObject toJSON() {
        
        JSONObject obj = new JSONObject();
        obj.put(PROP_BATCH_REFERENCE, this.getBatchReference());
        obj.put(PROP_CELLULAR_LOCATION, this.getCellularLocation());
        obj.put(PROP_CONCENTRATION, this.getConcentration());
        obj.put(PROP_CONSTRUCT_ID, this.getConstructId());
        obj.put(PROP_CONSTRUCT_NAME, this.getConstructName());
        obj.put(PROP_CREATE_DATE, this.getCreateDate());
        obj.put(PROP_DESCRIPTION, this.getDescription());
        obj.put(PROP_ID, this.getSampleId());
        obj.put(PROP_NAME, this.getSampleName());
        obj.put(PROP_PH, this.getPH());
        obj.put(PROP_MOLECULAR_WEIGHT, this.getMolecularWeight());
        obj.put(PROP_NUM_SUB_UNITS, this.getNumSubUnits());
        obj.put(PROP_ORIGIN, this.getOrigin());
        obj.put(PROP_TYPE, this.getType());
        obj.put(PROP_GI_NUMBER, this.getGiNumber());
        obj.put(PROP_TARGET_NAME, this.getTargetName());
        obj.put(PROP_TARGET_ID, this.getTargetId());
        obj.put(PROP_OWNER, this.getOwner());
        obj.put(PROP_GROUP, this.getGroup());
        
        return obj;
    }    
    
}
