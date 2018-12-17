/**
 * pims-web org.pimslims.lab.file IFileTypeListener.java
 * 
 * @author Marc Savitsky
 * @date 28 Jun 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.pimslims.lab.file.CaliperPCRFile;
import org.pimslims.lab.file.CaliperVerificationFile;

/**
 * IFileTypeListener
 * 
 */
public class IFileTypeListener implements ServletContextListener {

    /**
     * IFileTypeListener.contextDestroyed
     * 
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(final ServletContextEvent arg0) {
        // no action needed

    }

    /**
     * IFileTypeListener.contextInitialized
     * 
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(final ServletContextEvent event) {
        event.getServletContext().log("Initialising: " + this.getClass().getName());
        try {
            //Class.forName(AktaFile.class.getName());
            Class.forName(CaliperPCRFile.class.getName());
            Class.forName(CaliperVerificationFile.class.getName());
            //CHECKSTYLE:OFF Unusually, catching all exceptions is correct here
            // Tomcat does not report errors in listeners
        } catch (final Exception e) {
            event.getServletContext().setAttribute(Installation.START_UP_EXCEPTION, e);
        }
        event.getServletContext().log("Initialised: " + this.getClass().getName());
    }

}
