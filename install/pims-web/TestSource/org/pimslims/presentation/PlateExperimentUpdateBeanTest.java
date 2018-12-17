/**
 * V4_3-web org.pimslims.presentation PlateExperimentUpdateBeanTest.java
 * 
 * @author cm65
 * @date 22 Nov 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.plateExperiment.PlateExperimentBean;

/**
 * PlateExperimentUpdateBeanTest
 * 
 */
public class PlateExperimentUpdateBeanTest extends TestCase {

    private static final String UNIQUE = "peub" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    /**
     * Constructor for PlateExperimentUpdateBeanTest
     * 
     * @param name
     */
    public PlateExperimentUpdateBeanTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * PlateExperimentUpdateBeanTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * PlateExperimentUpdateBeanTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.PlateExperimentUpdateBean#PlateExperimentUpdateBean(org.pimslims.model.experiment.ExperimentGroup)}
     * .
     */
    public void testPlateExperimentUpdateBean() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group =
                new ExperimentGroup(version, PlateExperimentUpdateBeanTest.UNIQUE, "test");
            final PlateExperimentUpdateBean bean = new PlateExperimentUpdateBean(group, null);
            Assert.assertEquals(PlateExperimentUpdateBeanTest.UNIQUE, bean.getName());
            Assert.assertEquals(0, bean.getExperimentBeans().size());
            Assert.assertEquals(0, bean.getHolderColumns().length);
            Assert.assertEquals(1, bean.getMaxSubPositions());
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for {@link org.pimslims.presentation.PlateExperimentUpdateBean#getExperimentBeans()}.
     */
    public void testGetExperimentBeans() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            Experiment experiment = this.makeExperimentInGroup(version, "");
            final long startTime = System.currentTimeMillis();
            final ExperimentGroup group = experiment.getExperimentGroup();
            experiment = null; // lose the reference
            version.flush();
            version.getSession().clear(); // TODO version.clear();
            //HibernateUtil.clear2ndLevelCache();
            final PlateExperimentUpdateBean bean = new PlateExperimentUpdateBean(group, null);
            System.out.println("Time to make PlateExperimentUpdateBean: "
                + (System.currentTimeMillis() - startTime) + "ms");
            Assert.assertEquals(1, bean.getExperimentBeans().size());
            Assert.assertNull(bean.getProtocolBean());

            final Map parms = bean.getParameterMapObsolete();
            Assert.assertEquals(1, parms.size());

        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.PlateExperimentUpdateBean#PlateExperimentUpdateBean(org.pimslims.model.experiment.ExperimentGroup)}
     * .
     */
    public void testScore() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {

            final PlateExperimentUpdateBean bean0 =
                new PlateExperimentUpdateBean(this.makeExperimentInGroup(version, "zero")
                    .getExperimentGroup(), null);
            Assert.assertEquals(0, bean0.getGroupScore());

            final Experiment experiment1 = this.makeExperimentInGroup(version, "one");
            final Parameter parm = new Parameter(version, experiment1);
            parm.setValue("1");
            final Protocol protocol =
                new Protocol(version, PlateExperimentUpdateBeanTest.UNIQUE, experiment1.getExperimentType());
            final ParameterDefinition def = new ParameterDefinition(version, "__score", "Int", protocol);
            final List<String> possibleValues = Arrays.asList(new String[] { "0", "1" });
            def.setPossibleValues(possibleValues);
            parm.setParameterDefinition(def);
            final PlateExperimentUpdateBean bean1 =
                new PlateExperimentUpdateBean(experiment1.getExperimentGroup(), null);
            Assert.assertEquals(1, bean1.getGroupScore());

            Assert.assertEquals(1, bean0.compareTo(bean1));
        } finally {
            version.abort();
        }
    }

