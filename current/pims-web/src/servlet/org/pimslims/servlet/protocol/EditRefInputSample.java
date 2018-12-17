/**
 * 
 */
package org.pimslims.servlet.protocol;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.protocol.ProtocolBean;
import org.pimslims.presentation.protocol.RefInputSampleBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * An example of a PIMS servlet
 * 
 * @baseUrl Example/org.pimslims.model.molecule.Molecule or
 * @baseUrl Example/org.pimslims.model.sample.Sample:124
 * 
 * @author cm65
 * 
 */
public class EditRefInputSample extends
/* common servlet that provides utility methods */PIMSServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Forwards to an edit page for the RefInputSample";
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }

        final java.io.PrintWriter writer = response.getWriter();
        String pathInfo = request.getPathInfo();
        if (null == pathInfo || 1 >= pathInfo.length()) {
            this.writeErrorHead(request, response, "No ref input sample specified",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        pathInfo = pathInfo.substring(1); // strip initial slash

        // get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return; // error message has already been sent
        }
        // always use a transaction in a try/catch block
        try {
            //final MetaClass metaClass = null; // type to show, if any
            RefInputSample ris = null;
            if (null != pathInfo) {
                ris = version.get(pathInfo); // e.g.
                // Example/org.pimslims.model.experiment.Experiment:42355
                if (null == ris) {
                    this.writeErrorHead(request, response, "No such RefInputSample: " + pathInfo,
                        HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
            }

            // make a list of sample categories
            final Collection<SampleCategory> categories =
                version.getAll(SampleCategory.class, 0, CreateWithSampleCategory.MAX_CATEGORIES);
            if (CreateWithSampleCategory.MAX_CATEGORIES <= categories.size()) {
                throw new ServletException("Sorry, you have too many sample categories");
            }
            request.setAttribute("sampleCategories", ModelObjectShortBean
                .getModelObjectShortBeans(categories));

            request.setAttribute("bean", new RefInputSampleBean(ris));
            request.setAttribute("protocol", new ProtocolBean(ris.getProtocol()));

            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/protocol/CreateEditRefInputSample.jsp");
            dispatcher.forward(request, response);
            version.commit();
        } catch (final AbortedException e1) {
            this.log("example aborted", e1);
            writer.print("Sorry, there has been a problem, please try again.");
        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            } // tidy up the transaction
        }
    }

}
