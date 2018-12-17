/**
 * xtalPiMSApi org.pimslims.business.crystallization.service ImageSaveAndFindTest.java
 * 
 * @author cm65
 * @date 26 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.business.crystallization.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Location;
import org.pimslims.business.core.service.LocationService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.view.ImageView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.AbstractXtalTest;

/**
 * ImageSaveAndFindTest
 * 
 */
public class ImageSaveAndFindTest extends AbstractXtalTest {

    protected static final String URL = "http://imager/" + UNIQUE + "/";

    /**
     * Constructor for ImageSaveAndFindTest
     * 
     * @param methodName
     * @param dataStorage
     */
    public ImageSaveAndFindTest(String methodName, DataStorage dataStorage) {
        super(methodName, dataStorage);
    }

    public void TODOtestFindAll() throws BusinessException {
        try {

            this.dataStorage.openResources("administrator");
            final ImageService imageService = dataStorage.getImageService();

            final BusinessCriteria criteria = new BusinessCriteria(imageService);

            //TODO prepareSearchCriteria( barcode, well, inspectionName, instrument, temperature, type, screen,                 description, criteria);

            criteria.setMaxResults(1);
            criteria.setFirstResult(0);

            final Collection<ImageView> images = imageService.findViews(criteria);
            assertNotNull(images);
            if (0 < images.size()) {
                assertEquals(1, images.size());
                final Iterator<ImageView> it = images.iterator();
                final ImageView image = it.next();
                assertNotNull(image);
            }

        } finally {
            this.dataStorage.closeResources();
        }
    }

    public void testSaveAndLink() throws BusinessException {

        try {
            this.dataStorage.openResources("administrator");

            Image img = null;
            TrialPlate trialPlate = this.createTrialPlate(BARCODE);
            img = createImage(trialPlate, createImager());
            this.dataStorage.flush();
            assertNotNull(img.getLocation());
            img.setImageType(null); // so will get default image type for instrument
            // the alternative is to make a new image type
            // and a matching PiMS image type with a url

            Collection<Image> images = new ArrayList<Image>();
            images.add(img);
            ImagerService imagerService = dataStorage.getImagerService();
            imagerService.createAndLink(images);

            checkImage(this.dataStorage, img);

        } finally {
            dataStorage.closeResources();
        }
    }

    public void testSaveAndLinkWithFullPath() throws BusinessException {

        try {
            this.dataStorage.openResources("administrator");

            Image img = null;
            TrialPlate trialPlate = this.createTrialPlate(BARCODE + "b");
            img = createImage(trialPlate, createImager());
            this.dataStorage.flush();
            img.setImageType(null); // so will get default image type for instrument
            // the alternative is to make a new image type
            // and a matching PiMS image type with a url
            img.setImagePath(URL + img.getImagePath());

            Collection<Image> images = new ArrayList<Image>();
            images.add(img);
            ImagerService imagerService = dataStorage.getImagerService();
            imagerService.createAndLink(images);

            checkImage(this.dataStorage, img);

        } finally {
            dataStorage.closeResources();
        }
    }

    // PiMSDBImpl overrides this, since Location does not fully model a PiMS Instrument
    protected Location createImager() throws BusinessException {

        LocationService imagerService = dataStorage.getLocationService();
        Location imager = new Location();
        imager.setName(UNIQUE);
        imagerService.create(imager);
        return imager;
    }

    private int imageNo = 0;

    protected Image createImage(TrialPlate trialPlate, Location imager) throws BusinessException {
        TrialService dropService = dataStorage.getTrialService();
        //LocationService locationService = dataStorage.getLocationService();
        PlateInspectionService inspectionService = dataStorage.getPlateInspectionService();
        PlateInspection inspection = this.getPlateInspection(trialPlate);
        inspection.setLocation(imager);
        inspectionService.create(inspection);
        TrialDrop drop = trialPlate.getTrialDrop("C04.2");

        Image image = new Image();
        image.setDrop(drop);
        image.setImagePath("" + (imageNo++) + "/" + trialPlate.getBarcode() + "/C4.2.bmp");
        //was image.setImageType(org.pimslims.business.crystallization.model.ImageType.COMPOSITE);
        // but now the PiMS ImageType must contain a URL
        image.setLocation(imager);

        image.setPlateInspection(inspection);
        inspection.setImages(Collections.singleton(image));

        //img.setSizeX(Math.round(uploadImage[i].getImage().getWidth()));
        //img.setSizeY(Math.round(uploadImage[i].getImage().getHeight()));
        //img.setXLengthPerPixel(uploadImage[i].getPixel().getWidth());
        //img.setYLengthPerPixel(uploadImage[i].getPixel().getHeight());
        //img.setImageDate(uploadImage[i].getDateImaged());
        //img.setColourDepth(uploadImage[i].getColourDepth());

        return image;
    }

