/**
 * DM org.pimslims.persistence HQLUtil.java
 * 
 * @author bl67
 * @date 22 Feb 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 bl67 This library is free software; you can redistribute it and/or modify it
 *           under the terms of the GNU Lesser General Public License as published by the Free Software
 *           Foundation; either version 2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */
package org.pimslims.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.pimslims.dao.AccessControllerImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaClassImpl;
import org.pimslims.metamodel.MetaProperty;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.search.Condition;
import org.pimslims.search.Conditions;
import org.pimslims.search.PIMSCriteria;
import org.pimslims.search.PIMSCriteriaImpl;
import org.pimslims.search.Paging.Order;
import org.pimslims.search.Serial;

public class JpqlQuery {

    /**
     * POSSIBLE_ACCESS_OBJECTS String
     */
    public static final String POSSIBLE_ACCESS_OBJECTS = "possibleAccessObjects";

    private String jpqlString;

    private javax.persistence.Query query;

    private ReadableVersion version;

    private Class clazz;

    /**
     * Constructor for HqlQuery
     * 
     * @param version
     * @param from
     * @param string
     */
    public JpqlQuery(String query, ReadableVersion version, Class from) {
        this(query, version, from, true, null, "", "A");
    }

    /**
     * Constructor for JpqlQuery
     * 
     * @param query JPQL
     * @param version current read transaction
     * @param from the class to list
     * @param labNotebooks filter replies for this set of lab notebooks
     */
    public JpqlQuery(String query, ReadableVersion version, Class from, Collection<LabNotebook> labNotebooks) {
        this(query, version, from, true, labNotebooks, "", "A");
    }

    /**
     * Constructor for JpqlQuery
     * 
     * @param jpql
     * @param version
     * @param clazz
     * @param accessControl false if subselect already filters
     * @param labNotebooks Books of interest, null means all permitted, empty set means only ref data
     * @param orderBy
     * @param accessControlAlias
     */
    JpqlQuery(String jpql, ReadableVersion version, Class clazz, boolean accessControl,
        Collection<LabNotebook> labNotebooks, String orderBy, String accessControlAlias) {
        if (!version.isAdmin())
            if (null == labNotebooks) {
                labNotebooks = version.getReadableLabNotebooks();
            } else {
                labNotebooks.retainAll(version.getReadableLabNotebooks());
            }
        this.setJpqlString("select " + jpql); // make sure it is a query
        if (accessControl && null != labNotebooks
            && (LabBookEntry.class.isAssignableFrom(clazz) || Attachment.class.isAssignableFrom(clazz))) {
            if (getJpqlString().contains("where")) {
                setJpqlString(jpqlString + " and ");
            } else {
                setJpqlString(jpqlString + " where ");
            }
            this.jpqlString +=
                " ("
                    + accessControlAlias
                    + ".access is null"
                    + (labNotebooks.isEmpty() ? "" : " OR " + accessControlAlias
                        + ".access in (:possibleAccessObjects)") + ") ";
        }
        this.jpqlString += " " + orderBy;
        this.query = version.getEntityManager().createQuery(this.jpqlString);
        this.version = version;
        this.clazz = clazz;
        if (this.jpqlString.contains(POSSIBLE_ACCESS_OBJECTS)) {
            this.setParameterList(POSSIBLE_ACCESS_OBJECTS, labNotebooks);
        }

    }

