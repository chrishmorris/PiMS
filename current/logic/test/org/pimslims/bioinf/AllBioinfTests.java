package org.pimslims.bioinf;



import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.bioinf.local.SWSearchTester;
import org.pimslims.bioinf.local.SequenceGetterTester;
import org.pimslims.bioinf.newtarget.AllNewTargetTests;
import org.pimslims.bioinf.targets.UtilTester;
import org.pimslims.lab.sequence.DnaSequenceTest;

public class AllBioinfTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for org.pimslims.bioinf");
        // $JUnit-BEGIN$
        suite.addTestSuite(DnaSequenceTest.class);
        //suite.addTestSuite(GenBankMultipleCDSNucleotideParserTest.class);
        //suite.addTestSuite(PimsBlastXmlParserTester.class);
        suite.addTestSuite(SWSearchTester.class);
        suite.addTestSuite(SequenceGetterTester.class);
        //suite.addTestSuite(TopHitBeanUtilityTester.class);
        //suite.addTestSuite(BlastMultipleTester.class);
        //suite.addTestSuite(BioJavaGenBankProteinParserTester.class);
        //suite.addTestSuite(CdsTest.class);
        //suite.addTestSuite(DBFetchTest.class);

        // fails in ant task
        // suite.addTestSuite(NCBIBLastTest.class);
        // fails in ant task
        // suite.addTestSuite(WUBLastTest.class);
        // $JUnit-END$
        // no longer maintained suite.addTestSuite(TestLeedsHTMLTargetsParser.class);
        suite.addTestSuite(UtilTester.class);
        suite.addTest(AllNewTargetTests.suite());
        //suite.addTest(org.pimslims.bioinf.newtarget.AllTests.suite());
        return suite;
    }

}
