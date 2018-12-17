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

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.AbstractHolderType;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.model.target.TargetGroup;
import org.pimslims.persistence.HibernateUtilTester;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * RestrictionTest
 * 
 */
public class SubCriteriaTest extends AbstractTestCase {

    private static final Calendar NOW = Calendar.getInstance();

    public void testMRoleSearch() {
        wv = getWV();
        try {

            //create HolderCategory
            HolderCategory hc = create(HolderCategory.class);
            //create a holder on this HolderCategory
            Holder holder = create(Holder.class, AbstractHolder.PROP_HOLDERCATEGORIES, hc);
            assertEquals(1, holder.getHolderCategories().size());
            assertTrue(holder.getHolderCategories().contains(hc));

            wv.flush();
            long start = HibernateUtilTester.getStatistics(model).getCloseStatementCount();

            //holder's criteria
            Map<String, Object> holderCriteria = new HashMap<String, Object>();
            //using hcCriteria instead of put a specific HolderCategory
            holderCriteria.put(AbstractHolder.PROP_HOLDERCATEGORIES, hc);

            Collection<Holder> holders = wv.findAll(Holder.class, holderCriteria);
            assertEquals(1, holders.size());
            assertTrue(holders.contains(holder));

            //make sure join is used
            long end = HibernateUtilTester.getStatistics(model).getCloseStatementCount();
            assertEquals(1, end - start);

        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testSearchWith2Names() {
        wv = getWV();
        try {

            //create HolderCategory
            HolderCategory hc = create(HolderCategory.class);
            //create a holder on this HolderCategory
            Holder holder = create(Holder.class, AbstractHolder.PROP_HOLDERCATEGORIES, hc);
            assertEquals(1, holder.getHolderCategories().size());
            assertTrue(holder.getHolderCategories().contains(hc));

            //HolderCategory criteria
            Map<String, Object> hcCriteria = new HashMap<String, Object>();
            hcCriteria.put(HolderCategory.PROP_NAME, hc.getName());
            //holder's criteria
            Map<String, Object> holderCriteria = new HashMap<String, Object>();
            holderCriteria.put(AbstractHolder.PROP_NAME, holder.getName());
            holderCriteria.put(AbstractHolder.PROP_HOLDERCATEGORIES, hcCriteria);
            Collection<Holder> holders = wv.findAll(Holder.class, holderCriteria);
            assertEquals(1, holders.size());
            assertTrue(holders.contains(holder));

        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testSimpleSubCriteriaSearch() {
        wv = getWV();
        try {

            //create HolderCategory
            HolderCategory hc = create(HolderCategory.class);
            String hcName = hc.getName();

            //create a unrelated holder
            create(Holder.class);
            //create a holder on this HolderCategory
            Holder holder = create(Holder.class, AbstractHolder.PROP_HOLDERCATEGORIES, hc);
            assertEquals(1, holder.getHolderCategories().size());
            assertTrue(holder.getHolderCategories().contains(hc));

            wv.flush();
            long start = HibernateUtilTester.getStatistics(model).getCloseStatementCount();

            //HolderCategory sub-criteria
            Map<String, Object> hcCriteria = new HashMap<String, Object>();
            hcCriteria.put(HolderCategory.PROP_NAME, hcName);

            //holder's criteria
            Map<String, Object> holderCriteria = new HashMap<String, Object>();
            //using hcCriteria instead of put a specific HolderCategory
            holderCriteria.put(AbstractHolder.PROP_HOLDERCATEGORIES, hcCriteria);

            //this search will generate hql join search
            Collection<Holder> holders = wv.findAll(Holder.class, holderCriteria);
            assertEquals(1, holders.size());
            assertTrue(holders.contains(holder));

            //make sure join is used
            long end = HibernateUtilTester.getStatistics(model).getCloseStatementCount();
            assertEquals(1, end - start);

        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testWithMOSubCriteriaSearch() {
        wv = getWV();
        try {

            //create holderType
            HolderCategory hc = create(HolderCategory.class);
            //String hcName = hc.getName();
            HolderType ht = create(HolderType.class);
            ht.addDefaultHolderCategory(hc);

            //create a unrelated holder
            create(Holder.class);
            //create a holder on this holderType
            Holder holder = create(Holder.class, AbstractHolder.PROP_HOLDERTYPE, ht);
            assertNotNull(holder.getHolderType());

            wv.flush();
            long start = HibernateUtilTester.getStatistics(model).getCloseStatementCount();

            //holder type sub-criteria
            Map<String, Object> htCriteria = new HashMap<String, Object>();
            htCriteria.put(AbstractHolderType.PROP_DEFAULTHOLDERCATEGORIES, hc);

            //holder's criteria
            Map<String, Object> holderCriteria = new HashMap<String, Object>();
            //using hcCriteria instead of put a specific HolderCategory
            holderCriteria.put(AbstractHolder.PROP_HOLDERTYPE, htCriteria);
            //this search will generate hql join search
            Collection<Holder> holders = wv.findAll(Holder.class, holderCriteria);
            assertEquals(1, holders.size());
            assertTrue(holders.contains(holder));

            //make sure join is used
            long end = HibernateUtilTester.getStatistics(model).getCloseStatementCount();
            assertEquals(1, end - start);

        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testROESearch() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            //create data
            String componentType1 = "componentType1";
            String componentType2 = "componentType2";
            String componentType3 = "componentType3";
            ResearchObjectiveElement roe1 =
                create(ResearchObjectiveElement.class, ResearchObjectiveElement.PROP_COMPONENTTYPE,
                    componentType1);
            ResearchObjectiveElement roe2 =
                create(ResearchObjectiveElement.class, ResearchObjectiveElement.PROP_COMPONENTTYPE,
                    componentType2);
            create(ResearchObjectiveElement.class, ResearchObjectiveElement.PROP_COMPONENTTYPE,
                componentType3);

            //do search
            Map<String, Object> criteria =
                Conditions.newMap(ResearchObjectiveElement.PROP_COMPONENTTYPE,
                    Conditions.or(Conditions.eq(componentType1), Conditions.eq(componentType2)));
            Collection<ResearchObjectiveElement> roes = wv.findAll(ResearchObjectiveElement.class, criteria);
            assertEquals(2, roes.size());
            assertTrue(roes.contains(roe1));
            assertTrue(roes.contains(roe2));

            //do search with paging
            roes = wv.findAll(ResearchObjectiveElement.class, criteria, new Paging(0, 1));
            assertEquals(1, roes.size());
            assertTrue(roes.contains(roe2));
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testROESearch2() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            //create data
            //target group and target
            TargetGroup tg = create(TargetGroup.class);
            Target target1 = create(Target.class, Target.PROP_TARGETGROUPS, tg);
            assertTrue(target1.getTargetGroups().contains(tg));
            Target target2 = create(Target.class, Target.PROP_TARGETGROUPS, tg);
            Target target3 = create(Target.class);
            //Experiment and exp type
            ExperimentType Order = create(ExperimentType.class);
            wv.flush();
            Experiment exp1 = new Experiment(wv, UNIQUE, NOW, NOW, Order);
            Experiment exp2 =
                POJOFactory.createModelObject(wv, Experiment.class, false, Collections.EMPTY_MAP);
            Experiment exp3 = create(Experiment.class);
            Experiment exp4 = create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, Order);
            //ResearchObjective
            ResearchObjective ro1 = create(ResearchObjective.class, ResearchObjective.PROP_EXPERIMENTS, exp1);
            ResearchObjective ro2 = create(ResearchObjective.class, ResearchObjective.PROP_EXPERIMENTS, exp2);
            ResearchObjective ro3 = create(ResearchObjective.class, ResearchObjective.PROP_EXPERIMENTS, exp3);
            ResearchObjective ro4 = create(ResearchObjective.class, ResearchObjective.PROP_EXPERIMENTS, exp4);
            ro4.addExperiment((Experiment) create(Experiment.class));
            //ResearchObjectiveElement
            ResearchObjectiveElement roe1 =
                create(ResearchObjectiveElement.class, ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE, ro1);
            roe1.setTarget(target1);
            ResearchObjectiveElement roe2 =
                create(ResearchObjectiveElement.class, ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE, ro2);
            roe2.setTarget(target2);
            ResearchObjectiveElement roe3 =
                create(ResearchObjectiveElement.class, ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE, ro3);
            roe3.setTarget(target3);
            ResearchObjectiveElement roe4 = create(ResearchObjectiveElement.class);
            roe4.setTarget(target1);
            ResearchObjectiveElement roe5 =
                create(ResearchObjectiveElement.class, ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE, ro4);
            roe5.setTarget(target1);
            //search for for ResearchObjectiveElements which have not been ordered (no experiment of experimentType Order), and which belong to targets within a targetGroup
            //prepare search

            //do search
            Map<String, Object> expCriteria =
                Conditions.newMap(Experiment.PROP_EXPERIMENTTYPE, Conditions.eq(Order));
            Map<String, Object> roCriteria =
                Conditions.newMap(ResearchObjective.PROP_EXPERIMENTS, expCriteria);
            Map<String, Object> targetCriteria = Conditions.newMap(Target.PROP_TARGETGROUPS, tg);

            Map<String, Object> roeCriteria = new HashMap<String, Object>();
            roeCriteria.put(ResearchObjectiveElement.PROP_TARGET, targetCriteria);
            roeCriteria.put(ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE,
                Conditions.notExistsMap(roCriteria));

            Collection<ResearchObjectiveElement> roes =
                wv.findAll(ResearchObjectiveElement.class, roeCriteria);
            //check result
            assertFalse(roes.contains(roe1)); //roe1 links with order expType
            assertTrue(roes.contains(roe2));
            assertFalse(roes.contains(roe3));//roe3 does not link with TargetGroup tg
            assertTrue(roes.contains(roe4));
            assertFalse(roes.contains(roe5)); //roe5 links with order expType 
            //do paged search
            roes = wv.findAll(ResearchObjectiveElement.class, roeCriteria, new Paging(0, 1));
            assertEquals(1, roes.size());
            assertTrue(roes.contains(roe4));
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }
}
