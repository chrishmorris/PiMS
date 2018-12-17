package org.pimslims.presentation.experiment;

import org.pimslims.model.experiment.Experiment;

@Deprecated
// now integrated into ExperimentBean
public class ExperimentParameterBean extends ExperimentBean {
    // final LinkedHashMap<String, String> Parameters = new LinkedHashMap<String, String>();

    public ExperimentParameterBean(final Experiment exp) {
        super(exp);
        /*   final List<Parameter> parameters = new LinkedList<Parameter>(exp.getParameters());
           Collections.sort(parameters, new ParameterComparator());
           for (final Parameter parameter : parameters) {
               this.Parameters.put(parameter.getName(), parameter.getValue());
           } */

    }

    /**
     * @return the parameters
     * 
     *         public Map<String, String> getParameters() { return Parameters; }
     */

}
