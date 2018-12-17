package org.pimslims.search;

/**
 * pims-web org.pimslims.servlet.construct SearchConstructTest.java
 * 
 * @author Marc Savitsky
 * @date 27 Jan 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.Assert;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.target.Alias;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.Target;
import org.pimslims.model.target.TargetGroup;
import org.pimslims.search.Conditions;
import org.pimslims.search.Paging;
import org.pimslims.search.Searcher;
import org.pimslims.search.Paging.Order;
import org.pimslims.search.conditions.HQLCondition;
import org.pimslims.test.AbstractTestCase;

/**
 * SetupPlateTest hmm
 */
public class SearchTargetTest extends AbstractTestCase {

    Target target1;

    Target target2;

    Target target3;

    Target target4;

    Target target5;

    Target target6;

    Target target7;

    Target target8;

    Target target9;

    Target target10;

    Target target11;

    Target target12;

    /**
     * Constructor for SetupPlateTest
     * 
     * @param name
     */
    public SearchTargetTest(final String name) {
        super(name);
    }

    /**
     * SetupPlateTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.wv = this.getWV();

        final TargetGroup tg1 = this.create(TargetGroup.class, TargetGroup.PROP_NAME, "TargetGroup1");
        final TargetGroup tg2 = this.create(TargetGroup.class);

        final TargetStatus ts1 = this.create(TargetStatus.class, TargetStatus.PROP_NAME, "TargetStatus1");
        final TargetStatus ts2 = this.create(TargetStatus.class, TargetStatus.PROP_NAME, "TargetStatus2");
        final TargetStatus ts3 = this.create(TargetStatus.class, TargetStatus.PROP_NAME, "TargetStatus3");
        Milestone milestone;

        //this.create(Milestone.class, Milestone.PROP_STATUS, ts1);
        //this.create(Milestone.class, Milestone.PROP_STATUS, ts2);

        final Molecule protein1 = this.create(Molecule.class, Substance.PROP_NAME, "Target1");
        final Molecule protein2 = this.create(Molecule.class, Substance.PROP_NAME, "Target2");
        final Molecule protein3 = this.create(Molecule.class, Substance.PROP_NAME, "Target3");
        final Molecule protein4 = this.create(Molecule.class, Substance.PROP_NAME, "Target4");
        final Molecule protein5 = this.create(Molecule.class, Substance.PROP_NAME, "Target5");
        final Molecule protein6 = this.create(Molecule.class, Substance.PROP_NAME, "Target6");
        final Molecule protein7 = this.create(Molecule.class, Substance.PROP_NAME, "Target7");
        final Molecule protein8 = this.create(Molecule.class, Substance.PROP_NAME, "Target8");
        final Molecule protein9 = this.create(Molecule.class, Substance.PROP_NAME, "Target9");
        final Molecule protein10 = this.create(Molecule.class, Substance.PROP_NAME, "Target10");
        final Molecule protein11 = this.create(Molecule.class, Substance.PROP_NAME, "Target11");
        final Molecule protein12 = this.create(Molecule.class, Substance.PROP_NAME, "Target12");

        this.target1 = this.create(Target.class, Target.PROP_PROTEIN, protein1);
        this.target1.setAliasNames(Collections.singletonList("Target1"));
        this.target1.addTargetGroup(tg1);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts1);
        milestone.setDate(new GregorianCalendar(2019, 0, 10));
        this.target1.addMilestone(milestone);

        this.target2 = this.create(Target.class, Target.PROP_PROTEIN, protein2);
        this.target2.setAliasNames(Collections.singletonList("Target2"));
        this.target2.addTargetGroup(tg1);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts1);
        milestone.setDate(new GregorianCalendar(2019, 0, 20));
        this.target2.addMilestone(milestone);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts2);
        milestone.setDate(new GregorianCalendar(2019, 0, 25));
        this.target2.addMilestone(milestone);

        this.target3 = this.create(Target.class, Target.PROP_PROTEIN, protein3);
        this.target3.setAliasNames(Collections.singletonList("Target3"));
        this.target3.addTargetGroup(tg1);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts1);
        milestone.setDate(new GregorianCalendar(2019, 0, 21));
        this.target3.addMilestone(milestone);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts2);
        milestone.setDate(new GregorianCalendar(2019, 0, 24));
        this.target3.addMilestone(milestone);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts3);
        milestone.setDate(new GregorianCalendar(2019, 0, 27));
        this.target3.addMilestone(milestone);

        this.target4 = this.create(Target.class, Target.PROP_PROTEIN, protein4);
        this.target4.setAliasNames(Collections.singletonList("Target4"));
        this.target4.addTargetGroup(tg1);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts1);
        milestone.setDate(new GregorianCalendar(2019, 0, 22));
        this.target4.addMilestone(milestone);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts2);
        milestone.setDate(new GregorianCalendar(2019, 0, 26));
        this.target4.addMilestone(milestone);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts3);
        milestone.setDate(new GregorianCalendar(2019, 0, 30));
        this.target4.addMilestone(milestone);

        this.target5 = this.create(Target.class, Target.PROP_PROTEIN, protein5);
        this.target5.setAliasNames(Collections.singletonList("Target5"));
        this.target5.addTargetGroup(tg2);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts1);
        milestone.setDate(new GregorianCalendar(2019, 1, 28));
        this.target5.addMilestone(milestone);

        this.target6 = this.create(Target.class, Target.PROP_PROTEIN, protein6);
        this.target6.setAliasNames(Collections.singletonList("Target6"));
        this.target6.addTargetGroup(tg2);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts1);
        milestone.setDate(new GregorianCalendar(2019, 1, 12));
        this.target6.addMilestone(milestone);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts2);
        milestone.setDate(new GregorianCalendar(2019, 1, 25));
        this.target6.addMilestone(milestone);

        this.target7 = this.create(Target.class, Target.PROP_PROTEIN, protein7);
        this.target7.setAliasNames(Collections.singletonList("Target7"));
        this.target7.addTargetGroup(tg2);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts1);
        milestone.setDate(new GregorianCalendar(2019, 0, 22));
        this.target7.addMilestone(milestone);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts2);
        milestone.setDate(new GregorianCalendar(2019, 0, 25));
        this.target7.addMilestone(milestone);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts3);
        milestone.setDate(new GregorianCalendar(2019, 0, 26));
        this.target7.addMilestone(milestone);

        this.target8 = this.create(Target.class, Target.PROP_PROTEIN, protein8);
        this.target8.setAliasNames(Collections.singletonList("Target8"));
        this.target8.addTargetGroup(tg2);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts1);
        milestone.setDate(new GregorianCalendar(2019, 1, 21));
        this.target8.addMilestone(milestone);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts2);
        milestone.setDate(new GregorianCalendar(2019, 1, 24));
        this.target8.addMilestone(milestone);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts3);
        milestone.setDate(new GregorianCalendar(2019, 1, 27));
        this.target8.addMilestone(milestone);

        this.target9 = this.create(Target.class, Target.PROP_PROTEIN, protein9);
        this.target9.setAliasNames(Collections.singletonList("Target9"));
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts1);
        milestone.setDate(new GregorianCalendar(2019, 1, 01));
        this.target9.addMilestone(milestone);

        this.target10 = this.create(Target.class, Target.PROP_PROTEIN, protein10);
        this.target10.setAliasNames(Collections.singletonList("Target10"));
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts1);
        milestone.setDate(new GregorianCalendar(2019, 0, 01));
        this.target10.addMilestone(milestone);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts2);
        milestone.setDate(new GregorianCalendar(2019, 0, 12));
        this.target10.addMilestone(milestone);

        this.target11 = this.create(Target.class, Target.PROP_PROTEIN, protein11);
        this.target11.setAliasNames(Collections.singletonList("Target11"));
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts1);
        milestone.setDate(new GregorianCalendar(2019, 0, 21));
        this.target11.addMilestone(milestone);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts2);
        milestone.setDate(new GregorianCalendar(2019, 0, 24));
        this.target11.addMilestone(milestone);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts3);
        milestone.setDate(new GregorianCalendar(2019, 1, 05));
        this.target11.addMilestone(milestone);

        this.target12 = this.create(Target.class, Target.PROP_PROTEIN, protein12);
        this.target12.setAliasNames(Collections.singletonList("Target12"));
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts1);
        milestone.setDate(new GregorianCalendar(2019, 0, 11));
        this.target12.addMilestone(milestone);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts2);
        milestone.setDate(new GregorianCalendar(2019, 0, 14));
        this.target12.addMilestone(milestone);
        milestone = this.create(Milestone.class, Milestone.PROP_STATUS, ts3);
        milestone.setDate(new GregorianCalendar(2019, 0, 17));
        this.target12.addMilestone(milestone);

    }

    /**
     * SetupPlateTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        if (this.wv.isCompleted()) {
            // can't tidy up
            return;
        }
        if (null != this.wv) {
            this.wv.abort(); // not testing persistence
        }
    }

    public void testProteinNameSearch() {

        try {
            //prepare search
            final Map<String, Object> mCriteria =
                Conditions.newMap(Substance.PROP_NAME, Conditions.contains("Target1"));

            final Map<String, Object> criteria = Conditions.newMap(Target.PROP_PROTEIN, mCriteria);

            //do search
            final Collection<Target> targets = this.wv.findAll(Target.class, criteria, new Paging(0, 12));

            Assert.assertTrue(targets.contains(this.target1));
            Assert.assertFalse(targets.contains(this.target2));
            Assert.assertFalse(targets.contains(this.target3));
            Assert.assertFalse(targets.contains(this.target4));
            Assert.assertFalse(targets.contains(this.target5));
            Assert.assertFalse(targets.contains(this.target6));
            Assert.assertFalse(targets.contains(this.target7));
            Assert.assertFalse(targets.contains(this.target8));
            Assert.assertFalse(targets.contains(this.target9));
            Assert.assertTrue(targets.contains(this.target10));
            Assert.assertTrue(targets.contains(this.target11));
            Assert.assertTrue(targets.contains(this.target12));

        } finally {
            this.wv.abort();
        }
    }

    public void testAliasSearch() {

        try {
            //prepare search
            final Map<String, Object> criteria =
                Conditions.newMap(Target.PROP_ALIASES, Conditions.newMap(Alias.PROP_NAME, Conditions
                    .contains("Target1")));
            //Conditions.newMap(Target.PROP_ALIASES, "Target1");

            //do search
            final Collection<Target> targets = this.wv.findAll(Target.class, criteria, new Paging(0, 12));

            Assert.assertTrue(targets.contains(this.target1));
            Assert.assertFalse(targets.contains(this.target2));
            Assert.assertFalse(targets.contains(this.target3));
            Assert.assertFalse(targets.contains(this.target4));
            Assert.assertFalse(targets.contains(this.target5));
            Assert.assertFalse(targets.contains(this.target6));
            Assert.assertFalse(targets.contains(this.target7));
            Assert.assertFalse(targets.contains(this.target8));
            Assert.assertFalse(targets.contains(this.target9));
            Assert.assertTrue(targets.contains(this.target10));
            Assert.assertTrue(targets.contains(this.target11));
            Assert.assertTrue(targets.contains(this.target12));

        } finally {
            this.wv.abort();
        }
    }

    public void testTargetGroupSearch() {

        try {
            //prepare search
            final TargetGroup targetGroup =
                this.wv.findFirst(TargetGroup.class, TargetGroup.PROP_NAME, "TargetGroup1");

            final Map<String, Object> criteria = Conditions.newMap(Target.PROP_TARGETGROUPS, targetGroup);

            //do search
            final Collection<Target> targets = this.wv.findAll(Target.class, criteria, new Paging(0, 12));

            Assert.assertTrue(targets.contains(this.target1));
            Assert.assertTrue(targets.contains(this.target2));
            Assert.assertTrue(targets.contains(this.target3));
            Assert.assertTrue(targets.contains(this.target4));
            Assert.assertFalse(targets.contains(this.target5));
            Assert.assertFalse(targets.contains(this.target6));
            Assert.assertFalse(targets.contains(this.target7));
            Assert.assertFalse(targets.contains(this.target8));
            Assert.assertFalse(targets.contains(this.target9));
            Assert.assertFalse(targets.contains(this.target10));
            Assert.assertFalse(targets.contains(this.target11));
            Assert.assertFalse(targets.contains(this.target12));

        } finally {
            this.wv.abort();
        }
    }

    public void testStatusSearch() {

        try {
            //prepare search
            final TargetStatus targetStatus =
                this.wv.findFirst(TargetStatus.class, TargetStatus.PROP_NAME, "TargetStatus2");

            final Map<String, Object> mCriteria = Conditions.newMap(Milestone.PROP_STATUS, targetStatus);

            final Map<String, Object> criteria = Conditions.newMap(Target.PROP_MILESTONES, mCriteria);

            //do search
            final Collection<Target> targets = this.wv.findAll(Target.class, criteria, new Paging(0, 12));

            Assert.assertFalse(targets.contains(this.target1));
            Assert.assertTrue(targets.contains(this.target2));
            Assert.assertTrue(targets.contains(this.target3));
            Assert.assertTrue(targets.contains(this.target4));
            Assert.assertFalse(targets.contains(this.target5));
            Assert.assertTrue(targets.contains(this.target6));
            Assert.assertTrue(targets.contains(this.target7));
            Assert.assertTrue(targets.contains(this.target8));
            Assert.assertFalse(targets.contains(this.target9));
            Assert.assertTrue(targets.contains(this.target10));
            Assert.assertTrue(targets.contains(this.target11));
            Assert.assertTrue(targets.contains(this.target12));

        } finally {
            this.wv.abort();
        }
    }

    public void testQuickSearch() {

        try {
            final MetaClass metaClass = this.wv.getMetaClass(Target.class);
            final Map criteria = new java.util.HashMap();
            final Paging paging = new Paging(0, 12);

            Collection<ModelObject> targets = new ArrayList<ModelObject>();

            final Searcher s = new Searcher(this.wv);
            targets = s.search(metaClass, criteria, "Target3", paging);

            Assert.assertFalse(targets.contains(this.target1));
            Assert.assertFalse(targets.contains(this.target2));
            Assert.assertTrue(targets.contains(this.target3));
            Assert.assertFalse(targets.contains(this.target4));
            Assert.assertFalse(targets.contains(this.target5));
            Assert.assertFalse(targets.contains(this.target6));
            Assert.assertFalse(targets.contains(this.target7));
            Assert.assertFalse(targets.contains(this.target8));
            Assert.assertFalse(targets.contains(this.target9));
            Assert.assertFalse(targets.contains(this.target10));
            Assert.assertFalse(targets.contains(this.target11));
            Assert.assertFalse(targets.contains(this.target12));

        } finally {
            this.wv.abort();
        }
    }

    //TODO this fails, replace it
    public void TODOtestTargetSearch() {

        try {
            //prepare search
            final Map<String, Object> pCriteria =
                Conditions.newMap(Substance.PROP_NAME, Conditions.contains("Target"));

            final Map<String, Object> criteria = Conditions.newMap(Target.PROP_PROTEIN, pCriteria);

            final Map<String, Object> mCriteria =
                Conditions.newMap(Milestone.PROP_DATE, new HQLCondition(
                    " milestones.date=(select max(milestone2.date) from " + Milestone.class.getName()
                        + " milestone2 where milestone2.target=A) "));
            criteria.put(Target.PROP_MILESTONES, mCriteria);

            //do search
            final LinkedHashMap<String, Order> orderBys = new LinkedHashMap<String, Order>();
            orderBys.put("milestones.date", Order.DESC);
            orderBys.put(LabBookEntry.PROP_DBID, Order.DESC);
            final Paging paging = new Paging(0, 12, orderBys);
            final Collection<Target> targets = this.wv.findAll(Target.class, criteria, paging);

            Assert.assertTrue(targets.contains(this.target1));
            Assert.assertTrue(targets.contains(this.target2));
            Assert.assertTrue(targets.contains(this.target3));
            Assert.assertTrue(targets.contains(this.target4));
            Assert.assertTrue(targets.contains(this.target5));
            Assert.assertTrue(targets.contains(this.target6));
            Assert.assertTrue(targets.contains(this.target7));
            Assert.assertTrue(targets.contains(this.target8));
            Assert.assertTrue(targets.contains(this.target9));
            Assert.assertTrue(targets.contains(this.target10));
            Assert.assertTrue(targets.contains(this.target11));
            Assert.assertTrue(targets.contains(this.target12));

            final Object[] resultArray = targets.toArray();

            final Collection<Target> targetList = new ArrayList<Target>();
            targetList.add(this.target5);
            targetList.add(this.target8);
            targetList.add(this.target6);
            targetList.add(this.target11);
            targetList.add(this.target9);
            targetList.add(this.target4);
            targetList.add(this.target3);
            targetList.add(this.target7);
            targetList.add(this.target2);
            targetList.add(this.target12);
            targetList.add(this.target10);
            targetList.add(this.target1);

            final Object[] targetArray = targetList.toArray();

            for (int i = 0; i < 12; i++) {
                final Target r = (Target) resultArray[i];
                final Target t = (Target) targetArray[i];
                Assert.assertEquals("Not as expected", t.getProtein().getName(), r.getProtein().getName());
            }

        } finally {
            this.wv.abort();
        }
    }

    public void testTargetLastMilestoneSearch() throws AccessException, ConstraintException {
        try {
            final TargetStatus ts1 = this.create(TargetStatus.class);
            final TargetStatus ts2 = this.create(TargetStatus.class);

            final Target t1 = this.create(Target.class);
            t1.addMilestone((Milestone) this.create(Milestone.class, Milestone.PROP_STATUS, ts2));
            t1.addMilestone((Milestone) this.create(Milestone.class, Milestone.PROP_STATUS, ts1));
            final Target t2 = this.create(Target.class);
            t2.addMilestone((Milestone) this.create(Milestone.class, Milestone.PROP_STATUS, ts1));
            t2.addMilestone((Milestone) this.create(Milestone.class, Milestone.PROP_STATUS, ts2));
            final Target t3 = this.create(Target.class);
            t3.addMilestone((Milestone) this.create(Milestone.class, Milestone.PROP_STATUS, ts1));

            final Map<String, Object> mCriteria = Conditions.newMap(Milestone.PROP_STATUS, ts1);
            mCriteria.put(Milestone.PROP_DATE, new HQLCondition(
                " milestones.date=(select max(milestone2.date) from " + Milestone.class.getName()
                    + " milestone2 where milestone2.target=A) "));
            final Map<String, Object> criteria = Conditions.newMap(Target.PROP_MILESTONES, mCriteria);

            //do search
            final Collection<Target> targets = this.wv.findAll(Target.class, criteria, new Paging(0, 10));

            //TODO Assert.assertTrue(targets.contains(t1));
            Assert.assertFalse(targets.contains(t2));
            Assert.assertTrue(targets.contains(t3));
            Assert.assertEquals(2, targets.size());
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    /**
     * All of the milestones are created with the same date, the list of targets produced should still be able
     * to find the latest milestone if dbid is used to select
     * 
     * SearchTargetTest.testTargetLatestMilestoneSearch
     * 
     * @throws AccessException
     * @throws ConstraintException
     */
    public void TODOtestTargetLatestMilestoneSearch() throws AccessException, ConstraintException {
        try {
            final TargetStatus ts1 = this.create(TargetStatus.class);
            final TargetStatus ts2 = this.create(TargetStatus.class);

            // targets need to be created here as 
            // default milestone 'Selected' created too
            final Target t1 = this.create(Target.class);
            final Target t2 = this.create(Target.class);
            final Target t3 = this.create(Target.class);

            final Calendar c = Calendar.getInstance();

            Milestone milestone = (Milestone) this.create(Milestone.class, Milestone.PROP_STATUS, ts2);
            milestone.setDate(c);
            t1.addMilestone(milestone);
            milestone = (Milestone) this.create(Milestone.class, Milestone.PROP_STATUS, ts1);
            milestone.setDate(c);
            t1.addMilestone(milestone);

            milestone = (Milestone) this.create(Milestone.class, Milestone.PROP_STATUS, ts1);
            milestone.setDate(c);
            t2.addMilestone(milestone);
            milestone = (Milestone) this.create(Milestone.class, Milestone.PROP_STATUS, ts2);
            milestone.setDate(c);
            t2.addMilestone(milestone);

            milestone = (Milestone) this.create(Milestone.class, Milestone.PROP_STATUS, ts1);
            milestone.setDate(c);
            t3.addMilestone(milestone);

            final Map<String, Object> mCriteria = Conditions.newMap(Milestone.PROP_STATUS, ts1);
            mCriteria
                .put(
                    Milestone.PROP_DATE,
                    new HQLCondition(
                        " milestones.date=(select max(milestone2.date) from "
                            + Milestone.class.getName()
                            + " milestone2 where milestone2.target=A) and  milestones.dbId=(select max(milestone3.dbId) from "
                            + Milestone.class.getName()
                            + " milestone3 where milestone3.target=A and milestone3.date=milestones.date )"));
            final Map<String, Object> criteria = Conditions.newMap(Target.PROP_MILESTONES, mCriteria);

            //do search
            final Collection<Target> targets = this.wv.findAll(Target.class, criteria, new Paging(0, 10));

            //System.out.println("targets size [" + targets.size() + "]");

            //Collection<Milestone> milestones = t1.getMilestones();
            /* for (final Milestone m : milestones) {
                 System.out.println("target 1 milestone [" + m.getStatus().get_Name() + ":"
                     + m.getDate().getTimeInMillis() + ":"
                     + DateFormat.getDateTimeInstance().format(m.getDate().getTime()));
             } */
/*
            milestones = t2.getMilestones();
            for (final Milestone m : milestones) {
                System.out.println("target 2 milestone [" + m.getStatus().get_Name() + ":"
                    + m.getDate().getTimeInMillis() + ":"
                    + DateFormat.getDateTimeInstance().format(m.getDate().getTime()));
            } */

            //milestones = t3.getMilestones();
            /*for (final Milestone m : milestones) {
                System.out.println("target 3 milestone [" + m.getStatus().get_Name() + ":"
                    + m.getDate().getTimeInMillis() + ":"
                    + DateFormat.getDateTimeInstance().format(m.getDate().getTime()));
            } */

            Assert.assertTrue(targets.contains(t1));
            Assert.assertFalse(targets.contains(t2));
            Assert.assertTrue(targets.contains(t3));
            Assert.assertEquals(2, targets.size());
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }
}
