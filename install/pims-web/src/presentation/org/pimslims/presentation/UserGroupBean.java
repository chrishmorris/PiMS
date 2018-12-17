package org.pimslims.presentation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.pimslims.access.Access;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;

public class UserGroupBean extends ModelObjectShortBean {

    /**
     * Can these permissions be changed by the operator?
     */
    private final boolean immutable;

    /**
     * Permissions for reference data: anyone can read, only admin can change
     */
    private static final Permission REFERENCE_PERMISSION = new Permission(false, true, false, false, false,
        true);

    /**
     * the administrator can do anything
     */
    public static final Permission ADMIN_PERMISSION = new Permission(true, true, true, true, true, true);

    private final Map<String, Permission> permissions = new HashMap<String, Permission>();

    private boolean isToView;

    private final ModelObjectShortBean head;

    public UserGroupBean(final UserGroup group) {
        this(group, false);
    }

    /**
     * Make a bean from the database
     * 
     * @param name user group name
     * @param immutable are these permissions changeable?
     */
    public UserGroupBean(final UserGroup group, final boolean immutable) {
        super(group);
        this.immutable = immutable;

        // set up standard permissions
        //this.permissions.put(Access.WORLD_WRITABLE, UserGroupBean.ADMIN_PERMISSION);
        /*if (Access.ADMINISTRATOR.equals(this.getName())) {
            this.permissions.put(Access.REFERENCE, UserGroupBean.ADMIN_PERMISSION);
            return;
        } */
        this.permissions.put(Access.REFERENCE, UserGroupBean.REFERENCE_PERMISSION);
        final User boss = group.getHeader();
        if (null == boss) {
            this.head = null;
        } else {
            this.head = new ModelObjectShortBean(boss);
        }
    }

    /**
     * Make a bean from the submitted form values
     * 
     * @param name user group name
     * @param permissions, in format ${name}:${op}:${owner}
     */
    public UserGroupBean(final UserGroup group, final Collection<String> parms) {
        super(group);
        this.immutable = false; // ignored
        for (final Iterator iter = parms.iterator(); iter.hasNext();) {
            final String parm = (String) iter.next();
            if (!parm.startsWith(this.getName())) {
                continue; // e.g. "owner"
            }
            final int end = parm.lastIndexOf(":");
            final String owner = parm.substring(end + 1);
            final String op = parm.substring(this.getName().length() + 1, end);
            if ("create".equals(op)) {
                this.getPermission(owner).setCreate(true);
            }
            if ("read".equals(op)) {
                this.getPermission(owner).setRead(true);
            }
            if ("update".equals(op)) {
                this.getPermission(owner).setUpdate(true);
            }
            if ("delete".equals(op)) {
                this.getPermission(owner).setDelete(true);
            }
            if ("unlock".equals(op)) {
                this.getPermission(owner).setUnlock(true);
            }
        }
        final User boss = group.getHeader();
        if (null == boss) {
            this.head = null;
        } else {
            this.head = new ModelObjectShortBean(boss);
        }
    }

    public Permission getPermission(final String owner) {
        if (!this.permissions.containsKey(owner)) {
            // may be parsing HTTP parameters
            this.permissions.put(owner, new Permission(false, false, false, false, false, true));
        }
        return this.permissions.get(owner);
    }

    public void setPermission(final String owner, final Permission permission) {
        this.permissions.put(owner, permission);
    }

    public Map<String, Permission> getPermissions() {
        return new HashMap<String, Permission>(this.permissions);
    }

    /**
     * A bean to represent create, read, update, and delete permissions for a particular user group and owner
     * 
     * @author cm65
     * 
     */
    public static class Permission {

        private boolean create;

        private boolean read;

        private boolean update;

        private boolean delete;

        private boolean unlock;

        /**
         * Can these permissions be changed by the operator?
         */
        private final boolean immutable;

        public Permission(final boolean create, final boolean read, final boolean update,
            final boolean delete, final boolean unlock, final boolean immutable) {
            this.create = create;
            this.read = read;
            this.update = update;
            this.delete = delete;
            this.unlock = unlock;
            this.immutable = immutable;
        }

        /**
         * @return Returns the create permission.
         */
        public boolean getCreate() {
            return this.create;
        }

        /**
         * @return Returns the delete permission.
         */
        public boolean getDelete() {
            return this.delete;
        }

        /**
         * @return Returns the read permission.
         */
        public boolean getRead() {
            return this.read;
        }

        /**
         * @return Returns the update permission.
         */
        public boolean getUpdate() {
            return this.update;
        }

        /**
         * @return Returns the immutable.
         */
        public boolean getImmutable() {
            return this.immutable;
        }

        // setters, for use only while parsing HTTP parameters

        /**
         * @param create The create to set.
         */
        private void setCreate(final boolean create) {
            this.create = create;
        }

        /**
         * @param delete The delete to set.
         */
        private void setDelete(final boolean delete) {
            this.delete = delete;
        }

        /**
         * @param read The read to set.
         */
        private void setRead(final boolean read) {
            this.read = read;
        }

        /**
         * @param update The update to set.
         */
        private void setUpdate(final boolean update) {
            this.update = update;
        }

        /**
         * @return Returns the unlock.
         */
        public boolean getUnlock() {
            return this.unlock;
        }

        /**
         * @param unlock The unlock to set.
         */
        private void setUnlock(final boolean unlock) {
            this.unlock = unlock;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof UserGroupBean)) {
            return false;
        }
        return this.getName().equals(((UserGroupBean) other).getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.getName().hashCode() + 76957;
    }

    /**
     * @return Returns the immutable.
     */
    public boolean isImmutable() {
        return this.immutable;
    }

    /**
     * UserGroupBean.setIsToView
     * 
     * @param b
     */
    public void setIsToView(final boolean b) {
        this.isToView = true;
    }

    /**
     * @return Returns the isToView.
     */
    public boolean isToView() {
        return this.isToView;
    }

    /**
     * @param isToView The isToView to set.
     */
    public void setToView(final boolean isToView) {
        this.isToView = isToView;
    }

    /**
     * UserGroupBean.getHead
     * 
     * @return
     */
    public ModelObjectShortBean getHead() {
        return this.head;
    }

}
