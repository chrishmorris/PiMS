/**
 * xtalPiMS_Web org.pimslims.crystallization.servlet IspybResultsTest.java
 * 
 * @author cm65
 * @date 13 Dec 2011
 * 
 *       Protein Information Management System
 * @version: 4.3
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.servlet;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;
import org.pimslims.properties.PropertyGetter;

/**
 * IspybResultsTest
 * 
 */
public class CrystalTreatmentTest extends TestCase {

	private final AbstractModel model;
	private static final String UNIQUE = "c" + System.currentTimeMillis();
	private static final Calendar NOW = Calendar.getInstance();

	/**
	 * Constructor for IspybResultsTest
	 * 
	 * @param name
	 */
	public CrystalTreatmentTest(String name) {
		super(name);
		this.model = org.pimslims.dao.ModelImpl.getModel();
		PropertyGetter.setWorkingDirectory("web/WEB-INF/");
	}

	public void testCopyTreatmentNoChain() throws ConstraintException,
			AccessException {
		WritableVersion version = this.model
				.getWritableVersion(Access.ADMINISTRATOR);
		try {
			Map<String, Experiment> experiments = getSourceAndDestinationExperiments(version);
			Experiment source = (Experiment) experiments
					.get("sourceExperiment");
			Experiment destination = (Experiment) experiments
					.get("destinationExperiment");
			CrystalTreatmentServlet.doCopyTreatmentHistory(source, destination,
					version);
			fail("Should throw a BusinessException '"
					+ CrystalTreatmentServlet.COPYTREATMENT_NO_CHAIN + "'");
		} catch (BusinessException e) {
			String msg = e.getMessage();
			Assert.assertEquals(CrystalTreatmentServlet.COPYTREATMENT_NO_CHAIN,
					msg);
		} finally {
			version.abort();
		}
	}

	public void testCopyOneTreatment() throws ConstraintException,
			AccessException, BusinessException {
		WritableVersion version = this.model
				.getWritableVersion(Access.ADMINISTRATOR);
		try {
			Map<String, Experiment> experiments = getSourceAndDestinationExperiments(version);
			Experiment source = (Experiment) experiments
					.get("sourceExperiment");
			Sample sample = getOutput(source);

			ExperimentType type = new ExperimentType(version, UNIQUE + "et");
			Experiment treatment = new Experiment(version, UNIQUE + "ex2", NOW,
					NOW, type);
			InputSample inputSample = new InputSample(version, treatment);
			inputSample.setName("Crystal");
			inputSample.setSample(sample);
			new OutputSample(version, treatment).setSample(new Sample(version,
					UNIQUE + "t1out"));

			Experiment destination = (Experiment) experiments
					.get("destinationExperiment");
			CrystalTreatmentServlet.doCopyTreatmentHistory(source, destination,
					version);

			// Now check the results
			Sample destOutput = getOutput(destination);
			assertEquals(1, destOutput.getInputSamples().size());

			Experiment destTreatment = destOutput.getInputSamples().iterator()
					.next().getExperiment();
			assertFalse(destTreatment.equals(treatment));

		} finally {
			version.abort();
		}
	}

	private Sample getOutput(Experiment source) {
		Sample sample = source.getOutputSamples().iterator().next().getSample();
		return sample;
	}

	public void testCopyTreatmentSameSourceAndDestination() {
		WritableVersion version = this.model
				.getWritableVersion(Access.ADMINISTRATOR);
		try {
			Map<String, Experiment> experiments = getSourceAndDestinationExperiments(version);
			Experiment source = (Experiment) experiments
					.get("sourceExperiment");
			CrystalTreatmentServlet.doCopyTreatmentHistory(source, source,
					version);
			fail("Should throw a BusinessException '"
					+ CrystalTreatmentServlet.COPYTREATMENT_SAME_EXPERIMENTS
					+ "'");
		} catch (ConstraintException e) {
			e.printStackTrace();
		} catch (AccessException e) {
			e.printStackTrace();
		} catch (BusinessException e) {
			String msg = e.getMessage();
			Assert.assertEquals(
					CrystalTreatmentServlet.COPYTREATMENT_SAME_EXPERIMENTS, msg);
		} finally {
			version.abort();
		}
	}

	public void testCopyTreatmentSourceIsNotSelectionExperiment()
			throws ConstraintException, AccessException {
		WritableVersion version = this.model
				.getWritableVersion(Access.ADMINISTRATOR);
		try {
			Map<String, Experiment> experiments = getSourceAndDestinationExperiments(version);
			Experiment source = (Experiment) experiments
					.get("sourceExperiment");
			Experiment destination = (Experiment) experiments
					.get("destinationExperiment");
			final ExperimentType type = new ExperimentType(version,
					CrystalTreatmentTest.UNIQUE + "badExptType");
			Protocol protocol = new Protocol(version,
					CrystalTreatmentTest.UNIQUE + "badprotocol", type);
			source.setProtocol(protocol);
			CrystalTreatmentServlet.doCopyTreatmentHistory(source, destination,
					version);
			fail("Should throw a BusinessException '"
					+ CrystalTreatmentServlet.COPYTREATMENT_BAD_SOURCE + "'");
		} catch (BusinessException e) {
			String msg = e.getMessage();
			Assert.assertEquals(
					CrystalTreatmentServlet.COPYTREATMENT_BAD_SOURCE, msg);
		} finally {
			version.abort();
		}
	}

