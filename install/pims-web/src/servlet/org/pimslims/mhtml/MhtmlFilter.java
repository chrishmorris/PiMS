/**
 * V5_0-web org.pimslims.mhtml MhtmlFilter.java
 * 
 * @author cm65
 * @date 16 Jul 2013
 * 
 *       Protein Information Management System
 * @version: 4.0
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.mhtml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.pimslims.util.File;

/**
 * MhtmlFilter Provides "Save" links for all PiMS pages, that saves a file that MsWord can open. The saved
 * file is in MHTML format, mime-encoded HTML. TODO there is a problem with the view of an experiment,
 * possibly because of the call to FindJsp.
 */
public class MhtmlFilter implements Filter {

    /**
     * _MHTML_IMAGES String
     */
    private static final String _MHTML_IMAGES = "_MHTML_IMAGES";

    /**
     * SAVE_AS_MHTML String
     */
    public static final String SAVE_AS_MHTML = "_MHTML";

    /**
     * ResponseWrapper that saves the data which the servlet writes
     * 
     */
    public static class ResponseWrapper extends HttpServletResponseWrapper {

        protected final ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        private ServletOutputStream sos;

        private PrintWriter printWriter;

        public ResponseWrapper(final HttpServletResponse response) {
            super(response);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            if (null != this.printWriter) {
                throw new IllegalStateException("getWriter has already been called");
            }
            if (null == this.sos) {
                this.sos = new ServletOutputStream() {
                    @Override
                    public void write(final int arg0) throws IOException {
                        ResponseWrapper.this.bytes.write(arg0);
                    }
                };
            }
            return this.sos;
        }

        /**
         * ResponseWrapper.getWriter
         * 
         * @see javax.servlet.ServletResponseWrapper#getWriter()
         */
        @Override
        public PrintWriter getWriter() throws IOException {
            if (null != this.sos) {
                throw new IllegalStateException("getOutputStream has already been called");
            }
            if (null == this.printWriter) {
                this.printWriter = new PrintWriter(this.bytes);
            }
            return this.printWriter;
        }

        @Override
        public void setStatus(final int sc, final String sm) {
            if (200 != sc) {
                System.err.println(this.getClass() + ": " + sc + " " + sm);
            }
            super.setStatus(sc, sm);
        }

        @Override
        public void setStatus(final int sc) {
            if (200 != sc) {
                System.err.println(this.getClass() + ": " + sc);
            }
            super.setStatus(sc);
        }

        /**
         * ResponseWrapper.getData
         * 
         * @return
         */
        public byte[] getData() throws IOException {
            this.bytes.close();
            return this.bytes.toByteArray();
        }

    }

    public static final String _MHTML_PARTS = "_MHTML_PARTS";

    /**
     * MhtmlFilter.destroy
     * 
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    // e.g. pims/Save/Search/org.pimslims.model.experiment.Experiment/Experiments.doc
    static final Pattern WRAPPED_URI = Pattern.compile("^/.*/Save/[^/]*(/.*)/[^/]*.(doc|xls|mhtml)");

