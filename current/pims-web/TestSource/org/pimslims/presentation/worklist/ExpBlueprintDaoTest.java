package org.pimslims.presentation.worklist;

import java.util.List;

import junit.framework.Assert;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.test.AbstractTestCase;

@Deprecated
// obsolete
public class ExpBlueprintDaoTest extends AbstractTestCase {

    public ExpBlueprintDaoTest() {
        super("ExpBlueprintDao Test");
    }

    final String milestoneCodeName1 = "PCR" + System.currentTimeMillis();

    final String milestoneCodeName2 = "Clone" + System.currentTimeMillis();

    public void testExpBlueprint_readyForNext() throws AccessException, ConstraintException {

        this.wv = this.getWV();
        try {
            // prepare testing data
            final ResearchObjective expb =
                ExpBlueprintDaoTest.createExpBlueprint(this.wv, null, "OK", this.milestoneCodeName1);
            final ResearchObjective expb2 =
                ExpBlueprintDaoTest.createExpBlueprint(this.wv, null, "Failed", this.milestoneCodeName1);
            ExpBlueprintDaoTest.createExpBlueprint(this.wv, null, "Failed", null);
            // check results
            // ready for next
            final List<ConstructProgressBean> expbs =
                ExpBlueprintDao.getNoProgressExpBlueprint(this.wv, 0, true, null, 10);
            Assert.assertTrue(expbs.size() >= 1);
            Assert.assertTrue(this.containsExpb(expb, expbs));
            Assert.assertFalse(this.containsExpb(expb2, expbs));
            // not ready
            final List<ConstructProgressBean> expbs2 =
                ExpBlueprintDao.getNoProgressExpBlueprint(this.wv, 0, false, null, 10);
            Assert.assertTrue(expbs2.size() >= 1);
            Assert.assertTrue(this.containsExpb(expb2, expbs2));
            Assert.assertFalse(this.containsExpb(expb, expbs2));
            // all
            final List<ConstructProgressBean> expbs3 =
                ExpBlueprintDao.getNoProgressExpBlueprint(this.wv, 0, null, null, 10);
            Assert.assertTrue(expbs3.size() >= 2);
            Assert.assertTrue(this.containsExpb(expb, expbs3));
            Assert.assertTrue(this.containsExpb(expb2, expbs3));
            // time
            final List<ConstructProgressBean> expbs4 =
                ExpBlueprintDao.getNoProgressExpBlueprint(this.wv, 1, null, null, 10);
            Assert.assertFalse(this.containsExpb(expb, expbs4));
            Assert.assertFalse(this.containsExpb(expb2, expbs4));
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    public void testExpBlueprint_milestone() throws AccessException, ConstraintException {
        this.wv = this.getWV();
        try {
            // prepare testing data
            final ResearchObjective expb1 =
                ExpBlueprintDaoTest.createExpBlueprint(this.wv, null, "OK", this.milestoneCodeName1);

            ResearchObjective expb2 =
                ExpBlueprintDaoTest.createExpBlueprint(this.wv, null, "Failed", this.milestoneCodeName2);
            expb2 = ExpBlueprintDaoTest.createExpBlueprint(this.wv, expb2, "Failed", this.milestoneCodeName1);

            ResearchObjective expb3 =
                ExpBlueprintDaoTest.createExpBlueprint(this.wv, null, "Failed", this.milestoneCodeName2);
            expb3 = ExpBlueprintDaoTest.createExpBlueprint(this.wv, expb3, "OK", this.milestoneCodeName2);

            ResearchObjective expb4 =
                ExpBlueprintDaoTest.createExpBlueprint(this.wv, null, "Failed", this.milestoneCodeName1);
            expb4 = ExpBlueprintDaoTest.createExpBlueprint(this.wv, expb4, "OK", this.milestoneCodeName2);

            final ResearchObjective expb5 = this.create(ResearchObjective.class);
            // check results
            // last milestone is milestoneCodeName1
            List<ConstructProgressBean> expbs =
                ExpBlueprintDao.getNoProgressExpBlueprint(this.wv, 0, null, this.milestoneCodeName1, 10);
            Assert.assertTrue(this.containsExpb(expb1, expbs));
            Assert.assertTrue(this.containsExpb(expb2, expbs));
            Assert.assertFalse(this.containsExpb(expb5, expbs));
            Assert.assertFalse(this.containsExpb(expb3, expbs));
            Assert.assertFalse(this.containsExpb(expb4, expbs));

            // last milestone is milestoneCodeName1 and result is ok
            expbs = ExpBlueprintDao.getNoProgressExpBlueprint(this.wv, 0, true, this.milestoneCodeName1, 10);
            Assert.assertTrue(this.containsExpb(expb1, expbs));
            Assert.assertFalse(this.containsExpb(expb2, expbs));
            Assert.assertFalse(this.containsExpb(expb3, expbs));
            Assert.assertFalse(this.containsExpb(expb4, expbs));
            Assert.assertFalse(this.containsExpb(expb5, expbs));
            // last milestone is milestoneCodeName2
            expbs = ExpBlueprintDao.getNoProgressExpBlueprint(this.wv, 0, null, this.milestoneCodeName2, 10);
            Assert.assertFalse(this.containsExpb(expb1, expbs));
            Assert.assertFalse(this.containsExpb(expb2, expbs));
            Assert.assertTrue(this.containsExpb(expb3, expbs));
            Assert.assertTrue(this.containsExpb(expb4, expbs));
            Assert.assertFalse(this.containsExpb(expb5, expbs));
            // all
            expbs = ExpBlueprintDao.getNoProgressExpBlueprint(this.wv, 0, null, null, 10);
            Assert.assertTrue(this.containsExpb(expb1, expbs));
            Assert.assertTrue(this.containsExpb(expb2, expbs));
            Assert.assertTrue(this.containsExpb(expb3, expbs));
            Assert.assertTrue(this.containsExpb(expb4, expbs));
            Assert.assertFalse(this.containsExpb(expb5, expbs));
            // time
            expbs = ExpBlueprintDao.getNoProgressExpBlueprint(this.wv, 1, true, this.milestoneCodeName1, 10);
            Assert.assertFalse(this.containsExpb(expb1, expbs));
            Assert.assertFalse(this.containsExpb(expb2, expbs));
            Assert.assertFalse(this.containsExpb(expb3, expbs));
            Assert.assertFalse(this.containsExpb(expb4, expbs));
            Assert.assertFalse(this.containsExpb(expb5, expbs));
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    private boolean containsExpb(final ResearchObjective expb, final List<ConstructProgressBean> expbs) {
        int count = 0;
        for (final ConstructProgressBean eb : expbs) {
            if (eb.getConstructHook().equals(expb.get_Hook())) {
                count++;
            }
        }
        Assert.assertTrue(
            "Found more than one ResearchObjective in collection which hook=" + expb.get_Hook(), count <= 1);
        if (count == 1) {
            return true;
        } else {
            return false;
        }

    }

    static ResearchObjective createExpBlueprint(final WritableVersion wv, ResearchObjective expb,
        final String expStatus, final String milestoneCodeName) throws AccessException, ConstraintException {
        if (expb == null) {
            expb = AbstractTestCase.create(wv, ResearchObjective.class);
        }
        final Experiment exp =
            AbstractTestCase.create(wv, Experiment.class, Experiment.PROP_STATUS, expStatus);

        TargetStatus milestoneCode;
        if (milestoneCodeName != null) {
            milestoneCode = wv.findFirst(TargetStatus.class, TargetStatus.PROP_NAME, milestoneCodeName);
            if (milestoneCode == null) {
                milestoneCode =
                    AbstractTestCase
                        .create(wv, TargetStatus.class, TargetStatus.PROP_NAME, milestoneCodeName);
            }
        } else {
            milestoneCode = AbstractTestCase.create(wv, TargetStatus.class);
        }
        final Milestone milestone =
            AbstractTestCase.create(wv, Milestone.class, Milestone.PROP_STATUS, milestoneCode);
        milestone.setDate(java.util.Calendar.getInstance());
        milestone.setExperiment(exp);
        exp.setProject(expb);
        Assert.assertEquals(expStatus, exp.getStatus());
        Assert.assertTrue(expb.getMilestones().contains(milestone));

        return expb;

    }
}
