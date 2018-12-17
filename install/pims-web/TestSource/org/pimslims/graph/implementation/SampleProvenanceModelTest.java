/**
 * current-pims-web org.pimslims.utils.graph.implementation ExperimentModelTest.java
 * 
 * @author cm65
 * @date 22 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
 */
package org.pimslims.graph.implementation;

import java.util.Calendar;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.graph.GraphGenerationException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;

/**
 * ExperimentModelTest See also ExperimentModelTest
 */
public class SampleProvenanceModelTest extends TestCase {

    private static final String UNIQUE = "em" + System.currentTimeMillis();

    private static final Calendar NOW = java.util.Calendar.getInstance();

    private final AbstractModel model;

    /**
     * @param methodName
     */
    public SampleProvenanceModelTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.utils.graph.implementation.ExperimentModel#getExperimentModel(org.pimslims.metamodel.ModelObject)}
     * .
     * 
     * @throws ConstraintException
     */
    public final void testSample() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Sample sample = new Sample(version, SampleProvenanceModelTest.UNIQUE);
            final SampleProvenanceModel model = new SampleProvenanceModel(sample);
            final org.pimslims.graph.IGraph graph = model.createGraphModel(sample);

            Assert.assertNotNull(graph);

        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.utils.graph.implementation.ExperimentModel#getExperimentModel(org.pimslims.metamodel.ModelObject)}
     * .
     * 
     * @throws ConstraintException
     */
    public final void testExperiment() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, SampleProvenanceModelTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, SampleProvenanceModelTest.UNIQUE, SampleProvenanceModelTest.NOW,
                    SampleProvenanceModelTest.NOW, type);
            final SampleProvenanceModel model = new SampleProvenanceModel(experiment);
            final org.pimslims.graph.IGraph graph = model.createGraphModel(experiment);

            Assert.assertNotNull(graph);

        } finally {
            version.abort();
        }
    }

    public final void testOutput() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, SampleProvenanceModelTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, SampleProvenanceModelTest.UNIQUE, SampleProvenanceModelTest.NOW,
                    SampleProvenanceModelTest.NOW, type);
            final Sample sample2 = new Sample(version, SampleProvenanceModelTest.UNIQUE + "out");
            new OutputSample(version, experiment).setSample(sample2);
            final SampleProvenanceModel graph = new SampleProvenanceModel(sample2);
            graph.createGraphModel(sample2);
            Assert.assertNotNull(graph);
            Assert.assertEquals(1, graph.getEdges().size());
            Assert.assertEquals(1, graph.getLinks().size());
        } finally {
            version.abort();
        }
    }

    public final void testFlagExperiment() throws ConstraintException, GraphGenerationException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, SampleProvenanceModelTest.UNIQUE);
            final Experiment lastExperiment =
                new Experiment(version, SampleProvenanceModelTest.UNIQUE, SampleProvenanceModelTest.NOW,
                    SampleProvenanceModelTest.NOW, type);
            final Sample firstSample = new Sample(version, SampleProvenanceModelTest.UNIQUE);
            final Sample lastSample = new Sample(version, SampleProvenanceModelTest.UNIQUE + "out");
            new InputSample(version, lastExperiment).setSample(firstSample);
            new OutputSample(version, lastExperiment).setSample(lastSample);

            final Experiment firstExperiment =
                new Experiment(version, SampleProvenanceModelTest.UNIQUE + "first",
                    SampleProvenanceModelTest.NOW, SampleProvenanceModelTest.NOW, type);
            new OutputSample(version, firstExperiment).setSample(firstSample);

            version.flush();
            SampleProvenanceModel graph = new SampleProvenanceModel(lastSample);
            graph.createGraphModel(lastSample);
            Assert.assertEquals(2, graph.getEdges().size());
            Assert.assertEquals(3, graph.getNodes().size());

            graph = new SampleProvenanceModel(lastSample, 1);
            graph.createGraphModel(lastSample);
            Assert.assertNotNull(graph);
            Assert.assertEquals(1, graph.getEdges().size());
            Assert.assertEquals(2, graph.getNodes().size());

        } finally {
            version.abort();
        }
    }

    public final void testSideBranch() throws ConstraintException, GraphGenerationException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, SampleProvenanceModelTest.UNIQUE);
            final Experiment lastExperiment =
                new Experiment(version, SampleProvenanceModelTest.UNIQUE, SampleProvenanceModelTest.NOW,
                    SampleProvenanceModelTest.NOW, type);
            final Sample firstSample = new Sample(version, SampleProvenanceModelTest.UNIQUE);
            final Sample lastSample = new Sample(version, SampleProvenanceModelTest.UNIQUE + "out");
            new InputSample(version, lastExperiment).setSample(firstSample);
            new OutputSample(version, lastExperiment).setSample(lastSample);

            final Experiment firstExperiment =
                new Experiment(version, SampleProvenanceModelTest.UNIQUE + "first",
                    SampleProvenanceModelTest.NOW, SampleProvenanceModelTest.NOW, type);
            new OutputSample(version, firstExperiment).setSample(firstSample);

            // make a failed branch from starting sample, should be ignored
            final Experiment experiment2 =
                new Experiment(version, SampleProvenanceModelTest.UNIQUE + "two",
                    SampleProvenanceModelTest.NOW, SampleProvenanceModelTest.NOW, type);
            experiment2.setStatus("Failed");
            new OutputSample(version, experiment2).setSample(new Sample(version,
                SampleProvenanceModelTest.UNIQUE + "bad"));
            new InputSample(version, experiment2).setSample(firstSample);

            version.flush();

            final SampleProvenanceModel graph = new SampleProvenanceModel(lastSample);
            graph.createGraphModel(lastSample);
            Assert.assertNotNull(graph);
            Assert.assertEquals(2, graph.getEdges().size());
            Assert.assertEquals(3, graph.getNodes().size());

        } finally {
            version.abort();
        }
    }

}
