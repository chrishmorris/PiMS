/**
 * command ImporterTest.java
 * 
 * @author cm65
 * @date 16 May 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.formulatrix.command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
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
import org.pimslims.business.crystallization.model.Imager;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.SampleQuantity;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.ConditionService;
import org.pimslims.business.crystallization.service.ImagerService;
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
import org.pimslims.formulatrix.command.Importer;
import org.pimslims.formulatrix.implementation.ManufacturerDataStorageImpl;
import org.pimslims.formulatrix.implementation.ManufacturerVersion;
import org.pimslims.formulatrix.implementation.PlateInspectionServiceImpl;
import org.pimslims.formulatrix.implementation.RockImagerConnection;
import org.pimslims.formulatrix.implementation.TrialServiceImpl;

/**
 * ImporterTest
 * TODO 99 the job is done when this passes
 */
public class ImporterTest extends AbstractXtalTest {

    private static final Calendar NOW = Calendar.getInstance();

    private final DataStorage pims = this.dataStorage;

    private final ManufacturerDataStorageImpl rm;
    private final RockImagerConnection ri;
    
    public static final String pathToTestImage="//SQLPLAYGROUND/RockMakerStorage/WellImages/209/plateID_209/batchID_11/wellNum_1/profileID_1/d1_r498_ef.jpg";
    
    /**
     * Constructor for ImporterTest
     * 
     * @param methodName
     * @param dataStorage
     */
    public ImporterTest(String methodName) {
        super(methodName, new DataStorageImpl(ModelImpl.getModel()));
        this.rm = new ManufacturerDataStorageImpl(false, ManufacturerVersion.OULU);
        this.ri = new RockImagerConnection(true);
    }

    @Test
    public void xtestGetLatestUpdateTime() throws BusinessException {
        try {
            this.pims.openResources("administrator");
            PlateInspectionService pis = this.pims.getPlateInspectionService();
            TrialPlate plate = this.createTrialPlate(UNIQUE);
            PlateInspection inspection = getPlateInspection(plate);
            pis.create(inspection);
            this.pims.flush();
            Calendar latestUpdateTime = new Importer(null, null, (DataStorageImpl) pims).getLatestUpdateTime();
            assertEquals(inspection.getInspectionDate().getTimeInMillis() / 1000, latestUpdateTime
                .getTimeInMillis() / 1000);
        } finally {
            this.pims.abort();
        }
    }

    public void testFileCopy() throws IOException {
    	File fxImage=new File(ImporterTest.pathToTestImage);
    	File pimsImage=new File("C:/FormulatrixImages/test/copy_"+ NOW.getTime().getTime() +".jpg");
    	assertTrue(fxImage.exists());
    	assertFalse(pimsImage.exists());
    	FileUtils.copyFile(fxImage, pimsImage);
    	assertTrue(pimsImage.exists());
    	FileUtils.deleteQuietly(pimsImage);
    	assertFalse(pimsImage.exists());
    }
    
