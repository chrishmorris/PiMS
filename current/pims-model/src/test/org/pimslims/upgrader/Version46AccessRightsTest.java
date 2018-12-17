/**
 * pims-model org.pimslims.upgrader Version43Test.java
 * 
 * @author cm65
 * @date 10 Feb 2012
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.upgrader;

import java.util.Calendar;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.people.Person;

/**
 * Version43Test Tests model changes for PiMS5.0 / PiMSPro0.7
 */
public class Version46AccessRightsTest extends TestCase {

    public static Test suite() {
        TestSuite ret = new TestSuite();
        ret.addTestSuite(Version46AccessRightsTest.class);
        return ret;
    }

    private static final String UNIQUE = "v46_" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    /**
     * @param name
     */
    public Version46AccessRightsTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testUserGroup() throws ConstraintException, AccessException {
        MetaClass metaClass = this.model.getMetaClass(UserGroup.class.getName());
        MetaAttribute attribute = metaClass.getAttribute(UserGroup.PROP_IS_PUBLIC);
        assertNotNull(attribute);

        WritableVersion version = this.model.getTestVersion();
        try {
            UserGroup group = new UserGroup(version, UNIQUE);
            assertFalse(group.getIsPublic());
            group.setIsPublic(Boolean.TRUE);
            version.flush(); // ensure all DB constraints are exercised

            assertEquals(Boolean.TRUE, group.get_Value(UserGroup.PROP_IS_PUBLIC));

            group.set_Value(UserGroup.PROP_IS_PUBLIC, false);
            assertEquals(Boolean.FALSE, group.getIsPublic());

        } finally {
            version.abort();
        }
    }

    public void testAdmin() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            UserGroup group = new UserGroup(version, UNIQUE);
            LabNotebook notebook = new LabNotebook(version, UNIQUE);
            new Permission(version, "admin", notebook, group);
            version.flush(); // ensure all DB constraints are exercised

        } finally {
            version.abort();
        }
    }

    public void testPersonProperties() throws ConstraintException {
        MetaClass metaClass = this.model.getMetaClass(Person.class.getName());
        MetaAttribute attribute = metaClass.getAttribute(Person.PROP_EMAIL);

        WritableVersion version = this.model.getTestVersion();
        try {
            User user = new User(version, UNIQUE);
            assertNull(user.getFamilyName());
            assertNull(user.getGivenName());

            user.setFamilyName(UNIQUE + "f");
            user.setGivenName(UNIQUE + "g");
            version.flush(); // ensure all DB constraints are exercised

            assertEquals(UNIQUE + "f", user.getFamilyName());
            assertEquals(UNIQUE + "g", user.getGivenName());

        } finally {
            version.abort();
        }
    }

    public void testEmail() throws ConstraintException {

        WritableVersion version = this.model.getTestVersion();
        try {
            User user = new User(version, UNIQUE);
            user.setFamilyName(UNIQUE + "f");
            assertNull(user.getEmail());
            assertNull(user.getPerson().get_Value(Person.PROP_EMAIL));

            user.setEmail(UNIQUE + "@somewhere.edu");
            version.flush(); // ensure all DB constraints are exercised

            assertEquals(UNIQUE + "@somewhere.edu", user.getEmail());
            assertEquals(UNIQUE + "@somewhere.edu", user.getPerson().getEmail());

        } finally {
            version.abort();
        }
    }

}
