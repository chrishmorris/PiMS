/**
 * current-pims-web org.pimslims.utils.graph.implementation ExperimentAndSampleModelTest.java
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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.graph.GraphGenerationException;
import org.pimslims.graph.INode;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.properties.PropertyGetter;

/**
 * ExperimentAndSampleModelTest See also ExperimentModelTest
 */
public class ExperimentAndSampleModelTest extends TestCase {

    private static final String UNIQUE = "esm" + System.currentTimeMillis();

    private static final Calendar NOW = java.util.Calendar.getInstance();

    private final AbstractModel model;

    /**
     * @param methodName
     */
    public ExperimentAndSampleModelTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.utils.graph.implementation.ExperimentAndSampleModel#getExperimentAndSampleModel(org.pimslims.metamodel.ModelObject)}
     * .
     * 
     * @throws ConstraintException
     */
    public final void testSample() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Sample sample = new Sample(version, ExperimentAndSampleModelTest.UNIQUE);
            final SampleCategory category =
                new SampleCategory(version, ExperimentAndSampleModelTest.UNIQUE + "cate");
            sample.addSampleCategory(category);
            final AbstractGraphModel model = new ExperimentAndSampleModel();
            final org.pimslims.graph.IGraph graph = model.createGraphModel(sample);
            Assert.assertEquals(1, graph.getNodes().size());
            final INode node = graph.getNodes().iterator().next();
            Assert.assertEquals(node.getId(), sample.get_Hook());
            Assert.assertEquals(category.getName(), node.getCluster());

            Assert.assertNotNull(graph);

        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.utils.graph.implementation.ExperimentAndSampleModel#getExperimentAndSampleModel(org.pimslims.metamodel.ModelObject)}
     * .
     * 
     * @throws ConstraintException
     */
    public final void testExperiment() throws ConstraintException, GraphGenerationException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ExperimentAndSampleModelTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, ExperimentAndSampleModelTest.UNIQUE,
                    ExperimentAndSampleModelTest.NOW, ExperimentAndSampleModelTest.NOW, type);
            final AbstractGraphModel model = new ExperimentAndSampleModel();
            final org.pimslims.graph.IGraph graph = model.createGraphModel(experiment);

            Assert.assertNotNull(graph);
            Assert.assertEquals(1, graph.getNodes().size());
            final String dotInput =
                org.pimslims.servlet.DotServlet
                    .generateGraphModelAndInput(experiment, "/View/", 1, 800, 1024);
            Assert.assertNotNull(dotInput);
            Assert.assertTrue(dotInput, dotInput.contains("color = red"));
            //Assert.assertTrue(dotInput, dotInput.contains("fillcolor = white"));
            Assert.assertTrue(dotInput, dotInput.contains("javascript:"));
        } finally {
            version.abort();
        }
    }

    // not in ExperimentModelTest
    public final void testOutput() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ExperimentAndSampleModelTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, ExperimentAndSampleModelTest.UNIQUE,
                    ExperimentAndSampleModelTest.NOW, ExperimentAndSampleModelTest.NOW, type);
            final Sample sample = new Sample(version, ExperimentAndSampleModelTest.UNIQUE);
            new OutputSample(version, experiment).setSample(sample);
            final AbstractGraphModel graph =
                new ExperimentAndSampleModel(Collections.singleton((ModelObject) experiment),
                    AbstractGraphModel.MAX_NODES);
            Assert.assertNotNull(graph);
            Assert.assertEquals(1, graph.getEdges().size());
            Assert.assertEquals(1, graph.getLinks().size());
        } finally {
            version.abort();
        }
    }

    public final void testInput() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ExperimentAndSampleModelTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, ExperimentAndSampleModelTest.UNIQUE,
                    ExperimentAndSampleModelTest.NOW, ExperimentAndSampleModelTest.NOW, type);
            final Sample sample = new Sample(version, ExperimentAndSampleModelTest.UNIQUE);
            new InputSample(version, experiment).setSample(sample);
            new OutputSample(version, experiment).setSample(sample);
            final AbstractGraphModel graph =
                new ExperimentAndSampleModel(Collections.singleton((ModelObject) sample),
                    AbstractGraphModel.MAX_NODES);
            Assert.assertNotNull(graph);
            Assert.assertEquals(2, graph.getEdges().size());
            Assert.assertEquals(2, graph.getLinks().size());
        } finally {
            version.abort();
        }
    }

    public final void testFlagExperiment() throws ConstraintException, GraphGenerationException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ExperimentAndSampleModelTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, ExperimentAndSampleModelTest.UNIQUE,
                    ExperimentAndSampleModelTest.NOW, ExperimentAndSampleModelTest.NOW, type);
            final Sample sample = new Sample(version, ExperimentAndSampleModelTest.UNIQUE);
            final Sample sample2 = new Sample(version, ExperimentAndSampleModelTest.UNIQUE + "out");
            new InputSample(version, experiment).setSample(sample);
            new OutputSample(version, experiment).setSample(sample2);

            final String dotInput =
                org.pimslims.servlet.DotServlet.generateGraphModelAndInput(sample2, "/View/", 1, 800, 1024);
            Assert.assertNotNull(dotInput);
            //Assert.assertTrue(dotInput, dotInput.contains("springgreen"));

        } finally {
            version.abort();
        }
    }

    public void testFile() throws IOException {
        PropertyGetter.setWorkingDirectory("WebContent/WEB-INF/");
        final File file = PropertyGetter.getFileProperty("sample_icon", "diagram/sample.gif");
        //System.out.println(file.getAbsolutePath());
        new FileReader(file).close();
    }
}
