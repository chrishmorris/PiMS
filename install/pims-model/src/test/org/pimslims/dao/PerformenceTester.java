/**
 * ExtendedTestModel org.pimslims.hibernate PerformenceTest.java
 * 
 * @author bl67
 * @date 08-Jun-2006
 * 
 *       Protein Information Management System
 * @version: 0.5
 * 
 *           Copyright (c) 2006 bl67 This library is free software; you can redistribute it and/or modify it
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
package org.pimslims.dao;

// import memops.api.Test.NaturalSource;
// import org.pimslims.model.implementation.Project;
// import memops.api.Test.Target;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.stat.Statistics;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.people.Person;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;
import org.pimslims.persistence.HibernateUtilTester;
import org.pimslims.test.AbstractTestCase;

/**
 * PerformenceTest
 * 
 */
public class PerformenceTester extends AbstractTestCase {

    /**
     * The model in use
     */
    protected final AbstractModel model = ModelImpl.getModel();

    private static List<String> metaClassNameList = new LinkedList<String>();
    static {
        metaClassNameList.add(Target.class.getName());
        metaClassNameList.add(Organisation.class.getName());
    }

    /*
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        // HibernateUtil.showStatistics();
        Statistics stat = HibernateUtilTester.getStatistics(model);
        // make sure every session is closed
        assert stat.getSessionCloseCount() == stat.getSessionOpenCount();
        // make sure second level cache is used
        // assert stat.getSecondLevelCacheHitCount()>0;
        // reset the Statistics
    }

    public void testMtoMRoleInsertQuery() throws AbortedException, ConstraintException, AccessException {
        wv = getWV();
        String scHook = null;
        try {
            SampleCategory sc = create(SampleCategory.class);
            scHook = sc.get_Hook();
            create(Sample.class, AbstractSample.PROP_SAMPLECATEGORIES, sc);
            create(Sample.class, AbstractSample.PROP_SAMPLECATEGORIES, sc);
            create(Sample.class, AbstractSample.PROP_SAMPLECATEGORIES, sc);
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }

        wv = getWV();
        try {
            Statistics stat = HibernateUtilTester.getStatistics(model);
            SampleCategory sc = wv.get(scHook);
            Sample sample = create(Sample.class);
            wv.flush();
            long start = stat.getCloseStatementCount();
            //sample.setHbSampleCategories(Collections.singleton(sc));
            sample.addSampleCategory(sc);
            wv.flush();
            //HibernateUtil.showStatisticDetails();
            System.out.println((stat.getCloseStatementCount() - start)
                + " statements for add a sample to a SampleCategory");
            //1 for select existing abstractsamples from SampleCategory
            //another one for insert new sample into SampleCategory
            assertEquals(2, (stat.getCloseStatementCount() - start));
            wv.flush();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }

        wv = getWV();
        try {
            Statistics stat = HibernateUtilTester.getStatistics(model);
            SampleCategory sc = wv.get(scHook);
            Sample sample = create(Sample.class);
            wv.flush();
            long start = stat.getCloseStatementCount();
            sample.setSampleCategories(Collections.singleton(sc));
            wv.flush();
            //HibernateUtil.showStatisticDetails();
            System.out.println((stat.getCloseStatementCount() - start)
                + " statements for add a sample to a SampleCategory");
            //1 for select existing abstractsamples from SampleCategory
            //another one for insert new sample into SampleCategory
            assertEquals(2, (stat.getCloseStatementCount() - start));
            wv.flush();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }

    public void testQueryCache() {
        long firstDuration = 0;
        for (int i = 0; i < 3; i++) {
            ReadableVersion rv = model.getReadableVersion(AbstractModel.SUPERUSER);
            Date dateStart = new Date();

            try {
                for (String metaClassName : metaClassNameList)
                    rv.findAll(model.getMetaClass(metaClassName).getJavaClass(), Collections.EMPTY_MAP);
                rv.commit();
            } catch (final Exception ex) {
                throw new RuntimeException(ex);
                // fail(ex.toString());

            } finally {
                if (!rv.isCompleted()) {
                    rv.abort();
                }
            }
            Date dateEnd = new Date();
            // for(String metaClassName:metaClassNameList)
            // HibernateUtil.showStatistics( metaClassName);
            // System.out.println("Loop"+i+" use: "+
            // (dateEnd.getTime()-dateStart.getTime()));
            if (i == 0)
                firstDuration = dateEnd.getTime() - dateStart.getTime();
            else
                // when second level cache works, the second/third query should
                // be much more quiker than first one
                assertTrue((dateEnd.getTime() - dateStart.getTime()) < firstDuration || firstDuration < 1000);
        }
    }

    public void testEnityCache() {
        String hook;
        String name;
        Organisation orgnisation;
        wv = getWV();
        try {
            orgnisation = create(Organisation.class);
            hook = orgnisation.get_Hook();
            name = orgnisation.getName();
            wv.commit();
        } catch (AbortedException ex1) {
            throw new RuntimeException(ex1);
        } catch (ConstraintException ex1) {
            throw new RuntimeException(ex1);
        } catch (AccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        ReadableVersion rv = model.getReadableVersion(AbstractModel.SUPERUSER);
        try {
            Organisation orgnisation2 = (Organisation) rv.get(hook);
            assertTrue(orgnisation2.getName().equals(name));
            rv.commit();
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
            // fail(ex.toString());

        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
        // for(String metaClassName:metaClassNameList)
        // HibernateUtil.showStatistics( metaClassName);
    }

    public void testLeak() {

        Date start = new Date();
        for (int j = 0; j < 1; j++) {
            for (int i = 0; i < 1; i++) {
                WritableVersion wv = ModelImpl.getModel().getWritableVersion(AbstractModel.SUPERUSER);
                ModelObject testObject;
                try {
                    testObject = wv.create(Person.class, org.pimslims.model.api.PersonTester.ATTRIBUTES);
                    wv.delete(testObject);
                    wv.commit();
                } catch (ModelException ex) {
                    ex.printStackTrace();
                    fail(ex.getMessage());
                } finally {
                    if (!wv.isCompleted()) {
                        wv.abort();
                    }
                }

            }
            Date end = new Date();
            Runtime runtime = Runtime.getRuntime();
            System.out.println("done " + (j + 1) + "" + " time " + ((end.getTime() - start.getTime()) / 1000)
                + " memory " + (runtime.totalMemory() - runtime.freeMemory()));
            start = end;

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        HibernateUtilTester.getStatistics(model).clear();
    }
}
