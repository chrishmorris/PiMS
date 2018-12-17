package org.pimslims.crystallization.implementation.integration;

import org.pimslims.crystallization.datastorage.DataStorageFactory;

public class ConstructViewTest extends org.pimslims.crystallization.integration.ConstructViewTest {
    public ConstructViewTest(String methodName) {
        super(methodName, DataStorageFactory.getDataStorageFactory().getDataStorage());

    }
}
