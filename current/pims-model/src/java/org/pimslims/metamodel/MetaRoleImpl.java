/*
 * Created on 23-Dec-2004 @author: Chris Morris
 */
package org.pimslims.metamodel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.pimslims.annotation.Role;
import org.pimslims.dao.AccessControllerImpl;
import org.pimslims.dao.FlushMode;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.ReadableVersionImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.persistence.JpqlQuery;
import org.pimslims.search.PIMSCriteriaImpl;
import org.pimslims.search.Serial;

/**
 * Implementation of MetaRole that uses memops.metamodel.MetaRole
 * 
 * @version 0.1
 */
public final class MetaRoleImpl extends AbstractMetaProperty implements org.pimslims.metamodel.MetaRole {

    private static final Map<String, MetaRole> ROLES = new HashMap<String, MetaRole>();

    /**
     * @param metaClass the type the required role belongs to
     * @param roleName the name of the role (may be plural)
     * @return a representation of the role
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    protected static final MetaRole getMetaRole(final org.pimslims.metamodel.MetaClass metaClass,
        final String roleName) {
        final String storedRoleName = metaClass.getName() + "/" + roleName;
        if (ROLES.containsKey(storedRoleName)) {
            return ROLES.get(storedRoleName);
        }
        MetaRole ret;
        try {
            ret = new MetaRoleImpl(metaClass, roleName);
        } catch (final IllegalArgumentException e) {
            return null;
        }
        ROLES.put(storedRoleName, ret);
        return ret;
    }

    private final Role annotation;

    private final String baseName;

    private final String helpText;

    Pattern LAST_PART = Pattern.compile(".*?\\.(\\w+)");

    /**
     * @param metaClass the type this role belongs to
     * @param roleName name of role
     */
    private MetaRoleImpl(final org.pimslims.metamodel.MetaClass metaClass, final String roleName) {
        super(metaClass, roleName);

        this.annotation = getAnnotation(Role.class);
        if (annotation == null) {
            throw new IllegalArgumentException(metaClass + " does not have annotation for role: " + name);
        }

        this.baseName = MetaUtils.getSingleName(roleName); //TODO using role's basename when it is available
        this.helpText = this.annotation.helpText();

    }

    /**
     * {@inheritDoc}
     */
    public boolean add(final ModelObject a, final ModelObject b) throws ConstraintException {
        assert null != b;

        if (1 == getHigh()) {
            //* to 1
            // no add method, just a set method
            final List<ModelObject> old = new LinkedList<ModelObject>(get(a));
            if (0 < old.size()) {
                final ModelObject oldB = old.get(0);
                if (b.equals(oldB)) {
                    return false; // no change to collection
                }
                throw new ConstraintException(a, getRoleName(), b, "At most one associate allowed");
            }
            super.setValue(a, b);
            return true;
        } // else
          //* to m
        final Collection<ModelObject> bs = this.get(a);
        bs.add(b);
        this.set_Role(a, bs);

        return true;
    }

    public boolean add(final ModelObject a, final java.util.Collection<? extends ModelObject> bs)
        throws ConstraintException {
        boolean ret = false;

        for (final Object element : bs) {
            final ModelObject b = (ModelObject) element;
            ret = ret | add(a, b);
        }

        return ret;
    }

