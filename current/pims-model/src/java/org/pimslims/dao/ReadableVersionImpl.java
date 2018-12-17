/*
 * Created on 15-Oct-2004
 */

package org.pimslims.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.pimslims.access.Access;
import org.pimslims.access.PIMSAccess;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.DeleteConstraintException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.persistence.HibernateUtil;
import org.pimslims.persistence.JpqlQuery;
import org.pimslims.search.PIMSCriteria;
import org.pimslims.search.PIMSCriteriaImpl;
import org.pimslims.search.Paging;
import org.pimslims.upgrader.DatabaseUpdater;

/**
 * @author cm65
 * 
 */
/**
 * ReadableVersionImpl
 * 
 */
public class ReadableVersionImpl implements ReadableVersion {
    private int timeout = 90000;//in ms

    /**
     * @return Returns the timeout.
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * @param timeout The timeout (in ms) to set.
     */
    public void setTimeout(final int timeout) {
        this.timeout = timeout;
    }

    public boolean isTimeout() {

        if ((Calendar.getInstance().getTimeInMillis() - this.startDate.getTimeInMillis()) > timeout) {
            return true;
        }
        return false;
    }

    /**
     * currect version collection
     */
    private static final Collection<ReadableVersionImpl> currentVersions = Collections
        .synchronizedSet(new HashSet<ReadableVersionImpl>());

    /**
     * @param impl
     */
    private synchronized static void addToCurrentVersions(final ReadableVersionImpl rv) {
        Collection<ReadableVersionImpl> versions = null;
        synchronized (currentVersions) {
            versions = new HashSet<ReadableVersionImpl>(currentVersions);
        }
        for (final ReadableVersionImpl oldVersion : versions) {
            if (oldVersion.isTimeout()) {
                if (!oldVersion.isCompleted()) {
                    oldVersion.abort();
                }
            }
        }
        if (!rv.isCompleted()) {
            currentVersions.add(rv);
        }

    }

    /**
     * @return Returns the currentVersions.
     */
    public synchronized static Collection<ReadableVersionImpl> getCurrentVersions() {
        return currentVersions;
    }

    /**
     * @return how many rv is running
     */
    public synchronized static int getReadableVersionNumber() {
        return currentVersions.size();
    }

    /**
     * find out this version has this model object
     * 
     * @param mo
     * @return a version contains this model object
     */
    public synchronized static ReadableVersion getVersion(final ModelObject mo) {
        synchronized (currentVersions) {
            for (final ReadableVersion rv : currentVersions) {
                final Session session = rv.getSession();
                try {
                    if (session.isOpen() && session.contains(mo)) {
                        return rv;
                    }
                } catch (final org.hibernate.SessionException e) {
                    //session may closed
                    if (e.getMessage().contains("Session is closed")) {
                        continue;
                    }
                    throw e;
                }
            }
            return null;
        }
    }

    /**
     * @param impl
     */
    private static void removeFromCurrentVersions(final ReadableVersion rv) {
        currentVersions.remove(rv);

    }

    public static String nextName(final String tag, final String oldName) {
        String suffix = " " + tag + " 1";
        String prefix = oldName.trim();
        final Matcher m = getCopiedPattern(tag).matcher(oldName);
        if (m.matches()) {
            // it's e.g. "T0017.FL-pcr copy 3"
            final String number = m.group(2);
            final int copy = Integer.parseInt(number) + 1;
            prefix = m.group(1);
            suffix = " " + tag + " " + copy;
        }
        if (prefix.length() + suffix.length() > MAX_NAME_LENGTH) {
            prefix = prefix.substring(0, MAX_NAME_LENGTH - suffix.length());
        }
        return prefix + suffix;
    }

    /**
     * @param tag a string to use making new names - must avoid clashes with records that cant be viewed, e.g.
     *            username
     * @return a pattern matching any name made by makeName
     */
    public static Pattern getCopiedPattern(final String tag) {
        return Pattern.compile("(^.*?) " + tag + " (\\d{1,9})$");
    }

