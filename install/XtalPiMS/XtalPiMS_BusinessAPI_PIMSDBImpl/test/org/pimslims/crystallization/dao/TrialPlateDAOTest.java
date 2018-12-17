package org.pimslims.crystallization.dao;

import junit.framework.TestCase;

import org.pimslims.access.PIMSAccess;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.CrystalType;

public class TrialPlateDAOTest extends TestCase {

	private static final String UNIQUE = "tpd" + System.currentTimeMillis();
	private final AbstractModel model;

	public TrialPlateDAOTest(String name) {
		super(name);
		this.model = ModelImpl.getModel();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	// this test is based on the dimensions of the CrystalQuick square well
	// plate
	public void testInitTrialDrops() throws ConstraintException {
		WritableVersion version = this.model
				.getWritableVersion(PIMSAccess.ADMINISTRATOR);
		try {
			Holder holder = new Holder(version, UNIQUE);
			CrystalType holderType = new CrystalType(version,
					CrystalType.NO_RESERVOIR, UNIQUE);
			holderType.setMaxColumn(12);
			holderType.setMaxRow(8);
			holderType.setMaxSubPosition(3);
			holder.setHolderType(holderType);
			PlateType plateType = new PlateType();
			TrialPlate trialPlate = new TrialPlate(plateType);
			TrialPlateDAO.initTrialDrops(trialPlate, holder);
			assertEquals(3 * 12 * 8, trialPlate.getTrialDrops().size());
		} finally {
			version.abort();
		}

	}

}
