/** 
 * pims-model org.pimslims.utils UtilTestSuite.java
 * @author pajanne
 * @date Jun 22, 2009
 *
 * Protein Information Management System
 * @version: 3.0
 *
 * Copyright (c) 2009 pajanne 
 * The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * UtilTestSuite
 *
 */
public class UtilTestSuite extends TestSuite {
    
    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for org.pimslims.util");
        suite.addTestSuite(AbstractTestCaseTest.class);
        return suite;
    }

}
