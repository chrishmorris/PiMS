/**
 * tools org.pimslims.command.DataUpdate ExperimentConnectorTest.java
 * 
 * @author cm65
 * @date 14 Jun 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.command.DataUpdate;

import java.util.Calendar;

import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;

/**
 * ExperimentConnectorTest
 * 
 */
public class ExperimentConnectorTest extends TestCase {

    private static final String UNIQUE = "ec" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    /**
     * Constructor for ExperimentConnectorTest
     * 
     * @param name
     */
    public ExperimentConnectorTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testParameter() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            ExperimentType type = new ExperimentType(version, UNIQUE + "t");
            Experiment experiment = new Experiment(version, UNIQUE, NOW, NOW, type);
            Parameter parameter = new Parameter(version, experiment);
            parameter.setName(UNIQUE);
            Protocol protocol = new Protocol(version, UNIQUE + "p", type);
            experiment.setProtocol(protocol);
            ParameterDefinition pd =
                new ParameterDefinition(version, parameter.getName(), "String", protocol);

            ExperimentConnector.fixParameters(version);

            assertEquals(pd, parameter.getParameterDefinition());

        } finally {
            version.abort();
        }
    }

    public void testInput() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            ExperimentType type = new ExperimentType(version, UNIQUE + "t");
            Experiment experiment = new Experiment(version, UNIQUE, NOW, NOW, type);
            InputSample is = new InputSample(version, experiment);
            is.setName(UNIQUE);
            Protocol protocol = new Protocol(version, UNIQUE + "p", type);
            experiment.setProtocol(protocol);
            SampleCategory category = new SampleCategory(version, UNIQUE + "c");
            RefInputSample ris = new RefInputSample(version, category, protocol);
            ris.setName(is.getName());

            ExperimentConnector.fixInputs(version);

            assertEquals(ris, is.getRefInputSample());

        } finally {
            version.abort();
        }
    }

    public void testOutput() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            ExperimentType type = new ExperimentType(version, UNIQUE + "t");
            Experiment experiment = new Experiment(version, UNIQUE, NOW, NOW, type);
            OutputSample os = new OutputSample(version, experiment);
            os.setName(UNIQUE);
            Protocol protocol = new Protocol(version, UNIQUE + "p", type);
            experiment.setProtocol(protocol);
            SampleCategory category = new SampleCategory(version, UNIQUE + "c");
            RefOutputSample ros = new RefOutputSample(version, category, protocol);
            ros.setName(os.getName());

            ExperimentConnector.fixOutputs(version);

            assertEquals(ros, os.getRefOutputSample());

        } finally {
            version.abort();
        }
    }

}