    private Experiment makeExperimentInGroup(final WritableVersion version, final String suffix)
        throws ConstraintException {
        Experiment experiment;
        {

            final ExperimentType type =
                new ExperimentType(version, PlateExperimentUpdateBeanTest.UNIQUE + suffix);
            final Protocol protocol =
                new Protocol(version, PlateExperimentUpdateBeanTest.UNIQUE + suffix, type);
            final ParameterDefinition pdef =
                new ParameterDefinition(version, PlateExperimentUpdateBeanTest.UNIQUE, "String", protocol);

            final ExperimentGroup group =
                new ExperimentGroup(version, PlateExperimentUpdateBeanTest.UNIQUE + suffix, "test");
            experiment =
                new Experiment(version, PlateExperimentUpdateBeanTest.UNIQUE + suffix,
                    PlateExperimentUpdateBeanTest.NOW, PlateExperimentUpdateBeanTest.NOW, type);
            experiment.setExperimentGroup(group);
            experiment.setProject(new ResearchObjective(version, PlateExperimentUpdateBeanTest.UNIQUE
                + suffix, "test"));
            new OutputSample(version, experiment).setSample(new Sample(version,
                PlateExperimentUpdateBeanTest.UNIQUE + "out" + suffix));
            new InputSample(version, experiment).setSample(new Sample(version,
                PlateExperimentUpdateBeanTest.UNIQUE + "in1" + suffix));
            new InputSample(version, experiment).setSample(new Sample(version,
                PlateExperimentUpdateBeanTest.UNIQUE + "in2" + suffix));
            new Parameter(version, experiment).setParameterDefinition(pdef);
        }
        return experiment;
    }

    /**
     * Test method for {@link org.pimslims.presentation.PlateExperimentUpdateBean#getParameterMap()}.
     */
    public void testGetParameterMap() throws AccessException, ConstraintException {

        final WritableVersion version = this.model.getTestVersion();
        try {
            final Experiment experiment = this.makeExperimentInGroup(version, "");
            new Parameter(version, experiment);

            final PlateExperimentUpdateBean bean =
                new PlateExperimentUpdateBean(experiment.getExperimentGroup(), null);

            final Map parms = bean.getParameterMapObsolete();
            Assert.assertEquals(2, parms.size());

        } finally {
            version.abort();
        }
    }

    /**
     * Test method for {@link org.pimslims.presentation.PlateExperimentUpdateBean#getExperimentBeans()}.
     */
    public void testProtocol() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Experiment experiment = this.makeExperimentInGroup(version, "");
            final Protocol protocol =
                new Protocol(version, PlateExperimentUpdateBeanTest.UNIQUE + "p",
                    experiment.getExperimentType());
            experiment.setProtocol(protocol);

            final PlateExperimentUpdateBean bean =
                new PlateExperimentUpdateBean(experiment.getExperimentGroup(), null);
            Assert.assertEquals(1, bean.getExperimentBeans().size());
            Assert.assertNotNull(bean.getProtocolBean());
            Assert.assertEquals(protocol.getName(), bean.getProtocolBean().getName());

        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.PlateExperimentUpdateBean#getPlateExperimentLayout(org.pimslims.model.holder.Holder)}
     * .
     */
    public void testGetPlateExperimentLayout() throws ConstraintException, AccessException {

        final WritableVersion version = this.model.getTestVersion();
        try {
            final HolderType holderType =
                new HolderType(version, PlateExperimentUpdateBeanTest.UNIQUE + "Plate");
            holderType.setMaxColumn(1);
            holderType.setMaxRow(1);
            final Holder holder = new Holder(version, PlateExperimentUpdateBeanTest.UNIQUE, holderType);
            final Experiment experiment = this.makeExperimentInGroup(version, "");
            this.makeSample(version, holder, experiment);
            version.flush();

            final PlateExperimentUpdateBean bean =
                new PlateExperimentUpdateBean(experiment.getExperimentGroup(), null);

            final PlateExperimentBean[][] layout = bean.getPlateExperimentLayout(holder);
            Assert.assertEquals(1, layout.length);
            Assert.assertEquals(1, layout[0].length);
            final String[] rows = bean.getHolderRows();
            Assert.assertEquals(1, rows.length);
            final String[] columns = bean.getHolderColumns();
            Assert.assertEquals(1, columns.length);
            final ModelObjectBean ne = bean.getNorthEastPlate();
            Assert.assertNull(ne);

        } finally {
            version.abort();
        }
    }

