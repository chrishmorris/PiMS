/**
 * pims-web org.pimslims.servlet SearchSamplesTest.java
 * 
 * @author Marc Savitsky
 * @date 31 Oct 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation.sample;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.stat.Statistics;
import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.search.Conditions;
import org.pimslims.search.Paging;
import org.pimslims.search.Searcher;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * TODO move this to data model
 * 
 */
public class SampleSearcherTest extends AbstractTestCase {

    //private static final String UNIQUE = "ss" + System.currentTimeMillis();

    //private static final Calendar NOW = Calendar.getInstance();

    Sample sample1;

    Sample sample2;

    Sample sample3;

    Sample sample4;

    SampleCategory samplecategory;

    ExperimentGroup experimentgroup;

    User user;

    String details = "Sample Details" + System.currentTimeMillis();

    /**
     * @param name
     */
    public SampleSearcherTest(final String name) {
        super(name);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.wv = this.getWV();

        this.samplecategory = POJOFactory.createSampleCategory(this.wv);
        this.user = POJOFactory.createUser(this.wv);

        this.experimentgroup = POJOFactory.create(this.wv, ExperimentGroup.class);
        final Experiment exp = POJOFactory.createExperiment(this.wv);
        exp.setExperimentGroup(this.experimentgroup);

        this.sample1 = POJOFactory.createSample(this.wv);
        this.sample1.addSampleCategory(this.samplecategory);
        this.sample1.setIsActive(true);
        this.sample1.setDetails(this.details);
        this.sample1.setAssignTo(this.user);
        this.sample1.setOutputSample(POJOFactory.createOutputSample(this.wv, exp));

        final Sample sample2 = POJOFactory.createSample(this.wv);
        sample2.setIsActive(true);
        sample2.setDetails(this.details);
        sample2.setAssignTo(this.user);

        final Sample sample3 = POJOFactory.createSample(this.wv);
        sample3.setIsActive(true);
        sample3.setDetails(this.details);
        sample3.setAssignTo(this.user);
        sample3.setOutputSample(POJOFactory.createOutputSample(this.wv, exp));

        final Sample sample4 = POJOFactory.createSample(this.wv);
        sample4.setIsActive(true);
        sample4.setDetails(this.details);
        sample4.setAssignTo(this.user);

        final Sample sample5 = POJOFactory.createSample(this.wv);
        sample5.setIsActive(true);
        sample5.setDetails(this.details);
        sample5.setAssignTo(this.user);
        sample5.setOutputSample(POJOFactory.createOutputSample(this.wv, exp));

        final Sample sample6 = POJOFactory.createSample(this.wv);
        sample6.setIsActive(true);
        sample6.setDetails(this.details);
        //was sample6.setAssignTo(POJOFactory.createUser(this.wv));

        final Sample sample7 = POJOFactory.createSample(this.wv);
        sample7.setIsActive(true);
        sample7.setDetails(this.details);
        //was sample7.setAssignTo(POJOFactory.createUser(this.wv));
        sample7.setOutputSample(POJOFactory.createOutputSample(this.wv, exp));

        final Sample sample8 = POJOFactory.createSample(this.wv);
        sample8.setIsActive(true);
        sample8.setDetails(this.details);
        //was sample8.setAssignTo(POJOFactory.createUser(this.wv));

        final Sample sample9 = POJOFactory.createSample(this.wv);
        sample9.setIsActive(true);
        sample9.setDetails(this.details);
        //was sample9.setAssignTo(POJOFactory.createUser(this.wv));
        sample9.setOutputSample(POJOFactory.createOutputSample(this.wv, exp));

    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        if (this.wv.isCompleted()) {
            // can't tidy up
            return;
        }
        if (null != this.wv) {
            this.wv.abort(); // not testing persistence
        }
    }

    public void testSearchForSample() {
        try {
            final Map<String, Object> scCriteria = new HashMap<String, Object>();
            scCriteria.put(SampleCategory.PROP_NAME, this.samplecategory.getName());

            final Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(AbstractSample.PROP_ISACTIVE, true);
            criteria.put(AbstractSample.PROP_SAMPLECATEGORIES, scCriteria);

            final MetaClass metaClass = this.wv.getMetaClass(Sample.class);
            Collection<ModelObject> results;

            final Searcher s = new Searcher(this.wv);
            results = s.search(criteria, metaClass);
            Assert.assertEquals(1, results.size());

        } finally {
            this.wv.abort();
        }
    }

