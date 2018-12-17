/**
 * pims-web org.pimslims.servlet AjaxUnique.java
 * 
 * @author Marc Savitsky
 * @date 25 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Marc Savitsky * *
 * 
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;

/**
 * AjaxUnique
 * 
 */
public class AjaxExists extends PIMSServlet {

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Create a PiMS record and send an XML reply";
    }

    @Override
    //TODO no, only GET required
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final java.io.PrintWriter writer = response.getWriter();

        final String metaClassName = request.getPathInfo().substring(1);
        //System.out.println("AjaxExists [" + metaClassName + "]");

        MetaClass metaClass = null;
        try {
            metaClass = this.getModel().getMetaClass(metaClassName);
        } catch (final java.lang.AssertionError e) {
            //can't find metaclass by name
        }
        if (null == metaClass) {
            // TODO provide AJAX error reporter
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            writer.print("Unknown type: " + metaClassName);
            return;
        }

        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return;
        }

        try {
            final java.util.Map parms = request.getParameterMap();
            final java.util.Map<String, Object> params = new HashMap<String, Object>();

            //Map params = parseValues(version, parms, pmeta, errorMessages);
            for (final Iterator iter = parms.entrySet().iterator(); iter.hasNext();) {
                final Map.Entry entry = (Map.Entry) iter.next();
                final String key = (String) entry.getKey();
                final String[] values = (String[]) entry.getValue();
                for (int i = 0; i < values.length; i++) {
                    //System.out.println("AjaxExists Parameter [" + key + "," + values[i] + "]");
                    params.put(key, values[i]);
                }
            }

            final ModelObject mObj = version.findFirst(metaClass.getJavaClass(), params);

            /*if (null != mObj) {
                //System.out.println("AjaxExists FALSE [" + mObj.get_Name() + ":" + mObj.get_Hook() + "]");
            }*/

            AjaxExists.sendXMLResponse(request, response, mObj);

        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public static void sendXMLResponse(final HttpServletRequest request, final HttpServletResponse response,
        final ModelObject createdObject) throws IOException {
        final Element rootElement = new Element("unique");
        final Element elem = new Element("modelobject");
        if (null != createdObject) {
            elem.setAttribute("hook", createdObject.get_Hook());
        }
        rootElement.addContent(elem);

        // TODO for all roles, for all associates, add an <object> element

        final Document xml = new Document(rootElement);
        response.setContentType("text/xml");
        final XMLOutputter xo = new XMLOutputter();
        xo.output(xml, response.getWriter());
    }
}
