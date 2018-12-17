/**
 * current-pims-web org.pimslims.servlet.standard ViewHost.java
 * 
 * @author Susy Griffiths
 * @date 23-Apr-2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 susy The copyright holder has licenced the STFC to redistribute this software
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
import org.pimslims.presentation.HostBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * Display details of Host and link to Sample stocks
 * 
 */
public class ViewHost extends PIMSServlet {

    /**
     * Constructor for ViewHost
     */
    public ViewHost() {
        super();
    }

    /**
     * ViewHost.getServletInfo
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Display details of Host and link to Sample stocks";
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
            this.writeErrorHead(request, response, "Host must be specified",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        pathInfo = pathInfo.substring(1); // strip initial slash

        // get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);
        try {
            /* TODO final Host host = this.getRequiredObject(version, request, response, pathInfo);
            if (null == extension) {
                return;
            }
            final HostBean bean = new HostBean(host); */

            // make a dummy bean to test JSP
            final HostBean hbean = new HostBean();
            hbean.setHostHook("org.pimslims.model.host.host:nnnn");
            hbean.setHostName("BL21 (DE3) pLysS");
            hbean.setHostOrganism("E.coli");
            hbean.setStrain("BL21 (DE3) pLysS");
            hbean
                .setComments("BL21(DE3)pLysS cells carry the lambda DE3 lysogen."
                    + "Also contain the pLysS plasmid, "
                    + "which constitutively expresses T7 lysozyme, an inhibitor for T7 polymerase which. "
                    + "This provides tight control of T7 RNA polymerase, which is necessary when the recombinant protein to be expressed is toxic.");
            hbean.setGenotype("F- ompT hsdSB(rB-mB-)gal dcm (DE3) pLysS (CamR)");
            hbean.setHarbouredPlasmids("pLysS, lambda DE3 lysogen");
            hbean.setAntibioticRes("Chloramphenicol");
            hbean.setSuppliers("InVitrogen, Promega");
            hbean.setHostUse("For expressing proteins that are toxic to E.Coli");
            //no selectable markers

            //TODO request.setAttribute("mayUpdate", host.get_MayUpdate());
            request.setAttribute("mayUpdate", Boolean.TRUE);
            request.setAttribute("hostBean", hbean);

            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/standard/ViewHost.jsp");
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
