/**
 * pims-dm org.pimslims.search PIMSCriteriaHibernateImpl.java
 * 
 * @author Jonathan Diprose
 * @date 10 Nov 2008
 * 
 *       Protein Information Management System
 * @version: 2.3
 * 
 *           Copyright (c) 2008 Jonathan Diprose The copyright holder has licenced the STFC to redistribute
 *           this software
 */
package org.pimslims.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.pimslims.dao.AccessControllerImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.ReadableVersionImpl;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.search.PIMSCriteria2Alias.JoinType;
import org.pimslims.search.Paging.Order;

/**
 * PIMSCriteriaHibernateImpl
 * 
 * Suggested replacement for PIMSCriteriaImpl. This is an implementation of PIMSCriteria2 that basically
 * provides a thin wrapper around a Hibernate Criteria and allows Hibernate's wide array of search criteria,
 * aliasing and fetching to be used
 * 
 * Declared final to prevent the access control being circumvented in a subclass
 * 
 * @author Jon Diprose
 */
@Deprecated
// hibernate specific
public final class PIMSCriteriaHibernateImpl implements PIMSCriteria2 {

    /**
     * Build a criterion that builds a criterion of the form: (foo like '%keyword%' [or bar like '%keyword%'
     * [...]])
     * 
     * @param metaClass - the metaClass for which the criterion should be built
     * @param keyword - the keyword to search against
     * @return The Criterion that represents the above ... like ... or clause
     */
    public static Criterion anyLike(final MetaClass metaClass, final String keyword) {
        final String likeKeyword = "%" + keyword + "%";
        final Junction or = Restrictions.disjunction();
        for (MetaAttribute ma : metaClass.getAttributes().values()) {
            if (!ma.isDerived() && !ma.isHidden() && String.class.equals(ma.getType())) {
                or.add(Restrictions.like(ma.getName(), likeKeyword));
            }
        }
        return or;
    }

    /**
     * The ReadableVersionImpl that we will query.
     */
    private final ReadableVersionImpl rv;

    /**
     * The class of the objects for which we are searching.
     */
    private final Class<? extends ModelObject> targetJavaClass;

    /**
     * The collection of Criterion with which to restrict the search.
     */
    private final Collection<Criterion> criterions = new ArrayList<Criterion>();

    /**
     * The collection of Alias referenced by the criterions.
     */
    private final Collection<PIMSCriteria2Alias> aliases = new ArrayList<PIMSCriteria2Alias>();

    /**
     * The relationships to be fetched, mapped to how they are to be fetched.
     */
    private final Map<String, FetchMode> fetches = new HashMap<String, FetchMode>();

    /**
     * Whether or not the query is cacheable.
     */
    private boolean cacheable = true;

    /**
     * The flush mode for the query.
     */
    private FlushMode flushMode = null;

    /**
     * The paging for the query.
     */
    private Paging paging = null;

    /**
     * The timeout on the query, in seconds.
     */
    private int timeout = -1;

    /**
     * Constructor signature as in Bill's implementation.
     * 
     * @param rv - the ReadableVersion - NB Must really be a ReadableVersionImpl
     * @param targetJavaClass - the target java class for the query
     * @param paging - the paging settings for the query
     */
    public PIMSCriteriaHibernateImpl(final ReadableVersion rv,
        final Class<? extends ModelObject> targetJavaClass, final Paging paging) {
        super();
        this.rv = (ReadableVersionImpl) rv;
        this.targetJavaClass = targetJavaClass;
        setPaging(paging);
    }

    //******************************************************************************
    //******************************************************************************
    // PIMSCriteria2 Interface Implementation
    //******************************************************************************
    //******************************************************************************

    /**
     * @return The count of entities found by the query
     * @see org.pimslims.search.PIMSCriteria2#count()
     */
    public int count() {

        // Call buildCriteria direct, because we don't want paging or fetch joins
        final Criteria criteria = buildCriteria();

        // Get a project that counts distinct target class ids
        criteria.setProjection(Projections.countDistinct("id"));

        // Run the query and return the count
        return ((Number) criteria.list().get(0)).intValue();

    }

    /**
     * @return The Collection of ModelObjects returned by the query
     * @see org.pimslims.search.PIMSCriteria2#list()
     */
    @SuppressWarnings("unchecked")
    public <T extends ModelObject> Collection<T> list() {
        return getCriteria().list();
    }

