package org.pimslims.crystallization.implementation.integration;

import org.pimslims.crystallization.datastorage.DataStorageFactory;

public class ScreenViewImplTest extends org.pimslims.crystallization.integration.ScreenViewTest {

    public ScreenViewImplTest(String methodName) {
        super(methodName, DataStorageFactory.getDataStorageFactory().getDataStorage());

    }

}
