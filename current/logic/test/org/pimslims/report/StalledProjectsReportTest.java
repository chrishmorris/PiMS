package org.pimslims.report;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.target.ResearchObjective;

public class StalledProjectsReportTest extends TestCase {

    private static final String UNIQUE = "spr" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private static final Calendar YESTERDAY = Calendar.getInstance();

    private static final Calendar TWO_DAYS_AGO = Calendar.getInstance();

    private static final Calendar TOMORROW = Calendar.getInstance();
    static {
        YESTERDAY.setTimeInMillis(NOW.getTimeInMillis() - 24 * 60 * 60 * 1000);
        TOMORROW.setTimeInMillis(NOW.getTimeInMillis() + 24 * 60 * 60 * 1000);
        TWO_DAYS_AGO.setTimeInMillis(NOW.getTimeInMillis() - 2 * 24 * 60 * 60 * 1000);
    }

    final AbstractModel model;

    /**
     * @param name
     */
    public StalledProjectsReportTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testStalledToPresent() throws ConstraintException {
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            ResearchObjective researchObjective = new ResearchObjective(version, UNIQUE, UNIQUE);
            ExperimentType type = new ExperimentType(version, UNIQUE);
            Experiment experiment = new Experiment(version, UNIQUE, YESTERDAY, YESTERDAY, type);
            experiment.setStatus("Failed");
            experiment.setProject(researchObjective);

            Map<String, Object> experimentCriteria = new HashMap();
            experimentCriteria.put(Experiment.PROP_EXPERIMENTTYPE, type);
            Map<String, Object> projectCriteria =
                Collections.singletonMap(ResearchObjective.PROP_COMMONNAME, (Object) UNIQUE);
            StalledProjectsReport report =
                new StalledProjectsReport(version, TWO_DAYS_AGO, TOMORROW, projectCriteria,
                    experimentCriteria);
            assertEquals(0, report.getResumed().size());
            assertTrue(1 == report.count());
            assertEquals(1, report.getStalled().size());
            Calendar from = report.getStalled().iterator().next();
            assertEquals(YESTERDAY, from);
        } finally {
            version.abort();
        }
    }

    public void testResumed() throws ConstraintException {
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            ResearchObjective researchObjective = new ResearchObjective(version, UNIQUE, UNIQUE);
            ExperimentType type = new ExperimentType(version, UNIQUE);
            Experiment experiment = new Experiment(version, UNIQUE, YESTERDAY, YESTERDAY, type);
            experiment.setStatus("Failed");
            experiment.setProject(researchObjective);

            new Experiment(version, UNIQUE + "r", NOW, NOW, type).setProject(researchObjective);

            Map<String, Object> experimentCriteria = new HashMap();
            experimentCriteria.put(Experiment.PROP_EXPERIMENTTYPE, type);
            Map<String, Object> projectCriteria =
                Collections.singletonMap(ResearchObjective.PROP_COMMONNAME, (Object) UNIQUE);
            StalledProjectsReport report =
                new StalledProjectsReport(version, TWO_DAYS_AGO, TOMORROW, projectCriteria,
                    experimentCriteria);
            assertTrue(1 == report.count());
            assertEquals(1, report.getResumed().size());
            assertEquals(0, report.getStalled().size());
            Calendar from = (Calendar) (report.getResumed().iterator().next()[0]);
            assertEquals(YESTERDAY, from);
        } finally {
            version.abort();
        }
    }

    //TODO test cancelled projects

    public void testProjectFilter() throws ConstraintException {

        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            ResearchObjective researchObjective = new ResearchObjective(version, UNIQUE + "other", UNIQUE);
            ExperimentType type = new ExperimentType(version, UNIQUE);
            Experiment experiment = new Experiment(version, UNIQUE, NOW, NOW, type);
            experiment.setStatus("Failed");
            experiment.setProject(researchObjective);
            new Experiment(version, UNIQUE + "r", NOW, NOW, type).setProject(researchObjective);

            Map<String, Object> experimentCriteria = new HashMap();
            experimentCriteria.put(Experiment.PROP_EXPERIMENTTYPE, type);
            Map<String, Object> projectCriteria =
                Collections.singletonMap(ResearchObjective.PROP_COMMONNAME, (Object) UNIQUE);
            StalledProjectsReport report =
                new StalledProjectsReport(version, YESTERDAY, TOMORROW, projectCriteria, experimentCriteria);
            assertEquals(0, report.getResumed().size());
            assertEquals(0, report.getStalled().size());
            assertEquals(0, report.count());
        } finally {
            version.abort();
        }
    }

    public void testAccess() throws ConstraintException, AbortedException, AccessException {

        String username = UNIQUE;
        // get ready
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            User user = new User(version, username);
            UserGroup group = new UserGroup(version, username);
            group.addMemberUser(user);
            LabNotebook book = new LabNotebook(version, username);
            new Permission(version, "create", book, group);
            new Permission(version, "read", book, group);
            new Permission(version, "update", book, group);
            version.commit();
        } finally {
            if (!version.isCompleted())
                version.abort();
        }
        // test
        version = model.getWritableVersion(username);
        try {
            ResearchObjective researchObjective = new ResearchObjective(version, UNIQUE, UNIQUE);
            ExperimentType type = new ExperimentType(version, UNIQUE);
            Experiment experiment = new Experiment(version, UNIQUE, YESTERDAY, YESTERDAY, type);
            experiment.setStatus("Failed");
            experiment.setProject(researchObjective);

            Map<String, Object> experimentCriteria = new HashMap();
            experimentCriteria.put(Experiment.PROP_EXPERIMENTTYPE, type);
            Map<String, Object> projectCriteria =
                Collections.singletonMap(ResearchObjective.PROP_COMMONNAME, (Object) UNIQUE);
            StalledProjectsReport report =
                new StalledProjectsReport(version, TWO_DAYS_AGO, TOMORROW, projectCriteria,
                    experimentCriteria);
            assertEquals(0, report.getResumed().size());
            assertTrue(1 == report.count());
            assertEquals(1, report.getStalled().size());
            Calendar from = report.getStalled().iterator().next();
            assertEquals(YESTERDAY, from);
        } finally {
            version.abort();
        }

        // tidy up
        version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            User user = version.findFirst(User.class, User.PROP_NAME, username);
            Set<UserGroup> userGroups = user.getUserGroups();
            Collection<LabNotebook> books = new HashSet(1);
            for (Iterator iterator = userGroups.iterator(); iterator.hasNext();) {
                UserGroup userGroup = (UserGroup) iterator.next();
                Set<Permission> permissions = userGroup.getPermissions();
                for (Iterator iterator2 = permissions.iterator(); iterator2.hasNext();) {
                    Permission permission = (Permission) iterator2.next();
                    books.add(permission.getLabNotebook());
                }
            }
            version.delete(userGroups);
            version.delete(books);
            user.delete();
            version.commit();
        } finally {
            if (!version.isCompleted())
                version.abort();
        }
    }
}
