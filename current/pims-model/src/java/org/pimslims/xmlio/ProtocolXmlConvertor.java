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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.protocol.Protocol;
import org.xml.sax.SAXException;

/**
 * XmlExporter
 * 
 */
public class ProtocolXmlConvertor extends XmlConvertor {

    /**
     * The Protocol schema used for validation
     */
    public static final String PROTOCOL_SCHEMA = "data/schemas/protocol.xsd";

    /**
     * Converts protocol XML data into protocol XML bean with schema validation
     * 
     * @param protocol xml data input stream
     * @return protocolXmlBean
     * @throws JAXBException
     * @throws SAXException
     */
    public static ProtocolXmlBean convertXmlToProtocolBean(final InputStream in) throws JAXBException,
        SAXException, IOException {
        return ProtocolXmlBean.class.cast(XmlConvertor.convertXmlToBean(XmlConvertor
            .convertStreamToString(in), ProtocolXmlBean.class, ProtocolXmlConvertor.PROTOCOL_SCHEMA));
    }

    /**
     * Converts protocol XML data into protocol model object with schema validation
     * 
     * @param writable version
     * @param protocol xml data input stream
     * @return protocol
     * @throws JAXBException
     * @throws AccessException
     * @throws ConstraintException
     * @throws SAXException
     * @throws IOException
     */
    public static Protocol convertXmlToProtocol(final WritableVersion version, final InputStream in)
        throws JAXBException, ConstraintException, AccessException, SAXException, IOException {
        final ProtocolXmlBean protocolXmlBean = ProtocolXmlConvertor.convertXmlToProtocolBean(in);
        return protocolXmlBean.getProtocol(version);
    }

}
