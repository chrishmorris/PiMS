/** 
 * pims-dm org.pimslims.search PIMSCriteria2.java
 * @author Jonathan Diprose
 * @date 10 Nov 2008
 * 
 * Protein Information Management System
 * @version: 2.3
 * 
 * Copyright (c) 2008 Jonathan Diprose The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.search;

import java.util.Collection;

import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.criterion.Criterion;
import org.pimslims.metamodel.ModelObject;

/**
 * PIMSCriteria2 - a suggested replacement for PIMSCriteria that is much closer
 * to Hibernate's Criteria interface
 */
public interface PIMSCriteria2 {

    /**
     * Run the query as a count.
     * 
     * @return The number of ModelObjects returned by the query
     */
    int count();

    /**
     * Return the list of results from the query.
     * 
     * @return The collection of ModelObjects returned by the query
     */
    <T extends ModelObject> Collection<T> list();

    /**
     * Return the only result. Throws a NonUniqueResultException if the
     * query returns more than one ModelObject.
     * 
     * @return The single ModelObjects returned by the query
     */
    ModelObject getSingleResult();

    /**
     * @param criterion - the criterion to add
     * @return The PIMSCriteria2 instance, for chaining
     * @see org.hibernate.Criteria#add(org.hibernate.criterion.Criterion)
     */
    PIMSCriteria2 add(Criterion criterion);

    /**
     * Create an alias with JoinType.INNER_JOIN.
     * 
     * @param associationPath - the associationPath to be aliased
     * @param alias - the alias for the associationPath
     * @return The PIMSCriteria2 instance, for chaining
     * @see #createAlias(String, String, org.pimslims.search.PIMSCriteria2Alias.JoinType)
     */
    PIMSCriteria2 createAlias(String associationPath, String alias);

    /**
     * @param associationPath - the associationPath to be aliased
     * @param alias - the alias for the associationPath
     * @param joinType - the joinType by which to join associationPath
     * @return The PIMSCriteria2 instance, for chaining
     * @see org.hibernate.Criteria#createAlias(java.lang.String, java.lang.String, int)
     */
    PIMSCriteria2 createAlias(String associationPath, String alias, PIMSCriteria2Alias.JoinType joinType);

    /**
     * @param alias - the alias to be created
     * @return The PIMSCriteria2 instance, for chaining
     * @see org.hibernate.Criteria#createAlias(java.lang.String, java.lang.String, int)
     */
    PIMSCriteria2 createAlias(PIMSCriteria2Alias alias);

    /**
     * @param cacheable - true if the query is to be cached, otherwise false
     * @return The PIMSCriteria2 instance, for chaining
     * @see org.hibernate.Criteria#setCacheable(boolean)
     */
    PIMSCriteria2 setCacheable(boolean cacheable);

    /**
     * @param associationPath - the associationPath to be fetched
     * @param mode - the fetchMode by which to fetch the associationPath
     * @return The PIMSCriteria2 instance, for chaining
     * @see org.hibernate.Criteria#setFetchMode(java.lang.String, org.hibernate.FetchMode)
     */
    PIMSCriteria2 setFetchMode(String associationPath, FetchMode mode);

    /**
     * @param flushMode - the flushMode for the query
     * @return The PIMSCriteria2 instance, for chaining
     * @see org.hibernate.Criteria#setFlushMode(org.hibernate.FlushMode)
     */
    PIMSCriteria2 setFlushMode(FlushMode flushMode);

    /**
     * Set the paging for the query.
     * 
     * @param paging - the paging for the query
     * @return The PIMSCriteria2 instance, for chaining
     */
    PIMSCriteria2 setPaging(Paging paging);

    /**
     * Zero means no or infinite timeout, less than zero means the default
     * timeout value will be used.
     * 
     * @param timeout - the timeout for the query in seconds
     * @return The PIMSCriteria2 instance, for chaining
     * @see org.hibernate.Criteria#setTimeout(int)
     */
    PIMSCriteria2 setTimeout(int timeout);

}