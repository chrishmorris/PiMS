/**
 * org.pimslims.upgrader DateFixerTest.java
 * 
 * @date 21-Dec-2006 10:59:11
 * 
 * @author Bill
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
package org.pimslims.upgrader;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.test.POJOFactory;

@Deprecated
// these tests can exhaust memory on a large database
public class DataFixerTest extends org.pimslims.test.AbstractTestCase {

    public void testCurrentMileStoneIsValid() {
        if (!DataFixer.CheckAndfixMileStoneDate())
            log("Invalid milestone found and fixed!");
        else
            log("All milestones are correct!");
        assertTrue("Current DB's Milestone data still has problem", DataFixer.CheckAndfixMileStoneDate());
    }

    public void testRemoveDuplicatedSelectMilestone() {
        if (!DataFixer.RemoveDuplicatedSelectMilestone())
            log("Invalid milestone found and fixed!");
        else
            log("All milestone are correct!");
        assertTrue("Current DB's milestone data still has problem",
            DataFixer.RemoveDuplicatedSelectMilestone());
    }

    public void testCheckAndfixMileStoneDate() {
        DataFixer.CheckAndfixMileStoneDate();
        assertTrue("Current DB's Milestone data still has problem", DataFixer.CheckAndfixMileStoneDate());

        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            TargetStatus code = wv.findFirst(TargetStatus.class, TargetStatus.PROP_NAME, "Selected");
            if (code == null)
                code = POJOFactory.createStatus(wv, "Selected");
            Target t = POJOFactory.createTarget(wv);
            Milestone m1 = POJOFactory.createMilestone(wv, t);
            Milestone m2 = POJOFactory.createMilestone(wv, t, code);
            // make true it is later than first one
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis((new Date()).getTime() + 10000);
            m2.setDate(date);
            assertTrue(m1.getTarget() == t);
            assertTrue(m2.getTarget() == t);
            assertTrue(m2.getStatus() == code);
            assertTrue(m2.getDate().after(m1.getDate()));
            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        assertFalse("Did not find the wrong milestone", DataFixer.CheckAndfixMileStoneDate());
        assertTrue("Did not fix the wrong milestone", DataFixer.CheckAndfixMileStoneDate());
    }

    public void xtestCheckAndFixExperiment() {
        DataFixer.CheckAndFixExperiment();
        assertTrue("Current DB's experiment data still has problem", DataFixer.CheckAndFixExperiment());
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            Experiment exp = POJOFactory.createExperiment(wv);
            exp.setStatus(null);
            exp.setDetails("Yes");
            TargetStatus code = wv.findFirst(TargetStatus.class, TargetStatus.PROP_NAME, "Selected");
            Target t = (Target) wv.findFirst(Target.class, Collections.EMPTY_MAP);
            Project expb = (Project) wv.findFirst(ResearchObjective.class, Collections.EMPTY_MAP);
            if (code != null && t != null && expb != null) {
                Milestone milestone = POJOFactory.createMilestone(wv, t, code);
                exp.setProject(expb);
                milestone.setExperiment(exp);
            }
            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        assertFalse("Did not find the wrong Experiment", DataFixer.CheckAndFixExperiment());
        assertTrue("Did not fix the wrong Experiment", DataFixer.CheckAndFixExperiment());

    }

    public void testCheckAndFixExperimentParameters() {
        DataFixer.CheckAndFixExperimentParameters();
        assertTrue("Current DB's experiment Parameters data still has problem",
            DataFixer.CheckAndFixExperimentParameters());
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            Parameter parameter = POJOFactory.createParameter(wv);
            ParameterDefinition pd = POJOFactory.createParameterDefinition(wv);
            pd.setName("test name" + new Date());
            parameter.setParameterDefinition(pd);
            parameter.setName("");
            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        assertFalse("Did not find the wrong Experiment Parameters",
            DataFixer.CheckAndFixExperimentParameters());
        assertTrue("Did not fix the wrong Experiment Parameters", DataFixer.CheckAndFixExperimentParameters());

    }

    /*
     * public void testCheckAndFixExperimentName() { DataFixer.CheckAndFixExperimentName();
     * assertTrue("Current DB's experiment name data still has
     * problem",DataFixer.CheckAndFixExperimentName()); WritableVersion wv =
     * model.getWritableVersion(AbstractModel.SUPERUSER); try { Experiment
     * exp=POJOFactory.createExperiment(wv); exp.setName(""); wv.commit(); } catch (ModelException ex) {
     * fail(ex.toString()); } finally { if (!wv.isCompleted()) {wv.abort();} } assertFalse("Did not find the
     * wrong Experiment name" ,DataFixer.CheckAndFixExperimentName()); assertTrue("Did not fix the wrong
     * Experiment name",DataFixer.CheckAndFixExperimentName()); }
     */
}
