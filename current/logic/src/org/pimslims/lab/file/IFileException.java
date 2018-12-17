/*
 * BusinessException.java Created on 27 June 2007, 11:59 To change this template, choose Tools | Template
 * Manager and open the template in the editor.
 */

package org.pimslims.lab.file;

/**
 * 
 * @author Marc Savitsky
 */
public class IFileException extends java.lang.Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5097806470509406260L;

    /**
     * Constructs an instance of <code>BusinessException</code> with the specified detail message and cause.
     * 
     * @param msg the detail message.
     * @param cause the cause of the exception
     */
    public IFileException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an instance of <code>BusinessException</code> with the cause.
     * 
     * @param cause the cause of the exception
     */
    public IFileException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an instance of <code>BusinessException</code> with the specified detail message.
     * 
     * @param msg the detail message.
     */
    public IFileException(final String msg) {
        super(msg);
    }
}
