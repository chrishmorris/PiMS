package org.pimslims.presentation.construct;

import java.util.Calendar;

import junit.framework.Assert;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ConstructUtility;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.test.AbstractTestCase;

public class ConstructResultBeanTest extends AbstractTestCase {

    private static final String PROTEIN_NAME = "protein" + System.currentTimeMillis();

    private static final String TARGET_NAME = "target" + System.currentTimeMillis();

    private static final String CONSTRUCT_NAME = "construct" + System.currentTimeMillis();

    private static final Calendar DATE = java.util.Calendar.getInstance();

    private static final String STATUS_NAME = "status" + System.currentTimeMillis();

    private static final Calendar FIVE_MINUTES_AGO = Calendar.getInstance();
    static {
        ConstructResultBeanTest.FIVE_MINUTES_AGO.setTimeInMillis(ConstructResultBeanTest.FIVE_MINUTES_AGO
            .getTimeInMillis() - 1000L * 60 * 5);
    }

    private final AbstractModel model;

    public ConstructResultBeanTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    public final void testNoExperiment() throws AccessException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Milestone milestone = ConstructResultBeanTest.createMilestone(version);
            final ResearchObjective construct = milestone.getResearchObjective();
            ConstructBeanReader.readConstruct(construct);
            final ConstructResultBean bean = new ConstructResultBean(version, milestone);
            Assert.assertEquals(milestone, bean.getBlueprintStatus());
            Assert.assertEquals(milestone.get_Hook(), bean.getHook());
            Assert.assertEquals(construct.get_Hook(), bean.getConstructHook());
            Assert.assertEquals(construct.get_Hook(), bean.getConBean().getHook());
            Assert.assertEquals(construct.getCommonName(), bean.getName());
            Assert.assertEquals(construct, bean.getExpBlueprint());
            Assert.assertNotNull(bean.getExperimentHook());
            Assert.assertEquals(ConstructResultBeanTest.DATE.getTime(), bean.getDateOfExperiment().getTime());
            Assert.assertEquals(ConstructResultBeanTest.STATUS_NAME, bean.getMilestoneName());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testMilestoneAndExperiment() throws AccessException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Milestone milestone = ConstructResultBeanTest.createMilestone(version);
            final ResearchObjective construct = milestone.getResearchObjective();
            final ExperimentType type = new ExperimentType(version, "et" + System.currentTimeMillis());
            final Experiment experiment =
                new Experiment(version, "exp" + System.currentTimeMillis(), ConstructResultBeanTest.DATE,
                    ConstructResultBeanTest.DATE, type);
            experiment.setProject(construct);
            experiment.setDetails("details");
            milestone.setExperiment(experiment);

            final ConstructResultBean bean = new ConstructResultBean(version, milestone);
            Assert.assertEquals(milestone, bean.getBlueprintStatus());
            Assert.assertEquals(milestone.get_Hook(), bean.getHook());
            Assert.assertEquals(construct.get_Hook(), bean.getConstructHook());
            Assert.assertEquals(construct.get_Hook(), bean.getConBean().getHook());
            Assert.assertEquals(experiment.get_Hook(), bean.getExperimentHook());
            Assert.assertEquals(experiment.getDetails(), bean.getComments());
            Assert.assertEquals(ConstructResultBeanTest.DATE.getTime(), bean.getDateOfExperiment().getTime());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testNoMilestone() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final ResearchObjective construct =
                new ResearchObjective(version, ConstructResultBeanTest.CONSTRUCT_NAME, "test");
            new ResearchObjectiveElement(version, ConstructUtility.SPOTCONSTRUCT, "test", construct);

            final ExperimentType type = new ExperimentType(version, "et" + System.currentTimeMillis());
            final Experiment experiment =
                new Experiment(version, "exp" + System.currentTimeMillis(), ConstructResultBeanTest.DATE,
                    ConstructResultBeanTest.DATE, type);
            experiment.setProject(construct);
            experiment.setDetails("details");

            final ConstructResultBean bean =
                new ConstructResultBean(version, experiment, ConstructBeanReader.readConstruct(construct));
            Assert.assertNull(bean.getBlueprintStatus());
            Assert.assertEquals(construct.get_Hook(), bean.getConstructHook());
            Assert.assertEquals(construct.get_Hook(), bean.getConBean().getHook());
            Assert.assertEquals(experiment.get_Hook(), bean.getExperimentHook());
            Assert.assertEquals(experiment.getDetails(), bean.getComments());
            Assert.assertEquals(ConstructResultBeanTest.DATE.getTime(), bean.getDateOfExperiment().getTime());
            Assert.assertEquals(experiment.getName(), bean.getExperimentName());
            Assert.assertNull(bean.getMilestoneName());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testExperimentAndMilestone() throws AccessException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Milestone milestone = ConstructResultBeanTest.createMilestone(version);
            final Experiment experiment = this.createExperiment(version, milestone);

            final ResearchObjective construct = milestone.getResearchObjective();
            final ConstructResultBean bean =
                new ConstructResultBean(version, experiment, ConstructBeanReader.readConstruct(milestone
                    .getResearchObjective()));

