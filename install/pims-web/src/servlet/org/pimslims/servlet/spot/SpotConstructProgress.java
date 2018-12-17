/**
 * 
 */
package org.pimslims.servlet.spot;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.servlet.utils.ProgressListener;
import org.pimslims.presentation.worklist.ConstructProgressBean;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.sample.SampleProgress;

import uk.ac.sspf.spot.beans.ConstructFilterBean;
import uk.ac.sspf.spot.pims.ConstructProgress;

/**
 * Servlet to provide SPOT Construct Progress report
 * 
 * TODO make this obsolete by http://cselnx4.dl.ac.uk:8080/jira/browse/PIMS-3164
 * 
 * @author Jon Diprose
 */
@Deprecated
// too slow, lists all historic constructs
public class SpotConstructProgress extends PIMSServlet {

    /**
     * Code to satisfy Serializable Interface
     */
    private static final long serialVersionUID = 5472658439124797387L;

    /**
     * Our display jsp
     */
    private static final String JSP = "/JSP/spot/ConstructProgress.jsp";

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Construct Progress Report";
    }

    /**
     * Get the filter from the request and use it to filter the progress query
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // Get an empty filter
        final ConstructFilterBean filter = new ConstructFilterBean();

        // Populate it
        if (null != request.getParameter(ConstructFilterBean.MILESTONE_PARAMETER)
            && !ConstructFilterBean.RESULT_ANY.equalsIgnoreCase(request
                .getParameter(ConstructFilterBean.MILESTONE_PARAMETER))) {
            filter.setMilestone(request.getParameter(ConstructFilterBean.MILESTONE_PARAMETER));
        }
        filter.setOrganism(request.getParameter(ConstructFilterBean.ORGANISM_PARAMETER));
        //filter.setProject(request.getParameter(ConstructFilterBean.PROJECT_PARAMETER));
        filter.setProteinName(request.getParameter(ConstructFilterBean.PROTEIN_NAME_PARAMETER));
        if (null != request.getParameter(ConstructFilterBean.RESULT_PARAMETER)) {
            filter.setResult(request.getParameter(ConstructFilterBean.RESULT_PARAMETER));
        }
        final String daysAgo = request.getParameter(ConstructFilterBean.DAYS_AGO_PARAMETER);
        if (null != daysAgo && !"".equals(daysAgo)) {
            try {
                filter.setDaysAgo(Integer.parseInt(daysAgo));
            } catch (final NumberFormatException e) {
                this.writeErrorHead(request, response, "Not a number: " + daysAgo,
                    HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        // Process the request
        this.processRequest(request, response, filter);

    }

    /**
     * <p>
     * Common parts of doGet() and doPost() handling. Gets the list of progress, adds it to the request and
     * forwards it to the JSP.
     * </p>
     * 
     * <p>
     * </p>
     * 
     * @param request - the HttpServletRequest
     * @param response - the HttpServletResponse
     * @param filter - the ConstructFilterBean to apply
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequest(final HttpServletRequest request, final HttpServletResponse response,
        final ConstructFilterBean filter) throws ServletException, IOException {
        System.out.println("--ConstructProgress processRequest--");
        // Ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }
        final PrintWriter writer = response.getWriter();

        // send the header now, since the look up may take some time
        this.writeHead(request, response, "PIMS: Construct Progress");

        final ReadableVersion readableVersion = this.getReadableVersion(request, response);
        if (readableVersion == null) {
            return;
        }
        try {
            // Get a ConstructProgress

            final Collection<Target> targets = ConstructProgress.getAll(readableVersion);
            final ConstructProgress cp = new ConstructProgress(readableVersion, filter);
            final ProgressListener progressBar =
                new ProgressListener(targets.size(), writer, "Formating results...");
            // Warning - potentially slow
            final Collection<ConstructProgressBean> progressList = cp.getProgressList(targets, progressBar);

            // Add to the request
            request.setAttribute("filter", filter);
            request.setAttribute("progressList", progressList);
            //request.setAttribute("milestoneList", SampleProgress.getAllExpTypeNames(readableVersion));
            request.setAttribute("milestoneList", SampleProgress.getAllStatusNames(readableVersion));

            // Get a RequestDispatcher for the jsp
            final RequestDispatcher dispatcher = request.getRequestDispatcher(SpotConstructProgress.JSP);

            // headers already sent
            dispatcher.include(request, response);
            PIMSServlet.writeFoot(writer, request);
        } finally {
            // Done with the readableVersion
            if (!readableVersion.isCompleted()) {
                readableVersion.abort();
            }
        }

    }

}
