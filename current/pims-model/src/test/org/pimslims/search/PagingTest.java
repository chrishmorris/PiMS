/**
 * DM org.pimslims.metamodel PagingTest.java
 * 
 * @author bl67
 * @date 22 Feb 2008
 * 
 *       Protein Information Management System
 * @version: 2.1
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.search.Paging.Order;
import org.pimslims.search.conditions.HQLCondition;
import org.pimslims.test.AbstractTestCase;

/**
 * PagingTest
 * 
 */
public class PagingTest extends AbstractTestCase {
    public void testBasic() {
        //constructor 1
        Paging paging = new Paging(1, 2);
        assertEquals(2, paging.getLimit());
        assertEquals(1, paging.getStart());
        assertEquals(1, paging.getOrderBy().size());
        assertEquals("dbId", paging.getOrderBy().keySet().iterator().next());
        assertEquals(Paging.Order.DESC, paging.getOrderBy().values().iterator().next());
        //constructor 2
        LinkedHashMap<String, Order> orderBy = new LinkedHashMap<String, Order>();
        orderBy.put("name", Paging.Order.ASC);
        paging = new Paging(10, 20, orderBy);
        assertEquals(20, paging.getLimit());
        assertEquals(10, paging.getStart());
        assertEquals(1, paging.getOrderBy().size());
        assertEquals("name", paging.getOrderBy().keySet().iterator().next());
        assertEquals(Paging.Order.ASC, paging.getOrderBy().values().iterator().next());
        //constructor 3
        paging = new Paging(10, 20, "details", Paging.Order.DESC);
        paging.setLimit(-1);
        assertEquals(-1, paging.getLimit());
        paging.setStart(-2);
        assertEquals(-2, paging.getStart());
        assertEquals(1, paging.getOrderBy().size());

        paging.addOrderBy("type", Paging.Order.ASC);
        assertEquals(2, paging.getOrderBy().size());
        assertEquals("details", paging.getOrderBy().keySet().iterator().next());
        assertEquals(Paging.Order.DESC, paging.getOrderBy().values().iterator().next());

        paging.setOrderBy(null);
        paging.addOrderBy("type", Paging.Order.ASC);
        assertEquals(1, paging.getOrderBy().size());
        assertEquals("type", paging.getOrderBy().keySet().iterator().next());
        assertEquals(Paging.Order.ASC, paging.getOrderBy().values().iterator().next());

        //illigal add
        paging = new Paging(10, 20);

        paging.addOrderBy("type", Paging.Order.ASC);
        assertTrue(paging.getOrderBy().keySet().contains("type"));

    }

    public void testLimitedSearch() throws AccessException, ConstraintException, AbortedException {
        String hook1 = null;
        String hook2 = null;
        wv = getWV();
        try {
            create(Sample.class);
            hook1 = ((Sample) create(Sample.class)).get_Hook();
            hook2 = ((Sample) create(Sample.class)).get_Hook();
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }

        rv = getRV();
        try {
            Collection<Sample> sampleResults = rv.getAll(Sample.class, new Paging(0, 1));
            assertNotNull(sampleResults);
            assertEquals(1, sampleResults.size());
            assertEquals(hook2, sampleResults.iterator().next().get_Hook());

            sampleResults = rv.getAll(Sample.class, new Paging(1, 1));
            assertNotNull(sampleResults);
            assertEquals(1, sampleResults.size());
            assertEquals(hook1, sampleResults.iterator().next().get_Hook());
        } finally {
            if (!rv.isCompleted())
                rv.abort();
        }
    }

