/**
 * xtalPiMS Business API - PIMSDB Impl org.pimslims.crystallization.dao SchedulePlanDAO.java
 * 
 * @author bl67
 * @date 25 Feb 2009
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2009 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.schedule.SchedulePlan;
import org.pimslims.model.schedule.SchedulePlanOffset;

/**
 * SchedulePlanDAO
 * 
 */
public class SchedulePlanDAO extends
    GenericDAO<SchedulePlan, org.pimslims.business.crystallization.model.SchedulePlan> {

    private final SchedulePlanOffsetDAO offsetDAO;

    /**
     * Constructor for SchedulePlanDAO
     * 
     * @param version
     */
    public SchedulePlanDAO(final ReadableVersion version) {
        super(version);
        offsetDAO = new SchedulePlanOffsetDAO(version);

    }

    /**
     * SchedulePlanDAO.createPORelated
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#createPORelated(org.pimslims.metamodel.AbstractModelObject,
     *      org.pimslims.business.XtalObject)
     */
    @Override
    protected void createPORelated(final SchedulePlan pobject,
        final org.pimslims.business.crystallization.model.SchedulePlan xobject) throws ConstraintException,
        BusinessException, ModelException {
        for (final org.pimslims.business.crystallization.model.SchedulePlanOffset xOffset : xobject
            .getOffsets()) {
            final SchedulePlanOffset poffset = offsetDAO.createPO(xOffset);
            poffset.setSchedulePlan(pobject);
        }

    }

    /**
     * SchedulePlanDAO.getFullAttributes
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#getFullAttributes(org.pimslims.business.XtalObject)
     */
    @Override
    protected Map<String, Object> getFullAttributes(
        final org.pimslims.business.crystallization.model.SchedulePlan xobject) {
        final Map<String, Object> attributes = new HashMap<String, Object>(getKeyAttributes(xobject));
        attributes.put(SchedulePlan.PROP_DETAILS, xobject.getDescription());
        return attributes;
    }

    /**
     * SchedulePlanDAO.getKeyAttributes
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#getKeyAttributes(org.pimslims.business.XtalObject)
     */
    @Override
    protected Map<String, Object> getKeyAttributes(
        final org.pimslims.business.crystallization.model.SchedulePlan xobject) {
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(SchedulePlan.PROP_NAME, xobject.getName());
        return attributes;
    }

    /**
     * SchedulePlanDAO.getPClass
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#getPClass()
     */
    @Override
    protected Class<SchedulePlan> getPClass() {
        return SchedulePlan.class;
    }

    /**
     * SchedulePlanDAO.loadXAttribute
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#loadXAttribute(org.pimslims.metamodel.AbstractModelObject)
     */
    @Override
    protected org.pimslims.business.crystallization.model.SchedulePlan loadXAttribute(
        final SchedulePlan pobject) throws BusinessException {
        final org.pimslims.business.crystallization.model.SchedulePlan xPlan =
            new org.pimslims.business.crystallization.model.SchedulePlan();
        xPlan.setName(pobject.getName());
        xPlan.setDescription(pobject.getDetails());
        return xPlan;
    }

    /**
     * SchedulePlanDAO.loadXRole
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#loadXRole(org.pimslims.business.XtalObject,
     *      org.pimslims.metamodel.AbstractModelObject)
     */
    @Override
    protected org.pimslims.business.crystallization.model.SchedulePlan loadXRole(
        final org.pimslims.business.crystallization.model.SchedulePlan xobject, final SchedulePlan pobject)
        throws BusinessException {
        final List<org.pimslims.business.crystallization.model.SchedulePlanOffset> offsets =
            new LinkedList<org.pimslims.business.crystallization.model.SchedulePlanOffset>();
        for (final SchedulePlanOffset poffset : pobject.getSchedulePlanOffsets()) {
            final org.pimslims.business.crystallization.model.SchedulePlanOffset xOffset =
                offsetDAO.getSimpleXO(poffset);
            xOffset.setSchedulePlan(xobject);
            offsets.add(xOffset);
        }
        xobject.setOffsets(offsets);
        return xobject;
    }

    /**
     * SchedulePlanDAO.updatePORelated
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#updatePORelated(org.pimslims.metamodel.AbstractModelObject,
     *      org.pimslims.business.XtalObject)
     */
    @Override
    protected void updatePORelated(final SchedulePlan pobject,
        final org.pimslims.business.crystallization.model.SchedulePlan xobject) throws ModelException {
        // TODO Auto-generated method stub

    }

}
