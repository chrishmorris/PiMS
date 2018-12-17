package org.pimslims.servlet.dnatarget;

import org.pimslims.servlet.spot.SpotTarget;

/**
 * Servlet implementation class for Servlet: DnaTarget
 * 
 */
public class DnaTarget extends SpotTarget implements javax.servlet.Servlet {
    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public DnaTarget() {
        super();
    }

    /**
     * @return String path to JSP
     */
    @Override
    protected String getJspPath() {
        return "/JSP/dnaTarget/DNATarget.jsp";
    }

    /**
     * @return Servlet descriptor string
     * 
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "DNA Target page";
    }

}
