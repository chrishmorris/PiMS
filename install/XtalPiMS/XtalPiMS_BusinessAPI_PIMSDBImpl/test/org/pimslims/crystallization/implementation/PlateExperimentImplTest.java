/*
 * PlateExperimentServiceImplTest.java JUnit based test Created on 09 August 2007, 16:56
 */

package org.pimslims.crystallization.implementation;

import java.util.Calendar;
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessCriterion;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.service.PlateExperimentService;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.PlateExperimentView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.PlateExperimentServiceTest;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;

/**
 * 
 * @author ian
 */
public class PlateExperimentImplTest extends PlateExperimentServiceTest {

    private static final Calendar NOW = Calendar.getInstance();

    final TrialServiceImplTest trialTest;

    public PlateExperimentImplTest(final String testName) {
        super(testName, DataStorageFactory.getDataStorageFactory().getDataStorage());
        trialTest = new TrialServiceImplTest(this.dataStorage);
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite(PlateExperimentImplTest.class);

        return suite;
    }

    /**
     * Test of findPlateViews, of class uk.ac.ox.oppf.platedb.business.PlateExperimentServiceImpl.
     */
    public void testFindPlateViewsByBARCODE() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            //create 3 plate experiment
            final WritableVersion version =
                (WritableVersion) ((DataStorageImpl) this.dataStorage).getVersion();
            final TrialService trialservice = this.dataStorage.getTrialService();
            final TrialPlate plate = trialTest.createFilledTrialPlate(version, BARCODE + 0);
            trialservice.saveTrialPlate(plate);
            trialservice.saveTrialPlate(trialTest.createFilledTrialPlate(version, BARCODE + 1));
            version.flush();
            final PlateExperimentService service = this.dataStorage.getPlateExperimentService();

            //search with BARCODE
            final BusinessCriteria criteria = new BusinessCriteria(service);
            final BusinessCriterion criterion =
                BusinessExpression.Equals(PlateExperimentView.PROP_BARCODE, BARCODE + 0, true);
            criteria.add(criterion);
            Collection<PlateExperimentView> result = service.findViews(criteria);
            assertNotNull(result);
            assertEquals(1, result.size());
            PlateExperimentView pv = result.iterator().next();
            assertEquals(BARCODE + 0, pv.getBarcode());
            assertEquals(plate.getDescription(), pv.getDescription());

            //search by construct
            final BusinessCriteria criteria2 = new BusinessCriteria(service);
            final BusinessCriterion criterion2 =
                BusinessExpression.Equals(PlateExperimentView.PROP_CONSTRUCT_NAME, plate.getConstruct()
                    .getName(), true);
            criteria2.add(criterion2);
            result = service.findViews(criteria2);
            assertEquals(1, result.size());
            pv = result.iterator().next();
            assertEquals(plate.getConstruct().getName(), pv.getConstructName());
            assertEquals(BARCODE + 0, pv.getBarcode());
            assertEquals(plate.getDescription(), pv.getDescription());
            assertTrue(pv.getConstructId() > 1);

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    /**
     * Test of findPlateViewCount, of class uk.ac.ox.oppf.platedb.business.PlateExperimentServiceImpl.
     */
    public void testFindPlateViewsCount() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            final WritableVersion version =
                (WritableVersion) ((DataStorageImpl) this.dataStorage).getVersion();
            final PlateExperimentService service = this.dataStorage.getPlateExperimentService();

            //get inital count
            final BusinessCriteria criteria = new BusinessCriteria(service);
            final int oldCount = service.findViewCount(criteria);
            //create 2 plate experiment
            final TrialService trialservice = this.dataStorage.getTrialService();
            trialservice.saveTrialPlate(trialTest.createFilledTrialPlate(version, BARCODE));
            trialservice.saveTrialPlate(trialTest.createFilledTrialPlate(version, BARCODE + 1));
            version.flush();
            //new count
            final int newCount = service.findViewCount(criteria);
            //assertEquals(oldCount + 2, newCount);

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    //TODO need to create a test to check access control 
    public void _testFindPlateViewsWithAccessControl() throws BusinessException {
        this.dataStorage.openResources("administrator");
        String user1Name = null;
        String user2Name = null;
        try {
            final org.pimslims.business.core.model.Group g1 = this.createXGroup();
            final org.pimslims.business.core.model.Group g2 = this.createXGroup();
            user1Name = g1.getUsers().iterator().next().getUsername();
            user2Name = g2.getUsers().iterator().next().getUsername();
            this.dataStorage.commit();
        } catch (final BusinessException e) {
            this.dataStorage.abort();
            fail();
        }
        //user1 create something
        this.dataStorage.openResources(user1Name);
        try {
            //create 3 plate experiment
            final WritableVersion version =
                (WritableVersion) ((DataStorageImpl) this.dataStorage).getVersion();
            final TrialService trialservice = this.dataStorage.getTrialService();
            trialservice.saveTrialPlate(trialTest.createFilledTrialPlate(version, BARCODE + "user1"));

            this.dataStorage.commit();
        } catch (final ConstraintException e) {
            e.printStackTrace();
            this.dataStorage.abort();
            fail();
        }
        //user2 create something
        this.dataStorage.openResources(user2Name);
        try {
            //create 3 plate experiment
            final WritableVersion version =
                (WritableVersion) ((DataStorageImpl) this.dataStorage).getVersion();
            final TrialService trialservice = this.dataStorage.getTrialService();
            trialservice.saveTrialPlate(trialTest.createFilledTrialPlate(version, BARCODE + "user2"));
            this.dataStorage.commit();
        } catch (final ConstraintException e) {
            e.printStackTrace();
            this.dataStorage.abort();
            fail();
        }
        //user1 should only see user1's plate not user2's
        this.dataStorage.openResources(user1Name);
        try {
            final PlateExperimentService service = this.dataStorage.getPlateExperimentService();
            //search with BARCODE of user1
            final BusinessCriteria criteria = new BusinessCriteria(service);
            final BusinessCriterion criterion =
                BusinessExpression.Equals(PlateExperimentView.PROP_BARCODE, BARCODE + "user1", true);
            criteria.add(criterion);
            final Collection<PlateExperimentView> result = service.findViews(criteria);
            assertNotNull(result);
            assertEquals(2, result.size());
            //search with BARCODE of user2
            final BusinessCriteria criteria2 = new BusinessCriteria(service);
            final BusinessCriterion criterion2 =
                BusinessExpression.Equals(PlateExperimentView.PROP_BARCODE, BARCODE + "user2", true);
            criteria2.add(criterion2);
            final Collection<PlateExperimentView> result2 = service.findViews(criteria2);
            assertNotNull(result2);
            assertEquals(0, result2.size());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

}
