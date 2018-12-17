/*
 * Created on 20-Jul-2005 @author: Chris Morris
 */
package org.pimslims.lab;

import java.util.Collections;
import java.util.HashMap;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.protocol.ProtocolFactory;
import org.pimslims.lab.protocol.ProtocolUtility;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;

/**
 * Test lab methods for protocol utility
 * 
 * @version 0.2
 */
public class TestProtocolUtility extends TestCase {

    /**
     * MY_PROTOCOL1 String
     */
    private static final String MY_PROTOCOL1 = "my protocol1" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * @param methodName the name of hte test method to run
     */
    public TestProtocolUtility(final String methodName) {
        super(methodName);
        this.model = org.pimslims.dao.ModelImpl.getModel();
    }

    private static final String TEN = "abcdefghij";

    public void testNameClash() throws ConstraintException, AccessException {

        final WritableVersion wv = this.model.getTestVersion();
        try {
            final ExperimentType experimentType =
                ProtocolFactory.createExperimentType(wv, "test type " + new java.util.Date().getTime(),
                    Collections.EMPTY_MAP);
            final String name = TestProtocolUtility.MY_PROTOCOL1;
            final Protocol protocol =
                ProtocolFactory.createProtocol(wv, name + " administrator 1", experimentType,
                    Collections.EMPTY_MAP);

            Assert.assertNotNull(protocol);
            final String newName = Util.makeName(wv, name, org.pimslims.model.protocol.Protocol.class);
            Assert.assertEquals(name + " administrator 2", newName);

        } finally {
            wv.abort();
        }
    }

    public void testDuplicate() {
        final WritableVersion wv = this.model.getTestVersion();
        try {
            final ExperimentType experimentType =
                ProtocolFactory.createExperimentType(wv, "test type " + new java.util.Date().getTime(),
                    Collections.EMPTY_MAP);
            final Protocol protocol =
                ProtocolFactory.createProtocol(wv, TestProtocolUtility.MY_PROTOCOL1, experimentType,
                    Collections.EMPTY_MAP);
            ProtocolFactory.createSampleCategory(wv, "test category " + new java.util.Date().getTime(),
                Collections.EMPTY_MAP);

            final SampleCategory scat =
                ProtocolFactory.createSampleCategory(wv, "testSampleCat1", new HashMap());

            final java.util.Map refIns = new java.util.HashMap();
            refIns.put(RefInputSample.PROP_NAME, "RefInputSample1");
            refIns.put(RefInputSample.PROP_SAMPLECATEGORY, scat);
            refIns.put(RefInputSample.PROP_DISPLAYUNIT, "mM");
            refIns.put(RefInputSample.PROP_PROTOCOL, protocol);
            Util.getOrCreate(wv, org.pimslims.model.protocol.RefInputSample.class, refIns);

            final java.util.Map refOuts = new java.util.HashMap();
            refOuts.put(RefOutputSample.PROP_NAME, "RefOutputSample1");
            refOuts.put(RefOutputSample.PROP_SAMPLECATEGORY, scat);
            refOuts.put(RefOutputSample.PROP_DISPLAYUNIT, "mM");
            refOuts.put(RefOutputSample.PROP_PROTOCOL, protocol);

            final java.util.Map refOuts1 = new java.util.HashMap();
            refOuts1.put(RefOutputSample.PROP_NAME, "RefOutputSample2");
            refOuts1.put(RefOutputSample.PROP_SAMPLECATEGORY, scat);
            refOuts1.put(RefOutputSample.PROP_DISPLAYUNIT, "L");
            refOuts1.put(RefOutputSample.PROP_PROTOCOL, protocol);

            Util.getOrCreate(wv, org.pimslims.model.protocol.RefOutputSample.class, refOuts);
            Util.getOrCreate(wv, org.pimslims.model.protocol.RefOutputSample.class, refOuts1);

            final java.util.Map attributes = new java.util.HashMap();
            attributes.put("name", "ParameterDefinition created");
            attributes.put(ParameterDefinition.PROP_PARAMTYPE, "String");
            ProtocolFactory.createParameterDefinition(wv, protocol, attributes);

            attributes.put(ParameterDefinition.PROP_NAME, "paramdef 1");
            attributes.put(ParameterDefinition.PROP_DEFAULTVALUE, "112233");
            attributes.put(ParameterDefinition.PROP_PARAMTYPE, "Int");
            ProtocolFactory.createParameterDefinition(wv, protocol, attributes);

            Assert.assertNotNull(protocol);
            final Protocol duplicate = ProtocolUtility.duplicate(protocol, wv);
            Assert.assertNotNull(duplicate);
            Assert.assertTrue("Names are not equal", duplicate.getName().contains(protocol.getName()));
            Assert.assertEquals("Parameter definition numbers are different", duplicate
                .getParameterDefinitions().size(), protocol.getParameterDefinitions().size());
            for (final ParameterDefinition pd : duplicate.getParameterDefinitions()) {
                final String pdname = pd.getName();
                for (final ParameterDefinition pd1 : protocol.getParameterDefinitions()) {
                    if (pd1.getName().equals(pdname)) {
                        Assert.assertEquals("Parameter values were not copied ", pd.getDefaultValue(),
                            pd1.getDefaultValue());
                        Assert.assertEquals("Parameter types were not copied ", pd.getParamType(),
                            pd1.getParamType());
                    }
                }
            }
            Assert.assertEquals("RefInputSamples number is different", duplicate.getRefInputSamples().size(),
                protocol.getRefInputSamples().size());
            for (final RefInputSample rf : duplicate.getRefInputSamples()) {
                final String rfname = rf.getName();
                for (final RefInputSample rf1 : protocol.getRefInputSamples()) {
                    if (rfname.equals(rf1.getName())) {
                        Assert.assertEquals("Display inits set fails ", rf.getDisplayUnit(),
                            rf1.getDisplayUnit());
                    }
                }
            }
            Assert.assertEquals("RefOutputSamples number is different", duplicate.getRefOutputSamples()
                .size(), protocol.getRefOutputSamples().size());
            for (final RefOutputSample rf : duplicate.getRefOutputSamples()) {
                final String rfname = rf.getName();
                for (final RefOutputSample rf1 : protocol.getRefOutputSamples()) {
                    if (rfname.equals(rf1.getName())) {
                        Assert.assertEquals("Display inits set fails ", rf.getDisplayUnit(),
                            rf1.getDisplayUnit());
                    }
                }
            }

            wv.abort();
        } catch (final ConstraintException ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        } catch (final AccessException ex) {
            Assert.fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        // test successful
    }

    private static final java.util.Map CRITERIA = new java.util.HashMap();
    static {
        TestProtocolUtility.CRITERIA.put(Protocol.PROP_NAME, "PiMS PCR"); // reference protocol
    }

}
