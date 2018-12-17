/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.business.crystallization.view;

import net.sf.json.JSONObject;


/**
 *
 * @author ian
 */
public class ScreenView {
    public static final String PROP_NAME = "name";
    public static final String PROP_MANUFACTURER_NAME = "manufacturerName";
    public static final String PROP_TYPE = "screenType";
    //@TODO Maybe but not currently deemed necessary
    //public static final String PROP_CONDITIONS = "conditions";
    
    private String name;
    private String manufacturerName;
    private String screenType;
    //@TODO Maybe - but not currently deemed necessary
    //private List<ConditionView> conditions;

    public ScreenView() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String type) {
        this.screenType = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((manufacturerName == null) ? 0 : manufacturerName.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((screenType == null) ? 0 : screenType.hashCode());
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
        final ScreenView other = (ScreenView) obj;
        if (manufacturerName == null) {
            if (other.manufacturerName != null)
                return false;
        } else if (!manufacturerName.equals(other.manufacturerName))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (screenType == null) {
            if (other.screenType != null)
                return false;
        } else if (!screenType.equals(other.screenType))
            return false;
        return true;
    }

//    public List<ConditionView> getConditions() {
//        return conditions;
//    }
//
//    public void setConditions(List<ConditionView> conditions) {
//        this.conditions = conditions;
//    }
    
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        
        obj.put(PROP_NAME, this.getName());
        obj.put(PROP_MANUFACTURER_NAME, this.getManufacturerName());
        obj.put(PROP_TYPE, this.getScreenType());
        
        return obj;
    }
}
