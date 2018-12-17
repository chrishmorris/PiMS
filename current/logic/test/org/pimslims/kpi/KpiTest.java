package org.pimslims.kpi;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.kpi.BarChartBean.BarBean;
import org.pimslims.kpi.BarChartBean.SliceBean;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;
import org.pimslims.report.ExperimentReport;

public class KpiTest extends TestCase {

	private static final String QUERY = "select experiment from Experiment as experiment where experiment.experimentType=:type";
	private static final String UNIQUE = "kpi" + System.currentTimeMillis();
	private static final Calendar NOW = Calendar.getInstance();
	final AbstractModel model;

	/**
	 * @param name
	 */
	public KpiTest(String name) {
		super(name);
		this.model = ModelImpl.getModel();
	}

	public void testAttritionFailureCount() throws ConstraintException {
		WritableVersion version = model.getTestVersion();
		try {
			ExperimentType type = new ExperimentType(version, UNIQUE + "one");
			Calendar then = Calendar.getInstance();
			then.setTimeInMillis(NOW.getTimeInMillis() - 24 * 60 * 60 * 1000);
			Experiment before = new Experiment(version, UNIQUE + "before",
					then, then, type);
			Sample sample = new Sample(version, UNIQUE);
			new OutputSample(version, before).setSample(sample);
			Experiment after = new Experiment(version, UNIQUE, NOW, NOW, type);
			after.setStatus("Failed");
			after.setProtocol(new Protocol(version, UNIQUE, type));
			new InputSample(version, after).setSample(sample);

			BarChartBean chart = getChart(version, type);

			assertEquals("http://" + UNIQUE, chart.getURL());
			assertEquals(1, chart.getBars().length);
			BarBean bar = chart.getBars()[0];
			// assertEquals(24f, bar.getWidth());
			assertEquals(2, bar.getSlices().length);
			assertEquals(0f, bar.getSlices()[0].getLength());
			SliceBean slice = bar.getSlices()[1];
			assertEquals(1f, slice.getLength());
			assertEquals("red", slice.getColor());
		} finally {
			version.abort();
		}
	}

	/**
	 * KpiTest.getChart
	 * 
	 * @param version
	 * @param type
	 * @return
	 */
	private BarChartBean getChart(WritableVersion version, ExperimentType type) {
		Map<String, Object> parameters = new HashMap();
		parameters.put(Experiment.PROP_EXPERIMENTTYPE, type);
		ExperimentReport report = new ExperimentReport(version, parameters,
				null);
		BarChartBean chart = report.getAttrition("http://" + UNIQUE);
		return chart;
	}

	/**
	 * KpiTest.testAttritionDisclosure
	 * 
	 * @throws ConstraintException
	 * @throws AccessException
	 * @throws AbortedException
	 * 
	 *             Test that ordinary user can see own data
	 */
	public void testAttritionDisclosure() throws ConstraintException,
			AccessException, AbortedException {
		// set up: a user who can't read own writing
		String username = UNIQUE;
		WritableVersion version = model
				.getWritableVersion(Access.ADMINISTRATOR);
		String readable = UNIQUE + "ro";
		try {
			UserGroup userGroup = new UserGroup(version, UNIQUE);
			LabNotebook book = new LabNotebook(version, readable);
			new Permission(version, "read", book, userGroup);
			new Permission(version, "create", book, userGroup);
			new Permission(version, "update", book, userGroup);
			userGroup.addMemberUser(new User(version, username));
			new ExperimentType(version, UNIQUE);

			version.commit();
		} finally {
			if (!version.isCompleted())
				version.abort();
		}

		version = model.getWritableVersion(username);
		try {

			Calendar then = Calendar.getInstance();
			then.setTimeInMillis(NOW.getTimeInMillis() - 24 * 60 * 60 * 1000);
			ExperimentType type = version.findFirst(ExperimentType.class,
					"name", UNIQUE);
			Experiment before = new Experiment(version, UNIQUE + "before",
					then, then, type);
			Sample sample = new Sample(version, UNIQUE);
			new OutputSample(version, before).setSample(sample);
			Experiment after = new Experiment(version, UNIQUE, NOW, NOW, type);
			after.setStatus("Failed");
			after.setProtocol(new Protocol(version, UNIQUE, type));
			new InputSample(version, after).setSample(sample);
			BarChartBean chart = getChart(version, type);
			assertEquals("http://" + UNIQUE, chart.getURL());
			assertEquals(1, chart.getBars().length);
		} finally {
			version.abort();
		}

		// tidy up
		version = model.getWritableVersion(Access.ADMINISTRATOR);
		try {
			User user = version.findFirst(User.class, User.PROP_NAME, username);
			version.delete(user.getUserGroups());
			user.delete();
			version.findFirst(ExperimentType.class, "name", UNIQUE).delete();
			version.findFirst(LabNotebook.class, "name", readable).delete();
			version.commit();
		} finally {
			if (!version.isCompleted())
				version.abort();
		}

	}

