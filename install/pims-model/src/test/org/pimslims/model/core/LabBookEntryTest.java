/**
 * pims-model org.pimslims.model.core LabBookEntryTest.java
 * 
 * @author cm65
 * @date 1 Feb 2011
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.model.core;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.people.Organisation;
import org.pimslims.test.AbstractTestCase;

/**
 * LabBookEntryTest
 * 
 */
public class LabBookEntryTest extends AbstractTestCase {

    /**
     * Constructor for LabBookEntryTest
     * 
     * @param name
     */
    public LabBookEntryTest(String name) {
        super(name);
    }

    /**
     * Test method for {@link org.pimslims.model.core.LabBookEntry#setPageNumber(java.lang.String)}. Check
     * attribute added in V4_2
     * 
     * @throws ConstraintException
     */
    public void testSetPageNumber() throws ConstraintException {
        WritableVersion wv = this.getWV();
        try {
            Organisation page = new Organisation(wv, UNIQUE + "name");
            page.setPageNumber(UNIQUE);
            wv.flush();
            Organisation found = wv.get(page.get_Hook());
            assertEquals(UNIQUE, found.getPageNumber());
        } finally {
            wv.abort();
        }
    }

}
