/** 
 * xtalpims-ws-stub uk.ac.ox.oppf.www.wsplate.impl TestPlateInfoProviderImpl.java
 * @author Jon
 * @date 26 Aug 2010
 *
 * Protein Information Management System
 * @version: 4.1
 *
 * Copyright (c) 2010 Jon 
 * The copyright holder has licenced the STFC to redistribute this software
 */
package uk.ac.ox.oppf.www.wsplate.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.persistence.Persistence;
import javax.sql.DataSource;

import junit.framework.TestCase;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.model.SchedulePlan;
import org.pimslims.business.crystallization.model.SchedulePlanOffset;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.service.ImagerService;
import org.pimslims.business.crystallization.service.ScheduleService;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.ScheduleView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.crystallization.implementation.ImagerServiceImpl;
import org.pimslims.crystallization.implementation.TrialServiceImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
// FIXME VMXi Bodge - no UserDB
//import org.pimslims.user.jndi.JNDIHelper;
//import org.pimslims.user.userdb.util.UserInfoProvider;

import uk.ac.ox.oppf.www.wsplate.GetImagingTasks;
import uk.ac.ox.oppf.www.wsplate.GetImagingTasksResponse;
import uk.ac.ox.oppf.www.wsplate.GetPlateIDError;
import uk.ac.ox.oppf.www.wsplate.ImagedPlate;
import uk.ac.ox.oppf.www.wsplate.ImagedPlateResponse;
import uk.ac.ox.oppf.www.wsplate.ImagingPlate;
import uk.ac.ox.oppf.www.wsplate.ImagingPlateError;
import uk.ac.ox.oppf.www.wsplate.ImagingPlateResponse;
import uk.ac.ox.oppf.www.wsplate.ImagingTask;
import uk.ac.ox.oppf.www.wsplate.Robot;
import uk.ac.ox.oppf.www.wsplate.SkippedImaging;
import uk.ac.ox.oppf.www.wsplate.SkippedImagingResponse;
import uk.ac.ox.oppf.www.wsplate.SupportsPriority;
import uk.ac.ox.oppf.www.wsplate.SupportsPriorityResponse;
import uk.ac.ox.oppf.www.wsplate.UpdatedPriority;
import uk.ac.ox.oppf.www.wsplate.UpdatedPriorityResponse;

/**
 * TestPlateInfoProviderImpl
 *
 */
public class TestImagingTaskProviderImpl extends TestCase {

    /**
     * The name of the DataSource as stated in persistence.xml.
     * Currently java:comp/env/jdbc/ISGO2008DB
     */
    public static final String DATA_SOURCE_NAME = "java:comp/env/jdbc/UserDB";
    
    /**
     * The DataSource to use
     */
    private static DataSource dataSource = null;
    
    /**
     * <p>
     * Unique string used to avoid name clashes.
     * </p>
     */
    private static String UNIQUE = "test" + System.currentTimeMillis();

    /**
     * Number of SchedulePlanOffsets with which to test.
     */
    private static final int OFFSET_COUNT = 10;

    /**
     * Initialize dataSource
     */
    private static void initializeDataSource() {

        BasicDataSource ds = new BasicDataSource();
        
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/userdb");
        ds.setUsername("userdb-user");
        ds.setPassword("changeMe");
        ds.setDefaultAutoCommit(false);
        ds.setInitialSize(1);

        dataSource = ds;

    }
    
    private final Robot robot = new Robot();
    private final SchedulePlan sp = new SchedulePlan();
    private final org.pimslims.business.crystallization.model.PlateType pt = new org.pimslims.business.crystallization.model.PlateType();
    
    /**
     * Constructor for TestPlateInfoProviderImpl 
     * @param name
     * @throws Exception 
     */
    public TestImagingTaskProviderImpl(String name) throws Exception {
        super(name);
        // FIXME VMXi Bodge - no UserDB
        //if (null == dataSource) {
        //    initializeDataSource();
        //}
        //JNDIHelper.initialize(DATA_SOURCE_NAME, dataSource);

        robot.setID("1");
        robot.setName(UNIQUE + "-imager");

        final List<SchedulePlanOffset> offsets = new ArrayList<SchedulePlanOffset>();
        sp.setName(UNIQUE + "-sp");
        sp.setOffsets(offsets);
        for (int i = 0; i < OFFSET_COUNT; i++) {
            final SchedulePlanOffset spo = new SchedulePlanOffset();
            spo.setImagingNumber(i + 1);
            spo.setOffsetHoursFromTimeZero(i * 5);
            spo.setPriority((0 == i) ? 1 : i);
            spo.setSchedulePlan(sp);
            offsets.add(spo);
        }

        pt.setColumns(12);
        pt.setName(UNIQUE + "-pt");
        pt.setReservoir(2);
        pt.setRows(8);
        pt.setSubPositions(2);
        // TODO pt.setDefaultSchedulePlan(sp);
    }

