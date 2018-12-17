package org.pimslims.crystallization.implementation.integration;

import org.pimslims.crystallization.datastorage.DataStorageFactory;

public class GroupViewTest extends org.pimslims.crystallization.integration.GroupViewTest {
    public GroupViewTest(String methodName) {
        super(methodName, DataStorageFactory.getDataStorageFactory().getDataStorage());

    }
}
