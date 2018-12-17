/**
 * V2_2-pims-web org.pimslims.properties PropertyGetterTest.java
 * 
 * @author cm65
 * @date 9 May 2008
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2008 cm65 *
 *  .
 */
package org.pimslims.properties;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.ModelImpl;

/**
 * PropertyGetterTest
 * 
 */
public class PropertyGetterTest extends TestCase {

    /**
     * MyTest class used to test getInstance method
     * 
     */
    public static class MyTest {
        // no body needed for this test 
    }

    //private final AbstractModel model;

    /**
     * @param name name of method to test
     */
    public PropertyGetterTest(final String name) {
        super(name);
        // must open model to access properties
        ModelImpl.getModel();
    }

    public void testGetDefaultString() {
        Assert.assertEquals("aaa", PropertyGetter.getStringProperty("test", "aaa"));
    }

    public void testGetString() {
        Assert.assertFalse(null == PropertyGetter.getStringProperty("db.url", null));
    }

    public void testGetDefaultInstance() {
        MyTest test;

        test = PropertyGetter.getInstance("test", MyTest.class);
        Assert.assertFalse(null == test);
    }

}
