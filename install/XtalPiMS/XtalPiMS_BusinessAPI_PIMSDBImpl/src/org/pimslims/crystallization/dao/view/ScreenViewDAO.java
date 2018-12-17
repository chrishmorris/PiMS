/**
 * xtalPiMSImpl org.pimslims.crystallization.dao.view ScreeViewDAO.java
 * 
 * @author bl67
 * @date 21 May 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.dao.view;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.PropertyNameConvertor;
import org.pimslims.business.crystallization.view.ScreenView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.ScreenDAO;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.holder.RefHolder;

/**
 * ScreeViewDAO
 * 
 */
public class ScreenViewDAO extends AbstractViewDAO<ScreenView> implements ViewDAO<ScreenView>,
    PropertyNameConvertor<ScreenView> {

    /**
     * Constructor for ScreeViewDAO
     * 
     * @param version
     */
    public ScreenViewDAO(final ReadableVersion version) {
        super(version);
    }

    /**
     * ScreeViewDAO.convertPropertyName
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#convertPropertyName(java.lang.String)
     */
    @Override
    public String convertPropertyName(final String propertyName) throws BusinessException {
        if (propertyName == null) {
            throw new IllegalArgumentException("Null filter name");
        }
        if (propertyName.equals(ScreenView.PROP_NAME)) {
            return "refHolder.name";
        } else if (propertyName.equals(ScreenView.PROP_TYPE)) {
            return "holderCategorie.name";
        } else if (propertyName.equals(ScreenView.PROP_MANUFACTURER_NAME)) {
            return "supplier.name";
        }
        throw new BusinessException("Unable to find matching property in " + this.getClass());

    }

    /**
     * ScreeViewDAO.getCountableName
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#getCountableName()
     */
    @Override
    String getCountableName() {
        return "refHolder";
    }

    /**
     * ScreeViewDAO.getRootClass
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#getRootClass()
     */
    @Override
    Class getRootClass() {
        return RefHolder.class;
    }

    /**
     * ScreeViewDAO.getViewHQL
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#getViewHQL(org.pimslims.business.criteria.BusinessCriteria)
     */
    public String getViewReallyHQL(final BusinessCriteria criteria) {
        final String selectHQL =
            "select refHolder.name,holderCategorie.name,supplier.name"
                + " from "
                + RefHolder.class.getName()
                + " as refHolder left join refHolder.holderCategories as holderCategorie left join refHolder.refHolderSources as refHolderSource"
                + " left join refHolderSource.supplier as supplier ";
        final String subSelect =
            "holderCategorie.name!='" + ScreenDAO.HOLDER_CATEGORY_NAME
                + "' and refHolder.dbId in (select rh.dbId from " + RefHolder.class.getName()
                + " as rh left join rh.holderCategories as hc where hc.name='"
                + ScreenDAO.HOLDER_CATEGORY_NAME + "')";
        return buildViewQueryHQL(criteria, selectHQL, subSelect, "refHolder", RefHolder.class);
    }

    /**
     * ScreeViewDAO.runSearch
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#runSearch(org.hibernate.Query)
     */
    Collection<ScreenView> runHqlSearch(final Query hqlQuery) {
        final Collection<ScreenView> views = new LinkedList<ScreenView>();
        final List<?> results = hqlQuery.list();
        // int j = 0;
        for (final Object object : results) {
            final Object result[] = (Object[]) object;

            final ScreenView view = new ScreenView();
            view.setName((String) result[0]);
            final String pimsScreenTypeName = (String) result[1];
            view.setScreenType(ScreenDAO.getXtalScreenType(pimsScreenTypeName).toString());
            view.setManufacturerName((String) result[2]);
            views.add(view);
        }
        return views;
    }

    @Override
    public Collection<ScreenView> findViews(BusinessCriteria criteria) throws BusinessException {
        return doFindViews(criteria);
    }

    /**
     * AbstractViewDAO.findViews
     * 
     * @see org.pimslims.crystallization.dao.view.ViewDAO#findViews(org.pimslims.business.criteria.BusinessCriteria)
     */
    private Collection doFindViews(final BusinessCriteria criteria) throws BusinessException {

        // build HQL
        log.debug("start find view with criteria ");
        final String hql = getViewReallyHQL(criteria);
        final org.hibernate.Query hqlQuery = version.getSession().createQuery(hql).setCacheable(true);
        if (criteria != null && criteria.getMaxResults() > 0) {
            hqlQuery.setFirstResult(criteria.getFirstResult());
            hqlQuery.setMaxResults(criteria.getMaxResults());
        }
        log.debug("hqlQuery= " + hqlQuery.getQueryString());
        final Collection runSearch = runHqlSearch(hqlQuery);
        log.debug("finish findViews with criteria");
        return runSearch;

    }

    @Override
    public Integer findViewCount(BusinessCriteria criteria) throws BusinessException {
        return doFindViewCount(criteria);
    }

    /**
     * AbstractViewDAO.findViewCount
     * 
     * @see org.pimslims.crystallization.dao.view.ViewDAO#findViewCount(org.pimslims.business.criteria.BusinessCriteria)
     */
    private Integer doFindViewCount(final BusinessCriteria criteria) throws BusinessException {
        final String selectHQL = getViewReallyHQL(criteria);
        final String fromString = " from " + getRootClass().getName() + " ";
        final int fromStringIndex = selectHQL.indexOf(fromString);
        String countHQL =
            "select count (distinct " + getCountableName() + ") " + selectHQL.substring(fromStringIndex);
        // remove orderby
        countHQL = (countHQL.split("ORDER BY"))[0];

        // build HQL
        log.debug("findViewCount");
        final org.hibernate.Query hqlQuery = version.getSession().createQuery(countHQL).setCacheable(true);
        log.debug("hqlQuery= " + hqlQuery.getQueryString());
        return ((Number) hqlQuery.list().iterator().next()).intValue();

    }

}
