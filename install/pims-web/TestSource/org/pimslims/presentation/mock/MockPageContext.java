/**
 * pims-web org.pimslims.tag TestPageContext.java
 * 
 * @author cm65
 * @date 3 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
 */
package org.pimslims.presentation.mock;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.el.ELContext;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.ErrorData;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.tagext.BodyContent;

import junit.framework.Assert;

import org.pimslims.access.Access;

/**
 * TestPageContext Mock page context for use when testing tags
 * 
 */
@SuppressWarnings("deprecation")
// implements some deprecated methods from API
public class MockPageContext extends PageContext {

    private final StringWriter stringWriter = new StringWriter();

    private final ServletRequest request;

    private final Map<String, Object> attributes = new HashMap<String, Object>();

    /**
     * @param request
     */
    public MockPageContext(final ServletRequest request) {
        super();
        this.request = request;
    }

    /**
     * 
     */
    public MockPageContext() {
        super();
        this.request =
            new MockHttpServletRequest("get", new MockHttpSession(Access.ADMINISTRATOR),
                Collections.EMPTY_MAP);
    }

    /**
     * @see javax.servlet.jsp.PageContext#forward(java.lang.String)
     */
    @Override
    public void forward(final String arg0) throws ServletException, IOException {
        Assert.fail("forwarded to: " + arg0);
    }

    /**
     * @see javax.servlet.jsp.PageContext#getException()
     */
    @Override
    public Exception getException() {
        Assert.fail("get exception");
        return null;
    }

    /**
     * @see javax.servlet.jsp.PageContext#getPage()
     */
    @Override
    public Object getPage() {
        Assert.fail("get exception");
        return null;
    }

    /**
     * @see javax.servlet.jsp.PageContext#getResponse()
     */
    @Override
    public ServletResponse getResponse() {
        Assert.fail("get response");
        return null;
    }

    /**
     * @see javax.servlet.jsp.PageContext#getServletConfig()
     */
    @Override
    public ServletConfig getServletConfig() {
        Assert.fail("get servlet config");
        return null;
    }

    /**
     * @see javax.servlet.jsp.PageContext#getServletContext()
     */
    @Override
    public ServletContext getServletContext() {
        Assert.fail("get servlet context");
        return null;
    }

    /**
     * @see javax.servlet.jsp.PageContext#getSession()
     */
    @Override
    public HttpSession getSession() {
        Assert.fail("get session"); // LATER provide mock session
        return null;
    }

    /**
     * @see javax.servlet.jsp.PageContext#handlePageException(java.lang.Exception)
     */
    @Override
    public void handlePageException(final Exception arg0) throws ServletException, IOException {

        Assert.fail("handle exception");
    }

    /**
     * @see javax.servlet.jsp.PageContext#handlePageException(java.lang.Throwable)
     */
    @Override
    public void handlePageException(final Throwable arg0) throws ServletException, IOException {

        Assert.fail("handle exception");
    }

    /**
     * @see javax.servlet.jsp.PageContext#include(java.lang.String)
     */
    @Override
    public void include(final String arg0) throws ServletException, IOException {

        Assert.fail("include: " + arg0);
    }

    /**
     * @see javax.servlet.jsp.PageContext#include(java.lang.String, boolean)
     */
    @Override
    public void include(final String arg0, final boolean arg1) throws ServletException, IOException {

        Assert.fail("include: " + arg0 + ", " + arg1);
    }

    /**
     * @see javax.servlet.jsp.PageContext#initialize(javax.servlet.Servlet, javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, java.lang.String, boolean, int, boolean)
     */
    @Override
    public void initialize(final Servlet arg0, final ServletRequest arg1, final ServletResponse arg2,
        final String arg3, final boolean arg4, final int arg5, final boolean arg6) throws IOException {

        Assert.fail("initialize");
    }

    /**
     * @see javax.servlet.jsp.PageContext#release()
     */
    @Override
    public void release() {

        Assert.fail("release");
    }

    /**
     * @see javax.servlet.jsp.JspContext#findAttribute(java.lang.String)
     */
    @Override
    public Object findAttribute(final String arg0) {
        Assert.fail("find attribute: " + arg0);
        return null;
    }

