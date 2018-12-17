/**
* V4_4-web org.pimslims.servlet.orders OrderPrimersTest.java
 * 
 * @author cm65
 * @date 16 Oct 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.orders;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;

/**
 * OrderPrimersTest
 * 
 */
public class OrderPrimersTest extends TestCase {

    private final AbstractModel model;

    private final String userName = "op" + System.currentTimeMillis();

    /**
     * Constructor for OrderPrimersTest
     * 
     * @param name
     */
    public OrderPrimersTest(final String name) {
        super(name);
        this.model = org.pimslims.dao.ModelImpl.getModel();
    }

    /**
     * OrderPrimersTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final WritableVersion version =
            this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final User user = new User(version, this.userName);
            final UserGroup group = new UserGroup(version, this.userName);
            group.addMemberUser(user);
            final LabNotebook book = new LabNotebook(version, this.userName);
            new Permission(version, "read", book, group);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * OrderPrimersTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        final WritableVersion version =
            this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final User user = version.findFirst(User.class, User.PROP_NAME, this.userName);
            final UserGroup group = user.getUserGroups().iterator().next();
            final Set<Permission> permissions = group.getPermissions();
            final Set<LabNotebook> books = new HashSet();
            for (final Iterator iterator = permissions.iterator(); iterator.hasNext();) {
                final Permission permission = (Permission) iterator.next();
                books.add(permission.getLabNotebook());
                permission.delete();
            }
            for (final Iterator iterator = books.iterator(); iterator.hasNext();) {
                final LabNotebook labNotebook = (LabNotebook) iterator.next();
                labNotebook.delete();
            }
            group.delete();
            user.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        super.tearDown();
    }

    public void testPrivate() {
        final WritableVersion version = this.model.getWritableVersion(this.userName);
        try {
            final HashMap<String, String> primers = OrderPrimers.getPrimerOrderList(version);
            Assert.assertEquals(0, primers.size());
        } finally {
            version.abort();
        }
    }

    public void testAll() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final HashMap<String, String> primers = OrderPrimers.getPrimerOrderList(version);
            Assert.assertTrue(0 < primers.size());
        } finally {
            version.abort();
        }
    }

}
