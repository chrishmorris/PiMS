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
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.experiment.ExperimentBean;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.presentation.sample.SampleBean;
import org.pimslims.report.Filtered;

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

        final String categoryName = T2CReportTest.UNIQUE + "cat";
        final String sampleHook = this.setup(categoryName, "");

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
        //TODO assertNotNull(request.getAttribute("selectedProducts"));
        final ExperimentBean bean = (ExperimentBean) experiments.iterator().next();
        Assert.assertEquals(T2CReportTest.UNIQUE + "methodDescription", bean.getMethod());
        final String[] selected = (String[]) request.getAttribute(T2CReport.KEYWORD);
        final Collection<String> allKeywords = (Collection<String>) request.getAttribute("allKeywords");
        Assert.assertTrue(allKeywords.contains(T2CReportTest.UNIQUE));
        Assert.assertTrue(allKeywords.contains(T2CReport.METHOD));
        // was Assert.assertEquals(T2CReport.METHOD, selected[0]);
        // clean up
        this.cleanup(sampleHook);
    }

    public void testSearch() throws ServletException, IOException, AccessException, ConstraintException,
        AbortedException {

        final T2CReport servlet = new T2CReport();
        servlet.init(new MockServletConfig(this.model));
        final HttpServletResponse response = new MockHttpServletResponse();
        final Map<String, String[]> parms = new HashMap();
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        // no request.setPathInfo("/" + sampleHook);
        servlet.doGet(request, response);
        Assert.assertNull(request.getAttribute("sample"));
    }

