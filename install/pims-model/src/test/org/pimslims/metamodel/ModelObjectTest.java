/**
 * pims-model org.pimslims.metamodel ModelObjectTest.java
 * 
 * @author cm65
 * @date Jul 27, 2012
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.metamodel;

import java.util.Calendar;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.target.Milestone;

/**
 * ModelObjectTest Tests AbstractModelObject
 */
public class ModelObjectTest extends TestCase {

    private static final String UNIQUE = "mo" + System.currentTimeMillis();

    private final AbstractModel model;

    public ModelObjectTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testName() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            Calendar now = Calendar.getInstance();
            ExperimentType type = new ExperimentType(version, UNIQUE);
            Experiment experiment = new Experiment(version, UNIQUE, now, now, type);
            TargetStatus status = new TargetStatus(version, UNIQUE);
            AbstractModelObject mo = new Milestone(version, now, status, experiment);
            mo.get_Name();
        } finally {
            version.abort();
        }
    }

}
