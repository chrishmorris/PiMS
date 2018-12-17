/**
 * V4_0-web org.pimslims.servlet.experiment SearchExperimentTest.java
 * 
 * @author cm65
 * @date 19 Jan 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.experiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.report.ExperimentReport;
import org.pimslims.report.PivotTable.Column;
import org.pimslims.report.ThroughputReport;
import org.pimslims.search.Paging;
import org.pimslims.servlet.SearchFilter;
import org.pimslims.servlet.report.T2CReport;

/**
 * SearchExperimentTest
 * 
 */
public class SearchExperimentTest extends TestCase {

    private static final String UNIQUE = "se" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    /**
     * Constructor for SearchExperimentTest
     * 
     * @param name
     */
    public SearchExperimentTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.experiment.SearchExperiment#getTotalSingleExperimentCount(org.pimslims.dao.ReadableVersion)}
     * .
     */
    public void testGetTotalSingleExperimentCount() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final int before = SearchExperiment.getTotalSingleExperimentCount(version);

            this.newDoubleOutputExperiment(version, "t");

            final int after = SearchExperiment.getTotalSingleExperimentCount(version);
            Assert.assertEquals(before + 1, after);

        } finally {
            version.abort();
        }
    }

    private void newDoubleOutputExperiment(final WritableVersion version, final String unique)
        throws ConstraintException {
        final ExperimentType type = new ExperimentType(version, SearchExperimentTest.UNIQUE + unique);
        final Experiment experiment =
            new Experiment(version, SearchExperimentTest.UNIQUE + unique, SearchExperimentTest.NOW,
                SearchExperimentTest.NOW, type);
        new OutputSample(version, experiment).setSample(new Sample(version, SearchExperimentTest.UNIQUE
            + unique + "s1"));
        new OutputSample(version, experiment).setSample(new Sample(version, SearchExperimentTest.UNIQUE
            + unique + "s2"));
        version.flush();
    }

    public void TODOtestSearch() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        final SearchFilter criteria = new SearchFilter();
        SearchExperiment.updateCriteria(criteria);
        try {
            Collection results = new ArrayList();
            final SearchFilter criteria1 = new SearchFilter(criteria);
            SearchExperiment.updateCriteria(criteria1);

            final ExperimentReport report1 = new ExperimentReport(version, criteria1.getMap(), "");
            ((Collection<ModelObject>) results).addAll(report1.getResults(new Paging(0, 20)));
            report1.getAttrition("");
            final int before = report1.count();
            //Assert.assertEquals(limit, results.size());
            this.newDoubleOutputExperiment(version, "");
            results = new ArrayList();

            final ExperimentReport report = new ExperimentReport(version, criteria.getMap(), "");
            ((Collection<ModelObject>) results).addAll(report.getResults(new Paging(0, before + 1)));
            final int after = report.count();
            Assert.assertEquals(before + 1, after);
            //TODO Assert.assertEquals(before + 1, results.size());

        } finally {
            version.abort();
        }
    }

    public void testChartAll() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {

            final ExperimentReport report =
                new ExperimentReport(version, SearchExperiment.onlySingleExperiments(), null);
            report.getAttrition("http://example/pims/SearchExperiment");

        } finally {
            version.abort();
        }
    }

    public void testQuickReport() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {

            final ExperimentReport report =
                new ExperimentReport(version, SearchExperiment.onlySingleExperiments(),
                    SearchExperimentTest.UNIQUE + "nonesuch");
            Assert.assertEquals(0, report.count());

        } finally {
            version.abort();
        }
    }

    public final void testThroughput() throws ServletException, IOException, AbortedException,
        ConstraintException, AccessException {

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ExperimentType type = new ExperimentType(version, SearchExperimentTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, SearchExperimentTest.UNIQUE, SearchExperimentTest.NOW,
                    SearchExperimentTest.NOW, type);
            experiment.setProtocol(new Protocol(version, SearchExperimentTest.UNIQUE + "prot", type));
            experiment.setProject(new ResearchObjective(version, SearchExperimentTest.UNIQUE + "t",
                SearchExperimentTest.UNIQUE));
            new Parameter(version, experiment).setName(SearchExperimentTest.UNIQUE + "parm");
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final SearchExperiment servlet = new SearchExperiment();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map parms = new HashMap();
        parms.put("_presentation", new String[] { "throughput" });
        parms.put("name", new String[] { SearchExperimentTest.UNIQUE });
        parms.put(T2CReport.KEYWORD, new String[] { SearchExperimentTest.UNIQUE + "parm" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setQueryString("SUBMIT=Search");
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertNull(request.getAttribute("noSearch"));
        Assert.assertTrue(0 < (Integer) request.getAttribute("resultSize"));
        Assert.assertEquals(new Integer(1), request.getAttribute("pagenumber"));
        Assert.assertEquals(new Integer(20), request.getAttribute("pagesize"));

        final ThroughputReport throughput = (ThroughputReport) request.getAttribute("throughput");
        Assert.assertNotNull(throughput);
        final Iterator<Column> iterator = throughput.getColumnInterator();
        Assert.assertTrue(iterator.hasNext());
        final Column first = iterator.next();
        Assert.assertEquals(SearchExperimentTest.UNIQUE + "prot", first.getName());
        Assert.assertTrue(iterator.hasNext());
        final Column second = iterator.next();
        Assert.assertEquals(SearchExperimentTest.UNIQUE + "parm", second.getName());
        Assert.assertFalse(iterator.hasNext());
        /* final Iterator iterator = throughput.getColumnInterator();
        for (; iterator.hasNext();) {
            final PivotTable.Column column = (Column) iterator.next();
            System.out.println(column.getName());
        } */
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Experiment experiment =
                version.findFirst(Experiment.class, Experiment.PROP_NAME, SearchExperimentTest.UNIQUE);
            experiment.delete();
            experiment.getProtocol().delete();
            experiment.getExperimentType().delete();
            experiment.getResearchObjective().delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public final void testSearchAll() throws ServletException, IOException, AbortedException,
        ConstraintException, AccessException {

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ExperimentType type = new ExperimentType(version, SearchExperimentTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, SearchExperimentTest.UNIQUE + "only", SearchExperimentTest.NOW,
                    SearchExperimentTest.NOW, type);
            experiment.setProtocol(new Protocol(version, SearchExperimentTest.UNIQUE + "prot", type));
            experiment.setProject(new ResearchObjective(version, SearchExperimentTest.UNIQUE,
                SearchExperimentTest.UNIQUE));
            new Parameter(version, experiment).setName(SearchExperimentTest.UNIQUE + "parm");
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final SearchExperiment servlet = new SearchExperiment();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map parms = new HashMap();
        parms.put("_presentation", new String[] { "list" });
        parms.put("search_all", new String[] { SearchExperimentTest.UNIQUE + "only" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setQueryString("SUBMIT=Quick+Search");
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertNull(request.getAttribute("noSearch"));
        Assert.assertTrue(0 < (Integer) request.getAttribute("resultSize"));
        Assert.assertEquals(new Integer(1), request.getAttribute("pagenumber"));
        Assert.assertEquals(new Integer(20), request.getAttribute("pagesize"));

        final ThroughputReport throughput = (ThroughputReport) request.getAttribute("throughput");
        Assert.assertNull(throughput);

        Assert.assertEquals(new Integer(1), request.getAttribute("resultSize"));

        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Experiment experiment =
                version.findFirst(Experiment.class, Experiment.PROP_NAME, SearchExperimentTest.UNIQUE
                    + "only");
            experiment.delete();
            experiment.getProtocol().delete();
            experiment.getExperimentType().delete();
            experiment.getResearchObjective().delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