    /**
     * TestPlateInfoProviderImpl.setUp
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // FIXME VMXi Bodge - no UserDB
        //UserInfoProvider.setEmf(Persistence.createEntityManagerFactory("UserDB"));
    }

    /**
     * TestPlateInfoProviderImpl.tearDown
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        // FIXME VMXi Bodge - no UserDB
        //UserInfoProvider.getEmf().close();
        //UserInfoProvider.setEmf(null);
    }

    /**
     * <p>
     * Get the WritableVersion from this DataStorage.
     * </p>
     */
    protected WritableVersion getWritableVersion(DataStorage dataStorage) {
        return ((DataStorageImpl) dataStorage).getWritableVersion();
    }

    /**
     * Test method for {@link uk.ac.ox.oppf.www.wsplate.impl.PlateInfoProviderImpl#getPlateID(uk.ac.ox.oppf.www.wsplate.GetPlateID)}.
     * @throws BusinessException 
     * @throws ConstraintException 
     * @throws GetPlateIDError 
     */
    public void test_getImagingTasksScheduled() throws BusinessException, ConstraintException {
        final DataStorageFactoryProvider dsfp = new MockDataStorageFactoryProvider();
        final DataStorage dataStorage = dsfp.getDataStorageFactory().getDataStorage();

        try {

            dataStorage.openResources("administrator");

            final ScheduleService ss = dataStorage.getScheduleService();
            ss.create(sp);
            this.getWritableVersion(dataStorage).flush();

            final TrialService ts = dataStorage.getTrialService();
            ((TrialServiceImpl) ts).create(pt, sp); // TODO ts.create(pt);
            final TrialPlate tp = ts.createTrialPlate(UNIQUE, pt);
            getWritableVersion(dataStorage).flush();
            assertNotNull(tp.getCreateDate());

            final ImagerService is = dataStorage.getImagerService();
            is.schedulePlate(UNIQUE, robot.getName());
            getWritableVersion(dataStorage).flush();

            final GetImagingTasks request = new GetImagingTasks();
            request.setPlateID(UNIQUE);
            request.setRobot(robot);

            final ImagingTaskProviderImpl itp = new ImagingTaskProviderImpl(dsfp);
            final GetImagingTasksResponse response = itp._getImagingTasks(request, dataStorage);

            assertNotNull(response.getWrapper());
            assertNotNull(response.getWrapper().getItem());
            final ImagingTask[] tasks = response.getWrapper().getItem();
            assertEquals(10, tasks.length);

        }
        finally {
            dataStorage.abort();
        }
    }

    /**
     * Test method for {@link uk.ac.ox.oppf.www.wsplate.impl.PlateInfoProviderImpl#getPlateID(uk.ac.ox.oppf.www.wsplate.GetPlateID)}.
     * @throws BusinessException 
     * @throws ConstraintException 
     * @throws GetPlateIDError 
     */
    public void test_getImagingTasksUnscheduled() throws BusinessException, ConstraintException {
        final DataStorageFactoryProvider dsfp = new MockDataStorageFactoryProvider();
        final DataStorage dataStorage = dsfp.getDataStorageFactory().getDataStorage();

        try {

            dataStorage.openResources("administrator");

            final ScheduleService ss = dataStorage.getScheduleService();
            ss.create(sp);
            this.getWritableVersion(dataStorage).flush();

            final TrialService ts = dataStorage.getTrialService();
            ((TrialServiceImpl) ts).create(pt, sp); // TODO ts.create(pt);
            final TrialPlate tp = ts.createTrialPlate(UNIQUE, pt);
            getWritableVersion(dataStorage).flush();
            assertNotNull(tp.getCreateDate());

            final GetImagingTasks request = new GetImagingTasks();
            request.setPlateID(UNIQUE);
            request.setRobot(robot);

            final ImagingTaskProviderImpl itp = new ImagingTaskProviderImpl(dsfp);
            final GetImagingTasksResponse response = itp._getImagingTasks(request, dataStorage);

            assertNotNull(response.getWrapper());
            assertNotNull(response.getWrapper().getItem());
            final ImagingTask[] tasks = response.getWrapper().getItem();
            assertEquals(10, tasks.length);

        }
        finally {
            dataStorage.abort();
        }
    }

