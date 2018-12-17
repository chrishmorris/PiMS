/**
 * org.pimslims.hibernate Query.java
 * 
 * @date 13 Apr 2007 21:00:47
 * 
 * @author Bill Lin
 * @see http://www.pims-lims.org
 * @version: 1.2
 * 
 *           Copyright (c) 2007
 * 
 *           This library is free software; you can redistribute it and/or modify it under the terms of the
 *           GNU Lesser General Public License as published by the Free Software Foundation; either version
 *           2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.pimslims.dao.AccessControllerImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.ReadableVersionImpl;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.target.Alias;
import org.pimslims.model.target.Target;
import org.pimslims.persistence.HibernateUtil;
import org.pimslims.persistence.JpqlQuery;
import org.pimslims.search.Paging.Order;
import org.pimslims.search.conditions.Contain;
import org.pimslims.search.conditions.ContainsLike;

/**
 * Query for search
 * 
 */
public class PIMSCriteriaImpl implements PIMSCriteria {

    /**
     * @param hqlQuery
     * @param attributeMap
     * @return
     */
    public static void bindingParameters(final JpqlQuery hqlQuery, final Map<String, Object> attributeMap) {
        hqlQuery.doBindingParameters(attributeMap, new Serial());
    }

    /**
     * remove attribute/role not searchable
     * 
     * @param classToSearch
     * @param attributeMap
     * @return TODO return a Condition
     */
    public static Map<String, Object> rebuildAttributes(final MetaClass classToSearch,
        final Map<String, Object> attributeMap) {
        final Map<String, Object> filteredAttributes = new LinkedHashMap<String, Object>();
        for (final String name : attributeMap.keySet()) {
            final Object value = attributeMap.get(name);
            if (name.equalsIgnoreCase(Conditions.AND) || name.equalsIgnoreCase(Conditions.OR)
                || name.equalsIgnoreCase(Conditions.NOTEXISTS)) {
                filteredAttributes.put(name, rebuildAttributes(classToSearch, (Map) value));
            } else {
                final MetaAttribute attribute = classToSearch.getAttribute(name);
                if (attribute != null)//attribute
                {//TODO a special case for SeqString as it is clob in oracle which is not searchable
                 //multi-attribute is not searchable
                    if (!name.equalsIgnoreCase("SeqString") && !(value instanceof Collection)) {
                        if (value instanceof Condition) {
                            filteredAttributes.put(name, value);
                        } else {
                            //convert to condition
                            filteredAttributes.put(name, Conditions.eq(value));
                        }
                    }
                } else {//role TODO get this under test, then generate OR for String[]
                    final MetaRole role = classToSearch.getMetaRole(name);
                    if (role != null) {
                        if (value instanceof Condition) {
                            filteredAttributes.put(name, value);
                        } else if (value instanceof Map) {
                            //rebuild sub-Attributes
                            filteredAttributes.put(name,
                                rebuildAttributes(role.getOtherMetaClass(), (Map) value));
                        } else if (value instanceof Collection) {
                            Collection<Condition> conditions = new HashSet<Condition>();
                            Collection values = (java.util.Collection) value;
                            for (Iterator iterator = values.iterator(); iterator.hasNext();) {
                                Object object = (Object) iterator.next();
                                conditions.add(Conditions.eq(object));
                            }
                            filteredAttributes.put(name, Conditions.or(conditions));
                        } else {
                            //convert to condition
                            filteredAttributes.put(name, Conditions.eq(value));
                        }

                    }
                }
            }
        }
        return filteredAttributes;
    }

    /** The version used to search */
    private final ReadableVersion rv; //

    /** the java class to find */
    private final Class targetJavaClass;

    public MetaClass metaClass;

    /**
     * search options: the roles to join this is for performance:load related object in one query.
     */
    private final List<String> joinRoleNames;

    /**
     * the criteria Map for search: attribute name => value required or/and role name => model object required
     */
    //TODO private
    java.util.Map<String, Object> attributeMap = Collections.EMPTY_MAP;

    public Paging paging = null;

    /**
     * labNotebooks Collection<LabNotebook> Limit responses to pages in Reference, or in this book Null means
     * no limit Empty collection means only reference data wanted.
     */
    private Collection<LabNotebook> labNotebooks;

    public PIMSCriteriaImpl(final ReadableVersion rv, final Class targetJavaClass, final Paging paging,
        Collection<LabNotebook> labNotebooks) {
        super();
        this.rv = rv;
        this.targetJavaClass = targetJavaClass;
        setPaging(paging);
        joinRoleNames = new LinkedList();
        joinRoleNames.addAll(getMetaClass().getJoinRoleNames());
        this.labNotebooks = labNotebooks;
    }

