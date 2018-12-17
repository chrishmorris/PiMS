/**
 * 
 */
package org.pimslims.servlet.spot;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.servlet.PIMSServlet;

/**
 * An example of a PIMS servlet
 * 
 * 
 * @author cm65
 * 
 */
public class SpotReports extends
/* common servlet that provides utility methods */PIMSServlet {

    @Override
    public String getServletInfo() {
        return "SPoT Reports menu";
    }

    /**
     * Note that this servlet uses the writeHead() method, and the JSP contains no header. This is because
     * this servlet displays a progress bar. If no progress bar is used, it is better to show the header in
     * the JSP page.
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

        final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/spot/SpotReports.jsp");
        dispatcher.forward(request, response);

    }

}
