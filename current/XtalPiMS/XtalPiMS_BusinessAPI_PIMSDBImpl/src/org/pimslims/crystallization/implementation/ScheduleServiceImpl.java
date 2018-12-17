/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.crystallization.implementation;

import java.util.ArrayList;
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
import org.pimslims.crystallization.dao.SchedulePlanDAO;
import org.pimslims.crystallization.dao.SchedulePlanOffsetDAO;

/**
 * 
 * @author ian
 */
public class ScheduleServiceImpl extends BaseServiceImpl implements ScheduleService {

    private final SchedulePlanDAO planDAO;

    private final SchedulePlanOffsetDAO offsetDAO;

    public ScheduleServiceImpl(final DataStorage baseStorage) {
        super(baseStorage);
        planDAO = new SchedulePlanDAO(this.version);
        offsetDAO = new SchedulePlanOffsetDAO(this.version);
    }

    public void create(final SchedulePlan schedulePlan) throws BusinessException {
        planDAO.createPO(schedulePlan);
    }

    public void update(final SchedulePlan schedulePlan) throws BusinessException {
        planDAO.updatePO(schedulePlan);
    }

    public void create(final SchedulePlanOffset schedulePlanOffset) throws BusinessException {
        offsetDAO.createPO(schedulePlanOffset);
    }

    public void update(final SchedulePlanOffset schedulePlanOffset) throws BusinessException {
        offsetDAO.updatePO(schedulePlanOffset);
    }

    public void create(final Schedule schedule) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void update(final Schedule schedule) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SchedulePlan findByName(final String name) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void createSchedule(final TrialPlate plate, final SchedulePlan plan) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<SchedulePlan> findAllSchedulePlans(final BusinessCriteria criteria)
        throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Schedule> findAllSchedules(final BusinessCriteria criteria) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<SchedulePlan> findSchedulePlans(final BusinessCriteria criteria)
        throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Integer findViewCount(final BusinessCriteria criteria) throws BusinessException {
        // TODO Auto-generated method stub
        return 0;
    }

    public Collection<ScheduleView> findViews(final BusinessCriteria criteria) throws BusinessException {
        // TODO Auto-generated method stub
        return new ArrayList<ScheduleView>();
    }

    public String convertPropertyName(final String property) throws BusinessException {
        // TODO Auto-generated method stub
        return property;
    }

    public SchedulePlan findSchedulePlanByName(final String name) throws BusinessException {
        final org.pimslims.model.schedule.SchedulePlan pPlan =
            version.findFirst(org.pimslims.model.schedule.SchedulePlan.class,
                org.pimslims.model.schedule.SchedulePlan.PROP_NAME, name);
        return planDAO.getFullXO(pPlan);
    }

}
