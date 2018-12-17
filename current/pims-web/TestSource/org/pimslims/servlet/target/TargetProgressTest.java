/**
 * current-pims-web org.pimslims.servlet.target TargetProgressTest.java
 * 
 * @author cm65
 * @date 7 Mar 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.servlet.target;

import java.sql.Time;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.Assert;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.target.TargetExperimentBean;
import org.pimslims.test.AbstractTestCase;

/**
 * TargetProgressTest
 * 
 */
public class TargetProgressTest extends AbstractTestCase {

    /**
     * 
     */
    private static final long ONE_DAY = 1000 * 60 * 60 * 24L;

    private static final String UNIQUE = "test" + System.currentTimeMillis();

    /**
     * 
     */
    //private static final String SCIENTIST = "p" + TargetProgressTest.UNIQUE;
    private static final Calendar NOW = java.util.Calendar.getInstance();

    private static final Calendar yesterday = Calendar.getInstance();
    {
        TargetProgressTest.yesterday.setTime(new Time(TargetProgressTest.NOW.getTime().getTime()
            - TargetProgressTest.ONE_DAY - 1));

    }

    private final AbstractModel model;

    /**
     * @param name
     */
    public TargetProgressTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.target.TargetProgress#search(org.pimslims.dao.ReadableVersion, java.lang.String, java.lang.String, long, boolean)}
     * .
     * 
     * @throws ConstraintException
     */
    public final void testSearchNone() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final User creator = new User(version, TargetProgressTest.UNIQUE);
            final Collection<TargetExperimentBean> search =
                TargetProgress.search(version, creator.get_Hook(), null, TargetProgressTest.ONE_DAY, true);
            Assert.assertNotNull(search);
            Assert.assertEquals(0, search.size());
        } finally {
            version.abort();
        }
    }

    public final void testSearchNoTarget() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final User scientist = AbstractTestCase.create(version, User.class);
            final ResearchObjective eb = new ResearchObjective(version, TargetProgressTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, TargetProgressTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, TargetProgressTest.UNIQUE, TargetProgressTest.NOW,
                    TargetProgressTest.NOW, type);
            //experiment.setCreator(scientist);
            experiment.setProject(eb);
            final Collection<TargetExperimentBean> search =
                TargetProgress.search(version, scientist.get_Hook(), null, TargetProgressTest.ONE_DAY, true);
            Assert.assertNotNull(search);
            Assert.assertEquals(0, search.size());
        } finally {
            version.abort();
        }
    }

    public final void testSearchOneTarget() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final User scientist = AbstractTestCase.create(version, User.class);
            final ResearchObjective eb = new ResearchObjective(version, TargetProgressTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, TargetProgressTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, TargetProgressTest.UNIQUE, TargetProgressTest.NOW,
                    TargetProgressTest.NOW, type);
            //experiment.setCreator(scientist);
            experiment.setProject(eb);

            final Molecule protein = new Molecule(version, "protein", TargetProgressTest.UNIQUE);
            final Target target = new Target(version, TargetProgressTest.UNIQUE, protein);
            final ResearchObjectiveElement bc = new ResearchObjectiveElement(version, "test", "target", eb);
            bc.setTarget(target);
            //target.setCreator(scientist);

            final Collection<TargetExperimentBean> search =
                TargetProgress.search(version, scientist.get_Hook(), null, TargetProgressTest.ONE_DAY, true);
            Assert.assertNotNull(search);
            Assert.assertEquals(1, search.size());
            final TargetExperimentBean bean = search.iterator().next();
            Assert.assertEquals(scientist.getName(), bean.getCreator());
            Assert.assertEquals(0, bean.getDaysSinceLastProgress());
            Assert.assertEquals(scientist.getName(), bean.getExperimentator());
            Assert.assertEquals("", bean.getLastMilestoneName());

        } finally {
            version.abort();
        }
    }

    public final void testSearchOneTargetManyExperiments() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final User scientist = AbstractTestCase.create(version, User.class);
            final ResearchObjective eb = new ResearchObjective(version, TargetProgressTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, TargetProgressTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, TargetProgressTest.UNIQUE, TargetProgressTest.NOW,
                    TargetProgressTest.NOW, type);
            //experiment.setCreator(scientist);
            experiment.setProject(eb);

            final Experiment experiment2 =
                new Experiment(version, TargetProgressTest.UNIQUE + "e2", TargetProgressTest.yesterday,
                    TargetProgressTest.yesterday, type);
            //experiment2.setCreator(scientist);
            experiment2.setProject(eb);

            final Molecule protein = new Molecule(version, "protein", TargetProgressTest.UNIQUE + "e2");
            final Target target = new Target(version, TargetProgressTest.UNIQUE + "e2", protein);
            final ResearchObjectiveElement bc = new ResearchObjectiveElement(version, "test", "target", eb);
            bc.setTarget(target);
            //target.setCreator(scientist);

            final Collection<TargetExperimentBean> search =
                TargetProgress.search(version, scientist.get_Hook(), null, TargetProgressTest.ONE_DAY, true);
            Assert.assertNotNull(search);
            Assert.assertEquals(1, search.size());
            final TargetExperimentBean bean = search.iterator().next();
            Assert.assertEquals(scientist.getName(), bean.getCreator());
            Assert.assertEquals(0, bean.getDaysSinceLastProgress());
            Assert.assertEquals(scientist.getName(), bean.getExperimentator());
            Assert.assertEquals("", bean.getLastMilestoneName());

        } finally {
            version.abort();
        }
    }

    public final void testSearchTargetTwoExperiments() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final User scientist = AbstractTestCase.create(version, User.class);
            final ResearchObjective eb = new ResearchObjective(version, TargetProgressTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, TargetProgressTest.UNIQUE);

            final Experiment experiment2 =
                new Experiment(version, TargetProgressTest.UNIQUE + "e2", TargetProgressTest.yesterday,
                    TargetProgressTest.yesterday, type);
            //experiment2.setCreator(scientist);
            experiment2.setProject(eb);

            final Molecule protein = new Molecule(version, "protein", TargetProgressTest.UNIQUE + "e2");
            final Target target = new Target(version, TargetProgressTest.UNIQUE + "e2", protein);
            final ResearchObjectiveElement bc = new ResearchObjectiveElement(version, "test", "target", eb);
            bc.setTarget(target);
            //target.setCreator(scientist);

            final Collection<TargetExperimentBean> search =
                TargetProgress.search(version, scientist.get_Hook(), null, TargetProgressTest.ONE_DAY, true);
            Assert.assertNotNull(search);
            Assert.assertEquals(0, search.size());

            final Collection<TargetExperimentBean> search2 =
                TargetProgress.search(version, scientist.get_Hook(), null, TargetProgressTest.ONE_DAY * 3,
                    true);
            Assert.assertNotNull(search2);
            Assert.assertEquals(1, search2.size());

            final TargetExperimentBean bean = search2.iterator().next();
            Assert.assertEquals(scientist.getName(), bean.getCreator());
            Assert.assertEquals(1, bean.getDaysSinceLastProgress());
            Assert.assertEquals(scientist.getName(), bean.getExperimentator());
            Assert.assertEquals("", bean.getLastMilestoneName());

        } finally {
            version.abort();
        }
    }

    /**
     * exp1 (today) exp2 (2 days before) \ /
     * 
     * expBluePrint1 expBluePrint2 | |
     * 
     * bluePrint1 bluePrint2 | |
     * 
     * target 1 target 2
     * 
     * TargetProgressTest.testSearchTargetThreeExperiments
     * 
     * @throws ConstraintException 2 targets and 2 experiments linked through 2 expblueprint
     * @throws AccessException
     */
    public final void testSearchTargetThreeExperiments() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final User scientist = AbstractTestCase.create(version, User.class);
            final ResearchObjective eb = new ResearchObjective(version, TargetProgressTest.UNIQUE, "test");
            final ResearchObjective eb2 =
                new ResearchObjective(version, TargetProgressTest.UNIQUE + "eb2", "test2");
            final ExperimentType type = new ExperimentType(version, TargetProgressTest.UNIQUE);

            final Experiment experiment =
                new Experiment(version, TargetProgressTest.UNIQUE, TargetProgressTest.NOW,
                    TargetProgressTest.NOW, type);
            //experiment.setCreator(scientist);
            experiment.setProject(eb);

            final Experiment experiment2 =
                new Experiment(version, TargetProgressTest.UNIQUE + "e2", TargetProgressTest.yesterday,
                    TargetProgressTest.yesterday, type);
            //experiment2.setCreator(scientist);
            experiment2.setProject(eb2);

            final Molecule protein = new Molecule(version, "protein", TargetProgressTest.UNIQUE + "e2");
            final Target target = new Target(version, TargetProgressTest.UNIQUE, protein);
            final ResearchObjectiveElement bc = new ResearchObjectiveElement(version, "test", "target", eb);
            bc.setTarget(target);
            //target.setCreator(scientist);

            //final Molecule protein2 =
            new Molecule(version, "protein", TargetProgressTest.UNIQUE + "p2");
            final Target target2 = new Target(version, TargetProgressTest.UNIQUE + "t2", protein);
            final ResearchObjectiveElement bc2 =
                new ResearchObjectiveElement(version, "test2", "target2", eb2);
            bc2.setTarget(target2);
            //target2.setCreator(scientist);

            final Collection<TargetExperimentBean> search =
                TargetProgress.search(version, scientist.get_Hook(), null, TargetProgressTest.ONE_DAY, true);
            Assert.assertNotNull(search);
            Assert.assertEquals(1, search.size());

            TargetExperimentBean bean = search.iterator().next();
            Assert.assertEquals(scientist.getName(), bean.getCreator());
            Assert.assertEquals(0, bean.getDaysSinceLastProgress());
            Assert.assertEquals(scientist.getName(), bean.getExperimentator());
            Assert.assertEquals(TargetProgressTest.UNIQUE, bean.getTarget().getName());
            Assert.assertEquals(TargetProgressTest.UNIQUE, bean.getLastExperiment().getName());

            final Collection<TargetExperimentBean> search2 =
                TargetProgress.search(version, scientist.get_Hook(), null, TargetProgressTest.ONE_DAY * 3,
                    true);
            Assert.assertNotNull(search2);
            Assert.assertEquals(2, search2.size());
            final Iterator<TargetExperimentBean> it = search2.iterator();
            while (it.hasNext()) {
                bean = it.next();
                if (bean.getLastExperiment().get_Name().equals(TargetProgressTest.UNIQUE)) {
                    Assert.assertEquals(0, bean.getDaysSinceLastProgress());
                } else {
                    Assert.assertEquals(1, bean.getDaysSinceLastProgress());
                }
            }

        } finally {
            version.abort();
        }
    }

    //2 targets and 2 experiments linked through 1 expblueprint
    public final void testSearchTarget4() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final User scientist = AbstractTestCase.create(version, User.class);
            final ResearchObjective eb = new ResearchObjective(version, TargetProgressTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, TargetProgressTest.UNIQUE);

            final Experiment experiment =
                new Experiment(version, TargetProgressTest.UNIQUE, TargetProgressTest.NOW,
                    TargetProgressTest.NOW, type);
            //experiment.setCreator(scientist);
            experiment.setProject(eb);

            final Experiment experiment2 =
                new Experiment(version, TargetProgressTest.UNIQUE + "e2", TargetProgressTest.yesterday,
                    TargetProgressTest.yesterday, type);
            //experiment2.setCreator(scientist);
            experiment2.setProject(eb);

            final Molecule protein = new Molecule(version, "protein", TargetProgressTest.UNIQUE + "e2");
            final Target target = new Target(version, TargetProgressTest.UNIQUE, protein);
            final ResearchObjectiveElement bc = new ResearchObjectiveElement(version, "test", "target", eb);
            bc.setTarget(target);
            //target.setCreator(scientist);

            //final Molecule protein2 =
            new Molecule(version, "protein", TargetProgressTest.UNIQUE + "p2");
            final Target target2 = new Target(version, TargetProgressTest.UNIQUE + "t2", protein);
            final ResearchObjectiveElement bc2 =
                new ResearchObjectiveElement(version, "test2", "target2", eb);
            bc2.setTarget(target2);
            //target2.setCreator(scientist);

            final Collection<TargetExperimentBean> search =
                TargetProgress.search(version, scientist.get_Hook(), null, TargetProgressTest.ONE_DAY, true);
            Assert.assertNotNull(search);
            Assert.assertEquals(1, search.size());

            TargetExperimentBean bean = search.iterator().next();
            Assert.assertEquals(scientist.getName(), bean.getCreator());
            Assert.assertEquals(0, bean.getDaysSinceLastProgress());
            Assert.assertEquals(scientist.getName(), bean.getExperimentator());
            // Cannot tell which target was progressed.
            //  Assert.assertEquals(TargetProgressTest.UNIQUE, bean.getTarget().getName());
            Assert.assertEquals(TargetProgressTest.UNIQUE, bean.getLastExperiment().getName());

            final Collection<TargetExperimentBean> search2 =
                TargetProgress.search(version, scientist.get_Hook(), null, TargetProgressTest.ONE_DAY * 3,
                    true);
            Assert.assertNotNull(search2);
            Assert.assertEquals(2, search2.size());
            final Iterator<TargetExperimentBean> it = search2.iterator();
            while (it.hasNext()) {
                bean = it.next();
                if (bean.getLastExperiment().get_Name().equals(TargetProgressTest.UNIQUE)) {
                    Assert.assertEquals(0, bean.getDaysSinceLastProgress());
                } else {
                    Assert.assertEquals(1, bean.getDaysSinceLastProgress());
                }
            }

        } finally {
            version.abort();
        }
    }

    //1 targets and 2 experiments linked through 1 expblueprint
    public final void testSearchTarget5() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final User scientist = AbstractTestCase.create(version, User.class);
            final ResearchObjective eb = new ResearchObjective(version, TargetProgressTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, TargetProgressTest.UNIQUE);

            final Experiment experiment =
                new Experiment(version, TargetProgressTest.UNIQUE, TargetProgressTest.NOW,
                    TargetProgressTest.NOW, type);
            //experiment.setCreator(scientist);
            experiment.setProject(eb);

            final Experiment experiment2 =
                new Experiment(version, TargetProgressTest.UNIQUE + "e2", TargetProgressTest.yesterday,
                    TargetProgressTest.yesterday, type);
            //experiment2.setCreator(scientist);
            experiment2.setProject(eb);

            final Molecule protein = new Molecule(version, "protein", TargetProgressTest.UNIQUE + "e2");
            final Target target = new Target(version, TargetProgressTest.UNIQUE, protein);
            final ResearchObjectiveElement bc = new ResearchObjectiveElement(version, "test", "target", eb);
            bc.setTarget(target);
            //target.setCreator(scientist);

            final Collection<TargetExperimentBean> search =
                TargetProgress.search(version, scientist.get_Hook(), null, TargetProgressTest.ONE_DAY, false);
            Assert.assertNotNull(search);
            Assert.assertEquals(1, search.size());

            TargetExperimentBean bean = search.iterator().next();
            Assert.assertEquals(scientist.getName(), bean.getCreator());
            Assert.assertEquals(0, bean.getDaysSinceLastProgress());
            Assert.assertEquals(scientist.getName(), bean.getExperimentator());
            Assert.assertEquals(TargetProgressTest.UNIQUE, bean.getTarget().getName());
            Assert.assertEquals(TargetProgressTest.UNIQUE, bean.getLastExperiment().getName());

            final Collection<TargetExperimentBean> search2 =
                TargetProgress.search(version, scientist.get_Hook(), null, TargetProgressTest.ONE_DAY * 4,
                    false); // show only the last experiment on the target
            Assert.assertNotNull(search2);
            Assert.assertEquals(1, search2.size());
            final Iterator<TargetExperimentBean> it = search2.iterator();
            while (it.hasNext()) {
                bean = it.next();
                if (bean.getLastExperiment().get_Name().equals(TargetProgressTest.UNIQUE)) {
                    Assert.assertEquals(0, bean.getDaysSinceLastProgress());
                } else {
                    Assert.assertEquals(2, bean.getDaysSinceLastProgress());
                }
            }

        } finally {
            version.abort();
        }
    }

    //1 targets and 2 experiments linked through 1 expblueprint
    public final void testTargetBeanEquals() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final User scientist = AbstractTestCase.create(version, User.class);
            final ResearchObjective eb = new ResearchObjective(version, TargetProgressTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, TargetProgressTest.UNIQUE);

            final Experiment experiment =
                new Experiment(version, TargetProgressTest.UNIQUE, TargetProgressTest.NOW,
                    TargetProgressTest.NOW, type);
            //experiment.setCreator(scientist);
            experiment.setProject(eb);

            final Experiment experiment2 =
                new Experiment(version, TargetProgressTest.UNIQUE + "e2", TargetProgressTest.yesterday,
                    TargetProgressTest.yesterday, type);
            //experiment2.setCreator(scientist);
            experiment2.setProject(eb);

            final Molecule protein = new Molecule(version, "protein", TargetProgressTest.UNIQUE + "e2");
            final Target target = new Target(version, TargetProgressTest.UNIQUE, protein);
            final ResearchObjectiveElement bc = new ResearchObjectiveElement(version, "test", "target", eb);
            bc.setTarget(target);
            //target.setCreator(scientist);

            final Collection<TargetExperimentBean> search2 =
                TargetProgress.search(version, scientist.get_Hook(), null, TargetProgressTest.ONE_DAY * 3,
                    true);
            Assert.assertNotNull(search2);
            Assert.assertEquals(2, search2.size());
            final Iterator<TargetExperimentBean> it = search2.iterator();
            final TargetExperimentBean bean1 = it.next();
            final TargetExperimentBean bean2 = it.next();
            Assert.assertEquals(bean1, bean2);

            final Collection<TargetExperimentBean> search3 =
                TargetProgress.search(version, scientist.get_Hook(), null, TargetProgressTest.ONE_DAY, true);
            Assert.assertNotNull(search3);
            Assert.assertEquals(1, search3.size());

        } finally {
            version.abort();
        }
    }
}
