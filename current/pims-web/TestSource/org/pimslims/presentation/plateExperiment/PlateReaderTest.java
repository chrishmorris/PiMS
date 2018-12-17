/**
 * current-pims-web org.pimslims.presentation.plateExperiment PlateReaderTest.java
 * 
 * @author cm65
 * @date 21 Feb 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.presentation.plateExperiment;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.bioinf.local.SequenceGetter;
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
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.WellExperimentBean;
import org.pimslims.presentation.experiment.InputSampleBean;
import org.pimslims.presentation.experiment.OutputSampleBean;

/*
 * PlateReaderTest
 */
public class PlateReaderTest extends TestCase {

    private static final String UNIQUE = "test" + System.currentTimeMillis();

    private static final Calendar NOW = java.util.Calendar.getInstance();

    private final AbstractModel model;

    /**
     * @param name
     */
    public PlateReaderTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.plateExperiment.PlateReader#PlateReader(org.pimslims.dao.ReadableVersion, org.pimslims.model.experiment.ExperimentGroup)}
     * .
     * 
     * @throws ConstraintException
     * @throws AccessException
     */
    public final void testPlateReaderReadableVersionExperimentGroup() throws ConstraintException,
        AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = new ExperimentGroup(version, PlateReaderTest.UNIQUE, "test");
            final PlateReader reader = new PlateReader(version, group, null);
            final List<WellExperimentBean> experiments = reader.getExperiments();
            Assert.assertNotNull(experiments);
            Assert.assertEquals(0, experiments.size());
            final List<Sample> samples = reader.getSamples();
            Assert.assertEquals(0, samples.size());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    /**
     * Test method for {@link org.pimslims.presentation.plateExperiment.PlateReader#getExperiments()}.
     * 
     * @throws AccessException
     */
    public final void testGetExperiments() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = new ExperimentGroup(version, PlateReaderTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, PlateReaderTest.UNIQUE);
            final Experiment exp =
                new Experiment(version, "exp1" + PlateReaderTest.UNIQUE, PlateReaderTest.NOW,
                    PlateReaderTest.NOW, type);
            exp.setExperimentGroup(group);
            exp.setStatus("Failed");
            new OutputSample(version, exp).setName("out");

            final PlateReader reader = new PlateReader(version, group, null);
            Assert.assertEquals(new Integer(1), reader.getMaxSubPosition());
            final List<WellExperimentBean> experiments = reader.getExperiments();
            Assert.assertNotNull(experiments);
            Assert.assertEquals(1, experiments.size());
            final WellExperimentBean bean = experiments.iterator().next();
            Assert.assertEquals(exp.getName(), bean.getName());
            Assert.assertEquals(exp.getDbId(), bean.getDbId());
            Assert.assertEquals("", bean.getRow());
            Assert.assertEquals("", bean.getColumn());
            Assert.assertNotNull("out", bean.getOutputSampleName());

            Assert.assertEquals(0, bean.getInputSampleHooks().size());
            Assert.assertEquals(0, bean.getInputSampleNames().size());
            Assert.assertEquals(0, bean.getInputSampleVolumes().size());
            Assert.assertEquals("Failed", bean.getStatus());
            /* TODO Assert.assertEquals(bean.getStartDate(), Utils.getDate(PlateReaderTest.NOW), bean
                .getStartDate()); */
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void test2SubPositions() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = this.create2SubPositions(version);

            final PlateReader reader = new PlateReader(version, group, new Integer(1));
            Assert.assertEquals(new Integer(2), reader.getMaxSubPosition());
            final List<WellExperimentBean> experiments = reader.getExperiments();
            Assert.assertNotNull(experiments);
            Assert.assertEquals(1, experiments.size());

        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testGetPcrProductLength() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = new ExperimentGroup(version, PlateReaderTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, PlateReaderTest.UNIQUE);
            final Experiment exp =
                new Experiment(version, "exp1" + PlateReaderTest.UNIQUE, PlateReaderTest.NOW,
                    PlateReaderTest.NOW, type);
            exp.setExperimentGroup(group);
            exp.setStatus("Failed");
            final ResearchObjective ro = new ResearchObjective(version, PlateReaderTest.UNIQUE, "test");
            final ResearchObjectiveElement roe =
                new ResearchObjectiveElement(version, PlateReaderTest.UNIQUE, PlateReaderTest.UNIQUE, ro);
            final Molecule tmc =
                new Molecule(version, "DNA", PlateReaderTest.UNIQUE + SequenceGetter.PCR_PRODUCT);
            tmc.setSequence("AAAA");
            final Collection<Molecule> tmcs = new HashSet();
            tmcs.add(tmc);
            roe.setTrialMolComponents(tmcs);
            exp.setProject(ro);
            new OutputSample(version, exp).setName("out");
            version.flush();

