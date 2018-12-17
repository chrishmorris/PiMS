package org.pimslims.crystallization.integration;

import java.util.Calendar;
import java.util.Collection;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.ConditionService;
import org.pimslims.business.crystallization.service.ScreenService;
import org.pimslims.business.crystallization.view.ConditionView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.AbstractXtalTest;

public class ConditionViewTest extends AbstractXtalTest {

    private static final Calendar NOW = Calendar.getInstance();

    public ConditionViewTest(final String methodName, final DataStorage dataStorage) {
        super(methodName, dataStorage);

    }

    /**
     * this test create condition with no component, search does not work on such situation
     * ConditionViewTest._testConditionViewFind
     * 
     * @throws BusinessException
     */
    public void _testConditionViewFind() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {
            // Instantiate and name a new Screen
            final Screen screen = new Screen();
            screen.setName("localName" + UNIQUE);
            final Condition condition = new Condition();
            condition.setFinalpH(new Float(14.0 / (1.0)));
            condition.setLocalNumber(12);
            condition.setVolatileCondition(true);
            condition.setManufacturerName("manufacturerName" + getUnique());
            condition.setManufacturerScreenName("manufacturerScreenName" + getUnique());
            condition.setManufacturerCatalogueCode("ManufacturerCatalogueCode" + getUnique());
            condition.setManufacturerCode("manufacturerCode" + getUnique());
            condition.setLocalName(screen.getName());
            // Add the Condition to the Screen
            screen.getConditionPositions().put(new WellPosition("H1"), condition);

            final ScreenService screenService = this.dataStorage.getScreenService();
            screenService.create(screen);

            final ConditionService service = this.dataStorage.getConditionService();
            //find Views
            final BusinessCriteria criteria = new BusinessCriteria(service);
            final Collection<ConditionView> anyviews = service.findViews(criteria);//test empty criteria

            criteria.add(BusinessExpression.Equals(ConditionView.PROP_LOCAL_NAME, condition.getLocalName(),
                true));
            final Collection<ConditionView> views = service.findViews(criteria);

            //check views
            //            assertEquals(1, views.size());
            //            final ConditionView view = views.iterator().next();
            //            assertEquals(condition.getLocalName(), view.getLocalName());
            //            assertEquals(condition.getId(), view.getConditionId());
            //            assertEquals(condition.getFinalpH(), view.getFinalpH().floatValue());
            //            assertEquals(condition.getManufacturerCatalogueCode(), view.getManufacturerCatCode());
            //            assertEquals(condition.getManufacturerCode(), view.getManufacturerCode());
            //            assertEquals(condition.getManufacturerScreenName(), view.getManufacturerScreenName());
            //            assertEquals(condition.getManufacturerName(), view.getManufacturerName());
            //TODO Components, other details,...

            //check count
            assertEquals(1, service.findViewCount(criteria).intValue());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

}
