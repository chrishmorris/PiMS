/**
 * org.pimslims.generatedApi.role RefSampleToHazardPhrases.java
 * 
 * @date 20-Oct-2006 08:53:54
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
package org.pimslims.model.api;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.pimslims.exception.ModelException;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;

/**
 * RefSampleToHazardPhrases Test association between the Hazard phrases and the refSample
 * 
 */
public class ProtocolTester extends org.pimslims.test.AbstractTestCase {

    public void testParameterDefinitionList() {
        String protocolhook = null;
        wv = getWV();
        try {
            ParameterDefinition pd3 = create(ParameterDefinition.class);
            pd3.setLabel("3");
            ParameterDefinition pd1 = create(ParameterDefinition.class);
            pd1.setLabel("1");
            ParameterDefinition pd2 = create(ParameterDefinition.class);
            pd2.setLabel("2");
            Protocol protocol = create(Protocol.class);
            protocolhook = protocol.get_Hook();
            List<ParameterDefinition> parameterDefinitions = new LinkedList<ParameterDefinition>();
            parameterDefinitions.add(pd1);
            parameterDefinitions.add(pd2);
            protocol.setParameterDefinitions(parameterDefinitions);
            wv.flush();
            wv.getSession().refresh(protocol);
            ParameterDefinition pdNext = protocol.getParameterDefinitions().iterator().next();
            assertEquals(2, protocol.getParameterDefinitions().size());
            assertEquals(pd1.get_Hook(), pdNext.get_Hook());

            pd3.setProtocol(protocol);
            assertEquals(3, protocol.getParameterDefinitions().size());
            assertEquals(pd1.get_Hook(), pdNext.get_Hook());
            wv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        rv = getRV();
        try {
            rv.getSession().clear();
            Protocol protocol = rv.get(protocolhook);
            assertEquals(3, protocol.getParameterDefinitions().size());
            Iterator<ParameterDefinition> it = protocol.getParameterDefinitions().iterator();
            ParameterDefinition pdNext = it.next();
            assertEquals("1", pdNext.getLabel());
            pdNext = it.next();
            assertEquals("2", pdNext.getLabel());
            pdNext = it.next();
            assertEquals("3", pdNext.getLabel());

        } finally {
            if (!rv.isCompleted())
                rv.abort();
        }
    }

    public void testResultParameterDefinition() {
        wv = getWV();
        try {
            ParameterDefinition pd = create(ParameterDefinition.class);
            pd.setIsResult(Boolean.TRUE);
            Protocol protocol = create(Protocol.class);
            List<ParameterDefinition> parameterDefinitions = new LinkedList<ParameterDefinition>();
            parameterDefinitions.add(pd);
            protocol.setParameterDefinitions(parameterDefinitions);

            ParameterDefinition pdNext = protocol.getResultParamDefinitions().iterator().next();
            assertEquals(1, protocol.getResultParamDefinitions().size());
            assertEquals(pd.get_Hook(), pdNext.get_Hook());
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            wv.abort(); // not testing persistence here
        }
    }

    public void testSetupParameterDefinition() {
        wv = getWV();
        try {
            ParameterDefinition pd = create(ParameterDefinition.class);
            pd.setIsResult(Boolean.FALSE);
            Protocol protocol = create(Protocol.class);
            List<ParameterDefinition> parameterDefinitions = new LinkedList<ParameterDefinition>();
            parameterDefinitions.add(pd);
            protocol.setParameterDefinitions(parameterDefinitions);

            ParameterDefinition pdNext = protocol.getSetupParamDefinitions().iterator().next();
            assertEquals(1, protocol.getSetupParamDefinitions().size());
            assertEquals(pd.get_Hook(), pdNext.get_Hook());
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            wv.abort(); // not testing persistence here
        }
    }
}
