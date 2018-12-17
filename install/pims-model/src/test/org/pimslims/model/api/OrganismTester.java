/**
 * pims-model org.pimslims.model.api OrganismTester.java
 * 
 * @author pajanne
 * @date Jun 9, 2009
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.model.api;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.DuplicateKeyConstraintException;
import org.pimslims.model.reference.Organism;

/**
 * OrganismTester
 * 
 */
public class OrganismTester extends org.pimslims.test.AbstractTestCase {

    public void testUniqueNameConstraint() {
        WritableVersion version = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            new Organism(version, "organismName");
            new Organism(version, "organismName");
            fail("No exception found");
        } catch (DuplicateKeyConstraintException e) {
            System.out.println(e.getMessage());
            assertTrue(e.getMessage().contains("duplicate key"));
        } catch (ConstraintException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence here
        }
    }
}
