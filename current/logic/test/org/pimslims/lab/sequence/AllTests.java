/**
 * V3_3-web org.pimslims.lab.sequence AllTests.java
 * 
 * @author cm65
 * @date 3 Dec 2009
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.lab.sequence;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * AllTests
 * 
 */
public class AllTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for org.pimslims.lab.sequence");
        //$JUnit-BEGIN$
        suite.addTestSuite(DnaSequenceTest.class);
        suite.addTestSuite(PositionAwareSequenceTester.class);
        suite.addTestSuite(AmbiguousDnaSequenceTest.class);
        //$JUnit-END$
        assert 0 < suite.countTestCases();
        assert null != suite.testAt(2);
        return suite;
    }

}
