/**
 * pims-web org.pimslims.servlet.standard ViewLocation.java
 * 
 * @author Marc Savitsky
 * @date 31 Jan 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky * *
 * 
 */
package org.pimslims.servlet.standard;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.molecule.Extension;
import org.pimslims.presentation.construct.ExtensionBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * Display a DNA primer extension, and the related protein tag
 * 
 */
public class ViewExtension extends PIMSServlet {

    /**
     * 
     */
    public ViewExtension() {
        super();
    }

    @Override
    public String getServletInfo() {
        return "Display a DNA primer extension, and the related protein tag";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }

        final java.io.PrintWriter writer = response.getWriter();
        String pathInfo = request.getPathInfo();
        if (null == pathInfo || 0 == pathInfo.length()) {
            this.writeErrorHead(request, response, "Extension must be specified",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        pathInfo = pathInfo.substring(1); // strip initial slash

        // get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);
        try {
            final Extension extension =
                (Extension) this.getRequiredObject(version, request, response, pathInfo);
            if (null == extension) {
                return;
            }
            final ExtensionBean bean = new ExtensionBean(extension);
            //TODO are the lines below needed?
            bean.setDirection(extension.getExtensionType());
            bean.setExName(extension.get_Name());
            bean.setExSeq(extension.getSequence());
            bean.setEncodedTag(extension.getRelatedProteinTagSeq());
            bean.setRestrictionSite(extension.getRestrictionEnzyme());
            final String hook = extension.get_Hook();
            bean.setExtensionHook(hook);

            request.setAttribute("mayUpdate", extension.get_MayUpdate());
            //request.setAttribute("mayUpdate", Boolean.TRUE);
            request.setAttribute("extension", bean);

            /* request.setAttribute("accessObjects", ModelObjectShortBean.getModelObjectShortBeans(version
                .getCurrentProjects())); */

            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/standard/ViewExtension.jsp");
            dispatcher.forward(request, response);
            version.commit();
        } catch (final AbortedException e1) {
            this.log("example aborted", e1);
            writer.print("Sorry, there has been a problem, please try again.");
        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            } // tidy up the transaction
        }
    }
}
