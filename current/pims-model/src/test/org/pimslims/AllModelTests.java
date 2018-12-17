package org.pimslims;

import java.io.FileNotFoundException;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.pimslims.dao.ModelImpl;
import org.pimslims.model.experiment.ParameterTest;
import org.pimslims.other.TimeoutTest;
import org.pimslims.persistence.JpqlQueryTest;
import org.pimslims.upgrader.UpgraderTestSuite;

/**
 * AllTests
 * 
 */
public class AllModelTests extends TestCase {

    public AllModelTests() {
        super("Testing the implementation of the whole PiMS model project");
    }

    /**
     * @return test suite
     */
    public static junit.framework.Test suite() {
        final junit.framework.TestSuite suite = new TestSuite();

        // v41

        suite.addTestSuite(ParameterTest.class);

        // access
        suite.addTest(org.pimslims.access.AccessImplTester.suite());
        suite.addTest(org.pimslims.access.AccessTestSuite.suite());
        // dao
        suite.addTest(org.pimslims.dao.DaoTestSuite.suite());
        // metamodel
        suite.addTestSuite(org.pimslims.metamodel.MetaClassImplTester.class);
        // model
        suite.addTest(org.pimslims.model.ApiTestSuite.suite());
        // other
        // persistence
        suite.addTestSuite(org.pimslims.persistence.HibernateUtilTester.class);
        // search
        suite.addTest(org.pimslims.search.SearchTestSuite.suite());
        // upgrader 
        suite.addTest(UpgraderTestSuite.suite());
        // util
        suite.addTest(org.pimslims.test.UtilTestSuite.suite());
        suite.addTestSuite(TimeoutTest.class);
        suite.addTestSuite(JpqlQueryTest.class);
        return suite;
    }

    public static void oneTimeTearDown() {
        /* could HibernateUtilTester.showStatistics(model);
        final Statistics stat = HibernateUtilTester.getStatistics();
        // make sure every session is closed
        assert stat.getSessionCloseCount() == stat.getSessionOpenCount(); */
    }

    /**
     * Runs these tests from the command line
     * 
     * @param args ignored
     */
    public static void main(final String[] args) {
        try {
            ModelImpl.getModel(new java.io.File("conf/Properties"));
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        TestRunner.run(suite());
    }
}