    // There is a copy in xtalPiMS. Unfortunately, pims-web also uses this. See sampleDAO.
    // TODO insecure, replace this with a method that returns a JpqlQuery
    @Deprecated
    public static String getWhereHQL(final ReadableVersion rv, final String alias,
        final List<String> conditions, final Class targetJavaClass) {
        if (conditions == null || conditions.size() == 0 && rv.isAdmin()) {
            return " ";
        }
        String whereString = "";

        //TODO the tests don't properly cover both cases here
        if (AccessControllerImpl.isSearchAccessControlNeeded(targetJavaClass)
            && (!rv.isAdmin() || null != rv.getReadableLabNotebooks())) {
            final ReadableVersion version = rv;
            /* moved to caller if (!AccessControllerImpl.isSearchAccessControlNeeded(targetJavaClass)
                 || (version.isAdmin() && null == labNotebooks)) {
                 return "";
             } */
            String dbids = "";
            for (final Iterator iterator = rv.getReadableLabNotebooks().iterator(); iterator.hasNext();) {
                final LabNotebook accessObject = (LabNotebook) iterator.next();
                if (accessObject != null) {
                    dbids += accessObject.getDbId();
                    if (iterator.hasNext()) {
                        dbids += ",";
                    }
                }

            }
            if (dbids.length() == 0) {
                dbids = "-1";
            }
            if (dbids.endsWith(",")) {
                dbids = dbids.substring(0, dbids.length() - 1);
            }
            final String accessHQL =
                " (" + alias + ".access is null OR " + alias + ".access.id in (" + dbids + ") ) ";
            whereString = accessHQL + " and ";
        }
        for (final Iterator iter = conditions.iterator(); iter.hasNext();) {
            String condition = (String) iter.next();
            if (condition.trim().length() > 0) {
                condition = condition.trim();
                if (condition.toLowerCase().startsWith("where")) {
                    condition = condition.substring("where".length());
                }
                whereString += condition + " ";
                if (iter.hasNext()) {
                    whereString += " and ";
                }
            }

        }
        if (whereString.endsWith(" and ")) {
            whereString = whereString.substring(0, whereString.lastIndexOf(" and "));
        }
        if ("".equals(whereString)) {
            return "";
        }
        return " where " + whereString;
    }

    /**
     * JpqlQuery.addAccessControlJpql
     */
    private void addAccessControlJpql() {
        // TODO Auto-generated method stub

    }

    private static String getAccessControlHQL(final ReadableVersion rv, final String alias,
        final Class targetJavaClass, Collection<LabNotebook> labNotebooks) {
        final ReadableVersion version = rv;
        /* moved to caller if (!AccessControllerImpl.isSearchAccessControlNeeded(targetJavaClass)
             || (version.isAdmin() && null == labNotebooks)) {
             return "";
         } */
        String dbids = "";
        for (final Iterator iterator = labNotebooks.iterator(); iterator.hasNext();) {
            final LabNotebook accessObject = (LabNotebook) iterator.next();
            if (accessObject != null) {
                dbids += accessObject.getDbId();
                if (iterator.hasNext()) {
                    dbids += ",";
                }
            }

        }
        if (dbids.length() == 0) {
            dbids = "-1";
        }
        if (dbids.endsWith(",")) {
            dbids = dbids.substring(0, dbids.length() - 1);
        }
        return " (" + alias + ".access is null OR " + alias + ".access.id in (" + dbids + ") ) ";
    }

    /**
     * @param javaClassName
     * @param attributeName
     * @param readableVersionImpl
     * @return HQL string
     */
    // only called within the data model project
    @Deprecated
    // slow in Postgresql before 9.2
    public static JpqlQuery createAttributeCountHQL(final Class javaClass, final String attributeName,
        ReadableVersion version) {
        JpqlQuery query =
            new JpqlQuery("count(distinct A." + attributeName + ") from " + javaClass.getName() + " as A",
                version, javaClass);

        return query;
    }

    public static boolean isAttributePopulated(final MetaAttribute ma, ReadableVersion version) {

        JpqlQuery query =
            new JpqlQuery(" 1 from " + ma.getMetaClass().getJavaClass().getName() + " A where A."
                + ma.getName() + " is not null", version, ma.getMetaClass().getJavaClass());

        // Added to give the yes/no
        query.setMaxResults(1);

        java.util.Collection results = null;
        try {
            results = query.list();
            // Results is either no rows (no non-null values) or one row (at least one non-null value) 
            return (!results.isEmpty());
        } catch (final org.hibernate.exception.SQLGrammarException e) {
            throw new RuntimeException("Can not test populated state of " + ma, e);
        }

    }

    public static boolean isPopulated(final Class javaClass, final String attributeName,
        ReadableVersion version) {
        JpqlQuery query =
            new JpqlQuery(" 1 from " + javaClass.getName() + " javaClass where javaClass." + attributeName
                + " is not null", version, javaClass);
        query.setMaxResults(1);
        // results will be no rows, or one row containing 1
        boolean ret = !query.list().isEmpty();
        return ret;
    }