    public void testSortedGetAll() throws AccessException, ConstraintException {
        String hook2 = null;

        wv = getWV();
        try {
            //create 2 exps with different start date
            //exp1 earlier than exp2
            Experiment exp = create(Experiment.class);
            java.util.Calendar dateBefore = java.util.Calendar.getInstance();
            dateBefore.setTimeInMillis(System.currentTimeMillis() - 1);
            exp.setStartDate(dateBefore);

            java.util.Calendar dateAfter = java.util.Calendar.getInstance();
            dateAfter.setTimeInMillis(System.currentTimeMillis());
            exp = create(Experiment.class);
            exp.setStartDate(dateAfter);
            hook2 = exp.get_Hook();

            //search by wrong attribute which is a role
            try {
                wv.getAll(Experiment.class, new Paging(0, 1, Experiment.PROP_PROJECT, Paging.Order.DESC));
                fail("should not success if orderby role!");
            } catch (Exception e) {
                //it is fine
            }
            //search by DESC date
            Collection<Experiment> expResults =
                wv.getAll(Experiment.class, new Paging(0, 1, Experiment.PROP_STARTDATE, Paging.Order.DESC));
            assertNotNull(expResults);
            //should find later exp
            assertEquals(hook2, expResults.iterator().next().get_Hook());

            //search by asc date
            expResults =
                wv.getAll(Experiment.class, new Paging(0, 1, Experiment.PROP_STARTDATE, Paging.Order.ASC));
            assertNotNull(expResults);
            //should find earlier exp
            assertNotSame(hook2, expResults.iterator().next().get_Hook());
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }

    public void testSortedFindAll() throws AccessException, ConstraintException {
        String hook1 = null;
        String hook2 = null;
        wv = getWV();
        try {
            //create 2 exps with different start date
            ExperimentType et = create(ExperimentType.class);
            //exp1 earlier than exp2
            java.util.Calendar dateBefore = java.util.Calendar.getInstance();
            dateBefore.setTimeInMillis(System.currentTimeMillis() - 1);
            Experiment exp = create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
            exp.setStartDate(dateBefore);
            hook1 = exp.get_Hook();

            java.util.Calendar date = java.util.Calendar.getInstance();
            date.setTimeInMillis(System.currentTimeMillis());
            exp = create(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, et);
            exp.setStartDate(date);
            hook2 = exp.get_Hook();

            java.util.Calendar dateAfter = java.util.Calendar.getInstance();
            dateAfter.setTimeInMillis(System.currentTimeMillis() + 1);
            exp = create(Experiment.class);
            exp.setStartDate(dateAfter);

            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(Experiment.PROP_EXPERIMENTTYPE, et);
            //search by DESC date
            Collection<Experiment> expResults =
                wv.findAll(Experiment.class, criteria, new Paging(0, 10, Experiment.PROP_STARTDATE,
                    Paging.Order.DESC));
            assertEquals(2, expResults.size());
            //should find later exp first
            assertEquals(hook2, expResults.iterator().next().get_Hook());

            //search by asc date
            expResults =
                wv.findAll(Experiment.class, criteria, new Paging(0, 10, Experiment.PROP_STARTDATE,
                    Paging.Order.ASC));
            assertEquals(2, expResults.size());
            //should find earlier exp first
            assertEquals(hook1, expResults.iterator().next().get_Hook());
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }

    public void testTargetSearch() {
        this.wv = getWV();
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
            // orderBys.put("milestones.date", Order.DESC); // <========= this line leads to an error after fix to PRIV-216
            orderBys.put(LabBookEntry.PROP_DBID, Order.DESC);
            final Paging paging = new Paging(0, 12, orderBys);

            final PIMSCriteria query = new PIMSCriteriaImpl(wv, Target.class, paging, null);
            query.setAttributeMap(criteria);
            query.setPaging(paging);
            query.list();
        } finally {
            this.wv.abort();
        }
    }

    public void testROSearch() {
        this.wv = getWV();
        try {
            final Paging paging = new Paging(0, 1);
            new PIMSCriteriaImpl(wv, ResearchObjective.class, paging, wv.getReadableLabNotebooks());
        } finally {
            this.wv.abort();
        }
    }

}
