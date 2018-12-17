/**
 * V2_0-pims-web org.pimslims.servlet.mock MockServletContext.java
 * 
 * @author cm65
 * @date 11 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
 */
package org.pimslims.presentation.mock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

/**
 * MockServletContext TODO fix duplication with MockContext
 */
public class MockServletContext implements ServletContext {

    /**
     * MockServletContext.addFilter
     * 
     * @see javax.servlet.ServletContext#addFilter(java.lang.String, java.lang.Class)
     */
    @Override
    public Dynamic addFilter(final String arg0, final Class<? extends Filter> arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockServletContext.addFilter
     * 
     * @see javax.servlet.ServletContext#addFilter(java.lang.String, javax.servlet.Filter)
     */
    @Override
    public Dynamic addFilter(final String arg0, final Filter arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockServletContext.addFilter
     * 
     * @see javax.servlet.ServletContext#addFilter(java.lang.String, java.lang.String)
     */
    @Override
    public Dynamic addFilter(final String arg0, final String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockServletContext.addListener
     * 
     * @see javax.servlet.ServletContext#addListener(java.lang.Class)
     */
    @Override
    public void addListener(final Class<? extends EventListener> arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * MockServletContext.addListener
     * 
     * @see javax.servlet.ServletContext#addListener(java.lang.String)
     */
    @Override
    public void addListener(final String arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * MockServletContext.addListener
     * 
     * @see javax.servlet.ServletContext#addListener(java.util.EventListener)
     */
    @Override
    public <T extends EventListener> void addListener(final T arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * MockServletContext.addServlet
     * 
     * @see javax.servlet.ServletContext#addServlet(java.lang.String, java.lang.Class)
     */
    @Override
    public javax.servlet.ServletRegistration.Dynamic addServlet(final String arg0,
        final Class<? extends Servlet> arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockServletContext.addServlet
     * 
     * @see javax.servlet.ServletContext#addServlet(java.lang.String, javax.servlet.Servlet)
     */
    @Override
    public javax.servlet.ServletRegistration.Dynamic addServlet(final String arg0, final Servlet arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockServletContext.addServlet
     * 
     * @see javax.servlet.ServletContext#addServlet(java.lang.String, java.lang.String)
     */
    @Override
    public javax.servlet.ServletRegistration.Dynamic addServlet(final String arg0, final String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockServletContext.createFilter
     * 
     * @see javax.servlet.ServletContext#createFilter(java.lang.Class)
     */
    @Override
    public <T extends Filter> T createFilter(final Class<T> arg0) throws ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockServletContext.createListener
     * 
     * @see javax.servlet.ServletContext#createListener(java.lang.Class)
     */
    @Override
    public <T extends EventListener> T createListener(final Class<T> arg0) throws ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockServletContext.createServlet
     * 
     * @see javax.servlet.ServletContext#createServlet(java.lang.Class)
     */
    @Override
    public <T extends Servlet> T createServlet(final Class<T> arg0) throws ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockServletContext.declareRoles
     * 
     * @see javax.servlet.ServletContext#declareRoles(java.lang.String[])
     */
    @Override
    public void declareRoles(final String... arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * MockServletContext.getClassLoader
     * 
     * @see javax.servlet.ServletContext#getClassLoader()
     */
    @Override
    public ClassLoader getClassLoader() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockServletContext.getDefaultSessionTrackingModes
     * 
     * @see javax.servlet.ServletContext#getDefaultSessionTrackingModes()
     */
    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockServletContext.getEffectiveMajorVersion
     * 
     * @see javax.servlet.ServletContext#getEffectiveMajorVersion()
     */
    @Override
    public int getEffectiveMajorVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * MockServletContext.getEffectiveMinorVersion
     * 
     * @see javax.servlet.ServletContext#getEffectiveMinorVersion()
     */
    @Override
    public int getEffectiveMinorVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * MockServletContext.getEffectiveSessionTrackingModes
     * 
     * @see javax.servlet.ServletContext#getEffectiveSessionTrackingModes()
     */
    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockServletContext.getFilterRegistration
     * 
     * @see javax.servlet.ServletContext#getFilterRegistration(java.lang.String)
     */
    @Override
    public FilterRegistration getFilterRegistration(final String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockServletContext.getFilterRegistrations
     * 
     * @see javax.servlet.ServletContext#getFilterRegistrations()
     */
    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockServletContext.getJspConfigDescriptor
     * 
     * @see javax.servlet.ServletContext#getJspConfigDescriptor()
     */
    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockServletContext.getServletRegistration
     * 
     * @see javax.servlet.ServletContext#getServletRegistration(java.lang.String)
     */
    @Override
    public ServletRegistration getServletRegistration(final String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockServletContext.getServletRegistrations
     * 
     * @see javax.servlet.ServletContext#getServletRegistrations()
     */
    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockServletContext.getSessionCookieConfig
     * 
     * @see javax.servlet.ServletContext#getSessionCookieConfig()
     */
    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockServletContext.setInitParameter
     * 
     * @see javax.servlet.ServletContext#setInitParameter(java.lang.String, java.lang.String)
     */
    @Override
    public boolean setInitParameter(final String arg0, final String arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * MockServletContext.setSessionTrackingModes
     * 
     * @see javax.servlet.ServletContext#setSessionTrackingModes(java.util.Set)
     */
    @Override
    public void setSessionTrackingModes(final Set<SessionTrackingMode> arg0) throws IllegalStateException,
        IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    private final Map<String, Object> attributes = new HashMap<String, Object>();

    /**
     * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
     */
    public Object getAttribute(final String arg0) {
        return this.attributes.get(arg0);
    }

    /**
     * @see javax.servlet.ServletContext#getAttributeNames()
     */
    public Enumeration getAttributeNames() {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.ServletContext#getContext(java.lang.String)
     */
    public ServletContext getContext(final String arg0) {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.ServletContext#getInitParameter(java.lang.String)
     */
    public String getInitParameter(final String arg0) {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.ServletContext#getInitParameterNames()
     */
    public Enumeration getInitParameterNames() {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.ServletContext#getMajorVersion()
     */
    public int getMajorVersion() {
        // // COULD implement
        return 0;
    }

    /**
     * @see javax.servlet.ServletContext#getMimeType(java.lang.String)
     */
    public String getMimeType(final String arg0) {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.ServletContext#getMinorVersion()
     */
    public int getMinorVersion() {
        // // COULD implement
        return 0;
    }

    /**
     * @see javax.servlet.ServletContext#getNamedDispatcher(java.lang.String)
     */
    public RequestDispatcher getNamedDispatcher(final String arg0) {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
     */
    public String getRealPath(final String arg0) {
        return "WebContent/" + arg0;
    }

    /**
     * @see javax.servlet.ServletContext#getRequestDispatcher(java.lang.String)
     */
    public RequestDispatcher getRequestDispatcher(final String arg0) {
        return new MockRequestDispatcher(arg0);
    }

    /**
     * @see javax.servlet.ServletContext#getResource(java.lang.String)
     */
    public URL getResource(final String arg0) throws MalformedURLException {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.ServletContext#getResourceAsStream(java.lang.String)
     */
    public InputStream getResourceAsStream(final String path) {

        final File file = new File("WebContent" + path);
        /* if (!file.isAbsolute()) {
            assert null != PropertyGetter.workingDirectory;
            file = new File(PropertyGetter.workingDirectory, path);
        } */
        try {
            return new FileInputStream(file);
        } catch (final FileNotFoundException e) {
            System.out.println("Not found: " + file.getAbsolutePath());
            return null;
        }
    }

    /**
     * @see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
     */
    public Set getResourcePaths(final String arg0) {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.ServletContext#getServerInfo()
     */
    public String getServerInfo() {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.ServletContext#getServlet(java.lang.String)
     */
    public Servlet getServlet(final String arg0) throws ServletException {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.ServletContext#getServletContextName()
     */
    public String getServletContextName() {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.ServletContext#getServletNames()
     */
    public Enumeration getServletNames() {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.ServletContext#getServlets()
     */
    public Enumeration getServlets() {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.ServletContext#log(java.lang.String)
     */
    public void log(final String arg0) {
        // // COULD implement

    }

    /**
     * @see javax.servlet.ServletContext#log(java.lang.Exception, java.lang.String)
     */
    public void log(final Exception arg0, final String arg1) {
        // // COULD implement

    }

    /**
     * @see javax.servlet.ServletContext#log(java.lang.String, java.lang.Throwable)
     */
    public void log(final String arg0, final Throwable arg1) {
        // // COULD implement

    }

    /**
     * @see javax.servlet.ServletContext#removeAttribute(java.lang.String)
     */
    public void removeAttribute(final String arg0) {
        // // COULD implement

    }

    /**
     * @see javax.servlet.ServletContext#setAttribute(java.lang.String, java.lang.Object)
     */
    public void setAttribute(final String arg0, final Object arg1) {
        this.attributes.put(arg0, arg1);
    }

    /**
     * MockServletContext.getContextPath
     * 
     * @see javax.servlet.ServletContext#getContextPath()
     */
    public String getContextPath() {
        // // COULD implement
        return null;
    }

}
