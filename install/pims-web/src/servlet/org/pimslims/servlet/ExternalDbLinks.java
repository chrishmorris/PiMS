/**
 * pims-web org.pimslims.servlet ExternalDbLinks.java
 * 
 * @author Marc Savitsky
 * @date 11 Dec 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.reference.Database;

/**
 * ExternalDbLinks
 * 
 */
public class ExternalDbLinks extends PIMSServlet {

    /**
     * Id for notes box
     */
    public static final String EXTERNALDBLINKS = "externalDbLinks";

    /**
     * Parameter name for adding a new note
     */
    public static final String ADD_NOTE = "_addNote";

    public ExternalDbLinks() {
        super();
    }

    @Override
    public String getServletInfo() {
        return "Add a note";
    }

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        // a user may end up here if their browser does not support the referer header
        // TODO send a page that does a javascript back
        throw new ServletException(this.getClass().getName() + " does not support GET");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final Map<String, String[]> parms = request.getParameterMap();

        PIMSServlet.validatePost(request);
        final WritableVersion version = this.getWritableVersion(request, response);
        if (null == version) {
            return;
        }
        try {
            String value = null; // can use this method to set to null
            final String[] values = parms.get("modelobject");

            if (1 == values.length && !"".equals(values[0])) {
                value = values[0];
            }
            if (1 < values.length) {
                throw new ServletException("Too many values for: modelobject");
            }

            final ModelObject object = version.get(value);
            if (null == object) {
                throw new ServletException("ModelObject [" + value + "] is null");
            }

            this.processRequest(version, parms, object);

            version.commit();
            PIMSServlet.redirectPostToReferer(request, response, ExternalDbLinks.EXTERNALDBLINKS);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            this.log("You aren't allowed to do that edit. You were on page: "
                + PIMSServlet.getReferer(request));
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * ${hook}:${fieldName}
     */

    public void processRequest(final WritableVersion version, final Map<String, String[]> parms,
        final ModelObject object) throws ServletException, AccessException, ConstraintException {

        final Map<String, Object> attrMap = new HashMap<String, Object>();
        attrMap.put(Attachment.PROP_PARENTENTRY, object);

        for (final Iterator iter = parms.keySet().iterator(); iter.hasNext();) {
            final String key = (String) iter.next();
            if ("modelobject".equals(key)) {
                continue;
            }
            if (key.startsWith("_")) {
                continue; // special parms, e.g. _anchor
            }
            String value = null; // can use this method to set to null
            final String[] values = parms.get(key);
            if (1 == values.length && !"".equals(values[0])) {
                value = values[0];
            }
            if (1 < values.length) {
                throw new ServletException("Too many values for: " + key);
            }

            System.out.println("ExternalDbLinks processRequest [" + key + ":" + value + "]");
            System.out.println("ExternalDbLinks processRequest [" + ExternalDbLink.PROP_DATABASE + "]");

            if (ExternalDbLink.PROP_DATABASE.equals(key)) {
                final Database dbName = version.findFirst(Database.class, Database.PROP_NAME, value);
                attrMap.put(key, dbName);
            } else {
                attrMap.put(key, value);
            }
        }

        version.create(object.get_Owner(), ExternalDbLink.class, attrMap);
    }
}
