package org.pimslims.crystallization.business;

import java.util.Collection;

import junit.framework.TestCase;

import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.model.Crystal;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.CrystalService;

public abstract class CrystalServiceTest extends TestCase {

    protected static final WellPosition WELL_POSITION = new WellPosition("A01.1");
    
    protected final DataStorage dataStorage;

    public CrystalServiceTest(String testName, DataStorage impl) {
        super(testName);
        this.dataStorage = impl;
    }

    /**
     * Need to make a stack of stuff for testing, including the TrialDrop sample in
     * WELL_POSITION. Might also be useful to allow testing on existing data.
     * 
     * @return The barcode of the test plate
     */
    protected abstract String prepareTestData() throws Exception;
    
    public void testGetService() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            CrystalService service = this.dataStorage.getCrystalService();
            assertNotNull(service);
            assertEquals(this.dataStorage, service.getDataStorage());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    public void testCreateFindDelete() throws Exception {
        this.dataStorage.openResources("administrator");
        
        
        try {
            String barcode = prepareTestData();
            
            CrystalService service = this.dataStorage.getCrystalService();
            Crystal toMake = new Crystal();
            toMake.setBarcode(barcode);
            toMake.setWellPosition(WELL_POSITION);
            toMake.setX(new Integer(500));
            toMake.setY(new Integer(1100));
            toMake.setR(new Integer(200));

            service.create(toMake);
            
            assertNotNull(toMake.getId());
            assertTrue(-1l != toMake.getId().longValue());
            assertNotNull(toMake.getNum());
            
            // I think this is unnecessary as its triggerd by getting the id, but still...
            service.getDataStorage().flush();
            

            Collection<Crystal> crystals = service.findByBarcodeAndWell(barcode, WELL_POSITION);

            assertFalse(crystals.isEmpty());
            
            Crystal found = crystals.iterator().next();
            assertEquals(toMake.getId(), found.getId());
            assertEquals(toMake.getBarcode(), found.getBarcode());
            assertEquals(toMake.getWellPosition(), found.getWellPosition());
            assertEquals(toMake.getX(), found.getX());
            assertEquals(toMake.getY(), found.getY());
            assertEquals(toMake.getR(), found.getR());
            assertEquals(toMake.getNum(), found.getNum());
            
            service.delete(toMake);
            
            // Need to flush here or the updates to experiment don't get pushed
            service.getDataStorage().flush();
            
            crystals = service.findByBarcodeAndWell(barcode, WELL_POSITION);

            assertTrue(crystals.isEmpty());
            
            
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

}
