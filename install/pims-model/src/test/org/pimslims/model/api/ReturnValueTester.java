/**
 * org.pimslims.generatedApi.role RefSampleToHazardPhrases.java
 * 
 * @date 20-Oct-2006 08:53:54
 * 
 * @author Bill Lin
 * 
 *         Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 2.0
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

import org.pimslims.exception.ModelException;
import org.pimslims.model.target.Target;

/**
 * RefSampleToHazardPhrases Test association between the Hazard phrases and the refSample
 * 
 */
public class ReturnValueTester extends org.pimslims.test.AbstractTestCase {

    public void testEmptyCollection() {
        wv = getWV();
        try {
            Target t = create(Target.class);
            t.setAliases(null);
            assertTrue(t.getAliases() != null);
            t.setDetails(null);
            assertTrue(t.getDetails().equals(""));

            t.setMilestones(null);
            assertTrue(t.getMilestones() != null);
            t.setResearchObjectiveElements(null);
            assertTrue(t.getResearchObjectiveElements() != null);
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
