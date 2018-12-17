/**
 * pims-web org.pimslims.presentation.servlet.utils ErrorMessagesPropertiesTest.java
 * 
 * @author cm65
 * @date 3 Sep 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.servlet.utils;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * ErrorMessagesPropertiesTest
 * 
 */
public class ErrorMessagesPropertiesTest extends TestCase {

    /**
     * Test method for
     * {@link org.pimslims.presentation.servlet.utils.ErrorMessagesProperties#getProperty(java.lang.String)}.
     */
    public void testNotFound() {
        try {
            ErrorMessagesProperties.getProperty("nonesuch");
            Assert.fail("Exception expected for missing error message");
        } catch (final AssertionError e) {
            // that's the intended behavior;
        }
    }

    public void testFound() {

        final String message = ErrorMessagesProperties.getProperty("DATE.IS.WRONG");
        Assert.assertEquals("The date is incorrect format", message);
    }

}
