/**
 * V3_1-pims-web org.pimslims.lab.experiment CohortTest.java
 * 
 * @author cm65
 * @date 13 Jan 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.lab.experiment;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.protocol.ProtocolFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;

/**
 * CohortTest
 * 
 */
public class CohortTest extends TestCase {

    private static final String UNIQUE = "c" + System.currentTimeMillis();

    private static final String PLATE_NAME = CohortTest.UNIQUE + "p";

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    /**
     * Constructor for CohortTest
     * 
     * @param name
     */
    public CohortTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    private ExperimentGroup createGroup(final WritableVersion wv) throws ConstraintException, AccessException {
        final ExperimentType type =
            ProtocolFactory.getExperimentType(wv, "cohort" + System.currentTimeMillis());
        final Collection<ExperimentType> types = Collections.singleton(type);
        Assert.assertNotNull(type);
        final Protocol protocol = new Protocol(wv, "cohort" + System.currentTimeMillis(), type);
        final SampleCategory category = new SampleCategory(wv, CohortTest.UNIQUE);
        final RefOutputSample output = new RefOutputSample(wv, category, protocol);
        output.setName("output");
        final HolderType holderType = new HolderType(wv, CohortTest.UNIQUE);
        holderType.setMaxRow(3);
        holderType.setMaxColumn(3);
        final Holder expPlate = new Holder(wv, CohortTest.PLATE_NAME, holderType);
        final ExperimentGroup group =
            HolderFactory.createPlateExperiment(wv, null, expPlate, "group " + new Date().getTime(),
                "just testing", types, CohortTest.NOW, CohortTest.NOW, "", // details
                protocol, Collections.EMPTY_MAP, null);
        return group;
    }

    /**
     * CohortTest.createNextExperiment
     * 
     * Add a follow on experiment, with a new experiment type
     * 
     * @param experiment
     * @throws AccessException
     * @throws ConstraintException
     */
    private Experiment createNextExperiment(final Experiment experiment, final Protocol protocol,
        final int index) throws ConstraintException, AccessException {
        final WritableVersion version = (WritableVersion) experiment.get_Version();
        final OutputSample os = experiment.findFirst(Experiment.PROP_OUTPUTSAMPLES);
        final Sample sample = os.getSample();
        final ExperimentType type = protocol.getExperimentType();
        final Experiment next =
            new Experiment(version, CohortTest.UNIQUE + "next" + index, CohortTest.NOW, CohortTest.NOW, type);
        new InputSample(version, next).setSample(sample);
        next.setProtocol(protocol);
        final Sample out = new Sample(version, CohortTest.UNIQUE + "s" + index);
        new OutputSample(version, next).setSample(out);
        return next;
    }

    public void testPlate() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = this.createGroup(version);
            final Cohort cohort = new Cohort(group.getExperiments());
            final List<Holder> plates = cohort.getPlates();
            Assert.assertEquals(1, plates.size());
            Assert.assertEquals(CohortTest.PLATE_NAME, plates.iterator().next().getName());
            Assert.assertEquals(1, cohort.getNumberOfStages());
            Assert.assertEquals(9, cohort.getWells().size());
            final Iterator<WellBean> iterator = cohort.getWells().iterator();
            Assert.assertEquals("A01", iterator.next().getWell());
            Assert.assertEquals("B01", iterator.next().getWell());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testTwoPlates() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = this.createGroup(version);
            final Holder expPlate = new Holder(version, CohortTest.PLATE_NAME + "2", null);
            final HolderType holderType = new HolderType(version, CohortTest.UNIQUE + "ht2");
            holderType.setMaxRow(1);
            holderType.setMaxColumn(1);
            expPlate.setHolderType(holderType);
            final Protocol protocol = group.getExperiments().iterator().next().getProtocol();
            final Experiment experiment =
                new Experiment(version, CohortTest.UNIQUE, CohortTest.NOW, CohortTest.NOW,
                    protocol.getExperimentType());
            experiment.setProtocol(protocol);
            final Sample sample = new Sample(version, CohortTest.UNIQUE + "s2");
            sample.setContainer(expPlate);
            sample.setRowPosition(1);
            sample.setColPosition(1);
            new OutputSample(version, experiment).setSample(sample);

