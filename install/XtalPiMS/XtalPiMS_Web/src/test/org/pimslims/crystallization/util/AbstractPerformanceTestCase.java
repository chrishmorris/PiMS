package org.pimslims.crystallization.util;

import java.util.logging.Logger;

import junit.framework.TestCase;

import org.pimslims.business.DataStorage;
import org.pimslims.business.exception.BusinessException;

public abstract class AbstractPerformanceTestCase extends TestCase {
	Logger log;

	DataStorage pimsDS;

	public static void oneTimeSetup() throws Exception {
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		log = getLogger();
	}

	public void testPiMSDBCall() throws BusinessException {
		final DataStorage pimsDS = DataStorageFactory.getPiMSDS();
		pimsDS.openResources("administrator");
		try {
			final long start = System.currentTimeMillis();
			_testCall(pimsDS);
			log.info("PiMSDB Using " + (System.currentTimeMillis() - start)
					/ 1000.0 + "s");
		} finally {
			pimsDS.closeResources();
		}

	}

	/*
	 * public void testPlateDBCall() throws BusinessException { final
	 * DataStorage plateDS = DataStorageFactory.getPlateDS();
	 * plateDS.openResources("administrator", true, true); try { final long
	 * start = System.currentTimeMillis(); _testCall(plateDS);
	 * log.info("PlateDB Using " + (System.currentTimeMillis() - start) / 1000.0
	 * + "s"); } catch (final BusinessException e) { log.error(e.getMessage(),
	 * e); fail(); } finally { plateDS.closeResources(); } }
	 */
	public abstract void _testCall(DataStorage dataStorage)
			throws BusinessException;

	public abstract Logger getLogger();
}
