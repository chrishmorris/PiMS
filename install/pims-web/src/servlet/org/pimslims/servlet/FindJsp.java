/**
 * V4_1-web org.pimslims.servlet RedirectToDefaultJsp.java
 * 
 * @author cm65
 * @date 24 Aug 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * FindJsp
 * 
 * Include a custom JSP, or if none exists a default one. For example visiting
 * /read/FindJsp?_JSP=experiment/parameters/Expression.jsp gets /JSP/experiment/parameters/Default.jsp but
 * visiting /read/FindJsp/experiment/parameters?_JSP=OPPF Purification Affinity Desalt.jsp gets
 * /JSP/experiment/parameters/OPPF Purification Affinity Desalt.jsp
 */
public class FindJsp extends PIMSServlet {

    /**
     * JSP_TO_FIND String
     */
    public static final String JSP_TO_FIND = "_JSP";

    /**
     * PIMSServlet.getServletInfo
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Redirects to an appropriate JSP, using path to find servlet. ";
    }

    static final Pattern PATH_INFO = Pattern.compile("/(.*?)/([^/]*)\\.jsp");

    /**
     * RedirectToDefaultJsp.service
     * 
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final String pathInfo = request.getParameter(FindJsp.JSP_TO_FIND); // e.g. "/create/org.pimslims.model.target.Target.class"
        final Matcher m = FindJsp.PATH_INFO.matcher(pathInfo);
        if (!m.matches()) {
            throw new AssertionError("Bad path: " + request.getPathInfo());
        }
        final String jspName = m.group(2);
        final String folder = m.group(1);
        PIMSServlet.dispatchCustomJsp(request, response, jspName, folder, this.getServletContext());
    }

}
