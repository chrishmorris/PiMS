/*
 * Created on 07-Jul-2005 @author: Chris Morris
 */
package org.pimslims.access;

import java.io.FileNotFoundException;
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
     * Test with user other than administrator
     * 
     * @Deprecated // no longer supporting WORLD_WRITABLE public void testNonAdmin() {
     * 
     *             WritableVersion wv = null; try { wv = model.getWritableVersion("nonesuch");
     *             fail("Writable version with invalid user"); } catch (IllegalArgumentException e) { //
     *             that's fine } finally { if (null != wv) { wv.abort(); } }
     * 
     *             wv = model.getWritableVersion(USERNAME); attributes.put("name", "nonAdmin" + new
     *             java.util.Date().getTime()); try { ModelObject object =
     *             wv.create(AccessControllerImpl.WORLD_WRITABLE, metaClass.getJavaClass(), attributes);
     * 
     *             assertEquals(USERNAME, ((LabBookEntry) object).getCreator().getName());
     *             assertEquals("owner", AccessControllerImpl.WORLD_WRITABLE, object.get_Owner());
     * 
     *             ((ReadableVersionImpl) wv).getAccessController().resetFirstTimeAccessSearch(true); //
     *             getAll java.util.Collection list = wv.getAll(metaClass.getJavaClass(), 0, 10000);
     *             assertTrue("getAll", list.contains(object));
     * 
     *             // test find java.util.Map<String, Object> criteria = new java.util.HashMap<String,
     *             Object>(); criteria.put("name", attributes.get("name")); java.util.Collection found =
     *             wv.findAll(metaClass.getJavaClass(), criteria); assertTrue("find: " + criteria.get("name"),
     *             found.contains(object)); wv.delete(object); } catch (AssertionError e) {
     *             assertTrue(e.getMessage().contains("all")); } catch (ModelException ex) { Throwable cause =
     *             ex; while (null != cause.getCause()) { cause = cause.getCause(); } cause.printStackTrace();
     *             fail(ex.getMessage()); } catch (RuntimeException ex) { Throwable cause = ex; while (null !=
     *             cause.getCause()) { cause = cause.getCause(); } cause.printStackTrace();
     *             fail(ex.getMessage()); } finally { wv.abort(); } }
     */

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
            //                (java.util.List) ownerObject.findAll(AccessObject.PROP_PERMISSIONS, criteria);
            // TODO assertTrue("some permissions", 0 < permissions.size());

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
        // TODO super.testDesiredRemove();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.AbstractTestAccess#testDesiredDelete()
     */
    @Override
    public void testDesiredDelete() {
        // TODO super.testDesiredDelete();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.AbstractTestAccess#testLaterCantExtend()
     */
    @Override
    public void testLaterCantExtend() {
        // LATER super.testLaterCantExtend();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.AbstractTestAccess#testLaterCantExtend2()
     */
    @Override
    public void testLaterCantExtend2() {
        // LATER super.testLaterCantExtend2();
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
