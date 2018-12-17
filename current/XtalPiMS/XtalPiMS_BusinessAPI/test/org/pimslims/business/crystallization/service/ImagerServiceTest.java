/**
 * xtalPiMSApi org.pimslims.business.crystallization.service ImagerServiceTest.java
 * 
 * @author jon
 * @date 8 Sep 2010
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2010 jon The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.business.crystallization.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.model.ImageType;
import org.pimslims.business.crystallization.model.Imager;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.SchedulePlan;
import org.pimslims.business.crystallization.model.SchedulePlanOffset;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.view.ScheduleView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.AbstractXtalTest;

/**
 * ImagerServiceTest
 * 
 */
// In use by OPPF-UK
public class ImagerServiceTest extends AbstractXtalTest {

    /**
     * <p>
     * The logger for this class.
     * </p>
     */
    protected static final java.util.logging.Logger LOGGER =
        java.util.logging.Logger.getLogger(ImagerServiceTest.class.getName());

    /**
     * <p>
     * Unique string used to avoid name clashes.
     * </p>
     */
    protected static String UNIQUE = "test" + System.currentTimeMillis();

    /**
     * Number of SchedulePlanOffsets with which to test.
     */
    protected static final int OFFSET_COUNT = 10;

    /**
     * Constructor for ImagerServiceTest
     * 
     * @param name
     */
    public ImagerServiceTest(final String methodName, final DataStorage dataStorage) {
        super(methodName, dataStorage);
    }

    /**
     * ImagerServiceTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * ImagerServiceTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetService() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {
            final ImagerService service = this.dataStorage.getImagerService();
            assertNotNull(service);
        } finally {
            this.dataStorage.abort();
        }
    }



    /**
     * ImagerServiceImplTest.testFindSchedules
     * 
     * @throws BusinessException
     * @see org.pimslims.business.crystallization.service.ImagerServiceTest#testFindSchedules()
     */
    public void testFindSchedules() throws BusinessException {

        SchedulePlan sp = new SchedulePlan();
        List<SchedulePlanOffset> offsets = new ArrayList<SchedulePlanOffset>();
        sp.setName(UNIQUE + "-sp");
        sp.setOffsets(offsets);
        for (int i = 0; i < OFFSET_COUNT; i++) {
            SchedulePlanOffset spo = new SchedulePlanOffset();
            spo.setImagingNumber(i + 1);
            spo.setOffsetHoursFromTimeZero(i * 5);
            spo.setPriority((0 == i) ? 1 : i);
            spo.setSchedulePlan(sp);
            offsets.add(spo);
        }

        PlateType pt = new PlateType();
        pt.setColumns(12);
        pt.setName(UNIQUE + "-pt");
        pt.setReservoir(2);
        pt.setRows(8);
        pt.setSubPositions(2);
        // TODO pt.setDefaultSchedulePlan(sp);

        try {

            this.dataStorage.openResources("administrator");
            ScheduleService ss = this.dataStorage.getScheduleService();
            ss.create(sp);

            TrialService ts = this.dataStorage.getTrialService();
            ts.create(pt, sp); // TODO ts.create(pt);
            TrialPlate tp = ts.createTrialPlate(UNIQUE, pt);

            this.dataStorage.flush();

            ImagerService is = this.dataStorage.getImagerService();
            is.schedulePlate(UNIQUE, null);
            this.dataStorage.flush();

            setSqlLogToDebug();
            long prepped = System.currentTimeMillis();
            Collection<ScheduleView> schedules = is.findSchedules(UNIQUE, "");
            long read = System.currentTimeMillis();
            restoreDefaultLoggingLevel();

            assertNotNull(schedules);
            assertEquals(OFFSET_COUNT, schedules.size());
            Calendar expected = tp.getCreateDate();
            for (ScheduleView schedule : schedules) {
                assertEquals(UNIQUE, schedule.getBarcode());
                assertNotNull(schedule.getDateToImage());
                // assertEquals(expected, schedule.getDateToImage()) fails as TimeZone is not the same
                assertEquals(0, expected.compareTo(schedule.getDateToImage()));
                expected.add(Calendar.HOUR, 5);
                //String name = ((ImagerServiceImpl) is).createName(UNIQUE, schedule.getDateToImage());
                //assertEquals(name, schedule.getName());
                assertEquals(ImagerService.IMAGING_STATE_SCHEDULED, schedule.getState());
            }

            long readThreshold = 100;
            long readPerformance = read - prepped;
            LOGGER.info("testFindSchedules(): Read performance = " + readPerformance + "ms");
            assertTrue("Read performance threshold exceeded (" + readThreshold + "ms, " + readPerformance
                + "ms)", (readThreshold >= readPerformance));

        } finally {
            // Not testing persistence
            this.dataStorage.abort();
        }

    }

