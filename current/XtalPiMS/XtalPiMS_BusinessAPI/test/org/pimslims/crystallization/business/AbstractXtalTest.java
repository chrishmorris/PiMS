package org.pimslims.crystallization.business;

/**
 * 
 * @author Bill
 */
import java.awt.Color;
import java.util.Calendar;
import java.util.Collections;

import junit.framework.TestCase;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Location;
import org.pimslims.business.core.model.Organisation;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.service.ConstructService;
import org.pimslims.business.core.service.GroupService;
import org.pimslims.business.core.service.LocationService;
import org.pimslims.business.core.service.OrganisationService;
import org.pimslims.business.core.service.PersonService;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.ScoreValue;
import org.pimslims.business.crystallization.model.ScoringScheme;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.ScreenType;
import org.pimslims.business.crystallization.model.Software;
import org.pimslims.business.crystallization.model.SoftwareScore;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.UserScore;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.PlateInspectionService;
import org.pimslims.business.crystallization.service.ScoringSchemeService;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.exception.BusinessException;

public abstract class AbstractXtalTest extends TestCase {
    protected final DataStorage dataStorage;

    private static int count = 0;

    // unique string to avoid name clashes
    protected static final String UNIQUE = "_" + System.currentTimeMillis();

    protected static final String BARCODE = "bc" + UNIQUE;

    public AbstractXtalTest(final String methodName, final DataStorage dataStorage) {
        super(methodName);
        this.dataStorage = dataStorage;
    }

