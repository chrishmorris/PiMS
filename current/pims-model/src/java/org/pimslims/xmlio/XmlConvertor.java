/**
 * pims-web org.pimslims.data XmlExporter.java
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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.pimslims.properties.PropertyGetter;
import org.xml.sax.SAXException;

/**
 * XmlExporter
 * 
 */
public class XmlConvertor {

    /**
     * Converts Java XML bean object to XML output string
     * 
     * @param bean
     * @return xml string
     * @throws JAXBException
     */
    public static String convertBeanToXml(final Object bean, final String schemaName) throws JAXBException {
        assert null != bean : "ERROR: Object must not be null.";
        try {
            final JAXBContext context = JAXBContext.newInstance(bean.getClass());
            final Marshaller marshaller = context.createMarshaller();
            if (null != schemaName) {
                final Schema schema = XmlConvertor.getSchema(schemaName);
                marshaller.setSchema(schema);
            }
            final StringWriter sw = new StringWriter();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(bean, sw);
            return sw.toString();
        } catch (final SAXException e) {
            // bad schema
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts XML input string into java XML bean
     * 
     * @param xml
     * @param classContext
     * @param schemaName
     * @return Object
     * @throws JAXBException
     * @throws SAXException
     * @throws IOException
     */
    public static <T> T convertXmlToBean(final String xml, final Class<T> classContext,
        final String schemaName) throws JAXBException, SAXException, IOException {

        // see http://www.java.net/forum/topic/glassfish/metro-and-jaxb/linkageerror-duplicate-class-definition-0
        System.setProperty("com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true");

        final InputStream in = new ByteArrayInputStream(xml.getBytes());
        final JAXBContext context = JAXBContext.newInstance(classContext);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final Schema schema = XmlConvertor.getSchema(schemaName);
        unmarshaller.setSchema(schema);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        return classContext.cast(unmarshaller.unmarshal(reader));
    }

    private static Schema getSchema(final String schemaName) throws SAXException {
        final File schemaFile = PropertyGetter.getFileProperty("xsd schema", schemaName);
        final Schema schema =
            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaFile);
        return schema;
    }

    /**
     * Converts InputStream to String
     * 
     * @param in
     * @return
     * @throws IOException
     */
    public static String convertStreamToString(final InputStream in) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        final StringBuilder sb = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        in.close();

        return sb.toString();
    }

}
