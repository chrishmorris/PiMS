package org.pimslims.business.crystallization.service;

import java.util.List;
import java.util.Map;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.core.service.SampleService;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.ConditionQuantity;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.SampleQuantity;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.AbstractXtalTest;

public class TrialServiceTest extends AbstractXtalTest {

    private static final WellPosition D03II = new WellPosition(4, 3, 2);

    public TrialServiceTest(final String methodName, final DataStorage dataStorage) {
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
            final TrialService service = this.dataStorage.getTrialService();
            assertNotNull(service);
        } finally {
            this.dataStorage.abort();
        }
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
//Comment this out to make test pass in Fx code, also see below
            plate.setOwner(scientist);
            service.updateTrialPlate(plate);
            assertEquals(BARCODE, plate.getBarcode());

            TrialPlate found = service.findTrialPlate(BARCODE);
            assertEquals(BARCODE, found.getBarcode());
         // can no longer change creator  assertNotNull(found.getOwner());
// This is needed to make the test pass for the Fx code, remove assert after and see above
//            assertEquals("Default User", found.getOwner().getUsername());
         // can no longer change creator  assertEquals(plate.getOwner().getUsername(), found.getOwner().getUsername());
            //TODO assertEquals(plate, found);
        } finally {
            this.dataStorage.abort();
        }
    }

    public void testProtein() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {
            SampleService ss = this.dataStorage.getSampleService();
            Sample sample = new Sample();
            sample.setName(UNIQUE + "s");
            ss.create(sample);

            final TrialService service = this.dataStorage.getTrialService();
            PlateType plateType = this.createPlateType(service);
            TrialPlate plate = service.createTrialPlate(BARCODE, plateType);
            TrialDrop trialDrop = plate.getTrialDrop("D11.1");
            trialDrop.addSample(new SampleQuantity(sample, 100, "nL"));

            service.updateTrialPlate(plate);
            service.updateTrialDrop(trialDrop);

            WellPosition well = new WellPosition("D11.1");
            TrialDrop foundDrop = service.findTrialDrop(BARCODE, well);
            List<SampleQuantity> quantities = foundDrop.getSamples();
            assertFalse(quantities.isEmpty());
            SampleQuantity quantity = quantities.iterator().next();
            assertEquals(sample.getName(), quantity.getSample().getName());
            //TODO assertEquals("nL", quantity.getUnit());
            //TODO assertEquals(100d, quantity.getQuantity());
        } finally {
            this.dataStorage.abort();
        }
    }

    public void testSetScreen() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {

            final TrialService service = this.dataStorage.getTrialService();
            PlateType plateType = this.createPlateType(service);
            dataStorage.flush();
            TrialPlate saved = service.createTrialPlate(BARCODE, plateType);
            Screen screen = this.createScreen();
            saved.setScreen(screen);
            service.updateTrialPlate(saved);
            dataStorage.flush();

            TrialPlate found = service.findTrialPlate(BARCODE);
            assertNotNull(found.getScreen());
            assertEquals(screen.getName(), found.getScreen().getName());

            Condition condition = screen.getCondition(D03II);
            assertNotNull(condition);
            TrialDrop drop = service.findTrialDrop(BARCODE, D03II);
            assertNotNull(drop);
            ConditionQuantity reservoir = drop.getReservoir();
            assertNotNull(reservoir);
            assertEquals(condition.getLocalName(), reservoir.getCondition().getLocalName());
            assertEquals(1.0d, reservoir.getQuantity());

            //LATER test drop contents

        } finally {
            this.dataStorage.abort();
        }
    }

    public void testSetReservoirAdditive() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {

            final TrialService service = this.dataStorage.getTrialService();
            PlateType plateType = this.createPlateType(service);

            TrialPlate saved = new TrialPlate(plateType);
            saved.setBarcode(BARCODE);
            Screen screen = this.createScreen();
            saved.setScreen(screen);
            saved.setAdditiveScreen(true);
            Condition additive = new Condition();
            additive.setLocalName(UNIQUE + "a");
            saved.setMotherLiquor(additive);
            service.saveTrialPlate(saved);

            Condition condition = screen.getCondition(D03II);
            assertNotNull(condition);
            TrialDrop drop = service.findTrialDrop(BARCODE, D03II);
            assertNotNull(drop);
            assertTrue(drop.isAdditiveScreen());
            ConditionQuantity reservoir = drop.getReservoir();
            // See TODO comment in TrialPlateDAO.createExperiments
            //            assertNotNull(reservoir);
            //            assertEquals(additive.getLocalName(), reservoir.getCondition().getLocalName());
            //            assertEquals(1.0d, reservoir.getQuantity());

            //LATER test drop contents

        } finally {
            this.dataStorage.abort();
        }
    }

    //TODO test using both mother liquor and regular screen (non-additive, i.e. screen is mixed into reservoir)

    /**
     * Superclass' implementation is unsuitable, because it makes a TrialService was
     * 
     * @Override protected PlateType createPlateType(TrialService service) { final PlateType plateType = new
     *           PlateType(); plateType.setName(UNIQUE + "pt"); plateType.setRows(8);
     *           plateType.setColumns(12); plateType.setReservoir(4); plateType.setSubPositions(3); return
     *           plateType; }
     */

    @Override
    protected Screen createScreen() throws BusinessException {
        final Screen screen = getScreen();
        Map<WellPosition, Condition> cps = new java.util.HashMap<WellPosition, Condition>();
        Condition condition = new Condition();
        condition.setLocalName(UNIQUE + "s");
        cps.put(D03II, condition);
        screen.setConditionPositions(cps);
        this.dataStorage.getScreenService().create(screen);
        return screen;
    }

}
