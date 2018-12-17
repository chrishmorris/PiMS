/*
 * OrganisationService.java
 *
 * Created on 02 May 2007, 15:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pimslims.business.core.service;

import org.pimslims.business.BaseService;
import java.util.Collection;
import org.pimslims.business.core.model.Organisation;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.exception.BusinessException;

/**
 *
 * @author IMB
 */
public interface OrganisationService extends BaseService {

    /**
     * Get information on a specific institute.
     * @return information about the institute.
     * @param id 
     * @throws org.pimslims.business.exception.BusinessException 
     */
    public Organisation find(long id) throws BusinessException;

    /**
     * Finds and organisation by name.
     * @param name
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Organisation findByName(String name) throws BusinessException;
    
    /**
     * Insert new institute in the DB.
     * 
     * @param organisation 
     * @throws org.pimslims.business.exception.BusinessException 
     */
    public void create(Organisation organisation) throws BusinessException;

    /**
     * 
     * @param organisation 
     * @throws org.pimslims.business.exception.BusinessException 
     */
    public void update(Organisation organisation) throws BusinessException;

    /**
     * Finds all the organisations.
     * @param criteria
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Organisation> findAll(BusinessCriteria criteria) throws BusinessException;
}
