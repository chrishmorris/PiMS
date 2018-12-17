/**
 * 
 */
package org.pimslims.lab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.sample.SampleFactory;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;

/**
 * @author cm65
 * 
 */
public class TestSampleFactory extends TestCase {

    private final AbstractModel model;

    private WritableVersion wv = null;

    /**
     * For creating the output sample
     */
    private static final Map OUTPUT_ATTRIBUTES = new java.util.HashMap();
    static {
        TestSampleFactory.OUTPUT_ATTRIBUTES.put("name", "mixed" + System.currentTimeMillis()); // make
        // a
        // unique
        // name
    }

    /**
     * Values for creating a test sample category
     */
    private java.util.Set sampleCategories;

    private static final HashMap SCAT_ATTRIBUTES = new HashMap();
    static {
        TestSampleFactory.SCAT_ATTRIBUTES.put("name", "testSampleCategory" + System.currentTimeMillis());
    }

    /**
     * Molecule for calcium chloride
     */
    private Molecule calciumChloride;

    private static final HashMap CACL2_ATTRIBUTES = new HashMap();
    static {
        TestSampleFactory.CACL2_ATTRIBUTES.put("name", "calcium chloride" + System.currentTimeMillis());
        TestSampleFactory.CACL2_ATTRIBUTES.put("molType", "other");
    }

    /**
     * Molecule for magnesium sulphate
     */
    private Molecule magnesiumSulphate;

    public static final HashMap MGSO4_ATTRIBUTES = new HashMap();
    static {
        TestSampleFactory.MGSO4_ATTRIBUTES.put("name", "magnesium sulphate" + (new Date()));
        TestSampleFactory.MGSO4_ATTRIBUTES.put("molType", "other");
    }

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(TestSampleFactory.class);
    }

    /**
     * Constructor for TestSampleUtilty.
     * 
     * @param methodName
     */
    public TestSampleFactory(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        this.verifySampleCategory();
        this.wv = this.model.getTestVersion();
        final ModelObject sampleCategory =
            this.wv.create(SampleCategory.class, TestSampleFactory.SCAT_ATTRIBUTES);
        this.sampleCategories = new java.util.HashSet();
        this.sampleCategories.add(sampleCategory);

        final ModelObject caCl2 = this.wv.create(Molecule.class, TestSampleFactory.CACL2_ATTRIBUTES);
        this.calciumChloride = (Molecule) caCl2;
        final ModelObject mgSO4 = this.wv.create(Molecule.class, TestSampleFactory.MGSO4_ATTRIBUTES);
        this.magnesiumSulphate = (Molecule) mgSO4;
        // TODO create or find the Molecule for this

        super.setUp();

    }

    /**
     * 
     */
    private void verifySampleCategory() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ModelObject sampleCategory =
                version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME,
                    TestSampleFactory.SCAT_ATTRIBUTES.get("name"));
            if (sampleCategory != null) {
                throw new RuntimeException("Found existed SampleCategory:" + sampleCategory);
            }
        } finally {
            version.abort();
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        if (null != this.wv) {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
        super.tearDown();

    }

    /**
     * test defineSolution and getMoles
     */
    public void testDefine() {
        try {
            final Map components = new HashMap();
            components.put(this.calciumChloride, new Float(0.1f)); // 0.1M
            final Sample solution =
                SampleFactory.defineSolution(this.wv, "0.1M CaCl2" + System.currentTimeMillis(), 1.0f,
                    components, new ArrayList(this.sampleCategories));
            Assert.assertEquals("volumeUnit", "L", solution.getAmountUnit());
            Assert.assertEquals("define volume", 1.0f, solution.getCurrentAmount().floatValue(), 0.000001f);
            Assert.assertEquals("define moles", 0.1f, SampleFactory.getMoles(solution, this.calciumChloride),
                0.0000001f);
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        }
    }

    private static final float INPUT = 0.0001f; // 0.1L

    public void testMix() {
        try {
            this.verifySampleCategory();
            //final boolean oldFlushModel = ((WritableVersionImpl) this.wv).isFlushSessionAfterCreate();
            ((WritableVersionImpl) this.wv).getFlushMode().setFlushSessionAfterCreate(false);
            this.wv.getSession().setFlushMode(org.hibernate.FlushMode.MANUAL); // TODO remove

            final Sample output =
                SampleFactory.defineSolution(this.wv, "empty" + System.currentTimeMillis(), 0.0f,
                    Collections.EMPTY_MAP, new ArrayList(this.sampleCategories));
            Map components = new HashMap();
            components.put(this.calciumChloride, new Float(0.1f));
            final Sample input =
                SampleFactory.defineSolution(this.wv, "1L 0.1M CaCl2" + System.currentTimeMillis(), 0.001f,
                    components, new ArrayList(this.sampleCategories));
            SampleFactory.mix(output, input, TestSampleFactory.INPUT);
            Assert.assertEquals("output volume", TestSampleFactory.INPUT, output.getCurrentAmount()
                .floatValue(), 0.0000001f);
            Assert.assertEquals("input volume", 0.001f - TestSampleFactory.INPUT, input.getCurrentAmount()
                .floatValue(), 0.0000001f);
            final SampleComponent sc =
                (SampleComponent) output.findFirst(AbstractSample.PROP_SAMPLECOMPONENTS);
            Assert.assertEquals("concentration unit", "M", sc.getConcentrationUnit());
            Assert.assertEquals("concentration", 0.1f, sc.getConcentration().floatValue(), 0.0000001f);

            components = new HashMap();
            components.put(this.magnesiumSulphate, new Float(0.1f));
            final Sample input2 =
                SampleFactory.defineSolution(this.wv, "1L  MgSO4" + System.currentTimeMillis(), 0.001f,
                    components, new ArrayList(this.sampleCategories));
            SampleFactory.mix(output, input2, TestSampleFactory.INPUT);
            final Collection scs = output.getSampleComponents();
            Assert.assertEquals("two components", 2, scs.size());
            for (final Iterator iter = scs.iterator(); iter.hasNext();) {
                final SampleComponent oldSC = (SampleComponent) iter.next();
                Assert.assertEquals("half and half: " + oldSC.getRefComponent().getName(), 0.05f, sc
                    .getConcentration().floatValue(), 0.000001f);
            }

            // TODO test diluting
            /*
             * LATER check for match with existing component of sample SampleUtility.mix(output, input,
             * INPUT);
             */
            this.verifySampleCategory();
            this.wv.flush();
            this.verifySampleCategory();
        } catch (final ConstraintException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        }

    }

}
