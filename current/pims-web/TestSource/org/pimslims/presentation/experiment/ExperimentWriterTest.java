package org.pimslims.presentation.experiment;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.ExperimentUtility;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.construct.ConstructBeanWriterTest;
import org.pimslims.test.POJOFactory;

public class ExperimentWriterTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ExperimentWriterTest.class);
    }

    private AbstractModel model;

    public ExperimentWriterTest(final String methodName) {
        super(methodName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.model = ModelImpl.getModel();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCreateOutputSamplesForExperiment() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Experiment exp = POJOFactory.createExperiment(version);

            final Protocol protocol = POJOFactory.createProtocol(version);
            final SampleCategory sc = POJOFactory.createSampleCategory(version);
            final RefOutputSample refOutputSample = new RefOutputSample(version, sc, protocol);
            refOutputSample.setName("product");
            ExperimentWriter.createOutputSamplesForExperiment(version, exp, protocol);

            version.flush();

        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

    public void testAddExpBlueprint() {
        final WritableVersion version = this.model.getTestVersion();
        final String forwardPrimer = new String("Forward Primer");
        final String reversePrimer = new String("Reverse Primer");

        try {
            final Experiment experiment = POJOFactory.createExperiment(version);
            final ResearchObjective expBlueprint = ConstructBeanWriterTest.createConstructBean(version);
            experiment.setProject(expBlueprint);

            final InputSample inputSampleFP = POJOFactory.createInputSample(version, experiment);
            final RefInputSample refInputSampleFP = POJOFactory.createRefInputSample(version);
            refInputSampleFP.setName(forwardPrimer);
            final Map<String, Object> mapFP = new HashMap<String, Object>();
            mapFP.put(SampleCategory.PROP_NAME, forwardPrimer);
            final SampleCategory sampleCategoryFP =
                version.findAll(SampleCategory.class, mapFP).iterator().next();
            refInputSampleFP.setSampleCategory(sampleCategoryFP);
            inputSampleFP.setRefInputSample(refInputSampleFP);
            experiment.addInputSample(inputSampleFP);

            final InputSample inputSampleRP = POJOFactory.createInputSample(version, experiment);
            final RefInputSample refInputSampleRP = POJOFactory.createRefInputSample(version);
            refInputSampleRP.setName(reversePrimer);
            final Map<String, Object> mapRP = new HashMap<String, Object>();
            mapRP.put(SampleCategory.PROP_NAME, reversePrimer);
            final SampleCategory sampleCategoryRP =
                version.findAll(SampleCategory.class, mapRP).iterator().next();
            refInputSampleRP.setSampleCategory(sampleCategoryRP);
            inputSampleRP.setRefInputSample(refInputSampleRP);
            experiment.addInputSample(inputSampleRP);

            ExperimentUtility.setExpBlueprintSamples(experiment, expBlueprint);

            final Experiment primerdesignExperiment =
                ExperimentUtility.getPrimerDesignExperiment(expBlueprint);

            for (final InputSample input : experiment.getInputSamples()) {
                if (input.getRefInputSample().getName().equals(forwardPrimer)) {
                    Assert.assertEquals("Forward Primer",
                        ExperimentUtility.getForwardPrimerSample(primerdesignExperiment), input.getSample());
                }
                if (input.getRefInputSample().getName().equals(reversePrimer)) {
                    Assert.assertEquals("Reverse Primer",
                        ExperimentUtility.getReversePrimerSample(primerdesignExperiment), input.getSample());
                }
            }

            version.flush();

        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }
}
