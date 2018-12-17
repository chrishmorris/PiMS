/**
 * 
 */
package org.pimslims.servlet.spot;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ConstructMileStoneUtil;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.reference.WorkflowItem;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.construct.ConstructBeanWriterTest;

/**
 * @author cm65
 * 
 */
public class SpotNewConstructMilestoneTest extends TestCase {

    private static final String COMMENTS = "comments";

    private final AbstractModel model;

    /**
     * @param methodName
     */
    public SpotNewConstructMilestoneTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.lab.ConstructMileStoneUtil#createMilestone(java.lang.String, java.lang.String, java.util.Date, org.pimslims.dao.WritableVersion, org.pimslims.model.target.ResearchObjective, org.pimslims.presentation.ConstructBean, org.pimslims.model.reference.ExperimentType)}
     * .
     */
    public final void testCreateMilestoneNoExperiment() {

        final WritableVersion version =
            this.model.getTestVersion();
        try {

            final Molecule protein = new Molecule(version, "protein", "test" + System.currentTimeMillis());
            final Target target = new Target(version, ConstructBeanWriterTest.TARGET_NAME, protein);
            final ResearchObjective construct =
                ConstructBeanWriterTest.createConstructBeanForTarget(version, target, new User(version,
                    ConstructBeanWriterTest.SCIENTIST), "", ConstructBeanWriterTest.FORWARD_EXTENSION);
            final ExperimentType type = new ExperimentType(version, "type" + System.currentTimeMillis());
            final WorkflowItem wfi = new WorkflowItem(version, type);
            wfi.setStatus(new TargetStatus(version, type.getName()));
            final Date date = new Date();
            final Milestone milestone =
                ConstructMileStoneUtil.createMilestone(version, construct, target.get_Hook(), type, null,
                    date, SpotNewConstructMilestoneTest.COMMENTS);
            Assert.assertEquals(date, milestone.getDate().getTime());
            Assert.assertEquals(construct, milestone.getResearchObjective());
            final Experiment experiment = milestone.getExperiment();
            Assert.assertEquals(construct, experiment.getProject());
            Assert.assertEquals(type, experiment.getExperimentType());
            Assert.assertEquals("OK", experiment.getStatus());
            final Set<Milestone> milestones = experiment.getMilestones();
            Assert.assertEquals(1, milestones.size());
            Assert.assertTrue(milestones.contains(milestone));
            Assert.assertEquals(SpotNewConstructMilestoneTest.COMMENTS, experiment.getDetails());

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.lab.ConstructMileStoneUtil#createMilestone(java.lang.String, java.lang.String, java.util.Date, org.pimslims.dao.WritableVersion, org.pimslims.model.target.ResearchObjective, org.pimslims.presentation.ConstructBean, org.pimslims.model.reference.ExperimentType)}
     * .
     */
    public final void testCreateMilestoneWithExperiment() {

        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Molecule protein = new Molecule(version, "protein", "test" + System.currentTimeMillis());
            final Target target = new Target(version, ConstructBeanWriterTest.TARGET_NAME, protein);
            final ResearchObjective construct =
                ConstructBeanWriterTest.createConstructBeanForTarget(version, target, new User(version,
                    ConstructBeanWriterTest.SCIENTIST), "", ConstructBeanWriterTest.FORWARD_EXTENSION);
            final ExperimentType type = new ExperimentType(version, "type" + System.currentTimeMillis());
            final WorkflowItem wfi = new WorkflowItem(version, type);
            wfi.setStatus(new TargetStatus(version, type.getName()));
            final Date date = new Date();
            final Calendar now = java.util.Calendar.getInstance();
            final Experiment experiment =
                new Experiment(version, "test" + System.currentTimeMillis(), now, now, type);
            final Milestone milestone =
                ConstructMileStoneUtil.createMilestone(version, construct, target.get_Hook(), type,
                    experiment, date, SpotNewConstructMilestoneTest.COMMENTS);
            Assert.assertEquals(date, milestone.getDate().getTime());
            Assert.assertEquals(construct, milestone.getResearchObjective());
            Assert.assertTrue(experiment.getMilestones().contains(milestone));
            Assert.assertEquals(experiment, milestone.getExperiment());
            Assert.assertEquals(SpotNewConstructMilestoneTest.COMMENTS, experiment.getDetails());

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {

        super.setUp();
        this.verifyTarget();
    }

    /**
     * 
     */
    private void verifyTarget() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Target t =
                version.findFirst(Target.class, Target.PROP_NAME, ConstructBeanWriterTest.TARGET_NAME);
            Assert.assertNull(t);
        } finally {
            version.abort(); // not testing persistence
        }
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
        this.verifyTarget();
    }

}
