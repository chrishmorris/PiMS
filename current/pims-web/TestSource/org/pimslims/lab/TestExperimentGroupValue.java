package org.pimslims.lab;

import java.util.Calendar;
import java.util.Collections;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.ExperimentInputSampleSetter;
import org.pimslims.lab.experiment.ExperimentParameterSetter;
import org.pimslims.lab.experiment.ExperimentValue;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;

/*
 * Tests the facilities for getting and setting values for an experiment in a group
 */
public class TestExperimentGroupValue extends TestCase {

    private Experiment experiment;

    private Sample sample;

    private ExperimentType experimentType;

    private SampleCategory category;

    private WritableVersion wv;

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp() Note that this code is not transaction safe. Do not copy it,
     *      except for test purposes.
     */
    @Override
    protected void setUp() throws Exception {

        final ModelImpl model = (ModelImpl) ModelImpl.getModel();
        this.wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        this.experimentType = new ExperimentType(this.wv, "teg" + System.currentTimeMillis());
        final Calendar now = java.util.Calendar.getInstance();
        this.experiment =
            new Experiment(this.wv, "teg" + System.currentTimeMillis(), now, now, this.experimentType);
        final Parameter parm = new Parameter(this.wv, this.experiment);
        parm.setName("temperature");

        this.category = new SampleCategory(this.wv, "teg" + System.currentTimeMillis());
        this.sample = new Sample(this.wv, "teg" + System.currentTimeMillis());
        this.sample.setSampleCategories(Collections.singleton(this.category));

    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        this.sample.delete();
        this.experiment.delete();
        this.experimentType.delete();
        this.category.delete();
        this.wv.abort();
    }

    public void testGetParameter() {
        try {
            final ExperimentValue parameter = new ExperimentParameterSetter("temperature");

            parameter.set(this.experiment, "37.0");

            final String temp = (String) parameter.get(this.experiment);
            Assert.assertEquals("set and get", "37.0", temp);
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testGetInputSample() {
        try {
            final ExperimentValue input = new ExperimentInputSampleSetter("medium");
            input.set(this.experiment, this.sample);
            Assert.assertEquals("set and get", this.sample, input.get(this.experiment));
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        }

    }

}
