/*
 * Created on 25-Aug-2004 @author: Chris Morris
 */
package org.pimslims.metamodel;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;

/**
 * Generic test cases for Model, for use when testing an implementation of Model. This tests only the public
 * interface. When implementing Model, it will be necessary to write extra test cases to cover the state space
 * adequately.
 * 
 * This is the fundamental set of test cases - you should ensure these succeed before working on other test
 * cases.
 * 
 * @version 0.1
 */
public class AbstractTestModel extends junit.framework.TestCase {

    /**
     * The implementation being tested
     */
    protected final AbstractModel model;

    /**
     * Subclasses must create a model and call this constructor.
     * 
     * @param model model to test
     * @param name name of test
     */
    protected AbstractTestModel(final AbstractModel model, final String name) {
        super(name);
        this.model = model;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() {
        // implementations may need this method
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() {
        // model.disconnect();
    }

    /**
     * Test features required for version 0.1
     */
    public void testV0_1() {

        /*
         * TODO test getMetaClasses() and getMetaClass(String)
         */
        try {
            model.getMetaClass("nonesuch");
            fail("get non-existent metaclass");
        } catch (AssertionError e) {
            // that's fine
        }

        // test getReadableVersion(String)
        ReadableVersion rv = model.getReadableVersion(AbstractModel.SUPERUSER);
        assertNotNull("readable version", rv);
        try {
            assertTrue(rv.isAdmin());
        } finally {
            rv.abort();
        }

        // test getReportVersion(String)
        rv = model.getReportVersion(AbstractModel.SUPERUSER);
        assertNotNull("report version", rv);
        try {
            assertTrue(rv.isAdmin());
        } finally {
            rv.abort();
        }

        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        assertNotNull("writable version", wv);
        assertFalse(wv.isCompleted());
        try {
            // test getWritableVersion(String)
            assertTrue(wv.isAdmin());
            // the functionality of WritableVersion is tested elsewhere
        } finally {
            wv.abort();
        }

    }

    /**
     * Test features required later
     */
    public void testRequiredLater() {

        /*
         * TODO test getModelUpdateVersion() ModelUpdateVersion uv = model.getModelUpdateVersion(); try {
         * assertTrue(uv.isAdmin()); } finally { uv.abort(); }
         */

        // test getReadableVersion(String, Date)
        ReadableVersion rv = model.getReadableVersion(AbstractModel.SUPERUSER);
        java.util.Date date = rv.getDate();
        ReadableVersion rv2 = model.getReadableVersion(AbstractModel.SUPERUSER, date);
        assertEquals("getReadableVersion(String, Date)", rv, rv2);
        rv.abort();
        rv2.abort();
    }

}
