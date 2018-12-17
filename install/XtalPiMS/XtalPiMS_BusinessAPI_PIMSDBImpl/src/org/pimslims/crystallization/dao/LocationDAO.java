/**
 * xtalPiMSImpl org.pimslims.crystallization.dao ConstructDAO.java
 * 
 * @author bl67
 * @date 11 Mar 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 bl67 This library is free software; you can redistribute it and/or modify it
 *           under the terms of the GNU Lesser General Public License as published by the Free Software
 *           Foundation; either version 2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */
package org.pimslims.crystallization.dao;

import java.util.HashMap;
import java.util.Map;

import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.experiment.Instrument;

/**
 * TODO map this to Instrument, not Location
 * 
 */
public class LocationDAO extends GenericDAO<Instrument, org.pimslims.business.core.model.Location> {

    /**
     * @param version
     */
    public LocationDAO(final ReadableVersion version) {
        super(version);
    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#createPORelated(org.pimslims.metamodel.AbstractModelObject,
     *      org.pimslims.business.XtalObject)
     */
    @Override
    protected void createPORelated(final Instrument pobject,
        final org.pimslims.business.core.model.Location xobject) throws ConstraintException,
        BusinessException, ModelException {
        // nothing

    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#getFullAttributes(org.pimslims.business.XtalObject)
     */
    @Override
    protected Map<String, Object> getFullAttributes(final org.pimslims.business.core.model.Location xobject) {
        final Map<String, Object> attributes = new HashMap<String, Object>(getKeyAttributes(xobject));

        attributes.put(org.pimslims.model.experiment.Instrument.PROP_TEMPERATURE, new Float(xobject
            .getTemperature()));
        attributes.put(org.pimslims.model.experiment.Instrument.PROP_PRESSURE, new Float(xobject
            .getPressure()));
        return attributes;
    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#getKeyAttributes(org.pimslims.business.XtalObject)
     */
    @Override
    protected Map<String, Object> getKeyAttributes(final org.pimslims.business.core.model.Location xobject) {
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.experiment.Instrument.PROP_NAME, xobject.getName());
        return attributes;
    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#getPClass()
     */
    @Override
    protected Class<Instrument> getPClass() {
        return Instrument.class;
    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#loadXAttribute(org.pimslims.metamodel.AbstractModelObject)
     */
    @Override
    protected org.pimslims.business.core.model.Location loadXAttribute(final Instrument pobject)
        throws BusinessException {

        final org.pimslims.business.core.model.Location location =
            new org.pimslims.business.core.model.Location();
        location.setId(pobject.getDbId());
        location.setName(pobject.getName());
        if (pobject.getPressure() != null) {
            location.setPressure(pobject.getPressure());
        }
        if (pobject.getTemperature() != null) {
            location.setTemperature(pobject.getTemperature());
        }
        return location;
    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#updatePORelated(org.pimslims.metamodel.AbstractModelObject,
     *      org.pimslims.business.XtalObject)
     */
    @Override
    protected void updatePORelated(final Instrument pobject,
        final org.pimslims.business.core.model.Location xobject) throws ModelException {
        // nothing

    }

    @Override
    protected org.pimslims.business.core.model.Location loadXRole(
        final org.pimslims.business.core.model.Location xobject, final Instrument pobject)
        throws BusinessException {
        return xobject;
    }

}
