/**
 * V4_3-web org.pimslims.graph.implementation ProtocolGraphModelTest.java
 * 
 * @author cm65
 * @date 24 Feb 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.graph.implementation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.graph.IEdge;
import org.pimslims.graph.IGraph;
import org.pimslims.graph.INode;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.protocol.Workflow;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;

/**
 * ProtocolGraphModelTest
 * 
 */
public class ProtocolGraphModelTest extends TestCase {

    private static final String UNIQUE = "pgm" + System.currentTimeMillis();

    private final AbstractModel model;

    public ProtocolGraphModelTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testOneProtocol() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ProtocolGraphModelTest.UNIQUE);
            final Protocol protocol = new Protocol(version, ProtocolGraphModelTest.UNIQUE, type);
            final IGraph graph = new ProtocolGraphModel(protocol).createGraphModel(protocol);
            Assert.assertEquals(1, graph.getNodes().size());
        } finally {
            version.abort();
        }
    }

    public void testOutput() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ProtocolGraphModelTest.UNIQUE);
            final Protocol protocol = new Protocol(version, ProtocolGraphModelTest.UNIQUE + "p", type);
            final SampleCategory category = new SampleCategory(version, ProtocolGraphModelTest.UNIQUE);
            new RefOutputSample(version, category, protocol);
            final IGraph graph = new ProtocolGraphModel(protocol).createGraphModel(protocol);
            Assert.assertEquals(2, graph.getNodes().size());
            Assert.assertEquals(1, graph.getEdges().size());
            final IEdge edge = graph.getEdges().iterator().next();
            Assert.assertEquals(protocol.getName(), edge.getHead().getAttributes().get("label"));
        } finally {
            version.abort();
        }
    }

    public void testInput() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ProtocolGraphModelTest.UNIQUE);
            final Protocol protocol = new Protocol(version, ProtocolGraphModelTest.UNIQUE + "p", type);
            final SampleCategory category =
                new SampleCategory(version, ProtocolGraphModelTest.UNIQUE + "cat");
            new RefInputSample(version, category, protocol);
            final IGraph graph = new ProtocolGraphModel(protocol).createGraphModel(protocol);
            Assert.assertEquals(2, graph.getNodes().size());
            Assert.assertEquals(1, graph.getEdges().size());
            final IEdge edge = graph.getEdges().iterator().next();
            Assert.assertEquals(category.getName(), edge.getHead().getAttributes().get("label"));
        } finally {
            version.abort();
        }
    }

    public void testThreeInARow() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ProtocolGraphModelTest.UNIQUE);
            final SampleCategory category = new SampleCategory(version, ProtocolGraphModelTest.UNIQUE);
            final SampleCategory category2 =
                new SampleCategory(version, ProtocolGraphModelTest.UNIQUE + "cat2");
            final Protocol first = new Protocol(version, ProtocolGraphModelTest.UNIQUE + "one", type);
            first.setIsForUse(true);
            new RefOutputSample(version, category, first);
            final Protocol second = new Protocol(version, ProtocolGraphModelTest.UNIQUE + "two", type);
            second.setIsForUse(true);
            new RefInputSample(version, category, second);
            new RefOutputSample(version, category2, second);
            final Protocol third = new Protocol(version, ProtocolGraphModelTest.UNIQUE + "three", type);
            third.setIsForUse(true);
            new RefInputSample(version, category2, third);
            final IGraph graph = new ProtocolGraphModel(first).createGraphModel(first);
            Assert.assertEquals(5, graph.getNodes().size());
            Assert.assertEquals(4, graph.getEdges().size());
        } finally {
            version.abort();
        }
    }

    public void testWorkflow() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {

            final ExperimentType type = new ExperimentType(version, ProtocolGraphModelTest.UNIQUE);
            final SampleCategory category = new SampleCategory(version, ProtocolGraphModelTest.UNIQUE);
            final SampleCategory category2 =
                new SampleCategory(version, ProtocolGraphModelTest.UNIQUE + "cat2");
            final Protocol first = new Protocol(version, ProtocolGraphModelTest.UNIQUE + "one", type);
            first.setIsForUse(true);
            new RefOutputSample(version, category, first);
            final Protocol second = new Protocol(version, ProtocolGraphModelTest.UNIQUE + "two", type);
            second.setIsForUse(true);
            new RefInputSample(version, category, second);
            new RefOutputSample(version, category2, second);
            final Protocol third = new Protocol(version, ProtocolGraphModelTest.UNIQUE + "three", type);
            third.setIsForUse(true);
            new RefInputSample(version, category2, third);

            final Workflow workflow = new Workflow(version, ProtocolGraphModelTest.UNIQUE);
            workflow.addProtocol(second);
            final IGraph graph = new ProtocolGraphModel(workflow).createGraphModel(workflow);
            Assert.assertEquals(3, graph.getNodes().size());
            final Set<String> labels = new HashSet(3);
            final Collection<INode> nodes = graph.getNodes();
            for (final Iterator iterator = nodes.iterator(); iterator.hasNext();) {
                final INode node = (INode) iterator.next();
                labels.add((String) node.getAttributes().get("label"));
            }
            Assert.assertTrue(labels.contains(second.getName()));
            Assert.assertTrue(labels.contains(category2.getName()));
            Assert.assertTrue(labels.contains(category.getName()));
        } finally {
            version.abort();
        }
    }

    public void testFork() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ProtocolGraphModelTest.UNIQUE);
            final SampleCategory category = new SampleCategory(version, ProtocolGraphModelTest.UNIQUE);
            final Protocol first = new Protocol(version, ProtocolGraphModelTest.UNIQUE + "one", type);
            new RefOutputSample(version, category, first);
            final Protocol second = new Protocol(version, ProtocolGraphModelTest.UNIQUE + "two", type);
            new RefInputSample(version, category, second);
            final Protocol third = new Protocol(version, ProtocolGraphModelTest.UNIQUE + "three", type);
            new RefInputSample(version, category, third);
            final IGraph graph = new ProtocolGraphModel(first).createGraphModel(first);
            Assert.assertEquals(4, graph.getNodes().size());
            Assert.assertEquals(3, graph.getEdges().size());
        } finally {
            version.abort();
        }
    }

}
