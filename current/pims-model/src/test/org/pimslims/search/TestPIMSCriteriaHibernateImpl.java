/**
 * pims-model org.pimslims.search TestPIMSCriteriaHibernateImpl.java
 * 
 * @author Jonathan Diprose
 * @date 10 Nov 2008
 * 
 *       Protein Information Management System
 * @version: 2.3
 * 
 *           Copyright (c) 2008 Jonathan Diprose The copyright holder has licenced the STFC to redistribute
 *           this software
 */
package org.pimslims.search;

import java.util.Collection;

import junit.framework.Assert;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.stat.Statistics;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * TestPIMSCriteriaHibernateImpl
 * 
 * Test cases for PIMSCriteriaHibernateImpl that basically mirror the tests in SampleSearchTest in the
 * pims-web project. Hopefully these test cases demonstrate that PIMSCriteria2 is easier and safer to use than
 * PIMSCriteria
 */
public class TestPIMSCriteriaHibernateImpl extends AbstractTestCase {

    /**
     * The Log for this class.
     */
    //private static final Logger LOG = LoggerFactory.getLogger(TestPIMSCriteriaHibernateImpl.class);

    private static class Logger {

        /**
         * Logger.debug
         * 
         * @param string
         */
        public void debug(String string) {
            // 

        }

        /**
         * Logger.info
         * 
         * @param string
         */
        public void info(String string) {
            // 

        }
    }

    private static final Logger LOG = new Logger();

    /**
     * A sample for which to search.
     */
    Sample sample;

    /**
     * A SampleCategory for which to search.
     */
    SampleCategory samplecategory;

    /**
     * A details string for which to search.
     */
    String details = "Sample Details" + System.currentTimeMillis();

    /**
     * @param name - the name of this test
     */
    public TestPIMSCriteriaHibernateImpl(final String name) {
        super(name);
    }

    /**
     * @throws Exception if something fails
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        try {

            /* 
             * TODO Allow testing with a different username to demonstrate
             * that the access control works as expected
             */
            this.wv = this.getWV();

            this.samplecategory = POJOFactory.createSampleCategory(this.wv);
            this.sample = POJOFactory.createSample(this.wv);
            LOG.debug("AccessObject=" + this.sample.getAccess());
            this.sample.addSampleCategory(this.samplecategory);
            this.sample.setIsActive(Boolean.TRUE);
            this.sample.setDetails(this.details);

            final Sample sample2 = POJOFactory.createSample(this.wv);
            sample2.setIsActive(Boolean.TRUE);
            sample2.setDetails(this.details);

