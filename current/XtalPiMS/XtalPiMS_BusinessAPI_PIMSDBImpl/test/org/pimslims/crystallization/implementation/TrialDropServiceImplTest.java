/*
 * SampleServiceImplTest.java JUnit based test Created on 09 August 2007, 16:56
 */

package org.pimslims.crystallization.implementation;

import java.sql.Timestamp;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.ConditionQuantity;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.SampleQuantity;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.sample.Sample;

/**
 * 
 * @author ian
 */
public class TrialDropServiceImplTest extends TestCase {

    // unique string used to avoid name clashes
    private static final String UNIQUE = "test" + System.currentTimeMillis();

    private static final Timestamp NOW = new Timestamp(System.currentTimeMillis());

    protected final DataStorageImpl dataStorage;

    TrialDrop trialDrop;

    WellPosition position = new WellPosition(7, 12, 3);

    SampleQuantity sq;

    private final String trialDropName = "trialDrop" + UNIQUE;

    public TrialDropServiceImplTest(String testName) {
        super(testName);
        dataStorage = DataStorageFactory.getDataStorageFactory().getDataStorage();
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TrialDropServiceImplTest.class);

        return suite;
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //create a trialDrop
        trialDrop = new TrialDrop();
        trialDrop.setName(trialDropName);
        trialDrop.setWellPosition(position);
        // protein sample
        org.pimslims.business.core.model.Sample xSample = new org.pimslims.business.core.model.Sample();
        xSample.setName("protein" + UNIQUE);
        sq = new SampleQuantity(xSample, 0.001, "L");
        trialDrop.addSample(sq);

        //TODO more TrialDrop details
    }

    /**
     * Test of create trialDrop
     */
    public void testCreate() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            WritableVersion version = this.dataStorage.getWritableVersion();

            //create pims trialDrop with empty name
            TrialDropServiceImpl service = this.dataStorage.getTrialDropService();
            service.create(trialDrop);

            // verify pims trialDrop
            assertNotNull(trialDrop.getId());
            Sample pimsSample = version.get(trialDrop.getId());
            assertNotNull(pimsSample);
            assertEquals(trialDropName, pimsSample.getName());
            assertEquals(position.getColumn(), pimsSample.getColPosition().intValue());
            assertEquals(position.getRow(), pimsSample.getRowPosition().intValue());
            assertEquals(position.getSubPosition(), pimsSample.getSubPosition().intValue());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    /**
     * Test of create trialDrop
     */
    public void testUpdate() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            WritableVersion version = this.dataStorage.getWritableVersion();

            TrialService ts = this.dataStorage.getTrialService();
            PlateType type = new PlateType();
            type.setColumns(12);
            type.setRows(8);
            type.setSubPositions(2);
            type.setReservoir(2);
            type.setName(UNIQUE);
            ts.create(type);
            TrialPlate plate = ts.createTrialPlate(UNIQUE, type);

            //create pims trialDrop 
            trialDrop.setPlate(plate);
            plate.addTrialDrop(trialDrop);
            TrialDropServiceImpl service = this.dataStorage.getTrialDropService();
            service.create(trialDrop);

            // update the trial drop
            trialDrop.setDescription("description");
            Condition condition = new Condition();
            ConditionQuantity additive = new ConditionQuantity(condition, 0, "L");
            trialDrop.setMotherLiquor(additive);
            org.pimslims.business.core.model.Sample sample = new org.pimslims.business.core.model.Sample();
            sample.setName("s" + UNIQUE);
            SampleQuantity sq = new SampleQuantity(sample, 0, "L");
            trialDrop.clearSamples();
            trialDrop.addSample(sq);

            service.updateTrialDrop(trialDrop);
            TrialDrop found = service.findTrialDrop(trialDrop.getPlate(), trialDrop.getWellPosition());
            assertNotNull(found);
            assertEquals(trialDrop.getDescription(), found.getDescription());
            assertEquals(trialDrop.getMotherLiquor(), found.getMotherLiquor());
            assertEquals(trialDrop.isAdditiveScreen(), found.isAdditiveScreen());
            assertEquals(trialDrop.getPlate(), found.getPlate());
            assertEquals(trialDrop.getId(), found.getId());
            assertEquals(trialDrop.getReservoir(), found.getReservoir());
            assertEquals(1, found.getSamples().size());
            assertEquals(sample.getName(), found.getSamples().iterator().next().getSample().getName());
            //TODO test quantities

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    /**
     * Test of load trialDrop
     */
    public void testgetWell() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            WritableVersion version = this.dataStorage.getWritableVersion();

            //create pims trialDrop with empty name
            TrialDropServiceImpl service = this.dataStorage.getTrialDropService();
            service.create(trialDrop);

            // reload trialDrop from pims sample
            assertNotNull(trialDrop.getId());
            Sample pimsSample = version.get(trialDrop.getId());
            TrialDrop trialDropReloaded = service.getWell(pimsSample);

            // verify reloaded trialDrop
            assertNotNull(trialDropReloaded);
            assertEquals(trialDropName, trialDropReloaded.getName());
            assertEquals(position.getColumn(), trialDropReloaded.getWellPosition().getColumn());
            assertEquals(position.getRow(), trialDropReloaded.getWellPosition().getRow());
            assertEquals(position.getSubPosition(), trialDropReloaded.getWellPosition().getSubPosition());
            //verify SampleQuantity
            SampleQuantity sqReloaded = trialDropReloaded.getSamples().iterator().next();
            assertNotNull(sqReloaded);
            assertEquals(sq.getQuantity(), sqReloaded.getQuantity());
            assertEquals(sq.getUnit(), sqReloaded.getUnit());
            //verify sample
            assertEquals(sq.getSample().getName(), sqReloaded.getSample().getName());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }
}
