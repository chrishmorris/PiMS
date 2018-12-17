/*
 * Created on 26-Jul-2004 @author: Chris Morris
 */
package org.pimslims.metamodel;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.util.File;

/**
 * Provides generic methods for manipulating a model object.
 * 
 * It can be used to view and update an in-memory representation of a persistent model object. Each instance
 * is part of a "version" of the model, corresponding to a current transaction. Alternatively a generated
 * class will provide specific methods to get and set the attributes of the object, and to view and change its
 * associations.
 * 
 * @see AbstractModel
 * @see MetaClass
 * @see ReadableVersion
 * @has * - 1 org.pimslims.metamodel.MetaClass
 * 
 * @version 0.1
 */
public interface ModelObject {

    /**
     * a constant representing a set of no model objects Provided as a convenience for generics-safe clients.
     */
    java.util.Set<ModelObject> EMPTY_SET = java.util.Collections.emptySet();

    /**
     * @return a representation of the transaction this is part of
     */
    public abstract ReadableVersion get_Version();

    /**
     * @return the name of the owner of the information in this object
     */
    @Deprecated
    // use get_LabNotebookName
    public abstract String get_Owner();

    public abstract String get_LabNotebookName();

    /**
     * Returns a representation of the type of this object
     * 
     * @return MetaClass object representing the metadata
     */
    public abstract MetaClass get_MetaClass();

    /**
     * Gets a "hook" that represents a persistent object in the data model.
     * 
     * @see ReadableVersion#get
     * 
     * @return string uniquely specifying a persistent model object
     */
    public abstract String get_Hook();

    /**
     * Get a user-friendly name for an object
     * 
     * Note: Some model types have name fields which could be used to improve this. We really need a
     * convention that there is a string field called "name".
     * 
     * @return a human-readable name for the object
     */
    public String get_Name();

    /**
     * Check whether this object's state is valid.
     * 
     * @return true if the object is in a consistent state
     */
    public abstract boolean checkInvariant();

    // methods for attributes

    /**
     * gets the value of an attribute
     * 
     * @param attributeName the name of the field to get
     * @return the value of the attribute
     */
    public abstract Object get_Value(String attributeName);

    /**
     * @return attribute name => value
     */
    public abstract java.util.Map<String, Object> get_Values();

    /**
     * sets the value of an attribute.
     * 
     * @precondition the value is an instance of the correct class
     * @param attributeName the name of the field to set
     * @param value the new value of the attribute
     * @throws AccessException if the object is not writable
     * @throws ConstraintException if the value is not legal
     */
    abstract void set_Value(String attributeName, Object value) throws AccessException, ConstraintException;

    /**
     * Sets several attributes or roles and then checks the invariant for the model type as well as the
     * constraints for the individual attributes. Where there is a consistency rule relating the values of
     * several attributes, this method can be used to ensure that the objec's state is always valid.
     * 
     * The value passed for a role must be a collection of ModelObjects.
     * 
     * @param attributes map of attribute or role name => value
     * @throws AccessException if the object is not writable
     * @throws ConstraintException if after the new values are set the object is not in a legal state
     */
    abstract void set_Values(java.util.Map<String, Object> attributes) throws AccessException,
        ConstraintException;

    /**
     * Checks the legality of a value for an attribute
     * 
     * @param attributeName of attribute
     * @param value desired
     * @return true if the value is permitted
     */
    public abstract boolean checkConstraint(Object value, String attributeName);

    // methods for roles

    /**
     * Report whether another object is associated with an object of this type
     * 
     * @precondition the objects belong to the same version
     * @param roleName name of role to be checked
     * @param other object of other type
     * @return true if they are currently associated
     */
    abstract boolean isAssociated(String roleName, ModelObject other);

    /**
     * Returns a collection of all objects associated with this one in the role concerned.
     * 
     * If the role is unordered, then the collection is a Set. If the role is ordered, then the collection is
     * a List.
     * 
     * @precondition the supplied object is of this type
     * @param roleName name of role to be checked
     * @return a list of the associated objects
     */
    abstract java.util.Collection get(String roleName);

