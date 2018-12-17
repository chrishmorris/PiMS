/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pimslims.business.crystallization.service;

import java.util.Collection;
import org.pimslims.business.BaseService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Software;
import org.pimslims.business.exception.BusinessException;

/**
 * This Service will give access to software elements
 * @author Ian Berry
 */
public interface SoftwareService extends BaseService {

    /**
     * Find the software by its name
     * @param name - the name of the software component to find
     * @return the Software object found or null if it is not found
     * @throws org.pimslims.business.exception.BusinessException 
     */
    public Software findByName(String name) throws BusinessException;

    /**
     * 
     * @param paging
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Software> findAll(BusinessCriteria criteria) throws BusinessException;

    /**
     * Create a new software element in the database
     * @param software
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(Software software) throws BusinessException;

    /**
     * Updates a software element in the database
     * @param software
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void update(Software software) throws BusinessException;
}
