/**
 * ExtendedTestModel ccp.api.Implementation AbstractModeObject.java
 * 
 * @author bl67
 * @date 16-Jun-2006
 * 
 *       Protein Information Management System
 * @version: 0.5
 * 
 *           Copyright (c) 2006 bl67 This library is free software; you can redistribute it and/or modify it
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
package org.pimslims.metamodel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.pimslims.access.Access;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.ReadableVersionImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.Annotation;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.util.File;
import org.pimslims.util.FileImpl;

@org.pimslims.annotation.MetaClass(helpText = "the AbstractModelObject.", subclasses = {
    org.pimslims.model.core.SystemClass.class, org.pimslims.model.core.LabBookEntry.class,
    org.pimslims.model.reference.PublicEntry.class, org.pimslims.model.core.Attachment.class })
public abstract class AbstractModelObject implements ModelObject {

    protected int hashCode = Integer.MIN_VALUE;

    private MetaClassImpl metaClass = null;

    protected boolean isInit = false; //flag which tell in init or not

    private boolean isSetDefaultValues = false; //flag which tell in init or not

    private ReadableVersion version;

    /**
     * 
     */
    protected AbstractModelObject() {
        /* may be null when hibernate create/load it */
    }

    /**
     * @param version
     * @throws ConstraintException
     */
    public AbstractModelObject(final ReadableVersion version) throws ConstraintException {
        this.version = version;
        SetDefaultValues(version);
    }

    /**
     * @see org.pimslims.metamodel.ModelObject#add(java.lang.String, java.util.Collection)
     */
    public boolean add(String roleName, Collection<? extends ModelObject> others) throws ConstraintException {

        return getMetaRole(roleName).add(this, others);
    }

    /**
     * @see org.pimslims.metamodel.ModelObject#add(java.lang.String, org.pimslims.metamodel.ModelObject)
     */
    public boolean add(final String roleName, final ModelObject other) throws ConstraintException {

        return getMetaRole(roleName).add(this, other);
    }

    private org.pimslims.metamodel.MetaRole getMetaRole(final String roleName) {
        return get_MetaClass().getMetaRole(roleName);
    }

    /**
     * @see org.pimslims.metamodel.ModelObject#checkConstraint(java.lang.Object, java.lang.String)
     */
    public boolean checkConstraint(final Object value, final String attributeName) {
        MetaAttribute ma = get_MetaClass().getAttribute(attributeName);
        return ma.isValid(value);
    }

    /**
     * @see org.pimslims.metamodel.ModelObject#checkInvariant()
     */
    public boolean checkInvariant() {
        // LATER invariant
        return false;
    }

    public int compareTo(final Object obj) {
        if (obj.hashCode() > hashCode()) {
            return 1;
        } else if (obj.hashCode() < hashCode()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Generic delete method. This is correct for many classes, but not all. The exceptions must override this
     * method with a generated one. This applies if the class has a required role, and deletion will not
     * cascade to those associates.
     * 
     * @see org.pimslims.metamodel.ModelObject#delete()
     */
    public void delete() throws AccessException, ConstraintException {
        final WritableVersion version = (WritableVersion) this.get_Version();
        assert !version.isCompleted() : "this transaction has already been committed or aborted";
        if (!version.mayDelete(this)) {
            throw new AccessException(this);
        }

        // remove the object from other objects use it (2way roles only, can not
        // know which object use it in 1 way role)
        final Collection<MetaRole> metaRoles = get_MetaClass().getMetaRoles().values();

        for (final MetaRole role : metaRoles) {
            if (!role.isOneWay() && !role.getName().equalsIgnoreCase(LabBookEntry.PROP_ACCESS)) {
                final MetaRole otherRole = role.getOtherRole();
                // delete this object's Child
                if (((MetaRoleImpl) otherRole).isParentRole()) {
                    for (final ModelObject otherSideMo : role.get(this)) {
                        try {
                            if (version.get(otherSideMo.get_Hook()) != null) {
                                otherSideMo.delete();
                            }
                        } catch (final ConstraintException e) {
                            // TODO some can be ignored, some can't
                        }
                    }
                }
                // not impliment isChangeable yet
                // else if(!otherRole.isChangeable()) {
                // won't be able to remove, hope it cascades
                // continue;
                // }
                // another side must not Required, remove from another side
                else if (!(otherRole.isRequired() && otherRole.getHigh() == 1)) {
                    //  System.out.println("role:" + role.getName() + " of " + this.get_Hook());
                    for (final ModelObject otherSideMo : role.get(this)) {
                        ((AbstractModelObject) otherSideMo).doRemove((MetaRoleImpl) otherRole, this);
                    }
                }

            }
        }

        // delete the related file
        if (this.get_MetaClass().getName().equalsIgnoreCase("org.pimslims.model.annotation.Annotation")) {
            //FileImpl.getFile(this).deleteWithoutAnnotation();

        } else {
            //for (File file : this.get_Files())
            //    file.delete();
        }

        final EntityManager session = version.getEntityManager();
        if (session.contains(this)) {
            session.remove(this);
            // System.out.println(this.get_Hook() + " is deleted!");

        }
    }

    public void doAdd(final MetaRoleImpl metaRole, final ModelObject otherObject) {
        if (otherObject == null) {
            return;
        }

        //X to 1 role
        if (metaRole.getHigh() == 1) {
            final AbstractModelObject oldMo = (AbstractModelObject) metaRole.getValue(this);

            //set this with new object
            metaRole.setValue(this, otherObject);

            //remove this from old related object
            if (oldMo != null) {
                oldMo.doRemove((MetaRoleImpl) metaRole.getOtherRole(), this);
            }
        } else {
            //X to M role
            final Collection<ModelObject> hbValues = (Collection<ModelObject>) metaRole.getValue(this);
            hbValues.add(otherObject);
            metaRole.setValue(this, hbValues);

        }

    }

    public void doRemove(final MetaRoleImpl metaRole, final ModelObject otherObject) {
        if (otherObject == null) {
            return;
        }

        //X to 1 role
        if (metaRole.getHigh() == 1) {
            metaRole.setValue(this, null);
        } else {
            //X to M role
            final Collection<ModelObject> otherObjects = metaRole.get(this);
            if (otherObjects.contains(otherObject)) {
                otherObjects.remove(otherObject);
            }
            metaRole.setValue(this, otherObjects);

        }

    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {

        if (null == obj) {
            return false;
        }

        if (!(obj instanceof AbstractModelObject)) {
            return false;
        }

        final AbstractModelObject target = (AbstractModelObject) obj;

        return (this.hashCode() == target.hashCode());

    }

    /**
     * @see org.pimslims.metamodel.ModelObject#findAll(java.lang.String, java.util.Map)
     */
    public <T extends ModelObject> Collection<T> findAll(final String roleName, final Map criteria) {
        if (criteria.size() == 1) {
            final Object key = criteria.keySet().iterator().next();
            final Object value = criteria.entrySet().iterator().next();
            if (key instanceof String && value instanceof ModelObject) {
                return findAll(roleName, (String) key, (ModelObject) value);
            }
        }
        return this.findAll(roleName, criteria, 0, Integer.MAX_VALUE);
    }

    /**
     * @see org.pimslims.metamodel.ModelObject#findAll(java.lang.String, java.util.Map)
     */
    public <T extends ModelObject> Collection<T> findAll(final String roleName, final Map criteria,
        final int start, final int limit) {
        if (criteria.size() == 1) {
            final Object key = criteria.keySet().iterator().next();
            final Object value = criteria.entrySet().iterator().next();
            if (key instanceof String && value instanceof ModelObject) {
                return findAll(roleName, (String) key, (ModelObject) value);
            }
        }
        return getMetaRole(roleName).findAll(this, criteria, start, limit);
    }

    /**
     * find the objects match a single criteria,, which using findAll(String roleName, String attributeName,
     * ModelObject attributeValue)
     * 
     * @param roleName
     * @param attributeName
     * @param attributeValue
     * @return objects match a single criteria
     */
    public <T extends ModelObject> Collection<T> findAll(final String roleName, final String attributeName,
        final Object attributeValue) {
        if (attributeValue instanceof ModelObject) {
            return getMetaRole(roleName).findAll(this, attributeName, (ModelObject) attributeValue, 0,
                Integer.MAX_VALUE);
        }
        final Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put(attributeName, attributeValue);
        return this.findAll(roleName, criteria, 0, Integer.MAX_VALUE);
    }

    /**
     * find the objects match a single criteria,, which using findAll(String roleName, String attributeName,
     * ModelObject attributeValue)
     * 
     * @param roleName
     * @param attributeName
     * @param attributeValue
     * @return objects match a single criteria
     */
    private <T extends ModelObject> Collection<T> findAll(final String roleName, final String attributeName,
        final Object attributeValue, final int start, final int limit) {
        if (attributeValue instanceof ModelObject) {
            return getMetaRole(roleName).findAll(this, attributeName, (ModelObject) attributeValue, start,
                limit);
        }
        final Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put(attributeName, attributeValue);
        return this.findAll(roleName, criteria, start, limit);
    }

    /**
     * find the first object linked
     * 
     * @param <T>
     * @param roleName
     * @return the first object match the criteria
     */
    public <T extends ModelObject> T findFirst(final String roleName) {
        final Map criteria = java.util.Collections.EMPTY_MAP;
        final Collection<ModelObject> result = findAll(roleName, criteria, 0, 1);
        if (result.size() == 0) {
            return null;
        }
        return (T) (result.iterator().next());
    }

    /**
     * find the first object match the single criteria by using findAll(String roleName, String attributeName,
     * Object attributeValue)
     * 
     * @param <T>
     * @param roleName
     * @param attributeName
     * @param attributeValue
     * @return the first object match the criteria
     */
    public <T extends ModelObject> T findFirst(final String roleName, final String attributeName,
        final Object attributeValue) {
        final Collection<ModelObject> result = findAll(roleName, attributeName, attributeValue, 0, 1);
        if (result.size() == 0) {
            return null;
        }
        return (T) result.iterator().next();
    }

    /**
     * flush this object if ID is null
     * 
     * @throws ConstraintException
     */
    public void flush() throws ConstraintException {

        ((org.pimslims.dao.WritableVersionImpl) this.get_Version()).saveNew(this);
        if (((ReadableVersionImpl) this.get_Version()).getFlushMode().isFlushSessionAfterCreate()) {
            ((org.pimslims.dao.WritableVersionImpl) this.get_Version()).flush(this);
        }
    }

    /**
     * @see org.pimslims.metamodel.ModelObject#get(java.lang.String)
     */
    public Collection get(final String roleName) {
        return getMetaRole(roleName).get(this);
    }

    /**
     * @see org.pimslims.metamodel.ModelObject#get_Version()
     */
    public ReadableVersion get_Version() {
        if (null == this.version) {
            this.version = ReadableVersionImpl.getVersion(this);
        }
        return this.version;
    }

    /**
     * get the hook
     */
    public String get_Hook() {

        // now dbid should be not null
        assert this.getDbId() != null : this.getClass() + " should not get a null ID";

        // hook should be className+:+dbid
        return this.getClass().getName() + ":" + this.getDbId();
    }

    /**
     * @see org.pimslims.metamodel.ModelObject#get_MayDelete()
     */
    public boolean get_MayDelete() {
        return this.get_Version().mayDelete(this);
    }

    /**
     * @see org.pimslims.metamodel.ModelObject#get_MayUpdate()
     */
    public boolean get_MayUpdate() {
        if (isInit || isSetDefaultValues) {
            return true; //during init or set default values, it is always updatable
        }
        return get_Version().mayUpdate(this);
    }

    /**
     * @see org.pimslims.metamodel.ModelObject#get_MetaClass()
     */
    public MetaClassImpl get_MetaClass() {
        if (metaClass == null) {

            Class c = this.getClass();
            metaClass = (MetaClassImpl) MetaClassImpl.getMetaClass(c);
        }
        return metaClass;
    }

    /**
     * @see org.pimslims.metamodel.ModelObject#get_Name()
     */
    public String get_Name() {
        return getName();
    }

    /**
     * @return
     */
    public abstract String getName();

    public String get_Owner() {
        if (null == this.getAccess()) {
            return Access.REFERENCE;
        }
        return this.getAccess().getName();
    }

    public String get_LabNotebookName() {
        return get_Owner();
    }

    /**
     * @see org.pimslims.metamodel.ModelObject#get_Value(java.lang.String)
     */
    public Object get_Value(final String attributeName) {
        return this.get_prop(attributeName);
    }

    /**
     * @see org.pimslims.metamodel.ModelObject#get_Values()
     */
    public Map<String, Object> get_Values() {
        Map<String, Object> values = new HashMap<String, Object>();
        Map as = get_MetaClass().getAttributes();
        for (java.util.Iterator i = as.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();
            String name = (String) e.getKey();
            Object value = ((MetaAttribute) e.getValue()).get(this);
            values.put(name, value);
        }
        return values;
    }

    /**
     * implemented in BaseClass TODO: AccessObject not linked to BaseClass, now linked to LabBookEntry
     */
    public abstract org.pimslims.model.core.LabNotebook getAccess();

    /**
     * get the id of this object
     * 
     * @return the id of this object
     */
    abstract public Long getDbId();

    @Override
    public int hashCode() {
        if (Integer.MIN_VALUE == this.hashCode) {
            this.hashCode = this.get_Hook().hashCode();
        }
        return this.hashCode;
    }

    /**
     * @see org.pimslims.metamodel.ModelObject#isAssociated(java.lang.String,
     *      org.pimslims.metamodel.ModelObject)
     */
    public boolean isAssociated(final String roleName, final ModelObject other) {
        return getMetaRole(roleName).areAssociated(this, other);
    }

    protected void init(final java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        isInit = true;
        try {
            for (final String attrName : attributes.keySet()) {
                set_prop(attrName, attributes.get(attrName));
            }
        } finally {
            isInit = false;
        }
        checkConstraints();
        //TODO namelist is disabled for performance
        //createNamelist(attributes);
        this.flush();

    }

    /**
     * check the data, by default it does not do anything
     * 
     * @throws ConstraintException
     */
    protected void checkConstraints() throws ConstraintException {
        //default it does not do anything

    }

    /**
     * @throws ConstraintException
     * @see org.pimslims.metamodel.ModelObject#remove(java.lang.String, java.util.Collection)
     */
    public boolean remove(final String roleName, final Collection<ModelObject> others)
        throws ConstraintException {
        return getMetaRole(roleName).remove(this, others);
    }

    /**
     * @throws ConstraintException
     * @see org.pimslims.metamodel.ModelObject#remove(java.lang.String, org.pimslims.metamodel.ModelObject)
     */
    public boolean remove(final String roleName, final ModelObject other) throws ConstraintException {
        return getMetaRole(roleName).remove(this, other);
    }

    /**
     * using the setter of base class to set a value get basesetter from attribute or role
     * 
     * @param prop_name
     * @param value
     * @throws ConstraintException
     * @throws AccessException
     */
    protected void set_prop(final String prop_name, final Object value) throws ConstraintException {

        final MetaProperty property = get_MetaClass().getProperty(prop_name);
        if (property == null) {
            throw new RuntimeException("Can not find property: " + prop_name + " in "
                + get_MetaClass().getName());
        } else if (property.isDerived()) {
            //ignore set for derived value
            return;
        }
        try {
            property.set_prop(this, value);
        } catch (AccessException e) {

            throw new RuntimeException(e);
        }

    }

    /**
     * using the getter of base class to get a value, get basegetter from attribute or role
     * 
     * @param prop_name
     * @param value
     * @throws ConstraintException
     */
    protected Object get_prop(final String prop_name) {

        final MetaProperty property = get_MetaClass().getProperty(prop_name);
        if (property == null)
            return null;

        return property.get_prop(this);
    }

    /**
     * @see org.pimslims.metamodel.ModelObject#set_Role(java.lang.String, java.util.Collection)
     */
    public void set_Role(final String roleName, final Collection<ModelObject> associates)
        throws AccessException, ConstraintException {
        this.set_prop(roleName, associates);
    }

    public void set_Role(final String roleName, final ModelObject associate) throws AccessException,
        ConstraintException {
        set_Role(roleName, Collections.singleton(associate));

    }

    /**
     * @see org.pimslims.metamodel.ModelObject#set_Value(java.lang.String, java.lang.Object)
     */
    public void set_Value(final String attributeName, final Object value) throws AccessException,
        ConstraintException {
        try {
            this.set_prop(attributeName, value);
        } catch (final ConstraintException e) {
            if (e.getCause() != null && e.getCause() instanceof AccessException) {
                throw (AccessException) e.getCause();
            }
            throw e;

        }
    }

    /**
     * @see org.pimslims.metamodel.ModelObject#set_Values(java.util.Map)
     */
    public void set_Values(final Map<String, Object> map) throws AccessException, ConstraintException {
        for (final String attributeName : map.keySet()) {
            this.set_prop(attributeName, map.get(attributeName));
        }
    }

    abstract protected void setDbId(Long dbId);

    /**
     * @throws ConstraintException
     * 
     */
    private void SetDefaultValues(final ReadableVersion version) throws ConstraintException {
        try {
            //get next ID for hibernate
            final Long nextID = IDGenerator.getNextID((WritableVersion) version);
            setDbId(nextID);
            //default properties

        } finally {
            isSetDefaultValues = false;
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (this.getDbId() == null) {
            return super.toString();
        }
        //else
        final String hook = get_Hook();
        final String name = get_Name();
        if (name == null || hook.contains(name)) {
            return get_Hook().toString();
        }
        //else
        return name + " [" + hook + "]";
    }

    protected boolean isEmpty(final Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            return ((String) value).length() == 0;
        }
        if (value instanceof Collection) {
            return ((Collection) value).size() == 0;
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.ModelObject#getFiles()
     */
    public Collection<File> get_Files() {
        if (!(this instanceof LabBookEntry)) {
            return Collections.EMPTY_SET;
        }
        final Collection<Annotation> annotations = ((LabBookEntry) this).getAnnotations();
        final Collection<FileImpl> ret = new java.util.ArrayList<FileImpl>();
        for (final Annotation element : annotations) {
            final Annotation annotation = element;
            ret.add(FileImpl.getFile(annotation));
        }
        // sort the files by their annotation's dbid
        FileImpl[] fileArray = new FileImpl[ret.size()];
        fileArray = ret.toArray(fileArray);
        java.util.Arrays.sort(fileArray);
        final Collection<File> fret = new java.util.ArrayList<File>();
        fret.addAll(java.util.Arrays.asList(fileArray));

        return fret;

    }

    /**
     * find the first object match the criteria, which using findAll(String roleName, Map criteria)
     * 
     * @param roleName
     * @param criteria
     * @return the first object match the criteria
     */
    public <T extends ModelObject> T findFirst(final String roleName, final Map criteria) {
        final Collection<ModelObject> result = findAll(roleName, criteria, 0, 1);
        if (result.size() == 0) {
            return null;
        }
        return (T) result.iterator().next();
    }
}
