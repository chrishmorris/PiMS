/**
 * current-pims-web org.pimslims.lab.target TargetProgressTester.java
 * 
 * @author pvt43
 * @date 12 Feb 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 pvt43
 * 
 * 
 */
package org.pimslims.lab.target;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.search.Conditions;
import org.pimslims.test.AbstractTestCase;

/**
 * TargetProgressTester
 * 
 */
public class TargetProgressTester extends AbstractTestCase {

    /**
     * 
     */
    public TargetProgressTester() {
        super("Test search with criterias");
    }

    public void testSearch() {
        try {
            this.wv = this.getWV();

            final Map<String, Object> critials = new HashMap<String, Object>();
            //critials.put(Experiment.PROP_EXPERIMENTTYPE, et);

            critials.put(LabBookEntry.PROP_CREATOR, Conditions.notNull());
            critials.put(Experiment.PROP_PROJECT, Conditions.notNull());
            final long time = System.currentTimeMillis() - (long) 1000 * 60 * 60 * 24 * 30;
            final Calendar start = Calendar.getInstance();
            start.setTimeInMillis(time);
            critials.put(Experiment.PROP_STARTDATE, Conditions.greaterThan(start));
            final Collection<Experiment> exps = this.wv.findAll(Experiment.class, critials);
            System.out.println(exps);
            this.wv.abort();
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }
}
