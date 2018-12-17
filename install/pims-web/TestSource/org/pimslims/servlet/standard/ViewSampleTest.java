/**
 * V3_3-web org.pimslims.servlet.standard ViewSampleTest.java
 * 
 * @author cm65
 * @date 24 Feb 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.standard;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.pimslims.lab.NMR;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.Workflow;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.experiment.InputSampleBean;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.presentation.mru.MRUController;
import org.pimslims.presentation.protocol.RefInputSampleBean;
import org.pimslims.test.AbstractTestCase;

/**
 * ViewSampleTest
 * 
 */
public class ViewSampleTest extends TestCase {

    private static final String UNIQUE = "vs" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    /**
     * @param name
     */
    public ViewSampleTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testGetInputSampleBeans() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ViewSampleTest.UNIQUE);
            final Protocol protocol = new Protocol(version, ViewSampleTest.UNIQUE, type);
            final SampleCategory category = new SampleCategory(version, ViewSampleTest.UNIQUE);
            final RefInputSample ris = new RefInputSample(version, category, protocol);
            final Experiment experiment =
                new Experiment(version, ViewSampleTest.UNIQUE + "exp", ViewSampleTest.NOW,
                    ViewSampleTest.NOW, type);
            new InputSample(version, experiment).setRefInputSample(null);
            new InputSample(version, experiment).setRefInputSample(ris);
            MRUController.addObject(Access.ADMINISTRATOR, experiment);
            final Sample sample = new Sample(version, ViewSampleTest.UNIQUE + "one");
            sample.addSampleCategory(category);
            MRUController.addObject(Access.ADMINISTRATOR, sample);
            version.flush();

            final List<InputSampleBean> inputs = ViewSample.getCouldUse(sample, Access.ADMINISTRATOR);
            Assert.assertEquals(1, inputs.size());
            Assert.assertEquals(experiment.getName(), inputs.iterator().next().getExperimentName());
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
            MRUController.clearAll();
        }
    }

    public void testGetMissingInputsOnly() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ViewSampleTest.UNIQUE);
            final Protocol protocol = new Protocol(version, ViewSampleTest.UNIQUE, type);
            final SampleCategory category = new SampleCategory(version, ViewSampleTest.UNIQUE);
            final RefInputSample ris = new RefInputSample(version, category, protocol);
            final Experiment experiment =
                new Experiment(version, ViewSampleTest.UNIQUE + "exp", ViewSampleTest.NOW,
                    ViewSampleTest.NOW, type);
            final InputSample is = new InputSample(version, experiment);
            is.setRefInputSample(ris);
            MRUController.addObject(Access.ADMINISTRATOR, experiment);
            final Sample sample = new Sample(version, ViewSampleTest.UNIQUE + "one");
            sample.addSampleCategory(category);
            is.setSample(sample);
            MRUController.addObject(Access.ADMINISTRATOR, sample);
            version.flush();

            final List<InputSampleBean> inputs = ViewSample.getCouldUse(sample, Access.ADMINISTRATOR);
            Assert.assertEquals(0, inputs.size());

            final List<InputSampleBean> usedIn = ViewSample.getUsedIn(sample, Access.ADMINISTRATOR);
            Assert.assertEquals(1, usedIn.size());
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
            MRUController.clearAll();
        }
    }

    public final void testDoGetNone() throws ServletException, IOException, AbortedException,
        ConstraintException, AccessException {
        final ViewSample servlet = new ViewSample();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }

    public final void testDoGet() throws ServletException, IOException, AbortedException,
        ConstraintException, AccessException {
        String sample1Hook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Sample sample = new Sample(version, ViewSampleTest.UNIQUE + "one");
            sample1Hook = sample.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        //this.model.getMetaClass(Sample.class.getName());
        final ViewSample servlet = new ViewSample();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setPathInfo("/" + sample1Hook);

        final long start = System.currentTimeMillis();
        servlet.doGet(request, response);
        final long time = System.currentTimeMillis() - start;
        System.out.println(servlet.getClass().getName() + " using " + time + " ms to view a sample");
        Assert.assertTrue("Too long to view a sample: " + time, time < 6000);

        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        // clean up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Sample sample1 = (Sample) version.get(sample1Hook);
            sample1.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public final void testGetNextExperiments() throws ConstraintException {
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final SampleCategory category = new SampleCategory(version, ViewSampleTest.UNIQUE);
            final ExperimentType type = new ExperimentType(version, ViewSampleTest.UNIQUE);
            final Protocol protocol = new Protocol(version, ViewSampleTest.UNIQUE, type);
            final RefInputSample in = new RefInputSample(version, category, protocol);

            final Protocol protocol2 = new Protocol(version, ViewSampleTest.UNIQUE + "two", type);
            protocol2.setIsForUse(false);
            new RefInputSample(version, category, protocol2);

            final Sample sample = new Sample(version, ViewSampleTest.UNIQUE);
            sample.addSampleCategory(category);
            final Collection<RefInputSampleBean> beans = ViewSample.getRefInputBeans(sample);
            Assert.assertEquals(1, beans.size());
            Assert.assertEquals(in.get_Hook(), beans.iterator().next().getHook());
        } finally {
            version.abort();
        }
    }

    public final void testGetNextExperimentInWorkflow() throws ConstraintException {
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final SampleCategory category = new SampleCategory(version, ViewSampleTest.UNIQUE);
            final ExperimentType type = new ExperimentType(version, ViewSampleTest.UNIQUE);
            final Protocol protocol = new Protocol(version, ViewSampleTest.UNIQUE, type);
            final RefInputSample in = new RefInputSample(version, category, protocol);
            final Workflow workflow = new Workflow(version, ViewSampleTest.UNIQUE);
            workflow.addProtocol(protocol);
            // another suitable protocol, but not in workflow
            final Protocol protocol2 = new Protocol(version, ViewSampleTest.UNIQUE + "two", type);
            new RefInputSample(version, category, protocol2);

            final Sample sample = new Sample(version, ViewSampleTest.UNIQUE);
            sample.addSampleCategory(category);
            final Project ro = new ResearchObjective(version, ViewSampleTest.UNIQUE, "test");
            final Experiment source =
                new Experiment(version, ViewSampleTest.UNIQUE, ViewSampleTest.NOW, ViewSampleTest.NOW, type);
            source.setProject(ro);
            new OutputSample(version, source).setSample(sample);
            workflow.addProject(ro);
            final Collection<RefInputSampleBean> beans = ViewSample.getRefInputBeans(sample);
            Assert.assertEquals(1, beans.size());
            Assert.assertEquals(in.get_Hook(), beans.iterator().next().getHook());
        } finally {
            version.abort();
        }
    }

    public void testNotSuitable() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final SampleCategory category = new SampleCategory(version, ViewSampleTest.UNIQUE);
            final ExperimentType type =
                version.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, NMR.EXPERIMENT_TYPE);
            final Protocol protocol = new Protocol(version, ViewSampleTest.UNIQUE, type);
            new ParameterDefinition(version, NMR.PARAMETER_DEFINITION, "String", protocol)
                .setDefaultValue("14N");
            new RefInputSample(version, category, protocol);

            final Sample sample = new Sample(version, ViewSampleTest.UNIQUE + " 2H13C");
            sample.addSampleCategory(category);

            final Collection<RefInputSampleBean> beans = ViewSample.getRefInputBeans(sample);
            Assert.assertEquals(0, beans.size());

        } finally {
            version.abort();
        }

    }

    public void testSuitable() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final SampleCategory category = new SampleCategory(version, ViewSampleTest.UNIQUE);
            final ExperimentType type =
                version.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, NMR.EXPERIMENT_TYPE);
            final Protocol protocol = new Protocol(version, ViewSampleTest.UNIQUE, type);
            new ParameterDefinition(version, NMR.PARAMETER_DEFINITION, "String", protocol)
                .setDefaultValue("2H13C");
            final RefInputSample in = new RefInputSample(version, category, protocol);

            final Sample sample = new Sample(version, ViewSampleTest.UNIQUE + " 2H13C");
            sample.addSampleCategory(category);

            final Collection<RefInputSampleBean> beans = ViewSample.getRefInputBeans(sample);
            Assert.assertEquals(1, beans.size());
            Assert.assertEquals(in.get_Hook(), beans.iterator().next().getHook());
        } finally {
            version.abort();
        }

    }

    /**
     * 
     * /** Test method for
     * {@link org.pimslims.servlet.standard.ViewSample#getAllSorted(org.pimslims.dao.ReadableVersion, java.lang.Class)}
     * .
     * 
     * public void testGetAllSorted() { Assert.fail("Not yet implemented"); }
     * 
     * /** Test method for
     * {@link org.pimslims.servlet.standard.ViewSample#getHolders(org.pimslims.dao.ReadableVersion)}.
     * 
     * public void testGetHolders() { Assert.fail("Not yet implemented"); }
     */

}
