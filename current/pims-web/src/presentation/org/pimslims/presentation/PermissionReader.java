/**
 * 
 */
package org.pimslims.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.pimslims.access.PIMSAccess;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.AccessRightsUtility;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;

/**
 * Business logic for viewing access rights
 * 
 * @author cm65
 * 
 */
public class PermissionReader {

    /**
     * DELETE String
     */
    private static final String DELETE = PIMSAccess.DELETE;

    /**
     * UPDATE String
     */
    private static final String UPDATE = PIMSAccess.UPDATE;

    /**
     * READ String
     */
    private static final String READ = PIMSAccess.READ;

    /**
     * CREATE String
     */
    private static final String CREATE = PIMSAccess.CREATE;

    /**
     * MAX_OWNERS int
     */
    private static final int MAX_OWNERS = 100;

    private static final String UNLOCK = "unlock";

    /**
     * the current transaction
     */
    protected final ReadableVersion version;

    /**
     * The lab notebooks This is final because it is important that the same order is used throughout
     */
    protected final List<LabNotebook> owners;

    protected final List<UserGroup> groups;

    /**
     * @param version
     */
    public PermissionReader(final ReadableVersion version) {
        this(version, PermissionReader.getAllOwners(version), PermissionReader.getAllGroups(version));
    }

    /**
     * @param version
     */
    public PermissionReader(final ReadableVersion version, final Collection<LabNotebook> myOwners,
        final Collection<UserGroup> myGroups) {
        this.version = version;
        this.owners = new ArrayList(myOwners);
        this.groups = new ArrayList(myGroups);
    }

    public static Collection<LabNotebook> getAllOwners(final ReadableVersion version) {
        return version.getAll(LabNotebook.class, 0, PermissionReader.MAX_OWNERS);
    }

    public List<ModelObjectShortBean> getOwners() {
        // was ret.add(0, Access.REFERENCE);
        return ModelObjectShortBean.getBeans(this.owners);
    }

    public List<UserGroupBean> getGroups() {
        final List<UserGroupBean> ret = new ArrayList<UserGroupBean>();

        /* used to represent the administrator first, no longer include it
        final UserGroupBean admin = UserGroupBean.getAdministratorBean(this.owners);
        ret.add(0, admin); */

        final Collection<UserGroup> groups = PermissionReader.getAllGroups(this.version);

        // speedup
        for (final UserGroup group : groups) {
            /*
             * System.out.println(new Timestamp(System.currentTimeMillis()) +"processing
             * group:"+group.getName());
             */
            final UserGroupBean userGroupBean = new UserGroupBean(group);
            for (final LabNotebook owner : this.owners) {
                /* if (Access.WORLD_WRITABLE.equals(owner.getName())) {
                    continue; // set in constructor of UserGroupBean
                } */
                userGroupBean.setPermission(
                    owner.getName(),
                    new UserGroupBean.Permission(AccessRightsUtility.isPermitted(group,
                        PermissionReader.CREATE, owner), AccessRightsUtility.isPermitted(group,
                        PermissionReader.READ, owner), AccessRightsUtility.isPermitted(group,
                        PermissionReader.UPDATE, owner), AccessRightsUtility.isPermitted(group,
                        PermissionReader.DELETE, owner), AccessRightsUtility.isPermitted(group,
                        PermissionReader.UNLOCK, owner), false));
            }
            ret.add(userGroupBean);
        }
        return ret;
    }

    protected static Collection<UserGroup> getAllGroups(final ReadableVersion version) {
        final List<String> joinroles = new LinkedList<String>();
        joinroles.add(LabNotebook.PROP_PERMISSIONS);
        return version.getAll(UserGroup.class, joinroles);
    }

    /**
     * PermissionReader.getOwnersForGroups
     * 
     * @param groupHooks
     * @return
     */
    public String[] getOwnersForGroups(final String[] groupHooks) {
        final List<String> ret = new ArrayList();
        for (int i = 0; i < groupHooks.length; i++) {
            final UserGroup group = this.version.get(groupHooks[i]);
            for (final Iterator iterator = group.getPermissions().iterator(); iterator.hasNext();) {
                final Permission p = (Permission) iterator.next();
                ret.add(p.getAccessObject().get_Hook());
            }
        }
        return ret.toArray(new String[ret.size()]);
    }

    /**
     * PermissionReader.getGroupsForOwners
     * 
     * @param ownerHooks
     * @return
     */
    public String[] getGroupsForOwners(final String[] ownerHooks) {
        final List<String> ret = new ArrayList();
        for (int i = 0; i < ownerHooks.length; i++) {
            final LabNotebook owner = this.version.get(ownerHooks[i]);
            for (final Iterator iterator = owner.getPermissions().iterator(); iterator.hasNext();) {
                final Permission p = (Permission) iterator.next();
                ret.add(p.getUserGroup().get_Hook());
            }
        }
        return ret.toArray(new String[ret.size()]);
    }

}
