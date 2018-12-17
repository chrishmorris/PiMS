/**
 * V3_1-pims-web org.pimslims.servlet.plateExperiment OrderPlateTest.java
 * 
 * @author cm65
 * @date 26 Feb 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.plateExperiment;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;

/**
 * OrderPlateTest
 * 
 */
public class OrderPlateTest extends TestCase {

    private static final String UNIQUE = "op" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for OrderPlateTest
     * 
     * @param name
     */
    public OrderPlateTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.plateExperiment.OrderPlate#createPrimerPlates(org.pimslims.dao.WritableVersion, org.pimslims.model.holder.Holder)}
     * .
     * 
     * @throws ConstraintException
     * @throws AccessException
     */
    public void TODOtestCreatePrimerPlates() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final HolderType holderType = new HolderType(version, OrderPlateTest.UNIQUE);
            holderType.setMaxRow(0);
            holderType.setMaxColumn(0);
            final Holder holder = new Holder(version, OrderPlateTest.UNIQUE, holderType);
            OrderPlate.createPrimerPlates(version, holder);
        } finally {
            version.abort();
        }
    }

    public void testCreatePlate() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final HolderType holderType = new HolderType(version, OrderPlateTest.UNIQUE);
            holderType.setMaxRow(4);
            holderType.setMaxColumn(6);
            final Holder setup = new Holder(version, OrderPlateTest.UNIQUE, holderType);
            final Sample sample = new Sample(version, OrderPlateTest.UNIQUE);
            sample.setHolder(setup);
            sample.setColPosition(1);
            sample.setRowPosition(1);

            final ExperimentType type = new ExperimentType(version, OrderPlateTest.UNIQUE);
            final Protocol protocol = new Protocol(version, OrderPlateTest.UNIQUE, type);
            final SampleCategory category = new SampleCategory(version, OrderPlateTest.UNIQUE);
            new RefOutputSample(version, category, protocol);
            new RefInputSample(version, category, protocol);

            final ExperimentGroup group = OrderPlate.createPlate(version, protocol, setup);
            Assert.assertEquals(1, group.getExperiments().size());
        } finally {
            version.abort();
        }
    }

}
