/**
 * ccp.api.pojo ErrorMessageTester.java
 * 
 * @date 25 Apr 2007 13:41:58
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 * Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 * Copyright (c) 2007
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

import org.hibernate.exception.ConstraintViolationException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.DuplicateKeyConstraintException;
import org.pimslims.model.core.BookCitation;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.test.AbstractTestCase;


public class ExceptionTester extends AbstractTestCase {

    public void testDuplicateKeyConstraint() {
        wv = getWV();
        try {
            new Molecule(wv, "DNA", "test");
            new Molecule(wv, "DNA", "test");
            fail("No exception found");
        } catch (DuplicateKeyConstraintException e) {
            System.out.println(e.getMessage());
            assertTrue(e.getMessage().contains("duplicate key"));
        } catch (ConstraintException ce) {
            ce.printStackTrace();
            fail(ce.getMessage());
        } catch (ConstraintViolationException cve) {
            fail(cve.getMessage());
        } finally {
            wv.abort(); // not testing persistence here
        }
    }

    public void testLineBreakConstraint() throws AccessException {
        wv = getWV();
        try {
            BookCitation bc = create(BookCitation.class);
            bc.setTitle("a" + '\n' + "b");
            fail("No exception found");
        } catch (ConstraintException e) {
            assertTrue(e.getMessage().contains(
                org.pimslims.constraint.ConstraintFactory.CONTAINS_NO_LINEBREAK.toString()));
        } finally {
            wv.abort(); // not testing persistence here
        }
    }

    public void testMustBeOneOfConstraint() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            Experiment exp = create(Experiment.class);
            exp.setStatus("OK");
            exp.setExperimentType((ExperimentType) create(ExperimentType.class));
            try {
                exp.setStatus("O_K");
                fail("No exception found");
            } catch (ConstraintException e) {
                assertTrue(e.getMessage().toUpperCase().contains("MUST_BE_ONE_OF"));
            }
        } finally {
            wv.abort(); // not testing persistence here
        }
    }
}
