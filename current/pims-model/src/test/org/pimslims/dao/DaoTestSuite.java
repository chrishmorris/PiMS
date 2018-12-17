/*
 * Created on 25-Oct-2004 @author: Chris Morris
 */
package org.pimslims.dao;

import java.io.FileNotFoundException;

import junit.textui.TestRunner;

import org.pimslims.model.experiment.LockTest;

/**
 * Test suite for the implementation of the PIMS back end. This implementation is based on the hibernate tools
 * code generator.
 */
public class DaoTestSuite extends junit.framework.TestSuite {

    /**
     * 
     */
    public DaoTestSuite() {
        super("Testing the implementation of org.pimslims.metamodel");
    }

    /**
     * @return test suite
     */
    public static junit.framework.Test suite() {
        final junit.framework.TestSuite suite = new DaoTestSuite();

        suite.addTestSuite(AbstractModelObjectTest.class);
        suite.addTestSuite(ConcurrentVersionTest.class);
        suite.addTestSuite(DeleteTest.class);
        suite.addTestSuite(ExceptionTest.class);
        suite.addTestSuite(FileImplTester.class);
        suite.addTestSuite(IDGeneratorTester.class);
        suite.addTestSuite(MetaRoleImplTest.class);
        suite.addTestSuite(MetaUtilsTest.class);
        suite.addTestSuite(ModelImplTester.class);
        suite.addTestSuite(ModelObjectTester.class);
        suite.addTestSuite(PerformenceTester.class);
        suite.addTestSuite(ReadableVersionImplTester.class);
        suite.addTestSuite(ReadableVersionTest.class);
        suite.addTestSuite(ReflectiveModelObjectTester.class);
        suite.addTestSuite(RoleMtoMTester.class);
        suite.addTestSuite(WritableVersionImplTester.class);
        suite.addTestSuite(UniqueNameTest.class);
        suite.addTestSuite(LockTest.class);

        // OutputSample is now child of Experiment
        // suite.addTestSuite(OutputSampleReverseTest.class); 
        // one to many role
        // suite.addTestSuite(PackagesTester.class);

        // test case unstable testVersion failed sometimes
        // junit.framework.ComparisonFailure: expected:<Morris & Lin...> but was:<Lin & Morris...>
        // suite.addTestSuite(JournalCitationTester.class);

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
