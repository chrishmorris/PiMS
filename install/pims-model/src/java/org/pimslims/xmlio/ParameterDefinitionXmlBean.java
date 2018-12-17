/**
 * pims-web org.pimslims.xmlio ParameterDefinitionXmlBean.java
 * 
 * @author pajanne
 * @date Jan 14, 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.xmlio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

import org.pimslims.model.protocol.ParameterDefinition;

/**
 * ParameterDefinitionXmlBean
 * 
 */
@XmlType(name = "", propOrder = { "name", "label", "paramType", "unit", "displayUnit", "defaultValue",
    "minValue", "maxValue", "isMandatory", "isGroupLevel", "isResult", "possibleValues" })
public class ParameterDefinitionXmlBean {
    private String name;

    private String label;

    private String paramType;

    private String unit;

    private String displayUnit;

    private String defaultValue;

    private String minValue;

    private String maxValue;

    private Boolean isMandatory = true;

    private Boolean isGroupLevel = false;

    private Boolean isResult = false;

    private List<String> possibleValues = new ArrayList<String>(0);

    /**
     * Constructor for ParameterDefinitionXmlBean
     * 
     * @param org.pimslims.model.protocol.ParameterDefinition
     */
    public ParameterDefinitionXmlBean(final ParameterDefinition parameterDefinition) {
        this.name = parameterDefinition.getName();
        this.label = parameterDefinition.getLabel();
        this.paramType = parameterDefinition.getParamType();
        this.unit = parameterDefinition.getUnit();
        this.displayUnit = parameterDefinition.getDisplayUnit();
        this.defaultValue = parameterDefinition.getDefaultValue();
        this.minValue = parameterDefinition.getMinValue();
        this.maxValue = parameterDefinition.getMaxValue();
        this.isMandatory = parameterDefinition.getIsMandatory();
        this.isGroupLevel = parameterDefinition.getIsGroupLevel();
        this.isResult = parameterDefinition.getIsResult();
        this.possibleValues = parameterDefinition.getPossibleValues();
    }

    // Required by jaxb
    private ParameterDefinitionXmlBean() {
        // required by jaxb
    }

    /**
     * @return Returns the name.
     */
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
     * @return Returns the label.
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * @param label The label to set.
     */
    public void setLabel(final String label) {
        this.label = label;
    }

    /**
     * @return Returns the paramType.
     */
    public String getParamType() {
        return this.paramType;
    }

    /**
     * @param paramType The paramType to set.
     */
    public void setParamType(final String paramType) {
        this.paramType = paramType;
    }

    /**
     * @return Returns the unit.
     */
    public String getUnit() {
        return this.unit;
    }

    /**
     * @param unit The unit to set.
     */
    public void setUnit(final String unit) {
        this.unit = unit;
    }

    /**
     * @return Returns the displayUnit.
     */
    public String getDisplayUnit() {
        return this.displayUnit;
    }

    /**
     * @param displayUnit The displayUnit to set.
     */
    public void setDisplayUnit(final String displayUnit) {
        this.displayUnit = displayUnit;
    }

    /**
     * @return Returns the defaultValue.
     */
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * @param defaultValue The defaultValue to set.
     */
    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @return Returns the minValue.
     */
    public String getMinValue() {
        return this.minValue;
    }

    /**
     * @param minValue The minValue to set.
     */
    public void setMinValue(final String minValue) {
        this.minValue = minValue;
    }

    /**
     * @return Returns the maxValue.
     */
    public String getMaxValue() {
        return this.maxValue;
    }

    /**
     * @param maxValue The maxValue to set.
     */
    public void setMaxValue(final String maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * @return Returns the isMandatory.
     */
    public Boolean getIsMandatory() {
        return this.isMandatory;
    }

    /**
     * @param isMandatory The isMandatory to set.
     */
    public void setIsMandatory(final Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    /**
     * @return Returns the isGroupLevel.
     */
    public Boolean getIsGroupLevel() {
        return this.isGroupLevel;
    }

    /**
     * @param isGroupLevel The isGroupLevel to set.
     */
    public void setIsGroupLevel(final Boolean isGroupLevel) {
        this.isGroupLevel = isGroupLevel;
    }

    /**
     * @return Returns the isResult.
     */
    public Boolean getIsResult() {
        return this.isResult;
    }

    /**
     * @param isResult The isResult to set.
     */
    public void setIsResult(final Boolean isResult) {
        this.isResult = isResult;
    }

    /**
     * @return Returns the possibleValues.
     */
    public List<String> getPossibleValues() {
        return this.possibleValues;
    }

    /**
     * @param possibleValues The possibleValues to set.
     */
    public void setPossibleValues(final List<String> possibleValues) {
        this.possibleValues = possibleValues;
    }

    /**
     * Returns ParameterDefinition attribute map for creating associated model object.
     * 
     * @return attribute map
     */
    public Map<String, Object> getAttributeMap() {
        final Map<String, Object> attributeMap = new HashMap<String, Object>();
        attributeMap.put(ParameterDefinition.PROP_NAME, this.name);
        attributeMap.put(ParameterDefinition.PROP_LABEL, this.label);
        attributeMap.put(ParameterDefinition.PROP_PARAMTYPE, this.paramType);
        attributeMap.put(ParameterDefinition.PROP_UNIT, this.unit);
        attributeMap.put(ParameterDefinition.PROP_DISPLAYUNIT, this.displayUnit);
        attributeMap.put(ParameterDefinition.PROP_DEFAULTVALUE, this.defaultValue);
        attributeMap.put(ParameterDefinition.PROP_MINVALUE, this.minValue);
        attributeMap.put(ParameterDefinition.PROP_MAXVALUE, this.maxValue);
        attributeMap.put(ParameterDefinition.PROP_ISMANDATORY, this.isMandatory);
        attributeMap.put(ParameterDefinition.PROP_ISGROUPLEVEL, this.isGroupLevel);
        attributeMap.put(ParameterDefinition.PROP_ISRESULT, this.isResult);
        attributeMap.put(ParameterDefinition.PROP_POSSIBLEVALUES, this.possibleValues);
        return attributeMap;
    }

}
