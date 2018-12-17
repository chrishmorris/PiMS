/**
 * pims-model org.pimslims.search PlateSampleSearchTest.java
 * 
 * @author bl67
 * @date 9 Dec 2008
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2008 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.search;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.test.AbstractTestCase;

/**
 * PlateSampleSearchTest
 * 
 */
public class PlateSampleSearchTest extends AbstractTestCase {
    private SampleCategory samplecategory;

    private Sample sample1;

    private Sample sample2;

    private Sample sample3;

    private ExperimentGroup experimentGroup;

    private User assignTo;

    // select s from Sample s left join fetch s.outputSample as os 
    //left join os.experiment as e join s.sampleCategories sc 
    //where [access control on s, os and e] and (os.id is null or e.experimentGroup.id is null 
    //or not e.experimentGroup.id = :currentExperimentGroupId) and s.active = TRUE 
    //and sc.id = :sampleCategoryId [and s.assignTo.id = :personId] [and restrictions on sample]

    public void testBasicSearch() {
        Map<String, Object> sampleCriterials = new HashMap<String, Object>();
        sampleCriterials.put(AbstractSample.PROP_SAMPLECATEGORIES, samplecategory);
        sampleCriterials.put(AbstractSample.PROP_ISACTIVE, true);
        sampleCriterials.put(Sample.PROP_ASSIGNTO, assignTo);
        //not e.experimentGroup.id = :currentExperimentGroupId)
        Map<String, Object> expCriterials =
            Conditions.newMap(Experiment.PROP_EXPERIMENTGROUP,
                Conditions.or(Conditions.isNull(), Conditions.notEquals(experimentGroup)));

        Map<String, Object> osCriterials = Conditions.newMap(OutputSample.PROP_EXPERIMENT, expCriterials);

        sampleCriterials.put(Sample.PROP_OUTPUTSAMPLE, osCriterials);

        Collection<Sample> results = wv.findAll(Sample.class, sampleCriterials);
        //select distinct A from org.pimslims.model.sample.Sample A   left join fetch A.outputSample as outputSample  left join A.assignTo as assignTo  left join A.sampleCategories as sampleCategories  left join outputSample.experiment as experiment  left join experiment.experimentGroup as experimentGroup  where  (A.access is null OR A.access in (:possibleAccessObjects0_, :possibleAccessObjects1_) )  AND ( assignTo = :assignTo1  AND  A.isActive = :isActive2  AND (experiment.experimentGroup is null  or  experimentGroup != :experimentGroup4 ) AND  sampleCategories = :sampleCategories5 )  Order by A.dbId DESC  
        assertFalse(results.contains(sample1));
        assertTrue(results.contains(sample3));
        assertTrue(results.contains(sample2));

    }

    public void testAdvanceSearch() throws AccessException, ConstraintException {
        Map<String, Object> sampleCriterials = new HashMap<String, Object>();
        sampleCriterials.put(AbstractSample.PROP_SAMPLECATEGORIES, samplecategory);
        sampleCriterials.put(AbstractSample.PROP_ISACTIVE, true);
        sampleCriterials.put(Sample.PROP_ASSIGNTO, assignTo);
        sampleCriterials.put(AbstractSample.PROP_NAME,
            Conditions.or(Conditions.contains(sample1.getName()), Conditions.contains(sample2.getName())));
        //not e.experimentGroup.id = :currentExperimentGroupId)
        Map<String, Object> expCriterials =
            Conditions.newMap(Experiment.PROP_EXPERIMENTGROUP,
                Conditions.or(Conditions.isNull(), Conditions.notEquals(create(ExperimentGroup.class))));

        Map<String, Object> osCriterials = Conditions.newMap(OutputSample.PROP_EXPERIMENT, expCriterials);
        osCriterials.put(LabBookEntry.PROP_DBID, null);

        sampleCriterials.put(Sample.PROP_OUTPUTSAMPLE, Conditions.orMap(osCriterials));

        Collection<Sample> results = wv.findAll(Sample.class, sampleCriterials);
        //select distinct A from org.pimslims.model.sample.Sample A   left join fetch A.outputSample as outputSample  left join A.assignTo as assignTo  left join A.sampleCategories as sampleCategories  left join outputSample.experiment as experiment  left join experiment.experimentGroup as experimentGroup  where  (A.access is null OR A.access in (:possibleAccessObjects0_, :possibleAccessObjects1_) )  AND ( assignTo = :assignTo1  AND  A.isActive = :isActive2  AND ( lower( A.name) like :name3 or  lower( A.name) like :name4) AND (outputSample.dbId is null  OR (experiment.experimentGroup is null  or  experimentGroup != :experimentGroup7 )) AND  sampleCategories = :sampleCategories8 )  Order by A.dbId DESC    
        assertTrue(results.contains(sample1));
        assertTrue(results.contains(sample2));
        assertFalse(results.contains(sample3));

    }