    public void testSubPosition() throws ConstraintException, AccessException {

        final WritableVersion version = this.model.getTestVersion();
        try {
            final HolderType holderType =
                new HolderType(version, PlateExperimentUpdateBeanTest.UNIQUE + "Plate");
            holderType.setMaxColumn(1);
            holderType.setMaxRow(1);
            holderType.setMaxSubPosition(2);
            final Holder holder = new Holder(version, PlateExperimentUpdateBeanTest.UNIQUE, holderType);
            final Experiment experiment1 = this.makeExperimentInGroup(version, ".1");
            this.makeSample(version, holder, experiment1).setSubPosition(1);
            final Experiment experiment2 =
                new Experiment(version, PlateExperimentUpdateBeanTest.UNIQUE + ".2",
                    PlateExperimentUpdateBeanTest.NOW, PlateExperimentUpdateBeanTest.NOW,
                    experiment1.getExperimentType());
            final ExperimentGroup group = experiment1.getExperimentGroup();
            experiment2.setExperimentGroup(group);
            this.makeSample(version, holder, experiment2).setSubPosition(2);
            version.flush();

            final PlateExperimentUpdateBean bean = new PlateExperimentUpdateBean(group, 2);

            Assert.assertEquals(1, bean.getExperimentBeans().size());
            Assert.assertEquals(experiment2.getName(), bean.getExperimentBeans().iterator().next().getName());
            Assert.assertEquals(2, bean.getMaxSubPositions());

        } finally {
            version.abort();
        }
    }

    private Sample makeSample(final WritableVersion version, final Holder holder, final Experiment experiment)
        throws ConstraintException {
        final Sample sample = new Sample(version, experiment.getName());
        new OutputSample(version, experiment).setSample(sample);
        sample.setHolder(holder);
        sample.setColPosition(1);
        sample.setRowPosition(1);
        return sample;
    }

    public void test4Plates() throws ConstraintException, AccessException {

        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group =
                new ExperimentGroup(version, PlateExperimentUpdateBeanTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, PlateExperimentUpdateBeanTest.UNIQUE);
            final HolderType holderType =
                new HolderType(version, PlateExperimentUpdateBeanTest.UNIQUE + "Plate");
            holderType.setMaxColumn(1);
            holderType.setMaxRow(1);
            this.makePlate(version, group, type, "nw", holderType, 0, 0);
            this.makePlate(version, group, type, "ne", holderType, 1, 0);
            final Holder holderSW = this.makePlate(version, group, type, "sw", holderType, 0, 1);
            this.makePlate(version, group, type, "se", holderType, 1, 1);
            version.flush();

            final PlateExperimentUpdateBean bean = new PlateExperimentUpdateBean(group, null);

            PlateExperimentBean[][] layout = bean.getPlateExperimentLayout(holderSW);
            Assert.assertEquals(1, layout.length);
            Assert.assertEquals(1, layout[0].length);
            final String[] rows = bean.getHolderRows();
            Assert.assertEquals(1, rows.length);
            final String[] columns = bean.getHolderColumns();
            Assert.assertEquals(1, columns.length);

            final ModelObjectBean ne = bean.getNorthEastPlate();
            Assert.assertNotNull(ne);
            Assert.assertEquals(PlateExperimentUpdateBeanTest.UNIQUE + "ne", ne.getName());

            layout = bean.getSouthEastPlateLayout();
            Assert.assertEquals(1, layout.length);
            Assert.assertEquals(1, layout[0].length);

        } finally {
            version.abort();
        }

    }

    private Holder makePlate(final WritableVersion version, final ExperimentGroup group,
        final ExperimentType type, final String suffix, final HolderType holderType, final int columnStart,
        final int rowStart) throws ConstraintException {
        final Holder holder;

        final Experiment experiment =
            new Experiment(version, PlateExperimentUpdateBeanTest.UNIQUE + suffix,
                PlateExperimentUpdateBeanTest.NOW, PlateExperimentUpdateBeanTest.NOW, type);
        experiment.setExperimentGroup(group);
        final Sample sample = new Sample(version, PlateExperimentUpdateBeanTest.UNIQUE + suffix);
        new OutputSample(version, experiment).setSample(sample);
        sample.setColPosition(1 + columnStart);
        sample.setRowPosition(1 + rowStart);
        holder = new Holder(version, PlateExperimentUpdateBeanTest.UNIQUE + suffix, holderType);
        sample.setHolder(holder);
        return holder;
    }

}
