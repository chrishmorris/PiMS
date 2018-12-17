/*
 * Created on 09-Dec-2004 @author: Chris Morris
 */
package org.pimslims.model.api;

import java.util.Collections;
import java.util.HashMap;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.reference.ExperimentType;

/**
 * Tests generated class for Experiment
 */
public class ExperimentMetaClassTester extends org.pimslims.metamodel.TestAMetaClass {

    /**
     * Values for creating a generic experiment
     */
    public static final HashMap<String, Object> ATTRIBUTES =
        new HashMap<String, Object>(org.pimslims.test.POJOFactory.getAttrExperiment());

    /**
     * Values for creating an experiment type
     */
    public static final HashMap<String, Object> TYPE_ATTRIBUTES =
        new HashMap<String, Object>(org.pimslims.test.POJOFactory.getAttrExperimentType());

    /**
     * the experiment type being used
     */
    protected String typeHook;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        typeHook = setUpAssociate(ExperimentType.class, TYPE_ATTRIBUTES);
    }

    /**
     * 
     */
    public ExperimentMetaClassTester() {
        super(org.pimslims.model.experiment.Experiment.class.getName(), ModelImpl.getModel(),
            "testing implementation of Experiment classes", ATTRIBUTES);
    }

    /**
     * test the methods that need access to a model object
     */
    @Override
    public void testVersion() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        try {
            ModelObject type = wv.get(typeHook);
            assertNotNull(type);
            attributes.put(org.pimslims.model.experiment.Experiment.PROP_EXPERIMENTTYPE, Collections
                .singleton(type));
            ModelObject testObject = doTestVersion(wv);
            wv.delete(testObject);
        } catch (Throwable ex) {
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
     * test findAll method for this type
     */
    @Override
    public void testFindAll() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            ModelObject type = wv.get(typeHook);
            assertNotNull(type);
            attributes.put("experimentType", Collections.singleton(type));
            doTestFindAll(wv);
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

}