	/**
	 * KpiTest.testAttritionDisclosure
	 * 
	 * @throws ConstraintException
	 * @throws AccessException
	 * @throws AbortedException
	 * 
	 *             Checks that data that should be hidden, is hidden
	 */
	public void testAttritionNonDisclosure() throws ConstraintException,
			AccessException, AbortedException {
		// set up: a user who can't read own writing
		String username = UNIQUE + "nd";
		WritableVersion version = model
				.getWritableVersion(Access.ADMINISTRATOR);
		String readable = UNIQUE + "rond";
		String writable = UNIQUE + "wond";
		try {
			UserGroup userGroup = new UserGroup(version, UNIQUE + "ug2nd");
			new Permission(version, "read", new LabNotebook(version, readable),
					userGroup);
			LabNotebook writeOnly = new LabNotebook(version, writable);
			new Permission(version, "create", writeOnly, userGroup);
			new Permission(version, "update", writeOnly, userGroup);
			userGroup.addMemberUser(new User(version, username));
			new ExperimentType(version, UNIQUE);

			version.commit();
		} finally {
			if (!version.isCompleted())
				version.abort();
		}

		version = model.getWritableVersion(username);
		try {

			Calendar then = Calendar.getInstance();
			then.setTimeInMillis(NOW.getTimeInMillis() - 24 * 60 * 60 * 1000);
			ExperimentType type = version.findFirst(ExperimentType.class,
					"name", UNIQUE);
			Experiment before = new Experiment(version, UNIQUE + "before",
					then, then, type);
			Sample sample = new Sample(version, UNIQUE);
			new OutputSample(version, before).setSample(sample);
			Experiment after = new Experiment(version, UNIQUE, NOW, NOW, type);
			after.setStatus("Failed");
			after.setProtocol(new Protocol(version, UNIQUE, type));
			new InputSample(version, after).setSample(sample);
			BarChartBean chart = getChart(version, type);
			assertEquals("http://" + UNIQUE, chart.getURL());
			assertEquals(0, chart.getBars().length);
		} finally {
			version.abort();
		}

		// tidy up
		version = model.getWritableVersion(Access.ADMINISTRATOR);
		try {
			User user = version.findFirst(User.class, User.PROP_NAME, username);
			version.delete(user.getUserGroups());
			user.delete();
			version.findFirst(ExperimentType.class, "name", UNIQUE).delete();
			version.findFirst(LabNotebook.class, "name", readable).delete();
			version.findFirst(LabNotebook.class, "name", writable).delete();
			version.commit();
		} finally {
			if (!version.isCompleted())
				version.abort();
		}

	}

	public void testAttritionDelayAndSuccessCount() throws ConstraintException {
		WritableVersion version = model.getTestVersion();
		try {
			ExperimentType type = new ExperimentType(version, UNIQUE + "one");
			Calendar then = Calendar.getInstance();
			then.setTimeInMillis(NOW.getTimeInMillis() - 24 * 60 * 60 * 1000);
			Experiment before = new Experiment(version, UNIQUE + "before",
					then, then, type);
			Sample sample = new Sample(version, UNIQUE);
			new OutputSample(version, before).setSample(sample);
			Experiment after = new Experiment(version, UNIQUE, NOW, NOW, type);
			after.setStatus("OK");
			after.setProtocol(new Protocol(version, UNIQUE, type));
			new InputSample(version, after).setSample(sample);
			BarChartBean chart = getChart(version, type);
			assertEquals("http://" + UNIQUE, chart.getURL());
			assertEquals(1, chart.getBars().length);
			BarBean bar = chart.getBars()[0];
			assertEquals(24f, bar.getWidth());
			assertEquals(2, bar.getSlices().length);
			assertEquals(0f, bar.getSlices()[1].getLength());
			SliceBean slice = bar.getSlices()[0];
			assertEquals(1f, slice.getLength());
			assertEquals("green", slice.getColor());
		} finally {
			version.abort();
		}
	}

	/**
	 * KpiTest.testAttritionSort
	 * 
	 * @throws ConstraintException
	 * 
	 *             Verifies that the bars appear in pipeline order
	 */
	public void testAttritionSort() throws ConstraintException {
		WritableVersion version = model.getTestVersion();
		try {
			ExperimentType type = new ExperimentType(version, UNIQUE + "one");
			Calendar then = Calendar.getInstance();
			then.setTimeInMillis(NOW.getTimeInMillis() - 24 * 60 * 60 * 1000);
			Experiment before = new Experiment(version, UNIQUE + "before",
					then, then, type);
			Sample sample = new Sample(version, UNIQUE);
			new OutputSample(version, before).setSample(sample);

			// First experiments in report. note protocol name is late in
			// alphameric order:
			Protocol protocol = new Protocol(version, "z" + UNIQUE, type);
			Experiment after = new Experiment(version, UNIQUE, NOW, NOW, type);
			after.setStatus("Failed");
			after.setProtocol(protocol);
			new InputSample(version, after).setSample(sample);
			Experiment afterOk = new Experiment(version, UNIQUE + "ok", NOW,
					NOW, type);
			afterOk.setStatus("OK");
			afterOk.setProtocol(protocol);
			new InputSample(version, afterOk).setSample(sample);
			Sample okSample = new Sample(version, UNIQUE + "ok");
			new OutputSample(version, before).setSample(okSample);

			// last experiments in report
			Calendar later = Calendar.getInstance();
			then.setTimeInMillis(NOW.getTimeInMillis() + 24 * 60 * 60 * 1000);
			Experiment last = new Experiment(version, UNIQUE + "last", later,
					later, type);
			last.setStatus("OK");
			// note protocol name is early in alphameric order:
			last.setProtocol(new Protocol(version, "a" + UNIQUE, type));
			new InputSample(version, last).setSample(okSample);
			BarChartBean chart = getChart(version, type);
			assertEquals("http://" + UNIQUE, chart.getURL());
			assertEquals(2, chart.getBars().length);
			BarBean bar = chart.getBars()[0];
			assertEquals(protocol.get_Name(), bar.getCaption());
			assertEquals(24f, bar.getWidth());
			assertEquals(2, bar.getSlices().length);
			assertEquals(1f, bar.getSlices()[1].getLength());
			SliceBean slice = bar.getSlices()[0];
			assertEquals(1f, slice.getLength());
		} finally {
			version.abort();
		}
	}

	// TODO test security, sort
}