    /**
     * @see javax.servlet.jsp.JspContext#getAttribute(java.lang.String)
     */
    @Override
    public Object getAttribute(final String arg0) {
        return this.attributes.get(arg0);
    }

    /**
     * @see javax.servlet.jsp.JspContext#getAttribute(java.lang.String, int)
     */
    @Override
    public Object getAttribute(final String arg0, final int arg1) {
        Assert.fail("get attribute: " + arg0 + ", " + arg1);
        return null;
    }

    /**
     * @see javax.servlet.jsp.JspContext#getAttributeNamesInScope(int)
     */
    @Override
    public Enumeration getAttributeNamesInScope(final int arg0) {
        Assert.fail("get attribute names in scope: " + arg0);
        return null;
    }

    /**
     * @see javax.servlet.jsp.JspContext#getAttributesScope(java.lang.String)
     */
    @Override
    public int getAttributesScope(final String arg0) {
        Assert.fail("get attributes: " + arg0);
        return 0;
    }

    /**
     * @see javax.servlet.jsp.JspContext#removeAttribute(java.lang.String)
     */
    @Override
    public void removeAttribute(final String arg0) {
        Assert.fail("remvoe attribute: " + arg0);
    }

    /**
     * @see javax.servlet.jsp.JspContext#removeAttribute(java.lang.String, int)
     */
    @Override
    public void removeAttribute(final String arg0, final int arg1) {
        Assert.fail("removee attribute: " + arg0 + ", " + arg1);
    }

    /**
     * @see javax.servlet.jsp.JspContext#setAttribute(java.lang.String, java.lang.Object)
     */
    @Override
    public void setAttribute(final String arg0, final Object arg1) {
        this.attributes.put(arg0, arg1);

    }

    /**
     * @see javax.servlet.jsp.JspContext#setAttribute(java.lang.String, java.lang.Object, int)
     */
    @Override
    public void setAttribute(final String arg0, final Object arg1, final int arg2) {
        Assert.fail("set attribute: " + arg0 + " = " + arg1 + ", " + arg2);

    }

    /**
     * @see javax.servlet.jsp.JspContext#getOut() return mock writer
     */
    @Override
    public JspWriter getOut() {
        return new MockJspWriter(this.stringWriter);
    }

    /**
     * @see javax.servlet.jsp.PageContext#getRequest() return mock request
     */
    @Override
    public ServletRequest getRequest() {
        return this.request;
    }

    public StringBuffer getBuffer() {
        return this.stringWriter.getBuffer();
    }

    public String getOutput() {
        return new String(this.getBuffer());
    }

    /**
     * MockPageContext.getErrorData
     * 
     * @see javax.servlet.jsp.PageContext#getErrorData()
     */
    @Override
    public ErrorData getErrorData() {
        // // COULD implement
        return super.getErrorData();
    }

    /**
     * MockPageContext.pushBody
     * 
     * @see javax.servlet.jsp.PageContext#pushBody()
     */
    @Override
    public BodyContent pushBody() {
        // // COULD implement
        return super.pushBody();
    }

    /**
     * MockPageContext.popBody
     * 
     * @see javax.servlet.jsp.JspContext#popBody()
     */
    @Override
    public JspWriter popBody() {
        // // COULD implement
        return super.popBody();
    }

    /**
     * MockPageContext.pushBody
     * 
     * @see javax.servlet.jsp.JspContext#pushBody(java.io.Writer)
     */
    @Override
    public JspWriter pushBody(final Writer writer) {
        // // COULD implement
        return super.pushBody(writer);
    }

    @Override
    public ELContext getELContext() {
        // // COULD implement
        return null;
    }

    /**
     * JspContext.getExpressionEvaluator
     * 
     * @see javax.servlet.jsp.JspContext#getExpressionEvaluator()
     */
    @Override
    public ExpressionEvaluator getExpressionEvaluator() {
        // // COULD implement
        return null;
    }

    /**
     * JspContext.getVariableResolver
     * 
     * @see javax.servlet.jsp.JspContext#getVariableResolver()
     */
    @Override
    public VariableResolver getVariableResolver() {
        // // COULD implement
        return null;
    }
}
