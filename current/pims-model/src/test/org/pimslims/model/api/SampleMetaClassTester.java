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
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.search.Paging;

/**
 * Tests generated class for Experiment
 */
public class SampleMetaClassTester extends org.pimslims.metamodel.TestAMetaClass {

    /**
     * Values for creating a test ref sample (reagent)
     */
    public static final HashMap ATTR_SAMPLE = new HashMap();
    static {
        ATTR_SAMPLE.put("name", "test sample" + new java.util.Date().getTime());
    }

    /**
     * Values for creating a test sample category
     */
    public static final HashMap ATTR_SAMPLECATEGORY = new HashMap();
    static {
        ATTR_SAMPLECATEGORY.put("name", "test sample" + new java.util.Date().getTime());
    }

    /**
     * Values for creating a test location
     */
    public static final HashMap LOCATION_ATTRIBUTES = new HashMap();
    static {
        LOCATION_ATTRIBUTES.put("location", "testLocation " + new java.util.Date().getTime());
    }

    /**
     * Values for creating a test MolComponent
     */
    public static final HashMap ATTR_MOLCOMPONENT = new HashMap();
    static {
        ATTR_MOLCOMPONENT.put("name", "test molComponent");
    }

    /**
     * Values for creating a test component category
     */
    public static final HashMap ATTR_COMPONENTCATEGORY = new HashMap();
    static {
        ATTR_COMPONENTCATEGORY.put("name", "test component" + new java.util.Date().getTime());
    }

    /**
     * Identifies the SampleCategory
     */
    private String SampleCategoryHook = null;

    /**
     * the ComponentCategory being used
     */
    protected String ComponentCategoryHook = null;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            ModelObject sampleCategory =
                wv.create(org.pimslims.model.reference.SampleCategory.class, ATTR_SAMPLECATEGORY);
            assertNotNull(sampleCategory);
            SampleCategoryHook = sampleCategory.get_Hook();

