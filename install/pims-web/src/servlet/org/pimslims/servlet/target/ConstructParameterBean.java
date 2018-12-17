package org.pimslims.servlet.target;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Map;

import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ModelObjectShortBean;

public class ConstructParameterBean extends ModelObjectShortBean implements Comparable<Object>, Serializable {
    String targetName;

    String targetHook;

    String constructName;

    String constructHook;

    Map<String, java.util.List<String>> parameters;// name->value

    public ConstructParameterBean(final Target target, final ResearchObjective expb,
        final java.util.List<ParameterDefinition> pds) {
        this.constructName = expb.getCommonName();
        this.constructHook = expb.get_Hook();
        this.targetName = target.getName();
        this.targetHook = target.get_Hook();
        this.parameters = new java.util.LinkedHashMap<String, java.util.List<String>>();
        for (final ParameterDefinition pd : pds) {
            final java.util.List<String> values = new LinkedList<String>();
            for (final Experiment exp : expb.getExperiments()) {
                for (final Parameter parameter : exp.getParameters()) {
                    if (pd == parameter.getParameterDefinition()) {
                        values.add(parameter.getValue());
                    }
                }
            }
            this.parameters.put(pd.getName(), values);
        }
    }

    /**
     * @return the targetName
     */
    public String getTargetName() {
        return this.targetName;
    }

    /**
     * @param targetName the targetName to set
     */
    public void setTargetName(final String targetName) {
        this.targetName = targetName;
    }

    /**
     * @return the targetHook
     */
    public String getTargetHook() {
        return this.targetHook;
    }

    /**
     * @param targetHook the targetHook to set
     */
    public void setTargetHook(final String targetHook) {
        this.targetHook = targetHook;
    }

    /**
     * @return the constructName
     */
    public String getConstructName() {
        return this.constructName;
    }

    /**
     * @param constructName the constructName to set
     */
    public void setConstructName(final String constructName) {
        this.constructName = constructName;
    }

    /**
     * @return the constructHook
     */
    public String getConstructHook() {
        return this.constructHook;
    }

    /**
     * @param constructHook the constructHook to set
     */
    public void setConstructHook(final String constructHook) {
        this.constructHook = constructHook;
    }

    /**
     * @return the parameters
     */
    public Map<String, java.util.List<String>> getParameters() {
        return this.parameters;
    }

}
