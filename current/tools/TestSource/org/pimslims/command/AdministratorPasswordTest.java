/**
 * tools org.pimslims.command AdministratorPasswordTest.java
 * 
 * @author cm65
 * @date 7 Dec 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.command;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;

/**
 * AdministratorPasswordTest
 * 
 */
public class AdministratorPasswordTest extends TestCase {

    private static final String UNIQUE = "ap" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for AdministratorPasswordTest
     * 
     * @param name
     */
    public AdministratorPasswordTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testSave() throws ConstraintException, AccessException {
        WritableVersion version = this.model.getTestVersion();
        try {
            new AdministratorPassword(version).save(UNIQUE + "u", UNIQUE + "p");
            User user = version.findFirst(User.class, User.PROP_NAME, UNIQUE + "u");
            assertNotNull(user);
            assertTrue(user.isPassword(UNIQUE + "p"));
            new AdministratorPassword(version).save(UNIQUE + "u", UNIQUE + "p2");
            assertTrue(user.isPassword(UNIQUE + "p2"));
        } finally {
            version.abort();
        }
    }

}
