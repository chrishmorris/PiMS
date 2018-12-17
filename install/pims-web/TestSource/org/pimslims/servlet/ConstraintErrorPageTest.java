/**
 * current-pims-web org.pimslims.servlet ConstraintErrorPageTest.java
 * 
 * @author cm65
 * @date 25 Mar 2008
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2008 cm65 
 * 
 * 
 */
package org.pimslims.servlet;

import java.util.regex.Matcher;

import junit.framework.TestCase;

/**
 * ConstraintErrorPageTest
 * 
 */
public class ConstraintErrorPageTest extends TestCase {

    private static final String MESSAGE = "\"sam_abstractsample_name_projectid_must_be_unique\"";

    /**
     * @param name
     */
    public ConstraintErrorPageTest(String name) {
        super(name);
    }

    public void testRegExp() {
        Matcher m = ConstraintErrorPage.CONSTRAINT.matcher(MESSAGE);
        assertTrue(m.matches());
        assertEquals("name", m.group(2));
        assertEquals("abstractsample", m.group(1));
    }
}
