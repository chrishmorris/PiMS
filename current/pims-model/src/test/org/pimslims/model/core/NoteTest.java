/**
 * pims-model org.pimslims.model.core NoteTest.java
 * 
 * @author cm65
 * @date 14 May 2014
 * 
 *       Protein Information Management System
 * @version: 5.1
 * 
 *           Copyright (c) 2014 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.model.core;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.people.Organisation;

/**
 * NoteTest
 * 
 */
public class NoteTest extends TestCase {

    private static final String UNIQUE = "n" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for NoteTest
     * 
     * @param name
     */
    public NoteTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void test() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            LabBookEntry page = new Organisation(version, UNIQUE);
            Note note = new Note(version, page);
            assertTrue(page.getNotes().size() == 1);
            assertEquals(0, note.getRelatedEntries().size());
            note.addRelatedEntry(new Organisation(version, UNIQUE + "b"));
            note.addRelatedEntry(new Organisation(version, UNIQUE + "c"));
            version.flush();
            assertEquals(2, note.getRelatedEntries().size());
        } finally {
            version.abort();
        }
    }
}
