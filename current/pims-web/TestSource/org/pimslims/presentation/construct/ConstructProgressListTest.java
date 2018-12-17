/**
 * pims-web org.pimslims.presentation.construct ConstructProgressListTest.java
 * 
 * @author cm65
 * @date 22 Jun 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.construct;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.ModelObjectBean;

/**
 * ConstructProgressListTest
 * 
 */
@Deprecated
// obsolete
public class ConstructProgressListTest extends TestCase {

    private static final String UNIQUE = "cpl" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    public ConstructProgressListTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    public void testNone() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ConstructProgressListTest.UNIQUE);
            final ResearchObjective ro =
                new ResearchObjective(version, ConstructProgressListTest.UNIQUE,
                    ConstructProgressListTest.UNIQUE);
            new Experiment(version, ConstructProgressListTest.UNIQUE + "exp", ConstructProgressListTest.NOW,
                ConstructProgressListTest.NOW, type).setProject(ro);
            final Collection<List<ModelObjectBean>> progress = ConstructBeanReader.getConstructProgress(ro);
            Assert.assertTrue(progress.isEmpty());
        } finally {
            version.abort();
        }
    }

    public void testOne() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ConstructProgressListTest.UNIQUE);
            final ResearchObjective ro =
                new ResearchObjective(version, ConstructProgressListTest.UNIQUE,
                    ConstructProgressListTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, ConstructProgressListTest.UNIQUE + "exp",
                    ConstructProgressListTest.NOW, ConstructProgressListTest.NOW, type);
            experiment.setProject(ro);
            new OutputSample(version, experiment).setSample(new Sample(version,
                ConstructProgressListTest.UNIQUE));

            final Collection<List<ModelObjectBean>> progress = ConstructBeanReader.getConstructProgress(ro);
            Assert.assertEquals(1, progress.size());
            final List<ModelObjectBean> list = progress.iterator().next();
            Assert.assertEquals(1, list.size());
            Assert.assertEquals(experiment.getName(), list.iterator().next().getName());
        } finally {
            version.abort();
        }
    }

    public void testAncestor() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ResearchObjective ro =
                new ResearchObjective(version, ConstructProgressListTest.UNIQUE,
                    ConstructProgressListTest.UNIQUE);
            final ExperimentType type = new ExperimentType(version, ConstructProgressListTest.UNIQUE);
            final Sample input = new Sample(version, ConstructProgressListTest.UNIQUE + "in");
            final Experiment ancestor =
                new Experiment(version, ConstructProgressListTest.UNIQUE + "start",
                    ConstructProgressListTest.NOW, ConstructProgressListTest.NOW, type);
            new OutputSample(version, ancestor).setSample(input);
            ancestor.setProject(ro);

            final Experiment experiment =
                new Experiment(version, ConstructProgressListTest.UNIQUE + "final",
                    ConstructProgressListTest.NOW, ConstructProgressListTest.NOW, type);
            experiment.setProject(ro);
            new OutputSample(version, experiment).setSample(new Sample(version,
                ConstructProgressListTest.UNIQUE + "out"));
            new InputSample(version, experiment).setSample(input);

            final Collection<List<ModelObjectBean>> progress = ConstructBeanReader.getConstructProgress(ro);
            Assert.assertEquals(1, progress.size());
            final List<ModelObjectBean> list = progress.iterator().next();
            Assert.assertEquals(2, list.size());
            final Iterator<ModelObjectBean> iterator = list.iterator();
            Assert.assertEquals(ancestor.getName(), iterator.next().getName());
            Assert.assertEquals(experiment.getName(), iterator.next().getName());
        } finally {
            version.abort();
        }
    }

    public void testTrialsIgnored() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type =
                version.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, "Trials");
            final ResearchObjective ro =
                new ResearchObjective(version, ConstructProgressListTest.UNIQUE,
                    ConstructProgressListTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, ConstructProgressListTest.UNIQUE + "exp",
                    ConstructProgressListTest.NOW, ConstructProgressListTest.NOW, type);
            experiment.setProject(ro);
            experiment.setExperimentGroup(new ExperimentGroup(version, ConstructProgressListTest.UNIQUE,
                ConstructProgressListTest.UNIQUE));
            new OutputSample(version, experiment).setSample(new Sample(version,
                ConstructProgressListTest.UNIQUE));

            final Collection<List<ModelObjectBean>> progress = ConstructBeanReader.getConstructProgress(ro);
            Assert.assertEquals(0, progress.size());
        } finally {
            version.abort();
        }
    }
}
