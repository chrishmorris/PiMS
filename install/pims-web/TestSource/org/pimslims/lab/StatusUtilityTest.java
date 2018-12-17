package org.pimslims.lab;

import java.util.Calendar;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.reference.WorkflowItem;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.construct.ConstructBeanWriterTest;

public class StatusUtilityTest extends TestCase {

    private static final String NAME = "su" + System.currentTimeMillis();

    private static final Calendar NOW = java.util.Calendar.getInstance();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(StatusUtilityTest.class);
    }

    private final AbstractModel model;

    public StatusUtilityTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testNoTarget() {

        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType et = new ExperimentType(version, StatusUtilityTest.NAME);
            final TargetStatus status = new TargetStatus(version, StatusUtilityTest.NAME);
            final WorkflowItem workflowItem = new WorkflowItem(version, et);
            workflowItem.setStatus(status);
            final Experiment experiment =
                new Experiment(version, StatusUtilityTest.NAME, StatusUtilityTest.NOW, StatusUtilityTest.NOW,
                    et);
            final ResearchObjective eb = new ResearchObjective(version, StatusUtilityTest.NAME, "test");
            experiment.setProject(eb);

            StatusUtility.createMilestone(experiment);
            Assert.assertEquals(1, experiment.getMilestones().size());
            final Milestone milestone = experiment.getMilestones().iterator().next();
            Assert.assertEquals(experiment, milestone.getExperiment());
            Assert.assertEquals(eb, milestone.getResearchObjective());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final IllegalArgumentException e) {
            // that's fine
        } finally {
            version.abort();
        }
    }

    public void testTarget() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType et = new ExperimentType(version, StatusUtilityTest.NAME);
            final TargetStatus status = new TargetStatus(version, StatusUtilityTest.NAME);
            final WorkflowItem workflowItem = new WorkflowItem(version, et);
            workflowItem.setStatus(status);
            final Experiment experiment =
                new Experiment(version, StatusUtilityTest.NAME, StatusUtilityTest.NOW, StatusUtilityTest.NOW,
                    et);
            final Molecule protein = new Molecule(version, "protein", StatusUtilityTest.NAME);
            final Target target = new Target(version, StatusUtilityTest.NAME, protein);
            final ResearchObjective eb = new ResearchObjective(version, StatusUtilityTest.NAME, "test");
            experiment.setProject(eb);
            new ResearchObjectiveElement(version, "test", "test", eb).setTarget(target);

            StatusUtility.createMilestone(experiment);
            Assert.assertEquals(1, experiment.getMilestones().size());
            final Milestone milestone = experiment.getMilestones().iterator().next();
            Assert.assertEquals(experiment, milestone.getExperiment());
            Assert.assertEquals(eb, milestone.getResearchObjective());
            Assert.assertEquals(target, milestone.getTarget());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

    public void testConstruct() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType et = new ExperimentType(version, StatusUtilityTest.NAME);
            final TargetStatus status = new TargetStatus(version, StatusUtilityTest.NAME);
            final WorkflowItem workflowItem = new WorkflowItem(version, et);
            workflowItem.setStatus(status);
            final Experiment experiment =
                new Experiment(version, StatusUtilityTest.NAME, StatusUtilityTest.NOW, StatusUtilityTest.NOW,
                    et);
            final ResearchObjective eb = ConstructBeanWriterTest.createConstructBean(version);
            experiment.setProject(eb);

            StatusUtility.createMilestone(experiment);
            Assert.assertEquals(1, experiment.getMilestones().size());
            final Milestone milestone = experiment.getMilestones().iterator().next();
            Assert.assertEquals(experiment, milestone.getExperiment());
            Assert.assertEquals(eb, milestone.getResearchObjective());
            Assert.assertEquals(ConstructBeanWriterTest.TARGET_NAME, milestone.getTarget().getName());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

}
