package org.pimslims.crystallization.implementation.integration;

import org.pimslims.crystallization.datastorage.DataStorageFactory;

public class SampleViewTest extends org.pimslims.crystallization.integration.SampleViewTest {

    public SampleViewTest(String methodName) {
        super(methodName, DataStorageFactory.getDataStorageFactory().getDataStorage());

    }

}
