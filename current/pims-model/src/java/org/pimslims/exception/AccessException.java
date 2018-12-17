/*
 * Created on 26-Jul-2004 @author: Chris Morris
 */
package org.pimslims.exception;

import org.pimslims.metamodel.ModelObject;

/**
 * Reports an attempt to create, read, update, or delete model objects in violation of the current access
 * control rules.
 * 
 * 
 * @version 0.1
 */
public class AccessException extends ModelException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * the model object that the operation was trying to access (read, update, or delete), null it it was a
     * create operation.
     */
    public final ModelObject object;

    /**
     * @param object that the attempted access was to
     */
    public AccessException(final ModelObject object) {
        this("", object);
    }

    /**
     * @param message
     */
    public AccessException(final String message) {
        this(message, null);
    }

    /**
     * @param message
     * @param object
     */
    public AccessException(String message, ModelObject object) {
        super(message);
        this.object = object;
    }

}
