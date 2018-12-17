package org.pimslims.servlet;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.Note;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.people.Person;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.test.POJOFactory;

public class UpdateTest extends TestCase {

    private static final String UNIQUE = "u" + System.currentTimeMillis();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(UpdateTest.class);
    }

    private final AbstractModel model;

    public UpdateTest(final String methodName) {
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

    public final void testProcessEmptyRequest() throws AccessException, ConstraintException,
        ServletException, ParseException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<ModelObject, Map<String, Object>> objects =
                Update.processRequest(version, Collections.EMPTY_MAP, null);
            Assert.assertEquals(0, objects.size());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public final void testAddNote() throws ConstraintException, AccessException, ServletException,
        ParseException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final ResearchObjective entry =
                new ResearchObjective(version, "name" + System.currentTimeMillis(), "eb" + UpdateTest.UNIQUE);
            parms.put(entry.get_Hook() + ":" + Notes.ADD_NOTE, new String[] { UpdateTest.UNIQUE });
            Notes.processRequest(version, parms, null);
            Assert.assertEquals(1, entry.getNotes().size());
            final Note note = entry.getNotes().iterator().next();
            Assert.assertEquals(UpdateTest.UNIQUE, note.getDetails());
            Assert.assertNull(note.getAuthor());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public final void testProcessStringRequest() throws ConstraintException, AccessException,
        ServletException, ParseException, InterruptedException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final ResearchObjective modelObject =
                new ResearchObjective(version, "name" + System.currentTimeMillis(), "test");
            parms.put(modelObject.get_Hook() + ":" + ResearchObjective.PROP_WHYCHOSEN,
                new String[] { "name2" });
            parms.put(modelObject.get_Hook() + ":" + ResearchObjective.PROP_BIOCHEMICALFUNCTION,
                new String[] { "name2" });
            final long fetched = modelObject.getCreationDate().getTimeInMillis();
            parms.put(Update.FETCHED, new String[] { Long.toString(fetched) });
            Thread.sleep(1000);
            final Map<ModelObject, Map<String, Object>> objects = Update.processRequest(version, parms, null);
            Assert.assertEquals(1, objects.size());
            Assert.assertTrue(objects.containsKey(modelObject));
            Assert.assertTrue(objects.get(modelObject).containsKey(ResearchObjective.PROP_WHYCHOSEN));
            Assert.assertEquals("name2", objects.get(modelObject).get(ResearchObjective.PROP_WHYCHOSEN));
            Assert.assertEquals("name2", modelObject.getWhyChosen());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public final void testConflict() throws ConstraintException, AccessException, ParseException,
        ServletException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final long old = System.currentTimeMillis() - 1000;
            parms.put(Update.FETCHED, new String[] { Long.toString(old) });
            final ResearchObjective modelObject =
                new ResearchObjective(version, "name" + System.currentTimeMillis(), "test");
            parms.put(modelObject.get_Hook() + ":" + ResearchObjective.PROP_WHYCHOSEN,
                new String[] { "name2" });
            final Map<ModelObject, Map<String, Object>> objects = Update.processRequest(version, parms, null);
            Assert.fail("conflict not detected");
        } catch (final ConcurrentUpdateException e) {
            // that's right
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public final void testProcessMultiStringRequest() throws ConstraintException, AccessException,
        ServletException, ParseException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final Organisation modelObject = new Organisation(version, "name" + System.currentTimeMillis());
            parms.put(modelObject.get_Hook() + ":" + Organisation.PROP_ADDRESSES, new String[] { "a;b" });
            final Map<ModelObject, Map<String, Object>> objects = Update.processRequest(version, parms, null);
            Assert.assertEquals(1, objects.size());
            Assert.assertTrue(objects.containsKey(modelObject));

            final List<String> addresses = modelObject.getAddresses();
            Assert.assertEquals(java.util.Arrays.asList(new String[] { "a", "b" }), addresses);
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public final void testSetStringToNull() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final Person modelObject = new Person(version, "name" + System.currentTimeMillis());
            modelObject.setFamilyTitle("test");
            parms.put(modelObject.get_Hook() + ":" + Person.PROP_FAMILYTITLE, new String[] { null });
            final Map<ModelObject, Map<String, Object>> objects = Update.processRequest(version, parms, null);
            Assert.assertEquals(1, objects.size());
            Assert.assertTrue(objects.containsKey(modelObject));
            Assert.assertEquals("", modelObject.getFamilyTitle());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final ServletException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ParseException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

/* TODO find another float property, one that is not deprecated
    public final void testProcessFloatRequest() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final Location modelObject = new Location(version, "name1");
            parms.put(modelObject.get_Hook() + ":" + Location.PROP_PRESSURE, new String[] { "1.0" });
            final Map<ModelObject, Map<String, Object>> objects = Update.processRequest(version, parms, null);
            Assert.assertEquals(1, objects.size());
            Assert.assertTrue(objects.containsKey(modelObject));
            Assert.assertEquals(1.0f, modelObject.getPressure());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final ServletException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ParseException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public final void testSetFloatToNull() throws ConstraintException, AccessException, ServletException,
        ParseException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final Location modelObject = new Location(version, "name1");
            parms.put(modelObject.get_Hook() + ":" + Location.PROP_PRESSURE, new String[] { "" });
            final Map<ModelObject, Map<String, Object>> objects = Update.processRequest(version, parms, null);
            Assert.assertEquals(1, objects.size());
            Assert.assertTrue(objects.containsKey(modelObject));
            Assert.assertNull(modelObject.getPressure());
        } finally {
            version.abort(); // not testing persistence here
        }
    }  

    public final void testSetTwo() throws ConstraintException, AccessException, ServletException,
        ParseException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final Location modelObject = new Location(version, "name1");
            parms.put(modelObject.get_Hook() + ":" + Location.PROP_PRESSURE, new String[] { "" });
            parms.put(modelObject.get_Hook() + ":" + Location.PROP_NAME, new String[] { "name2" });
            final Map<ModelObject, Map<String, Object>> objects = Update.processRequest(version, parms, null);
            Assert.assertEquals("name2", modelObject.getName());
            Assert.assertEquals(1, objects.size());
            Assert.assertTrue(objects.containsKey(modelObject));
            final Map<String, Object> changed = objects.get(modelObject);
            Assert.assertEquals(2, changed.size());
            Assert.assertEquals(null, changed.get(Location.PROP_PRESSURE));
            Assert.assertEquals(modelObject.getName(), changed.get(Location.PROP_NAME));
            Assert.assertEquals("name2", modelObject.getName());
        } finally {
            version.abort(); // not testing persistence here
        }
    } */

    public final void testProcessRoleRequest() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final Molecule mo1 = new Molecule(version, "other", "name1");
            final Organism mo2 = new Organism(version, "name2");
            parms.put(mo1.get_Hook() + ":" + Substance.PROP_NATURALSOURCE, new String[] { mo2.get_Hook() });
            final Map<ModelObject, Map<String, Object>> objects = Update.processRequest(version, parms, null);
            Assert.assertEquals(1, objects.size());
            Assert.assertTrue(objects.containsKey(mo1));
            Assert.assertEquals(mo2, mo1.getNaturalSource());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final ServletException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ParseException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public final void testSetRoleToNull() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final Molecule mo1 = new Molecule(version, "other", "name1");
            final Organism mo2 = new Organism(version, "name2");
            mo1.setNaturalSource(mo2);
            parms.put(mo1.get_Hook() + ":" + Substance.PROP_NATURALSOURCE, new String[] { "[none]" });
            final Map<ModelObject, Map<String, Object>> objects = Update.processRequest(version, parms, null);
            Assert.assertEquals(1, objects.size());
            Assert.assertNull(mo1.getNaturalSource());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final ServletException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ParseException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    private final Calendar NOW = Calendar.getInstance();

    public final void testDate() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final Calendar now = java.util.Calendar.getInstance();
            final ExperimentType type = new ExperimentType(version, "type" + System.currentTimeMillis());
            final Experiment experiment =
                new Experiment(version, "testAmount" + System.currentTimeMillis(), now, now, type);
            parms.put(experiment.get_Hook() + ":" + Experiment.PROP_STARTDATE, new String[] { new Long(
                this.NOW.getTimeInMillis()).toString() });
            final Map<ModelObject, Map<String, Object>> objects = Update.processRequest(version, parms, null);
            Assert.assertEquals(1, objects.size());
            Assert.assertEquals(this.NOW, experiment.getStartDate());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final ServletException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ParseException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public final void testInputSampleAmount() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final Calendar now = java.util.Calendar.getInstance();
            final ExperimentType type = new ExperimentType(version, "type" + System.currentTimeMillis());
            final Experiment experiment =
                new Experiment(version, "testAmount" + System.currentTimeMillis(), now, now, type);
            final InputSample inputSample = new InputSample(version, experiment);
            parms.put(inputSample.get_Hook() + ":amount", new String[] { "50ul" }); // note that UL is standard
            final Map<ModelObject, Map<String, Object>> objects = Update.processRequest(version, parms, null);
            Assert.assertEquals(1, objects.size());
            Assert.assertEquals("uL", inputSample.getAmountDisplayUnit());
            Assert.assertEquals(50E-6f, inputSample.getAmount(), 1E-10f);
            Assert.assertEquals("L", inputSample.getAmountUnit());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final ServletException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ParseException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public final void testConcentration() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final Calendar now = java.util.Calendar.getInstance();
            final Sample sample = new Sample(version, UpdateTest.UNIQUE);
            Assert.assertNotNull(sample.getCreationDate());
            parms.put(sample.get_Hook() + ":concentration", new String[] { "0.1M" });
            final Map<ModelObject, Map<String, Object>> objects = Update.processRequest(version, parms, null);
            Assert.assertEquals(1, objects.size());
            Assert.assertEquals("M", sample.getConcDisplayUnit());
            Assert.assertEquals(0.1f, sample.getConcentration(), 1E-10f);
            Assert.assertEquals("M", sample.getConcentrationUnit());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final ServletException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ParseException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

/*
    public final void testSampleSequence() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final Sample sample = new Sample(version, UpdateTest.UNIQUE);
            parms.put(sample.get_Hook() + ":_SEQUENCE", new String[] { "AAAAAAAAAA" });
            final Map<ModelObject, Map<String, Object>> objects = Update.processRequest(version, parms, null);
            Assert.assertEquals(1, objects.size());
            Assert.assertEquals("AAAAAAAAAA",  sample.getSequence());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final ServletException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ParseException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence here
        }
    } */

    public final void testCurrentAmount() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final Calendar now = java.util.Calendar.getInstance();
            final Sample sample = new Sample(version, UpdateTest.UNIQUE);
            Assert.assertNotNull(sample.getCreationDate());
            parms.put(sample.get_Hook() + ":currentAmount", new String[] { "0.1kg" });
            final Map<ModelObject, Map<String, Object>> objects = Update.processRequest(version, parms, null);
            Assert.assertEquals(1, objects.size());
            Assert.assertEquals(0.1f, sample.getCurrentAmount());
            Assert.assertEquals("kg", sample.getAmountUnit());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final ServletException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ParseException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public final void testRefInputSampleAmount() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            //Timestamp now = new Timestamp(0);
            final ExperimentType type = new ExperimentType(version, "type" + System.currentTimeMillis());
            final Protocol protocol = new Protocol(version, "test" + System.currentTimeMillis(), type);
            final SampleCategory sc = POJOFactory.createSampleCategory(version);
            final RefInputSample ris = new RefInputSample(version, sc, protocol);
            parms.put(ris.get_Hook() + ":amount", new String[] { "50uL" });
            final Map<ModelObject, Map<String, Object>> objects = Update.processRequest(version, parms, null);
            Assert.assertEquals(1, objects.size());
            Assert.assertEquals("uL", ris.getDisplayUnit());
            Assert.assertEquals(50E-6f, ris.getAmount(), 1E-10f);
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final ServletException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ParseException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public final void testRefInputSampleAmountNoUnits() throws ConstraintException, AccessException,
        ServletException, ParseException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            //Timestamp now = new Timestamp(0);
            final ExperimentType type = new ExperimentType(version, "type" + System.currentTimeMillis());
            final Protocol protocol = new Protocol(version, "test" + System.currentTimeMillis(), type);
            final SampleCategory sc = POJOFactory.createSampleCategory(version);
            final RefInputSample ris = new RefInputSample(version, sc, protocol);
            parms.put(ris.get_Hook() + ":amount", new String[] { "0.000050" });
            Update.processRequest(version, parms, null);
            Assert.fail("Needs a unit");
        } catch (final IllegalArgumentException e) {
            // that's fine
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public final void testAliases() throws ConstraintException, AccessException, ServletException,
        ParseException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Map<String, String[]> parms = new HashMap<String, String[]>();
            final Molecule protein = new Molecule(version, "protein", UpdateTest.UNIQUE);
            final Target target = new Target(version, UpdateTest.UNIQUE, protein);
            parms.put(target.get_Hook() + ":" + Target.PROP_ALIASES, new String[] { "a;b" });
            Update.processRequest(version, parms, null);
            Assert.assertEquals(2, target.getAliases().size());
        } catch (final IllegalArgumentException e) {
            // that's fine
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public void testClean() {
        final List<String> list = Update.clean("2 ; 3;;4;");
        for (final String s : list) {
            System.out.println(s);
        }
        Assert.assertEquals(3, list.size());
    }
}
