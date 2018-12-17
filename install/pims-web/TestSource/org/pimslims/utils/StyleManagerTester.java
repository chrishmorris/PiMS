/**
 * V2_2-pims-web org.pimslims.utils StyleManagerTester.java
 * 
 * @author pvt43
 * @date 5 Nov 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * StyleManagerTester
 * 
 */
public class StyleManagerTester extends TestCase {

    public void testGetNextColor() {
        final StyleManager sm = new StyleManager();
        Assert.assertEquals(sm.styles[0], sm.getNextColor(20d, null));
        Assert.assertEquals(sm.styles[1], sm.getNextColor(20d, sm.styles[0]));
        Assert.assertEquals(sm.styles[0], sm.getNextColor(20d, sm.styles[1]));
        Assert.assertEquals(sm.styles[1], sm.getNextColor(20d, sm.styles[0]));
        Assert.assertEquals(sm.styles[2], sm.getNextColor(21d, sm.styles[1]));
        Assert.assertEquals(sm.styles[3], sm.getNextColor(25d, sm.styles[2]));
        Assert.assertEquals(sm.styles[0], sm.getNextColor(20d, sm.styles[3]));
    }
}
