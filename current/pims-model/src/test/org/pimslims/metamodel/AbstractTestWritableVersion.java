/*
 * Created on 25-Aug-2004 @author: Chris Morris
 */
package org.pimslims.metamodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.persistence.HibernateUtilTester;
import org.pimslims.test.AbstractTestCase;

/**
 * Generic test cases for implementations of WritableVersion. This tests only the public interface. It will be
 * necessary to write extra test cases to cover the state space of an implmeentation adequately.
 * 
 * Note that there are tests of the may methods in TestAccess. Persistence of changes made in a write
 * transaction is tested e.g. in TestReadableVersion.
 * 
 * Implementors must also find a way to test persistence accross the actual close down and restart of the
 * application.
 * 
 * @version 0.1
 */
public abstract class AbstractTestWritableVersion extends AbstractTestCase {

    /**
     * The implementation being tested
     */
    protected AbstractModel model;

    /**
     * hook for object created by setup method
     */
    protected String hook;

    /**
     * model type being tested
     */
    protected final MetaClass metaClass;

    /**
     * name => values, for use when creating an object of the MetaClass
     */
    protected final Map<String, Object> attributes;

    /**
     * time test began
     */
    protected java.sql.Timestamp date;

    /**
     * total number of obejcts in the model
     */
    protected int numObjects = 0;

    /**
     * Subclasses must create a model and call this constructor.
     * 
     * @param model model to test
     * @param name name of test
     * @param metaClassName the name of model type to test
     * @param attributes map name => value to use to create an instance of the MetaClass
     */
    protected AbstractTestWritableVersion(final AbstractModel model, final String name,
        final String metaClassName, final Map<String, Object> attributes) {
        super(name);
        this.model = model;
        this.metaClass = model.getMetaClass(metaClassName);
        this.attributes = attributes;
    }

    /**
     * Get a write transaction
     * 
     * @param username the user the version is for
     * @return a writable version to test
     */
    protected WritableVersion getWritableVersion(String username) {
        return model.getWritableVersion(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() {
        // clear Statistics
        HibernateUtilTester.clearStatistics(this.model);

        assertFalse(metaClass.getMetaRoles().containsKey("project"));

        date = new java.sql.Timestamp(new Date().getTime());
        // create an object
        WritableVersion wv = getWritableVersion(AbstractModel.SUPERUSER);
        assertNotNull("getting transaction", wv);
        try {
            assertNotNull("can get transaction", wv);
            assertTrue("admin", wv.isAdmin());
            assertFalse("date of previous update", wv.getDate().after(date));
            numObjects = wv.getCountOfAll(metaClass);
            ModelObject object = wv.create(metaClass.getJavaClass(), attributes);
            hook = object.get_Hook();
            assertNotNull(hook);
            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() {
        WritableVersion wv = getWritableVersion(AbstractModel.SUPERUSER);
        if (null == wv) {
            // already an error reported
            System.out.println("Can't get transaction");
            return;
        }
        try {
            assertTrue(wv.isAdmin());
            ModelObject object = wv.get(hook);
            // delete the object
            if (object != null) {
                wv.delete(object);
            }
            assertNull("delete", wv.get(hook));
            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        // print statistic
        HibernateUtilTester.getStatistics(model);
        // make sure every session is closed
        // assert stat.getSessionCloseCount()==stat.getSessionOpenCount();
        // HibernateUtil.showStatistics();
        HibernateUtilTester.showStatistics(this.metaClass.getName(), model);
    }

    /**
     * Test basic methods
     * 
     * Note that there is a test of find(MetaClass, Map, List) in TestMetaRole
     */
    public void testBasic() {
        // creation was done in setUp, these tests also test it
        WritableVersion wv = getWritableVersion(Access.ADMINISTRATOR);
        try {

            // get with bad hook
            assertNull("get with bad hook", wv.get("nonesuch"));
            try {
                assertNull("get with unknown hook", wv.get(metaClass.getMetaClassName() + ":999999999"));
            } catch (RuntimeException ex) {
                // that's fine
            }

            // test access methods
            assertEquals("get default admin", Access.REFERENCE, wv.getDefaultOwner(metaClass, null));

            // check creation
            ModelObject object = wv.get(hook);
            assertNotNull("create " + hook, object);
            assertEquals("version", wv, object.get_Version());

            // getAll

            java.util.Collection list = wv.getAll(metaClass.getJavaClass(), 0, 10);
            assertEquals("getAll", numObjects + 1, wv.getCountOfAll(metaClass));
            assertTrue("getAll", list.contains(object));

            // test find

            java.util.Collection found = wv.findAll(metaClass.getJavaClass(), attributes);
            assertTrue("find some", 0 < found.size());
            assertTrue("findAll", found.contains(object));

            wv.commit(); // commited but only read
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }

        }
        // delete will be done and tested in tearDown
    }

    /**
     * Test abort()
     */
    public void testAbort() {
        String hook2;
        WritableVersion wv = getWritableVersion(AbstractModel.SUPERUSER);
        date = new java.sql.Timestamp(new Date().getTime());
        try {
            assertTrue(wv.isAdmin());

            // delete one
            ModelObject object = wv.get(hook);
            wv.delete(object);
            java.util.Collection<ModelObject> objects = new ArrayList<ModelObject>();
            objects.add(object);
            // wv.delete(objects);
            assertNull("delete", wv.get(hook));
            wv.abort();
            wv = getWritableVersion(AbstractModel.SUPERUSER);
            // create one
            ModelObject object2 = create(wv, metaClass.getJavaClass());
            hook2 = object2.get_Hook();

            wv.abort(); // and now cancel those actions

            wv = getWritableVersion(AbstractModel.SUPERUSER);

            assertNull("abort create: " + hook2, wv.get(hook2));
            assertFalse("date of aborted update: " + wv.getDate() + " after: " + date,
                wv.getDate().after(date));
            object = wv.get(hook);
            assertNotNull("abort delete", object);

            java.util.Collection list = wv.getAll(metaClass.getJavaClass(), 0, 10);
            assertTrue("abort delete list", list.contains(wv.get(hook)));

        } catch (ModelException e) {
            e.printStackTrace();
            fail("ModelException");
        } catch (RuntimeException e) {
            e.printStackTrace();
            fail("ModelException");
        } finally {
            wv.abort();
        }

    }

    /**
     * Check hashcode is unique
     */
    public void testHashCode() {

        // test changing an attribute
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        try {
            ModelObject object1 = wv.create(Access.REFERENCE, metaClass.getJavaClass(), attributes);
            ModelObject object2 = wv.create(Access.REFERENCE, metaClass.getJavaClass(), attributes);
            assertTrue(object1.hashCode() != object2.hashCode());
        } catch (final Exception ex) {
            // we don't care where that fails, as long as it does
        } finally {
            wv.abort();
        }

    }

    /**
     * Test features required later
     */

    // LATER test getRecentVersion here,
    // or in TestModel
    // LATER test synchronization of requests from different threads
    /**
     * Test persistence accross the flushing of model buffers. This should be nearly the same as actually
     * restarting.
     */
    public void testFlush() {

        testBasic();
    }

}
