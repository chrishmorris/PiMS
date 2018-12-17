/*
 * Created on 09-Dec-2004 @author: Chris Morris
 */
package org.pimslims.model.api;

import java.util.HashMap;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.Annotation;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.target.TargetGroup;
import org.pimslims.test.POJOFactory;

/**
 * Tests generated class for annotation
 */
public class AnnotationTester extends org.pimslims.metamodel.TestAMetaClass {

    /**
     * Values for creating the object
     */
    public static final HashMap<String, Object> ATTRIBUTES = new HashMap<String, Object>(
        org.pimslims.test.POJOFactory.getAttrAnnotation());

    /**
     * 
     */
    public AnnotationTester(String methodName) {
        super(Annotation.class.getName(), ModelImpl.getModel(), methodName, ATTRIBUTES);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testVersion() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {

            java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>(ATTRIBUTES);
            TargetGroup targetProject = POJOFactory.createTargetGroup(wv);
            attributes.put(Attachment.PROP_PARENTENTRY, targetProject);

            // create
            ModelObject testObject = wv.create(javaClass, attributes);
            String hook = testObject.get_Hook();

            try {
                // get all
                assertTrue("getAll", wv.getAll(metaClass.getJavaClass(), 0, 100).contains(testObject));
            } catch (final UnsupportedOperationException ex1) {
                fail(ex1.getMessage());
            }
            // LATER assertTrue("created and found", wv.findAll(metaClass,
            // attributes).contains(testObject) );

            try {
                wv.delete(testObject);
                assertNull("deleted", wv.get(hook));
            } catch (org.pimslims.exception.ConstraintException ex1) {
                fail("Cant delete");
            }
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            wv.abort(); // not testing persistence here
        }
    }

    /**
     * Test getAnnotations() method
     */
    public void testgetAnnotations() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>(ATTRIBUTES);
            TargetGroup project = POJOFactory.createTargetGroup(wv);
            String projectHook = project.get_Hook();
            attributes.put(Attachment.PROP_PARENTENTRY, project);

            // create
            Annotation annotation = new Annotation(wv, attributes);
            String hook = annotation.get_Hook();

            try {
                // test project.getAnnotations()
                assertEquals("getAnnotations", project.getAnnotations().size(), 1);
            } catch (final UnsupportedOperationException ex1) {
                fail(ex1.getMessage());
            }

            try {
                // delete project should also delete all children
                wv.delete(project);
                assertNull("project deleted", wv.get(projectHook));
                assertNull("child annotation deleted", wv.get(hook));
            } catch (org.pimslims.exception.ConstraintException ex1) {
                fail("Cant delete");
            }
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            wv.abort(); // not testing persistence here
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testFindAll() {
        // creation is complicated,
        // so it is easier to test this in overridden testVersion method
    } // */
}
