package org.pimslims.presentation.worklist;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

public class SampleDAOTest extends AbstractTestCase {

    public SampleDAOTest() {
        super("SampleDAO Test");
    }

    SampleCriteria criteria; // the criteria to search samples

    @Override
    protected void setUp() throws Exception {

        super.setUp();
        this.resetCriteria();

    }

    private void resetCriteria() {
        this.criteria = new SampleCriteria();
        this.criteria.setAlreadyInUse(null);
        this.criteria.setDaysNoProgress(SampleCriteria.UNLIMITED);
        this.criteria.setLimit(10);
        this.criteria.setReadyForNext(null);
        this.criteria.setExpTypeNameReadyFor(null);
        this.criteria.setUserHookAssignedTo(null);
    }

    //TODO reinstate - currently fails on pims server
    public void xtestPerformance() throws AccessException, ConstraintException, InterruptedException {
        this.wv = this.getWV();
        try {
            // prepare testing data
            final String expTypeName1 = "expTypeName1" + System.currentTimeMillis();
            final String expTypeName2 = "expTypeName2" + System.currentTimeMillis();

            SampleDAOTest.createSampleWithNextExpType(this.wv, expTypeName1, "To_Be_Run");
            SampleDAOTest.createSampleWithNextExpType(this.wv, expTypeName2, "To_Be_Run");

            // search for sample ready for expTypeName1
            this.loadSampleBricks();
            this.messLoad(this.wv);
            this.loadSampleBricks();

        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    private void loadSampleBricks() {
        final Long begin = System.currentTimeMillis();
        //final org.hibernate.stat.Statistics stats = HibernateUtil.getStatistics();
        //final long before = stats.getCloseStatementCount();
        this.criteria = SampleCriteria.ASSIGNED_SAMPLE_SEARCH;
        this.checkPerformance();
        this.criteria = SampleCriteria.NEW_SAMPLE_SEARCH;
        this.checkPerformance();
        this.criteria = SampleCriteria.WEEK_OLD_SEARCH;
        this.checkPerformance();
        /*System.out.println("-Sample Bricks used " + (System.currentTimeMillis() - begin) / 1000.0 + "s, "
            + (stats.getCloseStatementCount() - before) + " sql Statement"); */
        // HibernateUtil.showStatisticDetails();
    }

    private void messLoad(final WritableVersion wv) {
        final Long begin = System.currentTimeMillis();
        final List<String> joinRoleNames = new LinkedList<String>();
        joinRoleNames.add(LabBookEntry.PROP_ATTACHMENTS);
        joinRoleNames.add(Target.PROP_RESEARCHOBJECTIVEELEMENTS);
        //joinRoleNames.add(Target.PROP_CITATIONS);
        joinRoleNames.add(LabBookEntry.PROP_CREATOR);
        joinRoleNames.add(Target.PROP_MILESTONES);
        joinRoleNames.add(Target.PROP_NUCLEICACIDS);
        joinRoleNames.add(Target.PROP_PROTEIN);
        //joinRoleNames.add(Target.PROP_PROJECTS);
        joinRoleNames.add(Target.PROP_SPECIES);
        joinRoleNames.add(LabBookEntry.PROP_ACCESS);
        wv.getAll(Target.class, joinRoleNames);
        final List<String> joinRoleNames2 = new LinkedList<String>();
        joinRoleNames.add(Experiment.PROP_PROJECT);
        joinRoleNames.add(Experiment.PROP_EXPERIMENTGROUP);
        joinRoleNames.add(Experiment.PROP_EXPERIMENTTYPE);
        joinRoleNames.add(Experiment.PROP_OUTPUTSAMPLES);
        joinRoleNames.add(Experiment.PROP_PROTOCOL);
        joinRoleNames.add(Experiment.PROP_PARAMETERS);
        joinRoleNames.add(LabBookEntry.PROP_ACCESS);
        wv.getAll(Experiment.class, joinRoleNames2);
        System.out.println("-Massload used " + (System.currentTimeMillis() - begin) / 1000.0 + "s ");
    }

    private void checkPerformance() {
        //final org.hibernate.stat.Statistics stats = HibernateUtil.getStatistics();
        //final Long begin = System.currentTimeMillis();
        //final long before = stats.getCloseStatementCount();
        //final List<SampleBean> sampleBeans = SampleDAO.getSampleBeanList(wv, criteria);
        // System.out.println(" SampleDao using
        // "+(stats.getCloseStatementCount()-before)+" sql Statement");
        // System.out.println(" SampleDao using
        // "+(System.currentTimeMillis()-begin)/1000.0+"s");
    }

    public void testSearchForSampleReadyForNext() throws AccessException, ConstraintException,
        InterruptedException {
        this.wv = this.getWV();
        try {
            // prepare testing data
            // sample with no experiment linked
            this.create(Sample.class);

            // sample is ready
            final Sample sample1 = SampleDAOTest.createSampleWithStatus(this.wv, "OK", null);

            // sample is not ready
            final Sample sample2 = SampleDAOTest.createSampleWithStatus(this.wv, "Failed", null);

            // check results
            // ready for next
            this.criteria.setReadyForNext(true);
            this.criteria.setLimit(10);
            final List<SampleBean> samples = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertTrue(samples.size() >= 1);

            Assert.assertTrue(SampleDAOTest.containsSample(samples, sample1));
            final SampleBean sampleBean = this.findSampleBean(samples, sample1);
            Assert.assertEquals("OK", sampleBean.getLastExpResult());
            Assert.assertNotNull(sampleBean.getLastExpHook());
            Assert.assertNotNull(sampleBean.getLastExpType());
            Assert.assertFalse(SampleDAOTest.containsSample(samples, sample2));
            // not ready
            this.criteria.setReadyForNext(false);
            final List<SampleBean> samples2 = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertTrue(samples2.size() >= 1);
            Assert.assertTrue(SampleDAOTest.containsSample(samples2, sample2));
            Assert.assertFalse(SampleDAOTest.containsSample(samples2, sample1));
            // all
            this.criteria.setReadyForNext(null);
            final List<SampleBean> samples3 = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertTrue(samples3.size() >= 2);
            Assert.assertTrue(SampleDAOTest.containsSample(samples3, sample2));
            Assert.assertTrue(SampleDAOTest.containsSample(samples3, sample1));
            Assert.assertEquals(sample2.getName(), this.findSampleBean(samples3, sample2).getSampleName());
            // sample created later should be list first
            this.criteria.setDaysNoProgress(1);
            final List<SampleBean> samples4 = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertFalse(SampleDAOTest.containsSample(samples4, sample2));
            Assert.assertFalse(SampleDAOTest.containsSample(samples4, sample1));
            // size
            this.criteria.setDaysNoProgress(0);
            this.criteria.setLimit(1);
            final List<SampleBean> samples5 = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertTrue(samples5.size() == 1);
            Assert.assertEquals(sample2.getName(), samples5.iterator().next().getSampleName());

            Assert.assertFalse(SampleDAOTest.containsSample(samples5, sample1));
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    public void testDaysNoProgress() throws AccessException, ConstraintException, InterruptedException {
        this.wv = this.getWV();
        try {
            // prepare testing data
            // sample with no experiment linked
            final Sample sample0 = this.create(Sample.class);

            // sample is ready
            final Sample sample1 = SampleDAOTest.createSampleWithStatus(this.wv, "OK", null);

            // sample is not ready
            final Sample sample2 = SampleDAOTest.createSampleWithStatus(this.wv, "Failed", null);

            // check all
            this.criteria.setDaysNoProgress(0);// 0 means all
            this.criteria.setLimit(10);
            List<SampleBean> sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertTrue(sampleBeans.size() >= 1);
            //assertTrue(containsSample(sampleBeans, sample0));
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample1));
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample2));
            // check orders&days
            this.criteria.setLimit(2);
            final Iterator<SampleBean> iter = sampleBeans.iterator();
            SampleBean sampleBean = iter.next();
            Assert.assertEquals(sample2.get_Hook(), sampleBean.getSampleHook());
            Assert.assertEquals(0, sampleBean.getNoProgressDays().intValue());

            sampleBean = iter.next();
            Assert.assertEquals(sample1.get_Hook(), sampleBean.getSampleHook());
            Assert.assertEquals(0, sampleBean.getNoProgressDays().intValue());

            // check before today
            this.criteria.setDaysNoProgress(200);
            this.criteria.setLimit(10);
            sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            for (final SampleBean sb : sampleBeans) {
                Assert.assertTrue(sb.getNoProgressDays() >= 200);
            }
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample0));
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample1));
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample2));
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    public void testReadyForExpType() throws AccessException, ConstraintException, InterruptedException {
        this.wv = this.getWV();

        try {
            // prepare testing data
            final String expTypeName1 = "expTypeName1" + System.currentTimeMillis();
            final String expTypeName2 = "expTypeName2" + System.currentTimeMillis();

            final Sample sample1 =
                SampleDAOTest.createSampleWithNextExpType(this.wv, expTypeName1, "To_be_run");
            final Sample sample2 =
                SampleDAOTest.createSampleWithNextExpType(this.wv, expTypeName2, "To_be_run");

            // search for sample ready for expTypeName1
            this.criteria.setLimit(10);
            this.criteria.setExpTypeNameReadyFor(expTypeName1);
            List<SampleBean> sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertTrue(sampleBeans.size() >= 1);
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample1));
            Assert.assertEquals(2, this.findSampleBean(sampleBeans, sample1).getNextExpTypes().size());
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample2));

            // search for sample ready for all
            // not ready
            this.criteria.setExpTypeNameReadyFor(null);
            sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertTrue(sampleBeans.size() >= 1);
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample1));
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample2));

            // search for sample ready for next or not
            this.criteria.setReadyForNext(true);
            sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample1));
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample2));
            this.criteria.setReadyForNext(false);
            sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample1));
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample2));

            // add status to sample
            SampleDAOTest.createSampleWithStatus(this.wv, "OK", sample1);
            SampleDAOTest.createSampleWithStatus(this.wv, "Failed", sample2);
            // ready for next
            this.criteria.setReadyForNext(true);
            sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample1));
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample2));

            // not ready for next
            this.criteria.setReadyForNext(false);
            sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample1));
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample2));

            // ready for expTypeName1
            this.criteria.setReadyForNext(true);
            this.criteria.setExpTypeNameReadyFor(expTypeName1);
            sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample2));
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample1));

            // ready for expTypeName1 and longer than 1 day
            this.criteria.setDaysNoProgress(1);
            sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample2));
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample1));
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    public void testReadyForNextWithFailedNextExp() throws AccessException, ConstraintException,
        InterruptedException {
        this.wv = this.getWV();

        try {

            // prepare testing data
            final String expTypeName1 = "expTypeName1" + System.currentTimeMillis();
            //sample 1 has no next exp
            final Sample sample1 = SampleDAOTest.createSampleWithNextExpType(this.wv, expTypeName1, "OK");
            sample1.setIsActive(true);
            //smple 2 has success next exp
            final Sample sample2 = SampleDAOTest.createSampleWithNextExpType(this.wv, expTypeName1, "OK");
            Experiment exp1 = this.create(Experiment.class);
            exp1.setStatus("OK");
            InputSample is1 = this.create(InputSample.class, InputSample.PROP_EXPERIMENT, exp1);
            is1.setSample(sample2);
            sample2.setIsActive(true);

            //sample3 has has failed next exp
            final Sample sample3 = SampleDAOTest.createSampleWithNextExpType(this.wv, expTypeName1, "OK");
            exp1 = this.create(Experiment.class);
            exp1.setStatus("Failed");
            is1 = this.create(InputSample.class, InputSample.PROP_EXPERIMENT, exp1);
            is1.setSample(sample3);
            sample3.setIsActive(true);

            //sample4 has has failed and success next exp
            final Sample sample4 = SampleDAOTest.createSampleWithNextExpType(this.wv, expTypeName1, "OK");
            exp1 = this.create(Experiment.class);
            exp1.setStatus("Failed");
            is1 = this.create(InputSample.class, InputSample.PROP_EXPERIMENT, exp1);
            is1.setSample(sample4);
            exp1 = this.create(Experiment.class);
            exp1.setStatus("OK");
            is1 = this.create(InputSample.class, InputSample.PROP_EXPERIMENT, exp1);
            is1.setSample(sample4);
            sample4.setIsActive(true);

            //sample5 has has failed and success next exp
            final Sample sample5 = SampleDAOTest.createSampleWithNextExpType(this.wv, expTypeName1, "OK");
            exp1 = this.create(Experiment.class);
            exp1.setStatus("To_be_run");
            is1 = this.create(InputSample.class, InputSample.PROP_EXPERIMENT, exp1);
            is1.setSample(sample5);
            exp1 = this.create(Experiment.class);
            exp1.setStatus("Failed");
            is1 = this.create(InputSample.class, InputSample.PROP_EXPERIMENT, exp1);
            is1.setSample(sample5);
            sample5.setIsActive(true);

            // search for new sample available
            this.criteria = SampleCriteria.NEW_SAMPLE_SEARCH;
            final List<SampleBean> sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertTrue(sampleBeans.size() >= 1);

            //sample has no next exp should be included
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample1));

            //sample has success next exp should not be included
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample2));

            //sample has failed next exp should  be included
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample3));

            //sample has failed and success next exp should not be included
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample4));

            //sample has failed and to_be_run next exp should not be included
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample5));

        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    public void testAlreadyInUse() throws AccessException, ConstraintException, InterruptedException {
        this.wv = this.getWV();
        try {
            // prepare testing data
            final String expTypeName1 = "expTypeName1" + System.currentTimeMillis();
            final String expTypeName2 = "expTypeName2" + System.currentTimeMillis();

            final Sample sample0 =
                SampleDAOTest.createSampleWithNextExpType(this.wv, expTypeName2, "To_be_run");
            // create sample which used in other 2 experiments
            final Sample sample1 =
                SampleDAOTest.createSampleWithNextExpType(this.wv, expTypeName1, "To_be_run");
            final Experiment exp1_1 = this.create(Experiment.class);
            final Experiment exp1_2 = this.create(Experiment.class);
            final InputSample is1_1 = this.create(InputSample.class, InputSample.PROP_EXPERIMENT, exp1_1);
            final InputSample is1_2 = this.create(InputSample.class, InputSample.PROP_EXPERIMENT, exp1_2);
            is1_1.setSample(sample1);
            is1_2.setSample(sample1);
            Assert.assertEquals(2, sample1.getInputSamples().size());

            // search for sample AlreadyInUse
            this.criteria.setAlreadyInUse(true);
            List<SampleBean> sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample0));
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample1));

            Assert.assertEquals(2, this.findSampleBean(sampleBeans, sample1).getExpsUsedIn().size());

            // not in use
            this.criteria.setAlreadyInUse(false);
            sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample1));
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample0));

            // all
            this.criteria.setAlreadyInUse(null);
            sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample1));
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample0));

            // add status to sample
            // sample already in used
            SampleDAOTest.createSampleWithStatus(this.wv, "OK", sample1);
            this.criteria.setReadyForNext(true);
            this.criteria.setAlreadyInUse(true);
            this.criteria.setExpTypeNameReadyFor(expTypeName1);
            sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample1));
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample0));

            // sample not in used
            SampleDAOTest.createSampleWithStatus(this.wv, "OK", sample0);
            this.criteria.setReadyForNext(true);
            this.criteria.setAlreadyInUse(false);
            this.criteria.setExpTypeNameReadyFor(expTypeName2);
            sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample1));
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample0));
            // active sample
            sample1.setIsActive(true);
            sample0.setIsActive(false);
            this.resetCriteria();
            this.criteria.setAlreadyInUse(null);
            this.criteria.setActive(true);
            sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample1));
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample0));
            Assert.assertTrue(sampleBeans.iterator().next().getActive());
            this.criteria.setActive(false);
            sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample1));
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample0));
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    private SampleBean findSampleBean(final List<SampleBean> sampleBeans, final Sample sample1) {
        for (final SampleBean sb : sampleBeans) {
            if (sb.getSampleHook().equals(sample1.get_Hook())) {
                return sb;
            }
        }
        return null;
    }

    public void testPersonAssignedto() throws AccessException, ConstraintException, InterruptedException {
        this.wv = this.getWV();
        try {
            // prepare testing data
            final String expTypeName1 = "expTypeName1" + System.currentTimeMillis();
            final String expTypeName2 = "expTypeName2" + System.currentTimeMillis();

            // create sample which is assigned to someone
            final Sample sample0 =
                SampleDAOTest.createSampleWithNextExpType(this.wv, expTypeName2, "To_be_run");
            final User p = this.create(User.class);
            sample0.setAssignTo(p);
            // create sample which used in other 2 experiments
            final Sample sample1 =
                SampleDAOTest.createSampleWithNextExpType(this.wv, expTypeName1, "To_be_run");
            final Experiment exp1_1 = this.create(Experiment.class);
            final Experiment exp1_2 = this.create(Experiment.class);
            final InputSample is1_1 = this.create(InputSample.class, InputSample.PROP_EXPERIMENT, exp1_1);
            final InputSample is1_2 = this.create(InputSample.class, InputSample.PROP_EXPERIMENT, exp1_2);
            is1_1.setSample(sample1);
            is1_2.setSample(sample1);
            Assert.assertEquals(2, sample1.getInputSamples().size());

            List<SampleBean> sampleBeans;
            // search for sample assigned to p
            this.resetCriteria();
            this.criteria.setUserHookAssignedTo(p.get_Hook());
            sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample1));
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample0));
            Assert.assertEquals(1, sampleBeans.size());
            Assert.assertEquals(p.get_Hook(), sampleBeans.iterator().next().getPersonHookAssignedTo());
            Assert.assertEquals(p.get_Name(), sampleBeans.iterator().next().getPersonNameAssignedTo());

            // search for sample AlreadyInUse
            this.resetCriteria();
            this.criteria.setAlreadyInUse(true);
            sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample0));
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample1));
            Assert.assertEquals(2, this.findSampleBean(sampleBeans, sample1).getExpsUsedIn().size());
            // search for sample AlreadyInUse and assigned to p
            this.resetCriteria();
            this.criteria.setAlreadyInUse(true);
            Assert.assertNull(this.criteria.getUserNameAssignedTo(this.wv));
            this.criteria.setUserHookAssignedTo(p.get_Hook());
            Assert.assertEquals(p.get_Name(), this.criteria.getUserNameAssignedTo(this.wv));
            sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample0));
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample1));
            // search for sample not AlreadyInUse and assigned to p
            this.resetCriteria();
            this.criteria.setUserHookAssignedTo(p.get_Hook());
            this.criteria.setAlreadyInUse(false);
            sampleBeans = SampleDAO.getSampleBeanList(this.wv, this.criteria);
            Assert.assertTrue(SampleDAOTest.containsSample(sampleBeans, sample0));
            Assert.assertFalse(SampleDAOTest.containsSample(sampleBeans, sample1));

        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    private static boolean containsSample(final List<SampleBean> sampleBeans, final Sample sample) {
        int count = 0;
        for (final SampleBean sb : sampleBeans) {
            if (sb.getSampleHook().equals(sample.get_Hook())) {
                count++;
            }
        }
        Assert.assertTrue("Found more than one sample in samplebeans which hook=" + sample.get_Hook(),
            count <= 1);
        if (count == 1) {
            return true;
        } else {
            return false;
        }

    }

    public void testGetNoProgressSamples_ReadyForNext() throws AccessException, ConstraintException,
        InterruptedException {
        this.wv = this.getWV();
        try {
            // prepare testing data
            // sample with no experiment linked
            this.create(Sample.class);

            // sample is ready
            final Sample sample1 = SampleDAOTest.createSampleWithStatus(this.wv, "OK", null);

            // sample is not ready
            final Sample sample2 = SampleDAOTest.createSampleWithStatus(this.wv, "Failed", null);

            // check results
            // ready for next
            final List<Sample> samples =
                SampleDAO.getNoProgressSamples(this.wv, 0, true, 10, null, null, null, null);
            Assert.assertTrue(samples.size() >= 1);
            Assert.assertTrue(samples.contains(sample1));
            Assert.assertFalse(samples.contains(sample2));
            // not ready
            final List<Sample> samples2 =
                SampleDAO.getNoProgressSamples(this.wv, 0, false, 10, null, null, null, null);
            Assert.assertTrue(samples2.size() >= 1);
            Assert.assertTrue(samples2.contains(sample2));
            Assert.assertFalse(samples2.contains(sample1));
            // all
            final List<Sample> samples3 =
                SampleDAO.getNoProgressSamples(this.wv, 0, null, 10, null, null, null, null);
            Assert.assertTrue(samples3.size() >= 2);
            Assert.assertTrue(samples3.contains(sample2));
            Assert.assertTrue(samples3.contains(sample1));
            Assert.assertEquals(sample2.getName(), samples3.iterator().next().getName());// sample created later
            // should be list first;
            // day
            final List<Sample> samples4 =
                SampleDAO.getNoProgressSamples(this.wv, 1, null, 10, null, null, null, null);
            Assert.assertFalse(samples4.contains(sample2));
            Assert.assertFalse(samples4.contains(sample1));
            // size
            final List<Sample> samples5 =
                SampleDAO.getNoProgressSamples(this.wv, 0, null, 1, null, null, null, null);
            Assert.assertTrue(samples5.size() == 1);
            Assert.assertEquals(sample2.getName(), samples5.iterator().next().getName());// sample created later
            // should be list first
            Assert.assertFalse(samples5.contains(sample1));
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    public void testGetNoProgressSamples_ReadyForExpType() throws AccessException, ConstraintException,
        InterruptedException {
        this.wv = this.getWV();
        try {
            // prepare testing data
            final String expTypeName1 = "expTypeName1" + System.currentTimeMillis();
            final String expTypeName2 = "expTypeName2" + System.currentTimeMillis();

            final Sample sample1 =
                SampleDAOTest.createSampleWithNextExpType(this.wv, expTypeName1, "To_be_run");
            final Sample sample2 =
                SampleDAOTest.createSampleWithNextExpType(this.wv, expTypeName2, "To_be_run");

            // search for sample ready for expTypeName1
            final List<Sample> samples =
                SampleDAO.getNoProgressSamples(this.wv, 0, null, 10, expTypeName1, null, null, null);
            Assert.assertTrue(samples.contains(sample1));
            Assert.assertFalse(samples.contains(sample2));

            // search for sample ready for all
            // not ready
            final List<Sample> samples2 =
                SampleDAO.getNoProgressSamples(this.wv, 0, null, 10, null, null, null, null);
            Assert.assertTrue(samples2.contains(sample2));
            Assert.assertTrue(samples2.contains(sample1));

            // search for sample ready for next or not
            final List<Sample> samples3 =
                SampleDAO.getNoProgressSamples(this.wv, 0, true, 10, null, null, null, null);
            Assert.assertFalse(samples3.contains(sample2));
            Assert.assertFalse(samples3.contains(sample1));
            final List<Sample> samples4 =
                SampleDAO.getNoProgressSamples(this.wv, 0, false, 10, null, null, null, null);
            Assert.assertTrue(samples4.contains(sample2));
            Assert.assertTrue(samples4.contains(sample1));

            // add status to sample
            SampleDAOTest.createSampleWithStatus(this.wv, "OK", sample1);
            SampleDAOTest.createSampleWithStatus(this.wv, "Failed", sample2);
            // ready for next
            final List<Sample> samples5 =
                SampleDAO.getNoProgressSamples(this.wv, 0, true, 10, null, null, null, null);
            Assert.assertFalse(samples5.contains(sample2));
            Assert.assertTrue(samples5.contains(sample1));
            // not ready for next
            final List<Sample> samples6 =
                SampleDAO.getNoProgressSamples(this.wv, 0, false, 10, null, null, null, null);
            Assert.assertTrue(samples6.contains(sample2));
            Assert.assertFalse(samples6.contains(sample1));
            // ready for expTypeName1
            final List<Sample> samples7 =
                SampleDAO.getNoProgressSamples(this.wv, 0, true, 10, expTypeName1, null, null, null);
            Assert.assertFalse(samples7.contains(sample2));
            Assert.assertTrue(samples7.contains(sample1));
            // ready for expTypeName1 and longer than 1 day
            final List<Sample> samples8 =
                SampleDAO.getNoProgressSamples(this.wv, 1, true, 10, expTypeName1, null, null, null);
            Assert.assertFalse(samples8.contains(sample2));
            Assert.assertFalse(samples8.contains(sample1));
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    public void testGetAssignedSampleIsOrdered() throws AccessException, ConstraintException,
        InterruptedException {
        this.wv = this.getWV();
        try {
            final User p = this.create(User.class);
            // prepare testing data
            // sample with no experiment linked
            final Sample sample0 = SampleDAOTest.createSampleWithStatus(this.wv, "OK", null);
            sample0.setAssignTo(p);
            sample0.setIsActive(true);

            // sample is ready
            final Sample sample1 = SampleDAOTest.createSampleWithStatus(this.wv, "OK", null);
            sample1.setAssignTo(p);
            sample1.setIsActive(true);
            // sample is not ready
            final Sample sample2 = SampleDAOTest.createSampleWithStatus(this.wv, "Failed", null);
            sample2.setAssignTo(p);
            sample2.setIsActive(true);
            // exps are created at same time
            final java.util.Calendar endDate = java.util.Calendar.getInstance();
            sample0.getOutputSample().getExperiment().setEndDate(endDate);
            sample1.getOutputSample().getExperiment().setEndDate(endDate);
            sample2.getOutputSample().getExperiment().setEndDate(endDate);

            // check results
            // ready for next
            final List<Sample> samples =
                SampleDAO.getNoProgressSamples(this.wv, 0, null, 10, null, null, true, p.get_Hook());
            Assert.assertTrue(samples.contains(sample0));
            Assert.assertTrue(samples.contains(sample1));
            Assert.assertTrue(samples.contains(sample2));
            // check order
            final Iterator it = samples.iterator();
            Assert.assertEquals(sample2.get_Hook(), ((Sample) it.next()).get_Hook());
            Assert.assertEquals(sample1.get_Hook(), ((Sample) it.next()).get_Hook());
            Assert.assertEquals(sample0.get_Hook(), ((Sample) it.next()).get_Hook());
            this.wv.abort();
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    public static Sample createSampleWithStatus(final WritableVersion wv, final String expStatus,
        final Sample existedSample) throws AccessException, ConstraintException, InterruptedException {
        Sample sample;
        if (existedSample != null) {
            sample = existedSample;
        } else {
            sample = AbstractTestCase.create(wv, Sample.class);
        }
        final Experiment exp =
            AbstractTestCase.create(wv, Experiment.class, Experiment.PROP_STATUS, expStatus);
        Thread.sleep(3);
        final java.util.Calendar startDate = java.util.Calendar.getInstance();
        startDate.setTimeInMillis(System.currentTimeMillis() - 10);
        exp.setStartDate(startDate);
        exp.setEndDate(java.util.Calendar.getInstance());
        final OutputSample os =
            AbstractTestCase.create(wv, OutputSample.class, OutputSample.PROP_EXPERIMENT, exp);
        os.setSample(sample);
        Assert.assertEquals(expStatus, exp.getStatus());
        Assert.assertEquals(exp, os.getExperiment());
        Assert.assertEquals(os, sample.getOutputSample());
        return sample;
    }

    /**
     * create a sample with have 2 refInputSample, one of them link with a protocol which exp
     * type=expTypeName, another is not
     * 
     * @param expTypeName
     * @return Sample
     * @throws AccessException
     * @throws ConstraintException
     * @throws InterruptedException
     */
    static Sample createSampleWithNextExpType(final WritableVersion wv, final String expTypeName,
        final String status) throws AccessException, ConstraintException, InterruptedException {
        // create a sample with a refSample

        final RefSample refSample = AbstractTestCase.create(wv, RefSample.class);
        final Sample sample = SampleDAOTest.createSampleWithStatus(wv, status, null);
        sample.setRefSample(refSample);
        final SampleCategory sc = POJOFactory.createSampleCategory(wv);
        sample.addSampleCategory(sc);

        // link this refSample with 2 protocols
        // first protocol's exp type="expType"
        ExperimentType expType = wv.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, expTypeName);
        if (expType == null) {
            expType =
                AbstractTestCase.create(wv, ExperimentType.class, ExperimentType.PROP_NAME, expTypeName);
        }
        final Protocol protocol1 =
            AbstractTestCase.create(wv, Protocol.class, Protocol.PROP_EXPERIMENTTYPE, expType);
        final RefInputSample refIS1 =
            AbstractTestCase.create(wv, RefInputSample.class, RefInputSample.PROP_PROTOCOL, protocol1);
        // the second protocol's exp type is not 'expType'
        final Protocol protocol2 = AbstractTestCase.create(wv, Protocol.class);
        final RefInputSample refIS2 =
            AbstractTestCase.create(wv, RefInputSample.class, RefInputSample.PROP_PROTOCOL, protocol2);
        // was refIS1.setRefSample(refSample);
        // was refIS2.setRefSample(refSample);
        refIS1.setSampleCategory(sc);
        refIS2.setSampleCategory(sc);

        // assert objects is correctly created
        Assert.assertEquals(expTypeName, protocol1.getExperimentType().getName());
        Assert.assertFalse(expTypeName.equalsIgnoreCase(protocol2.getExperimentType().getName()));
        Assert.assertEquals(refSample, sample.getRefSample());
        Assert.assertEquals(protocol1, refIS1.getProtocol());
        Assert.assertEquals(protocol2, refIS2.getProtocol());
        return sample;
    }

}
