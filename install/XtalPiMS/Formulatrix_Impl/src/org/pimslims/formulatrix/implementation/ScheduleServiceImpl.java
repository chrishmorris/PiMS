/**
 *  ScheduleServiceImpl.java
 * 
 * @author cm65
 * @date 21 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.formulatrix.implementation;

import java.util.Collection;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Schedule;
import org.pimslims.business.crystallization.model.SchedulePlan;
import org.pimslims.business.crystallization.model.SchedulePlanOffset;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.service.ScheduleService;
import org.pimslims.business.crystallization.view.ScheduleView;
import org.pimslims.business.exception.BusinessException;

/**
 * ScheduleServiceImpl
 * 
 */
public class ScheduleServiceImpl implements ScheduleService {

    private final ManufacturerConnection connection;

    /**
     * Constructor for ScheduleServiceImpl
     * 
     * @param mfrConnection
     */
    public ScheduleServiceImpl(ManufacturerConnection mfrConnection) {
        this.connection = mfrConnection;
    }

    /**
     * ScheduleServiceImpl.create
     * 
     * @see org.pimslims.business.crystallization.service.ScheduleService#create(org.pimslims.business.crystallization.model.SchedulePlan)
     */
    @Override
    public void create(SchedulePlan schedulePlan) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScheduleServiceImpl.create
     * 
     * @see org.pimslims.business.crystallization.service.ScheduleService#create(org.pimslims.business.crystallization.model.SchedulePlanOffset)
     */
    @Override
    public void create(SchedulePlanOffset schedulePlanOffset) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScheduleServiceImpl.create
     * 
     * @see org.pimslims.business.crystallization.service.ScheduleService#create(org.pimslims.business.crystallization.model.Schedule)
     */
    @Override
    public void create(Schedule schedule) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScheduleServiceImpl.createSchedule
     * 
     * @see org.pimslims.business.crystallization.service.ScheduleService#createSchedule(org.pimslims.business.crystallization.model.TrialPlate,
     *      org.pimslims.business.crystallization.model.SchedulePlan)
     */
    @Override
    public void createSchedule(TrialPlate plate, SchedulePlan plan) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScheduleServiceImpl.findSchedulePlanByName
     * 
     * @see org.pimslims.business.crystallization.service.ScheduleService#findSchedulePlanByName(java.lang.String)
     */
    @Override
    public SchedulePlan findSchedulePlanByName(String name) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScheduleServiceImpl.findSchedulePlans
     * 
     * @see org.pimslims.business.crystallization.service.ScheduleService#findSchedulePlans(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<SchedulePlan> findSchedulePlans(BusinessCriteria criteria) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScheduleServiceImpl.update
     * 
     * @see org.pimslims.business.crystallization.service.ScheduleService#update(org.pimslims.business.crystallization.model.SchedulePlan)
     */
    @Override
    public void update(SchedulePlan schedulePlan) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScheduleServiceImpl.update
     * 
     * @see org.pimslims.business.crystallization.service.ScheduleService#update(org.pimslims.business.crystallization.model.SchedulePlanOffset)
     */
    @Override
    public void update(SchedulePlanOffset schedulePlanOffset) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScheduleServiceImpl.update
     * 
     * @see org.pimslims.business.crystallization.service.ScheduleService#update(org.pimslims.business.crystallization.model.Schedule)
     */
    @Override
    public void update(Schedule schedule) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScheduleServiceImpl.getDataStorage
     * 
     * @see org.pimslims.business.BaseService#getDataStorage()
     */
    @Override
    public DataStorage getDataStorage() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScheduleServiceImpl.findViewCount
     * 
     * @see org.pimslims.business.ViewService#findViewCount(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Integer findViewCount(BusinessCriteria criteria) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScheduleServiceImpl.findViews
     * 
     * @see org.pimslims.business.ViewService#findViews(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<ScheduleView> findViews(BusinessCriteria criteria) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ScheduleServiceImpl.convertPropertyName
     * 
     * @see org.pimslims.business.criteria.PropertyNameConvertor#convertPropertyName(java.lang.String)
     */
    @Override
    public String convertPropertyName(String property) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

}
