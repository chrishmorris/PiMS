/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pimslims.business.crystallization.service;

import org.pimslims.business.BaseService;
import org.pimslims.business.ViewService;
import org.pimslims.business.crystallization.model.UserScore;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author ian
 */
public interface HumanScoreService extends BaseService, ViewService<ScoreView> {

    /**
     * 
     * @param id
     * @throws org.pimslims.business.exception.BusinessException
     * @return
     */
    public UserScore findHumanScore(long id) throws BusinessException;

    /**
     * 
     * @param score
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(UserScore score) throws BusinessException;
}
