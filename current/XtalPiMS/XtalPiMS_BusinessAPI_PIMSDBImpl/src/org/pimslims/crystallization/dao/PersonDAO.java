package org.pimslims.crystallization.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.people.PersonInGroup;

/**
 * <p>
 * Data Access Object for {@link org.pimslims.business.core.model.Person}
 * </p>
 * 
 * @author Bill Lin
 */
public class PersonDAO extends
    GenericDAO<org.pimslims.model.people.Person, org.pimslims.business.core.model.Person> {
    public PersonDAO(final ReadableVersion version) {
        super(version);
    }

    /**
     * get xPerson by userName
     * 
     * @param userName
     * @param isSimple if true return simple XO, else return full XO
     * @return
     * @throws BusinessException
     */
    public Person getxtalPerson(final String userName, final boolean isSimple) throws BusinessException {
        if (isSimple) {
            return getSimpleXO(getpimsPerson(userName));
        } else {
            return getFullXO(getpimsPerson(userName));
        }
    }

    /**
     * find pPerson by username
     * 
     * @param userName
     * @return
     */
    public org.pimslims.model.people.Person getpimsPerson(final String userName) {
        final org.pimslims.model.accessControl.User user =
            version.findFirst(org.pimslims.model.accessControl.User.class,
                org.pimslims.model.accessControl.User.PROP_NAME, userName);
        if (null == user) {
            return null;
        }
        return user.getPerson();

    }

    @Override
    protected Map<String, Object> getKeyAttributes(final org.pimslims.business.core.model.Person xPerson) {
        final Map<String, Object> attributes = new HashMap<String, Object>();
        if ((xPerson).getFamilyName() == null || (xPerson).getFamilyName().length() == 0) {
            attributes.put(org.pimslims.model.people.Person.PROP_FAMILYNAME, "Dummy");
        } else {
            attributes.put(org.pimslims.model.people.Person.PROP_FAMILYNAME, (xPerson).getFamilyName());
        }
        attributes.put(org.pimslims.model.people.Person.PROP_GIVENNAME, (xPerson).getGivenName());
        return attributes;
    }

    @Override
    protected Class<org.pimslims.model.people.Person> getPClass() {
        return org.pimslims.model.people.Person.class;
    }

    @Override
    protected Map<String, Object> getFullAttributes(final org.pimslims.business.core.model.Person xPerson) {
        final Map<String, Object> attributes = new HashMap<String, Object>(getKeyAttributes(xPerson));
        attributes.put(org.pimslims.model.people.Person.PROP_FAMILYTITLE, (xPerson).getFamilyTitle());
        attributes.put(org.pimslims.model.people.Person.PROP_TITLE, (xPerson).getTitle());

        return attributes;
    }

    @Override
    protected void createPORelated(final org.pimslims.model.people.Person pPerson,
        final org.pimslims.business.core.model.Person xPerson) throws ConstraintException {
        final org.pimslims.model.accessControl.User pimsUser =
            new org.pimslims.model.accessControl.User(getWritableVersion(), (xPerson).getUsername());
        pimsUser.setPerson(pPerson);
        //TODO set group
    }

    @Override
    protected void updatePORelated(final org.pimslims.model.people.Person pPerson,
        final org.pimslims.business.core.model.Person xObject) throws ModelException {
        final Person xPerson = xObject;

        if (xPerson.getGroups() != null && xPerson.getGroups().size() > 0) {
            if (xPerson.getGroups().size() > 1) {
                throw new ConstraintException("User " + xPerson.getFamilyName()
                    + " belongs to more than 1 group, currently, we can not handle it!");
            }
            for (final Group xGroup : xPerson.getGroups()) {
                putPersonInGroup(xPerson, xGroup);
            }
        }

        //TODO update pPerson's  user? ...

    }

    public void putPersonInGroup(final Person xPerson, final Group xGroup) throws ModelException {

        final org.pimslims.model.people.Person pimsPerson = getPO(xPerson);
        final org.pimslims.model.people.Group pimsGroup =
            findByName(org.pimslims.model.people.Group.class, xGroup.getName());
        if (pimsPerson == null || pimsGroup == null) {
            throw new ConstraintException("pimsPerson or pimsGroup is null:" + pimsPerson + "," + pimsGroup);
        }
        //remove old person in group
        for (final PersonInGroup pig : pimsPerson.getPersonInGroups()) {
            pig.delete();
        }
        //create new one
        createPiMSPersonInGroup(xPerson, pimsPerson, pimsGroup);

    }

    //create person in group based on xPerson
    private void createPiMSPersonInGroup(final Person xPerson,
        final org.pimslims.model.people.Person pimsPerson, final org.pimslims.model.people.Group pimsGroup)
        throws ConstraintException {
        final org.pimslims.model.people.PersonInGroup pg =
            new org.pimslims.model.people.PersonInGroup(getWritableVersion(), pimsGroup, pimsPerson);
        if (xPerson.getStartDate() != null) {
            pg.setStartDate(xPerson.getStartDate());
        }
        if (xPerson.getEndDate() != null) {
            pg.setEndDate(xPerson.getEndDate());
        }
        pg.setEmailAddress(xPerson.getEmailAddress());
        pg.setDeliveryAddress(xPerson.getDeliveryAddress());
        pg.setFaxNumber(xPerson.getFaxNumber());
        pg.setPosition(xPerson.getPosition());

        createUserinGroup(xPerson.getUsername(), pimsGroup.getName());

    }

    private void createUserinGroup(final String userName, final String groupName) throws ConstraintException {
        //for access control
        final User pUser = version.findFirst(User.class, User.PROP_NAME, userName);
        final UserGroup pUsergroup = version.findFirst(UserGroup.class, UserGroup.PROP_NAME, groupName);
        pUser.addUserGroup(pUsergroup);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Person loadXAttribute(final org.pimslims.model.people.Person pobject) throws BusinessException {

        //get user name for person's user
        String username = null;
        if (pobject.getUsers() != null && pobject.getUsers().size() > 0) {
            if (pobject.getUsers().size() > 1) {
                throw new BusinessException("Current xtalPiMS can not handel: a person has more than 1 user");
            }
            username = pobject.getUsers().iterator().next().getName();
        }
        final Person xPerson = new Person();
        xPerson.setUsername(username);

        xPerson.setFamilyName(pobject.getFamilyName());
        xPerson.setFamilyTitle(pobject.getFamilyTitle());
        xPerson.setGivenName(pobject.getGivenName());
        xPerson.setTitle(pobject.getTitle());
        return xPerson;
    }

    @Override
    protected org.pimslims.business.core.model.Person loadXRole(
        final org.pimslims.business.core.model.Person xobject, final org.pimslims.model.people.Person pobject)
        throws BusinessException {
        final Person xPerson = xobject;
        if (pobject.getPersonInGroups() != null) {
            final GroupDAO gDao = new GroupDAO(version);
            final Collection<Group> groups = new HashSet<Group>();
            for (final PersonInGroup personInGroup : pobject.getPersonInGroups()) {
                groups.add(gDao.getFullXO(personInGroup.getGroup()));
            }
            xPerson.setGroups(groups);
        }

        return xobject;
    }

    public User getUser(final Person xUser) {
        if (xUser == null) {
            return null;
        }
        User user = this.version.get(User.class, xUser.getId());
        if (user == null && xUser.getUsername() != null) {
            user = this.version.findFirst(User.class, User.PROP_NAME, xUser.getUsername());
        }
        if (user != null) {
            return user;
        }
        final org.pimslims.model.people.Person person = getPO(xUser);
        if (person == null || person.getUsers() == null || person.getUsers().isEmpty()) {
            return null;
        } else {
            return person.getUsers().iterator().next();
        }
    }

    public Collection<Person> findAll() throws BusinessException {
        final Collection<User> pUsers = this.version.getAll(User.class, 0, 10000);
        final Collection<Person> xPersons = new HashSet<Person>();
        for (final User user : pUsers) {
            xPersons.add(getFullXO(user.getPerson()));
        }
        return xPersons;
    }

    public void createUser2(final org.pimslims.model.people.Person pimsPerson, final Person xp)
        throws ConstraintException {
        if (getWritableVersion().findFirst(org.pimslims.model.accessControl.User.class,
            org.pimslims.model.accessControl.User.PROP_NAME, xp.getUsername()) != null) {
            return;
        }
        final org.pimslims.model.accessControl.User pimsUser =
            new org.pimslims.model.accessControl.User(getWritableVersion(), xp.getUsername());
        pimsUser.setPerson(pimsPerson);
    }

    public Person getSimpleXO(final User owner) {
        return getXPerson(owner);
    }

    public static Person getXPerson(final User user) {
        if (null == user) {
            return null;
        }
        final Person xPerson = new Person();
        xPerson.setUsername(user.getName());
        if (user.getPerson() != null) {

            final org.pimslims.model.people.Person pobject = user.getPerson();
            xPerson.setFamilyName(pobject.getFamilyName());
            xPerson.setFamilyTitle(pobject.getFamilyTitle());
            xPerson.setGivenName(pobject.getGivenName());
            xPerson.setTitle(pobject.getTitle());
            xPerson.setId(pobject.getDbId());
        }
        return xPerson;
    }
}
