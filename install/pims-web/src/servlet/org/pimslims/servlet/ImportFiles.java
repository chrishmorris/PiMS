/**
 * 
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jdom.JDOMException;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.file.FileFactory;
import org.pimslims.lab.file.IFileException;
import org.pimslims.lab.file.IFileType;

/**
 * List the files associated with a model object, and upload a new one. e.g.
 * /ListFiles/org.pimslims.model.target.Target:2345 x
 * 
 * TODO Is this in use? Maybe from fileImport_fat.jar?
 * 
 * @author cm65
 * 
 */
public class ImportFiles extends PIMSServlet {

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
        throw new ServletException("GET is not supported by: " + this.getClass().getName());
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
        System.out.println("org.pimslims.servlet.ImportFiles doPost()");
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new ServletException("no file found in submission");
        }
        final PrintWriter writer = response.getWriter();

        // TODO improve access control here
        final AbstractModel model = ModelImpl.getModel();
        final WritableVersion version = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);

        //final org.pimslims.dao.WritableVersion version = this.getWritableVersion(request, response);
        System.out.println("org.pimslims.servlet.ImportFiles doPost(" + version + ")");

        try {
            // Create a new file upload handler
            final java.util.Collection<FileItem> items = ImportFiles.getFileItems(request);

            for (final FileItem item : items) {

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

                final IFileType iFile = FileFactory.newFile(version, item);
                if (null != iFile) {
                    System.out.println("ImportFiles IFile is instance of [" + iFile.getClass().getName()
                        + "]");
                    iFile.process(version);
                }
                item.delete();
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
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final JDOMException e) {
            throw new ServletException(e);
        } catch (final IFileException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        // that's fine, show the results
        //PIMSServlet.redirectPostToReferer(request, response, anchor);
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
