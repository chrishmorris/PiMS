/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.business;

import java.net.URL;

/**
 *
 * @author ian
 */
public class Link {
    private URL url = null;
    private String name = "";

    public Link() {
    } 

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
