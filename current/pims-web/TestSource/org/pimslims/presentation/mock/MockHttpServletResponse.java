/**
 * V2_0-pims-web org.pimslims.servlet.mock MockHttpServletResponse.java
 * 
 * @author cm65
 * @date 10 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
 */
package org.pimslims.presentation.mock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * MockHttpServletResponse
 * 
 */
public class MockHttpServletResponse implements HttpServletResponse {

    private final boolean log;

    private final ByteArrayOutputStream outputStream;

    private int status = 0;

    private final Map<String, String> headers = new HashMap();

    private String contentType;

    private final PrintWriter writer;

    /**
     * @param log
     */
    public MockHttpServletResponse(final boolean log) {
        super();
        this.outputStream = new ByteArrayOutputStream();
        this.writer = new PrintWriter(this.outputStream);
        this.log = log;
    }

    /**
     * 
     */
    public MockHttpServletResponse() {
        super();
        this.outputStream = new ByteArrayOutputStream();
        this.writer = new PrintWriter(this.outputStream);
        this.log = false;
    }

    public String getOutput() {
        try {
            this.writer.flush();
            return this.outputStream.toString("UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie)
     */
    public void addCookie(final Cookie arg0) {
        // COULD implement

    }

    @Override
    public Collection<String> getHeaderNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<String> getHeaders(final String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#addDateHeader(java.lang.String, long)
     */
    public void addDateHeader(final String arg0, final long arg1) {
        // COULD implement

    }

    /**
     * @see javax.servlet.http.HttpServletResponse#addHeader(java.lang.String, java.lang.String)
     */
    public void addHeader(final String arg0, final String arg1) {
        // COULD implement

    }

    /**
     * @see javax.servlet.http.HttpServletResponse#addIntHeader(java.lang.String, int)
     */
    public void addIntHeader(final String arg0, final int arg1) {
        // COULD implement

    }

    /**
     * @see javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)
     */
    public boolean containsHeader(final String arg0) {
        // COULD implement
        return false;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(java.lang.String)
     */
    public String encodeRedirectURL(final String arg0) {
        return arg0;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#encodeRedirectUrl(java.lang.String)
     * @deprecated
     */
    @Deprecated
    public String encodeRedirectUrl(final String arg0) {
        // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)
     */
    public String encodeURL(final String arg0) {
        // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#encodeUrl(java.lang.String)
     * @deprecated
     */
    @Deprecated
    public String encodeUrl(final String arg0) {
        // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#sendError(int)
     */
    public void sendError(final int arg0) throws IOException {
        if (this.log) {
            System.out.println("Error: " + arg0);
        }
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#sendError(int, java.lang.String)
     */
    public void sendError(final int arg0, final String arg1) throws IOException {
        if (this.log) {
            System.out.println("Error: " + arg0 + " " + arg1);
        }

    }

    /**
     * @see javax.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)
     */
    public void sendRedirect(final String arg0) throws IOException {
        if (this.log) {
            System.out.println("Redirect: " + arg0);
        }

    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setDateHeader(java.lang.String, long)
     */
    public void setDateHeader(final String arg0, final long arg1) {
        // COULD implement

    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setHeader(java.lang.String, java.lang.String)
     */
    public void setHeader(final String arg0, final String arg1) {
        this.headers.put(arg0, arg1);
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setIntHeader(java.lang.String, int)
     */
    public void setIntHeader(final String arg0, final int arg1) {
        // COULD implement

    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setStatus(int)
     */
    public void setStatus(final int arg0) {
        this.status = arg0;
    }

    public int getStatus() {
        return this.status;
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setStatus(int, java.lang.String)
     */
    public void setStatus(final int arg0, final String arg1) {
        // COULD implement

    }

    /**
     * @see javax.servlet.ServletResponse#flushBuffer()
     */
    public void flushBuffer() throws IOException {
        // COULD implement

    }

    /**
     * @see javax.servlet.ServletResponse#getBufferSize()
     */
    public int getBufferSize() {
        // COULD implement
        return 0;
    }

    /**
     * @see javax.servlet.ServletResponse#getCharacterEncoding()
     */
    public String getCharacterEncoding() {
        // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.ServletResponse#getContentType()
     */
    public String getContentType() {
        return this.contentType;
    }

    /**
     * @see javax.servlet.ServletResponse#getLocale()
     */
    public Locale getLocale() {
        // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.ServletResponse#getOutputStream()
     */
    public ServletOutputStream getOutputStream() throws IOException {
        return new MockServletOutputStream(this.outputStream);
    }

    /**
     * @see javax.servlet.ServletResponse#getWriter()
     */
    public PrintWriter getWriter() throws IOException {
        return this.writer;
    }

    /**
     * @see javax.servlet.ServletResponse#isCommitted()
     */
    public boolean isCommitted() {
        // COULD implement
        return false;
    }

    /**
     * @see javax.servlet.ServletResponse#reset()
     */
    public void reset() {
        // COULD implement

    }

    /**
     * @see javax.servlet.ServletResponse#resetBuffer()
     */
    public void resetBuffer() {
        // COULD implement

    }

    /**
     * @see javax.servlet.ServletResponse#setBufferSize(int)
     */
    public void setBufferSize(final int arg0) {
        // COULD implement

    }

    /**
     * @see javax.servlet.ServletResponse#setCharacterEncoding(java.lang.String)
     */
    public void setCharacterEncoding(final String arg0) {
        // COULD implement

    }

    /**
     * @see javax.servlet.ServletResponse#setContentLength(int)
     */
    public void setContentLength(final int arg0) {
        // COULD implement

    }

    /**
     * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
     */
    public void setContentType(final String arg0) {
        this.contentType = arg0;
    }

    /**
     * @see javax.servlet.ServletResponse#setLocale(java.util.Locale)
     */
    public void setLocale(final Locale arg0) {
        // COULD implement

    }

    /**
     * @param string
     * @return
     */
    public String getHeader(final String string) {
        return this.headers.get(string);
    }

}
