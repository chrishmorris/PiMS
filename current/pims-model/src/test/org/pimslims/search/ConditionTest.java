/**
 * DM org.pimslims.hibernate RestrictionTest.java
 * 
 * @author bl67
 * @date 8 Feb 2008
 * 
 *       Protein Information Management System
 * @version: 2.0
 * 
 *           Copyright (c) 2008 bl67 This library is free software; you can redistribute it and/or modify it
 *           under the terms of the GNU Lesser General Public License as published by the Free Software
 *           Foundation; either version 2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */
package org.pimslims.search;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.Target;
import org.pimslims.search.conditions.HQLCondition;
import org.pimslims.test.AbstractTestCase;

/**
 * RestrictionTest
 * 
 */
public class ConditionTest extends AbstractTestCase {

    public void testIn() {
        AbstractCondition condition = Conditions.listContains("zinc sulfate,heptahydrate");
        assertEquals(" :value1 in (synonyms) ", condition.getHQLString("synonyms", "value", new Serial()));
    }

    public void testAndOrSearch() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            final String details = "details" + System.currentTimeMillis();
            //create expType
            final ExperimentType et = create(ExperimentType.class);
            //create 3 exp
            final Experiment exp1 = create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
            final Experiment exp2 = create(Experiment.class);
            final Experiment exp3 = create(Experiment.class);
            exp1.setDetails(details);
            exp2.setDetails(details);

            //do search
            final Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(Experiment.PROP_EXPERIMENTTYPE, et);
            criteria.put(LabBookEntry.PROP_DETAILS, details);

            //AND search
            Collection<Experiment> exps = wv.findAll(Experiment.class, Conditions.andMap(criteria));
            //verify
            assertTrue(exps.contains(exp1));
            assertFalse(exps.contains(exp2));
            assertFalse(exps.contains(exp3));

            //OR search
            exps = wv.findAll(Experiment.class, Conditions.orMap(criteria));
            //verify
            assertTrue(exps.contains(exp1));
            assertTrue(exps.contains(exp2));
            assertFalse(exps.contains(exp3));
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

    //TODO the search on exp which have 2 parameters: one has value1, another one has value2 
    public void _testExpParamAndSearch() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            final String UNIQUE = "" + System.currentTimeMillis();
            final String value1 = "value1" + UNIQUE;
            final String value2 = "value2" + UNIQUE;
            final String value3 = "value3" + UNIQUE;
            //create 3 exp
            //exp1 have 3 parameters
            final Experiment exp1 = create(Experiment.class);
            final Parameter p1_1 = create(Parameter.class, Parameter.PROP_EXPERIMENT, exp1);
            p1_1.setValue(value1);
            final Parameter p1_2 = create(Parameter.class, Parameter.PROP_EXPERIMENT, exp1);
            p1_2.setValue(value2);
            final Parameter p1_3 = create(Parameter.class, Parameter.PROP_EXPERIMENT, exp1);
            p1_3.setValue(value3);

            //exp1 have 2 parameters
            final Experiment exp2 = create(Experiment.class);
            final Parameter p2_1 = create(Parameter.class, Parameter.PROP_EXPERIMENT, exp2);
            p2_1.setValue(value1);
            final Parameter p2_2 = create(Parameter.class, Parameter.PROP_EXPERIMENT, exp2);
            p2_2.setValue(value2);

            //exp1 have 2 parameters
            final Experiment exp3 = create(Experiment.class);
            final Parameter p3_1 = create(Parameter.class, Parameter.PROP_EXPERIMENT, exp3);
            p3_1.setValue(value1);
            final Parameter p3_2 = create(Parameter.class, Parameter.PROP_EXPERIMENT, exp3);
            p3_2.setValue(value3);

            //do search
            final Map<String, Object> expCritials = new HashMap<String, Object>();
            expCritials.put(Experiment.PROP_PARAMETERS, Conditions.newMap(Parameter.PROP_VALUE, value2));
            expCritials.putAll(Conditions.andMap(Conditions.newMap(Experiment.PROP_PARAMETERS,
                Conditions.newMap(Parameter.PROP_VALUE, value3))));
            //AND search
            final Collection<Parameter> params =
                wv.findAll(Parameter.class, Parameter.PROP_EXPERIMENT, expCritials);
            //verify
            assertTrue(params.contains(p1_1));
            assertEquals(1, params.size());

        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

