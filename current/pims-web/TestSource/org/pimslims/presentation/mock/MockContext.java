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

    private final java.util.Map attributes = new java.util.HashMap();

    @Override
    public Dynamic addFilter(final String arg0, final Class<? extends Filter> arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Dynamic addFilter(final String arg0, final Filter arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Dynamic addFilter(final String arg0, final String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addListener(final Class<? extends EventListener> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addListener(final String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T extends EventListener> void addListener(final T arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public javax.servlet.ServletRegistration.Dynamic addServlet(final String arg0,
        final Class<? extends Servlet> arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public javax.servlet.ServletRegistration.Dynamic addServlet(final String arg0, final Servlet arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public javax.servlet.ServletRegistration.Dynamic addServlet(final String arg0, final String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Filter> T createFilter(final Class<T> arg0) throws ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends EventListener> T createListener(final Class<T> arg0) throws ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Servlet> T createServlet(final Class<T> arg0) throws ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void declareRoles(final String... arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public ClassLoader getClassLoader() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getEffectiveMajorVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getEffectiveMinorVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FilterRegistration getFilterRegistration(final String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ServletRegistration getServletRegistration(final String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean setInitParameter(final String arg0, final String arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setSessionTrackingModes(final Set<SessionTrackingMode> arg0) {
        // TODO Auto-generated method stub

    }

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
