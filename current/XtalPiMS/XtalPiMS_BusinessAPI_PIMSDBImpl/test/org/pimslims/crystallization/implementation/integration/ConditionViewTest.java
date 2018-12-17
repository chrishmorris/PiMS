package org.pimslims.crystallization.implementation.integration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;

public class ConditionViewTest extends org.pimslims.crystallization.integration.ConditionViewTest {
    public ConditionViewTest(final String methodName) {
        super(methodName, DataStorageFactory.getDataStorageFactory().getDataStorage());

    }

    private void deleteUser(String username) throws BusinessException, AccessException, ConstraintException {
        WritableVersion version;
        this.dataStorage.openResources("administrator");
        version = (WritableVersion) ((DataStorageImpl) this.dataStorage).getVersion();
        try {
            User user = version.findFirst(User.class, User.PROP_NAME, username);
            Set<UserGroup> groups = user.getUserGroups();
            for (Iterator iterator = groups.iterator(); iterator.hasNext();) {
                UserGroup userGroup = (UserGroup) iterator.next();
                Set<Permission> permissions = userGroup.getPermissions();
                Set<LabNotebook> books = new HashSet();
                for (Iterator iterator2 = permissions.iterator(); iterator2.hasNext();) {
                    Permission permission = (Permission) iterator2.next();
                    books.add(permission.getLabNotebook());
                }
                version.delete(books);
            }
            version.delete(groups);
            user.delete();
            this.dataStorage.commit();
            this.dataStorage.closeResources();
        } finally {
            if (!version.isCompleted()) {
                this.dataStorage.abort();
            }
        }
    }

    private void createUser(String username) throws ConstraintException, BusinessException {
        WritableVersion version = (WritableVersion) ((DataStorageImpl) this.dataStorage).getVersion();
        try {
            UserGroup group = new UserGroup(version, UNIQUE);
            LabNotebook book = new LabNotebook(version, UNIQUE);
            new Permission(version, "read", book, group);
            new Permission(version, "create", book, group);
            new Permission(version, "update", book, group);
            new Permission(version, "delete", book, group);
            new User(version, username).addUserGroup(group);
            this.dataStorage.commit();
            this.dataStorage.closeResources();
        } finally {
            if (!version.isCompleted()) {
                this.dataStorage.abort();
            }
        }
    }

    public void testConditionViewFindWith1Component() throws BusinessException, ConstraintException,
        AccessException { // set up

        // set up
        String username = UNIQUE + "u";
        this.dataStorage.openResources("administrator");
        createUser(username);

        this.dataStorage.openResources(username);

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
                assertEquals(component1.getComponent().getChemicalName(),
                    componentQuantity.getComponentName());
                assertEquals(component1.getQuantity() + " " + component1.getUnits(),
                    componentQuantity.getQuantity());
            }
            assertEquals(1, components.size());
            assertEquals(1, views.size());

            //check count
            assertEquals(1, service.findViewCount(criteria).intValue());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }

        // tidy up
        deleteUser(username);
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
