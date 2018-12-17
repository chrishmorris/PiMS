/**
 * V3_2-web org.pimslims.servlet AutomatedBlastListenerTest.java
 * 
 * @author cm65
 * @date 22 Jul 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import junit.framework.TestCase;

import org.pimslims.presentation.mock.MockServletContext;
import org.pimslims.presentation.mock.MockServletContextEvent;

/**
 * AutomatedBlastListenerTest
 * 
 */
public class AutomatedBlastListenerTest extends TestCase {

    /**
     * Test method for
     * {@link org.pimslims.servlet.AutomatedBlastListener#contextInitialized(javax.servlet.ServletContextEvent)}
     * .
     */
    public void testContextInitialized() {
        final ServletContext context = new MockServletContext();
        final ServletContextEvent event = new MockServletContextEvent(context);
        new AutomatedBlastListener().contextInitialized(event);
    }

}