    // note that this does not use access control
    public static <T extends LabBookEntry> boolean alreadyExists(final ReadableVersion version,
        final String name, final Class<T> clazz) {
        javax.persistence.Query query =
            version.getEntityManager().createQuery(
                " select A from " + clazz.getName() + " as A where name=:name");
        try {
            query.setParameter("name", name).getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    private final String username;

    private final java.sql.Timestamp date;

    private final Calendar startDate = Calendar.getInstance();

    /**
     * The model this version is part of
     */
    protected ModelImpl model;

    /**
     * the session this version used
     */
    protected final EntityManager entityManager;

    @Deprecated
    // hibernate specific
    protected final Session session;

    private final StackTraceElement[] stackTrace;

    protected final AccessControllerImpl accessController;

    /**
     * @param username the user this transaction is for
     * @param model the model this is a version of
     * @param date the time this version was committed
     * @param session
     */
    public ReadableVersionImpl(final String username, final ModelImpl model, final java.sql.Timestamp date,
        EntityManager entityManager) {
        super();
        this.username = username;
        this.model = model;
        this.date = date;

        this.entityManager = entityManager;
        this.entityManager.getTransaction().begin();
        this.session = HibernateUtil.getSession(entityManager);
        this.session.setFlushMode(org.hibernate.FlushMode.MANUAL);// set this
        // session
        // is read
        // only
        // JMD setTimeout causes hibernate to fail later with postgres so restrict to oracle
        if (HibernateUtil.isOracleDB()) {
            this.session.getTransaction().setTimeout(this.timeout);
        }
        try {
            this.session.beginTransaction();
        } catch (org.hibernate.exception.GenericJDBCException e) {
            throw new RuntimeException(
                "Can not connect to the database, please ask administrator to check and start the database!",
                e);
        }
        stackTrace = (new RuntimeException()).getStackTrace();
        addToCurrentVersions(this);
        try {
            accessController = new AccessControllerImpl(this, username);
        } catch (final IllegalArgumentException ex) {
            this.abort();
            throw ex;
        }

    }

    @Override
    public void abort() {
        try {
            assert (!isCompleted());
            try {
                this.entityManager.getTransaction().rollback();
            } catch (final HibernateException e) {
                // we are already in error recovery, can't do anything more
                // just inform programmer
                System.out.println("session is open:" + this.entityManager.isOpen());
                e.printStackTrace(); // TODO log
            }
        } finally {

            if (this.entityManager.isOpen()) {
                this.entityManager.close();
            }
            removeFromCurrentVersions(this);
        }
    }

    /**
     * @see org.pimslims.dao.ReadableVersion#close()
     */
    @Override
    public void close() {
        if (!this.isCompleted()) {
            this.abort();
        }

    }

    @Override
    public void commit() throws AbortedException, ConstraintException {
        assert (!isCompleted());
        try {
            this.entityManager.flush();
            this.entityManager.getTransaction().commit();
            this.entityManager.close(); // only close session when it success
            // otherwise leave it open and wait for
            // abort(rollback)
        } catch (final Exception ex) {
            Throwable cause = ex;
            while (true) {
                if (cause.getMessage().toLowerCase().contains("unique")) {
                    if (cause instanceof java.net.SocketException) {
                        // SocketException means the database server has closed down
                        throw new org.pimslims.exception.AbortedException(null,
                            "Unable to contact database server", cause);
                    }
                }

                if (cause instanceof org.hibernate.exception.ConstraintViolationException) {
                    if (cause.getMessage().startsWith("could not delete")) {
                        throw new DeleteConstraintException(cause);
                    } else if (cause.getCause() != null
                        && cause.getCause().getMessage().toLowerCase().contains("unique")) {
                        throw new org.pimslims.exception.DuplicateKeyConstraintException(cause.getMessage()
                            + " as " + cause.getCause().getMessage(), cause.getCause());
                    } else {
                        throw new org.pimslims.exception.ConstraintException(cause.getMessage(),
                            cause.getCause());
                    }
                }
                if (cause instanceof org.hibernate.ObjectDeletedException
                    || cause instanceof org.hibernate.TransientObjectException) {
                    throw new org.pimslims.exception.ConstraintException(
                        "Deletion would violate a constraint:" + cause.getMessage(), cause);
                }
                if (cause instanceof org.hibernate.PropertyValueException) {
                    final org.hibernate.PropertyValueException _cause =
                        (org.hibernate.PropertyValueException) cause;
                    throw new org.pimslims.exception.ConstraintException(
                        "Deletion would violate a constraint: " + _cause.getEntityName() + "'s "
                            + _cause.getPropertyName() + "should not be null! ", cause);
                }
                if (null == cause.getCause()) {
                    break;
                }
                cause = cause.getCause(); // look deeper
            }
            throw new org.pimslims.exception.AbortedException(null, "commit failed", cause);
        } finally {
            removeFromCurrentVersions(this);

        }
    }

    //@Override
    public PIMSCriteria CreateQuery(final Class targetJavaClass, final Paging paging,
        Collection<LabNotebook> labNotebooks) {
        return new PIMSCriteriaImpl(this, targetJavaClass, paging, labNotebooks);
    }

    /**
     * 
     * @param metaClass the type of objects to return
     * @param attributeMap attribute name => value required or role name => model object required
     * @param isQueryCacheable query is cacheable if true
     * @return
     */
    private <T extends ModelObject> java.util.Collection<T> findAll(final Class javaClass,
        final java.util.Map<String, Object> attributeMap, final Boolean isQueryCacheable,
        final List<MetaRole> joinRoles, final Paging paging) {
        assert (!isCompleted());
        final PIMSCriteria query = this.CreateQuery(javaClass, paging, null);
        query.setAttributeMap(attributeMap);
        query.setIsQueryCacheable(isQueryCacheable);
        query.setJoinMetaRoles(joinRoles);
        query.setPaging(paging);

        return query.list();

    }

    @Override
    public <T extends ModelObject> java.util.Collection<T> findAll(final Class<T> javaClass,
        final java.util.Map<String, Object> attributeMap) {

        return findAll(javaClass, attributeMap, false, Collections.EMPTY_LIST, (Paging) null);

    }

    @Override
    public <T extends ModelObject> java.util.Collection<T> findAll(final Class<T> javaClass,
        final java.util.Map<String, Object> attributeMap, final Paging paging) {

        return findAll(javaClass, attributeMap, false, Collections.EMPTY_LIST, paging);

    }

    @Deprecated
    // slow
    public <T extends ModelObject> java.util.Collection<T> findAll(final Class<T> javaClass,
        final Map<String, Object> criteria, final Collection<String> joinRoleNames) {
        final List<MetaRole> joinRole = new ArrayList<MetaRole>();
        for (final String rolename : joinRoleNames) {
            joinRole.add(this.getMetaClass(javaClass).getMetaRole(rolename));
        }
        return findAll(javaClass, criteria, false, joinRole, (Paging) null);
    }

    /**
     * @see org.pimslims.dao.ReadableVersion#findAll(java.lang.Class, java.lang.String, java.lang.Object)
     */
    @Override
    public <T extends ModelObject> Collection<T> findAll(final Class<T> javaClass,
        final String attributeName, final Object Value) {
        final Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put(attributeName, Value);
        return findAll(javaClass, criteria);
    }

    /**
     * Returns a collection of all the instances of a specified type Note: for types like Experiment and
     * Sample this could be very large indeed.
     * 
     * @param metaClass the type of objects to return
     * @param attributeMap attribute name => value required or role name => model object required
     * @return a set containing all the objects of the type
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    // can be slow, use windowed find
    public <T extends ModelObject> java.util.Collection<T> findAll(final MetaClass metaClass,
        final java.util.Map<String, Object> attributeMap) {

        return findAll(metaClass.getJavaClass(), attributeMap);

    }

    /**
     * @see org.pimslims.dao.ReadableVersion#findFirst(java.lang.Class, java.util.Map)
     */
    @Override
    public <T extends ModelObject> T findFirst(final Class<T> javaClass, final Map<String, Object> criteria) {
        final Collection<T> result =
            findAll(javaClass, criteria, false, Collections.EMPTY_LIST, new Paging(0, 1));
        if (result.size() == 0) {
            return null;
        }
        // else
        return result.iterator().next();
    }

    /**
     * @see org.pimslims.dao.ReadableVersion#findFirst(java.lang.Class, java.lang.String, java.lang.Object)
     */
    @Override
    public <T extends ModelObject> T findFirst(final Class<T> javaClass, final String atrtibuteName,
        final Object Value) {
        final Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put(atrtibuteName, Value);
        return this.<T> findFirst(javaClass, criteria); // this T cast is forced
        // by a bug from sun
        // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6302954
    }

    /**
     * @see org.pimslims.dao.ReadableVersion#get(java.lang.Class, java.lang.Long)
     */
    @Override
    public <T extends ModelObject> T get(final Class<T> javaclass, final Long id) {
        return (T) get(javaclass.getName() + ":" + id);
    }

    @Override
    public <T extends ModelObject> T get(final Long id) {
        assert (!isCompleted());
        assert (id != null);
        final T object = (T) model.getByID(this, id);
        if (object == null || !mayRead(object)) {
            return null;
        }
        return object;
    }

    @Override
    public <T extends ModelObject> T get(final String hook) {
        final T object = (T) getWithoutAccessControl(hook);
        if (object == null || !mayRead(object)) {
            return null;
        }
        return object;
    }

    @Deprecated
    // can be slow
    public <T extends ModelObject> java.util.Collection<T> getAll(final Class<T> javaClass) {
        return getAll(javaClass, (Paging) null);
    }

    /**
     * @see org.pimslims.dao.ReadableVersion#getAllUpdatable(java.lang.Class)
     */
    @Override
    public <T extends ModelObject> Collection<T> getAllUpdatable(final Class<T> javaClass) {
        final Collection<T> allresults = getAll(javaClass, (Paging) null);
        final Collection<T> results = new LinkedList<T>();
        for (final ModelObject mo : allresults) {
            if (mo.get_MayUpdate()) {
                results.add((T) mo);
            }
        }
        return results;
    }

    /**
     * @see org.pimslims.dao.ReadableVersion#getAll(java.lang.Class, int, int)
     */
    public <T extends ModelObject> Collection<T> getAll(final Class<T> javaClass, final int start,
        final int limit) {
        return getAll(javaClass, new Paging(start, limit));
    }

    @Override
    public <T extends ModelObject> java.util.Collection<T> getAll(final Class<T> javaClass,
        final Collection<String> roleNamesToBeJoin) {
        assert (!isCompleted());
        assert null != javaClass;

        return findAll(javaClass, new HashMap<String, Object>(), roleNamesToBeJoin);
    }

    /**
     * @see org.pimslims.dao.ReadableVersion#getAll(java.lang.Class, org.pimslims.search.Paging)
     */
    @Override
    public <T extends ModelObject> Collection<T> getAll(final Class<T> javaClass, final Paging paging) {
        assert (!isCompleted());
        assert null != javaClass;
        return findAll(javaClass, new HashMap<String, Object>(), false, Collections.EMPTY_LIST, paging);
    }

    @Override
    @Deprecated
    // can be slow
    public <T extends ModelObject> java.util.Collection<T> getAll(final MetaClass metaClass,
        final Collection<String> roleNamesToBeJoin) {
        return getAll(metaClass.getJavaClass(), roleNamesToBeJoin);
    }

    @Override
    @SuppressWarnings("unchecked")
    /**
     * get count of certain class
     */
    public int getCountOfAll(final MetaClass metaClass) {
        assert (!isCompleted());
        return this.count(metaClass.getJavaClass(), Collections.EMPTY_MAP);
    }

    @Override
    public java.sql.Timestamp getDate() {
        return date;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws AccessException
     */
    @Override
    public String getDefaultOwner(final MetaClass metaClass, final ModelObject context)
        throws AccessException {
        return this.accessController.getDefaultOwner(metaClass, context);
    }

    /**
     * @param javaClass
     * @return the metadata for the data model class
     */
    @Override
    public MetaClass getMetaClass(final Class javaClass) {
        return this.getModel().getMetaClass(javaClass.getName());
    }

    /**
     * @return Returns the model.
     */
    @Override
    public AbstractModel getModel() {
        return model;
    }

    /**
     * Get next value from the db sequence
     * 
     * @param dbSequenceName String
     * @return long next value
     */
    public long getNextVal(final String dbSequenceName) {
        final long[] ret = new long[1];
        this.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                final DatabaseUpdater dbu = DatabaseUpdater.getUpdater(conn);
                ret[0] = dbu.getNextSeqVal(dbSequenceName);
            }
        });

        return ret[0];

    }

