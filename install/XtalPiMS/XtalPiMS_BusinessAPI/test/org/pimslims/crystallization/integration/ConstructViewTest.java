package org.pimslims.crystallization.integration;

import java.util.Calendar;
import java.util.Collection;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.service.ConstructService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.view.ConstructView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.AbstractXtalTest;

public class ConstructViewTest extends AbstractXtalTest {

    private static final Calendar NOW = Calendar.getInstance();

    public ConstructViewTest(final String methodName, final DataStorage dataStorage) {
        super(methodName, dataStorage);

    }

    public void testConstructViewFind() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {
            final ConstructService service = this.dataStorage.getConstructService();
            final Construct toMake = new Construct(UNIQUE, null);
            final Person xPerson = createXPerson();

            toMake.setDescription("desc");
            toMake.setOwner(xPerson);
            service.create(toMake);

            final Construct result = service.findByName(toMake.getName());
            assertNotNull(result);
            assertEquals(toMake.getDescription(), result.getDescription());
            assertEquals(toMake.getName(), result.getName());
            assertEquals(toMake.getOwner().getUsername(), result.getOwner().getUsername());

            //find Views
            final BusinessCriteria criteria = new BusinessCriteria(service);
            criteria.add(BusinessExpression.Equals(ConstructView.PROP_NAME, toMake.getName(), true));
            final Collection<ConstructView> views = service.findViews(criteria);

            //check views
            assertEquals(1, views.size());
            final ConstructView view = views.iterator().next();
            assertEquals(toMake.getName(), view.getConstructName());
            assertEquals(toMake.getId(), view.getConstructId());
            assertEquals(toMake.getDescription(), view.getDescription());
            assertEquals(toMake.getOwner().getUsername(), view.getOwner());
            //TODO target,group

            //check count
            assertEquals(1, service.findViewCount(criteria).intValue());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

}
