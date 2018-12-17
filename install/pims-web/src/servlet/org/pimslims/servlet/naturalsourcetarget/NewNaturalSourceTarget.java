/**
 * pims-web org.pimslims.servlet.naturalsourceTarget NewNaturalSourceTarget.java
 * 
 * @author Marc Savitsky
 * @date 29 Apr 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky * * either *
 * 
 */
package org.pimslims.servlet.naturalsourcetarget;

import javax.servlet.http.HttpServletRequest;

import org.pimslims.presentation.TargetBean;
import org.pimslims.servlet.spot.SpotNewTarget;

/**
 * NewNaturalSourceTarget
 * 
 */
public class NewNaturalSourceTarget extends SpotNewTarget implements javax.servlet.Servlet {
    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public NewNaturalSourceTarget() {
        super();
    }

    /**
     * @return String path to JSP
     */
    @Override
    protected String getJspPath() {
        return "/JSP/naturalSourceTarget/NewNaturalSourceTarget.jsp";
    }

    /**
     * @param request
     * @param tb
     */
    @Override
    protected void setProteinProperties(final HttpServletRequest request, final TargetBean tb) {
        // Does nothing    
    }

    /**
     * @return Servlet descriptor string
     * 
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "New NaturalSource Target page";
    }
}
