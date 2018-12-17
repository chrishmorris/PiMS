package org.pimslims.lab;

import org.pimslims.exception.ConstraintException;

/**
 * @author Petr Troshin aka pvt43
 * 
 * CustomConstraintException
 * 
 */
public class CustomConstraintException extends ConstraintException {

    public String attributeName;

    public String value;

    public String metaClassName;

    public CustomConstraintException(final String message, final String attributeName, final String value,
        final String metaClassName) {
        super(message);
        this.attributeName = attributeName;
        this.value = value;
        this.metaClassName = metaClassName;
    }

}