/* no, filter does not apply to search
    public void testFilterOnly() throws ServletException, IOException, AccessException, ConstraintException,
        AbortedException {

        final T2CReport servlet = new T2CReport();
        servlet.init(new MockServletConfig(this.model));
        final HttpServletResponse response = new MockHttpServletResponse();
        final Map<String, String[]> parms = new HashMap();
        parms.put(T2CReport.KEYWORD, new String[] { "keyword" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, ((MockHttpServletResponse) response).getStatus());
        final Map<String, Filtered> keywords = (Map<String, Filtered>) request.getAttribute("keywords");
        assertNotNull(keywords);
        final Set<String> allKeywords = (Set<String>) request.getAttribute("allKeywords");
        final String[] selected = (String[]) request.getAttribute(T2CReport.KEYWORD);
        Assert.assertEquals(Filtered.IN, keywords.get("keyword"));
        Assert.assertTrue(allKeywords.contains("keyword"));
        Assert.assertEquals(1, selected.length);
    } */

    public void testFilterOut() throws ServletException, IOException, AccessException, ConstraintException,
        AbortedException {

        final String categoryName = T2CReportTest.UNIQUE + "cat";
        final String sampleHook = this.setup(categoryName, "out");

        final T2CReport servlet = new T2CReport();
        servlet.init(new MockServletConfig(this.model));
        final HttpServletResponse response = new MockHttpServletResponse();
        final Map<String, String[]> parms = new HashMap();
        parms.put(T2CReport.PRODUCT, new String[] { "nonesuch" });
        parms.put(T2CReport.KEYWORD, new String[] { "nonesuch" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        request.setPathInfo("/" + sampleHook);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, ((MockHttpServletResponse) response).getStatus());
        final SampleBean sampleBean = (SampleBean) request.getAttribute("sample");
        Assert.assertEquals(sampleHook, sampleBean.getHook());
        final Map<String, Filtered> filter = (Map<String, Filtered>) request.getAttribute("filterMap");
        Assert.assertEquals(1, filter.size());
        Assert.assertEquals(Filtered.OUT, filter.values().iterator().next());
        Assert.assertNotNull(request.getAttribute("keywords"));

        final Map<String, Filtered> keywords = (Map<String, Filtered>) request.getAttribute("keywords");
        Assert.assertEquals(Filtered.OUT, keywords.get(T2CReport.METHOD));

        // clean up
        this.cleanup(sampleHook);
    }

    public void testFilterIn() throws ServletException, IOException, AccessException, ConstraintException,
        AbortedException {

        final String categoryName = T2CReportTest.UNIQUE + "catIn";
        final String sampleHook = this.setup(categoryName, "in");

        final T2CReport servlet = new T2CReport();
        servlet.init(new MockServletConfig(this.model));
        final HttpServletResponse response = new MockHttpServletResponse();
        final Map<String, String[]> parms = new HashMap();
        parms.put(T2CReport.PRODUCT, new String[] { "nonesuch", categoryName });
        parms.put(T2CReport.KEYWORD, new String[] { T2CReport.METHOD });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        request.setPathInfo("/" + sampleHook);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, ((MockHttpServletResponse) response).getStatus());
        final SampleBean sampleBean = (SampleBean) request.getAttribute("sample");
        Assert.assertEquals(sampleHook, sampleBean.getHook());
        final Map<String, Filtered> filter = (Map<String, Filtered>) request.getAttribute("filterMap");
        Assert.assertEquals(1, filter.size());
        Assert.assertEquals(Filtered.IN, filter.values().iterator().next());

        final Map<String, Filtered> keywords = (Map<String, Filtered>) request.getAttribute("keywords");
        Assert.assertEquals(Filtered.IN, keywords.get(T2CReport.METHOD));
        // clean up
        this.cleanup(sampleHook);

        final Collection<String> allKeywords = (Collection<String>) request.getAttribute("allKeywords");
        final String[] selected = (String[]) request.getAttribute(T2CReport.KEYWORD);
        Assert.assertTrue(allKeywords.contains(T2CReportTest.UNIQUE));
        Assert.assertTrue(allKeywords.contains(T2CReport.METHOD));
        Assert.assertEquals(T2CReport.METHOD, selected[0]);
    }

    /**
     * T2CReportTest.setup
     * 
     * @param categoryName
     * @return
     * @throws ConstraintException
     * @throws AbortedException
     */
    private String setup(final String categoryName, final String unique) throws ConstraintException,
        AbortedException {
        String sampleHook = null;
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Sample sample = new Sample(version, T2CReportTest.UNIQUE + "one" + unique);
            sample.addSampleCategory(new SampleCategory(version, categoryName));
            final ExperimentType type = new ExperimentType(version, T2CReportTest.UNIQUE + unique);
            final Experiment experiment =
                new Experiment(version, T2CReportTest.UNIQUE + unique, T2CReportTest.NOW, T2CReportTest.NOW,
                    type);
            final Protocol protocol = new Protocol(version, T2CReportTest.UNIQUE + unique, type);
            protocol.setMethodDescription(T2CReportTest.UNIQUE + "methodDescription");
            experiment.setProtocol(protocol);
            new OutputSample(version, experiment).setSample(sample);
            new InputSample(version, experiment).setSample(new Sample(version, T2CReportTest.UNIQUE + "prev"
                + unique));
            final Parameter parameter = new Parameter(version, experiment);
            parameter.setName(T2CReportTest.UNIQUE);
            parameter.setValue(T2CReportTest.UNIQUE);
            sampleHook = sample.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        return sampleHook;
    }

    /**
     * T2CReportTest.cleanup
     * 
     * @param sampleHook
     * @throws AccessException
     * @throws ConstraintException
     * @throws AbortedException
     */
    private void cleanup(final String sampleHook) throws AccessException, ConstraintException,
        AbortedException {
        WritableVersion version;
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Sample sample = (Sample) version.get(sampleHook);
            version.delete(sample.getSampleCategories());
            final Experiment experiment = sample.getOutputSample().getExperiment();
            final List<InputSample> iss = experiment.getInputSamples();
            for (final Iterator iterator = iss.iterator(); iterator.hasNext();) {
                final InputSample inputSample = (InputSample) iterator.next();
                inputSample.getSample().delete();
            }
            experiment.delete();
            experiment.getProtocol().delete();
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

        final String categoryName = T2CReportTest.UNIQUE + "cat";
        final String sampleHook = this.setup(categoryName, "");

        final T2CReport servlet = new T2CReport();
        servlet.init(new MockServletConfig(this.model));
        final HttpServletResponse response = new MockHttpServletResponse();
        final Map<String, String[]> parms = new HashMap();
        parms.put(T2CReport.ACCEPT, new String[] { "pdf" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        request.setPathInfo("/" + sampleHook);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, ((MockHttpServletResponse) response).getStatus());
        // clean up
        this.cleanup(sampleHook);

    }

    //TODO test filtered PDF
}
