package org.pimslims.presentation.experiment;

import java.io.IOException;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
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
import org.pimslims.lab.Measurement;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.lab.protocol.ProtocolFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;

public class ExperimentGroupWriterTest extends TestCase {

    private static final String UNIQUE = "eg" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ExperimentGroupWriterTest.class);
    }

    /**
     * the database we are testing with
     */
    private final AbstractModel model;

    /**
     * the experiment grou we are testing with
     */
    private ExperimentGroup group;

    /**
     * the transaction we are testing with
     */
    private WritableVersion version;

    private Set<SampleCategory> categories;

    private ExperimentType type;

    private Protocol protocol;

    //private long time;

    public ExperimentGroupWriterTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /**
     * Terrifyingly large set up method to meet all data model constraints
     */
    @Override
    protected void setUp() {
        //this.time = System.currentTimeMillis();
        this.version = this.model.getTestVersion();

        try {
            final SampleCategory category = ProtocolFactory.getSampleCategory(this.version, "test");
            this.categories = Collections.singleton(category);
            final HolderCategory holderCategory =
                HolderFactory.createHolderCategory(this.version, "test" + ExperimentGroupWriterTest.UNIQUE,
                    Collections.EMPTY_MAP);
            final Set<HolderCategory> holderCategories = new HashSet(Collections.singleton(holderCategory));
            final HolderType holderType =
                HolderFactory.createHolderType(this.version, "refHolder" + ExperimentGroupWriterTest.UNIQUE,
                    holderCategories, java.util.Collections.EMPTY_MAP);
            holderType.setMaxRow(1);
            holderType.setMaxColumn(1);
            final Holder plate =
                HolderFactory.createHolder(this.version, null, "test" + ExperimentGroupWriterTest.UNIQUE,
                    holderType, java.util.Collections.EMPTY_MAP);
            Assert.assertNotNull(plate);
            this.type =
                ProtocolFactory.getExperimentType(this.version, "test" + ExperimentGroupWriterTest.UNIQUE);
            this.protocol = new Protocol(this.version, ExperimentGroupWriterTest.UNIQUE, this.type);
            new RefOutputSample(this.version, category, this.protocol);
            final Calendar now = Calendar.getInstance();
            this.group =
                HolderFactory.createPlateExperiment(this.version, null, plate, "group "
                    + ExperimentGroupWriterTest.UNIQUE, "just testing", Collections.singleton(this.type),
                    now, now, "", this.protocol, Collections.EMPTY_MAP, null // all wells
                    );
        } catch (final ConstraintException e) {
            this.version.abort();
            this.version = null;
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            this.version.abort();
            this.version = null;
            Assert.fail(e.getMessage());
        }
    }

    @Override
    protected void tearDown() {
        if (null != this.version) {
            this.version.abort();
        }
        //System.out.println("Time for method: " + (System.currentTimeMillis() - this.time) + "ms");
    }

    public void testExperimentNumber() throws AccessException {
        try {
            final StringReader sr =
                new StringReader("Experiment,Start Date,Start Time,Finish Date,Finish Time\n" + "1,,,,");

            final Collection<ExperimentType> experimentTypes = Collections.singleton(this.type);
            final ExperimentGroup group2 =
                HolderFactory.createExperimentGroup(this.version, null, ExperimentGroupWriterTest.UNIQUE,
                    "experiment group", experimentTypes, ExperimentGroupWriterTest.NOW,
                    ExperimentGroupWriterTest.NOW, "", this.protocol, 1);

            final ExperimentGroupWriter gw = new ExperimentGroupWriter(this.version, group2);
            gw.setValuesFromSpreadSheetForCreate(sr);

            final Collection errors = gw.getErrors();
            Assert.assertNotNull(this.version.findFirst(Experiment.class, Experiment.PROP_NAME,
                ExperimentGroupWriterTest.UNIQUE + ":01"));
            if (!errors.isEmpty()) {
                Assert.fail(errors.iterator().next().toString());
            }
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testExperimentName() throws AccessException {
        try {
            final StringReader sr =
                new StringReader("Job Description,Start Date,Start Time,Finish Date,Finish Time\n"
                    + ExperimentGroupWriterTest.UNIQUE + "exp,,,,");

            final Collection<ExperimentType> experimentTypes = Collections.singleton(this.type);
            final ExperimentGroup group2 =
                HolderFactory.createExperimentGroup(this.version, null, ExperimentGroupWriterTest.UNIQUE,
                    "experiment group", experimentTypes, ExperimentGroupWriterTest.NOW,
                    ExperimentGroupWriterTest.NOW, "", this.protocol, 1);

            final ExperimentGroupWriter gw = new ExperimentGroupWriter(this.version, group2);
            gw.setValuesFromSpreadSheetForCreate(sr);

            final Collection errors = gw.getErrors();
            Assert.assertNotNull(this.version.findFirst(Experiment.class, Experiment.PROP_NAME,
                ExperimentGroupWriterTest.UNIQUE + "exp"));
            if (!errors.isEmpty()) {
                Assert.fail((String) errors.iterator().next());
            }
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testSetMissingDate() {
        try {
            //final Experiment experiment = ExperimentGroupWriterTest.getExperimentByPosition(this.group, "A01");
            final ExperimentGroupWriter writer = new ExperimentGroupWriter(this.version, this.group);
            final StringReader sr =
                new StringReader("Well,Start Date,Start Time,Finish Date,Finish Time\n" + "A01,,,,");
            writer.setValuesFromSpreadSheetForCreate(sr);
            final Collection errors = writer.getErrors();
            Assert.assertEquals(0, errors.size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testSetDates() {
        try {
            final Experiment experiment =
                ExperimentGroupWriterTest.getExperimentByPosition(this.group, "A01");
            final ExperimentGroupWriter writer = new ExperimentGroupWriter(this.version, this.group);
            final StringReader sr =
                new StringReader("Well,Start Date,Start Time,Finish Date,Finish Time\n"
                    + "A01,03/12/2010,34:36.4,03/12/2010,02:09.1");
            writer.setValuesFromSpreadSheetForCreate(sr);
            final Calendar start = experiment.getStartDate();
            Assert.assertEquals(2010, start.get(Calendar.YEAR));
            final Calendar end = experiment.getEndDate();
            Assert.assertEquals(Calendar.DECEMBER, end.get(Calendar.MONTH));
            // the values in Start Time and Finish Time don't make any sense
            final Collection errors = writer.getErrors();
            Assert.assertEquals(0, errors.size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testSetNoSuchValueFromSpreadSheet() {
        final ExperimentGroupWriter writer = new ExperimentGroupWriter(this.version, this.group);
        final StringReader sr = new StringReader("well, nonesuch\n" + "C4,value");
        try {
            writer.setValuesFromSpreadSheetForCreate(sr);
            final Collection errors = writer.getErrors();
            Assert.assertEquals(1, errors.size());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testTooFewExperiments() throws ConstraintException, IOException {
        final Experiment c4 = ExperimentGroupWriterTest.getExperimentByPosition(this.group, "A01");

        final Parameter parm = new Parameter(this.version, c4);
        parm.setName("testParm");
        final ExperimentGroupWriter writer = new ExperimentGroupWriter(this.version, this.group);
        final StringReader sr = new StringReader("Well,testParm\n");
        try {
            writer.setValuesFromSpreadSheetForCreate(sr);
            Assert.fail("Too few experiments");
        } catch (final IllegalArgumentException e) {
            // that's right
        }

    }

    public void testBadGroupName() throws ConstraintException, IOException {
        final Experiment c4 = ExperimentGroupWriterTest.getExperimentByPosition(this.group, "A01");

        final Parameter parm = new Parameter(this.version, c4);
        parm.setName("testParm");
        final ExperimentGroupWriter writer = new ExperimentGroupWriter(this.version, this.group);
        final StringReader sr = new StringReader("GroupId,Well,testParm\n" + "nonesuch,A01,value");
        try {
            writer.setValuesFromSpreadSheetForCreate(sr);
            Assert.fail("Bad plate name");
        } catch (final IllegalArgumentException e) {
            // that's right
        }
    }

    public void testBadPlateNameAmend() throws ConstraintException, IOException {
        final Experiment c4 = ExperimentGroupWriterTest.getExperimentByPosition(this.group, "A01");

        final Parameter parm = new Parameter(this.version, c4);
        parm.setName("testParm");
        final ExperimentGroupWriter writer = new ExperimentGroupWriter(this.version, this.group);
        final StringReader sr = new StringReader("PlateId,Well,testParm\n" + "nonesuch,A01,value");
        try {
            writer.setValuesFromSpreadSheetForAmend(sr);
            Assert.fail("Bad plate name");
        } catch (final IllegalArgumentException e) {
            // that's right
        }
    }

    public void testBadPlateNameCreate() throws ConstraintException, IOException {
        final Experiment c4 = ExperimentGroupWriterTest.getExperimentByPosition(this.group, "A01");

        final Parameter parm = new Parameter(this.version, c4);
        parm.setName("testParm");
        final ExperimentGroupWriter writer = new ExperimentGroupWriter(this.version, this.group);
        final StringReader sr = new StringReader("PlateId,Well,testParm\n" + "nonesuch,A01,value");
        try {
            writer.setValuesFromSpreadSheetForCreate(sr);
        } catch (final IllegalArgumentException e) {
            Assert.fail("Bad plate name");
        }
    }

    public void testSetParameterValueFromSpreadSheet() {
        final Experiment c4 = ExperimentGroupWriterTest.getExperimentByPosition(this.group, "A01");
        try {
            final Parameter parm = new Parameter(this.version, c4);
            parm.setName("testParm");
            final ExperimentGroupWriter writer = new ExperimentGroupWriter(this.version, this.group);
            final StringReader sr =
                new StringReader("GroupId,Well,testParm\n" + this.group.getName() + ",A01,value");
            writer.setValuesFromSpreadSheetForCreate(sr);
            Assert.assertEquals("value", parm.getValue());
            final Collection errors = writer.getErrors();
            Assert.assertEquals(0, errors.size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testSetInputSampleFromSpreadSheet() {
        final Experiment c4 = ExperimentGroupWriterTest.getExperimentByPosition(this.group, "A01");
        try {
            final InputSample is = new InputSample(this.version, c4);
            is.setName("test");
            new InputSample(this.version, c4).setName("test2");
            final Sample sample = new Sample(this.version, "test" + System.currentTimeMillis());
            sample.setSampleCategories(this.categories);
            final ExperimentGroupWriter writer = new ExperimentGroupWriter(this.version, this.group);
            final StringReader sr =
                new StringReader("Well,test,test amount,test2,test2 number\n" + "A01," + sample.getName()
                    + ",,,");
            writer.setValuesFromSpreadSheetForCreate(sr);
            Assert.assertEquals(sample, is.getSample());
            final Collection errors = writer.getErrors();
            Assert.assertEquals(0, errors.size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testSetAmountFromSpreadSheet() {
        final Experiment c4 = ExperimentGroupWriterTest.getExperimentByPosition(this.group, "A01");
        try {
            final InputSample is = new InputSample(this.version, c4);
            is.setName("test");
            new InputSample(this.version, c4).setName("test2");
            final Sample sample = new Sample(this.version, "test" + System.currentTimeMillis());
            sample.setSampleCategories(this.categories);
            final ExperimentGroupWriter writer = new ExperimentGroupWriter(this.version, this.group);
            final StringReader sr =
                new StringReader("Well,test,test uL,test2,test2 number\n" + "A01," + sample.getName()
                    + ",5,,");
            writer.setValuesFromSpreadSheetForCreate(sr);
            Assert.assertNotNull(is.getAmount());
            final Measurement m = Measurement.getMeasurement(is.get_Values(), "amount");
            Assert.assertEquals("5uL", m.toString());
            Assert.assertEquals(sample, is.getSample());
            final Collection errors = writer.getErrors();
            Assert.assertEquals(0, errors.size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testSetNoInputSampleFromSpreadSheet() {
        final Experiment c4 = ExperimentGroupWriterTest.getExperimentByPosition(this.group, "A01");
        try {
            final InputSample is = new InputSample(this.version, c4);
            is.setName("test");
            new InputSample(this.version, c4).setName("test2");
            final ExperimentGroupWriter writer = new ExperimentGroupWriter(this.version, this.group);
            final StringReader sr = new StringReader("Well,test,test2\n" + "A01,,");
            writer.setValuesFromSpreadSheetForCreate(sr);
            Assert.assertEquals(null, is.getSample());
            final Collection errors = writer.getErrors();
            Assert.assertEquals(0, errors.size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testSetNoneInputSampleFromSpreadSheet() {
        final Experiment c4 = ExperimentGroupWriterTest.getExperimentByPosition(this.group, "A01");
        try {
            final InputSample is = new InputSample(this.version, c4);
            is.setName("test");
            final ExperimentGroupWriter writer = new ExperimentGroupWriter(this.version, this.group);
            final StringReader sr = new StringReader("Well,test\n" + "A01,None");
            writer.setValuesFromSpreadSheetForCreate(sr);
            Assert.assertEquals(null, is.getSample());
            final Collection errors = writer.getErrors();
            Assert.assertEquals(0, errors.size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testSetNoSuchExperimentFromSpreadSheet() {
        final Experiment c4 = ExperimentGroupWriterTest.getExperimentByPosition(this.group, "A01");
        try {
            final InputSample is = new InputSample(this.version, c4);
            is.setName("test");
            final ExperimentGroupWriter writer = new ExperimentGroupWriter(this.version, this.group);
            final StringReader sr = new StringReader("Well,test\n" + "Nonesuch,None");
            writer.setValuesFromSpreadSheetForCreate(sr);
            Assert.assertEquals(null, is.getSample());
            final Collection errors = writer.getErrors();
            Assert.assertEquals(1, errors.size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testSetTargetFromSpreadSheet() {
        final Experiment c4 = ExperimentGroupWriterTest.getExperimentByPosition(this.group, "A01");
        try {
            final ResearchObjective target = new ResearchObjective(this.version, "testTarget", "testTarget");
            // target.setLocalName("testTarget");
            final ExperimentGroupWriter writer = new ExperimentGroupWriter(this.version, this.group);
            final StringReader sr = new StringReader("Well,target\n" + "A01,testTarget  " // with trailing blank
            );
            writer.setValuesFromSpreadSheetForCreate(sr);
            Assert.assertEquals(target, c4.getResearchObjective());
            final Collection errors = writer.getErrors();
            Assert.assertEquals(0, errors.size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testSetConstructFromSpreadSheet() {
        final Experiment experiment = ExperimentGroupWriterTest.getExperimentByPosition(this.group, "A01");
        try {
            final ResearchObjective construct =
                new ResearchObjective(this.version, "testConstruct", "testConstruct");
            // no, these are not set in SSPF database
            // construct.setLocalName("testConstruct");
            final ExperimentGroupWriter writer = new ExperimentGroupWriter(this.version, this.group);
            final StringReader sr = new StringReader("Well,"/* +" Target," */
                + "Project\n" + "A01,"/* +"testTarget," */
                + "testConstruct");
            writer.setValuesFromSpreadSheetForCreate(sr);
            Assert.assertEquals(construct, experiment.getProject());
            final Collection errors = writer.getErrors();
            Assert.assertEquals(0, errors.size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testCreateConstructFromSpreadSheet() {
        final Experiment experiment = ExperimentGroupWriterTest.getExperimentByPosition(this.group, "A01");
        try {
            final ExperimentGroupWriter writer = new ExperimentGroupWriter(this.version, this.group);
            final StringReader sr = new StringReader("Well,"/* +" Target," */
                + "Construct\n" + "A01,"/* +"testTarget," */
                + ExperimentGroupWriterTest.UNIQUE);
            writer.setValuesFromSpreadSheetForCreate(sr);
            Assert.assertNotNull(experiment.getProject());
            Assert.assertEquals(ExperimentGroupWriterTest.UNIQUE, experiment.getProject().getName());
            final Collection errors = writer.getErrors();
            Assert.assertEquals(0, errors.size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    /* */

    public void testSetNewAmount() throws ConstraintException, IOException {

        final ExperimentGroup group =
            new ExperimentGroup(this.version, ExperimentGroupWriterTest.UNIQUE,
                ExperimentGroupWriterTest.UNIQUE);
        final ExperimentType type = new ExperimentType(this.version, ExperimentGroupWriterTest.UNIQUE);
        this.createExperiment(group, type, ExperimentGroupWriterTest.UNIQUE);
        final ExperimentGroupWriter writer = new ExperimentGroupWriter(this.version, group);
        final StringReader sr =
            new StringReader("GroupId,Well,Target,Construct,in,in mL\r\n" + ExperimentGroupWriterTest.UNIQUE
                + ",A1,,,,5");
        writer.setValuesFromSpreadSheetForAmend(sr);
        final Collection errors = writer.getErrors();
        Assert.assertEquals(0, errors.size());

    }

    private Sample createExperiment(final ExperimentGroup group, final ExperimentType type, final String name)
        throws ConstraintException {
        final Experiment experiment =
            new Experiment(this.version, name, ExperimentGroupWriterTest.NOW, ExperimentGroupWriterTest.NOW,
                type);
        experiment.setExperimentGroup(group);
        final Sample out = new Sample(this.version, name);
        out.setRowPosition(1);
        out.setColPosition(1);
        new OutputSample(this.version, experiment).setSample(out);
        new InputSample(this.version, experiment).setName("in");
        return out;
    }

    //TODO test amend, with 2 plates
    public void testSetNewAmountTwoPlates() throws ConstraintException, IOException {

        final ExperimentGroup group =
            new ExperimentGroup(this.version, ExperimentGroupWriterTest.UNIQUE,
                ExperimentGroupWriterTest.UNIQUE);
        final ExperimentType type = new ExperimentType(this.version, ExperimentGroupWriterTest.UNIQUE);
        final Sample out1 = this.createExperiment(group, type, ExperimentGroupWriterTest.UNIQUE + "e1");
        out1.setHolder(new Holder(this.version, ExperimentGroupWriterTest.UNIQUE + "h1", null));
        final Sample out2 = this.createExperiment(group, type, ExperimentGroupWriterTest.UNIQUE + "e2");
        out2.setHolder(new Holder(this.version, ExperimentGroupWriterTest.UNIQUE + "h2", null));
        final ExperimentGroupWriter writer = new ExperimentGroupWriter(this.version, group);
        final StringReader sr =
            new StringReader("PlateId,Well,Target,Construct,in,in mL\r\n" + ExperimentGroupWriterTest.UNIQUE
                + "h1" + ",A1,,,,5\r\n" + ExperimentGroupWriterTest.UNIQUE + "h2" + ",A1,,,,10\r\n");
        writer.setValuesFromSpreadSheetForAmend(sr);
        final Collection errors = writer.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    /**
     * @param group
     * @param position
     * @return the experiment at that position in the group
     * 
     */
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

}
