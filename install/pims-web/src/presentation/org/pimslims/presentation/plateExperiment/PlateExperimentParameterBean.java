package org.pimslims.presentation.plateExperiment;

import java.util.List;

import org.pimslims.model.experiment.Parameter;
import org.pimslims.presentation.ModelObjectShortBean;

public class PlateExperimentParameterBean extends ModelObjectShortBean {

    private Long definitionDbId = null;

    private String value = "";

    private String paramType = "";

    private boolean isScore;

    private Object[] possibleValues;

    public PlateExperimentParameterBean(final Parameter modelObject) {
        super(modelObject);
        if (null != modelObject.getParameterDefinition()) {
            this.definitionDbId = modelObject.getParameterDefinition().getDbId();
            this.isScore = modelObject.getParameterDefinition().getName().startsWith("__");
            final List<String> values = modelObject.getParameterDefinition().getPossibleValues();
            this.possibleValues = values.toArray();
            this.value = modelObject.getValue();
            this.paramType = modelObject.getParamType();
        }
    }

    /**
     * @return
     */
    public String getValue() {

        if ("Boolean".equals(this.paramType)) {
            // coerce value to one expected by JSP
            if ("yes".equalsIgnoreCase(this.value) || "true".equalsIgnoreCase(this.value)) {
                this.value = "true";
            } else if ("no".equalsIgnoreCase(this.value) || "false".equalsIgnoreCase(this.value)) {
                this.value = "false";
            }
        }
        return this.value;
    }

    /**
     * @return
     */
    public Long getDefinitionDbId() {
        return this.definitionDbId;
    }

    public boolean getIsScoreParameter() {
        return this.isScore;
    }

    public int getIntValue() {
        if (null == this.possibleValues) {
            return -1;
        }
        for (int i = 0; i < this.possibleValues.length; i++) {
            final String myValue = (String) this.possibleValues[i];
            if (this.value.equals(myValue)) {
                return i;
            }
        }
        return -1;
    }

}
