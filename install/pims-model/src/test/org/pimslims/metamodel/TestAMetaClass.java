/*
 * Created on 25-Aug-2004 @author: Chris Morris
 */
package org.pimslims.metamodel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.stat.Statistics;
import org.pimslims.constraint.Constraint;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.persistence.HibernateUtilTester;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * Generic test cases for a model type, for use when testing an implementation.
 * 
 * See also TestAMetaRole
 * 
 * @version 0.1
 */
public class TestAMetaClass extends AbstractTestCase {

    /**
     * The model being tested
     */
    protected final AbstractModel model;

    /**
     * root model type
     */
    protected final MetaClass modelObject;

    /**
     * model type being tested
     */
    protected final Class javaClass;

    /**
     * model type being tested
     */
    protected final MetaClass metaClass;

    /**
     * name => values, for use when creating an object of the MetaClass
     */
    protected final Map<String, Object> attributes;

    /**
     * Subclasses must create a model and call this constructor.
     * 
     * @param metaClassName the name of model type to test
     * @param model model to test
     * @param name name of test
     */
    protected TestAMetaClass(final String metaClassName, final AbstractModel model, final String name) {
        this(metaClassName, model, name, new HashMap<String, Object>());
    }

    /**
     * Subclasses must create a model and call this constructor.
     * 
     * @param metaClassName the name of model type to test
     * @param model model to test
     * @param methodName test method to call, all if null
     * @param attributes map name => value to use to create an instance of the MetaClass. If a value is not
     *            provided for a required attribute, this class will try to provide one.
     */
    protected TestAMetaClass(final String metaClassName, final AbstractModel model, final String methodName,
        final Map<String, Object> attributes) {
        super(methodName);
        this.model = model;
        modelObject = model.getRootMetaClass();
        try {
            this.javaClass = Class.forName(metaClassName);
        } catch (final ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        this.metaClass = model.getMetaClass(metaClassName);
        this.attributes = new HashMap<String, Object>(attributes);
    }

    /**
     * Associated objects created in setup
     */
    protected final java.util.Set<String> hookList = new java.util.HashSet<String>();

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
            hookList.add(hook);
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
        Statistics stat = HibernateUtilTester.getStatistics(model);
        if (stat.getSessionCloseCount() - stat.getSessionOpenCount() < 0)
            System.err.println((stat.getSessionOpenCount() - stat.getSessionCloseCount())
                + " session is not closed");

        POJOFactory.doRemoveTestingRecords("", hookList);

        stat = HibernateUtilTester.getStatistics(model);
        if (stat.getSessionCloseCount() - stat.getSessionOpenCount() < 0)
            System.err.println((stat.getSessionOpenCount() - stat.getSessionCloseCount())
                + " session is not closed");
        // make sure every session is closed
        // TODO enable assert
        // stat.getSessionCloseCount()==stat.getSessionOpenCount();

    }

/*
    private void doRemoveTestingRecords(String testCaseName, Collection<String> hookList) {
        WritableVersion wv = null;
        // first round delete but some object may not be deleted as constraint
        Category loggerCat = Logger.getLogger(Hibernate.class).getParent();
        Level oldLevel = loggerCat.getLevel();

        loggerCat.setLevel(Level.OFF);// it seems setLevel only work on Parent

        for (String hook : new LinkedList<String>(hookList)) {
            try {
                wv = model.getWritableVersion(AbstractModel.SUPERUSER);
                ModelObject mo = wv.get(hook);
                if (mo != null) {
                    mo.delete();
                    // throw new RuntimeException("when does this happen?");
                }
                wv.commit();
                hookList.remove(hook);
            } catch (ModelException ex) {
                // it is ok, maybe deleteable in second round
            } finally {
                if (!wv.isCompleted()) {
                    wv.abort();
                }
            }
        }

        loggerCat.setLevel(oldLevel);
        // second round delete
        for (String hook : new LinkedList<String>(hookList)) {
            try {
                wv = model.getWritableVersion(AbstractModel.SUPERUSER);
                ModelObject mo = wv.get(hook);
                if (mo != null)
                    mo.delete();
                wv.commit();
                hookList.remove(hook);
            } catch (ModelException ex) {
                //ex.printStackTrace();
                System.err.print(this.getName() + ": Can not delete testing Object " + hook + " as "
                    + ex.getMessage());
            } finally {
                if (!wv.isCompleted()) {
                    wv.abort();
                }
            }
        }
    } */

