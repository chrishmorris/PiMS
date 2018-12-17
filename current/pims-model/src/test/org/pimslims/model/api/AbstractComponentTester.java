/**
 * org.pimslims.generatedApi.pojo ChemElementTester.java
 * 
 * @date 22-Sep-2006 10:48:00
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

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.sample.SampleComponent;

/**
 * AbstractComponentTester
 * 
 */
public class AbstractComponentTester extends org.pimslims.test.AbstractTestCase {

    /**
     * @param name
     */
    public AbstractComponentTester(String name) {
        super(name);
    }

    public void testChangeableName() throws AccessException, ConstraintException {
        MetaClass MetaClassAC = model.getMetaClass(Substance.class.getName());
        MetaClass MetaClassMC = model.getMetaClass(Molecule.class.getName());
        checkNameIsChangeable(MetaClassAC);
        checkNameIsChangeable(MetaClassMC);
        wv = getWV();
        try {
            Molecule mc = create(Molecule.class);
            mc.setName("new name123");
            wv.flush();
            assertEquals("new name123", mc.getName());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

    /**
     * @param metaClassAC
     */
    private void checkNameIsChangeable(MetaClass metaClassAC) {
        assertTrue(metaClassAC.getAttribute(Substance.PROP_NAME).isChangeable());

    }

    public void testGetRefComponent() throws AccessException, ConstraintException, AbortedException {
        String scHook = null;
        //String primerHook = null;

        // create SampleComponent and primer for testing
        wv = getWV();
        try {
            SampleComponent sc = create(SampleComponent.class);
            scHook = sc.get_Hook();
            Primer primer = create(Primer.class);
            //primerHook = primer.get_Hook();
            sc.setRefComponent(primer);
            wv.commit(); // no need to commit here
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        // verify the way to get primer from SampleComponent
        wv = getWV();
        try {
            SampleComponent sc = wv.get(scHook);
            Substance ac = sc.getRefComponent();
            assertTrue(ac instanceof Primer);

            // now tidy up
            sc.delete();
            ac.delete();
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

}
