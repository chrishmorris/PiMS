/**
 * pims-web org.pimslims.xmlio XmlExporterTest.java
 * 
 * @author pajanne
 * @date Jan 12, 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.xmlio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.protocol.ProtocolFactory;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.properties.PropertyGetter;
import org.pimslims.test.POJOFactory;
import org.xml.sax.SAXException;

/**
 * XmlExporterTest
 * 
 */
public class ProtocolXmlConvertorTest extends TestCase {

    /**
     * UNIQUE String, used for avoiding name clashes in tests
     */
    private static final String UNIQUE = "protocol" + System.currentTimeMillis();

    /**
     * the basic properties of a protocol, represented in XML
     */
    private static final String BASIC_DETAILS =
        "<name>" + ProtocolXmlConvertorTest.UNIQUE + "</name>\n" + "<experimentType>PCR</experimentType>\n"
            + "<objective>objective</objective>\n" + "<method>method</method>\n"
            + "<remarks>remarks</remarks>\n" + "<details>details</details>\n"
            + "<creationDate>2009-01-19T13:47:21.182Z</creationDate>\n"
            + "<exportDate>2009-01-30T10:32:06.340Z</exportDate>";

    /**
     * the basic properties of a protocol, represented in XML
     */
    private static final String BASIC_WRONG_DETAILS =
        "<name>" + ProtocolXmlConvertorTest.UNIQUE + "</name>\n" + "<experimentType>PCR</experimentType>\n"
            + "<objective>objective</objective>\n" + "<method>method</method>\n"
            + "<remarks>remarks</remarks>\n" + "<details>details</details>\n"
            + "<creation>2009-01-19T13:47:21.182Z</creation>\n"
            + "<exportDate>2009-01-30T10:32:06.340Z</exportDate>";

    /**
     * the basic properties of a parameter, represented in XML
     */
    private static final String PARAM_DETAILS =
        "<parameterDefinition>\n" + "<name>" + ProtocolXmlConvertorTest.UNIQUE + "</name>\n"
            + "<label>label</label>\n" + "<paramType>Float</paramType>\n" + "<unit>unit</unit>\n"
            + "<displayUnit>displayUnit</displayUnit>\n" + "<defaultValue>45</defaultValue>\n"
            + "<minValue>10</minValue>\n" + "<maxValue>60</maxValue>\n" + "<isMandatory>true</isMandatory>\n"
            + "<isGroupLevel>false</isGroupLevel>\n" + "<isResult>false</isResult>\n"
            + "</parameterDefinition>\n";

    /**
     * the basic properties of a input sample, represented in XML
     */
    private static final String INSAMPLE_DETAILS =
        "<inputSample>\n" + "<sampleCategory>Forward Primer</sampleCategory>"
            + "<name>Forward primer</name> " + "<amount>1.0E-6</amount>" + "<unit>L</unit>"
            + "<displayUnit>uL</displayUnit>" + "</inputSample>\n";

    /**
     * the basic properties of a output sample, represented in XML
     */
    private static final String OUTSAMPLE_DETAILS =
        "<outputSample><sampleCategory>Nucleic acid</sampleCategory><name>PCR product</name>"
            + "<unit>L</unit><displayUnit>uL</displayUnit></outputSample>";

    /**
     * the basic properties of a output sample, represented in XML
     */
    private static final String UNKSAMPLE_DETAILS =
        "<outputSample><sampleCategory>Unknown category</sampleCategory><name>PCR product</name>"
            + "<unit>L</unit><displayUnit>uL</displayUnit></outputSample>";

    private final AbstractModel model;

    public ProtocolXmlConvertorTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
        PropertyGetter.setWorkingDirectory("pwd/");
    }

    /**
     * Test method for {@link org.pimslims.xmlio.XmlExchanger#getXmlStringFromObject(java.lang.Object)}.
     * 
     * @throws JAXBException
     */
    public void testConvertBeanToXmlSimple() throws JAXBException {
        final String protocolName = "test xml protocol";
        final ProtocolXmlBean protocol = new ProtocolXmlBean(protocolName);
        Assert.assertTrue(XmlConvertor.convertBeanToXml(protocol, ProtocolXmlConvertor.PROTOCOL_SCHEMA)
            .contains(protocolName));
    }

    public void testConvertBeanToXml() throws JAXBException, ConstraintException, AccessException {
        // writable version
        final WritableVersion wv = this.model.getTestVersion();
        try {
            // experiment type
            final ExperimentType experimentType =
                ProtocolFactory.createExperimentType(wv, "test type " + new java.util.Date().getTime(),
                    Collections.EMPTY_MAP);
            // protocol
            final Protocol protocol =
                ProtocolFactory.createProtocol(wv, "my protocol" + new java.util.Date().getTime(),
                    experimentType, Collections.EMPTY_MAP);
            protocol.setIsForUse(Boolean.TRUE);
            // parameter definition
            final Map<String, Object> parameterDefinitionAttrs = new HashMap();
            parameterDefinitionAttrs.put(ParameterDefinition.PROP_PROTOCOL, protocol);
            parameterDefinitionAttrs.put(ParameterDefinition.PROP_NAME, "test parameter");
            parameterDefinitionAttrs.put(ParameterDefinition.PROP_PARAMTYPE, "Boolean");
            final ParameterDefinition parameterDefinition =
                new ParameterDefinition(wv, parameterDefinitionAttrs);
            parameterDefinition.setDefaultValue("No");
            // output sample
            final SampleCategory sampleCategory = POJOFactory.createSampleCategory(wv);
            final RefOutputSample refOutputSample = new RefOutputSample(wv, sampleCategory, protocol);
            refOutputSample.setName("test output sample");
            // input sample
            final RefInputSample refInputSample = new RefInputSample(wv, sampleCategory, protocol);
            refInputSample.setName("test input sample");
            // protocol XML bean
            final ProtocolXmlBean protocolXmlBean = new ProtocolXmlBean(protocol);
            final String output =
                XmlConvertor.convertBeanToXml(protocolXmlBean, ProtocolXmlConvertor.PROTOCOL_SCHEMA);
            //System.out.println(output);
            Assert.assertTrue(output.contains(protocol.getName()));
            Assert.assertTrue(output.contains(parameterDefinition.getName()));
            Assert.assertTrue(output.contains(refOutputSample.getName()));
            Assert.assertTrue(output.contains(refInputSample.getName()));
            protocol.delete();
            experimentType.delete();
        } finally {
            wv.abort(); // not testing persistence
        }
    }

    public void testConvertXmlToProtocolSimple() throws ConstraintException, AccessException, JAXBException,
        SAXException, IOException {
        final String xml =
            "<protocol>\n" + ProtocolXmlConvertorTest.BASIC_DETAILS + "<parameterDefinitions/>\n"
                + "<inputSamples/>\n" + "<outputSamples/>\n" + "</protocol>";
        final InputStream in = new ByteArrayInputStream(xml.getBytes());
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            System.out.println(xml);
            final Protocol protocol = ProtocolXmlConvertor.convertXmlToProtocol(version, in);
            Assert.assertNotNull(protocol);
            Assert.assertEquals(ProtocolXmlConvertorTest.UNIQUE, protocol.getName());
            Assert.assertEquals("PCR", protocol.getExperimentType().getName());
            Assert.assertEquals("objective", protocol.getObjective());
            Assert.assertEquals("method", protocol.getMethodDescription());
            Assert.assertTrue(protocol.getRemarks().contains("remarks"));
            Assert.assertEquals("details", protocol.getDetails());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testConvertXmlToProtocolWithParam() throws ConstraintException, AccessException,
        JAXBException, SAXException, IOException {
        final String xml =
            "<protocol>\n" + ProtocolXmlConvertorTest.BASIC_DETAILS + "<parameterDefinitions>"
                + ProtocolXmlConvertorTest.PARAM_DETAILS + "</parameterDefinitions>\n" + "<inputSamples/>\n"
                + "<outputSamples/>\n" + "</protocol>";
        final InputStream in = new ByteArrayInputStream(xml.getBytes());
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Protocol protocol = ProtocolXmlConvertor.convertXmlToProtocol(version, in);
            Assert.assertNotNull(protocol);
            Assert.assertNotNull(protocol.getParameterDefinitions());
            Assert.assertTrue(protocol.getParameterDefinitions().size() == 1);
            final ParameterDefinition paramDef = protocol.getParameterDefinitions().get(0);
            Assert.assertEquals(ProtocolXmlConvertorTest.UNIQUE, paramDef.getName());
            Assert.assertEquals("Float", paramDef.getParamType());
            Assert.assertEquals("45", paramDef.getDefaultValue());
            Assert.assertEquals(Boolean.TRUE, paramDef.getIsMandatory());
            Assert.assertEquals(Boolean.FALSE, paramDef.getIsResult());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testConvertXmlToProtocolWithInputSample() throws ConstraintException, AccessException,
        JAXBException, SAXException, IOException {
        final String xml =
            "<protocol>\n" + ProtocolXmlConvertorTest.BASIC_DETAILS + "<parameterDefinitions/>\n"
                + "<inputSamples>\n" + ProtocolXmlConvertorTest.INSAMPLE_DETAILS + "</inputSamples>\n"
                + "<outputSamples/>\n" + "</protocol>";
        final InputStream in = new ByteArrayInputStream(xml.getBytes());
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Protocol protocol = ProtocolXmlConvertor.convertXmlToProtocol(version, in);
            Assert.assertNotNull(protocol);
            Assert.assertNotNull(protocol.getRefInputSamples());
            Assert.assertTrue(protocol.getRefInputSamples().size() == 1);
            final RefInputSample refInSample = protocol.getRefInputSamples().iterator().next();
            Assert.assertEquals("Forward Primer", refInSample.getSampleCategory().getName());
            Assert.assertEquals("Forward primer", refInSample.getName());
            Assert.assertEquals(new Float("1.0E-6"), refInSample.getAmount());
            Assert.assertEquals("L", refInSample.getUnit());
            Assert.assertEquals("uL", refInSample.getDisplayUnit());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testConvertXmlToProtocolWithOutputSample() throws ConstraintException, AccessException,
        JAXBException, SAXException, IOException {
        final String xml =
            "<protocol>\n" + ProtocolXmlConvertorTest.BASIC_DETAILS + "<parameterDefinitions/>\n"
                + "<inputSamples/>\n" + "<outputSamples>\n" + ProtocolXmlConvertorTest.OUTSAMPLE_DETAILS
                + "</outputSamples>\n" + "</protocol>";
        final InputStream in = new ByteArrayInputStream(xml.getBytes());
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Protocol protocol = ProtocolXmlConvertor.convertXmlToProtocol(version, in);
            Assert.assertNotNull(protocol);
            Assert.assertNotNull(protocol.getRefOutputSamples());
            Assert.assertTrue(protocol.getRefOutputSamples().size() == 1);
            final RefOutputSample refOutSample = protocol.getRefOutputSamples().iterator().next();
            Assert.assertEquals("Nucleic acid", refOutSample.getSampleCategory().getName());
            Assert.assertEquals("PCR product", refOutSample.getName());
            Assert.assertEquals("L", refOutSample.getUnit());
            Assert.assertEquals("uL", refOutSample.getDisplayUnit());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testConvertXmlToProtocolWithUnknownSampleCategory() throws AccessException, JAXBException,
        SAXException, ConstraintException, IOException {
        final String xml =
            "<protocol>\n" + ProtocolXmlConvertorTest.BASIC_DETAILS + "<parameterDefinitions/>\n"
                + "<inputSamples/>\n" + "<outputSamples>\n" + ProtocolXmlConvertorTest.UNKSAMPLE_DETAILS
                + "</outputSamples>\n" + "</protocol>";
        final InputStream in = new ByteArrayInputStream(xml.getBytes());
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            try {
                ProtocolXmlConvertor.convertXmlToProtocol(version, in);
                Assert.fail("Invalid sample category!");
                //CHECKSTYLE:OFF
            } catch (final RuntimeException e) {
                // That's fine
            }
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testConvertXmlToProtocolWithBadInput() throws ConstraintException, AccessException,
        JAXBException, SAXException, IOException {
        final String xml =
            "<protocol>\n" + ProtocolXmlConvertorTest.BASIC_WRONG_DETAILS + "<parameterDefinitions/>\n"
                + "<inputSamples/>\n" + "<outputSamples>\n" + ProtocolXmlConvertorTest.UNKSAMPLE_DETAILS
                + "</outputSamples>\n" + "</protocol>";
        final InputStream in = new ByteArrayInputStream(xml.getBytes());
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            try {
                ProtocolXmlConvertor.convertXmlToProtocol(version, in);
                Assert.fail("Bad input!");
                //CHECKSTYLE:OFF
            } catch (final UnmarshalException e) {
                // That's fine
            }
        } finally {
            version.abort(); // not testing persistence
        }
    }
}
