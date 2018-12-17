/**
 * V4_3-web org.pimslims.graph DotGraphUtilTest.java
 * 
 * @author cm65
 * @date 28 Feb 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.graph.implementation.AbstractGraphModel;
import org.pimslims.properties.PropertyGetter;
import org.pimslims.servlet.DotServlet;

/**
 * DotGraphUtilTest
 * 
 */
public class DotGraphUtilTest extends TestCase {

    /**
     * TestEdge
     * 
     */
    public static class TestEdge implements IEdge {

        private final INode head;

        private final INode tail;

        /**
         * Constructor for TestEdge
         * 
         * @param head
         * @param tail
         */
        public TestEdge(final TestNode head, final TestNode tail) {
            this.head = head;
            this.tail = tail;
        }

        /**
         * TestEdge.getHead
         * 
         * @see org.pimslims.graph.IEdge#getHead()
         */
        public INode getHead() {
            return this.head;
        }

        /**
         * TestEdge.getTail
         * 
         * @see org.pimslims.graph.IEdge#getTail()
         */
        public INode getTail() {
            return this.tail;
        }

        /**
         * TestEdge.getAttributes
         * 
         * @see org.pimslims.graph.IEdge#getAttributes()
         */
        @Override
        public Map getAttributes() {
            // TODO Auto-generated method stub
            return null;
        }

    }

    /**
     * TestNode
     * 
     */
    public static class TestNode implements INode {

        private final String id;

        public TestNode(final String id) {
            super();
            this.id = id;
            // initialise properties
            ModelImpl.getModel();
            PropertyGetter.setProxySetting();
        }

        /**
         * TestNode.getId
         * 
         * @see org.pimslims.graph.INode#getId()
         */
        public String getId() {
            return this.id;
        }

        /**
         * TestNode.getAttributes
         * 
         * @see org.pimslims.graph.INode#getAttributes()
         */
        public Map getAttributes() {
            return null;
        }

        /**
         * TestNode.setAttribute
         * 
         * @see org.pimslims.graph.INode#setAttribute(java.lang.String, java.lang.String)
         */
        public void setAttribute(final String attName, final String attValue) {

        }

        /**
         * INode.getCluster
         * 
         * @see org.pimslims.graph.INode#getCluster()
         */
        @Override
        public String getCluster() {
            return null;
        }

    }

    /**
     * TestGraphModel
     * 
     */
    public static final class TestGraphModel implements IGraph {

        private final Collection<INode> nodes = new HashSet();

        private final Set<IEdge> edges = new HashSet();

        public void addNode(final INode node) {
            this.nodes.add(node);
        }

        /**
         * TestGraphModel.getAttributes
         * 
         * @see org.pimslims.graph.IGraphElement#getAttributes()
         */
        public Map getAttributes() {
            return Collections.EMPTY_MAP;
        }

        /**
         * TestGraphModel.getAttribute
         * 
         * @see org.pimslims.graph.IGraphElement#getAttribute(java.lang.String)
         */
        public String getAttribute(final String attributeName) {
            return null;
        }

        /**
         * TestGraphModel.setAttribute
         * 
         * @see org.pimslims.graph.IGraphElement#setAttribute(java.lang.String, java.lang.String)
         */
        public void setAttribute(final String attName, final String attValue) {

        }

        /**
         * TestGraphModel.getNodes
         * 
         * @see org.pimslims.graph.IGraph#getNodes()
         */
        public Collection<INode> getNodes() {
            return this.nodes;
        }

        /**
         * TestGraphModel.getEdges
         * 
         * @see org.pimslims.graph.IGraph#getEdges()
         */
        public Set<IEdge> getEdges() {
            return this.edges;
        }

        /**
         * TestGraphModel.getNodesAttributes
         * 
         * @see org.pimslims.graph.IGraph#getNodesAttributes()
         */
        public Map getNodesAttributes() {
            return null;
        }

        /**
         * TestGraphModel.addEdge
         * 
         * @param testEdge
         */
        public void addEdge(final IEdge edge) {
            this.edges.add(edge);
        }

    }

    private static final String UNIQUE = "dgu" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for DotGraphUtilTest
     * 
     * @param name
     */
    public DotGraphUtilTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for {@link org.pimslims.graph.DotGraphUtil#getInputForDot(org.pimslims.graph.IGraph)}.
     */
    public void testGetNoInputForDot() throws GraphGenerationException {
        final IGraph model = new TestGraphModel();
        final String input = new GrappaModelLoader(model).produceData();
        Assert.assertTrue(input.contains("    label = \"Diagram is empty\""));
    }

    public void testGetNoES() throws GraphGenerationException {
        final String input =
            DotServlet.generateGraphModelAndInput(null, "http://pims/View/", AbstractGraphModel.MAX_NODES,
                800, 1024);

        Assert.assertTrue(input, input.contains("label = \"Diagram is empty\""));
    }

    /**
     * Test method for {@link org.pimslims.graph.DotGraphUtil#getInputForDot(org.pimslims.graph.IGraph)}.
     */
    public void testNode() throws GraphGenerationException {
        final TestGraphModel model = new TestGraphModel();
        model.addNode(new TestNode(DotGraphUtilTest.UNIQUE));
        final String input = new GrappaModelLoader(model).produceData();
        Assert.assertTrue(input.contains(DotGraphUtilTest.UNIQUE + " [\r\n" + "    id = "
            + DotGraphUtilTest.UNIQUE + "\r\n" + "  ];" + "\r\n" + "}\r\n"));
    }

    /**
     * Test method for {@link org.pimslims.graph.DotGraphUtil#getInputForDot(org.pimslims.graph.IGraph)}.
     */
    public void testEdge() throws GraphGenerationException {
        final TestGraphModel model = new TestGraphModel();
        final TestNode head = new TestNode(DotGraphUtilTest.UNIQUE + "h");
        model.addNode(head);
        final TestNode tail = new TestNode(DotGraphUtilTest.UNIQUE + "t");
        model.addNode(tail);
        model.addEdge(new TestEdge(head, tail));
        final String input = new GrappaModelLoader(model).produceData();
        Assert.assertTrue(input.endsWith(DotGraphUtilTest.UNIQUE + "h -> " + DotGraphUtilTest.UNIQUE
            + "t\r\n" + "}\r\n"));
    }

}