    public void xtestFindNoInspection() throws BusinessException {
        Calendar after = Calendar.getInstance();
        try {
            this.rm.openResources("administrator");
            this.pims.openResources("administrator");
            TrialPlate plate = this.createMfrTrialPlate(UNIQUE);
            PlateInspection inspection = getPlateInspection(plate);
            PlateInspectionService pis = this.rm.getPlateInspectionService();
            pis.create(inspection);
            after.setTimeInMillis(System.currentTimeMillis() + 2000);
            Importer importer = new Importer(this.rm, this.ri, (DataStorageImpl) pims);
			Collection<Integer> found = importer.inspectionsAfter(after);
            assertTrue(found.isEmpty());
        } finally {
            pims.abort();
            rm.abort();
            pims.closeResources();
            rm.closeResources();
        }        

        
        try {
            this.rm.openResources("administrator");
            PlateInspectionService pis = this.rm.getPlateInspectionService();
            ((PlateInspectionServiceImpl) pis).findInspectionIds(after,true);
        } finally {
            if (null != rm) {
                rm.abort();
                rm.closeResources();
            }
        }
    }

/*    
    //MAYBE @Test
    public void testImportScreen() throws BusinessException {

        try {
            this.mfr.openResources("administrator");
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
            this.mfr.getScreenService().create(screen);
            TrialPlate plate = this.createMfrTrialPlate(UNIQUE);
            plate.setScreen(screen);
            //TrialDrop drop = plate.getTrialDrop(new WellPosition("C12.1"));
            this.mfr.getTrialService().updateTrialPlate(plate);
            //MAYBE this.mfr.getTrialService().updateTrialDrop(drop);

            // export it
            Importer importer = new Importer(this.mfr, (DataStorageImpl) this.pims);
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
            if (null != mfr) {
                mfr.abort();
                mfr.closeResources();
            }
        }
    }
*/
    @Test
    public void testImportInspection() throws BusinessException, ConstraintException {

        try {
            this.rm.openResources("administrator");
            this.pims.openResources("administrator");
            Importer importer = new Importer(this.rm, this.ri, (DataStorageImpl) this.pims);

            // make a Formulatrix inspection
            TrialPlate plate = this.createMfrTrialPlate(UNIQUE);
//            LocationService imagerService = mfr.getLocationService();
//            Location imager = new Location();
//            imager.setName(plate.getBarcode() + "im");
//            imagerService.create(imager);
            
            Location imager=pims.getLocationService().findByName(Importer.INSTRUMENT_NAME);
            if(null==imager){ 
            	imager=Importer.createInstrument((DataStorageImpl)pims);
            }
            TrialService dropService = rm.getTrialService();
            //LocationService locationService = dataStorage.getLocationService();
            PlateInspectionService inspectionService = rm.getPlateInspectionService();
            PlateInspection inspection = this.getPlateInspection(plate);
            inspection.setLocation(imager); 
            inspectionService.create(inspection);

            TrialDrop drop = this.getTrialDrop(plate.getBarcode() + "A01.1", "A01.1");
            drop.setPlate(plate);
            plate.addTrialDrop(drop);
            ((TrialServiceImpl) dropService).saveTrialDrop(drop);

            Image image = new Image();
            image.setDrop(drop);
            image.setImageType(org.pimslims.business.crystallization.model.ImageType.COMPOSITE);
            image.setLocation(imager);

           
            image.setPlateInspection(inspection);
            inspection.setImages(Collections.singleton(image));

            Collection<Image> images = new ArrayList<Image>();
            images.add(image);
            rm.getImagerService().createAndLink(images);

        	//Importer ignores inspections on plates that aren't in pims, so put this plate there.
            importer.importPlate(inspection.getPlate().getBarcode());
        	importer.importInspection(image.getPlateInspection().getId());

        	this.pims.flush();

            //MAYBE now use trialService.findViews to find it, and test the URL of the image

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
            //FIXME
            assertEquals("Formulatrix", view.getInstrument());
            assertEquals(importer.pimsUrlPrefix+image.getImagePath(), view.getUrl());

        } finally {
            if (null != pims) {
                pims.abort();
                pims.closeResources();
            }
            if (null != rm) {
                rm.abort();
                rm.closeResources();
            }
        }

    }

    @Test
    public void xtestImportAfter() throws BusinessException {

        try {
            this.rm.openResources("administrator");
            this.pims.openResources("administrator");

            TrialPlate plate = this.createMfrTrialPlate(UNIQUE);
            TrialDrop drop = plate.getTrialDrop(new WellPosition("C12.1"));
            Sample sample = new Sample();
            sample.setName(UNIQUE + "s");
            this.rm.getSampleService().create(sample);
            drop.addSample(new SampleQuantity(sample, 100d * Math.pow(10, -9), "L")); //100nL
            this.rm.getTrialService().updateTrialPlate(plate);
            this.rm.getTrialService().updateTrialDrop(drop);

            PlateInspection inspection = getPlateInspection(plate);
            this.rm.getPlateInspectionService().create(inspection);
            Calendar before = Calendar.getInstance();
            before.setTimeInMillis(System.currentTimeMillis() - 1000);

            // export it

            Importer importer = new Importer(this.rm, this.ri, (DataStorageImpl) this.pims);
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
            if (null != rm) {
                rm.abort();
                rm.closeResources();
            }
        }

    }

    // could @Test
    public void couldtestImportConstruct() throws BusinessException {

        try {
            this.rm.openResources("administrator");
            this.pims.openResources("administrator");

            TrialPlate plate = this.createMfrTrialPlate(UNIQUE);
            Construct construct = new Construct(UNIQUE + "c");
            plate.setConstruct(construct);
            this.rm.getTrialService().updateTrialPlate(plate);

            // export it
            Importer importer = new Importer(this.rm, this.ri, (DataStorageImpl) this.pims);
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
            if (null != rm) {
                rm.abort();
                rm.closeResources();
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
    private TrialPlate createMfrTrialPlate(String barcode) throws BusinessException {

        TrialService trialService = this.rm.getTrialService();
        final PlateType plateType = createPlateType(trialService);
        final TrialPlate plate = trialService.createTrialPlate(barcode, plateType);
        return plate;

    }

    /**
     * Test method for {@link org.pimslims.formulatrix.command.Importer#setup(java.util.Calendar)}.
     */
    //MAYBE @Test
    public void MAYBEtestSetup() {
        fail("Not yet implemented");
    }

}
