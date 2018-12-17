/**
 * ccp.api.pojo MRUTester.java
 * 
 * @date 07-Feb-2007 07:33:39
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 *         Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 *           Copyright (c) 2007
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

import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * MRUTester
 * 
 */
public class MRUTester extends AbstractTestCase {
    String hook = null;

    String hook2 = null;

    long id1 = -1;

    long id2 = -1;

    public void testLoadByID() {

        wv = getWV();
        // create an object
        try {
            Experiment exp = POJOFactory.createExperiment(wv);
            Experiment exp2 = POJOFactory.createExperiment(wv);
            hook = exp.get_Hook();
            hook2 = exp2.get_Hook();
            id1 = exp.getDbId();
            id2 = exp2.getDbId();
            wv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        // load by dbid
        rv = getRV();

        try {
            ModelObject exp = rv.get(id1);
            assertEquals(hook, exp.get_Hook());
            ModelObject exp2 = rv.get(hook2);
            assertEquals(hook2, exp2.get_Hook());

            rv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    public void testPerformanceloadByID() {
        // load by dbid
        rv = getRV();

        try {
            rv.get(id1);

            rv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    public void testPerformanceloadByIDAndClass() {
        // load by dbid
        rv = getRV();

        try {
            rv.get(Experiment.class, id2);

            rv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }
}
