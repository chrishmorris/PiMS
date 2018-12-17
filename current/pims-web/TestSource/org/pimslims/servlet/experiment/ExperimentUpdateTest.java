/**
 * V2_3-pims-web org.pimslims.servlet.experiment ExperimentUpdateTest.java
 * 
 * @author cm65
 * @date 28 Aug 2008
 * 
 * Protein Information Management System
 * @version: 2.2
 * 
 * Copyright (c) 2008 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.experiment;

import java.util.Calendar;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.reference.WorkflowItem;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;

/**
 * ExperimentUpdateTest
 * 
 */
public class ExperimentUpdateTest extends TestCase {

    private static final String UNIQUE = "eu" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    /**
     * Constructor for ExperimentUpdateTest
     * 
     * @param name
     */
    public ExperimentUpdateTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.experiment.ExperimentUpdate#addMilestone(org.pimslims.dao.WritableVersion, java.lang.String, java.lang.String)} .
     * 
     * @throws ConstraintException
     * @throws AccessException
     */
    public void testAddMilestone() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ExperimentUpdateTest.UNIQUE);
            final WorkflowItem wfi = new WorkflowItem(version, type);
            final TargetStatus status = new TargetStatus(version, ExperimentUpdateTest.UNIQUE);
            wfi.setStatus(status);
            final Experiment experiment =
                new Experiment(version, ExperimentUpdateTest.UNIQUE, ExperimentUpdateTest.NOW,
                    ExperimentUpdateTest.NOW, type);
            final ResearchObjective eb = new ResearchObjective(version, ExperimentUpdateTest.UNIQUE, "test");
            final Molecule protein = new Molecule(version, "protein", ExperimentUpdateTest.UNIQUE);
            final Target target = new Target(version, ExperimentUpdateTest.UNIQUE, protein);
            new ResearchObjectiveElement(version, "test", "test", eb).setTarget(target);
            ExperimentUpdate.addMilestone(version, experiment.get_Hook(), eb.get_Hook());
            Assert.assertFalse(experiment.getMilestones().isEmpty());
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.experiment.ExperimentUpdate#addMilestone(org.pimslims.dao.WritableVersion, java.lang.String, java.lang.String)} .
     * 
     * @throws ConstraintException
     * @throws AccessException
     */
    public void testAddMilestoneNoTarget() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ExperimentUpdateTest.UNIQUE);
            final WorkflowItem wfi = new WorkflowItem(version, type);
            final TargetStatus status = new TargetStatus(version, ExperimentUpdateTest.UNIQUE);
            wfi.setStatus(status);
            final Experiment experiment =
                new Experiment(version, ExperimentUpdateTest.UNIQUE, ExperimentUpdateTest.NOW,
                    ExperimentUpdateTest.NOW, type);
            final ResearchObjective eb = new ResearchObjective(version, ExperimentUpdateTest.UNIQUE, "test");
            final Molecule protein = new Molecule(version, "protein", ExperimentUpdateTest.UNIQUE);
            new Target(version, ExperimentUpdateTest.UNIQUE, protein);
            new ResearchObjectiveElement(version, "test2", "test2", eb);
            ExperimentUpdate.addMilestone(version, experiment.get_Hook(), eb.get_Hook());
            Assert.assertFalse(experiment.getMilestones().isEmpty());
        } finally {
            version.abort();
        }
    }

}
