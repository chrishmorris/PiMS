/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.business.crystallization.service;

import java.util.Collection;

import org.pimslims.business.BaseService;
import org.pimslims.business.ViewService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Schedule;
import org.pimslims.business.crystallization.model.SchedulePlan;
import org.pimslims.business.crystallization.model.SchedulePlanOffset;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.view.ScheduleView;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author ian
 */
public interface ScheduleService extends BaseService, ViewService<ScheduleView> {
    /**
     * 
     * @param schedulePlan
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(SchedulePlan schedulePlan) throws BusinessException;

    /**
     * 
     * @param schedulePlan
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void update(SchedulePlan schedulePlan) throws BusinessException;

    /**
     * 
     * @param schedulePlanOffset
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(SchedulePlanOffset schedulePlanOffset) throws BusinessException;

    /**
     * 
     * @param schedulePlanOffset
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void update(SchedulePlanOffset schedulePlanOffset) throws BusinessException;

    /**
     * 
     * @param schedule
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(Schedule schedule) throws BusinessException;

    /**
     * 
     * @param schedule
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void update(Schedule schedule) throws BusinessException;

    /**
     * 
     * @param name
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<SchedulePlan> findSchedulePlans(BusinessCriteria criteria) throws BusinessException;

    /**
     * 
     * @param plate
     * @param plan
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void createSchedule(TrialPlate plate, SchedulePlan plan) throws BusinessException;

    public SchedulePlan findSchedulePlanByName(String name) throws BusinessException;

}
