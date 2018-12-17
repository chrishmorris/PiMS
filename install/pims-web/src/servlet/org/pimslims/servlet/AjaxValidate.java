/**
 * pims-web org.pimslims.servlet AjaxValidate.java
 * 
 * @author Marc Savitsky
 * @date 22 Nov 2007
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.properties.PropertyGetter;

/**
 * AjaxValidate
 * 
 */
public class AjaxValidate extends PIMSServlet {

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Validate and send an XML reply";
    }

    @Override
    //TODO only GET is needed
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        //System.out.println("AjaxValidate");
        //final ServletContext scontext = this.getServletContext();

        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return;
        }

        final java.io.PrintWriter writer = response.getWriter();

        final String metaClassName = request.getPathInfo().substring(1);
        final MetaClass metaClass = this.getModel().getMetaClass(metaClassName);
        if (null == metaClass) {
            // TODO provide AJAX error reporter
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            writer.print("Unknown type: " + metaClassName);
            return;
        }

        //System.out.println("AjaxValidate [" + metaClass.getMetaClassName() + "]");

        final java.util.Map parms = request.getParameterMap();

        final java.util.Map<String, Object> params = new HashMap<String, Object>();
        //Map params = parseValues(version, parms, pmeta, errorMessages);
        for (final Iterator iter = parms.entrySet().iterator(); iter.hasNext();) {
            final Map.Entry entry = (Map.Entry) iter.next();
            final String key = (String) entry.getKey();
            final String[] values = (String[]) entry.getValue();
            for (int i = 0; i < values.length; i++) {
                params.put(key, values[i]);
            }
        }

        try {

            String errorString = null;

            if (metaClassName.equals("org.pimslims.model.experiment.ExperimentGroup")) {
                //String pattern = (String) scontext.getAttribute("plate_name_pattern");
                final String pattern = PropertyGetter.getStringProperty("plate_name_pattern", null);
                //System.out.println("AjaxValidate pattern [" + pattern + "]");
                errorString = AjaxValidate.validateExperimentGroup(version, metaClass, params, pattern);
            }

            AjaxValidate.sendXMLResponse(request, response, errorString);

        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public static void sendXMLResponse(final HttpServletRequest request, final HttpServletResponse response,
        final String error) throws IOException {
        //System.out.println("sendXMLResponse [" + error + "]");
        final Element rootElement = new Element("validate");
        final Element elem = new Element("modelobject");
        if (null != error) {
            elem.setAttribute("error", error);
        }
        rootElement.addContent(elem);

        // TODO for all roles, for all associates, add an <object> element

        final Document xml = new Document(rootElement);
        response.setContentType("text/xml");
        final XMLOutputter xo = new XMLOutputter();
        xo.output(xml, response.getWriter());
    }

    public static String validateExperimentGroup(final ReadableVersion version, final MetaClass metaClass,
        final Map<String, Object> params, final String pattern) {

        for (final Iterator iter = params.entrySet().iterator(); iter.hasNext();) {
            final Map.Entry entry = (Map.Entry) iter.next();
            final String key = (String) entry.getKey();
            final String value = (String) entry.getValue();
            //System.out.println("AjaxValidate.validateExperimentGroup [" + metaClass.getMetaClassName() + ":"
            //    + key + ":" + value + ":" + pattern + "]");

            if (key.equals("name")) {

                final ModelObject mObj = version.findFirst(metaClass.getJavaClass(), params);
                if (null != mObj) {
                    return "an object with this name already exists";
                }

                if (pattern == null || pattern.length() < 1) {
                    continue;
                }

                final Pattern REGEXP = Pattern.compile(pattern);

                final Matcher matcher = REGEXP.matcher(value.trim());
                if (!matcher.matches()) {
                    return "the value [" + value + "] does not validate to the pattern [" + "PCRnnn" + "]";
                }
            }
        }
        return null;
    }
}
