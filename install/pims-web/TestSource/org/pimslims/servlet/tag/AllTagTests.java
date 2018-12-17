/**
 * pims-web org.pimslims.tag AllTagTests.java
 * 
 * @author cm65
 * @date 15 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
 */
package org.pimslims.servlet.tag;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * AllTagTests
 * 
 */
public class AllTagTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for org.pimslims.tag");
        //$JUnit-BEGIN$
        suite.addTestSuite(ListTest.class);
        suite.addTestSuite(ViewButtonTest.class);
        //suite.addTestSuite(RecordLinkTest.class);
        suite.addTestSuite(SequenceTest.class);
        suite.addTestSuite(InputTest.class);
        suite.addTestSuite(UtilsTest.class);
        //$JUnit-END$
        return suite;
    }

}
