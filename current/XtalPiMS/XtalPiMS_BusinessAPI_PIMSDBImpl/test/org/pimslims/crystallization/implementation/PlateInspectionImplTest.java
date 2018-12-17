/*
 * PlateExperimentServiceImplTest.java JUnit based test Created on 09 August 2007, 16:56
 */

package org.pimslims.crystallization.implementation;

import java.util.Calendar;
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.business.core.model.Location;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.service.PlateExperimentService;
import org.pimslims.business.crystallization.service.PlateInspectionService;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.InspectionView;
import org.pimslims.business.crystallization.view.PlateExperimentView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.InspectionServiceTest;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;

/**
 * 
 * @author ian
 */
public class PlateInspectionImplTest extends InspectionServiceTest {

    final TrialServiceImplTest trialTest;

    public PlateInspectionImplTest(final String testName) {
        super(testName, DataStorageFactory.getDataStorageFactory().getDataStorage());
        trialTest = new TrialServiceImplTest(this.dataStorage);
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite(PlateInspectionImplTest.class);

        return suite;
    }

    private TrialPlate createTrialPlate(final WritableVersion wv) throws ConstraintException,
        BusinessException {
        final TrialPlate plate = trialTest.createFilledTrialPlate(wv, "barcode" + System.currentTimeMillis());
        final TrialService service = this.dataStorage.getTrialService();
        service.saveTrialPlate(plate);
        return plate;

    }

    private PlateInspection createInspection(final TrialPlate trialPlate, String suffix)
        throws BusinessException {
        final PlateInspection inspection = new PlateInspection();
        inspection.setPlate(trialPlate);
        final Location location = super.createLocation(UNIQUE + suffix);
        inspection.setLocation(location);
        inspection.setInspectionDate(Calendar.getInstance());
        inspection.setInspectionName("inspectionName" + System.currentTimeMillis() + suffix);
        inspection.setDetails("inspection details");
        return inspection;
    }

    public void testCreate() throws BusinessException, ConstraintException {
        this.dataStorage.openResources("administrator");
        try {
            final WritableVersion version =
                (WritableVersion) ((DataStorageImpl) this.dataStorage).getVersion();
            //prepare data
            final TrialPlate plate = createTrialPlate(version);
            final PlateInspection inspection = createInspection(plate, "");
            //create inspection
            final PlateInspectionService service = dataStorage.getPlateInspectionService();
            service.create(inspection);
            //load inspection
            final Collection<PlateInspection> inspections = service.findByPlate(plate.getBarcode());
            //check results
            assertEquals(1, inspections.size());
            final PlateInspection inspection2 = inspections.iterator().next();
            assertEquals(inspection.getInspectionName(), inspection2.getInspectionName());
            assertEquals(inspection.getInspectionDate(), inspection2.getInspectionDate());
            assertEquals(inspection.getDetails(), inspection2.getDetails());
            assertNotNull(inspection2.getLocation());
            assertEquals(inspection.getLocation().getName(), inspection2.getLocation().getName());
            assertEquals(inspection.getPlate().getBarcode(), inspection2.getPlate().getBarcode());

        } finally {

            this.dataStorage.abort(); // not testing persistence
        }
    }

    public void testFindPlateView() throws BusinessException, ConstraintException {
        this.dataStorage.openResources("administrator");
        try {
            final WritableVersion version =
                (WritableVersion) ((DataStorageImpl) this.dataStorage).getVersion();

            //prepare data
            final TrialPlate plate = createTrialPlate(version);
            final PlateInspection inspection = createInspection(plate, "");
            //create inspection
            final PlateInspectionService service = dataStorage.getPlateInspectionService();
            service.create(inspection);
            version.flush();
            final BusinessCriteria criteria = new BusinessCriteria(service);
            criteria.add(BusinessExpression.Equals(InspectionView.PROP_BARCODE, plate.getBarcode(), true));

            final Collection<InspectionView> inspections = service.findViews(criteria);
            assertEquals(1, inspections.size());
            final InspectionView view = inspections.iterator().next();
            assertEquals(plate.getBarcode(), view.getBarcode());
            assertEquals(inspection.getInspectionDate(), view.getDate());
            assertEquals(inspection.getInspectionName(), view.getInspectionName());
            assertNotNull(view.getImager());
            assertEquals(inspection.getLocation().getName(), view.getImager());
            assertEquals(inspection.getLocation().getTemperature(), view.getTemperature());
            assertEquals(inspection.getDetails(), view.getDetails());
            //check count
            assertEquals(1, service.findViewCount(criteria).intValue());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }

    }

    public void testFindLastPlateView() throws BusinessException, ConstraintException {
        this.dataStorage.openResources("administrator");
        try {
            final WritableVersion version =
                (WritableVersion) ((DataStorageImpl) this.dataStorage).getVersion();

            //prepare data
            final TrialPlate plate = createTrialPlate(version);
            final PlateInspection inspection = createInspection(plate, "onelast" + UNIQUE);
            final PlateInspection inspection2 = createInspection(plate, "twolast" + UNIQUE);
            //create inspection
            final PlateInspectionService service = dataStorage.getPlateInspectionService();
            service.create(inspection);
            service.create(inspection2);
            version.flush();
            final BusinessCriteria criteria = new BusinessCriteria(service);
            criteria.add(BusinessExpression.Equals(InspectionView.PROP_BARCODE, plate.getBarcode(), true));
            //check count
            assertEquals(2, service.findViewCount(criteria).intValue());

            final Collection<InspectionView> inspections = service.findLatest(criteria);
            assertEquals(1, inspections.size());
            final InspectionView view = inspections.iterator().next();
            assertEquals(plate.getBarcode(), view.getBarcode());
            assertEquals(inspection2.getInspectionDate(), view.getDate());
            assertEquals(inspection2.getInspectionName(), view.getInspectionName());
            assertNotNull(view.getImager());
            assertEquals(inspection2.getLocation().getName(), view.getImager());
            assertEquals(inspection2.getLocation().getTemperature(), view.getTemperature());

            //check plateview's last image date
            final PlateExperimentService plateService = dataStorage.getPlateExperimentService();
            final BusinessCriteria plateCriteria = new BusinessCriteria(plateService);
            plateCriteria.add(BusinessExpression.Equals(PlateExperimentView.PROP_BARCODE, plate.getBarcode(),
                true));
            final Collection<PlateExperimentView> views = plateService.findViews(plateCriteria);
            final PlateExperimentView plateview = views.iterator().next();
            assertEquals(plate.getBarcode(), plateview.getBarcode());
            assertEquals(inspection2.getInspectionDate(), plateview.getLastImageDate());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }

    }

}
