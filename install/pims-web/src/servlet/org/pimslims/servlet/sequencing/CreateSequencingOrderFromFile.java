/**
 * 
 */
package org.pimslims.servlet.sequencing;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.management.InvalidAttributeValueException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadBase;
import org.pimslims.dao.WritableVersion;
import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.util.File;
import org.pimslims.utils.experiment.Utils;
import org.pimslims.utils.sequenator.SequencingInputDataParser;

/**
 * Load Leeds sequencing order from Excel file and record it to PIMS.
 * 
 * @author pvt43
 * 
 */
/**
 * @author Petr Troshin
 * 
 */
public class CreateSequencingOrderFromFile extends PIMSServlet {

    /**
     * 
     */
    public static final int MAX_UPLOAD = 20 * 1024 * 1024;

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Load Leeds Sequencing order";
    }

    /*
     * (non-Javadoc)
     *
     * @see http://jakarta.apache.org/commons/fileupload/apidocs/index.html
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        if (!FileUploadBase.isMultipartContent(request)) {
            throw new ServletException("no file found in submission");
        }

        WritableVersion version = null;
        try {
            version = this.getWritableVersion(request, response);
            ((WritableVersionImpl) version).getFlushMode().setFlushSessionAfterCreate(false);

            final Protocol protocol = Utils.getSOProtocol(version);
            if (protocol == null) {
                request
                    .setAttribute(
                        "message",
                        "Please contact developers with message: Please execute loadLeedsProtocols ant task to load Sequencing order protocol.");
                final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
                dispatcher.forward(request, response);
                ((WritableVersionImpl) version).getFlushMode().setFlushSessionAfterCreate(true);
                this.commit(request, response, version);
                return;
            }

            // Get File
            final File file = FixPlateLayout.uploadFile(request, response, version, "SequencingOrderFile");
            if (file == null) {
                throw new ServletException("File upload fails. Please contact PIMS developers!");
            }

            // Parse, validate data from the file 
            final SequencingInputDataParser parser =
                new SequencingInputDataParser(version, new FileInputStream(file.getFile()));
            parser.validateData();

            // Record data into experiments 
            final String orderId = parser.recordExperiments(protocol);
            final List<Experiment> exps = Utils.getExperimentsFromOrder(orderId, version);

            // Set attributes for rendering
            CreateSequencingOrder.setCommonAttributes(request, version, protocol, orderId, false, exps
                .iterator().next());
            request.setAttribute(CreateSequencingOrder.isNew, true);
            final RequestDispatcher rd = request.getRequestDispatcher("/JSP/sequencing/LoadInput.jsp");
            rd.forward(request, response);

            ((WritableVersionImpl) version).getFlushMode().setFlushSessionAfterCreate(false);
            this.commit(request, response, version);

        } catch (final IOException e) {
            throw new ServletException(e);
        } catch (final InvalidAttributeValueException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final AddressException e) {
            request.setAttribute("message",
                "It appears that the user email is incorrect. Please see below for detailed explanation: \n"
                    + e.getLocalizedMessage() + "\n Please correct email and try again.");
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
            dispatcher.forward(request, response);

            ((WritableVersionImpl) version).getFlushMode().setFlushSessionAfterCreate(true);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }
}