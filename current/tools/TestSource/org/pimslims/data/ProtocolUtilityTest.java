/**
 * V2_2-pims-web org.pimslims.data ProtocolUtilityTest.java
 * 
 * @author cm65
 * @date 29 May 2008
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2008 cm65 * .
 */
package org.pimslims.data;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.jdom.JDOMException;
import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;

/**
 * ProtocolUtilityTest
 * 
 */
public class ProtocolUtilityTest extends TestCase {

    private static final String UNIQUE = "put" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * @param name
     */
    public ProtocolUtilityTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.data.ProtocolUtility#processProtocol(org.pimslims.dao.WritableVersion, org.jdom.Element)}.
     * 
     * @throws IOException
     * @throws JDOMException
     * @throws ConstraintException
     * @throws AccessException
     */
    public final void testProcessNoProtocol() throws JDOMException, IOException, AccessException,
        ConstraintException {
        final Reader reader = new StringReader("");
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            new ProtocolUtility(version).loadFile(reader);
            Assert.fail("No input");
        } catch (final JDOMException e) {
            // that's fine
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testProcessProtocol() throws JDOMException, IOException, AccessException,
        ConstraintException {
        final Reader reader =
            new StringReader("<protocol name=\"" + ProtocolUtilityTest.UNIQUE + "\" \r\n"
                + "objective=\"Purify PCR product\" \r\n" + "creationDate=\"16-02-07\" \r\n"
                + "remarks=\"Based on use of Qiagen kit\" >" + "<experimentType>"
                + ProtocolUtilityTest.UNIQUE + "</experimentType>" + "</protocol>");
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ExperimentType type = new ExperimentType(version, ProtocolUtilityTest.UNIQUE);
            final Protocol protocol = new ProtocolUtility(version).loadFile(reader);
            Assert.assertNotNull(protocol);
            Assert.assertEquals(ProtocolUtilityTest.UNIQUE, protocol.getName());
            Assert.assertEquals(type, protocol.getExperimentType());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    /*
     * Test method for PIMS-2151 ProtocolUtility.ProcessOutputSample
     */
    public final void testProcessOutputSample() throws AccessException, ConstraintException, JDOMException,
        IOException {
        final Reader reader =
            new StringReader("<protocol name=\"" + ProtocolUtilityTest.UNIQUE + "\" \r\n"
                + "objective=\"Purify PCR product\" \r\n" + "creationDate=\"24-06-10\" \r\n"
                + "remarks=\"Based on use of Qiagen kit\" >" + "<experimentType>"
                + ProtocolUtilityTest.UNIQUE + "</experimentType>" + "<outputSamples>"
                + "<outputSample name=\"os1\" \r\n" + "sampleCatNamingSys=\"default\" \r\n"
                + "sampleCatName=\"" + ProtocolUtilityTest.UNIQUE + "\"  />\r\n" + "</outputSamples>"
                + "</protocol>");
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ExperimentType type = new ExperimentType(version, ProtocolUtilityTest.UNIQUE);
            final SampleCategory samCat = new SampleCategory(version, ProtocolUtilityTest.UNIQUE);
            final Protocol protocol = new ProtocolUtility(version).loadFile(reader);
            final RefOutputSample ros1 = protocol.findFirst(Protocol.PROP_REFOUTPUTSAMPLES);
            final SampleCategory ros1samCat = ros1.findFirst(RefOutputSample.PROP_SAMPLECATEGORY);

            Assert.assertNotNull(protocol);
            Assert.assertEquals(type, protocol.getExperimentType());
            Assert.assertEquals("Processed output sample", 1, protocol.getRefOutputSamples().size());
            Assert.assertEquals("Sample category correct", samCat, ros1samCat);

        } finally {
            version.abort(); // not testing persistence
        }

    }

    /**
     * /** Test method for
     * {@link org.pimslims.data.ProtocolUtility#processInputSample(org.pimslims.dao.WritableVersion, org.pimslims.model.protocol.Protocol, org.jdom.Element)}.
     * 
     * public final void testProcessInputSample() { Assert.fail("Not yet implemented"); // TODO }
     * 
     * /** Test method for
     * {@link org.pimslims.data.ProtocolUtility#processParameterDefinitions(org.pimslims.dao.WritableVersion, org.pimslims.model.protocol.Protocol, org.jdom.Element)}.
     * 
     * public final void testProcessParameterDefinitions() { Assert.fail("Not yet implemented"); // TODO }
     * 
     * /** Test method for {@link org.pimslims.data.ProtocolUtility#getPossibleValues(org.jdom.Element)}.
     * 
     * public final void testGetPossibleValues() { Assert.fail("Not yet implemented"); // TODO }
     */
    /**
     * Test method for
     * {@link org.pimslims.data.AbstractLoader#getSampleCategory(org.pimslims.dao.WritableVersion, java.lang.String, java.lang.String)}.
     * 
     * public final void testGetSampleCategory() { Assert.fail("Not yet implemented"); // TODO }
     * 
     * /** Test method for
     * {@link org.pimslims.data.AbstractLoader#UpdateOrCreate(org.pimslims.dao.WritableVersion, java.lang.Class, java.util.Map, java.util.Collection, java.util.Map)}.
     * 
     * public final void testUpdateOrCreate() { Assert.fail("Not yet implemented"); // TODO }
     */
}
