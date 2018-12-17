package org.pimslims.crystallization.performanceTest;

import java.util.Collection;
import java.util.logging.Logger;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.service.PlateExperimentService;
import org.pimslims.business.crystallization.view.PlateExperimentView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.util.AbstractPerformanceTestCase;

public class PlateListTest extends AbstractPerformanceTestCase {
	Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public void _testCall(final DataStorage dataStorage)
			throws BusinessException {
		final PlateExperimentService service = dataStorage
				.getPlateExperimentService();
		final BusinessCriteria criteria = new BusinessCriteria(service);
		criteria.setMaxResults(10);
		criteria.setFirstResult(0);
		final long start = System.currentTimeMillis();
		final int count = service.findViewCount(criteria);
		System.out.println("count using "
				+ (System.currentTimeMillis() - start) / 1000.0 + "s");
		if (count < 10) {
			log.warning("Don't have enough testing data!");
		}
		final Collection<PlateExperimentView> results = service
				.findViews(criteria);
	}

	@Override
	public Logger getLogger() {
		return log;
	}

}
