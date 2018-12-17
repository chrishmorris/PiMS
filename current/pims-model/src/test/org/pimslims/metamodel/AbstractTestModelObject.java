/*
 * Created on 25-Aug-2004 @author: Chris Morris
 */
package org.pimslims.metamodel;

import java.util.Map;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;

/**
 * Generic test cases for ModelObject, for use when testing an implementation.. This tests only the public
 * interface. It will be necessary to write extra test cases to cover the state space adequately.
 * 
 * Note that there are no tests for the methods concerned with associations. It is assumed that these simply
 * delegate to MetaRole, which is tested by TestMetaRole.
 * 
 * @version 0.1
 */
public abstract class AbstractTestModelObject extends junit.framework.TestCase {

    /**
     * The model being tested
     */
    protected final AbstractModel model;

    /**
     * model type to test
     */
    protected final MetaClass metaClass;

    /**
     * Subclasses must create a model and call this constructor.
     * 
     * @param model model to test
     * @param name name of test
     * @param metaClassName the name of the type to create an instance of
     * @param attributes names and values to use when creating it
     */
    protected AbstractTestModelObject(final AbstractModel model, final String name,
        final String metaClassName, final Map<String, Object> attributes) {
        super(name);
        this.model = model;
        metaClass = model.getMetaClass(metaClassName);
        this.attributes = attributes;
        attributeToBeTested = this.getTestAttribute();
    }

    protected final Map<String, Object> attributes;

    private String attributeToBeTested;

    /**
     * test operations required for version 0.1
     */
    public void testV0_1() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        assertNotNull(wv);
        try {
            ModelObject testObject = wv.create(Access.REFERENCE, metaClass.getJavaClass(), attributes);

            // getSpecificObject
            assertEquals("getSpecificObject", testObject, testObject

            );

            // getVersion
            assertEquals("getVersion", wv, testObject.get_Version());

            // test single get
            assertEquals("single get", attributes.get(attributeToBeTested),
                testObject.get_Value(attributeToBeTested));

            // test getall
            java.util.Map<String, Object> values = testObject.get_Values();
            assertEquals("getValues", attributes.get(attributeToBeTested).hashCode(),
                values.get(attributeToBeTested).hashCode());

            // test single set
            Object newValue = getNewValue();
            testObject.set_Value(attributeToBeTested, newValue);
            assertEquals("single set", testObject.get_Value(attributeToBeTested).hashCode(),
                newValue.hashCode());

            // test set though a map
            newValue = getNewValue();
            values = new java.util.HashMap<String, Object>();
            values.put(attributeToBeTested, newValue);
            testObject.set_Values(values);
            assertEquals("set though a map", newValue.hashCode(), testObject.get_Value(attributeToBeTested)
                .hashCode());

            // Owner
            assertEquals("owner", Access.REFERENCE, testObject.get_Owner());

        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            wv.abort(); // not testing persistence here
        }
    }

    /**
     * test operations on constraints required for version 0.1
     */
    public void testV0_1Constraints() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        assertNotNull(wv);
        try {
            ModelObject testObject = wv.create(Access.REFERENCE, metaClass.getJavaClass(), attributes);

            Object newValue = getNewValue();
            // get and set
            testObject.set_Value(attributeToBeTested, newValue);

            // constraints
            assertTrue("constraint true", testObject.checkConstraint("a", attributeToBeTested));
            // TODO test it when whole DM is ready
            // assertFalse("constraint false:
            // "+metaClass.getAttribute(attributeToBeTested).getConstraint().toString(),
            // testObject.checkConstraint("a\nb", attributeToBeTested) );
            /*
             * TODO length constraints String tooLong = "tooooooooooooooooooooooooooooooooooooooooooo long";
             * assertFalse("constraint false: "+metaClass.getAttribute("name").getConstraint().toString(),
             * testObject.checkConstraint(tooLong, "name") ); try {
             * testObject.setValue(attributeToBeTested,"a\nb"); fail("ConstrainException not thrown"); } catch
             * (final ConstraintException ex) { // should be thrown }
             */
            assertEquals("get", newValue, testObject.get_Value(attributeToBeTested));

            // LATER assertTrue(testObject.checkInvariant());

        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            wv.abort(); // not testing persistence here
        }
    }

    /**
     * get the attribute going to test
     * 
     * @return
     */
    public abstract String getTestAttribute();

    /**
     * get a new value which should not be repeatable
     * 
     * @return
     */
    public abstract Object getNewValue();
    // note that there is a test with a false invariant in
    // AbstractTestAccess.testLaterCantExtend2()

}
