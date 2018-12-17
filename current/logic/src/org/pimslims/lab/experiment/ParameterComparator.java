package org.pimslims.lab.experiment;

import java.util.Comparator;

import org.pimslims.model.experiment.Parameter;

public class ParameterComparator implements Comparator<Parameter> {

    @Override
    public int compare(final Parameter o1, final Parameter o2) {
        if (o1 == null || o2 == null) {
            return 0;
        } else if (o1.getParameterDefinition() == null || o2.getParameterDefinition() == null) {
            if (null == o1.getName() || null == o2.getName()) {
                return 0;
            }
            return o1.getName().compareTo(o2.getName());
        } else {
            return o1.getParameterDefinition().get_Name().compareTo(o2.getParameterDefinition().get_Name());
        }
    }

}
