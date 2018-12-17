/**
 * current-pims-web org.pimslims.servlet.standard HostListTest.java
 * 
 * @author susy
 * @date 24-Apr-2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 susy The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.standard;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.presentation.HostBean;

/**
 * HostListTest
 * 
 */
public class HostListTest extends TestCase {

    //private static String NAME = "name";

    /*
     * Test method for 'org.pimslims.servlet.standard.HostList.makeHostBean()'
     */
    public void testMakeHostBean() {
        final HostBean hostBean = new HostBean();
        Assert.assertNull(hostBean.getHostName());
        HostList.makeHostBean();
    }

}
