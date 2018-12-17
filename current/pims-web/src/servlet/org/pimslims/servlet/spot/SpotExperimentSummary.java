/**
 * 
 */
package org.pimslims.servlet.spot;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.worklist.ConstructProgressBean;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.sample.SampleProgress;

import uk.ac.sspf.spot.beans.ConstructFilterBean;
import uk.ac.sspf.spot.pims.ExperimentSummary;

/**
 * Servlet to provide SPOT Experiment Summary report
 * 
 * @author Jon Diprose
 */
@Deprecated
// target search now reports all these details, for selected targets
public class SpotExperimentSummary extends PIMSServlet {

    /**
     * Code to satisfy Serializable Interface
     */
    private static final long serialVersionUID = 5472658439124797387L;

    /**
     * Our display jsp
     */
    private static final String JSP = "/JSP/spot/SpotExperimentSummary.jsp";

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
     * Generate full report with no filter
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // send the header now, since the look up may take some time
        this.writeHead(request, response, "PiMS: Milestone Report");

        // Process the request
        this.processRequest(request, response, null);

        PIMSServlet.writeFoot(response.getWriter(), request);

    }

    /**
     * Get the filter from the request and use it to filter the progress query
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // Get an empty filter
        final ConstructFilterBean filter = new ConstructFilterBean();

        // If we weren't asked to show all
        if (request.getParameter("showAll") == null) {

            // Populate it
            filter.setMilestone(request.getParameter(ConstructFilterBean.MILESTONE_PARAMETER));
            filter.setOrganism(request.getParameter(ConstructFilterBean.ORGANISM_PARAMETER));
            //filter.setProject(request.getParameter(ConstructFilterBean.PROJECT_PARAMETER));
            filter.setProteinName(request.getParameter(ConstructFilterBean.PROTEIN_NAME_PARAMETER));

        }

        // send the header now, since the look up may take some time
        this.writeHead(request, response, "PiMS: Milestone Report");

        // Process the request
        this.processRequest(request, response, filter);

        PIMSServlet.writeFoot(response.getWriter(), request);

    }

    /**
     * <p>
     * Common parts of doGet() and doPost() handling. Gets the list of progress, adds it to the request and
     * forwards it to the JSP.
     * </p>
     * 
     * <p>
     * LATER This may need a progress bar
     * </p>
     * 
     * @param request - the HttpServletRequest
     * @param response - the HttpServletResponse
     * @param filter - the ConstructFilterBean to apply
     * @throws ServletException
     * @throws IOException
     */
    private void processRequest(final HttpServletRequest request, final HttpServletResponse response,
        final ConstructFilterBean filter) throws ServletException, IOException {

        // Ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }

        // Get the ReadableVersion for this request
        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return;
        }

        // Get an ExperimentSummary

        // Find all the targets
        final Collection<Target> targets = version.getAll(Target.class);
        ExperimentSummary es;
        // Warning - potentially slow
        Collection<ConstructProgressBean> progressList;
        try {
            es = new ExperimentSummary(version, filter);

            progressList = es.getProgressList(targets);
            // Add to the request
            request.setAttribute("filter", es.getFilter());
            request.setAttribute("progressList", progressList);
            request.setAttribute("milestoneList", SampleProgress.getAllExpTypeNames(version));
        } finally {

            // Done with the readableVersion
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        // Get a RequestDispatcher for the jsp
        final RequestDispatcher dispatcher = request.getRequestDispatcher(SpotExperimentSummary.JSP);

        // headers already sent
        dispatcher.include(request, response);

    }

}
