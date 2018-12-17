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
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.ExperimentType;

/**
 * DayBeanTest
 * 
 */
public class DayBeanTest extends TestCase {

    private static final String UNIQUE = "db" + System.currentTimeMillis();

    private static final String USERNAME = "u" + DayBeanTest.UNIQUE;

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    /**
     * Constructor for DayBeanTest
     * 
     * @param name
     */
    public DayBeanTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testConstructor() {
        final Calendar day = Calendar.getInstance();
        final WritableVersion version = this.model.getTestVersion();
        try {
            final DayBean bean = DayBean.getDayBean(day, version, Access.ADMINISTRATOR);
            Assert.assertEquals(0, bean.getExperiments().size());
            //Assert.assertEquals(null, bean.getOtherObjects());
            //Assert.assertEquals(null, bean.getTargets());
        } finally {
            version.abort();
        }
    }

    public void testOneExperiment() throws ConstraintException {
        //final Calendar day = Calendar.getInstance();
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, DayBeanTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, DayBeanTest.UNIQUE, DayBeanTest.NOW, DayBeanTest.NOW, type);
            final DayBean bean = DayBean.getDayBean(DayBeanTest.NOW, version, DayBeanTest.USERNAME);
            Assert.assertEquals(DayBeanTest.UNIQUE, DayBeanTest.UNIQUE);
            Assert.assertEquals(1, bean.getExperiments().size());
            Assert.assertEquals(experiment.getName(), bean.getExperiments().iterator().next().getName());
            //Assert.assertEquals(null, bean.getTargets());
        } finally {
            version.abort();
        }
    }
}
