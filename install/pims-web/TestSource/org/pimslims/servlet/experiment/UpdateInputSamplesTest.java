package org.pimslims.servlet.experiment;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;

public class UpdateInputSamplesTest extends TestCase {

    private static final String UNIQUE = "uis" + System.currentTimeMillis();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(UpdateInputSamplesTest.class);
    }

    private final AbstractModel model;

    public UpdateInputSamplesTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public final void testProcessEmptyRequest() throws AccessException, ConstraintException,
        ServletException, ParseException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            UpdateInputSamples.processRequest(version, Collections.EMPTY_MAP);
        } finally {
            version.abort(); // not testing persistence here
        }

    }

    public final void testSetSample() throws AccessException, ConstraintException, ServletException,
        ParseException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final Calendar now = java.util.Calendar.getInstance();
            final ExperimentType type = new ExperimentType(version, "test" + System.currentTimeMillis());
            final Experiment experiment =
                new Experiment(version, "testSetSample" + System.currentTimeMillis(), now, now, type);
            final Sample sample = new Sample(version, "name2");
            final InputSample inputSample = new InputSample(version, experiment);
            parms.put(inputSample.get_Hook() + ":sample", new String[] { sample.get_Hook() });
            UpdateInputSamples.processRequest(version, parms);
            Assert.assertEquals(sample, inputSample.getSample());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public final void testSetTarget() throws AccessException, ConstraintException, ServletException,
        ParseException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final Sample sample = new Sample(version, UpdateInputSamplesTest.UNIQUE + "2");

            // set target for sample
            final Calendar now = java.util.Calendar.getInstance();
            final ExperimentType type = new ExperimentType(version, "test" + System.currentTimeMillis());
            final ResearchObjective target =
                new ResearchObjective(version, "test" + System.currentTimeMillis(), "test");
            final Experiment before =
                new Experiment(version, "test" + System.currentTimeMillis(), now, now, type);
            before.setProject(target);
            final OutputSample os = new OutputSample(version, before);
            os.setSample(sample);

            final Experiment experiment =
                new Experiment(version, "testSetTarget" + System.currentTimeMillis(), now, now, type);
            final InputSample inputSample = new InputSample(version, experiment);
            parms.put(inputSample.get_Hook() + ":sample", new String[] { sample.get_Hook() });
            UpdateInputSamples.processRequest(version, parms);
            Assert.assertEquals(target, experiment.getProject());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public final void testAmount() throws AccessException, ConstraintException, ServletException,
        ParseException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final Calendar now = java.util.Calendar.getInstance();
            final ExperimentType type = new ExperimentType(version, "test" + System.currentTimeMillis());
            final Experiment experiment =
                new Experiment(version, "testAmount" + System.currentTimeMillis(), now, now, type);
            final InputSample inputSample = new InputSample(version, experiment);
            parms.put(inputSample.get_Hook() + ":amount", new String[] { "50uL" });
            UpdateInputSamples.processRequest(version, parms);
            Assert.assertEquals("uL", inputSample.getAmountDisplayUnit());
            Assert.assertEquals(50E-6f, inputSample.getAmount(), 1E-10f);
        } finally {
            version.abort(); // not testing persistence here
        }
    }

}
