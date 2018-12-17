/*
 * WebService.java
 *
 * Created on 01 May 2007, 14:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.core.model;

import java.net.URL;

/**
 * <p>Describes a Web Service</p>
 * @author IMB
 */
public class WebService {
    public static String PROP_ID  = "id";
    public static String PROP_END_POINT  = "endPoint";
    public static String PROP_OPERATION  = "operation";
    public static String PROP_SERVICE_TYPE  = "serviceType";
    
    private long id = -1l;
    private URL endPoint = null;
    private String operation = "";
    private WebServiceType serviceType = null;
    
    /**
     * Creates a new instance of a Web Service
     */
    public WebService () {
        
    }
    
    /**
     * 
     * @return 
     */
    public long getId() {
        return id;
    }
    
    /**
     * 
     * @param id 
     */
    public void setId(long id) {
        this.id = id;
    }
    
    /**
     * 
     * @return 
     */
    public URL getEndPoint() {
        return endPoint;
    }
    
    /**
     * 
     * @param endPoint 
     */
    public void setEndPoint(URL endPoint) {
        this.endPoint = endPoint;
    }
    
    /**
     * 
     * @return 
     */
    public String getOperation() {
        return operation;
    }
    
    /**
     * 
     * @param operation 
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }
    
    /**
     * 
     * @return 
     */
    public WebServiceType getServiceType() {
        return serviceType;
    }
    
    /**
     * 
     * @param serviceType 
     */
    public void setServiceType(WebServiceType serviceType) {
        this.serviceType = serviceType;
    }
    
}
