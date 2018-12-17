/**
 * pims-web org.pimslims.servlet.plateExperiment PlateExperimentDAOTest.java
 * 
 * @author bl67
 * @date 16 Oct 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.experiment;

import java.util.Collection;

import javax.servlet.ServletException;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * PlateExperimentDAOTest
 * 
 */
public class CreateExperimentOldTest extends AbstractTestCase {

    private ExperimentType experimentType;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.wv = this.getWV();

        this.experimentType = this.wv.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, "PCR");
        final Holder holder = POJOFactory.createHolder(this.wv);
        final ExperimentGroup group = POJOFactory.create(this.wv, ExperimentGroup.class);

        for (int i = 0; i < 20; i++) {
            final Experiment experiment = POJOFactory.createExperiment(this.wv);
            experiment.setName("Experiment" + new Integer(i).toString());
            experiment.setStatus("To_be_run");
            experiment.setExperimentType(this.experimentType);

            final Sample sample0 = POJOFactory.createSample(this.wv);
            final OutputSample outputSample0 = POJOFactory.createOutputSample(this.wv, experiment);
            outputSample0.setSample(sample0);

            final Sample sample1 = POJOFactory.createSample(this.wv);
            final OutputSample outputSample1 = POJOFactory.createOutputSample(this.wv, experiment);
            outputSample1.setSample(sample1);

            if (i % 2 == 0) {
                if (i < 10) {
                    experiment.setExperimentGroup(group);
                } else {
                    sample0.setHolder(holder);
                    sample1.setHolder(holder);
                }
            }
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testActiveExperimentTypes() throws AccessException, ConstraintException, ServletException {

        try {
            final long start = System.currentTimeMillis();
            final Collection<ModelObject> objects = CreateExperiment.activeExperimentTypes(this.wv);
            System.out.println("activeExperimentTypes [" + objects.size() + "] "
                + (System.currentTimeMillis() - start) + "ms");

            ModelObjectShortBean.getModelObjectShortBeans(objects);
            System.out.println("modelObjectShortBeans " + (System.currentTimeMillis() - start) + "ms");
        } finally {
            this.wv.abort();
        }
    }
}
