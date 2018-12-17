/*
 * WebServiceType.java
 *
 * Created on 01 May 2007, 15:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.core.model;

/**
 * <p>Describes a set of constants that describe the Web Services that can be made available.  
 * Probably obtained from the database to allow for extensibility</p>
 * @author IMB
 */
public class WebServiceType {
    public static String PROP_ID  = "id";
    public static String PROP_SERVICE_TYPE  = "serviceType";
    
    private long id = -1l;
    private String serviceType = "";

    /**
     * Creates a new instance of a WebServiceType
     */
    public WebServiceType() {
        
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
    public String getServiceType() {
        return serviceType;
    }

    /**
     * 
     * @param serviceType 
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
