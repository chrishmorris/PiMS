package org.pimslims.crystallization.dao;

import java.util.HashMap;
import java.util.Map;

import org.pimslims.business.crystallization.model.SchedulePlan;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.schedule.SchedulePlanOffset;

public class SchedulePlanOffsetDAO extends
    GenericDAO<SchedulePlanOffset, org.pimslims.business.crystallization.model.SchedulePlanOffset> {
    static long HOURTOMS = 1000 * 60 * 60;

    public SchedulePlanOffsetDAO(final ReadableVersion version) {
        super(version);
    }

    @Override
    protected void createPORelated(final SchedulePlanOffset pobject,
        final org.pimslims.business.crystallization.model.SchedulePlanOffset xobject)
        throws ConstraintException, BusinessException, ModelException {

    }

    @Override
    protected Map<String, Object> getFullAttributes(
        final org.pimslims.business.crystallization.model.SchedulePlanOffset xobject) {
        final Map<String, Object> attributes = new HashMap<String, Object>(getKeyAttributes(xobject));
        attributes.put(SchedulePlanOffset.PROP_PRIORITY, xobject.getPriority());
        return attributes;
    }

    @Override
    protected Map<String, Object> getKeyAttributes(
        final org.pimslims.business.crystallization.model.SchedulePlanOffset xobject) {
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(SchedulePlanOffset.PROP_OFFSETTIME, xobject.getOffsetHoursFromTimeZero() * HOURTOMS);
        final SchedulePlanDAO planDAO = new SchedulePlanDAO(version);
        attributes.put(SchedulePlanOffset.PROP_SCHEDULEPLAN, planDAO.getPO(xobject.getSchedulePlan()));
        return attributes;
    }

    @Override
    protected Class<SchedulePlanOffset> getPClass() {
        return SchedulePlanOffset.class;
    }

    @Override
    protected org.pimslims.business.crystallization.model.SchedulePlanOffset loadXAttribute(
        final SchedulePlanOffset pobject) throws BusinessException {
        final org.pimslims.business.crystallization.model.SchedulePlanOffset xobject =
            new org.pimslims.business.crystallization.model.SchedulePlanOffset();
        xobject.setOffsetHoursFromTimeZero(new Long(pobject.getOffsetTime() / HOURTOMS).intValue());
        xobject.setPriority(pobject.getPriority());
        return xobject;
    }

    @Override
    protected org.pimslims.business.crystallization.model.SchedulePlanOffset loadXRole(
        final org.pimslims.business.crystallization.model.SchedulePlanOffset xobject,
        final SchedulePlanOffset pobject) throws BusinessException {
        final SchedulePlanDAO planDAO = new SchedulePlanDAO(version);
        final SchedulePlan schedulePlan = planDAO.getSimpleXO(pobject.getSchedulePlan());
        xobject.setSchedulePlan(schedulePlan);
        return xobject;
    }

    @Override
    protected void updatePORelated(final SchedulePlanOffset pobject,
        final org.pimslims.business.crystallization.model.SchedulePlanOffset xobject) throws ModelException {

    }

}
