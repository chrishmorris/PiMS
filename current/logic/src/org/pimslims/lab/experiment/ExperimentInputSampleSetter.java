package org.pimslims.lab.experiment;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.sample.Sample;

@Deprecated
// incorrect, does not set Project or propagate parameters. Beleived unused.
public class ExperimentInputSampleSetter implements ExperimentValue {

    private final String name;

    public ExperimentInputSampleSetter(final String name) {
        super();
        this.name = name;
    }

    @Override
	public void set(final Experiment experiment, final Object value) throws ConstraintException {
        final InputSample is = new InputSample((WritableVersion) experiment.get_Version(), experiment);
        is.setName(this.name);
        is.setSample((Sample) value);
    }

    @Override
	public Sample get(final Experiment experiment) {
        // was: List<InputSample> iss = experiment.findAllSampleIos("name",
        // name);
        for (final InputSample is : experiment.getInputSamples()) {
            if (is.get_Name().equals(this.name)) {
                return is.getSample();
            }
        }
        return null;
    }

}
