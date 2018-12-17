/**
 * org.pimslims.persistence HibernateUtilTester.java
 * 
 * @date 23-Oct-2006 09:58:03
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
package org.pimslims.persistence;

import java.util.Arrays;
import java.util.Collection;

import junit.framework.TestCase;

import org.hibernate.HibernateException;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.model.reference.HazardPhrase;
import org.pimslims.model.sample.RefSample;
import org.pimslims.test.POJOFactory;

/**
 * HibernateUtilTester gives an OutOfMemoryError in its clinit
 * 
 */
public class HibernateUtilTester extends TestCase {

    /**
     * The model being tested
     */
    protected final AbstractModel model = ModelImpl.getModel();

    // note that this must be defined after the model
    //private static HibernateUtil persistence = new HibernateUtil();

    String HPHook = null, RSHook = null;

    public void testClear2ndLevelCache() throws HibernateException {
        // was assertTrue(HibernateUtil.get2ndLevelCacheSizeInMemory() > 0);
        HibernateUtilTester.clear2ndLevelCache(model);
        assertEquals(0, HibernateUtilTester.get2ndLevelCacheSizeInMemory(model));

    }

    protected void setUp() throws Exception {

        super.setUp();
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            // create new objects for tesing
            RefSample rs = POJOFactory.createRefSample(wv);
            HazardPhrase hp = POJOFactory.createHazardPhrase(wv);
            RSHook = rs.get_Hook();
            HPHook = hp.get_Hook();
            wv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            // delete testing object
            RefSample rs = (RefSample) wv.get(RSHook);
            HazardPhrase hp = (HazardPhrase) wv.get(HPHook);
            rs.delete();
            hp.delete();
            wv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        Statistics stat = HibernateUtilTester.getStatistics(model);
        if (stat.getSessionCloseCount() - stat.getSessionOpenCount() < 0)
            System.err.println((stat.getSessionOpenCount() - stat.getSessionCloseCount())
                + " session is not closed");
        // make sure every session is closed
        assert stat.getSessionCloseCount() == stat.getSessionOpenCount();
    }

    /**
     * Resets global interceptor to default state.
     * 
     * @param model
     * 
     * @throws ClassNotFoundException
     * @throws HibernateException
     */
    public static void clear2ndLevelCache(AbstractModel model) throws HibernateException {
        Statistics stats = ((ModelImpl) model).getSessionFactory().getStatistics();
        // clear enities
        Collection<String> SecondLevelCacheEntityNames = Arrays.asList(stats.getEntityNames());
        for (String SecondLevelCacheEntityName : SecondLevelCacheEntityNames) {
            ((ModelImpl) model).getSessionFactory().getCache().evictEntityRegion(SecondLevelCacheEntityName);
        }
        // seems to give an NPE:  sessionFactory.getCache().evictQueryRegions();

        // clear collections
        Collection<String> SecondLevelCacheCollectionRoleNames =
            Arrays.asList(stats.getCollectionRoleNames());
        for (String SecondLevelCacheCollectionRoleName : SecondLevelCacheCollectionRoleNames) {
            ((ModelImpl) model).getSessionFactory().getCache()
                .evictCollectionRegion(SecondLevelCacheCollectionRoleName);
        }
    }

    private static long get2ndLevelCacheSizeInMemory(AbstractModel model) {
        Statistics stats = ((ModelImpl) model).getSessionFactory().getStatistics();

        Collection<String> SecondLevelCacheRegionNames =
            Arrays.asList(stats.getSecondLevelCacheRegionNames());
        long totalSizeInMemory = 0;
        // System.out.println("\n------------------SecondLevelCache
        // Details------------");
        for (String SecondLevelCacheRegionName : SecondLevelCacheRegionNames) {
            org.hibernate.stat.SecondLevelCacheStatistics scStats =
                stats.getSecondLevelCacheStatistics(SecondLevelCacheRegionName);
            if (scStats.getSizeInMemory() > 0
                && !SecondLevelCacheRegionName.endsWith("UpdateTimestampsCache")) {
                totalSizeInMemory = +scStats.getSizeInMemory();
            }
        }
        System.gc();
        System.gc();
        System.gc();
        System.gc();
        return totalSizeInMemory;
    }

    public static Statistics getStatistics(AbstractModel model) {
        return ((ModelImpl) model).getSessionFactory().getStatistics();
    }

    /**
     * AbstractModel.clearStatistics
     * 
     * @see org.pimslims.dao.AbstractModel#clearStatistics()
     */
    public static void clearStatistics(AbstractModel model) {
        HibernateUtilTester.getStatistics(model).clear();
    }

    // print the general statistics of current sessionFactory
    public static void showStatistics(AbstractModel model) {
        Statistics stats = ((ModelImpl) model).getSessionFactory().getStatistics();
        // double queryCacheHitCount = stats.getQueryCacheHitCount();
        // double queryCacheMissCount = stats.getQueryCacheMissCount();
        // double queryCacheHitRatio =
        // queryCacheHitCount / (queryCacheHitCount + queryCacheMissCount);
        // log.info("Query Hit ratio:" + queryCacheHitRatio);
        stats.logSummary();

    }

    // print the general statistics of an entity in current sessionFactory
    public static void showStatistics(String entityName, AbstractModel model) {
        Statistics stats = ((ModelImpl) model).getSessionFactory().getStatistics();
        EntityStatistics entityStats = stats.getEntityStatistics(entityName);
        long changes =
            entityStats.getInsertCount() + entityStats.getUpdateCount() + entityStats.getDeleteCount();
        System.out.println(entityName + " changed " + changes + " times: " + entityStats.getInsertCount()
            + " insert, " + entityStats.getUpdateCount() + " update, " + entityStats.getDeleteCount()
            + " delete. ");
    }

}
