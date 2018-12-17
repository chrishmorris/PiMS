/**
 * V5_0-web org.pimslims.graph.implementation ModelObjectNodeTest.java
 * 
 * @author cm65
 * @date 11 Jun 2013
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.graph.implementation;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.graph.GraphFormat;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.sample.Sample;

/**
 * ModelObjectNodeTest
 * 
 */
public class ModelObjectNodeTest extends TestCase {

    private static final String UNIQUE = "mon" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for ModelObjectNodeTest
     * 
     * @param name
     */
    public ModelObjectNodeTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testMenu() throws ConstraintException {

        final WritableVersion version = this.model.getTestVersion();
        try {
            final ModelObject sample = new Sample(version, ModelObjectNodeTest.UNIQUE);
            final String js = new ModelObjectNode(sample).getAttribute(GraphFormat.DOT_URL);
            Assert.assertFalse(js.contains("\\\\"));
        } finally {
            version.abort();
        }
    }

}
