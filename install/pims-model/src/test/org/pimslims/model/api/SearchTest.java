/**
 * ccp.api.pojo searchTester.java
 * 
 * @date 24-Nov-2006 14:27:35
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

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.test.POJOFactory;

/**
 * searchTester
 * 
 * HashMap m=new HashMap();
 * 
 * m.put("name", "SPOT Construct Primer Design");
 * 
 * Experiment e = (Experiment)expBlueprint.findFirst(ExpBlueprint.PROP_EXPERIMENTS, Experiment.PROP_PROTOCOL,
 * m);
 * 
 * 
 */
public class SearchTest extends org.pimslims.test.AbstractTestCase {

    public void testSearchinList() {
        // create citation for testing
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        //String hook = null;
        String searchFor = "abc" + new Date();
        try {
            Molecule molComp = POJOFactory.createProtein(wv);

            List<String> keywords = new LinkedList<String>();
            keywords.add(searchFor);
            keywords.add("XYZ" + new Date());
            molComp.setKeywords(keywords);
            //hook = molComp.get_Hook();
            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        ReadableVersion rv = model.getReadableVersion(AbstractModel.SUPERUSER);
        try {
            String selectHQL =
                "select A from " + Molecule.class.getName()
                    + " A where exists (select B from A.keywords B where lower(B) like lower('%" + searchFor
                    + "%')) ";
            org.hibernate.Query hqlQuery = rv.getSession().createQuery(selectHQL).setCacheable(false);
            java.util.Collection<ModelObject> results = hqlQuery.list();

            assertEquals(results.size(), 1);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    public void testFindFirst() {
        String expHook = null, parHook = null, ebHook = null;
        // create experiment and Parameter
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            Experiment exp1 = POJOFactory.createExperiment(wv);
            Experiment exp2 = POJOFactory.createExperiment(wv);
            expHook = exp1.get_Hook();
            Protocol par = POJOFactory.createProtocol(wv);
            parHook = par.get_Hook();
            exp1.setProtocol(par);
            ResearchObjective eb = POJOFactory.createExpBlueprint(wv);
            eb.addExperiment(exp1);
            eb.addExperiment(exp2);
            ebHook = eb.get_Hook();
            wv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        // find and check adn clean up
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            Experiment exp = (Experiment) wv.get(expHook);
            Protocol par = (Protocol) wv.get(parHook);
            ResearchObjective eb = (ResearchObjective) wv.get(ebHook);

            Experiment e =
                (Experiment) eb.findFirst(ResearchObjective.PROP_EXPERIMENTS, Experiment.PROP_PROTOCOL, par);
            assertEquals(exp.get_Hook(), e.get_Hook());
            assertEquals(1, eb.findAll(ResearchObjective.PROP_EXPERIMENTS, Experiment.PROP_PROTOCOL, par)
                .size());

            wv.delete(eb.getExperiments());
            eb.delete();
            par.delete();
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

    public void testFindFirstInSpotDB() {
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {

            ExperimentType et = null;

            Map<String, Object> m = new HashMap<String, Object>();

            m.put(ExperimentType.PROP_NAME, "SPOT Construct Design");

            et = wv.findFirst(org.pimslims.model.reference.ExperimentType.class, m);
            ExperimentType et2 =
                wv.findFirst(org.pimslims.model.reference.ExperimentType.class, ExperimentType.PROP_NAME,
                    "SPOT Construct Design");

            Collection<ExperimentType> ets = wv.findAll(org.pimslims.model.reference.ExperimentType.class, m);
            Collection<ExperimentType> ets2 =
                wv.findAll(org.pimslims.model.reference.ExperimentType.class, ExperimentType.PROP_NAME,
                    "SPOT Construct Design");

            if (et == null && ets.size() == 0)
                return; // not SpotDB
            assertEquals(et, et2);
            assertTrue(ets.contains(et));
            assertTrue(ets.contains(et2));
            assertTrue(ets2.contains(et));
            Collection<ResearchObjective> ebs = wv.getAll(ResearchObjective.class, 0, 10);

            for (ResearchObjective eb : ebs) {
                Experiment e =
                    (Experiment) eb.findFirst(ResearchObjective.PROP_EXPERIMENTS,
                        Experiment.PROP_EXPERIMENTTYPE, et);
                if (e != null) {
                    // System.out.println("Experiment name: "+e.get_Hook());
                    assertTrue(e.getExperimentType().get_Hook().equals(et.get_Hook()));

                }
            }
            // wv.commit();
            // } catch (ModelException ex) {
            // ex.printStackTrace();
            // fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }
}
