/**
 * pims-model org.pimslims.dao AccessController.java
 * 
 * @author bl67
 * @date 23 Sep 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 bl67 This library is free software; you can redistribute it and/or modify it
 *           under the terms of the GNU Lesser General Public License as published by the Free Software
 *           Foundation; either version 2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */
package org.pimslims.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.NoResultException;

import org.pimslims.access.Access;
import org.pimslims.access.PIMSAccess;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ControlledAccess;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.metamodel.PublicAccess;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;

/**
 * AccessController TODO why do we need this? Doesn't it delegate most of the work?
 */
public class AccessControllerImpl {

    // empty for admin to save a big lookup
    private final Set<LabNotebook> readableNotebooks = new HashSet<LabNotebook>();

    private final org.pimslims.model.accessControl.User user;

    private final Access access;

    /**
     * @param version
     */
    private final ReadableVersion version;

    /**
     * The owner for newly created objects, unless another one is specified.
     */

    protected LabNotebook defaultOwner;

    //LabNotebook accessObjectofWORLDWRITABLE = null;

    private boolean defaultOwnerInited = false;

    /**
     * name => AccessObject
     */
    private final Map<String, LabNotebook> ownerCache = new java.util.HashMap<String, LabNotebook>();

    /**
     * Cache of objects with known mayUpdate status - to prevent db access for every update
     */
    private final java.util.Map<ModelObject, Boolean> mayUpdate =
        new java.util.HashMap<ModelObject, Boolean>();

    /**
     * Cache of objects with known mayRead status - to prevent db access for every read
     */
    private final Map<ModelObject, Boolean> mayRead = new java.util.HashMap<ModelObject, Boolean>();

    public AccessControllerImpl(final ReadableVersion version, final String username) {
        super();
        assert null != version;
        this.version = version;

        this.access = version.getModel().getAccess();
        assert null != this.access;
        //set defaultOwner
        if (org.pimslims.access.Access.ADMINISTRATOR.equals(username)) {
            this.user = null; // The PIMS admin is not represented in the database
            defaultOwner = null;
        } else {
            user = this.getUser(username, version);
            if (null == user) {

                throw new IllegalArgumentException("Unknown PIMS user name: " + username);
            }

            // get all the lab notebooks this user can read
            defaultOwner = getDefaultOwner(user);
            readableNotebooks.addAll(getAccessObjects(user, PIMSAccess.READ));

        }
    }

    /**
     * AccessControllerImpl.getDefaultOwner
     * 
     * @param user2
     * @return
     */
    public LabNotebook getDefaultOwner(final User user) {
        final List<LabNotebook> owners = getAccessObjects(user, PIMSAccess.CREATE);
        if (1 == owners.size()) {
            return owners.get(0);
        }
        return null;
    }

