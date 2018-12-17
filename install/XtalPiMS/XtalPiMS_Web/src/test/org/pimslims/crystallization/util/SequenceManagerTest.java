/*
 * PlateExperimentServiceImplTest.java JUnit based test Created on 09 August 2007, 16:56
 */

package org.pimslims.crystallization.util;

import java.util.Calendar;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.exception.ConstraintException;

public class SequenceManagerTest extends TestCase {

    private static final Calendar NOW = Calendar.getInstance();

    private static final String BARCODE = "sm" + System.currentTimeMillis();

    private final DataStorage dataStorage;

    public SequenceManagerTest(final String testName) {
        super(testName);
        this.dataStorage = DataStorageFactory.getDataStorageFactory().getDataStorage();
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite(SequenceManagerTest.class);

        return suite;
    }

    /**
     *  
     */
    public void testSequence() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            final TrialService trialservice = this.dataStorage.getTrialService();
            final TrialPlate plate = createFilledTrialPlate(BARCODE);
            trialservice.saveTrialPlate(plate);
            this.dataStorage.flush();

            SequenceManager manager = new SequenceManager(this.dataStorage);
            assertNull(manager.getSequence(BARCODE));
            // set for the first time
            manager.setSequence(BARCODE, "QWERTY");
            assertEquals("QWERTY", manager.getSequence(BARCODE));
            // change an existing sequence
            manager.setSequence(BARCODE, "WERTY");
            assertEquals("WERTY", manager.getSequence(BARCODE));

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    public TrialPlate createFilledTrialPlate(final String barcode) throws BusinessException,
        ConstraintException {

        //final Screen screen = createScreen(wv, barcode);

        // Create a PlateType
        final PlateType plateType = createDummyPlateType("PlateType" + barcode);
        plateType.setSubPositions(1);
        // create user
        //final Person user1 = super.createXPerson();
        //final Person user2 = super.createXGroup().getUsers().iterator().next();
        // Create xtal TrialPlate
        final TrialService service = this.dataStorage.getTrialService();
        final TrialPlate plate = new TrialPlate(plateType);
        plate.setBarcode(barcode);
        plate.setCreateDate(Calendar.getInstance());
        plate.buildAllTrialDrops();
        /* could  set sample
        //final Sample sample = new Sample(sampleName);
        final SampleQuantity sampleQuantity = new SampleQuantity(sample,
                0.0000001, "L");

        for (final TrialDrop trialDrop : plate.getTrialDrops()) {

            trialDrop.addSample(sampleQuantity);
        } */
        plate.setDescription("description for " + barcode);
        plate.setDestroyDate(Calendar.getInstance());

        //plate.setScreen(screen);

        //plate.setOperator(user1);
        //plate.setOwner(user2);
        //plate.setGroup(user2.getGroups().iterator().next());

        return plate;

    }

    private PlateType createDummyPlateType(final String name) {

        final PlateType plateType = new PlateType();
        plateType.setName(name);
        plateType.setRows(8);
        plateType.setColumns(12);
        plateType.setSubPositions(2);
        plateType.setReservoir(2);

        return plateType;

    }
}
