/**
 * ccp.api.pojo ExperimentTester.java
 * 
 * @date 02-Nov-2006 08:58:07
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

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.reference.ExperimentType;

/**
 * ExperimentTester
 * 
 */
public class ExperimentTester extends org.pimslims.test.AbstractTestCase {
    String expHook = null, parHook = null;

    private static final Calendar NOW = Calendar.getInstance();

    public void testValueConstraint() throws AccessException, AbortedException {
        wv = getWV();
        try {
            final Experiment exp = create(Experiment.class);
            exp.setStatus("bad");
            wv.commit();
            fail("Set invalid status");
        } catch (final ConstraintException e) {
            // that's right
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testDeletionConstraint() throws AccessException, AbortedException, ConstraintException {
        wv = getWV();
        String etHook = null;
        try {
            final Experiment exp = create(Experiment.class);
            etHook = exp.getExperimentType().get_Hook();
            wv.commit();

        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        wv = getWV();
        try {
            final ExperimentType et = wv.get(etHook);
            assertNotNull(et);
            et.delete();
            wv.commit();
            fail("Deleted exp type, but has experiment");
        } catch (final ConstraintException e) {
            // that's right
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testDeleteParameter() { // create experiment and Parameter
        wv = getWV();
        try {
            final Experiment exp = create(Experiment.class);
            expHook = exp.get_Hook();
            final Parameter par = create(Parameter.class, Parameter.PROP_EXPERIMENT, exp);
            parHook = par.get_Hook();
            assertEquals(exp.getParameters().size(), 1);
            assertTrue(exp.getParameters().contains(par));

            assertEquals(par.getExperiment(), exp);

            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        // load and delete and check
        wv = getWV();
        try {
            final Experiment exp = (Experiment) wv.get(expHook);
            final Parameter par = (Parameter) wv.get(parHook);
            par.delete();
            assertEquals(exp.getParameters().size(), 0);
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        // load and check again
        wv = getWV();
        try {
            final Experiment exp = (Experiment) wv.get(expHook);
            assertNotNull(exp);
            assertEquals(exp.getParameters().size(), 0);
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void _testCCPNFindNone() {
        final WritableVersion wv = getWV();
        try {
            final ExperimentType type = new ExperimentType(wv, "test");
            final HashMap<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(ExperimentType.PROP_NAME, "test");
            final Collection none = wv.findAll(ExperimentType.class, criteria);
            // TODO no match expected but was 1 and don't know why after removing core.Project
            assertEquals("no match", 0, none.size());
            type.delete(); // clean up
        } catch (final Throwable ex) {
            Throwable cause = ex;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            cause.printStackTrace();
            fail(cause.getClass().getName() + ": " + cause.getMessage());
        } finally {
            wv.abort(); // not testing persistence here
        }

    }

    public void testInputSampleList() {
        String experimentHook = null;
        wv = getWV();
        try {
            InputSample inSample1 = create(InputSample.class);
            inSample1.setName("InputSample1");
            InputSample inSample2 = create(InputSample.class);
            inSample2.setName("InputSample2");
            InputSample inSample3 = create(InputSample.class);
            inSample3.setName("InputSample3");
            Experiment exp = create(Experiment.class);
            experimentHook = exp.get_Hook();

            List<InputSample> inputSamples = new LinkedList<InputSample>();
            inputSamples.add(inSample1);
            inputSamples.add(inSample2);
            exp.setInputSamples(inputSamples);
            wv.flush();
            wv.getSession().refresh(exp);
            InputSample inputSampleNext = exp.getInputSamples().iterator().next();
            assertEquals(2, exp.getInputSamples().size());
            assertEquals(inputSampleNext.get_Hook(), inputSampleNext.get_Hook());

            inSample3.setExperiment(exp);
            assertEquals(3, exp.getInputSamples().size());
            assertEquals(inputSampleNext.get_Hook(), inputSampleNext.get_Hook());
            wv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        rv = getRV();
        try {
            rv.getSession().clear();
            Experiment exp = rv.get(experimentHook);
            assertEquals(3, exp.getInputSamples().size());
            Iterator<InputSample> it = exp.getInputSamples().iterator();
            InputSample inputSampleNext = it.next();
            assertEquals("InputSample1", inputSampleNext.getName());
            inputSampleNext = it.next();
            assertEquals("InputSample2", inputSampleNext.getName());
            inputSampleNext = it.next();
            assertEquals("InputSample3", inputSampleNext.getName());

        } finally {
            if (!rv.isCompleted())
                rv.abort();
        }
    }

    public void testCreateInputSample() throws ConstraintException {
        this.wv = this.getWV();
        try {
            ExperimentType type = new ExperimentType(wv, UNIQUE);
            Experiment experiment = new Experiment(wv, UNIQUE, NOW, NOW, type);
            new InputSample(wv, experiment);
            wv.flush();
        } finally {
            wv.abort();
        }
    }

}
