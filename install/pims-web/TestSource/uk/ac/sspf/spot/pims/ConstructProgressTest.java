/**
 * 
 */
package uk.ac.sspf.spot.pims;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.leeds.TargetUtility;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.worklist.ConstructProgressBean;
import org.pimslims.test.AbstractTestCase;

import uk.ac.sspf.spot.beans.ConstructFilterBean;

/**
 * @author cm65
 * 
 */
@Deprecated
// we are seeking to retire all the code this tests
public class ConstructProgressTest extends AbstractTestCase {

    private static final String SPECIES = "species" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * @param methodName
     */
    public ConstructProgressTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for {@link uk.ac.sspf.spot.pims.ExperimentSummary#getProgressList()}.
     */
    public void testEmptyProgressList() {
        final ReadableVersion version =
            this.model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final ConstructProgress summary = new ConstructProgress(version, null);
            final Collection<ConstructProgressBean> progress =
                summary.getProgressList(Collections.EMPTY_SET, null);
            Assert.assertEquals(0, progress.size());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

/*
    public void _testPerformance() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Collection<Target> targets = ConstructProgress.getAll(version);
            final ConstructProgress summary = new ConstructProgress(version, null);
            final org.hibernate.stat.Statistics stats = HibernateUtil.getStatistics();
            final long before = stats.getCloseStatementCount();
            System.out.println(System.currentTimeMillis());
            final Collection<ConstructProgressBean> progress = summary.getProgressList(targets, null);
            System.out.println("Found " + progress.size());
            System.out.println("getProgressList using " + (stats.getCloseStatementCount() - before)
                + " sql Statement");
            System.out.println(System.currentTimeMillis());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    } */

    public void testProgressList() throws AccessException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Target target = ConstructFastaTest.createTarget(version);
            final ConstructProgress summary = new ConstructProgress(version, null);
            final Collection<ConstructProgressBean> progress =
                summary.getProgressList(Collections.singleton(target), null);
            Assert.assertEquals(1, progress.size());
            final ConstructProgressBean bean = progress.iterator().next();
            Assert.assertEquals(ConstructFastaTest.CONSTRUCT_NAME, bean.getConstructId());
            Assert.assertEquals(ConstructFastaTest.DATE, bean.getDateOfExperimentDate());
            Assert.assertEquals(target.getName(), bean.getTargetName());
            Assert.assertEquals(0, bean.getDaysAgo());

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

    public void testOrganism() throws AccessException, IOException, ConstraintException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Target target = ConstructFastaTest.createTarget(version);
            target.setSpecies(new Organism(version, ConstructProgressTest.SPECIES));

            final Target target2 =
                ConstructFastaTest.createTarget(version, "org", "Failed", ConstructFastaTest.DATE);
            final Set<Target> targets = new HashSet<Target>();
            targets.add(target);
            targets.add(target2);

            final ConstructProgress summary = new ConstructProgress(version, null);
            final ConstructFilterBean filter = new ConstructFilterBean();
            filter.setOrganism(ConstructProgressTest.SPECIES);
            summary.setFilter(filter);
            final Collection<ConstructProgressBean> progress = summary.getProgressList(targets, null);
            Assert.assertEquals(1, progress.size());
            final ConstructProgressBean bean = progress.iterator().next();
            Assert.assertEquals(ConstructFastaTest.CONSTRUCT_NAME, bean.getConstructId());
            Assert.assertEquals(ConstructFastaTest.DATE, bean.getDateOfExperimentDate());
            Assert.assertEquals(target.getName(), bean.getTargetName());
            Assert.assertEquals(ConstructProgressTest.SPECIES, bean.getOrganism());

        } finally {
            version.abort();
        }
    }

    public void testMilestoneFilter() throws AccessException, InterruptedException, ConstraintException,
        IOException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final String milestoneName1 = "milestoneName1" + System.currentTimeMillis();
            final String milestoneName2 = "milestoneName2" + System.currentTimeMillis();
            final Target target = ConstructFastaTest.createTarget(version);
            final Target target2 =
                ConstructFastaTest.createTarget(version, "mfTwo2", "Failed", ConstructFastaTest.DATE);
            ConstructProgressTest.addTargetMilestone(version, target, milestoneName1);
            ConstructProgressTest.addTargetMilestone(version, target, milestoneName2);
            ConstructProgressTest.addTargetMilestone(version, target2, milestoneName2);
            ConstructProgressTest.addTargetMilestone(version, target2, milestoneName1);

