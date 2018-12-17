/*
 * ConstructService.java
 *
 * Created on 03 May 2007, 09:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.core.service;

import java.util.Collection;

import org.pimslims.business.BaseService;
import org.pimslims.business.ViewService;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.view.ConstructView;
import org.pimslims.business.exception.BusinessException;

/**
 * This Service will get the information about the construct used in an experiment. Since this information is
 * held and managed by Core PIMS, this should really just be a link to how to access this information in the
 * Core PIMS GUI - we do not want to have to reinvent the wheel to view these things!
 * 
 * @author IMB
 */
public interface ConstructService extends BaseService, ViewService<ConstructView> {
    /**
     * Because an id in the database might be a long or a string (or something else, but that can be derived
     * from a string?!
     * 
     * @param id
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Construct find(long id) throws BusinessException;

    /**
     * Finds all constructs.
     * 
     * @param paging
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Construct> findAll(BusinessCriteria criteria) throws BusinessException;

    /**
     * 
     * @param construct
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(Construct construct) throws BusinessException;

    /**
     * 
     * @param construct
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void update(Construct construct) throws BusinessException;

    /**
     * 
     * @param construct
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void close(Construct construct) throws BusinessException;

    /**
     * 
     * @param name
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Construct findByName(String name) throws BusinessException;

    /**
     * 
     * @param person
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Construct> findByUser(Person person) throws BusinessException;
}
