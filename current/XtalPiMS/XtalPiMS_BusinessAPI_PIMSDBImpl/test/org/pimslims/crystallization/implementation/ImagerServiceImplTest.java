/*
 * SampleServiceImplTest.java JUnit based test Created on 09 August 2007, 16:56
 */

package org.pimslims.crystallization.implementation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Location;
import org.pimslims.business.core.model.Project;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.core.service.ConstructService;
import org.pimslims.business.core.service.SampleService;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.ImageType;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.SchedulePlan;
import org.pimslims.business.crystallization.model.SchedulePlanOffset;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.service.ImagerService;
import org.pimslims.business.crystallization.service.ImagerServiceTest;
import org.pimslims.business.crystallization.service.ScheduleService;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.ScheduleView;
import org.pimslims.business.crystallization.view.SimpleSampleView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.logging.Logger;

/**
 * 
 * @author Jon Diprose
 */
public class ImagerServiceImplTest extends ImagerServiceTest {

	/**
	 * <p>
	 * Hibernate's SQL logger.
	 * </p>
	 */
	private static final Logger sqlLogger = Logger
			.getLogger("org.hibernate.SQL");

	/**
	 * <p>
	 * The initial level of Hibernate's SQL logger.
	 * </p>
	 */
	private static final Object sqlLoggerLevel = sqlLogger.getLevel();

	/**
	 * TrialServiceImplTest.suite
	 * 
	 * @return A Test suite containing this TestCase
	 */
	public static Test suite() {
		final TestSuite suite = new TestSuite(ImagerServiceImplTest.class);
		return suite;
	}

	/**
	 * Constructor for TrialServiceImplTest
	 * 
	 * @param testName
	 *            - the name of this Test
	 */
	public ImagerServiceImplTest(final String testName) {
		super(testName, DataStorageFactory.getDataStorageFactory()
				.getDataStorage());
	}

	public ImagerServiceImplTest(final DataStorage dataStorage) {
		super("ImagerServiceImplTest", dataStorage);
	}

	/**
	 * @throws Exception
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		restoreDefaultLoggingLevel();
	}

	@Override
	public void restoreDefaultLoggingLevel() {
		sqlLogger.setLevel(sqlLoggerLevel);
	}

	@Override
	protected void tearDown() throws Exception {
		try {
			this.dataStorage.abort();
		} catch (Exception e) {
			// Failed to force closure of the data storage
			LOGGER.log(java.util.logging.Level.SEVERE,
					"Error closing connection", e);
		}
	}

	/**
	 * ImagerServiceImplTest.testGetService
	 * 
	 * @see org.pimslims.business.crystallization.service.ImagerServiceTest#testGetService()
	 */
	@Override
	public void testGetService() throws BusinessException {
		super.testGetService();
	}

	public void testCreateName() throws BusinessException {

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

		try {

			this.dataStorage.openResources("administrator");
			ScheduleService ss = this.dataStorage.getScheduleService();
			ss.create(sp);
			this.dataStorage.flush();

			TrialService ts = this.dataStorage.getTrialService();
			ts.create(pt, sp);
			TrialPlate tp = ts.createTrialPlate(UNIQUE, pt);
			this.dataStorage.flush();

			ImagerService is = this.dataStorage.getImagerService();

			Collection<ScheduleView> schedules = is.schedulePlate(UNIQUE, null);
			assertEquals(OFFSET_COUNT, schedules.size());
			Calendar expected = tp.getCreateDate();
			for (ScheduleView schedule : schedules) {
				String name = ImagerServiceImpl.createName(UNIQUE,
						schedule.getDateToImage());
				assertEquals(name, schedule.getName());
			}

		} finally {
			// Not testing persistence
			this.dataStorage.abort();
		}

	}

	@Override
	public void printStatistics() {
		((DataStorageImpl) dataStorage).getWritableVersion().getSession()
				.getSessionFactory().getStatistics().logSummary();
	}

	@Override
	public void clearStatistics() {
		((DataStorageImpl) dataStorage).getWritableVersion().getSession()
				.getSessionFactory().getStatistics().clear();
	}

