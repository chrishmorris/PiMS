/**
 * tools org.pimslims.command DeleteLabNotebookTest.java
 * 
 * @author cm65
 * @date 4 Jun 2013
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.command;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.people.Organisation;

/**
 * DeleteLabNotebookTest
 * 
 */
public class DeleteLabNotebookTest extends TestCase {

    private static final String UNIQUE = "dln" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for DeleteLabNotebookTest
     * 
     * @param name
     */
    public DeleteLabNotebookTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testNonesuch() throws AccessException, ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            DeleteLabNotebook.delete(version, "nonesuch");
        } catch (IllegalArgumentException e) {
            // that's fine
        } finally {
            version.abort();
        }
    }

    public void testEmpty() throws AccessException, ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            new LabNotebook(version, UNIQUE);
            DeleteLabNotebook.delete(version, UNIQUE);
        } catch (IllegalArgumentException e) {
            // that's fine
        } finally {
            version.abort();
        }
    }

    public void testOne() throws AccessException, ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            LabNotebook book = new LabNotebook(version, UNIQUE);
            new Organisation(version, UNIQUE).setAccess(book);
            DeleteLabNotebook.delete(version, UNIQUE);
        } catch (IllegalArgumentException e) {
            // that's fine
        } finally {
            version.abort();
        }
    }

}
