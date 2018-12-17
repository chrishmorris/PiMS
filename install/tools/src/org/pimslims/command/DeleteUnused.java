/**
 * tools org.pimslims.command DeleteUnused.java
 * 
 * @author cm65
 * @date 5 Jun 2013
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.command;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;

/**
 * DeleteUnused
 * 
 */
public class DeleteUnused {

    public static void main(String[] args) {
        AbstractModel model = ModelImpl.getModel();
        WritableVersion version = null;
        try {

            version = model.getWritableVersion(Access.ADMINISTRATOR);
            EntityManager entityManager = version.getEntityManager();

            // note that this may discard information about complexes
            System.out.println("Deleting constructs with no experiments");
            update("DELETE from ResearchObjective as ro WHERE ro.experiments is empty", entityManager);

            System.out.println("Deleting targets with no constructs");
            update("DELETE from Target as t WHERE t.researchobjectiveelements is empty", entityManager);

            System.out.println("Deleting empty target groups");
            update("DELETE from TargetGroup as group WHERE group.targets is empty", entityManager);

            System.out.println("Deleting empty lab notebooks");
            update(
                "DELETE from LabNotebook as book WHERE (select LabBookEntry as page where page.access=book) is empty",
                entityManager);

            System.out.println("Deleting user groups with no permissions");
            update("DELETE from UserGroup as group WHERE group.permissions is empty", entityManager);
            System.out.println("Deleting empty user groups");
            update("DELETE from UserGroup as group WHERE group.memberusers is empty", entityManager);
            System.out.println("Deleting users with no permissions");
            update("DELETE from User as user WHERE user.usergroups is empty", entityManager);
            version.commit();
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
     * DeleteUnused.update
     * 
     * @param jpql
     * @param entityManager
     */
    private static void update(String jpql, EntityManager entityManager) {
        Query query = entityManager.createQuery(jpql);
        query.executeUpdate();
    }

}