            Assert.assertEquals(milestone, bean.getBlueprintStatus());
            Assert.assertEquals(milestone.get_Hook(), bean.getHook());
            Assert.assertEquals(construct.get_Hook(), bean.getConstructHook());
            Assert.assertEquals(construct.get_Hook(), bean.getConBean().getHook());
            Assert.assertEquals(construct.getCommonName(), bean.getName());
            Assert.assertEquals(construct, bean.getExpBlueprint());
            Assert.assertEquals(experiment.get_Hook(), bean.getExperimentHook());
            Assert.assertEquals(experiment.getDetails(), bean.getComments());
            Assert.assertEquals(ConstructResultBeanTest.DATE.getTime(), bean.getDateOfExperiment().getTime());
            // was Assert.assertEquals(1, bean.getFiles().size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testSort() throws AccessException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Milestone milestone1 = ConstructResultBeanTest.createMilestone(version);
            final Experiment experiment1 = this.createExperiment(version, milestone1);

            final ResearchObjective construct = milestone1.getResearchObjective();
            final ConstructResultBean bean1 =
                new ConstructResultBean(version, experiment1, ConstructBeanReader.readConstruct(construct));

            // now a previous milestone, but recorded later
            final Milestone milestone2 =
                ConstructResultBeanTest.doCreateMilestone(version, "two", construct,
                    ConstructResultBeanTest.FIVE_MINUTES_AGO);
            final Experiment experiment2 = this.createExperiment(version, milestone2);
            final ConstructResultBean bean2 =
                new ConstructResultBean(version, experiment2, ConstructBeanReader.readConstruct(construct));
            Assert.assertEquals("B".compareTo("A"), bean1.compareTo(bean2));

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    /**
     * ConstructResultBeanTest.createExperiment
     * 
     * @param version
     * @param milestone
     * @return
     * @throws ConstraintException
     */
    private Experiment createExperiment(final WritableVersion version, final Milestone milestone)
        throws ConstraintException {
        Experiment experiment;
        final ResearchObjective construct = milestone.getResearchObjective();
        final ExperimentType type = new ExperimentType(version, "et" + System.currentTimeMillis());
        experiment =
            new Experiment(version, "exp" + System.currentTimeMillis(), ConstructResultBeanTest.DATE,
                ConstructResultBeanTest.DATE, type);
        experiment.setProject(construct);
        experiment.setDetails("details");
        //was final Attachment file = POJOFactory.create(version, Attachment.class);
        //file.setDetails(ConstructResultBeanTest.FILENAME);
        //experiment.addAttachment(file);
        milestone.setExperiment(experiment);
        return experiment;
    }

    public static Milestone createMilestone(final WritableVersion version) throws ConstraintException,
        AccessException {
        final String suffix = "";
        final Molecule protein =
            new Molecule(version, "protein", ConstructResultBeanTest.PROTEIN_NAME + suffix);
        final Target target = new Target(version, ConstructResultBeanTest.TARGET_NAME + suffix, protein);
        target.setName("common" + System.currentTimeMillis());
        final ResearchObjective bp =
            new ResearchObjective(version, ConstructResultBeanTest.CONSTRUCT_NAME + suffix, "test");
        final ResearchObjectiveElement bpc =
            new ResearchObjectiveElement(version, ConstructUtility.SPOTCONSTRUCT, "test", bp);
        bpc.setTarget(target);
        final Molecule finalProtein =
            new Molecule(version, "protein", bp.getCommonName() + " Final Protein" + suffix);
        // ComponentCategory cc = version.findFirst(ComponentCategory.class,
        // FINAL_PROTEIN);
        // assertNotNull(cc);
        // finalProtein.addCategory(cc);
        // finalProtein.setSeqString("WWW");
        bpc.addTrialMolComponent(finalProtein);

        // set a milestone, this method is also used to test other reports
        Milestone milestone;
        final Calendar endDate = ConstructResultBeanTest.DATE;
        milestone = ConstructResultBeanTest.doCreateMilestone(version, suffix, bp, endDate);
        return milestone;
    }

    /**
     * ConstructResultBeanTest.doCreateMilestone
     * 
     * @param version
     * @param suffix
     * @param target
     * @param bp
     * @param endDate
     * @return
     * @throws ConstraintException
     * @throws AccessException
     */
    private static Milestone doCreateMilestone(final WritableVersion version, final String suffix,
        final ResearchObjective ro, final Calendar endDate) throws ConstraintException, AccessException {
        Milestone milestone;
        final TargetStatus code = new TargetStatus(version, ConstructResultBeanTest.STATUS_NAME + suffix);
        final Target target = ro.getResearchObjectiveElements().iterator().next().getTarget();
        milestone = new Milestone(version, endDate, code, target);
        final Experiment exp = AbstractTestCase.create(version, Experiment.class);
        exp.addMilestone(milestone);
        exp.setProject(ro);
        exp.setEndDate(endDate);
        return milestone;
    }

}