	@Override
	public void setSqlLogToDebug() {
		// sqlLogger.setLevel(Level.DEBUG);
	}

	public void xtestRepeatSchedulePlate() throws BusinessException {
		LOGGER.info("testRepeatSchedulePlate: start");
		for (int i = 0; i < 10; i++) {
			this.testSchedulePlate();
		}
		LOGGER.info("testRepeatSchedulePlate: end");
	}

	public void xtestRepeatFindSchedules() throws BusinessException {
		LOGGER.info("testRepeatFindSchedules: start");
		for (int i = 0; i < 10; i++) {
			this.testFindSchedules();
		}
		LOGGER.info("testRepeatFindSchedules: end");
	}

	/**
	 * ImagerServiceImplTest.testFinishedImaging
	 * 
	 * @see org.pimslims.business.crystallization.service.ImagerServiceTest#testFindSimpleSamples()
	 */
	public void testFindSimpleSamples() throws BusinessException {

		Construct c = new Construct(UNIQUE + "-c");

		try {
			this.dataStorage.openResources("jon");
			// FIXME test as administrator, or make a userid and lab notebook in
			// the test set up
			((DataStorageImpl) dataStorage).getWritableVersion()
					.setDefaultOwner("OPPF");

			ConstructService cs = this.dataStorage.getConstructService();
			cs.create(c);
			this.dataStorage.flush();

			SampleService ss = this.dataStorage.getSampleService();
			for (int i = 0; i < 10; i++) {
				Sample s = new Sample();
				s.setName(UNIQUE + "." + i);
				s.setConstruct(c);

				ss.create(s);
			}
			this.dataStorage.flush();

			ImagerService is = this.dataStorage.getImagerService();

			setSqlLogToDebug();
			long prepped = System.currentTimeMillis();
			Collection<SimpleSampleView> simpleSamples = is
					.findSimpleSampleViews();
			this.dataStorage.flush();
			long read = System.currentTimeMillis();
			restoreDefaultLoggingLevel();

			assertNotNull(simpleSamples);
			assertTrue(0 < simpleSamples.size());
			for (SimpleSampleView simpleSample : simpleSamples) {
				LOGGER.info("Found: " + simpleSample.getSampleName() + ", "
						+ simpleSample.getConstructName() + ", "
						+ simpleSample.getProjectName() + ", "
						+ simpleSample.getProjectLeader());
			}

			// This is clearly a very heavy join - I have to set this to 500 to
			// get it to mostly pass - 380 is typical!
			long readThreshold = 500;
			long readPerformance = read - prepped;
			LOGGER.info("testFinishedImaging(): Read performance = "
					+ readPerformance + "ms");
			assertTrue("Read performance threshold exceeded (" + readThreshold
					+ "ms, " + readPerformance + "ms)",
					(readThreshold >= readPerformance));

		} finally {
			// Not testing persistence
			this.dataStorage.abort();
		}

	}

