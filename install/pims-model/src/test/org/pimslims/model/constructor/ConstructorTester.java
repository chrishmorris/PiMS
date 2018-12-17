/**
 * org.pimslims.hibernate OtherEssentialTest.java
 * 
 * @date 08-Sep-2006 08:48:17
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
package org.pimslims.model.constructor;

import java.util.Collections;
import java.util.HashMap;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.api.BookCitationTester;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.BookCitation;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.people.Person;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.reference.WorkflowItem;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;
import org.pimslims.test.POJOFactory;

/**
 * OtherEssentialTest
 * 
 */
public class ConstructorTester extends org.pimslims.test.AbstractTestCase {

    /**
     * @param name
     */
    public ConstructorTester(String name) {
        super(name);

    }

    public void testOwner() throws AbortedException, ConstraintException, AccessException {
        String userName = null;
        wv = getWV();
        String owner = null;
        try {
            User user = create(wv, User.class);
            owner =
                user.getUserGroups().iterator().next().getPermissions().iterator().next().getLabNotebook()
                    .getName();
            userName = user.getName();
            wv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        wv = getWV(userName);
        try {
            // was with generic method create before, but mandatory attributes
            // now
            // required for BookCitation so changed to specific constructor
            Target target = POJOFactory.createTarget(wv);
            String Owner = target.get_Owner();
            assertTrue(Owner.equalsIgnoreCase(owner));
            BookCitationTester.ATTRIBUTES.put(Attachment.PROP_PARENTENTRY, target);
            BookCitation bc = new BookCitation(wv, BookCitationTester.ATTRIBUTES);
            Owner = bc.get_Owner();
            assertTrue(Owner.equalsIgnoreCase(owner));
            Target t = create(wv, Target.class);
            String TargetOwner = t.get_Owner();
            assertTrue(TargetOwner.equalsIgnoreCase(owner));
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        log("finished");
    }

    public void testEmptyConstructor() {
        wv = getWV();
        try {
            // A->B
            // BookCitation->Auther(person): m-m, not reqired-not required, no
            // parent relationship
            // create:
            // was with generic method create before, but mandatory attributes
            // now
            // required for BookCitation so changed to specific constructor
            Target target = POJOFactory.createTarget(wv);
            BookCitationTester.ATTRIBUTES.put(Attachment.PROP_PARENTENTRY, target);
            BookCitation bc = new BookCitation(wv, BookCitationTester.ATTRIBUTES);
            Person p = create(wv, Person.class);
            bc.setAuthors(p.get_Name());
            assertTrue(bc.getAuthors().length() == p.get_Name().length());
            wv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testIntialized() throws AccessException, ConstraintException, AbortedException {
        String sampleHook = null;
        WritableVersion wv = getWV();

        try {
            sampleHook = ((Sample) create(wv, Sample.class)).get_Hook();
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        wv = getWV();

        try {
            Sample sample = (Sample) wv.get(sampleHook);
            assertTrue(sample != null);
            Experiment exp = create(wv, Experiment.class);
            InputSample is = POJOFactory.createInputSample(wv, exp);
            is.setSample(sample);
            assertTrue(sample.getInputSamples().size() == 1);

            // delete testing objects
            is.delete();
            wv.flush();

            exp.delete();
            wv.flush();
            // sample.delete();
            wv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testEntityIsSaved() {
        wv = getWV();

        try {
            TargetStatus sb = create(TargetStatus.class, TargetStatus.PROP_NAME, "myname");
            assertNotNull(sb);
            // sb.flush();
            assertTrue(wv.getSession().contains(sb));

            // wv.commit(); //not need to commit here
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testNotnullDefaultValue() {
        wv = getWV();
        try {
            java.util.Map<String, Object> attributes = new HashMap<String, Object>();
            WorkflowItem sb = create(WorkflowItem.class);

            attributes.put(TargetStatus.PROP_NAME, "testNameForStatus");
            attributes.put(TargetStatus.PROP_WORKFLOWITEMS, Collections.singleton(sb));
            TargetStatus os = new TargetStatus(wv, attributes);
            assertNotNull(os);
            // wv.commit(); //not need to commit here
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

}
