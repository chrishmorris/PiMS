/**
 * V2_0-pims-web org.pimslims.servlet.mock MockServletConfig.java
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

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.pimslims.dao.AbstractModel;

/**
 * MockServletConfig
 * 
 */
public class MockServletConfig implements ServletConfig {

    private final ServletContext context;

    /**
     * @param model
     * @param context
     */
    public MockServletConfig(final AbstractModel model) {
        super();
        this.context = new MockServletContext();
        this.context.setAttribute("model", model);
    }

    /**
     * @see javax.servlet.ServletConfig#getInitParameter(java.lang.String)
     */
    public String getInitParameter(final String arg0) {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.ServletConfig#getInitParameterNames()
     */
    public Enumeration getInitParameterNames() {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.ServletConfig#getServletContext()
     */
    public ServletContext getServletContext() {
        return this.context;
    }

    /**
     * @see javax.servlet.ServletConfig#getServletName()
     */
    public String getServletName() {
        // // COULD implement
        return null;
    }

    /**
     * MockServletConfig.setAttribute
     * 
     * @param license
     */
    public void setAttribute(final String name, final Object value) {
        this.context.setAttribute(name, value);
    }

}
