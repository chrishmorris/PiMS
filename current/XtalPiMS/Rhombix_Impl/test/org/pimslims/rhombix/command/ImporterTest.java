/**
 * Rhombix_Impl org.pimslims.rhombix.command ImporterTest.java
 * 
 * @author cm65
 * @date 16 May 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.command;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Location;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.core.service.LocationService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.Component;
import org.pimslims.business.crystallization.model.ComponentQuantity;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.SampleQuantity;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.ConditionService;
import org.pimslims.business.crystallization.service.PlateInspectionService;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.ConditionView;
import org.pimslims.business.crystallization.view.ImageView;
import org.pimslims.business.crystallization.view.TrialDropView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.AbstractXtalTest;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.ModelImpl;
import org.pimslims.exception.ConstraintException;
import org.pimslims.rhombix.implementation.RhombixDataStorageImpl;
import org.pimslims.rhombix.implementation.RhombixVersion;
import org.pimslims.rhombix.implementation.TrialServiceImpl;

/**
 * ImporterTest
 * 
 */
public class ImporterTest extends AbstractXtalTest {

    private static final Calendar NOW = Calendar.getInstance();

    private final DataStorage pims = this.dataStorage;

    private final RhombixDataStorageImpl rhombix;

    /**
     * Constructor for ImporterTest
     * 
     * @param methodName
     * @param dataStorage
     */
    public ImporterTest(String methodName) {
        super(methodName, new DataStorageImpl(ModelImpl.getModel()));
        this.rhombix = new RhombixDataStorageImpl(false, RhombixVersion.HELSINKI);
    }

    @Test
    public void testGetLatestUpdateTime() throws BusinessException {
        try {
            this.pims.openResources("administrator");
            PlateInspectionService pis = this.pims.getPlateInspectionService();
            TrialPlate plate = this.createTrialPlate(UNIQUE);
            PlateInspection inspection = getPlateInspection(plate);
            pis.create(inspection);
            this.pims.flush();
            Calendar latestUpdateTime = new Importer(null, (DataStorageImpl) pims).getLatestUpdateTime();
            assertEquals(inspection.getInspectionDate().getTimeInMillis() / 1000,
                latestUpdateTime.getTimeInMillis() / 1000);
        } finally {
            this.pims.abort();
        }
    }

    public void testFindNoInspection() throws BusinessException {
        try {
            this.rhombix.openResources("administrator");
            this.pims.openResources("administrator");
            TrialPlate plate = this.createRhombixTrialPlate(UNIQUE);
            PlateInspection inspection = getPlateInspection(plate);
            PlateInspectionService pis = this.rhombix.getPlateInspectionService();
            pis.create(inspection);
            Calendar after = Calendar.getInstance();
            after.setTimeInMillis(System.currentTimeMillis() + 2000);
            Collection<Long> found =
                new Importer(this.rhombix, (DataStorageImpl) pims).inspectionsAfter(after);
            assertTrue(found.isEmpty());
        } finally { //TODO this looks wrong, must close both
            if (null != dataStorage) {
                dataStorage.abort();
                dataStorage.closeResources();
            }
        }
    }

