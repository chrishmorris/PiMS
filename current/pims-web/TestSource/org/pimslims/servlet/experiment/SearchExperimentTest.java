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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;
import org.pimslims.report.ExperimentReport;
import org.pimslims.search.Paging;

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

            this.newDoubleOutputExperiment(version);

            final int after = SearchExperiment.getTotalSingleExperimentCount(version);
            Assert.assertEquals(before + 1, after);

        } finally {
            version.abort();
        }
    }

    private void newDoubleOutputExperiment(final WritableVersion version) throws ConstraintException {
        final ExperimentType type = new ExperimentType(version, SearchExperimentTest.UNIQUE);
        final Experiment experiment =
            new Experiment(version, SearchExperimentTest.UNIQUE, SearchExperimentTest.NOW,
                SearchExperimentTest.NOW, type);
        new OutputSample(version, experiment).setSample(new Sample(version, SearchExperimentTest.UNIQUE
            + "s1"));
        new OutputSample(version, experiment).setSample(new Sample(version, SearchExperimentTest.UNIQUE
            + "s2"));
        version.flush();
    }

    public void testSearch() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            Collection results = new ArrayList();
            final int before =
                SearchExperimentTest.search(version, Collections.EMPTY_MAP, new Paging(0, 20), results, "");
            //Assert.assertEquals(limit, results.size());
            this.newDoubleOutputExperiment(version);
            results = new ArrayList();
            final Map<String, Object> criteria = new HashMap(Collections.EMPTY_MAP);
            SearchExperiment.updateCriteria(criteria);

            final ExperimentReport report = new ExperimentReport(version, criteria, "");
            ((Collection<ModelObject>) results).addAll(report.getResults(new Paging(0, before + 1)));
            final int after = report.count();
            Assert.assertEquals(before + 1, after);
            Assert.assertEquals(before + 1, results.size());

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

    static int search(final ReadableVersion version, final Map<String, Object> requestCriteria,
        final Paging paging, final Collection<ModelObject> results, final String svalue) {

        final Map<String, Object> criteria = new HashMap(requestCriteria);
        SearchExperiment.updateCriteria(criteria);

        final ExperimentReport report = new ExperimentReport(version, criteria, svalue);
        results.addAll(report.getResults(paging));
        report.getAttrition("");
        return report.count();

    }

}
