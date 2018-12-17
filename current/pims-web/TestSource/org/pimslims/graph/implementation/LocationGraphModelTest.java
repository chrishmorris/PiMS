/**
 * V4_3-web org.pimslims.graph.implementation LocationGraphModelTest.java
 * 
 * @author cm65
 * @date 17 Oct 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.graph.implementation;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.holder.Containable;
import org.pimslims.model.holder.Container;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.barcodeGraph.BarcodeGraphNode;
import org.pimslims.presentation.barcodeGraph.GraphModelBean;

/**
 * LocationGraphModelTest
 * 
 */
public class LocationGraphModelTest extends TestCase {

    private static final String UNIQUE = "lgm" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for LocationGraphModelTest
     * 
     * @param name
     */
    public LocationGraphModelTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * LocationGraphModelTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * LocationGraphModelTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for
     * {@link org.pimslims.graph.implementation.LocationGraphModel#createGraphModel(org.pimslims.metamodel.ModelObject)}
     * .
     */
    public void testCreateGraphModelNull() {
        new LocationGraphModel().createGraphModel(null);
    }

    public void testContains() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Container holder = new Holder(version, LocationGraphModelTest.UNIQUE, null);
            final Containable sample = new Sample(version, LocationGraphModelTest.UNIQUE);
            final LocationNode location = new LocationNode(holder);
            final BarcodeGraphNode node = new BarcodeGraphNode(location);
            node.addChild(new BarcodeGraphNode(new LocationNode(sample)));
            Assert.assertTrue(node.contains(new BarcodeGraphNode(new LocationNode(sample))));
        } finally {
            version.abort();
        }
    }

    public final void testHolder() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Holder holder = new Holder(version, LocationGraphModelTest.UNIQUE, null);
            final Sample sample = new Sample(version, LocationGraphModelTest.UNIQUE);
            sample.setContainer(holder);
            final LocationGraphModel graph = new LocationGraphModel().createGraphModel(holder);
            Assert.assertNotNull(graph);
            Assert.assertEquals(0, graph.getEdges().size());
            Assert.assertNotNull(GraphModelBean.getModel(graph));

        } finally {
            version.abort();
        }
    }

    public final void testSample() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Holder holder = new Holder(version, LocationGraphModelTest.UNIQUE, null);
            final Sample sample = new Sample(version, LocationGraphModelTest.UNIQUE);
            sample.setContainer(holder);
            final LocationGraphModel graph = new LocationGraphModel().createGraphModel(sample);
            Assert.assertNotNull(graph);
            Assert.assertEquals(1, graph.getEdges().size());
            Assert.assertNotNull(GraphModelBean.getModel(graph));

        } finally {
            version.abort();
        }
    }
}
