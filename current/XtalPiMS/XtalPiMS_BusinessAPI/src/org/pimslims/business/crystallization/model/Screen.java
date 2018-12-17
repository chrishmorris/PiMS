/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

import java.util.HashMap;
import java.util.Map;

import org.pimslims.business.XtalObject;

/**
 * 
 * @author ian
 */
public class Screen extends XtalObject {
    private String name = "";

    private String manufacturerName = "";

    private ScreenType screenType = ScreenType.Matrix;

    private Map<WellPosition, Condition> conditionPositions = new HashMap<WellPosition, Condition>();

    public Screen() {
    }

    public Screen(String name, String manufacturerName, ScreenType screenType,
        Map<WellPosition, Condition> conditionPositions) {
        this.name = name;
        this.manufacturerName = manufacturerName;
        this.screenType = screenType;
        this.conditionPositions = conditionPositions;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Screen other = (Screen) obj;
        if (this.name != other.name && (this.name == null || !this.name.equals(other.name))) {
            return false;
        }
        if (this.manufacturerName != other.manufacturerName
            && (this.manufacturerName == null || !this.manufacturerName.equals(other.manufacturerName))) {
            return false;
        }
        return true;
    }

    public String toString(){
    	return "name:"+this.name+", manufacturerName:"+manufacturerName+", screenType:"+screenType;
    }
    @Override
    public int hashCode() {
        return toString().hashCode();
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

    public ScreenType getScreenType() {
        return screenType;
    }

    public void setScreenType(ScreenType screenType) {
        this.screenType = screenType;
    }

    public Map<WellPosition, Condition> getConditionPositions() {
        return conditionPositions;
    }

    public void setConditionPositions(Map<WellPosition, Condition> conditionPositions) {
        this.conditionPositions = conditionPositions;
    }

    public Condition getCondition(WellPosition wellPosition) {
        return this.conditionPositions.get(wellPosition);
    }

}