    /**
     * @param criteria Map
     * @param needAccessControl
     * @param searchLink if true use 'AND' search between criterias otherwise use 'OR'
     * @param searchCon if true use 'like' search between criterias otherwise use '='
     * @param isSearchCaseSensitive
     * @return HQl for crieria Map
     * 
     *         public static String createCriterialHQL(final MetaClass ma, final Map<String, Object> criteria,
     *         final String alias, final boolean needAccessControl, String accessControlAlias) { if
     *         (accessControlAlias == null) { accessControlAlias = alias; } String criteriaHQL =
     *         doCreateCriterialHQL(ma, criteria, alias, "AND", new Serial()); if (needAccessControl) { if
     *         (criteriaHQL.length() <= 1) { criteriaHQL = getAccessHQL(accessControlAlias); } //else
     *         criteriaHQL = "(" + criteriaHQL + ") AND " + getAccessHQL(accessControlAlias); //ACCESS control
     *         always'AND' with others } return criteriaHQL; }
     */

    //TODO must handle multi-attributes, like Substance.synonyms
    //TODO adds access control twice, but so does constructor
    //TODO take Condition instead of criteria map
    private static String doCreateCriterialHQL(final MetaClass ma, final Map<String, Object> criteria,
        final String alias, final String searchLink, final Serial serial) {
        String criteriaHQL = "";

        //make attributes name sorted
        final List<String> attributeNameList = new ArrayList<String>(criteria.keySet());
        Collections.sort(attributeNameList);
        for (final String attributeName : attributeNameList) {
            final MetaClass subMetaClass = getSubMetalClass(ma, attributeName);
            final String hbName = attributeName;
            final Object value = criteria.get(attributeName);
            final String hqlName = alias + "." + hbName;
            if (hbName.equalsIgnoreCase(Conditions.AND) || hbName.equalsIgnoreCase(Conditions.OR)
                || hbName.equalsIgnoreCase(Conditions.NOTEXISTS)) {

                if (hbName.equalsIgnoreCase(Conditions.NOTEXISTS)) {
                    final String subCriterialHQL =
                        doCreateCriterialHQL(ma, (Map) value, alias, searchLink, serial);
                    final String joinHQL = createJoinHQL(ma, null, (Map) value, alias);
                    final String subFrom = joinHQL.replaceFirst("left join", "from");
                    criteriaHQL =
                        criteriaHQL + (" " + searchLink + " ") + Conditions.NOTEXISTS + "( " + subFrom
                            + " where " + subCriterialHQL + ")";
                } else if (value != null && ((Map) value).size() > 0) {
                    final String subSearchLink = hbName; //AND or OR
                    criteriaHQL =
                        criteriaHQL + (" " + searchLink + " ") + "("
                            + doCreateCriterialHQL(ma, (Map) value, alias, subSearchLink, serial) + ")";
                }
                continue;
            }

            if (value instanceof Condition) {
                criteriaHQL =
                    criteriaHQL + (" " + searchLink + " ")
                        + ((Condition) value).getHQLString(hqlName, attributeName, serial);

            } else if (value instanceof Map) {
                //it is sub-criteria, call this function recursively
                criteriaHQL =
                    criteriaHQL
                        + (" " + searchLink + " ")
                        + doCreateCriterialHQL(subMetaClass, (Map<String, Object>) value, attributeName,
                            searchLink, serial);

            }
        }

        if (criteriaHQL.startsWith(" " + searchLink + " ")) {
            criteriaHQL = criteriaHQL.substring((" " + searchLink + " ").length());
        }

        return criteriaHQL;

    }

    /**
     * @param criteria
     * @param joinRoles
     * @return HQL joins
     */
    public static String createJoinHQL(final MetaClass ma, Collection<String> joinRoleNames,
        final Map<String, Object> criteria, final String entityAlias) {
        String joinHQL = "";

        if (joinRoleNames == null) {
            joinRoleNames = Collections.EMPTY_SET;
        }
        if (joinRoleNames.size() > 0) {
            for (final String roleName : joinRoleNames) {
                joinHQL += " left join fetch " + getSingleJoin(entityAlias, roleName);
            }
        }
        //additional join from roles in criteria
        final Map<String, MetaProperty> roles = getRoleNames(ma, criteria);
        for (final String name : criteria.keySet()) {
            if (roles.keySet().contains(name)) {
                final String roleName = name;
                if (!joinRoleNames.contains(roleName)) {
                    joinHQL += " left join " + getSingleJoin(entityAlias, roleName);

                }
                //sub crieria
                if (criteria.get(roleName) instanceof Map) {
                    MetaProperty property = roles.get(roleName);
                    joinHQL +=
                        createJoinHQL(((MetaRole) property).getOtherMetaClass(), Collections.EMPTY_SET,
                            (Map) criteria.get(roleName), roleName);
                }
            } else if (name.equalsIgnoreCase(Conditions.AND) || name.equalsIgnoreCase(Conditions.OR)) {
                joinHQL += createJoinHQL(ma, Collections.EMPTY_SET, (Map) criteria.get(name), entityAlias);
            }
        }
        return joinHQL;
    }

