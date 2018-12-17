/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.business.crystallization.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 *
 * @author ian
 */
public class ConditionView  {
    public static final String PROP_ID = "conditionId";
    public static final String PROP_WELL = "well";
    public static final String PROP_LOCAL_NAME = "localName";
    public static final String PROP_LOCAL_NUMBER = "localNumber";
    public static final String PROP_MANUFACTURER_NAME = "manufacturerName";
    public static final String PROP_MANUFACTURER_SCREEN_NAME = "manufacturerScreenName";
    public static final String PROP_MANUFACTURER_CODE = "manufacturerCode";
    public static final String PROP_MANUFACTURER_CAT_CODE = "manufacturerCatCode";
    public static final String PROP_VOLATILE_CONDITION = "volatileCondition";
    public static final String PROP_FINAL_PH = "finalPH";
    public static final String PROP_SALT_CONDITION = "saltCondition";
    public static final String PROP_COMPONENTS = "components";

    private Long conditionId;
    private String well;
    private String localName;
    private String localNumber;
    private String manufacturerName;
    private String manufacturerScreenName;
    private String manufacturerCode;
    private String manufacturerCatCode;
    private Boolean volatileCondition;
    private Double finalpH;
    private Boolean saltCondition = false;
    private List<ComponentQuantityView> components;

    public ConditionView() {
        
    }
    
    /**
     * @param id
     * @param well
     * @param localName
     * @param localNumber
     * @param manufacturerName
     * @param manufacturerScreenName
     * @param manufacturerCode
     * @param manufacturerCatCode
     * @param volatileCondition
     * @param finalpH
     * @param components
     */
    public ConditionView(Long id, String well, String localName,
            String localNumber, String manufacturerName,
            String manufacturerScreenName, String manufacturerCode,
            String manufacturerCatCode, Boolean volatileCondition,
            Double finalpH, List<ComponentQuantityView> components) {
        super();
        this.conditionId = id;
        this.well = well;
        this.localName = localName;
        this.localNumber = localNumber;
        this.manufacturerName = manufacturerName;
        this.manufacturerScreenName = manufacturerScreenName;
        this.manufacturerCode = manufacturerCode;
        this.manufacturerCatCode = manufacturerCatCode;
        this.volatileCondition = volatileCondition;
        this.finalpH = finalpH;
        this.components = components;
    }

    /**
     * @param well
     * @param localName
     * @param localNumber
     * @param manufacturerName
     * @param manufacturerScreenName
     * @param manufacturerCode
     * @param manufacturerCatCode
     * @param volatileCondition
     * @param finalpH
     * @param components
     */
    public ConditionView(String well, String localName, String localNumber,
            String manufacturerName, String manufacturerScreenName,
            String manufacturerCode, String manufacturerCatCode,
            Boolean volatileCondition, Double finalpH,
            List<ComponentQuantityView> components) {
        super();
        this.well = well;
        this.localName = localName;
        this.localNumber = localNumber;
        this.manufacturerName = manufacturerName;
        this.manufacturerScreenName = manufacturerScreenName;
        this.manufacturerCode = manufacturerCode;
        this.manufacturerCatCode = manufacturerCatCode;
        this.volatileCondition = volatileCondition;
        this.finalpH = finalpH;
        this.components = components;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((finalpH == null) ? 0 : finalpH.hashCode());
        result = prime * result + ((conditionId == null) ? 0 : conditionId.hashCode());
        result = prime * result
                + ((localName == null) ? 0 : localName.hashCode());
        result = prime * result
                + ((localNumber == null) ? 0 : localNumber.hashCode());
        result = prime
                * result
                + ((manufacturerCatCode == null) ? 0 : manufacturerCatCode
                        .hashCode());
        result = prime
                * result
                + ((manufacturerCode == null) ? 0 : manufacturerCode.hashCode());
        result = prime
                * result
                + ((manufacturerName == null) ? 0 : manufacturerName.hashCode());
        result = prime
                * result
                + ((manufacturerScreenName == null) ? 0
                        : manufacturerScreenName.hashCode());
        result = prime
                * result
                + ((volatileCondition == null) ? 0 : volatileCondition
                        .hashCode());
        result = prime * result + ((well == null) ? 0 : well.hashCode());
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
        final ConditionView other = (ConditionView) obj;

        if (conditionId == null) {
            if (other.conditionId != null)
                return false;
        } else if (!conditionId.equals(other.conditionId))
            return false;
        if (localName == null) {
            if (other.localName != null)
                return false;
        } else if (!localName.equals(other.localName))
            return false;
        if (localNumber == null) {
            if (other.localNumber != null)
                return false;
        } else if (!localNumber.equals(other.localNumber))
            return false;
        
        if (manufacturerCode == null) {
            if (other.manufacturerCode != null)
                return false;
        } else if (!manufacturerCode.equals(other.manufacturerCode))
            return false;
        if (manufacturerName == null) {
            if (other.manufacturerName != null)
                return false;
        } else if (!manufacturerName.equals(other.manufacturerName))
            return false;
        if (manufacturerScreenName == null) {
            if (other.manufacturerScreenName != null)
                return false;
        } else if (!manufacturerScreenName.equals(other.manufacturerScreenName))
            return false;

        return true;
    }

