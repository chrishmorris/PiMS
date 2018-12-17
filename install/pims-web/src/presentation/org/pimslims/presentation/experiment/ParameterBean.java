package org.pimslims.presentation.experiment;

import org.pimslims.model.experiment.Parameter;
import org.pimslims.presentation.ModelObjectBean;

public class ParameterBean extends ModelObjectBean {

    private Long definitionDbId = null;

    private final String label;

    public ParameterBean(final Parameter modelObject) {
        super(modelObject);
        if (null != modelObject.getParameterDefinition()) {
            this.definitionDbId = modelObject.getParameterDefinition().getDbId();
        }
        this.label =
            null == modelObject.getParameterDefinition() ? modelObject.getName() : modelObject
                .getParameterDefinition().getLabel();
    }

    /**
     * @return
     */
    public String getValue() {
        String value = (String) super.getValues().get(Parameter.PROP_VALUE);
        if ("Boolean".equals(super.getValues().get(Parameter.PROP_PARAMTYPE))) {
            // coerce value to one expected by JSP
            if ("yes".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value)) {
                value = "true";
            } else if ("no".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                value = "false";
            }
        }
        return value;
    }

    /**
     * @return
     */
    public Long getDefinitionDbId() {
        return this.definitionDbId;
    }

    public String getLabel() {
        return this.label;
    }

}
