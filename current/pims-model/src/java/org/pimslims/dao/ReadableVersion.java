/*
 * Created on 26-Jul-2004 @author: Chris Morris
 */
package org.pimslims.dao;

import java.util.Collection;
import java.util.HashMap;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.search.Paging;

/**
 * Provides access to the data model for a read transaction.
 * 
 * The caller is responsible for either calling commit() or abort(). The best idiom for doing this is: <code>
 * WritableVersion rv = model.getWritableVersion(username);
 * try {
 *     // your code here
 *     rv.commit();
 * } finally {
 *     if (!rv.isCompleted) {rv.abort();}
 * }</code>
 * 
 * 
 * @version 0.1
 * @has 1 - * org.pimslims.metamodel.ModelObject
 * @depend - 1 org.pimslims.metamodel.AbstractModel
 */
public interface ReadableVersion extends java.io.Closeable {

    /**
     * @return time data was last committed
     */
    public java.sql.Timestamp getDate();

    /**
     * Find all accessible objects of a model type.
     * 
     * @param javaClass of objects to find
     * @return list of all objects of the type that are readable by the current user
     * 
     * @deprecated use getAll(javaClass, int, int) If you are sure that the number of results is acceptable,
     *             you can hard code 0, 100. Otherwise you should page the results.
     */
    @Deprecated
    public <T extends ModelObject> java.util.Collection<T> getAll(Class<T> javaClass);

    /**
     * get all updatable objects
     */
    @Deprecated
    public <T extends ModelObject> java.util.Collection<T> getAllUpdatable(Class<T> javaClass);

    /**
     * abort version if not completed
     */
    @Override
    public void close();

    /**
     * @param javaClass of objects to find
     * @param start index in list of first object to return
     * @param limit maximum number of objects to return
     * @return the records found
     */
    public <T extends ModelObject> Collection<T> getAll(Class<T> javaClass, int start, int limit);

    /**
     * @param javaClass of objects to find
     * @param Paging paging
     * @return the records found
     */
    public <T extends ModelObject> Collection<T> getAll(Class<T> javaClass, Paging paging);

    /**
     * Find all accessible objects of a model type.
     * 
     * @param metaClass of objects to find
     * @param joinRoleNames the roles want to be joined
     * @return list of all objects of the type that are readable by the current user
     * @deprecated use javaClass instead of metaClass
     */
    @Deprecated
    public <T extends ModelObject> java.util.Collection<T> getAll(MetaClass metaClass,
        Collection<String> joinRoleNames);

    /**
     * Find all accessible objects of a model type.
     * 
     * @param javaClass of objects to find
     * @param joinRoleNames the roles want to be joined
     * @return list of all objects of the type that are readable by the current user
     */
    @Deprecated
    public <T extends ModelObject> java.util.Collection<T> getAll(Class<T> javaClass,
        Collection<String> joinRoleNames);

    /**
     * Get the count of a model type.
     * 
     * @param metaClass of objects to find
     * @return the count
     */
    public int getCountOfAll(final MetaClass metaClass);

    /**
     * Get the count of a class which meet the criteria
     * 
     * @param <T>
     * @param javaClass
     * @param criteria
     * @return
     */
    public <T extends ModelObject> int count(Class<T> javaClass, java.util.Map<String, Object> criteria);

    /**
     * Find objects of a model type, matching some criteria.
     * 
     * Note: this initial specification has equality tests only. OJB includes an API for more general tests,
     * matching the expressivity of SQL. We should consider providing this.
     * 
     * @see ModelObject#findAll
     * @see ModelObject#get
     * 
     * @param metaClass of objects to find
     * @param criteria attribute name => value required or role name => model object required
     * @return list of all objects of the type, matching the criteria, that are readable by the current user
     * @deprecated use javaClass instead of metaClass
     */
    @Deprecated
    public <T extends ModelObject> java.util.Collection<T> findAll(MetaClass metaClass,
        java.util.Map<String, Object> criteria);

    /**
     * Find objects of a model type, matching some criteria.
     * 
     * 
     * @see ModelObject#findAll
     * @see ModelObject#get
     * 
     * @param javaClass of objects to find
     * @param criteria attribute name => value required or role name => model object required
     * @return list of all objects of the type, matching the criteria, that are readable by the current user
     */
    @Deprecated
    public <T extends ModelObject> java.util.Collection<T> findAll(Class<T> javaClass,
        java.util.Map<String, Object> criteria);

    /**
     * Find objects of a model type, matching some criteria with paging.
     * 
     * @see #findAll
     * @param Paging paging info
     * @return list of all objects of the type, matching the criteria, that are readable by the current user
     */
    public <T extends ModelObject> java.util.Collection<T> findAll(Class<T> javaClass,
        java.util.Map<String, Object> criteria, Paging paging);

    /**
     * @param <T> a data model class
     * @param javaClass
     * @param atrtibuteName
     * @param Value
     * @return the records found
     */
    public <T extends ModelObject> java.util.Collection<T> findAll(Class<T> javaClass, String atrtibuteName,
        Object Value);

    /**
     * @param javaClass
     * @param criteria
     * @return the first result of findAll(Class javaClass, java.util.Map<String, Object> criteria)
     */
    public <T extends ModelObject> T findFirst(Class<T> javaClass, java.util.Map<String, Object> criteria);

    /**
     * @param javaClass
     * @param atrtibuteName
     * @param Value
     * @return the first result of findAll(Class javaClass, String atrtibuteName,Object Value)
     */
    public <T extends ModelObject> T findFirst(Class<T> javaClass, String atrtibuteName, Object Value);

