/**
 * 
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.util.File;

/**
 * List the files associated with a PiMS record
 * 
 * @author cm65
 * 
 */
@WebServlet("/read/DoListFiles/*")
public class DoListFiles extends PIMSServlet {

    /**
     * Id attachments box
     */
    public static final String ATTACHMENTS = "attachments";

    /**
     * Id for images box
     */
    public static final String IMAGES = "images";

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
        return "Manage files";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final java.io.PrintWriter writer = response.getWriter();
        final String pathInfo = request.getPathInfo();
        if (null == pathInfo || 1 > pathInfo.length()) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("No object specified");
            return;
        }
        final String hook = pathInfo.substring(1);
        final org.pimslims.dao.ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return;
        }

        try {
            final LabBookEntry page = (LabBookEntry) this.getRequiredObject(version, request, response, hook);
            if (page == null) {
                return; // error mesasge was provided by PIMSServlet
            }
            // TODO convert size from "8356342" to "8M"            

            // call JSP to show search form and results
            final ModelObjectBean bean = BeanFactory.newBean(page);
            request.setAttribute("bean", bean);

            final Collection<File> files = page.get_Files();
            if (page instanceof Experiment) {
                final ExperimentGroup group = ((Experiment) page).getExperimentGroup();
                if (null != group) {
                    files.addAll(group.get_Files());
                }
            }
            final Collection<File> images = new ArrayList<File>(files.size());
            final Collection<File> attachments = new ArrayList<File>(files.size());
            for (final File file : files) {
                if (file.getMimeType().startsWith("image")) {
                    images.add(file);
                } else {
                    attachments.add(file);
                }
            }

            request.setAttribute("attachmentFiles", attachments);
            request.setAttribute("imageFiles", images);
            version.commit();
            RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/DoListFiles.jsp");
            if ("image".equals(request.getParameter("type"))) {
                dispatcher = request.getRequestDispatcher("/JSP/DoListImages.jsp");
            }

            // headers already sent
            dispatcher.include(request, response);
        } catch (final AbortedException e1) {
            this.log("list aborted", e1);
            writer.print("Sorry, there has been a problem, please try again.");
        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
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
        throw new ServletException("POST not supported by: " + this.getClass().getName());
    }

}
