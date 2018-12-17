/**
 * pims-model org.pimslims.persistence JpqlQueryTest.java
 * 
 * @author cm65
 * @date 24 Mar 2013
 * 
 *       Protein Information Management System
 * @version: 4.4
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.ReadableVersionImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.search.Condition;
import org.pimslims.search.Conditions;
import org.pimslims.search.PIMSCriteria;
import org.pimslims.search.PIMSCriteriaImpl;
import org.pimslims.search.Paging;
import org.pimslims.search.Serial;

/**
 * JpqlQueryTest
 * 
 */
public class JpqlQueryTest extends TestCase {

    private static final String UNIQUE = "jq" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for JpqlQueryTest
     * 
     * @param name
     */
    public JpqlQueryTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.persistence.JpqlQuery#createFindAllHQL(org.pimslims.search.PIMSCriteria, org.pimslims.dao.ReadableVersion, java.util.Collection)}
     * .
     */
    public void testCreateFindAllHQL() {
        Collection<LabNotebook> labNotebooks = null;
        ReadableVersion version = this.model.getTestVersion();
        try {
            PIMSCriteria pCriteria =
                new PIMSCriteriaImpl(version, Organisation.class, new Paging(0, 20), labNotebooks);
            JpqlQuery query = JpqlQuery.createFindAllHQL(pCriteria, version, labNotebooks);
            assertEquals(
                "select  distinct A  from org.pimslims.model.people.Organisation A     Order by A.dbId DESC  ",
                query.getJpqlString());
        } finally {
            version.abort();
        }
    }

    public void testWhere() {
        Collection<LabNotebook> labNotebooks = null;
        ReadableVersion version = this.model.getTestVersion();
        try {
            PIMSCriteria pCriteria =
                new PIMSCriteriaImpl(version, Organisation.class, new Paging(0, 20), labNotebooks);
            Map<String, Object> map = new HashMap();
            map.put("name", "nonesuch");
            pCriteria.setAttributeMap(map);
            JpqlQuery query = JpqlQuery.createFindAllHQL(pCriteria, version, labNotebooks);
            assertEquals(
                "select  distinct A  from org.pimslims.model.people.Organisation A   where  A.name = :name1   Order by A.dbId DESC  ",
                query.getJpqlString());
        } finally {
            version.abort();
        }
    }

    public void testJoin() {
        Collection<LabNotebook> labNotebooks = null;
        ReadableVersion version = this.model.getTestVersion();
        try {
            PIMSCriteria pCriteria =
                new PIMSCriteriaImpl(version, Organisation.class, new Paging(0, 20), labNotebooks);
            MetaClass metaClass = version.getMetaClass(Organisation.class);
            MetaRole role = metaClass.getMetaRole(Organisation.PROP_GROUPS);
            List<MetaRole> joins = Collections.singletonList(role);
            pCriteria.setJoinMetaRoles(joins);
            JpqlQuery query = JpqlQuery.createFindAllHQL(pCriteria, version, labNotebooks);
            assertEquals(
                "select  distinct A  from org.pimslims.model.people.Organisation A   left join fetch A.groups as groups    Order by A.dbId DESC  ",
                query.getJpqlString());
        } finally {
            version.abort();
        }
    }

    public void testJoinedSearch() {
        Collection<LabNotebook> labNotebooks = null;
        ReadableVersion version = this.model.getTestVersion();
        try {
            PIMSCriteria pCriteria =
                new PIMSCriteriaImpl(version, OutputSample.class, new Paging(0, 20), labNotebooks);
            pCriteria.setAttributeMap(Collections.singletonMap(OutputSample.PROP_EXPERIMENT,
                (Object) Collections.singletonMap(Experiment.PROP_STATUS, "To_be_run")));
            pCriteria.list();
        } finally {
            version.abort();
        }
    }