    private static String getSingleJoin(final String entityAlias, final String roleName) {
        //final char[] arrayName = roleName.toCharArray();
        return entityAlias + "." + roleName + " as " + roleName + " ";
    }

    /**
     * 
     * @param metaClass
     * @param criteria
     * @return column name to join on => property details This is all roles in the query, plus all
     *         multi-attributes like Substance.synonyms
     */
    static Map<String, MetaProperty> getRoleNames(final MetaClass metaClass,
        final Map<String, Object> criteria) {
        final Map<String, MetaProperty> roles = new LinkedHashMap<String, MetaProperty>();
        final Map<String, MetaRole> allRoles = ((MetaClassImpl) metaClass).getMetaRoles();
        Map<String, MetaAttribute> attributes = metaClass.getAttributes();
        for (final String name : criteria.keySet()) {
            if (allRoles.keySet().contains(name)) {
                roles.put(name, allRoles.get(name));
            } else if (attributes.containsKey(name)) {
                MetaAttribute attr = attributes.get(name);
                if (attr.isMulti()) {
                    roles.put(name, allRoles.get(name));
                }
            }
        }
        return roles;
    }

    /**
     * HQLBuilder.getMetalClass
     * 
     * @param ma
     * @param attributeName
     * @return
     */
    private static MetaClass getSubMetalClass(final MetaClass ma, final String attributeName) {
        final Map<String, MetaRole> allRoles = ((MetaClassImpl) ma).getMetaRoles();
        if (allRoles.keySet().contains(attributeName)) {
            final MetaRole role = allRoles.get(attributeName);
            return role.getOtherMetaClass();
        }
        return null;
    }

    /**
     * Find an entity role's related entities
     * 
     * @param metaClass java class name
     * @param metaRole role name of above class
     * @param criteria Map<role's attribute name, value>
     * @param entityAlias the alias of this entity
     * @param readableVersion
     * @return HQL string
     */
    // only called within the data model project
    public static JpqlQuery createRoleFindAllHQL(final org.pimslims.metamodel.MetaClass metaClass,
        final MetaRole metaRole, final Map<String, Object> criteria, final String entityAlias,
        final boolean isSearchCaseSensitive, String accessControlAlias, ReadableVersion version) {
        final String className = metaClass.getJavaClass().getName();

        // eg:"select B from org.pimslims.model.people.Person A inner join
        // A.authorCitations B where A=:A and B.title like 'pims' "
        //TODO not distinct for Oracle, nor with Order By
        String selectHQL =
            "distinct B from " + className + " A  inner join  A." + getHibernateName(metaRole.getRoleName())
                + " B ";

        final MetaClass otherMetaClass = metaRole.getOtherMetaClass();
        final String joinHQL = createJoinHQL(otherMetaClass, Collections.EMPTY_SET, criteria, "B");
        if (joinHQL.length() > 1) {
            selectHQL += " " + joinHQL;
        }

        selectHQL += " where A=:" + entityAlias;

        //TODO  add access control in constructor
        final boolean needAccessControl =
            AccessControllerImpl.isSearchAccessControlNeeded(otherMetaClass.getJavaClass())
                && !version.isAdmin();
        if (accessControlAlias == null) {
            accessControlAlias = "B";
        }
        String jpql = doCreateCriterialHQL(metaClass, criteria, "B", "AND", new Serial());
        /* if (needAccessControl) {
             if (jpql.length() <= 1) {
                 jpql = getAccessHQL(accessControlAlias);
             }
             //else
             jpql = "(" + jpql + ") AND " + getAccessHQL(accessControlAlias); //ACCESS control always'AND' with others
         } */

        // if no criteriaHQL, AND is not needed

        JpqlQuery hqlQuery =
            new JpqlQuery(selectHQL + " AND " + jpql, version, metaClass.getJavaClass(), needAccessControl,
                version.isAdmin() ? Collections.EMPTY_LIST : version.getReadableLabNotebooks(), hqlOrderBy(
                    "B", otherMetaClass.getOrderBy()), accessControlAlias);
        if (needAccessControl) {
            hqlQuery.setParameterList(POSSIBLE_ACCESS_OBJECTS, version.getReadableLabNotebooks());
        }
        return hqlQuery;

    }