    @Test
    public void testImportScreen() throws BusinessException {

        try {
            this.rhombix.openResources("administrator");
            this.pims.openResources("administrator");

            // set up
            Screen screen = new Screen();
            screen.setName(UNIQUE + "screen");
            Map<WellPosition, Condition> conditions = new HashMap();
            Condition condition = new Condition();
            condition.setLocalName("cond" + UNIQUE);
            ComponentQuantity quantity = new ComponentQuantity();
            Component component = new Component();
            component.setChemicalName("PEG4000");
            quantity.setComponent(component);
            quantity.setDisplyUnit("M");
            quantity.setUnits("M");
            quantity.setQuantity(1.0d);
            condition.addComponent(quantity);
            conditions.put(new WellPosition("E11"), condition);
            screen.setConditionPositions(conditions);
            this.rhombix.getScreenService().create(screen);
            TrialPlate plate = this.createRhombixTrialPlate(UNIQUE);
            plate.setScreen(screen);
            //TrialDrop drop = plate.getTrialDrop(new WellPosition("C12.1"));
            this.rhombix.getTrialService().updateTrialPlate(plate);
            //this.rhombix.getTrialService().updateTrialDrop(drop);

            // export it
            Importer importer = new Importer(this.rhombix, (DataStorageImpl) this.pims);
            importer.importPlate(plate.getBarcode());
            this.pims.flush();

            // check  
            TrialPlate found = this.pims.getTrialService().findTrialPlate(plate.getBarcode());
            assertNotNull(found);
            assertNotNull(found.getScreen());
            assertEquals(screen.getName(), found.getScreen().getName());

            ConditionService pcs = this.pims.getConditionService();
            final BusinessCriteria criteria = new BusinessCriteria(pcs);
            criteria.add(BusinessExpression.Equals(ConditionView.PROP_LOCAL_NAME, screen.getName(), true));
            Collection<ConditionView> views = pcs.findViews(criteria);
            assertEquals(1, views.size());
        } finally {
            if (null != pims) {
                pims.abort();
                pims.closeResources();
            }
            if (null != rhombix) {
                rhombix.abort();
                rhombix.closeResources();
            }
        }
    }

    @Test
    public void testImportInspection() throws BusinessException, ConstraintException {

        try {
            this.rhombix.openResources("administrator");
            this.pims.openResources("administrator");
            Importer importer = new Importer(this.rhombix, (DataStorageImpl) this.pims);

            // make a Rhombix inspection
            TrialPlate plate = this.createRhombixTrialPlate(UNIQUE);
            LocationService locationService = rhombix.getLocationService();
            Location location = new Location();
            location.setName(plate.getBarcode() + "loc");
            locationService.create(location);
            TrialService dropService = rhombix.getTrialService();
            //LocationService locationService = dataStorage.getLocationService();
            PlateInspectionService inspectionService = rhombix.getPlateInspectionService();
            PlateInspection inspection = this.getPlateInspection(plate);
            inspection.setLocation(location); //TODO no, location
            inspectionService.create(inspection);

            TrialDrop drop = this.getTrialDrop(plate.getBarcode() + "A01.1", "A01.1");
            drop.setPlate(plate);
            plate.addTrialDrop(drop);
            ((TrialServiceImpl) dropService).saveTrialDrop(drop);

            Image image = new Image();
            image.setDrop(drop);
            image
                .setImagePath("\\\\cdbmaster\\DCA\\TG-CQ-MG-20\\110629\\TG-CQ-MG-20_A01-1_BRITE_110629_200901_1.jpg");
            image.setImageType(org.pimslims.business.crystallization.model.ImageType.COMPOSITE);
            image.setLocation(location);

            // if the implementation includes locations, test them
            if (null != this.dataStorage.getLocationService()) {
                image.setLocation(this.createLocation(UNIQUE));
            }
            image.setPlateInspection(inspection);
            inspection.setImages(Collections.singleton(image));

            Collection<Image> images = new ArrayList<Image>();
            images.add(image);
            locationService.createImagesAndLink(images);

            // export it
            importer.importInspection(image.getPlateInspection().getId());
            this.pims.flush();

            //TODO now use trialService.findViews to find it, and test the URL of the image

            TrialService pts = this.pims.getTrialService();
            BusinessCriteria criteria = new BusinessCriteria(pts);
            criteria.add(BusinessExpression.Equals(TrialDropView.PROP_BARCODE, plate.getBarcode(), true));
            criteria.add(BusinessExpression.Equals(TrialDropView.PROP_WELL, "A01.1", true));
            Collection<TrialDropView> views = pts.findViews(criteria);
            assertEquals(1, views.size());
            TrialDropView found = views.iterator().next();
            assertEquals(plate.getBarcode(), found.getBarcode());
            assertEquals("A01.1", found.getWell());
            assertEquals(1, found.getImages().size());
            ImageView view = found.getImages().iterator().next();
            //TODO the test below must change if we need to support more than one Rhombix imager
            assertEquals("Rhombix", view.getInstrument());
            assertEquals(
                "http://sci-serv2.diamond.ac.uk:8080/xpims/rhombix/DCA/TG-CQ-MG-20/110629/TG-CQ-MG-20_A01-1_BRITE_110629_200901_1.jpg",
                view.getUrl());

        } finally {
            if (null != pims) {
                pims.abort();
                pims.closeResources();
            }
            if (null != rhombix) {
                rhombix.abort();
                rhombix.closeResources();
            }
        }

    }

