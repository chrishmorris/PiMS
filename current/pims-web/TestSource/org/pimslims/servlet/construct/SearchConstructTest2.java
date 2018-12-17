package org.pimslims.servlet.construct;

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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Assert;

import org.pimslims.lab.ConstructUtility;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.model.target.TargetGroup;
import org.pimslims.search.Conditions;
import org.pimslims.search.Paging;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * SetupPlateTest
 * 
 */
public class SearchConstructTest2 extends AbstractTestCase {

    final String componentType0 = "OpticConstruct";

    final String componentType1 = ConstructUtility.SPOTCONSTRUCT;

    final String componentType2 = "OtherConstruct";

    TargetGroup targetGroup;

    ExperimentType experimentType;

    ResearchObjective researchObjective1;

    ResearchObjective researchObjective2;

    ResearchObjective researchObjective3;

    ResearchObjective researchObjective4;

    ResearchObjective researchObjective5;

    ResearchObjective researchObjective6;

    ResearchObjective researchObjective7;

    ResearchObjective researchObjective8;

    ResearchObjective researchObjective9;

    ResearchObjective researchObjective10;

    ResearchObjective researchObjective11;

    ResearchObjective researchObjective12;

    /**
     * Constructor for SetupPlateTest
     * 
     * @param name
     */
    public SearchConstructTest2(final String name) {
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

        this.experimentType = POJOFactory.createExperimentType(this.wv);//assume this is order exp type
        final ExperimentType et = POJOFactory.createExperimentType(this.wv);
        final Experiment experiment1_1 = //exoerment1 does not link with order 
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
        final Experiment experiment1_2 = //exoerment1 does not link with order 
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
        final Experiment experiment1_3 = //exoerment1 does not link with order 
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
        final Experiment experiment1_4 = //exoerment1 does not link with order 
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
        final Experiment experiment1_5 = //exoerment1 does not link with order 
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
        final Experiment experiment1_6 = //exoerment1 does not link with order 
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
        final Experiment experiment0_1 = //experiment0 link with order
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, this.experimentType);
        final Experiment experiment0_2 = //experiment0 link with order
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, this.experimentType);
        final Experiment experiment0_3 = //experiment0 link with order
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, this.experimentType);
        final Experiment experiment0_4 = //experiment0 link with order
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, this.experimentType);
        final Experiment experiment0_5 = //experiment0 link with order
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, this.experimentType);
        final Experiment experiment0_6 = //experiment0 link with order
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, this.experimentType);

        final Experiment experiment2_1 = //exoerment1 does not link with order 
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
        final Experiment experiment2_2 = //exoerment1 does not link with order 
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
        final Experiment experiment2_3 = //exoerment1 does not link with order 
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
        final Experiment experiment2_4 = //exoerment1 does not link with order 
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
        final Experiment experiment2_5 = //exoerment1 does not link with order 
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
        final Experiment experiment2_6 = //exoerment1 does not link with order 
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
        final Experiment experiment2_7 = //exoerment1 does not link with order 
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
        final Experiment experiment2_8 = //exoerment1 does not link with order 
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
        final Experiment experiment2_9 = //exoerment1 does not link with order 
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
        final Experiment experiment2_10 = //exoerment1 does not link with order 
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
        final Experiment experiment2_11 = //exoerment1 does not link with order 
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
        final Experiment experiment2_12 = //exoerment1 does not link with order 
            this.create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);

        this.targetGroup = this.create(TargetGroup.class);
        final TargetGroup tg = this.create(TargetGroup.class);
        final Target target1 = this.create(Target.class, Target.PROP_TARGETGROUPS, this.targetGroup);
        Assert.assertTrue(target1.getTargetGroups().contains(this.targetGroup));
        final Target target0 = this.create(Target.class, Target.PROP_TARGETGROUPS, tg);

        ResearchObjectiveElement roe;
        this.researchObjective1 = POJOFactory.create(this.wv, ResearchObjective.class);
        roe =
            POJOFactory.create(this.wv, ResearchObjectiveElement.class,
                ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE, this.researchObjective1);
        roe.setComponentType(this.componentType0);
        roe.setTarget(target0);
        this.researchObjective1.addExperiment(experiment0_1);
        this.researchObjective1.addExperiment(experiment2_1);
        this.researchObjective1.setCommonName("researchObjective1");

        this.researchObjective2 = POJOFactory.create(this.wv, ResearchObjective.class);
        roe =
            POJOFactory.create(this.wv, ResearchObjectiveElement.class,
                ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE, this.researchObjective2);
        roe.setComponentType(this.componentType0);
        roe.setTarget(target0);
        this.researchObjective2.addExperiment(experiment1_1);
        this.researchObjective2.addExperiment(experiment2_2);
        this.researchObjective2.setCommonName("researchObjective2");

        this.researchObjective3 = POJOFactory.create(this.wv, ResearchObjective.class);
        roe =
            POJOFactory.create(this.wv, ResearchObjectiveElement.class,
                ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE, this.researchObjective3);
        roe.setComponentType(this.componentType0);
        roe.setTarget(target1);
        this.researchObjective3.addExperiment(experiment0_2);
        this.researchObjective3.addExperiment(experiment2_3);
        this.researchObjective3.setCommonName("researchObjective3");

        this.researchObjective4 = POJOFactory.create(this.wv, ResearchObjective.class);
        roe =
            POJOFactory.create(this.wv, ResearchObjectiveElement.class,
                ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE, this.researchObjective4);
        roe.setComponentType(this.componentType0);
        roe.setTarget(target1);
        this.researchObjective4.addExperiment(experiment1_2);
        this.researchObjective4.addExperiment(experiment2_4);
        this.researchObjective4.setCommonName("researchObjective4");

        this.researchObjective5 = POJOFactory.create(this.wv, ResearchObjective.class);
        roe =
            POJOFactory.create(this.wv, ResearchObjectiveElement.class,
                ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE, this.researchObjective5);
        roe.setComponentType(this.componentType1);
        roe.setTarget(target0);
        this.researchObjective5.addExperiment(experiment0_3);
        this.researchObjective5.addExperiment(experiment2_5);
        this.researchObjective5.setCommonName("researchObjective5");

        this.researchObjective6 = POJOFactory.create(this.wv, ResearchObjective.class);
        roe =
            POJOFactory.create(this.wv, ResearchObjectiveElement.class,
                ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE, this.researchObjective6);
        roe.setComponentType(this.componentType1);
        roe.setTarget(target0);
        this.researchObjective6.addExperiment(experiment1_3);
        this.researchObjective6.addExperiment(experiment2_6);
        this.researchObjective6.setCommonName("researchObjective6");

        this.researchObjective7 = POJOFactory.create(this.wv, ResearchObjective.class);
        roe =
            POJOFactory.create(this.wv, ResearchObjectiveElement.class,
                ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE, this.researchObjective7);
        roe.setComponentType(this.componentType1);
        roe.setTarget(target1);
        this.researchObjective7.addExperiment(experiment0_4);
        this.researchObjective7.addExperiment(experiment2_7);
        this.researchObjective7.setCommonName("researchObjective7");

        this.researchObjective8 = POJOFactory.create(this.wv, ResearchObjective.class);
        roe =
            POJOFactory.create(this.wv, ResearchObjectiveElement.class,
                ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE, this.researchObjective8);
        roe.setComponentType(this.componentType1);
        roe.setTarget(target1);
        this.researchObjective8.addExperiment(experiment1_4);
        this.researchObjective8.addExperiment(experiment2_8);
        this.researchObjective8.setCommonName("researchObjective8");

        this.researchObjective9 = POJOFactory.create(this.wv, ResearchObjective.class);
        roe =
            POJOFactory.create(this.wv, ResearchObjectiveElement.class,
                ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE, this.researchObjective9);
        roe.setComponentType(this.componentType2);
        roe.setTarget(target0);
        this.researchObjective9.addExperiment(experiment0_5);
        this.researchObjective9.addExperiment(experiment2_9);
        this.researchObjective9.setCommonName("researchObjective9");

        this.researchObjective10 = POJOFactory.create(this.wv, ResearchObjective.class);
        roe =
            POJOFactory.create(this.wv, ResearchObjectiveElement.class,
                ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE, this.researchObjective10);
        roe.setComponentType(this.componentType2);
        roe.setTarget(target0);
        this.researchObjective10.addExperiment(experiment1_5);
        this.researchObjective10.addExperiment(experiment2_10);
        this.researchObjective10.setCommonName("researchObjective10");

        this.researchObjective11 = POJOFactory.create(this.wv, ResearchObjective.class);
        roe =
            POJOFactory.create(this.wv, ResearchObjectiveElement.class,
                ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE, this.researchObjective11);
        roe.setComponentType(this.componentType2);
        roe.setTarget(target1);
        this.researchObjective11.addExperiment(experiment0_6);
        this.researchObjective11.addExperiment(experiment2_11);
        this.researchObjective11.setCommonName("researchObjective11");

        this.researchObjective12 = POJOFactory.create(this.wv, ResearchObjective.class);
        roe =
            POJOFactory.create(this.wv, ResearchObjectiveElement.class,
                ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE, this.researchObjective12);
        roe.setComponentType(this.componentType2);
        roe.setTarget(target1);
        this.researchObjective12.addExperiment(experiment1_6);
        this.researchObjective12.addExperiment(experiment2_12);
        this.researchObjective12.setCommonName("researchObjective12");

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

    public void testConstructsComponentType() {
        System.out.println("Start testConstructsComponentType");
        try {

            //prepare search
            final Map<String, Object> roeCriteria =
                Conditions.newMap(
                    ResearchObjectiveElement.PROP_COMPONENTTYPE,
                    Conditions.or(Conditions.eq("OpticConstruct"),
                        Conditions.eq(ConstructUtility.SPOTCONSTRUCT)));

            final Map<String, Object> roCriteria =
                Conditions.newMap(ResearchObjective.PROP_RESEARCHOBJECTIVEELEMENTS, roeCriteria);

            //do search
            final Collection<ResearchObjective> ros =
                this.wv.findAll(ResearchObjective.class, roCriteria, new Paging(0, 12));

            //check result
            //final Iterator resultsItr = ros.iterator();
            //while (resultsItr.hasNext()) {
            //    final ResearchObjective ror = (ResearchObjective) resultsItr.next();
            //    System.out.println("result [" + ror.get_Name() + "]");
            //}

            Assert.assertTrue(ros.contains(this.researchObjective1));
            Assert.assertTrue(ros.contains(this.researchObjective2));
            Assert.assertTrue(ros.contains(this.researchObjective3));
            Assert.assertTrue(ros.contains(this.researchObjective4));
            Assert.assertTrue(ros.contains(this.researchObjective5));
            Assert.assertTrue(ros.contains(this.researchObjective6));
            Assert.assertTrue(ros.contains(this.researchObjective7));
            Assert.assertTrue(ros.contains(this.researchObjective8));
            Assert.assertFalse(ros.contains(this.researchObjective9));
            Assert.assertFalse(ros.contains(this.researchObjective10));
            Assert.assertFalse(ros.contains(this.researchObjective11));
            Assert.assertFalse(ros.contains(this.researchObjective12));

        } finally {
            this.wv.abort();
        }
        //System.out.println("End testConstructsComponentType");
    }

    public void testConstructsExperimentType() {
        System.out.println("Start testConstructsExperimentType");
        try {

            //prepare search
            final Map<String, Object> expCriteria =
                Conditions.newMap(Experiment.PROP_EXPERIMENTTYPE, Conditions.eq(this.experimentType));

            final Map<String, Object> roCriteria =
                Conditions.notExistsMap(Conditions.newMap(ResearchObjective.PROP_EXPERIMENTS, expCriteria));

            //do search
            final Collection<ResearchObjective> ros =
                this.wv.findAll(ResearchObjective.class, roCriteria, new Paging(0, 12));

            //check result
            final Iterator resultsItr = ros.iterator();

            while (resultsItr.hasNext()) {
                final ResearchObjective ror = (ResearchObjective) resultsItr.next();
                System.out.println("result [" + ror.get_Name() + "]");
            }

            Assert.assertFalse(ros.contains(this.researchObjective1));
            Assert.assertTrue(ros.contains(this.researchObjective2));
            Assert.assertFalse(ros.contains(this.researchObjective3));
            Assert.assertTrue(ros.contains(this.researchObjective4));
            Assert.assertFalse(ros.contains(this.researchObjective5));
            Assert.assertTrue(ros.contains(this.researchObjective6));
            Assert.assertFalse(ros.contains(this.researchObjective7));
            Assert.assertTrue(ros.contains(this.researchObjective8));
            Assert.assertFalse(ros.contains(this.researchObjective9));
            Assert.assertTrue(ros.contains(this.researchObjective10));
            Assert.assertFalse(ros.contains(this.researchObjective11));
            Assert.assertTrue(ros.contains(this.researchObjective12));

        } finally {
            this.wv.abort();
        }
        System.out.println("End testConstructsExperimentType");
    }

    public void testConstructsTargetGroup() {
        System.out.println("Start testConstructsTargetGroup");
        try {

            //prepare search
            final Map<String, Object> targetCriteria =
                Conditions.newMap(Target.PROP_TARGETGROUPS, this.targetGroup);

            final Map<String, Object> roeCriteria = new HashMap<String, Object>();
            roeCriteria.put(ResearchObjectiveElement.PROP_TARGET, targetCriteria);

            //do search
            final Map<String, Object> roCriteria =
                Conditions.newMap(ResearchObjective.PROP_RESEARCHOBJECTIVEELEMENTS, roeCriteria);

            final Collection<ResearchObjective> ros =
                this.wv.findAll(ResearchObjective.class, roCriteria, new Paging(0, 12));

            //check result
            final Iterator resultsItr = ros.iterator();

            while (resultsItr.hasNext()) {
                final ResearchObjective ror = (ResearchObjective) resultsItr.next();
                System.out.println("result [" + ror.get_Name() + "]");
            }

            Assert.assertFalse(ros.contains(this.researchObjective1));
            Assert.assertFalse(ros.contains(this.researchObjective2));
            Assert.assertTrue(ros.contains(this.researchObjective3));
            Assert.assertTrue(ros.contains(this.researchObjective4));
            Assert.assertFalse(ros.contains(this.researchObjective5));
            Assert.assertFalse(ros.contains(this.researchObjective6));
            Assert.assertTrue(ros.contains(this.researchObjective7));
            Assert.assertTrue(ros.contains(this.researchObjective8));
            Assert.assertFalse(ros.contains(this.researchObjective9));
            Assert.assertFalse(ros.contains(this.researchObjective10));
            Assert.assertTrue(ros.contains(this.researchObjective11));
            Assert.assertTrue(ros.contains(this.researchObjective12));

        } finally {
            this.wv.abort();
        }
        System.out.println("End testConstructsTargetGroup");
    }

    public void testOpticConstructExperimentTypeAndTargetGroup() {
        System.out.println("Start testOpticConstructsExperimentTypeAndTargetGroup");
        try {

            final Map<String, Object> criteria = new HashMap<String, Object>();

            //prepare search
            final Map<String, Object> expCriteria =
                Conditions.newMap(Experiment.PROP_EXPERIMENTTYPE, Conditions.eq(this.experimentType));
            final Map<String, Object> roCriteria =
                Conditions.notExistsMap(Conditions.newMap(ResearchObjective.PROP_EXPERIMENTS, expCriteria));

            final Map<String, Object> targetCriteria =
                Conditions.newMap(Target.PROP_TARGETGROUPS, this.targetGroup);

            final Map<String, Object> roeCriteria = new HashMap<String, Object>();
            roeCriteria.put(ResearchObjectiveElement.PROP_TARGET, targetCriteria);
            roeCriteria
                .put(
                    ResearchObjectiveElement.PROP_COMPONENTTYPE,
                    Conditions.or(Conditions.eq("OpticConstruct"),
                        Conditions.eq(ConstructUtility.SPOTCONSTRUCT)));

            roCriteria.put(ResearchObjective.PROP_RESEARCHOBJECTIVEELEMENTS, roeCriteria);
            criteria.putAll(roCriteria);

            //do search
            final Collection<ResearchObjective> ros =
                this.wv.findAll(ResearchObjective.class, criteria, new Paging(0, 12));

            //check result
            final Iterator resultsItr = ros.iterator();

            while (resultsItr.hasNext()) {
                final ResearchObjective ror = (ResearchObjective) resultsItr.next();
                System.out.println("result [" + ror.get_Name() + "]");
            }

            Assert.assertFalse(ros.contains(this.researchObjective1));
            Assert.assertFalse(ros.contains(this.researchObjective2));
            Assert.assertFalse(ros.contains(this.researchObjective3));
            Assert.assertTrue(ros.contains(this.researchObjective4));
            Assert.assertFalse(ros.contains(this.researchObjective5));
            Assert.assertFalse(ros.contains(this.researchObjective6));
            Assert.assertFalse(ros.contains(this.researchObjective7));
            Assert.assertTrue(ros.contains(this.researchObjective8));
            Assert.assertFalse(ros.contains(this.researchObjective9));
            Assert.assertFalse(ros.contains(this.researchObjective10));
            Assert.assertFalse(ros.contains(this.researchObjective11));
            Assert.assertFalse(ros.contains(this.researchObjective12));

        } finally {
            this.wv.abort();
        }
        System.out.println("End testOpticConstructsExperimentTypeAndTargetGroup");
    }

}
