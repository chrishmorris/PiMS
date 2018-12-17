/*
 * Created on 14-Oct-2004 @author: Chris Morris
 */
package org.pimslims.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.naming.Context;

import org.hibernate.ObjectDeletedException;
import org.hibernate.SessionFactory;
import org.pimslims.access.Access;
import org.pimslims.access.AccessImpl;
import org.pimslims.constraint.Constraint;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.AbstractModelObject;
import org.pimslims.metamodel.Invariant;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaClassImpl;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.SystemClass;
import org.pimslims.model.reference.PublicEntry;
import org.pimslims.persistence.HibernateUtil;
import org.pimslims.upgrader.Messages;
import org.pimslims.upgrader.ModelUpdateVersion;
import org.pimslims.upgrader.ModelUpdateVersionImpl;
import org.pimslims.util.InstallationProperties;

/**
 * Represents the data model for protein production., This implementation is based on the code generated by
 * the hibnerate tools.
 */

/**
 * @author cm65
 * 
 */
public class ModelImpl extends AbstractModel {

    private static boolean isRevisionNumberChecked = true;// true: revision number in DB & jar will be automaticly checked

    protected static InstallationProperties properties;

    /**
     * @return a list of all the data owners currently defined
     */
    public Collection getAllOwnerNames() {
        throw new UnsupportedOperationException("implementation being changed");
    }

    final public String getProperty(final String name) {
        return properties.getRequiredProperty(name);
    }

    /**
     * The model - for normal purposes, there should only be one.
     */
    private static ModelImpl singleton = null;

    /**
     * The normal way to get a model.
     * 
     * @return the model
     */
    public static synchronized AbstractModel getModel() {
        if (null == singleton) {
            InstallationProperties props = new InstallationProperties();
            if (props.isMock()) {
                //singleton = new MockModel(props);
            } else {
                singleton = new ModelImpl(props);
            }
        }
        return singleton;
    }

    /**
     * @param properties
     * @return the model
     * @throws FileNotFoundException
     */
    public static synchronized AbstractModel getModel(final File properties) throws FileNotFoundException {
        if (null == singleton) {
            singleton = new ModelImpl(new InstallationProperties(properties));
        }
        return singleton;
    }

    /**
     * @param properties
     * @return the model
     */
    public static synchronized AbstractModel getModel(final Context properties) {
        if (null == singleton) {
            singleton = new ModelImpl(properties);
        }
        return singleton;
    }

    /*
     * private static synchronized void discardModel(ModelImpl model) { if (singleton==model)
     * {singleton=null;} }
     */

    /**
     * time of last update
     */
    protected java.sql.Timestamp date = new java.sql.Timestamp(new Date().getTime());

    /**
     * persistence represents and entity manager factory (session factory)
     */
    private final HibernateUtil persistence;

    /**
     * @param props the properties files
     */
    protected ModelImpl(InstallationProperties props) {
        super(new AccessImpl());
        properties = props;
        this.persistence = new HibernateUtil();
        checkRevisionNumber();
    }

    public static InstallationProperties getInstallationProperties() {
        assert null != properties;
        return properties;
    }

    private void checkRevisionNumber() {
        /* if (HibernateUtil.isOracleDB()) {
            return;
        } */
        System.out.println("PIMS connecting to database: " + properties.getRequiredProperty("db.url"));
        if (isRevisionNumberChecked) {
            final ModelUpdateVersionImpl muv = (ModelUpdateVersionImpl) getModelUpdateVersion();
            try {

                if (!muv.isRevisionNumberSame()) {
                    muv.abort();
                    final String ErrorMsg = Messages.getString("Upgrader_Error_Msg");
                    throw new RuntimeException(ErrorMsg);
                }
                muv.commit();
            } catch (final AbortedException e) {
                e.printStackTrace(); // LOG
                throw new RuntimeException(e);
            } catch (final ConstraintException e) {
                e.printStackTrace(); // LOG
                throw new RuntimeException(e);
            } finally {
                if (muv != null) {
                    if (muv.getSession() != null) {
                        if (!muv.isCompleted()) {
                            muv.abort();
                        }
                    }
                }
            }
        }
    }

