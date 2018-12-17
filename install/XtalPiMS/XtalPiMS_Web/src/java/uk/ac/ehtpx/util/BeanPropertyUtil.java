/*
 * BeanPropertyUtil.java Created on 19 April 2006, 16:42 To change this template, choose Tools | Template
 * Manager and open the template in the editor.
 */

package uk.ac.ehtpx.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

// Note: There is a design decision here to capture any related exceptions
// that may occur during the processing of this method, and wrap it
// into an IllegalArgumentException.
//
// Although it is generally not good practice to wrap all exceptions
// and pass them back as a string message, the nested exceptions
// aren't available until Java 1.4, and this should work with Java 1.1+
//
// Secondly, the method for accessing the property is supposed to be
// used dymanically with an editor that knows the existence of the
// properties given, so that if there is an error accessing it
// it can be treated as a problem, and whether it is because the get
// method is private (which would result in a invokation target exception)
// or whether it does not exist (which would result in the exception)
// is not particularly important.

/**
 * Provides utility methods for accessing JavaBeans.
 * 
 * @author Alex Blewitt &lt;<I><A href="mailto:Alex.Blewitt@ioshq.com">Alex.Blewitt@ioshq.com</A></I>&gt;
 * @version 1.0
 */
public class BeanPropertyUtil {
    /**
     * Implements a static utility class; cannot be instantiated.
     */
    private BeanPropertyUtil() {
    }

    /**
     * Stores the list of class/property names with associated read methods.
     */
    private static Map<String, Method> map = new WeakHashMap<String, Method>();

    /**
     * Return the value of the JavaBean <CODE>property</CODE> from <CODE>instance</CODE>.
     * 
     * Uses {@link java.beans.Introspector} to obtain the appropriate {@link java.beans.PropertyDescriptor},
     * and then the accessor method is invoked dynamically using {@link java.lang.reflect.Method}.
     * 
     * @return Object value of the property. Primitive types are wrapped automatically by the reflection
     *         package.
     * @see java.beans.Introspector
     * @see java.beans.PropertyDescriptor
     * @see java.beans.PropertyDescriptor#getReadMethod
     * @see java.lang.reflect.Method#invoke
     * @param name the property name to look up
     * @param instance the bean instance to use IllegalArgumentException if the <I>name</I> does not exist in
     *            <I>instance</I> or there is an exception dynamically invoking the accessor method .
     */
    public static Object getProperty(String name, Object instance) {
        try {

            Method read = getReadMethod(name, instance);
            if (read == null) {
                throw new IllegalArgumentException("Cannot find instance with property '" + name + "'");
            }

            return read.invoke(instance);
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (IntrospectionException ex) {
            throw new IllegalArgumentException(ex);
        } catch (InvocationTargetException ex) {
            throw new IllegalArgumentException(ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(ex);
        }

    }

    /**
     * Obtain the read method for property <em>name</em> associated with <em>instance</em>. Once the method is
     * located, it is stored in {@link #map} using a format <code>fully.qualified.Class#name</code> so that it
     * is obtained quicker on subsequent requests. As the map is a {@link java.util.WeakHashMap} then memory
     * requests may cause parts of this cache to be lost.
     * 
     * @param name the property name
     * @param instance the instance to query
     * @return Method
     * @throws IntrospectionException error
     */
    private static Method getReadMethod(final String name, final Object instance)
        throws IntrospectionException {
        String id = instance.getClass() + "#" + name;
        Method read = (Method) map.get(id);
        if (read == null) {
            // Get the BeanInfo, either from BeanInfo class or reflection
            BeanInfo info = Introspector.getBeanInfo(instance.getClass(), Introspector.USE_ALL_BEANINFO);
            // Lookup all of this bean's properties
            PropertyDescriptor pds[] = info.getPropertyDescriptors();
            // Search through the list for the one with 'name'
            for (int i = 0; i < pds.length; i++) {
                PropertyDescriptor pd = pds[i];
                if (name.equals(pd.getName())) {
                    // and now that we've found it, invoke it with no arguments
                    //(null)
                    read = pd.getReadMethod();
                    map.put(id, read);
                    break;
                }
            }
        }
        if (read == null) {
            throw new IllegalArgumentException("Cannot find instance with " + "property '" + name + "'");
        }
        return read;
    }
}
