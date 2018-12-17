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
    @Deprecated
    // offers little, please inline this wherever it is called
    public ArrayList<ModelObject> search(Map<String, Object> criteria, final MetaClass metaClass,
        final Paging paging) {
        criteria = Contain.convertMap(criteria);
        final PIMSCriteria query = new PIMSCriteriaImpl((version), metaClass.getJavaClass(), paging, null);
        query.setAttributeMap(criteria);
        return new ArrayList<ModelObject>(query.list());
    }

    @Deprecated
    // used in pims-web. Better to use Report.count().
    public int count(Map<String, Object> criteria, final MetaClass metaClass) {
        criteria = Contain.convertMap(criteria); //TODO probably mistaken
        final PIMSCriteria query = new PIMSCriteriaImpl((version), metaClass.getJavaClass(), null, null);
        query.setAttributeMap(criteria);
        return query.count();
        /* could: final PIMSCriteria query =
            ((ReadableVersionImpl) version).CreateQuery(javaClass, null, version.getReadableLabNotebooks());
        query.setAttributeMap(criteria);
        query.setIsQueryCacheable(false);
        return query.count(); */
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
        final PIMSCriteria query = new PIMSCriteriaImpl((version), metaClass.getJavaClass(), paging, null);
        query.setAttributeMap(attributeMap);
        //do search
        final ArrayList<ModelObject> results = new ArrayList<ModelObject>(query.list());
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
        final PIMSCriteria query = new PIMSCriteriaImpl((version), metaClass.getJavaClass(), null, null);
        query.setAttributeMap(attributeMap);
        //do search
        int size = query.count();
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

