/**
 * pims-dm org.pimslims.access RoleAccessTest.java
 * 
 * @author cm65
 * @date 4 Sep 2009
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.access;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.Target;
import org.pimslims.model.target.TargetGroup;

/**
 * RoleAccessTest
 * 
 */
public class RoleAccessTest extends TestCase {

    private static final String UNIQUE = "ra" + System.currentTimeMillis();

    private static final String USERNAME = UNIQUE + "user";

    private final AbstractModel model;

    /**
     * Constructor for RoleAccessTest
     * 
     * @param name
     */
    public RoleAccessTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void test() throws AccessException, ConstraintException, AbortedException {

        //set up
        String targetHook = null;
        String groupHook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            new User(version, USERNAME);

            Molecule protein = new Molecule(version, "protein", UNIQUE);
            Target target = new Target(version, UNIQUE, protein);
            targetHook = target.get_Hook();
            TargetGroup group = new TargetGroup(version, UNIQUE);
            LabNotebook access = new LabNotebook(version, UNIQUE);
            group.setAccess(access);
            protein.setAccess(access);
            group.addTarget(target);
            groupHook = group.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        // test
        version = this.model.getWritableVersion(RoleAccessTest.USERNAME);
        try {
            assertNull(version.get(groupHook));
            Target target = version.get(targetHook);
            assertEquals(0, target.getTargetGroups().size());
            assertEquals(null, target.getProtein());
        } finally {
            version.abort();
        }

        // clean up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Target target = version.get(targetHook);
            Molecule protein = target.getProtein();
            assertEquals(1, target.getTargetGroups().size());
            TargetGroup group = target.getTargetGroups().iterator().next();
            LabNotebook access = group.getAccess();
            group.delete();
            access.delete();
            target.delete();
            protein.delete();
            version.findFirst(User.class, User.PROP_NAME, USERNAME).delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
