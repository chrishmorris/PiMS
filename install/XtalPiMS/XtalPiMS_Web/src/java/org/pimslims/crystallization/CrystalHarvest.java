package org.pimslims.crystallization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.pimslims.business.exception.BusinessException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.sample.Sample;

public class CrystalHarvest {

	public static final String EXPERIMENT_TYPE = "Crystal";

	public static final class CrystalCoordinate implements Comparable {

		public final Integer x;
		public final Integer y;
		public final Integer r;
		public final Integer crystalNumber;
		public final int dbid;
		public final String hook;

		public CrystalCoordinate(Integer x, Integer y, Integer r,
				Integer crystalNumber, String hook) {
			super();
			this.x = x;
			this.y = y;
			this.r = r;
			this.crystalNumber = crystalNumber;
			this.hook = hook;
			String[] parts = hook.split(":");
			this.dbid = Integer.parseInt(parts[1]);
		}

		public String getX() {
			return x.toString();
	}

		public String getY() {
			return y.toString();
		}

		public String getR() {
			return r.toString();
		}

		public String getCrystalNumber() {
			return crystalNumber.toString();
		}

		public String getHook() {
			return hook;
		}

		public int getDbId() {
			return dbid;
		}

		public String toString() {
			return "x: " + x + ", y:" + y + ", r:" + r + ", hook:" + hook;
		}

		public int compareTo(Object o) {
			CrystalCoordinate cc1 = (CrystalCoordinate) o;
			return this.getDbId() - cc1.getDbId();
		}

	}

	public static List<CrystalCoordinate> getXYs(Experiment trial) {
		List<CrystalCoordinate> ret = new ArrayList();
		Set<OutputSample> oss = trial.getOutputSamples();
		for (Iterator iterator = oss.iterator(); iterator.hasNext();) {
			OutputSample os = (OutputSample) iterator.next();
			Sample drop = os.getSample();
			if (null != drop) {
				Set<InputSample> iss = drop.getInputSamples();
				for (Iterator iterator2 = iss.iterator(); iterator2.hasNext();) {
					InputSample is = (InputSample) iterator2.next();
					Experiment mount = is.getExperiment();
					if (!EXPERIMENT_TYPE.equals(mount.getExperimentType()
							.getName())) {
						// TODO what if they have added cryoprotectant to well?
						continue;
					}
					Integer x = getInteger(mount, "x");
					Integer y = getInteger(mount, "y");
					Integer r = getInteger(mount, "r");
					Integer crystalNumber = getInteger(mount, "Crystal number");
					CrystalCoordinate xy = new CrystalCoordinate(x, y, r,
							crystalNumber, mount.get_Hook());
					ret.add(xy);
				}
			}
		}
		if (ret.size() > 1) {
			Collections.sort(ret);
		}
		return ret;
	}

	public static CrystalCoordinate getSingleXY(Experiment selection)
			throws BusinessException {
		if (!EXPERIMENT_TYPE.equals(selection.getExperimentType().getName())) {
			// TODO what if they have added cryoprotectant to well?
			throw new BusinessException("Supplied experiment is not of type \""
					+ EXPERIMENT_TYPE + "\"");
		}
		Integer x = getInteger(selection, "x");
		Integer y = getInteger(selection, "y");
		Integer r = getInteger(selection, "r");
		Integer crystalNumber = getInteger(selection, "Crystal number");
		CrystalCoordinate xy = new CrystalCoordinate(x, y, r, crystalNumber,
				selection.get_Hook());
		return xy;
	}

	private static Integer getInteger(Experiment mount, String name) {
		Parameter parm = mount.findFirst(Experiment.PROP_PARAMETERS,
				Parameter.PROP_NAME, name);
		String value = parm.getValue();
		if (null == parm || null == value || "".equals(value)) {
			return null;
		}

		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new RuntimeException("Bad record for: " + name + " was: "
					+ value);
		}

	}

}