	public void testCopyTreatmentDestinationIsNotSelectionExperiment()
			throws ConstraintException, AccessException {
		WritableVersion version = this.model
				.getWritableVersion(Access.ADMINISTRATOR);
		try {
			Map<String, Experiment> experiments = getSourceAndDestinationExperiments(version);
			Experiment source = (Experiment) experiments
					.get("sourceExperiment");
			Experiment destination = (Experiment) experiments
					.get("destinationExperiment");
			final ExperimentType type = new ExperimentType(version,
					CrystalTreatmentTest.UNIQUE + "badExptType");
			Protocol protocol = new Protocol(version,
					CrystalTreatmentTest.UNIQUE + "badprotocol", type);
			destination.setProtocol(protocol);
			CrystalTreatmentServlet.doCopyTreatmentHistory(source, destination,
					version);
			fail("Should throw a BusinessException '"
					+ CrystalTreatmentServlet.COPYTREATMENT_BAD_DESTINATION
					+ "'");
		} catch (BusinessException e) {
			String msg = e.getMessage();
			Assert.assertEquals(
					CrystalTreatmentServlet.COPYTREATMENT_BAD_DESTINATION, msg);
		} finally {
			version.abort();
		}
	}

	// public void testCopyTreatmentOneExperiment() throws ConstraintException,
	// AccessException, BusinessException {
	// WritableVersion version = this.model
	// .getWritableVersion(Access.ADMINISTRATOR);
	// try {
	// Map<String, Experiment> experiments =
	// getSourceAndDestinationExperiments(version);
	// Experiment source = (Experiment) experiments
	// .get("sourceExperiment");
	// Experiment destination = (Experiment) experiments
	// .get("destinationExperiment");
	//
	// final ExperimentType type = new ExperimentType(version,
	// CrystalTreatmentTest.UNIQUE + "exptType2");
	// final Protocol protocol = new Protocol(version,
	// CrystalTreatmentTest.UNIQUE + "prot2", type);
	//
	// // add the chained experiment
	//
	// CrystalTreatmentServlet.doCopyTreatmentHistory(source, destination,
	// version);
	// // assert that copy exists
	// // assert that its input sample is the DESTINATION's output, not the
	// // source's
	// // assert that its parameters have been copied
	//
	// } finally {
	// version.abort();
	// }
	// }

	private Map<String, Experiment> getSourceAndDestinationExperiments(
			WritableVersion version) throws ConstraintException,
			AccessException {
		final ExperimentType type = new ExperimentType(version,
				CrystalTreatmentTest.UNIQUE + "exptType");
		Map<String, Object> protocolAttr = new HashMap<String, Object>();
		protocolAttr.put(Protocol.PROP_NAME, SelectCrystalServlet.SELECTION_PROTOCOL);
		final Protocol protocol = version.findFirst(Protocol.class,
				protocolAttr);
		final Sample sourceCrystal = new Sample(version,
				CrystalTreatmentTest.UNIQUE + "srcSample");
		final Sample destinationCrystal = new Sample(version,
				CrystalTreatmentTest.UNIQUE + "destSample");
		final Map<String, Object> attributes = new java.util.HashMap<String, Object>();
		attributes.put(Experiment.PROP_EXPERIMENTTYPE, type);
		attributes.put(Experiment.PROP_STARTDATE, new GregorianCalendar());
		attributes.put(Experiment.PROP_ENDDATE, new GregorianCalendar());
		attributes.put(Experiment.PROP_NAME, CrystalTreatmentTest.UNIQUE
				+ "sourceExpt");

		final Experiment source = version.create(Experiment.class, attributes);
		OutputSample out = new OutputSample(version, source);
		out.setSample(sourceCrystal);

		attributes.put(Experiment.PROP_NAME, CrystalTreatmentTest.UNIQUE
				+ "destinationExpt");
		final Experiment destination = version.create(Experiment.class,
				attributes);
		out = new OutputSample(version, destination);
		out.setSample(destinationCrystal);

		source.setProtocol(protocol);
		destination.setProtocol(protocol);
		Map<String, Experiment> objects = new HashMap<String, Experiment>();
		objects.put("sourceExperiment", source);
		objects.put("destinationExperiment", destination);
		return objects;
	}

}
