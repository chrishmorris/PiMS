/**
 * pims-model org.pimslims.dao LockTest.java
 * 
 * @author cm65
 * @date 7 Jan 2013
 * 
 *       Protein Information Management System
 * @version: 4.4
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.model.experiment;

import java.util.Calendar;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.reference.ExperimentType;

/**
 * LockTest
 * 
 */
public class LockTest extends TestCase {

    private static final String UNIQUE = "lock" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    private String username = "User" + UNIQUE;

    /**
     * @param name
     */
    public LockTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    // test: can't update locked page
    public void testExperiment() {
        WritableVersion version = this.model.getTestVersion();
        try {
            Experiment experiment = version.findFirst(Experiment.class, Experiment.PROP_NAME, UNIQUE);
            experiment.setInvoiceNumber(UNIQUE);
            version.flush(); // ensure all DB constraints are exercised
            fail("Updated a locked page");
        } catch (ConstraintException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("locked"));
        } finally {
            version.abort();
        }
    }

    /**
     * LockTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        WritableVersion version = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            User user = new User(version, username);
            UserGroup group = new UserGroup(version, UNIQUE);
            user.addUserGroup(group);
            LabNotebook book = new LabNotebook(version, UNIQUE);
            // note no unlock permission
            new Permission(version, org.pimslims.access.PIMSAccess.READ, book, group);
            new Permission(version, org.pimslims.access.PIMSAccess.UPDATE, book, group);
            ExperimentType type = new ExperimentType(version, UNIQUE);
            Experiment experiment = new Experiment(version, UNIQUE, NOW, NOW, type);
            experiment.setAccess(book);
            experiment.set_prop(Experiment.PROP_STATUS, "OK"); // or experiment.setIsLocked(true); or .setStatus("OK")
            version.commit();
        } catch (ConstraintException e) {
            assertTrue(e.getMessage().contains("locked"));
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * LockTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        // clean up
        WritableVersion version = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            Experiment experiment = version.findFirst(Experiment.class, Experiment.PROP_NAME, UNIQUE);
            if (null != experiment) {
                ExperimentType type = experiment.getExperimentType();
                LabNotebook book = experiment.getAccess();
                experiment.delete();
                type.delete();
                book.delete();
            }

            User user = version.findFirst(User.class, User.PROP_NAME, this.username);
            version.delete(user.getUserGroups());
            user.delete();
            version.commit();
        } catch (ConstraintException e) {
            assertTrue(e.getMessage().contains("locked"));
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        super.tearDown();
    }

    public void testAdminCanUnlock() throws AccessException, AbortedException, ConstraintException {
        // try to unlock
        WritableVersion version = this.model.getTestVersion();
        try {
            Experiment experiment = version.findFirst(Experiment.class, Experiment.PROP_NAME, UNIQUE);
            experiment.set_prop(Experiment.PROP_ISLOCKED, false);
            version.flush();
        } finally {

            version.abort();

        }
    }

    //  can't unlock without permission
    public void testUnlockForbidden() throws AccessException, AbortedException {
        // try to unlock
        WritableVersion version = this.model.getWritableVersion(username);
        try {
            Experiment experiment = version.findFirst(Experiment.class, Experiment.PROP_NAME, UNIQUE);
            experiment.setIsLocked(false);
            version.commit();
            fail("Unlocked, but shouldnt be able to");
        } catch (ConstraintException e) {
            assertTrue(e.getMessage().contains("permission"));
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    // test: unlocking is logged
    public void testUnlockLogged() throws AccessException, AbortedException, ConstraintException {
        WritableVersion version = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            Experiment experiment = version.findFirst(Experiment.class, Experiment.PROP_NAME, UNIQUE);
            LabNotebook book = experiment.getAccess();
            User user = version.findFirst(User.class, User.PROP_NAME, this.username);
            UserGroup group = user.getUserGroups().iterator().next();
            new Permission(version, org.pimslims.access.PIMSAccess.UNLOCK, book, group);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        // try to unlock
        version = this.model.getWritableVersion(username);
        try {
            Experiment experiment = version.findFirst(Experiment.class, Experiment.PROP_NAME, UNIQUE);
            experiment.setIsLocked(false);
            assertEquals(1, experiment.getNotes().size());
            experiment.setInvoiceNumber(UNIQUE); // confirm that it is unlocked
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
