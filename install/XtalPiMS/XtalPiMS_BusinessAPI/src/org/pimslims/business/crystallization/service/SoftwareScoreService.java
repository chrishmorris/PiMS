/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pimslims.business.crystallization.service;

import java.util.Collection;

import org.pimslims.business.BaseService;
import org.pimslims.business.ViewService;
import org.pimslims.business.crystallization.model.SoftwareScore;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author ian
 */
public interface SoftwareScoreService extends BaseService, ViewService<ScoreView> {

    /**
     * Utility Method to Insert a set of scores for a specific plate (used for automatic scoring). Parameters
     * 
     * @return the list of scores
     * @param scores a list of the scores to add to the database
     * @throws org.pimslims.business.exception.BusinessException
     * 
     */
    public void create(Collection<SoftwareScore> scores) throws BusinessException;

    /**
     * Insert score for a specific position in a plate (used for manual scoring). 
     * Parameters
     * 
     * @throws org.pimslims.business.exception.BusinessException
     * @param score
     * 
     * 
     */
    public void create(SoftwareScore score) throws BusinessException;

    /**
     * 
     * @param id
     * @throws org.pimslims.business.exception.BusinessException
     * @return
     */
    public SoftwareScore findSoftwareScore(long id) throws BusinessException;

 }