            wv.flush();
            wv.getSession().clear();

        } catch (Throwable t) {
            if (null != wv) {
                wv.abort();
                wv = null;
            }
            throw new RuntimeException(t);
        }
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        if (null != this.wv) {
            if (this.wv.isCompleted()) {
                // can't tidy up
                return;
            }
            this.wv.abort(); // not testing persistence
        }
    }

    /**
     * Demonstrate a search for a sample by sampleCategory.name.
     * 
     * @see SearchSampleTest.testSearchForSample
     */
    public void testSearchForSample() {
        LOG.debug("testSearchForSample()");
        try {

            PIMSCriteriaHibernateImpl instance = new PIMSCriteriaHibernateImpl(wv, Sample.class, null);

            instance.add(Restrictions.eq(AbstractSample.PROP_ISACTIVE, Boolean.TRUE));

            instance.createAlias(AbstractSample.PROP_SAMPLECATEGORIES, "sc");
            instance.add(Restrictions.eq("sc." + SampleCategory.PROP_NAME, this.samplecategory.getName()));

            Collection<ModelObject> results = instance.list();
            Assert.assertEquals(1, results.size());

        } finally {
            this.wv.abort();
        }
        LOG.debug("Done testSearchForSample()");
    }

    /**
     * Demonstrate a search for a sample both by sample.name and sampleCategory.name.
     * 
     * @see SearchSampleTest.testSearchForSampleByNameAndCategoryName
     */
    public void testSearchForSampleByNameAndCategoryName() {
        LOG.debug("testSearchForSampleByNameAndCategoryName()");
        try {

            PIMSCriteriaHibernateImpl instance = new PIMSCriteriaHibernateImpl(wv, Sample.class, null);

            instance.add(Restrictions.eq(AbstractSample.PROP_NAME, this.sample.getName()));
            instance.add(Restrictions.eq(AbstractSample.PROP_ISACTIVE, Boolean.TRUE));

            instance.createAlias(AbstractSample.PROP_SAMPLECATEGORIES, "sc");
            instance.add(Restrictions.eq("sc." + SampleCategory.PROP_NAME, this.samplecategory.getName()));

            Collection<ModelObject> results = instance.list();
            Assert.assertEquals(1, results.size());

        } finally {
            this.wv.abort();
        }
        LOG.debug("Done testSearchForSampleByNameAndCategoryName()");
    }

    /**
     * Demonstrate a name like '%foo%' search. Note that the search can be made case-insensitive by using
     * Restrictions.ilike rather than Restrictions.like.
     * 
     * @see SearchSampleTest.testSearchForSampleByNameAndCategoryName
     */
    public void testSearchForSampleByPartName() {
        LOG.debug("testSearchForSampleByPartName()");
        try {

            PIMSCriteriaHibernateImpl instance = new PIMSCriteriaHibernateImpl(wv, Sample.class, null);

            instance.add(Restrictions.like(AbstractSample.PROP_NAME,
                "%" + this.sample.getName().substring(0, 8) + "%"));
            instance.add(Restrictions.eq(AbstractSample.PROP_ISACTIVE, Boolean.TRUE));

            Collection<ModelObject> results = instance.list();
            Assert.assertEquals(2, results.size());

            instance.createAlias(AbstractSample.PROP_SAMPLECATEGORIES, "sc");
            instance.add(Restrictions.eq("sc." + SampleCategory.PROP_NAME, this.samplecategory.getName()));

            results = instance.list();
            Assert.assertEquals(1, results.size());

        } finally {
            this.wv.abort();
        }
        LOG.debug("Done testSearchForSampleByPartName()");
    }

    /**
     * As testSearchForSampleByPartName(), but this time demonstrating the count functionality.
     * 
     * @see SearchSampleTest.testSearchForSampleByNameAndCategoryName
     */
    public void testCountSampleByPartName() {
        LOG.debug("testCountSampleByPartName()");
        try {

            PIMSCriteriaHibernateImpl instance = new PIMSCriteriaHibernateImpl(wv, Sample.class, null);

            instance.add(Restrictions.like(AbstractSample.PROP_NAME,
                "%" + this.sample.getName().substring(0, 8) + "%"));
            instance.add(Restrictions.eq(AbstractSample.PROP_ISACTIVE, Boolean.TRUE));

            int count = instance.count();
            Assert.assertEquals(2, count);

            instance.createAlias(AbstractSample.PROP_SAMPLECATEGORIES, "sc");
            instance.add(Restrictions.eq("sc." + SampleCategory.PROP_NAME, this.samplecategory.getName()));

            count = instance.count();
            Assert.assertEquals(1, count);

        } finally {
            this.wv.abort();
        }
        LOG.debug("Done testCountSampleByPartName()");
    }

    /**
     * As testSearchForSampleByPartName(), but this time demonstrating the getSingleResult() functionality.
     * 
     * @see SearchSampleTest.testSearchForSampleByNameAndCategoryName
     */
    public void testGetSingleSampleByPartName() {
        LOG.debug("testGetSingleSampleByPartName()");
        try {

            PIMSCriteriaHibernateImpl instance = new PIMSCriteriaHibernateImpl(wv, Sample.class, null);

            instance.add(Restrictions.like(AbstractSample.PROP_NAME,
                "%" + this.sample.getName().substring(0, 8) + "%"));
            instance.add(Restrictions.eq(AbstractSample.PROP_ISACTIVE, Boolean.TRUE));

            try {
                instance.getSingleResult();
                Assert.fail("Should throw a NonUniqueResultException");
            } catch (org.hibernate.NonUniqueResultException e) {
                // Supposed to get here
            }

            instance.createAlias(AbstractSample.PROP_SAMPLECATEGORIES, "sc");
            instance.add(Restrictions.eq("sc." + SampleCategory.PROP_NAME, this.samplecategory.getName()));

            ModelObject mo = instance.getSingleResult();
            Assert.assertEquals(this.sample.getName(), ((Sample) mo).getName());

        } finally {
            this.wv.abort();
        }
        LOG.debug("Done testGetSingleSampleByPartName()");
    }

    /**
     * Check that the Sample search query does a fetch join on OutputSample, saving an extra n trips to the
     * database where n is the number of samples found by the search.
     * 
     * @throws ConstraintException - bubbled from an internal call to WritableVersion.flush()
     * @see SearchSampleTest.testSampleSearchFetchesOutputSample
     */
    public void testSampleSearchFetchesOutputSample() throws ConstraintException {
        LOG.debug("testSampleSearchFetchesOutputSample()");
        try {

            PIMSCriteriaHibernateImpl instance = new PIMSCriteriaHibernateImpl(wv, Sample.class, null);

            final ReadableVersion wvi = ((ReadableVersion) this.wv);
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

            /* 
             * But this call is made within the list method and hits the database,
             * so we need to call it here to get the results cached before we take
             * our count. It only actually matters if you test with a username
             * for which access control applies
             */
            wvi.getReadableLabNotebooks();

            // Build criteria
            instance.add(Restrictions.like(AbstractSample.PROP_NAME,
                "%" + this.sample.getName().substring(0, 8) + "%"));
            instance.add(Restrictions.eq(AbstractSample.PROP_ISACTIVE, Boolean.TRUE));

            // Get the current prepared statement count
            final long p1 = stats.getPrepareStatementCount();

            // Run the search
            LOG.debug("Searching: preparedStatementCount=" + p1);
            Collection<ModelObject> results = instance.list();
            Assert.assertEquals(2, results.size());

            // Check we are actually doing a fetch join on OutputSample
            final long p2 = stats.getPrepareStatementCount();
            LOG.debug("Done search: preparedStatementCount=" + p2);
            Assert
                .assertEquals(
                    "If the search query is a fetching OutputSample, (p2 - p1) = 1. If its not, (p2 - p1) = (1 + results.size()), ie 3. Set log4j.logger.org.hibernate.SQL=debug to see the statements.",
                    1, p2 - p1);

            // Reset statisticsEnabled flag
            stats.setStatisticsEnabled(isStatisticsEnabled);

        } finally {
            this.wv.abort();
        }
        LOG.debug("Done testSampleSearchFetchesOutputSample()");
    }

    /**
     * Demonstrate an any string attribute like '%foo%' search. Note that the search can be made
     * case-insensitive by using Restrictions.ilike rather than Restrictions.like.
     * 
     * Note that this doesn't walk the subclasses' attributes as the existing implementation does, but it
     * would not be difficult to code.
     * 
     * @see SampleSearchTest.testQuickSearchForSample
     */
    public void testQuickSearchForSample() {
        LOG.debug("testQuickSearchForSample()");
        try {

            PIMSCriteriaHibernateImpl instance = new PIMSCriteriaHibernateImpl(wv, Sample.class, null);

            final MetaClass metaClass = this.wv.getMetaClass(Sample.class);
            instance.add(PIMSCriteriaHibernateImpl.anyLike(metaClass, this.details.substring(7)));

            instance.add(Restrictions.eq(AbstractSample.PROP_ISACTIVE, Boolean.TRUE));

            instance.createAlias(AbstractSample.PROP_SAMPLECATEGORIES, "sc");
            instance.add(Restrictions.eq("sc." + SampleCategory.PROP_NAME, this.samplecategory.getName()));

            Collection<ModelObject> results = instance.list();
            Assert.assertEquals(1, results.size());

        } finally {
            this.wv.abort();
        }
        LOG.debug("Done testQuickSearchForSample()");
    }

    /**
     * Demonstrate an search for an active sample in a sample category but not in an experiment group.
     */
    public void testSearchForSampleNotInExperimentGroup() {
        LOG.info("testSearchForSampleNotInExperimentGroup()");
        try {

            PIMSCriteriaHibernateImpl instance = new PIMSCriteriaHibernateImpl(wv, Sample.class, null);

            instance.add(Restrictions.eq(AbstractSample.PROP_ISACTIVE, Boolean.TRUE));

            instance.createAlias(AbstractSample.PROP_SAMPLECATEGORIES, "sc");
            instance.add(Restrictions.eq("sc." + SampleCategory.PROP_NAME, this.samplecategory.getName()));

            instance.createAlias(Sample.PROP_OUTPUTSAMPLE, "os", PIMSCriteria2Alias.JoinType.LEFT_JOIN);
            instance.createAlias("os." + OutputSample.PROP_EXPERIMENT, "ex",
                PIMSCriteria2Alias.JoinType.LEFT_JOIN);

            Junction or = Restrictions.disjunction();
            or.add(Restrictions.isNull("os.id")); // This is probably unnecessary
            or.add(Restrictions.isNull("ex." + Experiment.PROP_EXPERIMENTGROUP));
            or.add(Restrictions.not(Restrictions.eq("ex." + Experiment.PROP_EXPERIMENTGROUP + ".id",
                new Long(1234))));

            instance.add(or);

            Collection<ModelObject> results = instance.list();
            Assert.assertEquals(1, results.size());
            ((Sample) results.iterator().next()).getOutputSample();

        } finally {
            this.wv.abort();
        }
        LOG.info("Done testSearchForSampleNotInExperimentGroup()");
    }

}