    /**
     * MhtmlFilter.doFilter
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
     *      javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(final ServletRequest rq, final ServletResponse rs, final FilterChain chain)
        throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) rq;
        request.setAttribute(MhtmlFilter.SAVE_AS_MHTML, Boolean.TRUE);
        final HttpServletResponse response = (HttpServletResponse) rs;
        final ResponseWrapper theirResponse = new ResponseWrapper(response);
        request.setAttribute(MhtmlFilter._MHTML_IMAGES, new HashSet<String>());
        request.setAttribute(MhtmlFilter._MHTML_PARTS, new HashSet<Mhtml.Part>());

        // could take file name off URL, but there are special rules for escaping
        final String uri = request.getRequestURI();
        if (uri.endsWith(".doc") || uri.endsWith(".docx")) {
            // Word sniffs the content, and opens these fine
            response.setContentType("application/msword");
            theirResponse.addHeader("Content-Disposition", "attachment; filename=PiMS_Report.doc");
        } else if (uri.endsWith(".xls") || uri.endsWith(".xlsx")) {
            // Excel gives a warning, and then can open these
            response.setContentType("application/msexcel");
            theirResponse.addHeader("Content-Disposition", "attachment; filename=PiMS_Report.xls");
        } else {
            // or tell the truth. 
            response.setContentType("message/rfc822");
            theirResponse.addHeader("Content-Disposition", "attachment; filename=PiMS_Report.mht");
        }

        // to pass on the request without processing chain.doFilter(theirRequest, theirResponse);

        final Matcher m = MhtmlFilter.WRAPPED_URI.matcher(uri);
        if (!m.matches()) {
            throw new ServletException("Unexpected request: " + uri);
        }
        final String theirUri = m.group(1);
        final RequestDispatcher rd = request.getRequestDispatcher(theirUri);
        try {
            rd.forward(request, theirResponse);
        } catch (final Exception e) {
            // try to report errors
            throw new ServletException(e);
        }
        //System.out.println(this.getClass().getName() + ": " + theirUri);  
        final byte[] page = theirResponse.getData();
        final ServletOutputStream os = response.getOutputStream();
        final Mhtml message = new Mhtml(os, page, theirUri);
        final String relativePath = "/skins/commercial/header_logo.png";
        final String contentType = "image/png";
        this.addStaticPart(request, message, relativePath, contentType);
        this.addStaticPart(request, message, "/skins/default/css/core/headerfooter.css", "text/css");
        this.addStaticPart(request, message, "/skins/default/css/core/forms5.0.0.css", "text/css");
        this.addStaticPart(request, message, "/skins/default/css/core/widgets5.0.0.css", "text/css");
        this.addStaticPart(request, message, "/skins/default/css/core/list.css", "text/css");
        this.addStaticPart(request, message, "/skins/default/css/core/print.css", "text/css");

        // see DoListFiles
        // could Collection<org.pimslims.util.File> attachments = request.getAttribute("attachmentFiles");
        // could Collection<org.pimslims.util.File> icons = request.getAttribute("MHTML_IMAGES");
        final Collection<org.pimslims.util.File> images =
            (Collection<File>) request.getAttribute("imageFiles");
        if (null != images) {
            for (final Iterator iterator = images.iterator(); iterator.hasNext();) {
                final File file = (File) iterator.next();
                message.addFilePart(file.getFile(), file.getMimeType(), request.getContextPath()
                    + "/ViewFile/" + file.getHook());
            }
        }

        final Collection<String> icons = (Collection<String>) request.getAttribute(MhtmlFilter._MHTML_IMAGES);
        for (final Iterator iterator = icons.iterator(); iterator.hasNext();) {
            final String path = (String) iterator.next();
            final URL requestURL = new URL(request.getRequestURL().toString());
            final URL url = new URL(requestURL, path);
            message.addPublicPart(url, "image/png", path);
        }
        final Collection<Mhtml.Part> parts =
            (Collection<Mhtml.Part>) request.getAttribute(MhtmlFilter._MHTML_PARTS);
        for (final Iterator iterator = parts.iterator(); iterator.hasNext();) {
            final Mhtml.Part part = (Mhtml.Part) iterator.next();
            message.addPart(part);
        }

        message.getMessage();
        os.close();
    }

    public static void addReportPart(final HttpServletRequest request, final byte[] data,
        final String contentType, final String relativeUrl) {
        final Collection<Mhtml.Part> parts =
            (Collection<Mhtml.Part>) request.getAttribute(MhtmlFilter._MHTML_PARTS);
        if (null == parts) {
            return;
        }
        parts.add(new Mhtml.Part(data, contentType, request.getContextPath() + relativeUrl));

    }

    /**
     * MhtmlFilter.addStaticPart Adds an icon or other public resource
     * 
     * @param request
     * @param message
     * @param relativePath
     * @param contentType
     * @throws MalformedURLException
     */
    private void addStaticPart(final HttpServletRequest request, final Mhtml message,
        final String relativePath, final String contentType) throws MalformedURLException {
        final URL requestURL = new URL(request.getRequestURL().toString());
        final URL url = new URL(requestURL, request.getContextPath() + relativePath);
        message.addPublicPart(url, contentType, request.getContextPath() + relativePath);
    }

    /**
     * MhtmlFilter.init
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(final FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

}
