package org.pimslims.crystallization.performanceTest;

import java.util.Collection;
import java.util.logging.Logger;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.service.PlateInspectionService;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.InspectionView;
import org.pimslims.business.crystallization.view.TrialDropView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.util.AbstractPerformanceTestCase;

public class TimeCourseTest extends AbstractPerformanceTestCase {
	Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public void _testCall(final DataStorage dataStorage)
			throws BusinessException {
		final PlateInspectionService service = dataStorage
				.getPlateInspectionService();
		final BusinessCriteria criteria = new BusinessCriteria(service);
		// criteria.add(BusinessExpression.Equals(TrialDropView.PROP_BARCODE,
		// barcode, true));
		final int count = service.findViewCount(criteria);
		if (count < 1) {
			log.warning("Don't have enough testing data!");
			return;
		}
		criteria.setMaxResults(1);
		criteria.setFirstResult(5);
		final Collection<InspectionView> results = service.findViews(criteria);
		// get barcode and inspectionName from inspection
		final String barcode = results.iterator().next().getBarcode();
		final String inspectionName = results.iterator().next()
				.getInspectionName();

		// find all trialDropViews related to this inspection
		final TrialService trialService = dataStorage.getTrialService();
		final BusinessCriteria trialCriteria = new BusinessCriteria(
				trialService);
		trialCriteria.add(BusinessExpression.Equals(TrialDropView.PROP_BARCODE,
				barcode, true));
		trialCriteria.add(BusinessExpression.Equals(TrialDropView.PROP_WELL,
				"A01.1", true));
		trialCriteria.setMaxResults(Integer.MAX_VALUE);
		trialCriteria.setFirstResult(0);
		final Collection<TrialDropView> trialResults = trialService
				.findViews(trialCriteria);

	}

	@Override
	public Logger getLogger() {
		return log;
	}

}
