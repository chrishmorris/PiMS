/*
 * Created on 06-Aug-2004 @author: Chris Morris
 */
package org.pimslims.metamodel;

import org.pimslims.constraint.Constraint;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;

/**
 * Represents an attribute of a model type.
 * 
 * Provides metadata about the attribute and methods for manipulating it.
 * 
 * @see MetaRole
 * 
 * @version 0.1
 */
public interface MetaAttribute extends MetaProperty {

    /**
     * @return the model type the attribute belongs to.
     */
    MetaClass getMetaClass();

    /**
     * @return the name of the attribute.
     */
    String getName();

    /**
     * @return the java.lang.Class for values of the type.
     */
    Class<?> getType();

    /**
     * @return default value if any, or null
     */
    Object getDefaultValue();

    /**
     * The maximum allowed length for string values. Set to zero if there is no maximum.
     * 
     * @return length of value
     */
    public int getLength();

    /**
     * Indicates whether this attribute is mutable after creation. The value returned indicates that semantics
     * of the model, not the access rights of the current user. The generated API only contains set methods
     * for changeable attributes.
     * 
     * @return true if this attribute can be modified
     */
    boolean isChangeable();

    /**
     * @return true if the attribute has a "not null" constraint
     */
    boolean isRequired();

    /**
     * Gets the validity rule for values of the attribute.
     * 
     * This feature allows the use of generic classes, e.g. the javaClass can be java.lang.String and the
     * constraint can be Constraint.Length(32).
     * 
     * @see Constraint.Length
     * 
     * @return an implementation of the validity rule for the values.
     */
    Constraint getConstraint();

    /**
     * Checks the constraint
     * 
     * @param value to check
     * @return true if the value is permitted
     */
    boolean isValid(Object value);

    /**
     * gets the value of the attribute
     * 
     * @precondition the object is of the correct type
     * @param object the instance the attribute belongs to
     * @return the value of the attribute
     */
    Object get(ModelObject object);

    /**
     * sets the value of the attribute.
     * 
     * @precondition the object is of the correct type
     * @precondition the value is an instance of the correct class
     * @param object the instance to update
     * @param value the new value of the attribute
     * @throws AccessException if the object is not writable
     * @throws ConstraintException if the value is not legal
     */
    void set(ModelObject object, Object value) throws AccessException, ConstraintException;

    /**
     * @return a description of the meaning of the attribute
     */
    String getHelpText();

    /**
     * @return true if there is a uniqueness constraint for this attribute
     */
    boolean isUnique();

    /**
     * Indicates whether this attribute is derived. These attributes cannot be set even at creation time.
     * 
     * @return true if this attributed is calculated rather than set
     */
    boolean isDerived();

    /**
     * MetaAttribute.isMulti e.g. addresses, synonyms
     * 
     * @return
     */
    boolean isMulti();
}
