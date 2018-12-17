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
import org.pimslims.presentation.servlet.utils.ProgressListener;
import org.pimslims.servlet.PIMSServlet;

import uk.ac.sspf.spot.beans.ConstructFastaBean;
import uk.ac.sspf.spot.beans.ConstructFilterBean;
import uk.ac.sspf.spot.pims.ConstructFasta;

/**
 * Servlet to provide SPOT Construct Progress report TODO make this obsolete by
 * http://cselnx4.dl.ac.uk:8080/jira/browse/PIMS-3163
 * 
 * @author Jon Diprose
 */
@Deprecated
// obsolete
public class SpotConstructFasta extends PIMSServlet {

    /**
     * Code to satisfy Serializable Interface
     */
    private static final long serialVersionUID = 5472658439124797387L;

    /**
     * Our display jsp
     */
    private static final String JSP = "/JSP/spot/SpotConstructFasta.jsp";

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Construct Fasta Report";
    }

    /**
     * Generate full report with no filter
     * 
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        if (!this.checkStarted(request, response)) {
            return;
        }
        final ReadableVersion version = this.getReadableVersion(request, response);

        try {
            // send the header now, since the look up may take some time
            this.writeHead(request, response, "PIMS: Construct FASTA Report");

            // Process the request// Show a progress bar
            final java.io.PrintWriter writer = response.getWriter();

            // Find all the targets
            final Collection targets = version.getAll(Target.class); // TODO page
            final ProgressListener progressBar =
                new ProgressListener(targets.size(), writer, "Formating results...");
            final Collection fastaList =
                SpotConstructFasta.processRequest(response, null, version, targets, progressBar);

            // Forward the request to the jsp
            request.setAttribute("filter", null);
            final RequestDispatcher dispatcher = request.getRequestDispatcher(SpotConstructFasta.JSP);
            // Add to the request
            request.setAttribute("fastaList", fastaList);
            // headers already sent
            dispatcher.include(request, response);

            PIMSServlet.writeFoot(response.getWriter(), request);
        } finally {

            // Done with the readableVersion
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    /**
     * Get the filter from the request and use it to filter the progress query
     * 
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
            // TODO Move to filter.populateFromRequest(request) method?
            filter.setMilestone(request.getParameter(ConstructFilterBean.MILESTONE_PARAMETER));
            filter.setOrganism(request.getParameter(ConstructFilterBean.ORGANISM_PARAMETER));
            //filter.setProject(request.getParameter(ConstructFilterBean.PROJECT_PARAMETER));
            filter.setProteinName(request.getParameter(ConstructFilterBean.PROTEIN_NAME_PARAMETER));

        }
        // Ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }
        final ReadableVersion readableVersion = this.getReadableVersion(request, response);
        try {

            // send the header now, since the look up may take some time
            this.writeHead(request, response, "PIMS: Construct FASTA Report");

            // Process the request// Show a progress bar
            final java.io.PrintWriter writer = response.getWriter();

            // Find all the targets
            final Collection targets = readableVersion.getAll(Target.class);
            if (2000 <= targets.size()) {
                throw new ServletException(
                    "Sorry, you can not use this report when you have more than 2000 targets in your db! please contact PiMS developer team.");
            }
            final ProgressListener progressBar =
                new ProgressListener(targets.size(), writer, "Formating results...");
            final Collection fastaList =
                SpotConstructFasta.processRequest(response, filter, readableVersion, targets, progressBar);

            // Forward the request to the jsp
            request.setAttribute("filter", filter);
            request.setAttribute("fastaList", fastaList);
            final RequestDispatcher dispatcher = request.getRequestDispatcher(SpotConstructFasta.JSP);
            // headers already sent
            dispatcher.include(request, response);

            PIMSServlet.writeFoot(response.getWriter(), request);
        } finally {

            // Done with the readableVersion
            if (!readableVersion.isCompleted()) {
                readableVersion.abort();
            }
        }

    }

    /**
     * <p>
     * Common parts of doGet() and doPost() handling. Gets the list of progress, adds it to the request and
     * forwards it to the JSP.
     * </p>
     * 
     * <p>
     * LATER This may need a progress bar. It's not really slow just now.
     * </p>
     * 
     * @param request - the HttpServletRequest
     * @param response - the HttpServletResponse
     * @param filter - the ConstructFilterBean to apply
     * @param readableVersion
     * @param targets
     * @param progressBar
     * @return
     * @throws ServletException
     * @throws IOException
     * 
     */
    static Collection<ConstructFastaBean> processRequest(final HttpServletResponse response,
        final ConstructFilterBean filter, final ReadableVersion readableVersion,
        final Collection<Target> targets, final ProgressListener progressBar) throws ServletException,
        IOException {

        // Get a ConstructFasta
        ConstructFasta cf;
        // Warning - potentially slow
        Collection<ConstructFastaBean> fastaList;
        cf = new ConstructFasta(readableVersion, filter);

        fastaList = cf.getProgressList(targets, progressBar);
        if (null != progressBar) {
            progressBar.setProgressHidden(true);
        }
        return fastaList;

    }

}
