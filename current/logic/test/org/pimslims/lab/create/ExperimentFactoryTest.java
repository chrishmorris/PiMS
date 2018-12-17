package org.pimslims.lab.create;

import java.util.Calendar;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;

public class ExperimentFactoryTest extends TestCase {

	private static final String UNIQUE = "ef" + System.currentTimeMillis();
	private static final Calendar NOW = Calendar.getInstance();
	private final AbstractModel model;

	/**
	 * Constructor for ExperimentFactoryTest
	 * 
	 * @param name
	 */
	public ExperimentFactoryTest(String name) {
		super(name);
		this.model = ModelImpl.getModel();
	}

	public void testCreateProtocolParametersForExperiment()
			throws ConstraintException, AccessException {
		WritableVersion version = this.model.getTestVersion();
		try {
			ExperimentType type = new ExperimentType(version, UNIQUE);
			Protocol protocol = new Protocol(version, UNIQUE, type);
			ParameterDefinition pd = new ParameterDefinition(version, UNIQUE,
					"String", protocol);
			Experiment experiment = new Experiment(version, UNIQUE, NOW, NOW,
					type);
			ExperimentFactory.createProtocolParametersForExperiment(version,
					protocol, experiment);
			Parameter parm = experiment.findFirst(Experiment.PROP_PARAMETERS,
					Parameter.PROP_NAME, pd.getName());
			assertNotNull(parm);
			assertEquals(pd, parm.getParameterDefinition());
		} finally {
			version.abort();
		}
	}

	public void testPropagateParametersForExperiment()
			throws ConstraintException, AccessException {
		WritableVersion version = this.model.getTestVersion();
		try {
			ExperimentType type = new ExperimentType(version, UNIQUE);
			Protocol protocol1 = new Protocol(version, UNIQUE + "one", type);
			ParameterDefinition pd1 = new ParameterDefinition(version, UNIQUE,
					"String", protocol1);
			Experiment experiment1 = new Experiment(version, UNIQUE + "one",
					NOW, NOW,
					type);
			ExperimentFactory.createProtocolParametersForExperiment(version,
					protocol1, experiment1);
			Parameter parm1 = experiment1.findFirst(Experiment.PROP_PARAMETERS,
					Parameter.PROP_NAME, pd1.getName());
			parm1.setValue("UNIQUE");
			Sample sample = new Sample(version, UNIQUE);
			new OutputSample(version, experiment1).setSample(sample);

			Protocol protocol = new Protocol(version, UNIQUE, type);
			ParameterDefinition pd = new ParameterDefinition(version, UNIQUE,
					"String", protocol);
			Experiment experiment = new Experiment(version, UNIQUE, NOW, NOW,
					type);
			new InputSample(version, experiment).setSample(sample);
			ExperimentFactory.createProtocolParametersForExperiment(version,
					protocol, experiment);
			Parameter parm = experiment.findFirst(Experiment.PROP_PARAMETERS,
					Parameter.PROP_NAME, pd.getName());
			assertNotNull(parm);
			assertEquals(pd, parm.getParameterDefinition());
			assertEquals(parm1.getValue(), parm.getValue());
		} finally {
			version.abort();
		}
	}

}
