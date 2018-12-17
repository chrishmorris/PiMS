/**
 * V3_2-web org.pimslims.lab AccessRightsUtilityTest.java
 * 
 * @author cm65
 * @date 16 Jun 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.lab;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;

/**
 * AccessRightsUtilityTest
 * 
 */
public class AccessRightsUtilityTest extends TestCase {

    private static final String UNIQUE = "aru" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for AccessRightsUtilityTest
     * 
     * @param name
     */
    public AccessRightsUtilityTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.lab.AccessRightsUtility#getCanCreateOwners(org.pimslims.model.accessControl.User)}.
     */
    public void testGetNoCanCreateOwners() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final User user = new User(version, AccessRightsUtilityTest.UNIQUE);
            final List<String> owners = AccessRightsUtility.getCanCreateOwners(version, user);
            Assert.assertEquals(0, owners.size());
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.lab.AccessRightsUtility#getCanCreateOwners(org.pimslims.model.accessControl.User)}.
     */
    public void testGetCanCreateOwners() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final User user = new User(version, AccessRightsUtilityTest.UNIQUE);
            final UserGroup group = new UserGroup(version, AccessRightsUtilityTest.UNIQUE);
            group.addMemberUser(user);
            final LabNotebook owner = new LabNotebook(version, AccessRightsUtilityTest.UNIQUE + "a");
            AccessRightsUtility.setCan(version, group, "create", owner);
            final List<String> owners = AccessRightsUtility.getCanCreateOwners(version, user);
            Assert.assertEquals(1, owners.size());
            Assert.assertEquals(owner.getName(), owners.iterator().next());
        } finally {
            version.abort();
        }
    }
}
