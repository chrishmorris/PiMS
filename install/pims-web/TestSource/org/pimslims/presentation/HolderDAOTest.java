package org.pimslims.presentation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.Sample;
import org.pimslims.test.AbstractTestCase;

public class HolderDAOTest extends AbstractTestCase {

    public HolderDAOTest() {
        super("HolderDAO Test");
    }

    public void testNoGaps() throws ConstraintException {
        this.wv = this.getWV();
        try {
            final HolderType type = new HolderType(this.wv, AbstractTestCase.UNIQUE);
            type.setMaxColumn(1);
            type.setMaxRow(1);
            final Holder holder = new Holder(this.wv, AbstractTestCase.UNIQUE, type);
            final int[] gap = HolderDAO.findFirstGap(holder);
            Assert.assertEquals(1, gap[0]);
            Assert.assertEquals(1, gap[1]);

            final Sample sample = new Sample(this.wv, AbstractTestCase.UNIQUE);
            sample.setContainer(holder);
            sample.setRowPosition(1);
            sample.setColPosition(1);

            Assert.assertNull(HolderDAO.findFirstGap(holder));

        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    public void testFindSecondColumn() throws ConstraintException {
        this.wv = this.getWV();
        try {
            final HolderType type = new HolderType(this.wv, AbstractTestCase.UNIQUE);
            type.setMaxColumn(2);
            type.setMaxRow(1);
            final Holder holder = new Holder(this.wv, AbstractTestCase.UNIQUE, type);

            final Sample sample = new Sample(this.wv, AbstractTestCase.UNIQUE);
            sample.setContainer(holder);
            sample.setRowPosition(1);
            sample.setColPosition(1);

            final int[] gap = HolderDAO.findFirstGap(holder);
            Assert.assertEquals(2, gap[0]);
            Assert.assertEquals(1, gap[1]);

        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    public void testFindSecondRow() throws ConstraintException {
        this.wv = this.getWV();
        try {
            final HolderType type = new HolderType(this.wv, AbstractTestCase.UNIQUE);
            type.setMaxColumn(1);
            type.setMaxRow(2);
            final Holder holder = new Holder(this.wv, AbstractTestCase.UNIQUE, type);

            final Sample sample = new Sample(this.wv, AbstractTestCase.UNIQUE);
            sample.setContainer(holder);
            sample.setRowPosition(1);
            sample.setColPosition(1);

            final int[] gap = HolderDAO.findFirstGap(holder);
            Assert.assertEquals(1, gap[0]);
            Assert.assertEquals(2, gap[1]);

        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    public void testFindHoldersNotPlates() throws AccessException, ConstraintException {
        this.wv = this.getWV();
        try {
            // prepare testing data
            final Holder holder1 = HolderDAOTest.createHolderWithSample(this.wv, "holder 1", null);
            final Holder holder2 = HolderDAOTest.createHolderWithHolder(this.wv, "holder 2", null);
            final Holder holder3 = HolderDAOTest.createPlateHolder(this.wv, "holder 3", null);

            // check results
            final Collection<Holder> holders = HolderDAO.getHolders(this.wv);

            Assert.assertTrue(holders.contains(holder1));
            Assert.assertTrue(holders.contains(holder2));
            Assert.assertFalse(holders.contains(holder3));

        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    public void testFindSuperHolders() throws AccessException, ConstraintException {
        this.wv = this.getWV();
        try {
            // prepare testing data
            final Holder holder = AbstractTestCase.create(this.wv, Holder.class);

            final Holder holder1 = HolderDAOTest.createHolderWithSample(this.wv, "holder 1", null);
            final Holder holder2 = HolderDAOTest.createHolderWithHolder(this.wv, "holder 2", null);
            final Holder holder3 = HolderDAOTest.createPlateHolder(this.wv, "holder 3", null);

            final Holder holder4 = HolderDAOTest.createHolderWithSample(this.wv, "holder 4", holder);
            final Holder holder5 = HolderDAOTest.createHolderWithHolder(this.wv, "holder 5", holder);
            final Holder holder6 = HolderDAOTest.createPlateHolder(this.wv, "holder 6", holder);

            final Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(AbstractHolder.PROP_SUPHOLDER, null);

            // check results
            final Collection<Holder> holders = HolderDAO.getHolders(this.wv, criteria);

            Assert.assertTrue(holders.contains(holder1));
            Assert.assertTrue(holders.contains(holder2));
            Assert.assertFalse(holders.contains(holder3));
            Assert.assertFalse(holders.contains(holder4));
            Assert.assertFalse(holders.contains(holder5));
            Assert.assertFalse(holders.contains(holder6));

        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    public void testFindSubHolders() throws AccessException, ConstraintException {
        this.wv = this.getWV();
        try {
            // prepare testing data
            final Holder holder = AbstractTestCase.create(this.wv, Holder.class);

            final Holder holder1 = HolderDAOTest.createHolderWithSample(this.wv, "holder 1", null);
            final Holder holder2 = HolderDAOTest.createHolderWithHolder(this.wv, "holder 2", null);
            final Holder holder3 = HolderDAOTest.createPlateHolder(this.wv, "holder 3", null);

            final Holder holder4 = HolderDAOTest.createHolderWithSample(this.wv, "holder 4", holder);
            final Holder holder5 = HolderDAOTest.createHolderWithHolder(this.wv, "holder 5", holder);
            final Holder holder6 = HolderDAOTest.createPlateHolder(this.wv, "holder 6", holder);

            final Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(AbstractHolder.PROP_SUPHOLDER, holder);

            // check results
            final Collection<Holder> holders = HolderDAO.getHolders(this.wv, criteria);

            Assert.assertFalse(holders.contains(holder1));
            Assert.assertFalse(holders.contains(holder2));
            Assert.assertFalse(holders.contains(holder3));
            Assert.assertTrue(holders.contains(holder4));
            Assert.assertTrue(holders.contains(holder5));
            Assert.assertFalse(holders.contains(holder6));

        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    static Holder createHolderWithSample(final WritableVersion wv, final String name, final Holder supHolder)
        throws AccessException, ConstraintException {

        final Holder holder = AbstractTestCase.create(wv, Holder.class);
        holder.addSample((Sample) AbstractTestCase.create(wv, Sample.class));

        if (null != name) {
            holder.setName(name);
        }
        if (null != holder) {
            holder.setParentHolder(supHolder);
        }

        return holder;
    }

    static Holder createHolderWithHolder(final WritableVersion wv, final String name, final Holder supHolder)
        throws AccessException, ConstraintException {

        final Holder holder = AbstractTestCase.create(wv, Holder.class);
        holder.addSubHolder((Holder) AbstractTestCase.create(wv, Holder.class));

        if (null != name) {
            holder.setName(name);
        }
        if (null != holder) {
            holder.setParentHolder(supHolder);
        }
        return holder;
    }

    static Holder createPlateHolder(final WritableVersion wv, final String name, final Holder supHolder)
        throws AccessException, ConstraintException {

        final Holder holder = AbstractTestCase.create(wv, Holder.class);
        final Sample sample = (Sample) AbstractTestCase.create(wv, Sample.class);
        final OutputSample outputSample = (OutputSample) AbstractTestCase.create(wv, OutputSample.class);
        final Experiment experiment = (Experiment) AbstractTestCase.create(wv, Experiment.class);
        final ExperimentGroup group = (ExperimentGroup) AbstractTestCase.create(wv, ExperimentGroup.class);

        experiment.setExperimentGroup(group);
        outputSample.setExperiment(experiment);
        sample.setOutputSample(outputSample);
        holder.addSample(sample);

        if (null != name) {
            holder.setName(name);
        }
        if (null != holder) {
            holder.setParentHolder(supHolder);
        }
        return holder;
    }
}