    /**
     * Returns an HQL string to make a search
     * 
     * @param class1.getN the class of the object on which .findAll has been called
     * @param roleName the role to be searched
     * @param roleName2 the role to be matched
     * @param thisEntityAlias
     * @param roleEntityAlias
     * @param readableVersion
     * @return HQL string
     */
    // only called within the data model project
    public static JpqlQuery createRoleFindAllHQL(final Class class1, final String roleName,
        final String roleName2, final String thisEntityAlias, final String roleEntityAlias,
        ReadableVersion version) {
        // eg:"select B from org.pimslims.model.people.Person A inner join
        // A.authorCitations B where A=:A and B.Project=:B "
        //TODO not distinct for Oracle, nor for Order By
        String select = "distinct associate from ";
        if (HibernateUtil.isOracleDB()) {
            /* Oracle does not support "select distinct" for a table with a CLOB attribute:
                http://opensource.atlassian.com/projects/hibernate/browse/HHH-3606
               which in our case means Molecule.  */
            select = "associate from ";
        }
        JpqlQuery hqlQuery =
            new JpqlQuery(select + class1.getName() + " as A inner join A." + roleName + " as associate "
                + " inner join associate." + roleName2 + " as toMatch " + " where A=:" + thisEntityAlias
                + " and toMatch.dbId=:" + roleEntityAlias, version, class1);

        return hqlQuery;
    }

    public static String getHibernateName(final String roleName) {
        return roleName.substring(0, 1).toLowerCase() + roleName.substring(1);
    }

    /**
     * @param linkedHashMap
     * @return a HQL string for order by dbid desc
     */
    public static String hqlOrderBy(final String entityAlias, final Map<String, Order> orderBy) {

        String hql = " Order by ";
        for (final String attributeName : orderBy.keySet()) {
            final String direction = orderBy.get(attributeName).toString();
            if (attributeName.contains("."))//role's attrbute order
                hql += attributeName + " " + direction + " , ";
            else
                hql += entityAlias + "." + attributeName + " " + direction + " , ";
        }

        return hql.replace(", ", " ");

    }

    /**
     * .HqlQuery.setString
     * 
     * @param arg0
     * @param arg1
     * @return
     * @see org.hibernate.Query#setString(java.lang.String, java.lang.String)
     */
    public void setString(String arg0, String arg1) {
        this.query.setParameter(arg0, arg1);
    }

    /**
     * .HqlQuery.setParameter
     * 
     * @param arg0
     * @param arg1
     * @return
     * @throws HibernateException
     * @see org.hibernate.Query#setParameter(java.lang.String, java.lang.Object)
     */
    public void setParameter(String arg0, Object arg1) throws HibernateException {
        this.query.setParameter(arg0, arg1);
    }

    public List list() {
        try {
            return this.query.getResultList();
        } catch (Exception ex) {
            // the query can trigger  a flush, so we may have write errors
            throw new RuntimeException("Sorry, there is an error in PiMS: " + this.jpqlString,
                WritableVersionImpl.processFlushException(null, ex));
        }
    }

    /**
     * .HqlQuery.setEntity
     * 
     * @param arg0
     * @param arg1
     * @return
     * @see org.hibernate.Query#setEntity(java.lang.String, java.lang.Object)
     */
    public void setEntity(String arg0, Object arg1) {
        this.query.setParameter(arg0, arg1);
    }

    /**
     * .HqlQuery.setFirstResult
     * 
     * @param arg0
     * @return
     * @see org.hibernate.Query#setFirstResult(int)
     */
    public void setFirstResult(int arg0) {
        this.query.setFirstResult(arg0);
    }

    /**
     * .HqlQuery.setMaxResults
     * 
     * @param arg0
     * @return
     * @see org.hibernate.Query#setMaxResults(int)
     */
    public void setMaxResults(int arg0) {
        this.query.setMaxResults(arg0);
    }