    /**
     * 
     * @return result collection
     */
    public <T extends ModelObject> List<T> list() {
        JpqlQuery selectHQL = JpqlQuery.createFindAllHQL(this, rv, this.labNotebooks);
        // de-duplicate the list
        return new ArrayList(new LinkedHashSet(selectHQL.getQueryList(this)));
    }

    /**
     * PIMSCriteriaImpl.wrap
     * 
     * @param queryPrefix
     * @param queryPostfix
     * @return
     */
    public List wrap(String queryPrefix, String queryPostfix) {
        JpqlQuery select = JpqlQuery.createFindAllHQL(this, rv, rv.getReadableLabNotebooks());
        JpqlQuery wrapped = select.wrap(queryPrefix, queryPostfix);
        wrapped.doBindingParameters(attributeMap, new Serial());

        //TODO that isn't quite right, move it to JpqlQUery where the right logic will be
        if (this.isSearchAccessControlNeeded()) {
            wrapped.setParameterList(JpqlQuery.POSSIBLE_ACCESS_OBJECTS, rv.getReadableLabNotebooks());
        }

        return wrapped.list();
    }

    /**
     * @see org.pimslims.search.PIMSCriteria#count()
     */
    @Override
    public int count() {
        final JpqlQuery selectHQL = JpqlQuery.count(this, rv, this.labNotebooks);
        try {
            selectHQL.doBindingParameters(this.attributeMap, new Serial());

            selectHQL.setLabNotebooks(this);
            // no paging for count
            List results = selectHQL.list();
            return ((Number) results.iterator().next()).intValue();
        } catch (NoSuchElementException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException("No count found from query: " + selectHQL.getJpqlString(), e);
        }
    }

    /**
     * @param attributesName
     */
    private void checkAttributes(final Set<String> attributesNames) {
        for (final String attributeName : attributesNames) {
            if (attributeName.contains(".")) {
                continue;//ignore role's attribute check
            }
            if (!(getMetaClass().getProperty(attributeName) instanceof MetaAttribute)) {
                throw new RuntimeException("Can not find attribute: " + attributeName + " in class: "
                    + targetJavaClass.getCanonicalName() + " for search!");
            }

        }

    }

    /**
     * @return Returns the attributeMap.
     */
    public java.util.Map<String, Object> getAttributeMap() {
        return attributeMap;
    }

    /**
     * @return Returns the isQueryCacheable.
     */
    @Override
    public Boolean getIsQueryCacheable() {
        return false;
    }

    /**
     * @return Returns the joinRoleNames.
     */
    @Override
    public List<String> getJoinRoleNames() {
        return joinRoleNames;
    }

    @Override
    public MetaClass getMetaClass() {
        if (metaClass == null) {
            metaClass = this.rv.getMetaClass(this.targetJavaClass);
        }
        return metaClass;
    }

    /**
     * @see org.pimslims.search.PIMSCriteria#getOrderBy()
     */
    @Override
    public Map<String, Order> getOrderBy() {
        if (paging == null || paging.getOrderBy() == null || paging.getOrderBy().isEmpty()) {

            return getMetaClass().getOrderBy();
        }
        return paging.getOrderBy();
    }

    /**
     * @return Returns the paging.
     */
    @Override
    public Paging getPaging() {
        return paging;
    }

    /**
     * @return Returns the targetJavaClass.
     */
    @Override
    public Class getTargetJavaClass() {
        return targetJavaClass;
    }

    /**
     * @see org.pimslims.search.PIMSCriteria#setAttributeMap(java.util.Map)
     */
    @Override
    public PIMSCriteria setAttributeMap(final java.util.Map<String, Object> attributeMap) {
        this.attributeMap = rebuildAttributes(getMetaClass(), attributeMap);
        return this;
    }

    /**
     * PIMSCriteriaImpl.getJpqlString
     * 
     * @return
     */
    public String getJpqlString() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.pimslims.search.PIMSCriteria#setIsQueryCacheable(java.lang.Boolean)
     */
    @Deprecated
    // does nothing
    public PIMSCriteria setIsQueryCacheable(final Boolean isQueryCacheable) {
        return this;
    }

    /**
     * @see org.pimslims.search.PIMSCriteria#setJoinMetaRoles(java.util.List)
     */
    public PIMSCriteria setJoinMetaRoles(final List<MetaRole> joinMetaRoles) {

        for (final MetaRole metaRole : joinMetaRoles) {
            if (!joinRoleNames.contains(metaRole.getName())) {
                joinRoleNames.add(metaRole.getName());
            }
        }
        return this;
    }