    /**
     * @param username
     * @param version2
     * @return
     */
    public User getUser(final String username, final ReadableVersion version2) {
        javax.persistence.Query query =
            version.getEntityManager().createQuery(" select A from User as A where name=:name", User.class);
        try {
            return (User) query.setParameter("name", username).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * @param user
     * @return
     */
    List<LabNotebook> getAccessObjects(final User user, String accessType) {
        if (user == null) {
            throw new RuntimeException("Can not get possible create owner for 'NULL' user");
        }
        final List<LabNotebook> owners = new java.util.ArrayList<LabNotebook>();
        {
            for (final UserGroup group : user.getUserGroups()) {
                for (final Permission permission : group.getPermissions()) {

                    if (accessType.equals(permission.getOpType())) {

                        owners.add(permission.getLabNotebook());
                    }
                }
            }
        }
        /*if (accessObjectofWORLDWRITABLE != null && PIMSAccess.CREATE.equals(accessType))
            owners.add(accessObjectofWORLDWRITABLE); */
        return owners;
    }

    private boolean firstTimeAccessSearch = true;

    // public static final String WORLD_WRITABLE = "all";

    public String getDefaultOwner(final MetaClass metaClass, final ModelObject context)
        throws AccessException {
        if (mayCreate(getDefaultOwnerName(), metaClass, new HashMap())) {
            return getDefaultOwnerName();
        }
        if (null != context && mayCreate(context.get_Owner(), metaClass, new HashMap())) {
            return context.get_Owner();
        }

        if (null == this.user) {
            // it's the administrator
            return org.pimslims.access.Access.REFERENCE;
        }
        final List<LabNotebook> owners = getAccessObjects(user, PIMSAccess.CREATE);
        if (1 == owners.size()) {
            return owners.get(0).getName();
        }
        if (owners.size() < 1) {
            throw new AccessException("User " + this.user.getName()
                + " don't have permission to create records.");
        }
        throw new AccessException("Unable to determine default owner due to user " + this.user.getName()
            + " can create records under more than 1 Data Owner: " + owners);

        // could return WORLD_WRITABLE, to support simple installations that want
        // no access control
        // return org.pimslims.metamodel.Access.WORLD_WRITABLE;
    }

    /**
     * Find a data owner
     * 
     * @param ownerName the name of the access object
     * @return access object
     * @throws ConstraintException
     */
    public org.pimslims.model.core.LabNotebook getOwner(final String ownerName) {
        if (Access.REFERENCE.equals(ownerName)) {
            return null;
        }
        if (!ownerCache.containsKey(ownerName)) {

            org.pimslims.model.core.LabNotebook owner =
                version.findFirst(org.pimslims.model.core.LabNotebook.class,
                    org.pimslims.model.core.LabNotebook.PROP_NAME, ownerName);
            //assert 1 >= owners.size() : "Duplicate data owners called: " + ownerName;
            if (null == owner) {
                throw new AssertionError("Unknown Lab Notebook: <" + ownerName + ">");
            }
            ownerCache.put(ownerName, owner);
        }
        final Object owner = ownerCache.get(ownerName);
        assert null != owner;
        return (org.pimslims.model.core.LabNotebook) owner;
    }

    /**
     * @return
     */
    public Collection<LabNotebook> getReadAccessObjects() {
        if (null != user && readableNotebooks.isEmpty()) {
            throw new IllegalStateException("No Lab Notebooks are readable by user: " + this.user.getName());
        }
        return readableNotebooks;
    }

    public static boolean isSearchAccessControlNeeded(final Class targetJavaClass) {
        if (targetJavaClass == null) {
            throw new AssertionError("Error in access control"); // was return false;
        }

        if (PublicAccess.class.isAssignableFrom(targetJavaClass)) {
            return false;
        }
        // TODO also use this for child classes
        if (ControlledAccess.class.isAssignableFrom(targetJavaClass)) {
            return true;
        }
        throw new IllegalArgumentException("Unexpected class: " + targetJavaClass.getName()); // was return false;    
    }

    /**
     * get the role name which this targetJavaClass's access control rely on TODO also use this for child
     * classes
     * 
     * @param targetJavaClass
     * @return
     */
    public String getAccessControlRoleName(final Class targetJavaClass) {
        if (!isSearchAccessControlNeeded(targetJavaClass)) {
            return null;
        }
        if (Attachment.class.isAssignableFrom(targetJavaClass)) {
            return Attachment.PROP_PARENTENTRY;
        }
        return null;
    }

    @Deprecated
    // seems not to be so useful
    public boolean mayCreate(final String owner, final MetaClass metaClass, final Map attributes) {
        return this.access.mayCreate(version, owner, metaClass, attributes);
    }

    public boolean mayDelete(final ModelObject object) {
        // TODO could cache results
        return this.access.mayDelete(object);
    }

    public boolean mayRead(final ModelObject object) {
        assert null != object;
        assert version == object.get_Version();
        if (!this.mayRead.containsKey(object)) {
            this.mayRead.put(object, this.access.mayRead(object));
        }
        return (this.mayRead.get(object)).booleanValue();
    }

    public boolean mayUpdate(final ModelObject object) {
        assert null != object;
        if (!mayUpdate.containsKey(object)) {
            mayUpdate.put(object, new Boolean(this.access.mayUpdate(object)));
        }
        return (mayUpdate.get(object)).booleanValue();
    }

    public boolean mayUnlock(final ModelObject object) {
        return this.access.mayUnlock(object);
    }

    public void setDefaultOwner(final String owner) {
        defaultOwner = this.getOwner(owner);
        //was readableNotebooks.add(defaultOwner); //That doesn't look right to me __Chris
        defaultOwnerInited = true;
    }

    public void setDefaultOwner(final LabNotebook owner) {
        defaultOwner = owner;
        defaultOwnerInited = true;
        if (null != owner) {
            // was readableNotebooks.add(defaultOwner); //That doesn't look right to me __Chris

            // JMD bodge to fix unnecessary db hit arising from subsequent calls to getOwner()
            // Why the string version trumps the AccessObject version I've no idea!
            ownerCache.put(owner.getName(), owner);
        }
    }

    /**
	 * 
	 */
    public void resetFirstTimeAccessSearch(final boolean value) {
        this.firstTimeAccessSearch = value;

    }

    /**
     * @return
     */
    public User getUser() {
        return this.user;
    }

    private boolean isAdmin() {
        return this.user == null;
    }

    private String getDefaultOwnerName() {
        if (this.defaultOwner != null)
            return defaultOwner.getName();
        if (isAdmin())
            return Access.ADMINISTRATOR;
        if (this.defaultOwner == null)
            return null;
        //shoud be impossible
        throw new RuntimeException("Internal logic error!");
    }

    /**
     * AccessControllerImpl.getCurrentDefaultOwner
     * 
     * @return
     * @throws AccessException
     */
    public LabNotebook getCurrentDefaultOwner() {
        if (this.version instanceof WritableVersion) {
            if (!defaultOwnerInited && ((WritableVersionImpl) this.version).forceDataOwnerCheck) {
                throw new RuntimeException("Default owner hasn't been set!");
            }
        }
        return defaultOwner;
    }

    String getCurrentDefaultOwnerName(Class javaClass) throws AccessException {
        if (getCurrentDefaultOwner() == null || !LabBookEntry.class.isAssignableFrom(javaClass))
            return getDefaultOwner(this.version.getMetaClass(javaClass), null);
        return defaultOwner.getName();
    }

}
