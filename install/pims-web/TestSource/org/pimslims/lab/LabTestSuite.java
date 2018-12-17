package org.pimslims.lab;

import junit.framework.TestSuite;

import org.pimslims.lab.experiment.CohortTest;

public class LabTestSuite extends TestSuite {
    /**
     * 
     */
    public LabTestSuite() {
        super("Testing the implementation of org.pimslims.lab");
    }

    /**
     * @return test suite
     */
    public static junit.framework.Test suite() {
        final junit.framework.TestSuite suite = new LabTestSuite();

        suite.addTestSuite(SDMPrimerDesignerTest.class);
        suite.addTestSuite(RadionuclideConcentrationTest.class);
        suite.addTestSuite(CohortTest.class);
        suite.addTestSuite(ConstructAnnotatorTest.class);
        suite.addTestSuite(CrystallizationUtilityTest.class);
        suite.addTestSuite(LocationUtilityTest.class);
        suite.addTestSuite(PimsStandardPrimerDesignerTest.class);
        // suite.addTestSuite(ProtocolFactoryTest.class);
        // TODO suite.addTestSuite(StatusUtilityTest.class);
        suite.addTestSuite(TestExperimentGroupValue.class);
        suite.addTestSuite(TestHolderFactory.class);

        suite.addTestSuite(TestPlate.class);
        suite.addTestSuite(TestProtocol.class);
        suite.addTestSuite(TestSampleFactory.class);
        suite.addTestSuite(TestWorkflowItem.class);
        suite.addTestSuite(TestYorkPrimerUtility.class);
        suite.addTestSuite(ProteinSequenceTest.class);
        suite.addTestSuite(PersonUtilityTest.class);
        suite.addTestSuite(ConstructUtilityTest.class);
        suite.addTestSuite(SupplierFactoryTest.class);
        suite.addTestSuite(ThreeLetterProteinSeqTest.class);
        //suite.addTestSuite(PositionAwareSequenceTester.class);
        //suite.addTestSuite(TargetProgressTester.class);
        //TODO suite.addTestSuite(org.pimslims.lab.sequence.AllTests.class);

        return suite;
    }

}