    /**
     * @return The single ModelObject returned by the query, or null
     * @see org.pimslims.search.PIMSCriteria2#getSingleResult()
     */
    public ModelObject getSingleResult() {
        final Object o = getCriteria().uniqueResult();
        if ((null != o) && (o instanceof ModelObject)) {
            return (ModelObject) o;
        }
        return null;
    }

    /**
     * @return This PIMSCriteriaHibernateImpl instance
     * @see org.pimslims.search.PIMSCriteria2#add(org.hibernate.criterion.Criterion)
     */
    public PIMSCriteria2 add(final Criterion criterion) {
        this.criterions.add(criterion);
        return this;
    }

    /**
     * Delegates to {@link #createAlias(String, String, JoinType)} with joinType=JoinType.INNER_JOIN.
     * 
     * NB LabBookEntry.PROP_ACCESS is a reserved alias.
     * 
     * @param associationPath - the associationPath to be aliased
     * @param alias - the alias for the associationPath
     * @return This PIMSCriteriaHibernateImpl instance
     * @see #createAlias(String, String, org.pimslims.search.PIMSCriteria2Alias.JoinType)
     * @see org.pimslims.search.PIMSCriteria2#createAlias(String, String)
     */
    public PIMSCriteria2 createAlias(final String associationPath, final String alias) {
        return this.createAlias(associationPath, alias, JoinType.INNER_JOIN);
    }

    /**
     * Delegates to {@link #createAlias(PIMSCriteria2Alias)}.
     * 
     * NB LabBookEntry.PROP_ACCESS is a reserved alias.
     * 
     * @param associationPath - the associationPath to be aliased
     * @param alias - the alias for the associationPath
     * @param joinType - the joinType by which to join associationPath
     * @return This PIMSCriteriaHibernateImpl instance
     * @see org.pimslims.search.PIMSCriteria2#createAlias(PIMSCriteria2Alias)
     */
    public PIMSCriteria2 createAlias(final String associationPath, final String alias,
        final PIMSCriteria2Alias.JoinType joinType) {
        return this.createAlias(new PIMSCriteria2Alias(associationPath, alias, joinType));
    }

    /**
     * NB LabBookEntry.PROP_ACCESS is a reserved alias.
     * 
     * @param alias - the alias to create
     * @return This PIMSCriteriaHibernateImpl instance
     * @see org.pimslims.search.PIMSCriteria2#createAlias(PIMSCriteria2Alias)
     */
    public PIMSCriteria2 createAlias(final PIMSCriteria2Alias alias) {
        if (LabBookEntry.PROP_ACCESS.equals(alias)) {
            throw new IllegalArgumentException(LabBookEntry.PROP_ACCESS + " is a reserved alias");
        }
        this.aliases.add(alias);
        return this;
    }

    /**
     * @param cacheable - the cacheable to set
     * @return This PIMSCriteriaHibernateImpl instance
     * @see org.pimslims.search.PIMSCriteria2#setCacheable(boolean)
     */
    public PIMSCriteria2 setCacheable(final boolean cacheable) {
        this.cacheable = cacheable;
        return this;
    }

    /**
     * @param associationPath - the associationPath for the alias
     * @param alias - the alias to use for the associationPath
     * @param joinType - the joinType to use when associating the alias
     * @return This PIMSCriteriaHibernateImpl instance
     * @see org.pimslims.search.PIMSCriteria2#setFetchMode(java.lang.String, org.hibernate.FetchMode)
     */
    public PIMSCriteria2 setFetchMode(final String associationPath, final FetchMode mode) {
        this.fetches.put(associationPath, mode);
        return this;
    }

    /**
     * @param flushMode - the flushMode to set
     * @return This PIMSCriteriaHibernateImpl instance
     * @see org.pimslims.search.PIMSCriteria2#setFlushMode(org.hibernate.FlushMode)
     */
    public PIMSCriteria2 setFlushMode(final FlushMode flushMode) {
        this.flushMode = flushMode;
        return this;
    }

    /**
     * @param paging - the paging to set
     * @return This PIMSCriteriaHibernateImpl instance
     * @see org.pimslims.search.PIMSCriteria2#setPaging(org.pimslims.search.Paging)
     */
    public PIMSCriteria2 setPaging(final Paging paging) {
        this.paging = paging;
        return this;
    }

