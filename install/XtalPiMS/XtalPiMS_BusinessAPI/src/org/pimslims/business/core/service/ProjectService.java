/*
 * ProjectService.java
 *
 * Created on 03 May 2007, 09:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.core.service;

import java.util.Collection;

import org.pimslims.business.BaseService;
import org.pimslims.business.core.model.Project;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author IMB
 */
public interface ProjectService extends BaseService {
    /**
     * 
     * @param id
     * @throws org.pimslims.business.exception.BusinessException
     * @return
     */
    public Project find(long id) throws BusinessException;

    /**
     * 
     * @param criteria
     * @throws org.pimslims.business.exception.BusinessException
     * @return
     */
    public Collection<Project> findAll(BusinessCriteria criteria) throws BusinessException;

    /**
     * 
     * @param project
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(Project project) throws BusinessException;

    /**
     * 
     * @param project
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void update(Project project) throws BusinessException;

    /**
     * 
     * @param project
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void close(Project project) throws BusinessException;

    /**
     * 
     * @param name
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Project findByName(String name) throws BusinessException;

}
