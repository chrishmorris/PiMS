/*
 * Created on 15-Oct-2004
 */
package org.pimslims.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.stat.Statistics;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.AbstractTestReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.api.OrganisationTester;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.persistence.HibernateUtilTester;
import org.pimslims.test.POJOFactory;

/**
 * @author cm65
 * 
 */
public class ReadableVersionImplTester extends AbstractTestReadableVersion {

    private static final String UNIQUE = "rvi" + System.currentTimeMillis();

    public ReadableVersionImplTester() {
        super(ModelImpl.getModel(), "testing implementation of read transaction", Organisation.class
            .getName(), OrganisationTester.ATTRIBUTES);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testCantWrite() {
        // LATER super.testCantWrite();
    }

    /**
     * Those two tests assumed the sequence "test_target" is in the database Assuming that sequence number
     * could only be positive and more then 0
     */
    public void testNextValue() {
        ReadableVersion rv = model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            assertTrue(((ReadableVersionImpl) rv).getNextVal("test_target") != 0);
        } finally {
            rv.abort();
        }
    }

    public void testSetValue() {
        ReadableVersion rv = model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        ((ReadableVersionImpl) rv).setSequenceStart("test_target", 10);
        assertEquals("Value for sequence cannot be set",
            ((ReadableVersionImpl) rv).getNextVal("test_target"), 11);
        rv.abort();
    }

