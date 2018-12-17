/**
 * current-pims-web org.pimslims.servlet.ajax AjaxRemove.java
 * 
 * @author cm65
 * @date 3 Mar 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.servlet.ajax;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.mru.MRUController;
import org.pimslims.servlet.PIMSServlet;

/**
 * AjaxRemove
 * 
 */
public class AjaxRemove extends PIMSServlet {

    /**
     * AjaxRemove.getServletInfo
     * 
     * 
     */
    @Override
    public String getServletInfo() {
        return "Undo some associations and give an ajax response";
    }

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        throw new ServletException("Get is not supported");
    }

    /**
     * TODO despite the name, this does not yet work by AJAX. At present, it refreshes the referrer.
     */
    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        PIMSServlet.validatePost(request);
        final WritableVersion version = this.getWritableVersion(request, response);
        if (null == version) {
            return;
        }
        //final Map<String, String[]> parms = request.getParameterMap();
        try {
            final ModelObject labBookPage =
                this.getRequiredObject(version, request, response, request.getParameter("from"));
            if (null == labBookPage) {
                return; // 404 already sent
            }
            final String roleName = request.getParameter("role");
            final Collection<ModelObject> removed =
                this.getObjects(version, request.getParameterValues("removed"));
            AjaxRemove.processRequest(labBookPage, roleName, removed);
            MRUController.addObjects(version.getUsername(), removed);
            request.setAttribute("role", roleName);
            request.setAttribute("labBookPage", new ModelObjectShortBean(labBookPage));
            request.setAttribute("removed", ModelObjectShortBean.getBeans(removed));
            request.setAttribute("associated", ModelObjectShortBean.getBeans(labBookPage.get(roleName)));
            version.commit();
            /* for Ajax: final RequestDispatcher rd = request.getRequestDispatcher("/JSP/json/Remove.jsp");
            rd.forward(request, response); */
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

    /**
     * AjaxRemove.getObjects
     * 
     * @param parameterValues
     * @return
     */
    private Collection<ModelObject> getObjects(final ReadableVersion version, final String[] parameterValues) {
        final Collection<ModelObject> ret = new ArrayList<ModelObject>(parameterValues.length);
        for (int i = 0; i < parameterValues.length; i++) {
            ret.add(version.get(parameterValues[i]));
        }
        return ret;
    }

    static final void processRequest(final ModelObject labBookPage, final String role,
        final Collection<ModelObject> removed) throws ConstraintException {
        labBookPage.remove(role, removed);
    }

}
