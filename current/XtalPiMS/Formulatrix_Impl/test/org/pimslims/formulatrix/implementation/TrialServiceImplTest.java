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

import org.pimslims.business.core.model.Person;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.service.TrialServiceTest;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.formulatrix.implementation.ManufacturerDataStorageImpl;
import org.pimslims.formulatrix.implementation.ManufacturerVersion;

/**
 * TrialServiceImplTest
 * 
 */
//TODO 02
public class TrialServiceImplTest extends TrialServiceTest {

    /**
     * Constructor for SampleServiceImplTest
     * 
     * @param methodName
     * @param dataStorage
     */
    public TrialServiceImplTest(String methodName) {
        super(methodName, new ManufacturerDataStorageImpl(false, ManufacturerVersion.OULU));
    }


    public void testCreateTrialPlate() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {
            final TrialService service = this.dataStorage.getTrialService();
            PlateType plateType = this.createPlateType(service);

            TrialPlate plate = service.createTrialPlate(BARCODE, plateType);
            assertNotNull(plate);
            Person scientist = new Person();
            scientist.setUsername(UNIQUE + "u");
            this.dataStorage.getPersonService().create(scientist);
            service.updateTrialPlate(plate);
            assertEquals(BARCODE, plate.getBarcode());

            TrialPlate found = service.findTrialPlate(BARCODE);
            assertEquals(BARCODE, found.getBarcode());
            assertNotNull(found.getOwner());
            assertEquals("Default User", found.getOwner().getUsername());
            //TODO assertEquals(plate, found);
        } finally {
            this.dataStorage.abort();
        }
    }
    
    
    /**
     * TrialServiceImplTest.testSetScreen
     * 
     * @see org.pimslims.business.crystallization.service.TrialServiceTest#testSetScreen()
     */
    @Override
    public void testSetScreen() throws BusinessException {
        // MAYBE reinstate this 
    } 

	@Override
	public void testProtein() throws BusinessException {
		// dont care
	}

	@Override
	public void testSetReservoirAdditive() throws BusinessException {
		// dont care
	}

    
    
}
