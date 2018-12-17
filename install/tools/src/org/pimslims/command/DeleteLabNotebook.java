/**
 * tools org.pimslims.command DeleteLabNotebook.java
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

import javax.persistence.Query;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabNotebook;

/**
 * DeleteLabNotebook
 * 
 */
public class DeleteLabNotebook {

    private static final String JPQL = "DELETE from LabBookEntry as page WHERE page.access=:book";

    public static void main(String[] args) {
        AbstractModel model = ModelImpl.getModel();
        WritableVersion version = null;
        try {
            for (int i = 0; i < args.length; i++) {
                version = model.getWritableVersion(Access.ADMINISTRATOR);
                String name = args[i];
                delete(version, name);
                System.out.println("Deleted all pages from: " + name);
                version.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (null != version && !version.isCompleted())
                version.abort();
        }
        System.out.println("OK: deletions done");
    }

    /**
     * DeleteLabNotebook.delete
     * 
     * @param version
     * @param name
     * @throws AccessException
     * @throws ConstraintException
     */
    static void delete(WritableVersion version, String name) throws AccessException, ConstraintException {
        LabNotebook book = version.findFirst(LabNotebook.class, LabNotebook.PROP_NAME, name);
        if (null == book) {
            throw new IllegalArgumentException("Lab notebook not found: " + name);
        }
        Query query = version.getEntityManager().createQuery(JPQL);
        query.setParameter("book", book);
        query.executeUpdate();
        book.delete();
    }

}