    /**
     * .HqlQuery.setParameterList
     * 
     * @param arg0
     * @param arg1
     * @return
     * @throws HibernateException
     * @see org.hibernate.Query#setParameterList(java.lang.String, java.util.Collection)
     */
    public void setParameterList(String arg0, Collection arg1) throws HibernateException {
        assert null != arg0;
        this.query.setParameter(arg0, arg1);
    }

    public List getQueryList(PIMSCriteriaImpl pimsCriteriaImpl) {

        this.doBindingParameters(pimsCriteriaImpl.getAttributeMap(), new Serial());

        setLabNotebooks(pimsCriteriaImpl);
        if (pimsCriteriaImpl.paging != null) {
            setFirstResult(pimsCriteriaImpl.paging.getStart());
            setMaxResults(pimsCriteriaImpl.paging.getLimit());
        }
        return list();
    }

    /**
     * JpqlQuery.setLabNotebooks
     * 
     * @param pimsCriteriaImpl
     */
    public void setLabNotebooks(PIMSCriteriaImpl pimsCriteriaImpl) {
        if (pimsCriteriaImpl.isSearchAccessControlNeeded()) {
            if (this.jpqlString.contains(POSSIBLE_ACCESS_OBJECTS)) {
                setParameterList(POSSIBLE_ACCESS_OBJECTS, pimsCriteriaImpl.getLabNotebooks());
            }
        }
    }

    /**
     * JpqlQuery.getQuery
     * 
     * @param rv
     * @param selectHQL
     * @param class1
     * @return
     * 
     *         Called in logic project
     * 
     *         public static JpqlQuery getQuery(ReadableVersion rv, String joinAndWhereHQL, Class<? extends
     *         LabBookEntry> class1) { // note that Oracle does not accept "distinct" String hql =
     *         " distinct A from " + class1.getName() + " as A " + joinAndWhereHQL;
     * 
     *         return new JpqlQuery(hql, rv, class1); }
     */

    /**
     * @return Returns the jpqlString.
     */
    public String getJpqlString() {
        return jpqlString;
    }

    /**
     * @param jpqlString The jpqlString to set.
     */
    private void setJpqlString(String jpqlString) {
        this.jpqlString = jpqlString;
    }

    public void doBindingParameters(final Map<String, Object> attributeMap, final Serial serial) {
        final JpqlQuery hqlQuery = this;
        for (final String name : attributeMap.keySet()) {

            final Object value = attributeMap.get(name);
            if (value instanceof Condition) {
                ((Condition) value).bindingParameter(hqlQuery);
            } else if (value instanceof Map) {
                //it is sub-criteria, call this function recursively
                doBindingParameters((Map<String, Object>) value, serial);
                /*                if (value instanceof ModelObject)
                                    hqlQuery.setEntity(name + serial.getValue(), value);
                                else
                                    hqlQuery.setParameter(name + serial.getValue(), value);*/
            } else {
                //just in case
                throw new RuntimeException("Can not do parameter binding for '" + name + "' with value:"
                    + value);
            }
        }
    }

    /**
     * JpqlQuery.wrap
     * 
     * @param queryPrefix
     * @param queryPostfix
     * @return
     */
    public JpqlQuery wrap(String queryPrefix, String queryPostfix) {
        return new JpqlQuery(queryPrefix + this.jpqlString + queryPostfix, this.version, this.clazz, false,
            this.version.getReadableLabNotebooks(), "", "A");
    }

    public static JpqlQuery count(PIMSCriteriaImpl pimsCriteriaImpl, ReadableVersion rv,
        Collection<LabNotebook> labNotebooks) {
        String countHQL = createFindAllHQL(pimsCriteriaImpl, rv, labNotebooks).getJpqlString();
        // note that method supplies access control
        //remove orderby
        countHQL = (countHQL.split("Order by"))[0];

        // $JMD - Remove fetch from joins - is this really the best I can do?
        // Ideally, HQLUtil would know its doing a count and not put the fetch in
        countHQL = countHQL.replace(" join fetch ", " join ");
        // JMD$

        /* ---------------  
        JpqlQuery selectHQL =
            new JpqlQuery(countHQL.substring(7), rv, pimsCriteriaImpl.getTargetJavaClass(), false,
                labNotebooks, "", "A");
        selectHQL.doBindingParameters(pimsCriteriaImpl.attributeMap, new Serial());

        selectHQL.setLabNotebooks(pimsCriteriaImpl);
        // no paging for count
        List results = selectHQL.list();

        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            Object object = (Object) iterator.next();
            System.out.println(object);
        }
        ///------------------- */

        //add count
        countHQL =
            "count ( A) from " + pimsCriteriaImpl.metaClass.getMetaClassName() + " as A where A in ("
                + countHQL + ")";
        JpqlQuery hqlQuery =
            new JpqlQuery(countHQL, rv, pimsCriteriaImpl.getTargetJavaClass(), false, labNotebooks, "", "A");

        return hqlQuery;
    }

