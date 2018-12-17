/*
 * Created on 20-Dec-2004 @author: Chris Morris
 */
package org.pimslims.metamodel;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.exception.ConstraintViolationException;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.target.ExpressionObjective;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.search.Paging;
import org.pimslims.search.Paging.Order;

/**
 * @author cm65
 * 
 */
public class MetaClassImpl implements org.pimslims.metamodel.MetaClass {

    /**
     * Reports the type of the parent.
     * 
     * The old metamodel assumes that every object is associated with a "parent". This forms a network of
     * links, like a heirarchical database. These links are marked as "compositions" in the UML. For example,
     * the parent of a SampleComponent is the sample it is in. Note that this is "parenthood" is nothing to do
     * with class inheritance.
     * 
     * @return the java class of the parent
     */
    public Class getParentClass() {
        return annotation.parent();
    }

    /**
     * @return the parent role, if can not find return null
     */
    public MetaRole getParentRole() {

        String parentRoleName = this.annotation.parentRoleName();
        // not every class has parent
        if (parentRoleName == null || parentRoleName.length() == 0)
            return null;
        return this.getMetaRole(parentRoleName);
    }

    /**
     * Map class name => MetaClass
     * 
     * This is used to ensure there is a single MetaClass instance for each model type.
     */
    private static final java.util.Map<String, MetaClassImpl> CLASSES = new HashMap<String, MetaClassImpl>();

    // TODO there seems to be some duplication with Model

    /**
     * Returns a representation of a model type.
     * 
     * @param javaClass the java.lang.class of the type to represent
     * @return a uk.ac.pims.metamodel.MetaClass representing the type
     */
    public static final MetaClass getMetaClass(Class javaClass) {
        if (javaClass.isInterface()) {
            assert javaClass == ExpressionObjective.class
                || javaClass == org.pimslims.model.experiment.Project.class;
            javaClass = ResearchObjective.class;
        }
        if (AbstractModelObject.class == javaClass) {
            return MODEL_OBJECT;
        }
        String fullname = javaClass.getName(); // e.g. org.pimslims.model.experiment.Gel
        if (CLASSES.containsKey(fullname)) {
            return CLASSES.get(fullname);
        }
        //String packageName = javaClass.getPackage().getName();

        MetaClassImpl ret = new MetaClassImpl(javaClass);
        synchronized (MetaClassImpl.class) {
            CLASSES.put(fullname, ret);
        }
        return ret;
    }

    /**
     * subtypes in model of this type
     */
    private java.util.Set<MetaClass> subtypes = null;

    /**
     * name => MetaAttribute
     */
    private Map<String, MetaAttribute> attributes = null;

    private final Map<String, MetaAttribute> ignoredAttributes =
        new java.util.HashMap<String, MetaAttribute>();

    /**
     * name => MetaRole
     */
    private Map<String, MetaRole> roles = null;

    private final Map<String, MetaRole> ignoredRoles = new java.util.HashMap<String, MetaRole>();

    /**
     * The java class this represents
     */
    private final Class javaClass;

    private final org.pimslims.annotation.MetaClass annotation;

    /**
     * @param javaClass
     */
    @SuppressWarnings("unchecked")
    MetaClassImpl(final Class javaClass) {
        super();
        this.javaClass = javaClass;
        this.annotation = this.getAnnotation(org.pimslims.annotation.MetaClass.class);
        getMetaProperties();
        //System.out.println("Created:" + this);
    }

