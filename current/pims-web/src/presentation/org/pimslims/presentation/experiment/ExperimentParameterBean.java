package org.pimslims.presentation.experiment;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pimslims.lab.experiment.ParameterComparator;

import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;

public class ExperimentParameterBean extends ExperimentBean {
    LinkedHashMap<String, String> Parameters = new LinkedHashMap<String, String>();

    static Comparator<Parameter> comparator = new ParameterComparator();

    public ExperimentParameterBean(Experiment exp) {
        super(exp);
        List<Parameter> parameters = new LinkedList<Parameter>(exp.getParameters());
        Collections.sort(parameters, comparator);
        for (Parameter parameter : parameters) {
            Parameters.put(parameter.getName(), parameter.getValue());
        }

    }

    /**
     * @return the parameters
     */
    public Map<String, String> getParameters() {
        return Parameters;
    }

}
