/**
 * current-pims-web org.pimslims.servlet.ajax AjaxUpdate.java
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
import java.text.ParseException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.presentation.ModelObjectUpdateBean;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.Update;

/**
 * AjaxUpdate
 * 
 */
public class AjaxUpdate extends Update {

    /**
     * @see org.pimslims.servlet.Update#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        throw new ServletException("Get is not supported");
    }

    /**
     * @see org.pimslims.servlet.Update#getServletInfo()
     */
    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        PIMSServlet.validatePost(request);
        final WritableVersion version = this.getWritableVersion(request, response);
        if (null == version) {
            return;
        }
        final Map<String, String[]> parms = request.getParameterMap();
        try {
            final HttpSession session = request.getSession();
            final Map<ModelObject, Map<String, Object>> editedObjects =
                Update.processRequest(version, parms, session);
            request.setAttribute("changed", ModelObjectUpdateBean.getBeans(editedObjects));
            version.commit();
            final RequestDispatcher rd = request.getRequestDispatcher("/JSP/json/Update.jsp");
            rd.forward(request, response);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            this.log("Access Exception for updating for: " + PIMSServlet.getReferer(request));
            throw new ServletException(e);
        } catch (final IOException e) {
            throw new ServletException(e);
        } catch (final ParseException e) {
            this.writeErrorHead(request, response, "Invalid date", HttpServletResponse.SC_BAD_REQUEST);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