    /**
     * The timeout on the query, in seconds. Values below 0 cause the default timeout to be used. A value of
     * zero means an infinite timeout.
     * 
     * @param timeout - the timeout to set
     * @return This PIMSCriteriaHibernateImpl instance
     * @see org.pimslims.search.PIMSCriteria2#setTimeout(int)
     */
    public PIMSCriteria2 setTimeout(final int timeout) {
        this.timeout = timeout;
        return this;
    }

    //******************************************************************************
    //******************************************************************************
    // End PIMSCriteria2 Interface Implementation
    //******************************************************************************
    //******************************************************************************

    /**
     * Get the criteria with paging and fetches.
     * 
     * @return The criteria
     */
    protected Criteria getCriteria() {

        final Criteria crit = buildCriteria();

        // $JMD Performance improvement for Sample, done here to allow it to be
        // overridden by a call to this.setFetchMode
        if (org.pimslims.model.sample.Sample.class.isAssignableFrom(targetJavaClass)) {
            crit.setFetchMode("outputSample", FetchMode.JOIN);
        }
        // JMD$

        // Paging
        if (null != paging) {
            crit.setFirstResult(paging.getStart());
            crit.setMaxResults(paging.getLimit());
            for (Map.Entry<String, Order> entry : paging.getOrderBy().entrySet()) {
                final org.hibernate.criterion.Order order;
                if (Paging.Order.ASC.equals(entry.getValue())) {
                    order = org.hibernate.criterion.Order.asc(entry.getKey());
                } else {
                    order = org.hibernate.criterion.Order.desc(entry.getKey());
                }
                crit.addOrder(order);
            }
        }

        /* 
         * $JMD Bill's implementation had joinRoleNames added with a left join.
         * As far as I can see this forces a join but the joined classes are not
         * fetched, making it pointless. I am also concerned that the fetch will
         * screw up the paging. A good solution may be to only fetch if there is
         * no paging requirement, other than for OutputSample above. JMD$
         */
        else {
            if (!fetches.isEmpty()) {
                for (Map.Entry<String, FetchMode> entry : fetches.entrySet()) {
                    crit.setFetchMode(entry.getKey(), entry.getValue());
                }
            }
        }

        // Return the criteria itself, to allow scroll, etc
        return crit;

    }

    /**
     * Build a Criteria object representing our query.
     * 
     * @return The Criteria object
     */
    protected Criteria buildCriteria() {

        // Basic starting point
        final Criteria crit = rv.getSession().createCriteria(targetJavaClass);

        // If we need access control
        if (AccessControllerImpl.isSearchAccessControlNeeded(targetJavaClass) && !rv.isAdmin()) {
            ;

            // Find the role we should restrict on
            final String accessControlRoleName = rv.getAccessControlRoleName(targetJavaClass);
            if ((null != accessControlRoleName) && (0 < accessControlRoleName.length())) {
                // TODO This code untested
                crit.createAlias(accessControlRoleName + "." + LabBookEntry.PROP_ACCESS,
                    LabBookEntry.PROP_ACCESS, CriteriaSpecification.LEFT_JOIN);
            }

            // Allow objects with no access control
            Criterion accessControl = Restrictions.isNull(LabBookEntry.PROP_ACCESS);

            // If we have some read privileges
            final Collection<LabNotebook> accessObjects = rv.getReadableLabNotebooks();
            if (!accessObjects.isEmpty()) {

                // Extract access object ids
                final Collection<Long> accessObjectIds = new ArrayList<Long>(accessObjects.size());
                for (LabNotebook ao : accessObjects) {
                    accessObjectIds.add(ao.getDbId());
                }

                // OR junction of no access control and allowed access
                accessControl =
                    Restrictions.or(accessControl,
                        Restrictions.in(LabBookEntry.PROP_ACCESS + ".id", accessObjectIds));
            }

            // Add the access control sub-clause to our criteria
            crit.add(accessControl);

        }

        // If there are some aliases
        if (!aliases.isEmpty()) {
            for (PIMSCriteria2Alias alias : aliases) {
                crit.createAlias(alias.getAssociationPath(), alias.getAlias(), alias.getJoinType());
            }
        }

        // If there are some criterions
        if (!criterions.isEmpty()) {
            for (Criterion criterion : criterions) {
                crit.add(criterion);
            }
        }

        // Apply tuning parameters
        crit.setCacheable(cacheable);
        if (null != flushMode) {
            crit.setFlushMode(flushMode);
        }
        if (-1 < timeout) {
            crit.setTimeout(timeout);
        }

        // Return the criteria
        return crit;

    }

}
