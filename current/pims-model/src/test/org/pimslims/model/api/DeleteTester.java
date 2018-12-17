/**
 * ccp.api.pojo DeleteTester.java
 * 
 * @date 31-Oct-2006 10:01:41
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 *         Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 *           Copyright (c) 2006
 * 
 *           This library is free software; you can redistribute it and/or modify it under the terms of the
 *           GNU Lesser General Public License as published by the Free Software Foundation; either version
 *           2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.model.api;

import java.util.Collections;

import org.pimslims.dao.AbstractModel;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.people.Group;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.reference.Database;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Project;
import org.pimslims.model.target.Target;
import org.pimslims.model.target.TargetGroup;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * DeleteTester: test POJO.delete() is correct or not 1Way Required (M->1) Normal + 1Way Required (M->M)
 * Normal - 1Way NotRequired (M->1) Normal + 1Way NotRequired (M->M) Normal + 2Way Required (1<->1) Parent +
 * 2Way Required (M<->1) Parent + 2Way Required (1<->M) Normal 2Way Required (1<->1) Normal - 2Way Required
 * (M<->M) Normal + 2Way Required (M<->1) Normal + 2Way NotRequired (M<->M) Normal + 2Way NotRequired (M<->1)
 * Normal + 2Way NotRequired (1<->1) Normal - 2Way NotRequired (1<->M) Normal + 2Way NotRequired (1<->1) Child
 * - 2Way NotRequired (1<->M) Child + test: (1) A is deleteable (2) delete A will not delete B (3) For 2 way
 * role, after delete, B does not have A
 */

public class DeleteTester extends AbstractTestCase {

