/**
 * xtalPiMS Business API - PIMSDB Impl org.pimslims.crystallization.dao.view AbstractSQLViewDAO.java
 * 
 * @author bl67
 * @date 23 Apr 2009
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2009 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.dao.view;

import java.util.Collection;

import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.logging.Logger;

/**
 * AbstractSQLViewDAO
 * 
 */
public abstract class AbstractSQLViewDAO<T> extends AbstractViewDAO<T> implements ViewDAO<T> {
    private static final Logger log = Logger.getLogger(AbstractSQLViewDAO.class);

    public static Long getLong(Object id) {
        if (null == id) {
            return null;
        }
        return ((Number) id).longValue();
    }

    public AbstractSQLViewDAO(final ReadableVersion version) {
        super(version);
    }

    @Override
    public Collection<T> findViews(final BusinessCriteria criteria) throws BusinessException {

        final String sql = getViewSql(criteria);

        try {
            log.debug("start find view with criteria ");
            final org.hibernate.SQLQuery sqlQuery = version.getSession().createSQLQuery(sql);
            sqlQuery.setCacheable(false);
            if (criteria != null && criteria.getMaxResults() > 0) {
                sqlQuery.setFirstResult(criteria.getFirstResult());
                sqlQuery.setMaxResults(criteria.getMaxResults());
            }
            log.debug("sqlQuery= " + sqlQuery.getQueryString());
            final Collection<T> runSearch = runSearch(sqlQuery);
            log.debug("finish findViews with criteria");
            return runSearch;
        } catch (HibernateException e) {
            log.error(sql, e);
            throw e;
        }

    }

    /**
     * AbstractViewDAO.findViewCount
     * 
     * @see org.pimslims.crystallization.dao.view.ViewDAO#findViewCount(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Integer findViewCount(final BusinessCriteria criteria) throws BusinessException {
        final String selectSQL = getViewSql(criteria);
        final String fromString = " from ";
        final int fromStringIndex = selectSQL.toLowerCase().indexOf(fromString);
        String sql = "select count (" + getCountableName() + ") " + selectSQL.substring(fromStringIndex);
        // remove orderby
        sql = (sql.split("ORDER BY"))[0];

        // build sql
        log.debug("findViewCount");
        final Query sqlQuery = version.getEntityManager().createNativeQuery(sql);
        //sqlQuery.setCacheable(true);
        log.debug("sqlQuery= " + sql);
        //sqlQuery.addScalar("count", StandardBasicTypes.INTEGER);
        return ((Number) sqlQuery.getSingleResult()).intValue();

    }

    abstract String getViewSql(BusinessCriteria criteria);

    protected String buildViewQuerySQL(final BusinessCriteria criteria, final String selectHQL,
        final String subSelect, final String accessControledMOName, final Class pClass) {
        assert null != pClass;
        final String oldQuerySQL =
            super.buildViewQueryHQL(criteria, selectHQL, subSelect, accessControledMOName, pClass);
        String newQuerySQL =
            oldQuerySQL.replace(accessControledMOName + ".access ", accessControledMOName + ".accessid ");
        newQuerySQL =
            newQuerySQL.replace(accessControledMOName + ".access.id ", accessControledMOName + ".accessid ");

        return newQuerySQL;
    }

    /**
     * 
     * AbstractViewDAO.runSearch: run the query and convert the results to views
     * 
     * @param hqlQuery
     * @return
     */
    abstract Collection<T> runSearch(org.hibernate.SQLQuery sqlQuery);

}
