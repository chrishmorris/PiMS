/**
 * 
 */
package org.pimslims.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.naming.Context;
import javax.naming.NamingException;

/**
 * A common interface for finding the installation settings for the implementation.
 * 
 * @author cm65
 * 
 */
public class InstallationProperties {

    private final java.util.Properties properties;

    private final Context envCtx;

    public InstallationProperties(java.io.InputStream input) {

        /*
         * try { Context initCtx = new InitialContext();//get tomcat Context envCtx = (Context)
         * initCtx.lookup("java:comp/env"); if(envCtx!=null) System.out.println("Found tomcat context for
         * InstallationProperties!"); else System.out.println("envCtx is null!"); } catch (NamingException e1) {
         * //it is ok, tomcat Context is not always available }
         */

        this.properties = new java.util.Properties();
        this.envCtx = null;
        try {
            this.properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Properties file could not be found !", e);
        }
    }

    public InstallationProperties() {
        this(getInputStream());
    }

    /**
     * @param propertiesFile
     * @throws FileNotFoundException
     */
    public InstallationProperties(File propertiesFile) throws FileNotFoundException {
        this(new java.io.FileInputStream(propertiesFile));
    }

    /**
     * @param propertiesContext
     * @throws FileNotFoundException
     */
    public InstallationProperties(Context propertiesContext) {
        this.envCtx = propertiesContext;
        this.properties = null;
        // a try load
        getRequiredProperty("db.url");

    }

    /**
     * @return input stream for default location for properties
     */
    private static java.io.InputStream getInputStream() {
        File props = new java.io.File("conf/Properties");
        java.io.InputStream in;
        try {
            in = new FileInputStream(props);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Properties file not found at: " + props.getAbsolutePath(), e);
        }
        return in;
    }

    public String getRequiredProperty(String name) {
        // load from context first
        String property = getProperty(name);
        // it is an error if both do not have this name
        if (property == null)
            throw new RuntimeException("Properties not found: " + name);
        return property;
    }

    public boolean isMock() {
        String property = getProperty("mock");
        if ("yes".equals(property)) {
            return true;
        }
        if ("true".equals(property)) {
            return true;
        }
        return false;
    }

    public String getProperty(String name) {
        String property = null;
        if (this.envCtx != null)
            try {
                property = (String) this.envCtx.lookup(name);
            } catch (javax.naming.NameNotFoundException e) {
                property = null;
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
        // load from file
        if (property == null && this.properties != null) {
            property = this.properties.getProperty(name);
        }
        return property;
    }

    public java.util.Properties getProperties() {
        return this.properties;
    }
}
