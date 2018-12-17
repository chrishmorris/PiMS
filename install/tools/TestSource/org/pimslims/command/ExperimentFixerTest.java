/**
 * pims-tools org.pimslims.command MissingInputFixerTest.java
 * 
 * @author cm65
 * @date 9 Feb 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.command;

import java.util.Calendar;
import java.util.Collection;

import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;

/**
 * MissingInputFixerTest
 * 
 */
public class ExperimentFixerTest extends TestCase {

    private static final String UNIQUE = "mif" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    AbstractModel model = ModelImpl.getModel();

    /**
     * Constructor for MissingInputFixerTest
     * 
     * @param name
     */
    public ExperimentFixerTest(String name) {
        super(name);
    }

    public void testNoBadExperiments() {
        ReadableVersion version = this.model.getReadableVersion(Access.ADMINISTRATOR);
        try {
            Collection<Experiment> bad = ExperimentFixer.findBadExperiments(version, 1);
            assertEquals(0, bad.size());
        } finally {
            version.abort();
        }
    }

    public void testFindOneBadInput() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            makeOneBadInput(version, "a");
            makeOneBadInput(version, "b");
            Collection<Experiment> bad = ExperimentFixer.findBadExperiments(version, 1);
            assertFalse(bad.isEmpty());
        } finally {
            version.abort();
        }
    }

    public void testFindOneBadOutput() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            makeOneBadOutput(version, "a");
            makeOneBadOutput(version, "b");
            Collection<Experiment> bad = ExperimentFixer.findBadExperiments(version, 1);
            assertFalse(bad.isEmpty());
        } finally {
            version.abort();
        }
    }

    public void testFixInput() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Experiment experiment = makeOneBadInput(version, "in");
            ExperimentFixer.fixExperiment(experiment);
            Collection<Experiment> bad = ExperimentFixer.findBadExperiments(version, 1);
            assertFalse(bad.contains(experiment));
        } finally {
            version.abort();
        }
    }

    public void testFixOutput() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Experiment experiment = makeOneBadOutput(version, "in");
            ExperimentFixer.fixExperiment(experiment);
            Collection<Experiment> bad = ExperimentFixer.findBadExperiments(version, 1);
            assertFalse(bad.contains(experiment));
        } finally {
            version.abort();
        }
    }

    /**
     * MissingInputFixerTest.makeOneBadExperiment
     * 
     * @param version
     * @throws ConstraintException
     */
    private Experiment makeOneBadInput(WritableVersion version, String suffix) throws ConstraintException {
        ExperimentType type = new ExperimentType(version, UNIQUE + suffix);
        Experiment ret = new Experiment(version, UNIQUE + suffix, NOW, NOW, type);
        Protocol protocol = new Protocol(version, UNIQUE + suffix, type);
        SampleCategory category = new SampleCategory(version, UNIQUE + suffix);
        new RefInputSample(version, category, protocol);
        ret.setProtocol(protocol);
        return ret;
    }

    private Experiment makeOneBadOutput(WritableVersion version, String suffix) throws ConstraintException {
        ExperimentType type = new ExperimentType(version, UNIQUE + suffix);
        Experiment ret = new Experiment(version, UNIQUE + suffix, NOW, NOW, type);
        Protocol protocol = new Protocol(version, UNIQUE + suffix, type);
        SampleCategory category = new SampleCategory(version, UNIQUE + suffix);
        new RefOutputSample(version, category, protocol);
        ret.setProtocol(protocol);
        new OutputSample(version, ret);
        return ret;
    }

}