    public Long getConditionId() {
        return conditionId;
    }

    public void setConditionId(Long id) {
        this.conditionId = id;
    }

    public String getWell() {
        return well;
    }

    public void setWell(String well) {
        this.well = well;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getLocalNumber() {
        return localNumber;
    }

    public void setLocalNumber(String localNumber) {
        this.localNumber = localNumber;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getManufacturerScreenName() {
        return manufacturerScreenName;
    }

    public void setManufacturerScreenName(String manufacturerScreenName) {
        this.manufacturerScreenName = manufacturerScreenName;
    }

    public String getManufacturerCode() {
        return manufacturerCode;
    }

    public void setManufacturerCode(String manufacturerCode) {
        this.manufacturerCode = manufacturerCode;
    }

    public String getManufacturerCatCode() {
        return manufacturerCatCode;
    }

    public void setManufacturerCatCode(String manufacturerCatCode) {
        this.manufacturerCatCode = manufacturerCatCode;
    }

    public Boolean isVolatileCondition() {
        return volatileCondition;
    }

    public void setVolatileCondition(Boolean volatileCondition) {
        this.volatileCondition = volatileCondition;
    }

    public Double getFinalpH() {
        return finalpH;
    }

    public void setFinalpH(Double finalpH) {
        this.finalpH = finalpH;
    }

    public List<ComponentQuantityView> getComponents() {
        if (components == null) {
            components = new ArrayList<ComponentQuantityView>();
        }
        return components;
    }

    public void setComponents(List<ComponentQuantityView> components) {
        this.components = components;
    }
    
    public void addComponent(ComponentQuantityView component) {
        getComponents().add(component);
    }

    public Boolean getSaltCondition() {
        return saltCondition;
    }

    public void setSaltCondition(Boolean saltCondition) {
        this.saltCondition = saltCondition;
    }

    public Boolean getVolatileCondition() {
        return volatileCondition;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put(PROP_ID, this.getConditionId());
        obj.put(PROP_WELL, this.getWell());
        obj.put(PROP_LOCAL_NAME, this.getLocalName());
        obj.put(PROP_LOCAL_NUMBER, this.getLocalNumber());
        obj.put(PROP_MANUFACTURER_NAME, this.getManufacturerName());
        obj.put(PROP_MANUFACTURER_SCREEN_NAME, this.getManufacturerScreenName());
        obj.put(PROP_MANUFACTURER_CODE, this.getManufacturerCode());
        obj.put(PROP_MANUFACTURER_CAT_CODE, this.getManufacturerCatCode());
        obj.put(PROP_VOLATILE_CONDITION, this.isVolatileCondition());
        if( this.getFinalpH()!=0){
        obj.put(PROP_FINAL_PH, String.format("%.2f", this.getFinalpH()));
        }
        obj.put(PROP_SALT_CONDITION, this.getSaltCondition());
        JSONArray objs = new JSONArray();
        Iterator<ComponentQuantityView> it = this.getComponents().iterator();
        while (it.hasNext()) {
            ComponentQuantityView component = it.next();
            
            objs.add(component.toJSON());
        }
        obj.put(PROP_COMPONENTS, objs);
        
        return obj;
    }
    
}
