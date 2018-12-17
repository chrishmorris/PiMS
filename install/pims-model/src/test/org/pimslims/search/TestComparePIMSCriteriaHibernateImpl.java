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
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TestPIMSCriteriaHibernateImpl
 * 
 * Test cases for PIMSCriteriaHibernateImpl that basically mirror the tests in SampleSearchTest in the
 * pims-web project. Hopefully these test cases demonstrate that PIMSCriteria2 is easier and safer to use than
 * PIMSCriteria
 */
public class TestComparePIMSCriteriaHibernateImpl extends AbstractTestCase {

    /**
     * The Log for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(TestComparePIMSCriteriaHibernateImpl.class);

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
    public TestComparePIMSCriteriaHibernateImpl(final String name) {
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

/*            PIMSCriteriaHibernateImpl instance = new PIMSCriteriaHibernateImpl(wv, Sample.class, null);

            instance.add(Restrictions.eq(AbstractSample.PROP_ISACTIVE, Boolean.TRUE));

            instance.createAlias(AbstractSample.PROP_SAMPLECATEGORIES, "sc");
            instance.add(Restrictions.eq("sc." + SampleCategory.PROP_NAME, this.samplecategory.getName()));

            Collection<ModelObject> results = instance.list();*/
            Map<String, Object> sampleCriterias = new HashMap<String, Object>();
            sampleCriterias.put(AbstractSample.PROP_ISACTIVE, true);
            sampleCriterias.put(AbstractSample.PROP_SAMPLECATEGORIES,
                Conditions.newMap(SampleCategory.PROP_NAME, this.samplecategory.getName()));

            Collection<Sample> results = wv.findAll(Sample.class, sampleCriterias);
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

/*            PIMSCriteriaHibernateImpl instance = new PIMSCriteriaHibernateImpl(wv, Sample.class, null);

            instance.add(Restrictions.eq(AbstractSample.PROP_NAME, this.sample.getName()));
            instance.add(Restrictions.eq(AbstractSample.PROP_ISACTIVE, Boolean.TRUE));

            instance.createAlias(AbstractSample.PROP_SAMPLECATEGORIES, "sc");
            instance.add(Restrictions.eq("sc." + SampleCategory.PROP_NAME, this.samplecategory.getName()));

            Collection<ModelObject> results = instance.list();*/
            Map<String, Object> sampleCriterias = new HashMap<String, Object>();
            sampleCriterias.put(AbstractSample.PROP_ISACTIVE, true);
            sampleCriterias.put(AbstractSample.PROP_NAME, this.sample.getName());
            sampleCriterias.put(AbstractSample.PROP_SAMPLECATEGORIES,
                Conditions.newMap(SampleCategory.PROP_NAME, this.samplecategory.getName()));
            Collection<Sample> results = wv.findAll(Sample.class, sampleCriterias);

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

/*            PIMSCriteriaHibernateImpl instance = new PIMSCriteriaHibernateImpl(wv, Sample.class, null);

            instance.add(Restrictions.like(AbstractSample.PROP_NAME, "%"
                + this.sample.getName().substring(0, 8) + "%"));
            instance.add(Restrictions.eq(AbstractSample.PROP_ISACTIVE, Boolean.TRUE));

            Collection<ModelObject> results = instance.list();*/
            Map<String, Object> sampleCriterias = new HashMap<String, Object>();
            sampleCriterias.put(AbstractSample.PROP_ISACTIVE, true);
            sampleCriterias.put(AbstractSample.PROP_NAME,
                Conditions.contains(this.sample.getName().substring(0, 8)));
            Collection<Sample> results = wv.findAll(Sample.class, sampleCriterias);

            Assert.assertEquals(2, results.size());
            sampleCriterias.put(AbstractSample.PROP_SAMPLECATEGORIES,
                Conditions.newMap(SampleCategory.PROP_NAME, this.samplecategory.getName()));
            results = wv.findAll(Sample.class, sampleCriterias);
/*            instance.createAlias(AbstractSample.PROP_SAMPLECATEGORIES, "sc");
            instance.add(Restrictions.eq("sc." + SampleCategory.PROP_NAME, this.samplecategory.getName()));

            results = instance.list();*/
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

/*            PIMSCriteriaHibernateImpl instance = new PIMSCriteriaHibernateImpl(wv, Sample.class, null);

            instance.add(Restrictions.like(AbstractSample.PROP_NAME, "%"
                + this.sample.getName().substring(0, 8) + "%"));
            instance.add(Restrictions.eq(AbstractSample.PROP_ISACTIVE, Boolean.TRUE));

            int count = instance.count();*/
            Map<String, Object> sampleCriterias = new HashMap<String, Object>();
            sampleCriterias.put(AbstractSample.PROP_ISACTIVE, true);
            sampleCriterias.put(AbstractSample.PROP_NAME,
                Conditions.contains(this.sample.getName().substring(0, 8)));
            int count = wv.count(Sample.class, sampleCriterias);

            Assert.assertEquals(2, count);
            sampleCriterias.put(AbstractSample.PROP_SAMPLECATEGORIES,
                Conditions.newMap(SampleCategory.PROP_NAME, this.samplecategory.getName()));
            count = wv.count(Sample.class, sampleCriterias);

/*            instance.createAlias(AbstractSample.PROP_SAMPLECATEGORIES, "sc");
            instance.add(Restrictions.eq("sc." + SampleCategory.PROP_NAME, this.samplecategory.getName()));

            count = instance.count();
*/          Assert.assertEquals(1, count);

        } finally {
            this.wv.abort();
        }
        LOG.debug("Done testCountSampleByPartName()");
    }

    /**
     * Demonstrate an search for an active sample in a sample category but not in an experiment group.
     */
    public void testSearchForSampleNotInExperimentGroup() {
        LOG.info("testSearchForSampleNotInExperimentGroup()");
        try {

/*            PIMSCriteriaHibernateImpl instance = new PIMSCriteriaHibernateImpl(wv, Sample.class, null);

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

            Collection<ModelObject> results = instance.list();*/
            Map<String, Object> expCriterials =
                Conditions.newMap(
                    Experiment.PROP_EXPERIMENTGROUP,
                    Conditions.newMap(LabBookEntry.PROP_DBID,
                        Conditions.or(Conditions.isNull(), Conditions.notEquals(new Long(1234)))));

            Map<String, Object> osCriterials = Conditions.newMap(OutputSample.PROP_EXPERIMENT, expCriterials);
            osCriterials.put(LabBookEntry.PROP_DBID, null);

            Map<String, Object> sampleCriterias = new HashMap<String, Object>();
            sampleCriterias.put(AbstractSample.PROP_ISACTIVE, true);
            sampleCriterias.put(AbstractSample.PROP_SAMPLECATEGORIES,
                Conditions.newMap(SampleCategory.PROP_NAME, this.samplecategory.getName()));
            sampleCriterias.put(Sample.PROP_OUTPUTSAMPLE, Conditions.orMap(osCriterials));

            Collection<Sample> results = wv.findAll(Sample.class, sampleCriterias);

            Assert.assertEquals(1, results.size());
            ((Sample) results.iterator().next()).getOutputSample();

        } finally {
            this.wv.abort();
        }
        LOG.info("Done testSearchForSampleNotInExperimentGroup()");
    }

}