            final PlateReader reader = new PlateReader(version, group, null);
            final List<WellExperimentBean> experiments = reader.getExperiments();
            Assert.assertNotNull(experiments);
            Assert.assertEquals(1, experiments.size());
            final WellExperimentBean bean = experiments.iterator().next();
            Assert.assertEquals(4, bean.getPcrProductSize());

        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testGetProteinMW() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = new ExperimentGroup(version, PlateReaderTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, PlateReaderTest.UNIQUE);
            final Experiment exp =
                new Experiment(version, "exp1" + PlateReaderTest.UNIQUE, PlateReaderTest.NOW,
                    PlateReaderTest.NOW, type);
            exp.setExperimentGroup(group);
            exp.setStatus("Failed");
            final ResearchObjective ro = new ResearchObjective(version, PlateReaderTest.UNIQUE, "test");
            final ResearchObjectiveElement roe =
                new ResearchObjectiveElement(version, PlateReaderTest.UNIQUE, PlateReaderTest.UNIQUE, ro);
            final Molecule tmc =
                new Molecule(version, "DNA", PlateReaderTest.UNIQUE + SequenceGetter.PROTEIN);
            tmc.setSequence("AAAAAAAAAAAAAAAA");
            final Collection<Molecule> tmcs = new HashSet();
            tmcs.add(tmc);
            roe.setTrialMolComponents(tmcs);
            exp.setProject(ro);
            new OutputSample(version, exp).setName("out");