    /**
     * Find a data owner
     * 
     * @param ownerName the name of the access object
     * @return access object
     * @throws ConstraintException
     */
    @Override
    public org.pimslims.model.core.LabNotebook getOwner(final String ownerName) {
        return this.accessController.getOwner(ownerName);
    }

    @Override
    public ReadableVersion getPreviousVersion() {
        // not supported in this initial implementation
        return null;
    }

    /**
     * @return
     */
    @Override
    public Collection<LabNotebook> getReadableLabNotebooks() {

        return this.accessController.getReadAccessObjects();
    }

    /**
     * @see org.pimslims.dao.ReadableVersion#getSearchableFields(org.pimslims.metamodel.MetaClass)
     */
    @Override
    public HashMap<String, MetaAttribute> getSearchableFields(final MetaClass metaClass) {
        final Map<String, MetaAttribute> attributes = metaClass.getAttributes();
        final HashMap<String, MetaAttribute> searchableAttributes = new HashMap<String, MetaAttribute>();
        for (final MetaAttribute ma : attributes.values()) {
            if (ma.isDerived() || ma.isHidden() || !String.class.equals(ma.getType())) {
                continue;
            }
            //Special case for MolComponent's SeqString as it is clob type and not searchable
            //TODO using meta info instead
            if (ma.getName().equalsIgnoreCase("SeqString")) {
                continue;
            }
            if (JpqlQuery.isAttributePopulated(ma, this)) {
                searchableAttributes.put(ma.getName(), ma);
            }

        }
        for (final MetaClass subClass : metaClass.getSubtypes()) {
            searchableAttributes.putAll(getSearchableFields(subClass));
        }
        return searchableAttributes;
    }

