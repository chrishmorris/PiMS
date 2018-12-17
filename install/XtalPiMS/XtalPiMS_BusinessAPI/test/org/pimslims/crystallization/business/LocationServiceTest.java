package org.pimslims.crystallization.business;

import java.util.Calendar;

import junit.framework.TestCase;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Location;
import org.pimslims.business.core.service.LocationService;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.service.PlateExperimentService;

public abstract class LocationServiceTest extends TestCase {

    protected static final String BARCODE = "barcode" + System.currentTimeMillis();

    protected static final String UNIQUE = "test" + System.currentTimeMillis();

    protected static final Calendar NOW = Calendar.getInstance();

    protected final DataStorage dataStorage;

    public LocationServiceTest(String testName, DataStorage impl) {
        super(testName);
        this.dataStorage = impl;
    }

    public void testGetService() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            LocationService service = this.dataStorage.getLocationService();
            assertNotNull(service);
            assertEquals(this.dataStorage, service.getDataStorage());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    public void testCreate() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            LocationService service = this.dataStorage.getLocationService();
            Location toMake = new Location();
            toMake.setName(UNIQUE);

            service.create(toMake);
            Location found = service.findByName(toMake.getName());

            assertEquals(toMake.getName(), found.getName());
            // LATER pressure, temperature

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    public void testFindByPlateInspection() throws Exception {
        this.dataStorage.openResources("administrator");
        try {

            // make a location
            Location location = new Location();
            location.setName(UNIQUE);
            LocationService locationService = this.dataStorage.getLocationService();
            locationService.create(location);

            // prepare the plate
           // PlateExperimentService pes = this.dataStorage.getPlateExperimentService();
            //PlateExperiment peToMake = new PlateExperiment();
            //peToMake.setDescription("desc" + UNIQUE);
            PlateType plateType = new PlateType();
            plateType.setName("Greiner");
            plateType.setColumns(12);
            plateType.setRows(8);
            plateType.setSubPositions(3);
            TrialPlate plate = new TrialPlate(plateType);
            plate.setBarcode(BARCODE);
            //peToMake.setPlate(plate);
            plate.buildAllTrialDrops();

            //peToMake.setWells(plate.getTrialDrops());
            //pes.create(peToMake);

/*            // make the plate inspection
            PlateInspectionService pis = this.dataStorage.getPlateInspectionService();
            PlateInspection pi = new PlateInspection();
            pi.setPlate(plate);
            pi.setInspectionDate(Calendar.getInstance());
            pi.setInspectionName("ins" + UNIQUE);
            pi.setLocation(location);
            pis.create(pi);

            Location found =
                    locationService.findByPlateInspection(pis.findByInspectionName(pi.getInspectionName()));
            if (found != null) {
                assertEquals(location.getName(), found.getName());
            }*/
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }

    }

    public void testCurrentPlateLocation() throws Exception {
        this.dataStorage.openResources("administrator");
        try {

            // make a location
            Location location = new Location();
            location.setName(UNIQUE);
            LocationService locationService = this.dataStorage.getLocationService();
            locationService.create(location);

            // prepare the plate
            //PlateExperimentService pes = this.dataStorage.getPlateExperimentService();
            //PlateExperiment peToMake = new PlateExperiment();
            //peToMake.setDescription("desc" + UNIQUE);

            PlateType plateType = new PlateType();
            plateType.setName("Greiner");
            plateType.setColumns(12);
            plateType.setRows(8);
            plateType.setSubPositions(3);
            TrialPlate plate = new TrialPlate(plateType);
            plate.setBarcode(BARCODE);
            //peToMake.setPlate(plate);
            plate.buildAllTrialDrops();

            //peToMake.setWells(plate.getTrialDrops());
            //pes.create(peToMake);

/*            // make the plate inspection - that is the only way to record plate location
            PlateInspectionService pis = this.dataStorage.getPlateInspectionService();
            PlateInspection pi = new PlateInspection();
            pi.setPlate(plate);
            pi.setInspectionDate(Calendar.getInstance());
            pi.setInspectionName("ins" + UNIQUE);
            pi.setLocation(location);
            pis.create(pi);

            Location found =
                    locationService.findCurrentPlateLocation(this.dataStorage.getTrialService().findTrialPlate(
                    BARCODE));
            if (found != null) {
                assertEquals(location.getName(), found.getName());
            }*/
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }

    }
}
