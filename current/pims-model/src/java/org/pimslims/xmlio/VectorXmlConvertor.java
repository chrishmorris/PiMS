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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

/**
 * VectorXmlConvertor
 * 
 */
public class VectorXmlConvertor extends XmlConvertor {

    /**
     * The Vector schema used for validation
     */
    public static final String VECTOR_SCHEMA = "data/schemas/vector.xsd";

    /**
     * Converts vector XML data into vector XML bean with schema validation
     * 
     * @param in
     * @return vectorXmlBean
     * @throws JAXBException
     * @throws SAXException
     * @throws IOException
     */
    public static VectorXmlBean convertXmlToVectorBean(final InputStream in) throws JAXBException,
        SAXException, IOException {
        return XmlConvertor.convertXmlToBean(XmlConvertor.convertStreamToString(in), VectorXmlBean.class,
            VectorXmlConvertor.VECTOR_SCHEMA);
    }
}