    @Test
    public void testImportAfter() throws BusinessException {

        try {
            this.rhombix.openResources("administrator");
            this.pims.openResources("administrator");

            TrialPlate plate = this.createRhombixTrialPlate(UNIQUE);
            TrialDrop drop = plate.getTrialDrop(new WellPosition("C12.1"));
            Sample sample = new Sample();
            sample.setName(UNIQUE + "s");
            this.rhombix.getSampleService().create(sample);
            drop.addSample(new SampleQuantity(sample, 100d * Math.pow(10, -9), "L")); //100nL
            this.rhombix.getTrialService().updateTrialPlate(plate);
            this.rhombix.getTrialService().updateTrialDrop(drop);

            PlateInspection inspection = getPlateInspection(plate);
            this.rhombix.getPlateInspectionService().create(inspection);
            Calendar before = Calendar.getInstance();
            before.setTimeInMillis(System.currentTimeMillis() - 1000);

            // export it

            Importer importer = new Importer(this.rhombix, (DataStorageImpl) this.pims);
            importer.importAfter(before);
            this.pims.flush();

            // check it made well samples
            TrialDrop found =
                this.pims.getTrialService().findTrialDrop(plate.getBarcode(), new WellPosition("C12.1"));
            assertNotNull(found);
            assertFalse(found.getSamples().isEmpty());
            assertEquals(sample.getName(), found.getSamples().iterator().next().getSample().getName());
            assertEquals(plate.getPlateType().getName(), found.getPlate().getPlateType().getName());
        } finally {
            if (null != pims) {
                pims.abort();
                pims.closeResources();
            }
            if (null != rhombix) {
                rhombix.abort();
                rhombix.closeResources();
            }
        }

    }

    // could @Test
    public void couldtestImportConstruct() throws BusinessException {

        try {
            this.rhombix.openResources("administrator");
            this.pims.openResources("administrator");

            TrialPlate plate = this.createRhombixTrialPlate(UNIQUE);
            Construct construct = new Construct(UNIQUE + "c");
            plate.setConstruct(construct);
            this.rhombix.getTrialService().updateTrialPlate(plate);

            // export it
            Importer importer = new Importer(this.rhombix, (DataStorageImpl) this.pims);
            importer.importPlate(plate.getBarcode());
            this.pims.flush();

            // check  
            Construct found = this.pims.getConstructService().findByName(construct.getName());
            assertNotNull(found);
        } finally {
            if (null != pims) {
                pims.abort();
                pims.closeResources();
            }
            if (null != rhombix) {
                rhombix.abort();
                rhombix.closeResources();
            }
        }

    }

    /**
     * ImporterTest.createRhombixTrialPlate
     * 
     * @param unique
     * @return
     * @throws BusinessException
     */
    private TrialPlate createRhombixTrialPlate(String barcode) throws BusinessException {

        TrialService trialService = this.rhombix.getTrialService();
        final PlateType plateType = createPlateType(trialService);
        final TrialPlate plate = trialService.createTrialPlate(barcode, plateType);
        return plate;

    }

    /**
     * Test method for {@link org.pimslims.rhombix.command.Importer#setup(java.util.Calendar)}.
     */
    //TODO @Test
    public void TODOtestSetup() {
        fail("Not yet implemented");
    }

}