            ModelObject componentCategory =
                wv.create(org.pimslims.model.reference.ComponentCategory.class, ATTR_COMPONENTCATEGORY);
            assertNotNull(componentCategory);
            ComponentCategoryHook = componentCategory.get_Hook();
            wv.commit();
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
        super.tearDown();
        // delete sampleCategory & componentCategory
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        if (null == wv) {
            return;
        } // Don't hide previous error
        try {
            ModelObject sampleCategory = wv.get(SampleCategoryHook);
            ModelObject componentCategory = wv.get(ComponentCategoryHook);

            if (null != sampleCategory) {
                wv.delete(sampleCategory);
            }
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
    public SampleMetaClassTester() {
        super(org.pimslims.model.sample.Sample.class.getName(), ModelImpl.getModel(),
            "testing implementation of Sample class", ATTR_SAMPLE);
    }

    /**
     * test the methods that need access to a model object overide the TestAMetaClass one
     */
    @Override
    public void testVersion() {
        WritableVersion wv = this.model.getWritableVersion(AbstractModel.SUPERUSER);

        try {
            // add sampleCategory
            java.util.Map atts = new java.util.HashMap(attributes);
            ModelObject sampleCategory = wv.get(SampleCategoryHook);
            assertNotNull(sampleCategory);
            atts.put("sampleCategories", Collections.singleton(sampleCategory));

            ModelObject testObject = wv.create(org.pimslims.model.sample.Sample.class, atts);
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
     * Check that internal attributes are not exposed
     */
    public void testMetadata() {
        Map attrMap = metaClass.getAttributes();
        assertFalse("ignored attribute", attrMap.containsKey("dbId"));

        MetaRole metaRole =
            model.getMetaClass(org.pimslims.model.sample.Sample.class.getName()).getMetaRole(
                "sampleCategories");
        assertNotNull("sampleCategories", metaRole);
    }

    /**
     * Check that searches for subclasses don't find superclass
     */
    public void testSearch() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        try {
            // add sampleCategory
            java.util.Map atts = new java.util.HashMap(attributes);
            ModelObject sampleCategory = wv.get(SampleCategoryHook);
            assertNotNull(sampleCategory);
            atts.put("sampleCategories", Collections.singleton(sampleCategory));

            // create
            ModelObject testObject = wv.create(org.pimslims.model.sample.Sample.class, atts);
            String hook = testObject.get_Hook();
            assertNotNull("created", wv.get(hook));

            // find all
            Map criteria = new java.util.HashMap();
            assertTrue("findAll",
                wv.findAll(AbstractSample.class, criteria, new Paging(0, 10)).contains(testObject));

            try {
                // get all
                assertTrue("getAll", wv.getAll(AbstractSample.class, 0, 10).contains(testObject));
                assertFalse("getAll subclass", wv.getAll(RefSample.class, 0, 10).contains(testObject));
            } catch (final UnsupportedOperationException ex1) {
                fail(ex1.getMessage());
            }
            wv.delete(testObject);
        } catch (AccessException ex) {
            throw new RuntimeException(ex);
        } catch (ConstraintException ex) {
            throw new RuntimeException(ex);
        } finally {
            wv.abort();
        }
    }

    /**
     * Test support for SampleComponent
     */
    public void testSampleComponent() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        try {
            // add sampleCategory
            java.util.Map atts = new java.util.HashMap(attributes);
            ModelObject sampleCategory = wv.get(SampleCategoryHook);
            assertNotNull(sampleCategory);
            atts.put("sampleCategories", Collections.singleton(sampleCategory));

            // create
            ModelObject sample = wv.create(org.pimslims.model.sample.Sample.class, atts);

            Map attrMolComponent = new HashMap();
            attrMolComponent.put("categories", Collections.singleton(wv.get(ComponentCategoryHook)));
            attrMolComponent.put(Molecule.PROP_MOLTYPE, "other");
            attrMolComponent.put("name", "name_test" + new Date().getTime());
            ModelObject molComponent =
                wv.create(org.pimslims.model.molecule.Molecule.class, attrMolComponent);

            Map attrSampleComponent = new HashMap();
            attrSampleComponent.put("abstractSample", sample);
            attrSampleComponent.put("refComponent", molComponent);
            ModelObject sampleComponent =
                wv.create(org.pimslims.model.sample.SampleComponent.class, attrSampleComponent);
            assertNotNull("sampleComponent", sampleComponent);

            // delete
            // TODO wv.delete(sampleComponent);
            // TODO wv.delete(molComponent);
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
     * Test get and set for Float values
     */
    public void testFloat() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        try {
            // create
            java.util.Map atts = new java.util.HashMap(attributes);
            ModelObject sampleCategory = wv.get(SampleCategoryHook);
            assertNotNull(sampleCategory);
            atts.put("sampleCategories", Collections.singleton(sampleCategory));
            Sample sample = (Sample) wv.create(org.pimslims.model.sample.Sample.class, atts);
            // test getting a float value
            sample.set_Value(AbstractSample.PROP_PH, new Float(7.0f));
            try {
                assertEquals("float value", new Float(7.0f), sample.getPh());
            } catch (RuntimeException ex1) {
                ex1.printStackTrace();
                fail(ex1.getMessage());
            }

            // delete
            wv.delete(sample);
        } catch (AccessException ex) {
            throw new RuntimeException(ex);
        } catch (ConstraintException ex) {
            throw new RuntimeException(ex);
        } finally {
            wv.abort();
        }
    }

    private static final Map NO_END_DATE = new HashMap();
    static {
        NO_END_DATE.put("endDate", null);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.TestAMetaClass#testFindAll()
     */
    @Override
    public void testFindAll() {
        // TODO like super.findAll(), but provide a sample category
    }

    /**
     * obsolete Test creating a sample location
     * 
     * public void testSampleLocation() { WritableVersion wv =
     * model.getWritableVersion(AbstractModel.SUPERUSER);
     * 
     * try { // create sample and location ModelObject sample = wv.create(javaClass, ATTRIBUTES); ModelObject
     * oldLocation = wv.create(Location.class, LOCATION_ATTRIBUTES); ModelObject newLocation =
     * wv.create(Location.class, LOCATION_ATTRIBUTES); // now create SampleLocation Map attributes = new
     * java.util.HashMap(); attributes.put("sample", Collections.singleton(sample));
     * attributes.put("location", Collections.singleton(oldLocation)); long now = new
     * java.util.Date().getTime(); // milliseconds attributes.put("startDate", new java.sql.Timestamp( now -
     * 1000*60*60 )); //obsolete ModelObject oldSampleLocation =
     * wv.create(org.pimslims.model.sample.SampleLocation.class, attributes); //assertNull("ended too soon",
     * oldSampleLocation.getValue("endDate")); // now move it attributes.put("location",
     * Collections.singleton(newLocation)); attributes.put("startDate", new java.sql.Timestamp( now ));
     * //obsolete ModelObject newSampleLocation = wv.create(org.pimslims.model.sample.SampleLocation.class,
     * attributes); assertTrue("find location", 0 <sample.findAll("sampleLocations", NO_END_DATE).size() );
     * //java.sql.Timestamp endDate = (java.sql.Timestamp)oldSampleLocation.getValue("endDate");
     * //assertNotNull("not ended", endDate); //assertEquals("not ended", now, endDate.getTime(), 0f);
     * 
     * //wv.delete(oldSampleLocation); //wv.delete(newSampleLocation); wv.delete(oldLocation);
     * wv.delete(newLocation); wv.delete(sample); } catch (AccessException ex) { throw new
     * RuntimeException(ex); } catch (ConstraintException ex) { ex.printStackTrace(); throw new
     * RuntimeException(ex); } finally { wv.abort(); } }
     */

    /**
     * The sample location test, through the CCPN API alone obsolete public void testCCPNSampleLocation() {
     * Project project = ((ModelImpl)model).getMemopsProject(); try { Sample sample = new Sample(project,
     * "sample", java.util.Collections.EMPTY_LIST); Location location = new Location(project,"first");
     * //obsolete SampleLocation sl = sample.newSampleLocation(location, new java.sql.Timestamp( new
     * Date().getTime() )); //java.util.Collection locations = sample.findAllSampleLocations("endDate", null);
     * //assertEquals("location with no end date", 1, locations.size()); // tidy up //sl.delete();
     * sample.delete(); location.delete(); } catch (ApiException e) { e.printStackTrace();
     * fail(e.getLocalizedMessage()); } }
     */
    /*
     * public void testAlias() { MetaClass pmeta = ServletUtil.getPIMSMetaClass(metaClass);
     * org.pimslims.metamodel.MetaRole reagent = pmeta.getMetaRole("conformsTo"); assertEquals("role alias",
     * "reagent specification", reagent.getAlias()); }
     */

}
