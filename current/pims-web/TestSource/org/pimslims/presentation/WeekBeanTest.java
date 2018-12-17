/**
 * V3_3-web org.pimslims.presentation DayBeanTest.java
 * 
 * @author cm65
 * @date 1 Oct 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation;

import java.util.Calendar;

import junit.framework.Assert;

import org.pimslims.access.Access;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.test.AbstractTestCase;

/**
 * DayBeanTest
 * 
 */
public class WeekBeanTest extends AbstractTestCase {

    private static final String UNIQUE = "db" + System.currentTimeMillis();

    private static final String USERNAME = "u" + WeekBeanTest.UNIQUE;

    private static final Calendar NOW = Calendar.getInstance();

    /**
     * Constructor for DayBeanTest
     * 
     * @param name
     */
    public WeekBeanTest(final String name) {
        super(name);
    }

    public void testConstructor() {
        final Calendar day = Calendar.getInstance();
        day.set(2009, Calendar.OCTOBER, 7);
        this.wv = this.getWV();
        try {
            final WeekBean weekBean = new WeekBean(day, this.wv, Access.ADMINISTRATOR);
            Assert.assertEquals(7, weekBean.getDayBeans().size());
            Assert.assertEquals(Calendar.MONDAY, weekBean.getMonday().get(Calendar.DAY_OF_WEEK));
            Assert.assertEquals(2009, weekBean.getMonday().get(Calendar.YEAR));
            Assert.assertEquals(Calendar.OCTOBER, weekBean.getMonday().get(Calendar.MONTH));
            Assert.assertEquals(5, weekBean.getMonday().get(Calendar.DAY_OF_MONTH));
        } finally {
            this.wv.abort();
        }
    }

    public void testOneExperiment() throws ConstraintException {
        final Calendar day = Calendar.getInstance();
        final String today =
            new String[] { "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" }[day
                .get(Calendar.DAY_OF_WEEK) - 1];
        this.wv = this.getWV();
        try {
            final ExperimentType type = new ExperimentType(this.wv, WeekBeanTest.UNIQUE);
            final Experiment experiment =
                new Experiment(this.wv, WeekBeanTest.UNIQUE, WeekBeanTest.NOW, WeekBeanTest.NOW, type);
            //experiment.setCreationDate(day);
            //experiment.setLastEditedDate(day);
            final WeekBean bean = new WeekBean(day, this.wv, WeekBeanTest.USERNAME);
            Assert.assertTrue(bean.getDayBean(today).getExperiments().size() > 0);
            boolean found = false;
            for (final ModelObjectShortBean shortBean : bean.getDayBean(today).getExperiments()) {
                if (shortBean.getHook().equals(experiment.get_Hook())) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
            //Assert.assertEquals(null, bean.getTargets());
        } finally {
            this.wv.abort();
        }
    }
}
