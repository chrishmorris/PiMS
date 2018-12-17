package org.pimslims.presentation.protocol;

import java.util.Comparator;
import java.util.List;

import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.presentation.ModelObjectBean;

/**
 * Represents a RefOutputSample used in a protocol.
 * 
 * @author ejd53
 * 
 */
public class ParameterDefinitionBean extends ModelObjectBean {

    private final String label;

    private final String paramType;

    private final String unit;

    private final String displayUnit;

    final Protocol protocol;

    private final List<String> possibleValues;

    private final Boolean isMandatory;

    private final String maxValue;

    private final String minValue;

    private final String defaultValue;

    private final Boolean isGroupLevel;

    private final Boolean isResult;

    public ParameterDefinitionBean(final ParameterDefinition parameterDefinition) {
        super(parameterDefinition);
        this.paramType = parameterDefinition.getParamType();
        this.unit = parameterDefinition.getUnit();
        this.displayUnit = parameterDefinition.getDisplayUnit();
        this.label = parameterDefinition.getLabel();
        this.protocol = parameterDefinition.getProtocol();
        this.possibleValues = parameterDefinition.getPossibleValues();
        this.isMandatory = parameterDefinition.getIsMandatory();
        this.maxValue = parameterDefinition.getMaxValue();
        this.minValue = parameterDefinition.getMinValue();
        this.defaultValue = parameterDefinition.getDefaultValue();
        this.isGroupLevel = parameterDefinition.getIsGroupLevel();
        this.isResult = parameterDefinition.getIsResult();
    }

    public static final Comparator NUMBER_OF_EXPERIMENTS = new Comparator() {
        public int compare(final Object arg0, final Object arg1) {
            final ParameterDefinitionBean bean0 = (ParameterDefinitionBean) arg0;
            final ParameterDefinitionBean bean1 = (ParameterDefinitionBean) arg1;
            Integer bean0Size = null;
            Integer bean1Size = null;
            if (bean0 != null) {
                bean0Size = bean0.protocol.getExperiments().size();
            }
            if (bean1 != null) {
                bean1Size = bean1.protocol.getExperiments().size();
            }
            if (bean0Size == null) {
                return -1;
            }
            if (bean1Size == null) {
                return 1;
            }
            return bean1Size.compareTo(bean0Size);
        }
    };

    /**
     * @return Returns the parameter label.
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * @return Returns the parameter type.
     */
    public String getParamType() {
        return this.paramType;
    }

    /**
     * @return Returns the unit.
     */
    public String getUnit() {
        return this.unit;
    }

    /**
     * @return Returns the unit.
     */
    public String getDisplayUnit() {
        return this.displayUnit;
    }

    /**
     * @return Returns the protocol.
     */
    public Protocol getProtocol() {
        return this.protocol;
    }

    /**
     * @return Returns the possibleValues.
     */
    public List<String> getPossibleValues() {
        return this.possibleValues;
    }

    /**
     * @return Returns the possibleValues.
     */
    public String getPossibleValuesAsString() {
        final java.util.Iterator i = this.possibleValues.iterator();
        String str = "";
        while (i.hasNext()) {
            str += i.next();
            if (i.hasNext()) {
                str += ";";
            }
        }
        return str;
    }

    public String getPossibleValuesAsJavascriptArray() {
        final java.util.Iterator i = this.possibleValues.iterator();
        final StringBuffer str = new StringBuffer();
        while (i.hasNext()) {
            str.append("\"" + i.next() + "\"");
            if (i.hasNext()) {
                str.append(",");
            }
        }
        return "[" + str.toString() + "]";
    }

    /**
     * @return Returns the isMandatory.
     */
    public Boolean getIsMandatory() {
        return this.isMandatory;
    }

    /**
     * @return Returns the maxValue.
     */
    public String getMaxValue() {
        return this.maxValue;
    }

    /**
     * @return Returns the minValue.
     */
    public String getMinValue() {
        return this.minValue;
    }

    /**
     * @return Returns the defaultValue.
     */
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * @return Returns the isGroupLevel.
     */
    public Boolean getIsGroupLevel() {
        return this.isGroupLevel;
    }

    /**
     * @return Returns the isResult.
     */
    public Boolean getIsResult() {
        return this.isResult;
    }

}
