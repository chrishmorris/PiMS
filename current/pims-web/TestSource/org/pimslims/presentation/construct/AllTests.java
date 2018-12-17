/**
 * current-pims-web org.pimslims.presentation.construct AllTests.java
 * 
 * @author cm65
 * @date 23 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
 * 
 */
package org.pimslims.presentation.construct;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.presentation.PrimerBeanTest;
import org.pimslims.presentation.TargetBeanReaderTest;

/**
 * AllTests
 * 
 */
public class AllTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for org.pimslims.presentation.construct and TargetBean");
        //$JUnit-BEGIN$

        suite.addTestSuite(ConstructProgressListTest.class);
        suite.addTestSuite(TargetBeanReaderTest.class);
        //suite.addTestSuite(org.pimslims.servlet.spot.SpotTargetTester.class);
        suite.addTestSuite(ConstructBeanTest.class);
        suite.addTestSuite(ConstructBeanWriterTest.class);
        suite.addTestSuite(ConstructBeanReaderTest.class);
        suite.addTestSuite(ConstructResultBeanTest.class);
        suite.addTestSuite(PrimerBeanTest.class);

        //TODO suite.addTestSuite(SpotNewConstructWizardTest.class);
        suite.addTestSuite(ConstructDeleterTest.class);
        //$JUnit-END$
        return suite;
    }

}