    /**
     * ImagerServiceImplTest.testSchedulePlate
     * 
     * @throws BusinessException
     * 
     * @see org.pimslims.business.crystallization.service.ImagerServiceTest#testSchedulePlate()
     */
    public void testSchedulePlate() throws BusinessException {

        SchedulePlan sp = new SchedulePlan();
        List<SchedulePlanOffset> offsets = new ArrayList<SchedulePlanOffset>();
        sp.setName(UNIQUE + "-sp");
        sp.setOffsets(offsets);
        for (int i = 0; i < OFFSET_COUNT; i++) {
            SchedulePlanOffset spo = new SchedulePlanOffset();
            spo.setImagingNumber(i + 1);
            spo.setOffsetHoursFromTimeZero(i * 5);
            spo.setPriority((0 == i) ? 1 : i);
            spo.setSchedulePlan(sp);
            offsets.add(spo);
        }

        PlateType pt = new PlateType();
        pt.setColumns(12);
        pt.setName(UNIQUE + "-pt");
        pt.setReservoir(2);
        pt.setRows(8);
        pt.setSubPositions(2);
        // TODO pt.setDefaultSchedulePlan(sp);

        try {

            this.dataStorage.openResources("administrator");
            ScheduleService ss = this.dataStorage.getScheduleService();
            ss.create(sp);
            this.dataStorage.flush();

            TrialService ts = this.dataStorage.getTrialService();
            ts.create(pt, sp); // TODO ts.create(pt);
            TrialPlate tp = ts.createTrialPlate(UNIQUE, pt);
            this.dataStorage.flush();
            assertNotNull(tp.getCreateDate());

            ImagerService is = this.dataStorage.getImagerService();

            clearStatistics();
            setSqlLogToDebug();
            long prepped = System.currentTimeMillis();
            Collection<ScheduleView> schedules = is.schedulePlate(UNIQUE, null);
            this.dataStorage.flush();
            long written = System.currentTimeMillis();
            restoreDefaultLoggingLevel();
            printStatistics();

            assertNotNull(schedules);
            assertEquals(OFFSET_COUNT, schedules.size());
            Calendar expected = tp.getCreateDate();
            for (ScheduleView schedule : schedules) {
                assertEquals(UNIQUE, schedule.getBarcode());
                assertNotNull(schedule.getDateToImage());
                // assertEquals(expected, schedule.getDateToImage()) fails as TimeZone is not the same
                assertEquals(0, expected.compareTo(schedule.getDateToImage()));
                expected.add(Calendar.HOUR, 5);
                assertEquals(ImagerService.IMAGING_STATE_SCHEDULED, schedule.getState());
            }

            long writeThreshold = 500;
            long writePerformance = written - prepped;
            LOGGER.info("testSchedulePlate(): Write performance = " + writePerformance + "ms");
            assertTrue("Write performance threshold exceeded (" + writeThreshold + "ms, " + writePerformance
                + "ms)", (writeThreshold >= writePerformance));

        } finally {
            // Not testing persistence
            this.dataStorage.abort();
        }

    }