    @Deprecated
    @Override
    public Session getSession() {
        return this.session;

    }

    /**
     * @return Returns the stackTrace.
     */
    public StackTraceElement[] getStackTrace() {
        return stackTrace;
    }

    public org.pimslims.model.accessControl.User getUser(final String username2) {
        return this.accessController.getUser(username2, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsername() {
        return username;
    }

    private <T extends ModelObject> T getWithoutAccessControl(final String hook) {
        assert (!isCompleted()) : "Transaction is completed";
        assert (hook != null);
        final T object = (T) model.getByHook(this, hook);
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAdmin() {
        return Access.ADMINISTRATOR.equals(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCompleted() {

        return !this.entityManager.isOpen();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mayCreate(final String owner, final MetaClass metaClass, final Map attributes) {
        return this.accessController.mayCreate(owner, metaClass, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mayDelete(final ModelObject object) {
        return this.accessController.mayDelete(object);
    }

    /**
     * @param object
     * @return boolean
     */
    public boolean mayRead(final ModelObject object) {
        if (this.accessController == null) {
            return true;
        }
        return this.accessController.mayRead(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mayUpdate(final ModelObject object) {
        return this.accessController.mayUpdate(object);
    }

    /**
     * Set database sequence value
     * 
     * @param dbSequenceName - sequence name
     * @param value - value to set
     */
    public void setSequenceStart(final String dbSequenceName, final int value) {
        this.getSession().doWork(new Work() {

            public void execute(Connection connection) throws SQLException {
                final DatabaseUpdater dbu = DatabaseUpdater.getUpdater(connection);
                dbu.setSequenceStart(dbSequenceName, value);
            }
        });
    }

    /**
     * @see org.pimslims.dao.ReadableVersion#count(java.lang.Class, java.util.Map)
     */
    @Override
    public <T extends ModelObject> int count(final Class<T> javaClass, final Map<String, Object> criteria) {
        assert (!isCompleted());
        final PIMSCriteria query = this.CreateQuery(javaClass, null, null);
        query.setAttributeMap(criteria);
        return query.count();

    }

    public static final Comparator<ModelObject> ALPHABETICAL_ORDER_OF_NAME = new Comparator<ModelObject>() {
        public int compare(final ModelObject arg0, final ModelObject arg1) {
            if (arg0 == null || arg0.get_Name() == null) {
                return -1;
            }
            if (arg1 == null || arg1.get_Name() == null) {
                return 1;
            }
            return arg0.get_Name().compareTo(arg1.get_Name());
        }
    };

    private static final int MAX_NAME_LENGTH = 80;

    /**
     * @see org.pimslims.dao.ReadableVersion#sortByName(java.util.Collection)
     */
    @Override
    public <T extends ModelObject> Collection<T> sortByName(final Collection<T> modelObjects) {
        final List<T> sortedResult = new ArrayList<T>(modelObjects);
        Collections.sort(sortedResult, ALPHABETICAL_ORDER_OF_NAME);
        return sortedResult;
    }

    /**
     * @see org.pimslims.dao.ReadableVersion#getCurrentUser()
     */
    @Override
    public User getCurrentUser() {
        return this.accessController.getUser();
    }

    /**
     * @return
     */
    public AccessControllerImpl getAccessController() {
        return this.accessController;
    }

    public String getAccessControlRoleName(final Class targetJavaClass) {
        if (this.accessController == null) {
            return null; //this.accessController may need todo search
        }
        return this.accessController.getAccessControlRoleName(targetJavaClass);
    }

    protected FlushMode pimsFlushMode = FlushMode.defaultMode();

    /**
     * @return Returns the pimsFlushMode.
     */
    public FlushMode getFlushMode() {
        if (pimsFlushMode == null) {
            pimsFlushMode = FlushMode.defaultMode();
        }
        return pimsFlushMode;
    }

    /**
     * @param pimsFlushMode The pimsFlushMode to set.
     */
    public void setFlushMode(final FlushMode pimsFlushMode) {
        this.pimsFlushMode = pimsFlushMode;
        this.entityManager.setFlushMode(pimsFlushMode.getJPAFlushMode());
    }

    public Collection<LabNotebook> getCurrentProjects() {
        if (this.getCurrentUser() == null)
            return getAll(org.pimslims.model.core.LabNotebook.class);
        //else
        return this.accessController.getAccessObjects(this.getCurrentUser(), PIMSAccess.CREATE);
    }

    public LabNotebook getFirstProject() {
        Collection<LabNotebook> projects = getCurrentProjects();
        if (projects.size() > 0)
            return projects.iterator().next();
        return null;
    }

    /**
     * WritableVersionImpl.getUniqueName
     * 
     * @see org.pimslims.dao.WritableVersion#getUniqueName(java.lang.Class, java.lang.String)
     */
    @Override
    public <T extends LabBookEntry> String getUniqueName(Class<T> class1, String prefix) {

        String pname = nextName(this.getUsername(), prefix);
        while (alreadyExists(this, pname, class1)) {
            pname = nextName(this.getUsername(), pname);
        }
        return pname;
    }

    /**
     * ReadableVersion.getCurrentLabNotebooks
     * 
     * @see org.pimslims.dao.ReadableVersion#getCurrentLabNotebooks()
     */
    @Override
    public Collection<LabNotebook> getCurrentLabNotebooks() {
        return this.getCurrentProjects();
    }

    /**
     * WritableVersion.getEntityManager
     * 
     * @see org.pimslims.dao.WritableVersion#getEntityManager()
     */
    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }
}
