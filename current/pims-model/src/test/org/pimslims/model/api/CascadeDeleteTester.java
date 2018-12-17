/**
 * org.pimslims.generatedApi.role CascadeTester.java
 * 
 * @date 18-Oct-2006 13:28:56
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

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.people.Group;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.people.Person;
import org.pimslims.model.people.PersonInGroup;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.WorkflowItem;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.test.POJOFactory;

/**
 * CascadeTester
 * 
 */
public class CascadeDeleteTester extends org.pimslims.test.AbstractTestCase {

    public void testExperimentTypeWorkFlowItem() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            ExperimentType type = this.create(ExperimentType.class);
            new WorkflowItem(wv, type);
            type.delete();
            wv.flush();
        } finally {
            wv.abort();
        }
    }

    public void testPermission() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            LabNotebook book = new LabNotebook(wv, UNIQUE);
            UserGroup group = new UserGroup(wv, UNIQUE);
            Permission p1 = new Permission(wv, "read", book, group);
            p1.delete(); // should not cascade
            new Permission(wv, "delete", book, new UserGroup(wv, UNIQUE + "two"));
            new Permission(wv, "update", new LabNotebook(wv, UNIQUE + "two"), group);
            book.delete(); // should cascade
            group.delete(); // should cascade
            wv.flush();
        } finally {
            wv.abort();
        }
    }

    public void testROE() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            ResearchObjectiveElement roe = this.create(ResearchObjectiveElement.class);
            roe.getResearchObjective().delete();
            wv.flush();
        } finally {
            wv.abort();
        }
    }

    public void testTarget() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            Target object = this.create(Target.class);
            object.setAliasNames(Collections.singleton("aaa"));
            object.delete();
            wv.flush();
        } finally {
            wv.abort();
        }
    }

    public void testCascadeDelete() {
        String targetHook = null, milestoneHook = null;
        wv = getWV();
        try {// create a new target with target status
            org.pimslims.model.target.Target target = create(Target.class);
            Milestone ts = POJOFactory.createMilestone(wv, target);
            assertTrue(target.getMilestones().contains(ts));
            targetHook = target.get_Hook();
            milestoneHook = ts.get_Hook();
            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        wv = getWV();
        try {// delete target status
            org.pimslims.model.target.Target target = (Target) wv.get(targetHook);
            assertTrue(target.getMilestones().size() == 1);
            ((ModelObject) wv.get(milestoneHook)).delete();
            assertTrue(target.getMilestones().size() == 0);
            wv.commit();

        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        wv = getWV();
        try {
            Target target = (Target) wv.get(targetHook);
            assertTrue(target != null);
            assertTrue(target.getMilestones() == null
                || target.get_MetaClass().getMetaRole(Target.PROP_MILESTONES).get(target).size() == 0);
            target.delete();
            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testCascadeDeleteUsedByAnotherObject() {
        String orgHook = null, pgHook = null, personHook = null;
        wv = getWV();
        try {// create some object
            Organisation org = new Organisation(wv, UNIQUE);
            orgHook = org.get_Hook();
            Group group = POJOFactory.createGroup(wv, org);
            //groupHook = group.get_Hook();
            Person person = new Person(wv, UNIQUE);
            personHook = person.get_Hook();
            PersonInGroup pg = new PersonInGroup(wv, group, person);
            pgHook = pg.get_Hook();
            assertTrue(group.getPersonInGroups().contains(pg));// group is in
            // used by
            // PersonInGroup
            org.setGroups(Collections.singleton(group));
            assertTrue(org.getGroups().contains(group));
            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        wv = getWV();
        try {
            Organisation org = (Organisation) wv.get(orgHook);
            PersonInGroup pg = (PersonInGroup) wv.get(pgHook);

            assertTrue(pg.getGroup() != null);
            org.delete();// delete org should cause group deleted and
            // PersonInGroup's group should be set null
            // assertTrue(pg.getGroup()==null);

            wv.commit();
            fail("org should not be deleted as group is required for PersonInGroup");
        } catch (ModelException ex) {
            // fail(ex.toString());
        } catch (org.hibernate.exception.ConstraintViolationException ex) {
            // fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        wv = getWV();
        try {
            Organisation org = (Organisation) wv.get(orgHook);
            PersonInGroup pg = (PersonInGroup) wv.get(pgHook);

            pg.delete();
            Person p = (Person) wv.get(personHook);
            p.delete();
            wv.delete(org.getGroups());
            org.delete();
            wv.commit();

        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }
}