    /**
     * ImagerServiceImplTest.testStartedImaging
     * 
     * @throws BusinessException
     * 
     * @see org.pimslims.business.crystallization.service.ImagerServiceTest#testStartedImaging()
     */
    public void testStartedImaging() throws BusinessException {

        SchedulePlan sp = new SchedulePlan();
        List<SchedulePlanOffset> offsets = new ArrayList<SchedulePlanOffset>();
        sp.setName(UNIQUE + "-sp");
        sp.setOffsets(offsets);
        for (int i = 0; i < OFFSET_COUNT; i++) {
            SchedulePlanOffset spo = new SchedulePlanOffset();
            spo.setImagingNumber(i + 1);
            spo.setOffsetHoursFromTimeZero(i * 5);
            spo.setPriority((0 == i) ? 1 : i);
            spo.setSchedulePlan(sp);
            offsets.add(spo);
        }

        PlateType pt = new PlateType();
        pt.setColumns(12);
        pt.setName(UNIQUE + "-pt");
        pt.setReservoir(2);
        pt.setRows(8);
        pt.setSubPositions(2);
        // TODO pt.setDefaultSchedulePlan(sp);

        try {
            this.dataStorage.openResources("administrator");
            ScheduleService ss = this.dataStorage.getScheduleService();
            ss.create(sp);

            TrialService ts = this.dataStorage.getTrialService();
            pt = ts.create(pt, sp); // TODO ts.create(pt);
            ts.createTrialPlate(UNIQUE, pt);

            this.dataStorage.flush();

            ImagerService is = this.dataStorage.getImagerService();
            is.schedulePlate(UNIQUE, null);
            is.createInstrument(UNIQUE + "-imager", 21f);
            this.dataStorage.flush();

            Collection<ScheduleView> schedules = is.findSchedules(UNIQUE, "");
            assertNotNull(schedules);
            ScheduleView schedule = schedules.iterator().next();
            assertEquals(ImagerService.IMAGING_STATE_SCHEDULED, schedule.getState());

            Calendar now = Calendar.getInstance();

            setSqlLogToDebug();
            long prepped = System.currentTimeMillis();
            String imagingId = is.startedImaging(UNIQUE, schedule.getDateToImage(), now, UNIQUE + "-imager");
            this.dataStorage.flush();
            long written = System.currentTimeMillis();
            restoreDefaultLoggingLevel();

            schedules = is.findSchedules(UNIQUE, "");
            assertNotNull(schedules);
            schedule = schedules.iterator().next();
            assertEquals(ImagerService.IMAGING_STATE_IMAGING, schedule.getState());
            assertEquals(schedule.getName(), imagingId);
            assertEquals(now, schedule.getDateImaged());
            assertEquals(UNIQUE + "-imager", schedule.getImager());

            long writeThreshold = 100;
            long writePerformance = written - prepped;
            LOGGER.info("testStartedImaging(): Write performance = " + writePerformance + "ms");
            assertTrue("Write performance threshold exceeded (" + writeThreshold + "ms, " + writePerformance
                + "ms)", (writeThreshold >= writePerformance));

        } finally {
            // Not testing persistence
            this.dataStorage.abort();
        }

    }

    /**
     * ImagerServiceImplTest.testStartedUnscheduledImaging
     * 
     * @throws BusinessException
     * 
     * @see org.pimslims.business.crystallization.service.ImagerServiceTest#testStartedUnscheduledImaging()
     */
    public void testStartedUnscheduledImaging() throws BusinessException {

        PlateType pt = new PlateType();
        pt.setColumns(12);
        pt.setName(UNIQUE + "-pt");
        pt.setReservoir(2);
        pt.setRows(8);
        pt.setSubPositions(2);

        try {
            this.dataStorage.openResources("administrator");
            TrialService ts = this.dataStorage.getTrialService();
            pt = ts.create(pt);
            ts.createTrialPlate(UNIQUE, pt);

            this.dataStorage.flush();

            ImagerService is = this.dataStorage.getImagerService();
            is.createInstrument(UNIQUE + "-imager", 21f);
            this.dataStorage.flush();

            Calendar now = Calendar.getInstance();

            setSqlLogToDebug();
            long prepped = System.currentTimeMillis();
            String imagingId = is.startedUnscheduledImaging(UNIQUE, now, now, UNIQUE + "-imager");
            this.dataStorage.flush();
            long written = System.currentTimeMillis();
            restoreDefaultLoggingLevel();

            Collection<ScheduleView> schedules = is.findSchedules(UNIQUE, "");
            assertNotNull(schedules);
            ScheduleView schedule = schedules.iterator().next();
            assertEquals(ImagerService.IMAGING_STATE_IMAGING, schedule.getState());
            assertEquals(schedule.getName(), imagingId);
            assertEquals(now, schedule.getDateToImage());
            assertEquals(now, schedule.getDateImaged());
            assertEquals(UNIQUE + "-imager", schedule.getImager());

            long writeThreshold = 100;
            long writePerformance = written - prepped;
            LOGGER.info("testStartedUnscheduledImaging(): Write performance = " + writePerformance + "ms");
            assertTrue("Write performance threshold exceeded (" + writeThreshold + "ms, " + writePerformance
                + "ms)", (writeThreshold >= writePerformance));

        } finally {
            // Not testing persistence
            this.dataStorage.abort();
        }

    }

