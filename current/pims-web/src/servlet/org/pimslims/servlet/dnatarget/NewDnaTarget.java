package org.pimslims.servlet.dnatarget;

import javax.servlet.http.HttpServletRequest;

import org.pimslims.presentation.TargetBean;
import org.pimslims.servlet.spot.SpotNewTarget;

/**
 * Servlet implementation class for Servlet: NewDnaTarget
 * 
 */
public class NewDnaTarget extends SpotNewTarget implements javax.servlet.Servlet {
    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public NewDnaTarget() {
        super();
    }

    /**
     * @return String path to JSP
     */
    @Override
    protected String getJspPath() {
        return "/JSP/dnaTarget/NewDNATarget.jsp";
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
        return "New DNA Target page";
    }
}
