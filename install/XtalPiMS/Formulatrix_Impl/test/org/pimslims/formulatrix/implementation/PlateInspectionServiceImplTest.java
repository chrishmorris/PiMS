/**
 *  TrialServiceImplTest.java
 * 
 * @author cm65
 * @date 13 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.formulatrix.implementation;

import java.util.Calendar;
import java.util.Collection;

import org.pimslims.business.core.model.Location;
import org.pimslims.business.core.service.LocationService;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.service.PlateInspectionService;
import org.pimslims.business.crystallization.service.PlateInspectionServiceTest;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.formulatrix.implementation.PlateInspectionServiceImpl;
import org.pimslims.formulatrix.implementation.ManufacturerDataStorageImpl;
import org.pimslims.formulatrix.implementation.ManufacturerVersion;

/**
 * TrialServiceImplTest
 * 
 */
//TODO 01
public class PlateInspectionServiceImplTest extends PlateInspectionServiceTest {

    /**
     * Constructor for SampleServiceImplTest
     * 
     * @param methodName
     * @param dataStorage
     */
    public PlateInspectionServiceImplTest(String methodName) {
        super(methodName, new ManufacturerDataStorageImpl(false, ManufacturerVersion.OULU));
    }

    public void testFindNoInspection() throws BusinessException {
        Calendar after = Calendar.getInstance();
        try {
        	this.dataStorage.openResources("administrator");
        	TrialService trialService = this.dataStorage.getTrialService();
            final PlateType plateType = createPlateType(trialService);
            final TrialPlate plate = trialService.createTrialPlate(UNIQUE, plateType);
            PlateInspection inspection = getPlateInspection(plate);
            PlateInspectionService pis = this.dataStorage.getPlateInspectionService();
            pis.create(inspection);
            after.setTimeInMillis(System.currentTimeMillis() + 2000);
            Collection<Integer> found =
            		((PlateInspectionServiceImpl)pis).findInspectionIds(after, true);
            assertTrue(found.isEmpty());
        } finally {
            if (null != dataStorage) {
                dataStorage.abort();
                dataStorage.closeResources();
            }
        }
        
        try {
            this.dataStorage.openResources("administrator");
            PlateInspectionService pis = this.dataStorage.getPlateInspectionService();
            ((PlateInspectionServiceImpl) pis).findInspectionIds(after,true);
        } finally {
            if (null != dataStorage) {
                dataStorage.abort();
                dataStorage.closeResources();
            }
        }
    }
    

    public void dontTestCreateInspectionWithLocation() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {
            final PlateInspectionService service = this.dataStorage.getPlateInspectionService();
            final LocationService locservice = this.dataStorage.getLocationService();
            TrialPlate plate = this.createTrialPlate(UNIQUE);
            PlateInspection inspection = getPlateInspection(plate);
            inspection.setDetails(UNIQUE);
            //inspection.setLocation(createLocation());
            Location loc = locservice.find(2L);
            System.out.println(loc.getName());
            inspection.setLocation(loc);
            service.create(inspection);
            
            PlateInspection found = service.find(inspection.getId());
            assertNotNull(found);
            assertNotNull(found.getLocation());
            assertNotNull(inspection.getLocation());
            assertEquals(inspection.getLocation().getId(), found.getLocation().getId());
        } finally {
            this.dataStorage.abort();
        }
    }

    public void testCreateInspection(){
    	System.out.println("PlateInspectionServiceImplTest.testCreateInspection(): Suppressing test in superclass, not appropriate for imagers with capture profiles");
    }
   
}
