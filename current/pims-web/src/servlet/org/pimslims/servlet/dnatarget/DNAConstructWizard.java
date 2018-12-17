package org.pimslims.servlet.dnatarget;

import org.pimslims.servlet.spot.CreateExpressionObjective;

/**
 * Servlet implementation class for Servlet: DNAConstructWizard
 * 
 */
public class DNAConstructWizard extends CreateExpressionObjective implements javax.servlet.Servlet {
    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public DNAConstructWizard() {
        super();
    }

    /**
     * @return String path to JSP
     */
    @Override
    protected String getJspPath() {
        return "/JSP/dnaTarget/DNAConstructWizardStep1.jsp";
    }

    /**
     * @param request a request from a URL
     * @param cb an instance of org.pimslims.presentation.construct.ConstructBean
     * 
     * 
     *            private void setDnaAndProtProperties(final HttpServletRequest request, final ConstructBean
     *            cb) { final String targetDnaSeq = request.getParameter("target_dna_seq");
     *            cb.setTargetDnaSeq(targetDnaSeq); }
     */

    /**
     * @param request
     * @param cb private void setNameForTarget(final HttpServletRequest request, final ConstructBean cb) {
     *            cb.setTargetName(request.getParameter("target_name")); }
     */

    /**
     * @return Servlet descriptor string
     * 
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "New DNA Construct page";
    }
}
