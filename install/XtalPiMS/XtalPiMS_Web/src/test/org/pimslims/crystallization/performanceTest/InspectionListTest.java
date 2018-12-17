package org.pimslims.crystallization.performanceTest;

import java.util.Collection;
import java.util.logging.Logger;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.service.PlateInspectionService;
import org.pimslims.business.crystallization.view.InspectionView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.util.AbstractPerformanceTestCase;

public class InspectionListTest extends AbstractPerformanceTestCase {
	Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public void _testCall(final DataStorage dataStorage)
			throws BusinessException {
		final PlateInspectionService service = dataStorage
				.getPlateInspectionService();
		final BusinessCriteria criteria = new BusinessCriteria(service);
		criteria.setMaxResults(10);
		criteria.setFirstResult(0);
		final int count = service.findViewCount(criteria);
		if (count < 10) {
			log.warning("Don't have enough testing data!");
		}
		final Collection<InspectionView> results = service.findViews(criteria);
		/*
		 * if (results.size() < 10) {
		 * log.warn("Don't have enough testing data!"); } else {
		 * assertEquals(10, results.size()); }
		 */
	}

	@Override
	public Logger getLogger() {
		return log;
	}

}
