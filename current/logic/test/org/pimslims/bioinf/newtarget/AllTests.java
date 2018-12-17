/**
 * current-pims-web org.pimslims.bioinf.newtarget AllTests.java
 * 
 * @author cm65
 * @date 25 Mar 2008
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.bioinf.newtarget;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * AllTests
 * 
 */
public class AllTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for org.pimslims.bioinf.newtarget");
        //$JUnit-BEGIN$
        //suite.addTestSuite(BioJavaGenBankProteinParserForDev.class);
        //suite.addTestSuite(GenBankMultipleCDSParserTester.class);
        suite.addTestSuite(EMBLNucleotideParserTester.class);
        suite.addTestSuite(PIMSTargetWriterTester.class);
        suite.addTestSuite(DRRecordExtractorTester.class);
        //suite.addTestSuite(BioJavaGenBankProteinParserTester.class);
        suite.addTestSuite(BioDBReferencesTester.class);
        suite.addTestSuite(UniProtParserTester.class);
        suite.addTestSuite(GenBankNucleotideParserTester.class);
        suite.addTestSuite(BioJavaFormatRecognitionTester.class);
        //suite.addTestSuite(DbRefBeanTest.class);
        suite.addTestSuite(BioFormatGuesserTest.class);
        //$JUnit-END$
        return suite;
    }

}