    public void testBetweenSearch() {
        wv = getWV();
        try {
            //create expType
            final ExperimentType et = create(ExperimentType.class);

            //create first exp
            final java.util.Calendar date = java.util.Calendar.getInstance();
            date.setTimeInMillis(System.currentTimeMillis());
            final Experiment exp1 = create(Experiment.class);
            exp1.setExperimentType(et);
            exp1.setStartDate(date);

            //create second exp which start date is in the future1
            final java.util.Calendar futureDate1 = java.util.Calendar.getInstance();
            futureDate1.setTimeInMillis(System.currentTimeMillis() + 50000);

            final Experiment exp2 = create(Experiment.class);
            exp2.setStartDate(futureDate1);
            exp2.setExperimentType(et);

            //create third  exp which start date is in the future1
            final java.util.Calendar futureDate2 = java.util.Calendar.getInstance();
            futureDate2.setTimeInMillis(System.currentTimeMillis() + 100001);
            final Experiment exp3 = create(Experiment.class);
            exp3.setStartDate(futureDate2);
            exp3.setExperimentType(et);

            //search for exp which startDate is between now+1 and futureDate1
            final java.util.Calendar dateToCompare = java.util.Calendar.getInstance();
            dateToCompare.setTimeInMillis(System.currentTimeMillis() + 1);
            final Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(Experiment.PROP_EXPERIMENTTYPE, et);
            criteria.put(Experiment.PROP_PROJECT, Conditions.isNull());
            criteria.put(Experiment.PROP_STARTDATE, Conditions.between(dateToCompare, futureDate1));
            final Collection<Experiment> exps = wv.findAll(Experiment.class, criteria);

            //verify
            assertTrue(exps.contains(exp2));
            assertFalse(exps.contains(exp1));
            assertFalse(exps.contains(exp3));
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testGreaterSearch() {
        wv = getWV();
        try {

            //create first exp
            final java.util.Calendar date = java.util.Calendar.getInstance();
            date.setTimeInMillis(System.currentTimeMillis());
            final Experiment exp1 = create(Experiment.class);
            exp1.setStartDate(date);

            //create second exp which start date is in the futur
            final java.util.Calendar futureDate = java.util.Calendar.getInstance();
            futureDate.setTimeInMillis(System.currentTimeMillis() + 1000000);
            final Experiment exp2 = create(Experiment.class);
            exp2.setStartDate(futureDate);

            //search for exp which startDate is in the future
            final Collection<Experiment> exps =
                wv.findAll(Experiment.class, Experiment.PROP_STARTDATE, Conditions.greaterThan(date));

            //verify only exp in the future found
            assertTrue(exps.contains(exp2));
            assertFalse(exps.contains(exp1));

        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testLessSearch() {
        wv = getWV();
        try {
            //create expType
            final ExperimentType et = create(ExperimentType.class);

            //create first exp
            final java.util.Calendar date = java.util.Calendar.getInstance();
            long now = System.currentTimeMillis();
            date.setTimeInMillis(now);
            final Experiment exp1 = create(Experiment.class);
            exp1.setExperimentType(et);
            exp1.setStartDate(date);

            //create second exp which start date is in the future
            final java.util.Calendar futureDate = java.util.Calendar.getInstance();
            futureDate.setTimeInMillis(now + 1000000);
            final Experiment exp2 = create(Experiment.class);
            exp2.setStartDate(futureDate);
            exp2.setExperimentType(et);

            //search for exp which startDate is before now (less then)
            final java.util.Calendar dateToCompare = java.util.Calendar.getInstance();
            dateToCompare.setTimeInMillis(now + 1000);
            final Map<String, Object> critials = new HashMap<String, Object>();
            critials.put(Experiment.PROP_EXPERIMENTTYPE, et);
            critials.put(Experiment.PROP_PROJECT, Conditions.isNull());
            critials.put(Experiment.PROP_STARTDATE, Conditions.lessThan(dateToCompare));
            final Collection<Experiment> exps = wv.findAll(Experiment.class, critials);

            //verify
            assertTrue(exps.contains(exp1));
            assertFalse(exps.contains(exp2));

        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testIsNullSearch() {
        wv = getWV();
        try {
            //create expType
            final User p = create(User.class);

            //create first exp
            final Experiment exp1 = create(Experiment.class);
            exp1.setOperator(p);

            //create second exp which  Operator is null
            final Experiment exp2 = create(Experiment.class);
            exp2.setExperimentType(exp1.getExperimentType());

            Map<String, Object> criteria = new HashMap();
            criteria.put(Experiment.PROP_OPERATOR, Conditions.isNull());
            criteria.put(Experiment.PROP_EXPERIMENTTYPE, exp1.getExperimentType());
            //search for exp which Operator is null

            final Collection<Experiment> exps = wv.findAll(Experiment.class, criteria);

            //verify
            assertTrue(exps.contains(exp2));
            assertFalse(exps.contains(exp1));

        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testIsEmptySearch() {
        wv = getWV();
        try {

            wv.findAll(Sample.class,
                Collections.singletonMap(Sample.PROP_INPUTSAMPLES, (Object) Conditions.isEmpty()),
                new Paging(0, 1));

        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void xtestHQLCondition() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            TargetStatus ts1 = create(TargetStatus.class);
            TargetStatus ts2 = create(TargetStatus.class);

            Target t1 = create(Target.class);
            t1.addMilestone((Milestone) create(Milestone.class, Milestone.PROP_STATUS, ts2));
            t1.addMilestone((Milestone) create(Milestone.class, Milestone.PROP_STATUS, ts1));
            Target t2 = create(Target.class);
            t2.addMilestone((Milestone) create(Milestone.class, Milestone.PROP_STATUS, ts1));
            t2.addMilestone((Milestone) create(Milestone.class, Milestone.PROP_STATUS, ts2));
            Target t3 = create(Target.class);
            t3.addMilestone((Milestone) create(Milestone.class, Milestone.PROP_STATUS, ts1));

            final Map<String, Object> mCriteria = Conditions.newMap(Milestone.PROP_STATUS, ts1);
            mCriteria.put(Milestone.PROP_DATE, new HQLCondition(
                " milestones.date=(select max(milestone2.date) from " + Milestone.class.getName()
                    + " milestone2 where milestone2.target=A) "));
            final Map<String, Object> criteria = Conditions.newMap(Target.PROP_MILESTONES, mCriteria);

            //do search
            final Collection<Target> targets = this.wv.findAll(Target.class, criteria, new Paging(0, 10));

            Assert.assertTrue(targets.contains(t1));
            Assert.assertFalse(targets.contains(t2));
            Assert.assertTrue(targets.contains(t3));
            assertEquals(2, targets.size());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }
}