    /**
     * MetaClassImpl.getAnnotation
     * 
     * @see org.pimslims.metamodel.MetaElement#getAnnotation(java.lang.Class)
     */
    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return (T) this.javaClass.getAnnotation(annotationClass);
    }

    /**
     * {@inheritDoc}
     */
    public boolean checkConstraint(final Object value, final String attributeName, final ModelObject object) {
        MetaAttribute attribute = this.getAttribute(attributeName);
        org.pimslims.constraint.Constraint constraint = attribute.getConstraint();
        return constraint.verify(attributeName, value, object);
    }

    public MetaAttribute getAttribute(final String name) {
        assert attributes != null;
        //normal attribute
        if (attributes.get(name) != null) {
            return attributes.get(name);
        }
        assert this.ignoredAttributes != null;
        //ignored attribute
        if (this.ignoredAttributes.get(name) != null)
            return this.ignoredAttributes.get(name);
        //not attribute
        return null;

    }

    /**
     * internal properties, not for clients TODO remove these getters from generated classes
     */
    protected static final Set<String> IGNORED_PROPERTIES = Collections
        .unmodifiableSet(new java.util.HashSet<String>(java.util.Arrays.asList(new String[] {
            //attributes
            "dbId", "molecularMass", "hasNonStdChirality",
            //roles
            "applicationData", // handled specially, not as a role
        })));

    /**
     * return Attributes NOT include ignored {@inheritDoc}
     */
    public Map<String, MetaAttribute> getAttributes() {
        assert attributes != null;

        return Collections.unmodifiableMap(attributes);

    }

    /**
     * 
     * @return AllAttributes include ignored
     */
    public Map<String, MetaAttribute> getAllAttributes() {
        assert attributes != null;
        assert ignoredAttributes != null;
        Map<String, MetaAttribute> ret = new java.util.HashMap<String, MetaAttribute>();

        ret.putAll(attributes);
        ret.putAll(ignoredAttributes);
        return Collections.unmodifiableMap(ret);

    }

    /**
     * 
     * @return AllRoles include ignored
     */
    public Map<String, MetaRole> getAllRoles() {
        assert roles != null;
        assert ignoredRoles != null;
        Map<String, MetaRole> ret = new java.util.HashMap<String, MetaRole>();

        ret.putAll(roles);
        ret.putAll(ignoredRoles);
        return Collections.unmodifiableMap(ret);

    }

    /**
     * {@inheritDoc}
     */
    public Class getJavaClass() {
        return javaClass;
    }

    /**
     * {@inheritDoc}
     */
    public String getMetaClassName() {
        return javaClass.getName();
    }

    /**
     * {@inheritDoc}
     */
    public MetaRole getMetaRole(final String name) {
        assert roles != null;
        //normal role
        if (roles.get(name) != null) {
            return roles.get(name);
        }
        assert this.ignoredRoles != null;
        //ignored role
        if (this.ignoredRoles.get(name) != null)
            return this.ignoredRoles.get(name);
        //not role
        return null;
    }

    public Map<String, MetaRole> getIgnoredRoles() {
        assert this.ignoredRoles != null;
        return this.ignoredRoles;
    }

    /**
     * Get all the roles in this class and its superclasses, except the ignored ones.
     * 
     * @see org.pimslims.metamodel.MetaClass#getMetaRoles()
     */
    public Map<String, MetaRole> getMetaRoles() {
        assert roles != null;
        return java.util.Collections.unmodifiableMap(roles);
    }

    public Map<String, MetaProperty> getMetaProperties() {
        Map<String, MetaProperty> ret = new java.util.HashMap<String, MetaProperty>();

        if (attributes != null) { //has been calculated before
            ret.putAll(attributes);
            ret.putAll(roles);
            ret.putAll(ignoredAttributes);
            ret.putAll(ignoredRoles);
            return Collections.unmodifiableMap(ret);
        }

        //first time caculation
        MetaClass parentMetaClass = MetaClassImpl.getMetaClass(javaClass.getSuperclass());
        if (parentMetaClass != MODEL_OBJECT) {//add parent's properties
            Map<String, MetaProperty> parentRet = ((MetaClassImpl) parentMetaClass).getMetaProperties();
            for (String parentPropertyName : parentRet.keySet()) {
                MetaProperty parentProp = parentRet.get(parentPropertyName);
                if (parentProp instanceof MetaAttributeImpl) {
                    ret.put(parentPropertyName, new MetaAttributeImpl(this, (MetaAttributeImpl) parentProp));
                } else
                    ret.put(parentPropertyName, parentProp);
            }
        }
        //add metaProperies declared in current class
        ret.putAll(getDeclaredMetaProperties());

        //seperate attributes and roles
        seperateMetaProperties(ret);
        return Collections.unmodifiableMap(ret);

    }

    /**
     * put MetaProperties into attributes and roles
     * 
     * @param metaProperties
     */
    private void seperateMetaProperties(Map<String, MetaProperty> metaProperties) {
        //should only run once
        assert attributes == null;
        assert roles == null;

        attributes = new HashMap<String, MetaAttribute>();
        roles = new HashMap<String, MetaRole>();
        for (String name : metaProperties.keySet()) {
            MetaProperty property = metaProperties.get(name);
            if (property instanceof MetaAttribute) {
                //ignored attribute
                if (IGNORED_PROPERTIES.contains(name)) {
                    this.ignoredAttributes.put(name, (MetaAttribute) property);
                } else {
                    attributes.put(name, (MetaAttribute) property);
                }
            } else if (property instanceof MetaRole) {
                //ignored role
                if (IGNORED_PROPERTIES.contains(name)) {
                    this.ignoredRoles.put(name, (MetaRole) property);
                } else {
                    roles.put(name, (MetaRole) property);
                }
            } else {
                throw new RuntimeException(name + " is not a role or attribute of " + this);
            }
        }

    }

    /**
     * @return metaProperies declared in current class
     */
    private Map<String, MetaProperty> getDeclaredMetaProperties() {
        Map<String, MetaProperty> ret = new java.util.HashMap<String, MetaProperty>();
        // get methods of this class
        List<Method> methods = new LinkedList<Method>();
        methods.addAll(Arrays.asList(javaClass.getDeclaredMethods()));
        for (Method method : methods) {
            String methodName = method.getName();
            // find all normal getter
            if (methodName.startsWith("get") /*&& !methodName.startsWith("getHb") */) {
                String name = MetaUtils.getStandardName(methodName.substring(3));
                MetaProperty property = null;
                //try with metaAttribute
                try {
                    property = new MetaAttributeImpl(this, name);
                } catch (IllegalArgumentException e) {
                    //otherwise try with metaRole
                    property = MetaRoleImpl.getMetaRole(this, name);
                }
                if (property != null) {
                    ret.put(name, property);
                } else {
                    //must be a help method
                }
            }

        }
        return ret;
    }

    public String getDBTableName() {
        javax.persistence.Table tableAnnotation =
            (javax.persistence.Table) this.getJavaClass().getAnnotation(javax.persistence.Table.class);
        return tableAnnotation.name();
    }

    /**
     * @return all the roles declared in this class and its baseClass, but not more higher class eg:
     *         citation's role include baseCitation,citation but not ExperimentalData
     */
    public Map<String, MetaRole> getDeclaredMetaRoles() {
        Map<String, MetaRole> ret = new java.util.HashMap<String, MetaRole>();
        //add all metaRole of this metaClass
        ret.putAll(getAllRoles());

        //remove parentRoles
        if (this.getSupertype() instanceof MetaClassImpl) {

            MetaClassImpl parent = (MetaClassImpl) this.getSupertype();
            Map<String, MetaRole> parentRoles = parent.getAllRoles();

            for (String parentRoleName : parentRoles.keySet()) {
                ret.remove(parentRoleName);
            }
        }
        return ret;
    }

    /**
     * @return all the attributes declared in this class and its baseClass, but not more higher class
     */
    public Map<String, MetaAttribute> getDeclaredMetaAttributes() {
        Map<String, MetaAttribute> ret = new java.util.HashMap<String, MetaAttribute>();
        //add all metaRole of this metaClass
        ret.putAll(getAllAttributes());

        //remove parentRoles
        if (this.getSupertype() instanceof MetaClassImpl) {
            MetaClassImpl parent = (MetaClassImpl) this.getSupertype();
            Map<String, MetaAttribute> parentRoles = parent.getAllAttributes();

            for (String parentRoleName : parentRoles.keySet()) {
                ret.remove(parentRoleName);
            }
        }

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    synchronized public java.util.Set<MetaClass> getSubtypes() {
        if (subtypes != null) {
            return subtypes;
        }
        subtypes = new HashSet<MetaClass>();
        Class[] classes = annotation.subclasses();
        for (Class element : classes) {
            //System.out.println(element);
            subtypes.add(getMetaClass(element));
        }
        return subtypes;
    }

    /**
     * {@inheritDoc}
     */
    public MetaClass getSupertype() {
        Class s = javaClass.getSuperclass();
        if (null == s || Object.class == s || AbstractModelObject.class == s) {
            return MODEL_OBJECT;
        }
        if (null == s.getAnnotation(org.pimslims.annotation.MetaClass.class)) {
            // this is a temporary superclass used for renaming, e.g.
            // Database extends deprecated DbName extends PublicEntry
            s = s.getSuperclass();
        }
        return getMetaClass(s);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInstance(final ModelObject object) {
        return javaClass.isInstance(object);
    }

    /**
     * {@inheritDoc}
     */
    public String getShortName() {

        return javaClass.getSimpleName();
    }

    /**
     */
    public String getPackageShortName() {
        String tableName = getDBTableName();
        String[] shortNames = tableName.split("_");
        if (shortNames.length == 2) {
            return shortNames[0];
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isAbstract() {
        return Modifier.isAbstract(javaClass.getModifiers());
    }

    /**
     * {@inheritDoc}
     */
    public String getHelpText() {
        return annotation.helpText();
    }

    /**
     * Create a record of this type. It is the caller's responsibility to check access permissions.
     * 
     * @param version the current write transaction
     * @param ownerName the owner for the new object
     * @param attrs map name => value for fields of new object
     * 
     * @throws AccessException if the current user cannot do this
     * @throws ConstraintException if the values provided are inconsistent
     */
    @SuppressWarnings("unchecked")
    public <T extends ModelObject> T create(final org.pimslims.dao.WritableVersion version,
        final String ownerName, final Map<String, Object> attrs) throws AccessException, ConstraintException {

        if (!version.mayCreate(ownerName, this, attributes)) {
            throw new AccessException("User: " + version.getUsername() + " may not create this: "
                + getMetaClassName() + " with owner: " + ownerName);
        }

        Map<String, Object> attributes = new java.util.HashMap<String, Object>(attrs);
        // convert parameter values for associations
        // TODO remvoe this code, get_SpecificObject() now returns this
        Map<String, MetaRole> metaroles = new HashMap<String, MetaRole>(this.getMetaRoles());
        for (Object element : attributes.entrySet()) {
            Map.Entry entry = (Map.Entry) element;
            String name = (String) entry.getKey();
            MetaRole role = metaroles.get(name);
            if (null == role) {
                if (null == this.getAttribute(name)) {
                    System.out.println("Can not find role " + name + " of " + this.getName());
                    for (String roleName : metaroles.keySet())
                        System.out.println("found role: " + roleName);
                }
                assert null != this.getAttribute(name) : "unknown role: " + name;
                continue;
            }
            Collection associates = null;
            if (entry.getValue() instanceof Collection) {
                associates = (Collection) entry.getValue();
            } else {
                associates = Collections.singleton(entry.getValue());
            }
            if (0 == associates.size()) {
                // iter.remove();
                continue;
            }

            if (1 == role.getHigh()) {
                assert 1 >= associates.size() : "just one: " + name;
                java.util.List list = new ArrayList(associates);
                assert null != list.get(0) && list.get(0) instanceof ModelObject : "associate:" + name + " "
                    + list.get(0);
                attributes.put(name, list.get(0));
            }
        }

        // set the data ownership if is not provided
        if (!attrs.containsKey(LabBookEntry.PROP_ACCESS)
            && LabBookEntry.class.isAssignableFrom(this.javaClass)) {
            org.pimslims.model.core.LabNotebook owner;
            try {
                owner = version.getOwner(ownerName);
            } catch (AssertionError e) {
                throw new AccessException("No such owner: '" + ownerName + "' ");
            }
            attributes.put(LabBookEntry.PROP_ACCESS, owner);
        }

        // now invoke the constructor
        T instance;
        // Class parentClass = getParentClass();
        Class clazz = this.getJavaClass();

        /**
         * parent is disabled
         */

        try {
            java.lang.reflect.Constructor c =
                clazz.getConstructor(new Class[] { WritableVersion.class, java.util.Map.class });
            instance = (T) c.newInstance(new Object[] { version, attributes });
            // newInstance=(T)version.save(instance);
        } catch (ConstraintViolationException e) {
            Throwable cause = e;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            if (cause instanceof java.sql.BatchUpdateException) {
                cause = ((java.sql.BatchUpdateException) cause).getNextException();
                throw new RuntimeException(cause);
            }
            throw new ConstraintException(cause.getMessage(), e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            Throwable cause = e;
            if (e.getTargetException() != null) {
                cause = e.getTargetException();
                //cause.printStackTrace(); 
            }
            while (true) {
                if (cause instanceof ConstraintException) {
                    throw (ConstraintException) cause;
                } else if (cause.getClass().getName().contains("PSQLException")
                    && cause.getMessage().contains("constraint")) {
                    throw new ConstraintException(cause.getMessage(), cause);
                } else if (cause instanceof org.hibernate.PropertyValueException) {
                    throw new ConstraintException(cause.getMessage(), e);
                } else if (cause instanceof RuntimeException
                    && cause.getMessage().contains("Default owner hasn't been set")) {
                    throw new AccessException(cause.getMessage() + " during create " + this);
                }
                if (null == cause.getCause()) {
                    break;
                }
                cause = cause.getCause();

            }
            throw new RuntimeException(cause);
        }
        return instance;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.MetaModelElement#getAlias()
     */
    public String getAlias() {
        //TODO no, rename RefSample
        if (RefSample.class.equals(this.javaClass)) {
            return "Recipe";
        }
        return getShortName();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.MetaModelElement#getName()
     */
    public String getName() {
        return this.getMetaClassName();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.MetaModelElement#getMetaClass()
     */
    public MetaClass getMetaClass() {
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.MetaModelElement#isHidden()
     */
    public boolean isHidden() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.MetaModelElement#isRequired()
     */
    public boolean isRequired() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.MetaClass#getProperty(java.lang.String)
     */
    public MetaProperty getProperty(String name) {

        getMetaProperties();
        if (attributes.keySet().contains(name))
            return attributes.get(name);
        else if (ignoredAttributes.keySet().contains(name))
            return ignoredAttributes.get(name);

        return getMetaRole(name);
    }

    /**
     * Returns null if there is no unique key, or more than one.
     * 
     * @return the MetaAttribute representing the unique key for object of this class
     */
    public MetaAttribute getKey() {
        String[] keyNames = annotation.keyNames();
        if (1 != keyNames.length) {
            return null;
        }
        String keyName = keyNames[0];
        return getAttribute(keyName);
    }

    // get keyNames of metaClass
    public List<String> getKeyNames() {
        String[] keyNames = annotation.keyNames();
        return new ArrayList<String>(Arrays.asList(keyNames));

    }

    /**
     * MetaClass instance to represent base class
     */
    public static final MetaClass MODEL_OBJECT = new MetaClass() {

        /**
         * .getAnnotation
         * 
         * @see org.pimslims.metamodel.MetaElement#getAnnotation(java.lang.Class)
         */
        @Override
        public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            return null;
        }

        public ModelObject create(WritableVersion version, String owner, Map attributes)
            throws AccessException, ConstraintException {
            throw new UnsupportedOperationException("You can only create specific types of model object");
        }

        public String getMetaClassName() {
            return java.lang.Object.class.getName();
        }

        public String getShortName() {
            return "Object";
        }

        public boolean isAbstract() {
            return false;
        }

        public Class getJavaClass() {
            return java.lang.Object.class;
        }

        public boolean isInstance(ModelObject object) {
            return true;
        }

        public MetaClass getSupertype() {
            return null;
        }

        public Set getSubtypes() {
            // LATER could provide a list of all types
            return null;
        }

        public Invariant getInvariant() {
            return Invariant.NONE;
        }

        public boolean checkConstraint(Object value, String attributeName, ModelObject object) {
            return true;
        }

        public MetaAttribute getAttribute(String name) {
            return null;
        }

        public Map<String, MetaAttribute> getAttributes() {
            return new HashMap<String, MetaAttribute>();
        }

        public MetaRole getMetaRole(String name) {
            return null;
        }

        public Map<String, MetaRole> getMetaRoles() {
            return new HashMap<String, MetaRole>();
        }

        public MetaProperty getProperty(String name) {
            return null;
        }

        public String getHelpText() {
            return "PiMS record";
        }

        public String getName() {
            return "Object";
        }

        public String getAlias() {
            return "Object";
        }

        public boolean isHidden() {
            return false;
        }

        public MetaClass getMetaClass() {
            return this;
        }

        public boolean isRequired() {
            return false;
        }

        public Map<String, Order> getOrderBy() {
            return Collections.emptyMap();
        }

        public Collection<String> getJoinRoleNames() {
            return Collections.emptySet();
        }

    };

    @Override
    public String toString() {
        return "MetaClass:" + this.javaClass.getName();
    }

    /**
     * @see org.pimslims.metamodel.MetaClass#getOrderBy()
     */
    Map<String, Order> orderby = null;

    synchronized public Map<String, Order> getOrderBy() {
        if (orderby == null) {
            orderby = new HashMap<String, Order>();
            //TODO   if ("dbId".equalsIgnoreCase(annotation.orderBy()))
            if (annotation.orderBy().equalsIgnoreCase("dbId"))
                orderby.put(annotation.orderBy(), Paging.Order.DESC);
            else
                orderby.put(annotation.orderBy(), Paging.Order.ASC);
        }
        return orderby;

    }

    Collection<String> joinRoleNames = null;

    /**
     * @see org.pimslims.metamodel.MetaClass#getJoinRoleNames()
     */
    synchronized public Collection<String> getJoinRoleNames() {
        if (joinRoleNames == null) {
            joinRoleNames = new HashSet<String>();
            for (MetaRole metaRole : this.getMetaRoles().values()) {
                if (metaRole.getHigh() == 1)
                    if (metaRole.getOtherRole() != null && metaRole.getOtherRole().getHigh() == 1)
                    //1 to 1 role
                    {
                        if (!joinRoleNames.contains(metaRole.getName()))
                            joinRoleNames.add(metaRole.getName());
                    }
            }
        }
        return joinRoleNames;
    }
}