    /**
     * First test the simple methods, that don't need a version This also attempts to provide a value for the
     * required attributes
     */
    public void testSimple() {

        assertNotNull("getJavaClass", metaClass.getJavaClass());

        // test all the attributes
        Map<String, MetaAttribute> as = metaClass.getAttributes();
        for (java.util.Iterator i = as.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();
            String name = (String) e.getKey();
            MetaAttribute a = (MetaAttribute) e.getValue();
            assertEquals("getAttribute: " + name, a, metaClass.getAttribute(name));
            assertEquals("MetaAttribute.getMetaClass", metaClass, a.getMetaClass());
            assertNotNull("getType", a.getType());
            Constraint constraint = a.getConstraint();
            assertNotNull("getConstraint for: " + name, constraint);

            if (a.isRequired() && !a.isDerived() && !attributes.containsKey(name)) {
                // try to provide a value for this attribute
                Object value = a.getDefaultValue();
                if (null == value) {
                    value = name + new java.util.Date().getTime(); // unique
                    // string
                }
                if (constraint.verify(name, value, null)) { // only use it if it
                    // is valid
                    attributes.put(name, value);
                } else {
                    System.out.println("Unable to make value for: " + name + " type: "
                        + a.getType().getName());
                }
            }
        }
        assertNull("getAttribute(nonesuch)", metaClass.getAttribute("nonesuch"));

        // test supertype
        MetaClass supertype = metaClass.getSupertype();
        // if (null!=supertype.getSubtypes()) { // base type does not return
        // subtypes
        assertTrue("getSupertype: " + supertype.getMetaClassName(),
            supertype.getSubtypes().contains(metaClass));
        // }
        for (String superName : supertype.getAttributes().keySet()) {
            assertTrue("supertype attributes", as.keySet().contains(superName));
        }
        assertTrue("supertype attributes", as.keySet().containsAll(supertype.getAttributes().keySet()));
    }

    /**
     * Check help docs are available
     */
    public void testHelpText() {
        String help = metaClass.getHelpText();
        assertNotNull("help text", help);
        // TODO assertFalse( "help text", "".equalsIgnoreCase(help) );

        // test help for attributes
        for (java.util.Iterator i = metaClass.getAttributes().values().iterator(); i.hasNext();) {
            MetaAttribute a = (MetaAttribute) i.next();
            help = a.getHelpText();
            assertNotNull("help text for " + a.getName(), help);
            // TODO assertFalse( "help text for "+a.getName(), "".equals(help)
            // );
        }

        Map ms = metaClass.getMetaRoles();
        for (java.util.Iterator i = ms.values().iterator(); i.hasNext();) {
            MetaRole role = (MetaRole) i.next();
            assertNotNull("help text for: " + role.getRoleName(), role.getHelpText());
            assertEquals("getMetaRole " + role.getRoleName(), metaClass.getMetaRole(role.getRoleName()), role);
        }
    }

    /**
     * Test methods involving associations
     */
    public void testV0_1MetaRoles() {
        // test getMetaRoles
        Map<String, MetaRole> ms = metaClass.getMetaRoles();
        for (java.util.Iterator i = ms.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();
            String name = (String) e.getKey();
            MetaRole role = (MetaRole) e.getValue();
            assertNotNull("role", role);
            assertEquals("getMetaRole " + name, metaClass.getMetaRole(name), role);
            assertTrue("low: " + name + " " + role.getLow(), role.getLow() >= 0);
            assertTrue("low <= high: " + name, role.getHigh() == -1 || role.getLow() <= role.getHigh());
            MetaRole other;
            MetaRole shouldBeSelf = null;
            other = role.getOtherRole();
            if (null == other) {
                // association is navigable only one way
                continue;
            }
            shouldBeSelf = other.getOtherRole();
            if (role != null && shouldBeSelf != null) {
                assertEquals("other role: " + name, role.getName(), shouldBeSelf.getName());
                assertEquals("other role: " + name, role, shouldBeSelf);
            }
        }
        assertNull(metaClass.getMetaRole("nonesuch"));

        // test supertype
        MetaClass supertype = metaClass.getSupertype();

        assertTrue("supertype associations", ms.keySet().containsAll(supertype.getMetaRoles().keySet()));

    }

