/**
 * V5_0-web org.pimslims.mhtml Mhtml.java
 * 
 * @author cm65
 * @date 15 Jul 2013
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.mhtml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Mhtml
 * 
 */
public class Mhtml {

    /**
     * StringSource
     * 
     */
    public static class Part implements DataSource {

        private final byte[] data;

        private final String contentType;

        private final String relativeUrl;

        /**
         * Constructor for HtmlSource
         * 
         * @param html
         */
        public Part(final byte[] data, final String contentType, final String relativeUrl) {
            super();
            this.data = data.clone();
            this.contentType = contentType;
            this.relativeUrl = relativeUrl;
        }

        /**
         * StringSource.getContentType
         * 
         * @see javax.activation.DataSource#getContentType()
         */
        @Override
        public String getContentType() {
            return this.contentType;
        }

        /**
         * StringSource.getInputStream
         * 
         * @see javax.activation.DataSource#getInputStream()
         */
        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(this.data);
        }

        /**
         * StringSource.getName
         * 
         * @see javax.activation.DataSource#getName()
         */
        @Override
        public String getName() {
            return "PiMS report";
        }

        /**
         * StringSource.getOutputStream
         * 
         * @see javax.activation.DataSource#getOutputStream()
         */
        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new java.lang.UnsupportedOperationException("No output stream implemented");
        }

        /**
         * ByteArrayDataSource.getRelativeUrl
         * 
         * @return
         */
        public String getRelativeUrl() {
            return this.relativeUrl;
        }

    }

    private final OutputStream os;

    private final byte[] html;

    private final String url;

    private MimeMessage message;

    private MimeMultipart mm;

    /**
     * Constructor for Mhtml
     * 
     * @param os
     * @param string
     * @param string2
     */
    public Mhtml(final OutputStream os, final byte[] html, final String url) {
        this.os = os;
        this.html = html;
        this.url = url;
        try {
            final Session session = Session.getDefaultInstance(new Properties());
            this.message = new MimeMessage(session);
            this.mm = new MimeMultipart("related");
            this.addPart(new Part(this.html, "text/html", this.url));
            this.message.setContent(this.mm);
            //TODO message.setSubject(title)
            this.message.addHeader("Content-Location", this.url);
        } catch (final MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructor for Mhtml
     * 
     * @param os2
     * @param string
     * @param url2
     */
    public Mhtml(final OutputStream os, final String string, final String url) {
        this(os, string.getBytes(), url);
    }

    /**
     * Mhtml.getMessage
     * 
     * @return
     */
    public void getMessage() throws IOException {
        try {
            this.message.writeTo(this.os);
        } catch (final MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    void addImage(final URL url, final String contentType) {
        this.addPublicPart(url, contentType, url.toExternalForm());
    }

    /**
     * Mhtml.addPublicPart
     * 
     * @param url a resource, obtainable with out a password
     * @param contentType the mime type
     * @param relativeURL the path used in the document to reference the resource
     */
    public void addPublicPart(final URL url, final String contentType, final String relativeURL) {
        try {
            this.addBodyPart(relativeURL, new URLDataSource(url));
        } catch (final MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Mhtml.addFilePart
     * 
     * @param file
     * @param contentType
     * @param relativeURL
     */
    public void addFilePart(final java.io.File file, final String contentType, final String relativeURL) {
        try {
            this.addBodyPart(relativeURL, new FileDataSource(file));
        } catch (final MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPart(final Part part) {
        try {
            this.addBodyPart(part.getRelativeUrl(), part);
        } catch (final MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Mhtml.getBodyPart
     * 
     * @return
     */
    private void addBodyPart(final String url, final DataSource dataSource) throws MessagingException {
        final MimeBodyPart ret = new MimeBodyPart();
        ret.setDisposition("inline");
        // could ret.setFileName("page"); 
        ret.addHeader("Content-Location", url);
        ret.setDataHandler(new DataHandler(dataSource));
        this.mm.addBodyPart(ret);
    }

}
