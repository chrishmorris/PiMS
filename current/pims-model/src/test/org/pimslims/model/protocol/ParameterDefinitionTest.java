/**
 * pims-model org.pimslims.model.protocol ParameterDefinitionTest.java
 * 
 * @author cm65
 * @date 13 Jan 2014
 * 
 *       Protein Information Management System
 * @version: 5.1
 * 
 *           Copyright (c) 2014 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.model.protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.reference.ExperimentType;

/**
 * ParameterDefinitionTest
 * 
 */
public class ParameterDefinitionTest extends TestCase {

    private static final String UNIQUE = "pd" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for ParameterDefinitionTest
     * 
     * @param name
     */
    public ParameterDefinitionTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testOrder() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            ExperimentType type = new ExperimentType(version, UNIQUE);
            Protocol protocol = new Protocol(version, UNIQUE, type);
            ParameterDefinition first = new ParameterDefinition(version, "one", "String", protocol);
            ParameterDefinition second = new ParameterDefinition(version, "two", "String", protocol);
            version.flush();
            assertEquals(first, protocol.getParameterDefinitions().iterator().next());
            protocol.setParameterDefinitions(protocol.getParameterDefinitions());
            assertEquals(first, protocol.getParameterDefinitions().iterator().next());
            List<ParameterDefinition> pds = new ArrayList(protocol.getParameterDefinitions());
            Collections.reverse(pds);
            protocol.setParameterDefinitions(pds);
            assertEquals(second, protocol.getParameterDefinitions().iterator().next());
        } finally {
            version.abort();
        }
    }

    public void testMoveDown() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            ExperimentType type = new ExperimentType(version, UNIQUE);
            Protocol protocol = new Protocol(version, UNIQUE, type);
            ParameterDefinition first = new ParameterDefinition(version, "one", "String", protocol);
            ParameterDefinition second = new ParameterDefinition(version, "two", "String", protocol);
            version.flush();
            assertEquals(first, protocol.getParameterDefinitions().iterator().next());
            second.moveDown(); // should be no-op
            assertEquals(first, protocol.getParameterDefinitions().iterator().next());

            first.moveDown();
            version.flush();
            assertEquals(second, protocol.getParameterDefinitions().iterator().next());
        } finally {
            version.abort();
        }
    }

}
