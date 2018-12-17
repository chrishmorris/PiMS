/** 
 * xtalpims-ws-stub uk.ac.ox.oppf.www.wsplate.util AxisUtil.java
 * @author jon
 * @date 23 Aug 2010
 *
 * Protein Information Management System
 * @version: 4.1
 *
 * Copyright (c) 2010 jon 
 * The copyright holder has licenced the STFC to redistribute this software
 */
package uk.ac.ox.oppf.www.wsplate.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.transport.http.HTTPConstants;
//import org.apache.rampart.RampartMessageData;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.persistence.HibernateUtil;
// FIXME VMXi Bodge - no UserDB
//import org.pimslims.user.userdb.util.UserInfoProvider;
import org.slf4j.LoggerFactory;

/**
 * AxisUtil
 *
 */
public class AxisUtil {

    private static final String DATA_STORAGE_FACTORY = "org.pimslims.dataStorageFactory";

    private static final Object _lock = new Object();
    
    /**
     * AxisUtil.getServletContext - get the underlying ServletContext
     * @return the underlying ServletContext
     */
    public static ServletContext getServletContext() {
        MessageContext mc = MessageContext.getCurrentMessageContext();
        return (ServletContext) mc.getProperty(HTTPConstants.MC_HTTP_SERVLETCONTEXT);
    }

    /**
     * AxisUtil.getHttpServletRequest - get the underlying HttpServletRequest
     * @return the underlying HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        MessageContext mc = MessageContext.getCurrentMessageContext();
        return (HttpServletRequest) mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
    }

    /**
     * AxisUtil.getHttpServletResponse - get the underlying HttpServletResponse
     * @return the underlying HttpServletResponse
     */
    public static HttpServletResponse getHttpServletResponse() {
        MessageContext mc = MessageContext.getCurrentMessageContext();
        return (HttpServletResponse) mc.getProperty(HTTPConstants.MC_HTTP_SERVLETRESPONSE);
    }

    /**
     * AxisUtil.getRampartUsername - get the username from Rampart
     * @return - the username from Rampart
     *
    public static String getRampartUsername() {
        MessageContext mc = MessageContext.getCurrentMessageContext();
        return (String) mc.getProperty(RampartMessageData.USERNAME);
    }
     */

    /**
     * AxisUtil.getBasicUsername - get the username from the request
     * @return - the username from Request
     */
    public static String getBasicUsername() {
        return getHttpServletRequest().getRemoteUser();
    }

    /**
     * AxisUtil.getDataStorageFactory - get the DataStorageFactory that has previously
     * been placed into the ServletContext by DataStorageStartupListener
     * @return - the DataStorageFactory
     */
    public static DataStorageFactory getDataStorageFactory() {
        //return (DataStorageFactory) getServletContext().getAttribute("dataStorageFactory");
        MessageContext mc = MessageContext.getCurrentMessageContext();
        AxisService as = mc.getAxisService();
        return (DataStorageFactory) as.getParameter(DATA_STORAGE_FACTORY).getValue();
    }
    
    /**
     * AxisUtil.shutDown - clean up database factories
     * 
     * @param cxt
     * @param service
     */
    public static void shutDown(ConfigurationContext cxt, AxisService service) {
        synchronized (_lock) {
            try {
                org.apache.axis2.description.Parameter param;
                param = service.getParameter(DATA_STORAGE_FACTORY);
                if (null != param) {
                    service.removeParameter(param);
                    // Note that DataStorageFactory needs no cleanup
                    // FIXME This is not true for the pims implementation
                    // - see the call to HibernateUtil.shutdown() below
                    // Apparently this is all handled in HibernateUtil#finalize
                    //HibernateUtil.shutdown();
                    param = null;
                }
                
                // FIXME VMXi bodge fix - no UserDB
                //if ((null != UserInfoProvider.getEmf()) && (UserInfoProvider.getEmf().isOpen())) {
                //    UserInfoProvider.getEmf().close();
                //}
                //UserInfoProvider.setEmf(null);
                
                // Log4J only!
                org.apache.log4j.MDC.clear();
                org.apache.log4j.LogManager.shutdown();
            } catch (final Exception ex) {
                ex.printStackTrace(); // can't communicate with the user from here
            }
        }
    }
    
    /**
     * AxisUtil.startUp - initialize database connection factories and store in Service
     * where necessary.
     * 
     * @param cxt
     * @param service
     */
    public static void startUp(ConfigurationContext cxt, AxisService service) {
        synchronized (_lock) {
            try {
                Class.forName("javax.naming.Context");
                final Context initCtx = new InitialContext();
                final Context context = (Context) initCtx.lookup("java:comp/env");
                final DataStorageFactory factory = DataStorageFactory.getDataStorageFactory(context);
                service.addParameter(DATA_STORAGE_FACTORY, factory);
                // NOTE: Don't need to store model as no use of pims-web.jar - would have to be in
                // ServletContext anyway, which I can't figure out how to get to from here
                
                // FIXME VMXi Bodge - no UserDB
                // // Initialize UserDB - we need this to get email addresses for plate owners
                // // See PlateInfoProviderImpl#getEmailForOwner(String)
                //if (null == UserInfoProvider.getEmf()) {
                //    UserInfoProvider.setEmf(Persistence.createEntityManagerFactory("UserDB"));
                //}
            } catch (final Exception ex) {
                ex.printStackTrace(); // can't communicate with the user from here
            }
        }
   }

}