    /**
     * Test method for {@link uk.ac.ox.oppf.www.wsplate.impl.PlateInfoProviderImpl#getPlateID(uk.ac.ox.oppf.www.wsplate.GetPlateID)}.
     * @throws BusinessException 
     * @throws ConstraintException 
     * @throws GetPlateIDError 
     */
    public void test_imagingPlate_imagedPlate() throws BusinessException, ConstraintException {
        final DataStorageFactoryProvider dsfp = new MockDataStorageFactoryProvider();
        final DataStorage dataStorage = dsfp.getDataStorageFactory().getDataStorage();

        try {

            dataStorage.openResources("administrator");

            final ScheduleService ss = dataStorage.getScheduleService();
            ss.create(sp);
            this.getWritableVersion(dataStorage).flush();

            final TrialService ts = dataStorage.getTrialService();
            ((TrialServiceImpl) ts).create(pt, sp); // TODO ts.create(pt);
            final TrialPlate tp = ts.createTrialPlate(UNIQUE, pt);
            getWritableVersion(dataStorage).flush();
            assertNotNull(tp.getCreateDate());

            final ImagerService is = dataStorage.getImagerService();
            ((ImagerServiceImpl) is).createInstrument(robot.getName(), 21f);
            final Collection<ScheduleView> schedules = is.schedulePlate(UNIQUE, robot.getName());
            getWritableVersion(dataStorage).flush();

            final ScheduleView schedule = schedules.iterator().next();
            final Calendar dateImaged = Calendar.getInstance();

            final ImagingPlate request = new ImagingPlate();
            request.setPlateID(UNIQUE);
            request.setRobot(robot);
            request.setDateImaged(dateImaged);
            request.setDateToImage(schedule.getDateToImage());
            request.setScheduled(true);

            final ImagingTaskProviderImpl itp = new ImagingTaskProviderImpl(dsfp);
            final ImagingPlateResponse response = itp._imagingPlate(request, dataStorage);

            // FIXME VMXi Bodge - pimsimpl jar out of date
            //final String expected = ImagerServiceImpl.createName(UNIQUE, schedule.getDateToImage());
            final String expected = ((ImagerServiceImpl) is).createName(UNIQUE, schedule.getDateToImage());
            assertEquals(expected, response.getImagingPlateReturn());

            getWritableVersion(dataStorage).flush();

            final GetImagingTasks request2 = new GetImagingTasks();
            request2.setPlateID(UNIQUE);
            request2.setRobot(robot);

            final GetImagingTasksResponse response2 = itp._getImagingTasks(request2, dataStorage);

            assertNotNull(response2.getWrapper());
            assertNotNull(response2.getWrapper().getItem());
            final ImagingTask[] tasks = response2.getWrapper().getItem();
            assertEquals(10, tasks.length);
            assertEquals(ImagerService.IMAGING_STATE_IMAGING, tasks[0].getState());

            getWritableVersion(dataStorage).flush();

            final ImagedPlate request3 = new ImagedPlate();
            request3.setPlateID(UNIQUE);
            request3.setRobot(robot);
            request3.setImagingID(response.getImagingPlateReturn());

            final ImagedPlateResponse response3 = itp._imagedPlate(request3, dataStorage);

            assertEquals(true, response3.getImagedPlateReturn());

            getWritableVersion(dataStorage).flush();

            final GetImagingTasksResponse response4 = itp._getImagingTasks(request2, dataStorage);

            assertNotNull(response4.getWrapper());
            assertNotNull(response4.getWrapper().getItem());
            final ImagingTask[] tasks4 = response4.getWrapper().getItem();
            assertEquals(10, tasks4.length);
            assertEquals(ImagerService.IMAGING_STATE_COMPLETED, tasks4[0].getState());

        }
        finally {
            dataStorage.abort();
        }
    }

    /**
     * Test method for {@link uk.ac.ox.oppf.www.wsplate.impl.PlateInfoProviderImpl#getPlateID(uk.ac.ox.oppf.www.wsplate.GetPlateID)}.
     * @throws BusinessException 
     * @throws ConstraintException 
     * @throws GetPlateIDError 
     */
    public void test_skippedImaging() throws BusinessException, ConstraintException {
        final DataStorageFactoryProvider dsfp = new MockDataStorageFactoryProvider();
        final DataStorage dataStorage = dsfp.getDataStorageFactory().getDataStorage();

        try {

            dataStorage.openResources("administrator");

            final ScheduleService ss = dataStorage.getScheduleService();
            ss.create(sp);
            this.getWritableVersion(dataStorage).flush();

            final TrialService ts = dataStorage.getTrialService();
            ((TrialServiceImpl) ts).create(pt, sp); // TODO ts.create(pt);
            final TrialPlate tp = ts.createTrialPlate(UNIQUE, pt);
            getWritableVersion(dataStorage).flush();
            assertNotNull(tp.getCreateDate());

            final ImagerService is = dataStorage.getImagerService();
            ((ImagerServiceImpl) is).createInstrument(robot.getName(), 21f);
            final Collection<ScheduleView> schedules = is.schedulePlate(UNIQUE, robot.getName());
            getWritableVersion(dataStorage).flush();

            final ScheduleView schedule = schedules.iterator().next();

            final SkippedImaging request = new SkippedImaging();
            request.setPlateID(UNIQUE);
            request.setRobot(robot);
            request.setDateToImage(schedule.getDateToImage());

            final ImagingTaskProviderImpl itp = new ImagingTaskProviderImpl(dsfp);
            final SkippedImagingResponse response = itp._skippedImaging(request, dataStorage);

            assertEquals(true, response.getSkippedPlateReturn());

            getWritableVersion(dataStorage).flush();

            final GetImagingTasks request2 = new GetImagingTasks();
            request2.setPlateID(UNIQUE);
            request2.setRobot(robot);

            final GetImagingTasksResponse response2 = itp._getImagingTasks(request2, dataStorage);

            assertNotNull(response2.getWrapper());
            assertNotNull(response2.getWrapper().getItem());
            final ImagingTask[] tasks = response2.getWrapper().getItem();
            assertEquals(10, tasks.length);
            assertEquals(ImagerService.IMAGING_STATE_SKIPPED, tasks[0].getState());

        }
        finally {
            dataStorage.abort();
        }
    }

