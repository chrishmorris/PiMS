/**
 * pims-web org.pimslims.presentation.experiment OPPFExperimentNameTest.java
 * 
 * @author Marc Savitsky
 * @date 28 Aug 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation.experiment;

import junit.framework.Assert;

import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.properties.PropertyGetter;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * OPPFExperimentNameTest
 * 
 */
public class OPPFExperimentNameTest extends AbstractTestCase {

    private final String expBlueprintName = "blueprint";

    private final String experimentTypeName = "type";

    private ResearchObjective expBlueprint;

    private ExperimentType experimentType;

    /**
     * Constructor for OPPFExperimentNameTest
     * 
     * @param name
     */
    public OPPFExperimentNameTest(final String methodName) {
        super(methodName);
    }

    /**
     * OPPFExperimentNameTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.wv = this.getWV();

        final Experiment experiment = POJOFactory.createExperiment(this.wv);
        this.expBlueprint = POJOFactory.createExpBlueprint(this.wv);
        this.expBlueprint.setLocalName(this.expBlueprintName);
        experiment.setProject(this.expBlueprint);
        this.experimentType = POJOFactory.createExperimentType(this.wv);
        this.experimentType.setName(this.experimentTypeName);
        experiment.setExperimentType(this.experimentType);

        final ExperimentNameFactory enf =
            PropertyGetter.getInstance("Experiment.Name.Factory", OPPFExperimentName.class);

        experiment.setName(enf.suggestExperimentName(this.wv, experiment, null));

        System.out.println("Base experiment name [" + experiment.getName() + "]");

    }

    /**
     * OPPFExperimentNameTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public final void testsuggestExperimentName() throws ConstraintException {

        try {
            final Experiment experiment = POJOFactory.createExperiment(this.wv);
            experiment.setProject(this.expBlueprint);
            experiment.setExperimentType(this.experimentType);

            final ExperimentNameFactory enf =
                PropertyGetter.getInstance("Experiment.Name.Factory", OPPFExperimentName.class);

            final long t = System.currentTimeMillis();
            experiment.setName(enf.suggestExperimentName(this.wv, experiment, null));
            System.out.println("suggestExperimentName [" + (System.currentTimeMillis() - t) + "]");

            Assert.assertEquals("blueprint type administrator 2", experiment.getName());

        } finally {
            this.wv.abort();
        }
    }
}
