/**
 * xtalPiMS_Web org.pimslims.crystallization.servlet PimsClassLoader.java
 * 
 * @author cm65
 * @date 6 Jul 2012
 * 
 *       Protein Information Management System
 * @version: 4.3
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.servlet;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * PimsClassLoader
 * 
 */
public class PimsClassLoader extends URLClassLoader {

    /**
     * Constructor for PimsClassLoader
     * 
     * @param array
     * @param parent
     */
    public PimsClassLoader(URL[] array, ClassLoader parent) {
        super(array, parent);
    }

    /**
     * PimsClassLoader.findClass
     * 
     * @see java.net.URLClassLoader#findClass(java.lang.String)
     */
    @Override
    protected Class<?> findClass(String arg0) throws ClassNotFoundException {
        //System.out.println("Find: " + arg0);
        return super.findClass(arg0);
    }

    /**
     * PimsClassLoader.loadClass
     * 
     * @see java.lang.ClassLoader#loadClass(java.lang.String)
     */
    @Override
    public Class<?> loadClass(String arg0) throws ClassNotFoundException {
        //System.out.println("Load: " + arg0);
        return super.loadClass(arg0);
    }

}
