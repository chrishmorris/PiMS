package org.pimslims.command;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.command.DataUpdate.ExperimentConnectorTest;
import org.pimslims.command.DataUpdate.ExperimentParameterFixerTest;

public class AllTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for org.pimslims.command");
        // $JUnit-BEGIN$

        // All these utilities load the database suite.addTestSuite(ScoreBoardUtilityTester.class);
        suite.addTestSuite(SampleCatsUtilityTester.class);
        //suite.addTestSuite(GenericRefLoaderTest.class);
        suite.addTestSuite(ProtocolUtilityTester.class);
        suite.addTestSuite(ReagentUtilityTester.class);
        //suite.addTestSuite(ChemicalUtilityTester.class);

        // $JUnit-END$
        // currently no tests suite.addTestSuite(RefDataLoaderTest.class);
        //obsolete suite.addTestSuite(org.pimslims.bioinf.targets.ServletTargetCreatorTest.class);
        suite.addTestSuite(ExperimentParameterFixerTest.class);
        suite.addTestSuite(ExperimentConnectorTest.class);
        return suite;
    }

}
