/**
 * org.pimslims.generatedApi.role RefSampleToHazardPhrases.java
 * 
 * @date 20-Oct-2006 08:53:54
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

import java.util.Collections;

import org.pimslims.dao.ModelImpl;
import org.pimslims.exception.ModelException;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.BookCitation;
import org.pimslims.model.core.Citation;
import org.pimslims.model.target.Target;

/**
 * RefSampleToHazardPhrases Test association between the Hazard phrases and the refSample
 * 
 */
public class CitationTester extends org.pimslims.test.AbstractTestCase {

    public void testCitation() {
        wv = getWV();
        String targetHook = null;
        String citationTitle = "book: testing";
        try {
            //ModelObject mo = wv.get(new Long(335313));
            //System.out.println(mo);

            Target t = create(Target.class);
            targetHook = t.get_Hook();

            BookCitation bc = create(BookCitation.class);
            bc.setBookTitle(citationTitle);

            t.setAttachments(Collections.singleton(((Attachment) bc)));

            wv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        ((ModelImpl) model).evict(Target.class);
        ((ModelImpl) model).evict(BookCitation.class);
        ((ModelImpl) model).evict(Citation.class);
        rv = getRV();
        try {
            Target t = rv.get(targetHook);
            assertNotNull(t);
            Citation citation = (Citation) t.getAttachments().iterator().next();
            assertNotNull(citation);
            BookCitation bc = (BookCitation) citation;
            assertEquals(citationTitle, bc.getBookTitle());
            rv.commit();

        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

}
