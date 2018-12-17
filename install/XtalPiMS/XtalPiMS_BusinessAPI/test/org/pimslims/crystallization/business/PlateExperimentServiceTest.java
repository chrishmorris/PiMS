package org.pimslims.crystallization.business;

import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.service.PlateExperimentService;

public abstract class PlateExperimentServiceTest extends AbstractXtalTest {

    // unique string to avoid name clashes
    static protected final String UNIQUE = "test" + System.currentTimeMillis();

    public PlateExperimentServiceTest(String methodName, DataStorage dataStorage) {
        super(methodName, dataStorage);
    }

    public void testGetService() throws Exception {
        try {
            this.dataStorage.openResources("administrator");

            PlateExperimentService service = this.dataStorage.getPlateExperimentService();
            assertNotNull(service);

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    //    public void testCreate() throws Exception {
    //        this.dataStorage.openResources("administrator");
    //        try {
    //
    //            PlateExperimentService service = this.dataStorage.getPlateExperimentService();
    //            PlateExperiment toMake = new PlateExperiment();
    //            toMake.setDescription("desc" + UNIQUE);
    //            PlateType plateType = new PlateType();
    //            plateType.setName("Greiner");
    //            plateType.setRows(8);
    //            plateType.setColumns(12);
    //            plateType.setReservoir(4);
    //            plateType.setSubPositions(4);
    //
    //            TrialPlate plate = new TrialPlate(plateType);
    //            plate.setBarcode(BARCODE);
    //            toMake.setPlate(plate);
    //            List<TrialDrop> wells = new ArrayList<TrialDrop>();
    //            TrialDrop well = new TrialDrop();
    //            well.setWellPosition(new WellPosition("A02"));
    //            wells.add(well);
    //            toMake.setWells(wells);
    //            Condition condition = new Condition();
    //            condition.setLocalName("cond" + UNIQUE);
    //            ConditionQuantity quantity = new ConditionQuantity(condition, 100.0, "uL");
    //
    //            well.setMotherLiquor(quantity);
    //            well.setReservoir(quantity);
    //
    //            service.create(toMake);
    //
    //            final Collection<PlateExperiment> experiments = service.findSimpleByPlate(plate, null);
    //            assertEquals(1, experiments.size());
    //            final PlateExperiment experiment = experiments.iterator().next();
    //            assertEquals(BARCODE, experiment.getPlate().getBarcode());
    //            assertEquals(toMake.getDescription(), experiment.getDescription());
    //            //wells = service.getWells(experiment);
    //            //assertEquals(1, wells.size());
    //            well = wells.iterator().next();
    //            assertEquals(new WellPosition("A02"), well.getWellPosition());
    //            // note that the condition in the well is not reported
    //
    //        } finally {
    //            this.dataStorage.abort(); // not testing persistence
    //        }
    //
    //    }

    //    public void testCreateWells() throws Exception {
    //        this.dataStorage.openResources("administrator");
    //        try {
    //
    //            PlateExperimentService service = this.dataStorage.getPlateExperimentService();
    //            PlateExperiment toMake = new PlateExperiment();
    //            toMake.setDescription("desc" + UNIQUE);
    //            PlateType plateType = new PlateType();
    //            plateType.setName("Greiner");
    //            plateType.setRows(8);
    //            plateType.setColumns(12);
    //            plateType.setReservoir(4);
    //            plateType.setSubPositions(4);
    //            TrialPlate plate = new TrialPlate(plateType);
    //            plate.setBarcode(BARCODE);
    //            toMake.setPlate(plate);
    //
    //            List<TrialDrop> wells = new ArrayList<TrialDrop>();
    //            TrialDrop well = new TrialDrop();
    //            well.setWellPosition(new WellPosition("A02.1"));
    //            wells.add(well);
    //            TrialDrop well2 = new TrialDrop();
    //            well2.setWellPosition(new WellPosition("A02.2"));
    //            wells.add(well2);
    //            toMake.setWells(wells);
    //            Condition condition = new Condition();
    //            condition.setLocalName("cond" + UNIQUE);
    //            ConditionQuantity quantity = new ConditionQuantity(condition, 100.0, "uL");
    //
    //            well.setMotherLiquor(quantity);
    //            well.setReservoir(quantity);
    //
    //            service.create(toMake);
    //
    //            final Collection<PlateExperiment> experiments = service.findByPlate(BARCODE, null);
    //            assertEquals(1, experiments.size());
    //            final PlateExperiment experiment = experiments.iterator().next();
    //            //wells = service.getWells(experiment);
    //            //assertEquals(2, wells.size());
    //
    //            PlateExperiment found = service.findByPlateAndWell(plate, new WellPosition("A02.2"));
    //            assertNotNull(found);
    //
    //        } finally {
    //            this.dataStorage.abort(); // not testing persistence
    //        }
    //
    //    }

    //    public void testCreateWithDeepWellPlate() throws Exception {
    //        //XtalSession session = this.dataStorage.getSession("administrator");
    //        this.dataStorage.openResources("administrator");
    //        try {
    //
    //            PlateExperimentService service = this.dataStorage.getPlateExperimentService();
    //            PlateExperiment toMake = new PlateExperiment();
    //            toMake.setDescription("desc" + UNIQUE);
    //            PlateType plateType = new PlateType();
    //            plateType.setName("Greiner");
    //            plateType.setRows(8);
    //            plateType.setColumns(12);
    //            plateType.setReservoir(4);
    //            plateType.setSubPositions(4);
    //            TrialPlate plate = new TrialPlate(plateType);
    //            plate.setBarcode(BARCODE);
    //            toMake.setPlate(plate);
    //
    //            Screen screenToMake = new Screen();
    //            screenToMake.setName("screen" + System.currentTimeMillis());
    //            Condition condition = new Condition();
    //            condition.setLocalName("c" + System.currentTimeMillis());
    //            //condition.setWellPosition(new WellPosition("H12"));
    //            //screenToMake.addCondition(condition);
    //            final ScreenService screenService = this.dataStorage.getScreenService();
    //            screenService.create(screenToMake);
    //            final DeepWellPlate deepWellPlate =
    //                screenService.createDeepWellPlate(screenToMake, "dwp" + UNIQUE);
    //            toMake.setDeepWellPlate(deepWellPlate);
    //            assertEquals(1, screenToMake.getConditionPositions().size());
    //            assertEquals(1, toMake.getWells().size());
    //            assertEquals(1, deepWellPlate.getConditionPositions().size());
    //            //assertEquals(new WellPosition("H12"), deepWellPlate.getConditionPositions().iterator().next().getWellPosition());
    //
    //            service.create(toMake);
    //
    //            final Collection<PlateExperiment> experiments = service.findByPlate(BARCODE, null);
    //            assertEquals(1, experiments.size());
    //            final PlateExperiment experiment = experiments.iterator().next();
    //            Collection<TrialDrop> wells = service.getWells(experiment, null);
    //            assertEquals(1, wells.size());
    //            TrialDrop well = wells.iterator().next();
    //            assertEquals(new WellPosition("H12"), well.getWellPosition());
    //            DeepWellPlate madeDwp = experiment.getDeepWellPlate();
    //            assertNotNull(madeDwp);
    //            assertEquals(deepWellPlate.getBarcode(), madeDwp.getBarcode());
    //            assertNotNull(madeDwp.getScreen());
    //            assertEquals(screenToMake.getName(), madeDwp.getScreen().getName());
    //            //Condition madeCondition = well.getCondition();
    //            //assertNotNull(madeCondition);
    //            //assertEquals(condition.getLocalName(), madeCondition.getLocalName());
    //
    //        } finally {
    //            this.dataStorage.abort(); // not testing persistence
    //        }
    //
    //    }

    //    public void testCreateWithSample() throws Exception {
    //        //XtalSession session = this.dataStorage.getSession("administrator");
    //        this.dataStorage.openResources("administrator");
    //        try {
    //
    //            PlateExperimentService service = this.dataStorage.getPlateExperimentService();
    //            PlateExperiment toMake = new PlateExperiment();
    //            toMake.setDescription("desc" + UNIQUE);
    //            PlateType plateType = new PlateType();
    //            plateType.setName("Greiner");
    //            plateType.setRows(8);
    //            plateType.setColumns(12);
    //            plateType.setReservoir(4);
    //            plateType.setSubPositions(4);
    //            TrialPlate plate = new TrialPlate(plateType);
    //            plate.setBarcode(BARCODE);
    //            toMake.setPlate(plate);
    //            Sample protein = new Sample("protein" + UNIQUE);
    //            this.dataStorage.getSampleService().create(protein);
    //            List<TrialDrop> wells = new ArrayList<TrialDrop>();
    //            TrialDrop well = new TrialDrop();
    //            well.setWellPosition(new WellPosition("A02"));
    //            SampleQuantity sampleQuantity = new SampleQuantity(protein, 100, "nL");
    //            well.addSample(sampleQuantity);
    //            wells.add(well);
    //            toMake.setWells(wells);
    //
    //            service.create(toMake);
    //
    //            final Collection<PlateExperiment> experiments = service.findByPlate(BARCODE, null);
    //            assertEquals(1, experiments.size());
    //            final PlateExperiment experiment = experiments.iterator().next();
    //            //wells = service.getWells(experiment);
    //            //assertEquals(1, wells.size());
    //            //well = wells.iterator().next();
    //            //final Sample madeProtein = well.getProtein();
    //            //assertNotNull(madeProtein);
    //            //assertEquals(protein.getName(), madeProtein.getName());
    //
    //        } finally {
    //            this.dataStorage.abort(); // not testing persistence
    //        }
    //
    //    }

    //    public void testCreateWithSampleAndDeepWellPlate() throws Exception {
    //        this.dataStorage.openResources("administrator");
    //        try {
    //
    //            PlateExperimentService service = this.dataStorage.getPlateExperimentService();
    //            PlateExperiment toMake = new PlateExperiment();
    //            toMake.setDescription("desc" + UNIQUE);
    //            PlateType plateType = new PlateType();
    //            plateType.setName("Greiner");
    //            plateType.setRows(8);
    //            plateType.setColumns(12);
    //            plateType.setReservoir(4);
    //            plateType.setSubPositions(4);
    //            TrialPlate plate = new TrialPlate(plateType);
    //            plate.setBarcode(BARCODE);
    //            toMake.setPlate(plate);
    //
    //            // set deep well plate
    //            Screen screenToMake = new Screen();
    //            screenToMake.setName("screen" + System.currentTimeMillis());
    //            Condition condition = new Condition();
    //            condition.setLocalName("c" + System.currentTimeMillis());
    //            //condition.setWellPosition(new WellPosition("H12"));
    //            //screenToMake.addCondition(condition);
    //            final ScreenService screenService = this.dataStorage.getScreenService();
    //            screenService.create(screenToMake);
    //            final DeepWellPlate deepWellPlate =
    //                screenService.createDeepWellPlate(screenToMake, "dwp" + UNIQUE);
    //            toMake.setDeepWellPlate(deepWellPlate);
    //
    //            // set protein
    //            //TODO this can't be right, it overrides the setting of the deep well plate
    //            Sample protein = new Sample("protein" + UNIQUE);
    //            this.dataStorage.getSampleService().create(protein);
    //            List<TrialDrop> wells = new ArrayList<TrialDrop>();
    //            TrialDrop well = new TrialDrop();
    //            well.setWellPosition(new WellPosition("H12"));
    //            SampleQuantity sampleQuantity = new SampleQuantity(protein, 100, "nL");
    //            well.addSample(sampleQuantity);
    //            wells.add(well);
    //            toMake.setWells(wells);
    //
    //            service.create(toMake);
    //
    //            final Collection<PlateExperiment> experiments = service.findByPlate(BARCODE, null);
    //            assertEquals(1, experiments.size());
    //            final PlateExperiment experiment = experiments.iterator().next();
    //            Collection<TrialDrop> madeWells = service.getWells(experiment, null);
    //            assertEquals(1, madeWells.size());
    //            TrialDrop madeWell = madeWells.iterator().next();
    //            assertEquals(new WellPosition("H12"), madeWell.getWellPosition());
    //            //final Sample madeProtein = madeWell.getProtein();
    //            //(madeProtein);
    //            //assertEquals(protein.getName(), madeProtein.getName());
    //            assertNotNull(experiment.getDeepWellPlate());
    //            assertEquals(deepWellPlate.getBarcode(), experiment.getDeepWellPlate().getBarcode());
    //            //Condition madeCondition = madeWell.getCondition();
    //            //assertNotNull(madeCondition);
    //            //assertEquals(condition.getLocalName(), madeCondition.getLocalName());
    //
    //        } finally {
    //            this.dataStorage.abort(); // not testing persistence
    //        }
    //
    //    }

    //    public void testConstruct() throws Exception {
    //        this.dataStorage.openResources("administrator");
    //        try {
    //
    //            PlateExperimentService service = this.dataStorage.getPlateExperimentService();
    //            PlateExperiment toMake = new PlateExperiment();
    //            toMake.setDescription("desc" + UNIQUE);
    //            PlateType plateType = new PlateType();
    //            plateType.setName("Greiner");
    //            plateType.setRows(8);
    //            plateType.setColumns(12);
    //            plateType.setReservoir(4);
    //            plateType.setSubPositions(4);
    //            TrialPlate plate = new TrialPlate(plateType);
    //            plate.setBarcode(BARCODE);
    //            toMake.setPlate(plate);
    //            // have to make sample to set scientist
    //            Construct construct = new Construct("construct" + UNIQUE, null);
    //            ConstructService constructService = this.dataStorage.getConstructService();
    //            constructService.create(construct);
    //            Sample protein = new Sample("sample" + UNIQUE);
    //            this.dataStorage.getSampleService().create(protein);
    //            // have to make a sample production to save construct
    //            //SampleProduction sp = new SampleProduction(protein, SampleProductionServiceTest.NOW);
    //            //sp.setDescription("desc");
    //            //this.dataStorage.getSampleProductionService().create(sp);
    //            List<TrialDrop> wells = new ArrayList<TrialDrop>();
    //            TrialDrop well = new TrialDrop();
    //            well.setWellPosition(new WellPosition("A02"));
    //            SampleQuantity sampleQuantity = new SampleQuantity(protein, 100, "nL");
    //            well.addSample(sampleQuantity);
    //            wells.add(well);
    //            toMake.setWells(wells);
    //
    //            service.create(toMake); // now make the plate experiment
    //
    //            final Collection<PlateExperiment> experiments = service.findByConstruct(construct, null);
    //            assertEquals(1, experiments.size());
    //            final PlateExperiment experiment = experiments.iterator().next();
    //            Collection<TrialDrop> madeWells = service.getWells(experiment, null);
    //            assertEquals(1, madeWells.size());
    //            TrialDrop madeWell = madeWells.iterator().next();
    //            //assertEquals(construct.getName(), madeWell.getProtein().getConstruct().getName());
    //
    //        } finally {
    //            this.dataStorage.abort(); // not testing persistence
    //        }
    //
    //    }
    //
    //    public void testScientist() throws Exception {
    //        this.dataStorage.openResources("administrator");
    //        try {
    //
    //            PlateExperimentService service = this.dataStorage.getPlateExperimentService();
    //            PlateExperiment toMake = new PlateExperiment();
    //            toMake.setDescription("desc" + UNIQUE);
    //            PlateType plateType = new PlateType();
    //            plateType.setName("Greiner");
    //            plateType.setRows(8);
    //            plateType.setColumns(12);
    //            plateType.setReservoir(4);
    //            plateType.setSubPositions(4);
    //            TrialPlate plate = new TrialPlate(plateType);
    //            plate.setBarcode(BARCODE);
    //            toMake.setPlate(plate);
    //            // have to make sample to set scientist
    //            Construct construct = new Construct("construct" + UNIQUE, null);
    //            ConstructService constructService = this.dataStorage.getConstructService();
    //            constructService.create(construct);
    //            Sample protein = new Sample("sample" + UNIQUE);
    //            this.dataStorage.getSampleService().create(protein);
    //            // have to make a sample production to save construct
    //            //SampleProduction sp = new SampleProduction(protein, SampleProductionServiceTest.NOW);
    //            //sp.setDescription("desc");
    //
    //            //this.dataStorage.getSampleProductionService().create(sp);
    //            List<TrialDrop> wells = new ArrayList<TrialDrop>();
    //            TrialDrop well = new TrialDrop();
    //            well.setWellPosition(new WellPosition("A02"));
    //            SampleQuantity sampleQuantity = new SampleQuantity(protein, 100, "nL");
    //            well.addSample(sampleQuantity);
    //            wells.add(well);
    //            toMake.setWells(wells);
    //            Person scientist = new Person();
    //            scientist.setUsername("user" + UNIQUE);
    //            scientist.setFamilyName("familyName" + UNIQUE);
    //            this.dataStorage.getPersonService().create(scientist);
    //            toMake.setOwner(scientist);
    //
    //            service.create(toMake); // now make the plate experiment
    //
    //            final Collection<PlateExperiment> experiments = service.findSimpleByUser(scientist, null);
    //            assertEquals(1, experiments.size());
    //            final PlateExperiment experiment = experiments.iterator().next();
    //            assertNotNull(experiment.getOwner());
    //            assertEquals(scientist.getUsername(), experiment.getOwner().getUsername());
    //
    //        } finally {
    //            this.dataStorage.abort(); // not testing persistence
    //        }

    //    }
}
