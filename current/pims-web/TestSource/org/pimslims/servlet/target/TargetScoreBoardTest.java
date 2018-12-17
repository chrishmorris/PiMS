/**
 * pims-web org.pimslims.servlet.target TargetScoreBoardTest.java
 * 
 * @author bl67
 * @date 23 Dec 2009
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2009 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.target;

import java.util.Calendar;

import junit.framework.Assert;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.leeds.TargetUtility;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.Target;
import org.pimslims.test.AbstractTestCase;

/**
 * TargetScoreBoardTest
 * 
 */
public class TargetScoreBoardTest extends AbstractTestCase {

    public void testScoresWith1Milestone() throws AccessException, ConstraintException {
        this.wv = this.getWV();
        try {
            final TargetStatus ts = this.create(TargetStatus.class);
            final java.util.Collection<TargetStatus> statuses = TargetUtility.getTargetStatuses(this.wv);

            final java.util.Map<String, Integer> historicalScores = new java.util.HashMap<String, Integer>();
            // scores 1 for every target that's EVER BEEN in each status, incl. now
            final java.util.Map<String, Integer> currentScores = new java.util.HashMap<String, Integer>();
            // scores 1 for every target that's in each status NOW
            for (final TargetStatus status : statuses) {
                historicalScores.put(status.getName(), new Integer(0));
                currentScores.put(status.getName(), new Integer(0));
            }
            TargetScoreboard.searchScores(this.wv, historicalScores, currentScores);
            Assert.assertEquals(0, historicalScores.get(ts.getName()).intValue());
            Assert.assertEquals(0, currentScores.get(ts.getName()).intValue());

            //create a target with milestone
            final Target target = this.create(Target.class);
            new Milestone(this.wv, Calendar.getInstance(), ts, target);
            TargetScoreboard.searchScores(this.wv, historicalScores, currentScores);
            Assert.assertEquals(1, currentScores.get(ts.getName()).intValue());
            Assert.assertEquals(1, historicalScores.get(ts.getName()).intValue());
        } finally {
            this.wv.abort();
        }
    }

    public void testScoresWith2Milestones() throws AccessException, ConstraintException {
        this.wv = this.getWV();
        try {
            final TargetStatus ts = this.create(TargetStatus.class);
            final TargetStatus ts2 = this.create(TargetStatus.class);
            final java.util.Collection<TargetStatus> statuses = TargetUtility.getTargetStatuses(this.wv);

            final java.util.Map<String, Integer> historicalScores = new java.util.HashMap<String, Integer>();
            // scores 1 for every target that's EVER BEEN in each status, incl. now
            final java.util.Map<String, Integer> currentScores = new java.util.HashMap<String, Integer>();
            // scores 1 for every target that's in each status NOW
            for (final TargetStatus status : statuses) {
                historicalScores.put(status.getName(), new Integer(0));
                currentScores.put(status.getName(), new Integer(0));
            }
            TargetScoreboard.searchScores(this.wv, historicalScores, currentScores);
            Assert.assertEquals(0, historicalScores.get(ts.getName()).intValue());
            Assert.assertEquals(0, currentScores.get(ts.getName()).intValue());

            //create a target with milestone
            final Target target = this.create(Target.class);
            new Milestone(this.wv, Calendar.getInstance(), ts, target);
            new Milestone(this.wv, Calendar.getInstance(), ts2, target);
            TargetScoreboard.searchScores(this.wv, historicalScores, currentScores);
            Assert.assertEquals(0, currentScores.get(ts.getName()).intValue());
            Assert.assertEquals(1, historicalScores.get(ts.getName()).intValue());
        } finally {
            this.wv.abort();
        }

    }

    public void testScoresWith3Milestones() throws AccessException, ConstraintException {
        this.wv = this.getWV();
        try {
            final TargetStatus ts = this.create(TargetStatus.class);
            final TargetStatus ts2 = this.create(TargetStatus.class);
            final java.util.Collection<TargetStatus> statuses = TargetUtility.getTargetStatuses(this.wv);

            final java.util.Map<String, Integer> historicalScores = new java.util.HashMap<String, Integer>();
            // scores 1 for every target that's EVER BEEN in each status, incl. now
            final java.util.Map<String, Integer> currentScores = new java.util.HashMap<String, Integer>();
            // scores 1 for every target that's in each status NOW
            for (final TargetStatus status : statuses) {
                historicalScores.put(status.getName(), new Integer(0));
                currentScores.put(status.getName(), new Integer(0));
            }
            TargetScoreboard.searchScores(this.wv, historicalScores, currentScores);
            Assert.assertEquals(0, historicalScores.get(ts.getName()).intValue());
            Assert.assertEquals(0, currentScores.get(ts.getName()).intValue());

            //create a target with milestone
            final Target target = this.create(Target.class);
            new Milestone(this.wv, Calendar.getInstance(), ts, target);
            new Milestone(this.wv, Calendar.getInstance(), ts2, target);
            new Milestone(this.wv, Calendar.getInstance(), ts, target);
            TargetScoreboard.searchScores(this.wv, historicalScores, currentScores);
            Assert.assertEquals(1, currentScores.get(ts.getName()).intValue());
            Assert.assertEquals(1, historicalScores.get(ts.getName()).intValue());
        } finally {
            this.wv.abort();
        }

    }
}
