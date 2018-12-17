package org.pimslims.crystallization.implementation;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.crystallization.business.ConstructServiceTest;
import org.pimslims.crystallization.datastorage.DataStorageFactory;

public class ConstructServiceImplTest extends ConstructServiceTest {

    /**
     * Constructor for ConstructServiceImplTest
     * 
     * @param methodName
     * @param dataStorage
     */
    public ConstructServiceImplTest(String methodName) {
        super(methodName, DataStorageFactory.getDataStorageFactory().getDataStorage());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ConstructServiceImplTest.class);

        return suite;
    }

}