    /**
     * ImagerServiceImplTest.testSkippedImaging
     * 
     * @throws BusinessException
     * 
     * @see org.pimslims.business.crystallization.service.ImagerServiceTest#testSkippedImaging()
     */
    public void testSkippedImaging() throws BusinessException {

        SchedulePlan sp = new SchedulePlan();
        List<SchedulePlanOffset> offsets = new ArrayList<SchedulePlanOffset>();
        sp.setName(UNIQUE + "-sp");
        sp.setOffsets(offsets);
        for (int i = 0; i < OFFSET_COUNT; i++) {
            SchedulePlanOffset spo = new SchedulePlanOffset();
            spo.setImagingNumber(i + 1);
            spo.setOffsetHoursFromTimeZero(i * 5);
            spo.setPriority((0 == i) ? 1 : i);
            spo.setSchedulePlan(sp);
            offsets.add(spo);
        }

        PlateType pt = new PlateType();
        pt.setColumns(12);
        pt.setName(UNIQUE + "-pt");
        pt.setReservoir(2);
        pt.setRows(8);
        pt.setSubPositions(2);
        // TODO pt.setDefaultSchedulePlan(sp);

        try {

            this.dataStorage.openResources("administrator");
            ScheduleService ss = this.dataStorage.getScheduleService();
            ss.create(sp);

            TrialService ts = this.dataStorage.getTrialService();
            pt = ts.create(pt, sp); // TODO ts.create(pt);
            ts.createTrialPlate(UNIQUE, pt);

            this.dataStorage.flush();

            ImagerService is = this.dataStorage.getImagerService();
            is.schedulePlate(UNIQUE, null);
            is.createInstrument(UNIQUE + "-imager", 21f);
            this.dataStorage.flush();

            Collection<ScheduleView> schedules = is.findSchedules(UNIQUE, "");
            assertNotNull(schedules);
            ScheduleView schedule = schedules.iterator().next();
            assertEquals(ImagerService.IMAGING_STATE_SCHEDULED, schedule.getState());

            setSqlLogToDebug();
            long prepped = System.currentTimeMillis();
            boolean ok = is.skippedImaging(UNIQUE, schedule.getDateToImage(), UNIQUE + "-imager");
            this.dataStorage.flush();
            long written = System.currentTimeMillis();
            restoreDefaultLoggingLevel();

            assertTrue(ok);
            schedules = is.findSchedules(UNIQUE, "");
            assertNotNull(schedules);
            schedule = schedules.iterator().next();
            assertEquals(ImagerService.IMAGING_STATE_SKIPPED, schedule.getState());
            assertEquals(UNIQUE + "-imager", schedule.getImager());

            long writeThreshold = 100;
            long writePerformance = written - prepped;
            LOGGER.info("testSkippedImaging(): Write performance = " + writePerformance + "ms");
            assertTrue("Write performance threshold exceeded (" + writeThreshold + "ms, " + writePerformance
                + "ms)", (writeThreshold >= writePerformance));

        } finally {
            // Not testing persistence
            this.dataStorage.abort();
        }

    }

