/*
 * TargetService.java
 *
 * Created on 03 May 2007, 09:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.core.service;

import org.pimslims.business.BaseService;
import java.util.Collection;

import org.pimslims.business.core.model.Target;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.exception.BusinessException;

/**
 *
 * @author IMB
 */
public interface TargetService extends BaseService {
    /**
     * 
     * @param id 
     * @return
     * @throws org.pimslims.business.exception.BusinessException 
     */
    public Target find(long id) throws BusinessException;

    /**
     * 
     * @param criteria
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Target> findAll(BusinessCriteria criteria) throws BusinessException;
    /**
     * 
     * @param target 
     * @throws org.pimslims.business.exception.BusinessException 
     */
    public void create(Target target) throws BusinessException;
    /**
     * 
     * @param target 
     * @throws org.pimslims.business.exception.BusinessException 
     */
    public void update(Target target) throws BusinessException;
    /**
     * 
     * @param target 
     * @throws org.pimslims.business.exception.BusinessException 
     */
    public void close (Target target) throws BusinessException;    
}