    /**
     * SearchSampleTest.testSearchForSampleByNameAndCategoryName
     * 
     * Checks that the query is built correctly when searching for a sample both by sample.name and
     * sampleCategory.name.
     */
    public void testSearchForSampleByNameAndCategoryName() {
        try {
            final Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(AbstractSample.PROP_NAME, this.sample1.getName());
            criteria.put(AbstractSample.PROP_ISACTIVE, true);

            final MetaClass metaClass = this.wv.getMetaClass(Sample.class);
            Collection<ModelObject> results;

            // Check we can find the sample by name alone
            final Searcher s = new Searcher(this.wv);
            results = s.search(criteria, metaClass);
            Assert.assertEquals(1, results.size());

            final Map<String, Object> scCriteria = new HashMap<String, Object>();
            scCriteria.put(SampleCategory.PROP_NAME, this.samplecategory.getName());

            criteria.put(AbstractSample.PROP_SAMPLECATEGORIES, scCriteria);

            // Check we can find the sample by name and sampleCategory name
            results = s.search(criteria, metaClass);
            Assert.assertEquals(1, results.size());

        } finally {
            this.wv.abort();
        }
    }

    /**
     * SearchSampleTest.testSearchForSampleByNameAndCategoryName
     * 
     * Checks that the query is built correctly when searching for a sample both by sample.name and
     * sampleCategory.name.
     */
    public void testSearchForSampleByPartName() {
        try {
            final Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(AbstractSample.PROP_NAME, this.sample1.getName().substring(0, 8));
            criteria.put(AbstractSample.PROP_ISACTIVE, true);

            final MetaClass metaClass = this.wv.getMetaClass(Sample.class);
            Collection<ModelObject> results;

            // Check we can find the sample by name alone
            final Searcher s = new Searcher(this.wv);
            results = s.search(criteria, metaClass);
            Assert.assertEquals(9, results.size());

            final Map<String, Object> scCriteria = new HashMap<String, Object>();
            scCriteria.put(SampleCategory.PROP_NAME, this.samplecategory.getName());

            criteria.put(AbstractSample.PROP_SAMPLECATEGORIES, scCriteria);

            // Check we can find the sample by name and sampleCategory name
            results = s.search(criteria, metaClass);
            Assert.assertEquals(1, results.size());

        } finally {
            this.wv.abort();
        }
    }

    /**
     * SearchSampleTest.testSampleSearchFetchesOutputSample
     * 
     * Checks that the Sample search query does a fetch join on OutputSample, saving an extra n trips to the
     * database where n is the number of samples found by the search.
     * 
     * @throws ConstraintException - bubbled from an internal call to WritableVersion.flush()
     */
    public void testSampleSearchFetchesOutputSample() throws ConstraintException {
        try {

            final Log log = LogFactory.getLog(this.getClass());

            final WritableVersionImpl wvi = ((WritableVersionImpl) this.wv);
            final Statistics stats = wvi.getSession().getSessionFactory().getStatistics();

            // Set statisticsEnabled flag, remembering current state
            final boolean isStatisticsEnabled = stats.isStatisticsEnabled();
            stats.setStatisticsEnabled(true);

            /* 
             * If this test is run in isolation, sample2 doesn't get flushed
             * to the database before I clear the cache, so force flush now
             */
            this.wv.flush();

            // Clear the cache, so we are not deceived by cache hits
            wvi.getSession().clear();

            // Build criteria
            final Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(AbstractSample.PROP_NAME, this.sample1.getName().substring(0, 8));
            criteria.put(AbstractSample.PROP_ISACTIVE, true);

            final MetaClass metaClass = this.wv.getMetaClass(Sample.class);
            Collection<ModelObject> results;

            // Get the current prepared statement count
            final long p1 = stats.getPrepareStatementCount();

            // Run the search
            log.info("Searching: preparedStatementCount=" + p1);
            final Searcher s = new Searcher(this.wv);
            results = s.search(criteria, metaClass);
            Assert.assertEquals(9, results.size());

            // Check we are actually doing a fetch join on OutputSample
            final long p2 = stats.getPrepareStatementCount();
            log.info("Done search: preparedStatementCount=" + p2);
            Assert
                .assertEquals(
                    "If the search query is a fetching OutputSample, (p2 - p1) = 1. If its not, (p2 - p1) = (1 + results.size()), ie 3. Set log4j.logger.org.hibernate.SQL=debug to see the statements.",
                    1, p2 - p1);

            // Reset statisticsEnabled flag
            stats.setStatisticsEnabled(isStatisticsEnabled);

        } finally {
            this.wv.abort();
        }
    }

