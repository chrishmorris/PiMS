/*
 * Created on 12-Apr-2005 @author Chris Morris
 * @version 0.1
 */
package org.pimslims.servlet;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import junit.framework.Assert;
import junit.framework.Test;
import junit.textui.TestRunner;

import org.pimslims.dao.ModelImpl;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.presentation.mock.MockServletContext;

/**
 * 
 * Test the ContextListener. If it fails, the error message from Tomcat is pretty unhelpful, so these tests
 * are pretty painstaking.
 */
public class TestListener extends junit.framework.TestCase {

    String perspectives = null;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.perspectives = "standard,";
    }

    public void testGetDefaultPerspective() {

        final String[] enabledPerspectives = this.perspectives.split(",");
        final Listener listener = new Listener();
        System.out.println("perspectives:" + this.perspectives);
        Assert.assertTrue(enabledPerspectives[0].equalsIgnoreCase(listener.getDefaultPerspective()));
    }

    public void testGetEnabledPerspective() {

        final String[] enabledPerspectives = this.perspectives.split(",");
        final Listener listener = new Listener();
        final List<String> perspectives = listener.getEnabledPerspectives();
        for (final String name : enabledPerspectives) {
            if (name != null && name.length() > 0) {
                Assert.assertTrue(perspectives.contains(name));
            }
        }
    }

    /**
     * make sure the class can be loaded and instantiated, and the context can be initialized
     */
    public void testListenerClass() {

        try {
            ModelImpl.getModel(new java.io.File("conf/Properties"));
        } catch (final FileNotFoundException e4) {
            Assert.fail(e4.getMessage());
        }

        try {
            Class.forName(ServletUtil.class.getName());
        } catch (final ClassNotFoundException e3) {
            Assert.fail(e3.getMessage());
        }

        Class listenerClass = null;
        java.lang.reflect.Method initializer = null;
        try {
            listenerClass = Class.forName(Listener.class.getName());
            initializer =
                listenerClass.getMethod("contextInitialized", new Class[] { ServletContextEvent.class });
        } catch (final ClassNotFoundException e) {
            Assert.fail(e.getMessage());
        } catch (final SecurityException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        Object listener = null;
        try {
            listener = listenerClass.newInstance();
        } catch (final InstantiationException e1) {
            Assert.fail(e1.getMessage());
        } catch (final IllegalAccessException e1) {
            Assert.fail(e1.getMessage());
        }

        final ServletContext context = new MockServletContext();
        try {
            initializer.invoke(listener, new Object[] { new ServletContextEvent(context) });
        } catch (final IllegalArgumentException e2) {
            throw new RuntimeException(e2);
        } catch (final IllegalAccessException e2) {
            throw new RuntimeException(e2);
        } catch (final InvocationTargetException e2) {
            Throwable cause = e2;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            cause.printStackTrace();
            Assert.fail(cause.getMessage());
        }

        //TODO assertNotNull("model not initialized", context.getAttribute("model"));

        // now shut down
        try {
            final java.lang.reflect.Method destructor =
                listenerClass.getMethod("contextDestroyed", new Class[] { ServletContextEvent.class });
            destructor.invoke(listener, new Object[] { new ServletContextEvent(context) });
        } catch (final SecurityException ex) {
            throw new RuntimeException(ex);
        } catch (final IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (final NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        } catch (final IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (final InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
        System.gc();
        System.runFinalization();
    }

    /**
     * @return a suite including all tests for this class
     */
    public static Test suite() {
        return new junit.framework.TestSuite(TestListener.class);
    }

    /**
     * Runs these tests from the command line
     * 
     * @param args ignored
     */
    public static void main(final String[] args) {
        TestRunner.run(TestListener.suite());
    }
}
