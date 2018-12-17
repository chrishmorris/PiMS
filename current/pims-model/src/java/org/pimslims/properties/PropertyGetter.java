/**
 * V2_0-pims-web org.pimslims.presentation PropertyGetter.java
 * 
 * @author cm65
 * @date 7 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
 */
package org.pimslims.properties;

import java.io.File;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

/**
 * PropertyGetter
 * 
 */
public class PropertyGetter {

    /**
     * 
     */
    private static final String JAVA_NAMING_FACTORY_INITIAL = "java.naming.factory.initial";

    /**
     * 
     */
    public static final String TOMCAT_NAMING_CONTEXT = "java:comp/env";

    /**
     * Location of WEB-INF/
     * 
     */
    private static String workingDirectory = null;

    /**
     * @param workingDirectory Location of WEB-INF/, absolute or relative to pwd of JVM
     */
    public static void setWorkingDirectory(final String workingDirectory) {
        System.out.println("Working directory set to: " + workingDirectory);
        assert workingDirectory != null && !workingDirectory.startsWith("null");
        PropertyGetter.workingDirectory = workingDirectory;
    }

    /**
     * 
     */
    private PropertyGetter() {
        super();
        // only provides static methods
    }

    /**
     * @return the JNDI context - this contains the properties from context.xml
     * @throws NamingException
     * @throws ClassNotFoundException
     */
    public static Context getNamingContext() throws NamingException {
        Context envCtx = null;
        Context initCtx = new InitialContext();
        try {
            envCtx = (Context) initCtx.lookup(PropertyGetter.TOMCAT_NAMING_CONTEXT);
        } catch (final javax.naming.NoInitialContextException e) {
            // supply our dummy initial context
            System.setProperty(PropertyGetter.JAVA_NAMING_FACTORY_INITIAL,
                PimsInitialContextFactory.class.getName());
            System.out.println(PropertyGetter.JAVA_NAMING_FACTORY_INITIAL + " set to: "
                + PimsInitialContextFactory.class.getName());
            initCtx = new InitialContext();
            envCtx = (Context) initCtx.lookup(PropertyGetter.TOMCAT_NAMING_CONTEXT);
        }
        return envCtx;
    }

    /**
     * @param name the name of the property
     * @param defaultValue the default
     * @return the value in context.xml,or the default
     */
    public static String getStringProperty(final String name, final String defaultValue) {
        Object value;
        try {
            value = PropertyGetter.getNamingContext().lookup(name);
        } catch (final NameNotFoundException e) {
            return defaultValue;
        } catch (final NamingException e) {
            throw new RuntimeException(e);
        }
        if (null == value) {
            return defaultValue;
        }
        return (String) value;
    }

    /**
     * @param name the name of the property
     * @param defaultValue the default
     * @return the value in context.xml,or the default
     */
    public static int getIntProperty(final String name, final int defaultValue) {
        Object value;
        try {
            value = PropertyGetter.getNamingContext().lookup(name);
        } catch (final NameNotFoundException e) {
            return defaultValue;
        } catch (final NamingException e) {
            throw new RuntimeException(e);
        }
        if (null == value) {
            return defaultValue;
        }
        return Integer.parseInt((String) value);
    }

    /**
     * @param name the name of the property
     * @param defaultPath the default location, either absolute, or relative to WEB-INF/
     * @return a File object suitable for opening
     */
    public static File getFileProperty(final String name, final String defaultPath) {
        String path = defaultPath;
        try {
            path = (String) PropertyGetter.getNamingContext().lookup(name);
        } catch (final NameNotFoundException e) {
            // stick with default
            //System.out.println("path: " + path); 
        } catch (final NamingException e) {
            throw new RuntimeException(e);
        }
        if (null == path) {
            path = defaultPath;
        }
        File file = new File(path);
        if (!file.isAbsolute()) {
            assert null != PropertyGetter.workingDirectory;
            file = new File(PropertyGetter.workingDirectory, path);
        }
        System.out.println("Using: " + name + " at: " + file.getAbsolutePath());
        return file;
    }

    public static void setProxySetting() {
        String name = "http.proxyHost";
        copy(name);
        copy("http.proxyPort");
        copy("http.nonProxyHosts");
    }

    public static void setSmtpSetting() {
        copy("mail.smtp.user");
        copy("mail.smtp.host");
        copy("mail.smtp.port");
        copy("mail.smtp.from");
        copy("mail.smtp.auth");
    }

    /**
     * PropertyGetter.copy
     * 
     * @param name
     * @return
     */
    private static String copy(String name) {
        final String value = PropertyGetter.getStringProperty(name, "");
        if (value != null && value.trim().length() > 0) {
            System.setProperty(name, value);
        }
        return value;
    }

    /**
     * Returns an instance of a named class, e.g. a factory. The class must have a public no-argument
     * constructor.
     * 
     * @param propertyName the property to read
     * @param defaultClass the default factory
     * @return an instance of the class
     */
    public static <T extends Object> T getInstance(final String propertyName, final Class<T> defaultClass) {
        final String className = PropertyGetter.getStringProperty(propertyName, null);
        Class<T> factory = defaultClass;
        try {
            if (null != className) {
                factory = (Class<T>) Class.forName(className);
            }
            return factory.newInstance();
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (final InstantiationException e) {
            Throwable cause = e;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            throw new RuntimeException(cause.getMessage(), cause);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
