/**
 * current-pims-web org.pimslims.performance Create.java
 * 
 * @author bl67
 * @date 28 Mar 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 bl67
 * 
 * 
 */
package org.pimslims.performance;

import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;
import org.pimslims.search.Searcher;
import org.pimslims.test.AbstractTestCase;

/**
 * Create
 * 
 */
public class GeneralTest extends AbstractTestCase {

    /**
     * @param name
     */
    public GeneralTest(final String name) {
        super(name);
    }

    private static int loopNum = 4;

    private static int subLoopNum = 100;

    public void xtestCreatePerformance() throws AccessException, ConstraintException, AbortedException {
        //HibernateUtil.getStatistics().clear();
        final long start = System.currentTimeMillis();
        for (int i = 0; i < GeneralTest.loopNum; i++) {
            this.wv = this.getWV();
            try {
                for (int j = 0; j < GeneralTest.subLoopNum; j++) {
                    this.create(Sample.class);
                    this.create(Target.class);
                    this.create(Experiment.class);
                }
                this.wv.commit();

            } finally {
                if (!this.wv.isCompleted()) {
                    this.wv.abort();
                }
            }
        }
        final long time = System.currentTimeMillis() - start;
        //final long createdNum = HibernateUtil.getStatistics().getEntityInsertCount();

    }

    public void xtestFastCreatePerformance() throws AccessException, ConstraintException, AbortedException {
        //HibernateUtil.getStatistics().clear();
        final long start = System.currentTimeMillis();
        for (int i = 0; i < GeneralTest.loopNum; i++) {
            this.wv = this.getWV();
            ((WritableVersionImpl) this.wv).getFlushMode().setFlushSessionAfterCreate(false);
            try {
                for (int j = 0; j < GeneralTest.subLoopNum; j++) {
                    this.create(Sample.class);
                    this.create(Target.class);
                    this.create(Experiment.class);
                }
                this.wv.commit();

            } finally {
                if (!this.wv.isCompleted()) {
                    this.wv.abort();
                }
            }
        }
        final long time = System.currentTimeMillis() - start;
        /*final long createdNum = HibernateUtil.getStatistics().getEntityInsertCount();
        System.out.println("PerformanceTest: Fast created " + createdNum + " entities used " + time / 1000.0
            + " s"); */
    }

    public void xtestSearchPerformance() throws AccessException, ConstraintException, AbortedException {
        //HibernateUtil.getStatistics().clear();
        final long start = System.currentTimeMillis();
        for (int i = 0; i < GeneralTest.loopNum; i++) {
            this.rv = this.getRV();
            final Searcher seracher = new Searcher(this.rv);
            try {

                seracher.searchAll(AbstractTestCase.model.getMetaClass(Sample.class.getName()), "" + i);
                seracher.searchAll(AbstractTestCase.model.getMetaClass(Target.class.getName()), "" + i);
                seracher.searchAll(AbstractTestCase.model.getMetaClass(Experiment.class.getName()), "" + i);
                this.rv.commit();

            } finally {
                if (!this.rv.isCompleted()) {
                    this.rv.abort();
                }
            }
        }
        final long time = System.currentTimeMillis() - start;
        /*final long createdNum = HibernateUtil.getStatistics().getEntityLoadCount();
        System.out.println("PerformanceTest: Search&Load " + createdNum + " entities used " + time / 1000.0
            + " s"); */
    }
}
