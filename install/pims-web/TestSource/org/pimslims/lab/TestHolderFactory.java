/**
 * 
 */
package org.pimslims.lab;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.lab.protocol.ProtocolFactory;
import org.pimslims.lab.sample.SampleFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.test.POJOFactory;

/**
 * TODO fix this - the data model has changed
 * 
 * @author cm65
 * 
 */
public class TestHolderFactory extends TestCase {

    public static final String INPUT_SAMPLE_NAME = "input";

    private static final String UNIQUE = "hf" + System.currentTimeMillis();

    /**
     * The database being tested
     */
    private final AbstractModel model;

    /**
     * Sample category for test
     */
    private SampleCategory category;

    /**
     * The transaction to use for testing
     */
    private WritableVersion wv = null;

    /**
     * A protein sample to use for testing
     */
    private Sample proteinSample = null;

    /**
     * a plate type to use for testing
     */
    HolderType holderType = null;

    /**
     * A plate for testing
     */
    private Holder plate = null;

    private HolderCategory holderCategory;

    // private RefSample refSample;

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(TestHolderFactory.class);
    }

    /**
     * Constructor for TestHolderFactory.
     * 
     * @param methodName
     */
    public TestHolderFactory(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.wv = this.model.getTestVersion();
        Assert.assertNotNull(this.wv);

        this.category = ProtocolFactory.getSampleCategory(this.wv, "hf" + new Date());
        this.proteinSample =
            SampleFactory.createSample(this.wv, "hsample" + new Date().getTime(),
                Collections.singleton(this.category), Collections.EMPTY_MAP);
        Assert.assertNotNull(this.proteinSample);
        this.proteinSample.setAmountUnit("L");
        this.proteinSample.setCurrentAmount(1.0f); // 1L

        this.holderCategory =
            HolderFactory.createHolderCategory(this.wv, "hf" + System.currentTimeMillis(),
                Collections.EMPTY_MAP);
        final Set<HolderCategory> categories = new HashSet(Collections.singleton(this.holderCategory));
        this.holderType =
            HolderFactory.createHolderType(this.wv, "refHolder" + new java.util.Date().getTime(), categories,
                java.util.Collections.EMPTY_MAP);
        Assert.assertNotNull(this.holderType);
        this.holderType.setMaxRow(8);
        this.holderType.setMaxColumn(12);
        this.plate =
            HolderFactory.createHolder(this.wv, null, "hf" + System.currentTimeMillis(), this.holderType,
                java.util.Collections.EMPTY_MAP);
        Assert.assertNotNull(this.plate);
    }

    /*
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        if (this.wv.isCompleted()) {
            // can't tidy up
            return;
        }
        /*
         * TODO plate.delete(); proteinSample.delete(); this.category.delete(); this.holderCategory.delete(); //
         */
        if (null != this.wv) {
            /*
             * long startTime = new java.util.Date().getTime(); wv.commit(); // not testing persistence long
             * duration = new java.util.Date().getTime() - startTime; System.out.println("testExperimentGroup
             * commit took: "+duration+"ms");
             */
            this.wv.abort();
        }

        super.tearDown();
    }

    public void testGetColumn() {
        final int column = HolderFactory.getColumn("C2", this.holderType);
        Assert.assertEquals(2, column);
        try {
            HolderFactory.getColumn("2", null);
            Assert.fail("Bad well");
        } catch (final IllegalArgumentException e) {
            // that's good
        }
    }

    public void testGetRow() {
        final int row = HolderFactory.getRow("C");
        Assert.assertEquals(2, row);
        try {
            HolderFactory.getRow("3");
            Assert.fail("Bad well");
        } catch (final IllegalArgumentException e) {
            // that's good
        }
    }

    /*
     * Test methods that don't use experiments
     */
    public void test() throws ConstraintException {

        java.util.Collection positions = HolderFactory.getPositions(this.holderType);
        Assert.assertEquals("96 wells", 96, positions.size());
        positions = HolderFactory.getPositions(this.plate);
        Assert.assertEquals("96 wells", 96, positions.size());

        HolderFactory.positionSample(this.plate, this.proteinSample, "D3");
        Assert.assertEquals("row position", 4, this.proteinSample.getRowPosition().intValue());
        Assert.assertEquals("column position", 3, this.proteinSample.getColPosition().intValue());

    }

    /*
     * Test method for 'org.pimslims.lab.HolderFactory.getRefHolder(WritableVersion, String)' public void
     * testGetRefHolder() { }
     */

    /* */
    public void testExperimentGroup() {

        //final long startTime = new java.util.Date().getTime();
        try {
            final ExperimentGroup group = this.createExperimentGroup(this.plate);
            Assert.assertEquals("plate for group", this.plate, HolderFactory.getPlate(group));
            Assert.assertEquals("group for plate", group, HolderFactory.getExperimentGroup(this.plate));

            final Experiment first = (Experiment) group.findFirst(ExperimentGroup.PROP_EXPERIMENTS);
            Assert.assertNotNull(first);
            Assert.assertNotNull("position of: " + first.getName(), HolderFactory.getPositionInHolder(first));

            // check an experiment
            Assert.assertTrue("some experiments in group", 0 < group.getExperiments().size());
            final Experiment c3 = TestHolderFactory.getExperimentByPosition(group, "C3");
            Assert.assertNotNull("get by position", c3);
            Assert.assertEquals("experiment position", "C03", HolderFactory.getPositionInHolder(c3));

            // check output sample
            Assert.assertEquals(1, c3.getOutputSamples().size());
            final OutputSample os = c3.getOutputSamples().iterator().next();
            Assert.assertNotNull(os.getSample());
            Assert.assertEquals("output", os.getName());
            Assert.assertEquals("output samples are active", Boolean.TRUE, os.getSample().getIsActive());
            Assert.assertTrue(os.getSample().getSampleCategories().contains(this.category));

            // test input samples were created
            final Collection<InputSample> iss = first.getInputSamples();
            Assert.assertEquals(1, iss.size());
            final InputSample is = iss.iterator().next();
            Assert.assertEquals(TestHolderFactory.INPUT_SAMPLE_NAME, is.getName());

            //final long duration = new java.util.Date().getTime() - startTime;
            //System.out.println("testExperimentGroup took: " + duration + "ms");

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        }
    }

    // not a good method, if there is more than one plate
    public static Experiment getExperimentByPosition(final ExperimentGroup group, final String position) {
        //TODO convert position A1 =>A01
        final Collection experiments = group.getExperiments();
        assert 0 < experiments.size() : "No experiments for group";
        for (final Iterator iter = experiments.iterator(); iter.hasNext();) {
            final Experiment experiment = (Experiment) iter.next();
            final String outputPosition = HolderFactory.getPositionInHolder(experiment);
            // System.out.println(experiment.getName()+" "+outputPosition);
            if (HolderFactory.positionEquals(position, outputPosition)) {
                return experiment;
            }
        }
        return null; // not found
    }

    private ExperimentGroup createExperimentGroup(final Holder expPlate) throws ConstraintException,
        AccessException {
        final ExperimentType type =
            ProtocolFactory.getExperimentType(this.wv, "hf" + System.currentTimeMillis());
        final Collection<ExperimentType> types = Collections.singleton(type);
        Assert.assertNotNull(type);
        final Protocol protocol = new Protocol(this.wv, "hf" + System.currentTimeMillis(), type);
        final SampleCategory sc = POJOFactory.createSampleCategory(this.wv);
        final RefInputSample input = new RefInputSample(this.wv, sc, protocol);
        input.setName(TestHolderFactory.INPUT_SAMPLE_NAME);
        final RefOutputSample output = new RefOutputSample(this.wv, this.category, protocol);
        output.setName("output");
        final ExperimentGroup group =
            HolderFactory.createPlateExperiment(this.wv, null, expPlate, "group " + new Date().getTime(),
                "just testing", types, Calendar.getInstance(), Calendar.getInstance(), "", // details
                protocol, Collections.EMPTY_MAP, null);
        return group;
    }

    public void testMultiLinePipette() throws ConstraintException, AccessException {

        // prepare from plate
        final ExperimentGroup group1 = this.createExperimentGroup(this.plate);
        final Experiment from = TestHolderFactory.getExperimentByPosition(group1, "D2");
        final ResearchObjective blueprint = new ResearchObjective(this.wv, TestHolderFactory.UNIQUE, "hf");
        from.setProject(blueprint);

        // pipette to a new plate
        final Holder plate2 =
            HolderFactory.createHolder(this.wv, null, "hf" + System.currentTimeMillis(), this.holderType,
                java.util.Collections.EMPTY_MAP);
        final ExperimentGroup group2 = this.createExperimentGroup(plate2);
        final Float AMOUNT = new Float(0.000001f);
        HolderFactory.multiLinePipette(this.wv, this.plate, group2, TestHolderFactory.INPUT_SAMPLE_NAME,
            AMOUNT);

        // check an experiment in the second plate
        final Experiment experiment = TestHolderFactory.getExperimentByPosition(group2, "D2");
        final Collection<InputSample> iss = experiment.getInputSamples();
        InputSample is = null;
        for (final Iterator iter = iss.iterator(); iter.hasNext();) {
            is = (InputSample) iter.next();
            if (TestHolderFactory.INPUT_SAMPLE_NAME.equals(is.getName())) {
                break;
            }
        }
        Assert.assertNotNull(is);
        Assert.assertEquals(TestHolderFactory.INPUT_SAMPLE_NAME, is.getName());
        Assert.assertEquals(AMOUNT, is.getAmount());
        final Sample sample =
            HolderFactory.getSampleByPosition(this.plate, HolderFactory.getRow(experiment),
                HolderFactory.getColumn(experiment));
        Assert.assertEquals(sample, is.getSample());
        Assert.assertEquals("target", blueprint, experiment.getProject());

        //final Timestamp endTime = new Timestamp(System.currentTimeMillis());
        //System.out.println("TestHolderFactory.testMultiLinePipette use:"
        //    + (endTime.getTime() - startTime.getTime()) + "ms");

    }

    public void testGetHolders() {
        try {
            this.createExperimentGroup(this.plate);
            final Collection<Holder> holders = HolderFactory.getHolders(this.category);
            Assert.assertTrue(holders.contains(this.plate));
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testGetHolderPoint() {
        try {
            final Holder holder =
                HolderFactory.createHolder(this.wv, null, "testTwo" + System.currentTimeMillis(),
                    this.holderType, java.util.Collections.EMPTY_MAP);
            Assert.assertNotNull(holder);

            final Sample sample =
                SampleFactory.createSample(this.wv, "tsample" + new Date().getTime(),
                    Collections.singleton(this.category), Collections.EMPTY_MAP);
            sample.setColPosition(10);
            sample.setRowPosition(10);
            holder.addSample(sample);
            Assert.assertEquals("southWestPlate", HolderFactory.getHolderPoint(holder));
            holder.removeSample(sample);

            final Sample sample1 =
                SampleFactory.createSample(this.wv, "tsample1" + new Date().getTime(),
                    Collections.singleton(this.category), Collections.EMPTY_MAP);
            sample1.setColPosition(14);
            sample1.setRowPosition(6);
            holder.addSample(sample1);
            Assert.assertEquals("northEastPlate", HolderFactory.getHolderPoint(holder));

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        }
    }

}
