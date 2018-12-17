package org.pimslims.crystallization.integration;

import java.util.Calendar;
import java.util.Collection;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Organisation;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.service.GroupService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.view.GroupView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.AbstractXtalTest;

public class GroupViewTest extends AbstractXtalTest {

    private static final Calendar NOW = Calendar.getInstance();

    public GroupViewTest(final String methodName, final DataStorage dataStorage) {
        super(methodName, dataStorage);

    }

    public void testGroupViewFind() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {
            final Organisation organisation = createXOrganisation();

            final Group toMake = new Group("group" + UNIQUE, organisation);
            final Person groupHead = createXPerson();
            toMake.setGroupHead(groupHead);
            final GroupService groupService = this.dataStorage.getGroupService();
            assertNotNull(groupService);
            groupService.create(toMake);

            //find Views
            final GroupService service = this.dataStorage.getGroupService();
            final BusinessCriteria criteria = new BusinessCriteria(service);
            criteria.add(BusinessExpression.Equals(GroupView.PROP_NAME, toMake.getName(), true));
            final Collection<GroupView> views = service.findViews(criteria);

            //check views
            assertEquals(1, views.size());
            final GroupView view = views.iterator().next();
            assertEquals(toMake.getName(), view.getGroupName());
            //TODO assertEquals(toMake.getGroupHead().getUsername(), view.getGroupHead());
            assertEquals(toMake.getOrganisation().getName(), view.getOrganisation());
            assertEquals(toMake.getId(), view.getGroupId());
            //check count
            assertEquals(1, service.findViewCount(criteria).intValue());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

}
