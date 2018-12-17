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

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.target.Target;
import org.pimslims.test.POJOFactory;

/**
 * TargetTest
 * 
 */
public class TargetTester extends org.pimslims.test.AbstractTestCase {

    public void testDerivedSeqString() {
        MetaClass targetMC = model.getMetaClass(Target.class.getName());
        MetaAttribute tseq = targetMC.getAttribute("seqString");
        assertTrue(tseq.isDerived());
    }

    public void testDerivedProteinName() {
        WritableVersion version = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            Target target = POJOFactory.createTarget(version);
            assertEquals(target.getProtein().getName(), target.getProteinName());
        } catch (ModelException ex) {
            Throwable cause = ex;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            cause.printStackTrace();
            fail(cause.getClass().getName() + ": " + cause.getMessage());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

}
