/**
 * pims-model org.pimslims Factory.java
 * 
 * @author cm65
 * @date 6 Jul 2012
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.util.InstallationProperties;

/**
 * Factory
 * 
 */
public class Factory {

    /**
     * The normal way to get a model.
     * 
     * @return the model
     */
    public static synchronized AbstractModel getModel() {
        try {
            ClassLoader loader = getClassLoader(Factory.class.getClassLoader());
            Class<?> clazz = loader.loadClass(ModelImpl.class.getName());
            Constructor<?> constructor = clazz.getConstructor(InstallationProperties.class);
            Object newInstance = constructor.newInstance(new InstallationProperties());
            return (AbstractModel) newInstance;

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Factory.getClassLoader
     * 
     * @param parent
     * @return Try putting these in the extra library:
     *         file:/C:/TortoiseSVN/current/pims-model/lib/runtime2/antlr-2.7.6.jar
     *         file:/C:/TortoiseSVN/current/pims-model/lib/runtime2/asm-3.1.jar
     *         file:/C:/TortoiseSVN/current/pims-model/lib/runtime2/c3p0-0.9.1.jar
     *         file:/C:/TortoiseSVN/current/pims-model/lib/runtime2/cglib-2.2.jar
     *         file:/C:/TortoiseSVN/current/pims-model/lib/runtime2/ehcache-core-2.4.3.jar
     *         file:/C:/TortoiseSVN/current/pims-model/lib/runtime2/javassist-3.12.0.GA.jar
     */
    static ClassLoader getClassLoader(ClassLoader parent) {
        try {
            File library = new File("lib/runtime2");
            assert library.exists() : "Not found: " + library.getAbsolutePath();
            File[] jars = library.listFiles();
            List<URL> urls = new ArrayList(jars.length);
            for (int i = 0; i < jars.length; i++) {
                if (jars[i].getName().endsWith(".jar")) {
                    URL url = jars[i].toURI().toURL();
                    urls.add(url);
                    //System.out.println(url); 
                }
            }
            ClassLoader loader = new URLClassLoader(urls.toArray(new URL[] {}), parent);
            return loader;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
