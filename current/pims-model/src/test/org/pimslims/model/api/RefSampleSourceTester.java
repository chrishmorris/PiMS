/*
 * Created on 09-Dec-2004 @author: Chris Morris
 */
package org.pimslims.model.api;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.sample.AbstractSample;

/**
 * Tests generated class for reagent source
 */
public class RefSampleSourceTester extends org.pimslims.metamodel.TestAMetaClass {

    /**
     * Values for creating the object
     */
    public static final HashMap ATTRIBUTES = new HashMap();
    static {
        ATTRIBUTES.put("catalogNum", "1-23");
    }

    /**
     * Values for creating a test reagent
     */
    public static final HashMap ATTR_REAGENT = new HashMap();
    static {
        ATTR_REAGENT.put("name", "test reagent " + new java.util.Date().getTime());
    }

    /**
     * Identifies the ref sample - this is a required association
     */
    private String reagentHook = null;

    /**
     * Values for creating a test sample category
     */
    public static final HashMap ATTR_SAMPLECATEGORY = new HashMap();
    static {
        ATTR_SAMPLECATEGORY.put("name", "test" + new java.util.Date().getTime());
    }

    /**
     * Identifies the suppplier organisation - this is a required association
     */
    private String supplierHook = null;

    /**
     * the sample category being used
     */
    private String sampleCategoryHook = null;

    /**
     * 
     */
    public RefSampleSourceTester(String methodName) {
        super(org.pimslims.model.sample.ReagentCatalogueEntry.class.getName(), ModelImpl.getModel(), methodName,
            ATTRIBUTES);
    }

    /**
     * Values for creating a supplier
     */
    public static final HashMap SUPPLIER_ATTRIBUTES = new HashMap();
    static {
        SUPPLIER_ATTRIBUTES.put("name", "test supplier " + new Date().getTime());
    }

    /**
     * {@inheritDoc}
     * 
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {

        super.setUp();
        ModelObject sampleCategory;
        supplierHook = setUpAssociate(org.pimslims.model.people.Organisation.class, SUPPLIER_ATTRIBUTES);

        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            sampleCategory =
                wv.create(org.pimslims.model.reference.SampleCategory.class, ATTR_SAMPLECATEGORY);
            sampleCategoryHook = sampleCategory.get_Hook();
            assertNotNull(sampleCategory);

            Map<String, Object> attrReagent = new HashMap<String, Object>(ATTR_REAGENT);
            attrReagent.put(AbstractSample.PROP_SAMPLECATEGORIES, Collections.singleton(sampleCategory));
            ModelObject reagent = wv.create(org.pimslims.model.sample.RefSample.class, attrReagent);
            reagentHook = reagent.get_Hook();

            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @throws ConstraintException
     * @throws AccessException
     */
    @Override
    protected void tearDown() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            ModelObject modelObject2 = wv.get(this.reagentHook);
            modelObject2.delete();
            ModelObject modelObject3 = wv.get(this.sampleCategoryHook);
            modelObject3.delete();

            wv.commit();
        } catch (AccessException e) {
            throw new RuntimeException(e);
        } catch (ConstraintException e) {
            throw new RuntimeException(e);
        } catch (AbortedException e) {
            throw new RuntimeException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        super.tearDown(); // delete the associates
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testVersion() {
        return;
        /* invalid test
                WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
                try {

                    ModelObject supplier = wv.get(supplierHook);
                    ModelObject reagent = wv.get(reagentHook);
                    ModelObject sampleCategory = wv.get(sampleCategoryHook);
                    attributes.put("supplier", supplier);
                    attributes.put("refSample", reagent);

                    // create
                    ModelObject testObject = wv.create(RefSampleSource.class, attributes);
                    String hook = testObject.get_Hook();

                    // check it
                    assertTrue("supplier set", testObject.isAssociated("supplier", supplier));

                    // name
                    assertEquals("name", supplier.get_Name() + ": 1-23", testObject.get_Name());

                    testObject.add("supplier", Collections.singleton(supplier));

                    try {
                        // get all
                        assertTrue("getAll", reagent.get("refSampleSources").contains(testObject));
                    } catch (final UnsupportedOperationException ex1) {
                        fail(ex1.getMessage());
                    }
                    Map conditions = new HashMap();
                    conditions.put("supplier", supplier);
                    conditions.put("refSample", reagent);
                    java.util.Collection found = reagent.findAll("refSampleSources", conditions);
                    assertTrue("created and found", found.contains(testObject));

                    wv.delete(reagent);
                    wv.delete(reagent);
                    try {
                        wv.delete(testObject);
                        assertNull("deleted", wv.get(hook));

                    } catch (org.pimslims.metamodel.ConstraintException ex1) {
                        fail("Cant delete");
                    }
                    assertNotNull("deleting RefSampleSource should not delete supplier", wv.get(supplierHook));
                    wv.delete(supplier);
                    wv.delete(sampleCategory);
                } catch (ModelException ex) {
                    ex.printStackTrace();
                    fail(ex.getMessage());
                } catch (final RuntimeException ex) {
                    ex.printStackTrace();
                    fail(ex.getMessage());
                } finally {
                    wv.abort(); // not testing persistence here
                }*/
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testHelpText() {
        // LATER need help text
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testFindAll() {
        // creation is complicated,
        // so it is easier to test this in overridden testVersion method
    }
}
