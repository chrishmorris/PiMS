/**
 * V3_2-web org.pimslims.servlet.mock MockServletContextEvent.java
 * 
 * @author cm65
 * @date 22 Jul 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.mock;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * MockServletContextEvent
 * 
 */
public class MockServletContextEvent extends ServletContextEvent {

    private final ServletContext context;

    /**
     * Constructor for MockServletContextEvent
     * 
     * @param source
     * @param context
     */
    public MockServletContextEvent(final ServletContext source) {
        super(source);
        this.context = source;
    }

    /**
     * MockServletContextEvent.getServletContext
     * 
     * @see javax.servlet.ServletContextEvent#getServletContext()
     */
    @Override
    public ServletContext getServletContext() {
        return this.context;
    }

    /**
     * MockServletContextEvent.getSource
     * 
     * @see java.util.EventObject#getSource()
     */
    @Override
    public Object getSource() {
        return super.getSource();
    }

}