    /**
     * ImagerServiceImplTest.testFinishedImaging
     * 
     * @see org.pimslims.business.crystallization.service.ImagerServiceTest#testFinishedImaging()
     */
    public void testFinishedImaging() throws BusinessException {

        PlateType pt = new PlateType();
        pt.setColumns(12);
        pt.setName(UNIQUE + "-pt");
        pt.setReservoir(2);
        pt.setRows(8);
        pt.setSubPositions(2);

        try {
            this.dataStorage.openResources("administrator");
            TrialService ts = this.dataStorage.getTrialService();
            pt = ts.create(pt);
            ts.createTrialPlate(UNIQUE, pt);

            this.dataStorage.flush();

            ImagerService is = this.dataStorage.getImagerService();
            is.createInstrument(UNIQUE + "-imager", 21f);
            this.dataStorage.flush();

            Calendar now = Calendar.getInstance();
            String imagingId = is.startedUnscheduledImaging(UNIQUE, now, now, UNIQUE + "-imager");

            this.dataStorage.flush();

            setSqlLogToDebug();
            long prepped = System.currentTimeMillis();
            boolean ok = is.finishedImaging(UNIQUE, imagingId, UNIQUE + "-imager");
            this.dataStorage.flush();
            long written = System.currentTimeMillis();
            restoreDefaultLoggingLevel();

            assertTrue(ok);
            Collection<ScheduleView> schedules = is.findSchedules(UNIQUE, "");
            assertNotNull(schedules);
            ScheduleView schedule = schedules.iterator().next();
            assertEquals(ImagerService.IMAGING_STATE_COMPLETED, schedule.getState());
            assertEquals(schedule.getName(), imagingId);
            assertEquals(UNIQUE + "-imager", schedule.getImager());

            long writeThreshold = 100;
            long writePerformance = written - prepped;
            LOGGER.info("testFinishedImaging(): Write performance = " + writePerformance + "ms");
            assertTrue("Write performance threshold exceeded (" + writeThreshold + "ms, " + writePerformance
                + "ms)", (writeThreshold >= writePerformance));

        } finally {
            // Not testing persistence
            this.dataStorage.abort();
        }

    }

    /**
     * ImagerServiceImplTest.testSetPriority
     * 
     * @throws BusinessException
     * 
     * @see org.pimslims.business.crystallization.service.ImagerServiceTest#testSetPriority()
     */
    public void testSetPriority() throws BusinessException {

        SchedulePlan sp = new SchedulePlan();
        List<SchedulePlanOffset> offsets = new ArrayList<SchedulePlanOffset>();
        sp.setName(UNIQUE + "-sp");
        sp.setOffsets(offsets);
        for (int i = 0; i < OFFSET_COUNT; i++) {
            SchedulePlanOffset spo = new SchedulePlanOffset();
            spo.setImagingNumber(i + 1);
            spo.setOffsetHoursFromTimeZero(i * 5);
            spo.setPriority((0 == i) ? 1 : i);
            spo.setSchedulePlan(sp);
            offsets.add(spo);
        }

        PlateType pt = new PlateType();
        pt.setColumns(12);
        pt.setName(UNIQUE + "-pt");
        pt.setReservoir(2);
        pt.setRows(8);
        pt.setSubPositions(2);
        // TODO pt.setDefaultSchedulePlan(sp);

        try {

            this.dataStorage.openResources("administrator");
            ScheduleService ss = this.dataStorage.getScheduleService();
            ss.create(sp);

            TrialService ts = this.dataStorage.getTrialService();
            ts.create(pt, sp); // TODO ts.create(pt);
            ts.createTrialPlate(UNIQUE, pt);

            this.dataStorage.flush();

            ImagerService is = this.dataStorage.getImagerService();
            is.schedulePlate(UNIQUE, null);
            this.dataStorage.flush();

            Collection<ScheduleView> schedules = is.findSchedules(UNIQUE, "");
            assertNotNull(schedules);
            ScheduleView schedule = schedules.iterator().next();
            assertEquals(1, schedule.getPriority());

            setSqlLogToDebug();
            long prepped = System.currentTimeMillis();
            boolean ok = is.setPriority(UNIQUE, schedule.getDateToImage(), "", 5);
            this.dataStorage.flush();
            long written = System.currentTimeMillis();
            restoreDefaultLoggingLevel();

            assertTrue(ok);
            schedules = is.findSchedules(UNIQUE, "");
            assertNotNull(schedules);
            schedule = schedules.iterator().next();
            assertEquals(5, schedule.getPriority());

            long writeThreshold = 100;
            long writePerformance = written - prepped;
            LOGGER.info("testSetPriority(): Write performance = " + writePerformance + "ms");
            assertTrue("Write performance threshold exceeded (" + writeThreshold + "ms, " + writePerformance
                + "ms)", (writeThreshold >= writePerformance));

        } finally {
            // Not testing persistence
            this.dataStorage.abort();
        }

    }

}
