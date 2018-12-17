package org.pimslims.servlet.sample;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.worklist.SampleCriteria;
import org.pimslims.test.AbstractTestCase;

public class SampleProgressTest extends AbstractTestCase {

    public void testGetAllExpTypeNames() throws AccessException, ConstraintException {
        this.wv = this.getWV();
        try {
            final ExperimentType et = this.create(ExperimentType.class);
            final String etName = et.get_Name();
            Assert.assertTrue(SampleProgress.getAllExpTypeNames(this.wv).contains(etName));

        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }

    }

    public void testGetAllStatusNames() throws AccessException, ConstraintException {
        this.wv = this.getWV();
        try {
            final TargetStatus ts = this.create(TargetStatus.class);
            final String tsName = ts.get_Name();
            Assert.assertTrue(SampleProgress.getAllStatusNames(this.wv).contains(tsName));

        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }

    }

    public void testGetSampleBeans() throws AccessException, ConstraintException {
        this.wv = this.getWV();
        try {
            final SampleCriteria criteria = new SampleCriteria();
            final String ready = "yes";
            final String inUsed = "any";
            final int noProgressDays = 100;
            final String nextExpType = "type" + System.currentTimeMillis();
            final String activeString = "no";
            final User p = this.create(User.class);
            final String userHook = p.get_Hook();
            SampleProgress.getSampleBeans(this.wv, criteria, ready, inUsed, noProgressDays, activeString,
                nextExpType, userHook);

            Assert.assertNotNull(criteria);
            Assert.assertTrue(criteria.getExpTypeNameReadyFor().startsWith("type"));
            Assert.assertTrue(criteria.getUserHookAssignedTo().equals(userHook));
            Assert.assertTrue(criteria.getUserNameAssignedTo(this.wv).equals(p.get_Name()));
            Assert.assertFalse(criteria.getActive());
            Assert.assertNull(criteria.getAlreadyInUse());
            Assert.assertEquals(100, criteria.getDaysNoProgress());
            Assert.assertTrue(criteria.getReadyForNext());

        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    public void testGetSelectedSampleHooks() throws AccessException, ConstraintException {
        this.wv = this.getWV();
        try {
            final Sample s = this.create(Sample.class);
            final Sample s2 = this.create(Sample.class);
            final Map<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put(s.get_Hook(), new String[] { "on" });
            parameterMap.put(s.get_Name(), new String[] { "on" });
            parameterMap.put(s2.get_Hook(), new String[] { "off" });

            final Collection<String> sampleHooks = SampleProgress.getSelectedSampleHooks(parameterMap);

            Assert.assertTrue(sampleHooks.size() == 1);
            Assert.assertTrue(sampleHooks.contains(s.get_Hook()));

        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    public void testProcessNewAssignment() throws AccessException, ConstraintException {
        this.wv = this.getWV();
        try {
            final Sample s = this.create(Sample.class);
            final User p = this.create(User.class);
            final String comments = "comment" + System.currentTimeMillis();
            SampleProgress.processNewAssignment(this.wv, Collections.singleton(s.get_Hook()), p.get_Hook(),
                comments);
            Assert.assertEquals(p, s.getAssignTo());
            Assert.assertEquals(comments, s.getDetails());
            SampleProgress.processNewAssignment(this.wv, Collections.singleton(s.get_Hook()), p.get_Hook(),
                comments);
            Assert.assertEquals(comments + "; " + comments, s.getDetails());

        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

}
