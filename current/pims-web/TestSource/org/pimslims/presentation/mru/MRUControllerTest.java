package org.pimslims.presentation.mru;

import java.util.Date;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.pimslims.access.Access;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.people.Person;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

public class MRUControllerTest extends AbstractTestCase {

    public void testGetMRUSingleClass() {
        this.wv = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);
        String hook = null;
        try {
            MRUController.clearAll();
            MRUController.setMaxSize(4);
            MRUController.setMaxSizeOfEachClass(2);
            Assert.assertEquals(4, MRUController.getMaxSize());
            hook = this.create10Targets();
            // the object should be on top
            final Target target = POJOFactory.createTarget(this.wv);
            final String commonName = "target" + new Date();
            target.setName(commonName);
            MRUController.addObject(Access.ADMINISTRATOR, target);
            // check size is correct
            Assert.assertEquals(2, MRUController.getMRUs(Access.ADMINISTRATOR).size());

            // check got correct mru
            final ModelObjectShortBean mru = MRUController.getMRUs(Access.ADMINISTRATOR).get(0);

            Assert.assertEquals(commonName, mru.getName());

            // add same object again which should not contained twice
            MRUController.addObject(Access.ADMINISTRATOR, target);
            final ModelObjectShortBean mru2 = MRUController.getMRUs(Access.ADMINISTRATOR).get(1);
            Assert.assertFalse(commonName.equals(mru2.getName()));

            // add null object should not change anything
            MRUController.addObject(Access.ADMINISTRATOR, (String) null);
            ModelObjectShortBean mru3 = MRUController.getMRUs(Access.ADMINISTRATOR).get(0);
            Assert.assertTrue(commonName.equals(mru3.getName()));
            MRUController.addObject(Access.ADMINISTRATOR, (ModelObject) null);
            mru3 = MRUController.getMRUs(Access.ADMINISTRATOR).get(0);
            Assert.assertTrue(commonName.equals(mru3.getName()));

            this.wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
        // test MRUController outside wv
        MRUController.addObject(Access.ADMINISTRATOR, hook);
        final ModelObjectShortBean mru = MRUController.getMRUs(Access.ADMINISTRATOR).get(0);
        Assert.assertEquals(hook, mru.getHook());
    }

