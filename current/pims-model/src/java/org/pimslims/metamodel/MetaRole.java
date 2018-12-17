/*
 * Created on 23-Jul-2004 @author: Chris Morris
 */
package org.pimslims.metamodel;

import java.util.Collection;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;

/**
 * Represents an association between two model types, proving metadata and implementation from the point of
 * view of one of them. Each association between model types has two roles.
 * 
 * This is a delegate for ModelObject. Users of the API will call it via generated methods of a subclass of
 * ModelObject.
 * 
 * @version 0.1
 */
public interface MetaRole extends MetaProperty {

    /**
     * 
     * @return model type of the objects that have this role
     */
    MetaClass getOwnMetaClass();

    /**
     * @return Returns the name to be used for this role in the UI.
     */
    String getAlias();

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.MetaModelElement#getName()
     */
    String getName();

    String getRoleName();

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.MetaModelElement#isHidden()
     */
    boolean isHidden();

    /**
     * 
     * @return type of objects this role associates with
     */
    MetaClass getOtherMetaClass();

    /**
     * 
     * @return minimum number of associated objects
     */
    int getLow();

    /**
     * 
     * @return maximum number of associated objects (-1 for any number)
     */
    int getHigh();

    /**
     * Returns a reverse view of the association. It will return null if the assocation is only navigable from
     * this end.
     * 
     * @return Role object representing the association from the other point of view
     */
    MetaRole getOtherRole();

    /**
     * @return true if the role preserves the order of associates
     */
    boolean isOrdered();

    /**
     * 
     * @return true if the role is one way
     */
    boolean isOneWay();

    /**
     * @return true if this role can be changed after creation
     */
    boolean isChangeable();

    /**
     * Report whether two objects are associated
     * 
     * @precondition the objects belong to the same version
     * @param a object of current type
     * @param b object of other type
     * @return true if they are currently associated
     */
    boolean areAssociated(final ModelObject a, final ModelObject b);

    /**
     * Adds an association between two model objects.
     * <p>
     * If the end result of the adds and removes violates a constraint for the association, then the commit
     * method will throw a ConstraintException.
     * </p>
     * 
     * @precondition the objects belong to the same version
     * @param a model object of current type
     * @param b model object of other type
     * @return true if the association was changed, i.e. if a and be were not associated before
     * @throws AccessException if object a is not updatable by the current user
     * @throws ConstraintException
     */
    boolean add(final ModelObject a, final ModelObject b) throws ConstraintException;

    /**
     * Adds associations between a model object of this type and some objects of the other type.
     * 
     * Implementing classes may override this with a more efficient implementation.
     * 
     * @precondition the objects belong to the same version
     * @param a model object of current type
     * @param bs list of model objects of other type
     * @throws AccessException if object a is not updatable by the current user
     * @return true if the association was changed, i.e. if a and some bs were not associated before
     * @throws ConstraintException
     */
    boolean add(final ModelObject a, final java.util.Collection<? extends ModelObject> bs) throws ConstraintException;

    /**
     * Removes an association between two model objects
     * 
     * If the end result of the adds and removes violates a constraint for the association, then the commit
     * method will throw a ConstraintException.
     * 
     * @precondition the objects belong to the same version
     * @param a model object of current type
     * @param b model object of other type
     * @return true if the association was changed, i.e. if a and b were associated before
     * @throws AccessException if object a is not updatable by the current user
     * @throws ConstraintException if this operation violates a data model constraint
     */
    boolean remove(final ModelObject a, final ModelObject b) throws ConstraintException;

    /**
     * Removes associations between a model object of this type and some objects of the other type.
     * 
     * Implementing classes may override this with a more efficient implementation.
     * 
     * A ConstraintException will be thrown on commit, if the association does not conform to the high and low
     * limits.
     * 
     * @precondition the objects belong to the same version
     * @param a model object of current type
     * @param bs list of model objects of other type
     * @throws AccessException if object a is not updatable by the current user
     * @return true if the association was changed, i.e. if a and some bs were associated before
     * @throws ConstraintException
     */
    boolean remove(final ModelObject a, final java.util.Collection<ModelObject> bs)
        throws ConstraintException;

    /**
     * Find objects associated with the supplied one in the role concerned.
     * 
     * The collection returned is a Set if the this role is unordered, or a List if it is ordered.
     * 
     * @param a object to return the associated off
     * @return a list of the associated objects
     */
    <T extends ModelObject> Collection<T> get(final ModelObject a);

    /**
     * Find objects associated with the supplied one in the role concerned, and also matching some criteria.
     * 
     * Note: this initial specification has equality tests only. OJB includes an API for more general tests,
     * matching the expressivity of SQL. We should consider providing this.
     * 
     * @param a object to return the associated off
     * @param criteria attribute name => value required, and role name => model instance required
     * @return list of all objects of the type, matching the criteria, that are readable by the current user
     * @deprecated use findAll(final ModelObject a, final java.util.Map<String, Object> criteria,int start,
     *             int limit) If you are sure that the number of results is acceptable, you can hard code 0,
     *             100. Otherwise you should page the results.
     */
    @Deprecated
    <T extends ModelObject> java.util.Collection<T> findAll(final ModelObject a,
        final java.util.Map<String, Object> criteria);

    /**
     * Find objects associated with the supplied one in the role concerned, and also matching some criteria.
     * 
     * Note: this initial specification has equality tests only. OJB includes an API for more general tests,
     * matching the expressivity of SQL. We should consider providing this.
     * 
     * @param a object to return the associated off
     * @param criteria attribute name => value required, and role name => model instance required
     * @param start index in list of first object to return
     * @param limit maximum number of objects to return
     * @return list of all objects of the type, matching the criteria, that are readable by the current user
     */
    <T extends ModelObject> java.util.Collection<T> findAll(final ModelObject a,
        final java.util.Map<String, Object> criteria, int start, int limit);

    /**
     * @param object
     * @param attributeName
     * @param attributeValue
     * @return modelobject which attributeName match attributeValue
     * @deprecated use findAll(final ModelObject object, final String attributeName, final ModelObject
     *             attributeValue,int start, int limit) If you are sure that the number of results is
     *             acceptable, you can hard code 0, 100. Otherwise you should page the results.
     */
    @Deprecated
    <T extends ModelObject> Collection<T> findAll(final ModelObject object, final String attributeName,
        final ModelObject attributeValue);

    /**
     * @param object
     * @param attributeName
     * @param attributeValue
     * @param start index in list of first object to return
     * @param limit maximum number of objects to return
     * @return modelobject which attributeName match attributeValue
     */
    <T extends ModelObject> Collection<T> findAll(final ModelObject object, final String attributeName,
        final ModelObject attributeValue, int start, int limit);

    /**
     * Checks that the low and high values are correct for all associated objects. Commit will throw a
     * ConstraintException unless this is true.
     * 
     * @param version the version of the repository to be checked - this is most likely to actually be a
     *            WritableVersion
     * @return true if the assocation is valid
     */
    boolean verify(final ReadableVersion version);

    /**
     * @return a description of meaning of the association
     */
    String getHelpText();

}
