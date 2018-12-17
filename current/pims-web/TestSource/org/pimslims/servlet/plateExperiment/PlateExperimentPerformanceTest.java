/**
 * current-pims-web org.pimslims.servlet.plateExperiment CreateExperimentGroupTest.java
 * 
 * @author cm65
 * @date 20 Mar 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.servlet.plateExperiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabNotebook;
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
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.presentation.plateExperiment.PlateExperimentUtility;
import org.pimslims.servlet.PIMSServlet;

/**
 * CreateExperimentGroupTest
 * 
 */
public class PlateExperimentPerformanceTest extends TestCase {

    private static final String UNIQUE = "xpepx" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    private String protocolHook;

    /**
     * objects to delete
     */
    private final List<String> hooks = new ArrayList<String>();

    private String labNotebookHook;

    private int count = 0;

    /**
     * @param name
     */
    public PlateExperimentPerformanceTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {

            final Protocol protocol = version.findFirst(Protocol.class, Protocol.PROP_NAME, "PiMS PCR");
            final ExperimentType type = protocol.getExperimentType();
            this.protocolHook = protocol.get_Hook();
            final LabNotebook owner =
                new LabNotebook(version, PlateExperimentPerformanceTest.UNIQUE + "book" + (this.count++));
            this.labNotebookHook = owner.get_Hook();
            final SampleCategory category =
                protocol.getRefOutputSamples().iterator().next().getSampleCategory();

            /*
            RefInputSample ris = new RefInputSample(version, category, protocol);

            Sample sample = new Sample(version, UNIQUE);
            this.sampleHook = sample.get_Hook();
            this.hooks.add(0, this.sampleHook);

            Timestamp now = new Timestamp(0L);
            Experiment previous = new Experiment(version, "previous" + UNIQUE, now, now, type);
            this.hooks.add(0, previous.get_Hook());
            OutputSample os = new OutputSample(version, previous); // will be deleted with previous
            os.setSample(sample);
            previous.setProject(eb);
            Experiment experiment = new Experiment(version, UNIQUE, now, now, type);
            new OutputSample(version, experiment);
            this.experimentHook = experiment.get_Hook();
            this.experimentDbId = experiment.getDbId();
            this.hooks.add(0, this.experimentHook);

            // make a parameter
            Parameter parameter = new Parameter(version, experiment);
            ParameterDefinition def = new ParameterDefinition(version, "String", protocol);
            parameter.setParameterDefinition(def);
            this.parameterDefinitionDbId = def.getDbId();
            this.parameterHook = parameter.get_Hook();
            assertNotNull(experiment.findFirst(Experiment.PROP_PARAMETERS,
                Parameter.PROP_PARAMETERDEFINITION, def));

            // make an input sample
            InputSample is = new InputSample(version, experiment);
            is.setRefInputSample(ris);
            this.refInputSampleDbId = ris.getDbId();
            this.inputSampleHook = is.get_Hook();
            this.hooks.add(0, this.inputSampleHook);
            */
            // delete these last
            this.hooks.add(protocol.get_Hook());
            this.hooks.add(category.get_Hook());
            this.hooks.add(type.get_Hook());
            this.hooks.add(this.labNotebookHook);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.plateExperiment.CreateExperimentGroup#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * .
     * 
     * @throws ServletException
     * @throws IOException
     */
    public final void testDoPost() throws ServletException, IOException, AccessException,
        ConstraintException, AbortedException {
        final CreateExperimentGroup servlet = new CreateExperimentGroup();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap();
        parms.put("protocol", new String[] { this.protocolHook });
        parms.put("groupName", new String[] { "group" + PlateExperimentPerformanceTest.UNIQUE });
        parms.put("numExperiments", new String[] { "96" });
        parms.put(PIMSServlet.LAB_NOTEBOOK_ID, new String[] { this.labNotebookHook });
        final MockHttpServletRequest request =
            new MockHttpServletRequest("post", new MockHttpSession(Access.ADMINISTRATOR), parms);
        final MockHttpServletResponse response = new MockHttpServletResponse(true);
        final long time = System.currentTimeMillis();
        servlet.doPost(request, response);
        System.out.println("" + (System.currentTimeMillis() - time) / 1000 + "s to create 96 experiments");
        Assert.assertEquals(HttpServletResponse.SC_SEE_OTHER, response.getStatus());
        final String location = response.getHeader("Location");
        Assert.assertTrue(location, location.startsWith("/pims/View/"));
        final String hook = location.substring("/pims/View/".length());

        // tidy up
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ExperimentGroup group = version.get(hook);
            this.deleteGroup(version, group);
            final LabNotebook book = version.get(this.labNotebookHook);
            book.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public final void testDoGet() throws ServletException, IOException, AccessException, ConstraintException,
        AbortedException {
        String hook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ExperimentType type =
                new ExperimentType(version, PlateExperimentPerformanceTest.UNIQUE + "etg");
            final Protocol protocol = new Protocol(version, PlateExperimentPerformanceTest.UNIQUE, type);
            final ParameterDefinition pdef =
                new ParameterDefinition(version, PlateExperimentPerformanceTest.UNIQUE, "String", protocol);
            final SampleCategory category =
                new SampleCategory(version, PlateExperimentPerformanceTest.UNIQUE);
            final RefOutputSample ros = new RefOutputSample(version, category, protocol);
            final RefInputSample ris = new RefInputSample(version, category, protocol);
            final ExperimentGroup group =
                new ExperimentGroup(version, PlateExperimentPerformanceTest.UNIQUE,
                    PlateExperimentPerformanceTest.UNIQUE);
            final HolderType holderType = new HolderType(version, group.getName());
            holderType.setMaxColumn(12);
            holderType.setMaxRow(8);
            final Holder holder = new Holder(version, group.getName(), holderType);
            for (int i = 0; i < 96; i++) {
                final Experiment experiment =
                    new Experiment(version, PlateExperimentPerformanceTest.UNIQUE + "e" + i,
                        PlateExperimentPerformanceTest.NOW, PlateExperimentPerformanceTest.NOW, type);
                experiment.setProtocol(protocol);
                experiment.setExperimentGroup(group);
                final ResearchObjective ro =
                    new ResearchObjective(version, PlateExperimentPerformanceTest.UNIQUE + "ro" + i, "test");
                experiment.setProject(ro);
                new Parameter(version, experiment).setParameterDefinition(pdef);
                final OutputSample os = new OutputSample(version, experiment);
                os.setRefOutputSample(ros);
                final Sample sample = new Sample(version, experiment.get_Name());
                sample.setHolder(holder);
                sample.setRowPosition(1 + i / 12);
                sample.setColPosition(1 + i % 12);
                os.setSample(sample);
                new InputSample(version, experiment).setRefInputSample(ris);
            }
            hook = group.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        final Map<String, String[]> parms = new HashMap();
        final EditPlate servlet = new EditPlate();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletRequest request =
            new MockHttpServletRequest("post", new MockHttpSession(Access.ADMINISTRATOR), parms);
        request.setPathInfo("/" + hook);
        final MockHttpServletResponse response = new MockHttpServletResponse(true);
        final long start = System.currentTimeMillis();
        /* org.apache.log4j.Logger.getLogger("org.hibernate.SQL").debug(
            this.getClass().getName() + " start test"); */
        servlet.doGet(request, response);
        /* If you want to log, use standard java logging org.apache.log4j.Logger.getLogger("org.hibernate.SQL")
            .debug(this.getClass().getName() + " test done"); */
        final long time = System.currentTimeMillis() - start;
        System.out.println(servlet.getClass().getName() + " using " + time + " ms to display a plate");

        // now clean up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ExperimentGroup group = version.get(hook);
            final Protocol protocol = PlateExperimentUtility.getGroupProtocol(group);
            final List<RefInputSample> riss = protocol.getRefInputSamples();
            for (final Iterator iterator = riss.iterator(); iterator.hasNext();) {
                final RefInputSample ris = (RefInputSample) iterator.next();
                ris.delete();

            }
            final Set<RefOutputSample> ross = protocol.getRefOutputSamples();
            for (final Iterator iterator = ross.iterator(); iterator.hasNext();) {
                final RefOutputSample ros = (RefOutputSample) iterator.next();
                final SampleCategory category = ros.getSampleCategory();
                ros.delete();
                category.delete();
            }
            final ExperimentType type = protocol.getExperimentType();

            this.deleteGroup(version, group);
            protocol.delete();
            type.delete();
            final LabNotebook book = version.get(this.labNotebookHook);
            book.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        Assert.assertTrue("Too long to display a plate: " + time, time < 4000);

    }

    private void deleteGroup(final WritableVersion version, final ExperimentGroup group)
        throws AccessException, ConstraintException {
        final Set<Experiment> experiments = group.getExperiments();
        for (final Iterator iterator = experiments.iterator(); iterator.hasNext();) {
            final Experiment experiment = (Experiment) iterator.next();
            final Set<OutputSample> oss = experiment.getOutputSamples();
            for (final Iterator iterator2 = oss.iterator(); iterator2.hasNext();) {
                final OutputSample os = (OutputSample) iterator2.next();
                if (null != os.getSample()) {
                    os.getSample().delete();
                }
            }
        }
        version.delete(experiments);
        group.delete();
    }

}
