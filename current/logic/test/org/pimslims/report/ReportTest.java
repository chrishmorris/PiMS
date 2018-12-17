package org.pimslims.report;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.search.Paging;

public class ReportTest extends TestCase {

    private static final String UNIQUE = "r" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    final AbstractModel model;

    /**
     * @param name
     */
    public ReportTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testNone() {
        WritableVersion version = model.getTestVersion();
        try {
            Report report = new Report(version, Experiment.class, Collections.EMPTY_MAP, "nonesuch");
            assertEquals(0, report.count());
            // assertEquals(0, report.getResults(new Paging(0, 20)).size());
        } finally {
            version.abort();
        }
    }

    public void testLike() throws ConstraintException {
        WritableVersion version = model.getTestVersion();
        try {
            Organisation organisation1 = new Organisation(version, "a" + UNIQUE + "z");
            Organisation organisation2 = new Organisation(version, "b" + UNIQUE + "z");
            version.flush();
            Report<Organisation> report =
                new Report(version, Organisation.class, Collections.EMPTY_MAP, UNIQUE);
            // organisation1.getCreationDate();
            assertEquals(2, report.count());
            Paging paging = new Paging(0, 20, Organisation.PROP_NAME, Paging.Order.ASC);
            List<Organisation> results = report.getResults(paging);
            assertEquals(2, results.size());
            assertEquals(organisation1, results.iterator().next());
            paging = new Paging(0, 20, LabBookEntry.PROP_DBID, Paging.Order.DESC);
            results = report.getResults(paging);
            assertEquals(2, results.size());
            // most recent
            assertEquals(organisation2, results.iterator().next());
        } finally {
            version.abort();
        }
    }

    public void testNoLike() throws ConstraintException {
        WritableVersion version = model.getTestVersion();
        try {
            new Organisation(version, "a" + UNIQUE + "b");
            Organisation organisation = new Organisation(version, UNIQUE);
            Map criteria = new HashMap();
            criteria.put(Organisation.PROP_NAME, UNIQUE);
            Report<Organisation> report = new Report(version, Organisation.class, criteria, null);
            assertEquals("" + organisation.getCreationDate(), 1, report.count());
            List<Organisation> results = report.getResults(new Paging(0, 20));
            assertEquals(1, results.size());
            assertEquals(organisation, results.iterator().next());
        } finally {
            version.abort();
        }
    }

    public void testExperimentBefore() throws ConstraintException {
        Calendar after = Calendar.getInstance();
        after.setTimeInMillis(NOW.getTimeInMillis() + 1000);
        Calendar before = Calendar.getInstance();
        before.setTimeInMillis(NOW.getTimeInMillis() - 1000);
        WritableVersion version = model.getTestVersion();
        try {
            ExperimentType type = new ExperimentType(version, UNIQUE);
            Experiment experiment = new Experiment(version, UNIQUE, before, NOW, type);
            Map criteria = new HashMap();
            criteria.put(Experiment.PROP_NAME, UNIQUE);
            Report<Experiment> report = new ExperimentReport(version, criteria, null, NOW, after);
            assertEquals(0, report.count());
            List<Experiment> results = report.getResults(new Paging(0, 20));
            assertEquals("" + (NOW.getTimeInMillis() - experiment.getStartDate().getTimeInMillis()), 0,
                results.size());
        } finally {
            version.abort();
        }
    }

    public void testExperimentAfter() throws ConstraintException {
        Calendar after = Calendar.getInstance();
        after.setTimeInMillis(NOW.getTimeInMillis() + 1000);
        Calendar before = Calendar.getInstance();
        before.setTimeInMillis(NOW.getTimeInMillis() - 1000);
        WritableVersion version = model.getTestVersion();
        try {
            ExperimentType type = new ExperimentType(version, UNIQUE);
            new Experiment(version, UNIQUE, NOW, after, type);
            Map criteria = new HashMap();
            criteria.put(Experiment.PROP_NAME, UNIQUE);
            Report<Experiment> report = new ExperimentReport(version, criteria, null, before, NOW);
            assertEquals(0, report.count());
            List<Experiment> results = report.getResults(new Paging(0, 20));
            assertEquals(0, results.size());
        } finally {
            version.abort();
        }
    }

