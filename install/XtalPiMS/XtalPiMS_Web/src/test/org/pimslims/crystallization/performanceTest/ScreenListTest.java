package org.pimslims.crystallization.performanceTest;

import java.util.Collection;
import java.util.logging.Logger;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.service.ScreenService;
import org.pimslims.business.crystallization.view.ScreenView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.util.AbstractPerformanceTestCase;

public class ScreenListTest extends AbstractPerformanceTestCase {
	Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public void _testCall(DataStorage dataStorage) throws BusinessException {
		ScreenService screenService = dataStorage.getScreenService();
		BusinessCriteria criteria = new BusinessCriteria(screenService);
		criteria.setMaxResults(10);
		criteria.setFirstResult(0);
		int count = screenService.findAllCount();
		if (count < 10)
			log.warning("Don't have enough testing data!");
		Collection<ScreenView> screens = screenService.findViews(criteria);
	}

	@Override
	public Logger getLogger() {
		return log;
	}

}