    public void testRO() {
        MRUController.clearAll();
        this.wv = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ModelObject project =
                new ResearchObjective(this.wv, AbstractTestCase.UNIQUE, AbstractTestCase.UNIQUE);
            MRUController.addObject(AbstractTestCase.UNIQUE, project);
            final List<MRU> recent =
                MRUController.getMRUs(AbstractTestCase.UNIQUE, ResearchObjective.class.getName());
            Assert.assertEquals(1, recent.size());
        } catch (final ModelException ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }

    }

    public void testGetMRUMultiClass() {
        this.wv = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);

        try {
            MRUController.clearAll();
            MRUController.setMaxSize(4);
            MRUController.setMaxSizeOfEachClass(2);
            Assert.assertEquals(4, MRUController.getMaxSize());
            Assert.assertEquals(2, MRUController.getMaxSizeOfEachClass());
            this.create10Targets();
            // the object should be on top
            final org.pimslims.model.experiment.Experiment exp = POJOFactory.createExperiment(this.wv);
            MRUController.addObject(Access.ADMINISTRATOR, exp);
            // check size is correct
            Assert.assertEquals(3, MRUController.getMRUs(Access.ADMINISTRATOR).size());

            // check got correct mru
            ModelObjectShortBean mru = MRUController.getMRUs(Access.ADMINISTRATOR).get(0);
            Assert.assertEquals(exp.get_Hook(), mru.getHook());

            // check got correct mru by className
            Assert
                .assertEquals(2, MRUController.getMRUs(Access.ADMINISTRATOR, Target.class.getName()).size());
            Assert.assertEquals(1, MRUController.getMRUs(Access.ADMINISTRATOR, Experiment.class.getName())
                .size());
            mru = MRUController.getMRUs(Access.ADMINISTRATOR, Experiment.class.getName()).get(0);
            Assert.assertEquals(exp.get_Hook(), mru.getHook());

            // check size is correct
            // we got 3 above, create another 3 here
            MRUController.addObject(Access.ADMINISTRATOR, POJOFactory.createExperiment(this.wv));
            MRUController.addObject(Access.ADMINISTRATOR, POJOFactory.createBookCitation(this.wv));
            MRUController.addObject(Access.ADMINISTRATOR, POJOFactory.createBookCitation(this.wv));

            Assert.assertEquals(4, MRUController.getMRUs(Access.ADMINISTRATOR).size());

            // wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    public void testGetPossibleMRUItems() throws AccessException, ConstraintException, AbortedException {
        MRUController.clearAll();
        MRUController.setMaxSize(4);
        MRUController.setMaxSizeOfEachClass(2);
        this.wv = this.getWV();
        String tName;
        String expName;
        try {
            final Target t = this.create(Target.class);
            final ResearchObjective exp = this.create(ResearchObjective.class);
            tName = t.get_Name();
            expName = exp.get_Name();
            MRUController.addObject(Access.ADMINISTRATOR, exp);
            MRUController.addObject(Access.ADMINISTRATOR, t);
            this.wv.commit();
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
        final Map<String, String> results =
            MRUControllerTest.getPossibleMRUItems(Access.ADMINISTRATOR, Target.class, null, true);
        Assert.assertTrue(results.values().contains(tName));
        Assert.assertFalse(results.values().contains(expName));

    }

    public void testGetPossibleMRUItemsForCurrentPerson() throws AccessException, ConstraintException,
        AbortedException {
        MRUController.clearAll();
        MRUController.setMaxSize(4);
        MRUController.setMaxSizeOfEachClass(2);
        this.wv = this.getWV();
        String personName1 = null;
        String userName = null;
        String userPersonName = null;
        try {
            //create person for MRU
            final Person person1 = this.create(Person.class);
            personName1 = person1.get_Name();

            //create user
            final User user = this.create(User.class);
            userName = user.get_Name();
            //create person for user
            final Person userPerson = this.create(Person.class);
            user.setPerson(userPerson);
            userPersonName = userPerson.get_Name();

            MRUController.addObject(userName, person1);
            this.wv.commit();
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }

        final Map<String, String> results =
            MRUControllerTest.getPossibleMRUItems(userName, Person.class, null, true);
        Assert.assertTrue(results.values().contains(personName1));
        Assert.assertTrue(results.values().contains(userPersonName));
        //System.out.println(results.values());
    }

    public void testGetMRURoleChoice() throws AbortedException, ConstraintException, AccessException {
        MRUController.clearAll();
        MRUController.setMaxSize(4);
        MRUController.setMaxSizeOfEachClass(2);

        // create a target which has a creater
        String hook = null;
        String role_currentHook = null;
        String role_currentName = null;
        String mru_hook = null;
        String username = null;
        this.wv = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final User user = this.create(User.class);
            username = user.getName();
            role_currentHook = user.get_Hook();
            role_currentName = user.get_Name();
            final User user2 = this.create(User.class);
            mru_hook = user2.get_Hook();
            MRUController.addObject(Access.ADMINISTRATOR, user2);// add a user
            // into mru
            this.wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }

        this.wv = AbstractTestCase.model.getWritableVersion(username);
        try {
            final Target t = this.create(Target.class);
            hook = t.get_Hook();
            this.wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }

        this.wv = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);

        try {
            // not required role will contain choice of [none]
            final MRURoleChoice mrc =
                MRUController
                    .getMRURoleChoice(Access.ADMINISTRATOR, hook, LabBookEntry.PROP_CREATOR, "false");
            Assert.assertEquals(hook, mrc.getModelObject_hook());
            Assert.assertEquals(LabBookEntry.PROP_CREATOR, mrc.getRole_name());
            Assert.assertEquals(role_currentHook, mrc.getRole_currentHook());
            Assert.assertEquals(role_currentHook, mrc.getRole_currentBean().getHook());
            Assert.assertEquals(role_currentName, mrc.getRole_currentObjectName());

            final Map<String, String> mruChoices = mrc.getPossibleMRUItems();
            Assert.assertEquals(2, mruChoices.size()); // [none],p2
            Assert.assertEquals("[none]", mruChoices.keySet().toArray()[0]);
            Assert.assertEquals(mru_hook, mruChoices.keySet().toArray()[1]);

            // required role will not contain choice of [none]
            final MRURoleChoice mrc2 =
                MRUController.getMRURoleChoice(Access.ADMINISTRATOR, hook, LabBookEntry.PROP_CREATOR, "true");
            Assert.assertEquals(hook, mrc2.getModelObject_hook());
            Assert.assertEquals(LabBookEntry.PROP_CREATOR, mrc2.getRole_name());
            Assert.assertEquals(role_currentHook, mrc2.getRole_currentHook());
            Assert.assertEquals(role_currentName, mrc2.getRole_currentObjectName());

            final Map<String, String> mruChoices2 = mrc2.getPossibleMRUItems();
            Assert.assertEquals(1, mruChoices2.size()); // p2
            Assert.assertEquals(mru_hook, mruChoices2.keySet().toArray()[0]);

            // clean up
            final ModelObject modelObject = this.wv.get(mrc.getModelObject_hook());
            modelObject.delete();
            this.wv.commit();

        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }

    }

    public void testRemoveMRU() {
        this.wv = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            MRUController.clearAll();
            MRUController.setMaxSize(4);
            MRUController.setMaxSizeOfEachClass(2);
            Assert.assertEquals(4, MRUController.getMaxSize());
            this.create10Targets();
            // the object should be on top
            final Target target = POJOFactory.createTarget(this.wv);
            final String commonName = "target" + new Date();
            target.setName(commonName);
            MRUController.addObject(Access.ADMINISTRATOR, target);
            // check size is correct
            Assert.assertEquals(2, MRUController.getMRUs(Access.ADMINISTRATOR).size());

            // check got correct mru
            ModelObjectShortBean mru = MRUController.getMRUs(Access.ADMINISTRATOR).get(0);
            Assert.assertTrue(commonName.equalsIgnoreCase(mru.getName()));
            // check delete
            MRUController.deleteObject(target.get_Hook());
            mru = MRUController.getMRUs(Access.ADMINISTRATOR).get(0);
            Assert.assertFalse(commonName.equalsIgnoreCase(mru.getName()));

            this.wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }

    }

    private String create10Targets() throws ConstraintException {
        String hook = null;
        for (int i = 0; i < 10; i++) {
            final Target target = POJOFactory.createTarget(this.wv);
            MRUController.addObject(Access.ADMINISTRATOR, target);
            hook = target.get_Hook();
        }
        return hook;
    }

    /**
     * 
     * @param userName
     * @param clazz
     * @param currentItemHook
     * @param isRequired
     * @return hook->name
     */
    private static Map<String, String> getPossibleMRUItems(final String userName, final Class clazz,
        final String currentItemHook, final Boolean isRequired) {
        Map<String, String> possibleItems = null;
        final ReadableVersion rv = ModelImpl.getModel().getReadableVersion(userName);
        try {
            possibleItems = MRUController.getPossibleMRUItems(rv, clazz, currentItemHook, isRequired);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
        return possibleItems;

    }

}
