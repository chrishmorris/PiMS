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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
//import org.apache.rampart.RampartMessageData;
import org.pimslims.crystallization.datastorage.DataStorageFactory;

/**
 * AxisUtil
 *
 */
public class AxisUtil {

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
        return (DataStorageFactory) getServletContext().getAttribute("dataStorageFactory");
    }

}