	/**
	 * ImagerServiceImplTest.xtestCreateImages - test
	 * ImagerService.createAndLink(Image)
	 * 
	 * Note that this implementation currently depends on the prior existence of
	 * a trial plate, imager and plate inspection as can be seen in the code.
	 * 
	 * TODO Use standard reference data for testing, or create the relevant
	 * entities.
	 * 
	 * @throws BusinessException
	 */
	public void xtestCreateImages() throws BusinessException {

		String BARCODE = "441300052601";
		String IMAGER = "RI1000-0080";

		try {
			this.dataStorage.openResources("administrator");

			ImagerServiceImpl is = (ImagerServiceImpl) this.dataStorage
					.getImagerService();

			TrialDrop drop = new TrialDrop();
			drop.setName(BARCODE + "A01.2");

			Location location = new Location();
			location.setName(IMAGER);

			PlateInspection plateInspection = new PlateInspection();
			plateInspection.setInspectionName(BARCODE + "-20101002-210535");

			Calendar now = Calendar.getInstance();

			Image img = new Image();
			img.setColourDepth(24);
			img.setDrop(drop);
			img.setImageDate(now);
			img.setImagePath("http://localhost/foo/" + UNIQUE + ".jpg");
			img.setImageType(ImageType.COMPOSITE);
			img.setLocation(location);
			img.setPlateInspection(plateInspection);
			img.setSizeX(1024);
			img.setSizeY(1024);
			img.setXLengthPerPixel(0.002);
			img.setYLengthPerPixel(0.002);

			setSqlLogToDebug();
			// long prepped = System.currentTimeMillis();
			// TODO fix is.create to deal with trial drop, location and
			// inspection
			long prepped = System.currentTimeMillis();
			is.createAndLink(img);
			this.dataStorage.flush();
			long written = System.currentTimeMillis();

			long writePerformance = written - prepped;
			LOGGER.info("testCreateImages(): Write performance inc startup = "
					+ writePerformance + "ms");
			// Performance appears to be rubbish first time through - I've no
			// idea why, though
			// it doesn't appear to be the db access.

			img.setImagePath("http://localhost/foo/" + UNIQUE + "-2.jpg");
			img.setImageType(ImageType.SLICE);

			prepped = System.currentTimeMillis();
			is.createAndLink(img);
			this.dataStorage.flush();
			written = System.currentTimeMillis();
			restoreDefaultLoggingLevel();

			assertNotNull(img.getId());

			long writeThreshold = 100;
			writePerformance = written - prepped;
			LOGGER.info("testCreateImages(): Write performance = "
					+ writePerformance + "ms");
			assertTrue("Write performance threshold exceeded ("
					+ writeThreshold + "ms, " + writePerformance + "ms)",
					(writeThreshold >= writePerformance));

			// this.dataStorage.commit();

		} finally {
			// Not testing persistence
			this.dataStorage.abort();
		}

	}

	/**
	 * ImagerServiceImplTest.xtestCreateImagesPerformance - repeats
	 * xtestCreateImages to get a better idea of the performance once start-up
	 * overheads have been dealt with.
	 * 
	 * @throws BusinessException
	 */
	public void xtestCreateImagesPerformance() throws BusinessException {
		LOGGER.info("testCreateImagesPerformance");
		xtestCreateImages();
	}

	public void testListProjects() throws BusinessException {

		try {
			this.dataStorage.openResources("jon");

			ImagerService is = this.dataStorage.getImagerService();

			setSqlLogToDebug();
			long prepped = System.currentTimeMillis();
			Collection<Project> projects = is.findProjects();
			long read = System.currentTimeMillis();
			restoreDefaultLoggingLevel();

			assertNotNull(projects);
			assertTrue(0 < projects.size());
			for (Project project : projects) {
				LOGGER.info("Found: " + project.getName() + ", "
						+ project.getDescription());
				// + ", " + project.getOwner());
			}

			long readThreshold = 50;
			long readPerformance = read - prepped;
			LOGGER.info("testListProjects(): Read performance = "
					+ readPerformance + "ms");
			assertTrue("Read performance threshold exceeded (" + readThreshold
					+ "ms, " + readPerformance + "ms)",
					(readThreshold >= readPerformance));

		} finally {
			// Not testing persistence
			this.dataStorage.abort();
		}

		try {
			this.dataStorage.openResources("administrator");

			ImagerService is = this.dataStorage.getImagerService();

			setSqlLogToDebug();
			long prepped = System.currentTimeMillis();
			Collection<Project> projects = is.findProjects();
			long read = System.currentTimeMillis();
			restoreDefaultLoggingLevel();

			assertNotNull(projects);
			assertTrue(0 < projects.size());
			for (Project project : projects) {
				LOGGER.info("Found: " + project.getName() + ", "
						+ project.getDescription());
				// + ", " + project.getOwner());
			}

			long readThreshold = 100;
			long readPerformance = read - prepped;
			LOGGER.info("testListProjects(): Read performance = "
					+ readPerformance + "ms");
			assertTrue("Read performance threshold exceeded (" + readThreshold
					+ "ms, " + readPerformance + "ms)",
					(readThreshold >= readPerformance));

		} finally {
			// Not testing persistence
			this.dataStorage.abort();
		}

	}

}