    protected void save(final Object xtalObject) throws BusinessException {

        if (xtalObject instanceof TrialDrop) {
            this.dataStorage.getTrialService().saveTrialDrop((TrialDrop) xtalObject);
            return;
        } else if (xtalObject instanceof TrialPlate) {
            this.dataStorage.getTrialService().saveTrialPlate((TrialPlate) xtalObject);
            return;
        } else if (xtalObject instanceof UserScore) {
            this.dataStorage.getHumanScoreService().create((UserScore) xtalObject);
            return;
        } else if (xtalObject instanceof SoftwareScore) {
            this.dataStorage.getSoftwareScoreService().create((SoftwareScore) xtalObject);
            return;
        } /* else if (xtalObject instanceof Image) {
                                                                                                                                   this.dataStorage.getImageService().create((Image) xtalObject);
                                                                                                                                   return;
                                                                                                                               } */else if (xtalObject instanceof PlateInspection) {
            this.dataStorage.getPlateInspectionService().create((PlateInspection) xtalObject);
            return;
        }
        throw new BusinessException("Can not find service for " + xtalObject);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected String getUnique() {
        return (count++) + UNIQUE;
    }

    protected Person createXPerson() throws BusinessException {
        final Person xPerson = new Person();
        xPerson.setUsername("u" + (count++));
        xPerson.setFamilyName("s" + getUnique());
        xPerson.setGivenName("GivenName");
        final PersonService personService = this.dataStorage.getPersonService();
        personService.create(xPerson);
        return xPerson;
    }

    protected Group createXGroup() throws BusinessException {
        final Group group = new Group();
        group.setName("name" + getUnique());
        group.setOrganisation(createXOrganisation());
        final GroupService service = this.dataStorage.getGroupService();
        final Person xPerson1 = createXPerson();
        group.addUser(xPerson1);
        service.create(group);
        return group;
    }

    protected Organisation createXOrganisation() throws BusinessException {
        final Organisation org = new Organisation();
        org.setName("name" + getUnique());
        final OrganisationService service = this.dataStorage.getOrganisationService();
        service.create(org);
        return org;
    }

    protected Construct createXConstruct() throws BusinessException {
        final Construct toCreate = new Construct("name" + getUnique());
        final ConstructService service = this.dataStorage.getConstructService();
        toCreate.setDescription("desc" + getUnique());
        service.create(toCreate);
        return toCreate;
    }

    protected ScoreValue createScoreValue() throws BusinessException {
        final ScoreValue sv = new ScoreValue();
        sv.setValue(count);
        sv.setColour(Color.blue);
        sv.setDescription("Crystals");

        final ScoringScheme ss = new ScoringScheme();
        ss.addScore(sv);
        sv.setScoringScheme(ss);
        ss.setName("test ScoringScheme" + getUnique());
        ss.setDescription("description of ScoringScheme" + getUnique());
        final ScoringSchemeService service = this.dataStorage.getScoringSchemeService();
        service.create(ss);

        return sv;
    }

    protected PlateType createPlateType(TrialService trialService) {
        final PlateType plateType = new PlateType();
        plateType.setName("plateType" + getUnique());
        plateType.setRows(8);
        plateType.setColumns(12);
        plateType.setReservoir(4);
        plateType.setSubPositions(3);
        PlateType ret = trialService.create(plateType);
        assertNotNull(ret);
        return ret;
    }

    protected TrialPlate createTrialPlate(final String platebarcode) throws BusinessException {
        TrialService trialService = this.dataStorage.getTrialService();
        final PlateType plateType = createPlateType(trialService);
        final TrialPlate plate = trialService.createTrialPlate(platebarcode, plateType);
        return plate;
    }

    private Software createSoftware() throws BusinessException {
        final Software software = new Software();
        software.setName("software" + getUnique());
        software.setDescription("software description");
        software.setVersion("software version");
        software.setVendorName("software vendorName");
        software.setVendorAddress("software vendorAddress");
        this.dataStorage.getSoftwareService().create(software);
        return software;
    }

    protected Screen createScreen() throws BusinessException {
        final Screen screen = getScreen();
        this.dataStorage.getScreenService().create(screen);
        return screen;
    }

    protected Screen getScreen() {
        final Screen screen = new Screen();
        screen.setName("screenName" + getUnique());
        screen.setManufacturerName("manufacturerName" + getUnique());
        screen.setScreenType(ScreenType.Matrix);
        return screen;
    }

    protected Condition createCondition() throws BusinessException {
        final Condition condition = new Condition();
        condition.setFinalpH(new Float(14.0 / (count + 1.0)));
        condition.setLocalName("localName" + getUnique());
        condition.setLocalNumber(count);
        condition.setVolatileCondition(true);
        condition.setManufacturerName("manufacturerName" + getUnique());
        condition.setManufacturerScreenName("manufacturerScreenName" + getUnique());
        condition.setManufacturerCatalogueCode("ManufacturerCatalogueCode" + getUnique());
        condition.setManufacturerCode("manufacturerCode" + getUnique());
        this.dataStorage.getConditionService().create(condition);
        return condition;
    }

    protected Location createLocation() throws BusinessException {
        final Location location = new Location();
        location.setLocationType("LocationType" + getUnique());
        location.setName("LocationName" + getUnique());
        location.setPressure(1.0 * count);
        location.setTemperature(10.0 * count);
        this.dataStorage.getLocationService().create(location);
        return location;
    }

    protected Location createLocation(final String name) throws BusinessException {
        final Location location = new Location();
        location.setLocationType("LocationType" + getUnique());
        location.setName(name);
        location.setPressure(1.0 * count);
        location.setTemperature(10.0 * count);
        this.dataStorage.getLocationService().create(location);
        return location;
    }

    protected UserScore getUserScore(final Calendar date) throws BusinessException {
        final UserScore us = new UserScore();
        us.setDate(date);
        us.setUser(createXPerson());
        us.setValue(createScoreValue());
        return us;
    }

    protected SoftwareScore getSoftwareScore(final Calendar date) throws BusinessException {
        final SoftwareScore ss = new SoftwareScore();
        ss.setDate(date);
        ss.setSoftwareAnnotator(createSoftware());
        ss.setValue(createScoreValue());
        return ss;
    }

    protected TrialDrop getTrialDrop(final String dropName, final String wellPosition) {
        final TrialDrop well = new TrialDrop();
        well.setName(dropName);
        well.setWellPosition(new WellPosition(wellPosition));
        return well;
    }

    protected Image getImage() throws BusinessException {
        final Image image = new Image();
        image.setImageDate(Calendar.getInstance());
        image.setColourDepth(256);
        image.setImagePath("c:/imagePath/" + getUnique());
        image.setSizeX(128);
        image.setSizeY(1200);
        image.setXLengthPerPixel(0.12);
        image.setXLengthPerPixel(0.2);
        image.setLocation(createLocation());
        return image;
    }

    protected PlateInspection getPlateInspection(final TrialPlate trialPlate) {
        final PlateInspection plateInspection = new PlateInspection();
        plateInspection.setPlate(trialPlate);
        plateInspection.setInspectionDate(Calendar.getInstance());
        plateInspection.setInspectionName(getUnique());
        plateInspection.setInspectionNumber((count++));
        return plateInspection;
    }

    /**
     * AbstractXtalTest.setLogToDebug
     */
    protected void setLogToDebug() {
        return;
    }

    /**
     * AbstractXtalTest.restoreDefaultLoggingLevel
     */
    protected void restoreDefaultLoggingLevel() {
        // implementations of test cases can implement this too if they want
        return;
    }

    /**
     * AbstractXtalTest.setSqlLogToDebug
     */
    protected void setSqlLogToDebug() {
        // implementations of test cases can implement this too if they want
        return;
    }

    /**
     * AbstractXtalTest.printStatistics
     */
    protected void printStatistics() {
        // implementations of test cases can implement this too if they want
        return;
    }

    /**
     * AbstractXtalTest.clearStatistics
     */
    protected void clearStatistics() {
        // implementations of test cases can implement this too if they want
        return;
    }

    }
