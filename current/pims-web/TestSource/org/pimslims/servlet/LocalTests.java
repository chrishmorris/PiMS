/*
 * Created on 01-Jun-2005 @author: Chris Morris
 */
package org.pimslims.servlet;

import junit.textui.TestRunner;

import org.pimslims.presentation.pdf.PdfReportTest;
import org.pimslims.presentation.sample.SampleSearcherTest;
import org.pimslims.servlet.access.CreateUserTest;
import org.pimslims.servlet.ajax.AjaxExistsTest;
import org.pimslims.servlet.expert.ExpertViewTest;
import org.pimslims.servlet.report.T2CReportTest;
import org.pimslims.servlet.sample.SampleProgressTest;
import org.pimslims.servlet.spot.CreateMutatedObjectiveTest;
import org.pimslims.servlet.spot.SpotNewTargetTest;
import org.pimslims.servlet.standard.ViewRefSampleTest;
import org.pimslims.servlet.standard.ViewSampleTest;
import org.pimslims.servlet.tag.AllTagTests;
import org.pimslims.servlet.target.TargetGroupDataTest;
import org.pimslims.servlet.target.TargetScoreBoardTest;

/**
 * Test features in org.pimslims.servlet which are directly executable without a servlet container
 * 
 * @version 1.0
 */
public class LocalTests extends junit.framework.TestSuite {

    /**
     * 
     */
    public LocalTests() {
        super("Test PIMS servlets methods locally");
    }

    /**
     * @return test suite
     */
    public static junit.framework.Test suite() {

        final junit.framework.TestSuite suite = new LocalTests();
        suite.addTestSuite(CreateMutatedObjectiveTest.class);
        suite.addTestSuite(PdfReportTest.class);
        suite.addTestSuite(QuickSearchTest.class);
        suite.addTestSuite(ChooseForCreateTest.class);
        suite.addTestSuite(ExpertViewTest.class);
        //TODO suite.addTestSuite(TargetProgressTest.class);
        suite.addTestSuite(DeleteTest.class);
        suite.addTestSuite(CreateTest.class);
        suite.addTest(TestPIMSServlet.suite());
        suite.addTest(org.pimslims.servlet.experiment.AllTestForExperiments.suite());
        suite.addTestSuite(UpdateTest.class);
        suite.addTestSuite(TargetGroupDataTest.class);
        //TODO suite.addTestSuite(SearchTargetTest.class);
        suite.addTestSuite(TargetScoreBoardTest.class);

        suite.addTestSuite(org.pimslims.servlet.spot.SpotNewConstructMilestoneTest.class);
        suite.addTestSuite(SampleProgressTest.class);
        suite.addTest(AllTagTests.suite());
        // currently in org.pimslims.presentation.construct.AllTests suite.addTestSuite(SpotNewConstructWizardTest.class);
        suite.addTestSuite(SpotNewTargetTest.class);
        suite.addTestSuite(AjaxExistsTest.class);

        suite.addTestSuite(SampleSearcherTest.class);
        suite.addTestSuite(SearchSampleTest.class);
        suite.addTestSuite(ViewSampleTest.class);
        // now tested elsewhere suite.addTestSuite(org.pimslims.servlet.spot.SpotTargetTester.class);
        // TODO add when test is fixed
        // suite.addTestSuite(org.pimslims.servlet.spot.SpotTargetExperimentsTest.class);
        //no, moved to RemoteTests suite.addTestSuite(TestHelpPages.class);

        // do this last, it messes up the properties methods
        suite.addTest(TestListener.suite());
        suite.addTestSuite(ViewRefSampleTest.class);
        suite.addTestSuite(CreateUserTest.class);
        suite.addTestSuite(AjaxValidatorTest.class);
        suite.addTestSuite(T2CReportTest.class);
        suite.addTestSuite(EditRoleTest.class);
        suite.addTest(org.pimslims.servlet.plateExperiment.AllTestForPlateExperiments.suite());
        return suite;
    }

    /**
     * Runs these tests from the command line
     * 
     * @param args ignored
     */
    public static void main(final String[] args) {
        TestRunner.run(LocalTests.suite());
    }
}
