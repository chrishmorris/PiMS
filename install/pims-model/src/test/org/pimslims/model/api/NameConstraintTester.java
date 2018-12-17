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

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.ExperimentType;

/**
 * ChemElementTester
 * 
 */
public class NameConstraintTester extends org.pimslims.test.AbstractTestCase {

    WritableVersion wv;

    private static final java.util.Calendar NOW = java.util.Calendar.getInstance();

    public void testChemElementNameConstraint() {

        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            ExperimentType expType = new ExperimentType(wv, "tests");
            new Experiment(wv, "Exp test name", NOW, NOW, expType);

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
