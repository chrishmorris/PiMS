/*
 * SampleServiceImplTest.java JUnit based test Created on 09 August 2007, 16:56
 */

package org.pimslims.crystallization.implementation;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Location;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.Component;
import org.pimslims.business.crystallization.model.ComponentQuantity;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.DeepWellPlate;
import org.pimslims.business.crystallization.model.PlateExperimentInfo;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.SampleQuantity;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.ScreenType;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.VolumeMap;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.PlateExperimentService;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.service.TrialServiceTest;
import org.pimslims.business.crystallization.view.PlateExperimentView;
import org.pimslims.business.crystallization.view.TrialDropView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.DAOUtils;
import org.pimslims.crystallization.dao.ScreenDAO;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.logging.Logger;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.holder.RefHolderOffset;
import org.pimslims.model.reference.CrystalType;

/**
 * 
 * @author ian
 */
public class TrialServiceImplTest extends TrialServiceTest {

    /**
     * <p>
     * The logger for this class.
     * </p>
     */
    private static final Logger LOGGER = Logger.getLogger(TrialServiceImplTest.class);

    /**
     * <p>
     * Unique string used to avoid name clashes.
     * </p>
     */
    private static String UNIQUE = "test" + System.currentTimeMillis();

    private static final String sampleName = "Sample" + UNIQUE;

    /**
     * TrialServiceImplTest.suite
     * 
     * @return A Test suite containing this TestCase
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite(TrialServiceImplTest.class);
        return suite;
    }

    /**
     * Constructor for TrialServiceImplTest
     * 
     * @param testName - the name of this Test
     */
    public TrialServiceImplTest(final String testName) {
        super(testName, DataStorageFactory.getDataStorageFactory().getDataStorage());
    }

    public TrialServiceImplTest(final DataStorage dataStorage) {
        super("TrialServiceImplTest", dataStorage);
    }

