/**
 * pims-web org.pimslims.servlet.naturalsourcetarget NaturalSourceTarget.java
 * 
 * @author Marc Savitsky
 * @date 29 Apr 2008
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2008 Marc Savitsky *  * either * 
    * 
  
  */
package org.pimslims.servlet.naturalsourcetarget;

import org.pimslims.servlet.spot.SpotTarget;

/**
 * NaturalSourceTarget
 * 
 */

public class NaturalSourceTarget extends SpotTarget implements javax.servlet.Servlet {
    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public NaturalSourceTarget() {
        super();
    }

    /**
     * @return String path to JSP
     */
    @Override
    protected String getJspPath() {
        return "/JSP/naturalsourceTarget/NaturalSourceTarget.jsp";
    }

    /**
     * @return Servlet descriptor string
     * 
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Natural Source Target page";
    }

}
