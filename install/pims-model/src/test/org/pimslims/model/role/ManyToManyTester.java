/**
 * GeneratedAPI.relationship Many_ManyTester.java
 * 
 * @date 13-Sep-2006 13:00:32
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
package org.pimslims.model.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.api.PersonTester;
import org.pimslims.model.core.BookCitation;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;
import org.pimslims.model.target.TargetGroup;
import org.pimslims.search.Searcher;
import org.pimslims.test.POJOFactory;

/**
 * Many_ManyTester
 * 
 */

public class ManyToManyTester extends org.pimslims.test.AbstractTestCase {

    public void testSearch() throws AccessException, ConstraintException {
        wv = getWV();
        try {

            create(Sample.class);

            //create a SampleCategory with 2 samples
            SampleCategory sc1 = create(SampleCategory.class);
            Sample sample11 = create(Sample.class);
            sample11.setSampleCategories(Collections.singleton(sc1));
            sample11.setIsActive(true);
            Sample sample12 = create(Sample.class);
            sample12.setSampleCategories(Collections.singleton(sc1));

            //create a SampleCategory with 1 samples
            SampleCategory sc2 = create(SampleCategory.class);
            Sample sample21 = create(Sample.class);
            sample21.setSampleCategories(Collections.singleton(sc2));
            //add this sample into previous SampleCategory
            sample21.addSampleCategory(sc1);

            //search sample by SampleCategory2
            Collection<Sample> results = wv.findAll(Sample.class, AbstractSample.PROP_SAMPLECATEGORIES, sc2);
            assertEquals("Can not find sample", 1, results.size());
            assertTrue(results.contains(sample21));

            //search sample by SampleCategory1
            results = wv.findAll(Sample.class, AbstractSample.PROP_SAMPLECATEGORIES, sc1);
            assertEquals("Can not find sample", 3, results.size());
            assertTrue(results.contains(sample21));
            assertTrue(results.contains(sample11));
            assertTrue(results.contains(sample12));

            HashMap<String, Object> criteria = new HashMap<String, Object>();
            //test searcher
            criteria.put(AbstractSample.PROP_ISACTIVE, true);
            criteria.put(AbstractSample.PROP_SAMPLECATEGORIES, sc1);

            Searcher s = new Searcher(wv);
            MetaClass metaClass = wv.getMetaClass(AbstractSample.class);

            Collection<LabNotebook> readableLabNotebooks = wv.getReadableLabNotebooks();
            assertNotNull(readableLabNotebooks);
            for (Iterator iterator = readableLabNotebooks.iterator(); iterator.hasNext();) {
                LabNotebook labNotebook = (LabNotebook) iterator.next();
                assertNotNull(labNotebook);
            }

            ArrayList<ModelObject> results2 = s.search(criteria, metaClass);
            assertEquals("Can not find sample", 1, results2.size());
            assertTrue(results2.contains(sample11));

        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testDelete() {

        int oldSize = 0;
        String targetHook = null;
        String projectHook = null;
        wv = getWV();
        try {// create a new target
            org.pimslims.model.target.TargetGroup tp = create(org.pimslims.model.target.TargetGroup.class);
            projectHook = tp.get_Hook();
            Target newTarget = create(Target.class);
            newTarget.addTargetGroup(tp);
            targetHook = newTarget.get_Hook();
            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            // test by call the adder
            org.pimslims.model.target.TargetGroup tp =
                (org.pimslims.model.target.TargetGroup) (wv.get(projectHook));
            oldSize = tp.getTargets().size();
            org.pimslims.model.target.Target target = (Target) wv.get(targetHook);
            assertTrue(tp.getTargets().contains(target));
            target.delete();
            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        try {
            org.pimslims.model.target.TargetGroup tp =
                (org.pimslims.model.target.TargetGroup) (wv.get(projectHook));
            assertEquals(oldSize - 1,
                tp.get_MetaClass().getMetaRole(org.pimslims.model.target.Project.PROP_TARGETS).get(tp).size());
            tp.delete();
            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testAdder() {
        wv = getWV();
        //String citationHook = null;
        try {
            // test by call the adder
            org.pimslims.model.people.Person person =
                (org.pimslims.model.people.Person) wv.create(org.pimslims.model.people.Person.class,
                    PersonTester.ATTRIBUTES);
            BookCitation citation = create(BookCitation.class);

            person.addAttachment(citation);

            assertEquals("added", 1, person.getAttachments().size());

            // not need to wv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testConstructor() {
        wv = getWV();
        try {
            Target target = POJOFactory.createTarget(wv);
            TargetGroup group = new TargetGroup(wv, UNIQUE);
            target.addTargetGroup(group);

            assertEquals("added", 1, target.getTargetGroups().size());
            assertEquals("added in reverse", 1, group.getTargets().size());

        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testMandatoryConstructor() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        //String citationHook = null;
        try {
            // test by call the constructor and it is a required argument
            Map scAttribute = new HashMap();
            scAttribute.put(SampleCategory.PROP_NAME, "myScName" + new Date());
            SampleCategory sc = new SampleCategory(wv, scAttribute);

            Map sampleAttribute = new HashMap();
            sampleAttribute.put(AbstractSample.PROP_NAME, "mySampleName" + new Date());
            sampleAttribute.put(AbstractSample.PROP_SAMPLECATEGORIES, Collections.singleton(sc));
            Sample sample = new Sample(wv, sampleAttribute);// create sample on
            // sc
            assertEquals("added", 1, sample.getSampleCategories().size());
            assertEquals("added in reverse", 1, sc.getAbstractSamples().size());

            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testSaveAndReload() {
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        String targetHook = null;
        String projectHook = null;
        try {
            // test by call the adder
            Target target = POJOFactory.createTarget(wv);
            TargetGroup project = POJOFactory.createTargetGroup(wv);
            target.addTargetGroup(project);

            assertEquals("added", 1, target.getTargetGroups().size());
            assertEquals("added in reverse", 1, project.getTargets().size());

            targetHook = target.get_Hook();
            projectHook = project.get_Hook();
            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        // test it is saved and reload correctly
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            Target target = (Target) wv.get(targetHook);
            TargetGroup project = (TargetGroup) wv.get(projectHook);
            assertEquals("persistence of target", 1, target.getTargetGroups().size());
            assertEquals("persistence of project", 1, project.getTargets().size());

            // now test remove
            target.removeTargetGroup(project);
            assertEquals("removed", 0, target.getTargetGroups().size());
            assertEquals("removed in reverse", 0, project.getTargets().size());

            wv.delete(target);
            wv.delete(project);

            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }

}