    public void testExperimentDuring() throws ConstraintException {
        Calendar after = Calendar.getInstance();
        after.setTimeInMillis(NOW.getTimeInMillis() + 1000);
        Calendar before = Calendar.getInstance();
        before.setTimeInMillis(NOW.getTimeInMillis() - 1000);
        WritableVersion version = model.getTestVersion();
        try {
            ExperimentType type = new ExperimentType(version, UNIQUE);
            new Experiment(version, UNIQUE, NOW, NOW, type);
            Map criteria = new HashMap();
            criteria.put(Experiment.PROP_NAME, UNIQUE);
            Report<Experiment> report = new ExperimentReport(version, criteria, null, before, after);
            assertEquals(1, report.count());
            List<Experiment> results = report.getResults(new Paging(0, 20));
            assertEquals(1, results.size());
        } finally {
            version.abort();
        }
    }

    public void TODOtestProduct() throws ConstraintException {
        Calendar after = Calendar.getInstance();
        after.setTimeInMillis(NOW.getTimeInMillis() + 1000);
        Calendar before = Calendar.getInstance();
        before.setTimeInMillis(NOW.getTimeInMillis() - 1000);
        WritableVersion version = model.getTestVersion();
        try {
            ExperimentType type = new ExperimentType(version, UNIQUE);
            Experiment experiment = new Experiment(version, UNIQUE, NOW, NOW, type);
            Sample sample = new Sample(version, UNIQUE);
            SampleCategory category = new SampleCategory(version, UNIQUE);
            sample.addSampleCategory(category);
            new OutputSample(version, experiment).setSample(sample);
            Map criteria = new HashMap();
            criteria.put(Experiment.PROP_NAME, UNIQUE);
            Report<Experiment> report = new ExperimentReport(version, criteria, null, before, after);
            assertEquals(1, report.count());
            List<Experiment> results = report.getResults(new Paging(0, 20));
            assertEquals(1, results.size());
        } finally {
            version.abort();
        }
    }

    public void TODOtestFilterLabNotebook() throws ConstraintException {
        WritableVersion version = model.getTestVersion();
        try {
            Organisation organisation1 = new Organisation(version, "a" + UNIQUE + "z");
            organisation1.setAccess(new LabNotebook(version, UNIQUE));
            Organisation organisation2 = new Organisation(version, "b" + UNIQUE + "z");
            version.flush();
            Report<Organisation> report =
                new Report(version, Organisation.class, Collections.EMPTY_MAP, UNIQUE);
            report.setLabNotebooks(Collections.EMPTY_SET);

            assertEquals(1, report.count());
            Paging paging = new Paging(0, 20, Organisation.PROP_NAME, Paging.Order.ASC);
            List<Organisation> results = report.getResults(paging);
            assertEquals(1, results.size());
            assertEquals(organisation2, results.iterator().next());
        } finally {
            version.abort();
        }
    }

