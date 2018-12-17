package org.pimslims.access;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.reference.PublicEntry;

public class AccessImpl extends PIMSAccess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Role for adding users to groups
     */
    public static final String MEMBER_ROLE = "leaders"; // TODO "members" -

    // currently broken

    /**
     * 
     */
    public AccessImpl() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.PIMSAccess#getOwner()
     */
    public MetaClass getOwner() {
        return this.model.getMetaClass(LabNotebook.class.getName());
    }

    /**
     * @see org.pimslims.access.PIMSAccess#getUser()
     */
    public MetaClass getUser() {
        return this.model.getMetaClass(User.class.getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.PIMSAccess#getPermission()
     */
    public MetaClass getPermission() {
        return this.model.getMetaClass(Permission.class.getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.PIMSAccess#getUserGroup()
     */
    public MetaClass getUserGroup() {
        AbstractModel abstractModel = this.model;
        return abstractModel.getMetaClass(UserGroup.class.getName());
    }

    /**
     * {@inheritDoc} TODO if we need this at all, it should report that an ordinary user can't create
     * reference data
     */
    @Override
    public boolean mayCreate(final ReadableVersion version, String ownerName, final MetaClass type,
        final Map attributes) {
        if (ownerName == null) {
            return false;
        }
        if (Access.ADMINISTRATOR.equals(version.getUsername())) {
            if (Access.REFERENCE.equals(ownerName)) {
                return true;
            }
            // just make sure the owner exists
            LabNotebook owner;
            try {
                owner = version.getOwner(ownerName);
            } catch (AssertionError e) {
                // no such owner;
                // TODO dont catch assertion errors
                return false;
            }
            return null != owner;
        }
        if (Access.REFERENCE.equals(ownerName)) {
            return false;
        }
        return isPermitted(PIMSAccess.CREATE, version, ownerName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mayDelete(final ModelObject object) {
        if (ADMINISTRATOR.equals(object.get_Version().getUsername())) {
            return true;
        }
        String ownerName = object.get_Owner();
        if (Access.REFERENCE.equals(ownerName)) {
            return false;
        }
        return isPermitted(PIMSAccess.DELETE, object.get_Version(), ownerName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mayRead(final ModelObject object) {
        if (ADMINISTRATOR.equals(object.get_Version().getUsername())) {
            return true;
        }

        if (PublicEntry.class.isAssignableFrom(object.getClass()))
            return true;

        String ownerName = object.get_Owner();
        if (Access.REFERENCE.equals(ownerName)) {
            return true;
        }
        return isPermitted(PIMSAccess.READ, object.get_Version(), ownerName);
    }

    /**
     * {@inheritDoc}
     * 
     * Note that this would be more efficient with a join
     */
    @Override
    public boolean mayUpdate(final ModelObject object) {
        assert null != object.get_Version();
        if (ADMINISTRATOR.equals(object.get_Version().getUsername())) {
            return true;
        }
        String ownerName = object.get_Owner();
        if (Access.REFERENCE.equals(ownerName)) {
            return false;
        }
        return isPermitted(PIMSAccess.UPDATE, object.get_Version(), ownerName);
    }

    /**
     * AccessImpl.mayUnlock
     * 
     * @see org.pimslims.access.Access#mayUnlock(org.pimslims.metamodel.ModelObject)
     */
    @Override
    public boolean mayUnlock(ModelObject object) {
        assert null != object.get_Version();
        if (ADMINISTRATOR.equals(object.get_Version().getUsername())) {
            return true;
        }
        String ownerName = object.get_Owner();
        return isPermitted(PIMSAccess.UNLOCK, object.get_Version(), ownerName);
    }

    @Override
    public Set<ModelObject> getPermittedOwners(MetaClass metaClass) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * The reason why this method takes the owner as a string, not an object, is to avoid infinite recursion -
     * getting an object involves calling this method to check the read permission.
     * 
     * @param opType the operation to check
     * @param version current transaction
     * @param ownerName the owner to check
     * @return true if the current user may perform the operation specified, for records owned by the data
     *         owner specified
     */
    protected boolean isPermitted(String opType, org.pimslims.dao.ReadableVersion version, String ownerName) {
        org.pimslims.model.accessControl.User user = ((ReadableVersion) version).getCurrentUser();
        java.util.Collection groups = user.getUserGroups();
        // groups -
        // currently fails
        for (Iterator iter = groups.iterator(); iter.hasNext();) {
            UserGroup group = (UserGroup) iter.next();
            java.util.Collection permissions = group.getPermissions(); // LATER
            // group.findAllPermissions("opType",
            // opType);
            for (Iterator iterator = permissions.iterator(); iterator.hasNext();) {
                Permission permission = (Permission) iterator.next();
                if (!opType.equals(permission.getOpType())) {
                    continue;
                }
                if (ownerName.equals(permission.getLabNotebook().getName())) {
                    return true;
                }
            }
        }
        return false;

    }

}
