/**
 * ccp.api.pojo Performancetest.java
 * 
 * @date 02-Jan-2007 12:39:29
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

import java.util.Map;

import org.hibernate.stat.Statistics;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.persistence.HibernateUtilTester;
import org.pimslims.test.POJOFactory;

/**
 * Performancetest
 * 
 */
public class PerformanceTester extends org.pimslims.test.AbstractTestCase {

    public void testNoUpdate() {
        Statistics stat = HibernateUtilTester.getStatistics(model);

        WritableVersion wv = getWV();
        try {
            long numberofStatement1 = stat.getEntityUpdateCount();
            Experiment experiment = POJOFactory.createExperiment(wv);
            Sample sample = POJOFactory.createSample(wv);
            RefInputSample refSample = POJOFactory.createRefInputSample(wv);
            Map<String, Object> newInputSampleAttributes = new java.util.HashMap<String, Object>();
            newInputSampleAttributes.put(InputSample.PROP_SAMPLE, sample);
            newInputSampleAttributes.put(InputSample.PROP_REFINPUTSAMPLE, refSample);
            newInputSampleAttributes.put(InputSample.PROP_EXPERIMENT, experiment);
            new InputSample(wv, newInputSampleAttributes);

            /*
             * Molecule molecule=POJOFactory.createMolecule(wv); NaturalSource
             * ns=POJOFactory.createNaturalSource(wv); Map<String, Object> attributes = new java.util.HashMap<String,
             * Object>(); attributes.put(MolComponent.PROP_MOLECULE,molecule);
             * attributes.put(MolComponent.PROP_NATURALSOURCE,ns);
             * attributes.put(MolComponent.PROP_NAME,"name"+System.currentTimeMillis());
             * attributes.put(MolComponent.PROP_MOLTYPE,"type"+System.currentTimeMillis()); MolComponent bc =
             * new MolComponent(wv,attributes);
             */

            wv.flush();
            long numberofStatement2 = stat.getEntityUpdateCount();
            assertEquals(numberofStatement1, numberofStatement2);

        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

}
