/**
 * ccp.api.pojo ExperimentTester.java
 * 
 * @date 02-Nov-2006 08:58:07
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 *         Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 *           Copyright (c) 2006
 * 
 *           This library is free software; you can redistribute it and/or modify it under the terms of the
 *           GNU Lesser General Public License as published by the Free Software Foundation; either version
 *           2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.model.api;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.model.crystallization.DropAnnotation;
import org.pimslims.model.crystallization.Score;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.holder.RefHolder;
import org.pimslims.model.holder.RefHolderOffset;
import org.pimslims.model.reference.CrystalType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.schedule.ScheduledTask;
import org.pimslims.persistence.HibernateUtilTester;
import org.pimslims.test.AbstractTestCase;

/**
 * ExperimentTester
 * 
 */
public class HolderTester extends org.pimslims.test.AbstractTestCase {
    public void testOffset() throws AccessException, ConstraintException, AbortedException {
        String holderHook = null;
        String refHolderHook = null;
        wv = getWV();
        try {

            final Holder holder0 = create(Holder.class);
            final Holder holder1 = create(Holder.class);
            final Holder holder2 = create(Holder.class);
            final CrystalType ct = create(CrystalType.class);
            ct.setResSubPosition(10);
            holder0.setHolderType(ct);
            holder1.setParentHolder(holder0);
            holder2.setParentHolder(holder0);

            final RefHolder refHolder = create(RefHolder.class);
            new RefHolderOffset(wv, refHolder, holder0);

            holderHook = holder0.get_Hook();
            refHolderHook = refHolder.get_Hook();
            verify(holder0, refHolder);
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        wv = getWV();
        try {

            final Holder holder0 = wv.get(holderHook);
            final RefHolder refHolder = wv.get(refHolderHook);

            verify(holder0, refHolder);
            holder0.delete();
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    /**
     * @param holder0
     * @param refHolder
     */
    private void verify(final Holder holder0, final RefHolder refHolder) {
        //verify holder and sub-holder
        assertEquals(2, holder0.getSubHolders().size());
        final Holder subHolder = (Holder) holder0.getSubHolders().iterator().next();
        assertEquals(holder0, subHolder.getParentHolder());
        //vertfy CrystalType
        assertNotNull(holder0.getHolderType());
        assertEquals(10, ((CrystalType) holder0.getHolderType()).getResSubPosition().intValue());
        //verfy holder and refHolder
        assertEquals(1, holder0.getRefHolderOffsets().size());
        assertEquals(refHolder, holder0.getRefHolderOffsets().iterator().next().getRefHolder());
    }

    public void testRefHolderToHolder() throws AccessException, ConstraintException, AbortedException,
        HibernateException {

        String refHolderHook = null;
        wv = getWV();
        try {

            final Holder holder0 = create(Holder.class);
            final Holder holder1 = create(Holder.class);
            final Holder holder2 = create(Holder.class);
            final Holder holder3 = create(Holder.class);

            final RefHolder refHolder = create(RefHolder.class);

            new RefHolderOffset(wv, refHolder, holder0);
            new RefHolderOffset(wv, refHolder, holder1);
            new RefHolderOffset(wv, refHolder, holder2);
            new RefHolderOffset(wv, refHolder, holder3);
            refHolderHook = refHolder.get_Hook();
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        HibernateUtilTester.clear2ndLevelCache(model);
        wv = getWV();
        try {
            final RefHolder refHolder = wv.get(refHolderHook);
            final long start = HibernateUtilTester.getStatistics(model).getCloseStatementCount();
            //search for RefHolderOffset which refHolder
            final Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(RefHolderOffset.PROP_REFHOLDER, refHolder);
            //find with joined holder
            final Collection<RefHolderOffset> offsets =
                wv.findAll(RefHolderOffset.class, criteria,
                    Collections.singletonList(RefHolderOffset.PROP_HOLDER));
            //get holder from RefHolderOffset
            final Set<Holder> holders = new HashSet<Holder>();
            for (final RefHolderOffset rho : offsets) {
                holders.add(rho.getHolder());
            }

            final long end = HibernateUtilTester.getStatistics(model).getCloseStatementCount();
            //System.out.println("CloseStatementCount: " + (end - start));
            assertEquals(1, end - start);

            // clean up
            for (final RefHolderOffset rho : offsets) {
                rho.delete();
            }

            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

    public void testUpdateDerivedData_crystalNumber() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            final Holder holder = create(Holder.class);
            final Score score_noCrystal = create(Score.class);
            final Score score_Crystal = create(Score.class, Score.PROP_NAME, "Crystals");
            //sample1 has 1 crystal annotation and 1 no-crystal
            final Sample sample1 = create(Sample.class, Sample.PROP_HOLDER, holder);
            final DropAnnotation annotation1_1 =
                create(DropAnnotation.class, DropAnnotation.PROP_SCORE, score_Crystal);
            annotation1_1.setSample(sample1);
            final DropAnnotation annotation1_2 =
                create(DropAnnotation.class, DropAnnotation.PROP_SCORE, score_noCrystal);
            annotation1_2.setSample(sample1);
            //sample2 has 1 no-crystal
            final Sample sample2 = create(Sample.class, Sample.PROP_HOLDER, holder);
            final DropAnnotation annotation2_1 =
                create(DropAnnotation.class, DropAnnotation.PROP_SCORE, score_noCrystal);
            annotation2_1.setSample(sample2);
            //sample3 has nothing
            create(Sample.class, Sample.PROP_HOLDER, holder);

            holder.updateDerivedData();
            assertEquals(1, holder.getCrystalNumber().intValue());
            assertNull(holder.getLastTask());

        } finally {
            wv.abort();
        }
    }

    public void testUpdateDerivedData_lastTask() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            final Holder holder = create(Holder.class);
            final ScheduledTask task1 =
                create(ScheduledTask.class, ScheduledTask.PROP_COMPLETIONTIME, Calendar.getInstance());
            task1.setHolder(holder);
            create(ScheduledTask.class, ScheduledTask.PROP_HOLDER, holder);
            final Calendar later = Calendar.getInstance();
            later.setTimeInMillis(System.currentTimeMillis() + 1);
            final ScheduledTask task3 = create(ScheduledTask.class, ScheduledTask.PROP_COMPLETIONTIME, later);
            task3.setHolder(holder);

            holder.updateDerivedData();
            assertEquals(0, holder.getCrystalNumber().intValue());
            assertEquals(task3, holder.getLastTask());

        } finally {
            wv.abort();
        }
    }

    /**
     * HolderTester.testSetLastTask - this tests to see if setLastTask is correctly NOT unsetting the value of
     * the previous lastTask's holder property.
     * 
     * @throws AccessException
     * @throws ConstraintException
     */
    public void testSetLastTask() throws AccessException, ConstraintException {
        MetaClass holderClass = AbstractTestCase.model.getMetaClass(Holder.class.getName());
        MetaRole lastTask = holderClass.getMetaRole(Holder.PROP_LASTTASK);
        assertTrue(lastTask.isOneWay());

        wv = getWV();
        try {
            final Holder holder = create(Holder.class);

            Calendar now = Calendar.getInstance();
            Calendar calSt1 = (Calendar) now.clone();
            now.add(Calendar.MINUTE, 1);
            Calendar calSt2 = (Calendar) now.clone();
            now.add(Calendar.MINUTE, 1);
            Calendar calSt3 = (Calendar) now.clone();

            final ScheduledTask task1 =
                create(ScheduledTask.class, ScheduledTask.PROP_COMPLETIONTIME, calSt1);
            task1.setHolder(holder);
            final ScheduledTask task2 =
                create(ScheduledTask.class, ScheduledTask.PROP_COMPLETIONTIME, calSt2);
            task2.setHolder(holder);
            final ScheduledTask task3 =
                create(ScheduledTask.class, ScheduledTask.PROP_COMPLETIONTIME, calSt3);
            task3.setHolder(holder);

            // Push to db
            wv.flush();

            /* 
             * OK, we've got what we need in the db. Now test setting and updating
             * Holder.lastTask with Holder#setLastTask(ScheduledTask)
             */

            holder.setLastTask(task1);
            wv.flush();
            assertEquals(task1, holder.getLastTask());
            assertEquals(holder, task1.getHolder());

            holder.setLastTask(task2);
            assertEquals(holder, task2.getHolder());
            assertEquals(holder, task1.getHolder());
            /* FIXME fails here with apparently null ScheduledTask.holder, which
             * I think is on task1 due to pims incorrectly unsetting task1.holder
             * because it thinks its linked to Holder.lastTask.
             * TODO I really really hope this isn't happening on Holder#setFirstSample(Sample)
             * (aka Holder#setLastTask(Sample))!
             */
            wv.flush();
            assertEquals(task2, holder.getLastTask());

            holder.setLastTask(task3);
            wv.flush();
            assertEquals(task3, holder.getLastTask());

        } finally {
            wv.abort();
        }
    }

}
