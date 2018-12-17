package org.pimslims.lab;

import java.util.Collection;

import junit.framework.Assert;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.CrystallizationUtility.ScoreBean;
import org.pimslims.lab.CrystallizationUtility.ScoreLimitation;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.Sample;
import org.pimslims.test.AbstractTestCase;

/**
 * Test the support for scores.
 * 
 * Note that there should be a reference protocol for crystallization, including an input sample called
 * "protein"
 * 
 * 
 * @author cm65
 * 
 */
public class CrystallizationUtilityTest extends AbstractTestCase {

    private static final String SAMPLE_NAME = "proteinSample" + System.currentTimeMillis();

    private static final String PLATE_NAME = "plate" + System.currentTimeMillis();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(CrystallizationUtilityTest.class);
    }

    private final AbstractModel model;

    public CrystallizationUtilityTest(final String methodName) {
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

    /*
     * Test method for 'org.pimslims.lab.CrystallizationUtility.getScoresFromTrial(ReadableVersion, String,
     * String, ScoreLimitation)'
     */
    public void testNoSuchTrial() {
        final ReadableVersion version =
            this.model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final Collection<ScoreBean> beans =
                CrystallizationUtility.getScoresFromTrial(version, "nonesuch", "sample", null);
            Assert.assertNotNull(beans);
            Assert.fail("no such trial");
        } catch (final IllegalArgumentException ex) {
            // that's fine
        } finally {
            version.abort();
        }
    }

    /*
     * Test method for 'org.pimslims.lab.CrystallizationUtility.getScoresFromTrial(ReadableVersion, String,
     * String, ScoreLimitation)'
     */
    public void testNoSuchSample() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final String trialName = "trial" + System.currentTimeMillis();
            new ExperimentGroup(version, trialName, "cu");
            CrystallizationUtility.getScoresFromTrial(version, trialName, "nonesuch", null);
            Assert.fail("no such sample");
        } catch (final IllegalArgumentException ex) {
            // that's fine
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

    /*
     * Test method for 'org.pimslims.lab.CrystallizationUtility.getScoresFromTrial(ReadableVersion, String,
     * String, ScoreLimitation)'
     */
    public void testGetPlateName() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final String trialName = this.makeCrystallizationExperiment(version);

            final Collection<ScoreBean> beans =
                CrystallizationUtility.getScoresFromTrial(version, trialName,
                    CrystallizationUtilityTest.SAMPLE_NAME, null);
            Assert.assertEquals(1, beans.size());
            final ScoreBean bean = beans.iterator().next();
            Assert.assertEquals(CrystallizationUtilityTest.PLATE_NAME, bean.getPlateName());

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {

            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

    private String makeCrystallizationExperiment(final WritableVersion version) throws ConstraintException,
        AccessException {
        final String trialName = "trial" + System.currentTimeMillis();
        final ExperimentGroup trial = new ExperimentGroup(version, trialName, "cu");
        final Sample sample = new Sample(version, CrystallizationUtilityTest.SAMPLE_NAME);
        final ExperimentType type = new ExperimentType(version, "cu" + System.currentTimeMillis());
        final Holder holder =
            new Holder(version, CrystallizationUtilityTest.PLATE_NAME, new HolderType(version,
                AbstractTestCase.UNIQUE));

        // add an experiment
        final Experiment experiment =
            AbstractTestCase.create(version, Experiment.class, Experiment.PROP_EXPERIMENTTYPE, type);
        trial.addExperiment(experiment);
        final InputSample is = new InputSample(version, experiment);
        is.setName("protein");
        is.setSample(sample);

        // LATER this bit is odd: the DM associates a plate with the experiment
        // only via an output sample
        final OutputSample os = new OutputSample(version, experiment);
        final Sample out = new Sample(version, experiment.getName() + " well");
        os.setSample(out);
        out.setHolder(holder);

        // set the score
        final Parameter score = new Parameter(version, experiment);
        score.setName("score");
        score.setValue("3");
        final Parameter method = new Parameter(version, experiment);
        method.setName("method");
        method.setValue("method");
        final Parameter schema = new Parameter(version, experiment);
        schema.setName("schema");
        schema.setValue("schema");

        return trialName;
    }

    /*
     * Test method for 'org.pimslims.lab.CrystallizationUtility.getScoresFromTrial(ReadableVersion, String,
     * String, ScoreLimitation)'
     */
    public void testFilterBySample() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final String trialName = this.makeCrystallizationExperiment(version);

            final String otherSampleName = "otherSample" + System.currentTimeMillis();
            new Sample(version, otherSampleName);
            final Collection<ScoreBean> beans =
                CrystallizationUtility.getScoresFromTrial(version, trialName, otherSampleName, null);
            Assert.assertEquals(0, beans.size());

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

    public void testGetScore() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final String trialName = this.makeCrystallizationExperiment(version);

            final Collection<ScoreBean> beans =
                CrystallizationUtility.getScoresFromTrial(version, trialName,
                    CrystallizationUtilityTest.SAMPLE_NAME, null);
            Assert.assertEquals(1, beans.size());
            final ScoreBean bean = beans.iterator().next();
            Assert.assertEquals(3, bean.getValue());
            Assert.assertEquals("method", bean.getMethod());
            Assert.assertEquals("schema", bean.getSchema());

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

    public void testFilterByScore() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final String trialName = this.makeCrystallizationExperiment(version);

            final ScoreLimitation filter = new ScoreLimitation() {
                public boolean isOfInterest(final int score) {
                    return false;
                }
            };
            final Collection<ScoreBean> beans =
                CrystallizationUtility.getScoresFromTrial(version, trialName,
                    CrystallizationUtilityTest.SAMPLE_NAME, filter);
            Assert.assertEquals(0, beans.size());

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

}