            final ConstructProgress summary = new ConstructProgress(version, null);
            final ConstructFilterBean filter = new ConstructFilterBean();
            filter.setMilestone(milestoneName2);
            summary.setFilter(filter);

            final Set<Target> targets = new HashSet<Target>();
            targets.add(target);
            targets.add(target2);
            final Collection<ConstructProgressBean> progress = summary.getProgressList(targets, null);
            Assert.assertEquals(1, progress.size());
            final ConstructProgressBean bean = progress.iterator().next();
            Assert.assertEquals(target.get_Hook(), bean.getTargetId());

        } finally {
            version.abort();
        }
    }

    private static void addTargetMilestone(final WritableVersion version, final Target target,
        final String milestoneName) throws AccessException, ConstraintException, InterruptedException {
        final Collection<ResearchObjective> bps = TargetUtility.getTargetExpBlueprint(target);
        Assert.assertNotNull(bps);
        Assert.assertTrue(bps.size() > 0);
        final ResearchObjective bp = bps.iterator().next();
        Thread.sleep(111);
        TargetStatus code = version.findFirst(TargetStatus.class, TargetStatus.PROP_NAME, milestoneName);
        if (code == null) {
            code = new TargetStatus(version, milestoneName);
        }
        final Milestone milestone = new Milestone(version, java.util.Calendar.getInstance(), code, target);
        final ExperimentType type = new ExperimentType(version, "type" + System.currentTimeMillis());
        final Calendar now = java.util.Calendar.getInstance();
        final Experiment experiment =
            new Experiment(version, "exp" + System.currentTimeMillis(), now, now, type);
        experiment.setProject(bp);
        experiment.setStatus("OK");
        milestone.setExperiment(experiment);
    }

    public void testResultFilter() throws AccessException, ConstraintException, IOException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Target target = ConstructFastaTest.createTarget(version);
            final Target target2 =
                ConstructFastaTest.createTarget(version, "Two", "Failed", ConstructFastaTest.DATE);
            final Set<Target> targets = new HashSet<Target>();
            targets.add(target);
            targets.add(target2);

            final ConstructProgress summary = new ConstructProgress(version, null);
            final ConstructFilterBean filter = new ConstructFilterBean();
            filter.setResult(ConstructFilterBean.RESULT_OK);
            summary.setFilter(filter);
            final Collection<ConstructProgressBean> progress = summary.getProgressList(targets, null);
            Assert.assertEquals(1, progress.size());
            final ConstructProgressBean bean = progress.iterator().next();
            Assert.assertEquals(ConstructFastaTest.CONSTRUCT_NAME, bean.getConstructId());
            Assert.assertEquals(ConstructFastaTest.DATE, bean.getDateOfExperimentDate());
            Assert.assertEquals(target.getName(), bean.getTargetName());

        } finally {
            version.abort();
        }
    }

    public void testDaysAgoFilter() throws AccessException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Target target =
                ConstructFastaTest
                    .createTarget(version, "AgoOne", "OK", new Date(System.currentTimeMillis()));
            final Date yesterday = new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 25));
            final Target target2 = ConstructFastaTest.createTarget(version, "AgoTwo", "OK", yesterday);
            final Set<Target> targets = new HashSet<Target>();
            targets.add(target);
            targets.add(target2);

            final ConstructProgress summary = new ConstructProgress(version, null);
            final ConstructFilterBean filter = new ConstructFilterBean();
            filter.setDaysAgo(1);
            summary.setFilter(filter);
            final Collection<ConstructProgressBean> progress = summary.getProgressList(targets, null);
            Assert.assertEquals(1, progress.size());
            final ConstructProgressBean bean = progress.iterator().next();
            Assert.assertEquals(ConstructFastaTest.CONSTRUCT_NAME + "AgoTwo", bean.getConstructId());
            Assert.assertEquals(yesterday, bean.getDateOfExperimentDate());
            Assert.assertEquals(target2.getName(), bean.getTargetName());

        } catch (final ConstraintException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

}
