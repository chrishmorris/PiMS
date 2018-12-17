package org.pimslims.crystallization;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.crystallization.implementation.ConstructServiceImplTest;
import org.pimslims.crystallization.implementation.ConstructTest;
import org.pimslims.crystallization.implementation.GroupServiceImplTest;
import org.pimslims.crystallization.implementation.ImageSaveAndFindImplTest;
import org.pimslims.crystallization.implementation.LocationServiceImplTest;
import org.pimslims.crystallization.implementation.OrganisationServiceImplTest;
import org.pimslims.crystallization.implementation.PersonTest;
import org.pimslims.crystallization.implementation.PlateExperimentImplTest;
import org.pimslims.crystallization.implementation.PlateInspectionImplTest;
import org.pimslims.crystallization.implementation.ProjectServiceImplTest;
import org.pimslims.crystallization.implementation.SampleServiceImplTest;
import org.pimslims.crystallization.implementation.SchedulePlanTest;
import org.pimslims.crystallization.implementation.ScoreServiceImplTest;
import org.pimslims.crystallization.implementation.ScoringSchemeServiceImplTest;
import org.pimslims.crystallization.implementation.ScreenServiceImplTest;
import org.pimslims.crystallization.implementation.SoftwareServiceImplTest;
import org.pimslims.crystallization.implementation.TrialDropServiceImplTest;
import org.pimslims.crystallization.implementation.TrialServiceImplTest;
import org.pimslims.crystallization.implementation.TrialServiceUtilsTest;
import org.pimslims.crystallization.implementation.integration.ConditionViewTest;
import org.pimslims.crystallization.implementation.integration.ConstructViewTest;
import org.pimslims.crystallization.implementation.integration.GroupViewTest;
import org.pimslims.crystallization.implementation.integration.ImageViewTest;
import org.pimslims.crystallization.implementation.integration.SampleViewTest;
import org.pimslims.crystallization.implementation.integration.ScoreViewTest;
import org.pimslims.crystallization.implementation.integration.ScreenViewImplTest;
import org.pimslims.crystallization.implementation.integration.TrialDropViewTest;
import org.pimslims.crystallization.refdata.ScreenUtilityTest;
import org.pimslims.crystallization.tools.AllTests;

public class AllxtalPiMSImplTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for org.pimslims.crystallization.implementation");
        //$JUnit-BEGIN$
        suite.addTest(PlateInspectionImplTest.suite());

        suite.addTestSuite(TrialServiceUtilsTest.class);
        suite.addTestSuite(ImageSaveAndFindImplTest.class);
        suite.addTest(ConstructServiceImplTest.suite());
        suite.addTest(OrganisationServiceImplTest.suite());
        suite.addTest(GroupServiceImplTest.suite());
        suite.addTest(LocationServiceImplTest.suite());

        suite.addTest(TrialDropServiceImplTest.suite());
        //suite.addTest(WellServiceImplTest.suite());
        suite.addTest(ScreenServiceImplTest.suite());
        suite.addTestSuite(ConstructTest.class);
        // suite.addTestSuite(SampleProductionTest.class);
        suite.addTestSuite(PersonTest.class);
        suite.addTest(PlateExperimentImplTest.suite());
        // suite.addTest(PlateServiceImplTest.suite());
        suite.addTest(SampleServiceImplTest.suite());
        // was suite.addTest(ImageServiceImplTest.suite());
        suite.addTest(SoftwareServiceImplTest.suite());
        suite.addTest(ScoringSchemeServiceImplTest.suite());
        suite.addTest(ScoreServiceImplTest.suite());
        suite.addTest(TrialServiceImplTest.suite());
        suite.addTest(ProjectServiceImplTest.suite());
        suite.addTestSuite(SchedulePlanTest.class);

        suite.addTest(AllTests.suite());

        //integration test
        suite.addTestSuite(ScoreViewTest.class);
        suite.addTestSuite(ScreenViewImplTest.class);
        suite.addTestSuite(GroupViewTest.class);
        suite.addTestSuite(ConstructViewTest.class);
        suite.addTestSuite(ConditionViewTest.class);
        suite.addTestSuite(SampleViewTest.class);
        suite.addTestSuite(ImageViewTest.class);
        suite.addTestSuite(TrialDropViewTest.class);
        suite.addTestSuite(ScreenUtilityTest.class);
        //$JUnit-END$
        return suite;
    }
}
