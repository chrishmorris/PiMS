/**
 * pims-web org.pimslims.servlet.location MoveObject.java
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

package org.pimslims.servlet.location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ContainerUtility;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.holder.Containable;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.location.Location;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.sample.LocationBean;
import org.pimslims.presentation.sample.SampleLocationBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * Move
 */
@Deprecated
// obsolete
public class MoveObject extends PIMSServlet {

    /**
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "manage sample locations";
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

            LocationBean parentLocationBean = null;
            if (null != request.getParameter("parentLocationHook")) {
                final String parentLocationHook = request.getParameter("parentLocationHook");
                final Location parentLocation = version.get(parentLocationHook);
                parentLocationBean = new LocationBean(parentLocation);
            }

            final List<LocationBean> allLocations = new ArrayList<LocationBean>();

            if (object instanceof org.pimslims.model.location.Location) {
                final LocationBean thisLocation = new LocationBean((Location) object);

                for (final LocationBean bean : LocationBean.getAllLocations(version)) {
                    if (!bean.getLocationBeanStack().contains(thisLocation)) {
                        allLocations.add(bean);
                    }
                }

            } else {
                allLocations.addAll(LocationBean.getAllLocationsIn(version, parentLocationBean));
            }

            if (null != request.getParameter("isAJAX")
                && "true".equals(request.getParameter("isAJAX").toString())) {
                response.setContentType("text/xml");
                final XMLOutputter xo = new XMLOutputter();
                xo.output(this.makeAjaxListXML(allLocations), response.getWriter());
                return;
            }

            Collections.sort(allLocations);
            request.setAttribute("results", allLocations);
            request.setAttribute("locationTrail",
                SampleLocationBean.getCurrentLocationTrail((Containable) object));
            request
                .setAttribute("currentobject", SampleLocationBean.getCurrentLocation((Containable) object));
            request.setAttribute("hostobject", new ModelObjectBean(object));

            version.commit();
            RequestDispatcher rd;
            rd = request.getRequestDispatcher("/JSP/location/MoveObject.jsp");

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
        final WritableVersion version = this.getWritableVersion(request, response);
        try {
            final ModelObject object = this.getRequiredObject(version, request, response, pathInfo);
            if (null == object) {
                return; // 404 already sent
            }

            final String locationHook = request.getParameter("locationhook");
            final Location location =
                (Location) this.getRequiredObject(version, request, response, locationHook);

            MoveObject.move(object, location);
            /*
            if (null == location) {
                return; // 404 already sent
            }

            if (object instanceof org.pimslims.model.sample.Sample) {
                SampleFactory.setCurrentLocation((Sample) object, location);
            }

            if (object instanceof org.pimslims.model.holder.Holder) {
                HolderFactory.setCurrentLocation((Holder) object, location);
            }

            if (object instanceof org.pimslims.model.location.Location) {
                final Location thisLocation = (Location) object;
                thisLocation.setLocation(location);
            }
            */

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

    private Document makeAjaxListXML(final Collection results) {

        final Element rootElement = new Element("list");
        for (final Object object : results) {
            final Element elem = new Element("object");
            // protocol name and hook in xml
            final LocationBean bean = (LocationBean) object;
            elem.setAttribute("name", bean.getName());
            elem.setAttribute("hook", bean.getHook());

            rootElement.addContent(elem);
        }
        final Document xml = new Document(rootElement);
        return xml;
    }

    public static void move(final Object object, final Location location) throws ConstraintException {

        if (null == location) {
            return; // 404 already sent
        }

        if (object instanceof org.pimslims.model.sample.Sample) {
            ContainerUtility.move((Sample) object, location);
        }

        if (object instanceof org.pimslims.model.holder.Holder) {
            ContainerUtility.move((Holder) object, location);
        }

        if (object instanceof org.pimslims.model.location.Location) {
            final Location thisLocation = (Location) object;
            thisLocation.setLocation(location);
        }

    }

}
