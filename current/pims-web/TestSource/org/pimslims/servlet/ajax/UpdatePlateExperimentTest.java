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
package org.pimslims.servlet.ajax;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import junit.framework.Assert;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Measurement;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.AbstractHolderType;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.experiment.InputSampleBean;
import org.pimslims.presentation.experiment.ParameterBean;
import org.pimslims.presentation.plateExperiment.PlateExperimentBean;
import org.pimslims.presentation.servlet.utils.ValueFormatter;
import org.pimslims.servlet.plateExperiment.BasicPlateExperimentUpdate;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * PlateUtilsTest
 * 
 */
public class UpdatePlateExperimentTest extends AbstractTestCase {

    private ExperimentGroup group;

    private static final Calendar YESTERDAY = Calendar.getInstance();
    static {
        UpdatePlateExperimentTest.YESTERDAY.setTimeInMillis(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
    }

    public static final int[] ROWS = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4,
        5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6,
        7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8,
        1, 2, 3, 4, 5, 6, 7, 8 };

    public static final int[] COLS = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3,
        3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7,
        7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10, 11, 11, 11, 11,
        11, 11, 11, 11, 12, 12, 12, 12, 12, 12, 12, 12 };

    public static final String[] ROWSTRING = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
        "K", "L" };

    protected static final String UNIQUE = "Test" + System.currentTimeMillis();

    public UpdatePlateExperimentTest(final String methodName) {
        super(methodName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.wv = this.getWV();
        final long now = System.currentTimeMillis();

        final HolderType holderType =
            (this.wv.findFirst(HolderType.class, AbstractHolderType.PROP_NAME, "96 deep well"));

        final Protocol protocol = POJOFactory.createProtocol(this.wv);
        final List<ParameterDefinition> parameterDefinitions = new ArrayList<ParameterDefinition>();
        final ParameterDefinition parameterDefinition0 =
            POJOFactory.create(this.wv, ParameterDefinition.class, ParameterDefinition.PROP_PARAMTYPE, "Int");
        parameterDefinitions.add(parameterDefinition0);
        final ParameterDefinition parameterDefinition1 =
            POJOFactory.create(this.wv, ParameterDefinition.class, ParameterDefinition.PROP_PARAMTYPE,
                "String");
        parameterDefinitions.add(parameterDefinition1);
        final ParameterDefinition parameterDefinition2 =
            POJOFactory.create(this.wv, ParameterDefinition.class, ParameterDefinition.PROP_PARAMTYPE,
                "Boolean");
        parameterDefinitions.add(parameterDefinition2);
        protocol.setParameterDefinitions(parameterDefinitions);

        final RefOutputSample refOutputSample = POJOFactory.create(this.wv, RefOutputSample.class);
        protocol.addRefOutputSample(refOutputSample);

        final RefInputSample refInputSample0 = POJOFactory.createRefInputSample(this.wv);
        refInputSample0.setName("RefInputSample0");
        protocol.addRefInputSample(refInputSample0);
        final RefInputSample refInputSample1 = POJOFactory.createRefInputSample(this.wv);
        refInputSample1.setName("RefInputSample1");
        protocol.addRefInputSample(refInputSample1);
        final RefInputSample refInputSample2 = POJOFactory.createRefInputSample(this.wv);
        refInputSample2.setName("RefInputSample2");
        protocol.addRefInputSample(refInputSample2);

        final ExperimentType experimentType1 =
            POJOFactory.create(this.wv, ExperimentType.class, ExperimentType.PROP_NAME,
                UpdatePlateExperimentTest.UNIQUE + "Experiment Type 1");
        //final ExperimentType experimentType2 =
        POJOFactory.create(this.wv, ExperimentType.class, ExperimentType.PROP_NAME,
            UpdatePlateExperimentTest.UNIQUE + "Experiment Type 2");

        this.group = POJOFactory.create(this.wv, ExperimentGroup.class);
        this.group.setName("Parent Group 0");
        final Holder holder = POJOFactory.createHolder(this.wv);
        holder.setName("Holder 0");
        holder.setHolderType(holderType);

        final Sample sample0 =
            POJOFactory.create(this.wv, Sample.class, AbstractSample.PROP_NAME,
                UpdatePlateExperimentTest.UNIQUE + "Sample0 " + now);

        final Sample sample1 =
            POJOFactory.create(this.wv, Sample.class, AbstractSample.PROP_NAME,
                UpdatePlateExperimentTest.UNIQUE + "Sample1 " + now);

        for (int i = 0; i < 40; i++) {
            final Experiment experiment = POJOFactory.createExperiment(this.wv);
            experiment.setName(UpdatePlateExperimentTest.UNIQUE + "Experiment0" + new Integer(i).toString());
            experiment.setStatus("To_be_run");
            experiment.setExperimentType(experimentType1);
            experiment.setExperimentGroup(this.group);
            experiment.setProtocol(protocol);

            final Parameter parameter0 = POJOFactory.createParameter(this.wv, experiment);
            parameter0.setName("Parameter 0");
            parameter0.setValue("value0");
            parameter0.setParameterDefinition(parameterDefinition0);
            //System.out.println("add parameter [" + parameter0.get_Name() + "] to experiment ["
            //    + experiment0.get_Name() + "]");

            final Parameter parameter1 = POJOFactory.createParameter(this.wv, experiment);
            parameter1.setName("Parameter 1");
            parameter1.setValue("");
            parameter1.setParameterDefinition(parameterDefinition1);

            final Parameter parameter2 = POJOFactory.createParameter(this.wv, experiment);
            parameter2.setName("Parameter 2");
            parameter2.setParameterDefinition(parameterDefinition2);
            if (i % 3 == 0) {
                parameter2.setValue("true");
            } else {
                parameter2.setValue("false");
            }

            final InputSample inputSample0 = POJOFactory.createInputSample(this.wv, experiment);
            inputSample0.setName("inputSample0");
            inputSample0.setSample(sample0);
            inputSample0.setRefInputSample(refInputSample0);

            final InputSample inputSample1 = POJOFactory.createInputSample(this.wv, experiment);
            inputSample1.setName("inputSample1");
            if (i % 3 == 0) {
                inputSample1.setSample(sample1);
            } else {
                inputSample1.setSample(sample0);
            }
            inputSample1.setRefInputSample(refInputSample1);

            final InputSample inputSample2 = POJOFactory.createInputSample(this.wv, experiment);
            inputSample2.setName("inputSample2");
            inputSample2.setRefInputSample(refInputSample2);

            final Sample sample = POJOFactory.createSample(this.wv);
            sample.setHolder(holder);
            sample.setColPosition(UpdatePlateExperimentTest.COLS[i]);
            sample.setRowPosition(UpdatePlateExperimentTest.ROWS[i]);
            final OutputSample outputSample = POJOFactory.createOutputSample(this.wv, experiment);
            outputSample.setSample(sample);

        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    //org.pimslims.model.experiment.ExperimentGroup%3A88225%3ApageNumber=dsasdasda&org.pimslims.model.experiment.ExperimentGroup%3A88225%3Aname=myGroup&org.pimslims.model.experiment.ExperimentGroup%3A88225%3AstartDate=04%2F02%2F2011&org.pimslims.model.experiment.ExperimentGroup%3A88225%3AendDate=04%2F02%2F2011&org.pimslims.model.experiment.ExperimentGroup%3A88225%3AisActive=true

    public void testUpdateGroupName() throws AccessException, ConstraintException, ServletException,
        ParseException {

        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final String newName = this.group.getName() + "n";
            parms.put(this.group.get_Hook() + ":" + ExperimentGroup.PROP_NAME, new String[] { newName });
            BasicPlateExperimentUpdate.processRequest(this.wv, parms);
            Assert.assertEquals(newName, this.group.getName());
        } finally {
            this.wv.abort();
        }
    }

    public void testStartDate() throws AccessException, ConstraintException, ServletException, ParseException {

        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final String date = ValueFormatter.formatDate(UpdatePlateExperimentTest.YESTERDAY);
            parms.put(this.group.get_Hook() + ":" + BasicPlateExperimentUpdate.START_DATE,
                new String[] { date });
            BasicPlateExperimentUpdate.processRequest(this.wv, parms);
            final Experiment first = this.group.getExperiments().iterator().next();
            Assert.assertEquals(date, ValueFormatter.formatDate(first.getStartDate()));
        } finally {
            this.wv.abort();
        }
    }

    public void testProcessRequestParameter() throws AccessException, ConstraintException, ServletException,
        ParseException {

        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();

            ParameterBean parameterBean = null;
            Object value = "";

            int i = 0;
            for (final Experiment experiment : this.group.getExperiments()) {
                if (null == parameterBean) {
                    parameterBean = new ParameterBean(experiment.getParameters().iterator().next());
                    if ("Boolean".equals(parameterBean.getValues().get(Parameter.PROP_PARAMTYPE))) {
                        value = new Boolean(true);
                    }
                    if ("String".equals(parameterBean.getValues().get(Parameter.PROP_PARAMTYPE))) {
                        value = new String("");
                    }
                    if ("Integer".equals(parameterBean.getValues().get(Parameter.PROP_PARAMTYPE))) {
                        value = new Integer(42);
                    }
                }

                if (i % 2 == 0) {
                    parms.put(experiment.get_Hook(), new String[] { value.toString() });

                }
                i++;
            }

            final ModelObjectShortBean object =
                (ModelObjectShortBean) parameterBean.getValues().get(Parameter.PROP_PARAMETERDEFINITION);
            parms.put("parameter", new String[] { object.getHook() });

            AjaxUpdatePlateExperiment.processRequest(this.wv, parms);

        } finally {
            this.wv.abort();
        }
    }

    public void testUpdateInputSample() throws ConstraintException {

        try {
            //final Map<String, String[]> parms = new HashMap<String, String[]>();            
            final Sample sample = POJOFactory.createSample(this.wv);
            final ResearchObjective ro =
                new ResearchObjective(this.wv, UpdatePlateExperimentTest.UNIQUE,
                    UpdatePlateExperimentTest.UNIQUE);
            final Experiment previous = POJOFactory.createExperiment(this.wv);
            new OutputSample(this.wv, previous).setSample(sample);
            previous.setProject(ro);

            final Experiment experiment = this.group.getExperiments().iterator().next();
            new PlateExperimentBean(experiment);
            final InputSample inputSample = experiment.getInputSamples().iterator().next();
            final RefInputSample ris = inputSample.getRefInputSample();

            AjaxUpdatePlateExperiment.updateInputSample(experiment, inputSample.getRefInputSample(),
                sample.get_Hook());
            final PlateExperimentBean beanB = new PlateExperimentBean(experiment);

            final InputSample isA =
                experiment.findFirst(Experiment.PROP_INPUTSAMPLES, InputSample.PROP_REFINPUTSAMPLE, ris);

            final InputSample isB = inputSample;

            Assert.assertEquals(isA.getSample(), isB.getSample());

            InputSampleBean isbB = null;
            for (final InputSampleBean bean : beanB.getInputSampleBeans()) {
                if (bean.getRefInputSampleDbId() == inputSample.getRefInputSample().getDbId()) {
                    isbB = bean;
                }
            }

            Assert.assertEquals(sample.get_Hook(), isbB.getSampleHook());
            //TODO check that the project is set for the experiment
            Assert.assertEquals(ro, experiment.getProject());

        } finally {
            this.wv.abort();
        }
    }

    public void testProcessRequestAmount() throws AccessException, ConstraintException, ServletException,
        ParseException {

        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            POJOFactory.createSample(this.wv);
            Experiment exp = null;

            int i = 0;
            for (final Experiment experiment : this.group.getExperiments()) {
                if (i % 2 == 0) {
                    parms.put(experiment.get_Hook() + ":amount", new String[] { "5uL" });

                    if (null == exp) {
                        exp = experiment;
                    }
                }
                i++;
            }

            final InputSample inputSample = exp.getInputSamples().iterator().next();
            parms.put("refInputSample", new String[] { inputSample.getRefInputSample().get_Hook() });

            AjaxUpdatePlateExperiment.processRequest(this.wv, parms);

        } finally {
            this.wv.abort();
        }
    }

    // TODO activate this test - but see PIMS-3334
    public void TODOtestProcessBadAmount() throws AccessException, ConstraintException, ServletException,
        ParseException {

        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            //final Sample sample = POJOFactory.createSample(this.wv);
            Experiment exp = null;

            int i = 0;
            for (final Experiment experiment : this.group.getExperiments()) {
                if (i % 2 == 0) {
                    parms.put(experiment.get_Hook() + ":amount", new String[] { "xxx" });

                    if (null == exp) {
                        exp = experiment;
                    }
                }
                i++;
            }

            final InputSample inputSample = exp.getInputSamples().iterator().next();
            parms.put("refInputSample", new String[] { inputSample.getRefInputSample().get_Hook() });

            AjaxUpdatePlateExperiment.processRequest(this.wv, parms);
            Assert.fail("No error message for bad amount");
        } finally {
            this.wv.abort();
        }
    }

    public void testProcessRequestSampleAndAmount() throws AccessException, ConstraintException,
        ServletException, ParseException {

        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final Sample sample = POJOFactory.createSample(this.wv);
            Experiment exp = null;

            int i = 0;
            for (final Experiment experiment : this.group.getExperiments()) {
                if (i % 2 == 0) {
                    parms.put(experiment.get_Hook() + ":sample", new String[] { sample.get_Hook() });
                    parms.put(experiment.get_Hook() + ":amount", new String[] { "5uL" });
                    //System.out.println("put experiment [" + experiment.get_Hook() + ":" + sample.get_Hook()
                    //    + "]");
                    if (null == exp) {
                        exp = experiment;
                    }
                }
                i++;
            }

            final InputSample inputSample = exp.getInputSamples().iterator().next();
            parms.put("refInputSample", new String[] { inputSample.getRefInputSample().get_Hook() });

            final Collection<PlateExperimentBean> beans =
                AjaxUpdatePlateExperiment.processRequest(this.wv, parms);

            for (final PlateExperimentBean bean : beans) {
                //System.out.println("PlateExperimentBean [" + bean.getExperimentName() + ":"
                //    + inputSample.getRefInputSample().get_Name() + ":" + sample.get_Hook() + ":5uL]");
                for (final InputSampleBean inputSampleBean : bean.getInputSampleBeans()) {

                    if (inputSampleBean.getRefInputSampleDbId() == inputSample.getRefInputSample().getDbId()) {

                        //System.out.println("PlateExperimentBean [" + inputSampleBean.getSampleHook() + ":"
                        //    + inputSampleBean.getAmount() + "]");

                        Assert.assertEquals(
                            "Assertion error expected [" + sample.get_Hook() + "] found ["
                                + inputSampleBean.getSampleHook() + "] for experiment ["
                                + bean.getExperimentName() + "]", sample.get_Hook(),
                            inputSampleBean.getSampleHook());
                        Assert.assertEquals(Measurement.getMeasurement("5uL").toString(), inputSampleBean
                            .getAmount().toString());

                    }
                }
            }

        } finally {
            this.wv.abort();
        }
    }
}