    /**
     * Sets the associates of this object in a specified role.
     * 
     * This is equivalent to remove(roleName, get(roleName)); add(roleName, associates); except that it checks
     * the cardinality constraints to ensure that the association is left in a permitted state.
     * 
     * If the role is ordered and the collection passed is a List, then the order will be saved, so that
     * get(roleName) will return an equal list.
     * 
     * @param roleName name of role to be modified
     * @param associates new associates for this role
     * @throws AccessException if the changes are not permitted
     * @throws ConstraintException if the modified role is inconsistent
     * 
     */
    abstract void set_Role(String roleName, java.util.Collection<ModelObject> associates)
        throws AccessException, ConstraintException;

    /**
     * Set the associate of this object in a specified role.
     * 
     * @param roleName name of role to be modified
     * @param associate new associate for this role
     * @throws AccessException if the changes are not permitted
     * @throws ConstraintException if the modified role is inconsistent
     * 
     */
    abstract void set_Role(String roleName, ModelObject associate) throws AccessException,
        ConstraintException;

    /**
     * Find objects associated with this one in the role concerned, and also matching some criteria.
     * 
     * Note: this initial specification has equality tests only. OJB includes an API for more general tests,
     * matching the expressivity of SQL. We should consider providing this.
     * 
     * @param roleName name of role to search
     * @param criteria attribute name => value required or role name => model object required
     * @return list of all objects of the type, matching the criteria, that are readable by the current user
     */
    public abstract <T extends ModelObject> java.util.Collection<T> findAll(String roleName,
        java.util.Map criteria);

    /**
     * Add an association between an object of this type and another object.
     * <p>
     * If the end result of the adds and removes violates a constraint for the association, then the commit
     * method will throw a ConstraintException.
     * </p>
     * 
     * @param roleName of the role the added object is to play
     * @param other object to add
     * @return true if the association was modified
     * @throws AccessException if the current user may not modify either the other object or this one
     * @throws ConstraintException
     */
    public abstract boolean add(String roleName, ModelObject other) throws ConstraintException;

    /**
     * Adds associations between a model object of this type and some objects of the other type.
     * 
     * @precondition the objects belong to the same version
     * @param roleName of the role the added objects are to play
     * @param others list of model objects of other type
     * @throws AccessException if one or more of the objects could not be added. In this case all the objects
     *             that can be added have been. The caller must call version.abort() if this is not the
     *             behaviour required.
     * @return true if the association was changed, i.e. if a and some bs were not associated before
     * @throws ConstraintException
     */
    abstract boolean add(String roleName, java.util.Collection<? extends ModelObject> others)
        throws ConstraintException;

    /**
     * Remove an object in an association with an object of this type.
     * 
     * If the end result of the adds and removes violates a constraint for the association, then the commit
     * method will throw a ConstraintException.
     * 
     * @param roleName of the role the object to remove played
     * @param other object to remove
     * @return true if the association was modified
     * @throws AccessException if the current user may not modify either the other object or this one
     * @throws ConstraintException if this operation violates a data model constraint
     */
    public abstract boolean remove(String roleName, ModelObject other) throws ConstraintException;

    /**
     * Removes associations between a model object of this type and some objects of the other type.
     * 
     * @precondition the objects belong to the same version
     * @param roleName of the role the objects to remove played
     * @param others list of model objects of other type
     * @throws AccessException if one or more of the objects could not be removed. In this case all the
     *             objects that can be removed have been. The caller must call version.abort() if this is not
     *             the behaviour required.
     * @return true if the association was changed, i.e. if a and some bs were associated before
     * @throws ConstraintException
     */
    abstract boolean remove(String roleName, java.util.Collection<ModelObject> others)
        throws ConstraintException;

    /**
     * @return a collection of the uploaded files associated with this object.
     */
    public java.util.Collection<File> get_Files();

    /**
     * @return true if the current user could update this object
     */
    public boolean get_MayUpdate();

    /**
     * @return true if the current user could delete this object
     */
    public boolean get_MayDelete();

    /**
     * Remove the database record which this object represents
     */
    public void delete() throws AccessException, ConstraintException;

}
