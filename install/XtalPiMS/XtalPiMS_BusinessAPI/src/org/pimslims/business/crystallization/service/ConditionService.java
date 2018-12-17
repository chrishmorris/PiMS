/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pimslims.business.crystallization.service;

import java.util.Collection;

import org.pimslims.business.BaseService;
import org.pimslims.business.ViewService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.view.ConditionView;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author ian
 */
public interface ConditionService extends BaseService, ViewService<ConditionView> {

    /**
     * 
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Condition> findAll() throws BusinessException;

    /**
     * 
     * @param criteria
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Condition> findConditions(BusinessCriteria criteria) throws BusinessException;

    /**
     * <p>
     * Get the List of Condition for the specified Screen.
     * </p>
     * 
     * @param screen - the Screen whose Condition are to be returned
     * @param paging
     * @return The List of Condition in the specified Screen
     * @throws org.pimslims.business.exception.BusinessException if an error occurs
     * 
     * @see #getConditions(String)
     * @see #getConditionsForManufacturerScreen(String)
     */
    public Collection<Condition> findConditions(Screen screen, BusinessCriteria criteria)
        throws BusinessException;

    /**
     * <p>
     * Get the List of Condition for the Screen with the specified name.
     * </p>
     * 
     * @param screenName - the name of the Screen whose Condition are to be returned
     * @param paging
     * @return The List of Condition in the Screen with the specified name
     * @throws org.pimslims.business.exception.BusinessException if an error occurs
     * 
     * @see #getConditions(Screen)
     * @see #getConditionsForManufacturerScreen(String)
     */
    public Collection<Condition> findConditions(String screenName, BusinessCriteria criteria)
        throws BusinessException;

    /**
     * <p>
     * Get the List of Condition for Screens with the specified manufacturer.
     * </p>
     * 
     * @param manufacturer - the name of the manufacturer who Screen Condition are to be returned
     * @param paging
     * @return The List of Condition in all the Screen with the specified manufacturer
     * @throws org.pimslims.business.exception.BusinessException if an error occurs
     * 
     * @see #getConditions(Screen)
     * @see #getConditions(String)
     */
    public Collection<Condition> findConditionsForManufacturerScreen(String manufacturer,
        BusinessCriteria criteria) throws BusinessException;

    /**
     * 
     * @param condition
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(Condition condition) throws BusinessException;

    public Collection<ConditionView> findViews(BusinessCriteria criteria) throws BusinessException;

  
}
