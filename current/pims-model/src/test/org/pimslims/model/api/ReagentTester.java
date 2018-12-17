/*
 * Created on 09-Dec-2004 @author: Chris Morris
 */
package org.pimslims.model.api;

import java.util.Collection;
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
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.ReagentCatalogueEntry;
import org.pimslims.test.POJOFactory;

/**
 * Tests generated class for reagents
 */
public class ReagentTester extends org.pimslims.metamodel.TestAMetaClass {

    /**
     * Values for creating a test ref sample (reagent)
     */
    public static final HashMap ATTR_SAMPLE = new HashMap();
    static {
        ATTR_SAMPLE.put("name", "test reagent " + new java.util.Date().getTime());
    }

    /**
     * Values for creating a test sample category
     */
    public static final HashMap ATTR_SAMPLECATEGORY = new HashMap();
    static {
        ATTR_SAMPLECATEGORY.put("name", "test sample category" + new java.util.Date().getTime());
    }

    public static final HashMap ATTR_SAMPLECATEGORY2 = new HashMap();
    static {
        ATTR_SAMPLECATEGORY2.put("name", "test sample category two" + new java.util.Date().getTime());
    }

    /**
     * Values for creating a test component category
     */
    public static final HashMap ATTR_COMPONENTCATEGORY = new HashMap();
    static {
        ATTR_COMPONENTCATEGORY.put("name", "test component category" + new java.util.Date().getTime());
    }

    /**
     * the experiment sampleCategory being used
     */
    protected String sampleCategoryHook = null;

    /**
     * the experiment componentCategory being used
     */
    protected String componentCategoryHook = null;

    private String sampleCategoryHook2 = null;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        /**
         * the sample category being used
         */
        ModelObject sampleCategory;
        /**
         * the component category being used
         */
        ModelObject componentCategory;
        super.setUp();

        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        sampleCategory = wv.create(org.pimslims.model.reference.SampleCategory.class, ATTR_SAMPLECATEGORY);
        assertNotNull(sampleCategory);
        sampleCategoryHook = sampleCategory.get_Hook();
        sampleCategory = wv.create(org.pimslims.model.reference.SampleCategory.class, ATTR_SAMPLECATEGORY2);
        assertNotNull(sampleCategory);
        sampleCategoryHook2 = sampleCategory.get_Hook();

