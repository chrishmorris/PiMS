/**
 * pims-web org.pimslims.servlet.sample AddSampleCategory.java
 * 
 * @author Marc Savitsky
 * @date 1 Feb 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky * *
 * 
 */
package org.pimslims.servlet.sample;

import java.io.IOException;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.SampleCategoryBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * AddSampleCategory
 * 
 */
public class AddSampleCategory extends PIMSServlet {

    /**
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "add a category to a sample";
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }

        String pathInfo = request.getPathInfo();
        if (null == pathInfo || 0 == pathInfo.length()) {
            this.writeErrorHead(request, response, "Sample must be specified",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        pathInfo = pathInfo.substring(1); // strip initial slash

        // get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);
        try {
            final ModelObject object = version.get(pathInfo);
            if (null == object) {
                this.writeErrorHead(request, response, "Sample not found: " + pathInfo,
                    HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            request.setAttribute("hostobject", new ModelObjectBean(object));
            request.setAttribute("objects", SampleCategoryBean.getAllSampleCategories(version));
            final Set<SampleCategory> categories = ((AbstractSample) object).getSampleCategories();
            request.setAttribute("categories", ModelObjectShortBean.getModelObjectShortBeans(categories));

            version.commit();
            final RequestDispatcher rd = request.getRequestDispatcher("/JSP/sample/AddSampleCategory.jsp");
            rd.forward(request, response);
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

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (null == pathInfo || 0 == pathInfo.length()) {
            this.writeErrorHead(request, response, "Sample must be specified",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        pathInfo = pathInfo.substring(1); // strip initial slash
        final String objectHook = request.getParameter("object");
        if (null == objectHook || "".equals(objectHook)) {
            this.writeErrorHead(request, response, "New object must be specified",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        final WritableVersion version = this.getWritableVersion(request, response);
        try {
            final ModelObject object = version.get(pathInfo);
            if (null == object) {
                this.writeErrorHead(request, response, "Object not found: " + pathInfo,
                    HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            final SampleCategory sampleCategory = version.get(objectHook);
            if (null == sampleCategory) {
                this.writeErrorHead(request, response, "SampleCategory not found: " + objectHook,
                    HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            if (object instanceof org.pimslims.model.sample.AbstractSample) {
                final AbstractSample thisSample = (AbstractSample) object;
                thisSample.addSampleCategory(sampleCategory);
            }

            version.commit();
            PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + object.get_Hook());
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
