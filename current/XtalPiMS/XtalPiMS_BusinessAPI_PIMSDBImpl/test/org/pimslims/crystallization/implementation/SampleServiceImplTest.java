/*
 * SampleServiceImplTest.java
 * JUnit based test
 *
 * Created on 09 August 2007, 16:56
 */

package org.pimslims.crystallization.implementation;

import java.sql.Timestamp;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.business.core.service.SampleService;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.SampleServiceTest;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.exception.ConstraintException;
import org.pimslims.dao.WritableVersion;

/**
 * 
 * @author ian
 */
public class SampleServiceImplTest extends SampleServiceTest {

    // unique string used to avoid name clashes
    private static final String UNIQUE = "test" + System.currentTimeMillis();

    private static final Timestamp NOW = new Timestamp(System.currentTimeMillis());

    public SampleServiceImplTest(String testName) {
        super(testName, DataStorageFactory.getDataStorageFactory().getDataStorage());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SampleServiceImplTest.class);

        return suite;
    }

    /**
     * Test of findByName method, of class uk.ac.ox.oppf.platedb.business.SampleServiceImpl.
     */
    public void testFindByName() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            WritableVersion version = (WritableVersion) (((DataStorageImpl) this.dataStorage).getVersion());
            org.pimslims.model.sample.Sample pimsSample =
                new org.pimslims.model.sample.Sample(version, "sample" + UNIQUE);

            SampleService service = this.dataStorage.getSampleService();
            assertNull(service.findByName("nonesuch"));
            org.pimslims.business.core.model.Sample xtalSample = service.findByName(pimsSample.getName());
            assertNotNull(xtalSample);
            assertEquals(pimsSample.getName(), xtalSample.getName());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    /* public void testFindByPlateAndWell() throws Exception {
         this.dataStorage.openResources("administrator");
         try {
             WritableVersion version = (WritableVersion) (((DataStorageImpl) this.dataStorage).getVersion());
             Holder holder = new Holder(version, "plate" + UNIQUE, Collections.EMPTY_SET);
             holder.setDetails("details");
             org.pimslims.model.sample.Sample protein =
                 new org.pimslims.model.sample.Sample(version, "protein" + UNIQUE);
             org.pimslims.model.sample.Sample output =
                 new org.pimslims.model.sample.Sample(version, "A02:" + UNIQUE);
             output.setRowPosition(1);
             output.setColPosition(2);
             output.setSubPosition(1);
             output.setHolder(holder);
             ExperimentType type = new ExperimentType(version, "test", "type" + UNIQUE);
             Experiment experiment = new Experiment(version, "exp" + UNIQUE + ":A02", NOW, NOW, type);
             OutputSample os = new OutputSample(version, experiment);
             os.setSample(output);
             InputSample is = new InputSample(version, experiment);
             is.setSample(protein);
             is.setName(SampleServiceImpl.PROTEIN);

             SampleService service = this.dataStorage.getSampleService();
             TrialPlate plate = new TrialPlate(new PlateType());
             plate.setBarcode(holder.getName());
             final Collection<org.pimslims.business.core.model.Sample> xtalSamples =
                 service.findByPlateAndWell(plate, new WellPosition("A02"), null);
             assertNotNull(xtalSamples);
             final org.pimslims.business.core.model.Sample xtalSample = xtalSamples.iterator().next();
             assertNotNull(xtalSample);
             assertEquals(protein.getName(), xtalSample.getName());
             // see also PlateExperimentServiceImplTest.testFindBySample
         } finally {
             this.dataStorage.abort(); // not testing persistence
         }
     }
    */
    public void testUpdate() throws BusinessException, ConstraintException {
        this.dataStorage.openResources("administrator");
        try {
            WritableVersion version = (WritableVersion) (((DataStorageImpl) this.dataStorage).getVersion());
            org.pimslims.model.sample.Sample pimsSample =
                new org.pimslims.model.sample.Sample(version, "sample" + UNIQUE);

            SampleService service = this.dataStorage.getSampleService();
            org.pimslims.business.core.model.Sample xtalSample = service.findByName(pimsSample.getName());
            assertEquals(pimsSample.getName(), xtalSample.getName());
            //update xtalSample
            xtalSample.setDescription("description2" + UNIQUE);
            xtalSample.setName("sample" + UNIQUE);
            service.update(xtalSample);
            assertEquals(xtalSample.getDescription(), pimsSample.getDetails());
            assertEquals(xtalSample.getName(), pimsSample.getName());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

}