    public void testDisclosed() throws ConstraintException, AccessException, AbortedException {
        // set up: a user who can't read own writing
        String username = UNIQUE;
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        String readable = UNIQUE + "ro";
        try {
            UserGroup userGroup = new UserGroup(version, UNIQUE);
            LabNotebook book = new LabNotebook(version, readable);
            new Permission(version, "read", book, userGroup);
            new Permission(version, "create", book, userGroup);
            new Permission(version, "update", book, userGroup);
            userGroup.addMemberUser(new User(version, username));
            new ExperimentType(version, UNIQUE);

            version.commit();
        } finally {
            if (!version.isCompleted())
                version.abort();
        }

        version = model.getWritableVersion(username);
        try {
            Organisation organisation = new Organisation(version, UNIQUE);
            Map criteria = new HashMap();
            criteria.put(Organisation.PROP_NAME, UNIQUE);
            Report<Organisation> report = new Report(version, Organisation.class, criteria, null);
            LabNotebook book = version.findFirst(LabNotebook.class, "name", readable);
            report.setLabNotebooks(Collections.singleton(book));
            assertEquals("" + organisation.getCreationDate(), 1, report.count());
            List<Organisation> results = report.getResults(new Paging(0, 20));
            assertEquals(1, results.size());
            assertEquals(organisation, results.iterator().next());

        } finally {
            version.abort();
        }

        // tidy up
        version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            User user = version.findFirst(User.class, User.PROP_NAME, username);
            version.delete(user.getUserGroups());
            user.delete();
            version.findFirst(ExperimentType.class, "name", UNIQUE).delete();
            version.findFirst(LabNotebook.class, "name", readable).delete();
            version.commit();
        } finally {
            if (!version.isCompleted())
                version.abort();
        }

    }

    /**
     * KpiTest.testAttritionDisclosure
     * 
     * @throws ConstraintException
     * @throws AccessException
     * @throws AbortedException
     * 
     *             Checks that data that should be hidden, is hidden
     */
    public void testUnreadableBook() throws ConstraintException, AccessException, AbortedException {
        // set up: a user who can't read own writing
        String username = UNIQUE;
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        String readable = UNIQUE + "ro";
        String writable = UNIQUE + "wo";
        try {
            UserGroup userGroup = new UserGroup(version, UNIQUE + "ug2");
            new Permission(version, "read", new LabNotebook(version, readable), userGroup);
            LabNotebook writeOnly = new LabNotebook(version, writable);
            new Permission(version, "create", writeOnly, userGroup);
            new Permission(version, "update", writeOnly, userGroup);
            userGroup.addMemberUser(new User(version, username));
            new ExperimentType(version, UNIQUE);

            version.commit();
        } finally {
            if (!version.isCompleted())
                version.abort();
        }

        version = model.getWritableVersion(username);
        try {
            Organisation organisation = new Organisation(version, UNIQUE);
            Map criteria = new HashMap();
            criteria.put(Organisation.PROP_NAME, UNIQUE);
            Report<Organisation> report = new Report(version, Organisation.class, criteria, null);
            LabNotebook book = version.findFirst(LabNotebook.class, "name", writable);
            report.setLabNotebooks(new HashSet(Collections.singleton(book)));
            assertEquals("" + organisation.getCreationDate(), 0, report.count());
            List<Organisation> results = report.getResults(new Paging(0, 20));
            assertEquals(0, results.size());

        } finally {
            version.abort();
        }

        // tidy up
        version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            User user = version.findFirst(User.class, User.PROP_NAME, username);
            version.delete(user.getUserGroups());
            user.delete();
            version.findFirst(ExperimentType.class, "name", UNIQUE).delete();
            version.findFirst(LabNotebook.class, "name", readable).delete();
            version.findFirst(LabNotebook.class, "name", writable).delete();
            version.commit();
        } finally {
            if (!version.isCompleted())
                version.abort();
        }

    }

    public void testCount2() throws ConstraintException {
        final ReadableVersion version = this.model.getReadableVersion(Access.ADMINISTRATOR);
        final MetaClass experimentMetaClass = this.model.getMetaClass(Experiment.class.getName());
        final Map<String, String[]> params = new HashMap();

        try {
            // final SearchFilter criteria1 = new SearchFilter(criteria);
            //SearchExperiment.updateCriteria(criteria1);

            final ExperimentReport report = new ExperimentReport(version, Collections.EMPTY_MAP, "");
            report.getResults(new Paging(20, 40));
            report.count();

        } finally {
            version.abort();
        }
    }

}
