/**
 * V5_0-web org.pimslims.servlet.report T2CReportTest.java
 * 
 * @author cm65
 * @date 14 May 2013
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.report;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
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
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.presentation.sample.SampleBean;

/**
 * T2CReportTest
 * 
 */
public class T2CReportTest extends TestCase {

    private static final String UNIQUE = "t2c" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    /**
     * Constructor for T2CReportTest
     * 
     * @param name
     */
    public T2CReportTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.report.T2CReport#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * .
     */
    public void testHtml() throws ServletException, IOException, AccessException, ConstraintException,
        AbortedException {

        String sampleHook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Sample sample = new Sample(version, T2CReportTest.UNIQUE + "one");
            final ExperimentType type = new ExperimentType(version, T2CReportTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, T2CReportTest.UNIQUE, T2CReportTest.NOW, T2CReportTest.NOW, type);
            new OutputSample(version, experiment).setSample(sample);
            new InputSample(version, experiment)
                .setSample(new Sample(version, T2CReportTest.UNIQUE + "prev"));
            sampleHook = sample.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final T2CReport servlet = new T2CReport();
        servlet.init(new MockServletConfig(this.model));
        final HttpServletResponse response = new MockHttpServletResponse();
        final Map<String, String[]> parms = new HashMap();
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        request.setPathInfo("/" + sampleHook);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, ((MockHttpServletResponse) response).getStatus());
        final SampleBean sampleBean = (SampleBean) request.getAttribute("sample");
        Assert.assertEquals(sampleHook, sampleBean.getHook());
        final Collection experiments = (Collection) request.getAttribute("experiments");
        Assert.assertEquals(1, experiments.size());

        // clean up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Sample sample = (Sample) version.get(sampleHook);
            final Experiment experiment = sample.getOutputSample().getExperiment();
            final List<InputSample> iss = experiment.getInputSamples();
            for (final Iterator iterator = iss.iterator(); iterator.hasNext();) {
                final InputSample inputSample = (InputSample) iterator.next();
                inputSample.getSample().delete();
            }
            experiment.delete();
            experiment.getExperimentType().delete();
            sample.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public void testBranchIgnored() throws ServletException, IOException, AccessException,
        ConstraintException, AbortedException {

        String sampleHook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Sample sample = new Sample(version, T2CReportTest.UNIQUE + "one");
            final Sample input = new Sample(version, T2CReportTest.UNIQUE + "prev");
            final ExperimentType type = new ExperimentType(version, T2CReportTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, T2CReportTest.UNIQUE, T2CReportTest.NOW, T2CReportTest.NOW, type);
            new InputSample(version, experiment).setSample(input);
            new OutputSample(version, experiment).setSample(sample);

            final Experiment failed =
                new Experiment(version, T2CReportTest.UNIQUE + "f", T2CReportTest.NOW, T2CReportTest.NOW,
                    type);
            new InputSample(version, failed).setSample(input);
            failed.setStatus("Failed");

            sampleHook = sample.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final T2CReport servlet = new T2CReport();
        servlet.init(new MockServletConfig(this.model));
        final HttpServletResponse response = new MockHttpServletResponse();
        final Map<String, String[]> parms = new HashMap();
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        request.setPathInfo("/" + sampleHook);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, ((MockHttpServletResponse) response).getStatus());
        final SampleBean sampleBean = (SampleBean) request.getAttribute("sample");
        Assert.assertEquals(sampleHook, sampleBean.getHook());
        final Collection experiments = (Collection) request.getAttribute("experiments");
        Assert.assertEquals(1, experiments.size()); // other branch should be ignored

        // clean up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Sample sample = (Sample) version.get(sampleHook);
            final Experiment experiment = sample.getOutputSample().getExperiment();
            final List<InputSample> iss = experiment.getInputSamples();
            for (final Iterator iterator = iss.iterator(); iterator.hasNext();) {
                final InputSample inputSample = (InputSample) iterator.next();
                final Sample input = inputSample.getSample();
                final Set<InputSample> iss2 = input.getInputSamples();
                for (final Iterator iterator2 = iss2.iterator(); iterator2.hasNext();) {
                    final InputSample inputSample2 = (InputSample) iterator2.next();
                    if (experiment != inputSample2.getExperiment()) {
                        inputSample2.getExperiment().delete();
                    }
                }
                input.delete();
            }
            experiment.delete();
            experiment.getExperimentType().delete();
            sample.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public void testPdf() throws ServletException, IOException, AccessException, ConstraintException,
        AbortedException {

        String sampleHook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Sample sample = new Sample(version, T2CReportTest.UNIQUE + "one");
            sampleHook = sample.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final T2CReport servlet = new T2CReport();
        servlet.init(new MockServletConfig(this.model));
        final HttpServletResponse response = new MockHttpServletResponse();
        final Map<String, String[]> parms = new HashMap();
        parms.put("presentationtype", new String[] { "pdf" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        request.setPathInfo("/" + sampleHook);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, ((MockHttpServletResponse) response).getStatus());

        // clean up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Sample sample = (Sample) version.get(sampleHook);
            sample.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
