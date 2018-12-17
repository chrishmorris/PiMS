/**
 * current-pims-web org.pimslims.servlet.standard HostList.java
 * 
 * @author susy
 * @date 24-Apr-2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 susy The copyright holder has licenced the STFC to redistribute this software
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
import org.pimslims.presentation.HostBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * HostList Prepare a list of Host Beans and html header, then forward to the presentation page
 * 
 */
public class HostList extends PIMSServlet {

    /**
     * Constructor for HostList
     */
    public HostList() {
        super();
        // 
    }

    /**
     * HostList.getServletInfo
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {

        return "HostList Prepare a list of HostBeans and html header, then forward to the presentation page";
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

        // get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);
        try {
            /*TODO need to retrieve a Collection of 'Host' objects from DB
             *  and use to create a Collection of HostBeans
             *  final Collection<HostBean> hbeans = makeHostBeans(version);
             *  final Map<String, Object> hoAttrMap = new java.util.HashMap<String, Object>();
             *  hoAttrMap.put(something, HostObject.PROP_something);
             *  final java.util.Collection<HostObject> hostObjects = rv.findAll(HostObject.class, hoAttrMap);
             *  for (final HostObject hostObject : hostObjects) {
             *      final HostBean hbean = new HostBean();
             *      hbean.setName(hostObject.getName());
             *      etc.
             *      hbeans.add(hbean);
             *  }

            *   request.setAttribute("hbeans", hbeans);
            */
            // make a dummy collection of beans to test JSP
            final Collection<HostBean> hbeans = new java.util.ArrayList<HostBean>();
            final HostBean hbean1 = new HostBean();
            final HostBean hbean2 = new HostBean();
            //bean1
            hbean1.setHostHook("org.pimslims.model.host.host:nnn1");
            hbean1.setHostName("BL21 (DE3) pLysS");
            hbean1.setHostOrganism("E.coli");
            //bean2
            hbean2.setHostHook("org.pimslims.model.host.host:nnn2");
            hbean2.setHostName("Rosetta Gami");
            hbean2.setHostOrganism("E.coli");
            hbeans.add(hbean1);
            hbeans.add(hbean2);

            //TODO request.setAttribute("mayUpdate", host.get_MayUpdate());
            request.setAttribute("mayUpdate", Boolean.TRUE);
            request.setAttribute("hostBeans", hbeans);

            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/standard/HostList.jsp");
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

    /**
     * HostList.makeHostBean
     * 
     * @return
     */
    public static HostBean makeHostBean() {

        final HostBean hb = new HostBean();
        return hb;
    }

}
