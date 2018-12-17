package org.pimslims.presentation.worklist;

import junit.framework.Test;
import junit.framework.TestSuite;

@Deprecated
// obsolete
public class WorkListAllTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for org.pimslims.presentation.worklist");
        // $JUnit-BEGIN$
        suite.addTestSuite(ExpBlueprintDaoTest.class);
        //TODO suite.addTestSuite(SampleDAOTest.class);

        // $JUnit-END$
        return suite;
    }

}