    /**
     * @param propertiesFile
     * @throws FileNotFoundException
     */
    public ModelImpl(final File propertiesFile) throws FileNotFoundException {
        super(new AccessImpl());
        this.persistence = new HibernateUtil();
        try {
            properties = new InstallationProperties(propertiesFile);
        } catch (final FileNotFoundException e) {
            System.out.println("Cannot find properties file: " + propertiesFile.getAbsolutePath() + " " // print
                // absolute
            // path
                );
            throw e;
        }
        checkRevisionNumber();

    }

    /**
     * @param propertiesContext
     */
    public ModelImpl(final Context propertiesContext) {
        super(new AccessImpl());
        properties = new InstallationProperties(propertiesContext);
        this.persistence = new HibernateUtil();

        checkRevisionNumber();

    }

    /**
     * Constructor for ModelImpl
     */
    protected ModelImpl() {
        // for MockModel
        this.persistence = new HibernateUtil();
    }

    /**
     * @param version the version of the model to get for
     * @param id the dbid of object to get
     * @return a model object
     */
    <T extends ModelObject> T getByID(final ReadableVersion version, final Long id) {
        T result = (T) version.getSession().get(LabBookEntry.class, id);
        if (result == null) {
            result = (T) version.getSession().get(PublicEntry.class, id);
        }
        if (result == null) {
            result = (T) version.getSession().get(SystemClass.class, id);
        }
        if (result == null) {
            result = (T) version.getSession().get(Attachment.class, id);
        }
        return result;

    }

