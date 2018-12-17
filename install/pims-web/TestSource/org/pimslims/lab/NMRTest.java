/**
 * V5_0-web org.pimslims.lab NMRTest.java
 * 
 * @author cm65
 * @date 20 Dec 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.lab;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;

/**
 * NMRTest
 * 
 */
public class NMRTest extends TestCase {

    private static final String UNIQUE = "nmr" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for NMRTest
     * 
     * @param name
     */
    public NMRTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    public void testSampleLabel() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {

            final Sample sample = new Sample(version, NMRTest.UNIQUE + " 2H13C");
            Assert.assertEquals("2H13C", NMR.getIsotopicLabelling(sample));
        } finally {
            version.abort();
        }

    }

    public void testNotSuitable() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final SampleCategory category = new SampleCategory(version, NMRTest.UNIQUE);
            final ExperimentType type =
                version.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, NMR.EXPERIMENT_TYPE);
            final Protocol protocol = new Protocol(version, NMRTest.UNIQUE, type);
            new ParameterDefinition(version, NMR.PARAMETER_DEFINITION, "String", protocol)
                .setDefaultValue("14N");
            new RefInputSample(version, category, protocol);

            final Sample sample = new Sample(version, NMRTest.UNIQUE + " 2H13C");
            sample.addSampleCategory(category);

            Assert.assertFalse(NMR.isSuitable(sample, protocol));
        } finally {
            version.abort();
        }

    }

    public void testSuitable() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final SampleCategory category = new SampleCategory(version, NMRTest.UNIQUE);
            final ExperimentType type =
                version.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, NMR.EXPERIMENT_TYPE);
            final Protocol protocol = new Protocol(version, NMRTest.UNIQUE, type);
            new ParameterDefinition(version, NMR.PARAMETER_DEFINITION, "String", protocol)
                .setDefaultValue("2H13C");
            new RefInputSample(version, category, protocol);

            final Sample sample = new Sample(version, NMRTest.UNIQUE + " 2H13C");
            sample.addSampleCategory(category);

            Assert.assertTrue(NMR.isSuitable(sample, protocol));
        } finally {
            version.abort();
        }

    }

}
