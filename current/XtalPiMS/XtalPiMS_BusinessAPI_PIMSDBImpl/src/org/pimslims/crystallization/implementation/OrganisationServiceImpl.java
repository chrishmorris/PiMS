/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.crystallization.implementation;

import java.util.Collection;

import org.pimslims.access.Access;
import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Location;
import org.pimslims.business.core.model.Organisation;
import org.pimslims.business.core.model.WebService;
import org.pimslims.business.core.service.OrganisationService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.GroupDAO;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;

/**
 * 
 * @author ian
 */
public class OrganisationServiceImpl extends BaseServiceImpl implements OrganisationService {

    public OrganisationServiceImpl(final DataStorage baseStorage) {
        super(baseStorage);
    }

    public Organisation find(final long id) throws BusinessException {
        return this.find(org.pimslims.model.people.Organisation.class.getName() + ":" + id);
    }

    public Organisation find(final String id) throws BusinessException {
        return GroupDAO.getXtalOrganisation((org.pimslims.model.people.Organisation) version.get(id));
    }

    /**
     * @param name
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Organisation findByName(final String name) throws BusinessException {
        return getXtalOrganisation(this.version, name);
    }

    /**
     * @TODO REQUIRED
     * @param organisation
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Group> getGroups(final Organisation organisation, final BusinessCriteria criteria)
        throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<WebService> getWebServices(final Organisation organisation,
        final BusinessCriteria criteria) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Location> getLocations(final Organisation organisation, final BusinessCriteria criteria)
        throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Organisation findByLocation(final Location location) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @param organisation
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(final Organisation organisation) throws BusinessException {
        if (!Access.ADMINISTRATOR.equals(version.getUsername())) {
            throw new AssertionError("Only the administrator can record new organisation");
        }
        try {
            final org.pimslims.model.people.Organisation po =
                new org.pimslims.model.people.Organisation((WritableVersion) version, organisation.getName());
            organisation.setId(po.getDbId());
            //TODO save attributes
        } catch (final ConstraintException e) {
            // unacceptable values, e.g. user name already exists
            throw new BusinessException(e);
        }
    }

    /**
     * @TODO REQUIRED
     * @param organisation
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void update(final Organisation organisation) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @param name
     * @return
     */
    static Organisation getXtalOrganisation(final ReadableVersion version, final String name) {
        final org.pimslims.model.people.Organisation pimsOrganisation =
            version.findFirst(org.pimslims.model.people.Organisation.class,
                org.pimslims.model.people.Organisation.PROP_NAME, name);

        if (null == pimsOrganisation) {
            return null;
        }
        return GroupDAO.getXtalOrganisation(pimsOrganisation);
    }

    public Collection<Organisation> findAll(final BusinessCriteria criteria) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
