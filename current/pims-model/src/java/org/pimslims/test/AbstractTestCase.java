/**
 * org.pimslims.generatedApi AbstractTestCase.java
 * 
 * @date 30-Oct-2006 14:20:00
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 *         Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 *           Copyright (c) 2006
 * 
 *           This library is free software; you can redistribute it and/or modify it under the terms of the
 *           GNU Lesser General Public License as published by the Free Software Foundation; either version
 *           2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.test;

import junit.framework.TestCase;

import org.pimslims.access.PIMSAccess;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.people.Person;

/**
 * AbstractTestCase
 * 
 */
public class AbstractTestCase extends TestCase {
    static final boolean printlog = false;

    /**
     * The model being tested
     */
    public static final AbstractModel model = ModelImpl.getModel();

    protected static final String UNIQUE = "test" + System.currentTimeMillis();

    protected WritableVersion wv = null;

    protected ReadableVersion rv = null;

    public AbstractTestCase() {
        super();
    }

    /**
     * @param name
     */
    public AbstractTestCase(String name) {
        super(name);
    }

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        if (!isAllSessionsAreClosed())
            System.out.println("Not all session are closed before this test:" + this.getName());

    }

    /*
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        try {
            // delete the saved records
            POJOFactory.removeTestingRecords(this.getName());
            if (!isAllSessionsAreClosed())
                System.out.println("1 or more session is not closed during this test:" + this.getName());
            /*
             * if(!isAllSessionsAreClosed()) fail("1 or more session is not closed during this test!");
             */
        } finally {
            super.tearDown();
        }

    }

    /**
     * Create a mock object with minimum attributes/roles only using current wv with a provided attribute/role
     * 
     * @param wv writableversion
     * @param javaClass
     * @return a mock modelObject with minimum attributes/roles only
     * @throws ConstraintException
     * @throws AccessException
     */
    public <T extends ModelObject> T create(Class javaClass, String propertyName, Object value)
        throws AccessException, ConstraintException {
        assertTrue(this.wv != null);
        assertTrue(!this.wv.isCompleted());
        return POJOFactory.<T> create(this.wv, javaClass, propertyName, value);
    }

    /**
     * Create a mock object with minimum attributes/roles only using current wv
     * 
     * @param wv writableversion
     * @param javaClass
     * @return a mock modelObject with minimum attributes/roles only
     * @throws ConstraintException
     * @throws AccessException
     */
    public <T extends ModelObject> T create(Class javaClass) throws AccessException, ConstraintException {
        assertTrue(this.wv != null);
        assertTrue(!this.wv.isCompleted());
        return POJOFactory.<T> create(this.wv, javaClass);
    }

    /**
     * Create a mock object with minimum attributes/roles only with a provided attribute/role
     * 
     * @param wv writableversion
     * @param javaClass
     * @return a mock modelObject with minimum attributes/roles only
     * @throws ConstraintException
     * @throws AccessException
     */
    public static <T extends ModelObject> T create(WritableVersion wv, Class javaClass, String propertyName,
        Object value) throws AccessException, ConstraintException {
        return POJOFactory.<T> create(wv, javaClass, propertyName, value);
    }

    /**
     * Create a mock object with minimum attributes/roles only
     * 
     * @param wv writableversion
     * @param javaClass
     * @return a mock modelObject with minimum attributes/roles only
     * @throws ConstraintException
     * @throws AccessException
     */
    public static <T extends ModelObject> T create(WritableVersion wv, Class javaClass)
        throws AccessException, ConstraintException {
        return POJOFactory.<T> create(wv, javaClass);
    }

    /**
     * Create a mock object with minimum roles but all attributes by using current wv
     * 
     * @param wv writableversion
     * @param javaClass
     * @return a mock modelObject with minimum roles but all attributes
     * @throws ConstraintException
     * @throws AccessException
     */
    public <T extends ModelObject> T createFull(Class javaClass) throws AccessException, ConstraintException {
        assertTrue(this.wv != null);
        assertTrue(!this.wv.isCompleted());
        return POJOFactory.<T> createWithFullAttributes(this.wv, javaClass);
    }

    /**
     * Create a mock object with minimum roles but all attributes
     * 
     * @param wv writableversion
     * @param javaClass
     * @return a mock modelObject with minimum roles but all attributes
     * @throws ConstraintException
     * @throws AccessException
     */
    public static <T extends ModelObject> T createFull(WritableVersion wv, Class javaClass)
        throws AccessException, ConstraintException {
        return POJOFactory.<T> createWithFullAttributes(wv, javaClass);
    }

    protected void log(String msg) {

        if (printlog)
            System.out.println(getName() + ":" + msg);
    }

    /**
     * @return a WritableVersion using a
     */
    protected WritableVersion getWV() {
        return model.getWritableVersion(AbstractModel.SUPERUSER);
    }

    /**
     * @return a WritableVersion using a user name
     * @throws ConstraintException
     * @throws AbortedException
     */
    protected WritableVersion getWV(String userName) throws AbortedException, ConstraintException {
        /* if (!userName.equals(AbstractModel.SUPERUSER)) {
             WritableVersion version = getWV();
             User user = version.findFirst(User.class, User.PROP_NAME, userName);
             try {
                 if (user == null) {
                     user = new User(version, userName);
                     version.commit();
                 } else {
                     version.abort();
                 }
             } finally {
                 if (!version.isCompleted())
                     version.abort();
             }
         } */
        return model.getWritableVersion(userName);
    }

    /**
     * @return a ReadableVersion using SUPERUSER
     */
    protected ReadableVersion getRV() {
        return model.getReadableVersion(AbstractModel.SUPERUSER);
    }

    /**
     * @return a ReadableVersion using a user name
     */
    protected ReadableVersion getRV(String userName) {
        return model.getReadableVersion(userName);
    }

    /**
     * 
     * @return is all session are closed
     */
    private boolean isAllSessionsAreClosed() {
        return model.isAllSessionsClosed();

    }

    /**
     * PlateSampleSearchTest.createUser
     * 
     * @return
     * @throws ConstraintException
     * @throws AccessException
     * @throws AbortedException
     */
    protected String createUser() throws AccessException, ConstraintException, AbortedException {
        WritableVersion wv = getWV();
        String userName;
        try {
            //create  new accessobject
            LabNotebook access_g1 = create(wv, LabNotebook.class);

            //Create  read permissions on each accessobject
            Permission permission1_read =
                create(wv, Permission.class, Permission.PROP_OPTYPE, PIMSAccess.READ);
            permission1_read.setAccessObject(access_g1);

            //Create  create permissions on each accessobject
            Permission permission1_create =
                create(wv, Permission.class, Permission.PROP_OPTYPE, PIMSAccess.CREATE);
            permission1_create.setAccessObject(access_g1);

            //Create  update permissions on each accessobject
            Permission permission1_UPDATE =
                create(wv, Permission.class, Permission.PROP_OPTYPE, PIMSAccess.UPDATE);
            permission1_UPDATE.setAccessObject(access_g1);

            //create  userGroup linked with each permission
            UserGroup usergroup1 = create(wv, UserGroup.class);
            usergroup1.addPermission(permission1_read);
            usergroup1.addPermission(permission1_create);
            usergroup1.addPermission(permission1_UPDATE);

            User user1 = create(wv, User.class, User.PROP_USERGROUPS, usergroup1);
            Person person = create(wv, Person.class);
            user1.setPerson(person);
            userName = user1.getName();
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
        return userName;
    }
}
