/*
 * Created on 26-Jul-2004 @author: Chris Morris
 */
package org.pimslims.exception;

/**
 * Checked exception that acts as the root of the heirarchy of exceptions for the data model.
 * 
 * The choice between checked and unchecked exceptions will be made as described in Chapter 8 of "Effective
 * Java" by Joshua Bloch.
 * 
 * @version 0.1
 */
public class ModelException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public ModelException() {
        super();
    }

    /**
     * @param message
     */
    public ModelException(final String message) {
        super(message);
    }

    /**
     * @param message
     * @param exception that is being wrapped
     */
    public ModelException(final String message, final Throwable exception) {
        super(message, exception);
    }
}