    public boolean remove(final ModelObject a, final java.util.Collection<ModelObject> bs)
        throws ConstraintException {
        boolean ret = false;

        for (final ModelObject b : bs) {
            ret = ret | remove(a, b);
        }

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    public boolean areAssociated(final ModelObject a, final ModelObject b) {
        // LATER this implementation is inefficient, a method is needed in the
        //  API
        return get(a).contains(b);
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    // can be slow
    public <T extends ModelObject> java.util.Collection<T> findAll(final ModelObject a,
        final Map<String, Object> criteria) {

        return findAll(a, criteria, 0, Integer.MAX_VALUE);
    }

    /**
     * @see org.pimslims.metamodel.MetaRole#findAll(org.pimslims.metamodel.ModelObject, java.util.Map, int,
     *      int)
     */
    public <T extends ModelObject> Collection<T> findAll(final ModelObject a, Map<String, Object> criteria,
        final int start, final int limit) {
        if (criteria == null || criteria.isEmpty()) {
            return get(a);// get all role related
        }
        try {
            criteria = PIMSCriteriaImpl.rebuildAttributes(this.getOtherMetaClass(), criteria);
            /**
             * currently the criteria of attribute of root Class is used for search LATER fully findAll
             * include subclass
             */
            final String entityAlias = "A";
            ReadableVersion version = a.get_Version();
            final AccessControllerImpl aci = ((ReadableVersionImpl) version).getAccessController();

            // create a new Criteria for attributes of root Class used for
            // search
            // eg: select B from org.pimslims.model.target.Target A inner join
            // A.hbBlueprintComponents B where A=:A AND B.ComponentType='abc'
            // Order by B.dbId desc
            final JpqlQuery select =
                JpqlQuery.createRoleFindAllHQL(this.getOwnMetaClass(), this, criteria, entityAlias, false,
                    aci.getAccessControlRoleName(this.getOwnMetaClass().getJavaClass()), version);
            //System.out.println("HQL: "+selectHQL);
            select.setFirstResult(start);
            select.setMaxResults(limit);

            select.setEntity(entityAlias, a);
            select.doBindingParameters(criteria, new Serial());
            final Collection<T> res = select.list();

            return res;
        } catch (final SecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @see org.pimslims.metamodel.MetaRole#findAll(org.pimslims.metamodel.ModelObject, java.lang.String,
     *      org.pimslims.metamodel.ModelObject)
     */
    public <T extends ModelObject> java.util.Collection<T> findAll(final ModelObject a,
        final String roleName, final ModelObject attributeValue) {
        return findAll(a, roleName, attributeValue, 0, Integer.MAX_VALUE);
    }

    /**
     * @see org.pimslims.metamodel.MetaRole#findAll(org.pimslims.metamodel.ModelObject, java.lang.String,
     *      org.pimslims.metamodel.ModelObject, int, int)
     */
    public <T extends ModelObject> Collection<T> findAll(final ModelObject mo, final String attributeName,
        final ModelObject attributeValue, final int start, final int limit) {
        try {

            attributeValue.get_Hook();// make sure ModelObject attributeValue
            // is saved

            final String ThisEntityAlias = "A";
            final String roleEntityAlias = "B";
            // create a new Criteria for attributes of root Class used for
            // search
            final JpqlQuery selectHQL =
                JpqlQuery.createRoleFindAllHQL(this.getOwnMetaClass().getJavaClass(), this.getRoleName(),
                    attributeName, ThisEntityAlias, roleEntityAlias, mo.get_Version());
            selectHQL.setEntity(ThisEntityAlias, mo);
            selectHQL.setParameter(roleEntityAlias, ((AbstractModelObject) attributeValue).getDbId());
            selectHQL.setFirstResult(start);
            selectHQL.setMaxResults(limit);

            final Collection<T> res = selectHQL.list();

            return res;
        } catch (final SecurityException ex) {
            throw new RuntimeException(ex);
        } catch (final IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    // can be slow
    public <T extends ModelObject> Collection<T> get(final ModelObject _a) {
        //final ModelObject a = _a;
        if (!_a.get_MetaClass().getJavaClass().isInstance(_a)) {
            return _a.get(this.name);// proxy's get method will use real
        }
        // instance
        Collection<T> results = null;

        final Object res = super.getValue(_a);
        if (res == null) {
            // must have been 0..1 assocation
            return Collections.emptyList();
        } else if (res instanceof Collection) {
            if (_a.get_Version() instanceof WritableVersion) {
                results = getNewCollection((Collection<T>) res);
            } else {
                results = (Collection<T>) res;//getNewCollection((Collection<T>) res);
            }
        } else {
            // must have been high==1
            assert res instanceof ModelObject;
            results = getNewCollection(Collections.singleton((T) res));
        }

        if (null == results) {
            // it must have been a 0..1 role with no member
            return Collections.emptySet();
        }
        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHelpText() {
        return this.annotation.helpText();
    }

    /**
     * {@inheritDoc}
     */
    public int getHigh() {
        return this.annotation.high();
    }

    /**
     * {@inheritDoc}
     */
    public int getLow() {
        return this.annotation.low();
    }

    /**
     * @return the java class on the other side of this role
     */
    Class otherJavaClass = null;

    public Class getOtherJavaClass() {
        if (otherJavaClass == null) {
            if (this.getHigh() == 1) {
                otherJavaClass = super.getType();
            } else if (super.getGenericType() instanceof ParameterizedType) {
                final Type[] returnTypes =
                    ((ParameterizedType) super.getGenericType()).getActualTypeArguments();
                otherJavaClass = (Class) returnTypes[0];
            } else {
                throw new IllegalArgumentException(super.getGenericType().toString());
            }
        }
        return otherJavaClass;

    }

    /**
     * {@inheritDoc}
     */
    public MetaClass getOtherMetaClass() {
        return MetaClassImpl.getMetaClass(getOtherJavaClass());
    }

    MetaRole otherRole = null;

    /**
     * {@inheritDoc}
     */
    public MetaRole getOtherRole() {
        if (otherRole == null) {
            final MetaClass otherMetaClass = getOtherMetaClass();
            final Map<String, MetaRole> otherRoles = otherMetaClass.getMetaRoles();
            final Map<String, MetaRole> otherRolesIgnored =
                ((MetaClassImpl) otherMetaClass).getIgnoredRoles();
            final String otherRoleName = this.annotation.reverseRoleName();
            if (otherRoleName == null || otherRoleName.length() == 0) {
                otherRole = null;
            } else {
                final Collection<String> pissbleOtherRoleNames = getOtherRoleNames(otherRoleName);

                for (final String possibleRoleName : pissbleOtherRoleNames) {

                    if (otherRoles.keySet().contains(possibleRoleName)) {
                        otherRole = otherRoles.get(possibleRoleName);
                        break;
                    } else if (otherRolesIgnored.keySet().contains(possibleRoleName)) {
                        otherRole = otherRolesIgnored.get(possibleRoleName);
                        break;
                    }
                }
            }
        }

        return otherRole;
    }

    /**
     * @param pissbleOtherRoleName
     * @return
     */
    private Collection<String> getOtherRoleNames(final String pissbleOtherRoleName) {
        // TODO this is a fake method to get other role name
        final Collection<String> possibleRoleNames = new HashSet<String>();
        possibleRoleNames.add(pissbleOtherRoleName);

        if (pissbleOtherRoleName.endsWith("s")) {
            possibleRoleNames.add(pissbleOtherRoleName + "ses");
        } else if (pissbleOtherRoleName.endsWith("y")) {
            possibleRoleNames.add(pissbleOtherRoleName.substring(0, pissbleOtherRoleName.length() - 2)
                + "ies");
        } else {
            possibleRoleNames.add(pissbleOtherRoleName + "s");
            possibleRoleNames.add("lab" + pissbleOtherRoleName.substring(0, 1).toUpperCase()
                + pissbleOtherRoleName.substring(1) + "s");
        }

        return possibleRoleNames;
    }

    public org.pimslims.metamodel.MetaClass getOwnMetaClass() {
        return metaClass;
    }

    /**
     * {@inheritDoc}
     */
    public String getRoleName() {
        return name;
    }

    /**
     * For upgrader, if it is a abstract role, it need to be ignored in DB
     */
    public boolean isAbstract() {
        return this.annotation.isAbstract();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isChangeable() {

        // PIMS-319 at the moment, all role which both high and low is 1 should
        // be unchangeable . (suggested by Chris)
        // all such kind of role into unchangeable in DM.
        if (this.getHigh() == 1 && this.getLow() == 1) {
            return false;
        }

        return this.annotation.isChangeable();
    }

    /**
     * @return true if this is a parent/child role
     */
    public boolean isChildRole() {
        if (null == getOtherRole()) {
            // not navigable the other way
            return false;
        }
        return ((MetaRoleImpl) getOtherRole()).isParentRole();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.MetaRole#isHidden()
     */
    @Override
    public boolean isHidden() {
        return "access".equals(getName());
    }

    /**
     * {@inheritDoc}
     */
    public boolean isOrdered() {
        return this.annotation.isOrdered();
    }

    Boolean isOneWay = null;

    /**
     * 
     * @return true if the role is one way
     */
    public boolean isOneWay() {
        if (isOneWay == null) {
            if (this.getOtherRole() == null) {
                isOneWay = true;
            } else {
                isOneWay = false;
            }
        }
        return isOneWay;
    }

    /**
     * @return true if this role is a child/parent role
     */
    Boolean isParentRole = null;

    public boolean isParentRole() {
        if (isParentRole == null) {
            final MetaRole parentRole = ((MetaClassImpl) this.metaClass).getParentRole();
            if (parentRole == null) {
                isParentRole = false;
            } else {
                isParentRole = parentRole.getName().equalsIgnoreCase(this.getName());
            }
        }
        return isParentRole;
    }

    /**
     * To comply with MetaModelElement interface
     */
    @Override
    public boolean isRequired() {
        return getLow() > 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws ConstraintException
     */
    public boolean remove(final ModelObject a, final ModelObject b) throws ConstraintException {
        assert null != b;

        if (1 == getHigh()) {
            //* to 1
            // no remove method, just a set method
            final List<ModelObject> old = new LinkedList<ModelObject>(get(a));
            if (0 < old.size()) {
                final ModelObject oldB = old.get(0);
                if (b.equals(oldB)) {
                    super.setValue(a, null);
                    return true;
                }
                throw new ConstraintException(a, getRoleName(), b, b + " is not in " + a);
            }
        } // else
          //* to m
        final Collection<ModelObject> bs = new HashSet(this.get(a));
        bs.remove(b);
        this.set_Role(a, bs);

        return true;
    }

    /**
     * Remove existing associates and set new ones
     * 
     * @param a object to set associates for
     * @param bs new collection of associates
     * @throws ConstraintException
     */
    public void set_Role(final ModelObject a, final Collection<ModelObject> bs) throws ConstraintException {
        ((AbstractModelObject) a).set_prop(this.getName(), bs);
    }

    /**
     * {@inheritDoc}
     */
    public boolean verify(final ReadableVersion version) {
        // LATER constraint
        return false;
    }

    public javax.persistence.JoinColumn getDBJoinColumn() {
        try {
            final javax.persistence.JoinColumn column = getAnnotation(javax.persistence.JoinColumn.class);
            return column;
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * @throws AccessException
     * @see org.pimslims.hibernate.AbstractMetaProperty#set_prop(org.pimslims.metamodel.ModelObject,
     *      java.lang.Object)
     */
    @Override
    public void set_prop(final ModelObject mo, Object value) throws ConstraintException, AccessException {

        //set role
        final MetaRoleImpl metaRole = this;
        if (((ReadableVersionImpl) mo.get_Version()).getFlushMode().isCheckValue()) {

            if (metaRole.isDerived()) {
                throw new RuntimeException("Can not set role/attribute " + this.getName() + " in "
                    + this.getMetaClass() + " as it is derived value");
            }
        }
        if (value == null) {
            value = Collections.EMPTY_SET;
        }
        //for *->M role, convert no-collection value to collection
        if (metaRole.getHigh() != 1) {
            if (!(value instanceof Collection)) {
                value = metaRole.getNewCollection(Collections.singleton(value));
            }
            if (value instanceof List && getType().toString().contains("Set")) {
                value = metaRole.getNewCollection(Collections.singleton(value));
            }

        } else if (metaRole.getHigh() == 1 && (value instanceof Collection)) {
            //for *->1 role, convert collection value to single value
            assert ((Collection) value).size() <= 1;
            if (((Collection) value).size() == 0) {
                value = null;
            } else if (((Collection) value).size() == 1) {
                value = ((Collection) value).iterator().next();
            } else {
                throw new ConstraintException(mo, metaRole.getName(), value, "Can not set " + metaRole
                    + "(high=1) with value " + value);
            }
        }

        // JMD Make FlushMode.autoUpdateInverseRoles do something
        //if (metaRole.isOneWay() || MetaClassImpl.IGNORED_PROPERTIES.contains(this.getName())) {
        FlushMode flushMode = ((ReadableVersionImpl) mo.get_Version()).getFlushMode();
        if (metaRole.isOneWay() || MetaClassImpl.IGNORED_PROPERTIES.contains(this.getName())
            || (!flushMode.isAutoUpdateInverseRoles())) {
            //|| this.getName().equalsIgnoreCase("access")) {
            // access is a oneway role
            super.setValue(mo, value);
        } else //two way role
        {
            final MetaRoleImpl otherMetaRole = (MetaRoleImpl) getOtherRole();
            //remove this from old other role objects
            final Collection<ModelObject> oldValues = get(mo);
            for (final ModelObject oldOtherObject : oldValues) {
                if (value instanceof ModelObject
                    || (value instanceof Collection && !((Collection) value).contains(oldOtherObject))) {
                    ((AbstractModelObject) oldOtherObject).doRemove(otherMetaRole, mo);
                }
            }
            //set value by reflection
            if (metaRole.getHigh() != 1) {

                final Collection<ModelObject> hbValues = (Collection<ModelObject>) super.getValue(mo);

                hbValues.clear();
                hbValues.addAll((Collection<? extends ModelObject>) value);
                value = hbValues;
            }
            //baseSetter.invoke(this, getRoleValue(metaRole, value));
            super.setValue(mo, value);
            //add this to new other role objects
            if (value == null) {
                /*nothing should do*/
            } else if (value instanceof Collection) {
                for (final ModelObject newOtherObject : (Collection<ModelObject>) value) {
                    if (!oldValues.contains(newOtherObject) && newOtherObject != null) {
                        ((AbstractModelObject) newOtherObject).doAdd(otherMetaRole, mo);
                    }
                }
            } else if (value instanceof ModelObject) {
                ((AbstractModelObject) value).doAdd(otherMetaRole, mo);
            } else {
                throw new RuntimeException(value
                    + " should be instanceof ModelObject or Collection<ModelObject>!");
            }
        }

        if (metaRole.getName().equals(LabBookEntry.PROP_ACCESS)) {
            updateChildOwner(mo, value);
        }

    }

    /**
     * MetaRoleImpl.updateChildOwner
     * 
     * @param mo
     * @param value
     * @throws ConstraintException
     * @throws AccessException
     */
    private void updateChildOwner(ModelObject mo, Object value) throws AccessException, ConstraintException {
        Map<String, MetaRole> allRoles = mo.get_MetaClass().getMetaRoles();
        for (MetaRole role : allRoles.values()) {
            if (((MetaRoleImpl) role).isChildRole()) {
                if (LabBookEntry.class.isAssignableFrom(role.getOtherMetaClass().getJavaClass())) {
                    final Collection<ModelObject> children = mo.get(role.getName());
                    for (ModelObject child : children) {
                        child.set_Value(LabBookEntry.PROP_ACCESS, value);
                    }
                }
            }
        }
    }

    /**
     * @see org.pimslims.metamodel.MetaProperty#get_prop(org.pimslims.metamodel.ModelObject)
     */
    @Override
    public Object get_prop(final ModelObject mo) {

        Object returnValue = super.getValue(mo);
        if (!((ReadableVersionImpl) mo.get_Version()).getFlushMode().isCheckValue()) {
            return returnValue;
        }

        if (returnValue != null) {
            //final MetaClass returnClass = this.getOtherMetaClass();
            if (this.getHigh() != 1) {
                final Collection oldValues = (Collection) returnValue;
                returnValue = new LinkedList();
                for (final Object value : oldValues) {
                    if (value != null
                        && ((ReadableVersionImpl) mo.get_Version()).mayRead((ModelObject) value)) {
                        ((Collection) returnValue).add(value);
                    }
                }

            } else if (this.getHigh() == 1) {
                returnValue = mo.get_Version().get(((ModelObject) returnValue).get_Hook());
            }
            if (this.getHigh() != 1) {
                returnValue = getNewCollection((Collection) returnValue);
            }
        }

        return returnValue;
    }

    /**
     * @param returnValue
     * @return
     */
    private Collection getNewCollection(Collection returnValue) {
        Collection newCollection;
        if (getType().toString().endsWith("List")) {
            newCollection = new LinkedList();
        } else {
            newCollection = new HashSet();
        }
        if (returnValue.size() == 1) {
            if (returnValue.iterator().next() instanceof Collection) {
                returnValue = (Collection) returnValue.iterator().next();
            }
        }

        newCollection.addAll(returnValue);
        while (newCollection.contains(null)) {
            newCollection.remove(null);
        }
        return newCollection;
    }

}
