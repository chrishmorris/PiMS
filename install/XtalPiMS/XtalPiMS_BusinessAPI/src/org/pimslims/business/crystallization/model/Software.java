/*
 * Software.java
 *
 * Created on 03 May 2007, 18:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

import java.net.URL;

import org.pimslims.business.XtalObject;

/**
 * <p>
 * Definition of software used for automatically scoring crystallization images</p>
 * 
 * @author IMB
 */
public class Software extends XtalObject {

    private String name = "";

    private String version = "";

    private String description = "";

    private String vendorName = "";

    private String vendorAddress = "";

    private URL vendorURL = null;

    /**
     * Creates a new instance of Software
     */
    public Software() {

    }

    /**
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     */
    public String getVersion() {
        return version;
    }

    /**
     * 
     * @param version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     */
    public String getVendorName() {
        return vendorName;
    }

    /**
     * 
     * @param vendorName
     */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    /**
     * 
     * @return
     */
    public String getVendorAddress() {
        return vendorAddress;
    }

    /**
     * 
     * @param vendorAddress
     */
    public void setVendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    /**
     * 
     * @return
     */
    public URL getVendorURL() {
        return vendorURL;
    }

    /**
     * 
     * @param vendorURL
     */
    public void setVendorURL(URL vendorURL) {
        this.vendorURL = vendorURL;
    }
}
