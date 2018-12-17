/**
 * V2_2-pims-web org.pimslims.servlet AjaxFileUpload.java
 * 
 * @author pvt43
 * @date 26 Aug 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.util.File;

/**
 * AjaxFileUpload
 * 
 */
@Deprecated
// suspected obsolete
public class AjaxFileUpload extends PIMSServlet {

    /**
     * 
     */
    public static final int MAX_UPLOAD = 50 * 1024 * 1024; // 50 Mb

    /**
     * AjaxFileUpload.getServletInfo
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return this.getClass().getName();
    }

    /**
     * AjaxFileUpload.doPost
     * 
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

        final String hook = pathInfo.substring(1, pathInfo.indexOf("&"));
        final String progressId = pathInfo.substring(pathInfo.indexOf("&") + 1);
        System.out.println("H" + hook + " P" + progressId);
        final WritableVersion version = this.getWritableVersion(request, response);

        final org.pimslims.metamodel.ModelObject object =
            this.getRequiredObject(version, request, response, hook);
        if (object == null) {
            return; // error message was provided by the
        }

        try {
            // Create a new file upload handler
            final DiskFileItemFactory factory = new DiskFileItemFactory();
            // TODO factory.setRepository(repository);

            factory.setSizeThreshold(AjaxFileUpload.MAX_UPLOAD * 100);
            final ServletFileUpload sfu = new ServletFileUpload(factory);
            sfu.setSizeMax(AjaxFileUpload.MAX_UPLOAD); // Maximum upload size
            /*
             this.writeHead(request, response, "FILE:");
             writer.print("The maximum size is: ");
             */

            final FileUploadProgress progressListener = new FileUploadProgress();

            request.getSession().setAttribute(progressId, progressListener);

            sfu.setProgressListener(progressListener);

            // Parse the request
            final List<FileItem> items = sfu.parseRequest(request);

            // Process the uploaded items
            // At present, our form just has one item,
            // but this code is capable of handling many
            final java.util.Iterator<FileItem> iter = items.iterator();
            String fileDesc = "";
            // Process only form fields to ensure that progressId is set prior to the file downloading
            while (iter.hasNext()) {
                final FileItem item = iter.next();
                if (item.isFormField()) {
                    // caller should set an id under which the progressListener will be provided 
                    if (item.getFieldName().equals("fileDescription")) {
                        fileDesc = item.getString();
                    }
                    // This is needed to current tab value recognition
                    if (item.getFieldName().equals("_tab")) {
                        final String curTab = item.getString();
                        if (!Util.isEmpty(curTab)) {
                            request.getSession().setAttribute("_tab", curTab);
                            // System.out.println("Set tab in Multi:" + curTab);
                        }
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
                // Write to a temp dir:
                //final java.io.File uploadedFile = new java.io.File("pathName");
                //item.write(uploadedFile); // renames the temp file

                final java.io.InputStream uploadedStream = item.getInputStream();

                final File file = version.createFile(uploadedStream, item.getName(), (LabBookEntry) object);
                file.setDescription(fileDesc.length() > 253 ? fileDesc.substring(253) : fileDesc);
                file.setMimeType(item.getContentType());
                uploadedStream.close();
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
        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        request.getSession().removeAttribute(progressId);
        System.out.println("ProgressId: removed from the session!");
        // that's fine, show the results
        //   PIMSServlet.writeFoot(writer);

        //PIMSServlet.redirectPostToReferer(request, response);
    }
}
