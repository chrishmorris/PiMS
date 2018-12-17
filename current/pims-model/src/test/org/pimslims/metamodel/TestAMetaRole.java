/*
 * Created on 25-Aug-2004 @author: Chris Morris
 */
package org.pimslims.metamodel;

import java.util.Iterator;
import java.util.Map;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.test.AbstractTestCase;

/**
 * Test cases for an association.
 * 
 * TODO the access rules for roles are not tested here.
 * 
 * @version 0.1
 */
public abstract class TestAMetaRole extends AbstractTestCase {

    /**
     * The model being tested
     */
    protected final AbstractModel model;

    /**
     * model type this end of the association
     */
    protected final MetaClass thisType;

    /**
     * model type for other end of the association
     */
    protected final MetaClass otherType;

    /**
     * the name used to find the metarole
     */
    protected final String metaRoleName;

    private final Map<String, Object> thisAttributes;

    private final Map<String, Object> otherAttributes;

    /**
     * MetaRole representing the association under test
     */
    protected final MetaRole metaRole;

    /**
     * Associated objects created in setup
     */
    protected final java.util.Set<String> associates = new java.util.HashSet<String>();

    /**
     * Subclasses must create a model and call this constructor.
     * 
     * @param model the model being tested
     * @param metaClassName name of type that has the role to test
     * @param metaRoleName name of role to test
     * @param thisAttributes name => value for attributes to create object at this end of association
     * @param otherAttributes name => value for attributes to create object at other end of association
     * @param name name of test
     */
    protected TestAMetaRole(final AbstractModel model, final String metaClassName, final String metaRoleName,
        final Map<String, Object> thisAttributes, final Map<String, Object> otherAttributes, final String name) {
        super(name);
        this.model = model;
        // modelObject = model.getRootMetaClass();/
        thisType = model.getMetaClass(metaClassName);
        metaRole = thisType.getMetaRole(metaRoleName);
        assert metaRole != null : " no role for " + metaRoleName;
        otherType = metaRole.getOtherMetaClass();
        this.thisAttributes = thisAttributes;
        this.otherAttributes = otherAttributes;
        this.metaRoleName = metaRoleName;
    }

    /**
     * Utility method to help make associated objects required for testing.
     * 
     * @param javaClass type of object to create
     * @param attributes map name => value to use to create it
     * @return the hook of the new object
     */
    protected String setUpAssociate(final Class javaClass, final Map<String, Object> attributes) {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        String hook = null; // value to return
        try {
            ModelObject object = wv.create(javaClass, attributes);
            hook = object.get_Hook();
            wv.commit();
            associates.add(hook);
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        return hook;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() {

        // delete all the associates
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        if (null == wv) {
            return;
        } // Don't hide previous error
        try {
            for (Iterator iter = associates.iterator(); iter.hasNext();) {
                String hook = (String) iter.next();
                ModelObject object = wv.get(hook);
                if (null != object) {
                    wv.delete(object);
                }
            }
            wv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        } // */
    }

    /**
     * First test the simple methods, that don't need a version
     */
    public void testSimple() {

        assertEquals("name", metaRoleName, metaRole.getRoleName());
        assertEquals("getOwnMetaClass", thisType, metaRole.getOwnMetaClass());
        assertNotNull("getOtherType", otherType);
        assertEquals("getOtherRole", thisType, metaRole.getOtherRole().getOtherMetaClass());
        assertEquals("getOtherRole", metaRole.getOtherRole().getOtherRole(), metaRole);

    }

    /**
     * test the methods that need access to a model object
     */
    public void testVersion() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            doTestVersion(wv);
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            wv.abort();
        }
    }