        componentCategory =
            wv.create(org.pimslims.model.reference.ComponentCategory.class, ATTR_COMPONENTCATEGORY);
        assertNotNull(componentCategory);
        componentCategoryHook = componentCategory.get_Hook();
        wv.commit();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() {
        /**
         * the sample category being used
         */
        ModelObject sampleCategory;
        /**
         * the component category being used
         */
        ModelObject componentCategory;
        super.tearDown();
        // delete sampleCategory & componentCategory
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        if (null == wv) {
            return;
        } // Don't hide previous error

        try {
            sampleCategory = wv.get(sampleCategoryHook);
            if (null != sampleCategory) {
                wv.delete(sampleCategory);
            }
            sampleCategory = wv.get(sampleCategoryHook2);
            if (null != sampleCategory) {
                wv.delete(sampleCategory);
            }
            componentCategory = wv.get(componentCategoryHook);
            if (null != componentCategory) {
                wv.delete(componentCategory);
            }
            wv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    /**
     * Constructor
     */
    public ReagentTester() {
        super(org.pimslims.model.sample.RefSample.class.getName(), ModelImpl.getModel(),
            "testing implementation of reagents", ATTR_SAMPLE);
    }

    /**
     * test the methods that need access to a model object
     */
    @Override
    public void testVersion() {
        /**
         * the sample category being used
         */
        ModelObject sampleCategory;

        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        try {
            sampleCategory = wv.get(sampleCategoryHook);
            HashMap<String, Object> atts = new HashMap<String, Object>(attributes);
            atts.put("sampleCategories", Collections.singleton(sampleCategory));
            ReagentCatalogueEntry productSource = POJOFactory.create(wv, ReagentCatalogueEntry.class);
            atts.put(org.pimslims.model.sample.RefSample.PROP_REFSAMPLESOURCES,
                Collections.singleton(productSource));
            ModelObject testObject = wv.create(org.pimslims.model.sample.RefSample.class, atts);

            // now add another category
            sampleCategory = wv.get(sampleCategoryHook2);
            testObject.add("sampleCategories", sampleCategory);

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
     * Tests that an exception is thrown
     */
    // TODO The DM has change, it is not a required linked anymore
    public void notestMissingAssociate() {
        WritableVersion wv = this.model.getWritableVersion(AbstractModel.SUPERUSER);

        try {
            HashMap<String, Object> atts = new HashMap<String, Object>(this.attributes);
            atts.remove("sampleCategories"); // ensure required links is
            // missing
            wv.create(org.pimslims.model.sample.RefSample.class, atts);
            fail("created with required link missing");
        } /*
                    * catch (Throwable ex) { Throwable cause = ex; while (null!=cause.getCause())
                    * {cause=cause.getCause();} if (cause instanceof ConstraintException) { // that's fine } else {
                    * cause.printStackTrace(); fail(cause.getClass().getName()+": "+cause.getMessage()); } }
                    */catch (AccessException e) {
            fail(e.getMessage());
        } catch (ConstraintException e) {
            // that's fine
        } finally {
            wv.abort(); // not testing persistence here
        }
    }

    /**
     * 
     */
    public void TODOtestSubstance() {
        /**
         * the componentCategory being used
         */
        ModelObject componentCategory;
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            componentCategory = wv.get(componentCategoryHook);

            java.util.Map<String, Object> attrSubstance = new java.util.HashMap();
            attrSubstance.put("categories", Collections.singleton(componentCategory));
            attrSubstance.put("name", "name_test" + new Date().getTime());
            ModelObject testObject = wv.create(org.pimslims.model.molecule.Molecule.class, attrSubstance);
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
     * Test support for SampleComponent
     */
    public void testSampleComponent() {
        /**
         * the sample category being used
         */
        ModelObject sampleCategory;
        /**
         * the componentCategory being used
         */
        ModelObject componentCategory;

        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        try {
            componentCategory = wv.get(componentCategoryHook);
            sampleCategory = wv.get(sampleCategoryHook);
            HashMap<String, Object> atts = new HashMap<String, Object>(attributes);
            atts.put("sampleCategories", Collections.singleton(sampleCategory));
            // create
            ModelObject sample = wv.create(org.pimslims.model.sample.Sample.class, atts);

            Map attrSubstance = new HashMap();
            attrSubstance.put("categories", Collections.singleton(componentCategory));
            attrSubstance.put("name", "name_test" + new Date().getTime());
            attrSubstance.put("molType", "other");
            ModelObject substance = wv.create(org.pimslims.model.molecule.Molecule.class, attrSubstance);

            Map attrSampleComponent = new HashMap();
            attrSampleComponent.put("abstractSample", sample);
            attrSampleComponent.put("refComponent", substance);
            ModelObject sampleComponent =
                wv.create(org.pimslims.model.sample.SampleComponent.class, attrSampleComponent);
            assertNotNull("sampleComponent", sampleComponent);

            // delete
            wv.delete(sampleComponent);
            wv.delete(substance);
            wv.delete(sample);
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
     * Test trying to delete when the object is required by another
     */
    public void testDeleteRequired() {
        /**
         * the sample category being used
         */
        ModelObject sampleCategory;
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        try {
            // componentCategory=wv.get(componentCategoryHook);
            sampleCategory = wv.get(sampleCategoryHook);
            HashMap<String, Object> atts = new HashMap<String, Object>(attributes);
            atts.put("sampleCategories", Collections.singleton(sampleCategory));
            // create
            ModelObject sample = wv.create(org.pimslims.model.sample.Sample.class, atts);

            Map attrSubstance = new HashMap();
            // attrSubstance.put("categories",
            // Collections.singleton(componentCategory));
            attrSubstance.put("name", "name_test" + new Date().getTime());
            ModelObject molComponent = wv.create(org.pimslims.model.molecule.Molecule.class, attrSubstance);

            Map attrSampleComponent = new HashMap();
            attrSampleComponent.put("abstractSample", sample);
            attrSampleComponent.put("refComponent", molComponent);
            ModelObject sampleComponent =
                wv.create(org.pimslims.model.sample.SampleComponent.class, attrSampleComponent);
            assertNotNull("sampleComponent", sampleComponent);

            // delete
            wv.delete(molComponent);
            wv.commit();
            fail("deleted molComponent while sampleComponent existed");
        } catch (AccessException e) {
            fail(e.getMessage());
        } catch (ConstraintException e) {
            // that's fine
        } catch (AbortedException e) {
            fail(e.getMessage());
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
    public void testHelpText() {
        // LATER help text needed for usedInExpTypes
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.TestAMetaClass#doTestFindAll(org.pimslims.metamodel.WritableVersion)
     */
    @Override
    protected void doTestFindAll(WritableVersion wv) throws ConstraintException, AccessException {
        Collection<ModelObject> categories = Collections.singleton(wv.get(this.sampleCategoryHook));
        this.attributes.put(AbstractSample.PROP_SAMPLECATEGORIES, categories);
        super.doTestFindAll(wv);
    }

}
