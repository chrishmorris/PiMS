/**
 * pims-web org.pimslims.servlet FileUploadStatus.java
 * 
 * @author Petr Troshin
 * @date August 2008
 * 
 *       Protein Information Management System
 * @version: 2.3
 * 
 *           Copyright (c) 2007 Petr Troshin * *
 * 
 */
package org.pimslims.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.lab.Util;

/**
 * FileUploadStatus
 * 
 */
@Deprecated
// suspected obsolete
public class FileUploadStatus extends PIMSServlet {

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final String progressId = request.getParameter("progressId");
        System.out.println("Inprogress:" + progressId);
        if (!Util.isEmpty(progressId)) {
            final FileUploadProgress progress =
                (FileUploadProgress) request.getSession().getAttribute(progressId);
            if (progress != null) {
                System.out.println("Inprogress:" + progress.getProgress());
                FileUploadStatus.sendXMLResponse(request, response, progress);
            }
        }

    }

    @Override
    public String getServletInfo() {
        return "Obtain a ProgressListener object from the session by Id supplied by the client and send a progress in an XML reply";
    }

    public static void sendXMLResponse(final HttpServletRequest request, final HttpServletResponse response,
        final FileUploadProgress progress) throws IOException {

        /*
        final Element rootElement = new Element("progress");
        if (null != progress) {
            System.out.println("Writing progress:" + progress.getProgress());
            rootElement.setAttribute("percent", progress.getProgress());
        }

        final Document xml = new Document(rootElement);
        response.setContentType("text/xml");
        final XMLOutputter xo = new XMLOutputter();
        xo.output(xml, response.getWriter());
        */

        final String per = progress.getProgress();
        final String out =
            "<div style='width: " + per
                + "%; height: 100%; text-align:center; background-color: rgb(0, 255, 0);'>" + per + "%</div>";
        response.getWriter().print(out);
    }
}
