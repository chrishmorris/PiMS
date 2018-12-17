package org.pimslims.model.api;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;
import org.pimslims.test.AbstractTestCase;

public class AccessObjectTester extends AbstractTestCase {
    /**
     * Values for creating a test Organisation
     */
    public static final HashMap<String, Object> ATTRIBUTES = new HashMap<String, Object>(
        org.pimslims.test.POJOFactory.getAttrAccessObject());

    /**
     * @param methodName test method to run
     */
    public AccessObjectTester(final String methodName) {
        super(methodName);
    }

    /**
     * Test create and find
     */
    public void testAccessObject() {
        final WritableVersionImpl wv =
            (WritableVersionImpl) ModelImpl.getModel().getWritableVersion(AbstractModel.SUPERUSER);
        assertNotNull(wv);
        try {
            final LabNotebook created = (LabNotebook) wv.create(LabNotebook.class, ATTRIBUTES);
            org.pimslims.model.core.LabNotebook found = null;
            for (final LabNotebook ao : wv.findAll(LabNotebook.class, new HashMap<String, Object>())) {
                if (ao.getName().equals(ATTRIBUTES.get(LabNotebook.PROP_NAME))) {
                    found = ao;
                    break;
                }
            }

            assertEquals("found", created, found);
            wv.delete(created);
            created.delete();
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

    public void testFindInOtherProject() throws ConstraintException, AccessException {
        wv = getWV();

        try {
            final LabNotebook owner = new LabNotebook(wv, "owner" + System.currentTimeMillis());
            final ExperimentType type = new ExperimentType(wv, "test");
            final Sample sample = new Sample(wv, "sample");
            sample.setAccess(owner);

            wv.setDefaultOwner(owner.getName());

            final Map<String, Object> attributes = new HashMap<String, Object>();
            attributes.put(Experiment.PROP_EXPERIMENTTYPE, type);
            attributes.put(Experiment.PROP_NAME, "test" + System.currentTimeMillis());
            attributes.put(Experiment.PROP_STARTDATE, java.util.Calendar.getInstance());
            attributes.put(Experiment.PROP_ENDDATE, java.util.Calendar.getInstance());
            final Experiment experiment = wv.create(Experiment.class, attributes);

            final Map attrMap = new HashMap();
            attrMap.put(InputSample.PROP_EXPERIMENT, experiment);
            attrMap.put(InputSample.PROP_NAME, "input");
            wv.create(InputSample.class, attrMap);

            /* the above fails, but this works:
            final InputSample made = new InputSample(version, experiment);
            made.setName("input");
            */

            final InputSample found =
                experiment.findFirst(Experiment.PROP_INPUTSAMPLES, InputSample.PROP_NAME, "input");
            Assert.assertNotNull(found);

        } finally {
            wv.abort();
        }
    }

    public void testDefaultOwner() {

        wv = getWV();
        try {
            final LabNotebook owner = new LabNotebook(wv, "owner" + System.currentTimeMillis());

            wv.setDefaultOwner(owner);

            final Target object = create( // check admin can now create
                Target.class);
            assertEquals("admin can create anything", owner.getName(), object.get_Owner());
            assertEquals("admin can create anything", owner.getName(), new Organisation(wv, "testAdmin3"
                + System.currentTimeMillis()).get_Owner());

        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            wv.abort();
        }
    }

/* test of obsolete code
    public void testDefaultCreator() {

        wv = getWV();
        try {
            final User user = create(User.class);
            wv.setDefaultCreator(user);
            final Target object = create( // check admin can now create
                Target.class);
            assertEquals(user, object.getCreator());

        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            wv.abort();
        }
    } */

    public void testDefaultOwnerWithConstructor() {

        wv = getWV();
        try {
            final LabNotebook owner = new LabNotebook(wv, "owner" + System.currentTimeMillis());

            wv.setDefaultOwner(owner.getName());
            final Target object =
                new Target(wv, "Target" + System.currentTimeMillis(), (Molecule) create(Molecule.class));
            assertEquals(owner.getName(), object.get_Owner());

            Map<String, Object> attributes = new HashMap<String, Object>();
            attributes.put(Target.PROP_NAME, "Target2" + System.currentTimeMillis());
            attributes.put(Target.PROP_PROTEIN, create(Molecule.class));
            final Target object2 = new Target(wv, attributes);
            assertEquals(owner.getName(), object2.get_Owner());

        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            wv.abort();
        }
    }

}
