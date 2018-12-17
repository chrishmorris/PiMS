/*
 * Created on 26-Jul-2004 @author: Chris Morris
 */
package org.pimslims.exception;

import org.pimslims.constraint.Constraint;
import org.pimslims.metamodel.ModelObject;

/**
 * Indicates that a data model value violates a constraint.
 * 
 * @see Constraint
 * @version 0.1
 */
public class ConstraintException extends ModelException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * The object whose attribute is being considered.
     */
    public ModelObject object;

    /**
     * The name of the attribute to chack
     */
    public final String attributeName;

    /**
     * Comment for <code>value</code>
     * 
     * The desired value for the attribute
     */
    public final Object value;

    /**
     * @param object in model that was being checked
     * @param attributeName of field concerned
     * @param value desired
     * @param message text explaining the problem
     */
    public ConstraintException(final ModelObject object, final String attributeName, final Object value,
        final String message, final Throwable cause) {
        super(object + "'s " + attributeName + " - should not be " + value + ": " + message, cause);
        this.object = object;
        this.attributeName = attributeName;
        this.value = value;
    }

    /**
     * @param object in model that was being checked
     * @param attributeName of field concerned
     * @param value desired
     * @param message text explaining the problem
     */
    public ConstraintException(final ModelObject object, final String attributeName, final Object value,
        final String message) {
        this(object, attributeName, value, message, null);
    }

    /**
     * @param object in model that was being checked
     * @param attributeName of field concerned
     * @param value desired
     */
    public ConstraintException(final ModelObject object, final String attributeName, final Object value) {
        this(object, attributeName, value, "Data model constraint violated", null);
    }

    /**
     * @param message
     */
    public ConstraintException(String message) {
        super(message);
        this.object = null;
        this.attributeName = null;
        this.value = null;
    }

    public ConstraintException(String message, Throwable cause) {
        super(message, cause);
        this.object = null;
        this.attributeName = null;
        this.value = null;
    }

    /**
     * @return Returns the attributeName.
     */
    public String getAttributeName() {
        return attributeName;
    }
}
