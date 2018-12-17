package org.pimslims.lab.experiment;

import java.util.Collection;
import java.util.HashSet;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;

public class ExperimentParameterSetter implements ExperimentValue {

    private final String parameterName;

    public ExperimentParameterSetter(final String string) {
        this.parameterName = string;
    }

    public void set(final Experiment experiment, final Object value) throws ConstraintException {
        final Collection<Parameter> parameters = this.getParameters(experiment);
        Parameter parameter;
        if (0 == parameters.size()) {
            try {
                parameter = new Parameter((WritableVersion) experiment.get_Version(), experiment);
                parameter.setName(this.parameterName);
            } catch (final ConstraintException e) {
                throw new RuntimeException(e); // should not happen
            }
        } else {
            parameter = parameters.iterator().next();
        }
        if (value == null) {
            parameter.setValue(null);
        } else {
            parameter.setValue(value.toString());
        }
    }

    public Object get(final Experiment experiment) {
        final Collection<Parameter> parameters = this.getParameters(experiment);
        if (0 == parameters.size()) {
            return null;
        }
        final Parameter parameter = parameters.iterator().next();
        return parameter.getValue();
    }

    private Collection<Parameter> getParameters(final Experiment experiment) {
        final Collection<Parameter> allParameters = experiment.getParameters();
        final Collection<Parameter> foundParameters = new HashSet<Parameter>();
        for (final Parameter p : allParameters) {
            if (p.getName().equalsIgnoreCase(this.parameterName)) {
                foundParameters.add(p);
            }
        }
        return foundParameters;
    }

}