    /**
     * @see org.pimslims.search.PIMSCriteria#setPaging(org.pimslims.search.Paging)
     */
    public PIMSCriteria setPaging(final Paging paging) {
        this.paging = paging;
        if (paging != null && paging.getOrderBy() != null) {
            checkAttributes(paging.getOrderBy().keySet());
        }
        return this;
    }

    /**
     * @see org.pimslims.search.PIMSCriteria#getAccessControlAlias()
     */
    public String getAccessControlAlias() {
        return ((ReadableVersionImpl) rv).getAccessControlRoleName(targetJavaClass);

    }

    /**
     * PIMSCriteriaImpl.useDistinct
     * 
     * @return true if the query requires a "distinct" keyword Note that this work must not be used for Oracle
     */
    public boolean useDistinct() {
        if (this.hasSubSelect() && null != this.paging) {
            Set<String> orderAttributes = this.paging.getOrderBy().keySet();
            for (Iterator iterator = orderAttributes.iterator(); iterator.hasNext();) {
                String attribute = (String) iterator.next();
                if (attribute.contains(".")) {
                    // see http://opensource.atlassian.com/projects/hibernate/browse/HHH-2984
                    return false;
                }
            }
        }
        return !HibernateUtil.isOracleDB();
    }

    /**
     * PIMSCriteriaImpl.hasSubSelect
     * 
     * @return
     */
    private boolean hasSubSelect() {
        Collection<Object> values = this.attributeMap.values();
        for (Iterator iterator = values.iterator(); iterator.hasNext();) {
            Object value = (Object) iterator.next();
            if (value instanceof Map) {
                return true;
            }
        }
        return false;
    }

    /**
     * Searcher.getTargetAdditionalConditions
     * 
     * @param condition
     * @return
     */
    static Map<String, Object> getTargetAdditionalConditions(final Condition condition) {
        final Map<String, Object> targetConditions = new HashMap<String, Object>();
        targetConditions.put(Target.PROP_ALIASES, Conditions.newMap(Alias.PROP_NAME, condition.clone()));
        targetConditions.put(Target.PROP_PROTEIN, Conditions.newMap(Substance.PROP_NAME, condition.clone()));

        return targetConditions;
    }

    /**
     * Searcher.mergeLikeCondition
     * 
     * @param metaClass
     * @param condition
     * @param otherAndCriteria
     * @return TODO see PRIV-216 use of this method is suspected to lead to double counting
     */
    public static java.util.Map<String, Object> mergeLikeCondition(final MetaClass metaClass,
        final Condition condition, final Map<String, Object> otherAndCriteria) {
        java.util.Map<String, Object> attributeMap = new LinkedHashMap<String, Object>();
        for (final MetaAttribute ma : metaClass.getAttributes().values()) {
            if (!ma.isDerived() && !ma.isHidden()) {
                if (ma.getType() == String.class) {
                    attributeMap.put(ma.getName(), condition.clone());
                } else if (ma.isMulti() && condition instanceof Contain) {
                    attributeMap.put(ma.getName(), new ContainsLike((String) ((Contain) condition).value));
                }
            }
        }
        //for target
        if (metaClass.getJavaClass() == Target.class) {
            attributeMap.putAll(PIMSCriteriaImpl.getTargetAdditionalConditions(condition));
        }
        attributeMap = Conditions.orMap(attributeMap);
        //put in otherCriteria
        if (otherAndCriteria != null && otherAndCriteria.size() > 0) {
            attributeMap.putAll(Conditions.andMap(otherAndCriteria));
        }
        return attributeMap;
    }

    /**
     * PIMSCriteriaImpl.setLabNotebooks
     * 
     * @param books
     */
    public void setLabNotebooks(Collection<LabNotebook> books) {
        this.labNotebooks = books;
    }

    /**
     * PIMSCriteriaImpl.isSearchAccessControlNeeded
     * 
     * @return
     */
    public boolean isSearchAccessControlNeeded() {
        return AccessControllerImpl.isSearchAccessControlNeeded(this.targetJavaClass)
            && (!this.rv.isAdmin() || null != this.labNotebooks);
    }

    /**
     * PIMSCriteriaImpl.getLabNotebooks
     * 
     * @return
     */
    public Collection<LabNotebook> getLabNotebooks() {
        if (null == this.labNotebooks) {
            return this.rv.getReadableLabNotebooks();
        }
        return this.labNotebooks;
    }

}