    /**
     * 2Way Required (M<->1) Parent 2Way NotRequired (1<->M) Child
     */
    public void test2way_Required_Parent_Mto1() {
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        String hookA1 = null, hookA2 = null, hookB = null;
        try {
            final Organisation org = POJOFactory.createOrganisation(wv);
            hookB = org.get_Hook();
            final Group is1 = POJOFactory.createGroup(wv, org);
            final Group is2 = POJOFactory.createGroup(wv, org);
            hookA1 = is1.get_Hook();
            hookA2 = is2.get_Hook();
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        // test delete(1) A1 is deleteable
        assertTrue(delete(hookA1));
        // test delete(2) delete A1 will not delete B
        assertTrue(isExist(hookB));
        // test B is deleteable and A2 is deleted at same time
        assertTrue(delete(hookB));
        assertFalse(isExist(hookA2));
    }

    /**
     * 2Way NotRequired (1<->1) Normal
     */
    public void test2way_NotRequired_Normal_1to1() {
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        String hookA1 = null, hookB = null;
        try {
            final Sample sample = POJOFactory.createSample(wv);
            hookB = sample.get_Hook();
            final Experiment exp = POJOFactory.createExperiment(wv);
            final OutputSample os1 = POJOFactory.createOutputSample(wv, exp);
            os1.setSample(sample);
            assertEquals(sample.getOutputSample(), os1);
            hookA1 = os1.get_Hook();
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        // test delete(1) A1 is deleteable
        assertTrue(delete(hookA1));
        // test delete(2) delete A1 will not delete B
        assertTrue(isExist(hookB));
        // test delete(3) after delete, B does not have A
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            final Sample s = (Sample) wv.get(hookB);
            assertTrue(s.getOutputSample() == null);
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        // test B is deleteable
        assertTrue(delete(hookB));

    }

    /**
     * 1Way A -> B Required (M->1) Normal
     */
    // TODO DM change 28 - ImportSample deleted - find other example
    public void notest1way_Required_Normal_Mto1() {
        /*
         * wv = model.getWritableVersion(AbstractModel.SUPERUSER); String hookA1 = null, hookA2 = null, hookB =
         * null; try { Organisation org = POJOFactory.createOrganisation(wv); hookB = org.get_Hook();
         * ImportSample is1=POJOFactory.createImportSample(wv,org); ImportSample
         * is2=POJOFactory.createImportSample(wv,org); hookA1 = is1.get_Hook(); hookA2 = is2.get_Hook();
         * wv.commit(); } catch (ModelException ex) { ex.printStackTrace(); fail(ex.getMessage()); } finally {
         * if (!wv.isCompleted()) { wv.abort(); } } // test delete(1) A1 is deleteable
         * assertTrue(delete(hookA1)); // test delete(2) delete A1 will not delete B
         * assertTrue(isExist(hookB)); // test B is not deleteable as A2 using B assertFalse(delete(hookB)); //
         * test delete(1) A2 is deleteable assertTrue(delete(hookA2)); // now B is not deleteable as no one
         * using B assertTrue(delete(hookB));
         */}

    /**
     * 1Way NotRequired (M->M) Normal
     */
    public void test1way_NotRequired_Normal_MtoM() {
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        String hookA1 = null, hookB = null;
        try {
            // A->B
            // Target->TargetProject: m-m, not reqired-not required, no
            // parent relationship
            // create:
            final TargetGroup project1 = POJOFactory.createTargetGroup(wv);
            final TargetGroup project2 = POJOFactory.createTargetGroup(wv);
            final Target target = POJOFactory.createTarget(wv);
            project1.setTargets(Collections.singleton(target));
            project2.setTargets(Collections.singleton(target));

            assertTrue(project1.getTargets().size() == 1);
            hookA1 = project1.get_Hook();
            //hookA2 = project2.get_Hook();
            hookB = target.get_Hook();
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        // test delete(1) A is deletable
        assertTrue(delete(hookA1));
        // test delete(2) delete A will not delete B
        assertTrue(isExist(hookB));
        // B is deletable
        assertTrue(delete(hookB));

    }

    public void notest2way_NotRequired_Normal_MtoM_NotChangable() {
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        String hookA1 = null, hookA2 = null, hookB = null;
        try {
            final TargetGroup p = POJOFactory.createTargetGroup(wv);
            final Target t1 = POJOFactory.createTarget(wv);
            final Target t2 = POJOFactory.createTarget(wv);
            t1.setTargetGroups(Collections.singleton(p));
            t2.setTargetGroups(Collections.singleton(p));
            assertTrue(p.getTargets().size() == 2);
            hookA1 = t1.get_Hook();
            hookA2 = t2.get_Hook();
            hookB = p.get_Hook();
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        // test delete(1) B is deletable
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            final Project p = (Project) wv.get(hookB);
            final Target t1 = (Target) wv.get(hookA1);
            final Target t2 = (Target) wv.get(hookA2);
            p.delete();
            assertEquals(t1.getProjects().size(), 0);
            assertEquals(t2.getProjects().size(), 0);
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        // test delete(2) delete B will not delete A
        assertTrue(isExist(hookA1));
        assertTrue(isExist(hookA2));

    }

    public void test2way_NotRequired_Normal_MtoM() {
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        String hookTested = null, hookRef = null;
        try {
            // A<-B
            // Target<->TargetProject: m-m, not reqired-not required, no
            // parent relationship
            // create:
            final TargetGroup project = POJOFactory.createTargetGroup(wv);
            final Target target = POJOFactory.createTarget(wv);
            project.setTargets(Collections.singleton(target));

            assertTrue(project.getTargets().size() == 1);
            hookTested = project.get_Hook();
            hookRef = target.get_Hook();
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        // test delete(1) A is deletable
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            final TargetGroup project = (TargetGroup) wv.get(hookTested);
            final Target target = (Target) wv.get(hookRef);

            project.delete();
            assertEquals(target.getTargetGroups().size(), 0);
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        // test delete(2) delete A will not delete B
        assertTrue(isExist(hookRef));
        // test delete(3) after delete, B does not have A
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            final Target target = (Target) wv.get(hookRef);
            assertEquals(target.getProjects().size(), 0);
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

    /**
     * 2Way Required (M<->M) Normal
     */
    //  don't know any other example in the DM
    public void notest2way_Required_Normal_MtoM() {
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        String hookA1 = null, hookA2 = null, hookB = null;
        try {
            // A<->B
            // sample->SampleCategorie: m-m, not reqired-required, no parent
            // relationship
            // create:
            final Sample sample1 = POJOFactory.createSample(wv);
            final SampleCategory sc = sample1.getSampleCategories().iterator().next();
            final Sample sample2 = POJOFactory.createSample(wv, sc);
            assertTrue(sc.getAbstractSamples().size() == 2);
            assertTrue(sc.getAbstractSamples().contains(sample1));
            hookA1 = sample1.get_Hook();
            hookA2 = sample2.get_Hook();
            hookB = sc.get_Hook();
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        // test delete(1) A1 is deletable
        assertTrue(delete(hookA1));
        // test delete(2) delete A1 will not delete B
        assertTrue(isExist(hookB));
        // test delete(3) after delete A1, B does not have A1
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            final SampleCategory sc = (SampleCategory) wv.get(hookB);
            assertEquals(sc.getAbstractSamples().size(), 1);
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        // test B is not deletable as B is required by A2
        assertFalse(delete(hookB));
        // A2 is deletable and then B is deletable
        assertTrue(delete(hookA2));
        assertTrue(delete(hookB));

    }

    /**
     * 2Way Required (M<->1) Normal 2Way NotRequired (1<->M) Normal
     */

    public void test2way_Required_Normal_Mto1() {
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        String hookA1 = null, hookA2 = null, hookB = null;
        try {
            // A->B
            // Target<->MolComponent: m-1, not reqired-required, no parent
            // relationship
            // create:
            final Target t1 = POJOFactory.createTarget(wv);
            final Molecule mc = t1.getProtein();
            final Target t2 = POJOFactory.createTarget(wv, mc);

            assertTrue(mc.getProteinForTargets().contains(t1));
            assertTrue(mc.getProteinForTargets().contains(t2));
            hookA1 = t1.get_Hook();
            hookA2 = t2.get_Hook();
            hookB = mc.get_Hook();
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        // test delete(1) A is deletable
        assertTrue(delete(hookA1));
        // test delete(2) delete A will not delete B
        assertTrue(isExist(hookB));
        // test delete(3) after delete A, B does not have A
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            final Molecule mc = (Molecule) wv.get(hookB);
            assertEquals(mc.getProteinForTargets().size(), 1);
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        // test B is not deletable as B is required by A2
        assertFalse(delete(hookB));
        // A2 is deletable and then B is deletable
        assertTrue(delete(hookA2));
        assertTrue(delete(hookB));
    }

    private boolean delete(final String hook) {
        // delete
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            final ModelObject mo = wv.get(hook);
            mo.delete();
            wv.commit();
        } catch (final ModelException ex) {
            return false;
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
                return false;
            }
        }
        // verify deleted
        return (!isExist(hook));
    }

    private boolean isExist(final String hook) {
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            final ModelObject mo = wv.get(hook);
            if (mo == null) {
                wv.commit();
                return false;
            }
            wv.commit();
            return true;

        } catch (final ModelException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testChildPermissions() throws AccessException, ConstraintException, AbortedException {
        String username = null;
        String hook = null;
        String dbHook = null;
        String objectHook = null;
        String ownerName = null;
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            // make a user with delete permission
            final LabNotebook owner = POJOFactory.create(wv, LabNotebook.class);
            final User user = POJOFactory.createUser(wv);
            username = user.get_Name();
            final UserGroup group = POJOFactory.create(wv, UserGroup.class);
            group.addMemberUser(user);
            new Permission(wv, "delete", owner, group);
            new Permission(wv, "read", owner, group);
            // make a DB Ref
            wv.setDefaultOwner(owner);
            final Database db = POJOFactory.createDbName(wv);
            dbHook = db.get_Hook();
            final LabBookEntry object = POJOFactory.createTarget(wv);
            objectHook = object.get_Hook();
            final ExternalDbLink link = new ExternalDbLink(wv, db, object);
            ownerName = owner.getName();
            assertEquals(ownerName, object.get_Owner());
            assertEquals(ownerName, link.get_Owner());
            hook = link.get_Hook();
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        // check user can delete it
        wv = model.getWritableVersion(username);
        try {
            final LabBookEntry object = wv.get(objectHook);
            assertEquals(ownerName, object.get_Owner());
            final ExternalDbLink link = wv.get(hook);
            assertEquals(ownerName, link.get_Owner());
            assertTrue(link.get_MayDelete());
            link.delete();
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            // test
            final Target target = (Target) wv.get(objectHook);
            assertEquals(0, target.getExternalDbLinks().size());

            // tidy up
            Molecule protein = target.getProtein();
            target.delete();
            protein.delete();
            final Database db = wv.get(dbHook);
            LabNotebook owner = target.getAccess();
            db.delete();
            Permission p = owner.getPermissions().iterator().next();
            UserGroup group = p.getUserGroup();
            User user = group.getMemberUsers().iterator().next();
            user.delete();
            group.delete();
            owner.delete();
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

}
