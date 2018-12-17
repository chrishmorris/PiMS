/**
 * pims-web org.pimslims.xmlio VectorXmlConvertor.java
 * 
 * @author pajanne
 * @date Apr 22, 2009
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
import java.util.Set;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.molecule.Construct;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.ReagentCatalogueEntry;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.properties.PropertyGetter;
import org.pimslims.test.POJOFactory;
import org.xml.sax.SAXException;

/**
 * VectorXmlConvertor
 * 
 */
public class VectorXmlConvertorTest extends TestCase {
    /**
     * UNIQUE String, used for avoiding name clashes in tests
     */
    private static final String UNIQUE = "Vector " + System.currentTimeMillis();

    /**
     * the basic properties of a vector, represented in XML
     */
    private static final String BASIC_DETAILS =
        "<name>" + VectorXmlConvertorTest.UNIQUE + "</name>" + "<sampleCategory>Vector</sampleCategory>"
            + "<sampleComponent name='" + VectorXmlConvertorTest.UNIQUE
            + "' componentCategory='Vector' molType='DNA' sequenceType='DNA' details='1234bp' />";

    /**
     * the basic properties for vector with resistance details, represented in XML
     */
    private static final String RESISTANCE_DETAILS =
        "<name>" + VectorXmlConvertorTest.UNIQUE + "</name>" + "<sampleCategory>Vector</sampleCategory>"
            + "<sampleComponent name='" + VectorXmlConvertorTest.UNIQUE
            + "' componentCategory='Vector' molType='DNA' sequenceType='DNA'>"
            + "<resistanceDetails name='Kannamycin' startSeq='3995' endSeq='4807' />" + "</sampleComponent>";

    /**
     * the basic properties for vector with promoter details, represented in XML
     */
    private static final String PROMOTER_DETAILS =
        "<name>" + VectorXmlConvertorTest.UNIQUE + "</name>" + "<sampleCategory>Vector</sampleCategory>"
            + "<sampleComponent name='" + VectorXmlConvertorTest.UNIQUE
            + "' componentCategory='Vector' molType='DNA' sequenceType='DNA'>"
            + "<promoterDetails name='T7 Promoter' startSeq='370' endSeq='386' />" + "</sampleComponent>";

    /**
     * the basic property of a resistance feature, represented in XML
     */
    private static final String MARKER_DETAILS =
        "<name>"
            + VectorXmlConvertorTest.UNIQUE
            + "</name>"
            + "<sampleCategory>Vector</sampleCategory>"
            + "<sampleComponent name='"
            + VectorXmlConvertorTest.UNIQUE
            + "' componentCategory='Vector' molType='DNA' sequenceType='DNA'>"
            + "<markerDetails name='Thrombin cleavage site' details='LVPR|GS' featureType='cleavage site' status='not cleaved' />"
            + "</sampleComponent>";

    /**
     * the properties of a supplier -refSampleSource
     */
    private static final String REFSAMPLESOURCE_DETAILS =
        "<name>"
            + VectorXmlConvertorTest.UNIQUE
            + "</name>"
            + "<sampleCategory>Vector</sampleCategory>"
            + "<refSampleSource catalogNum='69864-3' dataPageUrl='http://www.merckbiosciences.co.uk/product/69864' supplier='Novagen' />"
            + "<sampleComponent name='" + VectorXmlConvertorTest.UNIQUE
            + "' componentCategory='Vector' molType='DNA' sequenceType='DNA'>" + "</sampleComponent>";

    private final AbstractModel model;

