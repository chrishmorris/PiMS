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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.target.Target;
import org.pimslims.search.Condition;
import org.pimslims.search.Conditions;
import org.pimslims.search.PIMSCriteria;
import org.pimslims.search.PIMSCriteriaImpl;
import org.pimslims.search.Paging;

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
            assertEquals("select  A from org.pimslims.model.people.Organisation A    Order by A.dbId DESC  ",
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
                "select  A from org.pimslims.model.people.Organisation A  where  A.name = :name1   Order by A.dbId DESC  ",
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
                "select  A from org.pimslims.model.people.Organisation A   left join fetch A.groups as groups    Order by A.dbId DESC  ",
                query.getJpqlString());
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
                "select  A from org.pimslims.model.people.Organisation A   where  (A.access is null OR A.access in (:possibleAccessObjects))   Order by A.dbId DESC  ",
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
                "select  A from org.pimslims.model.people.Organisation A   where  (A.access is null)   Order by A.dbId DESC  ",
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
                "select  A from org.pimslims.model.people.Organisation A   left join A.addresses as addresses  where ( lower( addresses) like :addresses1 OR  lower( A.city) like :city2 OR  lower( A.country) like :country3 OR  lower( A.details) like :details4 OR  lower( A.emailAddress) like :emailAddress5 OR  lower( A.faxNumber) like :faxNumber6 OR  lower( A.name) like :name7 OR  lower( A.organisationType) like :organisationType8 OR  lower( A.pageNumber) like :pageNumber9 OR  lower( A.phoneNumber) like :phoneNumber10 OR  lower( A.postalCode) like :postalCode11 OR  lower( A.url) like :url12)  Order by A.dbId DESC  ",
                query.getJpqlString());
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
                "select  A from org.pimslims.model.target.Target A  where  A.name = :name1  and  (A.access is null OR A.access in (:possibleAccessObjects))   Order by A.dbId DESC  ",
                query.getJpqlString());

            // now check that Hibernate can compile the query
            query.getQueryList(pCriteria);
        } finally {
            version.abort();
        }
    }

}
