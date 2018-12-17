/**
 * 
 */
package org.pimslims.presentation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.pimslims.access.PIMSAccess;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.AccessRightsUtility;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;

/**
 * Business logic for updating access rights
 * 
 * @author cm65
 * 
 */
public class PermissionWriter extends PermissionReader {

    private final WritableVersion wv;

    /**
     * @param version
     */
    public PermissionWriter(final WritableVersion version, final Collection<LabNotebook> notebooks,
        final Collection<UserGroup> groups) {
        super(version, notebooks, groups);
        this.wv = version;
    }

    /**
     * Update the permissions for a user group
     * 
     * @param bean
     * @throws ConstraintException
     * @throws AccessException
     */
    void save(final UserGroupBean bean) throws ConstraintException, AccessException {

        // for every owner in the context, make the permissions conform to the bean
        for (final Iterator iter = this.owners.iterator(); iter.hasNext();) {
            final LabNotebook owner = (LabNotebook) iter.next();
            /* if (Access.WORLD_WRITABLE.equals(owner.getName())) {
                continue;
            } */

            final UserGroupBean.Permission permission = bean.getPermission(owner.getName());
            final UserGroup group = this.getGroup(bean.getName());
            if (permission.getCreate()) {
                AccessRightsUtility.setCan(this.wv, group, PIMSAccess.CREATE, owner);
            } else {
                AccessRightsUtility.setCant(this.wv, group, PIMSAccess.CREATE, owner);
            }
            if (permission.getRead()) {
                AccessRightsUtility.setCan(this.wv, group, PIMSAccess.READ, owner);
            } else {
                AccessRightsUtility.setCant(this.wv, group, PIMSAccess.READ, owner);
            }
            if (permission.getUpdate()) {
                AccessRightsUtility.setCan(this.wv, group, PIMSAccess.UPDATE, owner);
            } else {
                AccessRightsUtility.setCant(this.wv, group, PIMSAccess.UPDATE, owner);
            }
            if (permission.getDelete()) {
                AccessRightsUtility.setCan(this.wv, group, PIMSAccess.DELETE, owner);
            } else {
                AccessRightsUtility.setCant(this.wv, group, PIMSAccess.DELETE, owner);
            }
            if (permission.getUnlock()) {
                AccessRightsUtility.setCan(this.wv, group, "unlock" /*PIMSAccess.UNLOCK*/, owner);
            } else {
                AccessRightsUtility.setCant(this.wv, group, "unlock"/*PIMSAccess.UNLOCK */, owner);
            }
        }
    }

    private UserGroup getGroup(final String name) {
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(UserGroup.PROP_NAME, name);
        final Collection<UserGroup> groups = this.version.findAll(UserGroup.class, attributes);
        if (0 == groups.size()) {
            return null;
        }
        assert 1 == groups.size();
        return groups.iterator().next();
    }

    /**
     * Update all the permissions
     * 
     * @param parameters permissions, in format ${name}:${op}:${owner}
     * @throws AccessException
     * @throws ConstraintException
     */
    public void save(final Collection<String> parameters) throws ConstraintException, AccessException {
        // process all the relevant groups 
        for (final Iterator iter = this.groups.iterator(); iter.hasNext();) {
            final UserGroup group = (UserGroup) iter.next();
            final UserGroupBean bean = new UserGroupBean(group, parameters);
            this.save(bean);
        }
    }

}
