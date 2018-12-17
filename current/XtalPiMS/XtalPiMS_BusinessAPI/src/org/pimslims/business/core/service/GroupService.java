/*
 * GroupService.java
 *
 * Created on 30 April 2007, 23:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.core.service;

import java.util.Collection;

import org.pimslims.business.BaseService;
import org.pimslims.business.ViewService;
import org.pimslims.business.core.model.Group;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.view.GroupView;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author IMB
 */
public interface GroupService extends BaseService, ViewService<GroupView> {
    /**
     * Get information on a specific group.
     * 
     * @return information about the group.
     * @param id
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Group find(long id) throws BusinessException;

    /**
     * 
     * @param paging
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Group> findAll(BusinessCriteria criteria) throws BusinessException;

    /**
     * Insert new group in the DB.
     * 
     * @param group The group information
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(Group group) throws BusinessException;

    /**
     * 
     * @param group
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void update(Group group) throws BusinessException;

    /**
     * 
     * @param group
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void close(Group group) throws BusinessException;

    /**
     * 
     * @param name
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Group findByName(String name) throws BusinessException;

    /**
     * 
     * @param username
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Group> findByPerson(String username) throws BusinessException;
    
    /**
     * 
     * @param name
     * @param organisationId
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Group findByNameAndOrganisation(String name, long organisationId) throws BusinessException;

}
