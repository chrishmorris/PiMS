package org.pimslims.graph.implementation;

import java.util.Calendar;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.AbstractModel;
import org.pimslims.exception.ConstraintException;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;

public class PlateExperimentModelTest extends TestCase {

    private static final Calendar NOW = java.util.Calendar.getInstance();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(PlateExperimentModelTest.class);
    }

    private final AbstractModel model;

    public PlateExperimentModelTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /*
     * Test method for
     * 'org.pimslims.utils.graph.implementation.PlateExperimentModel.PlateExperimentModel(ReadableVersion,
     * Collection<ExperimentGroup>)'
     */
    public void test1PlateExperimentModel() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final ExperimentGroup group =
                new ExperimentGroup(version, "one" + System.currentTimeMillis(), "test");
            final PlateExperimentModel model = new PlateExperimentModel(group);
            Assert.assertEquals(1, model.getNodes().size());
            Assert.assertEquals(0, model.getEdges().size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

    /*
     * Test method for
     * 'org.pimslims.utils.graph.implementation.PlateExperimentModel.PlateExperimentModel(ReadableVersion,
     * Collection<ExperimentGroup>)'
     */
    public void testNextPlateExperimentModel() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final ExperimentGroup group =
                new ExperimentGroup(version, "group" + System.currentTimeMillis(), "test");
            final ExperimentType type =
                new ExperimentType(version, "type" + System.currentTimeMillis());
            final Experiment experiment =
                new Experiment(version, "exp" + System.currentTimeMillis(), PlateExperimentModelTest.NOW,
                    PlateExperimentModelTest.NOW, type);
            group.addExperiment(experiment);
            new OutputSample(version, experiment);
            final OutputSample os = new OutputSample(version, experiment);
            final Sample sample = new Sample(version, "sample" + System.currentTimeMillis());
            os.setSample(sample);
            final Experiment experiment2 =
                new Experiment(version, "exp2" + System.currentTimeMillis(), PlateExperimentModelTest.NOW,
                    PlateExperimentModelTest.NOW, type);
            final InputSample is = new InputSample(version, experiment2);
            is.setSample(sample);
            final ExperimentGroup group2 =
                new ExperimentGroup(version, "grouptwo" + System.currentTimeMillis(), "test");
            group2.addExperiment(experiment2);
            final PlateExperimentModel model = new PlateExperimentModel(group);
            Assert.assertEquals(2, model.getNodes().size());
            Assert.assertEquals(1, model.getEdges().size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

    /*
     * Test method for
     * 'org.pimslims.utils.graph.implementation.PlateExperimentModel.PlateExperimentModel(ReadableVersion,
     * Collection<ExperimentGroup>)'
     */
    public void testPreviousPlateExperimentModel() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final ExperimentGroup group =
                new ExperimentGroup(version, "group" + System.currentTimeMillis(), "test");
            final ExperimentType type =
                new ExperimentType(version, "type" + System.currentTimeMillis());
            final Experiment experiment =
                new Experiment(version, "exp" + System.currentTimeMillis(), PlateExperimentModelTest.NOW,
                    PlateExperimentModelTest.NOW, type);
            group.addExperiment(experiment);
            new OutputSample(version, experiment);
            final OutputSample os = new OutputSample(version, experiment);
            final Sample sample = new Sample(version, "sample" + System.currentTimeMillis());
            os.setSample(sample);
            final Experiment experiment2 =
                new Experiment(version, "exp2" + System.currentTimeMillis(), PlateExperimentModelTest.NOW,
                    PlateExperimentModelTest.NOW, type);
            final InputSample is = new InputSample(version, experiment2);
            is.setSample(sample);
            final ExperimentGroup group2 =
                new ExperimentGroup(version, "grouptwo" + System.currentTimeMillis(), "test");
            group2.addExperiment(experiment2);
            final PlateExperimentModel model = new PlateExperimentModel(group);
            Assert.assertEquals(2, model.getNodes().size());
            Assert.assertEquals(1, model.getEdges().size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

}
