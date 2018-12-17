package org.pimslims.lab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.pimslims.access.Access;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.people.Person;

/**
 * Logic for managing access rights and user definitions
 * 
 * @author cm65
 * 
 */
public class AccessRightsUtility {

    /**
     * 
     * @param version the current transaction
     * @return a Person instance representing the current user, if any
     */
    public static Person getCurrentUserAsPerson(final ReadableVersion version) {
        final User user = version.getCurrentUser();
        if (user == null) {
            return null;
        }
        final Person person = user.getPerson();
        return person;
    }

    public static List<String> getCanCreateOwners(final ReadableVersion version, final User user) {
        final Collection<Permission> permissions =
            version.findAll(Permission.class, Permission.PROP_OPTYPE, "create");
        final List<String> ret = new ArrayList(permissions.size());
        for (final Iterator iterator = permissions.iterator(); iterator.hasNext();) {
            final Permission permission = (Permission) iterator.next();
            if (null == user || permission.getUserGroup().isAssociated(UserGroup.PROP_MEMBERUSERS, user)) {
                ret.add(permission.getLabNotebook().getName());
            }
        }
        if (null == user) {
            ret.add(Access.REFERENCE);
        }
        return ret;
    }

    /**
     * Check a permission This code could be more efficient - work on it if performance is a problem
     * 
     * @param group
     * @param opType
     * @param owner
     * @return
     */
    public static boolean isPermitted(final UserGroup group, final String opType, final LabNotebook owner) {
        final java.util.Collection permissions = AccessRightsUtility.findPermissions(group, opType, owner);
        return 0 < permissions.size();
    }

    public static Collection<Permission> findPermissions(final UserGroup group, final String opType,
        final LabNotebook owner) {
        final Collection<Permission> permissions = new HashSet<Permission>();

        for (final Permission permission : group.getPermissions()) {
            if (permission.getOpType().equals(opType) && permission.getLabNotebook() == owner) {
                permissions.add(permission);
            }
        }
        return permissions;
    }

    public static void setCan(final WritableVersion version, final UserGroup group, final String opType,
        final LabNotebook owner) throws ConstraintException {
        final Collection<Permission> permissions = AccessRightsUtility.findPermissions(group, opType, owner);
        if (0 == permissions.size()) {
            new Permission(version, opType, owner, group);
        }
    }

    public static void setCant(final WritableVersion version, final UserGroup group, final String opType,
        final LabNotebook owner) throws AccessException, ConstraintException {
        final Collection<Permission> permissions = AccessRightsUtility.findPermissions(group, opType, owner);
        version.delete(permissions);
    }

}
