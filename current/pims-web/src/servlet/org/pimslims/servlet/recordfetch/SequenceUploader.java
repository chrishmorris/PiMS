/**
 * pims-web org.pimslims.servlet.recordfetch SequenceUploader.java
 * 
 * @author pvt43
 * @date 23 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 pvt43
 * 
 * 
 */
package org.pimslims.servlet.recordfetch;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.sequence.OPPFSequenceLoader;
import org.pimslims.lab.sequence.PlatePCRProductSequenceComparator;
import org.pimslims.servlet.ListFiles;
import org.pimslims.servlet.PIMSServlet;

/**
 * SequenceUploader Upload sequence archive with sequences out of sequinator to check against sequence in the
 * database (PCR Product sequence) Archive is assumed to contain text files with .seq extension but it is not
 * limited to this (other files are allowed as well)
 */
public class SequenceUploader extends PIMSServlet {

    /**
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Load sequence archive or plain text file for plate sequence comparison";
    }

    /**
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse) Method put a collection of a plates which can be used in
     *      sequence comparison in a request which is later rendered by ChoosePlate.jsp It also returns the
     *      results of file upload done in doPost()
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final ReadableVersion rv = this.getReadableVersion(request, response);
        if (rv == null) {
            return;
        }
        request.setAttribute("version", rv);
        request.setAttribute("plates", PlatePCRProductSequenceComparator.getEligiblePlates(rv));
        final RequestDispatcher rd = request.getRequestDispatcher("/JSP/plateSequenceCheck/ChoosePlate.jsp");
        rd.forward(request, response);
        try {
            rv.commit();
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }

    }

    /**
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final WritableVersion rw = this.getWritableVersion(request, response);
        if (rw == null) {
            return;
        }
        final PrintWriter writer = response.getWriter();
        OPPFSequenceLoader sl = null;
        try {
            sl = new OPPFSequenceLoader(rw, this.uploadFile(request, response));
            rw.commit();
        } catch (final AccessException e) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_FORBIDDEN);
            writer.print("You are not allowed to make these changes");
            return;
        } catch (final ConstraintException cex) {
            rw.abort();
            // TODO could use ConstraintException.attributeName to show the
            // error message by the input field
            request.setAttribute("javax.servlet.error.exception", cex);
            request.getRequestDispatcher("/update/ConstraintErrorPage").forward(request, response);
            return;
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } finally {
            if (!rw.isCompleted()) {
                rw.abort();
            }
        }
        request.setAttribute("seqloader", sl);
        System.out.println("DONE +++++");
        final RequestDispatcher rd = request.getRequestDispatcher("/JSP/plateSequenceCheck/ChoosePlate.jsp");
        rd.forward(request, response);
    }

    private InputStream uploadFile(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        InputStream uploadedStream = null;
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new ServletException("no file found in submission");
        }
        try {
            // Create a new file upload handler
            final DiskFileUpload upload = new DiskFileUpload();
            // Set upload parameters
            // TODO upload.setSizeThreshold(yourMaxMemorySize);
            upload.setSizeMax(ListFiles.MAX_UPLOAD);
            // TODO upload.setRepositoryPath(yourTempDirectory);

            // Parse the request
            final java.util.Collection items = upload.parseRequest(request);

            // Process the uploaded items
            // At present, our form just has one item,
            // but this code is capable of handling many

            final java.util.Iterator iter = items.iterator();

            while (iter.hasNext()) {
                final FileItem item = (FileItem) iter.next();

                if (item.isFormField()) {
                    continue;
                }
                if (0 == item.getName().trim().length()) {
                    this.writeErrorHead(request, response, "File not uploaded",
                        HttpServletResponse.SC_BAD_REQUEST);
                    final PrintWriter writer = response.getWriter();
                    writer.print("A file name must be specified");
                    PIMSServlet.writeFoot(writer, request);
                    return null;
                }
                if (0 == item.getSize()) {
                    this.writeErrorHead(request, response, "File not uploaded",
                        HttpServletResponse.SC_BAD_REQUEST);
                    final PrintWriter writer = response.getWriter();
                    writer.print(item.getName() + " is length 0");
                    PIMSServlet.writeFoot(writer, request);
                    return null;
                }
                // could item.write(uploadedFile); // renames the temp file
                //result = item.getString();

                uploadedStream = item.getInputStream();
                //TODO item.delete();
            }

        } catch (final SizeLimitExceededException e) {
            this.writeErrorHead(request, response, "File is too big", HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } catch (final FileUploadException e) {
            throw new ServletException(e);
        } catch (final IOException e) {
            throw new ServletException(e);
        }
        // that's fine, show the results

        return uploadedStream;
    }
}