    protected void doTestVersion(WritableVersion wv) throws AccessException, ConstraintException {

        ModelObject thisObject = createThisObject(wv, thisAttributes);
        ModelObject otherObject = createOtherObject(wv, this.otherAttributes);

        // test adding
        assertFalse("areAssociated", metaRole.areAssociated(thisObject, otherObject));
        assertEquals("get", 0, metaRole.get(thisObject).size());

        metaRole.add(thisObject, otherObject);
        assertEquals("add one", 1, metaRole.get(thisObject).size());
        assertTrue("add one", metaRole.areAssociated(thisObject, otherObject));
        // TODO test verify assertEquals("verify", metaRole.verify(wv),
        // metaRole.getOtherRole().verify(wv)); 

        assertEquals("get", thisObject.get(metaRoleName).size(), 1);
        assertTrue("get", thisObject.get(metaRoleName).contains(otherObject));
        assertEquals("findAll match", 1, thisObject.findAll(metaRoleName, otherAttributes).size());
        assertTrue("findAll", thisObject.findAll(metaRoleName, otherAttributes).contains(otherObject));

        String otherMetaRoleName = metaRole.getOtherRole().getRoleName();
        // now check the other role
        assertEquals("get reverse role", 1, otherObject.get(otherMetaRoleName).size());
        assertTrue("get reverse role", otherObject.get(otherMetaRoleName).contains(thisObject));
        assertEquals("findAll match reverse", 1, otherObject.findAll(otherMetaRoleName, thisAttributes)
            .size());
        assertTrue("findAll", otherObject.findAll(otherMetaRoleName, thisAttributes).contains(thisObject));

        // LATER test find join
        /*
         * Map criteria = new java.util.HashMap(); criteria.put("User.name", USERNAME ); java.util.Collection
         * found = wv.findAll(userGroup, criteria, java.util.Arrays.asList(new MetaRole[] {groupRole,
         * userRole}) ); assertEquals("find join", 1, found.size()); // should also test when should be none //
         */

        // test metarole remove
        metaRole.remove(thisObject, otherObject);
        assertEquals("remove one", 0, metaRole.get(thisObject).size());
        assertFalse("remove one", metaRole.areAssociated(thisObject, otherObject));
        assertEquals("findAll no match", 0, thisObject.findAll(metaRoleName, otherAttributes).size());

        // test modelobject add and remove
        thisObject.add(metaRoleName, otherObject);
        assertEquals("add one", 1, metaRole.get(thisObject).size());
        assertTrue("add one", thisObject.isAssociated(metaRoleName, otherObject));
        thisObject.remove(metaRole.getRoleName(), otherObject);
        assertEquals("remove one", 0, metaRole.get(thisObject).size());
        assertFalse("remove one", thisObject.isAssociated(metaRoleName, otherObject));

        // add and remove list
        java.util.Set<ModelObject> set = new java.util.HashSet<ModelObject>();
        set.add(otherObject);
        thisObject.add(metaRoleName, set);
        assertEquals("add set", 1, metaRole.get(thisObject).size());
        thisObject.remove(metaRole.getRoleName(), set);
        assertEquals("remove set", 0, metaRole.get(thisObject).size());

        // test delete while added
        thisObject.add(metaRoleName, otherObject);
        otherObject.delete();
        assertEquals("delete removes", 0, thisObject.get(metaRoleName).size());
        assertEquals("delete removes", 0, metaRole.get(thisObject).size());

        /*
         * TODO test inconsistent commit assertFalse("verify false",userRole.getOtherRole().verify(wv)); // a
         * u_ug needs a user try { wv.commit(); fail("inconsistent commit"); } catch (ConstraintException ex) { //
         * that's fine wv.abort(); // not testing persistence here }
         */

    }

    public void testPersistence() {
        String thisHook = null;
        String otherHook = null;

        // add
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            ModelObject thisObject = createThisObject(wv, thisAttributes);
            thisHook = thisObject.get_Hook();
            ModelObject otherObject = createOtherObject(wv, this.otherAttributes);
            otherHook = otherObject.get_Hook();

            metaRole.add(thisObject, otherObject);
            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } catch (RuntimeException ex) {
            Throwable cause = ex;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            cause.printStackTrace();
            fail(cause.toString());
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }

        System.gc();
        System.gc();
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            ModelObject thisObject = wv.get(thisHook);
            ModelObject otherObject = wv.get(otherHook);
            // check added
            assertEquals("add one", 1, metaRole.get(thisObject).size());
            assertTrue("add one", metaRole.areAssociated(thisObject, otherObject));

            // now delete one end
            // metaRole.remove(thisObject, otherObject); //TODO should not be
            // necessary
            wv.delete(otherObject);
            wv.commit();
        } catch (ModelException ex) {
            // the association has prevented the deletion
            fail(ex.toString());
        } catch (RuntimeException ex) {
            Throwable cause = ex;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            cause.printStackTrace();
            fail(cause.toString());
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }

        // check deleting one removed the association
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            ModelObject thisObject = wv.get(thisHook);
            assertEquals("removed by delete", 0, metaRole.get(thisObject).size());

            // finally clean up
            wv.delete(thisObject);
            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } catch (RuntimeException ex) {
            Throwable cause = ex;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            cause.printStackTrace();
            fail(cause.toString());
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }

    protected ModelObject createOtherObject(WritableVersion wv, Map<String, Object> otherAttributes)
        throws AccessException, ConstraintException {

        return wv.create(this.otherType.getJavaClass(), otherAttributes);
    }

    protected ModelObject createThisObject(WritableVersion wv, final Map<String, Object> thisAttributes)
        throws AccessException, ConstraintException {
        return wv.create(this.thisType.getJavaClass(), thisAttributes);
    }

}
