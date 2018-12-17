package org.pimslims.crystallization.implementation;

import java.util.Collection;
import java.util.List;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.model.Project;
import org.pimslims.business.core.service.PersonService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.PersonDAO;
import org.pimslims.exception.ModelException;

public class PersonServiceImpl extends BaseServiceImpl implements PersonService {
    private final PersonDAO personDAO;

    public PersonServiceImpl(final DataStorage baseStorage) {
        super(baseStorage);
        personDAO = new PersonDAO(getVersion());
    }

    public void close(final Person user) throws BusinessException {
        throw new UnsupportedOperationException("This method is not implemented");
    }

    /**
     * @param user
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(final Person user) throws BusinessException {
        adminOnly();
        try {
            personDAO.createPO(user);
        } catch (final Exception e) {
            System.out.println(user + " has problem!");
            throw new BusinessException(e);
        }
    }

    public Person find(final long userId) throws BusinessException {
        return this.find(org.pimslims.model.people.Person.class.getName() + ":" + userId);
    }

    public Person find(final String userId) throws BusinessException {
        return personDAO.getxtalPerson(userId, false);
    }

    public List<Person> findByFamilyName(final String familyName, final BusinessCriteria criteria)
        throws BusinessException {
        // TODO implement
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public List<Person> findByGivenName(final String givenName, final BusinessCriteria criteria)
        throws BusinessException {
        // TODO implement
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public List<Person> findByName(final String givenName, final String familyName,
        final BusinessCriteria criteria) throws BusinessException {
        // TODO implement
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public Person findByUsername(final String username) throws BusinessException {
        return personDAO.getxtalPerson(username, false);
    }

    public List<Group> findGroups(final Person person, final BusinessCriteria criteria)
        throws BusinessException {
        // LATER implement
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public Person findLocalContact(final Project project) throws BusinessException {
        // LATER implement
        throw new UnsupportedOperationException("This method is not implemented");
    }

    /**
     * REQUIRED
     * 
     * @param user
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void update(final Person xPerson) throws BusinessException {
        personDAO.updatePO(xPerson);
    }

    public Person findGroupHead(final Group group) throws BusinessException {
        // LATER Auto-generated method stub
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public List<Person> findByGroup(final Group group, final BusinessCriteria criteria)
        throws BusinessException {
        // LATER Auto-generated method stub
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public Person findOwner(final Project project) throws BusinessException {
        // LATER implement
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public void putPersonInGroup(final Person xPerson, final Group xGroup) throws BusinessException {
        try {
            personDAO.putPersonInGroup(xPerson, xGroup);
        } catch (final ModelException e) {
            throw new BusinessException(e);
        }

    }

    public Collection<Person> findAll(final BusinessCriteria criteria) throws BusinessException {
        return personDAO.findAll();
    }
}