    public void testAdvanceSearch2() throws AccessException, ConstraintException {
        Map<String, Object> sampleCriterials = new HashMap<String, Object>();
        sampleCriterials.put(AbstractSample.PROP_SAMPLECATEGORIES, samplecategory);
        sampleCriterials.put(AbstractSample.PROP_ISACTIVE, true);
        sampleCriterials.put(Sample.PROP_ASSIGNTO, assignTo);
        sampleCriterials.put(AbstractSample.PROP_NAME, Conditions.and(Conditions.notNull(),
            Conditions.or(Conditions.contains(sample1.getName()), Conditions.contains(sample2.getName()))));
        //not e.experimentGroup.id = :currentExperimentGroupId)
        Map<String, Object> expCriterials =
            Conditions.newMap(Experiment.PROP_EXPERIMENTGROUP,
                Conditions.or(Conditions.isNull(), Conditions.notEquals(create(ExperimentGroup.class))));

        Map<String, Object> osCriterials = Conditions.newMap(OutputSample.PROP_EXPERIMENT, expCriterials);

        sampleCriterials.put(Sample.PROP_OUTPUTSAMPLE, osCriterials);

        Collection<Sample> results = wv.findAll(Sample.class, sampleCriterials);
        //select distinct A from org.pimslims.model.sample.Sample A   left join fetch A.outputSample as outputSample  left join A.assignTo as assignTo  left join A.sampleCategories as sampleCategories  left join outputSample.experiment as experiment  left join experiment.experimentGroup as experimentGroup  where  (A.access is null OR A.access in (:possibleAccessObjects0_, :possibleAccessObjects1_) )  AND ( assignTo = :assignTo1  AND  A.isActive = :isActive2  AND (A.name is not null  and ( lower( A.name) like :name4 or  lower( A.name) like :name5)) AND (experiment.experimentGroup is null  or  experimentGroup != :experimentGroup7 ) AND  sampleCategories = :sampleCategories8 )  Order by A.dbId DESC      
        assertTrue(results.contains(sample1));
        assertTrue(results.contains(sample2));
        assertFalse(results.contains(sample3));

    }

    /**
     * @throws Exception if something fails
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        String userName = createUser();

        try {

            /* 
             * TODO Allow testing with a different username to demonstrate
             * that the access control works as expected
             */
            this.wv = this.getWV(userName);

            createSamples();

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
     * PlateSampleSearchTest.createSamples
     * 
     * @throws ConstraintException
     * @throws AccessException
     */
    private void createSamples() throws AccessException, ConstraintException {
        this.samplecategory = create(SampleCategory.class);
        this.assignTo = create(User.class);
        this.experimentGroup = create(ExperimentGroup.class);

        //create sample1 with exp group
        this.sample1 = create(Sample.class, AbstractSample.PROP_SAMPLECATEGORIES, samplecategory);
        sample1.setIsActive(true);
        sample1.setAssignTo(assignTo);
        Experiment exp = create(Experiment.class, Experiment.PROP_EXPERIMENTGROUP, experimentGroup);
        OutputSample os = create(OutputSample.class, OutputSample.PROP_EXPERIMENT, exp);
        os.setSample(sample1);

        //create sample2 without exp group
        this.sample2 = create(Sample.class, AbstractSample.PROP_SAMPLECATEGORIES, samplecategory);
        sample2.setIsActive(true);
        sample2.setAssignTo(assignTo);
        Experiment exp2 = create(Experiment.class);
        OutputSample os2 = create(OutputSample.class, OutputSample.PROP_EXPERIMENT, exp2);
        os2.setSample(sample2);

        //create sample3 without os/exp group
        this.sample3 = create(Sample.class, AbstractSample.PROP_SAMPLECATEGORIES, samplecategory);
        sample3.setIsActive(true);
        sample3.setAssignTo(assignTo);

        //create a nosie sample 
        create(Sample.class, AbstractSample.PROP_SAMPLECATEGORIES, samplecategory);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {

        if (null != this.wv) {
            if (this.wv.isCompleted()) {
                // can't tidy up
                return;
            }
            this.wv.abort(); // not testing persistence
        }
        super.tearDown();
    }

}
