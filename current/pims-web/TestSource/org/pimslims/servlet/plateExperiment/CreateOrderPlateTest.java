/**
 * V3_1-pims-web org.pimslims.servlet.plateExperiment CreateOrderPlateTest.java
 * 
 * @author cm65
 * @date 3 Feb 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.plateExperiment;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.ExperimentUtility;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.model.target.ResearchObjective;

/**
 * CreateOrderPlateTest
 * 
 */
public class CreateOrderPlateTest extends TestCase {

    private static final String UNIQUE = "cop" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for CreateOrderPlateTest
     * 
     * @param name
     */
    public CreateOrderPlateTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.plateExperiment.CreateOrderPlate#createOrderPlate(org.pimslims.dao.WritableVersion, java.lang.String, java.util.List)}
     * .
     * 
     * @throws ConstraintException
     * @throws AccessException
     */
    public void testProtocol() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            new ExperimentType(version, CreateOrderPlateTest.UNIQUE);
            final List<String> constructs = new ArrayList();
            final ExperimentGroup group = CreateOrderPlate.createOrderPlate(version, null, null, constructs);
            Assert.assertNotNull(group);
            Assert.assertEquals(constructs.size(), group.getExperiments().size());
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.plateExperiment.CreateOrderPlate#createOrderPlate(org.pimslims.dao.WritableVersion, java.lang.String, java.util.List)}
     * .
     * 
     * @throws ConstraintException
     * @throws AccessException
     */
    public void testConstruct() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ResearchObjective construct =
                new ResearchObjective(version, CreateOrderPlateTest.UNIQUE, CreateOrderPlateTest.UNIQUE);

            final ExperimentType type = new ExperimentType(version, CreateOrderPlateTest.UNIQUE);
            final Protocol protocol = new Protocol(version, CreateOrderPlateTest.UNIQUE, type);
            final SampleCategory category = new SampleCategory(version, CreateOrderPlateTest.UNIQUE);

            new RefOutputSample(version, category, protocol);
            final List<String> constructs = new ArrayList(1);
            constructs.add(construct.get_Hook());
            final ExperimentGroup group =
                CreateOrderPlate.createOrderPlate(version, null, protocol.get_Hook(), constructs);
            Assert.assertNotNull(group);
            Assert.assertEquals(constructs.size(), group.getExperiments().size());
            final Experiment experiment = group.getExperiments().iterator().next();
            Assert.assertEquals(construct, experiment.getProject());
            Assert.assertEquals(0, HolderFactory.getRow(experiment));
            Assert.assertEquals(0, HolderFactory.getColumn(experiment));
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.plateExperiment.CreateOrderPlate#createOrderPlate(org.pimslims.dao.WritableVersion, java.lang.String, java.util.List)}
     * .
     * 
     * @throws ConstraintException
     * @throws AccessException
     */
    public void testPrimers() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ResearchObjective construct =
                new ResearchObjective(version, CreateOrderPlateTest.UNIQUE, CreateOrderPlateTest.UNIQUE);

            final Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(Protocol.PROP_NAME, ExperimentUtility.SPOTCONSTRUCT_DESIGN_PROTOCOL);
            final Protocol designProtocol = version.findFirst(Protocol.class, new HashMap<String, Object>());

            final ExperimentType type = new ExperimentType(version, CreateOrderPlateTest.UNIQUE);
            criteria.clear();
            criteria.put(Experiment.PROP_PROTOCOL, designProtocol);
            criteria.put(Experiment.PROP_ENDDATE, new GregorianCalendar());
            criteria.put(Experiment.PROP_EXPERIMENTTYPE, type);
            criteria.put(Experiment.PROP_NAME, CreateOrderPlateTest.UNIQUE);
            criteria.put(Experiment.PROP_STARTDATE, new GregorianCalendar());
            final Experiment designExperiment = new Experiment(version, criteria);
            designExperiment.setProtocol(designProtocol);

            criteria.clear();
            criteria.put(Primer.PROP_DIRECTION, "forward");
            criteria.put(Substance.PROP_NAME, CreateOrderPlateTest.UNIQUE);
            criteria.put(Molecule.PROP_MOLTYPE, "DNA");
            criteria.put(Primer.PROP_ISUNIVERSAL, false);
            final Primer primer = new Primer(version, criteria);

