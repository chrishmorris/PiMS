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
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.location.Location;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.sample.LocationBean;
import org.pimslims.presentation.sample.SampleLocationBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * ViewLocation
 * 
 */
@Deprecated
// Location is obsolete
public class ViewLocation extends PIMSServlet {

    /**
     * 
     */
    public ViewLocation() {
        super();
    }

    @Override
    public String getServletInfo() {
        return "Custom view of a location";
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
            this.writeErrorHead(request, response, "Location must be specified",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        pathInfo = pathInfo.substring(1); // strip initial slash

        // get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);
        try {
            MetaClass metaClass = null; // type to show, if any
            final Location location = (Location) version.get(pathInfo);
            if (null == location) {
                this.writeErrorHead(request, response, "Location not found: " + pathInfo,
                    HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            request.setAttribute("location", BeanFactory.newBean(location));
            //request.setAttribute("modelObject", ModelObjectView.getModelObjectView(location));
            metaClass = location.get_MetaClass();
            request.setAttribute("metaClass", metaClass);
            request.setAttribute("mayUpdate", location.get_MayUpdate());
            request.setAttribute("owner", location.get_Owner());
            request.setAttribute("locationTrail", SampleLocationBean.getCurrentLocationTrail(location));
            //request.setAttribute("containsLocations", ModelObjectShortBean.getBeans(location.getContents()));
            final Collection<ModelObjectShortBean> contents = LocationBean.getContents(location);
            request.setAttribute("contents", contents);
            request.setAttribute("organisation", location.getOrganisation());
/* no. disclosure
            request.setAttribute("userPersons",
                PersonUtility.getHookAndName(PersonUtility.getUserPersons(version))); */

            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/standard/org.pimslims.model.location.Location.jsp");
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
