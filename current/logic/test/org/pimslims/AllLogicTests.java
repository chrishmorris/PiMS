package org.pimslims;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.json.JSONArrayTest;
import org.pimslims.json.JSONObjectTest;
import org.pimslims.kpi.KpiTest;
import org.pimslims.lab.TestMeasurement;
import org.pimslims.presentation.pdf.DocumentTest;
import org.pimslims.properties.PropertyGetterTest;
import org.pimslims.report.PivotTableTest;
import org.pimslims.report.ReportTest;
import org.pimslims.report.ThroughputReportTest;
import org.pimslims.sec.LicenseTest;
import org.pimslims.spreadsheet.CsvPrinterTest;
import org.pimslims.spreadsheet.SpreadsheetTest;
import org.pimslims.xmlio.ProtocolXmlConvertorTest;
import org.pimslims.xmlio.VectorXmlConvertorTest;

public class AllLogicTests {
    static public String TestingDataPath = "./data";

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(AllLogicTests.suite());
    }

    public static Test suite() {
        //when run all test, do not show logs of ref-data loader
        //AbstractLoader.silent = true;

        if (System.getProperty("TestingDataPath") != null) {
            AllLogicTests.TestingDataPath = System.getProperty("TestingDataPath");
        }
        final TestSuite suite = new TestSuite("Test for org.pimslims");

        suite.addTest(org.pimslims.bioinf.AllBioinfTests.suite());

        suite.addTest(org.pimslims.lab.LabTestSuite.suite());

        suite.addTestSuite(ProtocolXmlConvertorTest.class);
        suite.addTestSuite(VectorXmlConvertorTest.class);
        suite.addTestSuite(ReportTest.class);
        suite.addTestSuite(ThroughputReportTest.class);
        suite.addTestSuite(KpiTest.class);
        suite.addTestSuite(SpreadsheetTest.class);
        suite.addTestSuite(CsvPrinterTest.class);
        suite.addTestSuite(PivotTableTest.class);

        suite.addTestSuite(TestMeasurement.class);

        suite.addTestSuite(DocumentTest.class);
        suite.addTestSuite(JSONObjectTest.class);
        suite.addTestSuite(JSONArrayTest.class);
        suite.addTestSuite(LicenseTest.class);
        suite.addTestSuite(PropertyGetterTest.class);
        // TODO fix - it has an absolute path in it
        //suite.addTest(org.pimslims.utils.primer3.AllTests.suite());
        //suite.addTestSuite(TestPlasmid.class);
        return suite;
    }

}
