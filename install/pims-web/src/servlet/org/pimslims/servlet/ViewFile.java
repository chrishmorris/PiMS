/**
 * 
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.model.core.Annotation;
import org.pimslims.util.FileImpl;

/**
 * @author cm65
 * 
 */
public class ViewFile extends PIMSServlet {

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final String pathInfo = request.getPathInfo();
        if (null == pathInfo || 1 > pathInfo.length()) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("No object specified");
            return;
        }
        int last = pathInfo.lastIndexOf("/");
        if (0 == last) {
            // only one /
            last = pathInfo.length();
        }
        final String hook = pathInfo.substring(1, last);
        final org.pimslims.dao.ReadableVersion version = this.getReadableVersion(request, response);
        if (null == version) {
            return;
        }
        try {
            final Annotation object = (Annotation) this.getRequiredObject(version, request, response, hook);
            if (object == null) {
                return; // error message was provided by getRequiredObject
            }

            final org.pimslims.util.File file = FileImpl.getFile(object);
            String mimeType;
            try {
                file.open();
                mimeType = file.getMimeType();
            } catch (final IOException e) {
                this.writeErrorHead(request, response, "Sorry, unable to read file",
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            response.setContentType(mimeType);
            final String browser = request.getHeader("User-Agent");
            if (null != browser && browser.toLowerCase().contains("safari") && null != mimeType
                && mimeType.startsWith("text/")) {
                // safari misbehaves for all text/ mime types
                if ("text/csv".equals(mimeType)) {
                    response.setContentType("application/csv");
                }
            }

            // send the file to the browser
            response.setStatus(HttpServletResponse.SC_OK);
            // JMD Fix for IE HTTPS download issue
            //  files are never modified, so age can be long
            response.setHeader("cache-control", "private, max-age=" + 7 * 24 * 60 * 60);
            if (response.containsHeader("Pragma")) {
                response.setHeader("Pragma", "");
            }
            final OutputStream out = response.getOutputStream();
            final byte[] buffer = new byte[256];
            int length;
            while (true) {
                length = file.read(buffer);
                if (0 >= length) {
                    break;
                }
                out.write(buffer, 0, length);
            }
        } finally {
            if (!version.isCompleted()) {
                version.abort(); // can't send any error messages at this
                // point
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Display the contents of a file";
    }

}
