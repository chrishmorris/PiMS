package org.pimslims.search;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.ReadableVersionImpl;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.search.conditions.Contain;

/**
 * Class to perform searches in the database
 * 
 * @author Petr Troshin May 2006
 */
@Deprecated
// bad API design, does not add value to PIMSCriteria. Use Report.
public class Searcher {

    final AbstractModel model;

    final ReadableVersion version;

    public Searcher(final ReadableVersion rv) {
        model = org.pimslims.dao.ModelImpl.getModel();
        this.version = rv;
    }

    /**
     * Main search method. Search model objects based on criteria supplied
     * 
     * @param criteria
     * @param metaClass
     * @return List of ModelObjects
     */
    @Deprecated
    // set a paging object
    public ArrayList<ModelObject> search(final Map<String, Object> criteria, final MetaClass metaClass) {
        return search(criteria, metaClass, null);
    }

    /**
     * Main search method. Search model objects based on criteria supplied
     * 
     * @param criteria
     * @param metaClass
     * @param paging
     * @return List of ModelObjects
     */
    public ArrayList<ModelObject> search(Map<String, Object> criteria, final MetaClass metaClass,
        final Paging paging) {
        criteria = Contain.convertMap(criteria);
        return searchByQuery(criteria, metaClass.getJavaClass(), paging);
    }

    @Deprecated
    // used in pims-web. User Report.count().
    public int count(Map<String, Object> criteria, final MetaClass metaClass) {
        criteria = Contain.convertMap(criteria); //TODO probably mistaken
        return countQuery(criteria, metaClass.getJavaClass());
        /* could: final PIMSCriteria query =
            ((ReadableVersionImpl) version).CreateQuery(javaClass, null, version.getReadableLabNotebooks());
        query.setAttributeMap(criteria);
        query.setIsQueryCacheable(false);
        return query.count(); */
    }

    private ArrayList<ModelObject> searchByQuery(final Map<String, Object> criteria, final Class metaClass,
        final Paging paging) {
        final PIMSCriteria query = ((ReadableVersionImpl) version).CreateQuery(metaClass, paging, null);
        query.setAttributeMap(criteria);
        // was query.setIsQueryCacheable(false);
        return new ArrayList<ModelObject>(query.list());
    }

    private int countQuery(final Map<String, Object> criteria, final Class javaClass) {
        final PIMSCriteria query = ((ReadableVersionImpl) version).CreateQuery(javaClass, null, null);
        query.setAttributeMap(criteria);
        query.setIsQueryCacheable(false);
        return query.count();
    }

    /**
     * Search in all fields method.
     * 
     * @param metaClass
     * @param searchValue - value to search
     * @return - List of ModelObjects
     */
    @Deprecated
    // searchAll(final MetaClass metaClass, final String keyword,     final Paging paging) 
    public ArrayList<ModelObject> searchAll(final MetaClass metaClass, final String searchValue) {
        return searchAll(metaClass, searchValue, null);
    }

    /**
     * Search in all fields method.
     * 
     * @param metaClass
     * @param searchValue - value to search
     * @param paging
     * @return - List of ModelObjects
     */
    public ArrayList<ModelObject> searchAll(final MetaClass metaClass, final Condition condition,
        final Map<String, Object> otherAndCriteria, final Paging paging) {
        java.util.Map<String, Object> attributeMap =
            PIMSCriteriaImpl.mergeLikeCondition(metaClass, condition, otherAndCriteria);
        //do search
        final ArrayList<ModelObject> results = searchByQuery(attributeMap, metaClass.getJavaClass(), paging);
        // add results from subclass
        for (final MetaClass subClass : metaClass.getSubtypes()) {
            for (final ModelObject mo : searchAll(subClass, condition, otherAndCriteria, paging)) {
                if (!results.contains(mo)) //avoid duplicated record
                {
                    results.add(mo);
                }
            }
        }
        return results;

    }

    /**
     * Searcher.countAll
     * 
     * @param metaClass
     * @param condition Like test
     * @param otherAndCriteria criteria map
     * @return
     */
    private int countAll(final MetaClass metaClass, final Condition condition,
        final Map<String, Object> otherAndCriteria, String like) {
        java.util.Map<String, Object> attributeMap = new LinkedHashMap<String, Object>();
        for (final MetaAttribute ma : metaClass.getAttributes().values()) {
            if (!ma.isDerived() && !ma.isHidden() && ma.getType() == String.class) {
                attributeMap.put(ma.getName(), condition.clone());
            }
        }
        attributeMap = Conditions.orMap(attributeMap);
        //put in otherCriteria
        if (otherAndCriteria != null && otherAndCriteria.size() > 0) {
            attributeMap.putAll(Conditions.andMap(otherAndCriteria));
        }
        //do search
        int size = countQuery(attributeMap, metaClass.getJavaClass());
        /* final PIMSCriteria query =
            ((ReadableVersionImpl) version).CreateQuery(javaClass, null, version.getReadableLabNotebooks());
        query.setAttributeMap(criteria);
        query.setIsQueryCacheable(false);
        return query.count(); */

        // add results from subclass
        for (final MetaClass subClass : metaClass.getSubtypes()) {
            size += countAll(subClass, condition, otherAndCriteria, like);
        }
        return size;

    }

    public Collection<ModelObject> search(final MetaClass metaClass, final Map<String, Object> criteria,
        final String keyword) {
        final Condition condition = Conditions.contains(keyword);
        return searchAll(metaClass, condition, criteria, null);
    }

    public int count(final MetaClass metaClass, final Map<String, Object> criteria, final String keyword) {
        final Condition condition = Conditions.contains(keyword);
        return countAll(metaClass, condition, criteria, keyword);
    }

    public Collection<ModelObject> search(final MetaClass metaClass, final Map<String, Object> criteria,
        final String keyword, final Paging paging) {
        final Condition condition = Conditions.contains(keyword);
        return searchAll(metaClass, condition, criteria, paging);
    }

    public ArrayList<ModelObject> searchAll(final MetaClass metaClass, final String keyword,
        final Paging paging) {
        final Condition condition = Conditions.contains(keyword);
        return searchAll(metaClass, condition, Collections.EMPTY_MAP, paging);
    }

    @Deprecated
    // is this used?
    public int count(final MetaClass metaClass, final String keyword) {
        return count(metaClass, Collections.EMPTY_MAP, keyword);
    }

    public int countRecords(final MetaClass metaClass) {
        return this.version.getCountOfAll(metaClass);
    }

    /**
     * Return the name of the fields which is (1) possible to search in. (2) contains some values (something
     * has been recorded)
     * 
     * @param metaClass
     * @return Map - MetaAttributeName -> MetaAttribute
     * @throws SQLException
     */
    public HashMap<String, MetaAttribute> getSearchableFields(final MetaClass metaClass) {
        return this.version.getSearchableFields(metaClass);
    }

} // class end

