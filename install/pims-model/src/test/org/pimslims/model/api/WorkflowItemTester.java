/**
 * ccp.api.pojo ExperimentTester.java
 * 
 * @date 02-Nov-2006 08:58:07
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

import org.pimslims.exception.ModelException;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.reference.WorkflowItem;


/**
 * ExperimentTester
 * 
 */
public class WorkflowItemTester extends org.pimslims.test.AbstractTestCase {
    String expTypeHook = null, statusHook = null, workflowHook = null;

    public void testCreadAndReadWorkflowItem() { // create
        // WorkflowItem,ExperimentType
        // and Status
        wv = getWV();
        try {
            ExperimentType expType = create(ExperimentType.class);
            TargetStatus status = create(TargetStatus.class);
            WorkflowItem workflow = create(WorkflowItem.class, WorkflowItem.PROP_EXPERIMENTTYPE, expType);
            workflow.setStatus(status);
            expTypeHook = expType.get_Hook();
            statusHook = status.get_Hook();
            workflowHook = workflow.get_Hook();

            assertEquals(1, status.getWorkflowItems().size());
            assertEquals(1, expType.getWorkflowItems().size());
            assertTrue(status.getWorkflowItems().contains(workflow));
            assertTrue(expType.getWorkflowItems().contains(workflow));
            wv.commit();
        } catch (ModelException ex) {

            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        // load WorkflowItem,ExperimentType and Status
        wv = getWV();
        try {
            ExperimentType expType = wv.get(expTypeHook);
            assertEquals(1, expType.getWorkflowItems().size());
            TargetStatus status = wv.get(statusHook);
            assertEquals(1, status.getWorkflowItems().size());

            WorkflowItem workflow = wv.get(workflowHook);

            assertTrue(status.getWorkflowItems().contains(workflow));
            assertTrue(expType.getWorkflowItems().contains(workflow));

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
