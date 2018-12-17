package org.pimslims.presentation.experiment;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.NMR;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.mru.MRUController;

public class ExperimentReaderTest extends TestCase {

    /**
     * 
     */
    private static final String UNIQUE = "test" + System.currentTimeMillis();

    private static final Calendar NOW = java.util.Calendar.getInstance();

    final private AbstractModel model;

    public ExperimentReaderTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public final void testExperimentReader() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ExperimentReaderTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, ExperimentReaderTest.UNIQUE, ExperimentReaderTest.NOW,
                    ExperimentReaderTest.NOW, type);
            final ExperimentReader reader = new ExperimentReader(version, experiment);
            Assert.assertTrue(reader.get_MayUpdate());
            Assert.assertEquals(experiment, reader.getExperiment());
            Assert.assertEquals(Access.REFERENCE, reader.getOwner());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testGetNoPossibleSamples() throws AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ExperimentReaderTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, ExperimentReaderTest.UNIQUE, ExperimentReaderTest.NOW,
                    ExperimentReaderTest.NOW, type);
            new InputSample(version, experiment);

            final List<InputSampleBean> inputSamples =
                ExperimentReader.getInputSamples(experiment, Access.ADMINISTRATOR, Collections.EMPTY_SET);
            Assert.assertNotNull(inputSamples);
            Assert.assertEquals(1, inputSamples.size());
            final InputSampleBean bean = inputSamples.iterator().next();
            Assert.assertEquals(0, bean.getSamples().size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testGetOnePossibleSample() throws AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ExperimentReaderTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, ExperimentReaderTest.UNIQUE, ExperimentReaderTest.NOW,
                    ExperimentReaderTest.NOW, type);
            final InputSample inputSample = new InputSample(version, experiment);
            final Sample possible = new Sample(version, ExperimentReaderTest.UNIQUE);
            possible.setIsActive(Boolean.TRUE);
            final SampleCategory category = new SampleCategory(version, "sc" + System.currentTimeMillis());
            possible.addSampleCategory(category);
            final Protocol protocol = new Protocol(version, ExperimentReaderTest.UNIQUE, type);
            final RefInputSample ris = new RefInputSample(version, category, protocol);
            inputSample.setRefInputSample(ris);
            MRUController.addObject(Access.ADMINISTRATOR, possible);

            final List<InputSampleBean> inputSamples =
                ExperimentReader.getInputSamples(experiment, Access.ADMINISTRATOR, Collections.EMPTY_SET);
            Assert.assertNotNull(inputSamples);
            Assert.assertEquals(1, inputSamples.size());
            final InputSampleBean bean = inputSamples.iterator().next();
            Assert.assertEquals(1, bean.getSamples().size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testNotSuitable() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final SampleCategory category = new SampleCategory(version, ExperimentReaderTest.UNIQUE);
            final ExperimentType type =
                version.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, NMR.EXPERIMENT_TYPE);
            final Protocol protocol = new Protocol(version, ExperimentReaderTest.UNIQUE, type);
            new ParameterDefinition(version, NMR.PARAMETER_DEFINITION, "String", protocol)
                .setDefaultValue("14N");
            final RefInputSample ris = new RefInputSample(version, category, protocol);
            final Experiment experiment =
                new Experiment(version, ExperimentReaderTest.UNIQUE, ExperimentReaderTest.NOW,
                    ExperimentReaderTest.NOW, type);
            final InputSample inputSample = new InputSample(version, experiment);
            inputSample.setRefInputSample(ris);
            final Sample sample = new Sample(version, ExperimentReaderTest.UNIQUE + " 2H13C");
            sample.addSampleCategory(category);
            sample.setIsActive(true);

            final List<InputSampleBean> inputSamples =
                ExperimentReader.getInputSamples(experiment, Access.ADMINISTRATOR, Collections.EMPTY_SET);
            Assert.assertNotNull(inputSamples);
            Assert.assertEquals(1, inputSamples.size());
            final InputSampleBean bean = inputSamples.iterator().next();
            Assert.assertEquals(0, bean.getSamples().size());
        } finally {
            version.abort();
        }

    }

    public void testSuitable() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final SampleCategory category = new SampleCategory(version, ExperimentReaderTest.UNIQUE);
            final ExperimentType type =
                version.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, NMR.EXPERIMENT_TYPE);
            final Protocol protocol = new Protocol(version, ExperimentReaderTest.UNIQUE, type);
            new ParameterDefinition(version, NMR.PARAMETER_DEFINITION, "String", protocol)
                .setDefaultValue("2H13C");
            final RefInputSample ris = new RefInputSample(version, category, protocol);
            final Experiment experiment =
                new Experiment(version, ExperimentReaderTest.UNIQUE, ExperimentReaderTest.NOW,
                    ExperimentReaderTest.NOW, type);
            final InputSample inputSample = new InputSample(version, experiment);
            inputSample.setRefInputSample(ris);
            final Sample sample = new Sample(version, ExperimentReaderTest.UNIQUE + " 2H13C");
            sample.addSampleCategory(category);
            sample.setIsActive(true);

            final List<InputSampleBean> inputSamples =
                ExperimentReader.getInputSamples(experiment, Access.ADMINISTRATOR, Collections.EMPTY_SET);
            Assert.assertNotNull(inputSamples);
            Assert.assertEquals(1, inputSamples.size());
            final InputSampleBean bean = inputSamples.iterator().next();
            Assert.assertEquals(1, bean.getSamples().size());
        } finally {
            version.abort();
        }

    }

    public final void testExcludeExperimentOutput() throws AccessException {
        final WritableVersion version = this.model.getTestVersion();
        MRUController.clearAll();
        try {
            final ExperimentType type = new ExperimentType(version, ExperimentReaderTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, ExperimentReaderTest.UNIQUE, ExperimentReaderTest.NOW,
                    ExperimentReaderTest.NOW, type);
            final InputSample inputSample = new InputSample(version, experiment);
            final Sample possible = new Sample(version, ExperimentReaderTest.UNIQUE);
            possible.setIsActive(Boolean.TRUE);
            final SampleCategory category = new SampleCategory(version, "sc" + ExperimentReaderTest.UNIQUE);
            possible.addSampleCategory(category);
            final Protocol protocol = new Protocol(version, ExperimentReaderTest.UNIQUE, type);
            final RefInputSample ris = new RefInputSample(version, category, protocol);
            inputSample.setRefInputSample(ris);
            final OutputSample os = new OutputSample(version, experiment);
            os.setSample(possible);
            MRUController.addObject(Access.ADMINISTRATOR, possible);

            final Collection<Sample> except = ExperimentReader.getOutputsFromGroup(experiment);
            final List<InputSampleBean> inputSamples =
                ExperimentReader.getInputSamples(experiment, Access.ADMINISTRATOR, except);
            Assert.assertNotNull(inputSamples);
            Assert.assertEquals(1, inputSamples.size());
            final InputSampleBean bean = inputSamples.iterator().next();
            Assert.assertEquals(0, bean.getSamples().size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testGetPossibleSamplesExcludeGroup() throws AccessException {
        final WritableVersion version = this.model.getTestVersion();
        MRUController.clearAll();
        try {
            final ExperimentType type = new ExperimentType(version, ExperimentReaderTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, ExperimentReaderTest.UNIQUE, ExperimentReaderTest.NOW,
                    ExperimentReaderTest.NOW, type);
            final InputSample inputSample = new InputSample(version, experiment);
            final Sample possible = new Sample(version, ExperimentReaderTest.UNIQUE);
            possible.setIsActive(Boolean.TRUE);
            final SampleCategory category = new SampleCategory(version, "sc" + System.currentTimeMillis());
            possible.addSampleCategory(category);
            final Protocol protocol = new Protocol(version, ExperimentReaderTest.UNIQUE, type);
            final RefInputSample ris = new RefInputSample(version, category, protocol);
            inputSample.setRefInputSample(ris);
            final Experiment experiment2 =
                new Experiment(version, "two" + System.currentTimeMillis(), ExperimentReaderTest.NOW,
                    ExperimentReaderTest.NOW, type);
            final ExperimentGroup group =
                new ExperimentGroup(version, "group" + System.currentTimeMillis(), "test");
            experiment.setExperimentGroup(group);
            experiment2.setExperimentGroup(group);
            final OutputSample os = new OutputSample(version, experiment2);
            os.setSample(possible);
            MRUController.addObject(Access.ADMINISTRATOR, possible);
            MRUController.clearAll();

            final Collection<Sample> except = ExperimentReader.getOutputsFromGroup(experiment);
            final List<InputSampleBean> inputSamples =
                ExperimentReader.getInputSamples(experiment, Access.ADMINISTRATOR, except);
            Assert.assertNotNull(inputSamples);
            Assert.assertEquals(1, inputSamples.size());
            final InputSampleBean bean = inputSamples.iterator().next();
            Assert.assertEquals(0, bean.getSamples().size());

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testGetInputSamples() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ExperimentReaderTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, ExperimentReaderTest.UNIQUE, ExperimentReaderTest.NOW,
                    ExperimentReaderTest.NOW, type);
            final InputSample inputSample = new InputSample(version, experiment);
            inputSample.setAmountUnit("L");
            final Protocol protocol = new Protocol(version, ExperimentReaderTest.UNIQUE, type);
            final SampleCategory category = new SampleCategory(version, "sc" + System.currentTimeMillis());

            // make sure a possible error can happen
            final RefSample refSample = new RefSample(version, "rs" + System.currentTimeMillis());
            refSample.addSampleCategory(category);

            final RefInputSample ris = new RefInputSample(version, category, protocol);
            inputSample.setRefInputSample(ris);
            final List<InputSampleBean> inputSamples =
                ExperimentReader.getInputSamples(experiment, Access.ADMINISTRATOR, Collections.EMPTY_SET);
            Assert.assertNotNull(inputSamples);
            Assert.assertEquals(1, inputSamples.size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testGetInputSamplesOneSet() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ExperimentReaderTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, ExperimentReaderTest.UNIQUE, ExperimentReaderTest.NOW,
                    ExperimentReaderTest.NOW, type);
            final InputSample inputSample = new InputSample(version, experiment);
            inputSample.setAmountUnit("L");
            final Protocol protocol = new Protocol(version, ExperimentReaderTest.UNIQUE, type);
            final SampleCategory category = new SampleCategory(version, "sc" + System.currentTimeMillis());
            final Sample sample = new Sample(version, ExperimentReaderTest.UNIQUE);
            inputSample.setSample(sample);

            final RefInputSample ris = new RefInputSample(version, category, protocol);
            inputSample.setRefInputSample(ris);
            final List<InputSampleBean> inputSamples =
                ExperimentReader.getInputSamples(experiment, Access.ADMINISTRATOR, Collections.EMPTY_SET);
            Assert.assertNotNull(inputSamples);
            Assert.assertEquals(1, inputSamples.size());
            final InputSampleBean bean = inputSamples.iterator().next();
            Assert.assertEquals(1, bean.getSamples().size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

/*
    public final void testGetInputs() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        WritableVersion version = this.model.getTestVersion();
        try {
            ExperimentType type = new ExperimentType(version, "test" + System.currentTimeMillis());
            Experiment experiment =
                new Experiment(version, "test" + System.currentTimeMillis(), NOW, NOW, type);
            InputSample inputSample = new InputSample(version, experiment);
            inputSample.setAmountUnit("L");
            inputSample.setAmount(0.005f);
            Protocol protocol = new Protocol(version, "test" + System.currentTimeMillis(), type);
            SampleCategory category = new SampleCategory(version, "test", "sc" + System.currentTimeMillis());
            RefInputSample ris = new RefInputSample(version, category, protocol);
            inputSample.setRefInputSample(ris);

            // make some samples
            Sample sample1 = new Sample(version, "s_one" + System.currentTimeMillis());
            sample1.addSampleCategory(category);
            sample1.setIsActive(true);

            final ExperimentReader reader = new ExperimentReader(version, experiment);
            List<InputSampleBean> inputSamples = reader.getInputSamples(null);
            assertNotNull(inputSamples);
            assertEquals(1, inputSamples.size());
            InputSampleBean bean = inputSamples.iterator().next();
            Collection<SampleBean> samples = bean.getSamples();
            assertEquals(1, samples.size());
            assertEquals("5.0mL", bean.getAmount());
        } catch (ConstraintException e) {
            fail(e.getMessage());
        } catch (AccessException e) {
            fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    } */

    public final void testGetOutputSamples() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ExperimentReaderTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, ExperimentReaderTest.UNIQUE, ExperimentReaderTest.NOW,
                    ExperimentReaderTest.NOW, type);
            final OutputSample outputSample = new OutputSample(version, experiment);
            final Sample sample = new Sample(version, "out" + System.currentTimeMillis());

            sample.setAmountUnit("L");
            sample.setCurrentAmount(0.005f);
            outputSample.setSample(sample);
            final ExperimentReader reader = new ExperimentReader(version, experiment);
            final List<OutputSampleBean> outputSamples = reader.getOutputSamples();
            Assert.assertNotNull(outputSamples);
            Assert.assertEquals(1, outputSamples.size());
            final OutputSampleBean bean = outputSamples.iterator().next();
            Assert.assertEquals("5mL", bean.getAmount().toString());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    //TODO also test for NMR experiments
}
