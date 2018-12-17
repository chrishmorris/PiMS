/** 
 * current-pims-web org.pimslims.utils.testmodel AllTests.java
 * @author cm65
 * @date 26 Mar 2008
 *
 * Protein Information Management System
 * @version: 1.3
 *
 * Copyright (c) 2008 cm65 
     * 
     * 
  * 
 */
package org.pimslims.utils.testmodel;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * AllTests
 *
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.pimslims.utils.testmodel");
        //$JUnit-BEGIN$
        suite.addTestSuite(MolComponentTest.class);
        //$JUnit-END$
        return suite;
    }

}
