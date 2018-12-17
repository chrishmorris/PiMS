/**
 * GeneratedAPI.relationship One_OneTester.java
 * 
 * @date 13-Sep-2006 13:06:30
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

import org.hibernate.stat.Statistics;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.persistence.HibernateUtilTester;
import org.pimslims.test.POJOFactory;

/**
 * One_OneTester
 * 
 */
public class OneToOneTester extends org.pimslims.test.AbstractTestCase {
    /**
     * test generated code for bi-direction many to many
     * 
     * @throws ConstraintException
     * @throws AccessException
     * @throws AbortedException
     * 
     */

    public void testBi_1to1()

    {
        String osHook = null;
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            Experiment exp = POJOFactory.createExperiment(wv);
            POJOFactory.createOutputSample(wv, exp);

            OutputSample os = POJOFactory.createOutputSample(wv, exp);
            OutputSample os2 = POJOFactory.createOutputSample(wv, exp);

            Sample sample = POJOFactory.createSample(wv);

            os.setSample(sample);
            assertEquals(os, sample.getOutputSample());
            assertEquals(sample, os.getSample());
            os2.setSample(sample);
            assertEquals(os2, sample.getOutputSample());
            // as a 1 to 1 relationship, old os's sample should be null, as this
            // sample is in use with 2nd os
            assertTrue("previous os should not use sample any more! ", sample != os.getSample());

            //sampleHook = sample.get_Hook();
            osHook = os2.get_Hook();
            wv.flush();

            assertEquals(osHook, sample.getOutputSample().get_Hook());
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

    public void testBi_1to1SingleUpdate() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {

            Experiment exp = POJOFactory.createExperiment(wv);
            OutputSample os = POJOFactory.createOutputSample(wv, exp);
            Sample sample = POJOFactory.createSample(wv);
            wv.flush();
            Statistics stats = HibernateUtilTester.getStatistics(model);
            long beforeSave = stats.getCloseStatementCount();
            sample.setOutputSample(os);
            assertEquals(os.getSample(), sample);
            wv.flush();
            assertEquals(beforeSave + 2, stats.getCloseStatementCount());
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