    public static void checkImage(DataStorage dataStorage, Image img) throws BusinessException {

        String barcode = img.getPlateInspection().getPlate().getBarcode();
        String well = img.getDrop().getWellPosition().toString();
        String inspection = img.getPlateInspection().getInspectionName();

        // now check we can find it by bar code
        final ImageService imageService = dataStorage.getImageService();
        BusinessCriteria criteria = new BusinessCriteria(imageService);
        criteria.add(BusinessExpression.Equals(ImageView.PROP_BARCODE, barcode, true));
        Collection<ImageView> views = imageService.findViews(criteria);
        assertNotNull(views);
        assertEquals(1, views.size());
        ImageView view = views.iterator().next();
        assertNotNull(view);
        assertEquals(barcode, view.getBarcode());
        assertEquals(inspection, view.getInspectionName());
        assertEquals(well, view.getWell());
        assertTrue(
        		view.getUrl().endsWith(".gif") ||
        		view.getUrl().endsWith(".png") ||
        		view.getUrl().endsWith(".bmp") ||
        		view.getUrl().endsWith(".jpg") ||
        		view.getUrl().endsWith(".jpeg") 
        );
        //TODO must set url in imagetype assertTrue(view.getUrl().startsWith("http")); 
        //TODO assertEquals(img.getImageType(), view.getImageType());

        // now check we can find it by inspection name
        checkFindByName(barcode, well, inspection, imageService);
        //TODO assertEquals(img.getImageType(), view.getImageType());
    }

    private static void checkFindByName(String barcode, String well, String inspection,
        final ImageService imageService) throws BusinessException {
        BusinessCriteria criteria;
        Collection<ImageView> views;
        ImageView view;
        criteria = new BusinessCriteria(imageService);
        criteria.add(BusinessExpression.Equals(ImageView.PROP_INSPECTION_NAME, inspection, true));
        views = imageService.findViews(criteria);
        assertNotNull(views);
        assertEquals(1, views.size());
        view = views.iterator().next();
        assertNotNull(view);
        assertEquals(barcode, view.getBarcode());
        assertEquals(inspection, view.getInspectionName());
        assertEquals(well, view.getWell());
    }

    /* TODO test searching by the other criteria listed here
     * - they are offered in the UI
    private void prepareSearchCriteria( final String well, 
        final String instrument, final String temperature, final String type, final String screen,
        final String description, final BusinessCriteria criteria) throws BusinessException {
        
        if ((well != null) && (!well.equals(""))) {
            criteria.add(BusinessExpression.Equals(ImageView.PROP_WELL, well, true));
        }
        if ((instrument != null) && (!instrument.equals(""))) {
            criteria.add(BusinessExpression.Like(ImageView.PROP_INSTRUMENT, instrument, true, true));
        }
        if ((description != null) && (!description.equals(""))) {
            criteria.add(BusinessExpression.Like(ImageView.PROP_DESCRIPTION, description, true, true));
        }
        if ((type != null) && (!type.equals(""))) {
            criteria.add(BusinessExpression.Equals(ImageView.PROP_TYPE, type, true));
        }
        if ((temperature != null) && (!temperature.equals(""))) {
            criteria.add(BusinessExpression.Equals(ImageView.PROP_TEMPERATURE, temperature, true));
        }
        if ((screen != null) && (!screen.equals(""))) {
            criteria.add(BusinessExpression.Equals(ImageView.PROP_SCREEN, screen, true));
        }
    } */

}
