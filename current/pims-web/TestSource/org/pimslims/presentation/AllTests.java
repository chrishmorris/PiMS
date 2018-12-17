package org.pimslims.presentation;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.presentation.create.CustomGetterTest;
import org.pimslims.presentation.experiment.DefaultExperimentNameTest;
import org.pimslims.presentation.experiment.ExperimentBeanTest;
import org.pimslims.presentation.experiment.ExperimentCreatorTest;
import org.pimslims.presentation.experiment.ExperimentGroupWriterTest;
import org.pimslims.presentation.experiment.ExperimentReaderTest;
import org.pimslims.presentation.experiment.ExperimentWriterTest;
import org.pimslims.presentation.experiment.OPPFExperimentNameTest;
import org.pimslims.presentation.plateExperiment.PlateReaderTest;
import org.pimslims.presentation.protocol.ProtocolBeanTest;
import org.pimslims.presentation.sample.HolderContentsTest;
import org.pimslims.presentation.sample.SampleBeanTest;
import org.pimslims.presentation.sample.SampleLocationBeanTest;
import org.pimslims.presentation.vector.VectorBeanTest;

public class AllTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for org.pimslims.presentation");
        // $JUnit-BEGIN$    
        suite.addTestSuite(CsrfDefenceTest.class);
        suite.addTestSuite(HolderContentsTest.class);
        suite.addTestSuite(CodonBeanTest.class);
        //suite.addTest(WorkListAllTests.suite());
        suite.addTestSuite(PlateExperimentUpdateBeanTest.class);
        suite.addTestSuite(DayBeanTest.class);
        suite.addTestSuite(WeekBeanTest.class);
        suite.addTestSuite(ExperimentBeanTest.class);
        suite.addTestSuite(OPPFExperimentNameTest.class);
        suite.addTestSuite(DefaultExperimentNameTest.class);
        suite.addTestSuite(CustomGetterTest.class);
        suite.addTestSuite(ModelObjectBeanTest.class);
        suite.addTestSuite(NoteBeanTest.class);
        suite.addTestSuite(AttributeToHTMLTest.class);
        suite.addTestSuite(ComplexBeanTest.class);
        suite.addTestSuite(SampleBeanTest.class);
        suite.addTestSuite(ExperimentUpdateBeanTest.class);
        suite.addTestSuite(PlateReaderTest.class);
        suite.addTestSuite(ExperimentCreatorTest.class);
        suite.addTestSuite(ExperimentGroupWriterTest.class);
        suite.addTestSuite(PermissionReaderTest.class);
        suite.addTestSuite(PermissionWriterTest.class);
        suite.addTestSuite(ExperimentReaderTest.class);
        suite.addTestSuite(ExperimentWriterTest.class);
        suite.addTestSuite(SampleComponentBeanTest.class);
        suite.addTestSuite(SampleBeanWriterTest.class);
        suite.addTestSuite(TargetBeanWriterTest.class);
        suite.addTestSuite(ProtocolBeanTest.class);
        suite.addTestSuite(SampleLocationBeanTest.class);
        suite.addTestSuite(ServletUtilTest.class);
        suite.addTestSuite(StockUtilityTest.class);
        suite.addTestSuite(VectorBeanTest.class);
        // fails suite.addTestSuite(TargetBeanWriterTest.class);
        suite.addTest(org.pimslims.presentation.mru.MRUAllTests.suite());

        suite.addTest(org.pimslims.presentation.construct.AllTests.suite());

        // Leeds tests
        //        suite.addTestSuite(EntryCloneTest.class);
        // $JUnit-END$
        return suite;
    }

}