    /**
     * Test method for {@link uk.ac.ox.oppf.www.wsplate.impl.PlateInfoProviderImpl#getPlateID(uk.ac.ox.oppf.www.wsplate.GetPlateID)}.
     * @throws BusinessException 
     * @throws BusinessException 
     * @throws GetPlateIDError 
     */
    public void test_supportsPriority() throws BusinessException {
        final DataStorageFactoryProvider dsfp = new MockDataStorageFactoryProvider();
        final DataStorage dataStorage = dsfp.getDataStorageFactory().getDataStorage();

        try {

            dataStorage.openResources("administrator");

            final SupportsPriority request = new SupportsPriority();
            request.setRobot(robot);

            final ImagingTaskProviderImpl itp = new ImagingTaskProviderImpl(dsfp);
            final SupportsPriorityResponse response = itp._supportsPriority(request, dataStorage);

            assertEquals(true, response.getSupportsPriorityReturn());

        }
        finally {
            dataStorage.abort();
        }
    }

    /**
     * Test method for {@link uk.ac.ox.oppf.www.wsplate.impl.PlateInfoProviderImpl#getPlateID(uk.ac.ox.oppf.www.wsplate.GetPlateID)}.
     * @throws BusinessException 
     * @throws ConstraintException 
     * @throws GetPlateIDError 
     */
    public void test_updatedPriority() throws ImagingPlateError, BusinessException, ConstraintException {
        final DataStorageFactoryProvider dsfp = new MockDataStorageFactoryProvider();
        final DataStorage dataStorage = dsfp.getDataStorageFactory().getDataStorage();

        try {

            dataStorage.openResources("administrator");

            final ScheduleService ss = dataStorage.getScheduleService();
            ss.create(sp);
            this.getWritableVersion(dataStorage).flush();

            final TrialService ts = dataStorage.getTrialService();
            ((TrialServiceImpl) ts).create(pt, sp); // TODO ts.create(pt);
            final TrialPlate tp = ts.createTrialPlate(UNIQUE, pt);
            getWritableVersion(dataStorage).flush();
            assertNotNull(tp.getCreateDate());

            final ImagerService is = dataStorage.getImagerService();
            ((ImagerServiceImpl) is).createInstrument(robot.getName(), 21f);
            final Collection<ScheduleView> schedules = is.schedulePlate(UNIQUE, robot.getName());
            getWritableVersion(dataStorage).flush();

            final ScheduleView schedule = schedules.iterator().next();

            final UpdatedPriority request = new UpdatedPriority();
            request.setPlateID(UNIQUE);
            request.setRobot(robot);
            request.setDateToImage(schedule.getDateToImage());
            request.setPriority(7);

            final ImagingTaskProviderImpl itp = new ImagingTaskProviderImpl(dsfp);
            final UpdatedPriorityResponse response = itp._updatedPriority(request, dataStorage);
            assertEquals(true, response.getUpdatedPriorityReturn());

            getWritableVersion(dataStorage).flush();

            final GetImagingTasks request2 = new GetImagingTasks();
            request2.setPlateID(UNIQUE);
            request2.setRobot(robot);

            final GetImagingTasksResponse response2 = itp._getImagingTasks(request2, dataStorage);

            assertNotNull(response2.getWrapper());
            assertNotNull(response2.getWrapper().getItem());
            final ImagingTask[] tasks = response2.getWrapper().getItem();
            assertEquals(10, tasks.length);
            assertEquals(7, tasks[0].getPriority());

        }
        finally {
            dataStorage.abort();
        }
    }

}