            final Sample sample = new Sample(version, CreateOrderPlateTest.UNIQUE);

            criteria.clear();
            criteria.put(SampleComponent.PROP_REFCOMPONENT, primer);
            criteria.put(SampleComponent.PROP_ABSTRACTSAMPLE, sample);
            final SampleComponent sampleComponent = new SampleComponent(version, criteria);
            sample.addSampleComponent(sampleComponent);

            final OutputSample outputSample = new OutputSample(version, designExperiment);
            outputSample.setSample(sample);

            construct.addExperiment(designExperiment);

            final Protocol protocol = new Protocol(version, CreateOrderPlateTest.UNIQUE, type);
            final SampleCategory category = new SampleCategory(version, CreateOrderPlateTest.UNIQUE);

            new RefOutputSample(version, category, protocol);
            final List<String> constructs = new ArrayList(1);
            constructs.add(construct.get_Hook());
            final ExperimentGroup group =
                CreateOrderPlate.createOrderPlate(version, null, protocol.get_Hook(), constructs);
            Assert.assertNotNull(group);
            Assert.assertEquals(constructs.size(), group.getExperiments().size());
            final Experiment experiment = group.getExperiments().iterator().next();
            Assert.assertEquals(construct, experiment.getProject());
            Assert.assertEquals(0, HolderFactory.getRow(experiment));
            Assert.assertEquals(0, HolderFactory.getColumn(experiment));

            final Experiment primerdesignExperiment =
                ExperimentUtility.getPrimerDesignExperiment(experiment.getProject());
            Assert.assertNotNull(primerdesignExperiment);
            Assert.assertEquals(sample, ExperimentUtility.getForwardPrimer(primerdesignExperiment));

        } finally {
            version.abort();
        }
    }

    /**
     * Check that it fills the plate by column, not by row
     * 
     * CreateOrderPlateTest.testColumns
     * 
     * @throws ConstraintException
     * @throws AccessException
     */
    public void testColumns() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ResearchObjective construct1 =
                new ResearchObjective(version, CreateOrderPlateTest.UNIQUE, CreateOrderPlateTest.UNIQUE);

            final ResearchObjective construct2 =
                new ResearchObjective(version, CreateOrderPlateTest.UNIQUE + "two",
                    CreateOrderPlateTest.UNIQUE);
            final ExperimentType type = new ExperimentType(version, CreateOrderPlateTest.UNIQUE);
            final Protocol protocol = new Protocol(version, CreateOrderPlateTest.UNIQUE, type);
            final SampleCategory category = new SampleCategory(version, CreateOrderPlateTest.UNIQUE);
            new RefOutputSample(version, category, protocol);
            final List<String> constructs = new ArrayList(2);
            constructs.add(construct2.get_Hook());
            constructs.add(construct1.get_Hook()); // oldest - must be put in first well
            final ExperimentGroup group =
                CreateOrderPlate.createOrderPlate(version, null, protocol.get_Hook(), constructs);
            Assert.assertNotNull(group);
            Assert.assertEquals(constructs.size(), group.getExperiments().size());
            final Iterator<Experiment> iterator = group.getExperiments().iterator();
            // the order of them is not predictable
            final Experiment experiment = iterator.next();
            Experiment experiment1 = null;
            Experiment experiment2 = null;
            if (construct1 == experiment.getProject()) {
                experiment1 = experiment;
                experiment2 = iterator.next();
            } else {
                experiment1 = iterator.next();
                experiment2 = experiment;
            }

            Assert.assertEquals(construct1, experiment1.getResearchObjective());
            final int row1 = HolderFactory.getRow(experiment1);
            Assert.assertEquals(HolderFactory.getPositionInHolder(experiment1), 0, row1);
            Assert.assertEquals(0, HolderFactory.getColumn(experiment1));

            Assert.assertEquals(construct2, experiment2.getResearchObjective());
            Assert.assertEquals(HolderFactory.getPositionInHolder(experiment2), 1, HolderFactory
                .getRow(experiment2));
            Assert.assertEquals(0, HolderFactory.getColumn(experiment2));
        } finally {
            version.abort();
        }
    }
}
