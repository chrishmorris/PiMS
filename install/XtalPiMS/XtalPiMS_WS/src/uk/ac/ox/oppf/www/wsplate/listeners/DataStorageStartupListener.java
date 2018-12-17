/*
 * DataStorageStartupListener.java Created on 13 March 2006, 11:34 To change this template, choose Tools |
 * Template Manager and open the template in the editor.
 */
package uk.ac.ox.oppf.www.wsplate.listeners;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.pimslims.business.DataStorage;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageFactory;

import uk.ac.ox.oppf.www.wsplate.userdb.UserInfoProvider;

/**
 * 
 * @author djm76/imb
 * 
 *         For information on how to use this class for use in a tomcat webapplication check out the following
 *         link. Provides details of how to initialise and close the session factory when tomcat starts and
 *         closes - rather then when the first user hits the application. http://www.hibernate.org/114.html
 * 
 *         Basically: 1) use hibernate.cfg.xml as normal with the selected connection pool (dbcp) 2) create a
 *         listener class (HibernateListener) below 3) Add listener to web.xml with following xml fragment.
 * 
 *         <listener> <listener-class>org.mypackage.HibernateListener</listener-class> </listener>
 * 
 * 
 *         public class HibernateListener implements ServletContextListener {
 * 
 *         public void contextInitialized(ServletContextEvent event) { HibernateUtil.getSessionFactory(); //
 *         Just call the static initializer of that class }
 * 
 *         public void contextDestroyed(ServletContextEvent event) {
 *         HibernateUtil.getSessionFactory().close(); // Free all resources } }
 * 
 * 
 */
public class DataStorageStartupListener implements ServletContextListener {

    /**
     * Creates a new instance of DataStorageStartupListener.
     */
    public DataStorageStartupListener() {
    }

    /**
     * initialization.
     * 
     * @param event the event
     */
    public final void contextInitialized(final ServletContextEvent event) {
        try {
            Class.forName("javax.naming.Context");
            final Context initCtx = new InitialContext();
            final Context context = (Context) initCtx.lookup("java:comp/env");
            final DataStorageFactory factory = DataStorageFactory.getDataStorageFactory(context);
            event.getServletContext().setAttribute("dataStorageFactory", factory);
            event.getServletContext().setAttribute("model", factory.getModel());
            UserInfoProvider.setEmf(Persistence.createEntityManagerFactory("UserDB"));
        } catch (final Exception ex) {
            // a rare example when catching Exception is appropriate
            ex.printStackTrace(); // can't communicate with the user from here
            // save the exception so the UI can report it later
            event.getServletContext().setAttribute("exception", ex);
        }
    }

    /**
     * destroy event.
     * 
     * @param event the event
     */
    public final void contextDestroyed(final ServletContextEvent event) {
        try {
            final DataStorage dataStorage =
                (DataStorage) event.getServletContext().getAttribute("dataStorage");
            if (null != dataStorage) {
                dataStorage.disconnectResources();
            }
        } catch (final BusinessException ex) {
            ex.printStackTrace(); // can't communicate with the user from here
        }
    }
}
