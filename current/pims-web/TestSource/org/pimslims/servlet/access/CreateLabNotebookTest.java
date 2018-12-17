/**
 * V5_0-web org.pimslims.servlet.access CreateLabNotebookTest.java
 * 
 * @author cm65
 * @date 24 Jan 2013
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.access;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;

/**
 * CreateLabNotebookTest
 * 
 */
public class CreateLabNotebookTest extends TestCase {

    private static final String UNIQUE = "cln" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for CreateLabNotebookTest
     * 
     * @param name
     */
    public CreateLabNotebookTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.access.CreateLabNotebook#create(org.pimslims.dao.WritableVersion, java.lang.String, boolean, boolean, boolean, java.lang.String, java.lang.Integer)}
     * .
     */
    public void testCreate() throws ConstraintException, AccessException {

        final WritableVersion version = this.model.getTestVersion();
        try {
            CreateLabNotebook.create(version, CreateLabNotebookTest.UNIQUE, true, true, true,
                CreateLabNotebookTest.UNIQUE, 6);
            final LabNotebook book =
                version.findFirst(LabNotebook.class, "name", CreateLabNotebookTest.UNIQUE);
            Assert.assertNotNull(book);
            final User user = version.findFirst(User.class, "name", CreateLabNotebookTest.UNIQUE);
            Assert.assertNotNull(user);
            Assert.assertTrue(user.isPassword(CreateLabNotebookTest.UNIQUE));
            final UserGroup group = version.findFirst(UserGroup.class, "name", CreateLabNotebookTest.UNIQUE);
            Assert.assertNotNull(group);
            Assert.assertEquals(user, group.getHeader());
            Assert.assertEquals(new Integer(6), group.getMaxSize());
        } finally {
            version.abort();
        }
    }

}
