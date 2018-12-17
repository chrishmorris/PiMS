/*
 * Created on 8-Sept-2004 @author: Chris Morris
 */
package org.pimslims.metamodel;

import java.sql.Timestamp;
import java.util.Map;

import org.hibernate.stat.Statistics;
import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.persistence.HibernateUtilTester;

/**
 * Generic test cases for implementations of ReadableVersion. This tests only the public interface. It will be
 * necessary to write extra test cases to cover the state space of an implementation adequately.
 * 
 * Note that these tests have a lot of dependencies, more than the test cases in TestWritableVersion. If both
 * fail, it would be better to fix the defects found by TestWritableVersion first.
 * 
 * @version 0.1
 */
public abstract class AbstractTestReadableVersion extends junit.framework.TestCase {

    /**
     * The implementation being tested
     */
    protected final AbstractModel model;

    /**
     * hook for object created by setup method
     */
    protected String hook;

    /**
     * model type to test with
     */
    protected final MetaClass metaClass;

    /**
     * values to use when creating teh test object
     */
    protected final Map<String, Object> attributes;

    /**
     * time test began
     */
    protected final Timestamp date;

    /**
     * total number of objects in the model
     */
    protected int numObjects = 0;

    /**
     * Subclasses must create a model and call this constructor.
     * 
     * @param model model to test
     * @param name name of test
     * @param metaClassName the model type to use when testing
     * @param attributes values to use to create an instance
     */
    protected AbstractTestReadableVersion(final AbstractModel model, final String name,
        final String metaClassName, Map<String, Object> attributes

    ) {
        super(name);
        this.model = model;
        this.metaClass = model.getMetaClass(metaClassName);
        assert (metaClass != null);
        date = new Timestamp(System.currentTimeMillis());
        this.attributes = attributes;
    }

    /**
     * Subclasses must create a model and call this constructor.
     * 
     * @param model model to test
     * @param name name of test
     * 
     *            protected AbstractTestReadableVersion(final AbstractModel model, final String name) {
     *            this(model, name, "uk.ac.pims.access.User", AbstractTestAccess.USER_MAP); }
     */

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() {
        HibernateUtilTester.clearStatistics(this.model);
        // create an object for testing with
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            // LATER assertFalse("date of previous update",
            // wv.getDate().after(date) );
            numObjects = wv.getCountOfAll(metaClass);
            ModelObject object = wv.create(Access.REFERENCE, metaClass.getJavaClass(), attributes);

            hook = object.get_Hook();
            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        checkClosed(model);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() {
        checkClosed(model);
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        assertNotNull(wv);
        if (wv != null) {
            try {
                assertTrue(wv.isAdmin());
                // assertFalse("date of create: "+wv.getDate()+" before: "+date,
                // wv.getDate().before(date) );
                if (null != hook) {
                    ModelObject object = wv.get(hook);
                    // delete the object
                    if (object != null) {
                        wv.delete(object);
                    }
                }
                wv.commit();
            } catch (ModelException ex) {
                fail(ex.toString());
            } finally {
                if (!wv.isCompleted()) {
                    wv.abort();
                }
            }
        }

        // print statistic
        checkClosed(model);
        // HibernateUtil.showStatistics();
        // HibernateUtil.showStatistics( this.metaClass.getName());
    }

    public static void checkClosed(AbstractModel model) {
        Statistics stat = HibernateUtilTester.getStatistics(model);
        // make sure every session is closed
        assertEquals("Session is not closed", stat.getSessionCloseCount(), stat.getSessionOpenCount());
    }

    /**
     * Test basic methods
     * 
     * Note that there is a test of WritableVersion.find(MetaClass, Map, List) in TestMetaRole - that is
     * probably sufficient
     */
    public void testSearchForNull() {
        ReadableVersion rv = model.getReadableVersion(AbstractModel.SUPERUSER);
        try {
            assertTrue("admin", rv.isAdmin());
            assertFalse("date of create: " + rv.getDate() + " before " + date, !rv.getDate().after(date));

            ModelObject object = rv.get(hook);
            assertNotNull(object);
            assertEquals("version", rv, object.get_Version());

            // test find
            java.util.Collection found = rv.findAll(metaClass.getJavaClass(), attributes);
            assertTrue("findAll", found.contains(object));
            Map<String, Object> nonesuch = new java.util.HashMap<String, Object>(attributes);
            java.util.List<String> keys = new java.util.ArrayList<String>(attributes.keySet());
            nonesuch.put(keys.get(1), null);
            found = rv.findAll(metaClass.getJavaClass(), nonesuch);
            assertEquals("not found", 0, found.size());

            // test get all and the results should be order by ID (desc)
            found = rv.getAll(metaClass.getJavaClass(), 0, 10);
            assertTrue("getAll", found.contains(rv.get(hook)));
            assertTrue(((ModelObject) (found.iterator().next())).get_Hook().equals(hook));

            rv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
        // test abort()
        rv = model.getReadableVersion(AbstractModel.SUPERUSER);
        rv.abort();
    } // */

    /**
     * A simple implementation might supply a WritableVersion as a readable one. This could create a security
     * hole. Test that this vulnerability does not exist.
     */
    public void testCantCast() {
        ReadableVersion rv = model.getReadableVersion(AbstractModel.SUPERUSER);
        try {
            WritableVersion wv = (WritableVersion) rv;

            // delete something
            ModelObject object = wv.get(hook);
            wv.delete(object);

            wv.commit();
            fail("No exception, casting ReadableVersion to WritableVersion");
        } catch (Exception e) {
            // we dont care where that fails, as long as it does
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    /**
     * Check that you cant use a readable version to write with
     */
    public void testCantWrite() {

        // test changing an attribute
        ReadableVersion rv = model.getReadableVersion(AbstractModel.SUPERUSER);
        String attributeName = attributes.keySet().iterator().next();
        try {
            ModelObject object = rv.get(hook);
            object.set_Value(attributeName, attributes.get(attributeName));
            rv.commit();
            fail("No exception when altering an attribute");
        } catch (final ModelException ex) {
            // we don't care where that fails, as long as it does
            rv.abort();
        } catch (final UnsupportedOperationException ex) {
            // or one of these would be fine
            rv.abort();
        } catch (final NullPointerException ex) {
            fail("Attribute not found - please fix this test");
            rv.abort();
        } catch (final Exception ex) {
            ex.printStackTrace(); // LATER remove
            // we don't care where that fails, as long as it does
            rv.abort();
        }

    }

    // LATER test getPreviousVersion

}
