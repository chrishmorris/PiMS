/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.business;

import java.util.Collection;

import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.PropertyNameConvertor;
import org.pimslims.business.exception.BusinessException;

/**
 * This interface should be used by any of the services that will return views... It could have been combined
 * with the PropertyNameConverter, as you will always require both, but I wanted to keep the criteria things
 * independent of the rest so that at some stage it could be extracted out as a separate project for use in
 * other code!
 * 
 * @author Ian Berry
 */
public interface ViewService<T> extends PropertyNameConvertor<T> {
    /**
     * Gets the row count of the query
     * 
     * @param criteria
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Integer findViewCount(BusinessCriteria criteria) throws BusinessException;

    /**
     * Gets the results of the quesry
     * 
     * @param criteria
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<T> findViews(BusinessCriteria criteria) throws BusinessException;

}
