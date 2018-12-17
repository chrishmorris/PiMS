/**
 * tools org.pimslims.embl OwnerCheckerTest.java
 * 
 * @author cm65
 * @date 21 Sep 2009
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.embl;

import java.util.Collections;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.Project;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;

/**
 * OwnerCheckerTest
 * 
 * For currently unknown reasons, OwnerChanger did not change all targets. This is the test case for a job to
 * put this right.
 * 
 */
public class OwnerCheckerTest extends junit.framework.TestCase {

    private static final String UNIQUE = "chk" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * @param name
     */
    public OwnerCheckerTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testNoProject() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            ResearchObjective ro = new ResearchObjective(version, UNIQUE, UNIQUE);
            ResearchObjectiveElement element = new ResearchObjectiveElement(version, UNIQUE, UNIQUE, ro);
            element.setComponentType("target");
            Molecule protein = new Molecule(version, "protein", UNIQUE);
            Target target = new Target(version, UNIQUE, protein);
            element.setTarget(target);
            new OwnerChecker(version).check(ro);
        } catch (AssertionError e) {
            // that's fine
        } finally {
            version.abort();
        }
    }

    public void testNoOwner() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            ResearchObjective ro = new ResearchObjective(version, UNIQUE, UNIQUE);
            ResearchObjectiveElement element = new ResearchObjectiveElement(version, UNIQUE, UNIQUE, ro);
            element.setComponentType("target");
            Molecule protein = new Molecule(version, "protein", UNIQUE);
            Target target = new Target(version, UNIQUE, protein);
            Project project = new Project(version, UNIQUE, UNIQUE);
            target.setProjects(Collections.singleton(project));
            element.setTarget(target);
            new OwnerChecker(version).check(ro);
        } catch (AssertionError e) {
            // that's fine
        } finally {
            version.abort();
        }
    }

    public void testTarget() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Project project = new Project(version, UNIQUE, UNIQUE);
            new LabNotebook(version, UNIQUE);
            ResearchObjective ro = new ResearchObjective(version, UNIQUE, UNIQUE);
            ResearchObjectiveElement element = new ResearchObjectiveElement(version, UNIQUE, UNIQUE, ro);
            element.setComponentType("target");
            Molecule protein = new Molecule(version, "protein", UNIQUE);
            Target target = new Target(version, UNIQUE, protein);
            target.setProjects(Collections.singleton(project));
            element.setTarget(target);
            new OwnerChecker(version).check(ro);
            assertEquals(target.getName(), protein.get_Owner());
        } finally {
            version.abort();
        }
    }

    public void testOwnerCalledData() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Project project = new Project(version, UNIQUE, UNIQUE);
            LabNotebook owner = new LabNotebook(version, project.getName() + " data");
            ResearchObjective ro = new ResearchObjective(version, UNIQUE, UNIQUE);
            ResearchObjectiveElement element = new ResearchObjectiveElement(version, UNIQUE, UNIQUE, ro);
            element.setComponentType("target");
            Molecule protein = new Molecule(version, "protein", UNIQUE);
            Target target = new Target(version, UNIQUE, protein);
            target.setProjects(Collections.singleton(project));
            element.setTarget(target);
            new OwnerChecker(version).check(ro);
            assertEquals(owner.getName(), protein.get_Owner());
        } finally {
            version.abort();
        }
    }
}
