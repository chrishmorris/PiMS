package org.pimslims.crystallization.integration;

import java.util.Calendar;
import java.util.Collection;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.service.ScreenService;
import org.pimslims.business.crystallization.view.ScreenView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.AbstractXtalTest;

public class ScreenViewTest extends AbstractXtalTest {

    private static final Calendar NOW = Calendar.getInstance();

    public ScreenViewTest(String methodName, DataStorage dataStorage) {
        super(methodName, dataStorage);

    }

    public void testScreenViewFind() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {
            //create a trialPlate with 2 trialDrop
            Screen screen = createScreen();

            //find Views
            ScreenService service = this.dataStorage.getScreenService();
            BusinessCriteria criteria = new BusinessCriteria(service);
            criteria.add(BusinessExpression.Equals(ScreenView.PROP_NAME, screen.getName(), true));
            Collection<ScreenView> views = service.findViews(criteria);

            //check views
            assertEquals(1, views.size());
            ScreenView view = views.iterator().next();
            assertEquals(screen.getName(), view.getName());
            assertEquals(screen.getManufacturerName(), view.getManufacturerName());
            assertEquals(screen.getScreenType().toString(), view.getScreenType());
            //check count
            assertEquals(1, service.findViewCount(criteria).intValue());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

}
