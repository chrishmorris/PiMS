package org.pimslims.crystallization;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.pimslims.crystallization.performanceTest.InspectionListTest;
import org.pimslims.crystallization.performanceTest.PlateListTest;
import org.pimslims.crystallization.performanceTest.ScreenListTest;
import org.pimslims.crystallization.performanceTest.TimeCourseTest;
import org.pimslims.crystallization.performanceTest.TrialDropViewTest;
import org.pimslims.crystallization.util.DataStorageFactory;

public class AllPerformanceTest extends TestCase {

    public AllPerformanceTest() {
        super("All performance tests ");
    }

    /**
     * @return test suite
     */
    public static junit.framework.Test suite() {
        final junit.framework.TestSuite suite = new TestSuite();
        DataStorageFactory.getPiMSDS();

        suite.addTestSuite(ScreenListTest.class);
        suite.addTestSuite(PlateListTest.class);
        suite.addTestSuite(InspectionListTest.class);
        suite.addTestSuite(TrialDropViewTest.class);
        suite.addTestSuite(TimeCourseTest.class);
        return suite;
    }
}
