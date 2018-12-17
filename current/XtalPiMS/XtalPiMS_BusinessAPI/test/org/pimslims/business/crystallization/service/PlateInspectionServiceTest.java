package org.pimslims.business.crystallization.service;

import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.AbstractXtalTest;

public class PlateInspectionServiceTest extends AbstractXtalTest {

    public PlateInspectionServiceTest(final String methodName, final DataStorage dataStorage) {
        super(methodName, dataStorage);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetService() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {
            final PlateInspectionService service = this.dataStorage.getPlateInspectionService();
            assertNotNull(service);
        } finally {
            this.dataStorage.abort();
        }
    }

    public void testCreateInspection() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {
            final PlateInspectionService service = this.dataStorage.getPlateInspectionService();
            TrialPlate plate = this.createTrialPlate(UNIQUE);
            PlateInspection inspection = getPlateInspection(plate);
            inspection.setDetails(UNIQUE);
            service.create(inspection);

            PlateInspection found = service.find(inspection.getId());
            assertNotNull(found);
            assertEquals(inspection.getId(), found.getId());
        } finally {
            this.dataStorage.abort();
        }
    }

}
