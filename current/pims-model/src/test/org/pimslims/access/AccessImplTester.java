/*
 * Created on 07-Jul-2005 @author: Chris Morris
 */
package org.pimslims.access;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.textui.TestRunner;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.ReadableVersionImpl;
import org.pimslims.exception.AccessException;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.people.Organisation;

/**
 * Test the implementation of access rights
 * 
 */
public class AccessImplTester extends org.pimslims.metamodel.AbstractTestAccess {

    static AbstractModel model;

    public static final HashMap<String, Object> ATTRIBUTES = new HashMap<String, Object>();
    static {
        ATTRIBUTES.put(Organisation.PROP_NAME, System.currentTimeMillis() + "test");
        ATTRIBUTES.put(Organisation.PROP_ADDRESSES,
            java.util.Arrays.asList(new String[] { "10 Downing Street", "Gotham City" }));
        ATTRIBUTES.put(Organisation.PROP_ORGANISATIONTYPE,
            "testOrganisationType" + System.currentTimeMillis());

    }
    static {
        try {
            model = ModelImpl.getModel(new java.io.File("conf/Properties"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param methodName test method to run
     */
    public AccessImplTester(String methodName) {
        super(model, methodName, org.pimslims.model.people.Organisation.class.getName(), ATTRIBUTES, "name");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.AbstractTestAccess#testV0_2()
     */
    @Override
    public void testV0_2() throws AccessException {
        // do this first
        super.testV0_2();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.AbstractTestAccess#testV0_3()
     */
    @Override
    public void testV0_3() {
        // do this second
        super.testV0_3();
    }

    /**
     * Tests for some of the protected methods in AccessImpl
     */
    public void testImpl() {

        ReadableVersion rv = model.getWritableVersion(USERNAME);
        try {
            /*
             * TODO findAllAccessObjects try { List owners =
             * ((ModelImpl)model).getMemopsProject(rv).findAllAccessObjects("name",
             * "nonesuch"); assertEquals("no owners called nonesuch", 0,
             * owners.size()); } catch (ApiException e) { fail(e.getMessage()); }
             */
            LabNotebook ownerObject = rv.getOwner(OWNER);
            assertNotNull("owner", ownerObject);
            User userObject = ((ReadableVersionImpl) rv).getUser(USERNAME);
            assertNotNull("user", userObject);
            Map<String, Object> criteria = new HashMap<String, Object>();
            assertNotNull("group created", groupHook);
            criteria.put("userGroup", rv.get(groupHook));

            Collection<Permission> permissions = ownerObject.findAll(LabNotebook.PROP_PERMISSIONS, criteria);
            assertTrue("some permissions", 0 < permissions.size());

            permissions = rv.findAll(Permission.class, LabNotebook.PROP_PERMISSIONS, criteria);
            assertTrue("some permissions", 0 < permissions.size());

            assertTrue("may create",
                ((AccessImpl) model.getAccess()).isPermitted(PIMSAccess.CREATE, rv, OWNER));

            /*
             * ModelObject permission = (ModelObject)permissions.get(0);
             * assertEquals("permission name", GROUP_NAME+" "+"create"+" "+OWNER ,
             * permission.getName() );
             */
        } finally {
            rv.abort();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.AbstractTestAccess#testDesiredRemove()
     */
    @Override
    public void testDesiredRemove() {
        super.testDesiredRemove();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.AbstractTestAccess#testDesiredDelete()
     */
    @Override
    public void testDesiredDelete() {
        //TODO super.testDesiredDelete();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.AbstractTestAccess#testLaterCantExtend()
     */
    @Override
    public void testLaterCantExtend() {
        //TODO super.testLaterCantExtend();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.AbstractTestAccess#testLaterCantExtend2()
     */
    @Override
    public void testLaterCantExtend2() {
        //TODO super.testLaterCantExtend2();
    }

    /**
     * @return a suite including all tests for this class
     */
    public static Test suite() {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(AccessImplTester.class);
        TestSetup wrapper = new TestSetup(suite) {
            @Override
            protected void setUp() {
                suiteSetUp(model);
            }

            @Override
            protected void tearDown() {
                suiteTearDown(model);
                // was model.disconnect();
            }
        };
        return wrapper;
    }

    /**
     * Runs these tests from the command line
     * 
     * @param args ignored
     */
    public static void main(final String[] args) {
        TestRunner.run(suite());
    }

}
