/**
 * 
 */
package uk.ac.sspf.spot.pims;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ConstructMileStoneUtil;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.people.Person;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.reference.WorkflowItem;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;

import uk.ac.sspf.spot.beans.ConstructFastaBean;

/**
 * @author cm65
 * 
 */
@Deprecated
//we are seeking to retire all the code this tests
public class ConstructFastaTest extends TestCase {

    /**
     * 
     */
    protected static final String SEQ_WWW = "WWW";

    protected static final String CONSTRUCT_NAME = "construct" + System.currentTimeMillis();

    static final String PROTEIN_NAME = "protein";

    static final String TARGET_NAME = "test" + System.currentTimeMillis();

    static final Map<String, Object> FINAL_PROTEIN = new HashMap<String, Object>();
    static {
        ConstructFastaTest.FINAL_PROTEIN.put(ComponentCategory.PROP_NAME, "Final Protein");
    }

    protected static final java.util.Date DATE = new Date();

    public static final String SCIENTIST = "person" + System.currentTimeMillis();

    protected final AbstractModel model;

    /**
     * @param methodName
     */
    public ConstructFastaTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for
     * {@link uk.ac.sspf.spot.pims.ConstructFasta#getProgressList(java.util.Collection, org.pimslims.servlet.utils.ProgressListener)}
     * .
     */
    public void testNoTargets() {
        final ReadableVersion version =
            this.model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final ConstructFasta cf = new ConstructFasta(version, null);
            final Collection<ConstructFastaBean> progress = cf.getProgressList(Collections.EMPTY_LIST, null);
            Assert.assertEquals(0, progress.size());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

    public void testOneTarget() throws AccessException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Target target = ConstructFastaTest.createTarget(version);

            final ConstructFasta cf = new ConstructFasta(version, null);
            final Collection<ConstructFastaBean> progress =
                cf.getProgressList(Collections.singleton(target), null);
            Assert.assertEquals(1, progress.size());
            final ConstructFastaBean bean = progress.iterator().next();
            Assert.assertEquals(ConstructFastaTest.SEQ_WWW, bean.getSequence());
            Assert.assertEquals(">" + ConstructFastaTest.CONSTRUCT_NAME + " - ", bean.getDescription());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

    public static Target createTarget(final WritableVersion version) throws ConstraintException,
        AccessException {
        return ConstructFastaTest.createTarget(version, "", "OK", ConstructFastaTest.DATE);
    }

    public static Target createTarget(final WritableVersion version, final String suffix,
        final String result, final Date date2) throws ConstraintException, AccessException {
        final Molecule protein =
            new Molecule(version, "protein", ConstructFastaTest.PROTEIN_NAME + System.currentTimeMillis()
                + suffix);
        final Target target = new Target(version, ConstructFastaTest.TARGET_NAME + suffix, protein);
        target.setName("common" + System.currentTimeMillis());
        final ResearchObjective bp =
            new ResearchObjective(version, ConstructFastaTest.CONSTRUCT_NAME + suffix, "test");
        final ResearchObjectiveElement bpc =
            new ResearchObjectiveElement(version, ConstructFasta.SPOTCONSTRUCT_COMPONENT_TYPE, "test", bp);
        bpc.setTarget(target);
        Molecule finalProtein =
            version.findFirst(Molecule.class, Substance.PROP_NAME, bp.getCommonName()
                + " Final Protein" + suffix);
        if (finalProtein == null) {
            finalProtein = new Molecule(version, "protein", bp.getCommonName() + " Final Protein" + suffix);
        }
        final ComponentCategory cc =
            version.findFirst(ComponentCategory.class, ConstructFastaTest.FINAL_PROTEIN);
        Assert.assertNotNull("reference data missing", cc);
        finalProtein.addCategory(cc);
        finalProtein.setSequence(ConstructFastaTest.SEQ_WWW);
        bpc.addTrialMolComponent(finalProtein);

        // set a milestone, this method is also used to test other reports
        new Person(version, ConstructFastaTest.SCIENTIST);
        final TargetStatus code = new TargetStatus(version, "status" + System.currentTimeMillis() + suffix);
        final ExperimentType type = new ExperimentType(version, "type" + System.currentTimeMillis() + suffix);
        final WorkflowItem wfi = new WorkflowItem(version, type);
        wfi.setStatus(code);
        if ("OK".equals(result)) {
            ConstructMileStoneUtil.createMilestone(version, bp, target.get_Hook(), type, null, date2, "test");
        }
        /* was
        Milestone milestone = new Milestone(version, new Timestamp(DATE.getTime()), code, target);
        bp.addMilestone(milestone);
        Timestamp now = new Timestamp(date2.getTime());
        Experiment experiment = new Experiment(version, "exp" + System.currentTimeMillis(), now, now, type);
        experiment.setStatus(result);
        milestone.setExperiment(experiment);
        experiment.setCreator(scientist); */

        return target;
    }

}
