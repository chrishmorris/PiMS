package org.pimslims.crystallization.business;

import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.service.PlateInspectionService;

public abstract class InspectionServiceTest extends AbstractXtalTest {

    private static final long NOW = System.currentTimeMillis();

    public InspectionServiceTest(final String methodName, final DataStorage dataStorage) {
        super(methodName, dataStorage);
    }

    public void testGetService() throws Exception {
        this.dataStorage.openResources("administrator");
        try {

            final PlateInspectionService service = this.dataStorage.getPlateInspectionService();
            assertNotNull(service);
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

}
