/**
 * current-pims-web org.pimslims.presentation ExperimentUpdateBeanTest.java
 * 
 * @author cm65
 * @date 4 Mar 2008
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.presentation;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.reference.ExperimentType;

/**
 * ExperimentUpdateBeanTest
 * 
 */
public class ExperimentUpdateBeanTest extends TestCase {

    private static final String UNIQUE = "test" + System.currentTimeMillis();

    private static final Calendar NOW = java.util.Calendar.getInstance();

    private final AbstractModel model;

    /**
     * @param name
     */
    public ExperimentUpdateBeanTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for {@link org.pimslims.presentation.ExperimentUpdateBean#getBeans(java.util.Map)}.
     * 
     * @throws AccessException
     */
    public final void testNoBeans() throws AccessException {
        final Set<ExperimentUpdateBean> beans =
            ExperimentUpdateBean.getBeans(Access.ADMINISTRATOR, Collections.EMPTY_MAP);
        Assert.assertEquals(0, beans.size());
    }

    /**
     * Test method for {@link org.pimslims.presentation.ExperimentUpdateBean#getBeans(java.util.Map)}.
     * 
     * @throws AccessException
     * @throws ConstraintException
     */
    public final void testGetBeans() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ExperimentUpdateBeanTest.UNIQUE);
            final Map<ModelObject, Map<String, Object>> changed = new HashMap();
            final Experiment experiment =
                new Experiment(version, ExperimentUpdateBeanTest.UNIQUE, ExperimentUpdateBeanTest.NOW,
                    ExperimentUpdateBeanTest.NOW, type);
            new OutputSample(version, experiment);
            changed.put(experiment, Collections.EMPTY_MAP);
            final Set<ExperimentUpdateBean> beans =
                ExperimentUpdateBean.getBeans(Access.ADMINISTRATOR, changed);
            Assert.assertEquals(1, beans.size());
        } finally {
            version.abort(); // no testing persistence
        }
    }

    /**
     * Test whether updating an output sample is reported
     * 
     * @throws AccessException
     * @throws ConstraintException
     */
    public final void testUpdateOutput() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ExperimentUpdateBeanTest.UNIQUE);
            final Map<ModelObject, Map<String, Object>> changed = new HashMap();
            final Experiment experiment =
                new Experiment(version, ExperimentUpdateBeanTest.UNIQUE, ExperimentUpdateBeanTest.NOW,
                    ExperimentUpdateBeanTest.NOW, type);
            final OutputSample os = new OutputSample(version, experiment);
            changed.put(os, Collections.EMPTY_MAP);
            final Set<ExperimentUpdateBean> beans =
                ExperimentUpdateBean.getBeans(Access.ADMINISTRATOR, changed);
            Assert.assertEquals(1, beans.size());
            final ExperimentUpdateBean bean = beans.iterator().next();
            Assert.assertEquals(1, bean.getUpdatedOutputSamples().size());
        } finally {
            version.abort(); // no testing persistence
        }
    }

    /**
     * Test whether updating an input sample is reported
     * 
     * @throws AccessException
     * @throws ConstraintException
     */
    public final void testUpdateInput() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ExperimentUpdateBeanTest.UNIQUE);
            final Map<ModelObject, Map<String, Object>> changed = new HashMap();
            final Experiment experiment =
                new Experiment(version, ExperimentUpdateBeanTest.UNIQUE, ExperimentUpdateBeanTest.NOW,
                    ExperimentUpdateBeanTest.NOW, type);
            new OutputSample(version, experiment);
            final InputSample is = new InputSample(version, experiment);
            changed.put(is, Collections.EMPTY_MAP);
            final Set<ExperimentUpdateBean> beans =
                ExperimentUpdateBean.getBeans(Access.ADMINISTRATOR, changed);
            Assert.assertEquals(1, beans.size());
            final ExperimentUpdateBean bean = beans.iterator().next();
            Assert.assertEquals(1, bean.getUpdatedInputSamples().size());
        } finally {
            version.abort(); // no testing persistence
        }
    }

    /**
     * Check at most one bean is returned for an experiment
     * 
     * @throws AccessException
     * @throws ConstraintException
     */
    public final void testUpdateTwice() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ExperimentUpdateBeanTest.UNIQUE);
            final Map<ModelObject, Map<String, Object>> changed = new HashMap();
            final Experiment experiment =
                new Experiment(version, ExperimentUpdateBeanTest.UNIQUE, ExperimentUpdateBeanTest.NOW,
                    ExperimentUpdateBeanTest.NOW, type);
            new OutputSample(version, experiment);
            final InputSample is = new InputSample(version, experiment);
            changed.put(experiment, Collections.EMPTY_MAP);
            changed.put(is, Collections.EMPTY_MAP);
            final Set<ExperimentUpdateBean> beans =
                ExperimentUpdateBean.getBeans(Access.ADMINISTRATOR, changed);
            Assert.assertEquals(1, beans.size());
            final ExperimentUpdateBean bean = beans.iterator().next();
            Assert.assertEquals(1, bean.getUpdatedInputSamples().size());
        } finally {
            version.abort(); // no testing persistence
        }
    }

    /**
     * Update a parameter
     * 
     * @throws AccessException
     * @throws ConstraintException
     */
    public final void testUpdateParameter() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ExperimentUpdateBeanTest.UNIQUE);
            final Map<ModelObject, Map<String, Object>> changed = new HashMap();
            final Experiment experiment =
                new Experiment(version, ExperimentUpdateBeanTest.UNIQUE, ExperimentUpdateBeanTest.NOW,
                    ExperimentUpdateBeanTest.NOW, type);
            new OutputSample(version, experiment);
            final Parameter parm = new Parameter(version, experiment);
            changed.put(parm, Collections.EMPTY_MAP);
            final Set<ExperimentUpdateBean> beans =
                ExperimentUpdateBean.getBeans(Access.ADMINISTRATOR, changed);
            Assert.assertEquals(1, beans.size());
            final ExperimentUpdateBean bean = beans.iterator().next();
            Assert.assertEquals(1, bean.getUpdatedParameters().size());
        } finally {
            version.abort(); // no testing persistence
        }
    }
}
