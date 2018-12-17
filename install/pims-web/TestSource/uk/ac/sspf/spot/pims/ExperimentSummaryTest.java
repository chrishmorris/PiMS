/**
 * 
 */
package uk.ac.sspf.spot.pims;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.worklist.ConstructProgressBean;

import uk.ac.sspf.spot.beans.ConstructFilterBean;

/**
 * @author cm65
 * 
 */
@Deprecated
//we are seeking to retire all the code this tests
public class ExperimentSummaryTest extends TestCase {

    private static final String SPECIES = "species" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * @param methodName
     */
    public ExperimentSummaryTest(final String methodName) {
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
            final ExperimentSummary summary = new ExperimentSummary(version, null);
            final Collection<ConstructProgressBean> progress = summary.getProgressList(Collections.EMPTY_SET);
            Assert.assertEquals(0, progress.size());
        } finally {
            version.abort();
        }
    }

    public void testProgressList() throws AccessException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Target target = ConstructFastaTest.createTarget(version);
            final ExperimentSummary summary = new ExperimentSummary(version, null);
            final Collection<ConstructProgressBean> progress =
                summary.getProgressList(Collections.singleton(target));
            Assert.assertEquals(1, progress.size());
            final ConstructProgressBean bean = progress.iterator().next();
            Assert.assertEquals(ConstructFastaTest.CONSTRUCT_NAME, bean.getConstructId());
            Assert.assertEquals(ConstructFastaTest.DATE, bean.getDateOfExperimentDate());
            Assert.assertEquals(target.getName(), bean.getTargetName());

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

    public void _testPerformance() throws AccessException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final long start = System.currentTimeMillis();
            final java.util.List<String> rolesToJoin = new ArrayList<String>();
            rolesToJoin.add(Target.PROP_RESEARCHOBJECTIVEELEMENTS);
            rolesToJoin.add(Target.PROP_MILESTONES);
            final Collection<Target> targets =
                version.getAll(org.pimslims.model.target.Target.class, rolesToJoin);
            final ExperimentSummary summary = new ExperimentSummary(version, null);
            summary.getProgressList(targets);
            System.out.println((System.currentTimeMillis() - start) / 1000
                + "s used for summary.getProgressList with " + targets.size() + " targets");

        } finally {
            version.abort();
        }
    }

    public void testExtraBlueprint() throws AccessException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Target target = ConstructFastaTest.createTarget(version);
            final ExperimentSummary summary = new ExperimentSummary(version, null);
            final ResearchObjective bp =
                new ResearchObjective(version, "target" + System.currentTimeMillis(), "test");
            final ResearchObjectiveElement bpc = new ResearchObjectiveElement(version, "target", "test", bp);
            bpc.setTarget(target);
            final Collection<ConstructProgressBean> progress =
                summary.getProgressList(Collections.singleton(target));
            Assert.assertEquals(1, progress.size());
            final ConstructProgressBean bean = progress.iterator().next();
            Assert.assertEquals(ConstructFastaTest.CONSTRUCT_NAME, bean.getConstructId());

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

    public void testOrganism() throws AccessException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Target target = ConstructFastaTest.createTarget(version);
            target.setSpecies(new Organism(version, ExperimentSummaryTest.SPECIES));
            final Target target2 =
                ConstructFastaTest.createTarget(version, "Two", "Failed", ConstructFastaTest.DATE);
            final Set<Target> targets = new HashSet<Target>();
            targets.add(target);
            targets.add(target2);

            final ExperimentSummary summary = new ExperimentSummary(version, null);
            final ConstructFilterBean filter = new ConstructFilterBean();
            filter.setOrganism(ExperimentSummaryTest.SPECIES);
            summary.setFilter(filter);
            final Collection<ConstructProgressBean> progress = summary.getProgressList(targets);
            Assert.assertEquals(1, progress.size());
            final ConstructProgressBean bean = progress.iterator().next();
            Assert.assertEquals(ConstructFastaTest.CONSTRUCT_NAME, bean.getConstructId());
            Assert.assertEquals(ConstructFastaTest.DATE, bean.getDateOfExperimentDate());
            Assert.assertEquals(target.getName(), bean.getTargetName());
            Assert.assertEquals(ExperimentSummaryTest.SPECIES, bean.getOrganism());

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

}
