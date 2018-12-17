/**
 * 
 */
package org.pimslims.servlet.protocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.ExperimentTypeUtil;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.Database;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.protocol.ProtocolBean;
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
public class ViewProtocol extends
/* common servlet that provides utility methods */PIMSServlet {

    //private static final int MAX_CATEGORIES = 1000;

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
        return "Custom view of a protocol";
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

        final java.io.PrintWriter writer = response.getWriter();
        String pathInfo = request.getPathInfo();
        if (null == pathInfo || 1 >= pathInfo.length()) {
            this.writeErrorHead(request, response, "No protocol specified",
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
            final MetaClass metaClass = null; // type to show, if any
            Protocol protocol = null;
            if (null != pathInfo) {
                protocol = version.get(pathInfo); // e.g.
                // Example/org.pimslims.model.experiment.Experiment:42355
                if (null == protocol) {
                    this.writeErrorHead(request, response, "No such protocol: " + pathInfo,
                        HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
            }
            // LATER allow non-editing view, even if may edit
            boolean editing = protocol.get_MayUpdate();
            // PiMS 1074 The protocol editor passes a variable called "mayUpdate" to the JSP.
            //           If there are experiments for the protocol, this should be set to false.
            if (!protocol.getExperiments().isEmpty()) {
                editing = false;
            }

            request.setAttribute("metaClass", metaClass);

            // List sampleCategories = sampleCategoriesAsBeans(version);
            // Collections.sort(sampleCategories);
            // request.setAttribute("sampleCategories", sampleCategories);
            // } end if edit

            request.setAttribute("isEdit", editing);
            request.setAttribute("mayUpdate", editing); // load noEdit CSS if
            // not editing
            request.setAttribute("mayChangeForUse", protocol.get_MayUpdate());
            request.setAttribute("protocol", new ProtocolBean(protocol));
            request.setAttribute("protocolHook", protocol.get_Hook());
            request.setAttribute("experimentType", protocol.getExperimentType());
            //for protocol method file
            //request.setAttribute("modelObject", ModelObjectView.getModelObjectView(protocol));
            request.setAttribute("files", protocol.get_Files());

            final List<ModelObject> types =
                new ArrayList(PIMSServlet.getAll(version, org.pimslims.model.reference.ExperimentType.class));
            Collections.sort(types, ExperimentTypeUtil.ALPHABETICAL_ORDER_OF_NAME);
            request.setAttribute("experimentTypes", ModelObjectShortBean.getModelObjectShortBeans(types));
            request.setAttribute("instrumentTypes", ModelObjectShortBean.getModelObjectShortBeans(PIMSServlet
                .getAll(version, org.pimslims.model.reference.InstrumentType.class)));

            request.setAttribute("bean", BeanFactory.newBean(protocol));

            final Collection<Database> dbNames =
                PIMSServlet.getAll(version, org.pimslims.model.reference.Database.class);
            request.setAttribute("dbnames", ModelObjectBean.getModelObjectBeans(dbNames));

            request.setAttribute("description", "This is the protocol description");
            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/protocol/ViewProtocol.jsp");
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