    public VectorXmlConvertorTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
        PropertyGetter.setWorkingDirectory("pwd/");
    }

    public void testConvertXmlToVectorBean() throws JAXBException, SAXException, IOException {
        final String xml = "<vector>\n" + VectorXmlConvertorTest.BASIC_DETAILS + "</vector>";
        final InputStream in = new ByteArrayInputStream(xml.getBytes());
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            //System.out.println(xml);
            final VectorXmlBean vectorXmlBean = VectorXmlConvertor.convertXmlToVectorBean(in);
            Assert.assertNotNull(vectorXmlBean);
            Assert.assertEquals(VectorXmlConvertorTest.UNIQUE, vectorXmlBean.getName());
            Assert.assertEquals("Vector", vectorXmlBean.getSampleCategory());
            Assert.assertEquals(VectorXmlConvertorTest.UNIQUE, vectorXmlBean.getSampleComponent().getName());
            Assert.assertEquals("Vector", vectorXmlBean.getSampleComponent().getComponentCategory());
            Assert.assertEquals("DNA", vectorXmlBean.getSampleComponent().getMolType());
            Assert.assertEquals("DNA", vectorXmlBean.getSampleComponent().getSequenceType());
            Assert.assertEquals("1234bp", vectorXmlBean.getSampleComponent().getDetails());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testConvertXmlToVectorBeanWithResistance() throws JAXBException, SAXException, IOException {
        final String xml = "<vector>\n" + VectorXmlConvertorTest.RESISTANCE_DETAILS + "</vector>";
        final InputStream in = new ByteArrayInputStream(xml.getBytes());
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            //System.out.println(xml);
            final VectorXmlBean vectorXmlBean = VectorXmlConvertor.convertXmlToVectorBean(in);
            Assert.assertNotNull(vectorXmlBean);
            Assert.assertEquals(VectorXmlConvertorTest.UNIQUE, vectorXmlBean.getName());
            final MoleculeFeatureXmlBean featureXmlBean =
                vectorXmlBean.getSampleComponent().getResistanceDetails().get(0);
            Assert.assertEquals("Kannamycin", featureXmlBean.getName());
            Assert.assertEquals(3995, featureXmlBean.getStartSeq().intValue());
            Assert.assertEquals(4807, featureXmlBean.getEndSeq().intValue());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testConvertXmlToVectorBeanWithPromoter() throws JAXBException, SAXException, IOException {
        final String xml = "<vector>\n" + VectorXmlConvertorTest.PROMOTER_DETAILS + "</vector>";
        final InputStream in = new ByteArrayInputStream(xml.getBytes());
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            //System.out.println(xml);
            final VectorXmlBean vectorXmlBean = VectorXmlConvertor.convertXmlToVectorBean(in);
            Assert.assertNotNull(vectorXmlBean);
            Assert.assertEquals(VectorXmlConvertorTest.UNIQUE, vectorXmlBean.getName());
            final MoleculeFeatureXmlBean featureXmlBean =
                vectorXmlBean.getSampleComponent().getPromoterDetails().get(0);
            Assert.assertEquals("T7 Promoter", featureXmlBean.getName());
            Assert.assertEquals(370, featureXmlBean.getStartSeq().intValue());
            Assert.assertEquals(386, featureXmlBean.getEndSeq().intValue());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testConvertXmlToVectorBeanWithMarker() throws JAXBException, SAXException, IOException {
        final String xml = "<vector>\n" + VectorXmlConvertorTest.MARKER_DETAILS + "</vector>";
        final InputStream in = new ByteArrayInputStream(xml.getBytes());
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            //System.out.println(xml);
            final VectorXmlBean vectorXmlBean = VectorXmlConvertor.convertXmlToVectorBean(in);
            Assert.assertNotNull(vectorXmlBean);
            Assert.assertEquals(VectorXmlConvertorTest.UNIQUE, vectorXmlBean.getName());
            final MoleculeFeatureXmlBean featureXmlBean =
                vectorXmlBean.getSampleComponent().getMarkerDetails().get(0);
            Assert.assertEquals("Thrombin cleavage site", featureXmlBean.getName());
            Assert.assertEquals("cleavage site", featureXmlBean.getFeatureType());
            Assert.assertEquals("not cleaved", featureXmlBean.getStatus());
            Assert.assertEquals("LVPR|GS", featureXmlBean.getDetails());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testConvertXmlToVectorBeanWithSource() throws JAXBException, SAXException, IOException,
        ConstraintException, AccessException {
        final String xml = "<vector>\n" + VectorXmlConvertorTest.REFSAMPLESOURCE_DETAILS + "</vector>";
        final InputStream in = new ByteArrayInputStream(xml.getBytes());
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            //System.out.println(xml);
            final VectorXmlBean vectorXmlBean = VectorXmlConvertor.convertXmlToVectorBean(in);
            Assert.assertNotNull(vectorXmlBean);
            Assert.assertNotNull(vectorXmlBean.getRefSampleSource());
            final RefSample recipe = VectorXmlBean.save(version, vectorXmlBean);
            version.flush();
            final Set<ReagentCatalogueEntry> rsss = recipe.getRefSampleSources();
            Assert.assertEquals(1, rsss.size());
            for (final ReagentCatalogueEntry rss : rsss) {
                Assert.assertEquals(VectorXmlConvertorTest.UNIQUE, vectorXmlBean.getName());
                //final RefSampleSourceXmlBean rssBean = vectorXmlBean.getRefSampleSource();
                Assert.assertEquals("69864-3", rss.getCatalogNum());
                Assert.assertEquals("http://www.merckbiosciences.co.uk/product/69864", rss.getDataPageUrl());
                Assert.assertEquals("Novagen", rss.getSupplier().getName());
            }
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testSaveRecipeFromVectorBean() throws JAXBException, SAXException, IOException,
        ConstraintException, AccessException {
        final String xml = "<vector>\n" + VectorXmlConvertorTest.BASIC_DETAILS + "</vector>";
        final InputStream in = new ByteArrayInputStream(xml.getBytes());
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            //System.out.println(xml);
            final VectorXmlBean vectorXmlBean = VectorXmlConvertor.convertXmlToVectorBean(in);
            Assert.assertNotNull(vectorXmlBean);
            final RefSample recipe = VectorXmlBean.save(version, vectorXmlBean);
            Assert.assertEquals(VectorXmlConvertorTest.UNIQUE, recipe.getName());
            Assert.assertEquals("Vector", recipe.getSampleCategories().iterator().next().getName());
            final Construct vector =
                (Construct) recipe.getSampleComponents().iterator().next().getRefComponent();
            Assert.assertEquals(VectorXmlConvertorTest.UNIQUE, vector.getName());
            Assert.assertEquals("Vector", vector.getCategories().iterator().next().getName());
            Assert.assertEquals("DNA", vector.getMolType());
            Assert.assertEquals("DNA", vector.getSequenceType());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testConvertVectorBeanToXml() throws JAXBException, ConstraintException, AccessException {
        // writable version
        final WritableVersion wv = this.model.getTestVersion();
        try {
            // RefSample - recipe
            final RefSample recipe = POJOFactory.create(wv, RefSample.class);
            // RefSampleSource
            final ReagentCatalogueEntry source = POJOFactory.create(wv, ReagentCatalogueEntry.class);
            source.setRefSample(recipe);
            // construct
            final Construct vector = POJOFactory.create(wv, Construct.class);
            // SampleComponent
            new SampleComponent(wv, vector, recipe);

            // vector XML bean
            final VectorXmlBean vectorXmlBean = new VectorXmlBean(recipe);
            final String output = XmlConvertor.convertBeanToXml(vectorXmlBean, null);
            System.out.println(output);
            Assert.assertTrue(output.contains(vector.getName()));
            Assert.assertTrue(output.contains(vector.getDetails()));
            Assert.assertTrue(output.contains(vector.getMolType()));
            Assert.assertTrue(output.contains(vector.getSequenceType()));
            Assert.assertTrue(output.contains(vector.getConstructStatus()));
            Assert.assertTrue(output.contains(vector.getFunction()));
        } finally {
            wv.abort(); // not testing persistence
        }
    }

}
