/**
 * current-pims-web org.pimslims.servlet.tag MockHttpServletRequest.java
 * 
 * @author cm65
 * @date 16 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
 */
package org.pimslims.presentation.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import junit.framework.Assert;

import org.pimslims.access.Access;

/**
 * MockHttpServletRequest
 * 
 */
public class MockHttpServletRequest implements HttpServletRequest {

    /**
     * REFERER String
     */
    public static final String REFERER = "Referer";

    private final String method;

    private final HttpSession session;

    private final String username;

    private final Map<String, String[]> parameters;

    private final Map<String, Object> attributes = new HashMap<String, Object>();

    private String dispatchedPath;

    private String pathInfo; // e.g. "pims"

    private String path = ""; // e.g. "/View/org.pimslims.model.experiment.Experiment:434556"

    private String queryString = "";

    private String contentType = "text/html";

    private String requestURL = "";

    private final Map<String, String> headers = new HashMap();

    public static final String PIMS_URI = "http://pims.structuralbiology.eu:8080/pims/";

    /**
     * @param method
     */
    public MockHttpServletRequest(final String method, final MockHttpSession session,
        final Map<String, String[]> parameters) {
        super();
        this.method = method;
        this.session = session;
        this.username = session.getUsername();
        this.parameters = parameters;
        this.queryString = "";
        if (!"post".equals(method.toLowerCase())) {
            for (final Iterator iterator = parameters.entrySet().iterator(); iterator.hasNext();) {
                final Map.Entry<String, String[]> entry = (Map.Entry) iterator.next();
                this.queryString += entry.getKey() + "=";
                for (int i = 0; i < entry.getValue().length; i++) {
                    this.queryString += entry.getValue()[i] + ",";
                }
                if (0 < entry.getValue().length) {
                    // remove the last comma
                    this.queryString = this.queryString.substring(0, this.queryString.length() - 1);
                }
            }
        }
    }

    @Override
    public AsyncContext getAsyncContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ServletContext getServletContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AsyncContext startAsync(final ServletRequest arg0, final ServletResponse arg1)
        throws IllegalStateException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean authenticate(final HttpServletResponse arg0) throws IOException, ServletException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Part getPart(final String arg0) throws IOException, ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void login(final String arg0, final String arg1) throws ServletException {
        // TODO Auto-generated method stub

    }

    @Override
    public void logout() throws ServletException {
        // TODO Auto-generated method stub

    }

    /**
     * @param string
     * @param parameters2
     */
    public MockHttpServletRequest(final String method, final Map<String, String[]> parameters) {
        this(method, new MockHttpSession(Access.ADMINISTRATOR), parameters);
    }

    public Object getAttribute(final String arg0) {
        return this.attributes.get(arg0);
    }

    public Enumeration getAttributeNames() {
        Assert.fail("not implemented yet");
        return null;
    }

    public String getCharacterEncoding() {
        Assert.fail("not implemented yet");
        return null;
    }

