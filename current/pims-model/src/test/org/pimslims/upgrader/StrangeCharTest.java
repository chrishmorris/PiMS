/**
 * org.pimslims.upgrader StrangeCharTest.java
 * 
 * @date 15-Nov-2006 08:43:49
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
package org.pimslims.upgrader;

import java.util.Collection;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.model.target.Target;

/**
 * StrangeCharTest
 * 
 */
public class StrangeCharTest extends org.pimslims.test.AbstractTestCase {
    public void testRemove() {

        char strangeChar = '';
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {// create a new target
            Target t = wv.get(Target.class.getName() + ":" + 13953);
            char s = t.getWhyChosen().charAt(0);
            System.out.println("char:" + s + "=" + (new Character(s)).charValue() + ", type="
                + Character.getType(s));

            Collection<org.pimslims.model.target.Target> ts = wv.getAll(Target.class);
            for (Target t1 : ts)
                if (t1.getWhyChosen().contains("" + strangeChar)) {
                    t1.setWhyChosen(t1.getWhyChosen().replace(strangeChar, ' '));
                    System.out.println(t1.get_Hook() + "'s whyChosen is updated!");
                }

            wv.commit();
        } catch (ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }
}