            final Collection<Experiment> experiments = new HashSet(group.getExperiments());
            experiments.add(experiment);
            final Cohort cohort = new Cohort(experiments);
            final List<Holder> plates = cohort.getPlates();
            Assert.assertEquals(2, plates.size());
            Assert.assertEquals(10, cohort.getWells().size());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testNumberOfStages() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = this.createGroup(version);
            final Experiment experiment = group.findFirst(ExperimentGroup.PROP_EXPERIMENTS);
            final ExperimentType type = new ExperimentType(version, CohortTest.UNIQUE);
            final Protocol protocol = new Protocol(version, CohortTest.UNIQUE, type);
            this.createNextExperiment(experiment, protocol, 0);
            final Cohort cohort = new Cohort(group.getExperiments());
            Assert.assertEquals(2, cohort.getNumberOfStages());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testStopOnFailedExperiment() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = this.createGroup(version);
            final Experiment experiment = group.findFirst(ExperimentGroup.PROP_EXPERIMENTS);
            final ExperimentType type = new ExperimentType(version, CohortTest.UNIQUE);
            final Protocol protocol = new Protocol(version, CohortTest.UNIQUE, type);
            this.createNextExperiment(experiment, protocol, 0);
            experiment.setStatus("Failed");
            final Cohort cohort = new Cohort(group.getExperiments());
            Assert.assertEquals(1, cohort.getNumberOfStages());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testGetProtocol() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = this.createGroup(version);
            final Experiment experiment = group.findFirst(ExperimentGroup.PROP_EXPERIMENTS);
            final String well = HolderFactory.getPositionInHolder(experiment);
            final ExperimentType type = new ExperimentType(version, CohortTest.UNIQUE);
            final Protocol protocol = new Protocol(version, CohortTest.UNIQUE, type);
            final Experiment next = this.createNextExperiment(experiment, protocol, 0);
            final Cohort cohort = new Cohort(group.getExperiments());
            Assert.assertEquals(protocol, cohort.getProtocol(1));
            Assert.assertEquals(next, cohort.getExperiment(new WellBean(CohortTest.PLATE_NAME, well), 1));
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testGetMajorityProtocol() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = this.createGroup(version);
            final ExperimentType type = new ExperimentType(version, CohortTest.UNIQUE);
            final Protocol majority = new Protocol(version, CohortTest.UNIQUE, type);
            final Protocol minority = new Protocol(version, CohortTest.UNIQUE + "min", type);
            final Set<Experiment> experiments = group.getExperiments();
            final Iterator<Experiment> iterator = experiments.iterator();
            this.createNextExperiment(iterator.next(), minority, 0);
            this.createNextExperiment(iterator.next(), majority, 1);
            this.createNextExperiment(iterator.next(), majority, 2);
            this.createNextExperiment(iterator.next(), majority, 3);
            this.createNextExperiment(iterator.next(), minority, 4);
            final Cohort cohort = new Cohort(group.getExperiments());
            Assert.assertEquals(majority, cohort.getProtocol(1));
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testIgnoreQA() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = this.createGroup(version);
            final ExperimentType type = new ExperimentType(version, CohortTest.UNIQUE);
            final Protocol qa = new Protocol(version, CohortTest.UNIQUE + "qa", type);
            final Protocol production = new Protocol(version, CohortTest.UNIQUE + "min", type);
            final Set<Experiment> experiments = group.getExperiments();
            final Iterator<Experiment> iterator = experiments.iterator();
            Experiment experiment = iterator.next();
            this.createNextExperiment(experiment, production, 0);
            version.delete(this.createNextExperiment(experiment, qa, 1).getOutputSamples());
            experiment = iterator.next();
            version.delete(this.createNextExperiment(experiment, qa, 3).getOutputSamples());
            experiment = iterator.next();
            version.delete(this.createNextExperiment(experiment, qa, 4).getOutputSamples());
            this.createNextExperiment(experiment, production, 2);
            version.flush();

            final Cohort cohort = new Cohort(group.getExperiments());
            Assert.assertEquals(production, cohort.getProtocol(1));
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testGetLongestBranch() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = this.createGroup(version);
            final Experiment experiment = group.findFirst(ExperimentGroup.PROP_EXPERIMENTS);
            final String well = HolderFactory.getPositionInHolder(experiment);
            final ExperimentType type = new ExperimentType(version, CohortTest.UNIQUE);
            final Protocol protocol = new Protocol(version, CohortTest.UNIQUE + "p1", type);

            // tried, no good
            this.createNextExperiment(experiment, protocol, 0);

            // tried again, went forward
            final Experiment next = this.createNextExperiment(experiment, protocol, 1);
            final ExperimentType type2 = new ExperimentType(version, CohortTest.UNIQUE + "t2");
            final Protocol protocol2 = new Protocol(version, CohortTest.UNIQUE + "p2", type2);
            this.createNextExperiment(next, protocol2, 3);

            // tried again, no good
            this.createNextExperiment(experiment, protocol, 2);

            final Cohort cohort = new Cohort(group.getExperiments());
            Assert.assertEquals(3, cohort.getNumberOfStages());
            Assert.assertEquals(protocol2, cohort.getProtocol(2));
            Assert.assertEquals(next, cohort.getExperiment(new WellBean(CohortTest.PLATE_NAME, well), 1));
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testIgnoreRecovery() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentGroup group = this.createGroup(version);
            final ExperimentType type = new ExperimentType(version, CohortTest.UNIQUE);
            final Protocol production = new Protocol(version, CohortTest.UNIQUE, type);
            final Protocol recovery = new Protocol(version, CohortTest.UNIQUE + "min", type);
            final Set<Experiment> experiments = group.getExperiments();
            final Iterator<Experiment> iterator = experiments.iterator();
            this.createNextExperiment(iterator.next(), production, 1);
            this.createNextExperiment(iterator.next(), production, 2);
            final Experiment needsRecovery = iterator.next();
            final Experiment recoveryExperiment = this.createNextExperiment(needsRecovery, recovery, 3);
            final Experiment recovered = this.createNextExperiment(recoveryExperiment, production, 4);

            final Cohort cohort = new Cohort(group.getExperiments());
            Assert.assertEquals(production, cohort.getProtocol(1));
            final String well = HolderFactory.getPositionInHolder(needsRecovery);
            Assert
                .assertEquals(recovered, cohort.getExperiment(new WellBean(CohortTest.PLATE_NAME, well), 1));
        } finally {
            version.abort(); // not testing persistence
        }
    }

}