    /**
     * Find objects of a model type, matching criteria which specifiy attributes of associated types.
     * 
     * The list returned is a list of objects of the type specified, selected so that they are associated with
     * objects that match the criteria. For example to find all samples that include sodium hydroxide:
     * 
     * List sodiumHydroxide = version.find( Sample, map, // "Molecule.Formula" => "NaOH" list // [
     * Sample_SampleComponent, SampleComponent_Molecule ] );
     * 
     * @param javaClass of objects to find
     * @param criteria "type name"."attribute name" => value required
     * @param join a List of MetaRoles
     * @return list of all objects of the type, matching the criteria, that are readable by the current user
     *         ('AND' search)
     */
    @Deprecated
    public <T extends ModelObject> java.util.Collection<T> findAll(Class<T> javaClass,
        java.util.Map<String, Object> criteria, java.util.Collection<String> join);

    /**
     * Indicates that a transaction is complete. This method is required for read transactions as well as
     * write transactions, because some optimistic algorithms require the ability to cancel read transactions.
     * 
     * If AbortedException is thrown, the caller should retry. It is not necessary to call abort() after an
     * AbortedException.
     * 
     * If ConstraintException is thrown, a read transaction should abort and retry. A write transaction could
     * try to correct the constraints, or could abort.
     * 
     * @throws AbortedException if inconsistent values have been returned because this transaction conflicts
     *             with another
     * @throws ConstraintException if the model values are inconsistent
     */
    public void commit() throws AbortedException, ConstraintException;

    /**
     * Cancels the transaction. For a write transaction, cancels the pending updates the transaction made. For
     * a read transaction, exits the transaction and does not provide an AbortedException to warn of errors.
     * This should not be used for a read transaction merely to avoid the catch clause.
     */
    public void abort();

    /**
     * @return true if commit or abort has been called
     */
    public boolean isCompleted();

    /**
     * Get a model object
     * 
     * @see ModelObject#get_Hook
     * 
     * @param hook a string specifying the object
     * @return the user's representation
     * 
     */
    public <T extends ModelObject> T get(String hook);

    /**
     * Get a model object by ID
     * 
     * 
     * @param id the dbid of a modelObject
     * @return the user's representation
     * 
     */
    public <T extends ModelObject> T get(Long id);

    /**
     * get by id with class provided
     * 
     * @param javaclass
     * @param id the dbid of a modelObject
     * @return
     */
    public <T extends ModelObject> T get(Class<T> javaclass, Long id);

    /**
     * Get the previous version of the model.
     * 
     * This method is needed eventually, but it is OK for initial implementations to throw an
     * UnsupportedOperationException.
     * 
     * @return model as it was before last write transaction
     */
    public ReadableVersion getPreviousVersion();

    /**
     * Check whether this transaction is for an administrator. Used to implement special rules, when modifying
     * the types used for access control.
     * 
     * @return true if the owner of this version is an administrator
     */
    boolean isAdmin();

    /**
     * @return the name of the user carrying out this transaction
     */
    String getUsername();

    /**
     * @return a reference to the model this is a version of
     */
    public AbstractModel getModel();

    // methods for access control
    /**
     * Find a data owner
     * 
     * @param ownerName the name of the access object
     * @return access object
     * @throws ConstraintException
     */
    public org.pimslims.model.core.LabNotebook getOwner(String ownerName);

    /**
     * Check if an object is writable
     * 
     * @param object the object to update
     * @return true if the owner of the current version could change this object
     */
    public abstract boolean mayUpdate(ModelObject object);

    /**
     * Check if an object can be deleted
     * 
     * @param object the object to delete
     * @return true if the owner of the current version could delete this object
     */
    public abstract boolean mayDelete(ModelObject object);

    /**
     * Report whether the current user has creation rights with a specific Access object in a specific model
     * type.
     * 
     * @param owner represents the intended ownership of the new object
     * @param metaClass the model type of the intended object
     * @param attributes the intended attributes for the new object, may be examined by a Condition
     * @return true if the current transaction can create object of this type
     */
    @Deprecated
    // seems not to be so useful
    public boolean mayCreate(String owner, MetaClass metaClass, java.util.Map attributes);

    // hibernate specific, better to use getEntityManager
    public Session getSession();

    public EntityManager getEntityManager();

    /**
     * Gets a suitable default owner which the current owner is likely to use to create a new object of a
     * specified type.
     * 
     * Will return null if the user is not permitted to create any objects of the type concerned, of if it
     * isn't possible to identify one of the permitted owners as a likely default.
     * 
     * @param metaClass the model type of the object to be created
     * @param context a model object related to the request, e.g. the parent of the object to be created
     * @return model object representing the most likely owner object
     * @throws AccessException
     */
    public String getDefaultOwner(MetaClass metaClass, ModelObject context) throws AccessException;

    /**
     * Return the name of the fields which is (1) possible to search in. (2) contains some values (something
     * has been recorded)
     * 
     * @param metaClass
     * @return Map - MetaAttributeName -> MetaAttribute
     */
    public HashMap<String, MetaAttribute> getSearchableFields(MetaClass metaClass);

    /**
     * get the metaClass of a java class
     * 
     * @param javaClass
     * @return metaClass
     */
    public MetaClass getMetaClass(Class javaClass);

    /**
     * @param samples
     * @return
     */
    public <T extends ModelObject> Collection<T> sortByName(Collection<T> modelObjects);

    /**
     * @return the user of this session
     */
    public User getCurrentUser();

    public Collection<LabNotebook> getCurrentLabNotebooks();

    /**
     * ReadableVersion.getUniqueName
     * 
     * @param class1 The data model class the new record will be in
     * @param unique The start of the new name
     * @return A name which is not yet used in the table for this class
     */
    public <T extends LabBookEntry> String getUniqueName(Class<T> class1, String prefix);

    public abstract Collection<LabNotebook> getReadableLabNotebooks();
}
