package org.pimslims.crystallization.integration;

import java.util.Calendar;
import java.util.Collection;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.core.service.SampleService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.view.SampleView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.AbstractXtalTest;

public class SampleViewTest extends AbstractXtalTest {

    public SampleViewTest(final String methodName, final DataStorage dataStorage) {
        super(methodName, dataStorage);

    }

    public void testSampleViewFind() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {
            //create object for search
            final SampleService service = this.dataStorage.getSampleService();
            final Sample toMake = new Sample();
            toMake.setName("sample" + UNIQUE);
            toMake.setDescription("desc" + UNIQUE);
            toMake.setPH(7.0);
            toMake.setCreateDate(Calendar.getInstance());

            final Construct construct = createXConstruct();
            toMake.setConstruct(construct);
            service.create(toMake);
            this.dataStorage.flush();
            //find Views
            final BusinessCriteria criteria = new BusinessCriteria(service);
            criteria.add(BusinessExpression.Equals(SampleView.PROP_NAME, toMake.getName(), true));
            final Collection<SampleView> views = service.findViews(criteria);
            //check views
            assertEquals(1, views.size());
            final SampleView view = views.iterator().next();
            assertEquals(toMake.getName(), view.getSampleName());
            assertEquals(toMake.getId(), view.getSampleId());
            assertEquals(toMake.getDescription(), view.getDescription());
            assertEquals(toMake.getConstruct().getId(), view.getConstructId());
            assertEquals(toMake.getConstruct().getName(), view.getConstructName());
            assertEquals(toMake.getCreateDate(), view.getCreateDate());
            assertEquals(toMake.getPH(), view.getPH());
            //TODO other sample details
            //check count
            assertEquals(1, service.findViewCount(criteria).intValue());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }
}
