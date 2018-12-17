package org.pimslims.crystallization.integration;

import java.util.Calendar;
import java.util.Collection;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.service.ImageService;
import org.pimslims.business.crystallization.view.ImageView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.AbstractXtalTest;

public class ImageViewTest extends AbstractXtalTest {

    private static final Calendar NOW = Calendar.getInstance();

    private static String PLATEBARCODE = "PlateBarcode" + UNIQUE;

    public ImageViewTest(final String methodName, final DataStorage dataStorage) {
        super(methodName, dataStorage);

    }

    //TODO revive this test
    public void xtestImageViewFind() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {
            //create a trialPlate with 2 trialDrop
            final TrialPlate trialPlate = createTrialPlate(PLATEBARCODE);
            final TrialDrop trialDrop1 = getTrialDrop("drop1" + UNIQUE, "A02.1");
            final TrialDrop trialDrop2 = getTrialDrop("drop2" + UNIQUE, "B05.1");
            trialDrop1.setPlate(trialPlate);
            trialDrop2.setPlate(trialPlate);

            final PlateInspection plateInspection1 = getPlateInspection(trialPlate);
            final PlateInspection plateInspection2 = getPlateInspection(trialPlate);
            final Image image = getImage();
            image.setDrop(trialDrop1);
            //TODO inspection name is not available  as we can not map it to pims DB
            image.setPlateInspection(plateInspection1);

            //save beans
            save(trialDrop1);
            save(trialDrop2);
            save(plateInspection1);
            save(plateInspection2);
            save(image);

            //find Views
            final ImageService service = this.dataStorage.getImageService();
            final BusinessCriteria criteria = new BusinessCriteria(service);
            criteria.add(BusinessExpression.Equals(ImageView.PROP_BARCODE, PLATEBARCODE, true));
            final Collection<ImageView> views = service.findViews(criteria);

            //check views
            assertEquals(1, views.size());
            final ImageView view = views.iterator().next();
            assertEquals(PLATEBARCODE, view.getBarcode());
            assertEquals(image.getImageDate(), view.getDate());
            assertEquals(image.getImagePath(), view.getUrl());
            assertEquals(image.getDrop().getWellPosition().toString(), view.getWell());

            //TODO location
            //assertEquals(image.getLocation().getName(), view.getInstrument());
            //assertEquals(image.getLocation().getTemperature(), view.getTemperature());
            //assertEquals(image.getPlateInspection().getInspectionName(), view.getInspectionName());
            //assertEquals("???", view.getTimePoint());
            //view.getCondition()
            //TODO image parameters are not loaded
/*            assertEquals(image.getColourDepth(), view.getColour());
            assertEquals(image.getImageType().name(), view.getType());
            assertEquals(image.getSizeY(), view.getHeight().intValue());
            assertEquals(image.getSizeX(), view.getWidth().intValue());
            assertEquals(image.getYLengthPerPixel(), view.getHeightPerPixel());
            assertEquals(image.getXLengthPerPixel(), view.getWidthPerPixel());*/

            //check count
            assertEquals(1, service.findViewCount(criteria).intValue());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

}
