/**
 * GroupServiceImplTest.java
 * 
 * Created on 5 Feb 2008
 * 
 * Author seroul
 */
package org.pimslims.crystallization.implementation;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.business.crystallization.service.HumanScoreServiceTest;
import org.pimslims.crystallization.datastorage.DataStorageFactory;

public class HumanScoreServiceImplTest extends HumanScoreServiceTest {

    // unique string used to avoid name clashes
    private static final String UNIQUE = "hs" + System.currentTimeMillis();

    /**
     * @param name
     */
    public HumanScoreServiceImplTest(final String testName) {
        super(testName, DataStorageFactory.getDataStorageFactory().getDataStorage());
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite(HumanScoreServiceImplTest.class);

        return suite;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

    }
}
