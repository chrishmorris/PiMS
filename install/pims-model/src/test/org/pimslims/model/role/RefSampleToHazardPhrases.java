/**
 * org.pimslims.generatedApi.role RefSampleToHazardPhrases.java
 * 
 * @date 20-Oct-2006 08:53:54
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

import java.util.Collections;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.reference.HazardPhrase;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.RefSample;
import org.pimslims.test.POJOFactory;

/**
 * RefSampleToHazardPhrases Test association between the Hazard phrases and the refSample
 * 
 */
public class RefSampleToHazardPhrases extends org.pimslims.test.AbstractTestCase {

    String HPHook = null, RSHook = null;

    public void testCreateAssociation() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            RefSample rs = (RefSample) wv.get(this.RSHook);
            HazardPhrase hp = (HazardPhrase) wv.get(this.HPHook);
            // create the Association
            rs.setHazardPhrases(Collections.singleton(hp));

            // make sure association is correct
            assertEquals(1, rs.getHazardPhrases().size());
            assertTrue(rs.getHazardPhrases().contains(hp));
            wv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {

            RefSample rs = (RefSample) wv.get(RSHook);
            // make sure association is correct
            assertEquals(1, rs.getHazardPhrases().size());
            ModelObject hp = rs.findFirst(AbstractSample.PROP_HAZARDPHRASES);
            assertEquals(((HazardPhrase) hp).get_Hook(), HPHook);

            rs.delete();
            hp.delete();
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

    @Override
    protected void setUp() throws Exception {

        super.setUp();
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            // create new objects for tesing
            RefSample rs = POJOFactory.createRefSample(wv);
            HazardPhrase hp = POJOFactory.createHazardPhrase(wv);
            this.RSHook = rs.get_Hook();
            this.HPHook = hp.get_Hook();
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

}
