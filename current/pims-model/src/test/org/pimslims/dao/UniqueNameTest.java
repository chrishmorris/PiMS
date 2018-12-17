/**
 * pims-dm org.pimslims.dao UniqueNameTest.java
 * 
 * @author cm65
 * @date 18 Jun 2010
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.dao;

import java.util.Calendar;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;

/**
 * UniqueNameTest
 * 
 */
public class UniqueNameTest extends TestCase {

    private static final String UNIQUE = "un" + System.currentTimeMillis() + "un";

    private static final String TEN = "ottffssent";

    private static final String USERNAME = UNIQUE + "u";

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    /**
     * Constructor for UniqueNameTest
     * 
     * @param name
     */
    public UniqueNameTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testTwoClashes() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            new Sample(version, UNIQUE);
            new Sample(version, version.getUniqueName(Sample.class, UNIQUE));
            new Sample(version, version.getUniqueName(Sample.class, UNIQUE));
            version.flush();
        } finally {
            version.abort();
        }
    }

    public void testHidden() throws ConstraintException, AbortedException, AccessException {
        String sampleHook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            LabNotebook noteBook = new LabNotebook(version, UNIQUE + "one");
            version.setDefaultOwner(noteBook);
            Sample sample = new Sample(version, UNIQUE + " " + USERNAME + " 1");
            sampleHook = sample.get_Hook();
            new User(version, USERNAME);
            version.commit();
        } finally {
            if (!version.isCompleted())
                version.abort();
        }

        version = this.model.getWritableVersion(USERNAME);
        try {
            String nextName = version.getUniqueName(Sample.class, UNIQUE);
            new Sample(version, nextName);
            version.flush();
        } finally {
            version.abort();
        }

        // tidy up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            User user = (User) version.findFirst(User.class, User.PROP_NAME, USERNAME);
            user.delete();
            Sample sample = (Sample) version.get(sampleHook);
            LabNotebook noteBook = sample.getAccess();
            sample.delete();
            noteBook.delete();
            version.commit();
        } finally {
            if (!version.isCompleted())
                version.abort();
        }
    }

    public void testSpaces() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            ExperimentType type = new ExperimentType(version, UNIQUE + " and some spaces");
            String ename = version.getUniqueName(Experiment.class, type.getName());
            new Experiment(version, ename, NOW, NOW, type);
            ename = version.getUniqueName(Experiment.class, type.getName());
            String second = new Experiment(version, ename, NOW, NOW, type).getName();
            assertEquals(type.getName() + " administrator 2", second);
        } finally {
            version.abort();
        }

    }

    public void testNextNameLongName() {
        final String newName =
            WritableVersionImpl.nextName("tag", TEN + TEN + TEN + TEN + TEN + TEN + TEN + TEN);
        Assert.assertEquals(TEN + TEN + TEN + TEN + TEN + TEN + TEN + "ottf" + " tag 1", newName);
    }

}