    /**
     * Find any entity meet criteria for certain class
     * 
     * @param rv
     * @param labNotebooks Books of interest, null means all permitted, empty set means only ref data
     * @param extraJoin TODO
     * @param select TODO
     * @param groupBy
     * @param className java class name
     * @param criteria Map<role's attribute name, value>
     * @param isANDorORsearch use 'AND' search between criterias otherwise use 'OR'
     * @param LikeOrEqualSearch use 'like' search between criterias otherwise use '='
     * 
     * @return HQL string
     * 
     */
    public static JpqlQuery createFindAllHQL(final PIMSCriteria pCriteria, ReadableVersion rv,
        Collection<LabNotebook> labNotebooks, String extraJoin, String extraWhere, String select,
        String groupBy) {
        final String className = pCriteria.getTargetJavaClass().getName();

        // eg:"select A from org.pimslims.model.people.Person A where A.name like 'pims' "
        String selectHQL = select;

        final String joinHQL =
            createJoinHQL(pCriteria.getMetaClass(), pCriteria.getJoinRoleNames(),
                pCriteria.getAttributeMap(), "A");
        //if (joinHQL.length() > 1) {
        selectHQL += " " + joinHQL;
        //}
        selectHQL += extraJoin;

        boolean needAccessControl = ((PIMSCriteriaImpl) pCriteria).isSearchAccessControlNeeded();
        String accessControlAlias = pCriteria.getAccessControlAlias();
        if (accessControlAlias == null) {
            accessControlAlias = "A";
        }
        String jpql =
            doCreateCriterialHQL(pCriteria.getMetaClass(), pCriteria.getAttributeMap(), "A", "AND",
                new Serial());
        /* Now done in constructor of JpqlQuery
         * 
         * if (needAccessControl) {
        if (jpql.length() <= 1) {
            jpql = getAccessHQL(accessControlAlias);
        }
        //else
        jpql = "(" + jpql + ") AND " + getAccessHQL(accessControlAlias); //ACCESS control always'AND' with others
        } */
        if (null != extraWhere) {
            jpql += " " + extraWhere;
        }

        JpqlQuery hqlQuery;
        // if criteriaHQL is empty
        if (jpql.length() <= 1) {
            hqlQuery =
                new JpqlQuery(selectHQL + " " + jpql, rv, pCriteria.getTargetJavaClass(), needAccessControl,
                    labNotebooks, hqlOrderBy("A", pCriteria.getOrderBy()), accessControlAlias);
        } else {
            // else
            String ret = selectHQL + " where " + jpql;
            //System.out.println("HQLBuilder.createFindAllHQL: "+ret);
            hqlQuery =
                new JpqlQuery(ret, rv, pCriteria.getTargetJavaClass(), needAccessControl, labNotebooks,
                    groupBy + hqlOrderBy("A", pCriteria.getOrderBy()), accessControlAlias);
        }
        return hqlQuery;

    }

    /**
     * JpqlQuery.getSelect
     * 
     * @param extraSelect
     * @param className
     * @return
     */
    private static String getSelect(String extraSelect, final String className) {
        String selectHQL = " distinct A ";
        /* if (!((PIMSCriteriaImpl) pCriteria).useDistinct()) {
            selectHQL = "A from "; // may be needed for Oracle, but if so see PRIV-216
        } */
        selectHQL += extraSelect + " from " + className + " A ";
        return selectHQL;
    }

    /**
     * JpqlQuery.createFindAllHQL
     * 
     * @param expSearch
     * @param version2
     * @param labNotebooks
     * @param string
     * @return
     */
    public static JpqlQuery createFindAllHQL(final PIMSCriteria pCriteria, ReadableVersion rv,
        Collection<LabNotebook> labNotebooks) {
        return createFindAllHQL(pCriteria, rv, labNotebooks, "", null,
            getSelect("", pCriteria.getTargetJavaClass().getName()), "");
    }

}
