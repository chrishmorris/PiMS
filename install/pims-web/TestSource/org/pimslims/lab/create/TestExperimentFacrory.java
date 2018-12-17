/**
 * pims-web org.pimslims.lab.create TestExperimentFacrory.java
 * 
 * @author Marc Savitsky
 * @date 18 Jul 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky *
 * 
 *           .
 */
package org.pimslims.lab.create;

import junit.framework.Assert;

import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * TestExperimentFacrory
 * 
 */
public class TestExperimentFacrory extends AbstractTestCase {

    Experiment experiment;

    /**
     * @param name
     */
    public TestExperimentFacrory(final String name) {
        super(name);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.wv = this.getWV();
        this.experiment = POJOFactory.createExperiment(this.wv);
        final InputSample inputSampleA = POJOFactory.createInputSample(this.wv, this.experiment);
        final InputSample inputSampleB = POJOFactory.createInputSample(this.wv, this.experiment);

        final ResearchObjectiveElement blueprintComponentA = this.create(ResearchObjectiveElement.class);
        final Target targetA = POJOFactory.createTarget(this.wv);
        blueprintComponentA.setTarget(targetA);
        final ResearchObjective expBlueprintA = POJOFactory.createExpBlueprint(this.wv);
        expBlueprintA.addResearchObjectiveElement(blueprintComponentA);
        final Experiment expA = POJOFactory.createExperiment(this.wv);
        expA.setProject(expBlueprintA);
        final OutputSample outputSampleA = POJOFactory.createOutputSample(this.wv, expA);
        final Sample sampleA = POJOFactory.createSample(this.wv);
        sampleA.setOutputSample(outputSampleA);
        sampleA.addInputSample(inputSampleA);

        final ResearchObjectiveElement blueprintComponentB = this.create(ResearchObjectiveElement.class);
        final Target targetB = POJOFactory.createTarget(this.wv);
        blueprintComponentB.setTarget(targetB);
        final ResearchObjective expBlueprintB = POJOFactory.createExpBlueprint(this.wv);
        expBlueprintB.addResearchObjectiveElement(blueprintComponentB);
        final Experiment expB = POJOFactory.createExperiment(this.wv);
        expB.setProject(expBlueprintB);
        final OutputSample outputSampleB = POJOFactory.createOutputSample(this.wv, expB);
        final Sample sampleB = POJOFactory.createSample(this.wv);
        sampleB.setOutputSample(outputSampleB);
        sampleB.addInputSample(inputSampleB);

        final ResearchObjectiveElement blueprintComponentC = this.create(ResearchObjectiveElement.class);
        blueprintComponentC.setComponentType("complex");
        blueprintComponentC.setTarget(targetB);

        final ResearchObjectiveElement blueprintComponentD = this.create(ResearchObjectiveElement.class);
        blueprintComponentD.setComponentType("complex");
        blueprintComponentD.setTarget(targetA);

        final ResearchObjective expBlueprintC = POJOFactory.createExpBlueprint(this.wv);
        expBlueprintC.addResearchObjectiveElement(blueprintComponentC);
        expBlueprintC.addResearchObjectiveElement(blueprintComponentD);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        if (this.wv.isCompleted()) {
            // can't tidy up
            return;
        }
        if (null != this.wv) {
            this.wv.abort(); // not testing persistence
        }
    }

    public void testIsComplexExperiment() {
        try {
            Assert.assertTrue("Not a complex experiment", ExperimentFactory
                .isComplexExperiment(this.experiment));
        } finally {
            this.wv.abort();
        }
    }

    public void testGetComplex() {
        try {
            final ResearchObjective complex = ExperimentFactory.getComplex(this.experiment);
            Assert.assertNotNull(complex);
        } finally {
            this.wv.abort();
        }
    }

}
