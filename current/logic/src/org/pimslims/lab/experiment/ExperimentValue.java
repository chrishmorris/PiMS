package org.pimslims.lab.experiment;

import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;

@Deprecated
// suspected obsolete
public interface ExperimentValue {

    void set(Experiment experiment, Object value) throws ConstraintException;

    Object get(Experiment experiment);

}
