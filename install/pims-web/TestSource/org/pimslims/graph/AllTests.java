/**
 * current-pims-web org.pimslims.utils.graph AllTests.java
 * 
 * @author cm65
 * @date 26 Mar 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.graph;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.graph.implementation.ExperimentAndSampleModelTest;
import org.pimslims.graph.implementation.SampleProvenanceModelTest;
import org.pimslims.graph.implementation.LocationGraphModelTest;
import org.pimslims.graph.implementation.PlateExperimentModelTest;
import org.pimslims.graph.implementation.ProtocolGraphModelTest;

/**
 * AllTests
 * 
 */
public class AllTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for org.pimslims.utils.graph");
        //$JUnit-BEGIN$
        suite.addTestSuite(GrappaModelLoaderTest.class);
        suite.addTestSuite(DotGraphUtilTest.class);
        suite.addTestSuite(PlateExperimentModelTest.class);
        suite.addTestSuite(LocationGraphModelTest.class);
        suite.addTestSuite(ProtocolGraphModelTest.class);
        suite.addTestSuite(ExperimentAndSampleModelTest.class);
        suite.addTestSuite(SampleProvenanceModelTest.class);
        //$JUnit-END$
        return suite;
    }

}
