package org.pimslims.crystallization.implementation.integration;

import java.util.Collection;
import java.util.List;

import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.Component;
import org.pimslims.business.crystallization.model.ComponentQuantity;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.ConditionService;
import org.pimslims.business.crystallization.service.ScreenService;
import org.pimslims.business.crystallization.view.ComponentQuantityView;
import org.pimslims.business.crystallization.view.ConditionView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageFactory;

public class ConditionViewTest extends org.pimslims.crystallization.integration.ConditionViewTest {
    public ConditionViewTest(final String methodName) {
        super(methodName, DataStorageFactory.getDataStorageFactory().getDataStorage());

    }

    public void testConditionViewFindWith1Component() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {
            // Instantiate and name a new Screen
            final Screen screen = new Screen();
            screen.setName("localName1234");
            final Condition condition = new Condition();
            condition.setLocalName("localName1234");
            condition.setSaltCondition(true);
            // Add the Condition to the Screen
            screen.getConditionPositions().put(new WellPosition("H1"), condition);
            final ComponentQuantity component1 = createComponent(101);
            condition.addComponent(component1);

            final ScreenService screenService = this.dataStorage.getScreenService();
            screenService.create(screen);

            final ConditionService service = this.dataStorage.getConditionService();
            //find Views
            final BusinessCriteria criteria = new BusinessCriteria(service);

            criteria.add(BusinessExpression.Equals(ConditionView.PROP_LOCAL_NAME, condition.getLocalName(),
                true));
            final Collection<ConditionView> views = service.findViews(criteria);

            //check views for ComponentQuantityView
            final ConditionView view = views.iterator().next();
            assertEquals(condition.getLocalName(), view.getLocalName());
            assertTrue(view.getSaltCondition());
            final List<ComponentQuantityView> components = view.getComponents();
            for (final ComponentQuantityView componentQuantity : components) {
                assertEquals(component1.getComponent().getChemicalName(), componentQuantity
                    .getComponentName());
                assertEquals(component1.getQuantity() + " " + component1.getUnits(), componentQuantity
                    .getQuantity());
            }
            assertEquals(1, components.size());
            assertEquals(1, views.size());

            //check count
            assertEquals(1, service.findViewCount(criteria).intValue());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    public void testConditionViewFindWith2Component() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {
            // Instantiate and name a new Screen
            final Screen screen = new Screen();
            screen.setName("localName" + System.currentTimeMillis());
            final Condition condition = new Condition();
            condition.setLocalName(screen.getName());
            // Add the Condition to the Screen
            screen.getConditionPositions().put(new WellPosition("H1"), condition);
            final ComponentQuantity component1 = createComponent(101);
            final ComponentQuantity component2 = createComponent(102);
            condition.addComponent(component1);
            condition.addComponent(component2);

            final ScreenService screenService = this.dataStorage.getScreenService();
            screenService.create(screen);

            final ConditionService service = this.dataStorage.getConditionService();
            //find Views
            final BusinessCriteria criteria = new BusinessCriteria(service);

            criteria.add(BusinessExpression.Equals(ConditionView.PROP_LOCAL_NAME, condition.getLocalName(),
                true));
            final Collection<ConditionView> views = service.findViews(criteria);

            //check views for ComponentQuantityView
            final ConditionView view = views.iterator().next();
            assertEquals(condition.getLocalName(), view.getLocalName());
            final List<ComponentQuantityView> components = view.getComponents();

            assertEquals(2, components.size());
            assertEquals(1, views.size());

            //check count
            assertEquals(1, service.findViewCount(criteria).intValue());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    private ComponentQuantity createComponent(final int serial) {
        final ComponentQuantity componentQuantity = new ComponentQuantity();
        componentQuantity.setUnits("M");
        componentQuantity.setQuantity(0.101 + serial);
        final Component component = new Component();
        component.setChemicalName("chemicalName" + serial);
        componentQuantity.setComponent(component);
        return componentQuantity;
    }
}
