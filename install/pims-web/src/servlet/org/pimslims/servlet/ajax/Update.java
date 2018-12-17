/**
 * V2_2-pims-web org.pimslims.servlet.ajax Update.java
 * 
 * @author pvt43
 * @date 14 Aug 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.ajax;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.servlet.PIMSServlet;

/**
 * This servlet is for generic updates done on on property only
 * 
 */

@Deprecated
// obsolete - never used How does this differ from AjaxUpdate?
public class Update extends PIMSServlet {

    /**
     * PIMSServlet.getServletInfo
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Accept field in form hook:propertyName and update values";
    }

    /**
     * Update.doPost
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final WritableVersion rw = this.getWritableVersion(request, response);
        if (rw == null) {
            return;
        }

        final String key = request.getParameter("key");
        final String value = request.getParameter("value");

        /*
        final Map<String, String[]> params = request.getParameterMap();

        for (final String key : params.keySet()) {

        */
        final int i = key.lastIndexOf(":");
        if (key.indexOf(":") < 0) {
            throw new RuntimeException("Not properly encoded value: " + key);
        }

        final String hook = key.substring(0, i);
        if (!Util.isHookValid(hook)) {
            throw new RuntimeException("Not a hook: " + hook);
        }
        try {
            final ModelObject mo = rw.get(hook);
            if (mo == null) {
                throw new RuntimeException("Not such modelobject: " + hook);
            }

            org.pimslims.servlet.Update.updateValue(rw, value, key.substring(i + 1), mo);
            System.out.println("dated key: " + key + " val: " + value);
            rw.commit();
        } catch (final ConstraintException e) {
            throw new RuntimeException(e);
        } catch (final AccessException e) {
            throw new RuntimeException(e);
        } catch (final ParseException e) {
            throw new RuntimeException(e);
        } catch (final AbortedException e) {
            throw new RuntimeException(e);
        } finally {
            if (!rw.isCompleted()) {
                rw.abort();
            }
        }

        //Update.updateChangedMap(ret, value, name, object);
    }
}
