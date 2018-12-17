/**
 * org.pimslims.generatedApi.role ManyToOneTester.java
 * 
 * @date 21-Sep-2006 13:06:21
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 * Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 * Copyright (c) 2006
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 * 
 * A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.model.role;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;


/**
 * ManyToOneTester
 * 
 */
public class ManyToOneTester extends org.pimslims.test.AbstractTestCase {
    /**
     * test generated code for bi-direction many to one
     * 
     */

    public void testBi_Mto1() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            Molecule mc1 = new Molecule(wv, "other", "name1" + new Date());
            Target target = new Target(wv, "commonName" + new Date(),  mc1);
            assertTrue(target.getProtein() == mc1);
            Molecule mc2 = new Molecule(wv, "other", "name2" + new Date());
            mc2.setProteinForTargets(new HashSet<Target>(Collections.singletonList(target)));
            assertTrue(target.getProtein() == mc2);
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

    public void testBi_Mto1_SampleToRefSample() {
        wv = getWV();
        try {
            Sample sample1 = create(Sample.class);
            Sample sample2 = create(Sample.class);

            RefSample refSample1 = create(RefSample.class);
            RefSample refSample2 = create(RefSample.class);
            sample1.setRowPosition(10);
            sample1.setRefSample(refSample1);
            sample2.setRefSample(refSample1);
            assertEquals(refSample1, sample1.getRefSample());
            assertEquals(refSample1, sample2.getRefSample());
            assertEquals(10, sample1.getRowPosition().intValue());
            //refsample1 has 2 samples now
            assertEquals(2, refSample1.getConformings().size());
            assertTrue(refSample1.getConformings().contains(sample1));
            assertTrue(refSample1.getConformings().contains(sample2));

            //move sample1 from refsample1 to refsample2
            sample1.setRefSample(refSample2);

            //refsample1 should only have sample2
            assertFalse(refSample1.getConformings().contains(sample1));
            assertTrue(refSample1.getConformings().contains(sample2));
            //refsample2 should obly have sample1
            assertFalse(refSample2.getConformings().contains(sample2));
            assertTrue(refSample2.getConformings().contains(sample1));

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