    public void testAccessControl() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            LabNotebook book = new LabNotebook(version, UNIQUE);
            Collection<LabNotebook> labNotebooks = Collections.singleton(book);
            PIMSCriteria pCriteria =
                new PIMSCriteriaImpl(version, Organisation.class, new Paging(0, 20), labNotebooks);
            MetaRole role = version.getMetaClass(Organisation.class).getMetaRole(Organisation.PROP_GROUPS);
            JpqlQuery query = JpqlQuery.createFindAllHQL(pCriteria, version, labNotebooks);
            assertEquals(
                "select  distinct A  from org.pimslims.model.people.Organisation A    where  (A.access is null OR A.access in (:possibleAccessObjects))   Order by A.dbId DESC  ",
                query.getJpqlString());
        } finally {
            version.abort();
        }
    }

    public void testReferenceOnly() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            Collection<LabNotebook> labNotebooks = Collections.EMPTY_SET;
            PIMSCriteria pCriteria =
                new PIMSCriteriaImpl(version, Organisation.class, new Paging(0, 20), labNotebooks);
            MetaRole role = version.getMetaClass(Organisation.class).getMetaRole(Organisation.PROP_GROUPS);
            JpqlQuery query = JpqlQuery.createFindAllHQL(pCriteria, version, labNotebooks);
            assertEquals(
                "select  distinct A  from org.pimslims.model.people.Organisation A    where  (A.access is null)   Order by A.dbId DESC  ",
                query.getJpqlString());
        } finally {
            version.abort();
        }
    }

    public void testLike() {
        Collection<LabNotebook> labNotebooks = null;
        ReadableVersion version = this.model.getTestVersion();
        try {
            final Condition condition = Conditions.contains("nonesuch");
            MetaClass metaClass = version.getMetaClass(Organisation.class);
            java.util.Map<String, Object> map =
                PIMSCriteriaImpl.mergeLikeCondition(metaClass, condition, Collections.EMPTY_MAP);
            PIMSCriteria pCriteria =
                new PIMSCriteriaImpl(version, Organisation.class, new Paging(0, 20), labNotebooks);
            MetaRole role = version.getMetaClass(Organisation.class).getMetaRole(Organisation.PROP_GROUPS);
            pCriteria.setAttributeMap(map);
            JpqlQuery query = JpqlQuery.createFindAllHQL(pCriteria, version, labNotebooks);
            assertEquals(
                "select  distinct A  from org.pimslims.model.people.Organisation A   left join A.addresses as addresses  where ( lower( addresses) like :addresses1 OR  lower( A.city) like :city2 OR  lower( A.country) like :country3 OR  lower( A.details) like :details4 OR  lower( A.emailAddress) like :emailAddress5 OR  lower( A.faxNumber) like :faxNumber6 OR  lower( A.name) like :name7 OR  lower( A.organisationType) like :organisationType8 OR  lower( A.pageNumber) like :pageNumber9 OR  lower( A.phoneNumber) like :phoneNumber10 OR  lower( A.postalCode) like :postalCode11 OR  lower( A.url) like :url12)  Order by A.dbId DESC  ",
                query.getJpqlString());
        } finally {
            version.abort();
        }
    }

    public void testLikeTwice() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            new Organisation(version, UNIQUE).setCity(UNIQUE);
            final Condition condition = Conditions.contains(UNIQUE);
            MetaClass metaClass = version.getMetaClass(Organisation.class);
            java.util.Map<String, Object> map =
                PIMSCriteriaImpl.mergeLikeCondition(metaClass, condition, Collections.EMPTY_MAP);
            PIMSCriteria criteria =
                new PIMSCriteriaImpl(version, Organisation.class, new Paging(0, 20), Collections.EMPTY_SET);
            criteria.setAttributeMap(map);
            Assert.assertEquals(1, criteria.count());

        } finally {
            version.abort();
        }
    }

    /* TODO it would be great to make this test independent of Emerald data
     * Unfortunately I can't figure out what's special about their records
     * which triggers the defect
     * 
     * */
    public final void testSearchAllCountDeeper() {
        final ReadableVersion version = this.model.getTestVersion();
        try {

            final String search_all = "Embio-MoBio-";

            final Map<String, Object> map = Collections.EMPTY_MAP;

            MetaClass metaClass = version.getMetaClass(Protocol.class);
            PIMSCriteriaImpl search2 = new PIMSCriteriaImpl((version), Protocol.class, null, null);

            final Condition condition = Conditions.contains(search_all);
            search2.setAttributeMap(PIMSCriteriaImpl.mergeLikeCondition(metaClass, condition, map));

            //PIMSCriteriaImpl search = report.search;
            final int countOfResults = search2.count();
            final Paging paging = new Paging(0, countOfResults + 1);
            search2.setPaging(paging);
            List<Protocol> results = search2.list();

            assertEquals(results.size(), countOfResults);

        } finally {
            version.abort();
        }
    }

    public final void testSearchAllCountJoin() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {

            ExperimentType type = new ExperimentType(version, UNIQUE);
            List<String> remarks = new ArrayList<String>();
            remarks.add("");
            remarks.add(UNIQUE);
            Protocol protocol = new Protocol(version, UNIQUE + " x", type);
            protocol.setRemarks(remarks);
            protocol.setDetails(UNIQUE);
            protocol.setMethodDescription(UNIQUE);

            MetaClass metaClass = version.getMetaClass(Protocol.class);
            PIMSCriteriaImpl search2 = new PIMSCriteriaImpl((version), Protocol.class, null, null);

            final Condition condition = Conditions.contains(UNIQUE);
            search2.setAttributeMap(PIMSCriteriaImpl.mergeLikeCondition(metaClass, condition,
                (Map<String, Object>) Collections.EMPTY_MAP));

            //PIMSCriteriaImpl search = report.search;
            final int countOfResults = search2.count();
            final Paging paging = new Paging(0, countOfResults + 1);
            search2.setPaging(paging);
            List<Protocol> results = search2.list();
            assertEquals(results.size(), countOfResults);

        } finally {
            version.abort();
        }
    }

    public void testFilterCategories() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            // create samples
            final Sample sample = new Sample(version, UNIQUE);
            final SampleCategory category1 = new SampleCategory(version, UNIQUE);
            sample.addSampleCategory(category1);

            final Sample sample2 = new Sample(version, UNIQUE + "two");
            final SampleCategory category2 = new SampleCategory(version, UNIQUE + "two");
            sample2.addSampleCategory(category2);
            // create search criteria
            final Map parms = new HashMap();
            Collection<ModelObject> categories = new HashSet();
            categories.add(category1);
            categories.add(category2);
            parms.put(AbstractSample.PROP_SAMPLECATEGORIES, categories);
            PIMSCriteria criteria =
                new PIMSCriteriaImpl(version, Sample.class, new Paging(0, 20), Collections.EMPTY_SET);
            criteria.setAttributeMap(parms);
            Assert.assertEquals(2, criteria.count());
        } finally {
            version.abort();
        }
    }

    public void testAccessControlAndName() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            LabNotebook book = new LabNotebook(version, UNIQUE);
            Collection<LabNotebook> labNotebooks = Collections.singleton(book);
            PIMSCriteriaImpl pCriteria =
                new PIMSCriteriaImpl(version, Target.class, new Paging(0, 20), labNotebooks);
            Map<String, Object> map = new HashMap();
            map.put(Target.PROP_NAME, Conditions.eq("nonesuch"));
            pCriteria.setAttributeMap(map);
            JpqlQuery query = JpqlQuery.createFindAllHQL(pCriteria, version, labNotebooks);
            assertEquals(
                "select  distinct A  from org.pimslims.model.target.Target A   where  A.name = :name1  and  (A.access is null OR A.access in (:possibleAccessObjects))   Order by A.dbId DESC  ",
                query.getJpqlString());

            // now check that Hibernate can compile the query
            query.getQueryList(pCriteria);
        } finally {
            version.abort();
        }
    }

    private static final Calendar NOW = Calendar.getInstance();

    private static final Calendar YESTERDAY = Calendar.getInstance();

    private static final Calendar TWO_DAYS_AGO = Calendar.getInstance();

    private static final Calendar TOMORROW = Calendar.getInstance();
    static {
        YESTERDAY.setTimeInMillis(NOW.getTimeInMillis() - 24 * 60 * 60 * 1000);
        TOMORROW.setTimeInMillis(NOW.getTimeInMillis() + 24 * 60 * 60 * 1000);
        TWO_DAYS_AGO.setTimeInMillis(NOW.getTimeInMillis() - 2 * 24 * 60 * 60 * 1000);
    }

    private PIMSCriteria getExperimentCriteria(ReadableVersion version, Map<String, Object> map) {

        PIMSCriteriaImpl expSearch =
            (PIMSCriteriaImpl) ((ReadableVersionImpl) version).CreateQuery(Experiment.class, null, null);
        map.put(Experiment.PROP_PROJECT,
            Collections.singletonMap(ResearchObjective.PROP_COMMONNAME, (Object) UNIQUE));
        map.put(Experiment.PROP_STATUS, "Failed");
        expSearch.setAttributeMap(map);
        return expSearch;
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
            PIMSCriteria criteria = this.getExperimentCriteria(version, experimentCriteria);

            JpqlQuery query =
                JpqlQuery.createFindAllHQL(criteria, version, null,
                    " join A.researchObjective.experiments resumed ", " and A.endDate<resumed.startDate ",
                    "   A.endDate , min(resumed.startDate)  \r\n" + "\r\n"
                        + "from org.pimslims.model.experiment.Experiment A   \r\n", " group by A");
            query.doBindingParameters(criteria.getAttributeMap(), new Serial());
            List<Object[]> resumed = (List<Object[]>) query.list();
            assertEquals(1, resumed.size());
            // was assertEquals(0, report.getStalled().size());
            Calendar from = (Calendar) (resumed.iterator().next()[0]);
            assertEquals(YESTERDAY, from);
        } finally {
            version.abort();
        }
    }

}
