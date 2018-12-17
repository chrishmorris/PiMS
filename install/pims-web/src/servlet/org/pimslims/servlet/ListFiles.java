/**
 * 
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jdom.JDOMException;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.file.FileFactory;
import org.pimslims.lab.file.IFileException;
import org.pimslims.lab.file.IFileType;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.util.File;

/**
 * List the files associated with a model object, and upload a new one. e.g.
 * /ListFiles/org.pimslims.model.target.Target:2345 x
 * 
 * @author cm65
 * 
 */
public class ListFiles extends PIMSServlet {

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
            request.setAttribute("bean", BeanFactory.newBean(page));
            version.commit();
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/ListFiles.jsp");
            dispatcher.forward(request, response);
            PIMSServlet.writeFoot(writer, request);
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
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new ServletException("no file found in submission");
        }
        final PrintWriter writer = response.getWriter();
        final String pathInfo = request.getPathInfo();
        if (null == pathInfo || 1 > pathInfo.length()) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            writer.print("No object specified");
            return;
        }
        final String hook = pathInfo.substring(1);
        final org.pimslims.dao.WritableVersion version = this.getWritableVersion(request, response);

        final org.pimslims.metamodel.ModelObject object =
            this.getRequiredObject(version, request, response, hook);
        if (object == null) {
            return; // error mesasge was provided by the
        }

        String anchor = ListFiles.ATTACHMENTS;
        try {
            // Create a new file upload handler
            final java.util.Collection<FileItem> items = ListFiles.getFileItems(request);

            // Process the uploaded items
            // At present, our form just has one item,
            // but this code is capable of handling many (Not sure about this)

            File file = null;
            String fileTitle = "";
            String fileLegend = "";
            //final String fileDescription = "";
            for (final FileItem item : items) {

                if (item.isFormField()) {
                    if (item.getFieldName().equals("fileTitle")) {
                        fileTitle = item.getString();
                    }
                    //fileLegend is for image,fileDescription is for normal file, both are legend now
                    if (item.getFieldName().equals("fileLegend")) {
                        fileLegend = item.getString();
                    }
                    if (item.getFieldName().equals("fileDescription")) {
                        fileLegend = item.getString();
                    }
                    continue;
                }

                if (0 == item.getName().trim().length()) {
                    this.writeErrorHead(request, response, "File not uploaded",
                        HttpServletResponse.SC_BAD_REQUEST);
                    writer.print("A file name must be specified");
                    PIMSServlet.writeFoot(writer, request);
                    return;
                }
                if (0 == item.getSize()) {
                    this.writeErrorHead(request, response, "File not uploaded",
                        HttpServletResponse.SC_BAD_REQUEST);
                    writer.print(item.getName() + " is length 0");
                    PIMSServlet.writeFoot(writer, request);
                    return;
                }
                // could item.write(uploadedFile); // renames the temp file
                final java.io.InputStream uploadedStream = item.getInputStream();
                version.setDefaultOwner(((LabBookEntry) object).getAccess());
                file = version.createFile(uploadedStream, item.getName(), (LabBookEntry) object);
                System.out.println("Listfiles createFile[" + item.getName() + ":" + file.getName() + "]");
                final String mimeType = item.getContentType();
                if (mimeType.startsWith("image")) {
                    anchor = ListFiles.IMAGES;
                }
                System.out.println("Listfiles setMimeType[" + file.getName() + ":" + mimeType + "]");
                file.setMimeType(mimeType);
                System.out.println("Listfiles setMimeType[" + file.getName() + "]");
                uploadedStream.close();

                System.out.println("Listfiles [" + item.getName() + ":" + mimeType + "]");
                System.out.println("Listfiles [" + file.getName() + ":" + file.getExtension() + "]");

                final IFileType iFile = FileFactory.newFile(version, item);
                if (null != iFile) {
                    System.out.println("IFile [" + iFile.getClass().getName() + "]");
                    iFile.processAsAttachment(object);
                }
                item.delete();
            }

            if (null != file) {
                file.setTitle(fileTitle.length() > 253 ? fileTitle.substring(253) : fileTitle);
                file.setLegend(fileLegend.length() > 253 ? fileLegend.substring(253) : fileLegend);
            }

            version.commit();

        } catch (final org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException e) {
            this.writeErrorHead(request, response, "File is too big", HttpServletResponse.SC_BAD_REQUEST);
            writer.print("The maximum size is: " + ListFiles.MAX_UPLOAD);
            PIMSServlet.writeFoot(writer, request);
            return;
        } catch (final FileUploadException e) {
            throw new ServletException(e);
        } catch (final IOException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final JDOMException e) {
            throw new ServletException(e);
        } catch (final IFileException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        // that's fine, show the results
        PIMSServlet.redirectPostToReferer(request, response, anchor);
    }

    public static java.util.Collection<FileItem> getFileItems(final HttpServletRequest request)
        throws FileUploadException {
        final ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        // Set upload parameters
        // TODO upload.setSizeThreshold(yourMaxMemorySize);
        upload.setSizeMax(ListFiles.MAX_UPLOAD);
        // TODO upload.setRepositoryPath(yourTempDirectory);

        // Parse the request
        final java.util.Collection<FileItem> items = upload.parseRequest(request);
        return items;
    }
}