    /**
     * <p>
     * Get the WritableVersion from this DataStorage.
     * </p>
     */
    protected WritableVersion getWritableVersion() {
        return ((DataStorageImpl) dataStorage).getWritableVersion();
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testCreatePlateType() throws BusinessException {

        this.dataStorage.openResources("administrator");

        try {
            TrialService ts = this.dataStorage.getTrialService();
            PlateType type = new PlateType();
            type.setColumns(12);
            type.setRows(8);
            type.setName(UNIQUE);
            PlateType created = ts.create(type);

            PlateType found = ts.findPlateType(UNIQUE);
            assertNotNull(found);
            assertEquals(UNIQUE, found.getName());
            assertEquals(12, found.getColumns());
            assertEquals(8, found.getRows());

            long id = created.getId();
            found = ts.findPlateType(id);
            assertNotNull(found);
            assertEquals(UNIQUE, found.getName());
            assertEquals(12, found.getColumns());
            assertEquals(8, found.getRows());

        } finally {

            // Not testing persistence
            this.dataStorage.abort();

        }
    }

    /**
     * Test of create simple TrialPlate
     */
    public void testSimpleMapping() throws Exception {
        UNIQUE = "test" + System.currentTimeMillis();
        // Connect as administrator
        this.dataStorage.openResources("administrator");

        try {

            // Get a WritableVersion
            final WritableVersion wv = getWritableVersion();

            // Create a PlateType
            final PlateType plateType = createDummyPlateType("PlateType" + UNIQUE);

            // Create xtal TrialPlate
            final TrialPlate plate = new TrialPlate(plateType);
            plate.setBarcode(UNIQUE);
            plate.setCreateDate(Calendar.getInstance());
            plate.setDescription("description" + UNIQUE);
            plate.setDestroyDate(Calendar.getInstance());
            assertEquals(plateType, plate.getPlateType());
            // Create PiMS TrialPlate
            final TrialService service = this.dataStorage.getTrialService();
            service.saveTrialPlate(plate);

            // Verify PiMS Holder
            assertNotNull(plate.getId());
            final Holder holder = wv.findFirst(Holder.class, Holder.PROP_NAME, UNIQUE);
            assertNotNull(holder);

            assertEquals("PlateType" + UNIQUE, holder.getHolderType().getName());

            final Experiment exp = holder.getSamples().iterator().next().getOutputSample().getExperiment();
            assertEquals(2, exp.getInputSamples().size()); // protein and
                                                           // reservoir
                                                           // additive
            Iterator<InputSample> iterator = exp.getInputSamples().iterator();
            assertNotNull(iterator.next().getRefInputSample());
            // TODO assertNotNull(iterator.next().getRefInputSample());
            assertEquals(1, exp.getOutputSamples().size());
            OutputSample os = exp.getOutputSamples().iterator().next();
            assertNotNull(os.getRefOutputSample());

        }

        finally {

            // Not testing persistence
            this.dataStorage.abort();

        }

    }

    /**
     * Test of create TrialPlate with more details
     */
    public void testFullMapping() throws Exception {
        UNIQUE = "test" + System.currentTimeMillis();
        // Connect as administrator
        this.dataStorage.openResources("administrator");

        try {

            // Get a WritableVersion
            final WritableVersion wv = getWritableVersion();

            // Get a xtal filled TrialPlate
            final TrialPlate plate = createFilledTrialPlate(wv, UNIQUE);

            // Create PiMS TrialPlate
            final TrialService service = this.dataStorage.getTrialService();
            service.saveTrialPlate(plate);

            // TODO test reservoir

            // Verify PiMS TrialPlate

            assertNotNull(plate.getId());
            final Holder holder = wv.get(plate.getId());
            assertNotNull(holder);
            assertEquals("PlateType" + UNIQUE, holder.getHolderType().getName());
            assertEquals(2, ((CrystalType) holder.getHolderType()).getResSubPosition().intValue());
            // size
            final int numberOfWells = plate.getPlateType().getColumns() * plate.getPlateType().getRows();
            assertEquals(numberOfWells, holder.getSamples().size());
            // date
            assertEquals(plate.getCreateDate(), holder.getStartDate());
            assertEquals(plate.getDestroyDate(), holder.getEndDate());
            // description
            assertEquals(plate.getDescription(), holder.getDetails());
            // screen
            assertEquals(plate.getScreen().getName(), holder.getRefHolderOffsets().iterator().next()
                .getRefHolder().getName());

            final Experiment exp = holder.getSamples().iterator().next().getOutputSample().getExperiment();
            // construct
            assertEquals(plate.getConstruct().getName(), exp.getResearchObjective().getName());
            // operator and owner
            assertEquals(plate.getOperator().getUsername(), exp.getOperator().getName());
            assertEquals(plate.getOwner().getUsername(), exp.getCreator().getName());
            // group
            assertTrue(exp.getCreator().getUserGroups().iterator().next().getName()
                .contains(plate.getOwner().getGroups().iterator().next().getName()));
            // sample
            final org.pimslims.model.sample.Sample sample =
                DAOUtils.getSampleByPosition(holder, new WellPosition(1, 1));
            assertEquals(sampleName, sample.getOutputSample().getExperiment().getInputSamples().iterator()
                .next().getSample().getName());
        }

        finally {
            // Not testing persistence
            this.dataStorage.abort();
        }
    }

    public void testFillTrialPlate() throws BusinessException, ConstraintException {
        UNIQUE = "test" + System.currentTimeMillis();
        // Connect as administrator
        this.dataStorage.openResources("administrator");

        try {
            PlateExperimentInfo ei = new PlateExperimentInfo();
            ei.setRunAt(Calendar.getInstance());
            ei.setProtocolName("Fill Reservoir");

            WellPosition well = new WellPosition("B03.1");
            Screen screen = new Screen(); // or could use optimisation screen generator
            screen.setName(UNIQUE + "s");
            Map<WellPosition, Condition> cps = new HashMap();
            Condition condition = new Condition();
            condition.setLocalName(UNIQUE + "r");
            cps.put(well, condition);
            screen.setConditionPositions(cps);
            ScreenServiceImpl ss = (ScreenServiceImpl) this.dataStorage.getScreenService();
            ss.create(screen);
            DeepWellPlate deepWellPlate = ss.createDeepWellPlate(screen, UNIQUE + "dwp");
            VolumeMap volumeMap = new VolumeMap("L");
            volumeMap.setVolume(well, 0.005d);

            // Create a PlateType
            final PlateType plateType = createDummyPlateType("PlateType" + UNIQUE);
            // coud plateType.setSubPositions(subpositions);
            final TrialPlate plate = new TrialPlate(plateType);
            plate.setBarcode(UNIQUE);
            plate.setCreateDate(Calendar.getInstance());
            // could plate.buildAllTrialDrops();

            final WritableVersion version = getWritableVersion();
            Person person = new Person();
            person.setUsername(new User(version, UNIQUE).getName());
            ei.setOperator(person);
            final TrialService service = this.dataStorage.getTrialService();

            ((TrialServiceImpl) service).fillTrialPlate(plate, deepWellPlate, volumeMap, ei);

            // Test result is as expected
            TrialPlate plate2 = service.findTrialPlate(plate.getBarcode());
            assertNotNull(plate2);
            assertNotNull(plate2.getScreen());
            assertEquals(screen.getName(), plate2.getScreen().getName());

            TrialDrop drop = service.findTrialDrop(plate2.getBarcode(), well);
            assertNotNull(drop);
            assertNotNull(drop.getReservoir());
            assertNotNull(drop.getReservoir().getCondition());
            assertEquals(condition.getLocalName(), drop.getReservoir().getCondition().getLocalName());

        }

        finally {
            // Not testing persistence
            this.dataStorage.abort();
        }
    }

    public void testFillTrialPlateFromScreen() throws BusinessException, ConstraintException {
        UNIQUE = "test" + System.currentTimeMillis();
        // Connect as administrator
        this.dataStorage.openResources("administrator");

        try {
            PlateExperimentInfo ei = new PlateExperimentInfo();
            ei.setRunAt(Calendar.getInstance());
            ei.setProtocolName("Fill Reservoir");

            // Create a PlateType
            final PlateType plateType = createDummyPlateType("PlateType" + UNIQUE);

            // Check all four corners and somewhere in the middle
            WellPosition wellTopLeft_1 = new WellPosition(1, 1, 1);
            WellPosition wellTopRight_1 = new WellPosition(1, plateType.getColumns(), 1);
            WellPosition wellBottomLeft_1 = new WellPosition(plateType.getRows(), 1, 1);
            WellPosition wellBottomRight_1 = new WellPosition(plateType.getRows(), plateType.getColumns(), 1);
            WellPosition wellB03_1 = new WellPosition("B03.1");

            Screen screen = new Screen(); // or could use optimisation screen generator
            screen.setName(UNIQUE + "s");
            Map<WellPosition, Condition> cps = new HashMap<WellPosition, Condition>();

            Condition conditionTopLeft = new Condition();
            conditionTopLeft.setLocalName(UNIQUE + ":conditionTopLeft.1");
            cps.put(wellTopLeft_1, conditionTopLeft);

            Condition conditionTopRight = new Condition();
            conditionTopRight.setLocalName(UNIQUE + ":conditionTopRight.1");
            cps.put(wellTopRight_1, conditionTopRight);

            Condition conditionBottomLeft = new Condition();
            conditionBottomLeft.setLocalName(UNIQUE + ":conditionBottomLeft.1");
            cps.put(wellBottomLeft_1, conditionBottomLeft);

            Condition conditionBottomRight = new Condition();
            conditionBottomRight.setLocalName(UNIQUE + ":conditionBottomRight.1");
            cps.put(wellBottomRight_1, conditionBottomRight);

            Condition condition = new Condition();
            condition.setLocalName(UNIQUE + ":conditionB03.1");
            cps.put(wellB03_1, condition);

            screen.setConditionPositions(cps);

            ScreenServiceImpl ss = (ScreenServiceImpl) this.dataStorage.getScreenService();
            ss.create(screen);
            //DeepWellPlate deepWellPlate = ss.createDeepWellPlate(screen, UNIQUE + "dwp");
            VolumeMap volumeMap = new VolumeMap("L");
            volumeMap.setVolume(wellTopLeft_1, 0.005d);
            volumeMap.setVolume(wellTopRight_1, 0.005d);
            volumeMap.setVolume(wellBottomLeft_1, 0.005d);
            volumeMap.setVolume(wellBottomRight_1, 0.005d);
            volumeMap.setVolume(wellB03_1, 0.005d);

            // coud plateType.setSubPositions(subpositions);
            final TrialPlate plate = new TrialPlate(plateType);
            plate.setBarcode(UNIQUE);
            plate.setCreateDate(Calendar.getInstance());
            // could plate.buildAllTrialDrops();

            final WritableVersion version = getWritableVersion();
            Person person = new Person();
            person.setUsername(new User(version, UNIQUE).getName());
            ei.setOperator(person);
            final TrialService service = this.dataStorage.getTrialService();

            ((TrialServiceImpl) service).fillTrialPlate(plate, screen, volumeMap, ei, null);

            // Test result is as expected
            TrialPlate plate2 = service.findTrialPlate(plate.getBarcode());
            assertNotNull(plate2);
            assertNotNull(plate2.getScreen());
            assertEquals(screen.getName(), plate2.getScreen().getName());

            for (Map.Entry<WellPosition, Condition> entry : cps.entrySet()) {
                TrialDrop drop = service.findTrialDrop(plate2.getBarcode(), entry.getKey());
                assertNotNull(drop);
                assertNotNull(drop.getReservoir());
                assertNotNull(drop.getReservoir().getCondition());
                assertEquals(entry.getValue().getLocalName(), drop.getReservoir().getCondition()
                    .getLocalName());
            }

            Holder h = version.findFirst(Holder.class, AbstractHolder.PROP_NAME, plate2.getBarcode());
            assertNotNull(h);
            assertNotNull(h.getRefHolderOffsets());
            assertEquals(h.getRefHolderOffsets().size(), 1);

            RefHolderOffset rho = h.getRefHolderOffsets().iterator().next();
            assertEquals(rho.getRowOffset(), new Integer(0));
            assertEquals(rho.getColOffset(), new Integer(0));
            assertEquals(rho.getSubOffset(), new Integer(plateType.getReservoir() - 1));

        }

        finally {
            // Not testing persistence
            this.dataStorage.abort();
        }
    }

    /**
     * Test of create TrialPlate with more details
     */
    public void _testFullMappingWith4Subpositions() throws Exception {
        UNIQUE = "test" + System.currentTimeMillis();
        // Connect as administrator
        this.dataStorage.openResources("administrator");

        try {

            // Get a WritableVersion
            final WritableVersion wv = getWritableVersion();

            // Get a xtal filled TrialPlate
            final TrialPlate plate = createFilledTrialPlate(wv, UNIQUE, 4);

            // Create PiMS TrialPlate
            final TrialService service = this.dataStorage.getTrialService();
            service.saveTrialPlate(plate);

            // Verify PiMS TrialPlate

            assertNotNull(plate.getId());
            final Holder holder = wv.get(plate.getId());
            assertNotNull(holder);
            assertEquals("PlateType" + UNIQUE, holder.getHolderType().getName());
            assertEquals(2, ((CrystalType) holder.getHolderType()).getResSubPosition().intValue());
            // size
            final int numberOfWells = 4 * plate.getPlateType().getColumns() * plate.getPlateType().getRows();
            assertEquals(numberOfWells, holder.getSamples().size());
            // date
            assertEquals(plate.getCreateDate(), holder.getStartDate());
            assertEquals(plate.getDestroyDate(), holder.getEndDate());
            // description
            assertEquals(plate.getDescription(), holder.getDetails());
            // screen
            assertEquals(plate.getScreen().getName(), holder.getRefHolderOffsets().iterator().next()
                .getRefHolder().getName());
            // location
            assertEquals(plate.getLocation().getName(), DAOUtils.getCurrentLocation(holder).getName());

            final Experiment exp = holder.getSamples().iterator().next().getOutputSample().getExperiment();
            // construct
            assertEquals(plate.getConstruct().getName(), exp.getResearchObjective().getName());
            // operator and owner
            assertEquals(plate.getOperator().getUsername(), exp.getOperator().getName());
            assertEquals(plate.getOwner().getUsername(), exp.getCreator().getName());
            // group
            assertTrue(exp.getCreator().getUserGroups().iterator().next().getName()
                .contains(plate.getOwner().getGroups().iterator().next().getName()));
            // sample
            final org.pimslims.model.sample.Sample sample =
                DAOUtils.getSampleByPosition(holder, new WellPosition(1, 1));

            assertEquals(sampleName, sample.getOutputSample().getExperiment().getInputSamples().iterator()
                .next().getSample().getName());
        } finally {
            // Not testing persistence
            this.dataStorage.abort();
        }
    }

    public void testFindView() throws BusinessException, ConstraintException {
        UNIQUE = "ts" + System.currentTimeMillis();
        // Connect as administrator
        this.dataStorage.openResources("administrator");

        try {

            final TrialPlate plate = createTrialPlate();

            // find view
            final PlateExperimentService plateService = this.dataStorage.getPlateExperimentService();
            final Collection<PlateExperimentView> plateviews =
                plateService.findByPlateBarcode(plate.getBarcode());
            assertEquals(1, plateviews.size());
            final PlateExperimentView plateview = plateviews.iterator().next();
            // verify view
            assertEquals(plate.getBarcode(), plateview.getBarcode());
            assertEquals(plate.getConstruct().getName(), plateview.getConstructName());
            assertEquals(plate.getDescription(), plateview.getDescription());
            assertTrue(plateview.getGroup()
                .contains(plate.getOwner().getGroups().iterator().next().getName()));

            assertEquals(plate.getOwner().getUsername(), plateview.getOwner());
            assertEquals(plate.getPlateType().getName(), plateview.getPlateType());
            assertEquals(plate.getOperator().getUsername(), plateview.getRunBy());

            assertEquals(sampleName, plateview.getSampleName());
            assertEquals(plate.getScreen().getName(), plateview.getScreen());
            assertEquals(plate.getCreateDate(), plateview.getCreateDate());
            assertEquals(plate.getDestroyDate(), plateview.getDestroyDate());
            assertEquals("destroyed", plateview.getStatus());

        } finally {
            // Not testing persistence
            this.dataStorage.abort();

        }

    }

    public void TODOtestImageFindByWell() throws BusinessException, ConstraintException {
        UNIQUE = "ts" + System.currentTimeMillis();
        // Connect as administrator
        this.dataStorage.openResources("administrator");

        try {

            final TrialPlate plate = createTrialPlate();
            final TrialService trialService = dataStorage.getTrialService();
            final BusinessCriteria criteria = new BusinessCriteria(trialService);
            // TODO create an image
            criteria.add(BusinessExpression.Equals(TrialDropView.PROP_BARCODE, UNIQUE, true));
            criteria.add(BusinessExpression.Equals(TrialDropView.PROP_WELL, "B01.1", true));
            final Collection<TrialDropView> trialDropViews = trialService.findViews(criteria);
            assertNotNull(trialDropViews);
            assertEquals(1, trialDropViews.size());
            TrialDropView drop = trialDropViews.iterator().next();
            assertEquals(plate.getBarcode(), drop.getBarcode());
            assertEquals("B01.1", drop.getWell());

        } finally {
            // Not testing persistence
            this.dataStorage.abort();

        }

    }

    public void testUpdate() throws BusinessException, ConstraintException {
        UNIQUE = "test" + System.currentTimeMillis();
        // Connect as administrator
        this.dataStorage.openResources("administrator");
        final WritableVersion wv = getWritableVersion();
        try {
            final TrialPlate plate = createTrialPlate();
            String oldScreen = plate.getScreen().getName();
            final Calendar destroyDate = Calendar.getInstance();
            final String description = "destroried on " + destroyDate;
            // update plate
            plate.setDestroyDate(destroyDate);
            plate.setDescription(description);
            plate.setBarcode("barcode" + System.currentTimeMillis());

            // update plate screen
            plate.setScreen(createScreen(wv, plate.getBarcode()));
            // update creator and operator
            final Person creator = plate.getOwner();
            final Person operator = plate.getOperator();
            plate.setOperator(creator);
            plate.setOwner(operator);
            // update
            final TrialService trialService = this.dataStorage.getTrialService();
            trialService.updateTrialPlate(plate);

            this.dataStorage.flush();
            // find view
            final PlateExperimentService plateService = this.dataStorage.getPlateExperimentService();
            final Collection<PlateExperimentView> plateviews =
                plateService.findByPlateBarcode(plate.getBarcode());
            assertEquals(1, plateviews.size());
            final PlateExperimentView plateview = plateviews.iterator().next();
            // verify view
            assertEquals(plate.getBarcode(), plateview.getBarcode());
            assertEquals(plate.getConstruct().getName(), plateview.getConstructName());
            assertEquals(plate.getDescription(), plateview.getDescription());

            assertEquals(plate.getOperator().getUsername(), plateview.getRunBy());
            // can no longer change createor
            // assertEquals(plate.getOwner().getUsername(),
            // plateview.getOwner());
            assertEquals(plate.getPlateType().getName(), plateview.getPlateType());

            assertEquals(sampleName, plateview.getSampleName());
            assertEquals(plate.getScreen().getName(), plateview.getScreen());
            assertFalse(oldScreen.equals(plateview.getScreen()));
            assertEquals(plate.getCreateDate(), plateview.getCreateDate());
            assertEquals(plate.getDestroyDate(), plateview.getDestroyDate());
            assertEquals("destroyed", plateview.getStatus());
            assertFalse(plateview.isAdditiveScreen());

        } finally {
            // Not testing persistence
            this.dataStorage.abort();

        }
    }

    private TrialPlate createTrialPlate() throws BusinessException, ConstraintException {
        // Get a WritableVersion
        final WritableVersion wv = getWritableVersion();
        // Get a xtal filled TrialPlate
        final TrialPlate plate = createFilledTrialPlate(wv, UNIQUE);
        // Create PiMS TrialPlate
        final TrialService service = this.dataStorage.getTrialService();
        service.saveTrialPlate(plate);
        wv.flush();
        return plate;
    }

    public void testCreateTrialPlatePerformance() throws BusinessException, ConstraintException {
        // Get a WritableVersion
        this.dataStorage.openResources("administrator");
        final WritableVersion wv = getWritableVersion();
        try {
            final TrialService service = this.dataStorage.getTrialService();

            long start = System.currentTimeMillis();
            // Get a xtal filled TrialPlate
            final TrialPlate plate = createFilledTrialPlate(wv, UNIQUE);
            // Create PiMS TrialPlate
            service.saveTrialPlate(plate);
            wv.flush();
            long time = System.currentTimeMillis() - start;
            // TODO reduce this time
            assertTrue("Too long to create plate: " + time, time < 4000);
            System.out.println("Create trial plate took: " + time + "ms");

        } finally {
            // Not testing persistence
            this.dataStorage.abort();

        }
    }

    /**
     * Create a PlateType to use for testing
     * 
     * @return
     * @throws ConstraintException
     */
    private PlateType createDummyPlateType(final String name) {

        final PlateType plateType = new PlateType();
        plateType.setName(name);
        plateType.setRows(8);
        plateType.setColumns(12);
        plateType.setSubPositions(2);
        plateType.setReservoir(2);

        return plateType;

    }

    /**
     * Create and fill a TrialPlate with 1 drop subposition = 96 trialDrops
     * 
     * @param wv
     * @return
     * @throws BusinessException
     * @throws ConstraintException
     */
    public TrialPlate createFilledTrialPlate(final WritableVersion wv, final String barcode)
        throws BusinessException, ConstraintException {

        return createFilledTrialPlate(wv, barcode, 2);
    }

    /**
     * Create and fill a TrialPlate with n subposition or 96* trialDrops
     * 
     * @param wv
     * @return
     * @throws BusinessException
     * @throws ConstraintException
     */
    public TrialPlate createFilledTrialPlate(final WritableVersion wv, final String barcode,
        final int subpositions) throws BusinessException, ConstraintException {

        // Require at least two subpositions, one for the screen
        if (subpositions < 2) {
            throw new IllegalArgumentException(
                "Must be at least two subpositions, one for drop, one for reservoir");
        }
        final Screen screen = createScreen(wv, barcode);
        final Person user1 = super.createXPerson();
        final Person user2 = super.createXGroup().getUsers().iterator().next();

        // Create a PlateType
        final PlateType plateType = createDummyPlateType("PlateType" + barcode);
        plateType.setSubPositions(subpositions);
        plateType.setReservoir(subpositions);
        // create user
        // Create xtal TrialPlate
        final TrialService service = this.dataStorage.getTrialService();
        final TrialPlate plate = new TrialPlate(plateType);
        plate.setBarcode(barcode);
        plate.setCreateDate(Calendar.getInstance());
        plate.buildAllTrialDrops();
        // set sample
        final Sample sample = new Sample(sampleName);
        final SampleQuantity sampleQuantity = new SampleQuantity(sample, 0.0000001, "L");

        for (final TrialDrop trialDrop : plate.getTrialDrops()) {

            trialDrop.addSample(sampleQuantity);
        }
        plate.setDescription("description for " + barcode);
        plate.setDestroyDate(Calendar.getInstance());

        plate.setScreen(screen);
        final Location location = super.createLocation();
        plate.setLocation(location);
        plate.setOperator(user1);
        plate.setOwner(user2);
        plate.setGroup(user2.getGroups().iterator().next());
        final Construct construct = super.createXConstruct();
        plate.setConstruct(construct);
        LOGGER.debug("============ Created TrialPlate ============");

        return plate;

    }

    private Screen createScreen(final WritableVersion wv, final String barcode) throws BusinessException {
        // Create a Screen
        final Screen screen = new Screen();
        screen.setManufacturerName("OPPF");
        screen.setName("Screen" + barcode);
        screen.setScreenType(ScreenType.Matrix);

        // Create a Component
        final Component co = new Component();
        co.setChemicalName("Component" + barcode);

        // Wrap it in a ComponentQuantity at 2M
        final ComponentQuantity coq = new ComponentQuantity();
        coq.setComponent(co);
        coq.setQuantity(2d);
        coq.setUnits("M");

        // Create a Condition and add the ComponentQuantity
        final Condition c = new Condition();
        c.setLocalName("Condition" + barcode);
        c.addComponent(coq);

        // Add the Condition to the Screen
        final Map<WellPosition, Condition> cs = new HashMap<WellPosition, Condition>();
        cs.put(new WellPosition("A01"), c);
        screen.setConditionPositions(cs);

        // Store the Screen
        final ScreenDAO screenDAO = new ScreenDAO(wv);
        screenDAO.createPO(screen);
        LOGGER.debug("============ Created Screen ============");

        for (final Map.Entry<WellPosition, Condition> e : screen.getConditionPositions().entrySet()) {
            final String info = e.getValue().getLocalName();
            LOGGER.debug(e.getKey() + ": " + info);
        }
        LOGGER.debug("============ Checked Screen ============");
        return screen;
    }

}
