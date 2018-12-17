/*
 * ScoringSchemeService.java
 *
 * Created on 07 July 2007, 00:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pimslims.business.crystallization.service;

import java.util.Collection;
import org.pimslims.business.BaseService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.ScoringScheme;
import org.pimslims.business.exception.BusinessException;

/**
 *
 * @author IMB
 */
public interface ScoringSchemeService extends BaseService {

    /**
     * 
     * @param id 
     * @return
     * @throws org.pimslims.business.exception.BusinessException 
     */
    public ScoringScheme find(long id) throws BusinessException;

    /**
     * 
     * @param name
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public ScoringScheme findByName (String name) throws BusinessException;
    /**
     * Gets all the available scoring schemes
     * @param paging
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<ScoringScheme> findAll(BusinessCriteria criteria) throws BusinessException;

    /**
     * 
     * @param scoringScheme 
     * @throws org.pimslims.business.exception.BusinessException 
     */
    public void create(ScoringScheme scoringScheme) throws BusinessException;

    /**
     * 
     * @param scoringScheme 
     * @throws org.pimslims.business.exception.BusinessException 
     */
    public void update(ScoringScheme scoringScheme) throws BusinessException;
}
