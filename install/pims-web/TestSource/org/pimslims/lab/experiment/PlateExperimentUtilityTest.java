/**
 * pims-web org.pimslims.presentation.plateExperiment PlateUtilsTest.java
 * 
 * @author Marc Savitsky
 * @date 26 Jan 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.lab.experiment;

import java.util.Collection;

import junit.framework.Assert;

import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.plateExperiment.PlateExperimentUtility;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * PlateUtilsTest
 * 
 */
public class PlateExperimentUtilityTest extends AbstractTestCase {

    private ExperimentGroup group0;

    private ExperimentGroup group1;

    private ExperimentGroup group2;

    private ExperimentGroup group3;

    public static final int[] ROWS =
        new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6,
            7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6,
            7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6,
            7, 8 };

    public static final int[] COLS =
        new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4,
            4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8,
            8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10, 11, 11, 11, 11, 11, 11, 11, 11, 12,
            12, 12, 12, 12, 12, 12, 12 };

    public static final String[] ROWSTRING =
        new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L" };

    public PlateExperimentUtilityTest(final String methodName) {
        super(methodName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.wv = this.getWV();
        final long now = System.currentTimeMillis();

        final ExperimentType experimentType1 =
            POJOFactory.create(this.wv, ExperimentType.class, ExperimentType.PROP_NAME, "Experiment Type 1");
        final ExperimentType experimentType2 =
            POJOFactory.create(this.wv, ExperimentType.class, ExperimentType.PROP_NAME, "Experiment Type 2");

        //final SampleCategory category = POJOFactory.createSampleCategory(this.wv);

        this.group0 = POJOFactory.create(this.wv, ExperimentGroup.class);
        this.group0.setName("Parent Group 0");
        final Holder holder0 = POJOFactory.createHolder(this.wv);
        holder0.setName("Holder 0");

        this.group1 = POJOFactory.create(this.wv, ExperimentGroup.class);
        this.group1.setName("Parent Group 1");
        final Holder holder1 = POJOFactory.createHolder(this.wv);
        holder1.setName("Holder 1");

        this.group2 = POJOFactory.create(this.wv, ExperimentGroup.class);
        this.group2.setName("Child Group 2");
        final Holder holder2 = POJOFactory.createHolder(this.wv);
        holder2.setName("Holder 2");

        this.group3 = POJOFactory.create(this.wv, ExperimentGroup.class);
        this.group3.setName("Child Group 3");
        final Holder holder3 = POJOFactory.createHolder(this.wv);
        holder3.setName("Holder 3");

        final Sample sample0 =
            POJOFactory.create(this.wv, Sample.class, AbstractSample.PROP_NAME, "Sample0 " + now);

        final Sample sample1 =
            POJOFactory.create(this.wv, Sample.class, AbstractSample.PROP_NAME, "Sample1 " + now);

        final ParameterDefinition parameterDefinition = POJOFactory.createParameterDefinition(this.wv);
        parameterDefinition.setName("__Score");

        for (int i = 0; i < 40; i++) {
            final Experiment experiment0 = POJOFactory.createExperiment(this.wv);
            experiment0.setName("Experiment0" + new Integer(i).toString());
            experiment0.setStatus("To_be_run");
            experiment0.setExperimentType(experimentType1);

            final Parameter parameter0 = POJOFactory.createParameter(this.wv, experiment0);
            parameter0.setName("Parameter 0");
            parameter0.setValue("value0");
            //System.out.println("add parameter [" + parameter0.get_Name() + "] to experiment ["
            //    + experiment0.get_Name() + "]");

            final Parameter parameter1 = POJOFactory.createParameter(this.wv, experiment0);
            parameter1.setName("Parameter 1");
            parameter1.setValue("");

            final Parameter parameter2 = POJOFactory.createParameter(this.wv, experiment0);
            parameter2.setName("Parameter 2");
            if (i % 3 == 0) {
                parameter2.setValue("valueA");
            } else {
                parameter2.setValue("valueB");
            }

            if (i < 20) {
                experiment0.setExperimentGroup(this.group0);
            } else {
                experiment0.setExperimentGroup(this.group1);
            }

            final Experiment experiment1 = POJOFactory.createExperiment(this.wv);
            experiment1.setName("Experiment1" + new Integer(i).toString());
            experiment1.setStatus("To_be_run");
            experiment1.setExperimentType(experimentType2);
            experiment1.setExperimentGroup(this.group2);

            final Parameter parameterX = POJOFactory.createParameter(this.wv, experiment1);
            parameterX.setName("Parameter 3");
            parameterX.setValue("");
            parameterX.setParameterDefinition(parameterDefinition);

            final Experiment experiment2 = POJOFactory.createExperiment(this.wv);
            experiment2.setName("Experiment2" + new Integer(i).toString());
            experiment2.setStatus("To_be_run");
            experiment2.setExperimentType(experimentType2);
            experiment2.setExperimentGroup(this.group3);

            final Parameter parameterY = POJOFactory.createParameter(this.wv, experiment2);
            parameterY.setName("Parameter 4");
            parameterY.setValue("");
            parameterY.setParameterDefinition(parameterDefinition);

            final InputSample inputSample0 = POJOFactory.createInputSample(this.wv, experiment0);
            inputSample0.setName("inputSample0");

            inputSample0.setSample(sample0);

            final InputSample inputSample1 = POJOFactory.createInputSample(this.wv, experiment0);
            inputSample1.setName("inputSample1");
            if (i % 3 == 0) {
                inputSample1.setSample(sample1);
            } else {
                inputSample1.setSample(sample0);
            }

            final InputSample inputSample2 = POJOFactory.createInputSample(this.wv, experiment0);
            inputSample2.setName("inputSample2");

            final Sample sample = POJOFactory.createSample(this.wv);
            sample.setHolder(holder0);
            sample.setColPosition(PlateExperimentUtilityTest.COLS[i]);
            sample.setRowPosition(PlateExperimentUtilityTest.ROWS[i]);
            final OutputSample outputSample = POJOFactory.createOutputSample(this.wv, experiment0);
            outputSample.setSample(sample);

            //System.out.println("set experiment [" + experiment0.get_Name() + ":" + sample.getRowPosition()
            //    + ":" + sample.getColPosition() + "]");

            if (i % 2 == 0) {
                final InputSample input = POJOFactory.createInputSample(this.wv, experiment1);
                sample.addInputSample(input);
            } else {
                final InputSample input = POJOFactory.createInputSample(this.wv, experiment2);
                sample.addInputSample(input);
            }
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetChildGroups() {

        try {
            final Collection<ExperimentGroup> childGroups =
                PlateExperimentUtility.getChildGroups(this.group1, true);

            System.out.println("all the child plates of [" + this.group1.getName() + "]");
            for (final ExperimentGroup group : childGroups) {
                System.out.println("child group name [" + group.getName() + "]");
            }

            Assert.assertEquals(2, childGroups.size());

        } finally {
            this.wv.abort();
        }
    }

    public void testGetSiblingGroups() {

        try {
            final Collection<ExperimentGroup> siblingGroups =
                PlateExperimentUtility.getSiblingGroups(this.group2);

            System.out.println("all the sibling plates of [" + this.group2.getName() + "]");
            for (final ExperimentGroup group : siblingGroups) {
                System.out.println("sibling group name [" + group.getName() + "]");
            }

            Assert.assertEquals(2, siblingGroups.size());

        } finally {
            this.wv.abort();
        }
    }

}
