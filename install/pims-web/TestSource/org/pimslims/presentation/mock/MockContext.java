/**
 * V2_0-pims-web org.pimslims.servlet MockContext.java
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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
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

@Deprecated
// use MockServletContext
public class MockContext implements ServletContext {

    /**
     * MockContext.addFilter
     * 
     * @see javax.servlet.ServletContext#addFilter(java.lang.String, java.lang.Class)
     */
    @Override
    public Dynamic addFilter(final String arg0, final Class<? extends Filter> arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockContext.addFilter
     * 
     * @see javax.servlet.ServletContext#addFilter(java.lang.String, javax.servlet.Filter)
     */
    @Override
    public Dynamic addFilter(final String arg0, final Filter arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockContext.addFilter
     * 
     * @see javax.servlet.ServletContext#addFilter(java.lang.String, java.lang.String)
     */
    @Override
    public Dynamic addFilter(final String arg0, final String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockContext.addListener
     * 
     * @see javax.servlet.ServletContext#addListener(java.lang.Class)
     */
    @Override
    public void addListener(final Class<? extends EventListener> arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * MockContext.addListener
     * 
     * @see javax.servlet.ServletContext#addListener(java.lang.String)
     */
    @Override
    public void addListener(final String arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * MockContext.addListener
     * 
     * @see javax.servlet.ServletContext#addListener(java.util.EventListener)
     */
    @Override
    public <T extends EventListener> void addListener(final T arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * MockContext.addServlet
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
     * MockContext.addServlet
     * 
     * @see javax.servlet.ServletContext#addServlet(java.lang.String, javax.servlet.Servlet)
     */
    @Override
    public javax.servlet.ServletRegistration.Dynamic addServlet(final String arg0, final Servlet arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockContext.addServlet
     * 
     * @see javax.servlet.ServletContext#addServlet(java.lang.String, java.lang.String)
     */
    @Override
    public javax.servlet.ServletRegistration.Dynamic addServlet(final String arg0, final String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockContext.createFilter
     * 
     * @see javax.servlet.ServletContext#createFilter(java.lang.Class)
     */
    @Override
    public <T extends Filter> T createFilter(final Class<T> arg0) throws ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockContext.createListener
     * 
     * @see javax.servlet.ServletContext#createListener(java.lang.Class)
     */
    @Override
    public <T extends EventListener> T createListener(final Class<T> arg0) throws ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockContext.createServlet
     * 
     * @see javax.servlet.ServletContext#createServlet(java.lang.Class)
     */
    @Override
    public <T extends Servlet> T createServlet(final Class<T> arg0) throws ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockContext.declareRoles
     * 
     * @see javax.servlet.ServletContext#declareRoles(java.lang.String[])
     */
    @Override
    public void declareRoles(final String... arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * MockContext.getClassLoader
     * 
     * @see javax.servlet.ServletContext#getClassLoader()
     */
    @Override
    public ClassLoader getClassLoader() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockContext.getDefaultSessionTrackingModes
     * 
     * @see javax.servlet.ServletContext#getDefaultSessionTrackingModes()
     */
    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockContext.getEffectiveMajorVersion
     * 
     * @see javax.servlet.ServletContext#getEffectiveMajorVersion()
     */
    @Override
    public int getEffectiveMajorVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * MockContext.getEffectiveMinorVersion
     * 
     * @see javax.servlet.ServletContext#getEffectiveMinorVersion()
     */
    @Override
    public int getEffectiveMinorVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * MockContext.getEffectiveSessionTrackingModes
     * 
     * @see javax.servlet.ServletContext#getEffectiveSessionTrackingModes()
     */
    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockContext.getFilterRegistration
     * 
     * @see javax.servlet.ServletContext#getFilterRegistration(java.lang.String)
     */
    @Override
    public FilterRegistration getFilterRegistration(final String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockContext.getFilterRegistrations
     * 
     * @see javax.servlet.ServletContext#getFilterRegistrations()
     */
    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockContext.getJspConfigDescriptor
     * 
     * @see javax.servlet.ServletContext#getJspConfigDescriptor()
     */
    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockContext.getServletRegistration
     * 
     * @see javax.servlet.ServletContext#getServletRegistration(java.lang.String)
     */
    @Override
    public ServletRegistration getServletRegistration(final String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockContext.getServletRegistrations
     * 
     * @see javax.servlet.ServletContext#getServletRegistrations()
     */
    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockContext.getSessionCookieConfig
     * 
     * @see javax.servlet.ServletContext#getSessionCookieConfig()
     */
    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * MockContext.setInitParameter
     * 
     * @see javax.servlet.ServletContext#setInitParameter(java.lang.String, java.lang.String)
     */
    @Override
    public boolean setInitParameter(final String arg0, final String arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * MockContext.setSessionTrackingModes
     * 
     * @see javax.servlet.ServletContext#setSessionTrackingModes(java.util.Set)
     */
    @Override
    public void setSessionTrackingModes(final Set<SessionTrackingMode> arg0) throws IllegalStateException,
        IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    private final java.util.Map attributes = new java.util.HashMap();

    /**
     * {@inheritDoc}
     */
    public void log(final String arg0, final Throwable arg1) {
        System.out.println(arg0);
    }

    /**
     * {@inheritDoc}
     */
    public void log(final String arg0) {
        System.out.println(arg0);
    }

    /**
     * {@inheritDoc}
     */
    public Object getAttribute(final String arg0) {
        return this.attributes.get(arg0);
    }

    /**
     * {@inheritDoc}
     */
    public Enumeration getAttributeNames() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public ServletContext getContext(final String arg0) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getInitParameter(final String arg0) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Enumeration getInitParameterNames() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public int getMajorVersion() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public String getMimeType(final String arg0) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public int getMinorVersion() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public RequestDispatcher getNamedDispatcher(final String arg0) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getRealPath(final String arg0) {
        return "WebContent" + arg0;
    }

    /**
     * {@inheritDoc}
     */
    public RequestDispatcher getRequestDispatcher(final String arg0) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public URL getResource(final String arg0) throws MalformedURLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public InputStream getResourceAsStream(final String arg0) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Set getResourcePaths(final String arg0) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getServerInfo() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void removeAttribute(final String arg0) {
        this.attributes.remove(arg0);
    }

    /**
     * {@inheritDoc}
     */
    public void setAttribute(final String arg0, final Object arg1) {
        this.attributes.put(arg0, arg1);
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    public Servlet getServlet(final String arg0) throws ServletException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getServletContextName() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    public Enumeration getServletNames() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    public Enumeration getServlets() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    public void log(final Exception arg0, final String arg1) {
        /* empty */
    }

    /**
     * MockContext.getContextPath
     * 
     * @see javax.servlet.ServletContext#getContextPath()
     */
    public String getContextPath() {
        // // COULD implement
        return null;
    }

}