    public void testQuickSearchForSample() {
        try {
            final MetaClass metaClass = this.wv.getMetaClass(Sample.class);
            Collection<ModelObject> results;

            // Check we can find the sample by name alone
            final Searcher s = new Searcher(this.wv);
            results = s.searchAll(metaClass, this.details.substring(7));
            Assert.assertEquals(9, results.size());

            final Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(AbstractSample.PROP_ISACTIVE, true);

            final Map<String, Object> scCriteria = new HashMap<String, Object>();
            scCriteria.put(SampleCategory.PROP_NAME, this.samplecategory.getName());

            criteria.put(AbstractSample.PROP_SAMPLECATEGORIES, scCriteria);

            results = s.search(metaClass, criteria, this.details.substring(7));
            Assert.assertEquals(1, results.size());

        } finally {
            this.wv.abort();
        }
    }

    public void testQuickSearchPersonFilter() {
        try {
            final MetaClass metaClass = this.wv.getMetaClass(Sample.class);
            Collection<ModelObject> results;

            final Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(AbstractSample.PROP_NAME, this.sample1.getName().substring(0, 8));
            criteria.put(AbstractSample.PROP_ISACTIVE, true);

            final Searcher s = new Searcher(this.wv);
            results = s.search(criteria, metaClass);
            Assert.assertEquals(9, results.size());

            criteria.put(Sample.PROP_ASSIGNTO, this.user);
            results = s.search(criteria, metaClass);
            Assert.assertEquals(5, results.size());

        } finally {
            this.wv.abort();
        }
    }

    public void testQuickSearchGroupFilter() {
        try {
            final MetaClass metaClass = this.wv.getMetaClass(Sample.class);
            Collection<ModelObject> results;

            final Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(AbstractSample.PROP_NAME, this.sample1.getName().substring(0, 8));
            criteria.put(AbstractSample.PROP_ISACTIVE, true);

            final Searcher s = new Searcher(this.wv);
            results = s.search(criteria, metaClass);
            Assert.assertEquals(9, results.size());

            final Map<String, Object> expCriterials =
                Conditions.newMap(Experiment.PROP_EXPERIMENTGROUP,
                    Conditions.or(Conditions.isNull(), Conditions.notEquals(this.experimentgroup)));

            final Map<String, Object> osCriterials =
                Conditions.newMap(OutputSample.PROP_EXPERIMENT, expCriterials);

            criteria.put(Sample.PROP_OUTPUTSAMPLE, osCriterials);
            results = s.search(criteria, metaClass);
            Assert.assertEquals(4, results.size());

        } finally {
            this.wv.abort();
        }
    }

    public void testPagedQuickSearchForSample() {
        try {
            final MetaClass metaClass = this.wv.getMetaClass(Sample.class);
            Collection<ModelObject> results;

            final Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(AbstractSample.PROP_NAME, this.sample1.getName().substring(0, 8));
            criteria.put(AbstractSample.PROP_ISACTIVE, true);

            final Searcher s = new Searcher(this.wv);
            results = s.search(criteria, metaClass);
            Assert.assertEquals(9, results.size());

            Paging paging = new Paging(1, 2);
            results = s.search(criteria, metaClass, paging);
            Assert.assertEquals(2, results.size());

            paging = new Paging(2, 2);
            results = s.search(criteria, metaClass, paging);
            Assert.assertEquals(2, results.size());

        } finally {
            this.wv.abort();
        }
    }

}