    /**
     * test the methods that need access to a model object
     */
    public void testVersion() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        try {
            ModelObject testObject = doTestVersion(wv);
            wv.delete(testObject);
        } catch (ModelException ex) {
            Throwable cause = ex;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            cause.printStackTrace();
            fail(cause.getClass().getName() + ": " + cause.getMessage());
        } finally {
            wv.abort(); // not testing persistence here
        }
    }

    /**
     * Implementation of testVersion() method - separated out so subclasses can easily add other tests that
     * are part of the same transaction
     * 
     * @param wv transaction to use
     * @return the object that is created. The subclass may want to add some tests.
     * @throws ConstraintException
     * @throws AccessException
     */
    @SuppressWarnings("unchecked")
    protected ModelObject doTestVersion(WritableVersion wv) throws ConstraintException, AccessException {
        Map<String, Object> allattributes = new HashMap<String, Object>();
        allattributes.putAll(this.attributes);
        allattributes.putAll(getAdditionalProperties(wv));
        // create
        ModelObject testObject = wv.create(this.javaClass, allattributes);
        String hook = testObject.get_Hook();
        assertNotNull("created", wv.get(hook));
        assertNotNull("name", testObject.get_Name());

        // getValues
        testObject.get_Values();

        // isInstance
        assertTrue("isInstance", metaClass.isInstance(testObject));
        assertTrue("isInstance", modelObject.isInstance(testObject));

        // test the creation attributes
        Map<String, Object> editableAttributes = new HashMap<String, Object>();
        for (java.util.Iterator i = attributes.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();
            String name = (String) e.getKey();
            Object value = e.getValue();

            MetaAttribute attribute = metaClass.getAttribute(name);
            if (null == attribute) {
                MetaRole role = metaClass.getMetaRole(name);
                if (null == role) {
                    fail("attribute not found: " + name);
                }
                // TODO not for immutable associations e.g. parent
                java.util.Collection<ModelObject> collection = (java.util.Collection<ModelObject>) value;
                testObject.add(name, collection);
                continue;
            }
            assertNotNull("getAttribute: " + name, attribute);
            assertEquals(metaClass.getMetaClassName() + ".get" + name, value, attribute.get(testObject));
            if (attribute.getType().isArray()) {
                // hard to check array type
                continue;
            }
            if (attribute.isChangeable()) {
                attribute.set(testObject, value);
                editableAttributes.put(name, value);
                assertEquals(metaClass.getMetaClassName() + ".set" + name, value, attribute.get(testObject));
            }
        }

        // test setValues(Map)
        testObject.set_Values(editableAttributes);

        // LATER could also test setting all attributes which have a
        // defaultValue

        Map ms = metaClass.getMetaRoles();
        for (java.util.Iterator i = ms.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();
            String name = (String) e.getKey();
            MetaRole role = (MetaRole) e.getValue();
            assertNotNull("get associates: " + name, role.get(testObject));
        }

        // changing associations is tested in TestAMetaRole

        return testObject;

    }

    /**
     * Check that internal attributes are not exposed
     */
    public void testIgnoredAttributes() {
        Map as = metaClass.getAttributes();
        assertFalse("ignored attribute", as.containsKey("dbId"));
    }

    // TODO test MetaAttribute.getLength()

    /**
     * test findAll method for this type
     */
    public void testFindAll() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            doTestFindAll(wv);
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            wv.abort(); // not testing persistence here
        }
    }

    /**
     * Implementation of testVersion() method - separated out so subclasses can easily add other tests that
     * are part of the same transaction
     * 
     * @param wv transaction to use
     * @throws ConstraintException
     * @throws AccessException
     */
    protected void doTestFindAll(WritableVersion wv) throws ConstraintException, AccessException {
        Map<String, Object> allattributes = new HashMap<String, Object>();
        allattributes.putAll(this.attributes);
        allattributes.putAll(getAdditionalProperties(wv));
        // create
        ModelObject testObject = wv.create(javaClass, allattributes);
        String hook = testObject.get_Hook();
        assertNotNull("created", wv.get(hook));
        try {
            // get all
            assertTrue("getAll", wv.getAll(javaClass, 0, 10).contains(testObject));
        } catch (final UnsupportedOperationException ex1) {
            fail(ex1.getMessage());
        }
        java.util.Collection all = wv.findAll(metaClass.getJavaClass(), attributes);
        assertTrue("created and found", all.contains(testObject));

        wv.delete(testObject);
    }

    protected Map getAdditionalProperties(WritableVersion wv) {
        return Collections.EMPTY_MAP;
    }
}