    public int getContentLength() {
        Assert.fail("not implemented yet");
        return 0;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public ServletInputStream getInputStream() throws IOException {
        Assert.fail("not implemented yet");
        return null;
    }

    public String getLocalAddr() {
        Assert.fail("not implemented yet");
        return null;
    }

    public Locale getLocale() {
        Assert.fail("not implemented yet");
        return null;
    }

    public Enumeration getLocales() {
        Assert.fail("not implemented yet");
        return null;
    }

    public String getLocalName() {
        Assert.fail("not implemented yet");
        return null;
    }

    public int getLocalPort() {
        Assert.fail("not implemented yet");
        return 0;
    }

    public String getParameter(final String arg0) {
        final String[] strings = this.parameters.get(arg0);
        if (null == strings || 0 == strings.length) {
            return null;
        }
        return strings[0];
    }

    public Map getParameterMap() {
        return this.parameters;
    }

    public Enumeration getParameterNames() {
        Assert.fail("not implemented yet");
        return null;
    }

    public String[] getParameterValues(final String arg0) {
        return this.parameters.get(arg0);
    }

    public String getProtocol() {
        Assert.fail("not implemented yet");
        return null;
    }

    public BufferedReader getReader() throws IOException {
        Assert.fail("not implemented yet");
        return null;
    }

    @Deprecated
    public String getRealPath(final String arg0) {
        return "WebContent/" + arg0;
    }

    public String getRemoteAddr() {
        Assert.fail("not implemented yet");
        return null;
    }

    public String getRemoteHost() {
        Assert.fail("not implemented yet");
        return null;
    }

    public int getRemotePort() {
        Assert.fail("not implemented yet");
        return 0;
    }

    public RequestDispatcher getRequestDispatcher(final String path) {
        this.dispatchedPath = path;
        return new MockRequestDispatcher(path);
    }

    public String getDispatchedPath() {
        return this.dispatchedPath;
    }

    public String getScheme() {
        Assert.fail("not implemented yet");
        return null;
    }

    public String getServerName() {
        Assert.fail("not implemented yet");
        return null;
    }

    public int getServerPort() {
        Assert.fail("not implemented yet");
        return 0;
    }

    public boolean isSecure() {
        Assert.fail("not implemented yet");
        return false;
    }

    public void removeAttribute(final String arg0) {
        Assert.fail("not implemented yet");

    }

    public void setAttribute(final String arg0, final Object arg1) {
        this.attributes.put(arg0, arg1);

    }

    public void setCharacterEncoding(final String arg0) throws UnsupportedEncodingException {
        assert "UTF-8".equals(arg0);
    }

    public String getAuthType() {
        Assert.fail("not implemented yet");
        return null;
    }

    public String getContextPath() {
        return "/pims"; // usual path, test cases must check for this
    }

    public Cookie[] getCookies() {
        Assert.fail("not implemented yet");
        return null;
    }

    public long getDateHeader(final String arg0) {
        Assert.fail("not implemented yet");
        return 0;
    }

    public String getHeader(final String arg0) {
        return this.headers.get(arg0);
    }

    public Enumeration getHeaderNames() {
        Assert.fail("not implemented yet");
        return null;
    }

    public Enumeration getHeaders(final String arg0) {
        Assert.fail("not implemented yet");
        return null;
    }

    public int getIntHeader(final String arg0) {
        Assert.fail("not implemented yet");
        return 0;
    }

    public String getMethod() {
        return this.method;
    }

    public String getPathInfo() {
        return this.pathInfo;
    }

    public void setPathInfo(final String string) {
        this.pathInfo = string;
    }

    public String getPathTranslated() {
        Assert.fail("not implemented yet");
        return null;
    }

    public String getQueryString() {
        return this.queryString;
    }

    public void setQueryString(final String queryString) {
        this.queryString = queryString;
    }

    public String getRemoteUser() {
        return this.username;
    }

    public String getRequestedSessionId() {
        return this.session.getId();
    }

    public String getRequestURI() {
        return this.requestURL;
    }

    public StringBuffer getRequestURL() {
        return new StringBuffer(this.requestURL);
    }

    public String getServletPath() {
        return this.path;
    }

    public HttpSession getSession() {
        return this.session;
    }

    public HttpSession getSession(final boolean create) {
        if (create && (this.session == null)) {
            throw new AssertionError("not implemented yet");
        }
        return this.session;
    }

    public Principal getUserPrincipal() {
        Assert.fail("not implemented yet");
        return null;
    }

    public boolean isRequestedSessionIdFromCookie() {
        Assert.fail("not implemented yet");
        return false;
    }

    @Deprecated
    public boolean isRequestedSessionIdFromUrl() {
        Assert.fail("not implemented yet");
        return false;
    }

    public boolean isRequestedSessionIdFromURL() {
        Assert.fail("not implemented yet");
        return false;
    }

    public boolean isRequestedSessionIdValid() {
        Assert.fail("not implemented yet");
        return false;
    }

    public boolean isUserInRole(final String arg0) {
        if ("pims-user".equals(arg0)) {
            return true;
        }
        if ("pims-administrator".equals(arg0) && "administrator".equals(this.username)) {
            return true;
        }
        return false;
    }

    /**
     * @param path The path to set.
     */
    public void setPath(final String path) {
        // not implemented
    }

    /**
     * MockHttpServletRequest.setRequestURL
     * 
     * @param string
     */
    public void setRequestURL(final String string) {
        this.requestURL = string;
    }

    /**
     * MockHttpServletRequest.setServletPath
     * 
     * @param string
     */
    public void setServletPath(final String string) {
        this.path = string;
    }

    /**
     * MockHttpServletRequest.setReferer
     * 
     * @param string
     */
    public void setReferer(final String string) {
        this.setHeader(MockHttpServletRequest.REFERER, string);
    }

    /**
     * MockHttpServletRequest.setHeader
     * 
     * @param string
     * @param string2
     */
    public void setHeader(final String string, final String string2) {
        this.headers.put(string, string2);
    }

}