    // private java.lang.reflect.Method getter = null;
    /**
     * LATER support multiple version
     * 
     * @param version the version of the model to get for
     * @param hook a string identifying the object to get
     * @return a model object
     */
    @SuppressWarnings("unchecked")
    <T extends ModelObject> T getByHook(final ReadableVersion version, final String hook) {
        final int i = hook.indexOf(":"); // see ReadableVersion.getHook
        if (i == -1) {
            return null;
        }
        Long dbId;
        try {
            dbId = new Long(hook.substring(i + 1));
        } catch (final NumberFormatException ex1) {
            return null;
        }
        try {
            final Class javaClass = Class.forName(hook.substring(0, i));
            T specificObject;

            // it is needed to get ride of exception when The object with that
            // id was deleted
            // Later should have better solution to tell it is deleted or not
            // version.flush();

            specificObject = (T) version.getSession().get(javaClass, dbId);

            if (specificObject == null) {
                return null;
            }
            // make sure the java class is correct, it seems hibernate does not
            // check the javaclass.
            if (!specificObject.get_Hook().equals(hook)) {
                return null;
            }

            // was ((AbstractModelObject) specificObject).setReflectiveModelObject(version);
            return specificObject;

        } catch (final SecurityException ex) {
            throw new RuntimeException(ex);
        } catch (final IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (final ObjectDeletedException ex) {
            return null;
        } catch (final ClassNotFoundException ex) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetaClass getRootMetaClass() {
        return MetaClassImpl.MODEL_OBJECT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addAssociation(final String role1name, final MetaClass type1, final int low1,
        final int high1, final int low2, final int high2, final MetaClass type2, final String role2name) {
        // LATER add association
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected MetaAttribute addAttribute(final MetaClass metaClass, final String attributeName,
        final Class javaClass, final Constraint constraint) {
        final MetaAttribute a = metaClass.getAttribute(attributeName);
        if (a == null) {
            return null;
        }
        if (a.getType() != javaClass) { // TODO check constraint
            throw new IllegalStateException("incompatible attribute exists for: " + attributeName);
        }
        return a;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetaClass getMetaClass(final String metaClassName) {
        try {
            return MetaClassImpl.getMetaClass(Class.forName(metaClassName));
        } catch (final ClassNotFoundException ex) {
            throw new AssertionError("metaClass not found: " + metaClassName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected MetaClass addMetaClass(final Class javaClass, final String typeName, final MetaClass supertype,
        final Invariant invariant) {
        final MetaClass type = MetaClassImpl.getMetaClass(javaClass);
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized ReadableVersion getReadableVersion(final String username) {

        final ReadableVersion currentVersion =
            new ReadableVersionImpl(username, this, date, this.persistence.getEntityManager());
        assert (!(currentVersion instanceof WritableVersion));
        return currentVersion;
    }

    /**
     * Time limit for transactions
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized WritableVersion getWritableVersion(final String username) {
        WritableVersion currentVersion;
        try {
            currentVersion =
                new WritableVersionImpl(username, this, date, this.persistence.getEntityManager());
        } catch (final org.hibernate.exception.JDBCConnectionException e) {
            // not connected - will be detected separately
            e.printStackTrace(); // TODO log
            return null;
        }
        return currentVersion;
    }

    /**
     * AbstractModel.getTestVersion
     * 
     * @see org.pimslims.dao.AbstractModel#getTestVersion()
     */
    @Override
    public WritableVersion getTestVersion() {
        WritableVersion currentVersion;
        try {
            currentVersion = new TestVersionImpl(this, date, this.persistence.getEntityManager());
        } catch (final org.hibernate.exception.JDBCConnectionException e) {
            // not connected - will be detected separately
            e.printStackTrace(); // TODO log
            return null;
        }
        return currentVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelUpdateVersion getModelUpdateVersion() {
        ModelUpdateVersion currentVersion;
        try {
            currentVersion =
                new ModelUpdateVersionImpl(Access.ADMINISTRATOR, this, date,
                    this.persistence.getEntityManager());
        } catch (final org.hibernate.exception.JDBCConnectionException e) {
            throw new RuntimeException(e);
        }
        return currentVersion;
    }

    /**
     * Map metaclass name => name of methods in memops.implementation.Project
     * 
     * private static final java.util.Map<String, String> METHOD_NAMES = new java.util.HashMap<String,
     * String>(); static { METHOD_NAMES.put("org.pimslims.model.experiment.Experiment", "LabExperiment"); }
     */

    /**
     * Set appropriate criterion when searching for a subclass
     * 
     * @param metaClass type being searched for
     * @param criteria map name => value of search criteria
     * @return criteria, modified if necessary
     * 
     *         private java.util.Map getCriteria(final MetaClass metaClass, final java.util.Map criteria) {
     *         java.util.Map ret = ModelImpl.getSpecificObjects(criteria); Class javaClass =
     *         metaClass.getJavaClass(); if
     *         (javaClass.getSuperclass()==org.pimslims.model.sample.Sample.class) { ret.put("sampleType",
     *         metaClass.getShortName()); } if (TYPES.containsKey(metaClass.getMetaClassName())) {
     *         ret.put("experimentType", TYPES.get(metaClass.getMetaClassName())); } return ret; }
     */

    /**
     * @return a set of strings, the names of the model types
     */
    @Override
    public List<String> getClassNames() {
        final List<String> ret = new java.util.LinkedList<String>();
        final Class baseClass = AbstractModelObject.class;
        getSubClassNames(baseClass, ret);
        return ret;
    }

    /**
     * @param baseClass
     * @param ret
     */
    private void getSubClassNames(final Class baseClass, final List<String> ret) {
        final org.pimslims.annotation.MetaClass annotation =
            (org.pimslims.annotation.MetaClass) baseClass
                .getAnnotation(org.pimslims.annotation.MetaClass.class);
        if (annotation == null) {
            return;
        }
        for (final Class _class : annotation.subclasses()) {
            ret.add(_class.getName());
            getSubClassNames(_class, ret);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finalize() throws Throwable {
        this.persistence.finalize();
        super.finalize();
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public synchronized ReadableVersion getCurrentVersion(final ModelObject object) {
        return object.get_Version();
    }

    protected void setLastUpdateTime(final java.sql.Timestamp lastUpdateTime) {
        this.date = lastUpdateTime;
    }

    /**
     * @param b
     */
    public static void setCheckRevisionNumber(final boolean b) {
        isRevisionNumberChecked = b;
    }

    /**
     * ModelImpl.isAllSessionsClosed
     * 
     * @see org.pimslims.dao.AbstractModel#isAllSessionsClosed()
     */
    @Override
    public boolean isAllSessionsClosed() {
        return this.persistence.areAllSessionsClosed();
    }

    /**
     * ModelImpl.getSessionFactory
     * 
     * @return
     */
    @Deprecated
    // hibernate specific
    public SessionFactory getSessionFactory() {
        return this.persistence.getSessionFactory();
    }

    public void evict(Class class1) {
        ((ModelImpl) this).getSessionFactory().getCache().evictEntityRegion(class1);
    }

}
