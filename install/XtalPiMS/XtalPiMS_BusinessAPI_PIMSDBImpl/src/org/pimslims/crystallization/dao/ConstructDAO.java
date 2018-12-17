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

import org.pimslims.business.core.model.Construct;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.target.ResearchObjective;

/**
 * ConstructDAO
 * 
 */
public class ConstructDAO extends
    GenericDAO<org.pimslims.model.target.ResearchObjective, org.pimslims.business.core.model.Construct> {

    /**
     * @param version
     */
    public ConstructDAO(final ReadableVersion version) {
        super(version);
    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#createPORelated(org.pimslims.metamodel.AbstractModelObject,
     *      org.pimslims.business.XtalObject)
     */
    @Override
    protected void createPORelated(final ResearchObjective pobject, final Construct xobject)
        throws ConstraintException, BusinessException, ModelException {
        // nothing

    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#getFullAttributes(org.pimslims.business.XtalObject)
     */
    @Override
    protected Map<String, Object> getFullAttributes(final Construct xobject) {
        final Map<String, Object> attributes = new HashMap<String, Object>(getKeyAttributes(xobject));
        attributes.put(ResearchObjective.PROP_WHYCHOSEN, "Construct");
        attributes.put(ResearchObjective.PROP_DETAILS, xobject.getDescription());
        final PersonDAO pDAO = new PersonDAO(version);
        final User owner = pDAO.getUser(xobject.getOwner());
        if (owner != null) {
            attributes.put(ResearchObjective.PROP_OWNER, owner);
        }

        return attributes;
    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#getKeyAttributes(org.pimslims.business.XtalObject)
     */
    @Override
    protected Map<String, Object> getKeyAttributes(final Construct xobject) {
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(ResearchObjective.PROP_COMMONNAME, xobject.getName());
        return attributes;
    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#getPClass()
     */
    @Override
    protected Class<ResearchObjective> getPClass() {
        return ResearchObjective.class;
    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#loadXAttribute(org.pimslims.metamodel.AbstractModelObject)
     */
    @Override
    protected Construct loadXAttribute(final ResearchObjective pobject) throws BusinessException {

        final Construct construct = new Construct(pobject.getCommonName(), null);
        construct.setDescription(pobject.getDetails());

        return construct;
    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#loadXRole(org.pimslims.business.XtalObject,
     *      org.pimslims.metamodel.AbstractModelObject)
     */
    @Override
    protected Construct loadXRole(final Construct xobject, final ResearchObjective pobject)
        throws BusinessException {
        final PersonDAO pDAO = new PersonDAO(version);
        if (null != pobject.getOwner()) {
            final org.pimslims.business.core.model.Person xtalPerson = pDAO.getSimpleXO(pobject.getOwner());
            xobject.setOwner(xtalPerson);
        }
        return xobject;
    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#updatePORelated(org.pimslims.metamodel.AbstractModelObject,
     *      org.pimslims.business.XtalObject)
     */
    @Override
    protected void updatePORelated(final ResearchObjective pobject, final Construct xobject)
        throws ModelException {
        // nothing

    }

}
