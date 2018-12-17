/**
 * V3_1-pims-web org.pimslims.servlet Export.java
 * 
 * @author cm65
 * @date 30 Jan 2009
 * 
 *       Protein Information Management System
 * @version: 3.1
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.xmlio.ProtocolXmlBean;
import org.pimslims.xmlio.ProtocolXmlConvertor;
import org.pimslims.xmlio.XmlConvertor;

/**
 * Exports PiMS records as XML
 * 
 * This initial version only works for protocols
 * 
 */
public class Export extends PIMSServlet {

    /**
     * Export.getServletInfo
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Export a PiMS record as XML";
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final PrintWriter writer = response.getWriter();
        final String pathInfo = request.getPathInfo();
        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return;
        }
        try {
            ModelObject object = null;
            if (pathInfo != null && pathInfo.trim().length() > 0) {
                final String hook = pathInfo.substring(1);
                object = this.getRequiredObject(version, request, response, hook);
                if (null == object) {
                    return; // an error page has been shown
                }
            } else {
                PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
                writer.print("Hook is not provided"); // LATER
                // invalid
                // request
                // page
                return;
            }
            String output = null;
            if (object instanceof Protocol) {
                final Protocol protocol = (Protocol) object;
                final ProtocolXmlBean protocolXmlBean = new ProtocolXmlBean(protocol);
                output = XmlConvertor.convertBeanToXml(protocolXmlBean, ProtocolXmlConvertor.PROTOCOL_SCHEMA);
            } /* TODO add other types here */else {
                throw new ServletException("Sorry, you cannot yet export records of type: "
                    + object.getClass().getName());
            }
            response.setContentType("application/xml");
            writer.print(output);
        } catch (final JAXBException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        throw new ServletException("POST is not supported by: " + this.getClass().getName());
    }

}
