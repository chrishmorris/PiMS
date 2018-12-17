/**
 * pims-web org.pimslims.presentation.experiment ExperimentBeanTest.java
 * 
 * @author Marc Savitsky
 * @date 6 Oct 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation.experiment;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.Assert;

import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.properties.PropertyGetter;
import org.pimslims.util.File;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * ExperimentBeanTest
 * 
 */
public class ExperimentBeanTest extends AbstractTestCase {

    private static final String UNIQUE = "eb" + System.currentTimeMillis();

    //private AbstractModel model;

    private String experimentName;

    private final String expBlueprintName = "blueprint";

    private final String experimentTypeName = "type";

    private ResearchObjective expBlueprint;

    private ExperimentType experimentType;

    private ExperimentBean bean;

    /**
     * Constructor for ExperimentBeanTest
     * 
     * @param name
     */
    public ExperimentBeanTest(final String name) {
        super(name);
    }

    /**
     * ExperimentBeanTest.setUp
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

        this.experimentName = enf.suggestExperimentName(this.wv, experiment, null);
        experiment.setName(this.experimentName);
        final InputStream in = new ByteArrayInputStream(ExperimentBeanTest.UNIQUE.getBytes("UTF-8"));
        final File file = this.wv.createFile(in, ExperimentBeanTest.UNIQUE, experiment);
        file.setMimeType("image/png");

        this.bean = (ExperimentBean) BeanFactory.newBean(experiment);

    }

    /**
     * ExperimentBeanTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        this.wv.abort();
        this.wv.close();
        super.tearDown();
    }

    public final void testExperimentName() throws ConstraintException {
        Assert.assertEquals(this.bean.getExperimentName(), this.experimentName);
    }

    public final void testExperimentTypeName() throws ConstraintException {
        Assert.assertEquals(this.bean.getExperimentTypeName(), this.experimentType.getName());
    }

    public final void testExperimentTypeHook() throws ConstraintException {
        Assert.assertEquals(this.bean.getExperimentTypeHook(), this.experimentType.get_Hook());
    }

    public final void testExpBlueprintName() throws ConstraintException {
        Assert.assertEquals(this.bean.getBluePrint(), this.expBlueprint.getCommonName());
    }

    public void testClassDisplayName() {
        Assert.assertEquals("Experiment", this.bean.getClassDisplayName());
    }

}
