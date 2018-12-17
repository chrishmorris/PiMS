package org.pimslims.crystallization.dao.view;

import java.util.Collection;

import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.exception.BusinessException;

public interface ViewDAO<T> {

    /**
     * 
     * AbstractViewDAO.convertPropertyName Convert the property name of view to internal pims HQL name
     * 
     * @param propertyName
     * @return
     * @throws BusinessException
     */
    public abstract String convertPropertyName(String propertyName) throws BusinessException;

    /**
     * AbstractViewDAO.findViews search views by BusinessCriteria
     * 
     * @param criteria
     * @return
     * @throws BusinessException
     */
    public abstract Collection<T> findViews(BusinessCriteria criteria) throws BusinessException;

    /**
     * AbstractViewDAO.findViewCount get the count of views
     * 
     * @param criteria
     * @return
     * @throws BusinessException
     */
    public abstract Integer findViewCount(BusinessCriteria criteria) throws BusinessException;

}