/**
 * V5_0-web org.pimslims.servlet Reorder.java
 * 
 * @author cm65
 * @date 13 Jan 2014
 * 
 *       Protein Information Management System
 * @version: 4.5
 * 
 *           Copyright (c) 2014 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.protocol.ParameterDefinition;

/**
 * Reorder
 * 
 */
public class Reorder extends PIMSServlet {

    /**
     * Reorder.getServletInfo Move a child object up or down a list TODO currently just parameter definitions,
     * generalise it.
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Move a record up or down in a list";
    }

    /**
     * Reorder.doGet
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        throw new ServletException("Get is not supported");
    }

    /**
     * Reorder.doPost
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final WritableVersion version = this.getWritableVersion(request, response);
        if (null == version) {
            return;
        }
        try {
            final ParameterDefinition object =
                (ParameterDefinition) this.getRequiredObject(version, request, response,
                    this.getHook(request));
            assert object instanceof ParameterDefinition;
            object.moveDown();
            version.commit();
            PIMSServlet.redirectPostToReferer(request, response);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
