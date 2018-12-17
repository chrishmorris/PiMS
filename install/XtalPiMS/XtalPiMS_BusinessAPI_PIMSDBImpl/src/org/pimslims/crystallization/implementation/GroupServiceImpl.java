/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.crystallization.implementation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Organisation;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.model.Project;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.core.model.Target;
import org.pimslims.business.core.service.GroupService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.view.GroupView;
import org.pimslims.business.crystallization.view.PlateExperimentView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.GroupDAO;
import org.pimslims.crystallization.dao.view.GroupViewDAO;
import org.pimslims.crystallization.dao.view.ViewDAO;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.people.PersonInGroup;

/**
 * 
 * @author ian
 */
public class GroupServiceImpl extends BaseServiceImpl implements GroupService {
    final PersonServiceImpl personService;

    final GroupDAO groupDAO;

    public GroupServiceImpl(final DataStorage dataStorage) {
        super(dataStorage);
        personService = new PersonServiceImpl(dataStorage);
        groupDAO = new GroupDAO(version);
    }

    public Group find(final long id) throws BusinessException {
        return this.find(org.pimslims.model.people.Group.class.getName() + ":" + id);
    }

    /**
     * @param id
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Group find(final String id) throws BusinessException {
        return groupDAO.getFullXO((org.pimslims.model.people.Group) version.get(id));
    }

    public Group findByNameAndOrganisation(final String name, final long organisationId)
        throws BusinessException {

        final org.pimslims.model.people.Organisation pimsOrganisation = version.get(organisationId);
        final Set<org.pimslims.model.people.Group> pimsGroups = pimsOrganisation.getGroups();

        for (final org.pimslims.model.people.Group pimsGroup : pimsGroups) {
            if (pimsGroup.get_Name().equalsIgnoreCase(name)) {
                return groupDAO.getFullXO(pimsGroup);
            }
        }
        return null;
    }

    public Person getGroupHead(final Group group) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Organisation getOrganisation(final Group group) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Group> findByGroupHead(final Person groupHead, final BusinessCriteria criteria)
        throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Group> findByOrganisation(final Organisation organisation,
        final BusinessCriteria criteria) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * REQUIRED
     * 
     * @param person
     * @param paging
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Group> findByPerson(final Person person) throws BusinessException {
        final org.pimslims.model.people.Person pPerson =
            getVersion().findFirst(org.pimslims.model.people.Person.class,
                org.pimslims.model.people.Person.PROP_FAMILYNAME, person.getFamilyName());
        return findByPiMSPerson(pPerson);

    }

    private Collection<Group> findByPiMSPerson(final org.pimslims.model.people.Person pPerson)
        throws BusinessException {
        final Collection<Group> xGroups = new HashSet<Group>();
        for (final PersonInGroup pig : pPerson.getPersonInGroups()) {
            xGroups.add(groupDAO.getFullXO(pig.getGroup()));
        }

        return xGroups;
    }

    /**
     * REQUIRED
     * 
     * @param username
     * @param paging
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Group> findByPerson(final String username) throws BusinessException {
        final User pUser = findByName(User.class, username);
        if (pUser == null) {
            return null;
        }
        return findByPiMSPerson(pUser.getPerson());
    }

    public Group findByPlate(final PlateExperimentView plateExperiment) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Group> findByPlate(final TrialPlate plate, final BusinessCriteria criteria)
        throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Group> findByPlate(final String barcode, final BusinessCriteria criteria)
        throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Group findByConstruct(final Construct construct) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Group findBySample(final Sample sample) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Group findByProject(final Project project) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Group findByTrial(final TrialDrop trial) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Group findByTarget(final Target target) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @param group
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(final Group group) throws BusinessException {
        adminOnly();
        groupDAO.createPO(group);
    }

    /**
     * 
     * @param group
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void update(final Group group) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param group
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void close(final Group group) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Group findByName(final String name) throws BusinessException {

        final org.pimslims.model.people.Group pimsGroup =
            findByName(org.pimslims.model.people.Group.class, name);
        return groupDAO.getFullXO(pimsGroup);
    }

    public Collection<Group> findAll(final BusinessCriteria criteria) throws BusinessException {
        return groupDAO.findAll();
    }

    public Collection<GroupView> findViews(final BusinessCriteria criteria) throws BusinessException {
        return getViewDAO().findViews(criteria);
    }

    public Integer findViewCount(final BusinessCriteria criteria) throws BusinessException {
        return getViewDAO().findViewCount(criteria);
    }

    public String convertPropertyName(final String property) throws BusinessException {
        return getViewDAO().convertPropertyName(property);
    }

    private ViewDAO<GroupView> viewDAO;

    private ViewDAO<GroupView> getViewDAO() {
        if (viewDAO == null) {
            viewDAO = new GroupViewDAO(version);
        }
        return viewDAO;
    }
}
