package org.pimslims.crystallization;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.crystallization.CrystalHarvest.CrystalCoordinate;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;

public class CrystalHarvestTest extends TestCase {

	private static final Calendar NOW = Calendar.getInstance();
	private static final String UNIQUE = "ch" + System.currentTimeMillis();
	private final AbstractModel model;

	public CrystalHarvestTest(String name) {
		super(name);
		this.model = ModelImpl.getModel();
	}

	public void testModel() throws ClassNotFoundException,
			NoSuchMethodException, SecurityException {
		Class clazz = Class.forName("javax.persistence.EntityManager");
		Method method = clazz.getMethod("unwrap", Class.class);
		assertNotNull(method);
	}

	public void testFindNoXYs() throws ConstraintException {
		WritableVersion version = this.model
				.getWritableVersion(Access.ADMINISTRATOR);
		try {
			ExperimentType type = new ExperimentType(version, UNIQUE);
			Experiment trial = new Experiment(version, UNIQUE, NOW, NOW, type);
			List<CrystalCoordinate> xys = CrystalHarvest.getXYs(trial);
			assertEquals(0, xys.size());
		} finally {
			version.abort();
		}

	}

	public void testNullR() throws ConstraintException {
		WritableVersion version = this.model
				.getWritableVersion(Access.ADMINISTRATOR);
		try {
			ExperimentType type = new ExperimentType(version, UNIQUE);
			Experiment trial = new Experiment(version, UNIQUE + "trial", NOW,
					NOW, type);
			Sample drop = new Sample(version, UNIQUE + "drop");
			new OutputSample(version, trial).setSample(drop);

			ExperimentType mountType = version.findFirst(ExperimentType.class,
					ExperimentType.PROP_NAME, CrystalHarvest.EXPERIMENT_TYPE);
			assertNotNull(mountType);
			Experiment mount = new Experiment(version, UNIQUE + "mount", NOW,
					NOW, mountType);
			new InputSample(version, mount).setSample(drop);
			Sample crystal = new Sample(version, UNIQUE + "crysal");
			new OutputSample(version, mount).setSample(crystal);
			new Parameter(version, mount).setName("x");
			new Parameter(version, mount).setName("y");
			new Parameter(version, mount).setName("r");

			List<CrystalCoordinate> xys = CrystalHarvest.getXYs(trial);
			assertEquals(1, xys.size());
			CrystalCoordinate xy = xys.iterator().next();
			assertEquals(mount.get_Hook(), xy.hook);
			assertEquals(null, xy.x);
			assertEquals(null, xy.y);
			assertEquals(null, xy.r);
		} finally {
			version.abort();
		}

	}

	public void testXY() throws ConstraintException {
		WritableVersion version = this.model
				.getWritableVersion(Access.ADMINISTRATOR);
		try {
			ExperimentType type = new ExperimentType(version, UNIQUE);
			Experiment trial = new Experiment(version, UNIQUE + "trial", NOW,
					NOW, type);
			Sample drop = new Sample(version, UNIQUE + "drop");
			new OutputSample(version, trial).setSample(drop);

			ExperimentType mountType = version.findFirst(ExperimentType.class,
					ExperimentType.PROP_NAME, CrystalHarvest.EXPERIMENT_TYPE);
			assertNotNull(mountType);
			Experiment mount = new Experiment(version, UNIQUE + "mount", NOW,
					NOW, mountType);
			new InputSample(version, mount).setSample(drop);
			Sample crystal = new Sample(version, UNIQUE + "crysal");
			new OutputSample(version, mount).setSample(crystal);
			Parameter x = new Parameter(version, mount);
			x.setName("x");
			x.setValue("100");
			Parameter y = new Parameter(version, mount);
			y.setName("y");
			y.setValue("200");
			new Parameter(version, mount).setName("r");

			List<CrystalCoordinate> xys = CrystalHarvest.getXYs(trial);
			assertEquals(1, xys.size());
			CrystalCoordinate xy = xys.iterator().next();
			assertEquals(mount.get_Hook(), xy.hook);
			assertEquals(new Integer(100), xy.x);
			assertEquals(new Integer(200), xy.y);
		} finally {
			version.abort();
		}

	}

}