    public void testGetAll() {
        ReadableVersion rv = model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            Statistics stats = HibernateUtilTester.getStatistics(model);
            long beforeSave = stats.getCloseStatementCount();
            rv.getAll(Target.class);
            assertEquals(beforeSave + 1, stats.getCloseStatementCount());
            // wv.commit(); //not need to commit here
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    public void testGetAllFinds() {
        WritableVersion rv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            //Statistics stats = HibernateUtil.getStatistics();
            //long beforeSave = stats.getCloseStatementCount();
            Molecule mc = new Molecule(rv, "other", UNIQUE);
            Collection<Molecule> results = rv.getAll(Molecule.class, 0, 10);
            assertTrue(results.contains(mc));
            // wv.commit(); //not need to commit here
        } catch (ConstraintException e) {
            fail(e.getMessage());
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    public void testPagedGetAll() {
        WritableVersion rv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            Molecule mc = new Molecule(rv, "other", UNIQUE);
            Collection<Molecule> results = rv.getAll(Molecule.class, 0, 10);
            assertTrue(results.contains(mc));
            // wv.commit(); //not need to commit here
        } catch (ConstraintException e) {
            fail(e.getMessage());
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    public void testPagedGetOne() {
        WritableVersion rv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            Molecule mc2 = new Molecule(rv, "other", UNIQUE);
            Molecule mc = new Molecule(rv, "other", UNIQUE + "two");
            Collection<Molecule> results = rv.getAll(Molecule.class, 0, 1);
            assertEquals(1, results.size());
            assertTrue(results.contains(mc));
            results = rv.getAll(Molecule.class, 1, 1);
            assertEquals(1, results.size());
            assertTrue(results.contains(mc2));
            // wv.commit(); //not need to commit here
        } catch (ConstraintException e) {
            fail(e.getMessage());
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    public void testGetAllwithRole() {

        ReadableVersion rv = model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            Statistics stats = HibernateUtilTester.getStatistics(model);
            long beforeSave = stats.getCloseStatementCount();
            List<String> roleNames = new ArrayList<String>();
            roleNames.add(Target.PROP_TARGETGROUPS);
            roleNames.add(Target.PROP_MILESTONES);
            roleNames.add(Target.PROP_TARGETGROUPS);

            Collection<Target> resultsOld = rv.getAll(Target.class);

            Collection<Target> results = rv.getAll(Target.class, roleNames);
            assertEquals(resultsOld.size(), results.size());

            assertEquals(beforeSave + 2, stats.getCloseStatementCount());
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    public void testFindAll() {

        ReadableVersion rv = model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {

            Collection<Target> results = rv.getAll(Target.class, 0, 100);

            for (ModelObject mo : results) {
                Collection<ModelObject> bcs =
                    ((Target) mo).findAll(Target.PROP_RESEARCHOBJECTIVEELEMENTS,
                        ResearchObjectiveElement.PROP_COMPONENTTYPE, "SPOTConstruct");
                // for(ModelObject bc:bcs)
                // System.out.println(((BlueprintComponent)bc).getComponentType());
                int sizeByFindAll = bcs.size();

                int sizeByGet = 0;
                for (ResearchObjectiveElement bc : ((Target) mo).getResearchObjectiveElements()) {
                    if (bc.getComponentType().equalsIgnoreCase("SPOTConstruct"))
                        sizeByGet += 1;
                }
                // if(sizeByGet!=sizeByFindAll)
                // System.err.println("error");

                assertEquals(sizeByGet, sizeByFindAll);
            }

        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    public void _testFindAllWithRole() throws ConstraintException, AbortedException {
        WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        String targetStatusHook = null, targetHook = null;
        try {
            Target t1 = POJOFactory.createTarget(wv);
            Milestone ts = POJOFactory.createMilestone(wv, t1);
            targetStatusHook = ts.get_Hook();
            targetHook = t1.get_Hook();
            assertTrue(t1.getMilestones().contains(ts));
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        ReadableVersion rv = model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            Milestone ts = rv.get(targetStatusHook);
            Target t = rv.get(targetHook);
            Map critials1 = new HashMap();
            critials1.put(Target.PROP_NAME, t.getName());
            critials1.put(Target.PROP_MILESTONES, ts);

            Collection<Target> results1 = rv.findAll(Target.class, critials1);
            assertEquals(1, results1.size());
            assertEquals(targetHook, results1.iterator().next().get_Hook());

            Map critials2 = new HashMap();
            critials2.put(Target.PROP_MILESTONES, ts);

            Collection<Target> results2 = rv.findAll(Target.class, critials2);
            assertEquals(1, results2.size());
            assertEquals(targetHook, results2.iterator().next().get_Hook());

            rv.commit();

        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    public void testGetMataClass() {
        ReadableVersion rv = model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        assertTrue(rv.getMetaClass(Target.class).getJavaClass().getName().equals(Target.class.getName()));
        rv.abort();
    }

    public void testGetNoReadAccessObjects() throws AbortedException, ConstraintException, AccessException {
        WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        String username = "";
        try {
            User user = new User(wv, UNIQUE + "none");
            username = user.getName();
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }

        ReadableVersionImpl rv = (ReadableVersionImpl) model.getReadableVersion(username);
        try {
            //rv.accessController.accessObjectofWORLDWRITABLE = null;
            rv.accessController.resetFirstTimeAccessSearch(false);
            assertNotNull(rv.getReadableLabNotebooks());
            fail("No lab notebooks are readable");
        } catch (IllegalStateException ex) {
            //that's as it should be
        } finally {
            if (!rv.isCompleted())
                rv.abort();
        }
        // now tidy up
        wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            User user = wv.findFirst(User.class, User.PROP_NAME, username);
            user.delete();
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }

    public void testGetReadAccessObjects() throws AbortedException, ConstraintException, AccessException {
        WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        String username = "";
        try {
            UserGroup group = new UserGroup(wv, UNIQUE);
            User user = POJOFactory.createUser(wv);
            username = user.getName();
            group.addMemberUser(user);
            LabNotebook book = new LabNotebook(wv, UNIQUE);
            new Permission(wv, "read", book, group);
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }

        ReadableVersionImpl rv = (ReadableVersionImpl) model.getReadableVersion(username);
        try {
            //rv.accessController.accessObjectofWORLDWRITABLE = null;
            rv.accessController.resetFirstTimeAccessSearch(false);
            Collection<LabNotebook> readableLabNotebooks = rv.getReadableLabNotebooks();
            assertNotNull(readableLabNotebooks);
            assertTrue(readableLabNotebooks.size() != 0);
            for (Iterator iterator = readableLabNotebooks.iterator(); iterator.hasNext();) {
                LabNotebook labNotebook = (LabNotebook) iterator.next();
                assertNotNull(labNotebook);
            } /*
              List<String> cons = new ArrayList<String>();
              cons.add("test");
              String whereHQL = JpqlQuery.getWhereHQL(rv, "A", cons, Target.class);
              assertFalse(whereHQL.contains("()"));

              whereHQL = JpqlQuery.getWhereHQL(rv, "A", cons, ExperimentType.class);
              assertFalse(whereHQL.contains("where  and"));

              whereHQL = JpqlQuery.getWhereHQL(rv, "plate1", Collections.EMPTY_LIST, Image.class);
              assertFalse(whereHQL.endsWith("where ")); */

            assertEquals(username, ((ReadableVersion) rv).getCurrentUser().getName());
        } finally {
            if (!rv.isCompleted())
                rv.abort();
        }
        // now tidy up
        wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            User user = wv.findFirst(User.class, User.PROP_NAME, username);
            Set<UserGroup> groups = user.getUserGroups();
            for (Iterator iterator = groups.iterator(); iterator.hasNext();) {
                UserGroup group = (UserGroup) iterator.next();
                Set<Permission> permissions = group.getPermissions();
                for (Iterator iterator2 = permissions.iterator(); iterator2.hasNext();) {
                    Permission permission = (Permission) iterator2.next();
                    permission.getLabNotebook().delete();
                    permission.delete();
                }
                group.delete();
            }
            user.delete();
            wv.commit();
        } finally {
            if (!wv.isCompleted())
                wv.abort();
        }
    }
}
