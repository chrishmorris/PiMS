package org.pimslims.lab;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.ExperimentInputSampleSetter;
import org.pimslims.lab.experiment.ExperimentParameterSetter;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.plateExperiment.PlateExperimentUtility;
import org.pimslims.test.AbstractTestCase;

/*
 * To test java methods for new plate UI org.pimslims.lab.HolderUtility already has a couple of methods called
 * "pipette". Some of this class is commented out because of DM changes, but it can be reinstated:
 * pipette(inputSample, outputHolder, "A1", "H12", 1.5); pipette(inputHolder, outputHolder, "B4", "H12", 0.5);
 */
public class TestPlate extends AbstractTestCase {

    /**
     * NOW Calendar
     */
    private static final Calendar NOW = Calendar.getInstance();

    private static final String UNIQUE = "plate" + System.currentTimeMillis();

    private WritableVersion wv;

    private Holder plate;

    private ExperimentType type;

    private SampleCategory category;

    private HolderCategory holderCategory;

    private HolderType holderType;

    private Set<SampleCategory> sampleCategories;

    private Calendar startTime = null;

    private HashSet types;

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        final AbstractModel model = ModelImpl.getModel();
        this.wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);

        // make the plate
        this.holderCategory =
            HolderFactory.createHolderCategory(this.wv, "plate" + System.currentTimeMillis(),
                Collections.EMPTY_MAP);
        final Set<HolderCategory> holderCategories = new HashSet(Collections.singleton(this.holderCategory));
        this.holderType =
            HolderFactory.createHolderType(this.wv, "refHolder" + new java.util.Date().getTime(),
                holderCategories, java.util.Collections.EMPTY_MAP);
        Assert.assertNotNull(this.holderType);
        this.holderType.setMaxRow(8);
        this.holderType.setMaxColumn(12);
        this.startTime = TestPlate.NOW;
        this.plate =
            HolderFactory.createHolder(this.wv, null, "test1" + System.currentTimeMillis(), this.holderType,
                java.util.Collections.EMPTY_MAP);
        Timestamp endTime = new Timestamp(System.currentTimeMillis());
        this.log("createHolder using " + (endTime.getTime() - this.startTime.getTimeInMillis()));
        // make the experiment group
        this.type = new ExperimentType(this.wv, "plate" + System.currentTimeMillis());
        this.startTime = TestPlate.NOW;
        this.types = new HashSet(Collections.singleton(this.type));
        this.category = new SampleCategory(this.wv, "plate" + System.currentTimeMillis());
        this.sampleCategories = new HashSet(Collections.singleton(this.category));

        endTime = new Timestamp(System.currentTimeMillis());
        this.log("createExperimentGroup using " + (endTime.getTime() - this.startTime.getTimeInMillis()));
    }

    @Override
    protected void log(final String string) {
        //System.out.println("TestPlate:" + string);

    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws AccessException {

        this.wv.abort();
        // not testing persistence
    }

    // private ExperimentGroup group;

    public void testGroup() throws ConstraintException, AccessException {
        System.out.println("TestPlate.testgroup():Sorry, this test is slow");
        Assert.assertNotNull(this.plate.getHolderType());
        final Protocol protocol = this.createProtocol();
        final ExperimentGroup group =
            HolderFactory.createPlateExperiment(this.wv, null, this.plate,
                "plate" + System.currentTimeMillis(), "plate", this.types, TestPlate.NOW, TestPlate.NOW, "",
                protocol, Collections.emptyMap(), null);
        Assert.assertEquals(8 * 12, group.getExperiments().size());
        try {
            PlateExperimentUtility.setGroupValues(group, new ExperimentParameterSetter("isShaken"),
                Boolean.TRUE.toString());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        }
        final java.util.Map<String, Object> shaken =
            PlateExperimentUtility.getGroupValues(group, new ExperimentParameterSetter("isShaken"));
        Assert.assertTrue(shaken.containsKey("C03"));
        Assert.assertEquals("set and get", Boolean.TRUE.toString(), shaken.get("C03"));
        group.delete();
    }

    public void testGroupParameterSet() throws ConstraintException, AccessException {
        Assert.assertNotNull(this.plate.getHolderType());
        final Protocol protocol = this.createProtocol();
        final ExperimentGroup group =
            HolderFactory.createPlateExperiment(this.wv, null, this.plate,
                "plate" + System.currentTimeMillis(), "plate", this.types, TestPlate.NOW, TestPlate.NOW, "",
                protocol, Collections.emptyMap(), null);
        try {
            PlateExperimentUtility.setGroupValues(group, new ExperimentParameterSetter("isShaken"),
                Boolean.TRUE.toString());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        }
        final java.util.Map<String, Object> shaken =
            PlateExperimentUtility.getGroupValues(group, new ExperimentParameterSetter("isShaken"));
        Assert.assertEquals("set and get", Boolean.TRUE.toString(), shaken.get("C03"));
        group.delete();
    }

    public void testSampleSetInGroup() throws AccessException {
        try {
            Assert.assertNotNull(this.plate.getHolderType());
            final Protocol protocol = this.createProtocol();

            final ExperimentGroup group =
                HolderFactory.createPlateExperiment(this.wv, null, this.plate,
                    "plate" + System.currentTimeMillis(), "plate", this.types, TestPlate.NOW, TestPlate.NOW,
                    "", protocol, Collections.emptyMap(), null);
            final Sample sample = new Sample(this.wv, "plate" + System.currentTimeMillis());
            sample.setSampleCategories(this.sampleCategories);
            PlateExperimentUtility.setGroupValues(group, new ExperimentInputSampleSetter("isShaken"), sample);
            final java.util.Map<String, Object> shaken =
                PlateExperimentUtility.getGroupValues(group, new ExperimentInputSampleSetter("isShaken"));
            Assert.assertEquals("set and get", sample, shaken.get("C03"));
            group.delete();
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * @return
     * @throws ConstraintException
     */
    private Protocol createProtocol() throws ConstraintException {
        final Protocol protocol = new Protocol(this.wv, TestPlate.UNIQUE, this.type);
        new RefOutputSample(this.wv, this.category, protocol);
        return protocol;
    }

    /*
     * TODO public void testGradient() { try { PlateExperimentUtility.setGradient(group, new
     * ExperimentParameterSetter("temperature"), new LeftToRightGradient( 1.0f, 25.0f, "A1", "E5") );
     * java.util.Map<String, Object> temps = PlateExperimentUtility.getGroupValues( group, new
     * ExperimentParameterSetter("temperature") ); String middle = (String)temps.get("C3");
     * assertNotNull(middle); assertEquals("middle of gradient", 13.0, Float.parseFloat(middle), 0.000001f ); }
     * catch (NumberFormatException e) { fail(e.getMessage()); } }
     */

    public void testNext() throws ConstraintException {
        final ExperimentGroup group =
            new ExperimentGroup(this.wv, "Nextgroup" + System.currentTimeMillis(), "plate");
        final Experiment experiment =
            new Experiment(this.wv, "nextexp" + System.currentTimeMillis(), this.startTime, this.startTime,
                this.type);
        group.addExperiment(experiment);
        new OutputSample(this.wv, experiment);
        final OutputSample os = new OutputSample(this.wv, experiment);
        final Sample sample = new Sample(this.wv, "sample" + System.currentTimeMillis());
        os.setSample(sample);
        final Experiment experiment2 =
            new Experiment(this.wv, "exp2" + System.currentTimeMillis(), this.startTime, this.startTime,
                this.type);
        final InputSample is = new InputSample(this.wv, experiment2);
        is.setSample(sample);
        final ExperimentGroup group2 =
            new ExperimentGroup(this.wv, "grouptwo" + System.currentTimeMillis(), "plate");
        group2.addExperiment(experiment2);

        final Set<ExperimentGroup> next = PlateExperimentUtility.getNextPlateExperiments(group);
        Assert.assertEquals(1, next.size());
        Assert.assertEquals(group2, next.iterator().next());
    }

    public void testLast() throws ConstraintException {
        final ExperimentGroup group =
            new ExperimentGroup(this.wv, "lastgroup" + System.currentTimeMillis(), "plate");
        final Experiment experiment =
            new Experiment(this.wv, "lastexp" + System.currentTimeMillis(), this.startTime, this.startTime,
                this.type);
        group.addExperiment(experiment);
        new OutputSample(this.wv, experiment);
        final OutputSample os = new OutputSample(this.wv, experiment);
        final Sample sample = new Sample(this.wv, "sample" + System.currentTimeMillis());
        os.setSample(sample);
        final Experiment experiment2 =
            new Experiment(this.wv, "exp2" + System.currentTimeMillis(), this.startTime, this.startTime,
                this.type);
        final InputSample is = new InputSample(this.wv, experiment2);
        is.setSample(sample);
        final ExperimentGroup group2 =
            new ExperimentGroup(this.wv, "grouptwo" + System.currentTimeMillis(), "plate");
        group2.addExperiment(experiment2);

        final Set<ExperimentGroup> last = PlateExperimentUtility.getPreviousPlateExperiments(group2);
        Assert.assertEquals(1, last.size());
        Assert.assertEquals(group, last.iterator().next());
    }

    public void testDelete() throws ConstraintException, AccessException, AbortedException {
        final ExperimentGroup group =
            new ExperimentGroup(this.wv, "group" + System.currentTimeMillis(), "plate");
        final String groupHook = group.get_Hook();

        final Experiment experiment =
            new Experiment(this.wv, "exp" + System.currentTimeMillis(), this.startTime, this.startTime,
                this.type);
        final String expHook = experiment.get_Hook();

        group.addExperiment(experiment);
        new OutputSample(this.wv, experiment);
        final OutputSample os = new OutputSample(this.wv, experiment);
        final String osHook = os.get_Hook();

        final Sample sample = new Sample(this.wv, "sample" + System.currentTimeMillis());
        os.setSample(sample);
        final String sampleHook = sample.get_Hook();

        final Experiment experiment2 =
            new Experiment(this.wv, "exp2" + System.currentTimeMillis(), this.startTime, this.startTime,
                this.type);
        group.addExperiment(experiment2);
        final InputSample is = new InputSample(this.wv, experiment2);
        final String isHook = is.get_Hook();
        is.setSample(sample);
        //was this.wv.commit();

        this.wv = AbstractTestCase.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        PlateExperimentUtility.deleteExpGroup(this.wv, groupHook);
        //was this.wv.commit();

        this.wv = AbstractTestCase.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        Assert.assertNull(this.wv.get(groupHook));
        Assert.assertNull(this.wv.get(expHook));
        Assert.assertNull(this.wv.get(osHook));
        Assert.assertNull(this.wv.get(sampleHook));
        Assert.assertNull(this.wv.get(isHook));

    }
}
