/**
 * ccp.api.role OneWayTester.java
 * 
 * @date 08-Nov-2006 15:55:01
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

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.test.POJOFactory;

/**
 * OneWayTester
 * 
 */
public class OneWayTester extends org.pimslims.test.AbstractTestCase {
    public void testInstanceOf() {
        String scHook = null;
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            RefSample rs = POJOFactory.createRefSample(wv);
            Molecule mc = POJOFactory.createProtein(wv);
            SampleComponent sc = POJOFactory.createSampleComponent(wv, mc, rs);
            scHook = sc.get_Hook();
            wv.commit(); // not need to commit here
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
            SampleComponent sc = (SampleComponent) wv.get(scHook);
            AbstractSample as = sc.getAbstractSample();
            as = (AbstractSample) wv.getSession().load(RefSample.class, as.getDbId());
            assertTrue(as instanceof RefSample);
            // assertTrue(sc.getRefComponent() instanceof MolComponent);
            wv.commit(); // not need to commit here
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
