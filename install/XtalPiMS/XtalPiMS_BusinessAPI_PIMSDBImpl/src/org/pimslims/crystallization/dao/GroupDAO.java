package org.pimslims.crystallization.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pimslims.business.core.model.Organisation;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.people.Group;
import org.pimslims.model.people.PersonInGroup;

/**
 * <p>
 * Data Access Object for {@link org.pimslims.business.core.model.Group}
 * </p>
 * 
 * @author Bill Lin
 */
public class GroupDAO extends
    GenericDAO<org.pimslims.model.people.Group, org.pimslims.business.core.model.Group> {

    /**
     * GroupDAO.createPO
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#createPO(org.pimslims.business.XtalObject)
     */
    @Override
    public Group createPO(final org.pimslims.business.core.model.Group xobject) throws BusinessException {
        final Group group = super.createPO(xobject);
        /* used to set this group into its access control, but all access info should be reference data
        final AccessObject ao =
            this.version.findFirst(AccessObject.class, AccessObject.PROP_NAME, group.getName());
        assert ao != null;
        try {
            group.setAccess(ao);
        } catch (final ConstraintException e) {
            throw new BusinessException(e);
        } */
        return group;
    }

    public GroupDAO(final ReadableVersion version) {
        super(version);
    }

    @Override
    protected void createPORelated(final Group object, final org.pimslims.business.core.model.Group xobject)
        throws BusinessException, ModelException {
        final org.pimslims.business.core.model.Group xgroup = xobject;

        final PersonDAO personDAO = new PersonDAO(version);

        //create for access control
        assert xgroup.getOrganisation() != null;
        final String accessName = xgroup.getName();
        //new user group for this person group which using same name
        final UserGroup pUserGroup = new UserGroup(getWritableVersion(), accessName);
        //new dataOwner on this group
        final LabNotebook pAO = new LabNotebook(getWritableVersion(), accessName);
        //create permissions which include all permissions;
        new Permission(getWritableVersion(), "any", "read", pAO, pUserGroup);
        new Permission(getWritableVersion(), "any", "create", pAO, pUserGroup);
        new Permission(getWritableVersion(), "any", "update", pAO, pUserGroup);
        new Permission(getWritableVersion(), "any", "delete", pAO, pUserGroup);

        //create person in group
        for (final Person xp : xgroup.getUsers()) {
            //find/create person and put in group
            xp.setId(null);
            final org.pimslims.model.people.Person pimsPerson = personDAO.getPO(xp);
            if (pimsPerson == null) {
                personDAO.createPO(xp);
            } else {
                personDAO.createUser2(pimsPerson, xp);
            }
            personDAO.putPersonInGroup(xp, xgroup);

        }

        //set group header
        if (xobject.getGroupHead() != null) {
            User header =
                getWritableVersion().findFirst(User.class, User.PROP_NAME,
                    xobject.getGroupHead().getUsername());
            if (header == null) {
                personDAO.createPO(xobject.getGroupHead());
                personDAO.putPersonInGroup(xobject.getGroupHead(), xgroup);
                header =
                    getWritableVersion().findFirst(User.class, User.PROP_NAME,
                        xobject.getGroupHead().getUsername());
            }
            pUserGroup.setHeader(header);
        }
    }

    @Override
    protected Map<String, Object> getFullAttributes(final org.pimslims.business.core.model.Group xobject) {
        final org.pimslims.business.core.model.Group xGroup = xobject;

        //find pims' Organisation by name
        org.pimslims.model.people.Organisation pimsOrg = null;
        if (xGroup.getOrganisation() != null) {
            pimsOrg =
                findByName(org.pimslims.model.people.Organisation.class, xGroup.getOrganisation().getName());
        }

        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.people.Group.PROP_NAME, xGroup.getName());
        attributes.put(org.pimslims.model.people.Group.PROP_ORGANISATION, pimsOrg);
        return attributes;

    }

    @Override
    protected Map<String, Object> getKeyAttributes(final org.pimslims.business.core.model.Group xobject) {
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(org.pimslims.model.people.Group.PROP_NAME, (xobject).getName());
        return attributes;
    }

    @Override
    protected Class<Group> getPClass() {
        return org.pimslims.model.people.Group.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected org.pimslims.business.core.model.Group loadXAttribute(final Group pobject)
        throws BusinessException {
        final org.pimslims.business.core.model.Group xGroup = new org.pimslims.business.core.model.Group();
        xGroup.setName(pobject.getName());
        return xGroup;
    }

    @Override
    protected org.pimslims.business.core.model.Group loadXRole(
        final org.pimslims.business.core.model.Group xobject, final Group pobject) throws BusinessException {
        //load group's user
        final PersonDAO personDAO = new PersonDAO(version);
        final List<Person> xPersons = new LinkedList<Person>();
        for (final PersonInGroup pig : pobject.getPersonInGroups()) {
            xPersons.add(personDAO.getSimpleXO(pig.getPerson()));
        }
        (xobject).setUsers(xPersons);

        final org.pimslims.model.people.Organisation pimsOrg = pobject.getOrganisation();
        xobject.setOrganisation(getXtalOrganisation(pimsOrg));
        return xobject;

    }

    @Override
    protected void updatePORelated(final Group object, final org.pimslims.business.core.model.Group xobject)
        throws ModelException {
        // nothing

    }

    public Collection<org.pimslims.business.core.model.Group> findAll() throws BusinessException {
        final Collection<Group> pGroups = this.version.getAll(Group.class);
        final Collection<org.pimslims.business.core.model.Group> xGroups = new HashSet();
        for (final Group group : pGroups) {
            xGroups.add(this.getFullXO(group));
        }
        return xGroups;
    }

    /**
     * 
     * @param pimsOrganisation
     * @return
     */
    public static Organisation getXtalOrganisation(
        final org.pimslims.model.people.Organisation pimsOrganisation) {
        if (pimsOrganisation == null) {
            return null;
        }
        final Organisation organisation = new Organisation();
        organisation.setName(pimsOrganisation.getName());
        organisation.setId(pimsOrganisation.getDbId());
        return organisation;
    }
}