            final PlateReader reader = new PlateReader(version, group, null);
            final List<WellExperimentBean> experiments = reader.getExperiments();
            Assert.assertNotNull(experiments);
            Assert.assertEquals(1, experiments.size());
            final WellExperimentBean bean = experiments.iterator().next();
            Assert.assertEquals("1", bean.getProteinMW());

        } finally {
            version.abort(); // not testing persistence
        }
    }

    /**
     * Test method for {@link org.pimslims.presentation.plateExperiment.PlateReader#getExperiments()}.
     * 
     * @throws AccessException
     */
    public final void testGetConstruct() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = new ExperimentGroup(version, PlateReaderTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, PlateReaderTest.UNIQUE);
            final Experiment exp =
                new Experiment(version, "exp1" + PlateReaderTest.UNIQUE, PlateReaderTest.NOW,
                    PlateReaderTest.NOW, type);
            exp.setExperimentGroup(group);
            final ResearchObjective ro = new ResearchObjective(version, PlateReaderTest.UNIQUE, "test");
            exp.setProject(ro);
            new OutputSample(version, exp).setName("out");

            final PlateReader reader = new PlateReader(version, group, null);
            final List<WellExperimentBean> experiments = reader.getExperiments();
            Assert.assertNotNull(experiments);
            Assert.assertEquals(1, experiments.size());
            final WellExperimentBean bean = experiments.iterator().next();
            final ModelObjectShortBean construct = bean.getConstruct();
            Assert.assertNotNull(construct);
            Assert.assertEquals(ro.get_Hook(), construct.getHook());

            Assert.assertEquals(ro.getName(), bean.getConstructName());
            Assert.assertEquals(ro.get_Hook(), bean.getConstructHook());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testTwoOutputs() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = new ExperimentGroup(version, PlateReaderTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, PlateReaderTest.UNIQUE);
            final Experiment exp =
                new Experiment(version, "exp1" + PlateReaderTest.UNIQUE, PlateReaderTest.NOW,
                    PlateReaderTest.NOW, type);
            exp.setExperimentGroup(group);
            new OutputSample(version, exp).setName("one");
            new OutputSample(version, exp).setName("two");

            final PlateReader reader = new PlateReader(version, group, null);
            final List<WellExperimentBean> experiments = reader.getExperiments();
            Assert.assertNotNull(experiments);
            Assert.assertEquals(1, experiments.size());
            final WellExperimentBean bean = experiments.iterator().next();
            Assert.assertEquals(exp.getName(), bean.getName());
            Assert.assertEquals(exp.getDbId(), bean.getDbId());
            Assert.assertEquals("", bean.getRow());
            Assert.assertEquals("", bean.getColumn());
            Assert.assertNull(bean.getOutputSampleHook());
            final Map<String, OutputSampleBean> osBeans = bean.getOutputSamples();
            Assert.assertEquals(2, osBeans.size());
            Assert.assertTrue(osBeans.containsKey("two"));
            final OutputSampleBean osb = osBeans.get("two");
            Assert.assertEquals("two", osb.getOutputSampleName());

        } finally {
            version.abort(); // not testing persistence
        }
    }

    /**
     * Test method for {@link org.pimslims.presentation.plateExperiment.PlateReader#getExperiments()}.
     * 
     * @throws AccessException
     */
    public final void testGetParameters() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = new ExperimentGroup(version, PlateReaderTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, PlateReaderTest.UNIQUE);
            final Experiment exp =
                new Experiment(version, "exp1" + PlateReaderTest.UNIQUE, PlateReaderTest.NOW,
                    PlateReaderTest.NOW, type);
            exp.setExperimentGroup(group);
            new OutputSample(version, exp);
            final Parameter parm = new Parameter(version, exp);
            parm.setName("p" + PlateReaderTest.UNIQUE);
            parm.setValue("v" + PlateReaderTest.UNIQUE);

            final PlateReader reader = new PlateReader(version, group, null);
            final List<WellExperimentBean> experiments = reader.getExperiments();
            Assert.assertNotNull(experiments);
            Assert.assertEquals(1, experiments.size());
            final WellExperimentBean expBean = experiments.iterator().next();

            // test the old features
            Assert.assertEquals(1, expBean.getParameters().size());
            Assert.assertTrue(expBean.getParameters().containsKey(parm.getName()));
            Assert.assertEquals(1, expBean.getParameterIds().size());
            Assert.assertTrue(expBean.getParameterIds().containsKey(parm.getName()));

        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testOutputSample() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = new ExperimentGroup(version, PlateReaderTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, PlateReaderTest.UNIQUE);
            final Experiment exp =
                new Experiment(version, "exp1" + PlateReaderTest.UNIQUE, PlateReaderTest.NOW,
                    PlateReaderTest.NOW, type);
            exp.setExperimentGroup(group);
            final OutputSample os = new OutputSample(version, exp);
            os.setName("os" + PlateReaderTest.UNIQUE);
            final Sample sample = new Sample(version, "S" + PlateReaderTest.UNIQUE);
            os.setSample(sample);

            final PlateReader reader = new PlateReader(version, group, null);
            final List<WellExperimentBean> experiments = reader.getExperiments();
            Assert.assertNotNull(experiments);
            Assert.assertEquals(1, experiments.size());
            final WellExperimentBean bean = experiments.iterator().next();
            Assert.assertEquals(sample.get_Hook(), bean.getOutputSampleHook());
            Assert.assertEquals(sample.getName(), bean.getOutputSampleName());
            Assert.assertEquals(null, bean.getRefOutputSampleDbId());

        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testInputSample() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = new ExperimentGroup(version, PlateReaderTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, PlateReaderTest.UNIQUE);
            final Experiment exp =
                new Experiment(version, "exp1" + PlateReaderTest.UNIQUE, PlateReaderTest.NOW,
                    PlateReaderTest.NOW, type);
            exp.setExperimentGroup(group);
            new OutputSample(version, exp);
            final InputSample is = new InputSample(version, exp);
            is.setName("is" + PlateReaderTest.UNIQUE);
            final Sample sample = new Sample(version, "S" + PlateReaderTest.UNIQUE);
            is.setSample(sample);

            final PlateReader reader = new PlateReader(version, group, null);
            final List<WellExperimentBean> experiments = reader.getExperiments();
            Assert.assertNotNull(experiments);
            Assert.assertEquals(1, experiments.size());
            final WellExperimentBean expBean = experiments.iterator().next();

            // test the old way
            Assert.assertEquals(1, expBean.getInputSampleHooks().size());
            Assert.assertEquals(1, expBean.getInputSampleNames().size());
            Assert.assertEquals(1, expBean.getInputSampleVolumes().size());

            // test the new way
            Assert.assertEquals(1, expBean.getInputSamples().size());
            final InputSampleBean bean = expBean.getInputSamples().iterator().next();
            Assert.assertEquals(null, bean.getRefInputSampleDbId());

        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testRefInputSample() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup experimentGroup =
                new ExperimentGroup(version, PlateReaderTest.UNIQUE, "test");
            final ExperimentGroup group = experimentGroup;
            final ExperimentType type = new ExperimentType(version, PlateReaderTest.UNIQUE);
            final Experiment exp =
                new Experiment(version, "exp1" + PlateReaderTest.UNIQUE, PlateReaderTest.NOW,
                    PlateReaderTest.NOW, type);
            exp.setExperimentGroup(group);
            new OutputSample(version, exp);
            final InputSample is = new InputSample(version, exp);
            is.setName("is" + PlateReaderTest.UNIQUE);
            final Protocol protocol = new Protocol(version, "p" + PlateReaderTest.UNIQUE, type);
            exp.setProtocol(protocol);
            final SampleCategory category = new SampleCategory(version, "sc" + PlateReaderTest.UNIQUE);
            final RefInputSample ris = new RefInputSample(version, category, protocol);
            is.setRefInputSample(ris);
            final Sample sample = new Sample(version, "S" + PlateReaderTest.UNIQUE);
            sample.addSampleCategory(category);
            sample.setIsActive(Boolean.TRUE);

            final PlateReader reader = new PlateReader(version, group, null);
            final List<WellExperimentBean> experiments = reader.getExperiments();
            Assert.assertNotNull(experiments);
            Assert.assertEquals(1, experiments.size());
            final WellExperimentBean expBean = experiments.iterator().next();

            Assert.assertEquals(1, expBean.getInputSamples().size());
            //final InputSampleBean bean = expBean.getInputSamples().iterator().next();
            /*assertEquals(1, bean.getSamples().size());
            SampleBean sampleBean = bean.getSamples().iterator().next();
            assertEquals(sample.getName(), sampleBean.getName());*/

        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testTwoExperiments() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = new ExperimentGroup(version, PlateReaderTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, PlateReaderTest.UNIQUE);
            final Experiment exp =
                new Experiment(version, "exp1" + PlateReaderTest.UNIQUE, PlateReaderTest.NOW,
                    PlateReaderTest.NOW, type);
            exp.setExperimentGroup(group);
            new OutputSample(version, exp);
            final Experiment exp2 =
                new Experiment(version, "exp2" + PlateReaderTest.UNIQUE, PlateReaderTest.NOW,
                    PlateReaderTest.NOW, type);
            exp2.setExperimentGroup(group);
            new OutputSample(version, exp2);

            final PlateReader reader = new PlateReader(version, group, null);
            final List<WellExperimentBean> experiments = reader.getExperiments();
            Assert.assertNotNull(experiments);
            Assert.assertEquals(2, experiments.size());

        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testOrder() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = new ExperimentGroup(version, PlateReaderTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, PlateReaderTest.UNIQUE);

            final Experiment exp1 = this.getExperiment(version, type, null, 1, 10, null);
            group.addExperiment(exp1);

            final Experiment exp2 = this.getExperiment(version, type, null, 2, 8, null);
            group.addExperiment(exp2);

            final PlateReader reader = new PlateReader(version, group, null);
            List<WellExperimentBean> experiments = reader.getExperiments(new WellExperimentBean.RowOrder());
            Assert.assertEquals(2, experiments.size());
            Assert.assertEquals(exp1.getName(), experiments.iterator().next().getName());
            experiments = reader.getExperiments(new WellExperimentBean.ColumnOrder());
            Assert.assertEquals(2, experiments.size());
            Assert.assertEquals(exp2.getName(), experiments.iterator().next().getName());

        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testPlateLayout() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = this.createPlateLayout(version);

            final int[][] layout = PlateReader.getPlatelayout(group);
            Assert.assertNotNull(layout);
            Assert.assertEquals(0, layout[0][0]);
            Assert.assertEquals(0, layout[1][1]);
            Assert.assertEquals(1, layout[1][2]);
            Assert.assertEquals(1, layout[4][5]);
            Assert.assertEquals(0, layout[5][6]);

        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testPlateMinRow() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = this.createPlateLayout(version);
            final int[][] layout = PlateReader.getPlatelayout(group);

            Assert.assertEquals(1, PlateReader.getPlateMinRow(layout));

        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testPlateMaxRow() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = this.createPlateLayout(version);
            final int[][] layout = PlateReader.getPlatelayout(group);

            Assert.assertEquals(4, PlateReader.getPlateMaxRow(layout));

        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testPlateMinCol() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = this.createPlateLayout(version);
            final int[][] layout = PlateReader.getPlatelayout(group);

            Assert.assertEquals(2, PlateReader.getPlateMinCol(layout));

        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testPlateMaxCol() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = this.createPlateLayout(version);
            final int[][] layout = PlateReader.getPlatelayout(group);

            Assert.assertEquals(7, PlateReader.getPlateMaxCol(layout));

        } finally {
            version.abort(); // not testing persistence
        }
    }

    private ExperimentGroup createPlateLayout(final WritableVersion version) throws ConstraintException {

        final ExperimentGroup group = new ExperimentGroup(version, PlateReaderTest.UNIQUE, "test");
        final ExperimentType type = new ExperimentType(version, PlateReaderTest.UNIQUE);
        final HolderType holderType = new HolderType(version, PlateReaderTest.UNIQUE);
        holderType.setMaxColumn(12);
        holderType.setMaxRow(8);
        final Holder holder = new Holder(version, PlateReaderTest.UNIQUE, holderType);

        group.addExperiment(this.getExperiment(version, type, holder, 2, 3, null));
        group.addExperiment(this.getExperiment(version, type, holder, 2, 4, null));
        group.addExperiment(this.getExperiment(version, type, holder, 2, 5, null));
        group.addExperiment(this.getExperiment(version, type, holder, 2, 6, null));
        group.addExperiment(this.getExperiment(version, type, holder, 2, 7, null));
        group.addExperiment(this.getExperiment(version, type, holder, 2, 8, null));

        group.addExperiment(this.getExperiment(version, type, holder, 3, 3, null));
        group.addExperiment(this.getExperiment(version, type, holder, 3, 4, null));
        group.addExperiment(this.getExperiment(version, type, holder, 3, 5, null));
        group.addExperiment(this.getExperiment(version, type, holder, 3, 6, null));
        group.addExperiment(this.getExperiment(version, type, holder, 3, 7, null));
        group.addExperiment(this.getExperiment(version, type, holder, 3, 8, null));

        group.addExperiment(this.getExperiment(version, type, holder, 4, 3, null));
        group.addExperiment(this.getExperiment(version, type, holder, 4, 4, null));
        group.addExperiment(this.getExperiment(version, type, holder, 4, 5, null));
        group.addExperiment(this.getExperiment(version, type, holder, 4, 6, null));
        group.addExperiment(this.getExperiment(version, type, holder, 4, 7, null));
        group.addExperiment(this.getExperiment(version, type, holder, 4, 8, null));

        group.addExperiment(this.getExperiment(version, type, holder, 5, 3, null));
        group.addExperiment(this.getExperiment(version, type, holder, 5, 4, null));
        group.addExperiment(this.getExperiment(version, type, holder, 5, 5, null));
        group.addExperiment(this.getExperiment(version, type, holder, 5, 6, null));
        group.addExperiment(this.getExperiment(version, type, holder, 5, 7, null));
        group.addExperiment(this.getExperiment(version, type, holder, 5, 8, null));

        return group;
    }

    private ExperimentGroup create2SubPositions(final WritableVersion version) throws ConstraintException {

        final ExperimentGroup group = new ExperimentGroup(version, PlateReaderTest.UNIQUE, "test");
        final ExperimentType type = new ExperimentType(version, PlateReaderTest.UNIQUE);
        final HolderType holderType = new HolderType(version, PlateReaderTest.UNIQUE);
        holderType.setMaxColumn(1);
        holderType.setMaxRow(1);
        holderType.setMaxSubPosition(2);
        final Holder holder = new Holder(version, PlateReaderTest.UNIQUE, holderType);

        group.addExperiment(this.getExperiment(version, type, holder, 1, 1, 1));
        group.addExperiment(this.getExperiment(version, type, holder, 1, 1, 2));
        return group;
    }

    private Experiment getExperiment(final WritableVersion version, final ExperimentType type,
        final Holder holder, final int row, final int column, final Integer subPosition)
        throws ConstraintException {
        final Experiment experiment =
            new Experiment(version, "exp" + row + column + subPosition + PlateReaderTest.UNIQUE,
                PlateReaderTest.NOW, PlateReaderTest.NOW, type);
        //experiment.setExperimentGroup(group);
        final Sample sample =
            new Sample(version, "exp1" + row + column + subPosition + PlateReaderTest.UNIQUE);
        if (null != holder) {
            sample.setHolder(holder);
        }
        sample.setRowPosition(row);
        sample.setColPosition(column);
        sample.setSubPosition(subPosition);
        new OutputSample(version, experiment).setSample(sample);
        return experiment;
    }

}
